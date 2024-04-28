package com.macle.thumbnailator.controller;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class IThumbnailsService {
    /**
     * 指定大小缩放 若图片横比width小，高比height小，放大
     * 若图片横比width小，高比height大，高缩小到height，图片比例不变
     * 若图片横比width大，高比height小，横缩小到width，图片比例不变
     * 若图片横比width大，高比height大，图片按比例缩小，横为width或高为height
     *
     * @param resource  源文件路径
     * @param width     宽
     * @param height    高
     * @param toFile    生成文件路径
     */
    public String changeSize(MultipartFile resource, int width, int height, String toFile) {
        try {
            Thumbnails.of(resource.getInputStream()).size(width, height).outputFormat("jpg").toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "changeSize";
    }

    /**
     * 指定比例缩放 scale(),参数小于1,缩小;大于1,放大
     *
     * @param resource   源文件路径
     * @param scale      指定比例
     * @param toFile     生成文件路径
     */
    public String changeScale(MultipartFile resource, double scale, String toFile) {
        try {
            Thumbnails.of(resource.getInputStream()).scale(scale).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "changeScale";
    }

    /**
     * 添加水印 watermark(位置,水印,透明度)
     *
     * @param resource  源文件路径
     * @param center    水印位置
     * @param watermark   水印文件路径
     * @param opacity   水印透明度
     * @param toFile    生成文件路径
     */

    public String watermark(MultipartFile resource, Positions center, MultipartFile watermark, float opacity, String toFile) {
        try {
            Thumbnails.of(resource.getInputStream()).scale(1).watermark(center, ImageIO.read(watermark.getInputStream()), opacity).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "watermark";
    }

    /**
     * 图片旋转 rotate(度数),顺时针旋转
     *
     * @param resource  源文件路径
     * @param rotate    旋转度数
     * @param toFile    生成文件路径
     */
    public String rotate(MultipartFile resource, double rotate, String toFile) {
        try {
            Thumbnails.of(resource.getInputStream()).scale(1).rotate(rotate).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "rotate";
    }

    /**
     * 图片裁剪 sourceRegion()有多种构造方法，示例使用的是sourceRegion(裁剪位置,宽,高)
     *
     * @param resource  源文件路径
     * @param center    裁剪位置
     * @param width     裁剪区域宽
     * @param height    裁剪区域高
     * @param toFile    生成文件路径
     */
    public String region(MultipartFile resource, Positions center, int width, int height, String toFile) {
        try {
            Thumbnails.of(resource.getInputStream()).scale(1).sourceRegion(center, width, height).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "region";
    }
}
