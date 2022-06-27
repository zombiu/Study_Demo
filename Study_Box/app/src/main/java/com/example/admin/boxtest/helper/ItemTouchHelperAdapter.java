package com.example.admin.boxtest.helper;

/**
 * 一个接口来实现Adapter和ItemTouchHelper之间涉及数据的操作
 */
public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition, int toPosition);

    //数据删除
    void onItemDissmiss(int position);
}
