package com.hugo.study_dialog_demo.algo.sort;

/**
 * 选择排序，是稳定的排序算法
 */
class SelectionSort extends BaseSort {

    public SelectionSort() {

    }

    @Override
    protected void sort() {
        int end = arrays.length - 1;
        for (int i = 0; i < arrays.length; i++) {
            int index = 0;
            // 选出最大的那个索引位置
            for (int j = 1; j <= end; j++) {
                if (cmp(index, j)) {
                    index = j;
                }
            }
            swap(index, end);
            end--;
        }
    }

    @Override
    protected boolean cmp(int i1, int i2) {
        return arrays[i1] <= arrays[2];
    }
}
