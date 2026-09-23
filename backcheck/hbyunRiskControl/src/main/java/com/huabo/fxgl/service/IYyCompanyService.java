package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.YyCompany;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-02
 */
public interface IYyCompanyService extends IService<YyCompany> {
    PageInfo<YyCompany> findCompanyListByTeamid(Integer pageNo,Integer pageSize, Find find, Integer teamid,String fxjktype);
    PageInfo<YyCompany> findCompanyList(Integer pageNo,Integer pageSize, Find find, BigDecimal orgid, BigDecimal staffid,String fxjktype);
    boolean save(YyCompany yyCompany);
    YyCompany getById(BigDecimal companyId);
    
    void deleteComPageByCompanyId(BigDecimal companyId);

    void deleteComPageByTeamId(BigDecimal teamid);

    boolean updateEntity(YyCompany yyCompany);

    /**
     * 保存/更新时直接用 stepid 字符串列表写入 TBL_YY_COMPANY_PAGE
     */
    void insertPageIdsByStrList(BigDecimal companyId, List<String> pageIdList);

}
