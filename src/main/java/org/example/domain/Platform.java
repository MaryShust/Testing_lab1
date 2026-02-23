package org.example.domain;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Platform {
    @NonNull Location location;

    @Setter
    @Builder.Default
    Speaker speaker = null;

    public void placeSpeaker(@NonNull Speaker s) {
        this.speaker = s;
    }

    public void removeSpeaker() {
        this.speaker = null;
    }

    public boolean hasSpeaker() {
        return speaker != null;
    }
}