package net.troja.eve.mcp;

import net.troja.eve.esi.api.IncursionsApi;
import net.troja.eve.esi.api.UniverseApi;
import net.troja.eve.mcp.service.EveIncursionService;
import net.troja.eve.mcp.service.EveJumpService;
import net.troja.eve.mcp.service.SolarSystemService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfiguration {
    @Bean
    public ToolCallbackProvider mcpTools(EveJumpService eveJumpService, EveIncursionService incursionService, SolarSystemService solarSystemService) {
        return MethodToolCallbackProvider.builder().toolObjects(eveJumpService, incursionService, solarSystemService).build();
    }

    @Bean
    public IncursionsApi incursionsApi() {
        return new IncursionsApi();
    }

    @Bean
    public UniverseApi universeApi() {
        return new UniverseApi();
    }
}
