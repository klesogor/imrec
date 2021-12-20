package lab.imrec.injestor.images.controllers;

import lab.imrec.injestor.Modes;
import lab.imrec.injestor.images.dto.ImageData;
import lab.imrec.injestor.images.service.ImageUploadService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
@Profile(Modes.Injestor)
public class ImageUploadController {
    private final ImageUploadService service;

    public ImageUploadController(ImageUploadService service) {
        this.service = service;
    }

    @PostMapping
    public void handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        var start = System.nanoTime();

        var fileNameParts = file.getName().split("\\.");
        var extension = fileNameParts[fileNameParts.length - 1];
        var name = fileNameParts[0];
        var fileName = String.format("%s-%s.%s", name, UUID.randomUUID().toString(), extension);
        this.service.injestImageUpload(new ImageData.EmbeddedImage(fileName, file.getBytes()));

        var end = System.nanoTime();
        var diff = (end - start) / 1000000;
        System.out.println("Scheduled image processing in " + diff + "ms");
    }
}
