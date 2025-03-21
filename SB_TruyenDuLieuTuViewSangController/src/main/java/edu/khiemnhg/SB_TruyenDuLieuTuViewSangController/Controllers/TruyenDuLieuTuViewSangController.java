package edu.khiemnhg.SB_TruyenDuLieuTuViewSangController.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TruyenDuLieuTuViewSangController{
	@GetMapping("/v1")
	public String v1(@RequestParam(name="studentName", required = true, defaultValue = "") String studentName, ModelMap model) {
		model.addAttribute("studentName", studentName);
		return "v1";
	}
	@GetMapping("/v2")
	public String v2(@RequestParam(name="studentName", required = true, defaultValue = "") String studentName, ModelMap model) {
		model.addAttribute("studentName", studentName);
		return "v2";
	}
}