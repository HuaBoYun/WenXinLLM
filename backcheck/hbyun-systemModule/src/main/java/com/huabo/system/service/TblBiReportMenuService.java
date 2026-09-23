package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblBiReportMenu;

public interface TblBiReportMenuService {
    TblBiReportMenu geTblBiReportMenu(String id);

    Object isExistBiPageCode(String code);

    void updateReportMenu(TblBiReportMenu page);

    void saveReportMenu(TblBiReportMenu tbrm);

//    List<TblBiReportMenu> findByFatherid(String pid);

    void deleteReportMenu(TblBiReportMenu geTblBiReportMenu);

    TblBiReportMenu selectTblBiReportMenu(String idStr);

    void addPage(TblBiReportMenu page);

    List<TblBiReportMenu> selectByFatherid(String idStr);

    Map<String, Object> finreportMenuList(Integer pageNumber, Integer pageSize, String token, String staffId,String type);

    TblBiReportMenu geTblBiReport(BigDecimal pageid);

    /**
     * 主题仓库 删除
     * @param pageid
     * @param token 
     * @param type 
     * @return
     */
    JsonBean deleteReportMenuPageId(BigDecimal pageid, String token, String type) throws Exception;

    /**
     * 主题仓库下发至个人业务逻辑实现
     * @param token  - 用户登录验证令牌
     * @param type  - 下发主题类型 1-一级主题  2-二级主题
     * @param pageIds - 下发选中的主体主键
     * @param staffIds - 下发的用户主键
     * @param fatherId 
     * @return
     * @throws Exception
     */
	JsonBean distributeThemeReportToUser(String token, Integer type, String[] pageIds, String[] staffIds, String fatherId) throws Exception;

}
