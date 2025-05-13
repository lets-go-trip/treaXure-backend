package com.trip.treaxure.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API 표준 응답 형식")
public class ApiResponse<T> {
    
    @Schema(description = "응답 코드", example = "SU")
    private String code;
    
    @Schema(description = "응답 메시지", example = "Success")
    private String message;
    
    @Schema(description = "응답 데이터")
    private T data;
    
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code("SU")
                .message("Success")
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .code("SU")
                .message(message)
                .data(data)
                .build();
    }
    
    public static ApiResponse<?> success(String message) {
        return ApiResponse.builder()
                .code("SU")
                .message(message)
                .build();
    }
    
    public static ApiResponse<?> error(String code, String message) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
} 