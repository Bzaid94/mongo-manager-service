package com.blautech.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "${history-collection}")
public class History {
    @Id
    private String id;
    private String productId;
    private String eventType;
    private Map<String, Object> snapshot;
    private LocalDateTime date;
}