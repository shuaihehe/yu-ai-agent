package com.yupi.yuaiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

import java.util.List;

/**
 * 创建自定义的 RAG 检索增强顾问的工厂
 */
@Slf4j
public class LoveAppRagCustomAdvisorFactory {

    /**
     * 创建自定义的 RAG 检索增强顾问
     * @param vectorStore 向量存储
     * @param status 婚姻状态过滤条件（单身/恋爱/已婚）
     * @return Advisor
     */
    public static Advisor createLoveRagCustomAdvisor(VectorStore vectorStore, String status) {
        // 过滤特定状态的文档
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status", status)
                .build();
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .filterExpression(expression)  // 过滤条件
                .topK(3)
                .build();

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .build();
    }

    /**
     * 测试检索：检查指定 status 的文档是否能被检索到
     */
    public static void testRetrieval(VectorStore vectorStore, String status, String query) {
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status", status)
                .build();
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(3)
                .filterExpression(expression)
                .build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        log.info("=== 检索测试 status={}, 查询={} ===", status, query);
        log.info("检索到 {} 个文档", documents.size());
        for (int i = 0; i < documents.size(); i++) {
            Document doc = documents.get(i);
            log.info("文档[{}] metadata={}, 内容前80字={}", i, doc.getMetadata(),
                    doc.getText().substring(0, Math.min(80, doc.getText().length())));
        }
    }
}
