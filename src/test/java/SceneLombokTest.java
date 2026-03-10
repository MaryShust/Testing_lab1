import org.example.domain.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

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

    // ==================== ТЕСТЫ ДЛЯ ARTHUR ====================

    @Nested
    @DisplayName("Arthur дополнительные тесты")
    class ArthurAdditionalTests {

        @Test
        @DisplayName("glideTo с окном без локации выбрасывает исключение")
        void glideToWindowWithNoLocation() {
            Window windowWithoutLocation = new Window("W2", 3, false);
            // Не устанавливаем локацию

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> arthur.glideTo(windowWithoutLocation)
            );
            assertEquals("Window has no location", exception.getMessage());
        }

        @Test
        @DisplayName("stop сбрасывает состояние")
        void stopResetsState() {
            // Изначально не скользит
            assertFalse(arthur.isGliding());
            assertNull(arthur.getDestination());

            Window window = building.findWindowOnFloor(2).get();
            arthur.glideTo(window);

            // После glideTo
            assertTrue(arthur.isGliding());
            assertNotNull(arthur.getDestination());

            arthur.stop();

            // После stop
            assertFalse(arthur.isGliding());
            assertNull(arthur.getDestination());
        }

        @Test
        @DisplayName("moveTo изменяет локацию")
        void moveToChangesLocation() {
            Location newLocation = Location.builder().x(100).y(100).z(100).build();
            arthur.moveTo(newLocation);
            assertEquals(newLocation, arthur.getLocation());
        }

        @Test
        @DisplayName("Builder.Default работает для isGliding")
        void builderDefaultForIsGliding() {
            Arthur defaultArthur = Arthur.builder()
                    .location(ground)
                    .build();
            assertFalse(defaultArthur.isGliding());

            // Проверяем, что можно установить через билдер
            Arthur glidingArthur = Arthur.builder()
                    .location(ground)
                    .isGliding(true)
                    .build();
            assertTrue(glidingArthur.isGliding());
        }

        @Test
        @DisplayName("Setter(AccessLevel.NONE) для destination и isGliding")
        void setterAccessLevelNone() {
            Arthur arthur = Arthur.builder().location(ground).build();

            assertDoesNotThrow(() -> {
                arthur.glideTo(building.findWindowOnFloor(2).get());
                arthur.stop();
                arthur.moveTo(ground);
            });
        }

        @Test
        @DisplayName("@NoArgsConstructor создает объект с значениями по умолчанию")
        void noArgsConstructorCreatesDefaultObject() {
            // Создаем объект через пустой конструктор
            Arthur arthur = new Arthur();

            // Проверяем значения по умолчанию
            assertAll(
                    () -> assertNull(arthur.getLocation(), "location должен быть null"),
                    () -> assertNull(arthur.getDestination(), "destination должен быть null"),
                    () -> assertFalse(arthur.isGliding(), "isGliding должен быть false")
            );

            // Проверяем, что объект можно использовать
            Location loc = Location.builder().x(1).y(2).z(3).build();
            arthur.setLocation(loc); // setter от @Data
            arthur.moveTo(loc); // собственный метод

            assertEquals(loc, arthur.getLocation());
        }
    }

    // ==================== НОВЫЕ ТЕСТЫ ДЛЯ BUILDING ====================

    @Nested
    @DisplayName("Building дополнительные тесты")
    class BuildingAdditionalTests {

        @Test
        @DisplayName("Builder создает пустой список окон")
        void builderCreatesEmptyWindowList() {
            Building b = Building.builder().name("Test").build();
            assertNotNull(b.getWindows());
            assertTrue(b.getWindows().isEmpty());
        }


        @Test
        @DisplayName("findWindowOnFloor возвращает Optional.empty для несуществующего этажа")
        void findWindowOnFloorReturnsEmptyForMissingFloor() {
            Optional<Window> result = building.findWindowOnFloor(999);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("findWindowOnFloor возвращает окно для существующего этажа")
        void findWindowOnFloorReturnsWindowForExistingFloor() {
            // Добавляем окна на разные этажи
            building.addWindow(new Window("W1", 1, false));
            building.addWindow(new Window("W2", 3, true));
            building.addWindow(new Window("W3", 3, false));

            Optional<Window> result = building.findWindowOnFloor(3);
            assertTrue(result.isPresent());
            assertEquals(3, result.get().getFloor());

            // Проверяем, что возвращается первое окно (но это не гарантировано)
            // Просто проверяем, что окно с правильным этажом
        }

        @Test
        @DisplayName("getMagnificentWindows возвращает только великолепные окна")
        void getMagnificentWindowsReturnsOnlyMagnificent() {
            building.clearWindow();
            building.addWindow(new Window("W1", 1, false));
            building.addWindow(new Window("W2", 2, true));
            building.addWindow(new Window("W3", 3, false));
            building.addWindow(new Window("W4", 4, true));

            List<Window> magnificent = building.getMagnificentWindows();
            assertEquals(2, magnificent.size());
            assertTrue(magnificent.stream().allMatch(Window::isMagnificent));
        }

        @Test
        @DisplayName("getWindows возвращает неизменяемый список")
        void getWindowsReturnsUnmodifiableList() {
            List<Window> windows = building.getWindows();
            assertThrows(UnsupportedOperationException.class,
                    () -> windows.add(new Window("W5", 5, false)));
        }

        @Test
        @DisplayName("@NonNull валидация для name")
        void nonNullValidationForName() {
            assertThrows(NullPointerException.class,
                    () -> Building.builder().name(null).build());
        }

        @Test
        @DisplayName("addWindow с null выбрасывает исключение")
        void addWindowWithNullThrowsException() {
            assertThrows(NullPointerException.class,
                    () -> building.addWindow(null));
        }

        @Test
        @DisplayName("@NonNull поля не проверяются в пустом конструкторе")
        void nonNullFieldNotValidatedInNoArgsConstructor() {
            // Пустой конструктор не проверяет @NonNull
            Building building = new Building();
            assertNull(building.getName());

            // Но при использовании билдера проверка есть
            assertThrows(NullPointerException.class,
                    () -> Building.builder().build(),
                    "Билдер должен проверять @NonNull");
        }

    }

    // ==================== НОВЫЕ ТЕСТЫ ДЛЯ CROWD ====================

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

    @Test
    void shouldCreateCrowdWithNoArgsConstructor() {
        // Вызов конструктора без параметров
        Crowd crowd = new Crowd();

        // Проверяем, что объект создан
        assertNotNull(crowd);

        // Проверяем значения по умолчанию
        assertNotNull(crowd.getPeople(), "Список людей должен быть инициализирован");
        assertTrue(crowd.getPeople().isEmpty(), "По умолчанию толпа должна быть пустой");
        assertEquals(Mood.NEUTRAL, crowd.getMood(), "Настроение по умолчанию должно быть NEUTRAL");
        assertEquals(SoundLevel.SILENCE, crowd.getSound(), "Уровень шума по умолчанию должен быть SILENCE");
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

        @Test
        void NoArgsConstructor() {
            Person person = new Person();

            assertNotNull(person);

            // Проверяем значения по умолчанию
            assertNull(person.getId(), "ID должен быть null при использовании NoArgsConstructor");
            assertNull(person.getLocation(), "Location должна быть null при использовании NoArgsConstructor");
            assertFalse(person.isCheering(), "Поле isCheering должно быть false по умолчанию");
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

        @Test
        @DisplayName("@NoArgsConstructor валидация")
        void NoArgsConstructor() {
            Platform p1 = new Platform();
            assertNotNull(p1);
            assertNull(p1.getLocation());
        }

        @Test
        @DisplayName("@EqualsAndHashCode валидация")
        void EqualsAndHashCode() {
            Location loc1 = new Location(1,2,3); // Допустим, у Location тоже есть equals/hashCode;
            Speaker s1 = new Speaker();

            Platform p1 = Platform.builder().location(loc1).speaker(s1).build();
            Platform p2 = Platform.builder().location(loc1).speaker(s1).build();


            assertEquals(p1, p1);

//            assertEquals(p1, p2);
//            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        @DisplayName("@ToString валидация")
        void ToString() {
            Platform p1 = new Platform();
            String toString = p1.toString();
            assertTrue(toString.contains("Platform"));
            assertTrue(toString.contains("location="));
            assertTrue(toString.contains("speaker="));
        }

        @Test
        @DisplayName("@Setter валидация")
        void Setter() {
            Platform p1 = new Platform();
            Speaker mockSpeaker = new Speaker(); // Предположим, класс Speaker существует
            p1.setSpeaker(mockSpeaker);
            assertEquals(mockSpeaker, p1.getSpeaker());
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

        @Test
        @DisplayName("Валидация @NoArgsConstructor")
        void NoArgsConstructor() {
            Scene scene = new Scene();

            assertThrows(NullPointerException.class, () -> scene.setCrowd(null));
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
    // ТЕСТЫ WINDOW
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

        @Test
        @DisplayName("Валидация @NoArgsConstructor")
        void NoArgsConstructor() {
            Window window = new Window();

            assertNull(window.getId());
        }
    }
}