package ch.vaudoise.vaudoiseapi.exercice.domain;

import static ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfoTestSamples.*;
import static ch.vaudoise.vaudoiseapi.exercice.domain.CompanyTestSamples.*;
import static ch.vaudoise.vaudoiseapi.exercice.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ch.vaudoise.vaudoiseapi.exercice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientInfo.class);
        ClientInfo clientInfo1 = getClientInfoSample1();
        ClientInfo clientInfo2 = new ClientInfo();
        assertThat(clientInfo1).isNotEqualTo(clientInfo2);

        clientInfo2.setId(clientInfo1.getId());
        assertThat(clientInfo1).isEqualTo(clientInfo2);

        clientInfo2 = getClientInfoSample2();
        assertThat(clientInfo1).isNotEqualTo(clientInfo2);
    }

    @Test
    void personTest() {
        ClientInfo clientInfo = getClientInfoRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        clientInfo.setPerson(personBack);
        assertThat(clientInfo.getPerson()).isEqualTo(personBack);
        assertThat(personBack.getClientInfo()).isEqualTo(clientInfo);

        clientInfo.person(null);
        assertThat(clientInfo.getPerson()).isNull();
        assertThat(personBack.getClientInfo()).isNull();
    }

    @Test
    void companyTest() {
        ClientInfo clientInfo = getClientInfoRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        clientInfo.setCompany(companyBack);
        assertThat(clientInfo.getCompany()).isEqualTo(companyBack);
        assertThat(companyBack.getClientInfo()).isEqualTo(clientInfo);

        clientInfo.company(null);
        assertThat(clientInfo.getCompany()).isNull();
        assertThat(companyBack.getClientInfo()).isNull();
    }
}
