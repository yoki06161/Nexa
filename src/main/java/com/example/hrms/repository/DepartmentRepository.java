// src/main/java/com/example/hrms/repository/DepartmentRepository.java
package com.example.hrms.repository;

import com.example.hrms.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * 부서 이름으로 부서 조회
     */
    Optional<Department> findByName(String name);

    /**
     * 상위 부서가 없는 루트 부서 조회
     */
    List<Department> findByParentDepartmentIsNull();
    
    /**
     * 특정 상위 부서의 하위 부서들 조회
     */
    List<Department> findByParentDepartmentId(Long parentId);
}
