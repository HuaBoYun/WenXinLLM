package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqkFymx;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @author Administrator
* @description 针对表【TBL_YQNS_JSXM_TZWCQK_FYMX(建设项目投资完成情况费用明细表)】的数据库操作Service
* @createDate 2023-09-14 22:49:04
*/
public interface TblYqnsJsxmTzwcqkFymxService extends IService<TblYqnsJsxmTzwcqkFymx> {

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
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmTzwcqkFymx vo) throws Exception;

    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsJsxmTzwcqkFymx vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsJsxmTzwcqkFymx vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsJsxmTzwcqkFymx vo) throws Exception;

    /**
     * 条件删除
     * @param fymx
     * @return
     */
    int deleteByWrapper(TblYqnsJsxmTzwcqkFymx fymx);

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean exportData(HttpServletResponse response, String token, TblYqnsJsxmTzwcqkFymx vo) throws Exception;

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    JsonBean importData(MultipartFile file, String token) throws Exception;
}
