package com.huabo.etl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.etl.domain.KettleJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName : KettleJobLogMapper
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2022-12-03 15:51:18
 */
@Mapper
public interface KettleJobLogMapper extends BaseMapper<KettleJobLog> {

}
