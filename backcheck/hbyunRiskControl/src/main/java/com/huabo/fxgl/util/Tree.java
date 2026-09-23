package com.huabo.fxgl.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 树形菜单
 * @author SongXiangYing
 *
 */
public class Tree {

	private BigDecimal id;
	@Schema(name="路径")
	private String url;
	@Schema(name="父类id")
	private BigDecimal pId;
	private List<Tree> children =  new ArrayList<Tree>();
	@Schema(name="节点名")
	private String name;
	@Schema(name="")
	private String target;
	@Schema(name="是否父类")
	private Boolean isParent;
	@Schema(name="是否选中")
	private Boolean checked;
	@Schema(name="是否展开")
	private Boolean open;
	@Schema(name="类型")
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public BigDecimal getpId() {
		return pId;
	}
	public void setpId(BigDecimal pId) {
		this.pId = pId;
	}
	public List<Tree> getChildren() {
		return children;
	}
	public void setChildren(List<Tree> children) {
		this.children = children;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	
	
	
}
