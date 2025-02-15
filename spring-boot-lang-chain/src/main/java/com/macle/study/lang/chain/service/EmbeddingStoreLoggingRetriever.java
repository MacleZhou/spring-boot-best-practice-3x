package com.macle.study.lang.chain.service;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.retriever.Retriever;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class EmbeddingStoreLoggingRetriever implements Retriever<TextSegment> {
    private final EmbeddingStoreRetriever retriever;
    @Override
    public List<TextSegment> findRelevant(String text) {
        List<TextSegment> relevant = retriever.findRelevant(text);
        relevant.forEach(segment -> {
            log.debug("=======================================================");
            log.debug("Found relevant text segment: {}", segment);
        });
        return relevant;
    }
}