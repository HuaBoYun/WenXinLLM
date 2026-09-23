package com.huabo.contract.entity.yy;

import java.util.List;

/**
 * 关系图谱
 * @author tj
 *
 */
public class Relationship {

	private List<Nodes> nodes;
	private List<Relationships> relationships;
	public List<Nodes> getNodes() {
		return nodes;
	}
	public void setNodes(List<Nodes> nodes) {
		this.nodes = nodes;
	}
	public List<Relationships> getRelationships() {
		return relationships;
	}
	public void setRelationships(List<Relationships> relationships) {
		this.relationships = relationships;
	}
	
}
