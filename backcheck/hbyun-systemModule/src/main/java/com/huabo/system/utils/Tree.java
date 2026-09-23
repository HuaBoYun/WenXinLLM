package com.huabo.system.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.v3.oas.annotations.media.Schema;

public class Tree {

    private BigDecimal id;
    private String url;
    private BigDecimal pId;
    private List<Tree> children = new ArrayList();
    private String name;
    private String target;
    private Boolean isParent;
    private Boolean checked;
    private Boolean open;
    private String type;
    private Boolean disabled;
    
    private String bgimage;
    
    private String bgname;
    
    
    private String logoimage;
    
    private String logoname;
    
    
    private String jdztimage;
    
    private String jdztname;
    
    private String ctztimage;
    
    private String ctztname;
    
    private String baname;
    

    public String getBaname() {
		return baname;
	}

	public void setBaname(String baname) {
		this.baname = baname;
	}

	public String getBgimage() {
		return bgimage;
	}

	public void setBgimage(String bgimage) {
		this.bgimage = bgimage;
	}

	public String getBgname() {
		return bgname;
	}

	public void setBgname(String bgname) {
		this.bgname = bgname;
	}

	public String getLogoimage() {
		return logoimage;
	}

	public void setLogoimage(String logoimage) {
		this.logoimage = logoimage;
	}

	public String getLogoname() {
		return logoname;
	}

	public void setLogoname(String logoname) {
		this.logoname = logoname;
	}

	public String getJdztimage() {
		return jdztimage;
	}

	public void setJdztimage(String jdztimage) {
		this.jdztimage = jdztimage;
	}

	public String getJdztname() {
		return jdztname;
	}

	public void setJdztname(String jdztname) {
		this.jdztname = jdztname;
	}

	public String getCtztimage() {
		return ctztimage;
	}

	public void setCtztimage(String ctztimage) {
		this.ctztimage = ctztimage;
	}

	public String getCtztname() {
		return ctztname;
	}

	public void setCtztname(String ctztname) {
		this.ctztname = ctztname;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Tree() {
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getpId() {
        return this.pId;
    }

    public void setpId(BigDecimal pId) {
        this.pId = pId;
    }

    public List<Tree> getChildren() {
        return this.children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Boolean getIsParent() {
        return this.isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public Boolean getChecked() {
        return this.checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getOpen() {
        return this.open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

}
