package edu.khiemnhg.SB_TruyenDuLieuSangView.Controllers;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import edu.khiemnhg.SB_TruyenDuLieuSangView.Model.SinhVien;

@Controller
public class TruyenDuLieuSangViewController {
	@GetMapping("/")
	public String Truyen(Model m){
		m.addAttribute("mssv", "64131000");
		m.addAttribute("hoTen", "Nguyễn Hoàng Gia Khiêm");
		m.addAttribute("namSinh", "2004");
		m.addAttribute("gioiTinh", "Nam");
		return "index";
	}
	@GetMapping("/truyenObject")
	public String sinhVien(ModelMap m) {
		SinhVien sv = new SinhVien("64131000", "Khiêm", 21);
		m.addAttribute("sv", sv);
		return "sinhvien";
	}
	@GetMapping("/truyendsObject")
	public String danhSachSinhVien(ModelMap m) {
		ArrayList<SinhVien> dsSinhVien = new ArrayList<>();
		dsSinhVien.add(new SinhVien("64131001", "Quân", 21));
		dsSinhVien.add(new SinhVien("64131002", "Triết", 21));
		m.addAttribute("dssv", dsSinhVien);
		return "dssv";
	}
}
