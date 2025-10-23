package ch.vaudoise.vaudoiseapi.exercice.service.mapper;

import ch.vaudoise.vaudoiseapi.exercice.domain.Company;
import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
import ch.vaudoise.vaudoiseapi.exercice.domain.Person;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.CompanyDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ContractDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.PersonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contract} and its DTO {@link ContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personId")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    ContractDTO toDto(Contract s);

    @Named("personId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonDTO toDtoPersonId(Person person);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
