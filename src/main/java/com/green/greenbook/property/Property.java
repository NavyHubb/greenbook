package com.green.greenbook.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public abstract class Property {
    private String prefix;
}