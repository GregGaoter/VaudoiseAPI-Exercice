package ch.vaudoise.vaudoiseapi.exercice.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ch.vaudoise.vaudoiseapi.exercice.domain.Company} entity.
 * The DTO intentionally omits the company identifier field that must not be changed during an update.
 */
public class CompanyUpdateDTO implements Serializable {

    private UUID id;

    @NotNull
    private ClientInfoDTO clientInfo;

    // No companyIdentifier for the update

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ClientInfoDTO getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfoDTO clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyUpdateDTO)) {
            return false;
        }

        CompanyUpdateDTO companyUpdateDTO = (CompanyUpdateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyUpdateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id='" + getId() + "'" +
            ", clientInfo=" + getClientInfo() +
            "}";
    }
}
