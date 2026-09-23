package com.huabo.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.service.TblCirculationService;
import com.huabo.audit.service.TblYqnsJhxqService;
import com.huabo.audit.util.HttpClient;
import com.huabo.audit.util.PageResult;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHXQ(计划需求)】的数据库操作Service实现
 */
@Service
public class TblYqnsJhxqServiceImpl extends ServiceImpl<TblYqnsJhxqMapper, TblYqnsJhxq>
        implements TblYqnsJhxqService {
    TblStaffUtil loginStaff;
    @Autowired
    private TblStaffMapper tblStaffMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

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
    @Override
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJhxq vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        /*PageInfo<TblYqnsJhxq> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

        IPage<TblYqnsJhxq> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsJhxq> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getJhxqmc())) {
            wrapper.lambda().like(TblYqnsJhxq::getJhxqmc, vo.getJhxqmc());
        }
        if (StringUtils.isNotBlank(vo.getXmlxr())) {
            wrapper.lambda().like(TblYqnsJhxq::getXmlxr, vo.getXmlxr());
        }
        if (StringUtils.isNotBlank(vo.getSjxmlx())) {
            wrapper.lambda().like(TblYqnsJhxq::getSjxmlx, vo.getSjxmlx());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsJhxq::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsJhxq::getEndDate, vo.getEndDate());
        }
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsJhxq> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getList());
        PageResult<TblYqnsJhxq> page = new PageResult<TblYqnsJhxq>().build(pageInfo);
        return ResponseFormat.retParam(1, 200, pageInfo);
    }


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsJhxq vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getJhxqid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
        } else {
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
        }
        if(vo.getJhxqid()==null) {
        	vo.setJhxqid(RandomUtil.uuBigDecimalId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttByPk(vo.getJhxqid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getJhxqid().toString(), attId);
            }
        }

        return ResponseFormat.retParam(1, 200, vo);
    }

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean detail(String token, TblYqnsJhxq vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsJhxq bean = this.getById(vo.getJhxqid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJhxqid().toString());
        bean.setAttachments(attachments);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);

        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean delete(String token, TblYqnsJhxq vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        boolean ret = this.removeByIds(vo.getIds());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        for (String id : vo.getIds()) {
            this.baseMapper.deleteAttByPk(id);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 校验登录token有效性
     *
     * @param token
     * @return null or not null
     * @throws Exception
     */
    private JsonBean validToken(String token) throws Exception {
        loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        return null;
    }

    @Resource
    TblProcessAnalysisMapper tblProcessAnalysisMapper;
    @Resource
    TblProcessSettingMapper processSettingMapper;
    @Resource
    TblProcessAnalusisUserMapper tblProcessAnalusisUserMapper;
    @Resource
    TblCirculationService tblCirculationService;
    @Resource
    TblMyTaskMapper tblMytaskMapper;
    @Resource
    public TblCirculationMapper tblCirculationMapper;


    /**
     * 送审
     *
     * @param token
     * @param vo    jhxqid
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean submit(String token, TblYqnsJhxq vo) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblCirculation cir = null;
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            TblYqnsJhxq bean = this.getById(vo.getJhxqid());
            if (bean == null) {
                return ResponseFormat.retParam(0, 50002, null);
            }
            if (bean.getSpzt() == 1) {
                return ResponseFormat.retParam(0, 30007, null);
            }
            if (bean.getSpzt() == 3) {
                return ResponseFormat.retParam(0, 30009, null);
            }
            if ("2,4,5".contains(bean.getSpzt().toString())) {
                resultMap.put("codes", "0");
                resultMap.put("msg", "流程进行中！");
                return ResponseFormat.retParam(0, 30002, resultMap);
            }
            List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.getByFlowSetting(ProcessEnum.JHGL_JHXQ.name());
            String busType = ProcessEnum.JHGL_JHXQ.name();
            Integer orgid = Integer.parseInt(user.getLinkOrg().getOrgid().toString());
            List<TblProcessSettingEntity> settings = processSettingMapper.selectByOrgid(busType, orgid);
            String setting = ProcessEnum.JHGL_JHXQ.name();
            if (settings != null && settings.size() > 0) {
                setting = settings.get(0).getModule();
                list = this.tblProcessAnalysisMapper.getByFlowSetting(setting);
            }
            HashMap<String, Object> fields = new HashMap<String, Object>();
            if (list != null && list.size() > 0) {
                for (TblProcessAnalysis tblAnalysis : list) {
                    TblProcessAnalusisUser analysisUser = this.tblProcessAnalusisUserMapper.findOnd(tblAnalysis.getAnalid().toString(), bean.getJhxqid().toString());
                    if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("tcuserid")) {
                        fields.put("tcuserid", user.getStaffid().toString());
                    } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("bmfzr")) {
                        //部门负责人参数
                        TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(user.getLinkDetp().getOrgid().toString());
                        if (bmfzr == null) {
                            resultMap.put("code", "0");
                            resultMap.put("msg", "部门负责人需配置！");
                        }
                        fields.put("bmfzr", bmfzr.getStaffid().toString());
                        System.out.println("bmfzr:" + bmfzr.getStaffid());
                    } else {
                        //除此之外的参数都设置为当前提交人
                        if (StrUtil.isNotBlank(tblAnalysis.getUserid())) {
                            fields.put(tblAnalysis.getUserid(), user.getStaffid().toString());
                        }
                    }
                    if (analysisUser == null) {
                        analysisUser = new TblProcessAnalusisUser();
                        analysisUser.setAnalid(tblAnalysis.getAnalid().toString());
                        analysisUser.setFromid(bean.getJhxqid().toString());
                        analysisUser.setSpdate(new Date());
                        if (tblAnalysis.getUserid() != null) {
                            analysisUser.setStaffid(user.getRealname());
                        } else {
                            analysisUser.setStaffid(tblAnalysis.getRolename());
                        }
                        this.tblProcessAnalusisUserMapper.insertSetting(analysisUser);
                    }
                }
            }
            JSONObject jsonObject = JSONObject.fromObject(fields);
            Map<String, Object> map = HttpClient.startProcessAll(setting, jsonObject.toString());
            String prcessresult = (String) map.get("result");
            String processInstanceId = (String) map.get("processInstanceId");
            String processDefinitionKey = (String) map.get("processDefinitionKey");
            bean.setSpzt(Long.valueOf(TblNbsjAuditplan.SPKA));
            this.updateById(bean);
            cir = this.tblCirculationService.saveTblCirculationnew(TblCirculation.TYPE_JHXQSP, TblCirculation.TYPE_JHXQSP, bean.getJhxqmc(), TblCirculation.TYPE_JHXQSP + bean.getJhxqid(), user.getStaffid(), processInstanceId, processDefinitionKey, bean.getJhxqid().toString());
            if (prcessresult != null && prcessresult.equals("true")) {
                List<TblMyTask> tasks = HttpClient.findByTask("", user.getStaffid().toString(), 1, 10000);
                if (tasks != null && tasks.size() > 0) {
                    for (TblMyTask task : tasks) {
                        if (task.getProcessInstanceId().equals(processInstanceId)) {
                            Map<String, Object> map1 = HttpClient.handleProcessJson(user.getStaffid().toString(), "通过", task.getTaskId());
                            String result = (String) map1.get("result");
                            if (result != null && "true".equals(result)) {
                                String blande = "";
                                //查询执行人
                                String nextapprover = HttpClient.nextapprover(cir.getBusinesskey());
                                TblProcessAnalysis analysis = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande, setting);
                                Integer number = 1;
                                String usertaskid = analysis.getUsertaskid();
                                Integer num = Integer.parseInt(usertaskid.substring(usertaskid.length() - 1, usertaskid.length())) + number;
                                blande = usertaskid.substring(0, usertaskid.length() - 1) + num;
                                TblProcessAnalysis analysis1 = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande, setting);
                                TblProcessAnalusisUser analysisUser = this.tblProcessAnalysisMapper.findOnd(analysis1.getAnalid().toString(), bean.getJhxqid().toString());
                                task.setFromid(bean.getJhxqid().toString());
                                task.setApprover(user.getRealname());
                                task.setUsrid(user.getStaffid().toString());
                                task.setExamination("提交审批");
                                task.setProcessName(setting);
                                if (user.getTrole() != null && user.getTrole().getRname() != null) {
                                    task.setApprovalrole(user.getTrole().getRname());
                                }
                                task.setApprovaldate(new Date());
                                task.setResult("通过");
                                task.setCirid(cir.getCyid().toString());
                                if (nextapprover.contains("bmfzr")) {
                                    TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(user.getLinkDetp().getOrgid().toString());
                                    task.setHandle(bmfzr.getRealname());
                                } else {
                                    task.setHandle(nextapprover);
                                }
                                task.setAnalid(analysisUser.getAnalid().toString());
                                this.tblMytaskMapper.insertMyTaskSetting(task);
                            }
                        }
                    }
                }
                resultMap.put("codes", "1");
                resultMap.put("msg", "审批已提交！");
            } else {
                resultMap.put("codes", "0");
                resultMap.put("msg", "流程提交失败！");
                if (cir != null && cir.getCyid() != null) {
                    this.tblCirculationMapper.deleteEntityById(cir.getCyid());
                }
                return ResponseFormat.retParam(0, 30002, resultMap);
            }
        } catch (Exception e) {
            resultMap.put("codes", "0");
            resultMap.put("msg", "流程提交失败！");
            e.printStackTrace();
            if (cir != null && cir.getCyid() != null) {
                this.tblCirculationMapper.deleteEntityById(cir.getCyid());
            }
            return ResponseFormat.retParam(0, 30002, resultMap);
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsJhxq vo) throws Exception {
        /*JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;*/
        String[] titles = {"序号", "计划需求名称", "项目类型", "建议实施时间", "组织方式", "项目联系人"};
        List<TblYqnsJhxq> list;
        PageInfo<TblYqnsJhxq> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);

        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsJhxq bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getJhxqmc();
            obj[2] = bean.getSjxmlx();
//            if ("1".equals(bean.getSjxmlx())) {
//                obj[2] = "工程项目审计";
//            } else if ("2".equals(bean.getSjxmlx())) {
//                obj[2] = "经济责任审计或管理及专项审计";
//            } else {
//                obj[2] = "";
//            }
            obj[3] = DateUtil.formatDate(bean.getJysjsssj());
            obj[4] = bean.getZzfs();
            obj[5] = bean.getXmlxr();
            objs.add(obj);
        }

        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("计划需求", StandardCharsets.UTF_8.name()) + ".xlsx");
        ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public JsonBean importData(MultipartFile file, String token) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workBook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        List<Object[]> objList = new ArrayList<>();
        for (int i = 1; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            Object[] obj = new Object[lastCellNum];
            for (int k = 0; k < lastCellNum; k++) {
                XSSFCell cell = row.getCell(k);
                obj[k] = cell.getStringCellValue();
            }
            objList.add(obj);
        }
        for (Object[] obj : objList) {
            TblYqnsJhxq o = new TblYqnsJhxq();
            o.setCjr(loginStaff.getRealname());
            o.setCjsj(new Date());
            o.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
            o.setExt1(loginStaff.getStaffid().toString());
            o.setJhxqmc(obj[1].toString());
            if ("工程项目审计".equals(obj[2].toString())) {
                o.setSjxmlx("1");
            } else if ("经济责任审计或管理及专项审计".equals(obj[2].toString())) {
                o.setSjxmlx("2");
            } else {
                o.setSjxmlx(null);
            }
            this.baseMapper.insert(o);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }
}




