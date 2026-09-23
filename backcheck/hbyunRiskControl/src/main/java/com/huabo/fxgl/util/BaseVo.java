package com.huabo.fxgl.util;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *@ClassName BaseVo
 *@Description 实体类公共属性
 *@Author ZiYao
 *@Date 2022/4/12 16:25
 *@Version 1.0
 **/
@Schema(name="风险报送-分公司上报-单位月度风险评估表")
@Data
@Accessors(chain = true)
public abstract class BaseVo extends Model {
	
	@Schema(name="每页分页数量")
    Integer pageSize=15;
    
	@Schema(name="当前页数")
    Integer pageNum=1;
}
