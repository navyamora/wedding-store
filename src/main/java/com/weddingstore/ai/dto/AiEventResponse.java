package com.weddingstore.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiEventResponse {

    private Long eventId;
    private String eventTitle;
    private String result;
}