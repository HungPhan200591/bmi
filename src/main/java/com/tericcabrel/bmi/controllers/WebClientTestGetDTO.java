package com.tericcabrel.bmi.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author HUNGPC1
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class WebClientTestGetDTO {

//    private String field;
//    private String header;
//    private String input_type;
//    private String column_pkey;
//    private String column_dictionary_id;
//    private String column_hsize;
//    private String column_lsize;

    private String field;
    private String header;

    @JsonProperty("input_type")
    private String inputType;

    @JsonProperty("column_pkey")
    private String columnPkey;

    @JsonProperty("column_dictionary_id")
    private String columnDictionaryId;

    @JsonProperty("column_hsize")
    private String columnHsize;

    @JsonProperty("column_lsize")
    private String columnLsize;


}
