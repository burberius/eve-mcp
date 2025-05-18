package net.troja.eve.mcp.db.repository;

import net.troja.eve.mcp.db.model.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RegionsRepositoryTest {
    @Autowired
    private RegionsRepository regionsRepository;

    @Test
    void findOne() {
        Optional<Region> region = regionsRepository.findById(10000002);

        assertThat(region).isPresent();
        assertThat(region.get().getRegionName()).isEqualTo("The Forge");
    }
}