package com.ylw.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString(exclude = "company")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String account;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String role;

    @Column(name = "company_id", insertable = false, updatable = false)
    private int companyId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_id")
    @EqualsAndHashCode.Exclude  // 排除这个字段，避免递归
    private Company company;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserEmail> userEmails;
}