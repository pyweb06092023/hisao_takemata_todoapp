package com.example.todoapp;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class HomeController {
    private final TodoMapper todoMapper;

    public HomeController(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "繧・ｋ縺薙→邂｡逅・);
        return "index";
    }

    @GetMapping("/todos")
    public String todos(Model model) {
        List<Todo> todos = todoMapper.findAll();
        model.addAttribute("todos", todos);
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
        todoMapper.insert(todo);
        redirectAttributes.addFlashAttribute("message", "逋ｻ骭ｲ縺励∪縺励◆");
        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Todo todo = todoMapper.findById(id);
        if (todo == null) {
            redirectAttributes.addFlashAttribute("message", "隕九▽縺九ｊ縺ｾ縺帙ｓ縺ｧ縺励◆");
            return "redirect:/todos";
        }
        model.addAttribute("todo", todo);
        model.addAttribute("id", id);
        return "edit";
    }

    @GetMapping("/todos/{id}/delete")
    public String deleteForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Todo todo = todoMapper.findById(id);
        if (todo == null) {
            redirectAttributes.addFlashAttribute("message", "隕九▽縺九ｊ縺ｾ縺帙ｓ縺ｧ縺励◆");
            return "redirect:/todos";
        }
        model.addAttribute("todo", todo);
        model.addAttribute("id", id);
        return "delete";
    }

    @PostMapping("/todos/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        todoMapper.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "蜑企勁縺励∪縺励◆");
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
        todoMapper.update(todo);
        redirectAttributes.addFlashAttribute("message", "譖ｴ譁ｰ縺励∪縺励◆");
        return "redirect:/todos";
    }
}
