// src/main/java/com/example/hrms/service/DepartmentService.java
package com.example.hrms.service;

import com.example.hrms.model.Department;
import com.example.hrms.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 모든 부서 조회
     */
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    /**
     * 상위 부서가 없는 루트 부서 조회
     */
    public List<Department> getRootDepartments() {
        return departmentRepository.findByParentDepartmentIsNull();
    }

    /**
     * 특정 부서의 하위 부서들 조회
     */
    public List<Department> getSubDepartments(Long parentId) {
        return departmentRepository.findByParentDepartmentId(parentId);
    }

    /**
     * 부서 ID로 부서 조회
     */
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    /**
     * 부서 이름으로 부서 조회
     */
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }

    /**
     * 부서 저장
     */
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    /**
     * 부서 삭제
     */
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
