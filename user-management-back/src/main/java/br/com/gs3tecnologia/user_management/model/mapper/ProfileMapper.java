package br.com.gs3tecnologia.user_management.model.mapper;

import br.com.gs3tecnologia.user_management.model.dto.request.ProfileRequestDTO;
import br.com.gs3tecnologia.user_management.model.dto.response.ProfileResponseDTO;
import br.com.gs3tecnologia.user_management.model.entity.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    ProfileResponseDTO profileEntityToProfileResponseDTO(ProfileEntity profileEntity);

    ProfileEntity profileResponseDTOToProfileEntity(ProfileResponseDTO profileResponseDTO);
    ProfileEntity profileRequestDTOToProfileEntity(ProfileRequestDTO profileResponseDTO);

}
