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

    @GetMapping("/dashboard")
    public String dashboard(ModelMap model) {
        return "frontEndViews/dashboard";
    }

    @GetMapping("/page/list")
    public String pageList(ModelMap model) {
        return "frontEndViews/pageList";
    }

    @GetMapping("/page/addnew")
    public String addNewPage(ModelMap model) {
        return "frontEndViews/pageAddNew";
    }

    @GetMapping("/post/list")
    public String postList(ModelMap model) {
        return "frontEndViews/postList";
    }

    @GetMapping("/post/addnew")
    public String addNewPost(ModelMap model) {
        return "frontEndViews/postAddNew";
    }
}
