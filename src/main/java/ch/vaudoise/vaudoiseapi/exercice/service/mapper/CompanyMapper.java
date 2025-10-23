package ch.vaudoise.vaudoiseapi.exercice.service.mapper;

import ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo;
import ch.vaudoise.vaudoiseapi.exercice.domain.Company;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ClientInfoDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.CompanyDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "clientInfo", source = "clientInfo", qualifiedByName = "clientInfoFull")
    CompanyDTO toDto(Company s);

    @Named("clientInfoFull")
    ClientInfoDTO toDtoClientInfoId(ClientInfo clientInfo);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
