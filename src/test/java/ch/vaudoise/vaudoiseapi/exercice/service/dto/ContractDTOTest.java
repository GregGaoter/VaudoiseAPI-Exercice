package ch.vaudoise.vaudoiseapi.exercice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.vaudoise.vaudoiseapi.exercice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ContractDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractDTO.class);
        ContractDTO contractDTO1 = new ContractDTO();
        contractDTO1.setId(UUID.randomUUID());
        ContractDTO contractDTO2 = new ContractDTO();
        assertThat(contractDTO1).isNotEqualTo(contractDTO2);
        contractDTO2.setId(contractDTO1.getId());
        assertThat(contractDTO1).isEqualTo(contractDTO2);
        contractDTO2.setId(UUID.randomUUID());
        assertThat(contractDTO1).isNotEqualTo(contractDTO2);
        contractDTO1.setId(null);
        assertThat(contractDTO1).isNotEqualTo(contractDTO2);
    }
}
