package br.com.gs3tecnologia.user_management.model.mapper;

import br.com.gs3tecnologia.user_management.model.dto.request.UserRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.response.ProfileResponseDTO;
import br.com.gs3tecnologia.user_management.model.dto.response.UserResponseDTO;
import br.com.gs3tecnologia.user_management.model.entity.ProfileEntity;
import br.com.gs3tecnologia.user_management.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "profile", target = "profile", qualifiedByName = "profileToResponse")
    UserResponseDTO userEntityToUserResponseDTO(UserEntity userEntity);

    UserEntity userResponseDTOToUserEntity(UserResponseDTO userResponseDTO);

    UserEntity userRequestDTOToUserEntity(UserRequestDTO userRequestDTO);

    List<UserResponseDTO> userEntityListToUserResponseDTO(List<UserEntity> userEntityList);

    @Named("profileToResponse")
    static ProfileResponseDTO profileToResponse(ProfileEntity profileEntity) {
        return new ProfileResponseDTO(
                profileEntity.getName().toUpperCase(),
                profileEntity.getDescription());
    }
}
