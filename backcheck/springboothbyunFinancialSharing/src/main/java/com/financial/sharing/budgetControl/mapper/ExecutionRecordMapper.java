package com.financial.sharing.budgetControl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.budgetControl.dto.ReleaseRecordQueryParam;
import com.financial.sharing.budgetControl.dto.TransferRecordQueryParam;
import com.financial.sharing.budgetControl.entity.TblExecutionRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 执行记录Mapper接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface ExecutionRecordMapper extends BaseMapper<TblExecutionRecord> {

    /**
     * 分页查询释放记录, LEFT JOIN TBL_STAFF / TBL_ORGANIZATION / TBL_ACCOUNT_SUBJECT
     * 一并取操作人姓名 / 业务组织名称 / 科目名称
     *
     * 注意: resultType 是 TblExecutionRecord (不是 java.util.Map),
     * 因为达梦在 ResultType=Map 时会强行把 key 压成"全大写无下划线",
     * 即使 SELECT 里写了 AS "alias" 双引号别名也无效, 前端没法用驼峰拿到字段值.
     * 改用 entity 后, MyBatis 直接按属性名映射, 绕开这个达梦坑.
     */
    IPage<TblExecutionRecord> selectReleaseRecordPage(Page<TblExecutionRecord> page,
                                                     @Param("param") ReleaseRecordQueryParam param);

    /**
     * 不分页查询全部匹配的释放记录, 用于统计 (汇总金额/次数)
     */
    List<TblExecutionRecord> selectReleaseRecordList(@Param("param") ReleaseRecordQueryParam param);

    /**
     * 分页查询转移记录, 同样 LEFT JOIN TBL_STAFF / TBL_ORGANIZATION / TBL_ACCOUNT_SUBJECT
     * 拿姓名 / 业务组织名称 / 科目名称.
     * 跟 release 走的是同一张表 TBL_EXECUTION_RECORD, 靠 CONTROL_RESULT='TRANSFER' 区分.
     */
    IPage<TblExecutionRecord> selectTransferRecordPage(Page<TblExecutionRecord> page,
                                                      @Param("param") TransferRecordQueryParam param);

    /**
     * 不分页查询全部匹配的转移记录, 用于统计 (汇总金额/次数)
     */
    List<TblExecutionRecord> selectTransferRecordList(@Param("param") TransferRecordQueryParam param);

}

