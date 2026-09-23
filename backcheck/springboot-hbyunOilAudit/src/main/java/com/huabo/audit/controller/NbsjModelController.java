package com.huabo.audit.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 内部审计模型控制器
 * <p>提供内部审计模型的查询和管理接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/nbsj")
public class NbsjModelController {
	
	/*@Resource
	public PageBean pageBean;
	
	/*@Resource
	public TblNbsjAuditExperienceService tblNbsjAuditExperienceService;
	
	@Resource
	public TblNbsjAuditModelService tblNbsjAuditModelService;
	
	@Resource
	public TblNbsjAuditExperOutRulService tblNbsjAuditExperOutRulService;
	
	@Resource
	public TblNbsjOuterruleService nbsjouterruleService;
	
	@Resource
	public TblNbsjAuditStepService tblNbsjAuditStepService;
	
	private static final String DOCDIC = PropertyFileReader.getItem("doc.path");
    private static final String IMAGEURL = PropertyFileReader.getItem("image.url");
    private static final String DOCDICTY = PropertyFileReader.getItem("file.path");

	/**
     * 审计模型
     
    @Operation(summary="审计模型")
    @RequestMapping(value = "/auditmodel")
    public ModelAndView auditmodel(BigDecimal experId,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        TblNbsjAuditModelEntity model = this.tblNbsjAuditModelService.getByExperId(experId);
        if (null != model) {
            // model.setIMGUrl(IMAGEURL+model.getIMGUrl());
            mv.addObject("imageUrl", IMAGEURL + model.getIMGUrl());
        }
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
        TblNbsjAuditExperienceEntity experience = this.tblNbsjAuditExperienceService.getById(experId);
        mv.addObject("type", experience.getAuditExperienceType());
		mv.addObject("choiceSearch",choiceSearch); 
        mv.setViewName("nbsj/zyk/model");
        mv.addObject("experId", experId);
        mv.addObject("model", model);
        mv.addObject("view", request.getParameter("view"));
        return mv;
    }
    
    @Operation(summary="查询")
    @RequestMapping(value = "/auditmodelfind")
    public ModelAndView auditmodelfind(BigDecimal experId) {
        ModelAndView mv = new ModelAndView();
        TblNbsjAuditModelEntity model = this.tblNbsjAuditModelService.getByExperId(experId);
        if (null != model) {
            // model.setIMGUrl(IMAGEURL+model.getIMGUrl());
            mv.addObject("imageUrl", IMAGEURL + model.getIMGUrl());
        }
        mv.setViewName("nbsj/zyk/modelfind");
        mv.addObject("experId", experId);
        mv.addObject("model", model);
        return mv;
    }
    
    /**
     * 保存审计模型
     
    @Operation(summary="保存审计模型")
    @RequestMapping(value = "/auditmodel_save")
    public ModelAndView auditmodel_save(BigDecimal experId, MultipartFile modelFile,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String modelstep1 = request.getParameter("modelstep1");
        String modelstep2 = request.getParameter("modelstep2");
        String modelstep3 = request.getParameter("modelstep3");
        String modelstep4 = request.getParameter("modelstep4");
        String modelstep5 = request.getParameter("modelstep5");
        TblNbsjAuditExperienceEntity experience = this.tblNbsjAuditExperienceService.getById(experId);
        if (null != experience) {
            TblNbsjAuditModelEntity model = this.tblNbsjAuditModelService.getByExperId(experId);
            if (null != model) {
                model.setAuditExperience(experience);
                model.setCreateTime(model.getCreateTime());
                model.setUpdateTime(new Date());
                model.setModelstep1(modelstep1);
                model.setModelstep2(modelstep2);
                model.setModelstep3(modelstep3);
                model.setModelstep4(modelstep4);
                model.setModelstep5(modelstep5);
              //  model.setIMGUrl(modelUrl);
                this.tblNbsjAuditModelService.merge(model);
                mv.addObject("model", model);
            } else {
                TblNbsjAuditModelEntity auditModel = new TblNbsjAuditModelEntity();
                auditModel.setAuditExperience(experience);
                auditModel.setCreateTime(new Date());
               // auditModel.setIMGUrl(modelUrl);
                auditModel.setModelstep1(modelstep1);
                auditModel.setModelstep2(modelstep2);
                auditModel.setModelstep3(modelstep3);
                auditModel.setModelstep4(modelstep4);
                auditModel.setModelstep5(modelstep5);
                this.tblNbsjAuditModelService.save(auditModel);
                mv.addObject("model", auditModel);
            }
        }
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 		
        mv.setViewName("nbsj/zyk/model");
      //  mv.addObject("imageUrl", IMAGEURL + modelUrl);
        mv.addObject("experId", experId);
        return mv;
    }
    
   /* @RequestMapping(value = "/out_list")
    public ModelAndView outlist(BigDecimal experId, Integer pageNmber,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        pageBean = this.tblNbsjAuditExperOutRulService.findByExperId(experId, pageNmber, pageBean.getPageSize());
        mv.setViewName("nbsj/zyk/outlist");
        mv.addObject("experId", experId);
        mv.addObject("pageBean", pageBean);
        TblNbsjAuditExperienceEntity experience = this.tblNbsjAuditExperienceService.getById(experId);
        mv.addObject("type", experience.getAuditExperienceType());
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
    @RequestMapping(value = "/out_find")
    public ModelAndView out_find(BigDecimal experId, Integer pageNmber) {
        ModelAndView mv = new ModelAndView();
        pageBean = this.tblNbsjAuditExperOutRulService.findByExperId(experId, pageNmber, pageBean.getPageSize());
        mv.setViewName("nbsj/zyk/outfind");
        mv.addObject("experId", experId);
        mv.addObject("pageBean", pageBean);
        return mv;
    }
    
    @Operation(summary="保存")
    @RequestMapping(value = "/out_save", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String outsave(BigDecimal experId, String id) {
        if (StringUtils.isNotBlank(id)) {
            String[] ids = id.split(",");
            TblNbsjAuditExperienceEntity experience = this.tblNbsjAuditExperienceService.getById(experId);
            if (null != experience) {
                for (String str : ids) {
                    TblNbsjAuditExperOutRulEntity nbsjAuditExperOutRul = this.tblNbsjAuditExperOutRulService.findByExperIdAndOutId(experId, new BigDecimal(str));
                    if (null == nbsjAuditExperOutRul) {
                        TblNbsjOuterruleEntity outerrule = this.nbsjouterruleService.findByid(str);
                        TblNbsjAuditExperOutRulEntity auditExperOutRul = new TblNbsjAuditExperOutRulEntity();
                        auditExperOutRul.setOuterrule(outerrule);
                        auditExperOutRul.setAuditExperience(experience);
                        this.tblNbsjAuditExperOutRulService.save(auditExperOutRul);
                    }
                }
                return JsonBean.success();
            }
        }
        return JsonBean.success();
    }
    
    @Operation(summary="删除")
    @RequestMapping(value = "/out_del", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String out_del(BigDecimal experId, String id) {
        if (StringUtils.isNotBlank(id)) {
            String[] ids = id.split(",");
            for (String str : ids) {
                this.tblNbsjAuditExperOutRulService.delete(new BigDecimal(str));
            }
            return JsonBean.success();
        }
        return JsonBean.error();
    }*/
    
}
