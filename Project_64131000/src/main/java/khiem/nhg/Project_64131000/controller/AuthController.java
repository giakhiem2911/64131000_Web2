package khiem.nhg.Project_64131000.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 
    
    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "Sai thông tin đăng nhập.");
        }
        if (logout != null) {
            model.addAttribute("message", "Bạn đã đăng xuất thành công.");
        }
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
        String encodedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công. Mời đăng nhập.");
        return "redirect:/login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
    	request.getSession().invalidate();
        return "redirect:/login?logout"; // Chuyển hướng đến trang đăng nhập với thông báo
    }
}