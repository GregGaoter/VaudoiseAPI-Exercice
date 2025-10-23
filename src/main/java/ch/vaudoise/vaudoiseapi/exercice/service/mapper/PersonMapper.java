package ch.vaudoise.vaudoiseapi.exercice.service.mapper;

import ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo;
import ch.vaudoise.vaudoiseapi.exercice.domain.Person;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ClientInfoDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.PersonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {
    @Mapping(target = "clientInfo", source = "clientInfo", qualifiedByName = "clientInfoId")
    PersonDTO toDto(Person s);

    @Named("clientInfoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientInfoDTO toDtoClientInfoId(ClientInfo clientInfo);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
