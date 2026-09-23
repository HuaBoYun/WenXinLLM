package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.Controlmatrix;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
public interface IControlmatrixService extends IService<Controlmatrix> {

    Controlmatrix getControlmatrix(String conmatid);

    Controlmatrix getByFlowId(String flowid);

    List<Controlmatrix>  findTblControlmatrixByRiskCoping(String copingId);
    
    Map<String, Object> getControlmatrixCount(String orgid); 
    
    Map<String, Object> getControlmatrixList(String orgid,String type);

    
    
}
