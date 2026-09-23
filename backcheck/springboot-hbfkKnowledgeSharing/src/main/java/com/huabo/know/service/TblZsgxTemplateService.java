package com.huabo.know.service;

import com.hbfk.util.JsonBean;
import com.huabo.know.entity.TblZsgxTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.know.vo.param.TemplateListParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
public interface TblZsgxTemplateService extends IService<TblZsgxTemplate> {

    /**
     * 查询模板类型树形结构
     * @return
     * @throws Exception
     */
    JsonBean getTemplateCategoryTree() throws Exception;

    /**
     * 查询模板详情
     * @return
     * @throws Exception
     */
    JsonBean getTemplateDetail(Integer templateNumber) throws Exception;

    /**
     * 根据模板number查询模板数据
     * @param templateNumber
     * @return
     */
    TblZsgxTemplate getTemplateByNumber(Integer templateNumber) throws Exception;

    List<TblZsgxTemplate> getTemplateByNumbers(List<Integer> numbers);

    /**
     * list
     * @return
     */
    JsonBean list(TemplateListParam param) throws Exception;

}
