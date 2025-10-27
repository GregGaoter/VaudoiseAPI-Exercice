package ch.vaudoise.vaudoiseapi.exercice.service;

import ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo;
import ch.vaudoise.vaudoiseapi.exercice.domain.Company;
import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
import ch.vaudoise.vaudoiseapi.exercice.repository.ClientInfoRepository;
import ch.vaudoise.vaudoiseapi.exercice.repository.CompanyRepository;
import ch.vaudoise.vaudoiseapi.exercice.repository.ContractRepository;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.CompanyDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.CompanyUpdateDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.mapper.CompanyMapper;
import ch.vaudoise.vaudoiseapi.exercice.service.mapper.CompanyUpdateMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
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

    private final ContractRepository contractRepository;

    private final ClientInfoRepository clientInfoRepository;

    public CompanyService(
        CompanyRepository companyRepository,
        CompanyMapper companyMapper,
        CompanyUpdateMapper companyUpdateMapper,
        ContractRepository contractRepository,
        ClientInfoRepository clientInfoRepository
    ) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.companyUpdateMapper = companyUpdateMapper;
        this.contractRepository = contractRepository;
        this.clientInfoRepository = clientInfoRepository;
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
     * Deactivates a company and its associated entities.
     *
     * This method performs the following operations:
     * 1. Deactivates all contracts associated with the company by setting their end date to current time
     * 2. Deactivates the company's client information by setting active status to false
     *
     * @param id The UUID of the company to be deactivated
     * @throws RuntimeException if any database operation fails
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete Company : {}", id);

        Instant now = Instant.now();

        // Deactivate contracts
        List<Contract> contracts = contractRepository.findByCompanyId(id);
        contracts.forEach(contract -> contract.setEndDate(now));
        contractRepository.saveAll(contracts);

        // Deactivate client info
        companyRepository
            .findById(id)
            .ifPresent(company -> {
                ClientInfo clientInfo = company.getClientInfo();
                clientInfo.setActive(false);
                clientInfoRepository.save(clientInfo);
            });
    }
}
