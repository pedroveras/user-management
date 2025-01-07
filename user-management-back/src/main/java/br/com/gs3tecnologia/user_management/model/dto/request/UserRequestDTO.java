package br.com.gs3tecnologia.user_management.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UserRequestDTO(
        String id,

        @NotNull
        @NotBlank
        @NotEmpty
        String name,

        @NotNull
        @NotBlank
        @NotEmpty
        String email,

        @NotNull
        @NotBlank
        @NotEmpty
        String phone,

        @NotNull
        @NotBlank
        @NotEmpty
        String username,

        @Past
        LocalDate birthDate,

        @NotNull
        @NotBlank
        @NotEmpty
        String secret) {
}
