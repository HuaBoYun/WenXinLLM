package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.DesignPaymentInfoEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName DesignPaymentInfoMapper
 * @Description
 * @DATE 2023/9/30
 */
public interface DesignPaymentInfoMapper extends BaseMapper<DesignPaymentInfoEntity> {

    @SelectProvider(method="selectByEntity",type=DesignPaymentInfoMapperSqlConfig.class)
    @Results(id="designPaymentInfoResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PLAN_ID", property = "planId"),
            @Result(column = "NO", property = "no"),
            @Result(column = "CONTRACT_NO", property = "contractNo"),
            @Result(column = "ORG_ID", property = "org", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "ORG_ID", property = "orgId"),
            @Result(column = "CONTRACT_AMOUNT", property = "contractAmount"),
            @Result(column = "SETTLEMENT_AMOUNT", property = "settlementAmount"),
            @Result(column = "MATERIAL_AMOUNT", property = "materialAmount" ),
            @Result(column = "PAID_AMOUNT", property = "paidAmount" ),
            @Result(column = "BALANCE_PAYMENT", property = "balancePayment" ),
            @Result(column = "FIRST_TRIAL_TIME", property = "firstTrialTime" ),
            @Result(column = "FIRST_TRIAL_AMOUNT", property = "firstTrialAmount" ),
            @Result(column = "SECOND_TRIAL_TIME", property = "secondTrialTime" ),
            @Result(column = "SECOND_TRIAL_AMOUNT", property = "secondTrialAmount" ),
            @Result(column = "DRAWING_DESIGN_TIME", property = "drawingDesignTime" ),
            @Result(column = "CONTRACT_START_TIME", property = "contractStartTime" ),
            @Result(column = "CONTRACT_END_TIME", property = "contractEndTime"),
            @Result(column = "WORK_START_TIME", property = "workStartTime"),
            @Result(column = "WORK_END_TIME", property = "workEndTime"),
            @Result(column = "DELAY_TIMES", property = "delayTimes"),
            @Result(column = "DELAY_DAYS", property = "delayDays"),
            @Result(column = "DELAY_REASON", property = "delayReason"),
            @Result(column = "ARCHIVE_TIME", property = "archiveTime"),
            @Result(column = "BIDDING_SITUATION", property = "biddingSituation"),
            @Result(column = "IS_CONSISTENT_CON_PLAN", property = "isConsistentConPlan"),
            @Result(column = "IS_CONSISTENT_CON_WORK", property = "isConsistentConWork"),
            @Result(column = "REMARK", property = "remark"),
            @Result(column = "ID", property= "attachments", javaType = List.class, many = @Many(select= "selectAttachmentById"))
    })
    List<DesignPaymentInfoEntity> selectByEntity( DesignPaymentInfoEntity designPaymentInfoEntity) ;

    @SelectProvider(method="selectCountByEntity",type=DesignPaymentInfoMapperSqlConfig.class)
    Integer selectCountByEntity( DesignPaymentInfoEntity designPaymentInfoEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_DESIGN_PAYMENT WHERE ID = #{id}")
    @ResultMap(value= "designPaymentInfoResultMap")
    DesignPaymentInfoEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=DesignPaymentInfoMapperSqlConfig.class)
    void updateEntity(DesignPaymentInfoEntity designPaymentInfoEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=DesignPaymentInfoMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(DesignPaymentInfoEntity designPaymentInfoEntity);

    @DeleteProvider(method="deleteByIds", type=DesignPaymentInfoMapperSqlConfig.class)
    void deleteEntity(String ids);

    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_DESIGN_PAYMENT_ATT WHERE DPID = #{id})")
    List<TblAttachment> selectAttachmentById(BigDecimal id);

    @DeleteProvider(method="deleteAttachmentByIds", type=DesignPaymentInfoMapperSqlConfig.class)
    void deleteAttachmentByIds(String ids);

    @InsertProvider(method="insertAttachments", type=DesignPaymentInfoMapperSqlConfig.class)
    void insertAttachmentsWidthId(BigDecimal id, String attachmentId);
}
