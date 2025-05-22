package net.troja.eve.mcp.service;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static net.troja.eve.mcp.McpServerApplication.DATASOURCE;
import static net.troja.eve.mcp.db.repository.SolarSystemsRepositoryTest.SOLAR_SYSTEM_ID_JITA;
import static net.troja.eve.mcp.db.repository.SolarSystemsRepositoryTest.SOLAR_SYSTE_NAME_JITA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolarSystemServiceTest {
    @Mock
    private SolarSystemsRepository solarSystemsRepository;
    @Mock
    private FactionsRepository factionsRepository;
    @Mock
    private ConstellationsRepository constellationsRepository;
    @Mock
    private RegionsRepository regionsRepository;
    @Mock
    private UniverseApi universeApi;
    @InjectMocks
    private SolarSystemService classToTest;

    @ParameterizedTest
    @CsvSource({
            "0.0,null security",
            "0.1,low security",
            "0.5,high security",
            "1.0,high security"
    })
    void getSecurityValue(double security, String expected) {
        String actual = classToTest.getSecurityValue(security);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getSolarSystemInformation() throws ApiException {
        SolarSystem jita = new SolarSystem(SOLAR_SYSTEM_ID_JITA, SOLAR_SYSTE_NAME_JITA);
        jita.setSecurity(0.945913116665);
        jita.setConstellationID(20000026);
        jita.setFactionID(500001);
        jita.setRegionID(10000002);
        when(solarSystemsRepository.findBySolarSystemName(SOLAR_SYSTE_NAME_JITA)).thenReturn(Optional.of(jita));
        when(constellationsRepository.findById(20000026)).thenReturn(Optional.of(new Constellation(20000026, "Kimotoro")));
        when(regionsRepository.findById(10000002)).thenReturn(Optional.of(new Region(10000002, "The Forge")));
        when(factionsRepository.findById(500001)).thenReturn(Optional.of(new net.troja.eve.mcp.db.model.Faction(500001, "Caldari State")));
        prepareJumpsAndKills();

        String result = classToTest.getSolarSystemInformation(SOLAR_SYSTE_NAME_JITA);

        assertThat(result).isEqualTo("""
                Solar system Jita is in the constellation Kimotoro and belongs to region The Forge.
                It has a security level of 0.9, is in high security space and belongs to Caldari State.
                There were 5 jumps, 1234 NPC kills, 3 ship kills and 2 pod kills in the last hour.""");
    }

    private void prepareJumpsAndKills() throws ApiException {
        SystemJumpsResponse systemJumpsResponse = new SystemJumpsResponse();
        systemJumpsResponse.setSystemId(SOLAR_SYSTEM_ID_JITA);
        systemJumpsResponse.setShipJumps(5);
        when(universeApi.getUniverseSystemJumps(DATASOURCE, null)).thenReturn(List.of(systemJumpsResponse));
        SystemKillsResponse systemKillsResponse = new SystemKillsResponse();
        systemKillsResponse.setSystemId(SOLAR_SYSTEM_ID_JITA);
        systemKillsResponse.setNpcKills(1234);
        systemKillsResponse.setPodKills(2);
        systemKillsResponse.setShipKills(3);
        when(universeApi.getUniverseSystemKills(DATASOURCE, null)).thenReturn(List.of(systemKillsResponse));
        classToTest.updateJumpsAndKills();
    }
}