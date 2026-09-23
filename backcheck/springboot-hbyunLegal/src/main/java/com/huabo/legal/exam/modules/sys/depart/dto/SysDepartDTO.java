package com.huabo.legal.exam.modules.sys.depart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* <p>
* 部门信息数据传输类
* </p>
*
* @author 聪明笨狗
* @since 2020-09-02 17:25
*/
@Data
@Schema(name="部门信息", description="部门信息")
public class SysDepartDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @Schema(name = "ID", required = true)
    private String id;
    
    @Schema(name = "1公司2部门", required = true)
    private Integer deptType;
    
    @Schema(name = "所属上级", required = true)
    private String parentId;
    
    @Schema(name = "部门名称", required = true)
    private String deptName;
    
    @Schema(name = "部门编码", required = true)
    private String deptCode;
    
    @Schema(name = "排序", required = true)
    private Integer sort;
    
}
