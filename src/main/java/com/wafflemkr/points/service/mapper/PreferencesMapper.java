package com.wafflemkr.points.service.mapper;

import com.wafflemkr.points.domain.*;
import com.wafflemkr.points.service.dto.PreferencesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Preferences and its DTO PreferencesDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PreferencesMapper extends EntityMapper<PreferencesDTO, Preferences> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    PreferencesDTO toDto(Preferences preferences);

    @Mapping(source = "userId", target = "user")
    Preferences toEntity(PreferencesDTO preferencesDTO);

    default Preferences fromId(Long id) {
        if (id == null) {
            return null;
        }
        Preferences preferences = new Preferences();
        preferences.setId(id);
        return preferences;
    }
}
