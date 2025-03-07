package edu.khiemnhg.ChaoSpringBoot.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class WebController {
	@GetMapping("/")
	public String index(ModelMap m) {
		m.addAttribute("tb", "Hello");
		return "index";
	}
}
