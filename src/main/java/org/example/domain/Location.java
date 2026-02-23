package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

/**
 * Локация в пространстве
 */
@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class Location {
    @Getter double x;
    @Getter double y;
    @Getter double z;

    public double distanceTo(Location other) {
        return Math.sqrt(Math.pow(x - other.x, 2) +
                Math.pow(y - other.y, 2) +
                Math.pow(z - other.z, 2));
    }
}