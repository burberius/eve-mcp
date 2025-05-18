package net.troja.eve.mcp.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.UniverseApi;
import net.troja.eve.esi.model.SystemJumpsResponse;
import net.troja.eve.esi.model.SystemKillsResponse;
import net.troja.eve.mcp.db.model.Constellation;
import net.troja.eve.mcp.db.model.Region;
import net.troja.eve.mcp.db.model.SolarSystem;
import net.troja.eve.mcp.db.repository.ConstellationsRepository;
import net.troja.eve.mcp.db.repository.FactionsRepository;
import net.troja.eve.mcp.db.repository.RegionsRepository;
import net.troja.eve.mcp.db.repository.SolarSystemsRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.troja.eve.mcp.McpServerApplication.DATASOURCE;
import static net.troja.eve.mcp.service.EveJumpService.SECURITY_LEVEL_FORMATTER;

@Service
@Slf4j
@RequiredArgsConstructor
public class SolarSystemService {
    private final SolarSystemsRepository solarSystemsRepository;
    private final FactionsRepository factionsRepository;
    private final RegionsRepository regionsRepository;
    private final ConstellationsRepository constellationsRepository;
    private final UniverseApi universeApi;

    private final Map<Integer, SystemData> solarSystemData = new HashMap<>();

    @Tool(description = "What security level is the given security value")
    public String getSecurityValue(double security) {
        if (security < 0.1) {
            return "null security";
        } else if (security < 0.5) {
            return "low security";
        } else {
            return "high security";
        }
    }

    @Tool(description = "Get information about a solar system like security level, jumps and kills")
    public String getSolarSystemInformation(String system) {
        Optional<SolarSystem> solarSystem = solarSystemsRepository.findBySolarSystemName(system);
        if(solarSystem.isEmpty()) {
            return "The system " + system + " does not exist";
        }

        SolarSystem solarSystemInfo = solarSystem.get();
        SystemData systemData = solarSystemData.get(solarSystemInfo.getSolarSystemID());
        return "Solar system " +
                solarSystemInfo.getSolarSystemName() +
                " is in the constellation " +
                getConstellationName(solarSystemInfo.getConstellationID()) +
                " and belongs to region " +
                getRegionName(solarSystemInfo.getRegionID()) +
                ".\nIt has a security level of " + SECURITY_LEVEL_FORMATTER.format(solarSystemInfo.getSecurity()) +
                ", is in " + getSecurityValue(solarSystemInfo.getSecurity())+ " space and belongs to " +
                getFactionName(solarSystemInfo.getFactionID()) + ".\n" +
                "There were " + systemData.getJumps() + " jumps, " + systemData.getNpcKills() +
                " NPC kills, " + systemData.getShipKills() + " ship kills and " + systemData.getPodKills() +
                " pod kills in the last hour.";
    }

    @Scheduled(fixedRate = 3600 * 1000, initialDelay = 5000)
    void updateJumpsAndKills() {
        try {
            List<SystemJumpsResponse> universeSystemJumps = universeApi.getUniverseSystemJumps(DATASOURCE, null);
            universeSystemJumps.forEach(systemJumps -> solarSystemData.computeIfAbsent(systemJumps.getSystemId(), SystemData::new).setJumps(systemJumps.getShipJumps()));
            log.info("Updated {} jump entries", universeSystemJumps.size());
            List<SystemKillsResponse> universeSystemKills = universeApi.getUniverseSystemKills(DATASOURCE, null);
            universeSystemKills.forEach(systemKills -> {
                SystemData systemData = solarSystemData.computeIfAbsent(systemKills.getSystemId(), SystemData::new);
                systemData.setNpcKills(systemKills.getNpcKills());
                systemData.setShipKills(systemKills.getShipKills());
                systemData.setPodKills(systemKills.getPodKills());
            });
            log.info("Updated {} kill entries", universeSystemKills.size());
        } catch (ApiException e) {
            log.warn("Could not update jumps and kills information for solar systems", e);
        }
    }

    private String getConstellationName(final int constellationId) {
        return constellationsRepository.findById(constellationId).map(Constellation::getConstellationName)
                .orElse("Unknown");
    }

    private String getFactionName(final int factionId) {
        return factionsRepository.findById(factionId).map(net.troja.eve.mcp.db.model.Faction::getFactionName)
                .orElse("Unknown");
    }
    
    private String getRegionName(final int regionId) {
        return regionsRepository.findById(regionId).map(Region::getRegionName).orElse("Unknown");
    }

    @Data
    private static class SystemData {
        private int jumps;
        private int npcKills;
        private int shipKills;
        private int podKills;

        public SystemData(int jumps) {
            this.jumps = jumps;
        }
    }
}
