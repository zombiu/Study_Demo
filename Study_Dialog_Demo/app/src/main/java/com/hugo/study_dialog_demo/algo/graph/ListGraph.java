package com.hugo.study_dialog_demo.algo.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 图 邻接表实现
 *
 * @param <V>
 * @param <E>
 */
class ListGraph<V, E> implements Graph<V, E> {
    private Map<V, Vertex> vertexs = new HashMap<>();
    private Set<Edge> edgeSet = new HashSet<>();

    class Vertex<V, E> {
        Set<Edge<V, E>> inEdges;
        Set<Edge<V, E>> outEdges;
        V value;
    }

    class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        int weight;
    }

    /**
     * 最小生成树
     */
    public void mst() {
        HashMap<V, Vertex<V, E>> vertexMap = new HashMap<>();
        HashSet<Edge> edges = new HashSet<>();
        Vertex<V, E> vertex = null;

        // 存放所有参与切分的edge
        HashSet<Edge> remainEdges = new HashSet<>();
        while (edges.size() < vertexs.size() - 1) {
            vertexMap.put(vertex.value, vertex);
            // 无向图 使用 inEdges 或者 outEdges都可以
            // 寻找权值最小的边
            int min = 0;
            Edge[] objects = (Edge[]) vertex.outEdges.toArray();
            for (int i = 1; i < vertex.outEdges.size(); i++) {
                if (objects[min].weight > objects[i].weight) {
                    min = i;
                } else {
                    remainEdges.add(objects[i]);
                }
            }
            Edge<V, E> minWeightEdge = objects[min];
            edges.add(minWeightEdge);
            vertexMap.put(minWeightEdge.to.value, minWeightEdge.to);
        }
    }
}
