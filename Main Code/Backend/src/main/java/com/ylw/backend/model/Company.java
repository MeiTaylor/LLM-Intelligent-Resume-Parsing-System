package com.ylw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<JobPosition> jobPositions;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Resume> resumes;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> users;
}