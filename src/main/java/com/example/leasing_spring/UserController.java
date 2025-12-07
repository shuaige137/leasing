package com.example.leasing_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Список всех пользователей
    @GetMapping("/")
    public String listUsers(Model model, Authentication authentication) {
        List<UserEntity> users = userRepository.findAll();
        model.addAttribute("users", users);

        // Подсчет в контроллере
        long adminCount = 0;
        long userCount = 0;

        for (UserEntity user : users) {
            if ("ADMIN".equals(user.getRole())) {
                adminCount++;
            } else if ("USER".equals(user.getRole())) {
                userCount++;
            }
        }

        model.addAttribute("adminCount", adminCount);
        model.addAttribute("userCount", userCount);
        model.addAttribute("totalCount", users.size());

        // Текущий пользователь
        if (authentication != null) {
            model.addAttribute("currentUsername", authentication.getName());
        }

        return "admin/users";
    }

    // Удаление пользователя (кроме самого себя)
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             Authentication authentication) {
        String currentUsername = authentication.getName();
        UserEntity userToDelete = userRepository.findById(id).orElse(null);

        if (userToDelete != null && !userToDelete.getUsername().equals(currentUsername)) {
            userRepository.deleteById(id);
        }

        return "redirect:/admin/users/";
    }

    // Изменение роли пользователя
    @PostMapping("/update-role/{id}")
    public String updateUserRole(@PathVariable Long id,
                                 @RequestParam String newRole,
                                 Authentication authentication) {
        String currentUsername = authentication.getName();
        UserEntity user = userRepository.findById(id).orElse(null);

        if (user != null && !user.getUsername().equals(currentUsername)) {
            if (newRole.equals("ADMIN") || newRole.equals("USER")) {
                user.setRole(newRole);
                userRepository.save(user);
            }
        }

        return "redirect:/admin/users/";
    }
}