package com.huabo.audit.service;


import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblWgzzEntity;
import com.huabo.audit.oracle.vo.TblWgzzVo;
import com.huabo.audit.util.PageInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/26
 * @Time:10:38
 */

public interface TblWgzzService {

    //违规追责-列表查询
    JsonBean getwfzzlist(String token, Integer pageNumber, Integer pageSize,TblWgzzEntity param)throws Exception;

	PageInfo<TblWgzzEntity> getwfzzextlist(String token, Integer pageNumber, Integer pageSize,TblWgzzEntity param)throws Exception;

    JsonBean getwgzzXQList(String token, BigDecimal clueid)throws Exception;

    //违规追责新增/修改
    JsonBean saveUpdatewgzz(TblWgzzEntity tblWgzzEntity,String token)throws Exception;

    //违规追责删除
    JsonBean deletewgzz(String token, BigDecimal clueid)throws Exception;

    //违规经营投资问题线索管理台账列表
    JsonBean getwgzzArreyByList(String token, Integer pageNumber, Integer pageSize,String clueNaber)throws Exception;

    //违规经营投资问题线索管理台账新增/修改
    JsonBean updateaddWgzz(TblWgzzEntity tblWgzzEntity,String token)throws Exception;

    //违规经营投资问题线索管理台账删除
    JsonBean removewgzz(String token, BigDecimal clueid)throws Exception;

    JsonBean wgzzFileList(String token, BigDecimal clueid) throws Exception;

    PageInfo<TblWgzzEntity> getwfzzYslist(String token, Integer pageNumber, Integer pageSize,TblWgzzEntity param,Integer type)throws Exception;
    
}
