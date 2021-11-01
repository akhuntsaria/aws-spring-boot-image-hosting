package com.github.akhuntsaria.imagehosting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    @GetMapping
    public String home() {
        return "home";
    }

    @PostMapping
    public RedirectView upload(@RequestParam("file") MultipartFile file) {
        return new RedirectView("/");
    }
}
