package ch.vaudoise.vaudoiseapi.exercice.service;

import ch.vaudoise.vaudoiseapi.exercice.domain.Company;
import ch.vaudoise.vaudoiseapi.exercice.repository.CompanyRepository;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.CompanyDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.CompanyUpdateDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.mapper.CompanyMapper;
import ch.vaudoise.vaudoiseapi.exercice.service.mapper.CompanyUpdateMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ch.vaudoise.vaudoiseapi.exercice.domain.Company}.
 */
@Service
@Transactional
public class CompanyService {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    private final CompanyUpdateMapper companyUpdateMapper;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper, CompanyUpdateMapper companyUpdateMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.companyUpdateMapper = companyUpdateMapper;
    }

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyDTO save(CompanyDTO companyDTO) {
        LOG.debug("Request to save Company : {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        company = companyRepository.save(company);
        return companyMapper.toDto(company);
    }

    /**
     * Update a company.
     *
     * @param companyUpdateDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyDTO update(CompanyUpdateDTO companyUpdateDTO) {
        LOG.debug("Request to update Company : {}", companyUpdateDTO);

        UUID id = companyUpdateDTO.getId();
        Company databaseCompany = companyRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Company with id %s not found", String.valueOf(id))));
        Company updatedCompany = companyUpdateMapper.updateCompanyFromDto(companyUpdateDTO, databaseCompany);
        Company persistedCompany = companyRepository.save(updatedCompany);

        return companyMapper.toDto(persistedCompany);
    }

    /**
     * Partially update a company.
     *
     * @param companyUpdateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyDTO> partialUpdate(CompanyUpdateDTO companyUpdateDTO) {
        LOG.debug("Request to partially update Company : {}", companyUpdateDTO);

        return companyRepository
            .findById(companyUpdateDTO.getId())
            .map(existingCompany -> {
                companyUpdateMapper.partialUpdate(existingCompany, companyUpdateDTO);

                return existingCompany;
            })
            .map(companyRepository::save)
            .map(companyMapper::toDto);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Companies");
        return companyRepository.findAll(pageable).map(companyMapper::toDto);
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyDTO> findOne(UUID id) {
        LOG.debug("Request to get Company : {}", id);
        return companyRepository.findById(id).map(companyMapper::toDto);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }
}
