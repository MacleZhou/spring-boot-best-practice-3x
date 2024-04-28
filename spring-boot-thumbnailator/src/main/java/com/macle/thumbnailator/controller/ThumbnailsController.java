package com.macle.thumbnailator.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.annotation.Resource;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.UUID;

@RestController
public class ThumbnailsController {
    @Resource
    private IThumbnailsService thumbnailsService;

    /**
     * 指定大小缩放
     */
    @PostMapping("/changeSize")
    public String changeSize(MultipartFile resource, int width, int height) {
        String toFile = "~/JavaWorld/spring-boot-best-practice-3x/spring-boot-thumbnailator/images/changeSize";
        return thumbnailsService.changeSize(resource, width, height, toFile);
    }

    /**
     * 指定比例缩放
     */
    @PostMapping("/changeScale")
    public String changeScale(MultipartFile resource, double scale) {
        String toFile = "~/JavaWorld/spring-boot-best-practice-3x/spring-boot-thumbnailator/images/changeScale";
        return thumbnailsService.changeScale(resource, scale, toFile);
    }

    /**
     * 添加水印 watermark(位置,水印,透明度)
     */
    @PostMapping("/watermark")
    public String watermark(MultipartFile resource, Positions center, MultipartFile watermark, float opacity) {
        String toFile = "~/JavaWorld/spring-boot-best-practice-3x/spring-boot-thumbnailator/images/watermark";
        return thumbnailsService.watermark(resource, Positions.CENTER, watermark, opacity, toFile);
    }

    /**
     * 图片旋转 rotate(度数),顺时针旋转
     */
    @PostMapping("/rotate")
    public String rotate(MultipartFile resource, double rotate) {
        String toFile = "~/JavaWorld/spring-boot-best-practice-3x/spring-boot-thumbnailator/images/rotate";
        return thumbnailsService.rotate(resource, rotate, toFile);
    }

    /**
     * 图片裁剪
     */
    @PostMapping("/region")
    public String region(MultipartFile resource, Positions center, int width, int height) {
        String toFile = "~/JavaWorld/spring-boot-best-practice-3x/spring-boot-thumbnailator/images/region";
        return thumbnailsService.region(resource, Positions.CENTER, width, height, toFile);
    }

    @ApiOperation("上传业务图片")
    @PostMapping("/push/photo/{id}/{name}")
    public String pushHousingPhotoMethod(
            @ApiParam("SourceId") @PathVariable Integer id,
            @ApiParam("图片名称不约束，可不填则使用原名，可使用随机码或原名称，但必须带扩展名") @PathVariable(required = false) String name,
            @RequestParam MultipartFile file) throws InterruptedException, ExecutionException, IOException {
        String fileName = file.getOriginalFilename();
        String ext = StringUtils.substring(fileName, fileName.lastIndexOf('.'),fileName.length());
        /*
        File tempPhoto = File.createTempFile(UUIDUtil.make32BitUUID(), ext);
        file.transferTo(tempPhoto);//转储临时文件
        service.pushPhoto(id, name, tempPhoto);
        */
        return null;
    }

    /**
     * 生成一个32位无横杠的UUID
     */
    public synchronized static String make32BitUUID(){
        return UUID().toString().replace("-","");
    }
}
