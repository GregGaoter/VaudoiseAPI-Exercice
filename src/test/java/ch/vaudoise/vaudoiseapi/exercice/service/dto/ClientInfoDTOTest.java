package ch.vaudoise.vaudoiseapi.exercice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.vaudoise.vaudoiseapi.exercice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ClientInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientInfoDTO.class);
        ClientInfoDTO clientInfoDTO1 = new ClientInfoDTO();
        clientInfoDTO1.setId(UUID.randomUUID());
        ClientInfoDTO clientInfoDTO2 = new ClientInfoDTO();
        assertThat(clientInfoDTO1).isNotEqualTo(clientInfoDTO2);
        clientInfoDTO2.setId(clientInfoDTO1.getId());
        assertThat(clientInfoDTO1).isEqualTo(clientInfoDTO2);
        clientInfoDTO2.setId(UUID.randomUUID());
        assertThat(clientInfoDTO1).isNotEqualTo(clientInfoDTO2);
        clientInfoDTO1.setId(null);
        assertThat(clientInfoDTO1).isNotEqualTo(clientInfoDTO2);
    }
}
