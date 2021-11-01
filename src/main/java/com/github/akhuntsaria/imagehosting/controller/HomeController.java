package com.github.akhuntsaria.imagehosting.controller;

import com.github.akhuntsaria.imagehosting.service.S3Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    private final S3Service s3Service;

    public HomeController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping
    public String home() {
        return "home";
    }

    @PostMapping
    public RedirectView upload(@RequestParam("file") MultipartFile file) {
        String id = s3Service.upload(file);

        if (id == null) {
            //TODO: handle errors
            return new RedirectView("/");
        }

        return new RedirectView("/i/" + id);
    }
}
