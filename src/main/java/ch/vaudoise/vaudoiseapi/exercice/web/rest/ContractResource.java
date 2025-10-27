package ch.vaudoise.vaudoiseapi.exercice.web.rest;

import ch.vaudoise.vaudoiseapi.exercice.repository.ContractRepository;
import ch.vaudoise.vaudoiseapi.exercice.service.ContractService;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ContractDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ch.vaudoise.vaudoiseapi.exercice.domain.Contract}.
 */
@RestController
@RequestMapping("/api/contracts")
public class ContractResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContractResource.class);

    private static final String ENTITY_NAME = "contract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractService contractService;

    private final ContractRepository contractRepository;

    public ContractResource(ContractService contractService, ContractRepository contractRepository) {
        this.contractService = contractService;
        this.contractRepository = contractRepository;
    }

    /**
     * {@code POST  /contracts} : Create a new contract.
     *
     * @param contractDTO the contractDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractDTO, or with status {@code 400 (Bad Request)} if the contract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<ContractDTO> createContract(@Valid @RequestBody ContractDTO contractDTO) throws URISyntaxException {
        LOG.debug("REST request to save Contract : {}", contractDTO);

        if (contractDTO.getId() != null) {
            throw new BadRequestAlertException("A new contract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contractDTO = contractService.save(contractDTO);

        return ResponseEntity.created(new URI("/api/contracts/" + contractDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, contractDTO.getId().toString()))
            .body(contractDTO);
    }

    /**
     * {@code PUT  /contracts} : Updates an existing contract.
     *
     * @param contractDTO the contractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractDTO,
     * or with status {@code 400 (Bad Request)} if the contractDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping
    public ResponseEntity<ContractDTO> updateContract(@Valid @RequestBody ContractDTO contractDTO) throws URISyntaxException {
        LOG.debug("REST request to update Contract : {}", contractDTO);

        if (contractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!contractRepository.existsById(contractDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contractDTO = contractService.update(contractDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contractDTO.getId().toString()))
            .body(contractDTO);
    }

    /**
     * {@code PATCH  /contracts} : Partial updates given fields of an existing contract, field will ignore if it is null
     *
     * @param contractDTO the contractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractDTO,
     * or with status {@code 400 (Bad Request)} if the contractDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contractDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractDTO> partialUpdateContract(@NotNull @RequestBody ContractDTO contractDTO) throws URISyntaxException {
        LOG.debug("REST request to partial update Contract partially : {}", contractDTO);

        if (contractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!contractRepository.existsById(contractDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractDTO> result = contractService.partialUpdate(contractDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contractDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contracts} : get all the contracts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contracts in body.
     */
    @GetMapping
    public ResponseEntity<List<ContractDTO>> getAllContracts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Contracts");
        Page<ContractDTO> page = contractService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Handles HTTP GET requests to retrieve all active contracts for a given company.
     * <p>
     * This endpoint returns a list of {@link ContractDTO} objects representing the active
     * contracts associated with the specified company. The results are paginated internally
     * using the provided {@link Pageable} parameter, but only the content of the current page
     * is returned in the response body.
     * </p>
     *
     * @param companyId the unique identifier of the company whose active contracts should be retrieved
     * @param pageable  pagination information (page number, size, and sorting options) used to query the contracts
     * @return a {@link ResponseEntity} containing a list of {@link ContractDTO} for the active contracts
     *         of the specified company, with HTTP status 200 (OK)
     * @throws IllegalArgumentException if {@code companyId} is {@code null}
     */
    @GetMapping("/company/{companyId}/active")
    public ResponseEntity<List<ContractDTO>> getActiveContractsByCompanyId(
        @PathVariable UUID companyId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        Page<ContractDTO> page = contractService.findActiveByCompanyId(companyId, pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * Handles HTTP GET requests to retrieve all active contracts for a given person.
     * <p>
     * This endpoint returns a list of {@link ContractDTO} objects representing the active
     * contracts associated with the specified person. The results are paginated internally
     * using the provided {@link Pageable} parameter, but only the content of the current page
     * is returned in the response body.
     * </p>
     *
     * @param personId the unique identifier of the company whose active contracts should be retrieved
     * @param pageable  pagination information (page number, size, and sorting options) used to query the contracts
     * @return a {@link ResponseEntity} containing a list of {@link ContractDTO} for the active contracts
     *         of the specified person, with HTTP status 200 (OK)
     * @throws IllegalArgumentException if {@code companyId} is {@code null}
     */
    @GetMapping("/company/{companyId}/active")
    public ResponseEntity<List<ContractDTO>> getActiveContractsByPersonId(
        @PathVariable UUID personId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        Page<ContractDTO> page = contractService.findActiveByCompanyId(personId, pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /contracts/:id} : get the "id" contract.
     *
     * @param id the id of the contractDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractDTO> getContract(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Contract : {}", id);
        Optional<ContractDTO> contractDTO = contractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractDTO);
    }

    /**
     * {@code DELETE  /contracts/:id} : delete the "id" contract.
     *
     * @param id the id of the contractDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Contract : {}", id);
        contractService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
