package com.huabo.system.service;


import com.huabo.system.entity.TblLoginType;

import java.util.Map;

public interface TblLoginTypeService {

    Map<String, Object> findAll(String token, String staffId, Integer pageNumber, Integer pageSize);

    public TblLoginType findByid(String id);


    /**
     * 修改
     *
     * @param tblt
     */
    void updatetblLoginType(TblLoginType tblt);

    void save(TblLoginType tnt);

    Map<String, Object> findByLoginId(String loginid);

    Map<String, Object> del(String loginid);
}
