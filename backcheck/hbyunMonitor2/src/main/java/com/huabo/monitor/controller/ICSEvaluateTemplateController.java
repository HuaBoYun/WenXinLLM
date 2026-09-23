package com.huabo.monitor.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.CatVo;
import com.huabo.monitor.entity.TblAssEleCategory;
import com.huabo.monitor.entity.TblAssesscategory;
import com.huabo.monitor.entity.TblAssesselement;
import com.huabo.monitor.entity.TblAssesstemple;
import com.huabo.monitor.entity.TblFlow;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.service.ITblAssesselementService;
import com.huabo.monitor.service.ITblAssesstempleService;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.OrganizationService;
import com.huabo.monitor.service.TblAssEleCategoryService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.service.TblAssesscategoryService;
import com.huabo.monitor.service.TblFlowService;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.util.IPageResult;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * ICS: Internal Control Setting
 * evaluate template controller
 * Created: 2022/11/30
 */
@RestController
@Slf4j
@Tag(name="内控设置-评价模板",description="内控设置-评价模板")
@RequestMapping(value = "/nbkz")
public class ICSEvaluateTemplateController {


    @Resource
    public ITblAssesstempleService tblAssesstempleService;

    @Resource
    public OrganizationService organizationService;

    @Resource
    public TblAssesscategoryService tblAssesscategoryService;

    @Resource
    public TblAssEleCategoryService tblAssEleCategoryService;

    @Resource
    TblFlowService service;

    @Resource
    ITblStaffService iTblStaffService;

    @Resource
    ITblAssesselementService tblAssesselementService;

    @Resource
    TblAssessService tblAssessService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "评价模板-列表查询成功",
            busType = "内控设置",
            fail = "评价模板-列表查询失败",
            operationType = OperationType.SELECT,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-列表")
    @PostMapping(value = "/gzdg/def_tmpl_list")
    public JsonBean gzdg_def_tmpl_list( @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "分页当前数量", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "templename", description = "评价模板名称", required = false)@RequestParam(value = "templename", required = false) String templename,
            @Parameter(name = "templenumber", description = "评价模板编号", required = false)@RequestParam(value = "templenumber", required = false) String templenumber) throws Exception {
//        if (!ConstClass.checkToken(token)) {
//            return ConstClass.tokenFailure();
//        }
        TblAssesstemple assesstemple = new TblAssesstemple();
        assesstemple.setTemplename(templename);
        assesstemple.setTemplenumber(templenumber);
        TblStaffUtil userToken = userProvider.get();
        if (userToken == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        //IPage<TblAssesstemple> page = tblAssesstempleService.findAll(userToken.getCurrentOrg().getOrgid().toString(), pageNumber, assesstemple);
      PageInfo<TblAssesstemple> page = tblAssesstempleService.findAllNewPage(userToken.getCurrentOrg().getOrgid().toString(), pageNumber, assesstemple,pageSize,userToken);
      IPageResult<TblAssesstemple> pageInfo=new IPageResult<TblAssesstemple>().buildIpage(page);
        Map<String, Object> map = new HashMap<>();
        map.put("pageBean", pageInfo);
        map.put("templ", assesstemple);
        return ResponseFormat.retParam(1, 200, map);
    }

    @OperationLog(
            success = "评价模板-新增-新建评价模板下一步成功",
            busType = "内控设置",
            fail = "评价模板-新增-新建评价模板下一步失败",
            operationType = OperationType.ADD,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-新增-新建评价模板下一步")
    @PostMapping(value = "/gzdg/def_tmpl_index", produces = "application/json; charset=utf-8")
    public JsonBean gzdg_def_tmpl_index(
            @Parameter(name = "tblAssesstemple", description = "TblAssesstemple实体类") @RequestBody TblAssesstemple tblAssesstemple,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ) throws Exception {
//        if (!ConstClass.checkToken(token)) {
//            return ConstClass.tokenFailure();
//        }
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
       TblStaff createStaff=iTblStaffService.getById(userToken.getStaffid());
        TblOrganizationUtil currentOrg = userToken.getCurrentOrg();
        if(tblAssesstemple.getReorg()==null&&tblAssesstemple.getReorg().length()==0){
            return ResponseFormat.retParam(0, 202, "适用机构不能为空");
        }

        if(tblAssesstemple.getTemplenumber()==null){
            return ResponseFormat.retParam(0, 202, "模板编号不能为空");
        }

        if(tblAssesstemple.getTemplename()==null){
            return ResponseFormat.retParam(0, 202, "模板名称不能为空");
        }
         String[] orgids = tblAssesstemple.getReorg().split(",");
        if(tblAssesstemple.getAsstemid()!=null){ //修改
        	 tblAssesstemple.setStaffid(userToken.getStaffid());
            tblAssesstemple.setOrgid(currentOrg.getOrgid());
			tblAssesstemple.setModifydatetime(new Date());
			this.tblAssesstempleService.updateTemples(tblAssesstemple);
			tblAssesstempleService.removeTempleOrg(tblAssesstemple.getAsstemid());
			   for(String id:orgids){
               	if(id!=null){
               	  tblAssesstempleService.saveTempleOrg(new BigDecimal(id), tblAssesstemple.getAsstemid());
               	}
               }
			return ResponseFormat.retParam(1, 200, tblAssesstemple.getAsstemid());
        }else{//新增
        	List<TblAssesstemple> assesstemples = tblAssesstempleService.getTmplByNumber(tblAssesstemple.getTemplenumber(), currentOrg.getOrgid());
            if (assesstemples.size() == 0) { // 如果该编号在该公司不存在,保存
            	tblAssesstemple.setStaffid(userToken.getStaffid());
            	tblAssesstemple.setModifydatetime(new Date());
                tblAssesstemple.setOrgid(currentOrg.getOrgid());
                tblAssesstemple.setAsstemid(RandomUtil.uuBigDecimalId());
                tblAssesstemple.setCreatetime(new Date());
                tblAssesstemple.setLinkdeptid(userToken.getLinkDetp().getOrgid());
                tblAssesstempleService.insertTemples(tblAssesstemple);
                //关联适用机构
                for(String id:orgids){
                	if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(id)){
                	tblAssesstempleService.saveTempleOrg(new BigDecimal(id), tblAssesstemple.getAsstemid());
                	}
                }
                return ResponseFormat.retParam(1, 200, tblAssesstemple.getAsstemid());
            } else {
                return ResponseFormat.retParam(0, 202, "模板编号不能有重复");
            }
        }
    }

    @OperationLog(
            success = "评价模板-新增-新建评价模板下一步成功",
            busType = "内控设置",
            fail = "评价模板-新增-新建评价模板下一步失败",
            operationType = OperationType.ADD,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-新增-新建评价模板下一步")
    @GetMapping(value = "/gzdg/def_tmpl_index1")
    public JsonBean def_tmpl_index1(
            @Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId") BigDecimal tmplId,
            @Parameter(name = "view", description = "view") @RequestParam(value = "view") String view,
            @Parameter(name = "choiceSearch", description = "区域显隐控制") @RequestParam(value = "choiceSearch") String choiceSearch,
            @RequestHeader("token") String token
    ) throws Exception{

    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}

        Map<String, Object> mav = new HashMap<>();
        mav.put("tmplId", tmplId);
        mav.put("view", view);
        // 为页面查找区域显隐藏赋值

        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        mav.put("choiceSearch", choiceSearch);
        return ResponseFormat.retParam(1, 200, mav);
    }

    @OperationLog(
            success = "评价模板-新增-模板第二步 头信息查询成功",
            busType = "内控设置",
            fail = "评价模板-新增-模板第二步 头信息查询失败",
            operationType = OperationType.SELECT,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-新增-模板第二步 头信息")
    @GetMapping(value = "/gzdg/def_head")
    public JsonBean gzdg_def_head(@Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId") BigDecimal tmplId,
                                  @Parameter(name = "view", description = "view") @RequestParam(value = "view") String view,
                                  @Parameter(name = "choiceSearch", description = "区域显隐控制") @RequestParam(value = "choiceSearch") String choiceSearch,
                                  @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}

        Map<String, Object> mav = new HashMap<>();
        mav.put("tmplId", tmplId);
        mav.put("view", view);
        // 为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }

        mav.put("choiceSearch", choiceSearch);
        return ResponseFormat.retParam(1, 200, mav);
    }

    @OperationLog(
            success = "评价模板-新增-模板第二步左侧树信息查询成功",
            busType = "内控设置",
            fail = "评价模板-新增-模板第二步左侧树信息查询失败",
            operationType = OperationType.SELECT,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-新增-模板第二步左侧树信息")
    @GetMapping(value = "/gzdg/def_left_tree")
    public JsonBean gzdg_def_left_tree(@Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId") BigDecimal tmplId,
                                       @Parameter(name = "view", description = "view") @RequestParam(value = "view") String view,
                                       @Parameter(name = "choiceSearch", description = "区域显隐控制") @RequestParam(value = "choiceSearch") String choiceSearch,
                                       @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        Map<String, Object> mv = new HashMap<>();
        // 为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        mv.put("choiceSearch2", choiceSearch);
        mv.put("tmplId", tmplId);
        mv.put("view", view);
        return ResponseFormat.retParam(1, 200, mv);
    }


    @OperationLog(
            success = "评价模板-根据模板id查看评价体系左侧目录查询成功",
            busType = "内控设置",
            fail = "评价模板-根据模板id查看评价体系左侧目录查询失败",
            operationType = OperationType.SELECT,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-根据模板id查看评价体系左侧目录")
    @GetMapping(value = "/gzdg/det_temp_list")
    public JsonBean add_teml_list(
            @Parameter(name = "tmplId", description = "评价模板id") @RequestParam(value = "tmplId") BigDecimal tmplId,
            @Parameter(name = "nodeId", description = "nodeId", required = false) @RequestParam(value = "nodeId",required=false) BigDecimal nodeId,
            @Parameter(name = "view", description = "view", required = false) @RequestParam(value = "view",required=false) String view,
            @Parameter(name = "choiceSearch", description = "区域显隐控制", required = false) @RequestParam(value = "choiceSearch",required=false) String choiceSearch,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        List<TblAssesscategory> assesscategories = this.tblAssesscategoryService.findByTempleId(tmplId);
        Map<String, Object> mav = new HashMap<>();

        mav.put("tmplId", tmplId);
        mav.put("nodeId", nodeId);
        mav.put("view", view);
        // 为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        mav.put("choiceSearch", choiceSearch);
        if (null == nodeId) {
            mav.put("factor", 1);
        } else {
            mav.put("factor", assesscategories.size());
        }
        //传TEMPID查询分类信息
        if (assesscategories.size() != 0 && null == nodeId) {
            mav.put("assesscategories", assesscategories);
            return ResponseFormat.retParam(1, 200, mav);
        } else if(nodeId!=null) { //查询分类对应的条目信息
            List<TblAssEleCategory> assesscategory = this.tblAssEleCategoryService.getAssesscategoryByNodeId(nodeId);
            if (assesscategory.size() == 0) {
                mav.put("assesscategories", null);
                return ResponseFormat.retParam(1, 200, mav);

            } else {
            	List<TblAssEleCategory> assesscategoryNew=new ArrayList<TblAssEleCategory>();
            	for(TblAssEleCategory ca:assesscategory){
        			TblAssesscategory category = this.tblAssesscategoryService.get(ca.getAsscatid());
        			TblAssesselement assesselement=this.tblAssesselementService.get(ca.getAsseleid());
        			ca.setAssesscategory(category);
        			ca.setAssesselement(assesselement);
        			assesscategoryNew.add(ca);
            	}
                mav.put("assesscategory", assesscategoryNew);
                return ResponseFormat.retParam(1, 200, mav);
            }
        }else{
        	  mav.put("assesscategories", null);
        	 return ResponseFormat.retParam(1, 200, mav);
        }
    }


    @OperationLog(
            success = "评价模板-新增-内控评价树查询成功",
            busType = "内控设置",
            fail = "评价模板-新增-内控评价树查询失败",
            operationType = OperationType.SELECT,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-新增-内控评价树")
    @GetMapping(value = "/gzdg/getTree", produces = "application/json; charset=utf-8")
    public JsonBean leftTree(
            @Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId") BigDecimal tmplId,
            @Parameter(name = "view", description = "view") @RequestParam(value = "view") String view,
            @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        String tree = this.tblAssesscategoryService.GetTree(tmplId, "/nbkz/gzdg/det_temp_list?view=" + view);
        return ResponseFormat.retParam(1, 200, tree);
    }

    /**
     * @param tmplId
     * @param nodeId
     * @return
     */
    @OperationLog(
            success = "评价模板-新增-添加树节点成功",
            busType = "内控设置",
            fail = "评价模板-新增-添加树节点失败",
            operationType = OperationType.ADD,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-新增-添加树节点")
    @PostMapping(value = "/gzdg/cat_save", produces = "application/json; charset=utf-8")
    public JsonBean saveList(
            @RequestBody List<CatVo> catVos,
            @Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId") BigDecimal tmplId,
            @Parameter(name = "nodeId", description = "nodeId") @RequestParam(value = "nodeId",required=false) BigDecimal nodeId,
            @Parameter(name = "weightSum", description = "权重值weightSum") @RequestParam(value = "weightSum",required=true) double weightSum,
            @RequestHeader("token") String token)throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        TblAssesstemple assesstemple = this.tblAssesstempleService.findByid(tmplId);
        if (catVos.size() > 0) {
            DecimalFormat format = new DecimalFormat("######0");
            if (weightSum <= 100) {
                List<TblAssesscategory> have = new ArrayList<>();
                List<TblAssesscategory> delete = new ArrayList<>();
                List<TblAssesscategory> news = new ArrayList<>();
                List<TblAssesscategory> assesscategories = this.tblAssesscategoryService.findByTempleId(tmplId);
//                for (TblAssesscategory tblAssesscategory : assesscategories) {
//                    for (CatVo catVo : catVos) {
//                            tblAssesscategory.setCatdes(catVo.getDesc());
//                            tblAssesscategory.setCatname(catVo.getName());
//                            tblAssesscategory.setCatweight(catVo.getWeight());
//                            have.add(tblAssesscategory);
//                    }
//                }
//                for (TblAssesscategory tblAssesscategory : assesscategories) {
//                    boolean isDel = true;
//                    for (TblAssesscategory h : have) {
//                        if (h.getAsscatid().equals(tblAssesscategory.getAsscatid())) {
//                            isDel = false;
//                        }
//                    }
//                    if (isDel) {
//                        delete.add(tblAssesscategory);
//                    }
//                }
                for (CatVo catVo : catVos) {
                	if(catVo.getAsscatid()!=null&&catVo.getAsscatid().length()>0){
                		TblAssesscategory assesscategory=tblAssesscategoryService.get(new BigDecimal(catVo.getAsscatid()));
                		if(assesscategory!=null){
                			  assesscategory.setCatname(catVo.getName());
                              assesscategory.setCatweight(catVo.getWeight());
                              assesscategory.setCatdes(catVo.getDesc());
                             // assesscategory.setTblassesstemple(assesstemple);
                              assesscategory.setAsstemid(tmplId);
                              assesscategory.setFatherasscatid(nodeId);
                              tblAssesscategoryService.update(assesscategory);
                		}
                	}else{
                		 TblAssesscategory assesscategory = new TblAssesscategory();
                         assesscategory.setCatname(catVo.getName());
                         assesscategory.setCatweight(catVo.getWeight());
                         assesscategory.setCatdes(catVo.getDesc());
                         assesscategory.setAsstemid(tmplId);
                         assesscategory.setFatherasscatid(nodeId);
                         assesscategory.setAsscatid(RandomUtil.uuBigDecimalId());
                         news.add(assesscategory);
                	}
                }
                this.tblAssesscategoryService.addList(news);
                //this.tblAssesscategoryService.updateList(have);
                //this.tblAssesscategoryService.deleteListAndChildren(delete);
                List<TblAssesscategory> list = this.tblAssesscategoryService.findByTempleId(tmplId);
                Map<String, Object> ma = new HashMap<>();
                ma.put("tmplId", tmplId);
                ma.put("nodeId", nodeId);
                ma.put("categoryList", list);
                return ResponseFormat.retParam(1, 200, ma);
            } else {
                return ResponseFormat.retParam(0, 10001, "权重设置有误");
            }
        }
        return ResponseFormat.retParam(0, 10001, "保存失败");
    }


    @OperationLog(
            success = "评价模板-新增-要素导入列表查询成功",
            busType = "内控设置",
            fail = "评价模板-新增-要素导入列表查询失败",
            operationType = OperationType.SELECT,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-新增-要素导入列表")
    @PostMapping(value = "/gzdg/importYaosu")
    public JsonBean importYaosu(
            @Parameter(name = "pageNumber", description = "页数") @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "assesselement", description = "TblAssesselement实体类", required = false) @RequestBody TblAssesselement assesselement,
            @Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId") BigDecimal tmplId,
            @Parameter(name = "choiceSearch", description = "区域显隐控制", required = false) @RequestParam(value = "choiceSearch",required=false) String choiceSearch,
            @RequestHeader("token") String token
    ) throws Exception {
//        if (!ConstClass.checkToken(token)) {
//            return ConstClass.tokenFailure();
//        }

        // 根据模板id 获取下面所有的分类
        List<TblAssEleCategory> assEleCategories = this.tblAssEleCategoryService.getAssesscategoryByMuBanId(tmplId);
        List<String> notInele = new ArrayList<String>();
        for (TblAssEleCategory tblAssEleCategory : assEleCategories) {
            BigDecimal bigDecimal = tblAssEleCategory.getAsseleid();
            notInele.add(bigDecimal.toString());
        }
       // TblStaffUtil userToken = DealUserToken.parseUserToken(token);
        TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        BigDecimal orgid = userToken.getCurrentOrg().getOrgid();

        String sql = "SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and COMPANY =" + orgid;
        List<TblFlow> flows = new ArrayList<>();
        List list = service.findBySql(sql);
        if (list != null) {
            for (Object object : list) {
                Object[] oa = (Object[]) object;
                TblFlow flow = new TblFlow();
                flow.setFlowid(oa[0] != null ? new BigDecimal(oa[0].toString()) : null);
                flow.setFlowname(oa[1] != null ? oa[1].toString() : "");
                flows.add(flow);
            }
        }
        String str = StringUtils.join(notInele.toArray(), ",");
        //IPage<TblAssesselement> pageBean = tblAssesselementService.findByPageBean(orgid + "", pageNumber, assesselement, str);
       PageInfo<TblAssesselement> pageBean = tblAssesselementService.findByPageBeanNew(orgid + "", pageNumber, assesselement, notInele );
       IPageResult<TblAssesselement> pageInfo=new IPageResult<TblAssesselement>().buildIpage(pageBean);

        Map<String, Object> mav = new HashMap<>();
        mav.put("pageBean", pageInfo);
        mav.put("flows", flows);
        mav.put("conTocat", assesselement.getBusinesstype());
        mav.put("assesselement", assesselement);
        // 为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        mav.put("choiceSearch", choiceSearch);
        return ResponseFormat.retParam(1, 200, mav);
    }

    @OperationLog(
            success = "评价模板-要素导入-添加要导入的要素成功",
            busType = "内控设置",
            fail = "评价模板-要素导入-添加要导入的要素失败",
            operationType = OperationType.ADD,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-要素导入-添加要导入的要素")
	@PostMapping(value = "/gzdg/importYaosuSave")
	public JsonBean importYaosuSave(
		    		@Parameter(name = "id", description = "id") @RequestParam(value = "id")String id,
		    		@Parameter(name = "nodeId", description = "nodeId") @RequestParam(value = "nodeId")BigDecimal nodeId,
		            @Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId")BigDecimal tmplId,
		            @RequestHeader("token") String token ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
		if (StringUtils.isNotBlank(id) && null != tmplId) {
			if (id.lastIndexOf(',') == id.length() - 1) {
				id = id.substring(0, id.length() - 1);
			}
			List<TblAssesselement> assesselements = this.tblAssesselementService.getAssEssByIn(id);
			TblAssesscategory assesscategory = this.tblAssesscategoryService.get(nodeId);
			List<TblAssEleCategory> assEleCategories = this.tblAssEleCategoryService.getAssesscategoryByNodeId(nodeId);
			if (null != assesscategory && assesselements.size() > 0) {
				List<TblAssEleCategory> list = new ArrayList<TblAssEleCategory>();
				for (TblAssesselement assesselement : assesselements) {
					boolean isSave = true;
					for (TblAssEleCategory tblAssEleCategory : assEleCategories) {
						if (assesselement.getAsseleid().equals(tblAssEleCategory.getAsseleid())) {
							isSave = false;
						}
					}
					if (isSave) {
						TblAssEleCategory assEleCategory = new TblAssEleCategory();
						assEleCategory.setAsscatid(assesscategory.getAsscatid());
						assEleCategory.setAsseleid(assesselement.getAsseleid());
						assEleCategory.setStandardscore(new BigDecimal(5));
						list.add(assEleCategory);
					}
				}
				this.tblAssEleCategoryService.saveList(list);
				 return ResponseFormat.retParam(1, 200, null);
			}
		}
		 return ResponseFormat.retParam(1, 200, null);
	}


    @OperationLog(
            success = "评价模板-新增-模板要素删除成功",
            busType = "内控设置",
            fail = "评价模板-新增-模板要素删除失败",
            operationType = OperationType.DELETE,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-新增-模板要素删除")
    @DeleteMapping(value = "/gzdg/yaosudelete")
    public JsonBean yaosudelete(
            @Parameter(name = "elementGategoryIds", description = "elementGategoryId数组") @RequestParam(value = "elementGategoryIds")String elementGategoryIds,
            @Parameter(name = "tmplId", description = "tmplId", required = false) @RequestParam(value = "tmplId",required=false) String tmplId,
            @Parameter(name = "nodeId", description = "nodeId", required = false) @RequestParam(value = "nodeId",required=false) String nodeId,
            @RequestHeader("token") String token) throws Exception{
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        if(StringUtils.isNotBlank(elementGategoryIds)){
            String[] ids=elementGategoryIds.split(",");
        for (String id : ids) {
            TblAssEleCategory assEleCategory = this.tblAssEleCategoryService.get(new BigDecimal(id));
            if (null != assEleCategory) {
                this.tblAssEleCategoryService.delete(assEleCategory);
            }
        }
        }
        return ResponseFormat.retParam(1, 200, null);
    }


    @OperationLog(
            success = "评价模板-新增-评价体系条目删除成功",
            busType = "内控设置",
            fail = "评价模板-新增-评价体系条目删除失败",
            operationType = OperationType.DELETE,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-新增-评价体系条目删除")
    @DeleteMapping(value = "/gzdg/yaosudeletes")
    public JsonBean yaosudeletes(
            @Parameter(name = "elementGategoryIds", description = "评价体系条目id") @RequestParam(value = "elementGategoryIds")String elementGategoryIds,
            @Parameter(name = "tmplId", description = "tmplId", required = false) @RequestParam(value = "tmplId",required=false) String tmplId,
            @Parameter(name = "nodeId", description = "nodeId", required = false) @RequestParam(value = "nodeId",required=false) String nodeId,
            @RequestHeader("token") String token) throws Exception{
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        if(StringUtils.isNotBlank(elementGategoryIds)){
                this.tblAssesscategoryService.delete(elementGategoryIds);
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    @OperationLog(
            success = "评价模板-新增-模板要素标准分保存成功",
            busType = "内控设置",
            fail = "评价模板-新增-模板要素标准分保存失败",
            operationType = OperationType.ADD,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-新增-模板要素标准分保存")
    @GetMapping(value = "/gzdg/yaosusave", produces = "application/json; charset=utf-8")
    public JsonBean yaosuSave(
            @Parameter(name = "basickey", description = "basickey数组") @RequestParam(value = "basickey") BigDecimal[] basickey,
            @Parameter(name = "standardScore", description = "standardScore数组") @RequestParam(value = "standardScore") Double[] standardScore,
            @Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId") String tmplId,
            @Parameter(name = "nodeId", description = "nodeId") @RequestParam(value = "nodeId") String nodeId,
            @RequestHeader("token") String token)throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        for (int i = 0; i < basickey.length; i++) {
            TblAssEleCategory assEleCategory = this.tblAssEleCategoryService.get(basickey[i]);
            if (null != assEleCategory) {
                if (standardScore[i] != null) {
                    if (assEleCategory.getAssesselement().getAssessrules() >= standardScore[i]) {
                        assEleCategory.setStandardscore(new BigDecimal(standardScore[i]));
                        this.tblAssEleCategoryService.update(assEleCategory);
                    } else {
                        return ResponseFormat.retParam(0, 201, "标准分设置有误！");
                    }
                } else {
                    return ResponseFormat.retParam(0, 201, "标准分不能为空！");
                }
            }
        }
        return ResponseFormat.retParam(1, 200, "success");

    }

    @OperationLog(
            success = "评价模板-修改成功",
            busType = "内控设置",
            fail = "评价模板-修改失败",
            operationType = OperationType.UPDATE,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-修改")
    @GetMapping(value = "/gzdg/upadteTmpl", produces = "application/json; charset=utf-8")
    public JsonBean updateTmplCheck(
            @Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId") BigDecimal tmplId,
            @RequestHeader("token") String token)throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
           Integer assess=tblAssessService.findTblAssessByTempid(tmplId);
            if (assess > 0) {
                return ResponseFormat.retParam(0, 201, null);
            } else {
                return ResponseFormat.retParam(1, 200, tmplId);
            }
    }


    @OperationLog(
            success = "评价模板-修改或新建模板第一步查询成功",
            busType = "内控设置",
            fail = "评价模板-修改或新建模板第一步查询失败",
            operationType = OperationType.SELECT,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-修改或新建模板第一步")
    @GetMapping(value = "/gzdg/def_tmpl_add")
    public JsonBean gzdg_def_tmpl_add(
            @Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId") BigDecimal tmplId,
            @Parameter(name = "type", description = "type", required = false) @RequestParam(value = "type",required=false) String type,
            @Parameter(name = "view", description = "view", required = false) @RequestParam(value = "view",required=false) String view,
            @Parameter(name = "choiceSearch", description = "choiceSearch", required = false) @RequestParam(value = "choiceSearch",required=false) String choiceSearch,
            @RequestHeader("token") String token)throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        Map<String, Object> mav = new HashMap<>();
        if (null != tmplId) {
            TblAssesstemple assesstemple = tblAssesstempleService.findByid(tmplId);
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(assesstemple,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,assesstemple ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
            TblStaff staff=iTblStaffService.getById(assesstemple.getStaffid());
            assesstemple.setStaff(staff);
            mav.put("assesstemple", assesstemple);
            mav.put("view", view);
            // 为页面查找区域显隐藏赋值
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            mav.put("choiceSearch", choiceSearch);
            return ResponseFormat.retParam(1, 200, mav);
        }
        return ResponseFormat.retParam(0, 201,"模板ID未传递");
    }

    @OperationLog(
            success = "评价模板-删除成功",
            busType = "内控设置",
            fail = "评价模板-删除失败",
            operationType = OperationType.DELETE,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-删除")
    @DeleteMapping(value = "/gzdg/def_tmpl_del", produces = "application/json; charset=utf-8")
    public JsonBean gzdg_def_tmpl_del(
            @Parameter(name = "tmplId", description = "tmplId") @RequestParam(value = "tmplId") String tmplId,
            @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}

        String[] ids = tmplId.split(",");
        String errorMsg = "";
        for (String id : ids) {
            BigDecimal decimal = new BigDecimal(id);
            TblAssesstemple assesstemple = this.tblAssesstempleService.findByid(decimal);
            if (null != assesstemple) {
                if (assesstemple.getTblAssesses().size() > 0) {
                    // return JsonBean.error("模板已经在使用，不能删除");
                    errorMsg += assesstemple.getTemplename() + ",";
                } else {
                    List<TblAssEleCategory> assEleCategories = this.tblAssEleCategoryService.getAssesscategoryByMuBanId(decimal);
                    this.tblAssEleCategoryService.delete(assEleCategories);
                    this.tblAssesscategoryService.deleteByTempleId(decimal);

                    this.tblAssesstempleService.delete(assesstemple);
                }
            }
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            return ResponseFormat.retParam(0, 201, "模板正在使用不能删除");
        } else {
            return ResponseFormat.retParam(1, 200, "删除成功");
        }
    }

    @OperationLog(
            success = "评价模板-复制成功",
            busType = "内控设置",
            fail = "评价模板-复制失败",
            operationType = OperationType.ADD,
            subType = "评价模板"
    )
    @Operation(summary = "评价模板-复制")
    @PostMapping(value = "/gzdg/def_tmpl_copy", produces = "application/json; charset=utf-8")
    public JsonBean def_tmpl_copy(
    		@Parameter(name = "tblAssesstemple", description = "TblAssesstemple实体类") @RequestBody TblAssesstemple tblAssesstemple,
            @Parameter(name = "beforeTempId", description = "beforeTempId") @RequestParam(value = "beforeTempId") String beforeTempId,
            @RequestHeader("token") String token) throws Exception{
        String tempId="";
		if (StringUtils.isNotBlank(tblAssesstemple.getReorg())&& StringUtils.isNotBlank(tblAssesstemple.getTemplenumber())) {
			TblStaffUtil userToken = userProvider.get();
			if (userToken == null) {
		        return ResponseFormat.retParam(0, 20006, null);
			}
		       TblStaff createStaff=iTblStaffService.getById(userToken.getStaffid());
		        TblOrganizationUtil currentOrg = userToken.getCurrentOrg();
		        String[] orgids = tblAssesstemple.getReorg().split(",");
		    	List<TblAssEleCategory> aec = new ArrayList<TblAssEleCategory>();
		        List<TblAssesstemple> assesstemples = tblAssesstempleService.getTmplByNumber(tblAssesstemple.getTemplenumber(), currentOrg.getOrgid());
	            if (assesstemples.size() == 0) { // 如果该编号在该公司不存在,保存
	            	tblAssesstemple.setStaffid(userToken.getStaffid());
	            	tblAssesstemple.setModifydatetime(new Date());
	                tblAssesstemple.setOrgid(currentOrg.getOrgid());
	                tblAssesstemple.setAsstemid(RandomUtil.uuBigDecimalId());
	                tblAssesstempleService.insertTemples(tblAssesstemple);
	                //关联适用机构
	                for(String id:orgids){
	                	if(id!=null){
	                	tblAssesstempleService.saveTempleOrg(new BigDecimal(id), tblAssesstemple.getAsstemid());
	                	}
	                }
	                tempId=tblAssesstemple.getAsstemid().toString();
				// 原来模板
				TblAssesstemple tblAssesstempleCopy = this.tblAssesstempleService.findByid(tblAssesstemple.getAsstemid());
                List<TblAssesscategory> treeRoot = this.tblAssesscategoryService.findByTempleId(new BigDecimal(beforeTempId));
				for (TblAssesscategory tblAssesscategory : treeRoot) {
					TblAssesscategory tblAssesscategorycopy = new TblAssesscategory();
					tblAssesscategorycopy.setAsstemid(tblAssesstemple.getAsstemid());
					tblAssesscategorycopy.setCatdes(tblAssesscategory.getCatdes());
					tblAssesscategorycopy.setCatname(tblAssesscategory.getCatname());
					tblAssesscategorycopy.setCatweight(tblAssesscategory.getCatweight());
					tblAssesscategoryService.insertEntity(tblAssesscategorycopy);
					//Set<TblAssEleCategory> categories = tblAssesscategory.getAssEleCategories();
					List<TblAssEleCategory> categories = this.tblAssEleCategoryService.getAssesscategoryByNodeId(tblAssesscategory.getAsscatid());
					if (categories.size() > 0) {
						for (TblAssEleCategory tblAssEleCategory : categories) {
							TblAssesselement assesselement = tblAssesselementService.get(tblAssEleCategory.getAsseleid());
							TblAssEleCategory assEleCategory = new TblAssEleCategory();
							assEleCategory.setAsscatid(tblAssesscategorycopy.getAsscatid());
							assEleCategory.setAsseleid(assesselement.getAsseleid());
							aec.add(assEleCategory);
						}
					}
					//二级目录不存在
				/*	List<TblAssesscategory> treeNode = tblAssesscategoryService
							.getTreeByNodeId(tblAssesscategory.getAsscatid());
					for (TblAssesscategory tblAssesscategory2 : treeNode) {
						for (TblAssesscategory tblAssesscategory3 : treeNode) {
							TblAssesscategory tblAssesscategorycopy1 = new TblAssesscategory();
							tblAssesscategorycopy1.setTblAssesstemple(tblAssesstempleCopy);
							tblAssesscategorycopy1.setCatdes(tblAssesscategory3.getCatdes());
							tblAssesscategorycopy1.setCatname(tblAssesscategory3.getCatname());
							tblAssesscategorycopy1.setCatweight(tblAssesscategory3.getCatweight());
							tblAssesscategorycopy1.setTblAssesstemple(tblAssesstempleCopy);
							tblAssesscategorycopy1.setFatherasscatid(new BigDecimal(serializable.toString()));
							Serializable serializable1 = this.tblAssesscategoryService.add(tblAssesscategorycopy1);
							TblAssesscategory assesscategory = this.tblAssesscategoryService
									.get(new BigDecimal(serializable1.toString()));
							Set<TblAssEleCategory> categoriess = tblAssesscategory2.getAssEleCategories();
							if (categoriess.size() > 0) {
								for (TblAssEleCategory tblAssEleCategory : categoriess) {
									TblAssesselement assesselement = tblAssEleCategory.getAssesselement();
									TblAssEleCategory assEleCategory = new TblAssEleCategory();
									assEleCategory.setAssesscategory(assesscategory);
									assEleCategory.setAssesselement(assesselement);
									aec.add(assEleCategory);
								}
							}
						}
						treeNode = tblAssesscategoryService.getTreeByNodeId(tblAssesscategory2.getAsscatid());
					}*/
				}

				this.tblAssEleCategoryService.saveList(aec);
				return ResponseFormat.retParam(1, 200, tempId);
			} else {
				return ResponseFormat.retParam(0, 202, "模板编号不能有重复");
			}
		}
		return ResponseFormat.retParam(0, 202, "请完善信息");
	}

}
