package edu.khiemnhg.TongHopGK.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomepageController {
	@GetMapping("/")
	public String homepage() {
		return "index";
	}
}
