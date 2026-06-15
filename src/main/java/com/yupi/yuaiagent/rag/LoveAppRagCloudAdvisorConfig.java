package com.yupi.yuaiagent.rag;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeConnectionProperties;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
public class LoveAppRagCloudAdvisorConfig {

    private static final String KNOWLEDGE_INDEX = "恋爱大师";

    @Bean
    public DashScopeApi dashScopeApi(DashScopeConnectionProperties connectionProperties,
                                     ObjectProvider<RestClient.Builder> restClientBuilderProvider,
                                     ObjectProvider<WebClient.Builder> webClientBuilderProvider,
                                     ResponseErrorHandler responseErrorHandler) {
        // 1.1.2.0 中 DashScopeChatAutoConfiguration#dashscopeChatApi 是 private，未将 DashScopeApi 暴露为独立 Bean，
        // 这里手动使用 builder 构造并注册为 Bean，供 RAG 云检索使用。
        return DashScopeApi.builder()
                .apiKey(connectionProperties.getApiKey())
                .baseUrl(connectionProperties.getBaseUrl())
                .workSpaceId(connectionProperties.getWorkspaceId())
                .restClientBuilder(restClientBuilderProvider.getIfAvailable(RestClient::builder))
                .webClientBuilder(webClientBuilderProvider.getIfAvailable(WebClient::builder))
                .responseErrorHandler(responseErrorHandler)
                .build();
    }

    @Bean
    public Advisor loveAppRagCloudAdvisor(DashScopeApi dashScopeApi) {
        DocumentRetriever dashScopeDocumentRetriever = new DashScopeDocumentRetriever(
                dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(KNOWLEDGE_INDEX)
                        .build());

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(dashScopeDocumentRetriever)
                .build();
    }
}