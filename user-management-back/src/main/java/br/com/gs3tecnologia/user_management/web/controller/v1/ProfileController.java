package br.com.gs3tecnologia.user_management.web.controller.v1;

import br.com.gs3tecnologia.user_management.model.dto.ApiResponseDTO;
import br.com.gs3tecnologia.user_management.model.dto.request.ProfileRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.request.UserRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.response.ProfileResponseDTO;
import br.com.gs3tecnologia.user_management.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<String>> create(
            @Valid @RequestBody ProfileRequestDTO profileRequestDTO) {
        var profileId = profileService.createProfile(profileRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/v1/profiles/{profileId}")
                .buildAndExpand(profileId)
                .toUri();

        var apiResponseDTO = new ApiResponseDTO.Builder<>(profileId).build();

        return ResponseEntity
                .created(location)
                .body(apiResponseDTO);
    }

    @GetMapping("{id}")
    public ApiResponseDTO<ProfileResponseDTO> read(String id){
        return new ApiResponseDTO.Builder<>(
                profileService.readProfile(id))
                .build();
    }

    @GetMapping
    public ApiResponseDTO<Page<ProfileResponseDTO>> list(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "0") int pageSize) {
        return new ApiResponseDTO.Builder<>(
                profileService.getProfiles(pageNumber, pageSize))
                .build();
    }

    @PutMapping("{id}")
    public ApiResponseDTO<ProfileResponseDTO> update(
            @Valid @RequestBody ProfileRequestDTO profileRequestDTO,
            @PathVariable String id) {
        var profileResponse = profileService.updateProfile(profileRequestDTO, id);
        return new ApiResponseDTO.Builder<>(profileResponse).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
