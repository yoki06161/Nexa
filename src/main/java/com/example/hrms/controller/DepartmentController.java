// src/main/java/com/example/hrms/controller/DepartmentController.java
package com.example.hrms.controller;

import com.example.hrms.model.Department;
import com.example.hrms.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    /**
     * 부서 목록 페이지 - 상위 부서만 조회
     */
    @GetMapping
    public String listDepartments(Model model) {
        List<Department> departments = departmentService.getRootDepartments(); // 상위 부서만 조회
        model.addAttribute("departments", departments);
        return "departments/list"; // Thymeleaf 템플릿 경로
    }
    
    /**
     * 부서 생성 폼
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("allDepartments", departmentService.getAllDepartments());
        return "departments/create";
    }
    
    /**
     * 부서 생성 처리
     */
    @PostMapping
    public String createDepartment(@ModelAttribute Department department) {
        departmentService.saveDepartment(department);
        return "redirect:/departments";
    }
    
    /**
     * 부서 상세보기 페이지
     */
    @GetMapping("/{id}")
    public String viewDepartment(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        model.addAttribute("department", department);
        return "departments/view";
    }
    
    /**
     * 부서 수정 폼
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        model.addAttribute("department", department);
        model.addAttribute("allDepartments", departmentService.getAllDepartments());
        return "departments/edit";
    }
    
    /**
     * 부서 수정 처리
     */
    @PostMapping("/{id}/update")
    public String updateDepartment(@PathVariable Long id, @ModelAttribute Department department) {
        department.setId(id);
        departmentService.saveDepartment(department);
        return "redirect:/departments";
    }
    
    /**
     * 부서 삭제 처리
     */
    @GetMapping("/{id}/delete")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }
    
    /**
     * 하위 부서 목록 페이지
     */
    @GetMapping("/{id}/subdepartments")
    public String listSubDepartments(@PathVariable Long id, Model model) {
        Department parentDept = departmentService.getDepartmentById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        List<Department> subDepartments = departmentService.getSubDepartments(id);
        model.addAttribute("parentDepartment", parentDept);
        model.addAttribute("departments", subDepartments);
        return "departments/sublist"; // 새로운 Thymeleaf 템플릿 경로
    }
}
