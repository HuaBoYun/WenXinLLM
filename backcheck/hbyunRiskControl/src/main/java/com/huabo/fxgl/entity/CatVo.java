package com.huabo.fxgl.entity;

/**
 * 页面查找公共类
 *
 * @author taoyb
 */
public class CatVo  {
    private String name;//名称
    private String repdesc;
    private Double weight;
    private String asscatid;
    
    
    
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

    public String getRepdesc() {
        return repdesc;
    }

    public void setRepdesc(String repdesc) {
        this.repdesc = repdesc;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
