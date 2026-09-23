package com.huabo.audit.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PropertyFileReader;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.service.NbsjExperienceService;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblExperienceTypeService;
import com.huabo.audit.service.TblNbsjAduitExperienceService;
import com.huabo.audit.service.TblNbsjAuditDatumService;
import com.huabo.audit.service.TblNbsjAuditExperOutRulService;
import com.huabo.audit.service.TblNbsjAuditExperienceService;
import com.huabo.audit.service.TblNbsjAuditExperienceTypeService;
import com.huabo.audit.service.TblNbsjAuditModelService;
import com.huabo.audit.service.TblNbsjAuditStepSQLService;
import com.huabo.audit.service.TblNbsjAuditStepService;
import com.huabo.audit.service.TblNbsjAuditplanService;
import com.huabo.audit.service.TblNbsjBugCriterionService;
import com.huabo.audit.service.TblNbsjOuterruleService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjTypeService;
import com.huabo.audit.service.TblOrganizaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * 内部审计计划控制器
 * <p>提供内部审计计划的列表查询、审计工具等接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/nbsj")
public class NbsjPlanController {
	
	@Autowired
    private TblNbsjTypeService tblNbsjTypeService;
	
	@Resource
	public NbsjExperienceService nbsjExperienceService;
	
	
	@Resource
	public TblExperienceTypeService experienceTypeService;
	
	@Resource
	public TblNbsjAduitExperienceService nbsjAduitExperienceService;
	
	
	@Resource
	public TblNbsjAuditExperienceTypeService tblNbsjAuditExperienceTypeService;
	
	@Resource
	public TblNbsjAuditExperienceService tblNbsjAuditExperienceService;
	
	@Resource
	public TblNbsjAuditDatumService tblNbsjAuditDatumService;
	
	@Resource
	public TblNbsjAuditStepService tblNbsjAuditStepService;
	
	@Resource
	public TblNbsjAuditStepSQLService tblNbsjAuditStepSQLService;
	
	@Resource
	public TblNbsjAuditModelService tblNbsjAuditModelService;
	
	@Resource
	public TblNbsjAuditExperOutRulService tblNbsjAuditExperOutRulService;
	
	@Resource
	public AttachmentService attachmentService;
	
	@Resource
	public TblNbsjAuditplanService tblnbsjauditPlanService;
	
	@Resource
	public TblNbsjProjectService tblnbsjProjectService;
	
	@Resource
	public TblNbsjBugCriterionService  tblNbsjBugCriterionService;
	
	@Resource
	TblNbsjOuterruleService tblNbsjOuterruleService;
	
	@Resource
	private TblOrganizaService tblOrganizaService;
	@Resource
	public FreeMarkerConfig freeMarkerConfig;
	
	@Autowired
    TblAttachmentService tblAttachmentService;
	
	@Resource
    private UserProvider userProvider;
	
//	@Resource
//	public TblacctbookService tblacctbookService;
	
//	private static final Logger logger = LoggerFactory.getLogger(NbsjController.class);
	private static final String DOCDIC = PropertyFileReader.getItem("doc.path");
    private static final String IMAGEURL = PropertyFileReader.getItem("image.url");
    private static final String DOCDICTY = PropertyFileReader.getItem("file.path");
    
	@Operation(summary="sjgl-列表查询")
	@RequestMapping("/sjgl/project_plan_list")
	public JsonBean planList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true)String token) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		String sql=" SELECT * from TBL_NBSJ_AUDITPLAN where 1=1  AND AUDITORGID ="+loginStaff.getCurrentOrg().getOrgid();
		return null;
	}
 
//		if (StringUtils.isNotBlank(type)) {
//			try {
//				str = HttpClient.request(HttpClient.getDeptChildrenUrl, fields, null);
//				return str;
//			} catch (Exception e) {
//				List<Tree> list = this.tblOrganizaService.getTreebm(nodeId);
//				json = JSONObject.toJSONString(list);
//			}
//		} else {
//			try {
//				str = HttpClient.request(HttpClient.getDeptUrl, fields, null);
//				return str;
//			} catch (Exception e) {
//				List<Tree> list = this.tblOrganizaService.getNodeAllbm(nodeId);
//				json = JSONObject.toJSONString(list);
//			}
//		}
    
	/*@RequestMapping(value = "/sjgj/experiencesave",method= {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public @ResponseBody  String experiencesave(String reorg, TblNbsjExperienceEntity templete, HttpServletRequest request) {
        TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
        TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        if (null != templete.getTempleteId()) {
        TblNbsjExperienceEntity TblNbsjExperienceEntity = this.nbsjExperienceService.getById(templete.getTempleteId());
            String[] orgids = reorg.split(",");
            for (String orgid : orgids) {
                templete.getOrganizations().add((this.tblOrganizaService.findByOrg(new BigDecimal(orgid))));
            }
            TblNbsjExperienceEntity.setUpdateStaff(tblStaff);
            TblNbsjExperienceEntity.setUpdateTime(new Date());
            TblNbsjExperienceEntity.setStaff(TblNbsjExperienceEntity.getStaff());
            TblNbsjExperienceEntity.setOrganizations(templete.getOrganizations());
            TblNbsjExperienceEntity.setTempleteDesc(templete.getTempleteDesc());
            TblNbsjExperienceEntity.setTempleteName(templete.getTempleteName());
            TblNbsjExperienceEntity.setTemptype(templete.getTemptype());
            List<TblExperienceTypeEntity> root = this.experienceTypeService.getRoot(templete.getTempleteId());
            if (root.size() > 0) {
            	TblExperienceTypeEntity experienceType = root.get(0);
            	experienceType.setTargetName(templete.getTempleteName());
                this.experienceTypeService.saveOrUpdate(experienceType);
            }
            nbsjExperienceService.saveOrUpdate(TblNbsjExperienceEntity);
            return JsonBean.success();
        } else {
            List<TblNbsjExperienceEntity> list = this.nbsjExperienceService.getByCode(templete.getTempleteCode());
            if (list.size() == 0) {
                String[] orgids = reorg.split(",");
                for (String orgid : orgids) {
                    templete.getOrganizations().add((this.tblOrganizaService.findByOrgid(orgid)));
                }
                templete.setStaff(tblStaff);
                templete.setOrgId(organization.getOrgid());
               
                templete.setUpdateStaff(tblStaff);
                Serializable serializable = this.nbsjExperienceService.save(templete);
                TblExperienceTypeEntity  experienceType=new  TblExperienceTypeEntity();
                experienceType.setNbsjExperience(this.nbsjExperienceService.getById(new BigDecimal(serializable.toString())));
                experienceType.setTargetName(templete.getTempleteName());
              //  experienceType.setStatus(TblTargetType.TEMP_NUMBER);
                this.experienceTypeService.save(experienceType);
                return JsonBean.success((String) serializable);
            } else {
                return JsonBean.error("模板编号已存在");
            }
        }
    }
	
	@Operation(summary="sjgj-修改")
	@RequestMapping(value = "/sjgj/experience_modify_index")
    public ModelAndView experience_modify_index(BigDecimal id,String type,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        TblNbsjExperienceEntity tblNbsjExperience = this.nbsjExperienceService.getById(id);
        Set<TblOrganization> organizations = tblNbsjExperience.getOrganizations();
        List<TblOrganization> orgs = new ArrayList<TblOrganization>();
        orgs.addAll(organizations);
        Collections.sort(orgs, new Comparator<TblOrganization>() {
            @Override
            public int compare(TblOrganization o1, TblOrganization o2) {
                return o1.getOrgid().compareTo(o2.getOrgid());
            }
        });
        StringBuffer orgids = new StringBuffer();
        StringBuffer orgName = new StringBuffer();
        for (int i = 0; i < orgs.size(); i++) {
            TblOrganization organization = orgs.get(i);
            if ((orgs.size() - 1) == i) {
                orgids.append(organization.getOrgid().toString());
                orgName.append(organization.getOrgname().toString());
            } else {
                orgids.append(organization.getOrgid().toString() + ",");
                orgName.append(organization.getOrgname().toString() + ",");
            }
        }
        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选择的机构
        List<TblNbsjTypeEntity> list = tblNbsjTypeService.findAll(attribute.getOrgid().toString());
        mv.addObject("sjlxList", list);
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
        mv.setViewName("nbsj/sjgj/jyk/experience_add");
        mv.addObject("temp", tblNbsjExperience);
        mv.addObject("orgids", orgids.toString());
        mv.addObject("orgName", orgName.toString());
        // mv.addObject("pageBean", pageBean);
        mv.addObject("type", type);
        return mv;
    }
	
	@Operation(summary="sjgj-删除")
	@RequestMapping(value = "/sjgj/experience_delete", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String experience_delete(BigDecimal[] id) {
        for (BigDecimal bigDecimal : id) {
             TblNbsjExperienceEntity tblNbsjExperience = this.nbsjExperienceService.getById(bigDecimal);
            if (null != tblNbsjExperience) {
                this.nbsjAduitExperienceService.deleteByTempId(bigDecimal);
                this.experienceTypeService.deleteByTempId(bigDecimal);
                this.nbsjExperienceService.delete(tblNbsjExperience);
            }
        }
        return JsonBean.success();
    }
	
	@Operation(summary="sjjy-index")
	@RequestMapping(value = "/sjjy/index")
    public ModelAndView sjjy_index(String tempId, String type,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("tempId", tempId);
        mv.addObject("sjzb", false);
        mv.addObject("type", type);
        String status = request.getParameter("status");
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
        mv.addObject("status", status);
        mv.setViewName("nbsj/sjgj/jyk/def_tmpl_index");
        return mv;
    }
	
	/**
     * 审计经验树形
     
    @Operation(summary="审计经验树形")
    @RequestMapping(value = "/sjjy/def_left_tree")
    public ModelAndView sjjy_def_left_tree(BigDecimal tempId, String type,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("tempId", tempId);
        mv.addObject("type", type);
        String status = request.getParameter("status");
        mv.addObject("status", status);
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
        mv.setViewName("nbsj/sjgj/jyk/def_left_tree");
        return mv;
    }
    
    /*@RequestMapping(value = "/sjjy/getTree", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String sjjy_gzjfindOrganizationByTree(Integer nodeId, String type, Integer tempId, HttpServletRequest request) {
        String view=request.getParameter("view");
		String choiceSearch = request.getParameter("choiceSearch");
        String url = "/nbsj/sjjy/def_cat_list?1=1&view="+view+"&choiceSearch="+choiceSearch;
        if (null == nodeId) {
            List<Tree> tree = this.experienceTypeService.getRoot(tempId, url);
            return JSONObject.toJSONString(tree);
        } else {
            List<Tree> list = this.experienceTypeService.getTree(nodeId, tempId, url);
            return JSONObject.toJSONString(list);
        }
    }
    
   /* @RequestMapping(value = "/sjjy/def_cat_list")
    public ModelAndView sjjy_def_cat_list(BigDecimal targetId, BigDecimal tempId, Integer pageNumber, String type,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String status = request.getParameter("status");
//		if (status != null && status.equals("0")) {
//			pageBean = this.tblAduitProGramService.findByTargetIdMb(targetId, tempId, pageNumber, pageBean.getPageSize());
//		} else {
        pageBean = this.nbsjAduitExperienceService.findByTargetId(targetId, tempId, pageNumber, pageBean.getPageSize());
        //}
        mv.addObject("pageBean", pageBean);
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
        mv.addObject("targetId", targetId);
        mv.setViewName("nbsj/sjgj/jyk/def_cat_list");
        mv.addObject("type", type);
        String view = request.getParameter("view");
        mv.addObject("view", view);
        mv.addObject("status", status);
        mv.addObject("tempId", tempId);
        return mv;
    }
    
    /**
     * 审计经验--树形新增
     *
     
    @Operation(summary="审计经验--树形新增")
    @RequestMapping(value = "/sjjy/audit_type_add")
    public ModelAndView exaudit_type_add(BigDecimal tempId, BigDecimal parentId,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("tempId", tempId);
        String status = request.getParameter("status");
        mv.addObject("parentId", parentId);
        mv.addObject("status", status);
        mv.setViewName("nbsj/sjgj/jyk/audit_type_add");
        return mv;
    }
    
    /**
     * 审计经验--修改跳转
     
    @Operation(summary="审计经验--修改跳转")
    @RequestMapping(value = "/sjjy/audit_type_update")
    public ModelAndView exaudit_type_update(BigDecimal tempId, BigDecimal nodeId) {
        ModelAndView mv = new ModelAndView();
       TblExperienceTypeEntity tblExperienceType = this.experienceTypeService.getById(nodeId);
        if (null != tblExperienceType) {
            mv.addObject("target", tblExperienceType);
        }
        mv.addObject("tempId", tempId);
        mv.addObject("parentId", nodeId);
        mv.setViewName("nbsj/sjgj/jyk/audit_type_add");
        return mv;
    }
    
    /**
     * 保存树形
     *
     
    @Operation(summary="保存树形")
    @RequestMapping(value = "/sjjy/audit_type_save")
    public
    @ResponseBody
    String exaudit_type_save(Integer tempId, Integer parentId, String targetName, String targetDesc, BigDecimal targetId,HttpServletRequest request) {
     TblNbsjExperienceEntity tblNbsjExperience = this.nbsjExperienceService.getById(tempId);
        String status = request.getParameter("status");
        if (null != tblNbsjExperience) {
            if (null == targetId) {
            	  TblExperienceTypeEntity tblExperienceType=new  TblExperienceTypeEntity();
            	  tblExperienceType.setParentId(parentId);
            	  tblExperienceType.setNbsjExperience(tblNbsjExperience);
            	  tblExperienceType.setTargetName(targetName);
            	  tblExperienceType.setTargetDesc(targetDesc);
                if (status != null && status.trim().length() > 0) {
                	tblExperienceType.setStatus(Integer.parseInt(status));
                }
                this.experienceTypeService.save(tblExperienceType);
            } else {
               TblExperienceTypeEntity tblExperienceType = this.experienceTypeService.getById(targetId);
                if (null != tblExperienceType) {
                	tblExperienceType.setParentId(tblExperienceType.getParentId());
                    tblExperienceType.setNbsjExperience(tblNbsjExperience);
                    tblExperienceType.setTargetName(targetName);
                    tblExperienceType.setTargetDesc(targetDesc);
                    if (status != null && status.trim().length() > 0) {
                    	tblExperienceType.setStatus(Integer.parseInt(status));
                    }
                    this.experienceTypeService.merge(tblExperienceType);
                } else {
                    return JsonBean.error("保存失败！");
                }
            }
//            logger.info("保存审计目标成功！");
            return JsonBean.success();
        }
//        logger.info("保存审计目标失败！");
        return JsonBean.error("保存失败！");
    }
    
    @Operation(summary="sjjy-删除")
    @RequestMapping(value = "/sjjy/audit_type_del", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String exaudit_type_save(Integer tempId, Integer targetId) {
         TblExperienceTypeEntity tblExperienceType = this.experienceTypeService.getById(targetId);
        if (null != tblExperienceType) {
            int count = this.experienceTypeService.getCount(tempId, targetId);
            if (count == 0) {
                this.nbsjAduitExperienceService.deleteByTargetId(targetId);
                this.experienceTypeService.delete(tblExperienceType);
                return JsonBean.success();
            }
            return JsonBean.error("请先删除子集节点！");
        }
        return JsonBean.error();
    }
    
    @Operation(summary="sjjy-新增")
    @RequestMapping(value = "/sjjy/t06_trp_target_add_down")
    public ModelAndView ext06_trp_target_add_down(BigDecimal targetId, BigDecimal tempId,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgj/jyk/t06_trp_target_add_down");
        mv.addObject("tempId", tempId);
        String status = request.getParameter("status");
        mv.addObject("status", status);
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
        mv.addObject("targetId", targetId);
        return mv;
    }
    
    @Operation(summary="jyk/sjgj-新增")
    @RequestMapping(value = "/jyk/sjgj/template_add_inner", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String experience_add_inner(TblNbsjAduitExperienceEntity aduitTarget, BigDecimal tempId, BigDecimal targetId) {
        TblNbsjExperienceEntity tblNbsjExperience = this.nbsjExperienceService.getById(tempId);
         TblExperienceTypeEntity tblExperienceType = this.experienceTypeService.getById(targetId);
        aduitTarget.setNbsjExperience(tblNbsjExperience);
        aduitTarget.setExperienceType(tblExperienceType);
        if (null != aduitTarget.getProgramId()) {
            this.nbsjAduitExperienceService.merge(aduitTarget);
//            logger.info("修改审计目标");
        } else {
            this.nbsjAduitExperienceService.save(aduitTarget);
//            logger.info("保存审计目标");
        }
        return JsonBean.success();
    }
    
    /**
     * 审计目标删除
     
    @Operation(summary="审计目标删除")
    @RequestMapping(value = "/sjjy/target_delete", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String aduitExperience_delete(BigDecimal[] id) {
        for (BigDecimal bigDecimal : id) {
        	TblNbsjAduitExperienceEntity tblAduitTarget = this.nbsjAduitExperienceService.getById(bigDecimal);
            if (null != tblAduitTarget) {
                this.nbsjAduitExperienceService.delete(tblAduitTarget);
            }
        }
        return JsonBean.success();
    }
    
    @Operation(summary="sjjy-修改")
    @RequestMapping(value = "/sjjy/template_modify_inner")
    public ModelAndView experience_modify_inner(BigDecimal id,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String status = request.getParameter("status");
       TblNbsjAduitExperienceEntity tblNbsjAduitExperience = this.nbsjAduitExperienceService.getById(id);
        mv.setViewName("nbsj/sjgj/jyk/t06_trp_target_add_down");
        mv.addObject("tempId", tblNbsjAduitExperience.getNbsjExperience().getTempleteId());
        mv.addObject("targetId", tblNbsjAduitExperience.getExperienceType().getTargetId());
        mv.addObject("tblAduitTarget", tblNbsjAduitExperience);
        mv.addObject("status", status);
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
        return mv;
    }
    
//    @RequestMapping(value = "/sjjy/template_detail_inner")
//    public ModelAndView template_detail_inner(BigDecimal id,HttpServletRequest request) {
//        ModelAndView mv = new ModelAndView();
//        String status = request.getParameter("status");
//        TblAduitProGram tblAduitTarget = this.tblAduitProGramService.get(id);
//        mv.addObject("tempId", tblAduitTarget.getNbsjTemplete().getTempleteId());
//        mv.addObject("targetId", tblAduitTarget.getTarget().getTargetId());
//        mv.addObject("tblAduitTarget", tblAduitTarget);
//        mv.setViewName("nbsj/sjgj/jyk/t06_trp_target_detail_down");
//        mv.addObject("status", status);
//        return mv;
//    }
    
    @Operation(summary="sjjy-def_head")
    @RequestMapping(value = "/sjjy/def_head")
    public ModelAndView sjjy_def_head(String tempId, String type) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("tempId", tempId);
        mv.addObject("type", type);
        mv.setViewName("nbsj/sjgj/jyk/def_head");
        return mv;
    }
    
    @Operation(summary="sjgj-修改")
    @RequestMapping(value = "/sjgj/experiencelupdatestatus", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String experUpdateStatus(String id, Integer status) {
        if (StringUtils.isNotBlank(id)) {
            String[] ids = id.split(",");
            for (String str : ids) {
                TblNbsjExperienceEntity tblNbsjExperience = this.nbsjExperienceService.getById(new BigDecimal(str));
                if (null != tblNbsjExperience) {
                    if (!tblNbsjExperience.getStatus().equals(status)) {
                        if (TblNbsjExperienceEntity.UNNORMAL.equals(status))
                        	tblNbsjExperience.setStatus(TblNbsjExperienceEntity.UNNORMAL);
                        else
                        	tblNbsjExperience.setStatus(TblNbsjExperienceEntity.NORMAL);
                        this.nbsjExperienceService.saveOrUpdate(tblNbsjExperience);
                    }
                }
            }
            return JsonBean.success();
        }
        return JsonBean.error("修改失败");
    }
    
    /*@RequestMapping(value = "/left")
    public ModelAndView left(HttpServletRequest request)  throws Exception{
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/left");
        
        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
		TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
		
		mv.addObject("staffId",user.getStaffid());
		mv.addObject("username", user.getUsername());
		mv.addObject("password", user.getPassword());
    	String token = EncryptUtil.getInstance().DESencode("fengkong1", "hbyun");
		mv.addObject("token", token);
		
		List<TblBiReportMenu> menuList = this.tblManageRightService.findBiReportList(attribute.getOrgid(),new BigDecimal("102"));
		long time = new Date().getTime();
		 System.out.println(time);
		 MessageDigest md5 = MessageDigest.getInstance("MD5");
		 byte[] digest = md5.digest(("hbyfk"+"hbyfk147852"+time).getBytes("utf-8"));
		 String encodeBase64String = Base64.encodeBase64String(digest);
		 System.out.println(encodeBase64String);
		 String sign=URLEncoder.encode(encodeBase64String.trim(),"utf-8");
		 System.out.println(sign);
		 mv.addObject("time",time );
		 mv.addObject("sign",sign );
		 
		mv.addObject("menuList", menuList);
		mv.addObject("orgId",attribute.getOrgid());
        return mv;
    }
    
//    /**
//     * 审计分析读取数据
//     
//    @RequestMapping(value = "/sjfx")
//    public ModelAndView sjfx(HttpServletRequest request) {
//        ModelAndView mv = new ModelAndView();
//        String pid = "";
//        TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
//        List<TblBiPage> pages = tblDataDisplayService.findByOrgidIsSJ(user.getStaffid().toString(), user.getTblOrganization().getOrgid().toString());
//        if (pages != null && pages.size() > 0) {
//            TblBiPage page = pages.get(0);
//            pid = page.getPageid().toString();
//            List<TblBiChart> cList = tblBiChartService.find(pid);
//            String chartNames = "";
//            String chartTypes = "";
//            if (cList != null) {
//                for (TblBiChart c : cList) {
//                    chartNames += "\"" + c.getChartname() + "\",";
//                    chartTypes += "\"" + c.getChartname() + "\":\"" + c.getCharttype() + "\",";
//                }
//            }
//            if (chartNames.length() > 0) {
//                chartNames = chartNames.substring(0, chartNames.length() - 1);
//                chartTypes = chartTypes.substring(0, chartTypes.length() - 1);
//            }
//            mv.addObject("pid", pid);
//            mv.addObject("cnames", chartNames);
//            mv.addObject("ctypes", chartTypes);
//            mv.addObject("counter", 0);
//        }
//        mv.setViewName("qygk/bi/show");
//        return mv;
//    }
    @Operation(summary="main")
    @RequestMapping(value = "/main")
    public ModelAndView main() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/main41");
        return mv;
    }
    
    /**
     * 审计经验库入口
     
    @Operation(summary="审计经验库入口")
    @RequestMapping(value = "/hysh_main")
    public ModelAndView hyzsk_main() {
//        logger.info("审计经验库---主页面");
        ModelAndView mv = new ModelAndView("nbsj/zyk/hysj_min");
        return mv;
    }
    
    /**
     * 审计经验库左侧菜单
     
    @Operation(summary="审计经验库左侧菜单")
    @RequestMapping(value = "/hysj_left")
    public ModelAndView hyzsk_left(HttpServletRequest request, Integer pageNumber) {
//        logger.info("审计经验库--左侧菜单");
        ModelAndView mv = new ModelAndView("nbsj/zyk/hysj_left_tree");
        return mv;
    }
    
    /**
     * 获取审计经验库分类
     
    /*@RequestMapping(value = "/sjjy/getjytree", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String getJYTree(BigDecimal nodeId) {
        if (null != nodeId) {
            List<Tree> child = this.tblNbsjAuditExperienceTypeService.getChild(nodeId);
            return JSONObject.toJSONString(child);
        } else {
            List<Tree> root = this.tblNbsjAuditExperienceTypeService.getRoot();
            return JSONObject.toJSONString(root);
        }
    }
    
    /**
     * 审计经验库列表
     
    /*@RequestMapping(value = "/hyzsk_list")
    public ModelAndView hyzsk_list(HttpServletRequest request, String title, String autor, Integer pageNumber, BigDecimal typeId) {
//        logger.info("审计经验库--列表页");
        String title1=request.getParameter("title1");
        pageBean = this.tblNbsjAuditExperienceService.findByType(typeId, title1, autor, pageNumber, pageBean.getPageSize());
        ModelAndView mv = new ModelAndView("nbsj/zyk/hysj_list");
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
        mv.addObject("typeId", typeId);
        mv.addObject("title1", title1);
        mv.addObject("pageBean", pageBean);
        return mv;
    }
    
    /**
     * 跳转新增审计经验库类别
     
    @Operation(summary="跳转新增审计经验库类别")
    @RequestMapping(value = "/toadd_organ")
    public ModelAndView risk_type_toAdd(BigDecimal parentId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("parentId", parentId);
        mv.setViewName("nbsj/zyk/type_add");
        return mv;
    }
    
    /**
     * 跳转修改审计经验库类别
     
    @Operation(summary="跳转修改审计经验库类别")
    @RequestMapping(value = "/toModify_organ")
    public ModelAndView toModify_organ(BigDecimal nodeId) {
        TblNbsjAuditExperienceTypeEntity experienceType = this.tblNbsjAuditExperienceTypeService.getById(nodeId);
        ModelAndView mv = new ModelAndView();
        if (null != experienceType) {
            mv.addObject("experienceType", experienceType);
        }
        mv.setViewName("nbsj/zyk/type_add");
        return mv;
    }
    
    /**
     * 保存或修改审计经验库
     
   /* @RequestMapping(value = "/add_organ")
    public
    @ResponseBody
    String add_organ(String typeName, BigDecimal parentId, BigDecimal typeId, HttpServletRequest request) {
        if (null == typeId) {
            TblNbsjAuditExperienceTypeEntity experienceType = this.tblNbsjAuditExperienceTypeService.getById(typeId);
            if (null != experienceType) {
                TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
                TblNbsjAuditExperienceTypeEntity auditExperienceType = new TblNbsjAuditExperienceTypeEntity();
                auditExperienceType.setCreateStaff(user);
                auditExperienceType.setCreateTime(new Date());
                auditExperienceType.setTypeName(typeName);
                auditExperienceType.setParentId(parentId);
                this.tblNbsjAuditExperienceTypeService.save(auditExperienceType);
                return JsonBean.success();
            }
        } else {
            TblNbsjAuditExperienceTypeEntity experienceType = this.tblNbsjAuditExperienceTypeService.getById(typeId);
            if (null != experienceType) {
                experienceType.setTypeName(typeName);
                experienceType.setUpdateTime(new Date());
                this.tblNbsjAuditExperienceTypeService.merge(experienceType);
                return JsonBean.success();
            }
        }
        return JsonBean.error("保存失败！");
    }
    
    /**
     * 删除审计经验库类别
     
    @Operation(summary="删除审计经验库类别")
    @RequestMapping(value = "/del_organ", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String risk_type_disp(BigDecimal targetId) {
        TblNbsjAuditExperienceTypeEntity experienceType = this.tblNbsjAuditExperienceTypeService.getById(targetId);
        if (null != experienceType) {
            boolean isChild = this.tblNbsjAuditExperienceTypeService.IsChild(targetId);
            if (isChild) {
                this.tblNbsjAuditExperienceTypeService.delete(experienceType);
                return JsonBean.success();
            }
            return JsonBean.error("请先删除子节点！");
        } else {
            return JsonBean.error("节点不存在！");
        }
    }
    
    /**
     * 添加审计经验
     
    @Operation(summary="添加审计经验")
    @RequestMapping(value = "/toAddsjhy")
    public ModelAndView toAddhyzsk(HttpServletRequest request, BigDecimal typeId) {
//        logger.info("审计经验库--列表页---跳往新建页");
        TblNbsjAuditExperienceTypeEntity type = this.tblNbsjAuditExperienceTypeService.getById(typeId);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/zyk/sj_add");
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 		
        mv.addObject("type", type);
        return mv;
    }
    
    /**
     * 跳转审计经验库基本信息
     
    @Operation(summary="跳转审计经验库基本信息")
    @RequestMapping(value = "/updateAddsjhy")
    public ModelAndView updateAddsjhy(HttpServletRequest request, BigDecimal experId, BigDecimal typeId) {
//        logger.info("审计经验库--列表页---跳往新建页");
        ModelAndView mv = new ModelAndView();
        if (null != experId) {
            TblNbsjAuditExperienceEntity experience = this.tblNbsjAuditExperienceService.getById(experId);
            mv.addObject("experience", experience);
            mv.addObject("type", experience.getAuditExperienceType());
            mv.addObject("view", request.getParameter("view"));
        }
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
        mv.setViewName("nbsj/zyk/sj_add");
        return mv;
    }
	
    @Operation(summary="查询")
    @RequestMapping(value = "/findAddsjhy")
    public ModelAndView findAddsjhy(HttpServletRequest request, BigDecimal experId, BigDecimal typeId) {
//        logger.info("审计经验库--列表页---跳往详情页");
        ModelAndView mv = new ModelAndView();
        if (null != experId) {
            TblNbsjAuditExperienceEntity experience = this.tblNbsjAuditExperienceService.getById(experId);
            mv.addObject("experience", experience);
            mv.addObject("view", "c");
            mv.addObject("type", experience.getAuditExperienceType());
        }
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
        mv.setViewName("nbsj/zyk/sj_add");
        return mv;
    }
    
    /**
     * 审计经验库基本信息保存
     
    /*@RequestMapping(value = "/sj_add", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String fxzsk_add(HttpServletRequest request, TblNbsjAuditExperienceEntity auditExperience, String submitTimes, BigDecimal typeId) {
//        logger.info("审计经验库--列表页---保存");
        TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
        TblNbsjAuditExperienceTypeEntity type = this.tblNbsjAuditExperienceTypeService.getById(typeId);
        Date subTime = DateUtils.parse(submitTimes, DateUtils.DATE_SMALL_STR);
        if (null != type) {
            if (null != auditExperience.getExperId()) {
                auditExperience.setUpdateTime(new Date());
                auditExperience.setSubmitTime(subTime);
                auditExperience.setAuditExperienceType(type);
                auditExperience.setCreateStaff(user);
                this.tblNbsjAuditExperienceService.merge(auditExperience);
                return JsonBean.success(auditExperience.getExperId().toString());
            } else {
                auditExperience.setSubmitTime(subTime);
                auditExperience.setAuditExperienceType(type);
                auditExperience.setCreateStaff(user);
                auditExperience.setCreateTime(new Date());
                Serializable serializable = this.tblNbsjAuditExperienceService.save(auditExperience);
                return JsonBean.success((String) serializable);
            }
        }
        return JsonBean.error("保存失败！");
    }
    
    /**
     * 所需资料列表
     
    /*@RequestMapping(value = "/datum_list")
    public ModelAndView datum_list(BigDecimal experId, Integer pageNumber,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/zyk/datum_list");
        pageBean = this.tblNbsjAuditDatumService.findByExperId(experId, pageNumber, pageBean.getPageSize());
        TblNbsjAuditExperienceEntity experience = this.tblNbsjAuditExperienceService.getById(experId);
        mv.addObject("type", experience.getAuditExperienceType());
        mv.addObject("experId", experId);
        mv.addObject("pageBean", pageBean);
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
        mv.addObject("view", request.getParameter("view"));
        return mv;
    }
    
    @Operation(summary="查询")
    @RequestMapping(value = "/datum_find")
    public ModelAndView datum_find(BigDecimal experId, Integer pageNumber) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/zyk/datum_find");
        pageBean = this.tblNbsjAuditDatumService.findByExperId(experId, pageNumber, pageBean.getPageSize());
        mv.addObject("experId", experId);
        mv.addObject("pageBean", pageBean);
        return mv;
    }
    
    /**
     * 添加所需资料
     
    @Operation(summary="添加所需资料")
    @RequestMapping(value = "/datum_add")
    public ModelAndView datum_add(BigDecimal experId,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/zyk/datum_add");
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
        mv.addObject("experId", experId);
        return mv;
    }
    
    /**
     * 所需资源--修改跳转
     
    @Operation(summary="所需资源--修改跳转")
    @RequestMapping(value = "/datum_update")
    public ModelAndView datum_update(BigDecimal experId, BigDecimal datumId,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (null != datumId) {
            TblNbsjAuditDatumEntity auditDatum = this.tblNbsjAuditDatumService.getById(datumId);
            mv.addObject("auditDatum", auditDatum);
        }
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
        mv.setViewName("nbsj/zyk/datum_add");
        mv.addObject("experId", experId);
        return mv;
    }
    
    /**
     * 保存or修改所需资料
     
   /* @RequestMapping(value = "/datum_save", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String datum_save(HttpServletRequest request, TblNbsjAuditDatumEntity auditDatum, BigDecimal experId) {
//        logger.info("审计经验库--所需资料---保存");
        if (null != experId) {
            TblNbsjAuditExperienceEntity experience = this.tblNbsjAuditExperienceService.getById(experId);
            TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
            if (null != experience) {
                if (null != auditDatum.getDatumId()) {
                    auditDatum.setAuditExperience(experience);
                    auditDatum.setUpdateTime(new Date());
                    auditDatum.setCreateTime(new Date());
                    auditDatum.setCreateStaff(user);
                    this.tblNbsjAuditDatumService.merge(auditDatum);
                    return JsonBean.success(auditDatum.getDatumId().toString());
                } else {
                    auditDatum.setAuditExperience(experience);
                    auditDatum.setCreateTime(new Date());
                    auditDatum.setCreateStaff(user);
                    Serializable serializable = this.tblNbsjAuditDatumService.save(auditDatum);
                    return JsonBean.success((String) serializable);
                }
            }
        }
        return JsonBean.error("保存失败！");
    }
    
    /**
     * 删除所需资料
     
    @Operation(summary="删除所需资料")
    @RequestMapping(value = "/datum_del", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String datum_del(String id, HttpServletRequest request) {
//        logger.info("审计经验库--所需资料---删除");
        if (StringUtils.isNotBlank(id)) {
            String[] ids = id.split(",");
            for (String str : ids) {
                TblNbsjAuditDatumEntity datum = this.tblNbsjAuditDatumService.getById(new BigDecimal(str));
                if (null != datum) {
                    this.tblNbsjAuditDatumService.delete(datum);
                }
            }
            return JsonBean.success();
        }
        return JsonBean.error("删除失败！");
    }
    
    /**
     * 审计步骤--list
     
   /* @RequestMapping(value = "/step_list")
    public ModelAndView step_list(BigDecimal experId, Integer pageNumber,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        pageBean = this.tblNbsjAuditStepService.findByExper(experId, pageNumber, pageBean.getPageSize());
        mv.setViewName("nbsj/zyk/step_list");
        TblNbsjAuditExperienceEntity experience = this.tblNbsjAuditExperienceService.getById(experId);
        mv.addObject("type", experience.getAuditExperienceType());
        mv.addObject("experId", experId);
        mv.addObject("pageBean", pageBean);
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
        mv.addObject("view", request.getParameter("view"));
        return mv;
    }
    
    @Operation(summary="查询")
    @RequestMapping(value = "/step_find")
    public ModelAndView step_find(BigDecimal experId, Integer pageNumber) {
        ModelAndView mv = new ModelAndView();
        pageBean = this.tblNbsjAuditStepService.findByExper(experId, pageNumber, pageBean.getPageSize());
        mv.setViewName("nbsj/zyk/step_find");
        mv.addObject("experId", experId);
        mv.addObject("pageBean", pageBean);
        return mv;
    }
    
    /**
     * 审计步骤--添加
     
    @Operation(summary="审计步骤--添加")
    @RequestMapping(value = "/step_add")
    public ModelAndView step_add(BigDecimal experId,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/zyk/step_add");
        mv.addObject("experId", experId);
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
        return mv;
    }
    
    /**
     * 审计步骤保存or修改
     
    @Operation(summary="审计步骤保存or修改")
    @RequestMapping(value = "/step_save", produces = "application/json; charset=utf-8")
    public  @ResponseBody  String step_save(HttpServletRequest request, TblNbsjAuditStepEntity auditStep, BigDecimal experId) {
//        logger.info("审计经验库--所需资料---保存");
        if (null != experId) {
            TblNbsjAuditExperienceEntity experience = this.tblNbsjAuditExperienceService.getById(experId);
            if (null != experience) {
                if (null != auditStep.getStepId()) {
                    auditStep.setAuditExperience(experience);
                    auditStep.setUpdateTime(new Date());
                    auditStep.setCreateTime(new Date());
                    this.tblNbsjAuditStepService.merge(auditStep);
                    return JsonBean.success(auditStep.getStepId().toString());
                } else {
                    auditStep.setAuditExperience(experience);
                    auditStep.setCreateTime(new Date());
                    Serializable serializable = this.tblNbsjAuditStepService.save(auditStep);
                    return JsonBean.success((String) serializable);
                }
            }
        }
        return JsonBean.error("保存失败！");
    }
    
    /**
     * 审计步骤 跳转修改
     
    @Operation(summary="审计步骤 跳转修改")
    @RequestMapping(value = "/step_update")
    public ModelAndView step_update(BigDecimal experId, BigDecimal stepId,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (null != stepId) {
            TblNbsjAuditStepEntity step = this.tblNbsjAuditStepService.getById(stepId);
            List<TblNbsjAuditStepSQLEntity> list = this.tblNbsjAuditStepSQLService.findByStep(stepId);
            mv.addObject("list", list);
            mv.addObject("step", step);
        }
        mv.setViewName("nbsj/zyk/step_add");
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 		
        mv.addObject("experId", experId);
        return mv;
    }
    
    /**
     * 审计步骤--删除
     
    @Operation(summary="审计步骤--删除")
    @RequestMapping(value = "/step_del", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String step_del(String id, HttpServletRequest request) {
//        logger.info("审计经验库--审计步骤---删除");
        if (StringUtils.isNotBlank(id)) {
            String[] ids = id.split(",");
            for (String str : ids) {
                TblNbsjAuditStepEntity auditStep = this.tblNbsjAuditStepService.getById(new BigDecimal(str));
                if (null != auditStep) {
                    List<TblNbsjAuditStepSQLEntity> list = this.tblNbsjAuditStepSQLService.findByStep(auditStep.getStepId());
                    for (TblNbsjAuditStepSQLEntity tblNbsjAuditStepSQL : list) {
                        this.tblNbsjAuditStepSQLService.delete(tblNbsjAuditStepSQL);
                    }
                    this.tblNbsjAuditStepService.delete(auditStep);
                }
            }
            return JsonBean.success();
        }
        return JsonBean.error("删除失败！");
    }
    
    /**
     * 跳转添加类sql
     
    @Operation(summary="跳转添加类sql")
    @RequestMapping(value = "/class_sql")
    public ModelAndView add_sql(BigDecimal stepId, BigDecimal experId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/zyk/sql_add");
        mv.addObject("stepId", stepId);
        mv.addObject("experId", experId);
        return mv;
    }
    
    /**
     * 保存or修改类sql
     
    @Operation(summary="保存or修改类sql")
    @RequestMapping(value = "/classsql_save", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String classsql_save(String sqlContent, BigDecimal stepId, BigDecimal sqlId) {
        TblNbsjAuditStepEntity stepSQL = this.tblNbsjAuditStepService.getById(stepId);
        if (null != stepSQL) {
            if (null != sqlId) {
                TblNbsjAuditStepSQLEntity auditStepSQL = this.tblNbsjAuditStepSQLService.getById(sqlId);
                auditStepSQL.setSqlContent(sqlContent);
                auditStepSQL.setUpdateTime(new Date());
                auditStepSQL.setCreateTime(auditStepSQL.getCreateTime());
                this.tblNbsjAuditStepSQLService.merge(auditStepSQL);
                return JsonBean.success();
            } else {
                TblNbsjAuditStepSQLEntity auditStepSQL = new TblNbsjAuditStepSQLEntity();
                auditStepSQL.setCreateTime(new Date());
                auditStepSQL.setAuditStep(stepSQL);
                auditStepSQL.setSqlContent(sqlContent);
                this.tblNbsjAuditStepSQLService.save(auditStepSQL);
                return JsonBean.success();
            }
        }
        return JsonBean.error("保存失败！");
    }
    
    /**
     * 类sql 跳转修改
     
    @Operation(summary="类sql 跳转修改")
    @RequestMapping(value = "/classsql_update")
    public ModelAndView classsql_update(BigDecimal stepId, BigDecimal experId, BigDecimal id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/zyk/sql_add");
        TblNbsjAuditStepSQLEntity stepSQL = this.tblNbsjAuditStepSQLService.getById(id);
        if (null != stepSQL) {
            mv.addObject("stepSQL", stepSQL);
        }
        mv.addObject("stepId", stepId);
        mv.addObject("experId", experId);
        return mv;
    }
    
    /**
     * 类sql删除
     
    @Operation(summary="类sql删除")
    @RequestMapping(value = "/classsql_del", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String classsql_del(String id) {
        String[] ids = id.split(",");
        for (String str : ids) {
            TblNbsjAuditStepSQLEntity stepSQL = this.tblNbsjAuditStepSQLService.getById(new BigDecimal(str));
            this.tblNbsjAuditStepSQLService.delete(stepSQL);
        }
        return JsonBean.success();
    }
    
    /**
     * 删除审计经验库
     *
     * @param request
     * @param othids
     * @return
     
    @Operation(summary="删除审计经验库")
    @RequestMapping(value = "/sj_del")
    public
    @ResponseBody
    String sj_del(HttpServletRequest request, String[] othids) {
        for (String str : othids) {
            this.tblNbsjAuditExperOutRulService.deleteByExperId(new BigDecimal(str));
            this.tblNbsjAuditModelService.deleteByExperId(new BigDecimal(str));
            List<TblNbsjAuditStepEntity> findByExper = this.tblNbsjAuditStepService.findByExper(new BigDecimal(str));
            for (TblNbsjAuditStepEntity tblNbsjAuditStep : findByExper) {
                this.tblNbsjAuditStepSQLService.deleteByStepId(tblNbsjAuditStep.getStepId());
            }
            this.tblNbsjAuditStepService.deleteByExperId(new BigDecimal(str));
            this.tblNbsjAuditDatumService.deleteByExperId(new BigDecimal(str));
            this.tblNbsjAuditExperienceService.delete(new BigDecimal(str));
            return JsonBean.success();
        }
        return JsonBean.error();
    }
    
    /**
     * 审计经验库上传附件
     *
     * @param file
     * @param request
     * @param othartid
     * @return
     
    /*@RequestMapping(value = "/sjk_upload")
    public ModelAndView hyzsk_upload(MultipartFile file, HttpServletRequest request, BigDecimal experId, BigDecimal datumId) {
//        logger.info("管控智库--》行业知识库--》新建---添加附件");
        TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
        //String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/sjupload/");
        String path=DOCDICTY+"/upload/sjupload/" ;//修改成项目配置的统一文件夹路径
        String fileName = file.getOriginalFilename();
        long size = file.getSize();
        String url = "/upload/sjupload/";
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            TblAttachmentEntity a = new TblAttachmentEntity();
            a.setAttname(fileName);
            TblNbsjAuditDatumEntity auditDatum = this.tblNbsjAuditDatumService.getById(datumId);
//            a.getAuditDatums().add(auditDatum);
            a.setAttpath(url + fileName);
            a.setAttsize(new BigDecimal(size / 1024));
            a.setUploader(user.getUsername());
            a.setUploadtime(new Date());
            attachmentService.add(a);
            file.transferTo(targetFile);
        } catch (Exception e) {
//            logger.error("审计经验库-文件上传失败");
        }
        return datum_update(experId, datumId, request);
    }
    
    /**
     * 删除审计经验库附件
     
    @Operation(summary="删除审计经验库附件")
    @RequestMapping(value = "/sj_upload_del")
    public ModelAndView hyzsk_upload_del(HttpServletRequest request, String audid, String attid) {
        TblAttachmentEntity a = new TblAttachmentEntity();
        a.setAttid(new BigDecimal(attid));
        TblAuditexperienceEntity au = new TblAuditexperienceEntity();
        au.setAuditexperid(new BigDecimal(audid));
        attachmentService.delete(a);
        return null;
        // return toAddhyzsk(request,audid);
    }
    
    /**
     * 自动审计
     
    @Operation(summary="自动审计")
    @RequestMapping(value = "/sjgj/auto_audit")
    public ModelAndView auto_audit() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgj/auto_audit_index");
        return mv;
    }
    @Operation(summary="sjgj-获取树结构")
    @RequestMapping(value = "/sjgj/auto_left_tree")
    public ModelAndView auto_left_tree() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgj/auto_left_tree");
        return mv;
    }
    @Operation(summary="sjgj-auto_question")
    @RequestMapping(value = "/sjgj/auto_question")
    public ModelAndView auto_question() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgj/auto_question");
        return mv;
    }
   /* @RequestMapping(value = "/sjgj/auto_main")
    public ModelAndView auto_main(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String number = request.getParameter("pageNumber");
        Integer pageNumber = 1;// 分页管理当前页码
        if (number != null && !"".equals(number)) {
            pageNumber = Integer.parseInt(number);
        }
        String book = (String) request.getSession().getAttribute("book");// 获取选中的账套数据
        TblStaff ts = (TblStaff) request.getSession().getAttribute("longUser");
        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");//选则的机构
//        pageBean = tblacctbookService.findByTypeByGS(ts, TblAcctbook.ZB_TYPE, pageNumber, pageBean.getPageSize(),attribute.getOrgid().toString());
        mv.addObject("id", book);
        mv.addObject("pageBean", pageBean);
        mv.setViewName("nbsj/sjgj/auto_book_list");
        return mv;
    }
   /* @RequestMapping(value = "/sjgj/getjytreeTitle", produces = "application/json; charset=utf-8")
    public @ResponseBody  String getjytreeTitle(BigDecimal nodeId) {
        if (null != nodeId) {
            List<Tree> child = this.tblNbsjAuditExperienceTypeService.getChildByTitle(nodeId);
            return JSONObject.toJSONString(child);
        } else {
            List<Tree> root = this.tblNbsjAuditExperienceTypeService.getRoot();
            return JSONObject.toJSONString(root);
        }
    }
    @Operation(summary="sjgj-auto_execute")
    @RequestMapping(value = "sjgj/auto_execute", produces = "application/json; charset=utf-8")
    public  @ResponseBody  String execute(String jingyan, String book) {
        if (StringUtils.isNotBlank(jingyan) && StringUtils.isNotBlank(book)) {
            String[] jys = jingyan.split(",");
            String[] books = book.split(",");
            for (String str : jys) {
                //this.tblNbsjAuditStepSQLService.findByStep(stepId)
            }
        }
        return JsonBean.error();
    }
    
    
    
    @Operation(summary="xmgl-列表查询")
    @RequestMapping(value = "/xmgl/proj_log_list")
    public ModelAndView proj_log_list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/xmgl/proj_log_list");
        return mv;
    }
    @Operation(summary="xmgl-新增")
    @RequestMapping(value = "/xmgl/role_manage_add")
    public ModelAndView role_manage_add() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/xmgl/role_manage_add");
        return mv;
    }
    @Operation(summary="xmgl-修改")
    @RequestMapping(value = "/xmgl/role_manage_modify")
    public ModelAndView role_manage_modify() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/xmgl/role_manage_modify");
        return mv;
    }
    @Operation(summary="xmgl-列表查询")
    @RequestMapping(value = "/xmgl/read_list")
    public ModelAndView read_list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/xmgl/read_list");
        return mv;
    }
    @Operation(summary="xmgl-列表查询")
    @RequestMapping(value = "/xmgl/t06_receive_reader_list")
    public ModelAndView t06_receive_reader_list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/xmgl/t06_receive_reader_list");
        return mv;
    }
    @Operation(summary="sjgl-audit_plan_edit")
    @RequestMapping(value = "/sjgl/audit_plan_edit")
    public ModelAndView audit_plan_edit() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgl/audit_plan_edit");
        return mv;
    }
    @Operation(summary="sjgl-audit_plan_edit_02")
    @RequestMapping(value = "/sjgl/audit_plan_edit_02")
    public ModelAndView audit_plan_edit_02() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgl/audit_plan_edit_02");
        return mv;
    }
    @Operation(summary="sjgl-列表查询")
    @RequestMapping(value = "/sjgl/audit_plan_flow_list")
    public ModelAndView audit_plan_flow_list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgl/audit_plan_flow_list");
        return mv;
    }
    @Operation(summary="sjgl-列表查询")
    @RequestMapping(value = "/sjgl/audit_plan_list")
    public ModelAndView audit_plan_list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgl/audit_plan_list");
        return mv;
    }
    @Operation(summary="sjgl-新增")
    @RequestMapping(value = "/sjgl/project_plan_add")
    public ModelAndView project_plan_add() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgl/project_plan_add");
        return mv;
    }
    /**
     * 计划管理
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @author SongXiangYing
     * @date 2016年8月29日 下午4:20:14
     
    @Operation(summary="</p>")
    @RequestMapping(value = "/sjgl/sjjh_main")
    public ModelAndView sjjh_main() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgl/sjjh_main");
        return mv;
    }
    /**
     * 审计计划查看
     *
     * @return
     
    @Operation(summary="审计计划查看")
    @RequestMapping(value = "/jhck/sjjh_main")
    public ModelAndView jhck_main() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgl/jhck_main");
        return mv;
    }
    @Operation(summary="jhck-获取左侧树结构")
    @RequestMapping(value = "/jhck/treeleft")
    public ModelAndView jhck_organ_left(HttpServletRequest request) {
        TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        ModelAndView mv = new ModelAndView("nbsj/sjgl/jhck_organ_left");
        mv.addObject("orgid", organization.getOrgid());
        return mv;
    }
    @Operation(summary="jhck-获取树结构")
    @RequestMapping(value = "/jhck/findOrganizationByTree", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String jhck_findOrganizationByTrees(BigDecimal nodeId, BigDecimal orgId, HttpServletRequest request) {
        String json = "";
        if (null == nodeId) {
            TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 当前用户的机构
            nodeId = attribute1.getOrgid();
            List<Tree> list = this.tblOrganizaService.getTree(nodeId);
            for (Tree tree : list) {
                tree.setTarget("mainFramex");
                tree.setUrl("/nbsj/sjgl/audit_plan_list_search?orgid=" + tree.getId());
            }
            json = JSONObject.toJSONString(list);
        } else {
            List<Tree> list = this.tblOrganizaService.getNodeAll(nodeId);
            for (Tree tree : list) {
                tree.setTarget("mainFramex");
                tree.setUrl("/nbsj/sjgl/audit_plan_list_search?orgid=" + tree.getId());
            }
            json = JSONObject.toJSONString(list);
        }
        return json;
    }
    /**
     * 审计归档
     *
     * @return
     
    @Operation(summary="审计归档")
    @RequestMapping(value = "/sjgd/sjjh_main")
    public ModelAndView sjgd_main() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjgl/sjgd_main");
        return mv;
    }
    @Operation(summary="sjgd-获取左侧树结构")
    @RequestMapping(value = "/sjgd/treeleft")
    public ModelAndView sjgd_organ_left(HttpServletRequest request) {
        TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        ModelAndView mv = new ModelAndView("nbsj/sjgl/sjgd_organ_left");
        mv.addObject("orgid", organization.getOrgid());
        return mv;
    }
    @Operation(summary="sjgl-获取左侧树结构")
    @RequestMapping(value = "/sjgl/treeleft")
    public ModelAndView orgLeft(HttpServletRequest request) {
        TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        ModelAndView mv = new ModelAndView("nbsj/sjgl/organ_left");
        mv.addObject("orgid", organization.getOrgid());
        return mv;
    }
    @Operation(summary="sjgd-列表查询")
    @RequestMapping(value = "/sjgd/plan_list")
    public ModelAndView sjgd_view(HttpServletRequest request) {
        String orgid = request.getParameter("orgid");
        String jhName = request.getParameter("jhName");
        String planYear = request.getParameter("planYear");
        String pageNumber = request.getParameter("pageNumber");
        if (pageNumber == null || pageNumber == "") {
            pageNumber = "1";
        }
        if (StringUtils.isBlank(orgid)) {
            TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 当前用户的机构
            orgid = attribute1.getOrgid().toString();
        }
        pageBean = tblnbsjauditPlanService.findByTblnbsjauditPlanlistByorgid(orgid, jhName,planYear, Integer.parseInt(pageNumber), pageBean.getPageSize());
        ModelAndView mv = new ModelAndView();
        mv.addObject("orgid", orgid);
        mv.addObject("pageBean", pageBean);
      //查询框代码
        String choiceSearch = request.getParameter("choiceSearch");
        if(choiceSearch == null || "".equals(choiceSearch)) {
        	choiceSearch = "hide";
        }
        mv.addObject("choiceSearch",choiceSearch); 
        mv.setViewName("nbsj/sjgl/sjgd_project_plan_list");
        return mv;
    }
    /**
     * 查询计划下详情及对应项目列表
     *
     * @param request
     * @param planid
     * @return
     
    @Operation(summary="查询计划下详情及对应项目列表")
    @RequestMapping(value = "/sjgd/audit_plan_list_planId")
    public ModelAndView sjgd_plan_list_planId(HttpServletRequest request, BigDecimal planid) {
        ModelAndView mv = new ModelAndView();
        TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
        String pageNumber = request.getParameter("pageNumber");
        String projectname = request.getParameter("projectname");
        if (pageNumber == null || pageNumber == "") {
            pageNumber = "1";
        }
        String orgid = request.getParameter("orgid");
        mv.addObject("projectname", projectname);
        pageBean = this.tblnbsjProjectService.findByProjectListByplanid(projectname, Integer.parseInt(pageNumber), pageBean.getPageSize());
        mv.addObject("pageBean", pageBean);
        mv.addObject("orgid", orgid);
      //查询框代码
        String choiceSearch = request.getParameter("choiceSearch");
        if(choiceSearch == null || "".equals(choiceSearch)) {
        	choiceSearch = "hide";
        }
        mv.addObject("choiceSearch",choiceSearch); 
        mv.setViewName("nbsj/sjgl/sjgd_project_hold_list");
        return mv;
    }
    @Operation(summary="sjgd-列表查询")
    @RequestMapping(value = "/sjgd/audit_plan_list_planId_in")
    public ModelAndView sjgd_plan_list_planId_in(HttpServletRequest request, BigDecimal planid) {
        ModelAndView mv = new ModelAndView();
        String pageNumber = request.getParameter("pageNumber");
        String projectname = request.getParameter("projectname");
        if (pageNumber == null || pageNumber == "") {
            pageNumber = "1";
        }
        TblNbsjProjectEntity selectProject = this.tblnbsjProjectService.getSelectProject();
        String orgid = request.getParameter("orgid");
        mv.addObject("projectname", projectname);
        pageBean = this.tblnbsjProjectService.findByProjectListByplanid(projectname, Integer.parseInt(pageNumber), pageBean.getPageSize());
        mv.addObject("pageBean", pageBean);
        mv.addObject("orgid", orgid);
      //查询框代码
        String choiceSearch = request.getParameter("choiceSearch");
        if(choiceSearch == null || "".equals(choiceSearch)) {
        	choiceSearch = "hide";
        }
        mv.addObject("choiceSearch",choiceSearch); 	
        mv.setViewName("nbsj/sjgl/sjgd_project_hold_list_in");
        return mv;
    }
    @Operation(summary="sjgd-列表查询")
    @RequestMapping(value = "/sjgd/audit_plan_list_planId_in_save", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String audit_plan_list_planId_in_save(HttpServletRequest request, String id) {
        if (StringUtils.isNotBlank(id)) {
            String[] ids = id.split(",");
            TblNbsjProjectEntity selectProject = this.tblnbsjProjectService.getSelectProject();
            if (null != selectProject) {
                for (String string : ids) {
                    TblNbsjProjectEntity project = this.tblnbsjProjectService.getById(string);
                    if (null != project) {
                        TblNbsjProjectDataEntity data = new TblNbsjProjectDataEntity();
                        data.setOldProjectId(project.getProjectId());
                        data.setProjectId(selectProject.getProjectId());
                        this.tblNbsjProjectDataService.save(data);
                    }
                }
                return JsonBean.success();
            }
            return JsonBean.error("未找到实施项目");
        }
        return JsonBean.error("导入失败");
    }
    @Operation(summary="sjgd-获取树结构")
    @RequestMapping(value = "/sjgd/findOrganizationByTree", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String sjgd_findOrganizationByTrees(BigDecimal nodeId, BigDecimal orgId, HttpServletRequest request) {
        String json = "";
        if (null == nodeId) {
            TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 当前用户的机构
            nodeId = attribute1.getOrgid();
            List<Tree> list = this.tblOrganizaService.getTree(nodeId);
            for (Tree tree : list) {
                tree.setTarget("mainFramexs");
                tree.setUrl("/nbsj/sjgd/plan_list?orgid=" + tree.getId());
            }
            json = JSONObject.toJSONString(list);
        } else {
            List<Tree> list = this.tblOrganizaService.getNodeAll(nodeId);
            for (Tree tree : list) {
                tree.setTarget("mainFramexs");
                tree.setUrl("/nbsj/sjgd/plan_list?orgid=" + tree.getId());
            }
            json = JSONObject.toJSONString(list);
        }
        return json;
    }
    @Operation(summary="获取树结构")
    @RequestMapping(value = "/findOrganizationByTree", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String findOrganizationByTrees(BigDecimal nodeId, BigDecimal orgId, HttpServletRequest request) {
        String json = "";
        /*
         * if (null == nodeId) { nodeId = orgId; if (null == orgId) {
		 * TblOrganization attribute1 = (TblOrganization)
		 * request.getSession().getAttribute("hbOrgName");//当前用户的机构 nodeId =
		 * attribute1.getOrgid(); } }
		 
        if (null == nodeId) {
            TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 当前用户的机构
            nodeId = attribute1.getOrgid();
            List<Tree> list = this.tblOrganizaService.getTree(nodeId);
            for (Tree tree : list) {
                // if (!tree.getIsParent()) {
                tree.setTarget("mainFramex");
                tree.setUrl("/nbsj/sjgl/project_plan_list?orgid=" + tree.getId());
                // }
            }
            json = JSONObject.toJSONString(list);
        } else {
            // TblOrganization attribute1 = (TblOrganization)
            // request.getSession().getAttribute("hbOrgName");//当前用户的机构
            // nodeId = attribute1.getOrgid();
            List<Tree> list = this.tblOrganizaService.getNodeAll(nodeId);
            for (Tree tree : list) {
                // if (!tree.getIsParent()) {
                tree.setTarget("mainFramex");
                tree.setUrl("/nbsj/sjgl/project_plan_list?orgid=" + tree.getId());
                // }
            }
            json = JSONObject.toJSONString(list);
        }
        return json;
    }*/
}
