package com.example.admin.boxtest.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/chaychan
 *
 * @author ChayChan
 * @description: NormalEntity,need not implements MultiItemEntity interface
 * @date 2018/3/30  11:13
 */

public class NormalMultipleEntity {

    public static final int FIRST_ITEM = 1;
    public static final int SECOND_ITEM = 2;
    public static final int NORMAL_ITEM = 3;

    public int type;
    public String content;

    public NormalMultipleEntity(int type) {
        this.type = type;
    }

    public NormalMultipleEntity(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public static List<NormalMultipleEntity> getNormalMultipleEntities() {
        List<NormalMultipleEntity> list = new ArrayList<>();
        for (int i = 0; i <= 0; i++) {
            list.add(new NormalMultipleEntity(NormalMultipleEntity.FIRST_ITEM));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.SECOND_ITEM));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
//            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
//            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
//            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
//            list.add(new NormalMultipleEntity(NormalMultipleEntity.NORMAL_ITEM));
        }
        return list;
    }
}
