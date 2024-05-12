package com.macle.study.ai.tongyi;


import com.alibaba.cloud.ai.tongyi.audio.TongYiAudioSpeechOptions;
import lombok.Getter;
import lombok.Setter;


@Setter @Getter
public class AudioOptionsDTO extends TongYiAudioSpeechOptions {
    /**文本描述*/
    private String instructions ;
    // getter, setter
}