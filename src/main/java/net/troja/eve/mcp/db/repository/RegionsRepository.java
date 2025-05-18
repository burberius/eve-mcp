package net.troja.eve.mcp.db.repository;

import net.troja.eve.mcp.db.model.Region;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionsRepository extends CrudRepository<Region, Integer> {
}
