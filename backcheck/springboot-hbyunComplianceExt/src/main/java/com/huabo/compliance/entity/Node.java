package com.huabo.compliance.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {
	/**
	  * 节点编号
	  */
	 public String id;
	 /**
	  * 节点内容
	  */
	 public String text;
	 /**
	  * 父节点编号
	  */
	 public String parentId;

	 public boolean checked;
	 /**
	  * 孩子节点列表
	  */
	 private Children children = new Children();
	 private List<Node> childrens = new ArrayList<Node>();

	 public String state;
	 // 先序遍历，拼接JSON字符串
	 public String toString() {
	  String result = "{"
	   + "\"id\" : \"" + id + "\""
	   + ", \"text\" : \"" + text + "\""
	   //+ ", \"state\" : \"closed\""
	   //+ ", \"checked\" : \""+checked+"\""
	   + ", \"state\" : \"" + state + "\""
	  + ", \"parentId\" : \"" + parentId + "\"";

	  if (childrens != null && childrens.size() != 0) {
	   result += ", \"children\" : " + childrens.toString();
	  } else {
	   result += ", \"leaf\" : true";
	  }

	  return result + "}";
	 }

	// 兄弟节点横向排序
	public void sortChildren() {
		if (children != null && children.getSize() != 0) {
			children.sortChildren();
		}
	}

	// 添加孩子节点
	public void addChild(Node node) {
		this.children.addChild(node);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<Node> getChildren() {
		return childrens;
	}

	public void setChildren(List<Node> children) {
		this.childrens = children;
	}
}
