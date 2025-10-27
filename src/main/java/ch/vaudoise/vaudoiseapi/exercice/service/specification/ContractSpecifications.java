package ch.vaudoise.vaudoiseapi.exercice.service.specification;

import ch.vaudoise.vaudoiseapi.exercice.domain.Company_;
import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
import ch.vaudoise.vaudoiseapi.exercice.domain.Contract_;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public class ContractSpecifications {

    public static Specification<Contract> hasCompanyId(UUID companyId) {
        return (root, query, cb) -> cb.equal(root.get(Contract_.COMPANY).get(Company_.ID), companyId);
    }

    public static Specification<Contract> isActive() {
        return (root, query, cb) -> cb.greaterThan(root.get(Contract_.END_DATE), Instant.now());
    }

    public static Specification<Contract> updatedAfter(Instant fromDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Contract_.UPDATE_DATE), fromDate);
    }

    public static Specification<Contract> updatedBefore(Instant toDate) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Contract_.UPDATE_DATE), toDate);
    }

    public static Specification<Contract> updatedBetween(Instant fromDate, Instant toDate) {
        return (root, query, cb) -> cb.between(root.get(Contract_.UPDATE_DATE), fromDate, toDate);
    }
}
