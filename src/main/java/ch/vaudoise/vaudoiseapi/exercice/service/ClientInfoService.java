package ch.vaudoise.vaudoiseapi.exercice.service;

import ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo;
import ch.vaudoise.vaudoiseapi.exercice.repository.ClientInfoRepository;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ClientInfoDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.mapper.ClientInfoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo}.
 */
@Service
@Transactional
public class ClientInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientInfoService.class);

    private final ClientInfoRepository clientInfoRepository;

    private final ClientInfoMapper clientInfoMapper;

    public ClientInfoService(ClientInfoRepository clientInfoRepository, ClientInfoMapper clientInfoMapper) {
        this.clientInfoRepository = clientInfoRepository;
        this.clientInfoMapper = clientInfoMapper;
    }

    /**
     * Save a clientInfo.
     *
     * @param clientInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public ClientInfoDTO save(ClientInfoDTO clientInfoDTO) {
        LOG.debug("Request to save ClientInfo : {}", clientInfoDTO);
        ClientInfo clientInfo = clientInfoMapper.toEntity(clientInfoDTO);
        clientInfo = clientInfoRepository.save(clientInfo);
        return clientInfoMapper.toDto(clientInfo);
    }

    /**
     * Update a clientInfo.
     *
     * @param clientInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public ClientInfoDTO update(ClientInfoDTO clientInfoDTO) {
        LOG.debug("Request to update ClientInfo : {}", clientInfoDTO);
        ClientInfo clientInfo = clientInfoMapper.toEntity(clientInfoDTO);
        clientInfo = clientInfoRepository.save(clientInfo);
        return clientInfoMapper.toDto(clientInfo);
    }

    /**
     * Partially update a clientInfo.
     *
     * @param clientInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClientInfoDTO> partialUpdate(ClientInfoDTO clientInfoDTO) {
        LOG.debug("Request to partially update ClientInfo : {}", clientInfoDTO);

        return clientInfoRepository
            .findById(clientInfoDTO.getId())
            .map(existingClientInfo -> {
                clientInfoMapper.partialUpdate(existingClientInfo, clientInfoDTO);

                return existingClientInfo;
            })
            .map(clientInfoRepository::save)
            .map(clientInfoMapper::toDto);
    }

    /**
     * Get all the clientInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClientInfoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ClientInfos");
        return clientInfoRepository.findAll(pageable).map(clientInfoMapper::toDto);
    }

    /**
     *  Get all the clientInfos where Person is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClientInfoDTO> findAllWherePersonIsNull() {
        LOG.debug("Request to get all clientInfos where Person is null");
        return StreamSupport.stream(clientInfoRepository.findAll().spliterator(), false)
            .filter(clientInfo -> clientInfo.getPerson() == null)
            .map(clientInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the clientInfos where Company is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClientInfoDTO> findAllWhereCompanyIsNull() {
        LOG.debug("Request to get all clientInfos where Company is null");
        return StreamSupport.stream(clientInfoRepository.findAll().spliterator(), false)
            .filter(clientInfo -> clientInfo.getCompany() == null)
            .map(clientInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one clientInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClientInfoDTO> findOne(UUID id) {
        LOG.debug("Request to get ClientInfo : {}", id);
        return clientInfoRepository.findById(id).map(clientInfoMapper::toDto);
    }

    /**
     * Delete the clientInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete ClientInfo : {}", id);
        clientInfoRepository.deleteById(id);
    }
}
