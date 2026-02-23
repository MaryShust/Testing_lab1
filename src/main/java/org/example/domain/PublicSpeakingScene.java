package org.example.domain;

/**
 * Доменная модель для сцены публичного выступления перед толпой
 */
public class PublicSpeakingScene {

    // ---------- Перечисления ----------
    public enum Mood {
        NEUTRAL, EXULTANT
    }

    public enum SoundLevel {
        SILENCE, LOUD
    }

    public enum MovementType {
        WALKING, GLIDING
    }

    public enum ReactionType {
        CHEERING, APPLAUSE, BOOING
    }
}
