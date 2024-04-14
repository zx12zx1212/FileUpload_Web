package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

}
