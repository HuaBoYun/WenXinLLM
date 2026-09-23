package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 证书
 * @author Zxl
 *
 */
@Data
public class Cert {
	
	private Integer certid;
	/**
	 * 证书编号
	 */
	private String certNo;
	/**
	 * 证书id
	 */
	private String id;
	/**
	 * 证书名称
	 */
	private String certificateName;
	/**
	 * 开始时间
	 */
	private String startDate;
	/**
	 * 结束时间
	 */
	private String endDate;

	/**
	 * 证书明细
	 */
	private List<CertDetail> detail;
	
	private Integer companyid;
	private Date createdate;//保存数据时间


}
