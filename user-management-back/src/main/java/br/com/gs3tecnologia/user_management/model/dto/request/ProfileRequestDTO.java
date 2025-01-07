package br.com.gs3tecnologia.user_management.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProfileRequestDTO(
        @NotNull
        @NotBlank
        @NotEmpty
        String name,
        @NotNull
        @NotBlank
        @NotEmpty
        String description
) {
}
