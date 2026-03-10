package org.example.fibonacci;

import java.util.*;

public class FibonacciHeap {

    private Node min;
    private int size;

    public static class Node {
        private int key;
        private String value;
        public int degree;
        private boolean marked;
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

        // Геттеры для тестирования
        public int getKey() { return key; }
        public String getValue() { return value; }
        public int getDegree() { return degree; }
        public boolean isMarked() { return marked; }
        public Node getParent() { return parent; }
        public Node getChild() { return child; }
        public Node getLeft() { return left; }
        public Node getRight() { return right; }

        @Override
        public String toString() {
            return String.valueOf(key) + (marked ? "*" : "");
        }
    }

    public FibonacciHeap() {
        this.min = null;
        this.size = 0;
    }

    public Node insert(int key, String value) {
        Node node = new Node(key, value);
        if (min == null) {
            min = node;
        } else {
            addToRootList(node);
            if (node.key < min.key) {
                min = node;
            }
        }
        size++;
        return node;
    }

    public void merge(FibonacciHeap other) {
        if (other == null || other.min == null) return;
        if (this.min == null) {
            this.min = other.min;
            this.size = other.size;
        } else {
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

    public Integer extractMin() {
        if (min == null) return null;

        Node extracted = min;
        int minKey = extracted.key;

        if (extracted.child != null) {
            addChildrenToRootList(extracted);
        }

        removeFromRootList(extracted);

        if (extracted == extracted.right) {
            min = null;
        } else {
            min = extracted.right;
            consolidate();
        }

        size--;
        return minKey;
    }

    private void addChildrenToRootList(Node parent) {
        Node child = parent.child;
        do {
            child.parent = null;
            child = child.right;
        } while (child != parent.child);

        Node minLeft = min.left;
        Node childLeft = parent.child.left;

        min.left = childLeft;
        childLeft.right = min;
        parent.child.left = minLeft;
        minLeft.right = parent.child;
    }

    private void removeFromRootList(Node node) {
        node.left.right = node.right;
        node.right.left = node.left;
    }

    private void addToRootList(Node node) {
        node.left = min.left;
        node.right = min;
        min.left.right = node;
        min.left = node;
    }

    private void consolidate() {
        int maxDegree = (int) Math.floor(Math.log(size) / Math.log(2)) + 1;
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

        rebuildRootList(degreeTable);
    }

    private void link(Node y, Node x) {
        removeFromRootList(y);
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

    private void rebuildRootList(Node[] degreeTable) {
        min = null;
        for (Node node : degreeTable) {
            if (node != null) {
                if (min == null) {
                    min = node;
                    min.left = min;
                    min.right = min;
                } else {
                    addToRootList(node);
                    if (node.key < min.key) {
                        min = node;
                    }
                }
            }
        }
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
    public Node getMin() { return min; }
    public int getSize() { return size; }
    public boolean isEmpty() { return size == 0; }

    public List<Integer> getRootKeys() {
        List<Integer> keys = new ArrayList<>();
        if (min == null) return keys;

        Node current = min;
        do {
            keys.add(current.key);
            current = current.right;
        } while (current != min);

        return keys;
    }

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
}