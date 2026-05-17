package com.example.kur.interfaces.web;

import com.example.kur.application.service.CourseService;
import com.example.kur.application.service.EnrollmentService;
import com.example.kur.application.service.StudentService;
import com.example.kur.interfaces.web.form.EnrollmentForm;
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
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentController(EnrollmentService enrollmentService, StudentService studentService, CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("enrollments", enrollmentService.list());
        model.addAttribute("students", studentService.list());
        model.addAttribute("courses", courseService.list());
        model.addAttribute("form", new EnrollmentForm());
        return "enrollments/list";
    }

    @PostMapping
    public String enroll(@ModelAttribute("form") @Valid EnrollmentForm form, BindingResult binding, RedirectAttributes ra, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("enrollments", enrollmentService.list());
            model.addAttribute("students", studentService.list());
            model.addAttribute("courses", courseService.list());
            return "enrollments/list";
        }
        enrollmentService.enroll(form.getStudentId(), form.getCourseId());
        ra.addFlashAttribute("success", "Student enrolled");
        return "redirect:/enrollments";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id, RedirectAttributes ra) {
        enrollmentService.delete(id);
        ra.addFlashAttribute("success", "Enrollment deleted");
        return "redirect:/enrollments";
    }
}
