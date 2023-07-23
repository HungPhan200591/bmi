package com.tericcabrel.bmi.controllers;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HUNGPC1
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
public class WebClientTestGetDTOList {

    private List<WebClientTestGetDTO> webClientTestGetDTOList;

    public WebClientTestGetDTOList() {
        webClientTestGetDTOList = new ArrayList<>();
    }
}
