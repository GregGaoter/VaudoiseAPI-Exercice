package ch.vaudoise.vaudoiseapi.exercice.service.mapper;

import ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo;
import ch.vaudoise.vaudoiseapi.exercice.domain.Person;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ClientInfoDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.PersonUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonUpdateDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonUpdateMapper {
    /**
     * The {@code birthDate} field is explicitly ignored during the mapping process because it should not be overwritten when updating an existing entity.
     */
    @Mapping(target = "birthDate", ignore = true)
    Person toEntity(PersonUpdateDTO dto);

    /**
     * The {@code birthDate} field is explicitly ignored during the mapping process because it should not be overwritten when updating an existing entity.
     */
    @Mapping(target = "birthDate", ignore = true)
    Person updatePersonFromDto(PersonUpdateDTO dto, @MappingTarget Person entity);

    ClientInfo updateClientInfoFromDto(ClientInfoDTO dto, @MappingTarget ClientInfo entity);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Person entity, PersonUpdateDTO dto);
}
