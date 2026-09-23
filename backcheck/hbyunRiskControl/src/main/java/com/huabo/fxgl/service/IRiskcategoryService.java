package com.huabo.fxgl.service;

import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Riskcategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskcategoryService extends IService<Riskcategory> {
    Riskcategory findQYFXByOrgid(String orgid,String moduleType);
    
    /**
     * 查询风险类型
     * @param orgid
     * @param moduletype
     * @param riskcatName
     * @return
     */
    List<Riskcategory> getRiskCateTreeByOrgId(String orgid, String moduletype, String riskcatName);

    void deleteRiskcategory(Riskcategory riskcategory);
    List<BigDecimal> findRiskcatidByChildNode(Riskcategory riskcategory);

    public boolean findTblRiskOrganBynumber(String number,String org);

    public boolean findRiskOrganByName(String name,String unit,String moduletype);


    public JsonBean riskQueryLeft(String orgid, TblOrganizationUtil organization, String treeName);

    public Riskcategory geTblRiskcategory(BigDecimal riskcatId);
    List<Riskcategory> findRiskCateByRoot(BigDecimal rootId);

    String[] findRiskCateParentByAssPanid(String toString);


    List<BigDecimal> findRiskcatidsByChildNode1(String riskcatid);

    /**
     * 根据传入的公司ID和类型分类，初始化三个基本风险类型
     * @param orgid
     * @param type
     */
    
    void initRiskCategory(String orgid,String type);

   // String findRiskcatidByChildNode(String riskcatid);
}
