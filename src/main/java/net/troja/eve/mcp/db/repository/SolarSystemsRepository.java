package net.troja.eve.mcp.db.repository;

import net.troja.eve.mcp.db.model.SolarSystem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolarSystemsRepository extends CrudRepository<SolarSystem, Integer> {
    Optional<SolarSystem> findBySolarSystemName(String name);
}
