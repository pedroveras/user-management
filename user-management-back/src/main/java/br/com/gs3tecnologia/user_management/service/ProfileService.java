package br.com.gs3tecnologia.user_management.service;

import br.com.gs3tecnologia.user_management.exception.ProfileAlreadyExistsException;
import br.com.gs3tecnologia.user_management.exception.ProfileNotFoundException;
import br.com.gs3tecnologia.user_management.model.dto.request.ProfileRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.response.ProfileResponseDTO;
import br.com.gs3tecnologia.user_management.model.entity.ProfileEntity;
import br.com.gs3tecnologia.user_management.model.mapper.ProfileMapper;
import br.com.gs3tecnologia.user_management.model.mapper.UserMapper;
import br.com.gs3tecnologia.user_management.repository.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final ProfileRepository repository;
    private final ProfileMapper profileMapper;
    private final UserMapper userMapper;

    public ProfileService(ProfileRepository repository, ProfileMapper profileMapper, UserMapper userMapper) {
        this.repository = repository;
        this.profileMapper = profileMapper;
        this.userMapper = userMapper;
    }

    public void deleteProfile(String id) {
        repository.delete(getProfileById(id));
    }

    public Page<ProfileResponseDTO> getProfiles(int pageNumber, int pageSize) {
        if (pageSize == 0) pageSize = 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return repository
                .findAll(pageable)
                .map(profileMapper::profileEntityToProfileResponseDTO);
    }

    public ProfileResponseDTO readProfile(String id) {
        var profileEntity = getProfileById(id);
        return profileMapper.profileEntityToProfileResponseDTO(profileEntity);
    }

    public String createProfile(ProfileRequestDTO profileRequestDTO) {
        validateProfileName(profileRequestDTO);

        var profileEntity = profileMapper.profileRequestDTOToProfileEntity(profileRequestDTO);

        return repository.save(profileEntity)
                .getId();
    }

    public ProfileResponseDTO updateProfile(ProfileRequestDTO profileRequestDTO, String id) {
        var profileEntity = getProfileById(id);

        profileEntity.setName(profileRequestDTO.name());
        profileEntity.setDescription(profileRequestDTO.description());

        return profileMapper
                .profileEntityToProfileResponseDTO(
                        repository.save(profileEntity));
    }

    public ProfileEntity createUserProfileIfNotExists() {
        return repository.findByName("USER").orElseGet(() -> {
            ProfileEntity profileEntity = new ProfileEntity();
            profileEntity.setName("USER");
            profileEntity.setDescription("user");
            return repository.save(profileEntity);
        });
    }

    private void validateProfileName(ProfileRequestDTO profileRequestDTO) {
        if (repository.existsByName(profileRequestDTO.name())) {
            throw new ProfileAlreadyExistsException("""
                    Profile with name %s already exists
                    """.formatted(profileRequestDTO.name()));
        }
    }

    private ProfileEntity getProfileById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("""
                        Profile with id %s not found
                        """.formatted(id)));
    }

}
