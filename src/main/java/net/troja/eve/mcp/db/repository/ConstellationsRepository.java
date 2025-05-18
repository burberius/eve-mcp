package net.troja.eve.mcp.db.repository;

import net.troja.eve.mcp.db.model.Constellation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstellationsRepository extends CrudRepository<Constellation, Integer> {
}
