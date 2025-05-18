package net.troja.eve.mcp.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "mapSolarSystems")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SolarSystem {
    private Integer regionID;
    private Integer constellationID;
    @Id
    private Integer solarSystemID;
    private String solarSystemName;
    private Double security;
    private Integer factionID;

    public SolarSystem(Integer solarSystemID, String solarSystemName) {
        this.solarSystemID = solarSystemID;
        this.solarSystemName = solarSystemName;
    }
}
