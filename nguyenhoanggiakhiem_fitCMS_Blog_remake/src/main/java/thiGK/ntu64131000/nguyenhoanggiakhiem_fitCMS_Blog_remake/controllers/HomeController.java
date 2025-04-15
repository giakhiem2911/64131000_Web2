package thiGK.ntu64131000.nguyenhoanggiakhiem_fitCMS_Blog_remake.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thiGK.ntu64131000.nguyenhoanggiakhiem_fitCMS_Blog_remake.models.Topic;
import thiGK.ntu64131000.nguyenhoanggiakhiem_fitCMS_Blog_remake.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private List<Topic> topicList = new ArrayList<>();
    private List<Student> studentList = new ArrayList<>();
    private Long topicIdCounter = 1L;
    private Long studentIdCounter = 1L;

    // Hard-code dữ liệu ban đầu
    public HomeController() {
        // Hard-code Topics
        topicList.add(new Topic("Topic 1", "Description 1", 101L, "Type A"));
        topicList.get(0).setId(topicIdCounter++);
        topicList.add(new Topic("Topic 2", "Description 2", 102L, "Type B"));
        topicList.get(1).setId(topicIdCounter++);
        topicList.add(new Topic("Topic 3", "Description 3", 103L, "Type A"));
        topicList.get(2).setId(topicIdCounter++);

        // Hard-code Students
        studentList.add(new Student("Student 1", 201L));
        studentList.get(0).setId(studentIdCounter++);
        studentList.add(new Student("Student 2", 202L));
        studentList.get(1).setId(studentIdCounter++);
        studentList.add(new Student("Student 3", 201L));
        studentList.get(2).setId(studentIdCounter++);
    }

    @GetMapping("/")
    public String homepage(ModelMap model) {
        return "frontEndViews/index";
    }

    @GetMapping("/dashboard")
    public String dashboard(ModelMap model) {
        return "frontEndViews/index";
    }

    // Topic Operations
    @GetMapping("/topic/all")
    public String topicList(ModelMap model) {
        model.addAttribute("topicList", topicList);
        return "frontEndViews/topicList";
    }

    @GetMapping("/topic/new")
    public String addNewTopic(ModelMap model) {
        model.addAttribute("topic", new Topic());
        return "frontEndViews/topicAddNew";
    }

    @PostMapping("/topic/new")
    public String saveNewTopic(@ModelAttribute("topic") Topic topic, RedirectAttributes redirectAttributes) {
        topic.setId(topicIdCounter++);
        topicList.add(topic);
        redirectAttributes.addFlashAttribute("message", "Thêm Topic thành công!");
        return "redirect:/topic/all";
    }

    @GetMapping("/topic/view/{id}")
    public String viewTopic(@PathVariable("id") Long id, ModelMap model) {
        Optional<Topic> topic = topicList.stream().filter(t -> t.getId().equals(id)).findFirst();
        if (topic.isPresent()) {
            model.addAttribute("topic", topic.get());
            return "frontEndViews/topicView";
        } else {
            return "redirect:/topic/all";
        }
    }

    @GetMapping("/topic/edit/{id}")
    public String editTopic(@PathVariable("id") Long id, ModelMap model) {
        Optional<Topic> topic = topicList.stream().filter(t -> t.getId().equals(id)).findFirst();
        if (topic.isPresent()) {
            model.addAttribute("topic", topic.get());
            return "frontEndViews/topicEdit";
        } else {
            return "redirect:/topic/all";
        }
    }

    @PostMapping("/topic/edit/{id}")
    public String updateTopic(@PathVariable("id") Long id, @ModelAttribute("topic") Topic updatedTopic, RedirectAttributes redirectAttributes) {
        Optional<Topic> topic = topicList.stream().filter(t -> t.getId().equals(id)).findFirst();
        if (topic.isPresent()) {
            Topic existingTopic = topic.get();
            existingTopic.setTopicName(updatedTopic.getTopicName());
            existingTopic.setTopicDescription(updatedTopic.getTopicDescription());
            existingTopic.setSupervisorId(updatedTopic.getSupervisorId());
            existingTopic.setTopicType(updatedTopic.getTopicType());
            redirectAttributes.addFlashAttribute("message", "Cập nhật Topic thành công!");
        }
        return "redirect:/topic/all";
    }

    @PostMapping("/topic/delete/{id}")
    public String deleteTopic(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        topicList.removeIf(topic -> topic.getId().equals(id));
        redirectAttributes.addFlashAttribute("message", "Xóa Topic thành công!");
        return "redirect:/topic/all";
    }

    // Student Operations
    @GetMapping("/student/all")
    public String studentList(ModelMap model) {
        model.addAttribute("studentList", studentList);
        return "frontEndViews/studentList";
    }

    @GetMapping("/student/new")
    public String addNewStudent(ModelMap model) {
        model.addAttribute("student", new Student());
        return "frontEndViews/studentAddNew";
    }

    @PostMapping("/student/new")
    public String saveNewStudent(@ModelAttribute("student") Student student, RedirectAttributes redirectAttributes) {
        student.setId(studentIdCounter++);
        studentList.add(student);
        redirectAttributes.addFlashAttribute("message", "Thêm Student thành công!");
        return "redirect:/student/all";
    }

    @GetMapping("/student/view/{id}")
    public String viewStudent(@PathVariable("id") Long id, ModelMap model) {
        Optional<Student> student = studentList.stream().filter(s -> s.getId().equals(id)).findFirst();
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "frontEndViews/studentView";
        } else {
            return "redirect:/student/all";
        }
    }

    @GetMapping("/student/edit/{id}")
    public String editStudent(@PathVariable("id") Long id, ModelMap model) {
        Optional<Student> student = studentList.stream().filter(s -> s.getId().equals(id)).findFirst();
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "frontEndViews/studentEdit";
        } else {
            return "redirect:/student/all";
        }
    }

    @PostMapping("/student/edit/{id}")
    public String updateStudent(@PathVariable("id") Long id, @ModelAttribute("student") Student updatedStudent, RedirectAttributes redirectAttributes) {
        Optional<Student> student = studentList.stream().filter(s -> s.getId().equals(id)).findFirst();
        if (student.isPresent()) {
            Student existingStudent = student.get();
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setGroupId(updatedStudent.getGroupId());
            redirectAttributes.addFlashAttribute("message", "Cập nhật Student thành công!");
        }
        return "redirect:/student/all";
    }

    @PostMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        studentList.removeIf(student -> student.getId().equals(id));
        redirectAttributes.addFlashAttribute("message", "Xóa Student thành công!");
        return "redirect:/student/all";
    }
}