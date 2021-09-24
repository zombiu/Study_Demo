package com.hugo.study_dialog_demo.algo.sort;

class BaseSort {
    protected int[] arrays;

    public BaseSort() {

    }

    public void sort(int[] arrays) {
        if (arrays.length < 2) {
            return;
        }
        this.arrays = arrays;
        sort();
    }

    protected void sort() {

    }

    /**
     * 交换索引对应元素的位置
     * @param i1
     * @param i2
     */
    protected void swap(int i1, int i2) {
        int tmp = arrays[i1];
        arrays[i1] = arrays[i2];
        arrays[i2] = tmp;
    }

    /**
     * 比较
     * @param i1
     * @param i2
     * @return
     */
    protected boolean cmp(int i1, int i2) {
        // 前面的元素 大于后面的元素时，才进行交换。
        return arrays[i1] > arrays[i2];
    }
}
