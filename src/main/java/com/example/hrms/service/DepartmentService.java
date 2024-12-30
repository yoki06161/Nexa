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
    
    // 모든 부서 조회
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    // 부서 저장 (생성 및 수정)
    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }
    
    // ID로 부서 조회
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }
    
    // 부서 삭제
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
    
    // 부서명으로 부서 조회
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }
}