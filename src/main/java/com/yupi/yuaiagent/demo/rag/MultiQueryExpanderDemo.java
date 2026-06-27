package com.yupi.yuaiagent.demo.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 多查询展开器示例，没那么常用
 * 作用：利用大模型将用户原始查询改写为多个语义相近但表述不同的查询，
 * 从而提高 RAG 向量检索的召回率
 */
@Component
public class MultiQueryExpanderDemo {

    // AI 对话客户端构建器，用于调用大模型生成扩展查询
    private final ChatClient.Builder chatClientBuilder;

    // 通过构造函数注入 ChatClient.Builder
    public MultiQueryExpanderDemo(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }

    /**
     * 将原始查询扩展为多个查询
     * @param query 用户输入的原始查询文本
     * @return 扩展后的查询列表
     */
    public List<Query> expand(String query) {
        // 构建多查询扩展器实例
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder) // 传入 AI 客户端，用于调用大模型改写查询
                .numberOfQueries(3) // 指定生成的扩展查询数量为 3 个
                .build();
        // 执行查询扩展（注意：这里使用了硬编码字符串，应该改为 new Query(query)）
        List<Query> queries = queryExpander.expand(new Query("谁是程序员鱼皮啊？"));

        return queries;
    }
}
