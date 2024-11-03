package com.ylw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
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

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserEmail> userEmails;
}