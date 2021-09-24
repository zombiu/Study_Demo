package com.hugo.study_dialog_demo.algo.sort;

/**
 * 冒泡排序
 */
class BubbleSort {
    private int[] arrays;

    public void sort(int[] arrays) {
        if (arrays.length < 2) {
            return;
        }
        this.arrays = arrays;

        int end = arrays.length - 1;
        for (int i = 1; i < arrays.length; i++) {
            boolean isSort = true;
            for (int j = i; j <= end; j++) {
                if (cmp(i - 1, i)) {
                    swap(i, i - 1);
                    isSort = false;
                }
            }
            if (isSort) {
                break;
            }
            end--;
        }
    }

    private void swap(int i1, int i2) {
        int tmp = arrays[i1];
        arrays[i1] = arrays[i2];
        arrays[i2] = tmp;
    }

    public boolean cmp(int i1, int i2) {
        // 前面的元素 大于后面的元素时，才进行交换。
        if (arrays[i1] > arrays[i2]) {
            return true;
        }
        return false;
    }
}
