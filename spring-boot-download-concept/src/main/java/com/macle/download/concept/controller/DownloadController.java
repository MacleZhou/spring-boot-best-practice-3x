package com.macle.download.concept.controller;

import com.github.linyuzai.download.core.annotation.Download;;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;

@RestController
public class DownloadController {
    @Download(source = "classpath:/download/readme.txt")
    @GetMapping("/classpath")
    public void classpath() {
    }

    @Download
    @GetMapping("/file")
    public File file() {
        return new File("/Users/maclezhou/JavaWorld/spring-boot-best-practice-3x/spring-boot-download-concept/src/main/resources/download/readme_file.txt");
    }

    @Download(source="http://127.0.0.1:8080/image.jpg")
    @GetMapping("/http")
    public String http() {
        return "http://127.0.0.1:8080/image.jpg";
    }

    @Download(source = "classpath:/download/readme_mono.txt")
    @GetMapping("/classpath_mono")
    public Mono<Void> classpathMono() {
        return Mono.empty();
    }

    @Download
    @GetMapping("/file_mono")
    public Mono<File> fileMono() {
        return Mono.just(new File("/Users/maclezhou/JavaWorld/spring-boot-best-practice-3x/spring-boot-download-concept/src/main/resources/download/readme_file_mono.txt"));
    }

    @Download(source="http://127.0.0.1:8080/image.jpg")
    @GetMapping("/http_mono")
    public Mono<String> httpMono() {
        return Mono.just("http://127.0.0.1:8080/image.jpg");
    }
}
