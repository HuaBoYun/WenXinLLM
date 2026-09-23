package com.huabo.fxgl.mapper;

import com.huabo.fxgl.dto.MonthlyEvaluationDTO;
import com.huabo.fxgl.dto.ReminderDTO;
import com.huabo.fxgl.dto.RiskDatabaseDTO;
import com.huabo.fxgl.dto.RiskReviewAnalysisDTO;
import com.huabo.fxgl.dto.RiskEventCountDTO;
import com.huabo.fxgl.dto.RiskMeasureStatusDTO;
import com.huabo.fxgl.dto.RiskCompletionDTO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 催办提醒 Mapper 接口
 *
 * @author AI Assistant
 * @since 2025-01-07
 */
@Repository
public interface ReminderMapper {

    /**
     * 查询未读的催办提醒列表
     * 使用去重逻辑，每个业务表只保留最新的一条催办记录
     *
     * @return 催办提醒列表
     */
    @Select("SELECT " +
            "t1.REMINDERCONTENT AS nr, " +
            "t1.REMINDERBUSINESSTABLE AS lx, " +
            "t1.CREATOR AS xfr, " +
            "TO_CHAR(t1.CREATEDTIME, 'YYYY-MM-DD HH24:MI:SS') AS xfsj " +
            "FROM TBL_SYSTEM_REF_REMINDER_V t1 " +
            "WHERE t1.ID = ( " +
            "SELECT MAX(a.ID) " +
            "FROM TBL_SYSTEM_REF_REMINDER_V a " +
            "WHERE a.REMINDERBUSINESSTABLEID = t1.REMINDERBUSINESSTABLEID " +
            "AND a.CREATEDTIME >= SYSDATE - 7 " +
            ")" +
            "AND t1.CREATEDTIME >= SYSDATE - 7 " +
            "ORDER BY t1.ID DESC ")
    @Results({
            @Result(property = "nr", column = "nr"),
            @Result(property = "lx", column = "lx"),
            @Result(property = "xfr", column = "xfr"),
            @Result(property = "xfsj", column = "xfsj")
    })
    List<ReminderDTO> selectUnreadReminderList();

    /**
     * 查询月度评估情况一览表
     *
     * @return 月度评估情况列表
     */
    @Select("SELECT DISTINCT  R.RISKNUMBER,O.ORGNAME AS unitname,R.RISKNAME,R.RISKCREATEDT,R.RISKSTATUS, " +
            " ( SELECT e.RISK_CHANGE    " +
            "      FROM TBL_RISK_MONTHLY_EVALUATION e    " +
            "      WHERE R.RISKID = E.RISKID    " +
            "      ORDER BY E.CREATE_TIME DESC LIMIT 1) AS riskchange,    " +
            "      COALESCE(coping_count.coping_num, 0) AS copingcount    " +
            "FROM TBL_RISK_ASSPLAN_RISK s   " +
            " left join TBL_RISK r on s.riskid=r.riskid   " +
            " left join TBL_RISK_RISKMARKING a on a.assriskid=s.assriskid   " +
            " LEFT JOIN TBL_ORGANIZATION o ON R.UNIT = o.ORGID    " +
            " LEFT JOIN (    " +
            "  SELECT r.RISKID, COUNT(e.ID) AS coping_num    " +
            "  FROM TBL_RISK r    " +
            "   LEFT JOIN TBL_RISK_COPING c ON r.RISKID = c.RISKID    " +
            "   LEFT JOIN TBL_CONTROLMATRIX x ON c.RISKCOPINGID = x.RISKCOPINGID    " +
            "   LEFT JOIN TBL_CONTROL_ENTRIES e ON x.CONMATID = e.CONMATID " +
            "  GROUP BY r.RISKID    " +
            " ) coping_count ON R.RISKID = coping_count.RISKID    " +
            "            WHERE   a.asssatus=2")
    List<MonthlyEvaluationDTO> selectMonthlyEvaluationList();

    /**
     * 查询风险数据库一览表
     *
     * @return 风险数据库一览表列表
     */
    @Select("SELECT  " +
            "    r.RISKCATNAME AS RISKCATNAME,  " +
            "    COUNT(0) AS total_count,  " +
            "    SUM(CASE   " +
            "        WHEN ce.FIELD4 = '是' THEN 1   " +
            "        ELSE 0   " +
            "    END) AS null_risklevel_count,  " +
            "    SUM(CASE   " +
            "        WHEN ce.FIELD4 = '否'   " +
            "            OR ce.FIELD4 IS NULL   " +
            "            OR TRIM(ce.FIELD4) = '' THEN 1   " +
            "        ELSE 0   " +
            "    END) AS not_null_risklevel_count  " +
            "FROM  " +
            "    TBL_CONTROL_ENTRIES ce   " +
            "LEFT JOIN TBL_CONTROLMATRIX cm ON cm.CONMATID = ce.CONMATID  " +
            "LEFT JOIN TBL_RISK_COPING rcoping   ON rcoping.RISKCOPINGID = cm.RISKCOPINGID  " +
            "LEFT JOIN TBL_RISK r  ON r.RISKID = rcoping.RISKID  " +
            "GROUP BY  " +
            "    r.RISKCATNAME  " +
            "ORDER BY  " +
            "    r.RISKCATNAME")
    @Results({
            @Result(property = "riskcatname", column = "RISKCATNAME"),
            @Result(property = "totalCount", column = "total_count"),
            @Result(property = "completedCount", column = "null_risklevel_count"),
            @Result(property = "uncompletedCount", column = "not_null_risklevel_count")
    })
    List<RiskDatabaseDTO> selectRiskDatabaseList();

    /**
     * 查询风险审查情况分析(近12个月)
     *
     * @return 风险审查情况分析列表
     */
    @Select("SELECT " +
            "    o.ORGNAME, " +
            "    COUNT(*) AS risk_review_count " +
            "FROM TBL_RISK_REVIEW r " +
            "LEFT JOIN TBL_ORGANIZATION o ON r.STAFFDEPT = o.orgId " +
            "WHERE r.CREATETIME >= DATEADD(DAY, -360, GETDATE()) " +
            "GROUP BY r.STAFFDEPT, o.ORGNAME")
    @Results({
            @Result(property = "orgname", column = "ORGNAME"),
            @Result(property = "riskReviewCount", column = "risk_review_count")
    })
    List<RiskReviewAnalysisDTO> selectRiskReviewAnalysis();

    /**
     * 查询风险事件数统计
     *
     * @return 风险事件数统计列表
     */
    @Select("SELECT " +
            "    COUNT(CASE WHEN losseventcategory = 1 THEN 1 END) AS category1, " +
            "    COUNT(CASE WHEN losseventcategory = 2 THEN 1 END) AS category2, " +
            "    OCCUREDDEPARTMENT o " +
            "FROM TBL_RISKEVENT " +
            "GROUP BY OCCUREDDEPARTMENT ")
    @Results({
            @Result(property = "occurredDepartment", column = "o"),
            @Result(property = "category1", column = "category1"),
            @Result(property = "category2", column = "category2")
    })
    List<RiskEventCountDTO> selectRiskEventCount();

    /**
     * 查询风险措施状态统计
     *
     * @return 风险措施状态统计
     */
    @Select("SELECT  " +
            "    SUM(CASE   " +
            "        WHEN ce.FIELD4 = '是' THEN 1   " +
            "        ELSE 0   " +
            "    END) AS null_risklevel_count,  " +
            "    SUM(CASE   " +
            "        WHEN ce.FIELD4 = '否'   " +
            "            OR ce.FIELD4 IS NULL   " +
            "            OR TRIM(ce.FIELD4) = '' THEN 1   " +
            "        ELSE 0   " +
            "    END) AS not_null_risklevel_count  " +
            "FROM  " +
            "    TBL_CONTROL_ENTRIES ce   " +
            "LEFT JOIN TBL_CONTROLMATRIX cm ON cm.CONMATID = ce.CONMATID  " +
            "LEFT JOIN TBL_RISK_COPING rcoping   ON rcoping.RISKCOPINGID = cm.RISKCOPINGID  " +
            "LEFT JOIN TBL_RISK r  ON r.RISKID = rcoping.RISKID")
    @Results({
            @Result(property = "nullRisklevelCount", column = "null_risklevel_count"),
            @Result(property = "notNullRisklevelCount", column = "not_null_risklevel_count")
    })
    RiskMeasureStatusDTO selectRiskMeasureStatus();

    /**
     * 查询风险完成情况统计
     *
     * @return 风险完成情况统计列表
     */
    @Select("select a.GB, count(0) as count " +
            "from ( " +
            "    SELECT DISTINCT " +
            "        R.RISKID, " +
            "        CASE WHEN R.RISKSTATUS = 0 THEN 0 else 1 END gb " +
            "    FROM TBL_RISK_ASSPLAN_RISK s " +
            "    LEFT JOIN TBL_RISK r ON s.riskid = r.riskid " +
            "    LEFT JOIN TBL_RISK_RISKMARKING a ON a.assriskid = s.assriskid " +
            "    LEFT JOIN TBL_ORGANIZATION o ON R.UNIT = o.ORGID " +
            "    LEFT JOIN ( " +
            "        SELECT r.RISKID, COUNT(x.riskcopingid) AS coping_num " +
            "        FROM TBL_RISK r " +
            "        LEFT JOIN TBL_RISK_COPING c ON r.RISKID = c.RISKID " +
            "        LEFT JOIN TBL_CONTROLMATRIX x ON c.RISKCOPINGID = x.RISKCOPINGID " +
            "        GROUP BY r.RISKID " +
            "    ) coping_count ON R.RISKID = coping_count.RISKID " +
            "    WHERE a.asssatus = 2 and  s.assdate=(SELECT MAX(ASSDATE) FROM TBL_RISK_ASSPLAN_RISK WHERE RISKID = s.riskid) " +
            ") a " +
            "GROUP BY a.GB")
    @Results({
            @Result(property = "gb", column = "GB"),
            @Result(property = "count", column = "count")
    })
    List<RiskCompletionDTO> selectRiskCompletion();
}
