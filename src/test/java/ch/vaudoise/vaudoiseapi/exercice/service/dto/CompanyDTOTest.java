package ch.vaudoise.vaudoiseapi.exercice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.vaudoise.vaudoiseapi.exercice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CompanyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyDTO.class);
        CompanyDTO companyDTO1 = new CompanyDTO();
        companyDTO1.setId(UUID.randomUUID());
        CompanyDTO companyDTO2 = new CompanyDTO();
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
        companyDTO2.setId(companyDTO1.getId());
        assertThat(companyDTO1).isEqualTo(companyDTO2);
        companyDTO2.setId(UUID.randomUUID());
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
        companyDTO1.setId(null);
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
    }
}
