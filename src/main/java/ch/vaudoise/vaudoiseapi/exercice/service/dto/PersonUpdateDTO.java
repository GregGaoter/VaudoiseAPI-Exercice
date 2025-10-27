package ch.vaudoise.vaudoiseapi.exercice.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ch.vaudoise.vaudoiseapi.exercice.domain.Person} entity.
 * The DTO intentionally omits the birthDate field that must not be changed during an update.
 */
public class PersonUpdateDTO implements Serializable {

    private UUID id;

    @NotNull
    private ClientInfoDTO clientInfo;

    // No birthDate for the update

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
        if (!(o instanceof PersonUpdateDTO)) {
            return false;
        }

        PersonUpdateDTO personUpdateDTO = (PersonUpdateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personUpdateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonDTO{" +
            "id='" + getId() + "'" +
            ", clientInfo=" + getClientInfo() +
            "}";
    }
}
