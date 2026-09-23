package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcg;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcgGL;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_JHGL_JHCG(计划管理计划草稿)】的数据库操作Service
 */
public interface TblYqnsJhglJhcgService extends IService<TblYqnsJhglJhcg> {

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
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJhglJhcg vo) throws Exception;

    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsJhglJhcg vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param jhcgid
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, String jhcgid) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsJhglJhcg vo) throws Exception;







    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean exportData(HttpServletResponse response, String token, TblYqnsJhglJhcg vo) throws Exception;

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    JsonBean importData(MultipartFile file, String token) throws Exception;

	JsonBean copyUnique(String token, String jhcgid) throws Exception;

	JsonBean detailJhCgByChugao(String token, String jhcgid) throws Exception;
}
