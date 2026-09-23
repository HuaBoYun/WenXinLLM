package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzj;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GCXMZJ(工程项目造价表)】的数据库操作Service
 * @createDate 2023-09-07 16:46:40
 */
public interface TblYqnsGcxmzjService extends IService<TblYqnsGcxmzj> {

    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsGcxmzj vo) throws Exception;

    /**
     * 查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @param order 
     * @return
     * @throws Exception
     */
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmzj vo, Integer order) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsGcxmzj vo) throws Exception;


    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsGcxmzj vo) throws Exception;


    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    JsonBean importData(MultipartFile file, String token) throws Exception;

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean exportData(HttpServletResponse response, String token, TblYqnsGcxmzj vo) throws Exception;

    /**
     * 工程项目造价中间表选择工程项目造价列表信息
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
	JsonBean getChoiceList(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmzj vo) throws Exception;

}
