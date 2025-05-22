package net.troja.eve.mcp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.RoutesApi;
import net.troja.eve.mcp.db.model.SolarSystem;
import net.troja.eve.mcp.db.repository.SolarSystemsRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static net.troja.eve.mcp.ApiExceptionHandler.handleApiException;
import static net.troja.eve.mcp.McpServerApplication.DATASOURCE;

@Service
@RequiredArgsConstructor
@Slf4j
public class EveJumpService {
    public static final DecimalFormat SECURITY_LEVEL_FORMATTER = new DecimalFormat("#.#",
            DecimalFormatSymbols.getInstance(Locale.US));
    private final RoutesApi routesApi = new RoutesApi();

    private final SolarSystemsRepository solarSystemsRepository;

    @Tool(description = "Get secure jump route from source to destination")
    public String getJumpInformationSecure(String source, String destination) {
        return getJumpInformation(source, destination, "secure");
    }

    @Tool(description = "Get shortes/fastest jump route from source to destination")
    public String getJumpInformationShort(String source, String destination) {
        return getJumpInformation(source, destination, "shortest");
    }

    @Tool(description = "Get insecure jump route from source to destination")
    public String getJumpInformationInsecure(String source, String destination) {
        return getJumpInformation(source, destination, "insecure");
    }

    private String getJumpInformation(final String source, final String destination, final String routeSecurity) {
        Optional<SolarSystem> sourceSystem = solarSystemsRepository.findBySolarSystemName(source);
        if (sourceSystem.isEmpty()) {
            return "The system " + source + " does not exist";
        }
        Optional<SolarSystem> destinationSystem = solarSystemsRepository.findBySolarSystemName(destination);
        if (destinationSystem.isEmpty()) {
            return "The system " + destination + " does not exist";
        }

        try {
            List<Integer> systems = routesApi.getRouteOriginDestination(destinationSystem.get().getSolarSystemID(),
                    sourceSystem.get().getSolarSystemID(), null, null, DATASOURCE, routeSecurity, null);
            return formatJumpInformation(source, destination, systems, routeSecurity);
        } catch (ApiException e) {
            return handleApiException(e,
                    String.format("Could not get jump information from source %s to destination %s", source,
                            destination));
        }
    }

    private String formatJumpInformation(final String source, final String destination, List<Integer> systems,
                                         final String routeSecurity) {
        StringBuilder result = new StringBuilder();
        result.append("The ").append(routeSecurity).append(" route from ")
                .append(source)
                .append(" is ")
                .append(systems.size())
                .append(" jumps from ")
                .append(destination).append(":\n");
        systems.forEach(systemId -> result.append("* ").append(getSolarSystemName(systemId)).append("\n"));
        return result.toString();
    }

    private String getSolarSystemName(final int solarSystemId) {
        return solarSystemsRepository.findById(solarSystemId).map(solarSystem ->
                        solarSystem.getSolarSystemName() + " (" + SECURITY_LEVEL_FORMATTER.format(solarSystem.getSecurity()) +
                                ")")
                .orElse(Integer.toString(solarSystemId));
    }
}
