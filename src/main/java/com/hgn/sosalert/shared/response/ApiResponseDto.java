package com.hgn.sosalert.shared.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public final class ApiResponseDto<T> {

    private final String statusCode;
    private final String message;

    @Nullable
    private final T data;

    public ApiResponseDto(String statusCode, String message) {
        this(statusCode, message, null);
    }
}
