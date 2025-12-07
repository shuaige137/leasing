package com.example.leasing_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    UserRepository repo;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserEntity user) {

        if (repo.findByUsername(user.getUsername()) != null) {
            return "redirect:/register?error";
        }

        user.setPassword(encoder.encode(user.getPassword()));

        // Первая регистрация = ADMIN
        if (repo.count() == 0) user.setRole("ADMIN");
        else user.setRole("USER");

        repo.save(user);
        return "redirect:/login?registered";
    }
}