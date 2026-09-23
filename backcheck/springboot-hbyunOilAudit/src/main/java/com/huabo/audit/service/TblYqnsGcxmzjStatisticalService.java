package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_GCXMZJ_ZJB(工程项目造价)】的数据库操作 统计表 Service
 * @createDate 2023-09-10 20:34:27
 */
public interface TblYqnsGcxmzjStatisticalService  {

    /**
     * 工程项目造价表-建设单位统计表
     *
     * @param token
     * @param queryYear 
     * @return
     * @throws Exception
     */
    JsonBean selectTblYqnsGcxmzjJsdwStatisticalList(String token, Integer queryYear) throws Exception;

    /**
     * 工程项目造价表-建设单位统计表 - 详情信息
     *
     * @param token
     * @param queryYear
     * @return
     * @throws Exception
     */
     JsonBean selectTblYqnsGcxmzjJsdwStatisticalToOne(String token, String jsdw, Integer queryYear) throws Exception;



    /**
     * 工程项目造价表-施工单位统计表
     * @param queryYear 
     *
     * @return
     */
    JsonBean selectTblYqnsGcxmzjSgdwStatisticalList(String token, Integer queryYear) throws Exception;


    /**
     *  工程项目造价表-内外部 统计表
     * @param queryYear 
     *
     * @return
     */
    JsonBean selectTblYqnsGcxmzjNwbStatisticalList(String token, Integer queryYear) throws Exception;


    /**
     *  工程项目造价表-抽审表(按施工单位及额度) 统计表
     * @param token
     * @return
     * @throws Exception
     */
    JsonBean selectTblYqnsGcxmzjSampleStatisticalList(String token, Integer queryYear) throws Exception;


}
