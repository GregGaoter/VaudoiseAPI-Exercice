package ch.vaudoise.vaudoiseapi.exercice.domain;

import java.util.UUID;

public class CompanyTestSamples {

    public static Company getCompanySample1() {
        return new Company().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).companyIdentifier("companyIdentifier1");
    }

    public static Company getCompanySample2() {
        return new Company().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).companyIdentifier("companyIdentifier2");
    }

    public static Company getCompanyRandomSampleGenerator() {
        return new Company().id(UUID.randomUUID()).companyIdentifier(UUID.randomUUID().toString());
    }
}
