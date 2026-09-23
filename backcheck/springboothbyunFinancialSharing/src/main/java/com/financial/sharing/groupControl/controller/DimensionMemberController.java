 package com.financial.sharing.groupControl.controller;

 import com.financial.sharing.groupControl.dto.DimensionMemberQueryParam;
 import com.financial.sharing.groupControl.entity.TblDimensionMember;
 import com.financial.sharing.groupControl.service.DimensionMemberService;
 import com.hbfk.util.user.UserProvider;
 import com.financial.sharing.util.JsonMapper;
 import com.financial.sharing.util.UserUtils;
 import io.swagger.v3.oas.annotations.Operation;
 import io.swagger.v3.oas.annotations.tags.Tag;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.util.List;
 import java.util.Map;
 
 @Slf4j
 @RestController
 @RequestMapping("/groupControl/dimensionMember")
 @Tag(name = "维度成员管理")
 public class DimensionMemberController {
 
     @Autowired
     private DimensionMemberService dimensionMemberService;
 
     @PostMapping("/getTree")
     @Operation(summary = "查询成员树")
     public String getTree(@RequestBody DimensionMemberQueryParam param) {
         try {
             if (UserUtils.getUser() == null) {
                 return JsonMapper.buildFalseJson("用户未登录");
             }
             List<TblDimensionMember> tree = dimensionMemberService.getTree(param);
             return JsonMapper.buildSuccessJson(tree);
         } catch (Exception e) {
             log.error("查询成员树失败", e);
             return JsonMapper.buildFalseJson("查询成员树失败: " + e.getMessage());
         }
     }
 
     @PostMapping("/detail")
     @Operation(summary = "查询成员详情")
     public String detail(@RequestBody Map<String, String> params) {
         try {
             if (UserUtils.getUser() == null) {
                 return JsonMapper.buildFalseJson("用户未登录");
             }
             String memberId = params.get("memberId");
             if (memberId == null || memberId.trim().isEmpty()) {
                 return JsonMapper.buildFalseJson("成员ID不能为空");
             }
             TblDimensionMember member = dimensionMemberService.getDetail(memberId);
             if (member == null) {
                 return JsonMapper.buildFalseJson("成员不存在");
             }
             return JsonMapper.buildSuccessJson(member);
         } catch (Exception e) {
             log.error("查询成员详情失败", e);
             return JsonMapper.buildFalseJson("查询成员详情失败: " + e.getMessage());
         }
     }
 
     @PostMapping("/save")
     @Operation(summary = "保存成员")
     public String save(@RequestBody TblDimensionMember member) {
         try {
             if (UserUtils.getUser() == null) {
                 return JsonMapper.buildFalseJson("用户未登录");
             }
             if (member.getDimensionId() == null || member.getDimensionId().trim().isEmpty()) {
                 return JsonMapper.buildFalseJson("维度ID不能为空");
             }
             if (member.getMemberCode() == null || member.getMemberCode().trim().isEmpty()) {
                 return JsonMapper.buildFalseJson("成员编码不能为空");
             }
             if (member.getMemberName() == null || member.getMemberName().trim().isEmpty()) {
                 return JsonMapper.buildFalseJson("成员名称不能为空");
             }
             boolean result = dimensionMemberService.save(member);
             if (result) {
                 return JsonMapper.buildSuccessJson("保存成功");
             } else {
                 return JsonMapper.buildFalseJson("保存失败");
             }
         } catch (Exception e) {
             log.error("保存成员失败", e);
             return JsonMapper.buildFalseJson("保存成员失败: " + e.getMessage());
         }
     }
 
     @PostMapping("/delete")
     @Operation(summary = "删除成员")
     public String delete(@RequestBody Map<String, String> params) {
         try {
             if (UserUtils.getUser() == null) {
                 return JsonMapper.buildFalseJson("用户未登录");
             }
             String memberId = params.get("memberId");
             if (memberId == null || memberId.trim().isEmpty()) {
                 return JsonMapper.buildFalseJson("成员ID不能为空");
             }
             boolean result = dimensionMemberService.delete(memberId);
             if (result) {
                 return JsonMapper.buildSuccessJson("删除成功");
             } else {
                 return JsonMapper.buildFalseJson("删除失败");
             }
         } catch (Exception e) {
             log.error("删除成员失败", e);
             return JsonMapper.buildFalseJson("删除成员失败: " + e.getMessage());
         }
     }
 
     @PostMapping("/batchDelete")
     @Operation(summary = "批量删除成员")
     public String batchDelete(@RequestBody List<String> memberIds) {
         try {
             if (UserUtils.getUser() == null) {
                 return JsonMapper.buildFalseJson("用户未登录");
             }
             if (memberIds == null || memberIds.isEmpty()) {
                 return JsonMapper.buildFalseJson("成员ID列表不能为空");
             }
             boolean result = dimensionMemberService.batchDelete(memberIds);
             if (result) {
                 return JsonMapper.buildSuccessJson("批量删除成功");
             } else {
                 return JsonMapper.buildFalseJson("批量删除失败");
             }
         } catch (Exception e) {
             log.error("批量删除成员失败", e);
             return JsonMapper.buildFalseJson("批量删除成员失败: " + e.getMessage());
         }
     }
 
     @PostMapping("/move")
     @Operation(summary = "移动成员")
     public String move(@RequestBody Map<String, Object> params) {
         try {
             if (UserUtils.getUser() == null) {
                 return JsonMapper.buildFalseJson("用户未登录");
             }
             String memberId = (String) params.get("memberId");
             String targetParentId = (String) params.get("targetParentId");
             Integer sortNo = params.get("sortNo") != null ? Integer.parseInt(params.get("sortNo").toString()) : 0;
             if (memberId == null || memberId.trim().isEmpty()) {
                 return JsonMapper.buildFalseJson("成员ID不能为空");
             }
             boolean result = dimensionMemberService.move(memberId, targetParentId, sortNo);
             if (result) {
                 return JsonMapper.buildSuccessJson("移动成功");
             } else {
                 return JsonMapper.buildFalseJson("移动失败");
             }
         } catch (Exception e) {
             log.error("移动成员失败", e);
             return JsonMapper.buildFalseJson("移动成员失败: " + e.getMessage());
         }
     }
 
     @PostMapping("/updateStatus")
     @Operation(summary = "更新成员状态")
     public String updateStatus(@RequestBody Map<String, String> params) {
         try {
             if (UserUtils.getUser() == null) {
                 return JsonMapper.buildFalseJson("用户未登录");
             }
             String memberId = params.get("memberId");
             String status = params.get("status");
             if (memberId == null || memberId.trim().isEmpty()) {
                 return JsonMapper.buildFalseJson("成员ID不能为空");
             }
             if (status == null || status.trim().isEmpty()) {
                 return JsonMapper.buildFalseJson("状态不能为空");
             }
             boolean result = dimensionMemberService.updateStatus(memberId, status);
             if (result) {
                 return JsonMapper.buildSuccessJson("更新状态成功");
             } else {
                 return JsonMapper.buildFalseJson("更新状态失败");
             }
         } catch (Exception e) {
             log.error("更新成员状态失败", e);
             return JsonMapper.buildFalseJson("更新成员状态失败: " + e.getMessage());
         }
     }
 }