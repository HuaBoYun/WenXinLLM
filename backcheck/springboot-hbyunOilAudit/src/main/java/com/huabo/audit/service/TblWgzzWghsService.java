package com.huabo.audit.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsWgzzCljg;
import com.huabo.audit.oracle.entity.TblYqnsWgzzFlczyj;
import com.huabo.audit.oracle.entity.TblYqnsWgzzWtdz;
import com.huabo.audit.oracle.entity.TblYqnsWgzzWthc;
import com.huabo.audit.oracle.entity.TblYqnsWgzzWtsl;
import com.huabo.audit.oracle.entity.TblYqnsWgzzYsjgws;
import com.huabo.audit.oracle.entity.TblYqnsWgzzYstz;
import com.huabo.audit.util.R;

public interface TblWgzzWghsService {
	
    JsonBean flczyjList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzFlczyj tblYqnsWgzzFlczyj)throws Exception;

    JsonBean flczyjDetail(String token, BigDecimal id)throws Exception;
    
    JsonBean flczyjSave(String token, TblYqnsWgzzFlczyj tblWgzzShbg)throws Exception;
    
    JsonBean flczyjDelete(String token,BigDecimal id)throws Exception;
    
    R  flczyjFilesDelete(String token,String attId)throws Exception;

    JsonBean flczyjFileList(String token, BigDecimal id) throws Exception;
    
    
    JsonBean wthcList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzWthc tblYqnsWgzzWthc)throws Exception;

    JsonBean wthcDetail(String token, BigDecimal id)throws Exception;
    
    JsonBean wthcSave(String token, TblYqnsWgzzWthc tblWgzzShbg)throws Exception;
    
    JsonBean wthcDelete(String token,BigDecimal id)throws Exception;
    
    R  wthcFilesDelete(String token,String attId)throws Exception;

    JsonBean wthcFileList(String token, BigDecimal id) throws Exception;
    
    
    JsonBean wtdzList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzWtdz tblYqnsWgzzWtdz)throws Exception;

    JsonBean wtdzDetail(String token, BigDecimal id)throws Exception;
    
    JsonBean wtdzSave(String token, TblYqnsWgzzWtdz tblWgzzShbg)throws Exception;
    
    JsonBean wtdzDelete(String token,BigDecimal id)throws Exception;
    
    R  wtdzFilesDelete(String token,String attId)throws Exception;

    JsonBean wtdzFileList(String token, BigDecimal id) throws Exception;
    
    JsonBean wtdzIssued(String token, String staffIds,String ids) throws Exception;
    
    
    JsonBean ysjgwsList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzYsjgws tblYqnsWgzzYsjgws)throws Exception;

    JsonBean ysjgwsDetail(String token, BigDecimal id)throws Exception;
    
    JsonBean ysjgwsSave(String token, TblYqnsWgzzYsjgws tblWgzzShbg)throws Exception;
    
    JsonBean ysjgwsDelete(String token,BigDecimal id)throws Exception;
    
    R  ysjgwsFilesDelete(String token,String attId)throws Exception;

    JsonBean ysjgwsFileList(String token, BigDecimal id) throws Exception;
    
    
    JsonBean ystzList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzYstz tblYqnsWgzzYstz)throws Exception;

    JsonBean ystzDetail(String token, BigDecimal id)throws Exception;
    
    JsonBean ystzSave(String token, TblYqnsWgzzYstz tblWgzzShbg)throws Exception;
    
    JsonBean ystzDelete(String token,BigDecimal id)throws Exception;
    
    R  ystzFilesDelete(String token,String attId)throws Exception;

    JsonBean ystzFileList(String token, BigDecimal id) throws Exception;
    
    
    
    JsonBean wtslList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzWtsl tblYqnsWgzzWtsl)throws Exception;

    JsonBean wtslDetail(String token, BigDecimal id)throws Exception;
    
    JsonBean wtslSave(String token, TblYqnsWgzzWtsl tblYqnsWgzzWtsl)throws Exception;
    
    JsonBean wtslDelete(String token,BigDecimal id)throws Exception;
    
    R  wtslFilesDelete(String token,String attId)throws Exception;

    JsonBean wtslFileList(String token, BigDecimal id) throws Exception;
    
    
    
    JsonBean cljgList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzCljg tblYqnsWgzzCljg)throws Exception;

    JsonBean cljgDetail(String token, BigDecimal id)throws Exception;
    
    JsonBean cljgSave(String token, TblYqnsWgzzCljg tblYqnsWgzzCljg)throws Exception;
    
    JsonBean cljgDelete(String token,BigDecimal id)throws Exception;
    
    R  cljgFilesDelete(String token,String attId)throws Exception;

    JsonBean cljgFileList(String token, BigDecimal id) throws Exception;
    
    
}
