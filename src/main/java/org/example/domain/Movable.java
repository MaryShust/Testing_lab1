package org.example.domain;

import lombok.NonNull;

public interface Movable {
    void moveTo(@NonNull Location dest);
    Location getLocation();
}