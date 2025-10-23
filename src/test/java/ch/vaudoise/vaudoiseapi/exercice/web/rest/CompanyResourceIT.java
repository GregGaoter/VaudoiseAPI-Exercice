package ch.vaudoise.vaudoiseapi.exercice.web.rest;

import static ch.vaudoise.vaudoiseapi.exercice.domain.CompanyAsserts.*;
import static ch.vaudoise.vaudoiseapi.exercice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.vaudoise.vaudoiseapi.exercice.IntegrationTest;
import ch.vaudoise.vaudoiseapi.exercice.domain.Company;
import ch.vaudoise.vaudoiseapi.exercice.repository.CompanyRepository;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.CompanyDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.mapper.CompanyMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyResourceIT {

    private static final String DEFAULT_COMPANY_IDENTIFIER = "nzi-684";
    private static final String UPDATED_COMPANY_IDENTIFIER = "olp-544";

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    private Company insertedCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity() {
        return new Company().companyIdentifier(DEFAULT_COMPANY_IDENTIFIER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity() {
        return new Company().companyIdentifier(UPDATED_COMPANY_IDENTIFIER);
    }

    @BeforeEach
    void initTest() {
        company = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompany != null) {
            companyRepository.delete(insertedCompany);
            insertedCompany = null;
        }
    }

    @Test
    @Transactional
    void createCompany() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);
        var returnedCompanyDTO = om.readValue(
            restCompanyMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompanyDTO.class
        );

        // Validate the Company in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompany = companyMapper.toEntity(returnedCompanyDTO);
        assertCompanyUpdatableFieldsEquals(returnedCompany, getPersistedCompany(returnedCompany));

        insertedCompany = returnedCompany;
    }

    @Test
    @Transactional
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        insertedCompany = companyRepository.saveAndFlush(company);
        CompanyDTO companyDTO = companyMapper.toDto(company);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompanyIdentifierIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        company.setCompanyIdentifier(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().toString())))
            .andExpect(jsonPath("$.[*].companyIdentifier").value(hasItem(DEFAULT_COMPANY_IDENTIFIER)));
    }

    @Test
    @Transactional
    void getCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().toString()))
            .andExpect(jsonPath("$.companyIdentifier").value(DEFAULT_COMPANY_IDENTIFIER));
    }

    @Test
    @Transactional
    void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany.companyIdentifier(UPDATED_COMPANY_IDENTIFIER);
        CompanyDTO companyDTO = companyMapper.toDto(updatedCompany);

        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompanyToMatchAllProperties(updatedCompany);
    }

    @Test
    @Transactional
    void putNonExistingCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompany, company), getPersistedCompany(company));
    }

    @Test
    @Transactional
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany.companyIdentifier(UPDATED_COMPANY_IDENTIFIER);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyUpdatableFieldsEquals(partialUpdatedCompany, getPersistedCompany(partialUpdatedCompany));
    }

    @Test
    @Transactional
    void patchNonExistingCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(companyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(companyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, company.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return companyRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Company getPersistedCompany(Company company) {
        return companyRepository.findById(company.getId()).orElseThrow();
    }

    protected void assertPersistedCompanyToMatchAllProperties(Company expectedCompany) {
        assertCompanyAllPropertiesEquals(expectedCompany, getPersistedCompany(expectedCompany));
    }

    protected void assertPersistedCompanyToMatchUpdatableProperties(Company expectedCompany) {
        assertCompanyAllUpdatablePropertiesEquals(expectedCompany, getPersistedCompany(expectedCompany));
    }
}
