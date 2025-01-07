package br.com.gs3tecnologia.user_management.service;

import br.com.gs3tecnologia.user_management.exception.UserAlreadyExistsException;
import br.com.gs3tecnologia.user_management.exception.UserNotFoundException;
import br.com.gs3tecnologia.user_management.model.dto.request.ProfileRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.request.UserRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.request.UserUpdateRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.response.UserResponseDTO;
import br.com.gs3tecnologia.user_management.model.entity.ProfileEntity;
import br.com.gs3tecnologia.user_management.model.entity.UserEntity;
import br.com.gs3tecnologia.user_management.model.mapper.ProfileMapper;
import br.com.gs3tecnologia.user_management.model.mapper.UserMapper;
import br.com.gs3tecnologia.user_management.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private final ProfileService profileService;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                       ProfileService profileService,
                       UserMapper userMapper,
                       ProfileMapper profileMapper,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.profileService = profileService;
        this.userMapper = userMapper;
        this.profileMapper = profileMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void deleteUser(String id) {
        repository.delete(getUserById(id));
    }

    public Page<UserResponseDTO> getUsers(int pageNumber, int pageSize) {
        if (pageSize == 0) pageSize = 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return repository.findAll(pageable)
                .map(userMapper::userEntityToUserResponseDTO);
    }

    public UserResponseDTO readUser(String id) {
        var userEntity = getUserById(id);
        return userMapper.userEntityToUserResponseDTO(userEntity);
    }

    public UserResponseDTO readUserByUsername(String username) {
        var userEntity = getUserByUsername(username);
        return userMapper.userEntityToUserResponseDTO(userEntity);
    }

    public String createUser(UserRequestDTO userRequestDTO) {
        validateUserEmail(userRequestDTO);
        validateUsername(userRequestDTO);

        var profileEntity = profileService.createUserProfileIfNotExists();

        var userEntity = userMapper.userRequestDTOToUserEntity(userRequestDTO);
        userEntity.setSecret(passwordEncoder.encode(userEntity.getSecret()));
        userEntity.setProfile(profileEntity);

        return repository.save(userEntity).getUsername();
    }

    public UserResponseDTO updateUser(UserUpdateRequestDTO userRequestDTO, String id) {
        var userEntity = getUserById(id);
        userEntity.setEmail(userRequestDTO.email());
        userEntity.setPhone(userRequestDTO.phone());

        return userMapper
                .userEntityToUserResponseDTO(
                        repository.save(userEntity));
    }

    public UserResponseDTO addProfile(String profileId, String userId) {
        var user = getUserById(userId);
        var userProfile = user.getProfile();

        if (userProfile != null && userProfile.getId().equals(profileId)) {
            user.setProfile(null);
        } else {
            var profileResponseDTO = profileService.readProfile(profileId);
            var profileEntity = profileMapper.profileResponseDTOToProfileEntity(profileResponseDTO);

            user.setProfile(profileEntity);
        }

        return userMapper
                .userEntityToUserResponseDTO(
                        repository.save(user));
    }

    public void createDefaultUser() {
        if (!repository.findByUsername("admin").isPresent()) {
            var profileRequestDTO = new ProfileRequestDTO("admin", "admin");

            String profileId = profileService.createProfile(profileRequestDTO);
            var profile = new ProfileEntity();
            profile.setId(profileId);

            var user = new UserEntity();
            user.setName("Admin");
            user.setPhone("1111111");
            user.setSecret(passwordEncoder.encode("12345"));
            user.setEmail("admin@userapp.com");
            user.setBirthDate(LocalDate.of(1987, 02, 10));
            user.setUsername("admin");
            user.setProfile(profile);

            repository.save(user);
        }
    }

    private UserEntity getUserById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("""
                        User with id %s not found""".formatted(id)));
    }

    private UserEntity getUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("""
                        User with username %s not found""".formatted(username)));
    }

    private void validateUserEmail(UserRequestDTO userRequestDTO) {
        if (repository.existsByEmail(userRequestDTO.email())) {
            throw new UserAlreadyExistsException("""
                    User with email %s already exists
                    """.formatted(userRequestDTO.email()));
        }
    }

    private void validateUsername(UserRequestDTO userRequestDTO) {
        if (repository.existsByUsername(userRequestDTO.email())) {
            throw new UserAlreadyExistsException("""
                    User with username %s already exists
                    """.formatted(userRequestDTO.username()));
        }
    }
}
