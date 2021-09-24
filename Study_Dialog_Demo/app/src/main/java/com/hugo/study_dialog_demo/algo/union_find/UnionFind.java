package com.hugo.study_dialog_demo.algo.union_find;

/**
 * 并查集 通过链表+哈希表实现
 * rank优化 + 路径分裂/路径
 */
public class UnionFind<V> {

    private Node linkRoot;

    public static class Node<V> {
        Node parent;
        V value;
        int rank;
    }

    /*public int find(V v) {

    }

    public void union(int v1, int v2) {

    }

    public boolean isSame(int v1, int v2) {
        return false;
    }*/
}
