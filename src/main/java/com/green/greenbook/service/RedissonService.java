package com.green.greenbook.service;

import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.property.Property;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedissonService {

    private final RedissonClient redissonClient;
    private final int EMPTY = 0;

    public String keyResolver(Property property, String keyId) {
        final String prefix = property.getPrefix()+":%s";
        return String.format(prefix, keyId);
    }

    public void setHeartCnt(String key, int amount) {
        redissonClient.getBucket(key).set(String.valueOf(amount));
    }

    public int getHeartCnt(String key) {
        return Integer.parseInt((String) redissonClient.getBucket(key).get());
    }

    public void updateHeartCnt(final String key, boolean isIncrease){
        final String lockName = key + ":lock";
        final RLock lock = redissonClient.getLock(lockName);

        try {
            if(!lock.tryLock(1, 3, TimeUnit.SECONDS))
                return;

            final int currentHeartCnt = getHeartCnt(key);

            if (isIncrease) {
                setHeartCnt(key, currentHeartCnt+1);
            } else {
                if (currentHeartCnt <= EMPTY) {
                    throw new CustomException(ErrorCode.EMPTY);
                }
                setHeartCnt(key, currentHeartCnt-1);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.TRANSACTION_LOCK);
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

}