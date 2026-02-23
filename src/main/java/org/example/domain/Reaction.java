package org.example.domain;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Reaction {
    @NonNull private PublicSpeakingScene.ReactionType type;
    private int intensity;

    // Явный конструктор с параметрами
    public Reaction(PublicSpeakingScene.ReactionType type, int intensity) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (intensity < 0 || intensity > 100) {
            throw new IllegalArgumentException("Intensity must be 0-100");
        }
        this.type = type;
        this.intensity = intensity;
    }
}