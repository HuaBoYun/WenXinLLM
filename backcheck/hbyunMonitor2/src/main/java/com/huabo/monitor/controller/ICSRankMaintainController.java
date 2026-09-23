//package com.huabo.monitor.controller;
//
//import com.hbfk.entity.DealUserToken;
//import com.hbfk.entity.TblStaffUtil;
//import com.hbfk.util.JsonBean;
//import com.hbfk.util.ResponseFormat;
//import com.huabo.monitor.entity.TblAssesslevel;
//import com.huabo.monitor.service.TblAssesslevelService;
//import com.huabo.monitor.util.ConstClass;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.tags.TagOperation;
//import io.swagger.v3.oas.annotations.tags.TagParam;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * ICS: Internal Control Setting
// * rank maintain controller
// * Created: 2022/12/1
// */
//@RestController
//@Slf4j
//@Tag(name="内控设置-等级维护",description="内控设置-等级维护")
//@RequestMapping(value = "/nbkz")
//public class ICSRankMaintainController {
//    @Resource
//    public TblAssesslevelService tblAssesslevelService;
//
//
//    @Operation(summary = "等级维护-列表")
//    @RequestMapping(value = "/gzdg/level_list")
//    public JsonBean gzdg_level_list(@RequestHeader("token") String token) throws Exception {
//        if (!ConstClass.checkToken(token)) {
//            return ConstClass.tokenFailure();
//        }
//
//        Map<String, Object> mv = new HashMap<>();
//        TblStaffUtil userToken = DealUserToken.parseUserToken(token);
//
//        List<TblAssesslevel> list = this.tblAssesslevelService.findAll(userToken.getCurrentOrg().getOrgid().toString());
//        mv.put("list", list);
//        return ResponseFormat.retParam(1, 200, mv);
//    }
//
//
//    @Operation(summary = "等级维护-添加")
//    @GetMapping(value = "/gzdg/level_add")
//    public JsonBean gzdg_levle_add(@RequestHeader("token") String token) throws Exception {
//        if (!ConstClass.checkToken(token)) {
//            return ConstClass.tokenFailure();
//        }
//
//        Map<String, Object> mv = new HashMap<>();
//        TblStaffUtil userToken = DealUserToken.parseUserToken(token);
//
//        List<TblAssesslevel> list = this.tblAssesslevelService.findAll(userToken.getCurrentOrg().getOrgid().toString());
//        mv.put("list", list);
//        if (list.size() > 0) {
//            mv.put("no", "yes");
//            mv.put("lower", list.get(list.size() - 1).getLevellower().doubleValue() - 1);
//            if (list.get(list.size() - 1).getLevellower().doubleValue() - 1 <= 0) {
//                mv.put("no", "no");
//            }
//        } else {
//            mv.put("lower", "100");
//            mv.put("no", "yes");
//        }
//        return ResponseFormat.retParam(1, 200, mv);
//    }
//
//    @Operation(summary = "等级维护-保存")
//    @PostMapping(value = "/gzdg/level_add_save", produces = "application/json; charset=utf-8")
//    public JsonBean gzdg_level_add_save(
//            @Parameter(name = "assesslevel", description = "TblAssesslevel实体类") @RequestBody TblAssesslevel assesslevel,
//            @RequestHeader("token") String token
//    ) throws Exception {
//        if (!ConstClass.checkToken(token)) {
//            return ConstClass.tokenFailure();
//        }
//        TblStaffUtil userToken = DealUserToken.parseUserToken(token);
//
//
//        if (assesslevel != null) {
//            assesslevel.setTblcomany(userToken.getCurrentOrg().getOrgid().toString());
//            BigDecimal levelupper = assesslevel.getLevelupper();
//            BigDecimal levellower = assesslevel.getLevellower();
//            assesslevel.setModifieddate(LocalDateTime.now());
//            if (levelupper.doubleValue() > levellower.doubleValue()) {
//                this.tblAssesslevelService.add(assesslevel);
//                return ResponseFormat.retParam(1, 200, "success");
//            } else {
//                return ResponseFormat.retParam(0, 202, "级别数据有错误");
//            }
//
//        }
//        return ResponseFormat.retParam(0, 202, "数据不完整");
//    }
//
//    @Operation(summary = "等级维护-修改")
//    @GetMapping(value = "/gzdg/level_modify")
//    public JsonBean gzdg_level_modify(@RequestHeader("token") String token) throws Exception {
//        Map<String, Object> mv = new HashMap<>();
//        if (!ConstClass.checkToken(token)) {
//            return ConstClass.tokenFailure();
//        }
//        TblStaffUtil userToken = DealUserToken.parseUserToken(token);
//
//        List<TblAssesslevel> list = this.tblAssesslevelService.findAll(userToken.getCurrentOrg().getOrgid().toString());
//        mv.put("viewName","nbkz/gzdg/level_modi");
//        mv.put("list", list);
//        mv.put("count", list.size());
//        return ResponseFormat.retParam(1, 200, mv);
//    }
//
//}
