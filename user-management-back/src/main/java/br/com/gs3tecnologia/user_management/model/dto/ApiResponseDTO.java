package br.com.gs3tecnologia.user_management.model.dto;

import java.time.LocalDateTime;

public record ApiResponseDTO<T>(
        String message,
        String instant,
        T data
) {
    public static final class Builder<T> {
        private String message = "Request successfully processed";
        private final String instant = LocalDateTime.now().toString();
        private T data;

        public Builder() {
        }

        public Builder(T data) {
            this.data = data;
        }

        public Builder<T> setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> setData(T data) {
            this.data = data;
            return this;
        }

        public ApiResponseDTO<T> build() {
            return new ApiResponseDTO<>(message, instant, data);
        }
    }
}
