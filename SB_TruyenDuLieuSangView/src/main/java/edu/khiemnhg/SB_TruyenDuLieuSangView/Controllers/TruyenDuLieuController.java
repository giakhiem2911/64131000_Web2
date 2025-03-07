package edu.khiemnhg.SB_TruyenDuLieuSangView.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TruyenDuLieuController {
	@GetMapping("/")
	public String Truyen(Model m){
		m.addAttribute("mssv", "64131000");
		m.addAttribute("hoTen", "Nguyễn Hoàng Gia Khiêm");
		m.addAttribute("namSinh", "2004");
		m.addAttribute("gioiTinh", "Nam");
		return "index";
	}
}
