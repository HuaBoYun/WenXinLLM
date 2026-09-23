package com.huabo.contract.entity.yy;
/**
 * 节点关系
 * @author Administrator
 *
 */
public class Relationships {

	private String id ;
	private String type ;//类型
	private String startNode ;//开始节点id
	private String endNode ;//结束节点id
	private String properties ;//关系信息
	private Propertie propertie;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStartNode() {
		return startNode;
	}
	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}
	public String getEndNode() {
		return endNode;
	}
	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public Propertie getPropertie() {
		return propertie;
	}
	public void setPropertie(Propertie propertie) {
		this.propertie = propertie;
	}
	
	
}
