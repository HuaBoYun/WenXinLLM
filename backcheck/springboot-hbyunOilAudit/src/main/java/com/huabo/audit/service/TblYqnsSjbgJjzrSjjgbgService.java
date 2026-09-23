package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsSjbgJjzrSjjgbg;
/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJBG_JJZRSJJGBG(经济责任审计结果报告定稿表)】的数据库操作Service
 */
public interface TblYqnsSjbgJjzrSjjgbgService extends IService<TblYqnsSjbgJjzrSjjgbg> {
    /**
     * 查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjbgJjzrSjjgbg vo) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsSjbgJjzrSjjgbg vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsSjbgJjzrSjjgbg vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsSjbgJjzrSjjgbg vo) throws Exception;

}
