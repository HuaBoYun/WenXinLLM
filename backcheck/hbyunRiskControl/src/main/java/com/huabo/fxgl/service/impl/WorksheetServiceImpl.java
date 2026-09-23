package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.Worksheet;
import com.huabo.fxgl.entity.YyCompany;
import com.huabo.fxgl.mapper.WorksheetMapper;
import com.huabo.fxgl.service.IWorksheetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@Service
public class WorksheetServiceImpl extends ServiceImpl<WorksheetMapper, Worksheet> implements IWorksheetService {

    @Autowired
    private WorksheetMapper worksheetMapper;

    @Override
    public Boolean isSJByOrgId(String userOrgid) {
        Integer num = worksheetMapper.isSJByOrgId(userOrgid);
        if (num == 0) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public IPage<Worksheet> findAllTblWorksheetByorgid(String orgid, String orgtype, IPage page, String type, Find find) {
        QueryWrapper<Worksheet> queryWrapper = new QueryWrapper<>();
        IPage<Worksheet> pageBean = new Page<>();
        if(find!=null){
            if (StringUtils.isNotEmpty(find.getNum())) {
                queryWrapper.like("WORKSHEETNUMBER",find.getNum());
//                sql += " and WORKSHEETNUMBER like '%"+find.getNum()+"%'";
//                sqlCount += " and WORKSHEETNUMBER like '%"+find.getNum()+"%'";
            }
            if (StringUtils.isNotEmpty(find.getName())) {
                queryWrapper.like("WORKSHEETNAME",find.getName());
//                sql += " and WORKSHEETNAME like '%"+find.getName()+"%'";
//                sqlCount += " and WORKSHEETNAME like '%"+find.getName()+"%'";
            }
            if (StringUtils.isNotEmpty(find.getOrder())) {
                queryWrapper.orderByAsc("RECORDINGDATE");
//                sql += " order by RECORDINGDATE "+find.getOrder();
            }else {
                queryWrapper.orderByDesc("WORKSHEETID");
//                sql += " order by WORKSHEETID desc";
            }
        }
        if (orgtype!=null && orgtype.equals("0") && !"".equals(orgtype)) {
            pageBean = worksheetMapper.findAllTblWorksheetByorgid(orgid,  type, page,  queryWrapper);
//            sql = "select * from TBL_WORKSHEET where 1=1 and orgid="+orgid+" and WORKSHEETBYSYSTEM like '"+type+"%' ";
//            sqlCount = "select count(*) from TBL_WORKSHEET where 1=1  and orgid="+orgid+" and WORKSHEETBYSYSTEM like '"+type+"%' ";
        }else{
            pageBean = worksheetMapper.findAllTblWorksheetByorgid1(orgid,  type, page,  queryWrapper);
//            sql = "select * from TBL_WORKSHEET where 1=1 and orgid in (select ORGID from TBL_ORGANIZATION where 1=1 and  ORGTYPE=0 or orgid="+orgid+" start with  fatherorgid="+orgid+" connect by prior fatherorgid= ORGID)  and WORKSHEETBYSYSTEM like '"+type+"%' ";
//            sqlCount = "select count(*) from TBL_WORKSHEET where 1=1  and  orgid in (select ORGID from TBL_ORGANIZATION where 1=1 and ORGTYPE=0 or orgid="+orgid+" start with  fatherorgid="+orgid+" connect by prior fatherorgid =ORGID ) and WORKSHEETBYSYSTEM like '"+type+"%' ";
        }

        return pageBean;
    }

    @Override
    public void update(Worksheet worksheet) {
        Worksheet w = getById(worksheet.getWorksheetid().toString());
        w.setWorksheetname(worksheet.getWorksheetname());
        w.setAuditedorg(worksheet.getAuditedorg());
        w.setAudittarget(worksheet.getAudittarget());
        w.setAuditdescription(worksheet.getAuditdescription());
        w.setAuditprocess(worksheet.getAuditprocess());
        w.setAuditjudge(worksheet.getAuditjudge());
        w.setOrgid(worksheet.getOrgid());
        if (worksheet.getAttachments().size()>0) {
            Attachment att = (Attachment) worksheet.getAttachments().iterator().next();
            w.getAttachments().add(att);
        }
        this.saveOrUpdate(w);
    }

    @Override
    public String findByTblWorkSheetnumber(String num, String type, BigDecimal orgId) {
        String is = "0";
        List<Worksheet> list = new ArrayList<>();
        if (type != null){
             list =  worksheetMapper.findByTblWorkSheetnumber(num,type,orgId);
        }else {
             list =  worksheetMapper.findByTblWorkSheetnumber(num,null,orgId);
        }
        if(list != null && list.size() > 0){
            is = "1";
        }
        return is;
    }

    @Override
    public List<Worksheet> findAllTblWorksheetAlls(String type, String orgid) {
        if(StringUtils.isNotEmpty(type) && type.length()>2){type = type.substring(0,2);}
        return worksheetMapper.findAllByorgids(type,orgid);
    }

    @Override
    public List<Worksheet> findAllTblWorksheetAll(String type, String orgid) {
        if(StringUtils.isNotEmpty(type) && type.length()>2){type = type.substring(0,2);}
        return worksheetMapper.findAllByorgid(type,orgid);
    }
}
