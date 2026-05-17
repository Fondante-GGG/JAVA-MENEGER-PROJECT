package com.example.kur.interfaces.web;

import com.example.kur.application.service.StudentService;
import com.example.kur.application.service.UserManagementService;
import com.example.kur.interfaces.web.form.StudentForm;
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
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final UserManagementService userManagementService;

    public StudentController(StudentService studentService, UserManagementService userManagementService) {
        this.studentService = studentService;
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentService.list());
        return "students/list";
    }

    @GetMapping("/new")
    public String newStudent(Model model) {
        model.addAttribute("form", new StudentForm());
        model.addAttribute("mode", "create");
        return "students/form";
    }

    @PostMapping
    public String create(@ModelAttribute("form") @Valid StudentForm form, BindingResult binding, RedirectAttributes ra, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("mode", "create");
            return "students/form";
        }
        UUID id = studentService.create(form.getFullName(), form.getEmail());
        ra.addFlashAttribute("success", "Student created");
        return "redirect:/students/" + id + "/edit";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable UUID id, Model model) {
        var student = studentService.get(id);
        StudentForm form = new StudentForm();
        form.setFullName(student.fullName());
        form.setEmail(student.email());
        model.addAttribute("form", form);
        model.addAttribute("mode", "edit");
        model.addAttribute("id", id);
        return "students/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable UUID id, @ModelAttribute("form") @Valid StudentForm form, BindingResult binding, RedirectAttributes ra, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("id", id);
            return "students/form";
        }
        studentService.update(id, form.getFullName(), form.getEmail());
        ra.addFlashAttribute("success", "Student updated");
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id, RedirectAttributes ra) {
        studentService.delete(id);
        ra.addFlashAttribute("success", "Student deleted");
        return "redirect:/students";
    }

    @PostMapping("/{id}/grant-access")
    public String grantAccess(@PathVariable UUID id, RedirectAttributes ra) {
        var generatedPassword = userManagementService.grantStudentAccess(id);
        if (generatedPassword.isPresent()) {
            ra.addFlashAttribute("success", "Access granted. Temporary password: " + generatedPassword.get());
        } else {
            ra.addFlashAttribute("success", "Access granted");
        }
        return "redirect:/students";
    }

    @PostMapping("/{id}/revoke-access")
    public String revokeAccess(@PathVariable UUID id, RedirectAttributes ra) {
        userManagementService.revokeStudentAccess(id);
        ra.addFlashAttribute("success", "Access revoked");
        return "redirect:/students";
    }
}
