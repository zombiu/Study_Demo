package com.hugo.study_dialog_demo.algo.sort;

/**
 * 插入排序，稳定的排序算法
 */
class InsertSort extends BaseSort {

    @Override
    protected void sort() {

        for (int i = 1; i < arrays.length; i++) {
            for (int j = i; j > 0; j--) {
                /*if (arrays[j - 1] > arrays[j]) {

                }*/
                if (cmp(j - 1, j)) {
                    swap(j, j - 1);
                }
            }
        }
    }
}
