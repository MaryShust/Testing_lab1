package org.example.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Window {
    @NonNull private String id;
    private int floor;
    private boolean isMagnificent;
    private Location location;

    public Window(@NonNull String id, int floor, boolean isMagnificent) {
        if (floor < 1) {
            throw new IllegalArgumentException("Floor must be positive");
        }
        this.id = id;
        this.floor = floor;
        this.isMagnificent = isMagnificent;
    }
}
