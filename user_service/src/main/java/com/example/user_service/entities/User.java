package com.example.user_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users_table")
@Setter
@Getter
@ToString
public class User {
    @Id
    private Integer id;
    private String name;
    private String email;
    private String password;
}
