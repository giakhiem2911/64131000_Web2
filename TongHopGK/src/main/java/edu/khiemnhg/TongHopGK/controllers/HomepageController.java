package edu.khiemnhg.TongHopGK.controllers;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import edu.khiemnhg.TongHopGK.models.SinhVien;

@Controller
public class HomepageController {
	@GetMapping("/")
	public String homepage(ModelMap model) {
		return "frontEndViews/index";
	}
	@GetMapping("/about")
	public String about(ModelMap model) {
		return "frontEndViews/about";
	}
	@GetMapping("/list")
	public String list(ModelMap model) {
		ArrayList<SinhVien> dssv = new ArrayList<SinhVien>();
		model.addAttribute("dssv", dssv);
		return "frontEndViews/studentList";
	}
	@GetMapping("/add")
	public String addNew(ModelMap model) {
		return "frontEndViews/addNew";
	}
}
