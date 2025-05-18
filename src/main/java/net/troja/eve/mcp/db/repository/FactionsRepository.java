package net.troja.eve.mcp.db.repository;

import net.troja.eve.mcp.db.model.Faction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactionsRepository extends CrudRepository<Faction, Integer> {
}
