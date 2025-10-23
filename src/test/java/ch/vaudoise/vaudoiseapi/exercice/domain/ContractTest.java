package ch.vaudoise.vaudoiseapi.exercice.domain;

import static ch.vaudoise.vaudoiseapi.exercice.domain.CompanyTestSamples.*;
import static ch.vaudoise.vaudoiseapi.exercice.domain.ContractTestSamples.*;
import static ch.vaudoise.vaudoiseapi.exercice.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ch.vaudoise.vaudoiseapi.exercice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = getContractSample1();
        Contract contract2 = new Contract();
        assertThat(contract1).isNotEqualTo(contract2);

        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);

        contract2 = getContractSample2();
        assertThat(contract1).isNotEqualTo(contract2);
    }

    @Test
    void personTest() {
        Contract contract = getContractRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        contract.setPerson(personBack);
        assertThat(contract.getPerson()).isEqualTo(personBack);

        contract.person(null);
        assertThat(contract.getPerson()).isNull();
    }

    @Test
    void companyTest() {
        Contract contract = getContractRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        contract.setCompany(companyBack);
        assertThat(contract.getCompany()).isEqualTo(companyBack);

        contract.company(null);
        assertThat(contract.getCompany()).isNull();
    }
}
