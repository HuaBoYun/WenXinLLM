package com.huabo.monitor.entity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 页面查找公共类
 *
 * @author taoyb
 */
public class CatVo  {
    private String name;//名称
    private String desc;
    private Double weight;
    private String asscatid;
    private String content;
    
    
    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAsscatid() {
		return asscatid;
	}

	public void setAsscatid(String asscatid) {
		this.asscatid = asscatid;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
