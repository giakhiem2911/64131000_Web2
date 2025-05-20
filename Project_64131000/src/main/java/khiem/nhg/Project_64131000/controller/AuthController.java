package khiem.nhg.Project_64131000.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "frontEndModel/auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "frontEndModel/auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng.");
            return "redirect:/register";
        }

        user.setPasswordHash(user.getPasswordHash());
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công. Mời đăng nhập.");
        return "redirect:/login";
    }
    @GetMapping("/logout")
    public String logout() {
        // Phương thức này có thể để trống, vì Spring Security sẽ xử lý đăng xuất
        return "redirect:/login?logout"; // Chuyển hướng đến trang đăng nhập với thông báo
    }
}