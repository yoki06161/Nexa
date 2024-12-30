package com.example.hrms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String position; // 직책 필드

    private String role; // 역할 필드 (ROLE_ADMIN, ROLE_USER)

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private boolean approved; // 승인 여부 필드

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_departments",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> departments;

    // 추가된 필드
    @Transient
    private String departmentNames;

    // 추가적인 필드 및 메서드...
}