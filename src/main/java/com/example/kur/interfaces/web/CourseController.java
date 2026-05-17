package com.example.kur.interfaces.web;

import com.example.kur.application.service.CourseService;
import com.example.kur.interfaces.web.form.CourseForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("courses", courseService.list());
        return "courses/list";
    }

    @GetMapping("/new")
    public String newCourse(Model model) {
        model.addAttribute("form", new CourseForm());
        model.addAttribute("mode", "create");
        return "courses/form";
    }

    @PostMapping
    public String create(@ModelAttribute("form") @Valid CourseForm form, BindingResult binding, RedirectAttributes ra, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("mode", "create");
            return "courses/form";
        }
        UUID id = courseService.create(form.getTitle(), form.getDescription());
        ra.addFlashAttribute("success", "Course created");
        return "redirect:/courses/" + id + "/edit";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable UUID id, Model model) {
        var course = courseService.get(id);
        CourseForm form = new CourseForm();
        form.setTitle(course.title());
        form.setDescription(course.description());
        model.addAttribute("form", form);
        model.addAttribute("mode", "edit");
        model.addAttribute("id", id);
        return "courses/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable UUID id, @ModelAttribute("form") @Valid CourseForm form, BindingResult binding, RedirectAttributes ra, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("id", id);
            return "courses/form";
        }
        courseService.update(id, form.getTitle(), form.getDescription());
        ra.addFlashAttribute("success", "Course updated");
        return "redirect:/courses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id, RedirectAttributes ra) {
        courseService.delete(id);
        ra.addFlashAttribute("success", "Course deleted");
        return "redirect:/courses";
    }
}
