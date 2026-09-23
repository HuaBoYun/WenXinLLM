package com.huabo.audit.controller;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 整改跟踪
 */
public class NbsjRectifyController {
	
	/*@Resource
	public PageBean pageBean;
	
	@Resource
	public TblOrganizaService tblOrganizaService;
	
	@Resource
	public TblNbsjRefopmService tblNbsjRefopmService;
	
	@Autowired
	public TblNbsjReformSolutionService tblNbsjReformSolutionService;
	
	public ModelAndView mav = new ModelAndView();
	
	@Resource
	public TblNbsjProjectService tblnbsjProjectService;
	
	@Resource
	public UserService userService;
	
	@Resource
	public AttachmentService attachmentService;
	
	@Resource
	public TblNbsjSheetService tBlNbsjSheetService;
	
	@Resource
	public TblNbsjSheetReportService tblNbsjSheetReportService;
	
	@Resource
	public OrganizationService organizationService;
	
	@Resource
	public TblNbsjQuestionService tblNbsjQuestionService;
	
	@Resource
	public TblBugService tblBugService;
	
	@Resource
	public TblBugCriterionService tblBugCriterionService;
	
	
	/**
     * 整改分派---指派人员
     
    @Operation(summary="整改分派---指派人员")
    @RequestMapping(value = "/sjzg/indexs")
    public ModelAndView userIndex_zggz(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/nbsj/sjzg/user_index");
        String num = request.getParameter("type");
        mv.addObject("type", num);
        return mv;
    }
    /**
     * 整改分派---指派人员--左边
     
    @Operation(summary="整改分派---指派人员--左边")
    @RequestMapping(value = "/sjzg/lefts")
    public ModelAndView userLeft_zggz(HttpServletRequest request) {
        TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 当前用户的机构
        ModelAndView mv = new ModelAndView("/nbsj/sjzg/user_left");
        mv.addObject("orgid", attribute1.getOrgid());
        mv.addObject("targetFrame", "mainFramex");
        return mv;
    }
    /**
     * 集团版 --数据控制
     *
     
    @Operation(summary="集团版 --数据控制")
    @RequestMapping(value = "/findOrganizationByTreeAlls", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String findOrganizationByTrees(BigDecimal nodeId, String type, BigDecimal orgId, HttpServletRequest request) {
        String json = "";
        if (null == nodeId) {
            nodeId = orgId;
            if (null == orgId) {
                TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
                nodeId = tblStaff.getTblOrganization().getOrgid();
            }
        }
        if (StringUtils.isNotBlank(type)) {
            List<Tree> list = this.tblOrganizaService.getTree(nodeId);
            for (Tree tree : list) {
                if (!tree.getIsParent()) {
                    tree.setTarget("mainFramex");
                    tree.setUrl("/nbsj/sjzg/list?pid=" + tree.getId());
                }
            }
            json = JSONObject.toJSONString(list);
        } else {
            Tree tree = this.tblOrganizaService.getTreeRoot(nodeId);
            if (!tree.getIsParent()) {
                tree.setTarget("mainFramex");
                tree.setUrl("/nbsj/sjzg/list?pid=" + tree.getId());
            }
            json = JSONObject.toJSONString(tree);
        }
        return json;
    }
    /**
     * 整改分派---指派人员--列表
     *
     * @param request
     * @return
     
    @SuppressWarnings("rawtypes")
    @Operation(summary="整改分派---指派人员--列表")
    @RequestMapping(value = "/sjzg/list")
    public ModelAndView userListss_zggz(HttpServletRequest request) {
        ListPager pager = (ListPager) request.getSession().getAttribute("userPager");
        String page = request.getParameter("page");
        String pid = request.getParameter("pid");
        if (page == null) {
            UserService service = (UserService) SpringContextHolder.getBean("UserService");
            TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
            List list = null;
            if (pid != null) {
                list = service.findByAll(pid);// findAllByOrgId(pid,
                // orgService.getSubOrgs(pid));
            } else {
                list = service.findByAllORGID(attribute.getOrgid().toString());
            }
            pager = new ListPager(list);
            request.getSession().setAttribute("userPager", pager);
        } else {
            pager.navigate(page);
        }
        ModelAndView mv = new ModelAndView("/nbsj/sjzg/user_list");
        mv.addObject("pager", pager);
        return mv;
    }
    /**
     * 整改分派---指派人员-判断审计发现是否完成
     *
     * @param request
     * @return
     
    @Operation(summary="整改分派---指派人员-判断审计发现是否完成")
    @RequestMapping(value = "/sjzg/zgfp_fsStatus", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String zgfp_fsStatus(HttpServletRequest request) {
        String questionid = request.getParameter("questionid");
//        logger.info("整改分派---指派人员-判断审计发现是否完成");
        if (questionid != null && questionid.length() > 0) {
            TblNbsjRefopmEntity nbsjRefopm = tblNbsjRefopmService.getByQuestid(questionid);
            if (nbsjRefopm != null) {
                if (nbsjRefopm.getStatus() != 2) {
                    return JsonBean.success();
                } else {
                    return JsonBean.error("该审计发现已整改完毕");
                }
            } else {
                return JsonBean.success();
            }
        } else {
            return JsonBean.error("请选择要整改的问题");
        }
    }
    
    /**
     * 整改跟踪
     *
     * @return
     
    @Operation(summary="整改跟踪")
    @RequestMapping(value = "/sjzg/index")
    public ModelAndView index_question_implZggz(Integer pageNumber,String solutioncode,HttpServletRequest request) {
    	//用于区别整改跟踪和整改查询,后面的sql语句中会用到
		Integer tagType = 1;
        ModelAndView mv = new ModelAndView();
        TblStaff attribute = (TblStaff) request.getSession().getAttribute("longUser");
        TblOrganization attr= (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        pageBean = tblNbsjReformSolutionService.findAlls(attr.getOrgid().toString(), solutioncode, pageNumber, pageBean.getPageSize(),attribute.getStaffid().toString(),tagType);
		mv.addObject("pageBean",pageBean);
		mv.addObject("view", request.getParameter("view"));
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
		mv.setViewName("/nbsj/sjzg/nbsjzggz");
        return mv;
    }
    
    


	
	
	
    
    
    
    /**
     * 整改跟踪-左侧tree
     *
     * @param request
     * @param orgid
     * @return
     
    @Operation(summary="整改跟踪-左侧tree")
    @RequestMapping(value = "/sjzg/lefttree")
    public ModelAndView zggz_ffUserTree(HttpServletRequest request, String orgid) {
        UserService service = (UserService) SpringContextHolder.getBean("UserService");
        String treeName = request.getParameter("treeName");
        TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        if (StringUtils.isEmpty(orgid)) {
            TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
            orgid = user.getTblOrganization().getOrgid().toString();
            treeName = organization.getOrgname();
        }
        TblOrganization org = tblOrganizaService.get(new BigDecimal(orgid));
        String url = request.getParameter("url");
        if (url != null && !"".equals(url)) {
            request.getSession().setAttribute("url", url);
        } else {
            url = (String) request.getSession().getAttribute("url");
        }
        String tree = service.getUserTree(url, org);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjzg/zuser_tree");
        mv.addObject("tree", tree);
        mv.addObject("treeName", treeName);
        return mv;
    }
    /**
     * 整改跟踪-列表
     *
     * @param request
     * @return
     
    @Operation(summary="整改跟踪-列表")
    @RequestMapping(value = "/sjzg/review_tracking_list")
    public ModelAndView review_tracking_listZggz(HttpServletRequest request) {
        TblNbsjProjectEntity project = this.tblnbsjProjectService.getSelectProject();
        if (project != null) {
            String code = request.getParameter("code");
            String userid = request.getParameter("person1");
            String uid = request.getParameter("uid");
            Integer pageNumber = 1;
            String number = request.getParameter("pageNumber");
            if (number != null && number.length() > 0) {
                pageNumber = Integer.parseInt(number);
            }
            if (uid == null || uid.equals("")) {
                TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
                uid = user.getStaffid().toString();
            }
            pageBean = tblNbsjRefopmService.findQuestionZGGZ(project.getProjectId().toString(), code, userid, uid, pageNumber, pageBean.getPageSize());
            mav = new ModelAndView("nbsj/sjzg/review_tracking_list");
            mav.addObject("pageBean", pageBean);
            mav.addObject("project", project);
            if (userid != null && userid.length() > 0) {
                TblStaff user = userService.findById(userid);
                mav.addObject("user", user);
            }
            mav.addObject("code", code);
        } else {
            setErrorJsp("001",request);
            mav.setViewName("nbsj/main41");
        }
      //查询框代码
        String choiceSearch = request.getParameter("choiceSearch");
        if(choiceSearch == null || "".equals(choiceSearch)) {
        	choiceSearch = "hide";
        }
        mav.addObject("choiceSearch",choiceSearch); 
        return mav;
    }
    
    /**
     * 整改落实-左侧tree
     *
     * @param request
     * @param orgid
     * @return
     
    @Operation(summary="整改落实-左侧tree")
    @RequestMapping(value = "/sjzg/user/tree")
    public ModelAndView zggzUserTree(HttpServletRequest request, String orgid) {
        UserService service = (UserService) SpringContextHolder.getBean("UserService");
        HashMap map = tblNbsjRefopmService.getUserMap();
        String treeName = request.getParameter("treeName");
        TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        if (StringUtils.isEmpty(orgid)) {
            TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
            orgid = user.getTblOrganization().getOrgid().toString();
            treeName = organization.getOrgname();
        }
        TblOrganization org = tblOrganizaService.get(new BigDecimal(orgid));
        String url = request.getParameter("url");
        if (url != null && !"".equals(url)) {
            request.getSession().setAttribute("url", url);
        } else {
            url = (String) request.getSession().getAttribute("url");
        }
        String tree = service.getUserTreeWithMap(url, map, org);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjzg/user_tree");
        mv.addObject("tree", tree);
        mv.addObject("treeName", treeName);
        return mv;
    }
    /**
     * 审计整改-整改落实-列表
     *
     * @param request
     * @return
     
    @Operation(summary="审计整改-整改落实-列表")
    @RequestMapping(value = "/sjzg/question_rectify_impl_detail")
    public ModelAndView question_rectify_impl_detailZggz(HttpServletRequest request) {
        String rid = request.getParameter("rid");
        String stid = request.getParameter("uid");
        UserService uservice = (UserService) SpringContextHolder.getBean("UserService");
        TblNbsjRefopmEntity r = null;
        TblStaff sa = null;
        TblStaff st = (TblStaff) request.getSession().getAttribute("longUser");
        if (stid != null && !"".equals(stid)) {
            sa = uservice.findById(stid);
        }
        if (rid != null && !rid.equals("")) {
            r = tblNbsjRefopmService.getByid(rid);
        } else {
            if (sa != null) {
                r = tblNbsjRefopmService.findByUserid(sa.getStaffid().toString());
            } else {
                r = tblNbsjRefopmService.findByUserid(st.getStaffid().toString());
            }
        }
        ModelAndView mv = new ModelAndView();
        if (r != null && r.getStatus() != 2 && st.getStaffid().toString().equals(r.getNbsuser().getStaffid().toString())) {
            mv.setViewName("nbsj/sjzg/question_rectify_impl");
        } else {
            mv.setViewName("nbsj/sjzg/question_rectify_impl_detail");
        }
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
        if (r != null) {
            if (r.getReformdeadline() != null) {
                mv.addObject("date", sdfs.format(r.getReformdeadline()));
            }
            request.getSession().setAttribute("zglsList", r);
            mv.addObject("reform", r);
        }
        return mv;
    }
    /**
     * 整改落实
     *
     * @param request
     * @return
     
    @Operation(summary="整改落实")
    @RequestMapping(value = "/sjzg/ls")
    public ModelAndView zggzLs(HttpServletRequest request) {
        String rid = request.getParameter("rid");
        String measure = request.getParameter("measure");
        String carryout = request.getParameter("carryout");
        String handling = request.getParameter("handling");
        String deadline = request.getParameter("deadline");
        String result = request.getParameter("result");
        TblNbsjRefopmEntity r = null;
        if (rid != null && !rid.equals("")) {
            r = tblNbsjRefopmService.getByid(rid);
            r.setReformmeasure(measure);
            r.setReformcarryout(carryout);
            r.setHandling(handling);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if (deadline != null && !"".equals(deadline)) {
                    r.setReformdeadline(sdf.parse(deadline));
                } else {
                    r.setReformdeadline(new Date());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            r.setReformtime(new Date());
            r.setReformresult(result);
            r.setStatus(TblNbsjRefopmEntity.STATUSYES);
            tblNbsjRefopmService.update(r);
        }
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjzg/question_rectify_impl_detail");
        if (r != null) {
            mv.addObject("date", sdfs.format(r.getReformdeadline()));
        }
        mv.addObject("reform", r);
        return mv;
    }
    
    /**
     * @Title: del_solutionFJ 
    * @Description: 审计--整改方案--删除附件
    * @param request
    * @param solutionid	整改方案ID
    * @return ModelAndView
    * @throws
     
    @Operation(summary="zgfa-删除")
    @RequestMapping(value = "/zgfa/del_solutionFJ")
    public ModelAndView del_solutionFJ(HttpServletRequest request, String solutionid) {
    	ModelAndView mv = new ModelAndView();
    	String attid = request.getParameter("attid");
    	TblAttachmentEntity attachment = new TblAttachmentEntity();
    	if (StringUtils.isNotEmpty(attid)) {
    		attachment = attachmentService.findById(attid);
    	}
    	if (StringUtils.isNotBlank(solutionid)) {
    		TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
	        solution.getTblAttachments().remove(attachment);
	        tblNbsjReformSolutionService.saveOrUpdate(solution);//删除中间表
    	} else {
    		attachmentService.delete(attachment);
    	}
    	//查询框代码
    	String choiceSearch = request.getParameter("choiceSearch");
    	if(choiceSearch == null || "".equals(choiceSearch)) {
    		choiceSearch = "hide";
    	}
    	mv.setViewName("redirect:/nbsj/zgfa/to_solution_modify?selectedid=" + solutionid+"&choiceSearch="+choiceSearch);
        return mv;
    }
    
    
    
    @Operation(summary="sjss-新增")
    @RequestMapping(value = "/sjss/add_enclosure")
    public ModelAndView add_enclosure(HttpServletRequest request, BigDecimal sheetId, MultipartFile file) {
        TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
        //int index = file.getOriginalFilename().lastIndexOf(".");//截取文件名
        String fileName = file.getOriginalFilename();//.substring(0, index);//文件名
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String oldname = fileName.substring(0,fileName.lastIndexOf("."));
		String newname=fileName.replace(oldname, timeInMillis+"dggl");
		long size = file.getSize();
		try {
			boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
			if(flag){
//				 logger.info("上传成功");
			}else{
//				 logger.info("上传失败");
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
        TblAttachmentEntity a = new TblAttachmentEntity();
        a.setAttname(fileName);
        a.setAttpath(newname);
        a.setAttsize(new BigDecimal(size / 1024));
        a.setUploader(user.getUsername());
        a.setUploadtime(new Date());
        attachmentService.add(a);
        TblNbsjSheetEntity sheet = tBlNbsjSheetService.getById(sheetId);
        sheet.getTblAttachments().add(a);
        tBlNbsjSheetService.updateTblNbsjSheet(sheet);
        return pmp_audit_project_digao(request, sheetId, null);
    }
    
    
    /**
     * @Title: add_solutionFJ 
    * @Description: 审计---整改方案--上传附件
    * @param request
    * @param solutionid	整改方案id
    * @param file
    * @return ModelAndView
    * @throws
     
    @Operation(summary="zgfa-新增")
    @RequestMapping(value = "/zgfa/add_solutionFJ")
    public ModelAndView add_solutionFJ(HttpServletRequest request, String solutionid, MultipartFile file) {
    	ModelAndView mv = new ModelAndView();
        TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
       // String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/zgfa/");
        String fileName = file.getOriginalFilename();//文件名
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String oldname = fileName.substring(0,fileName.lastIndexOf("."));
		String newname=fileName.replace(oldname, timeInMillis+"sjzgfa");
		long size = file.getSize();
		try {
			boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
			if(flag){
//				 logger.info("上传成功");
			}else{
//				 logger.info("上传失败");
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
        TblAttachmentEntity a = new TblAttachmentEntity();
        a.setAttname(fileName);
        a.setAttpath(newname);
        a.setAttsize(new BigDecimal(size / 1024));
        a.setUploader(user.getUsername());
        a.setUploadtime(new Date());
        attachmentService.add(a);
        TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
        solution.getTblAttachments().add(a);
        tblNbsjReformSolutionService.saveOrUpdate(solution);
      //查询框代码
        String choiceSearch = request.getParameter("choiceSearch");
        if(choiceSearch == null || "".equals(choiceSearch)) {
        	choiceSearch = "hide";
        }
        mv.setViewName("redirect:/nbsj/zgfa/to_solution_modify?selectedid=" + solutionid+"&choiceSearch="+choiceSearch);
        return mv;
    }
    
    /**
     * @Title: solution_codeIsExist 
    * @Description: “整改方案”,判断编号是否重复
    * @param request
    * @param solutioncode	整改方案编号
    * @return String
    * @throws
     
    @Operation(summary="zgfa-solution_codeIsExist")
    @RequestMapping(value = "/zgfa/solution_codeIsExist",produces = "application/json;charset=utf-8")
    public @ResponseBody String solution_codeIsExist(HttpServletRequest request,String solutioncode) {
    	String orgid = request.getParameter("orgid");
    	List<TblNbsjReformSolutionEntity> list = tblNbsjReformSolutionService.solutionIsExist(solutioncode,orgid);
	     if (list != null && list.size() > 0) {
	         return list.size() + "";
	     }
	     return "0";
    	
    }
    
    /**
     * 整改方案-主页
     * @return
     
    @Operation(summary="整改方案-主页")
    @RequestMapping(value = "/zggz/zggz_index")
	public ModelAndView index_question_rectify_implZggzs(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
		mv.addObject("orgid", tblStaff.getTblOrganization().getOrgid());
		mv.addObject("orgtype", tblStaff.getTblOrganization().getOrgtype());
		mv.setViewName("nbsj/sjzg/zggz_index");
		return mv;
	}
    
    
    @Operation(summary="zggz-userleft")
    @RequestMapping(value = "/zggz/userleft")
	public ModelAndView userLeft_zggz_dd(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("nbsj/sjzg/zggz_left");
		TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
		mv.addObject("orgid", organization.getOrgid());
		mv.addObject("treeName", organization.getOrgname());
		mv.addObject("targetFrame", "mainFramex");
		return mv;
	}
    
    
    /**
     * 整改方案
     * @param orgId
     * @param solutioncode
     * @param pageNumber
     * @param request
     * @return
     
    @Operation(summary="整改方案")
    @RequestMapping(value = "/zgfa/solutionmgmt")
    public ModelAndView solutionMgmt(BigDecimal orgId, String solutioncode, Integer pageNumber,TblNbsjReformSolutionEntity solition,HttpServletRequest request) {
        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");//选则的机构
        TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgName");//当前用户的机构
        TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
        String pmid=request.getParameter("pmid");
        if(pmid!=null && pmid.trim().length()>0){
        	TblStaff user = userService.findById(pmid);
        	solition.setTblStaff(user);
        }
        if(attribute.getOrgid().toString().equals(attribute1.getOrgid().toString())){
            if (orgId == null) {
                orgId = tblStaff.getTblOrganization().getOrgid();
            }
                pageBean = tblNbsjReformSolutionService.findAll(orgId.toString(), solition, pageNumber, pageBean.getPageSize());
        }else{
            if (orgId == null) {
                orgId = attribute.getOrgid();
            }
            pageBean = tblNbsjReformSolutionService.findAll(orgId.toString(), solition, pageNumber, pageBean.getPageSize());
        }
        ModelAndView mv = new ModelAndView("nbsj/sjzg/solutionmgmt");
        mv.addObject("orgId", orgId);
        mv.addObject("pageBean", pageBean);
        mv.addObject("solition", solition);
      //查询框代码
        String choiceSearch = request.getParameter("choiceSearch");
        if(choiceSearch == null || "".equals(choiceSearch)) {
        	choiceSearch = "hide";
        }
        mv.addObject("choiceSearch",choiceSearch); 	
        return mv;

    }




	
	
	/**
	 * 整改方案-添加页面
	 * @param orgId
	 * @param request
	 * @return
	 
	@Operation(summary="整改方案-添加页面")
	@RequestMapping(value = "/zgfa/to_solution_add")
	public ModelAndView toSolutionAdd(BigDecimal orgId, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
		mv.setViewName("nbsj/sjzg/solution_add");
		mv.addObject("orgId", orgId);
		mv.addObject("tblStaff", tblStaff);
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
		return mv;
	}
    
	
	/**
	 * 整改方案-添加保存
	 * @param request
	 * @return
	 * @throws ParseException
	 
	@Operation(summary="整改方案-添加保存")
	@RequestMapping(value = "/zgfa/solution_add")
	public ModelAndView solutionAdd(HttpServletRequest request) throws ParseException {
		String solutioncode = request.getParameter("solutioncode");
		String solutionname = request.getParameter("solutionname");
		String orgid = request.getParameter("orgid");
		String enddate = request.getParameter("enddate");
		String memo = request.getParameter("memo");
		TblNbsjReformSolutionEntity solution =new TblNbsjReformSolutionEntity();
		solution.setSolutioncode(solutioncode);
		solution.setSolutionname(solutionname);
		solution.setRunstatus(0);
		Date date = new Date();
		solution.setCreatedate(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		solution.setEnddate(sdf.parse(enddate));
		solution.setMemo(memo);
		TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
		solution.setTblStaff(tblStaff);
		TblOrganization organization = this.tblOrganizaService.get(tblStaff.getTblOrganization().getOrgid());
		if (null != organization) {
			solution.setOrganization(organization);
		}
		if(orgid!=null){
			TblOrganization company = organizationService.findById(orgid);
			solution.setReformCompany(company);
		}
        //保存附件
        String attids = request.getParameter("attids");
        if (StringUtils.isNotBlank(attids)){
        	String[] ids = attids.split(",");
        	for (int i = 0; i < ids.length; i++) {
        		TblAttachmentEntity att = attachmentService.findById(ids[i].trim());
        		solution.getTblAttachments().add(att);
        	}
        }
		tblNbsjReformSolutionService.saveOrUpdate(solution);
		ModelAndView mv = new ModelAndView();
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
		mv.setViewName("redirect:/nbsj/zgfa/to_solution_modify?selectedid=" + solution.getSolutionid()+"&choiceSearch="+choiceSearch);
		return mv;
	}
	
	
	/**
	 * 整改方案-修改页面
	 * @param request
	 * @return
	 
	@Operation(summary="整改方案-修改页面")
	@RequestMapping(value = "/zgfa/to_solution_modify")
	public ModelAndView toSolutionModify(HttpServletRequest request) {
		String id = request.getParameter("selectedid");
		TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(id);
		ModelAndView mv = new ModelAndView();
		if (solution != null) {
			request.setAttribute("solution", solution);
			mv.addObject("orgId", solution.getOrganization().getOrgid());
			mv.setViewName("nbsj/sjzg/solution_modify");
		} else
			mv.setViewName("nbsj/sjzg/solutionmgmt");
		
		List<TblAttachmentEntity> list = attachmentService.findAllByTblNbsjReformSolution(id);//根据整改方案ID获取所有附件
		mv.addObject("fj", list);
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
		return mv;
	}
	
	

	/**
	 * 整改方案-修改保存
	 * @param request
	 * @return
	 * @throws ParseException
	 
	@Operation(summary="整改方案-修改保存")
	@RequestMapping(value = "/zgfa/solution_modify")
	public ModelAndView solutionModify(HttpServletRequest request) throws ParseException {
		String id = request.getParameter("solutionid");
		String solutioncode = request.getParameter("solutioncode");
		String solutionname = request.getParameter("solutionname");
		String memo = request.getParameter("memo");
		String orgid = request.getParameter("orgid");
		String enddate = request.getParameter("enddate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(id);
		solution.setSolutioncode(solutioncode);
		try {
			solution.setEnddate(sdf.parse(enddate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		solution.setSolutionname(solutionname);
		if(orgid!=null && !solution.getReformCompany().getOrgid().toString().equals(orgid)){
			TblOrganization company = organizationService.findById(orgid);
			List<TblNbsjRefopmEntity> list = tblNbsjRefopmService.findTblReformBySoulid(solution.getSolutionid().toString());
			if(list!=null && list.size()>0){
				for (TblNbsjRefopmEntity tblReform : list) {
					tblNbsjRefopmService.delete(tblReform);
				}
			}
			solution.setReformCompany(company);
		}
		solution.setMemo(memo);
		ModelAndView mv = new ModelAndView();
        //保存附件
        String attids = request.getParameter("attids");
        if (StringUtils.isNotBlank(attids)){
        	String[] ids = attids.split(",");
        	for (int i = 0; i < ids.length; i++) {
        		TblAttachmentEntity att = attachmentService.findById(ids[i].trim());
        		solution.getTblAttachments().add(att);
        	}
        }
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
		tblNbsjReformSolutionService.saveOrUpdate(solution);
		mv.setViewName("redirect:/nbsj/zgfa/to_solution_modify?selectedid=" + id);
		return mv;
	}
	
	
	
	@Operation(summary="zgfa-列表查询")
	@RequestMapping(value = "/zgfa/question_allocate_list")
	public ModelAndView zgfa_question_allocate_listZggz(HttpServletRequest request) {
		TblProblemEntity pro = new TblProblemEntity();
		String solutionid=request.getParameter("solutionid");
		String orgid=request.getParameter("orgid");
		String probnum = request.getParameter("probnum");
		String probname = request.getParameter("probname");
		String riskname = request.getParameter("riskname");
		String belongstoText = request.getParameter("belongstoText");
		String person = request.getParameter("person");
		String quest = request.getParameter("quest");
		if (probnum != null && probnum.length() > 0) {
			pro.setProbnumber(probnum);
		}
		if (probname != null && probname.length() > 0) {
			pro.setProbname(probname);
		}
		if (riskname != null && riskname.length() > 0) {
			pro.setRiskbelongsto(riskname);
		}
		if (belongstoText != null && belongstoText.length() > 0) {
			pro.setProborgs(belongstoText);
		}
		if (person != null && person.length() > 0) {
			pro.setPersonincharge(person);
		}
		if (quest != null && quest.length() > 0) {
			pro.setProbfrom(quest);
		}
		TblProblemService problemService = SpringContextHolder.getBean("TblProblemService");
		String number = request.getParameter("pageNumber");
		Integer pageNumber = 1;
		if (number != null && number.length() > 0) {
			pageNumber = Integer.parseInt(number);
		}
		//TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
		TblOrganization organization = organizationService.findById(orgid);
		mav = new ModelAndView("nbsj/sjzg/zgfa_question_allocate_list");
		TblNbsjProjectEntity selectProject = tblnbsjProjectService.getSelectProject();
		pageBean = problemService.getAllReformNbsj(pro, pageNumber, pageBean.getPageSize(), orgid, organization.getOrgtype().toString(),selectProject.getProjectId().toString(),solutionid);
		mav.addObject("pageBean", pageBean);
		mav.addObject("pro", pro);
		mav.addObject("orgid", orgid);
		mav.addObject("solutionid", solutionid);
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mav.addObject("choiceSearch",choiceSearch); 
		return mav;
	}
	
	
	
	
	/**
	 * 整改方案-保存整改内容
	 * @param request
	 * @return
	 
	@Operation(summary="整改方案-保存整改内容")
	@RequestMapping(value = "/zgfa/assign",produces = "application/json; charset=utf-8")
	public @ResponseBody String zgfaAssign(HttpServletRequest request) {
		String solutionid = request.getParameter("solutionid");
		String sId = request.getParameter("selectedId");
		TblStaff attribute = (TblStaff) request.getSession().getAttribute("longUser");
		TblNbsjBugService bugService = SpringContextHolder.getBean("TblNbsjBugService");
		if (sId != null && !sId.equals("") && solutionid!=null) {
			try {
				String[] list=sId.split(",");
				for (int i = 0; i < list.length; i++) {
					TblNbsjRefopmEntity reform = new TblNbsjRefopmEntity();
					if (list[i].indexOf("__b") >= 0) {
						TblNbsjBugEntity b = bugService.findById(new BigDecimal(list[i].replace("__b", "")));
						if(b!=null){
							TblNbsjRefopmEntity refopms = tblNbsjRefopmService.findTblReformByBugIdAndSoulid(b.getBugid().toString(),solutionid);
							if(refopms!=null){
								continue;
							}
						}
						reform.setTblBug(b);
					} else {
						TblNbsjQuestionEntity question = tblNbsjQuestionService.getById(new BigDecimal(list[i].replace("__p", "")));
						if(question!=null){
							TblNbsjRefopmEntity refopms = tblNbsjRefopmService.findTblReformBySheetIdAndSoulid(question.getQuestionId().toString(),solutionid);
							if(refopms!=null){
								continue;
							}
						}
						TblNbsjProjectEntity selectProject = tblnbsjProjectService.getSelectProject();
						reform.setTblnbsjProject(selectProject);
						reform.setNbsjQuestion(question);

					}
					TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
					reform.setStatus(TblReformEntity.ZGZ);
					reform.setTblCeaters(attribute);
					reform.setTblNbsjReformSolution(solution);
					tblNbsjRefopmService.save(reform);
				}
				return JsonBean.success();
			} catch (Exception e) {
				e.printStackTrace();
				return JsonBean.error("保存失败");
			}
		}else{
			return JsonBean.error("请选择");
		}
	}
	
	/**
	 * 删除整改内容
	 * @param request
	 * @return
	 
	@Operation(summary="删除整改内容")
	@RequestMapping(value = "/zgfa/deleteSolution",produces = "application/json; charset=utf-8")
	public @ResponseBody String deleteSolution(HttpServletRequest request) {
		String sId = request.getParameter("selectedId");
		
		if(sId!=null && sId.trim().length()>0){
			TblNbsjRefopmEntity refopm = tblNbsjRefopmService.getByid(sId);
			tblNbsjRefopmService.delete(refopm);
			return JsonBean.success();
		}else{
			return JsonBean.error("请选择");
		}
	}
	
	
	/**
	 * 整改方案-详细内容
	 * @param request
	 * @return
	 
	@Operation(summary="整改方案-详细内容")
	@RequestMapping(value = "/zgfa/to_solution_detail")
	public ModelAndView to_solution_detail(HttpServletRequest request) {
		String id = request.getParameter("selectedid");
		TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(id);
		ModelAndView mv = new ModelAndView();
		if (solution != null) {
			request.setAttribute("solution", solution);
			mv.addObject("orgId", solution.getOrganization().getOrgid());
			mv.setViewName("nbsj/sjzg/solution_detail");
		} else
			mv.setViewName("nbsj/sjzg/solutionmgmt");
		
		List<TblAttachmentEntity> list = attachmentService.findAllByTblNbsjReformSolution(id);//根据整改方案ID获取所有附件
		mv.addObject("fj", list);
		return mv;
	}
	
	
	/**
	 * 整改方案-下发主页
	 * @return
	 
	@Operation(summary="整改方案-下发主页")
	@RequestMapping(value = "/zgfa/zgfaxf_index")
	public ModelAndView zgfaxf_index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		String solutionid=request.getParameter("solutionid");
		TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
		mv.addObject("orgid", solution.getReformCompany().getOrgid());
		mv.addObject("solutionid",solutionid);
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
		mv.setViewName("nbsj/sjzg/zgfa_index");
		return mv;
	}
	
	
	/**
	 * 整改方案-下发左侧tree
	 * @param request
	 * @return
	 
	@Operation(summary="整改方案-下发左侧tree")
	@RequestMapping(value = "/zgfa/zgfa_userleft")
	public ModelAndView zgfa_userleft(HttpServletRequest request) {
		String orgid=request.getParameter("orgid");
		String solutionid=request.getParameter("solutionid");
		ModelAndView mv = new ModelAndView("/nbsj/sjzg/zgfa_left");
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
		mv.addObject("orgid", orgid);
		mv.addObject("solutionid", solutionid);
		mv.addObject("targetFrame", "mainFramex");
		return mv;
	}
	
	
	
	@Operation(summary="zgfa-列表查询")
	@RequestMapping(value = "/zgfa/zgfa_userList")
	public ModelAndView zgfa_userList(HttpServletRequest request) {
		String orgid = request.getParameter("orgid");
		TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
		if (orgid != null) {
			attribute=organizationService.findById(orgid);
		} 
		Integer pageNumber = 1;
		String number = request.getParameter("pageNumber");
		if (number != null && number.length() > 0) {
			pageNumber = Integer.parseInt(number);
		}
		pageBean=userService.findAllPageBeanPid(pageNumber, pageBean.getPageSize(), attribute);
		ModelAndView mv = new ModelAndView("/nbsj/sjzg/zgfa_user_list");
		String solutionid=request.getParameter("solutionid");
		mv.addObject("pageBean", pageBean);
        //查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
		mv.addObject("solutionid", solutionid);
		mv.addObject("orgid", orgid);
		return mv;
	}
	
	
	
	
	/**
	 * 整改方案-下发至用户
	 * @param request
	 * @return
	 
	@Operation(summary="整改方案-下发至用户")
	@RequestMapping(value = "/zgfa/xf_user",produces = "application/json; charset=utf-8")
	public @ResponseBody String xf_user(HttpServletRequest request) {
		String solutionid = request.getParameter("solutionid");
		String sId = request.getParameter("selectedId");
		if (sId != null && !sId.equals("") && solutionid!=null) {
			try {
				TblStaff user = userService.findById(sId);
				TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
				if(solution!=null && user!=null){
					solution.setReformUser(user);
					tblNbsjReformSolutionService.saveOrUpdate(solution);
					return JsonBean.success();
				}else{
					return JsonBean.error("下发失败");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return JsonBean.error("下发失败");
			}
		}else{
			return JsonBean.error("请选择");
		}
	}
	
	
	/**
	 * 整改方案-启动整改方案
	 * @param request
	 * @return
	 
	@Operation(summary="整改方案-启动整改方案")
	@RequestMapping(value = "/zgfa/start_solution",produces = "application/json; charset=utf-8")
	public @ResponseBody String start_solution(HttpServletRequest request) {
		String solutionid = request.getParameter("solutionid");
		if (solutionid!=null && solutionid.trim().length()>0) {
			try {
				TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
				if(solution!=null && solution.getRunstatus()<1){
					solution.setRunstatus(1);
					tblNbsjReformSolutionService.saveOrUpdate(solution);
					
					
					//启动方案的同时，下发邮件提醒整改负责人
					/*String[] copys=JDBCPropertiesUtils.zgfsemail.split(",");
					Product baicai=new Product(); 
					baicai.setZgcode(solution.getSolutioncode());    
					baicai.setType("1");
					baicai.setUserName(solution.getReformUser().getRealname());
					String path = request.getSession().getServletContext().getRealPath("/");
					baicai.setFilepath(path);
					baicai.setEmail(solution.getReformUser().getEmail());
					baicai.setPrice(1.5f);
					baicai.setCopys(copys);
					observer.update(baicai, 0.5f);
					
					return JsonBean.success();
				}else{
					if(solution!=null && solution.getRunstatus()==1){
						return JsonBean.error("方案已启动");
					}if(solution!=null && solution.getRunstatus()==3){
						return JsonBean.error("方案已完成");
					}else{
						return JsonBean.error("启动失败");
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return JsonBean.error("启动失败");
			}
		}else{
			return JsonBean.error("请选择");
		}
	}
	
	
	/**
	 * 整改方案-关闭整改方案
	 * @param request
	 * @return
	 
	@Operation(summary="整改方案-关闭整改方案")
	@RequestMapping(value = "/zgfa/close_solution",produces = "application/json; charset=utf-8")
	public @ResponseBody String close_solution(HttpServletRequest request) {
		String solutionid = request.getParameter("solutionid");
		if (solutionid!=null && solutionid.trim().length()>0) {
			try {
				TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
				if(solution!=null && (solution.getRunstatus()<3 || solution.getRunstatus()==4)){
					solution.setRunstatus(3);
					tblNbsjReformSolutionService.saveOrUpdate(solution);
					return JsonBean.success();
				}else{
					if(solution!=null && solution.getRunstatus()==3){
						return JsonBean.error("方案已关闭");
					}else{
						return JsonBean.error("关闭失败");
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return JsonBean.error("关闭失败");
			}
		}else{
			return JsonBean.error("请选择");
		}
	}
	/**
	 * 整改方案-删除方案
	 * @param request
	 * @return
	 
	@Operation(summary="整改方案-删除方案")
	@RequestMapping(value = "/zgfa/delete_Solution",produces = "application/json; charset=utf-8")
	public @ResponseBody String delete_Solution(HttpServletRequest request) {
		String sId = request.getParameter("selectedid");
		
		if(sId!=null && sId.trim().length()>0){
			TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(sId);
			if(solution!=null && solution.getRunstatus()==0){
				tblNbsjReformSolutionService.delete(solution);
				return JsonBean.success();
			}else{
				if(solution!=null && solution.getRunstatus()==3){
					return JsonBean.error("方案已结束");
				}else{
					return JsonBean.error("方案整改中");
				}
			}
		}else{
			return JsonBean.error("请选择");
		}
	}
	
	  /**
     * 整改分派-方案
     *
     * @param request
     * @return
     
    @Operation(summary="整改分派-方案")
    @RequestMapping(value = "/sjzg/question_allocate_list")
    public ModelAndView question_allocate_listZggz(BigDecimal orgId, String solutioncode, Integer pageNumber,HttpServletRequest request) {
    	TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
		pageBean = tblNbsjReformSolutionService.findAllByUserid(tblStaff.getStaffid().toString(), solutioncode, pageNumber, pageBean.getPageSize());
		ModelAndView mv = new ModelAndView("nbsj/sjzg/zgfp_solutionmgmt");
		mv.addObject("orgId", orgId);
		mv.addObject("pageBean", pageBean);
		mv.addObject("view", request.getParameter("view"));
		mv.addObject("solutioncode", solutioncode);
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch);
		return mv;
    }
    
    
    /**
	 * 整改分派-分派任务-主页
	 * @return
	 
	@Operation(summary="整改分派-分派任务-主页")
	@RequestMapping(value = "/zgfp/zgfp_index")
	public ModelAndView zgfp_index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		String solutionid=request.getParameter("solutionid");
		TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
		mv.addObject("orgid", solution.getReformCompany().getOrgid());
		mv.addObject("solutionid",solutionid);
		mv.setViewName("nbsj/sjzg/zgfp_index");
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch);
		return mv;
	}
	
	
	/**
	 * 整改分派-左侧tree
	 * @param request
	 * @return
	 
	@Operation(summary="整改分派-左侧tree")
	@RequestMapping(value = "/zgfp/zgfp_userleft")
	public ModelAndView zgfp_userleft(HttpServletRequest request) {
		String orgid=request.getParameter("orgid");
		String solutionid=request.getParameter("solutionid");
		ModelAndView mv = new ModelAndView("/nbsj/sjzg/zgfp_left");
		mv.addObject("orgid", orgid);
		mv.addObject("solutionid", solutionid);
		mv.addObject("targetFrame", "mainFramex");
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
		return mv;
	}
	
	
	/**
	 * 整改分派-右侧列表
	 * @param request
	 * @return
	 
	@Operation(summary="整改分派-右侧列表")
	@RequestMapping(value = "/zgfp/zgfp_defect_list")
	public ModelAndView zgfp_defect_list(HttpServletRequest request) {
		String solutionid=request.getParameter("solutionid");
		String orgid = request.getParameter("orgid");
		TblProblemEntity pro = new TblProblemEntity();
		String probnum = request.getParameter("probnum");
		String probname = request.getParameter("probname");
		String riskname = request.getParameter("riskname");
		String belongstoText = request.getParameter("belongstoText");
		String person = request.getParameter("person");
		String quest = request.getParameter("quest");
		if (probnum != null && probnum.length() > 0) {
			pro.setProbnumber(probnum);
		}
		if (probname != null && probname.length() > 0) {
			pro.setProbname(probname);
		}
		if (riskname != null && riskname.length() > 0) {
			pro.setRiskbelongsto(riskname);
		}
		if (belongstoText != null && belongstoText.length() > 0) {
			pro.setProborgs(belongstoText);
		}
		if (person != null && person.length() > 0) {
			pro.setPersonincharge(person);
		}
		if (quest != null && quest.length() > 0) {
			pro.setProbfrom(quest);
		}
		TblProblemService problemService = SpringContextHolder.getBean("TblProblemService");
		String number = request.getParameter("pageNumber");
		Integer pageNumber = 1;
		if (number != null && number.length() > 0) {
			pageNumber = Integer.parseInt(number);
		}
		String orgtype = request.getParameter("orgtype");
		TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
		if(orgid==null || orgid==""){
			orgid=attribute1.getOrgid().toString();
			orgtype=attribute1.getOrgtype().toString();
		}else{
			TblOrganization organ = organizationService.findById(orgid);
			orgid=organ.getOrgid().toString();
			orgtype=organ.getOrgtype().toString();
		}
		mav = new ModelAndView("nbsj/sjzg/zgfp_question_allocate_list");
		pageBean = problemService.getAllReformProBySlouidNbsj(pro,solutionid ,pageNumber, pageBean.getPageSize(), orgid, orgtype);
		mav.addObject("pageBean", pageBean);
		mav.addObject("pro", pro);
		mav.addObject("solutionid", solutionid);
		mav.addObject("orgid", orgid);
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mav.addObject("choiceSearch",choiceSearch); 
		return mav;
	}
	
	
	/**
	 * 整改分派-保存整改人
	 * @param request
	 * @return
	 
	@Operation(summary="整改分派-保存整改人")
	@RequestMapping(value = "/zgfp/assign")
	public ModelAndView zgfpAssign(HttpServletRequest request) {
			String userId = request.getParameter("userId");
			String sId = request.getParameter("reformid");
			String solutionid=request.getParameter("solutionid");
			TblNbsjBugService bugService = SpringContextHolder.getBean("TblNbsjBugService");
			if (sId != null && !sId.equals("")) {
				String[] selectid=sId.split(",");
				for (String string : selectid) {
					TblNbsjRefopmEntity reform = new TblNbsjRefopmEntity();
					if (string.indexOf("__b") >= 0) {
						TblNbsjBugEntity b = bugService.findById(new BigDecimal(string.replace("__b", "")));
						if(b!=null){
							TblNbsjRefopmEntity reforms = tblNbsjRefopmService.findTblReformByBugIdAndSoultionid(b.getBugid().toString(),solutionid);
							if(reforms!=null && reforms.getStatus()==1 && reforms.getNbsuser()==null){
								reforms.setLastreformstatus(1);
								if(userId!=null && userId!=""){
									TblStaff user = userService.findById(userId);
									reforms.setNbsuser(user);
								}
								reforms.setCreateDate( new Date());
								tblNbsjRefopmService.update(reforms);
							}else{
								if(reforms!=null && reforms.getNbsuser()!=null){
									reform.setLastreformstatus(1);
									if(userId!=null && userId!=""){
										TblStaff user = userService.findById(userId);
										reform.setNbsuser(user);
									}
									reform.setTblBug(b);
									reform.setTblCeaters(reforms.getTblCeaters());
									reform.setTblNbsjReformSolution(reforms.getTblNbsjReformSolution());
									reform.setCreateDate( new Date());
									reform.setStatus(1);
									tblNbsjRefopmService.save(reform);
									reforms.setLastreformstatus(0);
									if(reforms.getStatus()==1){
										reforms.setStatus(0);
									}
									tblNbsjRefopmService.update(reforms);
								}
							}
							
							
						}
						
						
					} else {
						TblNbsjQuestionEntity question = tblNbsjQuestionService.getById(new BigDecimal(string.replace("__p", "")));
						if(question!=null){
							TblNbsjRefopmEntity reforms = tblNbsjRefopmService.findTblReformBySheetIdAndSoultionid(question.getQuestionId().toString(),solutionid);
							if(reforms!=null && reforms.getStatus()==1 && reforms.getNbsuser()==null){
								reforms.setLastreformstatus(1);
								if(userId!=null && userId!=""){
									TblStaff user = userService.findById(userId);
									reforms.setNbsuser(user);
								}
								reforms.setCreateDate( new Date());
								tblNbsjRefopmService.update(reforms);
							}else{
								if(reforms!=null && reforms.getNbsuser()!=null){
									reform.setLastreformstatus(1);
									if(userId!=null && userId!=""){
										TblStaff user = userService.findById(userId);
										reform.setNbsuser(user);
									}
									reform.setNbsjQuestion(question);
									reform.setTblCeaters(reforms.getTblCeaters());
									reform.setTblNbsjReformSolution(reforms.getTblNbsjReformSolution());
									reform.setCreateDate( new Date());
									reform.setStatus(1);
									tblNbsjRefopmService.save(reform);
									reforms.setLastreformstatus(0);
									if(reforms.getStatus()==1){
										reforms.setStatus(0);
									}
									tblNbsjRefopmService.update(reforms);
								}
							}
							
						}
						

					}
				}
			}
			TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
			if(solution!=null && solution.getRunstatus()!=null && solution.getRunstatus()==4){
				solution.setRunstatus(2);
				tblNbsjReformSolutionService.saveOrUpdate(solution);
			}
			/*if(solution!=null) {
				//分派整改执行人，发送邮件提醒整改执行人
				String[] copys=JDBCPropertiesUtils.zgfsemail.split(",");
				Product baicai=new Product(); 
				baicai.setZgcode(solution.getSolutioncode());    
				baicai.setType("2");
				String path = request.getSession().getServletContext().getRealPath("/");
				baicai.setFilepath(path);
				if(userId!=null && userId!=""){
					TblStaff user = userService.findById(userId);
					baicai.setUserName(user.getRealname());
					baicai.setEmail(user.getEmail());
				}
				baicai.setPrice(1.5f);
				baicai.setCopys(copys);
				observer.update(baicai, 0.5f);
			}
		return zgfp_defect_list(request);
	}
	

	
	/**
     * 审计整改-整改落实
     *
     * @return
     
    @Operation(summary="审计整改-整改落实")
    @RequestMapping(value = "/sjzg/index_question_rectify_impl")
    public ModelAndView index_question_rectify_implZggz(String solutioncode, Integer pageNumber,HttpServletRequest request) {
      /*  ModelAndView mv = new ModelAndView();
        mv.setViewName("nbsj/sjzg/index_question_rectify_impl");
        return mv;
    	
    	ModelAndView mv = new ModelAndView();
		TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
		pageBean=tblNbsjReformSolutionService.findAllByUseridGZ(tblStaff.getStaffid().toString(), solutioncode, pageNumber, pageBean.getPageSize());
		mv.setViewName("nbsj/sjzg/zggz_solutionmgmt");
		mv.addObject("view", request.getParameter("view"));
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
		mv.addObject("pageBean", pageBean);
		return mv;
    	
    }
    
    
    
    
    /**
	 * 整改落实-主页
	 * @param solutionid
	 * @return
	 
	@Operation(summary="整改落实-主页")
	@RequestMapping(value = "/zgls/zgls_xindex")
	public ModelAndView zgls_xindex(String solutionid,HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		
		TblNbsjReformSolutionEntity findBySolutionid = tblNbsjReformSolutionService.findBySolutionid(solutionid.toString());
		
		mv.setViewName("/nbsj/sjzg/zgls_index");
		
		mv.addObject("orgid", findBySolutionid.getReformCompany().getOrgid());
		
		mv.addObject("solutionid", solutionid.toString());
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
		return mv;
		
		
	}
	
	/**
	 * 整改落实-左侧tree
	 * @param request
	 * @return
	 
	@Operation(summary="整改落实-左侧tree")
	@RequestMapping(value = "/zgls/userleft")
	public ModelAndView userleft_zgls(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/nbsj/sjzg/zgls_left");
		String orgid=request.getParameter("orgid");
		String solutionid=request.getParameter("solutionid");
		mv.addObject("orgid", orgid);
		mv.addObject("solutionid", solutionid);
		mv.addObject("targetFrame", "mainFramex");
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
		return mv;
	}
	
	/**
	 * 整改落实-列表
	 * @param orgId
	 * @return
	 
	@Operation(summary="整改落实-列表")
	@RequestMapping(value = "/zgls/reform_list")
	public ModelAndView reform_list(HttpServletRequest request){
		ModelAndView mv=new  ModelAndView();
		String solutionid=request.getParameter("solutionid");
		String orgid = request.getParameter("orgid");
		TblProblemEntity pro = new TblProblemEntity();
		String probnum = request.getParameter("probnum");
		String probname = request.getParameter("probname");
		String riskname = request.getParameter("riskname");
		String belongstoText = request.getParameter("belongstoText");
		String person = request.getParameter("person");
		String quest = request.getParameter("quest");
		if (probnum != null && probnum.length() > 0) {
			pro.setProbnumber(probnum);
		}
		if (probname != null && probname.length() > 0) {
			pro.setProbname(probname);
		}
		if (riskname != null && riskname.length() > 0) {
			pro.setRiskbelongsto(riskname);
		}
		if (belongstoText != null && belongstoText.length() > 0) {
			pro.setProborgs(belongstoText);
		}
		if (person != null && person.length() > 0) {
			pro.setPersonincharge(person);
		}
		if (quest != null && quest.length() > 0) {
			pro.setProbfrom(quest);
		}
		TblProblemService problemService = SpringContextHolder.getBean("TblProblemService");
		String number = request.getParameter("pageNumber");
		Integer pageNumber = 1;
		if (number != null && number.length() > 0) {
			pageNumber = Integer.parseInt(number);
		}
		TblOrganization organization = organizationService.findById(orgid);
		String orgtype="";
		if(organization!=null){
			orgtype=organization.getOrgtype().toString();
		}
		//获取当前用户
		TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
		String staffid="";
		if(staffid!=null){
			staffid = tblStaff.getStaffid().toString();
		}
		
		pageBean = problemService.getAllReformProBySlouidNbsjLS(pro,solutionid ,pageNumber, pageBean.getPageSize(), orgid, orgtype,staffid);
		mv.addObject("pageBean", pageBean);
		mv.addObject("pro", pro);
		mv.addObject("solutionid", solutionid);
		mv.addObject("orgid", orgid);
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
		mv.setViewName("nbsj/sjzg/zgls_reform_list");
		return mv;
	}
	/**
	 * 整改落实-跳转落实信息页面
	 * @param request
	 * @return
	 
	@Operation(summary="整改落实-跳转落实信息页面")
	@RequestMapping(value = "/zgls/tols_reform")
	public ModelAndView tols_reform(HttpServletRequest request){
		ModelAndView mv=new  ModelAndView();
		String reformid=request.getParameter("reformid");
		String orgid=request.getParameter("orgid");
		String solutionid=request.getParameter("solutionid");
		//查看整改方案，获取备注
		TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
		String memo = solution.getMemo();
		mv.addObject("memo", memo);
		List<TblAttachmentEntity> factAtt = null;
		if(reformid!=null && reformid.trim().length()>0){
			TblNbsjRefopmEntity reform = tblNbsjRefopmService.getByid(reformid);
			mv.addObject("reform", reform);
			//附件
			factAtt = attachmentService.findAllByTblNbsjReform(reformid);
		}
		String selectedId=request.getParameter("bugid");
		if (selectedId.indexOf("__b") >= 0) {
			TblBugEntity bug = tblBugService.findById(new BigDecimal(selectedId.replace("__b", "")));
			mv.addObject("bug",bug);
			TblBugCriterionEntity tblBugCriterion = tblBugCriterionService.findByTblBugCriterion(bug.getBugid().toString());
			if (tblBugCriterion != null) {
				mv.addObject("tblBugCriterion", tblBugCriterion);
			}

		}else{
			TblNbsjQuestionEntity question = tblNbsjQuestionService.getById(new BigDecimal(selectedId.replace("__p", "")));
			mv.addObject("question",question);
		}
		mv.addObject("reformid", reformid);
		mv.addObject("solutionid", solutionid);
		mv.addObject("orgid", orgid);
		mv.addObject("bugid",selectedId);
		mv.addObject("factAtt", factAtt);
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
		mv.setViewName("nbsj/sjzg/tols_reform");
		return mv;
	}
	
	/**
	 * 整改落实-保存落实信息
	 * @param request
	 * @return
	 
	@Operation(summary="整改落实-保存落实信息")
	@RequestMapping(value = "/zggz/ls")
	public ModelAndView sjzggzLs(HttpServletRequest request) {
		String rid = request.getParameter("reformid");
		String measure = request.getParameter("measure");
		String carryout = request.getParameter("carryout");
		String handling = request.getParameter("handling");
		String deadline = request.getParameter("deadline");
		String result = request.getParameter("result");
		String orgid = request.getParameter("orgid");
		String solutionid=request.getParameter("solutionid");
		if(solutionid!=null&& solutionid.trim().length()>0){
			TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
			if(solution!=null){
				solution.setRunstatus(2);
				tblNbsjReformSolutionService.saveOrUpdate(solution);
			}
		}

		TblNbsjRefopmEntity  r = null;
		if (rid != null && !rid.equals("")) {
			r = tblNbsjRefopmService.getByid(rid);
			r.setReformmeasure(measure);
			r.setReformcarryout(carryout);
			r.setHandling(handling);
			r.setStatus(2);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (deadline != null && !"".equals(deadline)) {
					r.setReformdeadline(sdf.parse(deadline));
				} else {
					r.setReformdeadline(new Date());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			r.setReformtime(new Date());
			r.setReformresult(result);
	        //保存附件
	        String attids = request.getParameter("attids");
	        if (StringUtils.isNotBlank(attids)){
	        	String[] ids = attids.split(",");
	        	for (int i = 0; i < ids.length; i++) {
	        		TblAttachmentEntity att = attachmentService.findById(ids[i].trim());
	        		r.getTblNbsjReformAtts().add(att);
	        	}
	        }
			tblNbsjRefopmService.update(r);
		}
		String bugid=request.getParameter("bugid");
		ModelAndView mv=new  ModelAndView();
		mv.addObject("orgid",orgid);
		mv.addObject("solutionid",solutionid);
//		mv.setViewName("redirect:/nbsj/zgls/tols_reform?bugid="+bugid);
//		return mv;
		return tols_reform(request);
	}


	/**
	 * 整改落实-保存附件
	 
	@SuppressWarnings("unchecked")
	@Operation(summary="整改落实-保存附件")
	@RequestMapping(value = "/zggz/zgls_addfj")
	 public ModelAndView zgls_addfj(HttpServletRequest request, MultipartFile file, String reformid) {
//        logger.info("整改落实--整改落实情况--添加附件");
        TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
        //String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/sjzgls/");
        String fileName = file.getOriginalFilename();
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String oldname = fileName.substring(0,fileName.lastIndexOf("."));
		String newname=fileName.replace(oldname, timeInMillis+"zgls");
		long size = file.getSize();
		try {
			boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
			if(flag){
//				 logger.info("上传成功");
			}else{
//				 logger.info("上传失败");
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
        try {
            TblAttachmentEntity a = new TblAttachmentEntity();
            a.setAttname(fileName);
            a.setAttpath(newname);
            a.setAttsize(new BigDecimal(size / 1024));
            a.setUploader(user.getUsername());
            a.setUploadtime(new Date());
            attachmentService.add(a);
            //获取整改落实
            TblNbsjRefopmEntity reform = tblNbsjRefopmService.getByid(reformid);
            reform.getTblNbsjReformAtts().add(a);
            //更新整改落实
            tblNbsjRefopmService.update(reform);
        } catch (Exception e) {
//            logger.error("整改落实--整改落实情况--添加附件--文件上传失败");
        }
        return tols_reform(request);
    }
	
	
	/**
	 * 整改落实--删除附件
	 
	@Operation(summary="整改落实--删除附件")
	@RequestMapping(value = "/zggz/zgls_deletefj")
    public ModelAndView zgls_delfj(HttpServletRequest request, String attid, String reformid) {
//        logger.info("整改落实--整改落实情况--附件删除");
        //获取整改落实
        TblNbsjRefopmEntity reform = tblNbsjRefopmService.getByid(reformid);
        TblAttachmentEntity att = attachmentService.findById(attid);
        //移除整改落实中的附件信息
        reform.getTblNbsjReformAtts().remove(att);
        //更新整改落实---删除中间表数据
        tblNbsjRefopmService.update(reform);
        return tols_reform(request);
    }
	
	
	/**
	 * 整改落实-提交落实信息
	 * @param request
	 * @return
	 
	@Operation(summary="整改落实-提交落实信息")
	@RequestMapping(value = "/zgls/save_solution",produces = "application/json; charset=utf-8")
	public @ResponseBody String save_solution(HttpServletRequest request) {
		String solutionid = request.getParameter("solutionid");
		TblStaff user=(TblStaff) request.getSession().getAttribute("longUser");
		if (solutionid!=null && solutionid.trim().length()>0) {
			try {
				
				 
				 List<TblNbsjRefopmEntity> wzg = tblNbsjRefopmService.findTblReformByUseridAndSoultionidzgz(user.getStaffid().toString(),solutionid);
				 if(wzg!=null && wzg.size()>0){
					 return JsonBean.error("整改未完成，无法提交");
				 }
				 List<TblNbsjRefopmEntity> list = tblNbsjRefopmService.findTblReformByUseridAndSoultionid(user.getStaffid().toString(),solutionid);
				 if(list!=null && list.size()>0){
					 for (TblNbsjRefopmEntity tblReform : list) {
						 tblReform.setStatus(3);
						 tblNbsjRefopmService.update(tblReform);
						 
					}
					 
					 Integer wcczs = tblNbsjRefopmService.findTblReformBySoultionidWWC(solutionid);
					 Integer wczs = tblNbsjRefopmService.findTblReformBySoultionidWC(solutionid);
					 TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
					 if(wczs==wcczs){
						 solution.setRunstatus(4);
						 tblNbsjReformSolutionService.saveOrUpdate(solution);
					 }
					 return JsonBean.success();
				 }else{
					 return JsonBean.error("提交失败");
				 }
				
			} catch (Exception e) {
				e.printStackTrace();
				return JsonBean.error("提交失败");
			}
		}else{
			return JsonBean.error("请选择");
		}
	}
	
	
    
    
	

	/**
	 * 整改跟踪——跟踪-主页
	 * @param solutionid
	 * @return
	 
	@Operation(summary="整改跟踪——跟踪-主页")
	@RequestMapping(value = "/zggz/zggz_xindex")
	public ModelAndView zggz_xindex(HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		String solutionid=request.getParameter("selectedid");
		TblNbsjReformSolutionEntity solution = tblNbsjReformSolutionService.findBySolutionid(solutionid);
		mv.addObject("orgid", solution.getReformCompany().getOrgid());
		mv.addObject("solutionid",solutionid);
		mv.setViewName("/nbsj/sjzg/gzIndex");
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
		return mv;
	}
    
	/**
	 * 整改跟踪——跟踪-左侧树
	 * @return
	 
	@Operation(summary="整改跟踪——跟踪-左侧树")
	@RequestMapping(value="/zggz/zggz_leftTree")
	public ModelAndView zggz_tree(HttpServletRequest request){
		String orgid=request.getParameter("orgid");
		String solutionid=request.getParameter("solutionid");
		ModelAndView mv = new ModelAndView("/nbsj/sjzg/sjgz_left");
		mv.addObject("orgid", orgid);
		mv.addObject("solutionid", solutionid);
		mv.addObject("targetFrame", "mainFramex");
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
		return mv;
	}
	
	/**
	 * 整改跟踪——跟踪-列表
	 * @param request
	 * @return
	 
	
	@Operation(summary="整改跟踪——跟踪-列表")
	@RequestMapping(value = "/zggz/zggz_defect_list")
	public ModelAndView zggz_defect_list(HttpServletRequest request) {
		String solutionid=request.getParameter("solutionid");
		String orgid = request.getParameter("orgid");
		TblProblemEntity pro = new TblProblemEntity();
		String probnum = request.getParameter("probnum");
		String probname = request.getParameter("probname");
		String riskname = request.getParameter("riskname");
		String belongstoText = request.getParameter("belongstoText");
		String person = request.getParameter("person");
		String quest = request.getParameter("quest");
		if (probnum != null && probnum.length() > 0) {
			pro.setProbnumber(probnum);
		}
		if (probname != null && probname.length() > 0) {
			pro.setProbname(probname);
		}
		if (riskname != null && riskname.length() > 0) {
			pro.setRiskbelongsto(riskname);
		}
		if (belongstoText != null && belongstoText.length() > 0) {
			pro.setProborgs(belongstoText);
		}
		if (person != null && person.length() > 0) {
			pro.setPersonincharge(person);
		}
		if (quest != null && quest.length() > 0) {
			pro.setProbfrom(quest);
		}
		TblProblemService problemService = SpringContextHolder.getBean("TblProblemService");
		String number = request.getParameter("pageNumber");
		Integer pageNumber = 1;
		if (number != null && number.length() > 0) {
			pageNumber = Integer.parseInt(number);
		}
		String orgtype = request.getParameter("orgtype");
		TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
		if(orgid==null || orgid==""){
			orgid=attribute1.getOrgid().toString();
			orgtype=attribute1.getOrgtype().toString();
		}else{
			TblOrganization organ = organizationService.findById(orgid);
			orgid=organ.getOrgid().toString();
			orgtype=organ.getOrgtype().toString();
		}
		mav = new ModelAndView("nbsj/sjzg/zggz_list");
		pageBean = problemService.nbsjZggz_gz(pro,solutionid ,pageNumber, pageBean.getPageSize(), orgid, orgtype);
		mav.addObject("pageBean", pageBean);
		mav.addObject("pro", pro);
		mav.addObject("solutionid", solutionid);
		mav.addObject("orgid", orgid);
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mav.addObject("choiceSearch",choiceSearch); 
		return mav;
	}
	
	
	/**
     * 审计整改-整改查询
     *
     * @param request
     * @return
     
    @Operation(summary="审计整改-整改查询")
    @RequestMapping(value = "/sjzg/question_rectify_distory_list")
    public ModelAndView question_rectify_distory_listZggz(HttpServletRequest request,Integer pageNumber,String code,BigDecimal orgId) {
    	zgcxListQuery(request, pageNumber, code, orgId);
    	mav.setViewName("nbsj/sjzg/question_rectify_distory_list");
    	mav.addObject("view", request.getParameter("view"));
    	mav.addObject("name", request.getParameter("name"));
    	//查询框代码
    	String choiceSearch = request.getParameter("choiceSearch");
    	if(choiceSearch == null || "".equals(choiceSearch)) {
    		choiceSearch = "hide";
    	}
    	mav.addObject("choiceSearch",choiceSearch); 	
        return mav;
    }
    
    private void zgcxListQuery(HttpServletRequest request, Integer pageNumber, String code, BigDecimal orgId) {
		TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");//选则的机构
		TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgName");//当前用户的机构
		TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
		//用于区别整改跟踪和整改查询,后面的sql语句中会用到
		String name=request.getParameter("name");
		Integer tagType = 2;
		if(attribute.getOrgid().toString().equals(attribute1.getOrgid().toString())){
			if (orgId == null) {
				orgId = tblStaff.getTblOrganization().getOrgid();
			}
			pageBean = tblNbsjReformSolutionService.findAllcxs(orgId.toString(), code,name, pageNumber, pageBean.getPageSize(), tblStaff.getStaffid().toString(),tagType);
			mav.addObject("pageBean",pageBean);
			mav.addObject("code",code);

		}else {
			if (orgId == null) {
				orgId = attribute.getOrgid();
			}
			pageBean = tblNbsjReformSolutionService.findAllcxs(orgId.toString(), code, name,pageNumber, pageBean.getPageSize(), tblStaff.getStaffid().toString(),tagType);
			mav.addObject("pageBean",pageBean);
			mav.addObject("code",code);
		}
	}
    
    
    
    
    private void zggzListQuery(HttpServletRequest request, Integer pageNumber, String code, BigDecimal orgId) {
		TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");//选则的机构
		TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgName");//当前用户的机构
		TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
		//用于区别整改跟踪和整改查询,后面的sql语句中会用到
		Integer tagType = 2;
		if(attribute.getOrgid().toString().equals(attribute1.getOrgid().toString())){
			if (orgId == null) {
				orgId = tblStaff.getTblOrganization().getOrgid();
			}
			pageBean = tblNbsjReformSolutionService.findAlls(orgId.toString(), code, pageNumber, pageBean.getPageSize(), tblStaff.getStaffid().toString(),tagType);
			mav.addObject("pageBean",pageBean);
			mav.addObject("code",code);

		}else {
			if (orgId == null) {
				orgId = attribute.getOrgid();
			}
			pageBean = tblNbsjReformSolutionService.findAlls(orgId.toString(), code, pageNumber, pageBean.getPageSize(), tblStaff.getStaffid().toString(),tagType);
			mav.addObject("pageBean",pageBean);
			mav.addObject("code",code);
		}
	}
    @Operation(summary="zggz-导出")
    @RequestMapping(value = "/zggz/export", produces = "application/json; charset=utf-8")
   	public void export_GZDG(HttpServletRequest request, HttpServletResponse response,String code) {
   		TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
   		TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");//选则的机构
   		List<TblNbsjReformSolutionEntity> list = tblNbsjReformSolutionService.findAlls(attribute.getOrgid().toString(), code, user.getStaffid().toString());
   		String[] cNames = { "方案编号", "方案名称", "创建人", "创建日期", "整改责任人", "状态" };
   		ExcelUtil exportUtil = new ExcelUtil("整改查询", cNames);
   		HSSFCell cell = null;
   		HSSFCellStyle bodyStyle = exportUtil.getBodyStyle();

   		for (int j = 0; j < list.size(); j++) {
   			HSSFRow bodyRow = exportUtil.getNextRow(j + 1);
   			TblNbsjReformSolutionEntity solution = list.get(j);
   			cell = bodyRow.createCell(0);
   			cell.setCellStyle(bodyStyle);
   			cell.setCellValue(solution.getSolutioncode());
   			cell = bodyRow.createCell(1);
   			cell.setCellStyle(bodyStyle);
   			cell.setCellValue(solution.getSolutionname());

   			cell = bodyRow.createCell(2);
   			cell.setCellStyle(bodyStyle);
   			cell.setCellValue(solution.getTblStaff().getRealname());

   			cell = bodyRow.createCell(3);
   			cell.setCellStyle(bodyStyle);
   			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   			cell.setCellValue(sdf.format(solution.getCreatedate()));
   			cell = bodyRow.createCell(4);
   			cell.setCellStyle(bodyStyle);
   			cell.setCellValue(solution.getReformUser()!=null?solution.getReformUser().getRealname():"");

   			cell = bodyRow.createCell(5);
   			cell.setCellStyle(bodyStyle);
   			if(solution.getRunstatus()==0){
   				cell.setCellValue("未分派");
   			}
   			if(solution.getRunstatus()==1){
   				cell.setCellValue("开始整改");
   			}
   			if(solution.getRunstatus()==2){
   				cell.setCellValue("整改中");
   			}
   			if(solution.getRunstatus()==3){
   				cell.setCellValue("方案已结束");
   			}
   			if(solution.getRunstatus()==4){
   				cell.setCellValue("整改完成");
   			}
   			
   		}
   		exportUtil.writeOut(response, "整改查询.xls");
   	}
    @Operation(summary="zggz-导出")
    @RequestMapping(value = "/zggz/exportNew", produces = "application/json; charset=utf-8")
   	public void export_GZDGNew(HttpServletRequest request, HttpServletResponse response,String code) {
   		TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
   		TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");//选则的机构
   		List<TblNbsjReformSolutionEntity> list = tblNbsjReformSolutionService.findAlls(attribute.getOrgid().toString(), code, user.getStaffid().toString());
   		
		ExcelUtil exportUtil = new ExcelUtil();
		short a = 1200;
		HSSFSheet sheet = exportUtil.getHSSFSheet("整改方案");
		int i = 0;
		HSSFRow bodyRow = exportUtil.getNextRow(i);
   		HSSFCell cell = null;
   		HSSFCellStyle bodyStyle = exportUtil.getBodyStyle();// 单元格样式
		bodyRow = sheet.createRow(i);
		bodyRow.setHeight(a);
		cell = bodyRow.createCell(0);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("方案编号");
		cell = bodyRow.createCell(1);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("方案名称");
		cell = bodyRow.createCell(2);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("创建人");
		cell = bodyRow.createCell(3);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("创建日期");
		cell = bodyRow.createCell(4);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("整改责任人");
		cell = bodyRow.createCell(5);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("状态");
		sheet.setColumnWidth(1, 40*256);
		i++;
		TblProblemService problemService = SpringContextHolder.getBean("TblProblemService");
		List<Object> objlist=new ArrayList<Object>();
		for (TblNbsjReformSolutionEntity solution : list) {
			bodyRow = sheet.createRow(i);
			bodyRow.setHeight(a);
			cell = bodyRow.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(solution.getSolutioncode());
			cell = bodyRow.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(solution.getSolutionname());
			cell = bodyRow.createCell(2);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(solution.getTblStaff().getRealname());
			cell = bodyRow.createCell(3);
			cell.setCellStyle(bodyStyle);
   			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			cell.setCellValue(sdf.format(solution.getCreatedate()));
			cell = bodyRow.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(solution.getReformUser()!=null?solution.getReformUser().getRealname():"");
			cell = bodyRow.createCell(5);
			cell.setCellStyle(bodyStyle);
   			if(solution.getRunstatus()==0){
   				cell.setCellValue("未分派");
   			}
   			if(solution.getRunstatus()==1){
   				cell.setCellValue("开始整改");
   			}
   			if(solution.getRunstatus()==2){
   				cell.setCellValue("整改中");
   			}
   			if(solution.getRunstatus()==3){
   				cell.setCellValue("方案已结束");
   			}
   			if(solution.getRunstatus()==4){
   				cell.setCellValue("整改完成");
   			}
   			//List<Object> ee = problemService.getAllReformProBySlouidNbsjCXList(null,solution.getSolutionid().toString(), attribute.getOrgid().toString(), null);
   			/*if(ee!=null&&ee.size()!=0) {
   				objlist.addAll(ee);
   			}
			i++;
		}
		//第二sheet页
		sheet = exportUtil.getHSSFSheet("整改问题");
		i =0;
		bodyRow = sheet.createRow(i);
		bodyRow.setHeight(a);
		cell = bodyRow.createCell(0);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("问题编号");
		cell = bodyRow.createCell(1);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("被合规单位");
		cell = bodyRow.createCell(2);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("问题详情");
		cell = bodyRow.createCell(3);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("问题来源");
		cell = bodyRow.createCell(4);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("发现人");
		cell = bodyRow.createCell(5);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("整改执行人");
		cell = bodyRow.createCell(6);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("状态");
		cell = bodyRow.createCell(7);
		cell.setCellStyle(bodyStyle);
		cell.setCellValue("整改方案编号");
		sheet.setColumnWidth(1, 40*150);
		sheet.setColumnWidth(2, 40*256);
		i++;
		Object[] objs = null;
		for (Object obj : objlist) {
			objs = (Object[])obj;
			bodyRow = sheet.createRow(i);
			bodyRow.setHeight(a);
			cell = bodyRow.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(objs[1].toString());//问题编号
			cell = bodyRow.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(objs[2].toString());//被合规单位
			cell = bodyRow.createCell(2);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(objs[6].toString());//问题详情
			cell = bodyRow.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(objs[3].toString());//问题来源
			cell = bodyRow.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(objs[4].toString());//发现人
			cell = bodyRow.createCell(5);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(objs[5]==null?"":objs[5].toString());//整改执行人
			cell = bodyRow.createCell(6);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(objs[7].toString());//状态
   			if(objs[7].toString().equals("0")){
   				cell.setCellValue("未分派");
   			}
   			if(objs[7].toString().equals("1")){
   				cell.setCellValue("开始整改");
   			}
   			if(objs[7].toString().equals("2")){
   				cell.setCellValue("整改中");
   			}
   			if(objs[7].toString().equals("3")){
   				cell.setCellValue("整改完成");
   			}
			cell = bodyRow.createCell(7);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(objs[8].toString());//整改方案编号
			i++;
		}
			
   		exportUtil.writeOut(response, "整改查询.xls");
   	}
    /**
	 * 整改查询-主页s
	 * @param solutionid
	 * @return
	 
	@Operation(summary="整改查询-主页s")
	@RequestMapping(value = "/zggz/zgcx_index")
	public ModelAndView zgcx_index(String[] solutionid,HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		TblNbsjReformSolutionEntity findBySolutionid = tblNbsjReformSolutionService.findBySolutionid(solutionid[0].toString());
		mv.setViewName("/nbsj/sjzg/zgcx_index");
		//mv.addObject("orgid", findBySolutionid.getReformCompany().getOrgid());
		mv.addObject("solutionid", solutionid[0].toString());
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
		return mv;
	}
	
	
	@Operation(summary="zggz-zgcxs_left")
	@RequestMapping(value = "/zggz/zgcxs_left")
	public ModelAndView zgcxs_left(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/nbsj/sjzg/zgcx_left");
		String orgid=request.getParameter("orgid");
		String solutionid=request.getParameter("solutionid");
		mv.addObject("orgid", orgid);
		mv.addObject("solutionid", solutionid);
		mv.addObject("targetFrame", "mainFramex");
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 
		return mv;
	}
	/**
	 * 整改跟踪---整改查询-列表
	 * @param orgId
	 * @return
	 
	@Operation(summary="整改跟踪---整改查询-列表")
	@RequestMapping(value = "/zggz/zgcx_list")
	public ModelAndView zgcx_list(HttpServletRequest request, String orgId){
		ModelAndView mv=new  ModelAndView();
		String solutionid=request.getParameter("solutionid");
		String orgid = request.getParameter("orgid");
		TblProblemEntity pro = new TblProblemEntity();
		String probnum = request.getParameter("probnum");
		String probname = request.getParameter("probname");
		String riskname = request.getParameter("riskname");
		String belongstoText = request.getParameter("belongstoText");
		String person = request.getParameter("person");
		String quest = request.getParameter("quest");
		if (probnum != null && probnum.length() > 0) {
			pro.setProbnumber(probnum);
		}
		if (probname != null && probname.length() > 0) {
			pro.setProbname(probname);
		}
		if (riskname != null && riskname.length() > 0) {
			pro.setRiskbelongsto(riskname);
		}
		if (belongstoText != null && belongstoText.length() > 0) {
			pro.setProborgs(belongstoText);
		}
		if (person != null && person.length() > 0) {
			pro.setPersonincharge(person);
		}
		if (quest != null && quest.length() > 0) {
			pro.setProbfrom(quest);
		}
		TblProblemService problemService = SpringContextHolder.getBean("TblProblemService");
		String number = request.getParameter("pageNumber");
		Integer pageNumber = 1;
		if (number != null && number.length() > 0) {
			pageNumber = Integer.parseInt(number);
		}
		TblOrganization organization = organizationService.findById(orgid);
		String orgtype="";
		if(organization!=null){
			orgtype=organization.getOrgtype().toString();
		}
		pageBean = problemService.getAllReformProBySlouidNbsjCX(pro,solutionid ,pageNumber, pageBean.getPageSize(), orgid, orgtype);
		mv.addObject("pageBean", pageBean);
		mv.addObject("pro", pro);
		mv.addObject("solutionid", solutionid);
		mv.addObject("orgid", orgid);
		mv.setViewName("nbsj/sjzg/zgcx_list");
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
		return mv;
	}
	
	
	@Operation(summary="zggz-zgcx_jg")
	@RequestMapping(value = "/zggz/zgcx_jg")
	public ModelAndView zgcx_jg(HttpServletRequest request,String selectedId,String solutionid) {
		String orgid = request.getParameter("orgid");
		ModelAndView mv = new ModelAndView();
		if (selectedId.indexOf("__b") >= 0) {
			TblBugEntity bug = tblBugService.findById(new BigDecimal(selectedId.replace("__b", "")));
			if (bug != null && bug.getBugid() != null){
				List<TblNbsjRefopmEntity> reforms = tblNbsjRefopmService.findAllTblReformByBugIdAndSoultionid(bug.getBugid().toString(),solutionid);
				mv.addObject("reforms",reforms);
				mv.addObject("bug",bug);
				TblBugCriterionEntity tblBugCriterion = tblBugCriterionService.findByTblBugCriterion(bug.getBugid().toString());
				if (tblBugCriterion != null) {
					mv.addObject("tblBugCriterion", tblBugCriterion);
				}
			}

		}else{
			TblNbsjQuestionEntity question = tblNbsjQuestionService.getById(new BigDecimal(selectedId.replace("__p", "")));
			if (question != null && question.getQuestionId() != null){
				List<TblNbsjRefopmEntity> reforms = tblNbsjRefopmService.findAllTblReformBySheetIdAndSoultionid(question.getQuestionId().toString(),solutionid);
				mv.addObject("reforms",reforms);
				mv.addObject("question",question);
			}
		}
		mv.addObject("solutionid",solutionid);
		mv.addObject("selectedId",selectedId);
		mv.addObject("orgid",orgid);
		mv.addObject("selectedId",selectedId);
		mv.setViewName("nbsj/sjzg/zgcx_jg");
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch);
		return mv;
	}
	
	
	@Operation(summary="zgcx-详情查询")
	@RequestMapping(value = "/zgcx/zgcx_detail")
	public ModelAndView detail(HttpServletRequest request) {
		TblNbsjRefopmEntity r = null;
		String rid = request.getParameter("rid");
		if (rid != null && !rid.equals("")) {
			r = tblNbsjRefopmService.getByid(rid);
		}
		ModelAndView mv = new ModelAndView();

		mv.setViewName("nbsj/sjzg/question_zgcx_impl_detail");

		TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");//选则的机构
		List<TblBugCriterionEntity> list = tblBugCriterionService.findAll(attribute.getOrgid().toString());
		mv.addObject("reform", r);
		mv.addObject("list", list);
		return mv;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
     * 工作底稿--新建
     
    @Operation(summary="工作底稿--新建")
    @RequestMapping(value = "/sjss/pmp_audit_project_digao")
	public ModelAndView pmp_audit_project_digao(HttpServletRequest request, BigDecimal sheetid, BigDecimal operateId) {
		ModelAndView mv = new ModelAndView();
		try {
			TblNbsjProjectEntity project = this.tblnbsjProjectService.getSelectProject();
			TblNbsjSheetEntity sheet = null;
			/**
			 * if (null != operateId) { TblNbsjOperate operate =
			 * this.tblNbsjOperateService.get(operateId); sheet =
			 * operate.getNbsjSheet()==null?new TBlNbsjSheet():operate.getNbsjSheet();
			 * if(sheet.getAuditDesc()==null || sheet.getAuditDesc().equals("")){
			 * if(operate.getAuthorization() != null &&
			 * operate.getAuthorization().getAduitProGram() != null){
			 * sheet.setAuditDesc(operate.getAuthorization().getAduitProGram().getSuditProcess());
			 * } } }
			 
			if (sheetid != null) {
				sheet = tBlNbsjSheetService.getById(sheetid);
				List<TblAttachmentEntity> fj = attachmentService.findAllByTblNBSJSheet(sheetid.toString());
				List<TblNbsjSheetReportEntity> tnsrList = tblNbsjSheetReportService.findReportListBySheet(sheetid.toString());
				mv.addObject("fj", fj);
				mv.addObject("tnsrList",tnsrList);

			} /*
				 * else{ if(sheet!=null && sheet.getSheetId()!=null){ List<TblAttachment> fj =
				 * attachmentService.findAllByTblNBSJSheet(sheet.getSheetId().toString());
				 * mv.addObject("fj", fj); } }
				 
			mv.addObject("sheet", sheet);
			String backUrl = request.getParameter("backUrl");
			mv.addObject("project", project);
			mv.addObject("operateId", operateId);
			mv.addObject("type", request.getParameter("type"));
			mv.addObject("modelTye", request.getParameter("type"));
			mv.addObject("backUrl", request.getParameter("backUrl"));
			mv.addObject("backLink", request.getParameter("backLink"));
			mv.addObject("dg", request.getParameter("dg"));
			mv.addObject("abc", "${project.tblnbsjorgs.orgname}");
			if (backUrl != null && backUrl.trim().length() > 0) {
				mv.setViewName("cz/dg/pmp_audit_project_digao");
			} else {
				mv.setViewName("nbsj/sjss/pmp_audit_project_digao");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//查询框代码
		String choiceSearch = request.getParameter("choiceSearch");
		if(choiceSearch == null || "".equals(choiceSearch)) {
			choiceSearch = "hide";
		}
		mv.addObject("choiceSearch",choiceSearch); 	
		return mv;
	}
	
    
    
	/**
     * @Author: TYB
     * @Date: 2017-04-10 下午 1:19
     * @Des: 后台报错跳转error页时传递提示
    *  data:001:未选择项目  002:项目未添加指引模板
    *  test01
    
   	public void setErrorJsp(String data,HttpServletRequest request){
   		request.getSession().removeAttribute("ErrorJsp");
   		request.getSession().setAttribute("ErrorJsp",data);
   	}*/
}
