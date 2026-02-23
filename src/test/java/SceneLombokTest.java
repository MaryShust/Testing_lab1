import org.example.domain.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.example.domain.PublicSpeakingScene.*;

@DisplayName("Тесты публичного выступления с Lombok")
class SceneLombokTest {

    private Location ground;
    private Location platformLoc;
    private Location windowLoc;
    private Crowd crowd;
    private Speaker speaker;
    private Building building;
    private Platform platform;
    private Arthur arthur;
    private Scene scene;

    @BeforeEach
    void setUp() {
        ground = Location.builder().x(0).y(0).z(0).build();
        platformLoc = Location.builder().x(10).y(0).z(0).build();
        windowLoc = Location.builder().x(10).y(5).z(5).build();

        crowd = Crowd.builder().build();
        speaker = Speaker.builder()
                .name("Orator")
                .location(Location.builder().x(10).y(0).z(1).build())
                .build();
        building = Building.builder()
                .name("Great Hall")
                .build();
        platform = Platform.builder()
                .location(platformLoc)
                .build();
        arthur = Arthur.builder()
                .location(ground)
                .build();

        Window window = new Window("W1", 2, true);
        window.setLocation(windowLoc);
        building.addWindow(window);

        scene = Scene.builder()
                .crowd(crowd)
                .speaker(speaker)
                .building(building)
                .platform(platform)
                .arthur(arthur)
                .build();
    }

    // --------------------------------------------------------------------
    // ТЕСТЫ LOCATION
    // --------------------------------------------------------------------

    @Nested
    @DisplayName("Location тесты")
    class LocationTests {

        @Test
        @DisplayName("Создание через билдер")
        void createWithBuilder() {
            Location loc = Location.builder()
                    .x(1).y(2).z(3)
                    .build();

            assertAll(
                    () -> assertEquals(1, loc.getX()),
                    () -> assertEquals(2, loc.getY()),
                    () -> assertEquals(3, loc.getZ())
            );
        }

        @Test
        @DisplayName("Расстояние между точками")
        void distance() {
            Location a = new Location(0, 0, 0);
            Location b = new Location(3, 4, 0);
            assertEquals(5.0, a.distanceTo(b));
        }

        @Test
        @DisplayName("Иммутабельность")
        void immutability() {
            Location loc = new Location(1, 2, 3);
            assertEquals(1, loc.getX());
            assertEquals(2, loc.getY());
            assertEquals(3, loc.getZ());
        }
    }

    // --------------------------------------------------------------------
    // ТЕСТЫ PERSON
    // --------------------------------------------------------------------

    @Nested
    @DisplayName("Person тесты")
    class PersonTests {

        @Test
        @DisplayName("Создание через билдер")
        void createWithBuilder() {
            Person p = Person.builder()
                    .id("123")
                    .location(ground)
                    .build();

            assertAll(
                    () -> assertEquals("123", p.getId()),
                    () -> assertEquals(ground, p.getLocation()),
                    () -> assertFalse(p.isCheering())
            );
        }

        @Test
        @DisplayName("@NonNull валидация")
        void nonNullValidation() {
            assertThrows(NullPointerException.class,
                    () -> Person.builder().id(null).location(ground).build());
            assertThrows(NullPointerException.class,
                    () -> Person.builder().id("123").location(null).build());
        }

        @Test
        @DisplayName("Ликование")
        void cheer() {
            Person p = Person.builder()
                    .id("123")
                    .location(ground)
                    .build();

            p.cheer();
            assertTrue(p.isCheering());
        }

        @Test
        @DisplayName("Перемещение")
        void move() {
            Person p = Person.builder()
                    .id("123")
                    .location(ground)
                    .build();

            Location newLoc = new Location(5, 5, 0);
            p.move(newLoc);
            assertEquals(newLoc, p.getLocation());
        }
    }

    // --------------------------------------------------------------------
    // ТЕСТЫ CROWD
    // --------------------------------------------------------------------

    @Nested
    @DisplayName("Crowd тесты")
    class CrowdTests {

        @Test
        @DisplayName("Создание через билдер")
        void createWithBuilder() {
            Crowd c = Crowd.builder().build();
            assertEquals(0, c.size());
            assertFalse(c.isCheering());
        }

        @Test
        @DisplayName("Добавление людей")
        void addPeople() {
            Person p1 = Person.builder().id("1").location(ground).build();
            Person p2 = Person.builder().id("2").location(ground).build();

            crowd.addPerson(p1);
            crowd.addPerson(p2);

            assertEquals(2, crowd.size());
            assertTrue(crowd.getPeople().contains(p1));
        }

        @Test
        @DisplayName("Ликование толпы")
        void cheering() {
            Person p = Person.builder().id("1").location(ground).build();
            crowd.addPerson(p);
            crowd.cheer();

            assertTrue(crowd.isCheering());
            assertTrue(p.isCheering());
            assertEquals(Mood.EXULTANT, crowd.getMood());
            assertEquals(SoundLevel.LOUD, crowd.getSound());
        }
    }

    // --------------------------------------------------------------------
    // ТЕСТЫ SPEAKER
    // --------------------------------------------------------------------

    @Nested
    @DisplayName("Speaker тесты")
    class SpeakerTests {

        @Test
        @DisplayName("Создание через билдер")
        void createWithBuilder() {
            Speaker s = Speaker.builder()
                    .name("Test")
                    .location(ground)
                    .build();

            assertEquals("Test", s.getName());
            assertEquals(ground, s.getLocation());
            assertFalse(s.isSpeaking());
        }

        @Test
        @DisplayName("Речь")
        void speak() {
            speaker.speak("Hello!");
            assertTrue(speaker.isSpeaking());
            assertEquals("Hello!", speaker.getCurrentSpeech());
        }

        @Test
        @DisplayName("Окончание речи")
        void stop() {
            speaker.speak("Hello");
            speaker.stop();
            assertFalse(speaker.isSpeaking());
            assertNull(speaker.getCurrentSpeech());
        }
    }

    // --------------------------------------------------------------------
    // ТЕСТЫ REACTION - ИСПРАВЛЕНО
    // --------------------------------------------------------------------

    @Nested
    @DisplayName("Reaction тесты")
    class ReactionTests {

        @Test
        @DisplayName("Создание через билдер")
        void createWithBuilder() {
            Reaction r = Reaction.builder()
                    .type(ReactionType.CHEERING)
                    .intensity(75)
                    .build();

            assertEquals(ReactionType.CHEERING, r.getType());
            assertEquals(75, r.getIntensity());
        }

        @Test
        @DisplayName("Создание через конструктор")
        void createWithConstructor() {
            Reaction r = new Reaction(ReactionType.CHEERING, 75);
            assertEquals(ReactionType.CHEERING, r.getType());
            assertEquals(75, r.getIntensity());
        }

        @Test
        @DisplayName("Валидация интенсивности")
        void validateIntensity() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Reaction(ReactionType.CHEERING, -1));
            assertThrows(IllegalArgumentException.class,
                    () -> new Reaction(ReactionType.CHEERING, 101));
            assertDoesNotThrow(() -> new Reaction(ReactionType.CHEERING, 0));
            assertDoesNotThrow(() -> new Reaction(ReactionType.CHEERING, 100));
        }

        @Test
        @DisplayName("@NonNull валидация")
        void nonNullValidation() {
            assertThrows(NullPointerException.class,
                    () -> new Reaction(null, 50));
        }
    }

    // --------------------------------------------------------------------
    // ТЕСТЫ WINDOW - ИСПРАВЛЕНО
    // --------------------------------------------------------------------

    @Nested
    @DisplayName("Window тесты")
    class WindowTests {

        @Test
        @DisplayName("Создание через конструктор с валидацией")
        void createWithConstructor() {
            Window w = new Window("W1", 2, true);
            w.setLocation(windowLoc);

            assertAll(
                    () -> assertEquals("W1", w.getId()),
                    () -> assertEquals(2, w.getFloor()),
                    () -> assertTrue(w.isMagnificent())
            );
        }

        @Test
        @DisplayName("Валидация этажа")
        void floorValidation() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Window("W1", 0, true));
            assertThrows(IllegalArgumentException.class,
                    () -> new Window("W1", -1, true));
        }

        @Test
        @DisplayName("@NonNull валидация id")
        void idValidation() {
            assertThrows(NullPointerException.class,
                    () -> new Window(null, 1, true));
        }

        @Test
        @DisplayName("Создание через билдер")
        void createWithBuilder() {
            Window w = Window.builder()
                    .id("W1")
                    .floor(2)
                    .isMagnificent(true)
                    .location(windowLoc)
                    .build();

            assertEquals("W1", w.getId());
            assertEquals(2, w.getFloor());
            assertTrue(w.isMagnificent());
            assertEquals(windowLoc, w.getLocation());
        }
    }

    // --------------------------------------------------------------------
    // ТЕСТЫ BUILDING
    // --------------------------------------------------------------------

    @Nested
    @DisplayName("Building тесты")
    class BuildingTests {

        @Test
        @DisplayName("Добавление окон")
        void addWindows() {
            building.clearWindow();
            Window w1 = new Window("W1", 1, false);
            Window w2 = new Window("W2", 2, true);

            building.addWindow(w1);
            building.addWindow(w2);

            assertEquals(2, building.getWindows().size());
            assertTrue(building.findWindowOnFloor(2).isPresent());
            assertEquals(1, building.getMagnificentWindows().size());
        }

        @Test
        @DisplayName("Поиск окна")
        void findWindow() {
            assertTrue(building.findWindowOnFloor(2).isPresent());
            assertTrue(building.findWindowOnFloor(99).isEmpty());
        }
    }

    // --------------------------------------------------------------------
    // ТЕСТЫ PLATFORM - ИСПРАВЛЕНО
    // --------------------------------------------------------------------

    @Nested
    @DisplayName("Platform тесты")
    class PlatformTests {

        @Test
        @DisplayName("Создание через билдер")
        void createWithBuilder() {
            Platform p = Platform.builder()
                    .location(platformLoc)
                    .build();

            assertEquals(platformLoc, p.getLocation());
            assertFalse(p.hasSpeaker());
            assertNull(p.getSpeaker());
        }

        @Test
        @DisplayName("Размещение оратора")
        void placeSpeaker() {
            platform.placeSpeaker(speaker);
            assertTrue(platform.hasSpeaker());
            assertEquals(speaker, platform.getSpeaker());
        }

        @Test
        @DisplayName("Удаление оратора")
        void removeSpeaker() {
            platform.placeSpeaker(speaker);
            platform.removeSpeaker();
            assertFalse(platform.hasSpeaker());
            assertNull(platform.getSpeaker());
        }

        @Test
        @DisplayName("@NonNull валидация")
        void nonNullValidation() {
            assertThrows(NullPointerException.class,
                    () -> platform.placeSpeaker(null));
        }
    }

    // --------------------------------------------------------------------
    // ТЕСТЫ ARTHUR
    // --------------------------------------------------------------------

    @Nested
    @DisplayName("Arthur тесты")
    class ArthurTests {

        @Test
        @DisplayName("Создание через билдер")
        void createWithBuilder() {
            Arthur a = Arthur.builder()
                    .location(ground)
                    .build();

            assertEquals(ground, a.getLocation());
            assertFalse(a.isGliding());
            assertNull(a.getDestination());
        }

        @Test
        @DisplayName("Скольжение к окну")
        void glideToWindow() {
            Window window = building.findWindowOnFloor(2).get();
            arthur.glideTo(window);

            assertTrue(arthur.isGliding());
            assertEquals(window.getLocation(), arthur.getDestination());
        }

        @Test
        @DisplayName("Остановка")
        void stop() {
            Window window = building.findWindowOnFloor(2).get();
            arthur.glideTo(window);
            arthur.stop();

            assertFalse(arthur.isGliding());
            assertNull(arthur.getDestination());
        }

        @Test
        @DisplayName("@NonNull валидация")
        void nonNullValidation() {
            assertThrows(NullPointerException.class,
                    () -> Arthur.builder().location(null).build());
            assertThrows(NullPointerException.class,
                    () -> arthur.glideTo(null));
            assertThrows(NullPointerException.class,
                    () -> arthur.moveTo(null));
        }
    }

    // --------------------------------------------------------------------
    // ТЕСТЫ SCENE
    // --------------------------------------------------------------------

    @Nested
    @DisplayName("Scene тесты")
    class SceneTests {

        @Test
        @DisplayName("Создание через билдер")
        void createWithBuilder() {
            Scene s = Scene.builder()
                    .crowd(crowd)
                    .speaker(speaker)
                    .building(building)
                    .platform(platform)
                    .arthur(arthur)
                    .build();

            assertNotNull(s.getCrowd());
            assertNotNull(s.getSpeaker());
            assertNotNull(s.getBuilding());
            assertNotNull(s.getPlatform());
            assertNotNull(s.getArthur());
            assertNotNull(s.getSceneTime());
        }

        @Test
        @DisplayName("Ликование толпы")
        void crowdCheering() {
            scene.crowdCheering();
            assertTrue(scene.getCrowd().isCheering());
        }

        @Test
        @DisplayName("Артур скользит к окну на втором этаже")
        void arthurGlidingToSecondFloor() {
            Window window = building.findWindowOnFloor(2).get();
            arthur.glideTo(window);

            assertTrue(scene.isArthurGlidingToSecondFloor());
        }

        @Test
        @DisplayName("Валидация @NonNull")
        void nonNullValidation() {
            assertThrows(NullPointerException.class,
                    () -> Scene.builder()
                            .crowd(null)
                            .speaker(speaker)
                            .building(building)
                            .platform(platform)
                            .arthur(arthur)
                            .build());
        }
    }

    // --------------------------------------------------------------------
    // ИНТЕГРАЦИОННЫЙ ТЕСТ
    // --------------------------------------------------------------------

    @Test
    @DisplayName("Полная сцена")
    void fullScene() {
        // Добавляем людей в толпу
        for (int i = 0; i < 5; i++) {
            crowd.addPerson(Person.builder()
                    .id(String.valueOf(i))
                    .location(Location.builder()
                            .x(5 + i)
                            .y(0)
                            .z(0)
                            .build())
                    .build());
        }

        // Действие
        platform.placeSpeaker(speaker);
        speaker.speak("Свобода!");
        crowd.cheer();

        Window window = building.findWindowOnFloor(2).get();
        arthur.glideTo(window);

        // Проверки
        assertAll(
                () -> assertTrue(platform.hasSpeaker()),
                () -> assertTrue(speaker.isSpeaking()),
                () -> assertEquals("Свобода!", speaker.getCurrentSpeech()),
                () -> assertTrue(crowd.isCheering()),
                () -> assertEquals(5, crowd.size()),
                () -> assertTrue(crowd.getPeople().stream().allMatch(Person::isCheering)),
                () -> assertTrue(arthur.isGliding()),
                () -> assertEquals(windowLoc, arthur.getDestination()),
                () -> assertTrue(scene.isArthurGlidingToSecondFloor())
        );
    }


}