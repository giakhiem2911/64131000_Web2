package edu.khiemnhg.ToChucBoCuc.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/")
	public String trangChu() {
		return "frontEndViews/index";
	}
}
