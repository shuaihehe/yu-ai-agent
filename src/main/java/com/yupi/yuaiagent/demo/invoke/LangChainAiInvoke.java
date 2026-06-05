package com.yupi.yuaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class LangChainAiInvoke {

    public static void main(String[] args) {
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(TestApiKey.APIKEY)
                .modelName("qwen-plus")
                .build();
        String answer = qwenChatModel.chat("你好，请介绍一下你自己");
        System.out.println(answer);
    }
}
