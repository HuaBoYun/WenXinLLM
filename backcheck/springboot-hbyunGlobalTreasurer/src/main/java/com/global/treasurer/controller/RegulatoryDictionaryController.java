package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryDictionary;
import com.global.treasurer.service.RegulatoryDictionaryService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 监管数据字典Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Controller
@RequestMapping({"/regulatory/dictionary", "/globalTreasurer/regulatory/dictionary"})
@Api(tags = "监管数据字典管理")
public class RegulatoryDictionaryController {
    private static final Logger log = LoggerFactory.getLogger(RegulatoryDictionaryController.class);

    @Resource
    private RegulatoryDictionaryService dictionaryService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询数据字典列表")
    public String getDictionaryList(@RequestParam(required = false) String dictType,
                                    @RequestParam(required = false) String dictName,
                                    @RequestParam(required = false) Integer isEnabled,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("dictType", dictType);
            params.put("dictName", dictName);
            params.put("isEnabled", isEnabled);
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);

            PageInfo<TblRegulatoryDictionary> pageInfo = dictionaryService.getDictionaryList(params);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询数据字典列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{dictionaryId}")
    @ResponseBody
    @ApiOperation("根据ID获取数据字典详情")
    public String getDictionaryById(@PathVariable String dictionaryId,
                                    @RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblRegulatoryDictionary dictionary = dictionaryService.getDictionaryById(dictionaryId);
            return new JsonBean(1, "成功", dictionary).toJson();
        } catch (Exception e) {
            log.error("获取数据字典详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增数据字典")
    public String addDictionary(@FlexibleRequestBody TblRegulatoryDictionary dictionary,
                                @RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            dictionary.setDictionaryId(null);
            TblRegulatoryDictionary saved = dictionaryService.saveDictionary(dictionary);
            return new JsonBean(1, "新增成功", saved).toJson();
        } catch (Exception e) {
            log.error("新增数据字典失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改数据字典")
    public String updateDictionary(@FlexibleRequestBody TblRegulatoryDictionary dictionary,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            if (dictionary.getDictionaryId() == null || dictionary.getDictionaryId().isEmpty()) {
                return new JsonBean(0, "字典ID不能为空", null).toJson();
            }

            TblRegulatoryDictionary saved = dictionaryService.saveDictionary(dictionary);
            return new JsonBean(1, "修改成功", saved).toJson();
        } catch (Exception e) {
            log.error("修改数据字典失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{dictionaryIds}")
    @ResponseBody
    @ApiOperation("删除数据字典")
    public String deleteDictionary(@PathVariable String dictionaryIds,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            String[] ids = dictionaryIds.split(",");
            if (ids.length == 1) {
                dictionaryService.deleteDictionary(ids[0]);
            } else {
                dictionaryService.batchDeleteDictionaries(Arrays.asList(ids));
            }
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除数据字典失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }
}

