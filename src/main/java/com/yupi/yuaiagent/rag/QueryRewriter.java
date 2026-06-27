package com.yupi.yuaiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * 查询重写器
 * 利用大模型对用户原始查询进行语义重写，提升检索召回率
 */
@Component
@Slf4j
public class QueryRewriter {

    private final ChatClient.Builder chatClientBuilder;

    public QueryRewriter(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }

    /**
     * 对原始查询进行重写
     * @param query 用户输入的原始查询文本
     * @return 重写后的 Query 对象
     */
    public Query rewrite(String query) {
        QueryTransformer queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();
        Query rewrittenQuery = queryTransformer.transform(new Query(query));
        log.info("原始查询: {}", query);
        log.info("重写查询: {}", rewrittenQuery.text());
        return rewrittenQuery;
    }
}
