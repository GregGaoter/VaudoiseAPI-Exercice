package ch.vaudoise.vaudoiseapi.exercice.domain;

import java.util.UUID;

public class ClientInfoTestSamples {

    public static ClientInfo getClientInfoSample1() {
        return new ClientInfo().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).name("name1").email("email1").phone("phone1");
    }

    public static ClientInfo getClientInfoSample2() {
        return new ClientInfo().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).name("name2").email("email2").phone("phone2");
    }

    public static ClientInfo getClientInfoRandomSampleGenerator() {
        return new ClientInfo()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString());
    }
}
