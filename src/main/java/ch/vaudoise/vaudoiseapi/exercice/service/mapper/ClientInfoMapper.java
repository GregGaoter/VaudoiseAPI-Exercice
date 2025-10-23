package ch.vaudoise.vaudoiseapi.exercice.service.mapper;

import ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ClientInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClientInfo} and its DTO {@link ClientInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientInfoMapper extends EntityMapper<ClientInfoDTO, ClientInfo> {}
