package org.SchoolApp.Web.Dtos.Mapper;

import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Web.Dtos.Request.ApprenantRequestDto;
import org.SchoolApp.Web.Dtos.Response.ApprenantResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ReferentielMapper.class, PromoMapper.class})
public interface ApprenantMapper {

    @Mappings({
            @Mapping(source = "dto.referentielId", target = "referentiel.id"),
            @Mapping(source = "dto.promoId", target = "promo.id"),
            @Mapping(source = "dto.user", target = "user") // User mapping through UserMapper
    })
    ApprenantEntity toEntity(ApprenantRequestDto dto);

    @Mappings({
            @Mapping(source = "entity.referentiel.libelle", target = "referentiel"),
            @Mapping(source = "entity.promo.libelle", target = "promo"),
            @Mapping(source = "entity.user", target = "user") // User mapping through UserMapper
    })
    ApprenantResponseDto toDto(ApprenantEntity entity);
}

