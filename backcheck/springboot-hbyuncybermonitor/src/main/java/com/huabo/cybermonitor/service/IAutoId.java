package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

public interface IAutoId {

    /**
     * 传入参数说明 tabName ---- 插入编号所在的表 列入 tbl_flow column --- 编号的列名 例如 tbl_flow 表中的
     * FLOWNUMBER orgCol --- 编号所在的组织的列名 列入 tbl_flow 表中 COMPANY orgid ----- 组织ID noId
     * ------ TBL_AUTONO_INFO的主键标识 列入 流程编号就是3 chChoiceCol -------
     * 插入编号所在的表添加额外条件的列名，例如风险分类里的 MODULETYPE choiceVal ---------
     * 插入编号所在的表添加额外条件的值，例如风险分类里的 MODULETYPE 的值FXSJK
     *
     *  项目编号生成
     */
    String findFlowNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer NoId,
                          String chChoiceCol, String choiceVal, String bjf) throws Exception;

}
