package com.github.akhuntsaria.imagehosting.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.github.akhuntsaria.imagehosting.service.DynamoDbService;
import com.github.akhuntsaria.imagehosting.service.S3Service;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLConnection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/i")
public class ImageController {

    private final DynamoDbService dynamoDbService;

    private final S3Service s3Service;

    public ImageController(DynamoDbService dynamoDbService, S3Service s3Service) {
        this.dynamoDbService = dynamoDbService;
        this.s3Service = s3Service;
    }

    @GetMapping("/{id}")
    public ModelAndView details(@PathVariable("id") String id, Model model) {
        Optional<S3Object> s3Object = s3Service.findById(id);

        if (s3Object.isEmpty()) {
            return new ModelAndView("404", HttpStatus.NOT_FOUND);
        }

        model.addAttribute("id", id);
        model.addAttribute("originalFilename", s3Object.get().getObjectMetadata()
                .getUserMetaDataOf("original-filename"));
        model.addAttribute("views", dynamoDbService.getViews(id));

        dynamoDbService.incrementViews(id);

        return new ModelAndView("image/details", HttpStatus.OK);
    }

    @GetMapping("{id}/d")
    public Object download(@PathVariable("id") String id) {
        Optional<S3Object> s3Object = s3Service.findById(id);

        if (s3Object.isEmpty()) {
            return new ModelAndView("404", HttpStatus.NOT_FOUND);
        }

        String filename = s3Object.get().getObjectMetadata().getUserMetaDataOf("original-filename");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                .contentType(MediaType.valueOf(URLConnection.guessContentTypeFromName(filename)))
                .headers(headers)
                .body(new InputStreamResource(s3Object.get().getObjectContent()));
    }
}
