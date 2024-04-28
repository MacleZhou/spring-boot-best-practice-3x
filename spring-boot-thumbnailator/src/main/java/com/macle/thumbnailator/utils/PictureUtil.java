package com.macle.thumbnailator.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class PictureUtil {

    /**
     * 水印图片
     */
    private static File markIco = null;

    //开机静态加载水印图片
    static {
        try {
            markIco = new File(new File("").getCanonicalPath() + "/icon.png");
            log.info("水印图片加载" + (markIco.exists() ? "成功" : "失败"));
        } catch (Exception e) {
        }
    }

    /**
     * 加水印
     */
    public void photoMark(File sourceFile, File toFile) throws IOException {
        Thumbnails.of(sourceFile)
                .size(600, 450)//尺寸
                .watermark(Positions.BOTTOM_CENTER/*水印位置：中央靠下*/,
                        ImageIO.read(markIco), 0.7f/*质量，越大质量越高(1)*/)
                //.outputQuality(0.8f)
                .toFile(toFile);//保存为哪个文件
    }

    /**
     * 生成图片缩略图
     */
    public void photoSmaller(File sourceFile, File toFile) throws IOException {
        Thumbnails.of(sourceFile)
                .size(200, 150)//尺寸
                //.watermark(Positions.CENTER, ImageIO.read(markIco), 0.1f)
                .outputQuality(0.4f)//缩略图质量
                .toFile(toFile);
    }

    /**
     * 生成视频缩略图（这块还没用到呢）
     */
    public void photoSmallerForVedio(File sourceFile, File toFile) throws IOException {
        Thumbnails.of(sourceFile)
                .size(440, 340)
                .watermark(Positions.BOTTOM_CENTER, ImageIO.read(markIco), 0.1f)
                .outputQuality(0.8f)
                .toFile(toFile);
    }
}