package org.example.domain;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Scene {
    @NonNull
    private Crowd crowd;
    @NonNull private Speaker speaker;
    @NonNull private Building building;
    @NonNull private Platform platform;
    @NonNull private Arthur arthur;

    @Builder.Default
    private LocalDateTime sceneTime = LocalDateTime.now();

    public void crowdCheering() {
        crowd.cheer();
    }

    public boolean isArthurGlidingToSecondFloor() {
        return arthur.isGliding() &&
                arthur.getDestination() != null &&
                building.findWindowOnFloor(2)
                        .map(Window::getLocation)
                        .map(loc -> loc.equals(arthur.getDestination()))
                        .orElse(false);
    }
}