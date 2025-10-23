package ch.vaudoise.vaudoiseapi.exercice.service.mapper;

import static ch.vaudoise.vaudoiseapi.exercice.domain.PersonAsserts.*;
import static ch.vaudoise.vaudoiseapi.exercice.domain.PersonTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonMapperTest {

    private PersonMapper personMapper;

    @BeforeEach
    void setUp() {
        personMapper = new PersonMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPersonSample1();
        var actual = personMapper.toEntity(personMapper.toDto(expected));
        assertPersonAllPropertiesEquals(expected, actual);
    }
}
