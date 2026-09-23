package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblWgzzEntity;
import com.huabo.audit.oracle.entity.TblWgzzShbg;
import com.huabo.audit.oracle.entity.TblWgzzWghs;
import com.huabo.audit.oracle.entity.TblWgzzWgjyYs;
import com.huabo.audit.service.TblWgjyYsService;
import com.huabo.audit.service.TblWgzzService;
import com.huabo.audit.service.TblWgzzWghsService;
import com.huabo.audit.util.PageInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.controller
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/26
 * @Time:11:08
 */
@RestController
@Slf4j
@Tag(name="违规追责",description="违规追责")
@RequestMapping(value = "/wgzz/projectTeam")
public class TblWgzzController {

    @Autowired
    TblWgzzService tblWgzzService;
    
    @Resource
    private UserProvider userProvider;

	@OperationLog(
			success = "违规追责受理列表页",
			busType = "整改追责",
			fail = "违规追责受理列表页",
			operationType = OperationType.SELECT,
			subType = "违规追责——获取违规追责——违规追责受理列表页"
	)
    @GetMapping("/wgzzList")
    @Operation(summary = "违规追责受理列表")
    public JsonBean getwgzzList(@Parameter(description="token")@RequestHeader("token") String token,
                                @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber") Integer pageNumber,
                                @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                @Parameter(name="clueNaber",description="clueNaber",required=false)@RequestParam(value ="clueNaber",required=false)String clueNaber,
			@Parameter(name="isaccepted",description="isaccepted",required=false)@RequestParam(value ="isaccepted",required=false)Integer isaccepted
                                ) {
        JsonBean jsonBean = null;
        try {
			TblWgzzEntity tblWgzzEntity = new TblWgzzEntity();
			tblWgzzEntity.setCluenaber(clueNaber);
			tblWgzzEntity.setIsaccepted(isaccepted);
            jsonBean = tblWgzzService.getwfzzlist(token,pageNumber,pageSize,tblWgzzEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonBean;
    }

	@Autowired
	private TblWgzzWghsService tblWgzzWghsService;
	@Autowired
	private TblWgjyYsService tblWgjyYsService;
	@OperationLog(
			success = "违规追责受理列表",
			busType = "整改追责",
			fail = "违规追责受理列表",
			operationType = OperationType.SELECT,
			subType = "违规追责——获取违规追责——初步核实/违规核查/移送函-违规追责受理列表，可以根据type分类"
	)
	@GetMapping("/wgzzExtList")
	@Operation(summary = "初步核实/违规核查/移送函-违规追责受理列表")
	public JsonBean getwgzzExtList(@Parameter(description="token") @RequestHeader("token") String token,
			@Parameter(name="type 1-初步核实 2-违规核查 3-移送函",required=true) @RequestParam("type") Integer type) {
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			TblWgzzEntity tblWgzzEntity = new TblWgzzEntity();
			tblWgzzEntity.setIsaccepted(1);
			PageInfo<TblWgzzEntity> pageInfo = tblWgzzService.getwfzzextlist(token, 1, 10000, tblWgzzEntity);
			if (CollectionUtil.isEmpty(pageInfo.getTlist())) {
				return ResponseFormat.retParam(1, 200, Collections.emptyList());
			}
			if (Objects.equals(type, 1)) {
				List<TblWgzzShbg> bySHBGExtList = tblWgzzWghsService.getBySHBGExtList(1, 10000);
				if (CollectionUtil.isNotEmpty(bySHBGExtList)) {
					List<String> cluenabers = bySHBGExtList.stream().map(TblWgzzShbg::getCluenaber).collect(Collectors.toList());
					if (CollectionUtil.isNotEmpty(cluenabers)) {
						List<TblWgzzEntity> collect = pageInfo.getTlist().stream().filter(item -> !cluenabers.contains(item.getCluenaber()))
								.collect(Collectors.toList());
						return ResponseFormat.retParam(1, 200, collect);
					}
				}
			} else if (Objects.equals(type, 2)) {
				List<TblWgzzWghs> byWghsExtList = tblWgzzWghsService.getByWghsExtList(1, 10000);
				if (CollectionUtil.isNotEmpty(byWghsExtList)) {
					List<String> cluenabers = byWghsExtList.stream().map(TblWgzzWghs::getCluenaber).collect(Collectors.toList());
					if (CollectionUtil.isNotEmpty(cluenabers)) {
						List<TblWgzzEntity> collect = pageInfo.getTlist().stream().filter(item -> !cluenabers.contains(item.getCluenaber()))
								.collect(Collectors.toList());
						return ResponseFormat.retParam(1, 200, collect);
					}
				}
			} else if (Objects.equals(type, 3)) {
				List<TblWgzzWgjyYs> byWgjyYsExtList = tblWgjyYsService.getByWgjyYsExtList(1, 10000);
				if (CollectionUtil.isNotEmpty(byWgjyYsExtList)) {
					List<String> cluenabers = byWgjyYsExtList.stream().map(TblWgzzWgjyYs::getCluenaber).collect(Collectors.toList());
					if (CollectionUtil.isNotEmpty(cluenabers)) {
						List<TblWgzzEntity> collect = pageInfo.getTlist().stream().filter(item -> !cluenabers.contains(item.getCluenaber()))
								.collect(Collectors.toList());
						return ResponseFormat.retParam(1, 200, collect);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200, Collections.emptyList());
	}

	@OperationLog(
			success = "获取违规追责详情",
			busType = "整改追责",
			fail = "获取违规追责详情",
			operationType = OperationType.SELECT,
			subType = "违规追责——获取违规追责——指定违规追责记录详情"
	)
    @GetMapping("/wgzzXQList")
    @Operation(summary = "违规追责列表AND违规经营投资问题线索管理台账详情")
    public JsonBean getwgzzXQList(@Parameter(description="token")@RequestHeader("token") String token,
                                @Parameter(description="clueid")@RequestParam("clueid") BigDecimal clueid
    ) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblWgzzService.getwgzzXQList(token,clueid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonBean;
    }

	@OperationLog(
			success = "违规追责受理新增/修改",
			busType = "整改追责",
			fail = "违规追责受理新增/修改",
			operationType = OperationType.ADD,
			subType = "违规追责——获取违规追责——违规追责列表页受理新增/修改"
	)
    @RequestMapping(value = "/addwgzz", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "违规追责受理新增/修改")
    public JsonBean saveUpdatewgzz(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                   @Parameter(name = "tblWgzzEntity", description = "实体", required = false) TblWgzzEntity tblWgzzEntity){
        JsonBean jsonbean = null;
        try {
            jsonbean = tblWgzzService.saveUpdatewgzz(tblWgzzEntity,token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonbean;
    }

	@OperationLog(
			success = "违规追责受理删除",
			busType = "整改追责",
			fail = "违规追责受理删除",
			operationType = OperationType.DELETE,
			subType = "违规追责——违规追责受理删除指定记录"
	)
    @PostMapping("/deletewgzz")
    @Operation(summary = "违规追责受理删除")
    public JsonBean deletewgzz(@Parameter(description="token")@RequestHeader("token")String token,
                                   @Parameter(description="clueid")@RequestParam("clueid")BigDecimal clueid){
        JsonBean jsonbean = null;
        try {
            jsonbean = tblWgzzService.deletewgzz(token,clueid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonbean;
    }

	@OperationLog(
			success = "违规经营投资问题线索管理台账列表",
			busType = "整改追责",
			fail = "违规经营投资问题线索管理台账列表",
			operationType = OperationType.SELECT,
			subType = "违规追责——获取违规经营投资问题线索管理台账列表"
	)
    @GetMapping("/wgzzArreyByList")
    @Operation(summary = "违规经营投资问题线索管理台账列表")
    public JsonBean getwgzzArreyByList(@Parameter(name="token",required=true) @RequestHeader("token")String token,
                                        @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber") Integer pageNumber,
                                        @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                       @Parameter(name="clueNaber",description="clueNaber",required=false)@RequestParam(value="clueNaber",required=false)String clueNaber
    ) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblWgzzService.getwgzzArreyByList(token,pageNumber,pageSize,clueNaber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonBean;
    }

	@OperationLog(
			success = "违规经营投资问题线索管理台账新增/修改",
			busType = "整改追责",
			fail = "违规经营投资问题线索管理台账新增/修改",
			operationType = OperationType.ADD,
			subType = "违规追责——违规经营投资问题线索管理台账列表页新增/修改"
	)
    @RequestMapping(value = "/saveupdatewgzz", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "违规经营投资问题线索管理台账新增/修改")
    public JsonBean updateaddWgzz(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name = "tblWgzzEntity", description = "实体", required = false) TblWgzzEntity tblWgzzEntity){
        JsonBean jsonbean = null;
        try {
            jsonbean = tblWgzzService.updateaddWgzz(tblWgzzEntity,token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonbean;
    }

	@OperationLog(
			success = "违规经营投资问题线索管理台账删除",
			busType = "整改追责",
			fail = "违规经营投资问题线索管理台账删除",
			operationType = OperationType.DELETE,
			subType = "违规追责——违规经营投资问题线索管理台账删除指定记录"
	)
    @PostMapping("/removewgzz")
    @Operation(summary = "违规经营投资问题线索管理台账删除")
    public JsonBean removewgzz(@Parameter(description="token")@RequestHeader("token")String token,
                               @Parameter(description="clueid")@RequestParam("clueid")BigDecimal clueid){
        JsonBean jsonbean = null;
        try {
            jsonbean = tblWgzzService.removewgzz(token,clueid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonbean;
    }

    /**
     * 违规追责-附件列表
     */
	@OperationLog(
			success = "违规追责-附件列表",
			busType = "整改追责",
			fail = "违规追责-附件列表",
			operationType = OperationType.SELECT,
			subType = "违规追责——获取违规追责-附件列表"
	)
    @GetMapping("/wgzz_file_list")
    @Operation(summary = "违规追责-附件列表")
    public JsonBean wgzz_file_list(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "clueid", description = "业务主键", required = true) @RequestParam(value = "clueid", required = true) BigDecimal clueid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzService.wgzzFileList(token,clueid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

	@OperationLog(
			success = "移送函-选择线索列表",
			busType = "整改追责",
			fail = "移送函-选择线索列表",
			operationType = OperationType.SELECT,
			subType = "违规追责——获取违规追责-移送函-选择线索列表相关信息"
	)
    @GetMapping("/wgzzYsList")
	@Operation(summary = "移送函-选择线索列表")
	public JsonBean wgzzYsList(@Parameter(description="token") @RequestHeader("token") String token,
			@Parameter(name="type 1-初步核实 2-违规核查 3-移送函",required=true) @RequestParam("type") Integer type) {
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			TblWgzzEntity tblWgzzEntity = new TblWgzzEntity();
			tblWgzzEntity.setIsaccepted(1);
			PageInfo<TblWgzzEntity> pageInfo = tblWgzzService.getwfzzYslist(token, 1, 10000, tblWgzzEntity,type);
			if (CollectionUtil.isEmpty(pageInfo.getTlist())) {
				return ResponseFormat.retParam(1, 200, Collections.emptyList());
			}
			if (Objects.equals(type, 1)) {
				List<TblWgzzShbg> bySHBGExtList = tblWgzzWghsService.getBySHBGExtList(1, 10000);
				if (CollectionUtil.isNotEmpty(bySHBGExtList)) {
					List<String> cluenabers = bySHBGExtList.stream().map(TblWgzzShbg::getCluenaber).collect(Collectors.toList());
					if (CollectionUtil.isNotEmpty(cluenabers)) {
						List<TblWgzzEntity> collect = pageInfo.getTlist().stream().filter(item -> !cluenabers.contains(item.getCluenaber()))
								.collect(Collectors.toList());
						return ResponseFormat.retParam(1, 200, collect);
					}
				}else {
					return ResponseFormat.retParam(1, 200, pageInfo.getTlist());
				}
			} else if (Objects.equals(type, 2)) {
				List<TblWgzzWghs> byWghsExtList = tblWgzzWghsService.getByWghsExtList(1, 10000);
				if (CollectionUtil.isNotEmpty(byWghsExtList)) {
					List<String> cluenabers = byWghsExtList.stream().map(TblWgzzWghs::getCluenaber).collect(Collectors.toList());
					if (CollectionUtil.isNotEmpty(cluenabers)) {
						List<TblWgzzEntity> collect = pageInfo.getTlist().stream().filter(item -> !cluenabers.contains(item.getCluenaber()))
								.collect(Collectors.toList());
						return ResponseFormat.retParam(1, 200, collect);
					}
				}else {
					return ResponseFormat.retParam(1, 200, pageInfo.getTlist());
				}
			} else if (Objects.equals(type, 3)) {
				List<TblWgzzWgjyYs> byWgjyYsExtList = tblWgjyYsService.getByWgjyYsExtList(1, 10000);
				if (CollectionUtil.isNotEmpty(byWgjyYsExtList)) {
					List<String> cluenabers = byWgjyYsExtList.stream().map(TblWgzzWgjyYs::getCluenaber).collect(Collectors.toList());
					if (CollectionUtil.isNotEmpty(cluenabers)) {
						List<TblWgzzEntity> collect = pageInfo.getTlist().stream().filter(item -> !cluenabers.contains(item.getCluenaber()))
								.collect(Collectors.toList());
						return ResponseFormat.retParam(1, 200, collect);
					}
				}else {
					return ResponseFormat.retParam(1, 200, pageInfo.getTlist());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200, Collections.emptyList());
	}

}
