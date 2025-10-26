package ch.vaudoise.vaudoiseapi.exercice.service.mapper;

import ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo;
import ch.vaudoise.vaudoiseapi.exercice.domain.Company;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ClientInfoDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.CompanyUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyUpdateDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyUpdateMapper {
    /**
     * The {@code companyIdentifier} field is explicitly ignored during the mapping process because it should not be overwritten when updating an existing entity.
     */
    @Mapping(target = "companyIdentifier", ignore = true)
    Company toEntity(CompanyUpdateDTO dto);

    /**
     * The {@code companyIdentifier} field is explicitly ignored during the mapping process because it should not be overwritten when updating an existing entity.
     */
    @Mapping(target = "companyIdentifier", ignore = true)
    Company updateCompanyFromDto(CompanyUpdateDTO dto, @MappingTarget Company entity);

    ClientInfo updateClientInfoFromDto(ClientInfoDTO dto, @MappingTarget ClientInfo entity);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Company entity, CompanyUpdateDTO dto);
}
