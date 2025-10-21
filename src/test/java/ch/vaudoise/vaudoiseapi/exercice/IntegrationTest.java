package ch.vaudoise.vaudoiseapi.exercice;

import ch.vaudoise.vaudoiseapi.exercice.config.AsyncSyncConfiguration;
import ch.vaudoise.vaudoiseapi.exercice.config.EmbeddedSQL;
import ch.vaudoise.vaudoiseapi.exercice.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { VaudoiseapiApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
