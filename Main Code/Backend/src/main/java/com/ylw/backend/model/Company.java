package com.ylw.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    // 与 JobPosition 的一对多关系
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<JobPosition> jobPositions;

    // 与 Resume 的一对多关系
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Resume> resumes;

    // 与 User 的一对多关系
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> users;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JobPosition> getJobPositions() {
        return jobPositions;
    }

    public void setJobPositions(List<JobPosition> jobPositions) {
        this.jobPositions = jobPositions;
    }

    public List<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(List<Resume> resumes) {
        this.resumes = resumes;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
