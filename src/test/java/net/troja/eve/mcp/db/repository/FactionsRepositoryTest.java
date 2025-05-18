package net.troja.eve.mcp.db.repository;

import net.troja.eve.mcp.db.model.Faction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FactionsRepositoryTest {
    @Autowired
    private FactionsRepository factionsRepository;

    @Test
    void findGallente() {
        Optional<Faction> gallente = factionsRepository.findById(500004);

        assertThat(gallente).isPresent();
        assertThat(gallente.get().getFactionName()).isEqualTo("Gallente Federation");
    }
}