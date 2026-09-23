package com.huabo.audit.oracle.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 树形菜单
 * @author SongXiangYing
 *
 */
@Data
public class TargetTypeTreeEntity {
	//目标类型id
	private Integer targetId;
	//父id
	private Integer parentId;
	//子集
	private List<TargetTypeTreeEntity> children =  new ArrayList<TargetTypeTreeEntity>();
	//目标名称
	private String targetName;
	//是否父节点
	private Boolean isParent;
	//是否能打开
	private Boolean open;
	//类型
	private String type;
}
