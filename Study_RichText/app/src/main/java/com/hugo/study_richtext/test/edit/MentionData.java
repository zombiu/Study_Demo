package com.hugo.study_richtext.test.edit;

import com.bobomee.android.mentions.model.FormatRange;
import com.bobomee.android.mentions.model.Range;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MentionData {
    private String id;
    private String name;
    private int start;
    private int end;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public static JSONObject convertJsonObject(Range range, Tag tag) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", tag.getTagId());
        jsonObject.put("name", tag.getTagLable());
        jsonObject.put("start", range.getFrom());
        jsonObject.put("end", range.getTo());
        return jsonObject;
    }

    public static MentionData convertMention(Range range, Tag tag) {
        MentionData mentionData = new MentionData();
        mentionData.setId(tag.getTagId().toString());
        mentionData.setName(tag.getTagLable().toString());
        mentionData.setStart(range.getFrom());
        mentionData.setEnd(range.getTo());
        return mentionData;
    }

    public Range convertRange() {
        FormatRange range = new FormatRange(start, end);
        Tag tag = new Tag(name, id);
        range.setInsertData(tag);
        return range;
    }

    public static Range convertRange(JSONObject jsonObject) {
        String id = jsonObject.optString("id");
        String name = jsonObject.optString("name");
        int start = jsonObject.optInt("start");
        int end = jsonObject.optInt("end");
        FormatRange range = new FormatRange(start, end);
        Tag tag = new Tag(name, id);
        range.setInsertData(tag);
        return range;
    }

    public static List<Range> convertRanges(JSONArray jsonArray) {
        List<Range> ranges = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject opt = jsonArray.optJSONObject(i);
            Range range = convertRange(opt);
            ranges.add(range);
        }
        Collections.sort(ranges);
        return ranges;
    }
}
