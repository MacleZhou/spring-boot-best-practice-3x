package com.macle.study.sensitive.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class SensitiveWordFilter {

    // 敏感词字典树
    private Map<Character, Map> sensitiveWordMap;

    // 最小匹配规则
    public static final int MATCH_TYPE_MIN = 1;
    // 最大匹配规则
    public static final int MATCH_TYPE_MAX = 2;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 初始化敏感词库
     */
    @PostConstruct
    public void init() {
        sensitiveWordMap = new HashMap<>();
        // 从Redis加载敏感词
        loadSensitiveWordsFromRedis();
    }

    /**
     * 从Redis加载敏感词
     */
    private void loadSensitiveWordsFromRedis() {
        try {
            Set<String> words = stringRedisTemplate.opsForSet().members("sensitive_words");
            if (words != null) {
                words.forEach(this::addWordToMap);
            }
        } catch (Exception e) {
            log.error("Failed to load sensitive words from Redis", e);
        }
    }

    /**
     * 添加敏感词到字典树
     */
    public void addWordToMap(String word) {
        Map<Character, Map> currentMap = sensitiveWordMap;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            Map<Character, Map> subMap = currentMap.get(c);

            if (subMap == null) {
                subMap = new HashMap<>();
                currentMap.put(c, subMap);
            }

            currentMap = subMap;

            if (i == word.length() - 1) {
                currentMap.put('$', null);
            }
        }
    }

    /**
     * 检查是否包含敏感词
     */
    public boolean containsSensitiveWord(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }

        for (int i = 0; i < text.length(); i++) {
            int length = checkSensitiveWord(text, i, MATCH_TYPE_MIN);
            if (length > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取敏感词
     */
    public Set<String> findSensitiveWords(String text) {
        Set<String> sensitiveWords = new HashSet<>();
        if (StringUtils.isBlank(text)) {
            return sensitiveWords;
        }

        for (int i = 0; i < text.length(); i++) {
            int length = checkSensitiveWord(text, i, MATCH_TYPE_MAX);
            if (length > 0) {
                sensitiveWords.add(text.substring(i, i + length));
                i = i + length - 1;
            }
        }
        return sensitiveWords;
    }

    /**
     * 替换敏感词
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }

        StringBuilder result = new StringBuilder(text);
        Set<String> words = findSensitiveWords(text);

        for (String word : words) {
            int index = result.indexOf(word);
            while (index != -1) {
                for (int i = index; i < index + word.length(); i++) {
                    result.setCharAt(i, '*');
                }
                index = result.indexOf(word, index + 1);
            }
        }

        return result.toString();
    }

    /**
     * 检查敏感词
     */
    private int checkSensitiveWord(String text, int beginIndex, int matchType) {
        Map<Character, Map> currentMap = sensitiveWordMap;
        int wordLength = 0;
        int maxLength = 0;

        for (int i = beginIndex; i < text.length(); i++) {
            char c = text.charAt(i);
            currentMap = currentMap.get(c);

            if (currentMap == null) {
                break;
            }

            wordLength++;

            if (currentMap.containsKey('$')) {
                if (matchType == MATCH_TYPE_MIN) {
                    return wordLength;
                } else {
                    maxLength = wordLength;
                }
            }
        }

        return maxLength;
    }
}