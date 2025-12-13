package com.example.leasing_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMING') or hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Список всех пользователей
    @GetMapping("/")
    public String listUsers(Model model, Authentication authentication) {
        List<UserEntity> users = userRepository.findAll();
        model.addAttribute("users", users);

        // Подсчет ролей
        long admingCount = users.stream().filter(u -> "ADMING".equals(u.getRole())).count();
        long adminCount = users.stream().filter(u -> "ADMIN".equals(u.getRole())).count();
        long userCount = users.stream().filter(u -> "USER".equals(u.getRole())).count();

        model.addAttribute("admingCount", admingCount);
        model.addAttribute("adminCount", adminCount);
        model.addAttribute("userCount", userCount);
        model.addAttribute("totalCount", users.size());

        // Текущий пользователь
        if (authentication != null) {
            model.addAttribute("currentUsername", authentication.getName());
            UserEntity currentUser = userRepository.findByUsername(authentication.getName());
            if (currentUser != null) {
                model.addAttribute("currentUserRole", currentUser.getRole());
            }
        }

        return "admin/users";
    }

    // Удаление пользователя
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        String currentUsername = authentication.getName();
        UserEntity currentUser = userRepository.findByUsername(currentUsername);
        UserEntity userToDelete = userRepository.findById(id).orElse(null);

        if (userToDelete == null) {
            redirectAttributes.addFlashAttribute("error", "Пользователь не найден");
            return "redirect:/admin/users/";
        }

        // Проверка прав
        String currentRole = currentUser.getRole();
        String targetRole = userToDelete.getRole();

        // ADMING может удалять всех, кроме себя
        if ("ADMING".equals(currentRole)) {
            if (userToDelete.getUsername().equals(currentUsername)) {
                redirectAttributes.addFlashAttribute("error", "Вы не можете удалить свою учётную запись");
            } else {
                userRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Пользователь успешно удалён");
            }
        }
        // ADMIN может удалять только USER'ов
        else if ("ADMIN".equals(currentRole)) {
            if (userToDelete.getUsername().equals(currentUsername)) {
                redirectAttributes.addFlashAttribute("error", "Вы не можете удалить свою учётную запись");
            } else if ("USER".equals(targetRole)) {
                userRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Пользователь успешно удалён");
            } else {
                redirectAttributes.addFlashAttribute("error", "Вы можете удалять только обычных пользователей (USER)");
            }
        }

        return "redirect:/admin/users/";
    }

    // Изменение роли пользователя
    @PostMapping("/update-role/{id}")
    public String updateUserRole(@PathVariable Long id,
                                 @RequestParam String newRole,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        String currentUsername = authentication.getName();
        UserEntity currentUser = userRepository.findByUsername(currentUsername);
        UserEntity user = userRepository.findById(id).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Пользователь не найден");
            return "redirect:/admin/users/";
        }

        // Проверка прав
        String currentRole = currentUser.getRole();

        // ADMING может назначать ADMIN и USER
        if ("ADMING".equals(currentRole)) {
            if (user.getUsername().equals(currentUsername)) {
                redirectAttributes.addFlashAttribute("error", "Вы не можете изменить свою роль");
            } else if ("ADMING".equals(newRole)) {
                redirectAttributes.addFlashAttribute("error", "Может быть только один главный администратор (ADMING)");
            } else if ("ADMIN".equals(newRole) || "USER".equals(newRole)) {
                user.setRole(newRole);
                userRepository.save(user);
                redirectAttributes.addFlashAttribute("success", "Роль пользователя успешно изменена");
            }
        }
        // ADMIN может назначать только USER (снижать до USER)
        else if ("ADMIN".equals(currentRole)) {
            if (user.getUsername().equals(currentUsername)) {
                redirectAttributes.addFlashAttribute("error", "Вы не можете изменить свою роль");
            } else if ("USER".equals(user.getRole()) && "USER".equals(newRole)) {
                redirectAttributes.addFlashAttribute("info", "Роль пользователя уже установлена как USER");
            } else if ("ADMIN".equals(user.getRole()) && "USER".equals(newRole)) {
                user.setRole(newRole);
                userRepository.save(user);
                redirectAttributes.addFlashAttribute("success", "Роль пользователя изменена на USER");
            } else {
                redirectAttributes.addFlashAttribute("error", "Вы можете назначать только роль USER");
            }
        }

        return "redirect:/admin/users/";
    }
}