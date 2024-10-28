package com.ckdwls.boardguide.Controller;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/images")
public class ImageController {
    
    private final Path imageLocation;

    private String profileDir;

    public ImageController(@Value("${crawling.images}") String uploadDir) {
        this.imageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.profileDir = uploadDir;
    } // 이미지가 저장된 경로

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            // 이미지 파일 경로 생성
            String decodedFilename = URLDecoder.decode(filename, StandardCharsets.UTF_8.toString());

            Path filePath = imageLocation.resolve(decodedFilename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // 파일이 존재하고 읽을 수 있을 때
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestBody MultipartFile image) {

        if(image.isEmpty()) {
            return ResponseEntity.badRequest().body("Please provide a valid file");
        }

        File directory = new File(profileDir);
        if(!directory.exists()) {
            directory.mkdirs();
        }

        try{
            Path filePath = Paths.get(profileDir, image.getOriginalFilename());
            Files.write(filePath,image.getBytes());
            return ResponseEntity.ok().body("Image uploaded successfully : " + filePath.toString());
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body("error");
        }
    }
    
}
