package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.entity.TblRisk;
import com.huabo.monitor.entity.TblRiskFlow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TblRiskMapper extends BaseMapper<TblRisk> {

    @Select("SELECT * FROM TBL_RISK WHERE RISKID IN (SELECT RISKID FROM TBL_RISK_FLOW WHERE FLOWID = #{flowid} ) ORDER BY RISKID DESC")
    List<TblRisk> findBysql(BigDecimal flowid);

    @Select("select * from TBL_RISK where riskid = #{riskid}")
    List<TblRisk> findByTiskid(String riskid);

    @Delete("delete from TBL_RISK where RISKID = #{riskid}")
    void deleteByriskID(Long riskid);

    @Insert("INSERT INTO TBL_RISK_FLOW (RISKID,FLOWID) VALUES (#{riskid},#{flowid})")
    void insertByzj(TblRiskFlow tblRiskFlow);

}
