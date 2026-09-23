package com.huabo.financialdata.mapper;

import com.huabo.financialdata.entity.entity.AccBook;
import com.huabo.financialdata.entity.entity.AccPeriod;
import com.huabo.financialdata.entity.entity.AccReportMprofit;
import com.huabo.financialdata.entity.entity.AccReportYbal;
import com.huabo.financialdata.entity.vo.prjData.ZcfzbRequestVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrjDataMapper {

    List<AccBook> findAccBookUrl(AccBook accBook);

    //获取账套数据
    List findAllZcfzb(ZcfzbRequestVo zcfzbRequestVo);

    //资产负债表查询数据
    List<AccPeriod> findAllAccPeriod(ZcfzbRequestVo zcfzbRequestVo);

    //资产负债表更新数据
    int updateTblAccReportYbal(AccReportYbal accReportYbal);

    //资产负债表查询数据
    List findAllAccSum(@Param("dbSource") String dbSource, @Param("strContext") String[] strContext, @Param("strYear") String strYear, @Param("strMonth") String strMonth);

    //获取利润表所有数据
    List findAllMprofit(@Param("dbSource") String dbSource);

    //修改当年主营业务收入和当年本年利润
    int updateTblAccReportMprofit(AccReportMprofit accReportMprofit);
}
