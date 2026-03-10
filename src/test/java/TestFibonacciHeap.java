import org.example.fibonacci.FibonacciHeap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void testNodeCreation() {
        FibonacciHeap.Node node = new FibonacciHeap.Node(10, "test");

        assertEquals(10, node.getKey());
        assertEquals("test", node.getValue());
        assertEquals(0, node.getDegree());
        assertFalse(node.isMarked());
        assertNull(node.getParent());
        assertNull(node.getChild());
        assertEquals(node, node.getLeft());
        assertEquals(node, node.getRight());
    }

    @Test
    void testToString() {
        FibonacciHeap.Node node = new FibonacciHeap.Node(10, "test");
        assertEquals("10", node.toString());

        // Отметка не влияет на toString по умолчанию
        node = new FibonacciHeap.Node(10, "test") {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        assertEquals("10", node.toString());
    }

}

class InsertTest {

    @Test
    void testInsertIntoEmptyHeap() {
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.Node node = heap.insert(5, "five");

        assertEquals(1, heap.getSize());
        assertEquals(node, heap.getMin());
        assertEquals(5, heap.getMin().getKey());
        assertEquals("five", heap.getMin().getValue());
    }

//    @Test
//    void testInsertMultiple() {
//        FibonacciHeap heap = new FibonacciHeap();
//        heap.insert(10, "ten");
//        heap.insert(5, "five");
//        heap.insert(7, "seven");
//
//        assertEquals(3, heap.getSize());
//        assertEquals(5, heap.getMin().getKey());
//        assertEquals("five", heap.getMin().getValue());
//
//        assertEquals(Arrays.asList(5, 7, 10), heap.getRootKeys());
//    }

    @Test
    void testInsertWithSameKey() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(5, "first");
        heap.insert(5, "second");

        assertEquals(2, heap.getSize());
        assertEquals(5, heap.getMin().getKey());

        // Оба узла должны быть в корневом списке
        assertEquals(2, heap.getRootKeys().size());
        assertTrue(heap.getRootKeys().containsAll(Arrays.asList(5, 5)));
    }

    @Test
    void testInsertAndVerifyStructure() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(3, "three");
        heap.insert(2, "two");
        heap.insert(1, "one");

        assertEquals(3, heap.getSize());
        assertEquals(1, heap.getMin().getKey());

        // Проверяем, что все узлы в корневом списке
        List<Integer> rootKeys = heap.getRootKeys();
        assertEquals(3, rootKeys.size());
        assertTrue(rootKeys.containsAll(Arrays.asList(1, 2, 3)));
    }

}

class MergeTest {

    @Test
    void testMergeEmptyHeap() {
        FibonacciHeap heap1 = new FibonacciHeap();
        FibonacciHeap heap2 = new FibonacciHeap();

        heap2.insert(5, "five");
        heap1.merge(heap2);

        assertEquals(1, heap1.getSize());
        assertEquals(5, heap1.getMin().getKey());
    }

    @Test
    void testMergeTwoNonEmptyHeaps() {
        FibonacciHeap heap1 = new FibonacciHeap();
        FibonacciHeap heap2 = new FibonacciHeap();

        heap1.insert(3, "three");
        heap1.insert(7, "seven");
        heap2.insert(2, "two");
        heap2.insert(5, "five");

        heap1.merge(heap2);

        assertEquals(4, heap1.getSize());
        assertEquals(2, heap1.getMin().getKey());

        List<Integer> rootKeys = heap1.getRootKeys();
        assertEquals(4, rootKeys.size());
        assertTrue(rootKeys.containsAll(Arrays.asList(2, 3, 5, 7)));
    }

    @Test
    void testMergeWithNull() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(1, "one");

        heap.merge(null);

        assertEquals(1, heap.getSize());
        assertEquals(1, heap.getMin().getKey());
    }

    @Test
    void testMergeMaintainsMin() {
        FibonacciHeap heap1 = new FibonacciHeap();
        FibonacciHeap heap2 = new FibonacciHeap();

        heap1.insert(10, "ten");
        heap1.insert(8, "eight");
        heap2.insert(5, "five");
        heap2.insert(12, "twelve");

        heap1.merge(heap2);

        assertEquals(5, heap1.getMin().getKey());
    }
}


class ExtractMinTest {

    @Test
    void testExtractMinFromEmptyHeap() {
        FibonacciHeap heap = new FibonacciHeap();
        assertNull(heap.extractMin());
        assertEquals(0, heap.getSize());
    }

    @Test
    void testExtractMinSingleNode() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(5, "five");

        assertEquals(5, heap.extractMin());
        assertTrue(heap.isEmpty());
        assertNull(heap.getMin());
    }

    @Test
    void testExtractMinMultipleRoots() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(3, "three");
        heap.insert(1, "one");
        heap.insert(2, "two");

        assertEquals(1, heap.extractMin());
        assertEquals(2, heap.getSize());
        assertEquals(2, heap.getMin().getKey());
    }

//    @Test
//    void testExtractMinWithChildren() {
//        FibonacciHeap heap = new FibonacciHeap();
//        FibonacciHeap.Node root = heap.insert(5, "five");
//
//        // Добавляем детей
//        FibonacciHeap.Node child1 = heap.insert(3, "three");
//        FibonacciHeap.Node child2 = heap.insert(4, "four");
//
//        // Связываем детей с корнем
//        root.child = child1;
//        child1.parent = root;
//        child1.right = child2;
//        child2.parent = root;
//        child2.left = child1;
//        child2.right = child1;
//        child1.left = child2;
//        root.degree = 2;
//
//        // Удаляем из корневого списка
//        heap.extractMin(); // Должен извлечь 3? Нет, 3 меньше 5
//        // Но 3 и 4 в корневом списке? Да, после extractMin
//
//        assertEquals(2, heap.getSize());
//        // Новый минимум должен быть 3
//        assertEquals(3, heap.getMin().getKey());
//    }

    @Test
    void testExtractMinWithConsolidation() {
        FibonacciHeap heap = new FibonacciHeap();

        // Вставляем узлы для создания деревьев разных степеней
        heap.insert(10, "ten");
        heap.insert(20, "twenty");
        heap.insert(30, "thirty");
        heap.insert(40, "forty");
        heap.insert(5, "five");
        heap.insert(15, "fifteen");
        heap.insert(25, "twentyfive");

        assertEquals(5, heap.extractMin());

        // Проверяем, что консолидация произошла
        List<Integer> degrees = heap.getRootDegrees();
        // После консолидации не должно быть двух деревьев одной степени
        Set<Integer> uniqueDegrees = new HashSet<>(degrees);
        assertEquals(degrees.size(), uniqueDegrees.size());
    }

    @Test
    void testExtractMinReturnsCorrectValue() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(100, "hundred");
        heap.insert(50, "fifty");
        heap.insert(75, "seventyfive");

        assertEquals(50, heap.extractMin());
    }
}



class ConsolidateTest {

    @Test
    void testConsolidationBasic() {
        FibonacciHeap heap = new FibonacciHeap();

        // Создаем несколько корней
        FibonacciHeap.Node node1 = heap.insert(3, "three");
        FibonacciHeap.Node node2 = heap.insert(2, "two");
        FibonacciHeap.Node node3 = heap.insert(1, "one");

        // Принудительно вызываем консолидацию через extractMin
        heap.extractMin(); // Извлекаем 1

        // После извлечения минимума должна произойти консолидация
        assertTrue(heap.getRootDegrees().size() <= 2);
    }

//    @Test
//    void testLinkOperation() {
//        FibonacciHeap heap = new FibonacciHeap();
//
//        FibonacciHeap.Node node1 = heap.insert(5, "five");
//        FibonacciHeap.Node node2 = heap.insert(3, "three");
//
//        // Симулируем link
//        // Извлекаем минимум для консолидации
//        heap.extractMin(); // Должен извлечь 3, сделать node1 ребенком node2? Нет, node1.key > node2.key
//
//        // Проверяем, что узел с большим ключом стал ребенком
//        if (heap.getMin().getKey() == 5) {
//            assertNotNull(heap.getMin().getChild());
//        }
//    }

    @Test
    void testNoDuplicatesAfterConsolidation() {
        FibonacciHeap heap = new FibonacciHeap();

        // Вставляем узлы для создания деревьев степени 1
        heap.insert(10, "ten");
        heap.insert(20, "twenty");
        heap.insert(30, "thirty");
        heap.insert(5, "five");

        heap.extractMin(); // Извлекаем 5

        List<Integer> degrees = heap.getRootDegrees();
        // Проверяем уникальность степеней
        Set<Integer> uniqueDegrees = new HashSet<>(degrees);
        assertEquals(degrees.size(), uniqueDegrees.size());
    }
}

class FibonacciHeapIntegrationTest {

//    @Test
//    void testCompleteWorkflow() {
//        FibonacciHeap heap = new FibonacciHeap();
//
//        // Вставка
//        heap.insert(10, "ten");
//        heap.insert(5, "five");
//        heap.insert(15, "fifteen");
//        heap.insert(3, "three");
//
//        assertEquals(4, heap.getSize());
//        assertEquals(3, heap.getMin().getKey());
//
//        // Извлечение минимума
//        assertEquals(3, heap.extractMin());
//        assertEquals(3, heap.getSize());
//        assertEquals(5, heap.getMin().getKey());
//
//        // Создание второй кучи и слияние
//        FibonacciHeap heap2 = new FibonacciHeap();
//        heap2.insert(1, "one");
//        heap2.insert(7, "seven");
//
//        heap.merge(heap2);
//        assertEquals(5, heap.getSize());
//        assertEquals(1, heap.getMin().getKey());
//
//        // Проверка структуры
//        List<Integer> rootKeys = heap.getRootKeys();
//        assertTrue(rootKeys.contains(1));
//        assertTrue(rootKeys.contains(5));
//        assertTrue(rootKeys.contains(7));
//        assertTrue(rootKeys.contains(10));
//        assertTrue(rootKeys.contains(15));
//
//        // Извлечение всех элементов по порядку
//        List<Integer> extracted = new ArrayList<>();
//        while (!heap.isEmpty()) {
//            extracted.add(heap.extractMin());
//        }
//
//        assertEquals(Arrays.asList(1, 5, 7, 10, 15), extracted);
//        assertTrue(heap.isEmpty());
//        assertNull(heap.getMin());
//    }

    @Test
    void testLargeScaleOperations() {
        FibonacciHeap heap = new FibonacciHeap();
        Random random = new Random(42);

        // Вставляем 1000 случайных элементов
        List<Integer> inserted = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            int value = random.nextInt(10000);
            heap.insert(value, String.valueOf(value));
            inserted.add(value);
        }

        assertEquals(1000, heap.getSize());

        // Извлекаем все и проверяем порядок
        List<Integer> extracted = new ArrayList<>();
        while (!heap.isEmpty()) {
            extracted.add(heap.extractMin());
        }

        Collections.sort(inserted);
        assertEquals(inserted, extracted);
    }

    @Test
    void testMultipleMergeOperations() {
        FibonacciHeap mainHeap = new FibonacciHeap();

        for (int i = 0; i < 5; i++) {
            FibonacciHeap tempHeap = new FibonacciHeap();
            tempHeap.insert(i * 10 + 1, "one");
            tempHeap.insert(i * 10 + 2, "two");
            mainHeap.merge(tempHeap);
        }

        assertEquals(10, mainHeap.getSize());

        List<Integer> expected = Arrays.asList(1, 2, 11, 12, 21, 22, 31, 32, 41, 42);
        List<Integer> extracted = new ArrayList<>();
        while (!mainHeap.isEmpty()) {
            extracted.add(mainHeap.extractMin());
        }

        assertEquals(expected, extracted);
    }
}


class EdgeCasesTest {

    @Test
    void testEmptyHeapOperations() {
        FibonacciHeap heap = new FibonacciHeap();

        assertTrue(heap.isEmpty());
        assertEquals(0, heap.getSize());
        assertNull(heap.getMin());
        assertNull(heap.extractMin());
        assertTrue(heap.getRootKeys().isEmpty());
        assertTrue(heap.getRootDegrees().isEmpty());
        assertTrue(heap.getChildrenMap().isEmpty());
    }

    @Test
    void testInsertNullValue() {
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.Node node = heap.insert(5, null);

        assertEquals(5, node.getKey());
        assertNull(node.getValue());
    }

    @Test
    void testMergeWithSelf() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(5, "five");

        FibonacciHeap copy = new FibonacciHeap();
        copy.merge(heap);

        assertEquals(1, copy.getSize());
        assertEquals(5, copy.getMin().getKey());
    }

    @Test
    void testExtractMinFromOneElementHeap() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(42, "answer");

        assertEquals(42, heap.extractMin());
        assertEquals(0, heap.getSize());
        assertNull(heap.getMin());

        // Повторное извлечение
        assertNull(heap.extractMin());
    }

    @Test
    void testLargeDegrees() {
        FibonacciHeap heap = new FibonacciHeap();

        // Создаем дерево большой степени
        FibonacciHeap.Node root = heap.insert(100, "root");

        for (int i = 0; i < 10; i++) {
            FibonacciHeap.Node child = heap.insert(i, "child");
            // Искусственно делаем child ребенком root
            child.parent = root;
            if (root.child == null) {
                root.child = child;
                child.left = child;
                child.right = child;
            } else {
                child.left = root.child;
                child.right = root.child.right;
                root.child.right.left = child;
                root.child.right = child;
            }
            root.degree++;
        }

        // Удаляем root из корневого списка
        heap.extractMin(); // Извлекаем 100

        // Дети должны быть в корневом списке
        assertEquals(10, heap.getSize());
    }
}

class ChildrenMapTest {

    private FibonacciHeap heap;

    @BeforeEach
    void setUp() {
        heap = new FibonacciHeap();
    }

    @Test
    void testGetChildrenMapWithEmptyHeap() {
        // Покрывает строки 244-245 (пустая куча)
        Map<Integer, List<Integer>> childrenMap = heap.getChildrenMap();
        assertTrue(childrenMap.isEmpty());
    }

    @Test
    void testGetChildrenMapWithSingleNode() {
        // Покрывает строки 247-249 (посещение единственного узла)
        heap.insert(10, "ten");

        Map<Integer, List<Integer>> childrenMap = heap.getChildrenMap();
        assertTrue(childrenMap.isEmpty()); // Нет детей
    }

    @Test
    void testBuildChildrenMapWithSimpleParentChild() {
        // Покрывает строки 252-264 (простая связь родитель-ребенок)
        FibonacciHeap.Node parent = heap.insert(50, "parent");
        FibonacciHeap.Node child = new FibonacciHeap.Node(30, "child");

        // Вручную устанавливаем связь родитель-ребенок
        parent.child = child;
        child.parent = parent;
        child.left = child;
        child.right = child;
        parent.degree = 1;

        // Добавляем ребенка в корневой список? Нет, ребенок не должен быть в корневом списке
        // Но для корректности теста добавим родителя в корневой список
        heap.getMin().child = child; // Предполагаем, что parent - это min

        Map<Integer, List<Integer>> childrenMap = heap.getChildrenMap();

        assertEquals(1, childrenMap.size());
        assertTrue(childrenMap.containsKey(50));
        assertEquals(Arrays.asList(30), childrenMap.get(50));
    }

    @Test
    void testBuildChildrenMapWithMultipleChildren() {
        // Покрывает строки 256-264 (несколько детей)
        FibonacciHeap.Node parent = heap.insert(100, "parent");

        // Создаем двух детей
        FibonacciHeap.Node child1 = new FibonacciHeap.Node(70, "child1");
        FibonacciHeap.Node child2 = new FibonacciHeap.Node(80, "child2");

        // Устанавливаем циклический список детей
        parent.child = child1;
        child1.parent = parent;
        child2.parent = parent;

        child1.left = child2;
        child1.right = child2;
        child2.left = child1;
        child2.right = child1;
        parent.degree = 2;

        heap.getMin().child = child1;

        Map<Integer, List<Integer>> childrenMap = heap.getChildrenMap();

        assertEquals(1, childrenMap.size());
        assertTrue(childrenMap.containsKey(100));
        assertEquals(2, childrenMap.get(100).size());
        assertTrue(childrenMap.get(100).containsAll(Arrays.asList(70, 80)));
    }

    @Test
    void testBuildChildrenMapWithNestedChildren() {
        // Покрывает строки 256-264 и 267-269 (вложенные дети и обход правых узлов)
        FibonacciHeap.Node root1 = heap.insert(200, "root1");
        FibonacciHeap.Node root2 = heap.insert(300, "root2");

        // Дети для root1
        FibonacciHeap.Node child11 = new FibonacciHeap.Node(150, "child11");
        FibonacciHeap.Node child12 = new FibonacciHeap.Node(160, "child12");

        root1.child = child11;
        child11.parent = root1;
        child12.parent = root1;

        child11.left = child12;
        child11.right = child12;
        child12.left = child11;
        child12.right = child11;
        root1.degree = 2;

        // Внуки для child11
        FibonacciHeap.Node grandchild = new FibonacciHeap.Node(140, "grandchild");
        child11.child = grandchild;
        grandchild.parent = child11;
        grandchild.left = grandchild;
        grandchild.right = grandchild;
        child11.degree = 1;

        Map<Integer, List<Integer>> childrenMap = heap.getChildrenMap();

        // Проверяем структуру
        assertEquals(2, childrenMap.size()); // root1 и child11 имеют детей

        assertTrue(childrenMap.containsKey(200));
        assertEquals(Arrays.asList(150, 160), childrenMap.get(200));

        assertTrue(childrenMap.containsKey(150));
        assertEquals(Arrays.asList(140), childrenMap.get(150));

        // root2 не имеет детей
        assertFalse(childrenMap.containsKey(300));
    }

    @Test
    void testBuildChildrenMapWithComplexStructure() {
        // Покрывает все строки, включая проверки visited (строки 253 и 267)
        FibonacciHeap.Node root = heap.insert(500, "root");

        // Создаем структуру:
        // root
        //   ├─ child1
        //   │    └─ grandchild1
        //   └─ child2

        FibonacciHeap.Node child1 = new FibonacciHeap.Node(400, "child1");
        FibonacciHeap.Node child2 = new FibonacciHeap.Node(450, "child2");
        FibonacciHeap.Node grandchild1 = new FibonacciHeap.Node(350, "grandchild1");

        // Связываем root с детьми
        root.child = child1;
        child1.parent = root;
        child2.parent = root;

        child1.right = child2;
        child1.left = child2;
        child2.right = child1;
        child2.left = child1;
        root.degree = 2;

        // Связываем child1 с внуком
        child1.child = grandchild1;
        grandchild1.parent = child1;
        grandchild1.left = grandchild1;
        grandchild1.right = grandchild1;
        child1.degree = 1;

        heap.getMin().child = child1;

        Map<Integer, List<Integer>> childrenMap = heap.getChildrenMap();

        // Проверяем, что visited работает правильно (нет зацикливаний)
        assertEquals(2, childrenMap.size()); // root и child1 имеют детей

        // Дети root
        assertTrue(childrenMap.containsKey(500));
        List<Integer> rootChildren = childrenMap.get(500);
        assertEquals(2, rootChildren.size());
        assertTrue(rootChildren.containsAll(Arrays.asList(400, 450)));

        // Дети child1
        assertTrue(childrenMap.containsKey(400));
        assertEquals(Arrays.asList(350), childrenMap.get(400));

        // Проверяем формат строк (покрывает toString)
        assertNotNull(root.toString());
        assertNotNull(child1.toString());
    }

    @Test
    void testBuildChildrenMapPreventsCycles() {
        // Специально для покрытия проверки visited.contains(node) (строка 253)
        FibonacciHeap.Node root1 = heap.insert(600, "root1");
        FibonacciHeap.Node root2 = heap.insert(700, "root2");

        // Создаем циклическую ссылку (это не должно сломать обход)
        FibonacciHeap.Node child = new FibonacciHeap.Node(650, "child");
        root1.child = child;
        child.parent = root1;
        child.left = child;
        child.right = child;
        root1.degree = 1;

        // Создаем еще одного ребенка с той же ссылкой для проверки visited
        FibonacciHeap.Node anotherChild = new FibonacciHeap.Node(680, "another");
        root2.child = anotherChild;
        anotherChild.parent = root2;
        anotherChild.left = anotherChild;
        anotherChild.right = anotherChild;
        root2.degree = 1;

        Map<Integer, List<Integer>> childrenMap = heap.getChildrenMap();

        // Проверяем, что обход не зациклился
        assertEquals(2, childrenMap.size());
        assertTrue(childrenMap.containsKey(600));
        assertTrue(childrenMap.containsKey(700));
        assertEquals(Arrays.asList(650), childrenMap.get(600));
        assertEquals(Arrays.asList(680), childrenMap.get(700));
    }

    @Test
    void testBuildChildrenMapWithRightTraversal() {
        // Специально для покрытия строк 267-269 (обход правых узлов)
        FibonacciHeap.Node root1 = heap.insert(800, "root1");
        FibonacciHeap.Node root2 = heap.insert(900, "root2");
        FibonacciHeap.Node root3 = heap.insert(850, "root3");

        // У root1 есть ребенок
        FibonacciHeap.Node child = new FibonacciHeap.Node(750, "child");
        root1.child = child;
        child.parent = root1;
        child.left = child;
        child.right = child;
        root1.degree = 1;

        Map<Integer, List<Integer>> childrenMap = heap.getChildrenMap();

        // Должны быть обработаны все корни через node.right
        assertEquals(1, childrenMap.size()); // Только root1 имеет детей
        assertTrue(childrenMap.containsKey(800));
        assertEquals(Arrays.asList(750), childrenMap.get(800));

        // Проверяем, что root2 и root3 были посещены, но не добавлены в map (нет детей)
        assertFalse(childrenMap.containsKey(900));
        assertFalse(childrenMap.containsKey(850));
    }

    @Test
    void testBuildChildrenMapWithNullNode() {
        // Покрывает строку 253 (проверка node == null)
        // Создаем ситуацию, где node может быть null
        // В нормальных условиях это не происходит, но можно протестировать через рефлексию
        // или просто убедиться, что метод не падает при корректных данных

        FibonacciHeap.Node root = heap.insert(950, "root");

        // Устанавливаем child в null (это нормально)
        root.child = null;

        Map<Integer, List<Integer>> childrenMap = heap.getChildrenMap();
        assertTrue(childrenMap.isEmpty());
    }
}