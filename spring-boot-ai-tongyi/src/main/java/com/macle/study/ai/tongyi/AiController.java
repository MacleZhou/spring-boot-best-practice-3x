package com.macle.study.ai.tongyi;

import com.alibaba.cloud.ai.tongyi.audio.api.SpeechResponse;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RestController
@RequestMapping("/ai")
public class AiController {


    @Resource
    private AiService aiService ;

    @GetMapping("/normal")
    public String normal(String message) {
        return this.aiService.normalCompletion(message) ;
    }

    @GetMapping("/stream")
    public Flux<ChatResponse> stream(String message) {
        return this.aiService.streamCompletion(message);
    }


    @PostMapping("/image")
    public void image(@RequestBody ImageOptionsDTO image) {
        ImageResponse resp = this.aiService.genImage(image.getInstructions(), image) ;
        resp.getResults().forEach(imageGen -> {
            Image img = imageGen.getOutput() ;
            String imgBase64 = img.getB64Json() ;
            System.out.println(imgBase64) ;
            String url = img.getUrl() ;
            System.out.println(url) ;
        }) ;
    }

    @PostMapping("/audio")
    public void audio(@RequestBody AudioOptionsDTO audio) {
        SpeechResponse resp = this.aiService.genAudio(audio.getInstructions(), audio);
        resp.getResults().forEach(speech -> {
            ByteBuffer buffer = speech.getOutput();
            Path path = Paths.get("/Users/maclezhou/aaaa.wav");
            try (FileChannel channel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                // 写入 ByteBuffer 到文件
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
