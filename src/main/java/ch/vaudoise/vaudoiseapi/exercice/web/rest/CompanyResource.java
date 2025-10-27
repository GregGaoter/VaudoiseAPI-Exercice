package ch.vaudoise.vaudoiseapi.exercice.web.rest;

import ch.vaudoise.vaudoiseapi.exercice.repository.CompanyRepository;
import ch.vaudoise.vaudoiseapi.exercice.service.CompanyService;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.CompanyDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.CompanyUpdateDTO;
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
import org.springdoc.core.annotations.ParameterObject;
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
 * REST controller for managing {@link ch.vaudoise.vaudoiseapi.exercice.domain.Company}.
 */
@RestController
@RequestMapping("/api/companies")
public class CompanyResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyResource.class);

    private static final String ENTITY_NAME = "company";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyService companyService;

    private final CompanyRepository companyRepository;

    public CompanyResource(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }

    /**
     * {@code POST  /companies} : Create a new company.
     *
     * @param companyDTO the companyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyDTO, or with status {@code 400 (Bad Request)} if the company has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO companyDTO) throws URISyntaxException {
        LOG.debug("REST request to save Company : {}", companyDTO);

        if (companyDTO.getId() != null) {
            throw new BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (companyDTO.getClientInfo().getId() != null) {
            throw new BadRequestAlertException("A new clientInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }

        companyDTO = companyService.save(companyDTO);

        return ResponseEntity.created(new URI("/api/companies/" + companyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, companyDTO.getId().toString()))
            .body(companyDTO);
    }

    /**
     * {@code PUT  /companies} : Updates an existing company.
     *
     * @param companyDTO the companyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDTO,
     * or with status {@code 400 (Bad Request)} if the companyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping
    public ResponseEntity<CompanyDTO> updateCompany(@Valid @RequestBody CompanyUpdateDTO companyUpdateDTO) throws URISyntaxException {
        LOG.debug("REST request to update Company : {}", companyUpdateDTO);

        if (companyUpdateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!companyRepository.existsById(companyUpdateDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyDTO companyDTO = companyService.update(companyUpdateDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, companyDTO.getId().toString()))
            .body(companyDTO);
    }

    /**
     * {@code PATCH  /companies} : Partial updates given fields of an existing company, field will ignore if it is null
     *
     * @param companyUpdateDTO the companyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDTO,
     * or with status {@code 400 (Bad Request)} if the companyUpdateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompanyDTO> partialUpdateCompany(@NotNull @RequestBody CompanyUpdateDTO companyUpdateDTO)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Company partially : {}", companyUpdateDTO);

        if (companyUpdateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!companyRepository.existsById(companyUpdateDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyDTO> result = companyService.partialUpdate(companyUpdateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, companyUpdateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /companies} : get all the companies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companies in body.
     */
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies(@ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Companies");
        Page<CompanyDTO> page = companyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /companies/:id} : get the "id" company.
     *
     * @param id the id of the companyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Company : {}", id);
        Optional<CompanyDTO> companyDTO = companyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyDTO);
    }

    /**
     * {@code DELETE  /companies/:id} : delete the "id" company.
     *
     * @param id the id of the companyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Company : {}", id);
        companyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
