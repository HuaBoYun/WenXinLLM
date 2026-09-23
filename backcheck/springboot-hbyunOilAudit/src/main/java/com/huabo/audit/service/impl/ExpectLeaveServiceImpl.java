package com.huabo.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.ExpectLeaveEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.ExpectLeaveMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.ExpectLeaveService;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
/**
 * @author Rui
 * @ClassName ExpectLeaveServiceImpl
 * @Description
 * @DATE 2023/9/14
 */
@Service
public class ExpectLeaveServiceImpl implements ExpectLeaveService {

    @Autowired
    private ExpectLeaveMapper expectLeaveMapper;

    @Autowired
    private TblStaffMapper tblStaffMapper;

    @Autowired
    private TblOrganizationMapper tblOrganizationMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String name, String teamLeader, String projectName,String ids) throws Exception {
        TblStaffUtil user = userProvider.get();
        ExpectLeaveEntity expectLeaveEntity = new ExpectLeaveEntity();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        expectLeaveEntity.setCreateUserId(user.getStaffid().toString());
        if(StringUtil.isNotEmpty(name)){
            expectLeaveEntity.setName(name);
        }

        if(StringUtil.isNotEmpty(projectName)){
            expectLeaveEntity.setProjectName(projectName);
        }
        if(StringUtils.isNotBlank(user.getDeptIds())){ 
            expectLeaveEntity.setQueryDeptIds(user.getDeptIds());
        }
        if(StringUtils.isNotBlank(ids)){
            expectLeaveEntity.setIds(ids);
        }

        if(StringUtil.isNotEmpty(teamLeader)){
            TblStaff tblStaff = new TblStaff();
            tblStaff.setRealname(teamLeader);
            expectLeaveEntity.setTeamLeader(tblStaff);
        }


        Page<ExpectLeaveEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> expectLeaveMapper.selectByEntity(expectLeaveEntity));
		PageInfo<ExpectLeaveEntity> pageInfo = new PageInfoUtil<ExpectLeaveEntity>().parsePageInfo(page);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        ExpectLeaveEntity expectLeaveEntity = expectLeaveMapper.selectById(id);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(expectLeaveEntity);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", expectLeaveEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public void updateEntity(ExpectLeaveEntity expectLeaveEntity) throws Exception{
        expectLeaveMapper.updateEntity(expectLeaveEntity);
    }

    @Override
    public void saveEntity(String token, ExpectLeaveEntity expectLeaveEntity) throws Exception{
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            expectLeaveEntity.setCreateUser(tblStaff);
        }
        expectLeaveMapper.insertEntity(expectLeaveEntity);
    }

    @Override
    public void deleteByIds(String ids) throws Exception{

        expectLeaveMapper.deleteEntity(ids);
    }

    public Date parseDate(String date){
        return DateUtil.parse(date.trim(),"yyyy-MM-dd").toSqlDate();
    }

    @Override
    public void resolveSheet(XSSFSheet sheet, String token) throws Exception {
        XSSFRow row = null;
        XSSFCell cell = null;

        List<TblStaff> tblStaffs = tblStaffMapper.selectList(null);
//        "序号","单位" , "预计退二线时间","审计时间","项目名称","任职时间(审计范围)","审计实施时间","组长","牵头人","主审","助审","姓名"
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
            row = sheet.getRow(i);
            if (row != null) {
                ExpectLeaveEntity expectLeaveEntity = new ExpectLeaveEntity();

                TblStaffUtil user = userProvider.get();
                if(user != null) {
                    TblStaff tblStaff = new TblStaff();
                    tblStaff.setStaffid(user.getStaffid());
                    expectLeaveEntity.setCreateUser(tblStaff);
                }

                cell = row.getCell(1);
                if(cell != null){
                    cell.setCellType(1);
                    expectLeaveEntity.setUnitName(cell.getStringCellValue());
                    QueryWrapper<TblOrganization> wrapper = new QueryWrapper<TblOrganization>();
                    wrapper.eq("ORGNAME",cell.getStringCellValue());
                    TblOrganization selectOne = tblOrganizationMapper.selectOne(wrapper);
                    if (!Objects.isNull(selectOne)){
                        expectLeaveEntity.setUnitId(selectOne.getOrgid());
                    }
                }
                
                
                cell = row.getCell(2);
                if(cell != null){
                    cell.setCellType(1);
                    expectLeaveEntity.setName(cell.getStringCellValue());
                }

                cell = row.getCell(3);
                if(cell != null){
                    cell.setCellType(1);
                    expectLeaveEntity.setRetireTime(parseDate(cell.getStringCellValue()));
                }

                cell = row.getCell(4);
                if(cell != null){
                    cell.setCellType(1);
                    expectLeaveEntity.setAuditTime(parseDate(cell.getStringCellValue()));
                }

                cell = row.getCell(5);
                if(cell != null){
                    cell.setCellType(1);
                    expectLeaveEntity.setProjectName(cell.getStringCellValue());
                }

                cell = row.getCell(6);
                if(cell != null){
                    String timeRange = cell.getStringCellValue();
                    if(StringUtil.isNotEmpty(timeRange)){
                        String[] timeArr = timeRange.split("~");
                        if(timeArr.length == 2){
                            expectLeaveEntity.setWorkStartTime(parseDate(timeArr[0]));
                            expectLeaveEntity.setWorkEndTime(parseDate(timeArr[1]));
                        }

                    }
                }

                cell = row.getCell(7);
                if(cell != null){
                    cell.setCellType(1);
                    expectLeaveEntity.setDoAuditTime(parseDate(cell.getStringCellValue()));
                }

                cell = row.getCell(8);
                if(cell != null){
                    cell.setCellType(1);
                    String username = cell.getStringCellValue();
                    List<TblStaff> filterStaff = tblStaffs.stream().filter(t->{
                        if(StringUtil.isNotEmpty(t.getRealname())){
                            return t.getRealname().equals(username);
                        }
                        return false;
                    }).collect(Collectors.toList());
                    if(filterStaff.size() > 0){
                        expectLeaveEntity.setTeamLeaderId(filterStaff.get(0).getStaffid()+"");
                        expectLeaveEntity.setTeamLeaderName(filterStaff.get(0).getRealname());
                    }
                }

                cell = row.getCell(9);
                if(cell != null){
                    cell.setCellType(1);
                    String username = cell.getStringCellValue();
                    List<TblStaff> filterStaff = tblStaffs.stream().filter(t->{
                        if(StringUtil.isNotEmpty(t.getRealname())){
                            return t.getRealname().equals(username);
                        }
                        return false;
                    }).collect(Collectors.toList());
                    if(filterStaff.size() > 0){
                        expectLeaveEntity.setLeaderId(filterStaff.get(0).getStaffid()+"");
                        expectLeaveEntity.setLeaderName(filterStaff.get(0).getRealname());
                    }
                }

                cell = row.getCell(10);
                if(cell != null){
                    cell.setCellType(1);
                    String username = cell.getStringCellValue();
                    List<TblStaff> filterStaff = tblStaffs.stream().filter(t->{
                        if(StringUtil.isNotEmpty(t.getRealname())){
                            return t.getRealname().equals(username);
                        }
                        return false;
                    }).collect(Collectors.toList());
                    if(filterStaff.size() > 0){
                        expectLeaveEntity.setChiefReviewerId(filterStaff.get(0).getStaffid()+"");
                        expectLeaveEntity.setChiefReviewerName(filterStaff.get(0).getRealname());
                    }
                }

                cell = row.getCell(11);
                if(cell != null){
                    cell.setCellType(1);
                    String username = cell.getStringCellValue();
                    List<TblStaff> filterStaff = tblStaffs.stream().filter(t->{
                        if(StringUtil.isNotEmpty(t.getRealname())){
                            return t.getRealname().equals(username);
                        }
                        return false;
                    }).collect(Collectors.toList());
                    if(filterStaff.size() > 0){
                        expectLeaveEntity.setDeputyReviewerId(filterStaff.get(0).getStaffid()+"");
                        expectLeaveEntity.setDeputyReviewerName(filterStaff.get(0).getRealname());
                    }
                }


                saveEntity(token, expectLeaveEntity);
            }
        }
    }

    @Override
    public void distribute(String ids, String personIds) throws Exception {
        String[] idArr = ids.split(",");

        for (String id : idArr){
            ExpectLeaveEntity expectLeaveEntity = expectLeaveMapper.selectById(id);
            if(expectLeaveEntity != null){
                expectLeaveEntity.setPersonIds(personIds);
                this.updateEntity(expectLeaveEntity);
            }
        }

    }
}
