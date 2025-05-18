package net.troja.eve.mcp.db.repository;

import net.troja.eve.mcp.db.model.SolarSystem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SolarSystemsRepositoryTest {
    public static final int SOLAR_SYSTEM_ID_JITA = 30000142;
    public static final String SOLAR_SYSTE_NAME_JITA = "Jita";

    @Autowired
    private SolarSystemsRepository solarSystemsRepository;

    @Test
    void findJitaById() {
        Optional<SolarSystem> jita = solarSystemsRepository.findById(SOLAR_SYSTEM_ID_JITA);

        assertThat(jita).isPresent();
        assertThat(jita.get().getSolarSystemName()).isEqualTo(SOLAR_SYSTE_NAME_JITA);
    }

    @Test
    void findJitaByName() {
        Optional<SolarSystem> jita = solarSystemsRepository.findBySolarSystemName(SOLAR_SYSTE_NAME_JITA);

        assertThat(jita).isPresent();
        assertThat(jita.get().getSolarSystemID()).isEqualTo(SOLAR_SYSTEM_ID_JITA);
    }
}