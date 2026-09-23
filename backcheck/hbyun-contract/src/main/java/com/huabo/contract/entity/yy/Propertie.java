package com.huabo.contract.entity.yy;


import lombok.Data;

/**
 * 节点信息
 * @author Administrator
 *
 */
@Data
public class Propertie {
	private String ntype; 
	private String name; 
	private String id;
	private String staffId;
	private String staffTypeName;
	private String companyId; 
	private String labels;//关系类型
	
}
