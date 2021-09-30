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

    /**
     * [start,end) 左闭右开
     *
     * @param start
     * @param end
     */
    protected void sort(int start, int end) {
        // 最少两个时，
        if (end - start < 2) {
            return;
        }
        int pivotPoint = pivotPoint(start, end);
        sort(start, pivotPoint);
        sort(pivotPoint, end);
    }

    /**
     * 首先从右往左扫描，遇到比轴点小的，就交换，
     * 再从左往右扫描，遇到比轴点大的，就交换，
     * 重复执行以上步骤
     *
     * @param start
     * @param end
     * @return
     */
    private int pivotPoint(int start, int end) {
        int pivot = arrays[start];
        int bi = start;
        int ei = end - 1;
        while (bi < ei) {
            while (bi < ei) {
                if (arrays[ei] <= pivot) {
                    swap(ei, bi);
                    bi++;
                    break;
                } else if (arrays[ei] > pivot) {
                    ei--;
                }
            }

            while (bi < ei) {
                if (arrays[bi] < pivot) {
                    bi++;
                } else if (arrays[bi] >= pivot) {
                    swap(ei, bi);
                    ei--;
                    break;
                }
            }
        }
        return bi;
    }
}
