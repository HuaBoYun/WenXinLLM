package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_GCXMZJ_ZJB(工程项目造价中间表)】的数据库操作Service
 * @createDate 2023-09-10 20:34:27
 */
public interface TblYqnsGcxmzjZjbService extends IService<TblYqnsGcxmzjZjb> {

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
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmzjZjb vo) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @param i 
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsGcxmzjZjb vo,int type) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsGcxmzjZjb vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsGcxmzjZjb vo) throws Exception;

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean exportData(HttpServletResponse response, String token, TblYqnsGcxmzjZjb vo) throws Exception;

    /**
     * 导入
     *
     * @param file
     * @param isCover 
     * @return
     * @throws IOException
     */
    JsonBean importData(MultipartFile file, String token, Integer isCover) throws Exception;


	JsonBean getAutoNo(String token) throws Exception;



}
