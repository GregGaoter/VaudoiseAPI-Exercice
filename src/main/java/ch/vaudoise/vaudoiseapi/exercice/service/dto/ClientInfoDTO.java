package ch.vaudoise.vaudoiseapi.exercice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientInfoDTO implements Serializable {

    private UUID id;

    @NotNull
    private Instant creationDate;

    @NotNull
    private Instant updateDate;

    @NotNull
    private String name;

    private String email;

    @Pattern(regexp = "^(?:\\+41|0041|0)(?:\\s?\\(0\\))?\\s?[1-9]{2}\\s?[0-9]{3}\\s?[0-9]{2}\\s?[0-9]{2}$")
    private String phone;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientInfoDTO)) {
            return false;
        }

        ClientInfoDTO clientInfoDTO = (ClientInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientInfoDTO{" +
            "id='" + getId() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
