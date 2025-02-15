package com.macle.study.lang.chain.api;

import com.macle.study.lang.chain.service.SBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class SBotController {
    private final SBotService sBotService;
    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody String question) {
        try {
            return ResponseEntity.ok(sBotService.askQuestion(question));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Sorry, I can't process your question right now.");
        }
    }
}