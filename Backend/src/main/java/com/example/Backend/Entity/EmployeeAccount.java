package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "employee_account")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAccount {

    @Id
    @Column(name = "account")
    private String account;

    @Column(name = "password")
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account")
    private Employee employee;
}
