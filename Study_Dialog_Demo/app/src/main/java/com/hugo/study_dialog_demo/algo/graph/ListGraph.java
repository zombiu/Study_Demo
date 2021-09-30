package com.hugo.study_dialog_demo.algo.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * 图 邻接表实现
 *
 * @param <V>
 * @param <E>
 */
class ListGraph<V, E> implements Graph<V, E> {
    private Set<Vertex> vertexSet = new HashSet<>();
    private Set<Edge> edgeSet = new HashSet<>();

    class Vertex<V, E> {
        Set<Edge<V, E>> inEdges;
        Set<Edge<V, E>> outEdges;
        V value;
    }

    class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        E weight;
    }

    /**
     * 最小生成树
     */
    public void mst() {

    }
}
