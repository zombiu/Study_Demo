package com.hugo.study_dialog_demo.algo.union_find;

import java.util.HashMap;
import java.util.Objects;

/**
 * 支持自定义数据的 并查集 通过链表+哈希表实现
 * 基于rank优化 + 路径分裂/路径
 */
public class UnionFind<V> {

    // v 自定义对象 Node 对应的节点
    private HashMap<V, Node> hashMap = new HashMap<>();

    public static class Node<V> {
        Node parent;
        V value;
        int rank = 1;

        public Node(V value) {
            this.value = value;
        }

    }

    /**
     * 寻找根节点
     * find的时候 对rank进行更新
     *
     * @param v
     * @return
     */
    public V find(V v) {
        Node node = hashMap.get(v);
        while (node != node.parent) {
            Node oldNode = node;
            node = node.parent;
            // 对rank进行更新
            node.rank = oldNode.rank + 1;
        }
        return (V) node.value;
    }

    public void union(V v1, V v2) {
        // v1的根节点
        Node node1 = hashMap.get(v1);

        if (node1 == null) {
            node1 = new Node(v1);
            node1.parent = node1;
            hashMap.put(v1, node1);
        }

        while (node1 != node1.parent) {
            node1 = node1.parent;
        }
        // v2的根节点
        Node node2 = hashMap.get(v2);
        if (node2 == null) {
            node2 = new Node(v1);
            node2.parent = node2;
            hashMap.put(v2, node2);
        }
        while (node2 != node2.parent) {
            node2 = node2.parent;
        }
        // 已经在同一个集合里面
        if (node1 == node2) {
            return;
        }

        if (node1.rank > node2.rank) {
            node2.parent = node1;
        } else {
            node1.parent = node2;
        }
    }

    public boolean isSame(V v1, V v2) {
        return Objects.equals(find(v1), find(v2));
    }
}
