package com.yupi.yuaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

@Configuration
public class PgVectorVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Bean
    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashEmbeddingModel) {
        PgVectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashEmbeddingModel)
                .dimensions(1024)                         // DashScope text-embedding-v3 输出 1024 维
                .distanceType(COSINE_DISTANCE)            // Optional: defaults to COSINE_DISTANCE
                .indexType(HNSW)                          // Optional: defaults to HNSW
                .initializeSchema(true)                   // Optional: defaults to false
                .schemaName("public")                     // Optional: defaults to "public"
                .vectorTableName("vector_store")          // Optional: defaults to "vector_store"
                .maxDocumentBatchSize(9)                 // DashScope text-embedding-v3 单批最多 10 条
                .build();

        // 加载文档
        List<Document> documents = loveAppDocumentLoader.loadMarkdown();
        // DashScope text-embedding-v3 单批最多 10 条，手动分批添加
        int batchSize = 9;
        for (int i = 0; i < documents.size(); i += batchSize) {
            List<Document> batch = documents.subList(i, Math.min(i + batchSize, documents.size()));
            vectorStore.add(batch);
        }
        return vectorStore;
    }
}
