package com.financial.sharing.controller;

import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.financial.sharing.util.JsonMapper;
import com.financial.sharing.service.AccountSubjectService;
import com.financial.sharing.vo.param.AccountSubjectQueryParam;
import com.financial.sharing.vo.param.AccountSubjectSaveParam;
import com.financial.sharing.vo.result.AccountSubjectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import com.github.pagehelper.PageInfo;

/**
 * 会计科目管理控制器
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "会计科目管理")
@RestController
@RequestMapping("/common/subject")
@CrossOrigin
public class AccountSubjectController {

    @Resource
    private AccountSubjectService accountSubjectService;

    @Resource
    private UserProvider userProvider;

    @ApiOperation("分页查询会计科目列表")
    @PostMapping(value = "/getList", consumes = "application/x-www-form-urlencoded")
    public String getAccountSubjectList(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam(required = false) Integer pageNumber,
                                      @RequestParam(required = false) Integer pageSize,
                                      @RequestParam(required = false) Long tenantId,
                                      @RequestParam(required = false) Long bookId,
                                      @RequestParam(required = false) String subjectCode,
                                      @RequestParam(required = false) String subjectName,
                                      @RequestParam(required = false) Integer subjectType,
                                      @RequestParam(required = false) Integer subjectLevel,
                                      @RequestParam(required = false) Integer isEnabled) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            // 构建查询参数
            AccountSubjectQueryParam queryParam = new AccountSubjectQueryParam();
            queryParam.setPageNum(pageNumber != null ? pageNumber : 1);
            queryParam.setPageSize(pageSize != null ? pageSize : 10);
            queryParam.setSubjectCode(subjectCode);
            queryParam.setSubjectName(subjectName);
            queryParam.setSubjectType(subjectType);
            queryParam.setSubjectLevel(subjectLevel);
            queryParam.setIsEnabled(isEnabled);

            // 添加调试日志
            log.info("构建的查询参数: pageNumber={}, pageSize={}, subjectCode={}, subjectName={}, subjectType={}",
                queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getSubjectCode(), queryParam.getSubjectName(), queryParam.getSubjectType());

            // 设置租户和账簿信息
            queryParam.setTenantId(tenantId != null ? tenantId : 1L);
            queryParam.setBookId(bookId != null ? bookId : 1L);

            // 查询数据
            PageInfo<AccountSubjectVO> result = accountSubjectService.getAccountSubjectPage(queryParam);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getList());
            data.put("totalRecord", result.getTotal());
            data.put("pageNumber", result.getPageNum());
            data.put("pageSize", result.getPageSize());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return json.toString();
        } catch (Exception e) {
            log.error("查询会计科目列表失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return json.toString();
        }
    }

    @ApiOperation("新增会计科目")
    @PostMapping(value = "/create", consumes = "application/x-www-form-urlencoded")
    public String createAccountSubject(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(required = false) String subjectCode,
                                     @RequestParam(required = false) String subjectName,
                                     @RequestParam(required = false) Integer subjectType,
                                     @RequestParam(required = false) Integer subjectLevel,
                                     @RequestParam(required = false) String parentSubjectId,
                                     @RequestParam(required = false) Integer balanceDirection,
                                     @RequestParam(required = false) String description,
                                     @RequestParam(required = false) Integer isLeaf,
                                     @RequestParam(required = false) Integer isEnabled) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            // 构建保存参数
            AccountSubjectSaveParam saveParam = new AccountSubjectSaveParam();
            saveParam.setSubjectCode(subjectCode);
            saveParam.setSubjectName(subjectName);
            saveParam.setSubjectType(subjectType);
            if (parentSubjectId != null && !parentSubjectId.isEmpty()) {
                saveParam.setParentSubjectId(Long.valueOf(parentSubjectId));
            }
            saveParam.setBalanceDirection(balanceDirection);
            saveParam.setIsLeaf(isLeaf);
            saveParam.setIsEnabled(isEnabled);
            saveParam.setTenantId(1L); // 测试环境使用固定值
            saveParam.setBookId(1L);   // 测试环境使用固定值
            saveParam.setCreator(1L);  // 测试环境使用固定值
            saveParam.setUpdater(1L); // 测试环境使用固定值

            // 保存数据
            AccountSubjectVO result = accountSubjectService.saveAccountSubject(saveParam);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return json.toString();
        } catch (Exception e) {
            log.error("新增会计科目失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("创建失败: " + e.getMessage());
            json.setData(null);
            return json.toString();
        }
    }

    @ApiOperation("获取科目树")
    @GetMapping("/tree")
    public String getAccountSubjectTree(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam(required = false) String level) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            // 获取科目树数据
            Long tenantId = 1L; // 测试环境使用固定值
            Long bookId = 1L;   // 测试环境使用固定值

            List<AccountSubjectVO> subjects = accountSubjectService.getAccountSubjectTree(bookId, tenantId);

            // 转换为树形结构
            List<Map<String, Object>> treeData = buildSubjectTree(subjects);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(treeData);
            // 使用JsonMapper支持LocalDateTime序列化
            return com.financial.sharing.util.JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取科目树失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return com.financial.sharing.util.JsonMapper.toJson(json);
        }
    }

    @ApiOperation("保存或更新会计科目")
    @PostMapping(value = "/saveOrUpdate", consumes = "application/x-www-form-urlencoded")
    public String saveOrUpdateAccountSubject(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestParam(required = false) String subjectId,
                                           @RequestParam(required = false) String subjectCode,
                                           @RequestParam(required = false) String subjectName,
                                           @RequestParam(required = false) Integer subjectType,
                                           @RequestParam(required = false) Integer subjectLevel,
                                           @RequestParam(required = false) String parentSubjectId,
                                           @RequestParam(required = false) Integer balanceDirection,
                                           @RequestParam(required = false) String description,
                                           @RequestParam(required = false) Integer isLeaf,
                                           @RequestParam(required = false) Integer isEnabled) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            // 构建保存参数
            AccountSubjectSaveParam saveParam = new AccountSubjectSaveParam();
            saveParam.setSubjectCode(subjectCode);
            saveParam.setSubjectName(subjectName);
            saveParam.setSubjectType(subjectType);
            if (parentSubjectId != null && !parentSubjectId.isEmpty()) {
                saveParam.setParentSubjectId(Long.valueOf(parentSubjectId));
            }
            saveParam.setBalanceDirection(balanceDirection);
            saveParam.setIsLeaf(isLeaf);
            saveParam.setIsEnabled(isEnabled);
            saveParam.setTenantId(1L); // 测试环境使用固定值
            saveParam.setBookId(1L);   // 测试环境使用固定值
            saveParam.setUpdater(1L); // 测试环境使用固定值

            AccountSubjectVO result;
            if (subjectId != null && !subjectId.isEmpty()) {
                // 更新操作
                saveParam.setSubjectId(Long.valueOf(subjectId));
                result = accountSubjectService.updateAccountSubject(saveParam);
            } else {
                // 新增操作
                saveParam.setCreator(1L); // 测试环境使用固定值
                result = accountSubjectService.saveAccountSubject(saveParam);
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return json.toString();
        } catch (Exception e) {
            log.error("保存或更新会计科目失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("操作失败: " + e.getMessage());
            json.setData(null);
            return json.toString();
        }
    }

    @ApiOperation("删除会计科目")
    @DeleteMapping("/{subjectId}")
    public String deleteAccountSubject(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @PathVariable String subjectId) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            Long id = new Long(subjectId);
            boolean result = accountSubjectService.deleteAccountSubject(id);

            Map<String, Object> data = new HashMap<>();
            data.put("subjectId", subjectId);
            data.put("success", result);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return json.toString();
        } catch (Exception e) {
            log.error("删除会计科目失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("删除失败: " + e.getMessage());
            json.setData(null);
            return json.toString();
        }
    }

    @ApiOperation("科目余额查询")
    @PostMapping(value = "/balance", consumes = "application/x-www-form-urlencoded")
    public String getSubjectBalance(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestParam(required = false) String subjectId,
                                  @RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            // TODO: 科目余额查询需要对接余额相关表，暂时返回空数据
            // 实际应该从科目余额表 TBL_ACCOUNT_SUBJECT_BALANCE 查询数据
            List<Map<String, Object>> balanceList = new ArrayList<>();

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(balanceList);
            return json.toString();
        } catch (Exception e) {
            log.error("科目余额查询失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return json.toString();
        }
    }

    @ApiOperation("批量删除会计科目")
    @DeleteMapping("/batch")
    public String batchDeleteAccountSubjects(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @RequestParam String subjectIds) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            // 将逗号分隔的字符串转换为List<Long>
            List<Long> subjectIdList = java.util.Arrays.stream(subjectIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(java.util.stream.Collectors.toList());

            boolean result = accountSubjectService.batchDeleteAccountSubjects(subjectIdList);

            Map<String, Object> data = new HashMap<>();
            data.put("success", result);
            data.put("deletedCount", subjectIdList.size());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return json.toString();
        } catch (Exception e) {
            log.error("批量删除会计科目失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("批量删除失败: " + e.getMessage());
            json.setData(null);
            return json.toString();
        }
    }

    @ApiOperation("启用/禁用会计科目")
    @PutMapping("/{subjectId}/status")
    public String updateAccountSubjectStatus(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @PathVariable Long subjectId,
                                           @RequestParam Integer isEnabled) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            boolean result = accountSubjectService.updateAccountSubjectStatus(subjectId, isEnabled);

            Map<String, Object> data = new HashMap<>();
            data.put("subjectId", subjectId);
            data.put("isEnabled", isEnabled);
            data.put("success", result);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return json.toString();
        } catch (Exception e) {
            log.error("更新会计科目状态失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("更新状态失败: " + e.getMessage());
            json.setData(null);
            return json.toString();
        }
    }

    @ApiOperation("批量启用/禁用会计科目")
    @PutMapping("/batch/status")
    public String batchUpdateAccountSubjectStatus(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @RequestParam Integer isEnabled,
                                               @RequestParam String subjectIds) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            // 将逗号分隔的字符串转换为List<Long>
            List<Long> subjectIdList = java.util.Arrays.stream(subjectIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(java.util.stream.Collectors.toList());

            boolean result = accountSubjectService.batchUpdateAccountSubjectStatus(subjectIdList, isEnabled);

            Map<String, Object> data = new HashMap<>();
            data.put("success", result);
            data.put("updatedCount", subjectIdList.size());
            data.put("isEnabled", isEnabled);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return json.toString();
        } catch (Exception e) {
            log.error("批量更新会计科目状态失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("批量更新状态失败: " + e.getMessage());
            json.setData(null);
            return json.toString();
        }
    }

    @ApiOperation("检查科目编码是否存在")
    @GetMapping("/check-code")
    public String checkAccountSubjectCodeExists(HttpServletRequest request,
                                             HttpServletResponse response,
                                             @RequestParam String subjectCode,
                                             @RequestParam Long bookId,
                                             @RequestParam Long tenantId,
                                             @RequestParam(required = false) Long excludeId) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            boolean exists = accountSubjectService.checkAccountSubjectCodeExists(subjectCode, bookId, tenantId, excludeId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(exists);
            return json.toString();
        } catch (Exception e) {
            log.error("检查科目编码是否存在失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("检查失败: " + e.getMessage());
            json.setData(null);
            return json.toString();
        }
    }

    @ApiOperation("根据ID查询会计科目详情")
    @GetMapping("/{subjectId}")
    public String getAccountSubjectById(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @PathVariable Long subjectId) {
        try {
            // 权限验证 - 测试环境暂时注释
            /*TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }*/

            AccountSubjectVO subject = accountSubjectService.getAccountSubjectById(subjectId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(subject);
            return json.toString();
        } catch (Exception e) {
            log.error("查询会计科目详情失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return json.toString();
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 构建科目树形结构
     */
    private List<Map<String, Object>> buildSubjectTree(List<AccountSubjectVO> subjects) {
        List<Map<String, Object>> tree = new ArrayList<>();

        // 按科目编码分组
        Map<Long, Map<String, Object>> nodeMap = new HashMap<>();
        List<Map<String, Object>> rootNodes = new ArrayList<>();

        // 创建所有节点
        for (AccountSubjectVO subject : subjects) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", subject.getSubjectId().toString());
            node.put("name", subject.getSubjectCode() + " " + subject.getSubjectName());
            node.put("subjectCode", subject.getSubjectCode());
            node.put("subjectName", subject.getSubjectName());
            node.put("subjectType", subject.getSubjectType());
            node.put("balanceDirection", subject.getBalanceDirection());
            node.put("isLeaf", subject.getIsLeaf());
            node.put("level", subject.getSubjectLevel());
            node.put("parentId", subject.getParentSubjectId() != null ? subject.getParentSubjectId().toString() : null);
            node.put("children", new ArrayList<>());

            nodeMap.put(subject.getSubjectId(), node);

            if (subject.getParentSubjectId() == null) {
                rootNodes.add(node);
            }
        }

        // 建立父子关系
        for (AccountSubjectVO subject : subjects) {
            if (subject.getParentSubjectId() != null) {
                Map<String, Object> parent = nodeMap.get(subject.getParentSubjectId());
                Map<String, Object> child = nodeMap.get(subject.getSubjectId());
                if (parent != null && child != null) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> childrenList = (List<Map<String, Object>>) parent.get("children");
                childrenList.add(child);
                }
            }
        }

        return rootNodes;
    }

}