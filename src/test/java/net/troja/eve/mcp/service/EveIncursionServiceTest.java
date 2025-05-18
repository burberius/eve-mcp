package net.troja.eve.mcp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.IncursionsApi;
import net.troja.eve.esi.model.IncursionsResponse;
import net.troja.eve.mcp.db.model.Constellation;
import net.troja.eve.mcp.db.model.Faction;
import net.troja.eve.mcp.db.model.SolarSystem;
import net.troja.eve.mcp.db.repository.ConstellationsRepository;
import net.troja.eve.mcp.db.repository.FactionsRepository;
import net.troja.eve.mcp.db.repository.SolarSystemsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static net.troja.eve.mcp.McpServerApplication.DATASOURCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EveIncursionServiceTest {
    @Mock
    private FactionsRepository factionsRepository;
    @Mock
    private ConstellationsRepository constellationsRepository;
    @Mock
    private SolarSystemsRepository systemsRepository;
    @Mock
    private IncursionsApi incursionsApi;
    @InjectMocks
    private EveIncursionService eveIncursionService;

    @Test
    void getIncursions() throws ApiException, JsonProcessingException {
        when(incursionsApi.getIncursions(DATASOURCE, null)).thenReturn(List.of(getIncursion()));
        when(constellationsRepository.findById(20000244)).thenReturn(Optional.of(new Constellation(20000244, "Somi")));
        when(factionsRepository.findById(500019)).thenReturn(Optional.of(new Faction(500019, "Sansha's Nation")));
        when(systemsRepository.findById(30001650)).thenReturn(Optional.of(new SolarSystem(30001650, "Riramia")));
        when(systemsRepository.findById(30001651)).thenReturn(Optional.of(new SolarSystem(30001651, "Nafomeh")));
        when(systemsRepository.findById(30001652)).thenReturn(Optional.of(new SolarSystem(30001652, "Pimsu")));
        when(systemsRepository.findById(30001653)).thenReturn(Optional.of(new SolarSystem(30001653, "Jarzalad")));
        when(systemsRepository.findById(30001654)).thenReturn(Optional.of(new SolarSystem(30001654, "Matyas")));
        when(systemsRepository.findById(30001655)).thenReturn(Optional.of(new SolarSystem(30001655, "Imeshasa")));

        String information = eveIncursionService.getIncursionsInformation();

        assertThat(information).isEqualTo("""
        Incursion in constellation Somi against faction Sansha's Nation has been established and the boss is present.
        Staging system is Matyas and the influence is 1.0.
        Infested solar systems are:
        * Riramia
        * Nafomeh
        * Pimsu
        * Jarzalad
        * Matyas
        * Imeshasa""");
    }

    private IncursionsResponse getIncursion() throws JsonProcessingException {
        JsonMapper jsonMapper = new JsonMapper();
        return jsonMapper.readValue("{\"constellationId\":20000244,\"factionId\":500019,\"hasBoss\":true,\"infestedSolarSystems\":[30001650,30001651,30001652,30001653,30001654,30001655],\"influence\":1.0,\"stagingSolarSystemId\":30001654,\"state\":\"ESTABLISHED\",\"type\":\"Incursion\",\"stateString\":\"established\"}",
                IncursionsResponse.class);
    }
}