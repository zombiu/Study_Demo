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
    private boolean showCategory = true;
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
        if (contacts == null || list.size() == 0) {
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
        return contacts;
    }

    public static List<UIContact> getContacts() {
        return UIContact.wraps(getData());
    }
}
