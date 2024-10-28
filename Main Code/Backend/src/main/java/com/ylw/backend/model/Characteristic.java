package com.ylw.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Characteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonProperty("分数")
    private int score;

    @JsonProperty("原因")
    private String reason;

    private String catagory;
}