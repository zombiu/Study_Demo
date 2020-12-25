package com.example.study_letterindexlist.bean;

import android.text.TextUtils;

import com.example.study_letterindexlist.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UIContact {
    //分组
    private String category;
    //排序的拼音
    private String sortName;
    private boolean showCategory = false;
    private MobileContact mobileContact;

    public UIContact(MobileContact mobileContact) {
        this.mobileContact = mobileContact;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public boolean isShowCategory() {
        return showCategory;
    }

    public void setShowCategory(boolean showCategory) {
        this.showCategory = showCategory;
    }

    public MobileContact getMobileContact() {
        return mobileContact;
    }

    public void setMobileContact(MobileContact mobileContact) {
        this.mobileContact = mobileContact;
    }

    public static UIContact wrap(MobileContact contact) {
        UIContact uiContact = new UIContact(contact);
        String indexLetter;
        String displayName = contact.getName();
        if (!TextUtils.isEmpty(displayName)) {
            String pinyin = PinyinUtils.getPinyin(displayName);
            char c = pinyin.toUpperCase().charAt(0);
            if (c >= 'A' && c <= 'Z') {
                indexLetter = c + "";
                uiContact.setSortName(pinyin);
            } else {
                indexLetter = "#";
                // 为了让排序排到最后
                uiContact.setSortName("{" + pinyin);
            }
            uiContact.setCategory(indexLetter);
        } else {
            uiContact.setSortName("");
        }
        return uiContact;
    }

    public static List<UIContact> wraps(List<MobileContact> contacts) {
        ArrayList<UIContact> list = new ArrayList<>();
        if (contacts == null || contacts.size() == 0) {
            return list;
        }
        for (MobileContact contact : contacts) {
            UIContact uiContact = UIContact.wrap(contact);
            list.add(uiContact);
        }

        Collections.sort(list, (o1, o2) -> o1.getSortName().compareToIgnoreCase(o2.getSortName()));

        String indexLetter;
        String preIndexLetter = null;
        for (UIContact info : list) {
            indexLetter = info.getCategory();
            if (preIndexLetter == null || !preIndexLetter.equals(indexLetter)) {
                info.setShowCategory(true);
            }
            preIndexLetter = indexLetter;
        }
        return list;
    }

    public static List<MobileContact> getData() {
        ArrayList<MobileContact> contacts = new ArrayList<>();
        MobileContact mobileContact1 = new MobileContact();
        mobileContact1.setName("碧海");
        contacts.add(mobileContact1);

        MobileContact mobileContact2 = new MobileContact();
        mobileContact2.setName("鱼龙");
        contacts.add(mobileContact2);

        MobileContact mobileContact3 = new MobileContact();
        mobileContact3.setName("诡秘之主");
        contacts.add(mobileContact3);

        MobileContact mobileContact4 = new MobileContact();
        mobileContact4.setName("革秦");
        contacts.add(mobileContact4);

        MobileContact mobileContact5 = new MobileContact();
        mobileContact5.setName("晚明");
        contacts.add(mobileContact5);

        MobileContact mobileContact6 = new MobileContact();
        mobileContact6.setName("长夜余火");
        contacts.add(mobileContact6);

        MobileContact mobileContact7 = new MobileContact();
        mobileContact7.setName("从姑获鸟开始");
        contacts.add(mobileContact7);

        MobileContact mobileContact8 = new MobileContact();
        mobileContact8.setName("我真没想重生啊");
        contacts.add(mobileContact8);

        MobileContact mobileContact9 = new MobileContact();
        mobileContact9.setName("重生之出人头地");
        contacts.add(mobileContact9);

        MobileContact mobileContact10 = new MobileContact();
        mobileContact10.setName("明天下");
        contacts.add(mobileContact10);

        MobileContact mobileContact11 = new MobileContact();
        mobileContact11.setName("一世之尊");
        contacts.add(mobileContact11);

        MobileContact mobileContact12 = new MobileContact();
        mobileContact12.setName("非洲酋长");
        contacts.add(mobileContact12);

        MobileContact mobileContact13 = new MobileContact();
        mobileContact13.setName("我有一座恐怖屋");
        contacts.add(mobileContact13);

        MobileContact mobileContact14 = new MobileContact();
        mobileContact14.setName("重生之官路商途");
        contacts.add(mobileContact14);

        MobileContact mobileContact15 = new MobileContact();
        mobileContact15.setName("狩魔手记");
        contacts.add(mobileContact15);

        MobileContact mobileContact16 = new MobileContact();
        mobileContact16.setName("惊悚乐园");
        contacts.add(mobileContact16);

        MobileContact mobileContact17 = new MobileContact();
        mobileContact17.setName("大圣传");
        contacts.add(mobileContact17);

        MobileContact mobileContact18 = new MobileContact();
        mobileContact18.setName("放开那个女巫");
        contacts.add(mobileContact18);

        MobileContact mobileContact19 = new MobileContact();
        mobileContact19.setName("地煞七十二变");
        contacts.add(mobileContact19);
        return contacts;
    }

    public static List<UIContact> getContacts() {
        return UIContact.wraps(getData());
    }
}
