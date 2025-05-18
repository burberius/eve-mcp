package net.troja.eve.mcp.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "mapConstellations")
@NoArgsConstructor
@AllArgsConstructor
public class Constellation {
    @Id
    private Integer constellationID;
    private String constellationName;
}
