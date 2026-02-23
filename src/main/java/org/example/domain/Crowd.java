package org.example.domain;

import lombok.*;
import org.example.domain.PublicSpeakingScene.Mood;
import org.example.domain.PublicSpeakingScene.SoundLevel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Толпа - совокупность людей, собравшихся вместе
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Crowd {
    @Builder.Default
    private final List<Person> people = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @Builder.Default
    private Mood mood = Mood.NEUTRAL;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    private SoundLevel sound = SoundLevel.SILENCE;

    public void addPerson(@NonNull Person p) {
        people.add(p);
    }

    public void cheer() {
        this.mood = Mood.EXULTANT;
        this.sound = SoundLevel.LOUD;
        people.forEach(Person::cheer);
    }

    public int size() {
        return people.size();
    }

    public boolean isCheering() {
        return mood == Mood.EXULTANT && sound == SoundLevel.LOUD;
    }

    public List<Person> getPeople() {
        return Collections.unmodifiableList(people);
    }
}