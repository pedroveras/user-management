package br.com.gs3tecnologia.user_management.model.dto.response;

public record UserResponseDTO(
        String id,
        String username,
        String name,
        String email,
        String phone,
        String birthDate,
        ProfileResponseDTO profile) {
}
