package khiem.nhg.Project_64131000.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@GetMapping("/dashboard")
	public String adminDashboard(Model model) {
	    model.addAttribute("title", "Admin Dashboard");
	    model.addAttribute("content", "frontEndModel/admin/dashboard :: content");
	    return "frontEndModel/admin_layout";
	}
}