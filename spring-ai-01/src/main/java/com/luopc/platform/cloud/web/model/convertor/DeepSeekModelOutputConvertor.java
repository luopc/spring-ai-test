package com.luopc.platform.cloud.web.model.convertor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.util.StringUtils;
import reactor.util.annotation.NonNull;

record DeepSeekModelResponse(String chainOfThought, String answer) {

}

public class DeepSeekModelOutputConvertor implements StructuredOutputConverter<DeepSeekModelResponse> {

    private static final Logger log = LoggerFactory.getLogger(DeepSeekModelOutputConvertor.class);
    private static final String OPENING_THINK_TAG = "<think>";
    private static final String CLOSING_THINK_TAG = "</think>";

    @Override
    public DeepSeekModelResponse convert(@NonNull String text) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("Text cannot be blank");
        }
        int openingThinkTagIndex = text.indexOf(OPENING_THINK_TAG);
        int closingThinkTagIndex = text.indexOf(CLOSING_THINK_TAG);

        if (openingThinkTagIndex != -1 && closingThinkTagIndex != -1 && closingThinkTagIndex > openingThinkTagIndex) {
            String chainOfThought = text.substring(openingThinkTagIndex + OPENING_THINK_TAG.length(), closingThinkTagIndex);
            String answer = text.substring(closingThinkTagIndex + CLOSING_THINK_TAG.length());
            return new DeepSeekModelResponse(chainOfThought, answer);
        } else {
            log.debug("No <think> tags found in the response. Treating entire text as answer.");
            return new DeepSeekModelResponse(null, text);
        }
    }

    @Override
    public String getFormat() {
        return "";
    }

}

