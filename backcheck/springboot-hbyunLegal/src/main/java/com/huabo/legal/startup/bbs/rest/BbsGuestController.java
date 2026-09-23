package com.huabo.legal.startup.bbs.rest;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.constant.PersonnelType;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.startup.bbs.domain.Bbs;
import com.huabo.legal.startup.bbs.service.BbsService;
import com.huabo.legal.startup.bbs.service.dto.BbsQueryDto;
import com.huabo.legal.vo.result.StaffResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 访客留言Api
 *
 * @author zhuhuix
 * @date 2022-06-09
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/guest/bbs")
@Tag(name="访客留言接口",description="访客留言接口")
public class BbsGuestController {

	private final BbsService bbsService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "新增留言")
	@PostMapping
	public ResponseEntity<Object> create(@RequestBody Bbs bbs) {

		return ResponseEntity.ok(bbsService.create(bbs));

	}

	@Operation(summary = "根据条件查询返回留言分页列表")
	@PostMapping("/page")
	public ResponseEntity<Object> getBbsPage(@RequestHeader("token") String token, @RequestBody BbsQueryDto bbsQueryDto) throws Exception {
		boolean flags = false;
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null || loginStaff.getLinkOrg() == null || loginStaff.getLinkOrg().getOrgid() == null) {
			throw new ServiceException(401, 20006);
		}
		Long staffId = loginStaff.getStaffid().longValue();
		Long orgid = loginStaff.getLinkOrg().getOrgid().longValue();
		StaffResult userInfoExam = tblStaffOracleService.getUserInfoExam(staffId, orgid, PersonnelType.BBS_PERSONNEL);
		if (userInfoExam != null) {
			flags = true;
		}
		return ResponseEntity.ok(bbsService.page(bbsQueryDto, flags));
	}

	@Operation(summary = "回复列表")
	@PostMapping("/reply/getList/{id}")
	public JsonBean getReplyList(@PathVariable("id") Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = bbsService.getReplyList(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("回复列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}
}
