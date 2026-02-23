package org.example.domain;

import lombok.*;

/**
 * Оратор - выступающий перед толпой
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Speaker {
    @NonNull
    private String name;
    @NonNull private Location location;
    @Setter(AccessLevel.NONE)
    private String currentSpeech;

    public void speak(@NonNull String speech) {
        this.currentSpeech = speech;
    }

    public void stop() {
        this.currentSpeech = null;
    }

    public boolean isSpeaking() {
        return currentSpeech != null;
    }
}