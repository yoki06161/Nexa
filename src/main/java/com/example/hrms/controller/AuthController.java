package com.example.hrms.controller;

import com.example.hrms.model.Department;
import com.example.hrms.model.User;
import com.example.hrms.service.DepartmentService;
import com.example.hrms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Validated User user, 
                               @RequestParam("confirmPassword") String confirmPassword, 
                               @RequestParam(value = "departmentIds", required = false) Long[] departmentIds,
                               BindingResult result, Model model) {
        // 비밀번호 일치 확인
        if (!user.getPassword().equals(confirmPassword)) {
            result.rejectValue("password", "error.user", "비밀번호가 일치하지 않습니다.");
        }

        // 이메일 중복 확인
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", "error.user", "이미 사용 중인 이메일입니다.");
        }

        // 부서 선택 확인
        if (departmentIds == null || departmentIds.length == 0) {
            result.rejectValue("departments", "error.user", "적어도 하나의 부서를 선택해야 합니다.");
        }

        // 에러가 있으면 회원가입 페이지로 다시 이동
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "register";
        }

        // 부서 설정
        Set<Department> departments = new HashSet<>();
        for (Long deptId : departmentIds) {
            departmentService.getDepartmentById(deptId).ifPresent(departments::add);
        }
        user.setDepartments(departments);

        // DB 저장
        userService.saveUser(user);

        return "redirect:/login?registerSuccess";
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}