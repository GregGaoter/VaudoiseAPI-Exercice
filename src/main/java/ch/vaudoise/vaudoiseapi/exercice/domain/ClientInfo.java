package ch.vaudoise.vaudoiseapi.exercice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * A ClientInfo.
 */
@Entity
@Table(name = "client_info")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Column(name = "update_date", nullable = false)
    private Instant updateDate;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Pattern(regexp = "^(?:\\+41|0041|0)(?:\\s?\\(0\\))?\\s?[1-9]{2}\\s?[0-9]{3}\\s?[0-9]{2}\\s?[0-9]{2}$")
    @Column(name = "phone")
    private String phone;

    @JsonIgnoreProperties(value = { "clientInfo" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "clientInfo")
    private Person person;

    @JsonIgnoreProperties(value = { "clientInfo" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "clientInfo")
    private Company company;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.creationDate = now;
        this.updateDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = Instant.now();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ClientInfo id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public ClientInfo creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public ClientInfo updateDate(Instant updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getName() {
        return this.name;
    }

    public ClientInfo name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public ClientInfo email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public ClientInfo phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        if (this.person != null) {
            this.person.setClientInfo(null);
        }
        if (person != null) {
            person.setClientInfo(this);
        }
        this.person = person;
    }

    public ClientInfo person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        if (this.company != null) {
            this.company.setClientInfo(null);
        }
        if (company != null) {
            company.setClientInfo(this);
        }
        this.company = company;
    }

    public ClientInfo company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientInfo)) {
            return false;
        }
        return getId() != null && getId().equals(((ClientInfo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientInfo{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
