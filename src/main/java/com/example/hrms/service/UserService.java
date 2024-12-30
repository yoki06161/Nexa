package com.example.hrms.service;

import com.example.hrms.model.Department;
import com.example.hrms.model.User;
import com.example.hrms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 이메일로 사용자 찾기
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 사용자 저장 (회원가입)
    public void saveUser(User user) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 기본 롤 설정
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        // 유저 저장
        userRepository.save(user);
    }
    
    // 모든 사용자 조회 메서드 (페이지네이션)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    // 사용자 ID로 조회
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    // 특정 부서의 사용자들 조회
    public List<User> findUsersByDepartments(Set<Department> departments) {
        return userRepository.findByDepartmentsIn(departments);
    }
    
    // 현재 사용자의 주소록에 포함된 팀원들 조회
    public List<User> getAddressBookForUser(User user) {
        Set<Department> departments = user.getDepartments();
        return findUsersByDepartments(departments)
                .stream()
                .filter(member -> !member.getId().equals(user.getId())) // 자신 제외
                .collect(Collectors.toList());
    }
    
    // 사용자 검색 기능 (이름, 이메일, 직책, 부서명)
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPositionContainingIgnoreCaseOrDepartments_NameContainingIgnoreCase(
            keyword, keyword, keyword, keyword, pageable);
    }

    // 사용자 업데이트
    public void updateUser(User user) {
        Optional<User> existingUserOpt = userRepository.findById(user.getId());
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPosition(user.getPosition());
            existingUser.setDepartments(user.getDepartments());
            existingUser.setRole(user.getRole());
            existingUser.setDualRole(user.isDualRole());
            existingUser.setStatus(user.getStatus());
            // 비밀번호는 변경 시 별도의 로직 필요
            userRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("Invalid user Id:" + user.getId());
        }
    }

    // 사용자 삭제
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 모든 부서 조회
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }
}