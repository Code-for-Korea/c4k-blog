package kr.codefor.blog.editor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/")
    public String Index(Model model) {
        model.addAttribute("msg", "2021년 01월 30일");
        return "index";
    }
}
