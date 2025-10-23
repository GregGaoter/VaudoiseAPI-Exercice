package ch.vaudoise.vaudoiseapi.exercice.domain;

import static ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfoTestSamples.*;
import static ch.vaudoise.vaudoiseapi.exercice.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ch.vaudoise.vaudoiseapi.exercice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Person.class);
        Person person1 = getPersonSample1();
        Person person2 = new Person();
        assertThat(person1).isNotEqualTo(person2);

        person2.setId(person1.getId());
        assertThat(person1).isEqualTo(person2);

        person2 = getPersonSample2();
        assertThat(person1).isNotEqualTo(person2);
    }

    @Test
    void clientInfoTest() {
        Person person = getPersonRandomSampleGenerator();
        ClientInfo clientInfoBack = getClientInfoRandomSampleGenerator();

        person.setClientInfo(clientInfoBack);
        assertThat(person.getClientInfo()).isEqualTo(clientInfoBack);

        person.clientInfo(null);
        assertThat(person.getClientInfo()).isNull();
    }
}
