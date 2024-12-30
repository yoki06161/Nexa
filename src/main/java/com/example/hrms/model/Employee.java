package com.example.hrms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String address;
    private String phone;
    private String position;
    private String department;
    private String hireDate;
    private Double salary;
    private String status; // 재직 중/휴직 중/퇴사 등
    
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
    
    private String imageUrl;
}