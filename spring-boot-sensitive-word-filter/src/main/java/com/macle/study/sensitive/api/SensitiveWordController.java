package com.macle.study.sensitive.api;


import com.macle.study.sensitive.config.SensitiveWordFilter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/sensitive")
@Slf4j
public class SensitiveWordController {

    @Resource
    private SensitiveWordFilter sensitiveWordFilter;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 添加敏感词
     */
    @PostMapping("/word")
    public ResponseEntity<String> addSensitiveWord(@RequestBody String word) {
        try {
            // 添加到Redis
            stringRedisTemplate.opsForSet().add("sensitive_words", word);
            // 添加到过滤器
            sensitiveWordFilter.addWordToMap(word);
            return ResponseEntity.ok("添加成功");
        } catch (Exception e) {
            log.error("添加敏感词失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("添加失败");
        }
    }

    /**
     * 检查文本是否包含敏感词
     */
    @PostMapping("/check")
    public ResponseEntity<Boolean> checkText(@RequestBody String text) {
        return ResponseEntity.ok(sensitiveWordFilter.containsSensitiveWord(text));
    }

    /**
     * 过滤敏感词
     */
    @PostMapping("/filter")
    public ResponseEntity<String> filterText(@RequestBody String text) {
        return ResponseEntity.ok(sensitiveWordFilter.filter(text));
    }

    /**
     * 获取文本中的所有敏感词
     */
    @PostMapping("/find")
    public ResponseEntity<Set<String>> findSensitiveWords(@RequestBody String text) {
        return ResponseEntity.ok(sensitiveWordFilter.findSensitiveWords(text));
    }
}