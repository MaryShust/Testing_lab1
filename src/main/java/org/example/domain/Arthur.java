package org.example.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Arthur implements Movable {
    @NonNull
    private Location location;

    @Setter(AccessLevel.NONE)
    private Location destination;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    private boolean isGliding = false;

    public void glideTo(@NonNull Window target) {
        if (target.getLocation() == null) {
            throw new IllegalStateException("Window has no location");
        }
        this.destination = target.getLocation();
        this.isGliding = true;
    }

    @Override
    public void moveTo(@NonNull Location dest) {
        this.location = dest;
    }

    public void stop() {
        this.isGliding = false;
        this.destination = null;
    }
}