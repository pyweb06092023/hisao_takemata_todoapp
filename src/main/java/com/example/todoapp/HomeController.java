package com.example.todoapp;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class HomeController {
    private static final int PAGE_SIZE = 10;
    private final TodoService todoService;

    public HomeController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "\u3084\u308b\u3053\u3068\u7ba1\u7406");
        return "index";
    }

    @GetMapping("/todos")
    public String todos(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "\u3059\u3079\u3066") String category,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "false") boolean showCompleted,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        String sortOrder = "desc".equals(order) ? "desc" : "asc";
        int currentPage = Math.max(page, 1);
        int totalCount = todoService.countForList(keyword, category, showCompleted);
        int totalPages = Math.max((totalCount + PAGE_SIZE - 1) / PAGE_SIZE, 1);
        currentPage = Math.min(currentPage, totalPages);
        List<Todo> todos = todoService.searchForList(keyword, category, sortOrder, showCompleted,
                PAGE_SIZE, (currentPage - 1) * PAGE_SIZE);
        model.addAttribute("todos", todos);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("order", sortOrder);
        model.addAttribute("showCompleted", showCompleted);
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "todos";
    }

    @GetMapping("/todos/new")
    public String createForm(Model model) {
        model.addAttribute("todo", new Todo());
        return "create";
    }

    @PostMapping("/todos/confirm")
    public String createConfirm(@Valid @ModelAttribute Todo todo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "create";
        }
        model.addAttribute("todo", todo);
        return "create-confirm";
    }

    @PostMapping("/todos/new")
    public String createFormBack(@ModelAttribute Todo todo, Model model) {
        model.addAttribute("todo", todo);
        return "create";
    }

    @PostMapping("/todos")
    public String create(@ModelAttribute Todo todo, RedirectAttributes redirectAttributes) {
        todoService.create(todo);
        redirectAttributes.addFlashAttribute("message", "\u767b\u9332\u3057\u307e\u3057\u305f");
        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Todo todo = todoService.findById(id);
        if (todo == null) {
            redirectAttributes.addFlashAttribute("message", "\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f");
            return "redirect:/todos";
        }
        model.addAttribute("todo", todo);
        model.addAttribute("id", id);
        return "edit";
    }

    @GetMapping("/todos/{id}/delete")
    public String deleteForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Todo todo = todoService.findById(id);
        if (todo == null) {
            redirectAttributes.addFlashAttribute("message", "\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f");
            return "redirect:/todos";
        }
        model.addAttribute("todo", todo);
        model.addAttribute("id", id);
        return "delete";
    }

    @PostMapping("/todos/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        todoService.delete(id);
        redirectAttributes.addFlashAttribute("message", "\u524a\u9664\u3057\u307e\u3057\u305f");
        return "redirect:/todos";
    }

    @PostMapping("/todos/{id}/confirm")
    public String editConfirm(@PathVariable Long id, @Valid @ModelAttribute Todo todo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            return "edit";
        }
        model.addAttribute("todo", todo);
        model.addAttribute("id", id);
        return "edit-confirm";
    }

    @PostMapping("/todos/{id}/edit")
    public String editFormBack(@PathVariable Long id, @ModelAttribute Todo todo, Model model) {
        model.addAttribute("todo", todo);
        model.addAttribute("id", id);
        return "edit";
    }

    @PostMapping("/todos/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Todo todo, RedirectAttributes redirectAttributes) {
        todo.setId(id);
        todoService.update(todo);
        redirectAttributes.addFlashAttribute("message", "\u4fdd\u5b58\u3057\u307e\u3057\u305f");
        return "redirect:/todos";
    }
}
