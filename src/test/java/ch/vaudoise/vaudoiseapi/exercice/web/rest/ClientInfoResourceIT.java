package ch.vaudoise.vaudoiseapi.exercice.web.rest;

import static ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfoAsserts.*;
import static ch.vaudoise.vaudoiseapi.exercice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.vaudoise.vaudoiseapi.exercice.IntegrationTest;
import ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo;
import ch.vaudoise.vaudoiseapi.exercice.repository.ClientInfoRepository;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ClientInfoDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.mapper.ClientInfoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ClientInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientInfoResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "0 53 335 4694";
    private static final String UPDATED_PHONE = "0041 910310650";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/client-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientInfoRepository clientInfoRepository;

    @Autowired
    private ClientInfoMapper clientInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientInfoMockMvc;

    private ClientInfo clientInfo;

    private ClientInfo insertedClientInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientInfo createEntity() {
        return new ClientInfo()
            .creationDate(DEFAULT_CREATION_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientInfo createUpdatedEntity() {
        return new ClientInfo()
            .creationDate(UPDATED_CREATION_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        clientInfo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedClientInfo != null) {
            clientInfoRepository.delete(insertedClientInfo);
            insertedClientInfo = null;
        }
    }

    @Test
    @Transactional
    void createClientInfo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClientInfo
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);
        var returnedClientInfoDTO = om.readValue(
            restClientInfoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientInfoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientInfoDTO.class
        );

        // Validate the ClientInfo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClientInfo = clientInfoMapper.toEntity(returnedClientInfoDTO);
        assertClientInfoUpdatableFieldsEquals(returnedClientInfo, getPersistedClientInfo(returnedClientInfo));

        insertedClientInfo = returnedClientInfo;
    }

    @Test
    @Transactional
    void createClientInfoWithExistingId() throws Exception {
        // Create the ClientInfo with an existing ID
        insertedClientInfo = clientInfoRepository.saveAndFlush(clientInfo);
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientInfoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientInfo.setCreationDate(null);

        // Create the ClientInfo, which fails.
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        restClientInfoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdateDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientInfo.setUpdateDate(null);

        // Create the ClientInfo, which fails.
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        restClientInfoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientInfo.setName(null);

        // Create the ClientInfo, which fails.
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        restClientInfoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientInfo.setActive(null);

        // Create the ClientInfo, which fails.
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        restClientInfoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientInfos() throws Exception {
        // Initialize the database
        insertedClientInfo = clientInfoRepository.saveAndFlush(clientInfo);

        // Get all the clientInfoList
        restClientInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientInfo.getId().toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getClientInfo() throws Exception {
        // Initialize the database
        insertedClientInfo = clientInfoRepository.saveAndFlush(clientInfo);

        // Get the clientInfo
        restClientInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, clientInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientInfo.getId().toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingClientInfo() throws Exception {
        // Get the clientInfo
        restClientInfoMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClientInfo() throws Exception {
        // Initialize the database
        insertedClientInfo = clientInfoRepository.saveAndFlush(clientInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientInfo
        ClientInfo updatedClientInfo = clientInfoRepository.findById(clientInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClientInfo are not directly saved in db
        em.detach(updatedClientInfo);
        updatedClientInfo
            .creationDate(UPDATED_CREATION_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .active(UPDATED_ACTIVE);
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(updatedClientInfo);

        restClientInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientInfoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientInfoToMatchAllProperties(updatedClientInfo);
    }

    @Test
    @Transactional
    void putNonExistingClientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientInfo.setId(UUID.randomUUID());

        // Create the ClientInfo
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientInfoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientInfo.setId(UUID.randomUUID());

        // Create the ClientInfo
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientInfo.setId(UUID.randomUUID());

        // Create the ClientInfo
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientInfoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientInfoWithPatch() throws Exception {
        // Initialize the database
        insertedClientInfo = clientInfoRepository.saveAndFlush(clientInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientInfo using partial update
        ClientInfo partialUpdatedClientInfo = new ClientInfo();
        partialUpdatedClientInfo.setId(clientInfo.getId());

        partialUpdatedClientInfo.creationDate(UPDATED_CREATION_DATE).active(UPDATED_ACTIVE);

        restClientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientInfo))
            )
            .andExpect(status().isOk());

        // Validate the ClientInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientInfoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClientInfo, clientInfo),
            getPersistedClientInfo(clientInfo)
        );
    }

    @Test
    @Transactional
    void fullUpdateClientInfoWithPatch() throws Exception {
        // Initialize the database
        insertedClientInfo = clientInfoRepository.saveAndFlush(clientInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientInfo using partial update
        ClientInfo partialUpdatedClientInfo = new ClientInfo();
        partialUpdatedClientInfo.setId(clientInfo.getId());

        partialUpdatedClientInfo
            .creationDate(UPDATED_CREATION_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .active(UPDATED_ACTIVE);

        restClientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientInfo))
            )
            .andExpect(status().isOk());

        // Validate the ClientInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientInfoUpdatableFieldsEquals(partialUpdatedClientInfo, getPersistedClientInfo(partialUpdatedClientInfo));
    }

    @Test
    @Transactional
    void patchNonExistingClientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientInfo.setId(UUID.randomUUID());

        // Create the ClientInfo
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientInfoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientInfo.setId(UUID.randomUUID());

        // Create the ClientInfo
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientInfo.setId(UUID.randomUUID());

        // Create the ClientInfo
        ClientInfoDTO clientInfoDTO = clientInfoMapper.toDto(clientInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clientInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClientInfo() throws Exception {
        // Initialize the database
        insertedClientInfo = clientInfoRepository.saveAndFlush(clientInfo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the clientInfo
        restClientInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientInfo.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clientInfoRepository.count();
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

    protected ClientInfo getPersistedClientInfo(ClientInfo clientInfo) {
        return clientInfoRepository.findById(clientInfo.getId()).orElseThrow();
    }

    protected void assertPersistedClientInfoToMatchAllProperties(ClientInfo expectedClientInfo) {
        assertClientInfoAllPropertiesEquals(expectedClientInfo, getPersistedClientInfo(expectedClientInfo));
    }

    protected void assertPersistedClientInfoToMatchUpdatableProperties(ClientInfo expectedClientInfo) {
        assertClientInfoAllUpdatablePropertiesEquals(expectedClientInfo, getPersistedClientInfo(expectedClientInfo));
    }
}
