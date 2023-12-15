package com.example.taskmanagementsystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseJson {
    private String message;
    private String status;

    public ResponseJson(String message, String status) {
        this.message = message;
        this.status = status;
    }
}
