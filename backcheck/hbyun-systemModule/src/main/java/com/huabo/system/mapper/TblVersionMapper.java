package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.ProcessSetting;
import com.huabo.system.entity.TblVersion;
import com.huabo.system.entity.TblYyUserQuery;
import com.huabo.system.page.PageBean;
import com.hbfk.util.PageInfo;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface TblVersionMapper extends BaseMapper<TblVersion> {


    @Select("select * from BATHDATA.TBL_VERSION where FID= #{fid}")
    List<TblVersion> findByfid(String fid);

    @Select("select * from BATHDATA.TBL_VERSION where FID!=100 order by SORT")
    List<TblVersion> findBysql();

}
