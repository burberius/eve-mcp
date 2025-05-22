package net.troja.eve.mcp.service;

import lombok.RequiredArgsConstructor;
import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.IncursionsApi;
import net.troja.eve.esi.model.IncursionsResponse;
import net.troja.eve.mcp.db.model.Constellation;
import net.troja.eve.mcp.db.repository.ConstellationsRepository;
import net.troja.eve.mcp.db.repository.FactionsRepository;
import net.troja.eve.mcp.db.repository.SolarSystemsRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.troja.eve.mcp.ApiExceptionHandler.handleApiException;
import static net.troja.eve.mcp.McpServerApplication.DATASOURCE;
import static net.troja.eve.mcp.service.SolarSystemService.VALUE_UNKNOWN;

@Service
@RequiredArgsConstructor
public class EveIncursionService {
    private final FactionsRepository factionsRepository;
    private final ConstellationsRepository constellationsRepository;
    private final SolarSystemsRepository systemsRepository;
    private final IncursionsApi incursionsApi;

    @Tool(description = "Get information about all active incursions")
    public String getIncursionsInformation() {
        try {
            List<IncursionsResponse> incursions = incursionsApi.getIncursions(DATASOURCE, null);
            StringBuilder buffer = new StringBuilder();
            incursions.forEach(incursion -> handleIncursion(incursion, buffer));
            return buffer.toString().trim();
        } catch (ApiException e) {
            return handleApiException(e, "Could not get incursion information");
        }
    }

    private void handleIncursion(final IncursionsResponse incursion, final StringBuilder buffer) {
        buffer.append("Incursion in constellation ").append(getConstellationName(incursion.getConstellationId()))
                .append(" against faction ").append(getFactionName(incursion.getFactionId()))
                .append(" has been ").append(incursion.getStateString())
                .append(" and the boss is ").append(incursion.getHasBoss() ? "present" : "not present")
                .append(".\n")
                .append("Staging system is ").append(getSolarSystemName(incursion.getStagingSolarSystemId()))
                .append(" and the influence is ").append(incursion.getInfluence()).append(".\n")
                .append("Infested solar systems are:\n");
        incursion.getInfestedSolarSystems().forEach(systemId ->
                buffer.append("* ").append(getSolarSystemName(systemId)).append("\n"));
        buffer.append("\n\n");
    }

    private String getConstellationName(final int constellationId) {
        return constellationsRepository.findById(constellationId).map(Constellation::getConstellationName)
                .orElse(VALUE_UNKNOWN);
    }

    private String getFactionName(final int factionId) {
        return factionsRepository.findById(factionId).map(net.troja.eve.mcp.db.model.Faction::getFactionName)
                .orElse(VALUE_UNKNOWN);
    }

    private String getSolarSystemName(final int solarSystemId) {
        return systemsRepository.findById(solarSystemId).map(net.troja.eve.mcp.db.model.SolarSystem::getSolarSystemName)
                .orElse(VALUE_UNKNOWN);
    }
}
