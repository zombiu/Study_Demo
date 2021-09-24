package com.hugo.study_dialog_demo.algo.sort;

import java.security.Policy;

/**
 * 快排,不稳定排序
 */
public class QuickSort extends BaseSort {

    @Override
    protected void sort() {
        sort(0, arrays.length);
    }

    protected void sort(int start, int end) {
        int pivotPoint = pivotPoint(start, end);
        sort(start, pivotPoint);
        sort(pivotPoint, end);
    }

    /**
     * 首先从右往左扫描
     *
     * @param start
     * @param end
     * @return
     */
    private int pivotPoint(int start, int end) {
        int pivot = arrays[start];
        int bi = start;
        int ei = end - 1;
        while (true) {
            while (bi < ei) {
                if (arrays[ei] < pivot) {
                    swap(ei, bi);
                    bi++;
                } else if (arrays[ei] > pivot) {
                    ei--;
                } else {
                    return bi;
                }
            }

            while (bi < ei) {
                if (arrays[bi] < pivot) {
                    bi++;
                } else if (arrays[bi] > pivot) {
                    swap(ei, bi);
                    ei--;
                } else {
                    return bi;
                }
            }
        }

    }
}
