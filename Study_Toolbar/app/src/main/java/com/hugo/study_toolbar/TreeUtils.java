package com.hugo.study_toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtils {
    public static Map<Integer, List<Item>> childrenMap = new HashMap<>();

    public static class Item {
        public int id;
        public int parentId;
        public long timestamp;

        public Item(int id, int parentId, long timestamp) {
            this.id = id;
            this.parentId = parentId;
            this.timestamp = timestamp;
        }
    }

    // 父 子 孙 任务 如果 我只有父和孙 任务 我这个怎么处理
    public static List<Item> buildTree(List<Item> items) {
        List<Item> root = new ArrayList<>();

        for (Item item : items) {
            // root里面保存的必然是 没有父任务的 根任务
            if (item.parentId == 0) {
                root.add(item);
            } else {
                // parentId 不等于0时， 就说明是parentId父任务的子任务
                // 不存在就put
                childrenMap.putIfAbsent(item.parentId, new ArrayList<>());
                //  父id 对应 子item list
                childrenMap.get(item.parentId).add(item);
            }
        }

//        buildTreeRecursively(root, childrenMap);

        // 在这里 对root 按规则(比如 时间戳) 进行排序
        sortTree(root, Collections.reverseOrder()); // 按照时间戳倒序排序
        return root;
    }

    private static void buildTreeRecursively(List<Item> parentItems, Map<Integer, List<Item>> childrenMap) {
        for (Item item : parentItems) {
            List<Item> children = childrenMap.get(item.id);
            if (children != null) {
                buildTreeRecursively(children, childrenMap);
            }
        }
    }

    private static void sortTree(List<Item> items, Comparator<Item> comparator) {
        if (items.isEmpty()) {
            return;
        }
        Collections.sort(items, comparator); // 对子项目进行排序
        for (Item item : items) {
            sortTree(childrenMap.get(item.id), comparator); // 递归对子项目的子项目进行排序
        }
    }

    /**
     * 将树状结构数据 平铺到list中
     *
     * @param roots
     * @param result
     * @return
     */
    public static void getUnfoldElements(List<Item> roots, List<Item> result) {
        for (Item item : roots) {
            result.add(item);
            getUnfoldElements(childrenMap.get(item.id), result);
        }
    }
}