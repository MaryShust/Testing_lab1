package org.example.fibonacci;

import java.util.*;

public class FibonacciHeap {

    public static class Node {
        public int key;
        public String value;
        public int degree;
        public boolean marked;
        public Node parent;
        public Node child;
        public Node left;
        public Node right;

        public Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.degree = 0;
            this.marked = false;
            this.parent = null;
            this.child = null;
            this.left = this;
            this.right = this;
        }

        @Override
        public String toString() {
            return String.valueOf(key) + (marked ? "*" : "");
        }
    }


    public Node min;
    public int size;


    public FibonacciHeap() {
        this.min = null;
        this.size = 0;
    }

    // --- 1. ВСТАВКА ---
    public Node insert(int key, String value) {
        Node node = new Node(key, value);
        if (min == null) {
            min = node;
        } else {
            // Вставляем в корневой список слева от min
            addToRootList(node);
            if (node.key < min.key) {
                min = node;
            }
        }
        size++;
        return node;
    }

    // --- 2. ОБЪЕДИНЕНИЕ КУЧ ---
    public void merge(FibonacciHeap other) {
        if (other == null || other.min == null) return;

        if (this.min == null) {
            this.min = other.min;
            this.size = other.size;
        } else {
            // Сшиваем корневые списки
            Node thisLeft = this.min.left;
            Node otherLeft = other.min.left;

            this.min.left = otherLeft;
            otherLeft.right = this.min;
            other.min.left = thisLeft;
            thisLeft.right = other.min;

            if (other.min.key < this.min.key) {
                this.min = other.min;
            }
            this.size += other.size;
        }
    }

    // --- 3. ИЗВЛЕЧЕНИЕ МИНИМУМА ---
    public int extractMin() {
        if (min == null) return -1;

        Node extracted = min;
        int minKey = extracted.key;

        // Перемещаем детей в корневой список
        if (extracted.child != null) {
            Node child = extracted.child;
            do {
                child.parent = null;
                child = child.right;
            } while (child != extracted.child);

            // Вставляем детей слева от min
            Node minLeft = min.left;
            Node childLeft = extracted.child.left;

            min.left = childLeft;
            childLeft.right = min;
            extracted.child.left = minLeft;
            minLeft.right = extracted.child;
        }

        // Удаляем min из корневого списка
        extracted.left.right = extracted.right;
        extracted.right.left = extracted.left;

        if (extracted == extracted.right) {
            min = null;
        } else {
            min = extracted.right;
            consolidate();
        }

        size--;
        return minKey;
    }

    // --- 4. КОНСОЛИДАЦИЯ ---
    private void consolidate() {
        if (min == null) return;

        // Максимальная степень = log₂(size) по формуле Фибоначчи
        int maxDegree = (int) Math.floor(Math.log(size) / Math.log(1.618)) + 1;
        Node[] degreeTable = new Node[maxDegree + 1];

        List<Node> roots = getAllRoots();

        for (Node x : roots) {
            int d = x.degree;

            while (degreeTable[d] != null) {
                Node y = degreeTable[d];
                if (x.key > y.key) {
                    Node temp = x;
                    x = y;
                    y = temp;
                }
                link(y, x);
                degreeTable[d] = null;
                d++;
            }
            degreeTable[d] = x;
        }

        // Восстанавливаем min
        min = null;
        for (int i = 0; i <= maxDegree; i++) {
            if (degreeTable[i] != null) {
                if (min == null || degreeTable[i].key < min.key) {
                    min = degreeTable[i];
                }
            }
        }
    }

    // --- 5. СВЯЗЫВАНИЕ (y становится ребенком x) ---
    private void link(Node y, Node x) {
        // Удаляем y из корневого списка
        y.left.right = y.right;
        y.right.left = y.left;

        // Делаем y ребенком x
        y.parent = x;
        y.marked = false;

        if (x.child == null) {
            x.child = y;
            y.left = y;
            y.right = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right.left = y;
            x.child.right = y;
        }

        x.degree++;
    }

    // --- 6. УМЕНЬШЕНИЕ КЛЮЧА ---
    public void decreaseKey(Node node, int newKey) {
        if (newKey > node.key) return;

        node.key = newKey;
        Node parent = node.parent;

        if (parent != null && node.key < parent.key) {
            cut(node, parent);
            cascadingCut(parent);
        }

        if (node.key < min.key) {
            min = node;
        }
    }

    // --- 7. ВЫРЕЗАНИЕ ---
    private void cut(Node node, Node parent) {
        // Удаляем node из списка детей parent
        if (node.right == node) {
            parent.child = null;
        } else {
            if (parent.child == node) {
                parent.child = node.right;
            }
            node.left.right = node.right;
            node.right.left = node.left;
        }
        parent.degree--;

        // Добавляем node в корневой список
        addToRootList(node);
        node.parent = null;
        node.marked = false;
    }

    // --- 8. КАСКАДНОЕ ВЫРЕЗАНИЕ ---
    private void cascadingCut(Node node) {
        Node parent = node.parent;
        if (parent != null) {
            if (!node.marked) {
                node.marked = true;
            } else {
                cut(node, parent);
                cascadingCut(parent);
            }
        }
    }

    // --- 9. УДАЛЕНИЕ ---
    public void delete(Node node) {
        decreaseKey(node, Integer.MIN_VALUE);
        extractMin();
    }

    // --- ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ---

    private void addToRootList(Node node) {
        node.left = min.left;
        node.right = min;
        min.left.right = node;
        min.left = node;
    }

    private List<Node> getAllRoots() {
        List<Node> roots = new ArrayList<>();
        if (min == null) return roots;

        Node current = min;
        do {
            roots.add(current);
            current = current.right;
        } while (current != min);

        return roots;
    }

    // --- МЕТОДЫ ДЛЯ ТЕСТИРОВАНИЯ ВНУТРЕННЕГО ПРЕДСТАВЛЕНИЯ ---

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Получить ключи корней в порядке обхода
    public List<Integer> getRootKeysInOrder() {
        List<Integer> keys = new ArrayList<>();
        if (min == null) return keys;

        Node current = min;
        do {
            keys.add(current.key);
            current = current.right;
        } while (current != min);

        return keys;
    }

    // Получить маркированные узлы
    public List<String> getMarkedNodes() {
        List<String> marked = new ArrayList<>();
        if (min == null) return marked;

        Set<Node> visited = new HashSet<>();
        collectMarkedNodes(min, visited, marked);

        return marked;
    }

    private void collectMarkedNodes(Node root, Set<Node> visited, List<String> marked) {
        if (root == null || visited.contains(root)) return;
        visited.add(root);

        if (root.marked) {
            marked.add(String.valueOf(root.key) + "(" + root.value + ")");
        }

        // Обходим детей
        if (root.child != null) {
            Node child = root.child;
            do {
                collectMarkedNodes(child, visited, marked);
                child = child.right;
            } while (child != root.child);
        }

        // Обходим соседей
        if (root.right != root && !visited.contains(root.right)) {
            collectMarkedNodes(root.right, visited, marked);
        }
    }

    // Получить карту детей для визуализации
    public Map<Integer, List<Integer>> getChildrenMap() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        if (min == null) return map;

        Set<Node> visited = new HashSet<>();
        buildChildrenMap(min, visited, map);

        return map;
    }

    private void buildChildrenMap(Node node, Set<Node> visited, Map<Integer, List<Integer>> map) {
        if (node == null || visited.contains(node)) return;
        visited.add(node);

        if (node.child != null) {
            List<Integer> children = new ArrayList<>();
            Node child = node.child;
            do {
                children.add(child.key);
                buildChildrenMap(child, visited, map);
                child = child.right;
            } while (child != node.child);
            map.put(node.key, children);
        }

        if (node.right != node && !visited.contains(node.right)) {
            buildChildrenMap(node.right, visited, map);
        }
    }

    // Размер корневого списка
    public int rootListSize() {
        if (min == null) return 0;
        int count = 0;
        Node current = min;
        do {
            count++;
            current = current.right;
        } while (current != min);
        return count;
    }

    // Проверить, содержит ли корневой список ключ
    public boolean rootListContains(int key) {
        if (min == null) return false;
        Node current = min;
        do {
            if (current.key == key) return true;
            current = current.right;
        } while (current != min);
        return false;
    }

    // Получить степени корней
    public List<Integer> getRootDegrees() {
        List<Integer> degrees = new ArrayList<>();
        if (min == null) return degrees;

        Node current = min;
        do {
            degrees.add(current.degree);
            current = current.right;
        } while (current != min);

        return degrees;
    }

    // Печать структуры кучи
    public void printHeap() {
        System.out.println("=== Fibonacci Heap ===");
        System.out.println("Min: " + (min != null ? min.key : "null"));
        System.out.println("Size: " + size);
        System.out.println("Roots (" + rootListSize() + "): " + getRootKeysInOrder());
        System.out.println("Root degrees: " + getRootDegrees());
        System.out.println("Marked nodes: " + getMarkedNodes());
        System.out.println("Children: " + getChildrenMap());
        System.out.println("======================");
    }
}