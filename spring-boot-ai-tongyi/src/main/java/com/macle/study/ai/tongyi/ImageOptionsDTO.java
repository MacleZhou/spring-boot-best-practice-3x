package com.macle.study.ai.tongyi;

import com.alibaba.cloud.ai.tongyi.image.TongYiImagesOptions;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ImageOptionsDTO extends TongYiImagesOptions {
    /**图片描述*/
    private String instructions ;
    // getter, setter
}