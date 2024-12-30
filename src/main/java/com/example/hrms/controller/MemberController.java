// src/main/java/com/example/hrms/controller/MemberController.java
package com.example.hrms.controller;

import com.example.hrms.model.User;
import com.example.hrms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private UserService userService;

    /**
     * 구성원 목록 페이지
     * ADMIN과 USER 모두 접근 가능
     */
    @GetMapping
    public String listMembers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> membersPage;

        if (search != null && !search.trim().isEmpty()) {
            membersPage = userService.searchUsers(search.trim(), pageable);
        } else {
            membersPage = userService.getAllUsers(pageable);
        }

        // 각 User 객체에 부서 이름을 결합한 문자열을 추가
        membersPage.getContent().forEach(member -> {
            String departments = member.getDepartments().stream()
                                   .map(dept -> dept.getName())
                                   .collect(Collectors.joining(", "));
            member.setDepartmentNames(departments);
        });

        model.addAttribute("membersPage", membersPage);
        model.addAttribute("search", search);
        return "members/list"; // templates/members/list.html
    }

    /**
     * 구성원 상세보기 페이지
     * ADMIN과 USER 모두 접근 가능
     */
    @GetMapping("/{id}")
    public String viewMember(@PathVariable Long id, Model model) {
        User member = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + id));
        model.addAttribute("member", member);
        return "members/view"; // templates/members/view.html
    }

    /**
     * 구성원 수정 폼 (ADMIN 전용)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User member = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + id));
        model.addAttribute("member", member);
        model.addAttribute("departments", userService.getAllDepartments()); // 부서 목록 추가
        return "members/edit"; // templates/members/edit.html
    }

    /**
     * 구성원 수정 처리 (ADMIN 전용)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/update")
    public String updateMember(@PathVariable Long id, @ModelAttribute("member") User member) {
        member.setId(id);
        userService.updateUser(member); // updateUser 메서드를 UserService에 추가해야 함
        return "redirect:/members?updateSuccess";
    }

    /**
     * 구성원 삭제 처리 (ADMIN 전용)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/delete")
    public String deleteMember(@PathVariable Long id) {
        userService.deleteUser(id); // deleteUser 메서드를 UserService에 추가해야 함
        return "redirect:/members?deleteSuccess";
    }
}
