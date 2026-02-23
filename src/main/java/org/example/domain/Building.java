package org.example.domain;

import lombok.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Здание с этажами и окнами
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Building {
    @NonNull
    private String name;

    @Builder.Default
    private final List<Window> windows = new ArrayList<>();

    public void addWindow(@NonNull Window w) {
        windows.add(w);
    }

    public void clearWindow() {
        windows.clear();
    }

    public Optional<Window> findWindowOnFloor(int floor) {
        return windows.stream()
                .filter(w -> w.getFloor() == floor)
                .findFirst();
    }

    public List<Window> getMagnificentWindows() {
        return windows.stream()
                .filter(Window::isMagnificent)
                .collect(Collectors.toList());
    }

    public List<Window> getWindows() {
        return Collections.unmodifiableList(windows);
    }
}