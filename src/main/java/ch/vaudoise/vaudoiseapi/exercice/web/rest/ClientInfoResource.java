package ch.vaudoise.vaudoiseapi.exercice.web.rest;

import ch.vaudoise.vaudoiseapi.exercice.repository.ClientInfoRepository;
import ch.vaudoise.vaudoiseapi.exercice.service.ClientInfoService;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ClientInfoDTO;
import ch.vaudoise.vaudoiseapi.exercice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo}.
 */
@RestController
@RequestMapping("/api/client-infos")
public class ClientInfoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClientInfoResource.class);

    private static final String ENTITY_NAME = "clientInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientInfoService clientInfoService;

    private final ClientInfoRepository clientInfoRepository;

    public ClientInfoResource(ClientInfoService clientInfoService, ClientInfoRepository clientInfoRepository) {
        this.clientInfoService = clientInfoService;
        this.clientInfoRepository = clientInfoRepository;
    }

    /**
     * {@code POST  /client-infos} : Create a new clientInfo.
     *
     * @param clientInfoDTO the clientInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientInfoDTO, or with status {@code 400 (Bad Request)} if the clientInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<ClientInfoDTO> createClientInfo(@Valid @RequestBody ClientInfoDTO clientInfoDTO) throws URISyntaxException {
        LOG.debug("REST request to save ClientInfo : {}", clientInfoDTO);

        if (clientInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new clientInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }

        clientInfoDTO = clientInfoService.save(clientInfoDTO);

        return ResponseEntity.created(new URI("/api/client-infos/" + clientInfoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, clientInfoDTO.getId().toString()))
            .body(clientInfoDTO);
    }

    /**
     * {@code PUT  /client-infos} : Updates an existing clientInfo.
     *
     * @param clientInfoDTO the clientInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientInfoDTO,
     * or with status {@code 400 (Bad Request)} if the clientInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping
    public ResponseEntity<ClientInfoDTO> updateClientInfo(@Valid @RequestBody ClientInfoDTO clientInfoDTO) throws URISyntaxException {
        LOG.debug("REST request to update ClientInfo : {}", clientInfoDTO);

        if (clientInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!clientInfoRepository.existsById(clientInfoDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        clientInfoDTO = clientInfoService.update(clientInfoDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clientInfoDTO.getId().toString()))
            .body(clientInfoDTO);
    }

    /**
     * {@code PATCH  /client-infos} : Partial updates given fields of an existing clientInfo, field will ignore if it is null
     *
     * @param clientInfoDTO the clientInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientInfoDTO,
     * or with status {@code 400 (Bad Request)} if the clientInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clientInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClientInfoDTO> partialUpdateClientInfo(@NotNull @RequestBody ClientInfoDTO clientInfoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to partial update ClientInfo partially : {}", clientInfoDTO);

        if (clientInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!clientInfoRepository.existsById(clientInfoDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClientInfoDTO> result = clientInfoService.partialUpdate(clientInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clientInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /client-infos} : get all the clientInfos.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientInfos in body.
     */
    @GetMapping
    public ResponseEntity<List<ClientInfoDTO>> getAllClientInfos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("person-is-null".equals(filter)) {
            LOG.debug("REST request to get all ClientInfos where person is null");
            return new ResponseEntity<>(clientInfoService.findAllWherePersonIsNull(), HttpStatus.OK);
        }

        if ("company-is-null".equals(filter)) {
            LOG.debug("REST request to get all ClientInfos where company is null");
            return new ResponseEntity<>(clientInfoService.findAllWhereCompanyIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of ClientInfos");
        Page<ClientInfoDTO> page = clientInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /client-infos/:id} : get the "id" clientInfo.
     *
     * @param id the id of the clientInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientInfoDTO> getClientInfo(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get ClientInfo : {}", id);
        Optional<ClientInfoDTO> clientInfoDTO = clientInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientInfoDTO);
    }

    /**
     * {@code DELETE  /client-infos/:id} : delete the "id" clientInfo.
     *
     * @param id the id of the clientInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientInfo(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete ClientInfo : {}", id);
        clientInfoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
