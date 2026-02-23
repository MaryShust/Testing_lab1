package org.example.domain;

import lombok.*;

/**
 * Отдельный человек
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @NonNull
    private String id;
    @NonNull private Location location;
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private boolean isCheering = false;

    public void cheer() {
        this.isCheering = true;
    }

    public void move(@NonNull Location newLoc) {
        this.location = newLoc;
    }
}