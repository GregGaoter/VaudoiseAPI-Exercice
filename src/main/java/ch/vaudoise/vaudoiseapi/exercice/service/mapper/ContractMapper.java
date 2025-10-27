package ch.vaudoise.vaudoiseapi.exercice.service.mapper;

import ch.vaudoise.vaudoiseapi.exercice.domain.Company;
import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
import ch.vaudoise.vaudoiseapi.exercice.domain.Person;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ContractDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contract} and its DTO {@link ContractDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonMapper.class, CompanyMapper.class })
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {
    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "companyId", source = "company.id")
    ContractDTO toDto(Contract s);

    @Mapping(target = "person", source = "personId")
    @Mapping(target = "company", source = "companyId")
    Contract toEntity(ContractDTO dto);

    Contract updateContractFromDto(ContractDTO dto, @MappingTarget Contract entity);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    default Person fromPersonId(UUID id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.setId(id);
        return person;
    }

    default Company fromCompanyId(UUID id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
