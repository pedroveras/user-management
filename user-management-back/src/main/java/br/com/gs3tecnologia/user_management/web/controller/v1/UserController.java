package br.com.gs3tecnologia.user_management.web.controller.v1;

import br.com.gs3tecnologia.user_management.model.dto.ApiResponseDTO;
import br.com.gs3tecnologia.user_management.model.dto.request.ProfileRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.request.UserRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.request.UserUpdateRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.response.UserResponseDTO;
import br.com.gs3tecnologia.user_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN') or @securityUtil.authUser(#id)")
    @GetMapping("{id}")
    public ApiResponseDTO<UserResponseDTO> read(@PathVariable String id) {
        return new ApiResponseDTO.Builder<>(
                userService.readUser(id))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN') or @securityUtil.authUserIdentification(#username)")
    @GetMapping("find/username/{username}")
    public ApiResponseDTO<UserResponseDTO> readByUsername(@PathVariable String username) {
        return new ApiResponseDTO.Builder<>(
                userService.readUserByUsername(username))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ApiResponseDTO<Page<UserResponseDTO>> list(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "0") int pageSize) {
        return new ApiResponseDTO.Builder<>(
                userService.getUsers(pageNumber, pageSize))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN') or @securityUtil.authUser(#id)")
    @PutMapping("{id}")
    public ApiResponseDTO<UserResponseDTO> update(
            @Valid @RequestBody UserUpdateRequestDTO userRequestDTO,
            @PathVariable String id) {
        var userResponse = userService.updateUser(userRequestDTO, id);
        return new ApiResponseDTO.Builder<>(userResponse).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ApiResponseDTO<String> create(
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        var user = userService.createUser(userRequestDTO);

        return new ApiResponseDTO.Builder<>(user).build();

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{userId}/profile/{profileId}")
    public ApiResponseDTO updateUserProfile(@PathVariable String userId, @PathVariable String profileId) {
        var userResponse = userService.addProfile(profileId, userId);
        return new ApiResponseDTO.Builder<>(userResponse).build();
    }
}
