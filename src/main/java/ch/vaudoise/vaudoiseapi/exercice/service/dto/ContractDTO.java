package ch.vaudoise.vaudoiseapi.exercice.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ch.vaudoise.vaudoiseapi.exercice.domain.Contract} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractDTO implements Serializable {

    private UUID id;

    @NotNull
    private Instant creationDate;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Instant updateDate;

    @NotNull
    private Instant startDate;

    private Instant endDate;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal costAmount;

    private PersonDTO person;

    private CompanyDTO company;

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

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractDTO)) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractDTO{" +
            "id='" + getId() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", costAmount=" + getCostAmount() +
            ", person=" + getPerson() +
            ", company=" + getCompany() +
            "}";
    }
}
