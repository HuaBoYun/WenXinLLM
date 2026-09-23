package com.huabo.legal.startup.bbs.rest;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.startup.bbs.domain.Bbs;
import com.huabo.legal.startup.bbs.service.BbsService;
import com.huabo.legal.vo.param.BbsAdminParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 管理留言Api
 *
 * @author zhuhuix
 * @date 2022-06-09
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/bbs")
@Tag(name="管理留言接口",description="管理留言接口")
public class BbsAdminController {

	private final BbsService bbsService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "回复留言")
	@PostMapping
	public ResponseEntity<Object> saveOrUpdate(@RequestBody Bbs bbs) {
		return ResponseEntity.ok(bbsService.saveOrUpdate(bbs));

	}

	@Operation(summary = "批量删除留言")
	@DeleteMapping
	public ResponseEntity<Object> deleteBbsInfo(@RequestBody BbsAdminParam ids) {
		return ResponseEntity.ok(bbsService.delete(ids));
	}

	@Operation(summary = "根据id获取留言信息")
	@GetMapping("/{id}")
	public JsonBean getBbsInfo(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null || loginStaff.getLinkOrg() == null) {
			throw new ServiceException(401, 20006);
		}
		int staffId = Integer.parseInt(loginStaff.getStaffid().toString());
		Bbs bbs = bbsService.findById(id, staffId);
		System.out.println("根据id获取留言信息："+bbs);
		return ResponseFormat.retParam(200, 200, bbs);
	}

}
