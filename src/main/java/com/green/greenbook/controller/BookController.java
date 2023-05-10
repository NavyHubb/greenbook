package com.green.greenbook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greenbook.domain.form.SearchForm;
import com.green.greenbook.domain.vo.BookVO;
import com.green.greenbook.domain.vo.NaverResultVO;
import java.net.URI;
import java.util.List;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class BookController {

    @GetMapping("/book")
    public List<BookVO> search(@RequestBody SearchForm form) {

        String clientId = "o2E_K7nOKatROhYYz1RA";
        String clientSecret = "RnDZr28WGN";

        URI uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com")
            .path("/v1/search/book.json")
            .queryParam("query", form.getTitle())
            .queryParam("display", 10)
            .queryParam("start", 1)
            .queryParam("sort", "sim")
            .encode()
            .build()
            .toUri();

        RequestEntity<Void> req = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", clientId)
            .header("X-Naver-Client-Secret", clientSecret)
            .build();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        ObjectMapper om = new ObjectMapper();
        NaverResultVO resultVO = null;

        try {
            resultVO = om.readValue(resp.getBody(), NaverResultVO.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<BookVO> books = resultVO.getItems();

        return books;
    }

}