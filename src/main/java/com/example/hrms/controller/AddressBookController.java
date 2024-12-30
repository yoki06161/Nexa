package com.example.hrms.controller;

import com.example.hrms.model.User;
import com.example.hrms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AddressBookController {
    
    private final UserService userService;

    @Autowired
    public AddressBookController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/address-book")
    public String showAddressBook(Model model, Authentication authentication) {
        // email 변수를 effectively final로 유지하기 위해 조건부 할당을 한 번만 수행
        final String email = (authentication != null && authentication.getPrincipal() instanceof UserDetails) ?
                             ((UserDetails) authentication.getPrincipal()).getUsername() : "";
        
        // 이메일이 비어있을 경우 예외 처리 또는 다른 로직 추가 가능
        if (email.isEmpty()) {
            throw new IllegalArgumentException("User is not authenticated or email is missing.");
        }
        
        User currentUser = userService.findByEmail(email)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid user email: " + email));
        
        List<User> addressBook = userService.getAddressBookForUser(currentUser);
        model.addAttribute("addressBook", addressBook);
        model.addAttribute("currentUser", currentUser);
        return "address-book"; // templates/address-book.html
    }
}