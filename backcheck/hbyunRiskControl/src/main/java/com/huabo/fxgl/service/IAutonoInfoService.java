package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.AutonoInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-15
 */
public interface IAutonoInfoService extends IService<AutonoInfo> {
    public String findFlowNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer noId, String chChoiceCol, String choiceVal, String bjf);
    public Integer getIsUseAutoNoInfo(BigDecimal orgid);
    String findNumberLevelNexidByParent(Integer noId, String parentNumberCol, String parentTblName, String parentIdCol, String parentId,
                                        String chilNumberCol, String chilTblName, String chilOrgCol, BigDecimal orgid, Map<String, String> choiceMap);
    
    
    
    public String findRootNumberByParentId(String chilNumberCol,
                                           String chilTblName, String chilParentCol, String parentIdCol,
                                           String parentTblName,String parnetOrgCol, BigDecimal orgid,Integer noId,
                                           String middleTblname,String middleChilCol,String middleParentCol,String type);


    /**
     * 传入参数说明 tabName ---- 插入编号所在的表 列入 tbl_flow column --- 编号的列名 例如 tbl_flow 表中的
     * FLOWNUMBER orgCol --- 编号所在的组织的列名 列入 tbl_flow 表中 COMPANY orgid ----- 组织ID noId
     * ------ TBL_AUTONO_INFO的主键标识 列入 流程编号就是3 chChoiceCol -------
     * 插入编号所在的表添加额外条件的列名，例如风险分类里的 MODULETYPE choiceVal ---------
     * 插入编号所在的表添加额外条件的值，例如风险分类里的 MODULETYPE 的值FXSJK
     */
    String selectFlowNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer noId,
                            String chChoiceCol, String choiceVal, String bjf) throws Exception;
    
    
}
