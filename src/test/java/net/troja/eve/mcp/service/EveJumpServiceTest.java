package net.troja.eve.mcp.service;

import net.troja.eve.mcp.db.model.SolarSystem;
import net.troja.eve.mcp.db.repository.SolarSystemsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static net.troja.eve.mcp.db.repository.SolarSystemsRepositoryTest.SOLAR_SYSTEM_ID_JITA;
import static net.troja.eve.mcp.db.repository.SolarSystemsRepositoryTest.SOLAR_SYSTE_NAME_JITA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EveJumpServiceTest {
    public static final int SOLAR_SYSTEM_ID_HEK = 30002053;
    public static final String SOLAR_SYSTE_NAME_HEK = "Hek";
    public static final int SOLAR_SYSTEM_ID_WH = 31000511;
    public static final String SOLAR_SYSTE_NAME_WH = "J125721";
    public static final String SOLAR_SYSTE_NAME_UNKNOWN = "Unknown";

    @Mock
    private SolarSystemsRepository solarSystemsRepository;
    @InjectMocks
    private EveJumpService classToTest;

    @Test
    void jitaToHek() {
        SolarSystem jita = new SolarSystem(SOLAR_SYSTEM_ID_JITA, SOLAR_SYSTE_NAME_JITA);
        jita.setSecurity(0.945913116665);
        when(solarSystemsRepository.findBySolarSystemName(SOLAR_SYSTE_NAME_JITA)).thenReturn(Optional.of(jita));
        when(solarSystemsRepository.findById(SOLAR_SYSTEM_ID_JITA)).thenReturn(Optional.of(jita));
        SolarSystem hek = new SolarSystem();
        hek.setSolarSystemID(SOLAR_SYSTEM_ID_HEK);
        when(solarSystemsRepository.findBySolarSystemName(SOLAR_SYSTE_NAME_HEK)).thenReturn(Optional.of(hek));

        String jumpInformation = classToTest.getJumpInformationSecure(SOLAR_SYSTE_NAME_JITA, SOLAR_SYSTE_NAME_HEK);

        assertThat(jumpInformation).startsWith("The secure route from Jita is 20 jumps from Hek:\n* Jita (0.9)");
    }

    @Test
    void eveApiException() {
        SolarSystem jita = new SolarSystem();
        jita.setSolarSystemID(SOLAR_SYSTEM_ID_JITA);
        when(solarSystemsRepository.findBySolarSystemName(SOLAR_SYSTE_NAME_JITA)).thenReturn(Optional.of(jita));
        SolarSystem wh = new SolarSystem();
        wh.setSolarSystemID(SOLAR_SYSTEM_ID_WH);
        when(solarSystemsRepository.findBySolarSystemName(SOLAR_SYSTE_NAME_WH)).thenReturn(Optional.of(wh));

        String jumpInformation = classToTest.getJumpInformationSecure(SOLAR_SYSTE_NAME_JITA, SOLAR_SYSTE_NAME_WH);

        assertThat(jumpInformation).isEqualTo("No route found");
    }

    @Test
    void unknownSolarSystemSource() {
        when(solarSystemsRepository.findBySolarSystemName(SOLAR_SYSTE_NAME_UNKNOWN)).thenReturn(Optional.empty());

        String jumpInformation = classToTest.getJumpInformationSecure(SOLAR_SYSTE_NAME_UNKNOWN, SOLAR_SYSTE_NAME_WH);

        assertThat(jumpInformation).isEqualTo("The system Unknown does not exist");
    }

    @Test
    void unknownSolarSystemDestination() {
        SolarSystem jita = new SolarSystem();
        jita.setSolarSystemID(SOLAR_SYSTEM_ID_JITA);
        when(solarSystemsRepository.findBySolarSystemName(SOLAR_SYSTE_NAME_JITA)).thenReturn(Optional.of(jita));
        when(solarSystemsRepository.findBySolarSystemName(SOLAR_SYSTE_NAME_UNKNOWN)).thenReturn(Optional.empty());

        String jumpInformation = classToTest.getJumpInformationSecure(SOLAR_SYSTE_NAME_JITA, SOLAR_SYSTE_NAME_UNKNOWN);

        assertThat(jumpInformation).isEqualTo("The system Unknown does not exist");
    }
}