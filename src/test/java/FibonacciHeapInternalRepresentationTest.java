import static org.junit.jupiter.api.Assertions.*;

import org.example.fibonacci.FibonacciHeap;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Модульное тестирование внутреннего представления Фибоначчиевой кучи.
 * Проверяет: структуру узлов, корневой список, списки детей,
 * маркеры "помеченности", консолидацию после extractMin.
 */
public class FibonacciHeapInternalRepresentationTest {

    // --- Вспомогательный класс для захвата состояний ---
    static class HeapSnapshot {
        final String operation;
        final Integer minKey;
        final int rootCount;
        final int totalNodes;
        final List<String> markedNodes;
        final List<Integer> rootListOrder;
        final Map<Integer, List<Integer>> childrenMap;

        HeapSnapshot(String op, FibonacciHeap heap) {
            this.operation = op;
            this.minKey = heap.min != null ? heap.min.key : null;
            this.rootCount = heap.rootListSize(); // метод нужно реализовать
            this.totalNodes = heap.size();
            this.markedNodes = heap.getMarkedNodes(); // метод нужно реализовать
            this.rootListOrder = heap.getRootKeysInOrder(); // метод нужно реализовать
            this.childrenMap = heap.getChildrenMap(); // метод нужно реализовать
        }

        void print() {
            System.out.println("--- " + operation + " ---");
            System.out.println("Min: " + minKey + ", Roots: " + rootCount + ", Nodes: " + totalNodes);
            System.out.println("Root order: " + rootListOrder);
            System.out.println("Marked: " + markedNodes);
            System.out.println("Children: " + childrenMap);
        }
    }

    private FibonacciHeap heap;
    private List<HeapSnapshot> snapshots;

    @BeforeEach
    void setUp() {
        heap = new FibonacciHeap();
        snapshots = new ArrayList<>();
    }

    // --- ХАРАКТЕРНЫЕ ТОЧКИ АЛГОРИТМА (точки останова) ---
    private void takeSnapshot(String point) {
        snapshots.add(new HeapSnapshot(point, heap));
    }

    // -----------------------------------------------------------
    // ТЕСТ 1: Вставка элементов и структура корневого списка
    // -----------------------------------------------------------
    @Test
    @DisplayName("Вставка: новые узлы добавляются в корневой список слева от min")
    void testInsertRootListOrder() {
        // Точка 1: Пустая куча
        takeSnapshot("A1: Empty heap");

        heap.insert(5, "A");
        // Точка 2: После вставки первого узла (он же min)
        takeSnapshot("A2: Insert 5 (min)");

        heap.insert(3, "B");
        // Точка 3: После вставки меньшего ключа (новый min слева)
        takeSnapshot("A3: Insert 3 (new min)");

        heap.insert(7, "C");
        // Точка 4: После вставки большего ключа (справа от min)
        takeSnapshot("A4: Insert 7");

        // --- ЭТАЛОННАЯ ПОСЛЕДОВАТЕЛЬНОСТЬ ---
        assertEquals(3, heap.min.key);
        assertEquals(List.of(3, 5, 7), snapshots.get(3).rootListOrder);
    }

    // -----------------------------------------------------------
    // ТЕСТ 2: ExtractMin и консолидация
    // -----------------------------------------------------------
    @Test
    @DisplayName("ExtractMin: консолидация деревьев одинаковой степени")
    void testExtractMinConsolidation() {

        heap.insert(1, "A");
        heap.insert(2, "B");
        heap.insert(3, "C");
        heap.insert(4, "D");
        takeSnapshot("B1: Before extractMin");

        int min = heap.extractMin();
        // Точка 5: После extractMin и консолидации
        takeSnapshot("B2: After extractMin (min=1)");

        // --- ЭТАЛОННАЯ ПРОВЕРКА ---
        assertEquals(1, min);
        assertEquals(2, heap.min.key);
        // Проверка, что корни имеют уникальные степени
        assertTrue(areRootDegreesUnique(heap));
    }

    // Вспомогательный метод для тестов
    private void makeChild(FibonacciHeap.Node parent, FibonacciHeap.Node child) {
        child.parent = parent;

        if (parent.child == null) {
            parent.child = child;
            child.left = child;
            child.right = child;
        } else {
            child.left = parent.child;
            child.right = parent.child.right;
            parent.child.right.left = child;
            parent.child.right = child;
        }
        parent.degree++;
    }

    @Test
    @DisplayName("DecreaseKey: каскадное вырезание")
    void testDecreaseKeyCascadingCut() {
        // Создаем корневой узел
        FibonacciHeap.Node root = new FibonacciHeap.Node(10, "root");
        heap.min = root;
        root.left = root;
        root.right = root;
        heap.size = 1;

        // Создаем и связываем узлы
        FibonacciHeap.Node nodeA = new FibonacciHeap.Node(20, "A");
        FibonacciHeap.Node nodeB = new FibonacciHeap.Node(30, "B");
        FibonacciHeap.Node nodeC = new FibonacciHeap.Node(40, "C");

        // Строим иерархию
        makeChild(root, nodeA);
        heap.size++;

        makeChild(nodeA, nodeB);
        heap.size++;

        makeChild(nodeB, nodeC);
        heap.size++;

        takeSnapshot("C1: Иерархия готова");

        // Уменьшаем ключ nodeC - должно вырезать, пометить родителя
        heap.decreaseKey(nodeC, 5);
        takeSnapshot("C2: После decreaseKey C");

        assertFalse(nodeA.marked, "Родитель A должен быть помечен");
        assertTrue(heap.rootListContains(5), "5 должен быть в корнях");

        // Уменьшаем ключ nodeB - должно вырезать и каскадно вырезать nodeA
        heap.decreaseKey(nodeB, 8);
        takeSnapshot("C3: После decreaseKey B");

        assertAll(
                () -> assertTrue(heap.rootListContains(8), "8 в корнях"),
                () -> assertTrue(heap.rootListContains(5), "5 в корнях"),
                () -> assertTrue(heap.rootListContains(10), "10 в корнях"),
                () -> assertEquals(3, heap.rootListSize(), "Три корня"),
                () -> assertEquals(5, heap.min.key, "Min = 5"),
                () -> assertTrue(nodeA.marked, "Маркер A сброшен"),
                () -> assertNull(nodeB.parent, "B должен быть корнем"),
                () -> assertNull(nodeC.parent, "C должен быть корнем")
        );
    }

    // -----------------------------------------------------------
    // ТЕСТ 4: Удаление узла (DecreaseKey + ExtractMin)
    // -----------------------------------------------------------
    @Test
    @DisplayName("Delete: специальный случай DecreaseKey до -inf")
    void testDeleteNode() {
        FibonacciHeap.Node node = heap.insert(15, "toDelete");
        heap.insert(5, "min");
        takeSnapshot("D1: Before delete");

        heap.delete(node);
        // Точка 8: После удаления узла
        takeSnapshot("D2: After delete");

        assertFalse(heap.rootListContains(15));
        assertEquals(5, heap.min.key);
    }

    // -----------------------------------------------------------
    // ТЕСТ 5: Объединение куч (Merge)
    // -----------------------------------------------------------
    @Test
    @DisplayName("Merge: слияние корневых списков")
    void testMergeHeaps() {
        FibonacciHeap heap1 = new FibonacciHeap();
        heap1.insert(1, "a");
        heap1.insert(3, "b");

        FibonacciHeap heap2 = new FibonacciHeap();
        heap2.insert(2, "c");
        heap2.insert(4, "d");

        takeSnapshot("E1: Before merge (heap1 min=1, heap2 min=2)");

        heap1.merge(heap2);
        takeSnapshot("E2: After merge");

        assertEquals(1, heap1.min.key);
        assertEquals(4, heap1.size());
    }

    // --- ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ ПРОВЕРОК ---
    private boolean areRootDegreesUnique(FibonacciHeap heap) {
        Set<Integer> degrees = new HashSet<>();
        FibonacciHeap.Node root = heap.min;
        if (root != null) {
            do {
                if (!degrees.add(root.degree)) {
                    return false; // Найдена повторяющаяся степень
                }
                root = root.right;
            } while (root != heap.min);
        }
        return true;
    }
}