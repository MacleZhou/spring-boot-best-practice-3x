package com.macle.thumbnailator.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

public class ImageUtilsTest {
    @Test
    public void imageFileToBase64() throws Exception {
        String filePath = "/Users/maclezhou/Personal 2/Photos/Bardeen/20130609_175405.jpg";
        String imageStr = ImageUtils.resizeImage(filePath, 60, 300, 420);
        System.out.println(imageStr);
    }
}
