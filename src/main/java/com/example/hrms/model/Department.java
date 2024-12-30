package com.example.hrms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // 상위 부서 (계층 구조)
    @ManyToOne
    @JoinColumn(name = "parent_department_id")
    private Department parentDepartment;

    // 하위 부서
    @OneToMany(mappedBy = "parentDepartment", cascade = CascadeType.ALL)
    private Set<Department> subDepartments;

    // 사용자들과의 관계
    @ManyToMany(mappedBy = "departments")
    private Set<User> users;
}
