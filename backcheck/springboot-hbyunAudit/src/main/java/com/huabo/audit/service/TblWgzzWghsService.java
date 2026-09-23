package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblWgzzShbg;
import com.huabo.audit.oracle.entity.TblWgzzWghs;
import com.huabo.audit.util.R;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:10:35
 */

public interface TblWgzzWghsService {
//违规核实列表查询
    JsonBean getByWghsList(String token, Integer pageNumber, Integer pageSize,String clueNaber,String verifycontent)throws Exception;

	List<TblWgzzWghs> getByWghsExtList(Integer pageNumber, Integer pageSize);

    //违规核实列表查询详情
    JsonBean getByWghsXQList(String token, BigDecimal id)throws Exception;


//违规核实新增/修改
    JsonBean getByaddlist(String token,TblWgzzWghs tblWgzzWghs,String attIds)throws Exception;
//违规核实删除
    JsonBean getByremoveList(String token,BigDecimal id)throws Exception;
    //违规核实附件删除
    R  deleteAttInfoByAttId(String token,String attId)throws Exception;
//核实报告列表查询
    JsonBean getBySHBGList(String token, Integer pageNumber, Integer pageSize,String clueNaber,String verifycontent)throws Exception;

	List<TblWgzzShbg> getBySHBGExtList(Integer pageNumber, Integer pageSize);

    //核实报告列表查询详情
    JsonBean getBySHBGXQList(String token, BigDecimal id)throws Exception;
//核实报告新增/修改
    JsonBean getBySHBGaddlist(String token, TblWgzzShbg tblWgzzShbg, String attIds)throws Exception;
//核实报告删除
    JsonBean getBySHBGremoveList(String token,BigDecimal id)throws Exception;
    //核实报告附件删除
    R  removeAttInfoByAttId(String token,String attId)throws Exception;
    //查新违规核实审批已完成的编号和内容
    JsonBean getByWghsBHlist(String token, Integer pageNumber, Integer pageSize,String clueNaber)throws Exception;

    JsonBean wgbgFileList(String token, BigDecimal id) throws Exception;
    
    
    public JsonBean getByidyhList(String token, BigDecimal id) throws Exception;
    
    public JsonBean uploadattbyid(BigDecimal id, String attid) throws Exception;
    
    public JsonBean delattbyid(BigDecimal attid, String token) throws Exception;
    
    public JsonBean wghsFileList(String token, BigDecimal id) throws Exception;
}
