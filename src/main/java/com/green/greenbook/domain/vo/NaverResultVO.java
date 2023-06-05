package com.green.greenbook.domain.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NaverResultVO {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<BookVO> items;
}