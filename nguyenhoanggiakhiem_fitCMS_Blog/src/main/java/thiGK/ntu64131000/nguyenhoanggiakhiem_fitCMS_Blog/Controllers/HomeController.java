package thiGK.ntu64131000.nguyenhoanggiakhiem_fitCMS_Blog.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import thiGK.ntu64131000.nguyenhoanggiakhiem_fitCMS_Blog.Models.Page;
import thiGK.ntu64131000.nguyenhoanggiakhiem_fitCMS_Blog.Models.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private List<Page> pageList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();
    private Long pageIdCounter = 1L;
    private Long postIdCounter = 1L;

    @GetMapping("/")
    public String homepage(ModelMap model) {
        model.addAttribute("pageList", pageList);
        return "frontEndViews/index";
    }

    @GetMapping("/dashboard")
    public String dashboard(ModelMap model) {
        model.addAttribute("pageList", pageList);
        return "frontEndViews/index";
    }

    // Page Operations
    @GetMapping("/page/list")
    public String pageList(ModelMap model) {
        model.addAttribute("pageList", pageList);
        return "frontEndViews/pageList";
    }

    @GetMapping("/page/addnew")
    public String addNewPage(ModelMap model) {
        model.addAttribute("page", new Page());
        return "frontEndViews/pageAddNew";
    }

    @PostMapping("/page/addnew")
    public String saveNewPage(@ModelAttribute("page") Page page) {
        page.setId(pageIdCounter++);
        pageList.add(page);
        return "redirect:/page/list";
    }

    @GetMapping("/page/view/{id}")
    public String viewPage(@PathVariable("id") Long id, ModelMap model) {
        Optional<Page> page = pageList.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (page.isPresent()) {
            model.addAttribute("page", page.get());
            return "frontEndViews/pageView";
        } else {
            return "redirect:/page/all";
        }
    }

    @GetMapping("/page/delete/{id}")
    public String deletePage(@PathVariable("id") Long id) {
        pageList.removeIf(page -> page.getId().equals(id));
        return "redirect:/page/list";
    }

    // Post Operations
    @GetMapping("/post/list")
    public String postList(ModelMap model) {
        model.addAttribute("postList", postList);
        return "frontEndViews/postList";
    }

    @GetMapping("/post/addnew")
    public String addNewPost(ModelMap model) {
        model.addAttribute("post", new Post());
        return "frontEndViews/postAddNew";
    }

    @PostMapping("/post/addnew")
    public String saveNewPost(@ModelAttribute("post") Post post) {
        post.setId(postIdCounter++);
        postList.add(post);
        return "redirect:/post/list";
    }

    @GetMapping("/post/view/{id}")
    public String viewPost(@PathVariable("id") Long id, ModelMap model) {
        Optional<Post> post = postList.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "frontEndViews/postView";
        } else {
            return "redirect:/post/list";
        }
    }

    @GetMapping("/post/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        postList.removeIf(post -> post.getId().equals(id));
        return "redirect:/post/list";
    }
}