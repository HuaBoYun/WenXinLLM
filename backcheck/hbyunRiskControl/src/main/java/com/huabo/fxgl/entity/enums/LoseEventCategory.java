package com.huabo.fxgl.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LoseEventCategory implements IEnum<Integer> {
    GENERAL_EVENT("一般事件", 1), MAJOR_EVENT("重大事件", 2);

    @JsonValue
    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public void setValue(int value) {
        this.value = value;
    }

//    @JsonValue
    private String categoryText;
    private int value;
    LoseEventCategory(String categoryText, int value) {
        this.categoryText = categoryText;
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return categoryText;
    }
}
