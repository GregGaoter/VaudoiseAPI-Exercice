package ch.vaudoise.vaudoiseapi.exercice.service.mapper;

import static ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfoAsserts.*;
import static ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientInfoMapperTest {

    private ClientInfoMapper clientInfoMapper;

    @BeforeEach
    void setUp() {
        clientInfoMapper = new ClientInfoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClientInfoSample1();
        var actual = clientInfoMapper.toEntity(clientInfoMapper.toDto(expected));
        assertClientInfoAllPropertiesEquals(expected, actual);
    }
}
