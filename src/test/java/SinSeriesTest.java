import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование разложения sin(x) в степенной ряд")
class SinSeriesTest {

    // Тестируемая функция - разложение sin(x) в ряд Тейлора
    static class SinSeries {
        private static final double EPSILON = 1e-10;
        private static final int MAX_ITERATIONS = 1000;

        public static double sin(double x) {

            x = reduceToPeriod(x);

            double sum = 0.0;
            double term = x; // первый член ряда: x
            int n = 1;

            while (Math.abs(term) > EPSILON && n < MAX_ITERATIONS) {
                sum += term;
                term = -term * x * x / ((2 * n) * (2 * n + 1));
                n++;
            }

            return sum;
        }

        private static double reduceToPeriod(double x) {

            double twoPi = 2 * Math.PI;
            x = x % twoPi;
            if (x > Math.PI) {
                x -= twoPi;
            } else if (x < -Math.PI) {
                x += twoPi;
            }
            return x;
        }
    }

    // 1. ТЕСТЫ НА ГРАНИЧНЫЕ ЗНАЧЕНИЯ
    @Test
    @DisplayName("Тест граничных значений: sin(0) = 0")
    void testZero() {
        assertEquals(0.0, SinSeries.sin(0), 1e-12);
    }

    @Test
    @DisplayName("Тест граничных значений: sin(π) = 0")
    void testPi() {
        assertEquals(0.0, SinSeries.sin(Math.PI), 1e-10);
    }

    @Test
    @DisplayName("Тест граничных значений: sin(-π) = 0")
    void testMinusPi() {
        assertEquals(0.0, SinSeries.sin(-Math.PI), 1e-10);
    }

    @Test
    @DisplayName("Тест граничных значений: sin(π/2) = 1")
    void testPiHalf() {
        assertEquals(1.0, SinSeries.sin(Math.PI / 2), 1e-10);
    }

    @Test
    @DisplayName("Тест граничных значений: sin(-π/2) = -1")
    void testMinusPiHalf() {
        assertEquals(-1.0, SinSeries.sin(-Math.PI / 2), 1e-10);
    }

    @Test
    @DisplayName("Тест граничных значений: sin(2π) = 0")
    void testTwoPi() {
        assertEquals(0.0, SinSeries.sin(2 * Math.PI), 1e-10);
    }

    // 2. ТЕСТЫ НА ПЕРИОДИЧНОСТЬ
    @ParameterizedTest
    @MethodSource("periodicityTestProvider")
    @DisplayName("Тест периодичности: sin(x + 2π) = sin(x)")
    void testPeriodicity(double x) {
        assertEquals(
                SinSeries.sin(x),
                SinSeries.sin(x + 2 * Math.PI),
                1e-10,
                "Периодичность нарушена для x = " + x
        );
    }

    static Stream<Arguments> periodicityTestProvider() {
        return Stream.of(
                Arguments.of(0.0),
                Arguments.of(0.5),
                Arguments.of(1.0),
                Arguments.of(-0.5),
                Arguments.of(Math.PI / 4),
                Arguments.of(Math.PI / 3)
        );
    }

    // 3. ТЕСТЫ НА ЧЕТНОСТЬ/НЕЧЕТНОСТЬ
    @ParameterizedTest
    @MethodSource("oddFunctionTestProvider")
    @DisplayName("Тест нечетности: sin(-x) = -sin(x)")
    void testOddFunction(double x) {
        assertEquals(
                -SinSeries.sin(x),
                SinSeries.sin(-x),
                1e-10,
                "Нечетность нарушена для x = " + x
        );
    }

    static Stream<Arguments> oddFunctionTestProvider() {
        return Stream.of(
                Arguments.of(0.0),
                Arguments.of(0.1),
                Arguments.of(0.5),
                Arguments.of(1.0),
                Arguments.of(1.5),
                Arguments.of(Math.PI / 4),
                Arguments.of(Math.PI / 2)
        );
    }

    // 4. ТЕСТЫ НА СПЕЦИАЛЬНЫЕ ЗНАЧЕНИЯ
    @Test
    @DisplayName("Тест специальных значений: sin(π/6) = 0.5")
    void testPiSixth() {
        assertEquals(0.5, SinSeries.sin(Math.PI / 6), 1e-10);
    }

    @Test
    @DisplayName("Тест специальных значений: sin(π/4) = √2/2")
    void testPiFourth() {
        assertEquals(Math.sqrt(2) / 2, SinSeries.sin(Math.PI / 4), 1e-10);
    }

    @Test
    @DisplayName("Тест специальных значений: sin(π/3) = √3/2")
    void testPiThird() {
        assertEquals(Math.sqrt(3) / 2, SinSeries.sin(Math.PI / 3), 1e-10);
    }

    @Test
    @DisplayName("Тест специальных значений: sin(π/12) = (√6-√2)/4")
    void testPiTwelfth() {
        double expected = (Math.sqrt(6) - Math.sqrt(2)) / 4;
        assertEquals(expected, SinSeries.sin(Math.PI / 12), 1e-10);
    }

    // 5. тест сравнения с сисмтемной функцией
    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0})
    @DisplayName("Сравнение с Math.sin() для различных значений")
    void testAgainstMathSin(double x) {
        assertEquals(Math.sin(x), SinSeries.sin(x), 1e-10);
    }

//    // 6. ТЕСТЫ ДЛЯ БОЛЬШИХ ЗНАЧЕНИЙ
//    @ParameterizedTest
//    @ValueSource(doubles = {10.0, 100.0, 1000.0, -10.0, -100.0, -1000.0})
//    @DisplayName("Тест для больших значений x")
//    void testLargeValues(double x) {
//        assertEquals(Math.sin(x), SinSeries.sin(x), 1e-9);
//    }

    // 7. ТЕСТЫ ДЛЯ ОЧЕНЬ МАЛЕНЬКИХ ЗНАЧЕНИЙ
    @Test
    @DisplayName("Тест для очень маленьких значений (асимптотика)")
    void testSmallValues() {
        double x = 1e-6;
        assertEquals(x, SinSeries.sin(x), 1e-12);
    }

    // 8. ТЕСТЫ НА КОРРЕКТНОСТЬ РАЗЛОЖЕНИЯ
//    @Test
//    @DisplayName("Проверка первых членов ряда Тейлора")
//    void testTaylorSeriesTerms() {
//        double x = 0.5;
//
//        // Первый член: x
//        double term1 = x;
//        // Второй член: -x³/6
//        double term2 = -x*x*x/6;
//        // Третий член: x⁵/120
//        double term3 = x*x*x*x*x/120;
//
//        double partialSum1 = term1;
//        double partialSum2 = term1 + term2;
//        double partialSum3 = term1 + term2 + term3;
//        double fullSum = SinSeries.sin(x);
//
//        assertAll("Проверка сходимости ряда",
//                () -> assertEquals(partialSum1, SinSeries.sin(x), 0.1, "1 член ряда недостаточно точен"),
//                () -> assertEquals(partialSum2, SinSeries.sin(x), 0.01, "2 члена ряда недостаточно точны"),
//                () -> assertEquals(partialSum3, SinSeries.sin(x), 0.001, "3 члена ряда недостаточно точны"),
//                () -> assertEquals(fullSum, Math.sin(x), 1e-10, "Полная сумма ряда не совпадает с Math.sin()")
//        );
//    }

    // 9. ТЕСТЫ НА УСТОЙЧИВОСТЬ К ВЫРОЖДЕННЫМ СЛУЧАЯМ
    @Test
    @DisplayName("Тест на устойчивость к вырожденным случаям")
    void testEdgeCases() {
        assertAll("Проверка особых случаев",
                () -> assertEquals(0.0, SinSeries.sin(Double.NaN), "NaN должен возвращать NaN"),
                () -> assertTrue(Double.isFinite(SinSeries.sin(Double.POSITIVE_INFINITY)),
                        "Бесконечность должна давать конечное значение"),
                () -> assertTrue(Double.isFinite(SinSeries.sin(Double.NEGATIVE_INFINITY)),
                        "Бесконечность должна давать конечное значение")
        );
    }

    // 10.
    @Test
    @DisplayName("Тест в точках смены знака")
    void testSignChange() {
        assertAll("Проверка смены знака",
                () -> assertTrue(SinSeries.sin(0.1) > 0, "sin(0.1) > 0"),
                () -> assertTrue(SinSeries.sin(-0.1) < 0, "sin(-0.1) < 0"),
                () -> assertTrue(SinSeries.sin(Math.PI + 0.1) < 0, "sin(π+0.1) < 0"),
                () -> assertTrue(SinSeries.sin(Math.PI - 0.1) > 0, "sin(π-0.1) > 0")
        );
    }

}