package edu.khiemnhg.TongHopGK.controllers;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.khiemnhg.TongHopGK.models.SinhVien;

@Controller
public class HomepageController {
	ArrayList<SinhVien> dssv = new ArrayList<SinhVien>();
	@GetMapping("/")
	public String homepage(ModelMap model) {
		model.addAttribute("dssv", dssv);
		return "frontEndViews/index";
	}
	@GetMapping("/about")
	public String about(ModelMap model) {
		return "frontEndViews/about";
	}
	@GetMapping("/list")
	public String list(ModelMap model) {
		model.addAttribute("dssv", dssv);
		return "frontEndViews/studentList";
	}
	@GetMapping("/add")
	public String addNew(@RequestParam(name="id", required=false) String id,
			@RequestParam(name="hoTen", required=false) String hoTen,
			@RequestParam(name="diemTB", required=false) Float diemTB,
			ModelMap model) {
		if (id == null || hoTen == null || diemTB == 0.0) {
	        return "frontEndViews/addNew";
	    }
		SinhVien sinhVien = new SinhVien(id, hoTen, diemTB);
		dssv.add(sinhVien);
		return "redirect:/list";
	}
}
