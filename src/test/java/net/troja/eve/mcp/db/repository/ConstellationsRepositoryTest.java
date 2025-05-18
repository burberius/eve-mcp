package net.troja.eve.mcp.db.repository;

import net.troja.eve.mcp.db.model.Constellation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ConstellationsRepositoryTest {
    @Autowired
    private ConstellationsRepository constellationsRepository;

    @Test
    void findOne() {
        Optional<Constellation> constellation = constellationsRepository.findById(20000026);

        assertThat(constellation).isPresent();
        assertThat(constellation.get().getConstellationName()).isEqualTo("Aulari");
    }
}