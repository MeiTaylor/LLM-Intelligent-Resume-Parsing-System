package com.ylw.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CharacteristicDTO {

    private String name;

    //@JsonProperty("分数")
    private int score;

    //@JsonProperty("原因")
    private String reason;

}
