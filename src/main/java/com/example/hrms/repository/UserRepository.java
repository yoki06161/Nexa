package com.example.hrms.repository;

import com.example.hrms.model.Department;
import com.example.hrms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    // 특정 부서에 속한 사용자들 조회
    List<User> findByDepartmentsIn(Set<Department> departments);
    
    // 검색 기능을 위한 커스텀 메서드
    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPositionContainingIgnoreCaseOrDepartments_NameContainingIgnoreCase(
        String name, String email, String position, String departmentName, Pageable pageable);
}
