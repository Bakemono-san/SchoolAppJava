package org.SchoolApp.Web.Dtos.Mapper;

import org.SchoolApp.Datas.Entity.EmargementEntity;
import org.SchoolApp.Web.Dtos.Response.EmargementResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmargementMapper {
    EmargementMapper INSTANCE = Mappers.getMapper(EmargementMapper.class);

    EmargementResponseDto toDto(EmargementEntity entity);

}
