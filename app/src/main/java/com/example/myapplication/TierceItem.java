package com.example.myapplication;

import java.util.ArrayList;

public class TierceItem {
    private int mNumber;
    private String level;
    private int value;
    private ArrayList<TierceItem> tierceItemList;
    private int columnIndex;

    public TierceItem(String level, int value, int number, int columnIndex) {
        this.level = level;
        this.value = value;
        this.mNumber = number;
        this.tierceItemList = new ArrayList<>();
        this.columnIndex = columnIndex;
    }

    public String getLevel() {
        return level;
    }

    public int getNumber() {
        return mNumber;
    }

    public int getValue() {
        return value;
    }

    public ArrayList<TierceItem> getTierceItemList() {
        return tierceItemList;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void addTierceItem(TierceItem tierceItem) {
        tierceItemList.add(tierceItem);
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getText() {
        return 0;
    }
}
