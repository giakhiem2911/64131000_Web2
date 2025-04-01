package thiGK.ntu64131000.nguyenhoanggiakhiem_fitCMS_Blog.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/")
	public String homepage(ModelMap model) {
		return "frontEndViews/index";
	}
}
