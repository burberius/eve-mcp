package net.troja.eve.mcp.db.model;

import jakarta.annotation.security.DenyAll;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "mapRegions")
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    @Id
    private Integer regionID;
    private String regionName;
}
