package com.macle.study.ai.tongyi;

import com.alibaba.cloud.ai.tongyi.audio.TongYiAudioSpeechOptions;
import com.alibaba.cloud.ai.tongyi.audio.api.SpeechPrompt;
import com.alibaba.cloud.ai.tongyi.audio.api.SpeechResponse;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.model.ModelClient;
import org.springframework.ai.model.ModelOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiService {

    @Resource
    private ChatClient chatClient;

    @Resource
    private StreamingChatClient streamChatClient;

    @Resource
    private ImageClient imageClient ;


    @Resource
    private ModelClient<SpeechPrompt, SpeechResponse> modelClient ;

    public String normalCompletion(String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatClient.call(prompt).getResult().getOutput().getContent();
    }

    public Flux<ChatResponse> streamCompletion(String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.streamChatClient.stream(prompt) ;
    }

    /**
     * @param instructions  图片说明
     * @param options       图片规格：大小, 图片数量，图片输出形式byte[],base64等
     */
    public ImageResponse genImage(String instructions, ImageOptions options) {
        ImagePrompt request = new ImagePrompt(instructions, options) ;
        return this.imageClient.call(request) ;
    }

    /**
     * @param instructions  文本内容
     * @param options       针对语音的配置
     * @return
     */
    public SpeechResponse genAudio(String instructions, ModelOptions options) {
        if (options instanceof TongYiAudioSpeechOptions speechOptions) {
            SpeechPrompt request = new SpeechPrompt(instructions, speechOptions) ;
            return this.modelClient.call(request) ;
        }
        return null ;
    }
}