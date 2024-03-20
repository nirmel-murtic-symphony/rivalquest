package is.symphony.rivalquest;

import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RivalQuestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RivalQuestApplication.class, args);
    }

    @Bean
    public EventStorageEngine eventStorageEngine() {
        // In production real event storage should be used
        return new InMemoryEventStorageEngine();
    }
}
