// UserController.java
package com.example.leasing_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public String listUsers(Model model) {
        List<UserEntity> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    // Просмотр конкретного пользователя
    @GetMapping("/view/{id}")
    public ModelAndView viewUser(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("admin/userView");
        UserEntity user = userRepository.findById(id).orElse(null);
        mav.addObject("user", user);
        return mav;
    }

    // Удаление пользователя (кроме самого себя)
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             @Autowired org.springframework.security.core.Authentication authentication) {
        // Проверяем, не удаляет ли администратор самого себя
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
                                 @RequestParam String newRole) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null && (newRole.equals("ADMIN") || newRole.equals("USER"))) {
            user.setRole(newRole);
            userRepository.save(user);
        }
        return "redirect:/admin/users/";
    }
}