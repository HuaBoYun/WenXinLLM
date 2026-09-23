package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 经营异常
 * @author tj
 *
 */
@Data
public class Abnormal {
	private Integer abnormalid;
	private String createTime;//创建时间
    private String putDate;//发布时间
    private String removeDate;//移除日期
    private String removeDepartment;//移除部门
    private String removeReason;//移除原因
    private String putReason;//发布原因
    private String putDepartment;//发布部门
    private Date createdate;//保存数据时间

	 
}
