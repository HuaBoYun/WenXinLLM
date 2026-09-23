package com.huabo.audit.controller;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

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
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblWgzzShbg;
import com.huabo.audit.oracle.entity.TblWgzzWghs;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblWgzzWghsService;
import com.huabo.audit.util.R;
import com.huabo.audit.util.SnowflakeIdWorker;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 违规追责控制器
 * <p>提供违规追责的项目团队管理、列表查询等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="违规追责",description="违规追责")
@RequestMapping(value = "/wghs/projectTeam")
public class TblWghsController {

    @Autowired
    TblWgzzWghsService tblWgzzWghsService;
    
    @Autowired
    TblAttachmentService tblAttachmentService;
    
    @Resource
    private UserProvider userProvider;
    
    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);

    @OperationLog(
            success = "违规核实列表",
            busType = "整改追责",
            fail = "违规核实列表",
            operationType = OperationType.SELECT,
            subType = "违规追责——违规核查——获取违规核实列表页相关信息"
    )
    @GetMapping("/wghsList")
    @Operation(summary = "违规核实列表")
    public JsonBean getByWghslist(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  @Parameter(name="clueNaber",description="clueNaber",required=false)@RequestParam(value="clueNaber",required=false) String clueNaber,
                                  @Parameter(name="verifycontent",description="verifycontent",required=false)@RequestParam(value="verifycontent",required=false) String verifycontent){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.getByWghsList(token,pageNumber,pageSize,clueNaber,verifycontent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规核实列表记录详情",
            busType = "整改追责",
            fail = "违规核实列表记录详情",
            operationType = OperationType.SELECT,
            subType = "违规追责——违规核查——获取违规核实列表记录详细信息"
    )
    @GetMapping("/wghsXQList")
    @Operation(summary = "违规核实列表详情")
    public JsonBean getByWghslist(@Parameter(description="token")@RequestHeader("token")String token,
                                  @Parameter(description="id")@RequestParam("id")BigDecimal id){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.getByWghsXQList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规核实新增/修改",
            busType = "整改追责",
            fail = "违规核实新增/修改",
            operationType = OperationType.ADD,
            subType = "违规追责——违规核查——执行违规核实新增/修改操作"
    )
    @RequestMapping(value = "/wghsSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "违规核实新增/修改")
    public JsonBean getByWghsSave(	@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                      @Parameter(name = "tblWgzzWghs", description = "实体", required = false) TblWgzzWghs tblWgzzWghs)
                                 {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.getByaddlist(token,tblWgzzWghs,tblWgzzWghs.getAttIds());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规核实删除",
            busType = "整改追责",
            fail = "违规核实删除",
            operationType = OperationType.DELETE,
            subType = "违规追责——违规核查——违规核实删除记录"
    )
    @PostMapping("/wghsRemove")
    @Operation(summary = "违规核实删除")
    public JsonBean getByWghsremove(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id
                             )
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.getByremoveList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规核实附件删除",
            busType = "整改追责",
            fail = "违规核实附件删除",
            operationType = OperationType.DELETE,
            subType = "违规追责——违规核查——违规核实删除附件"
    )
    @PostMapping("/wghsRemovefilue")
    @Operation(summary = "违规核实附件删除")
    public R getByWghcdelete(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attId") @RequestParam("attId")String attId) throws Exception {


        return this.tblWgzzWghsService.deleteAttInfoByAttId(token,attId);
    }

  //违规报告
  @OperationLog(
          success = "违规报告列表",
          busType = "整改追责",
          fail = "违规报告列表",
          operationType = OperationType.SELECT,
          subType = "违规追责——违规核查——获取违规报告列表相关信息"
  )
    @GetMapping("/shbgList")
    @Operation(summary = "违规报告列表")
    public JsonBean getByWghcList(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  @Parameter(name="clueNaber",description="clueNaber",required=false)@RequestParam(value="clueNaber",required=false) String clueNaber,
                                  @Parameter(name="verifycontent",description="verifycontent",required=false)@RequestParam(value="verifycontent",required=false) String verifycontent){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.getBySHBGList(token,pageNumber,pageSize,clueNaber,verifycontent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规报告列表详情",
            busType = "整改追责",
            fail = "违规报告列表详情",
            operationType = OperationType.SELECT,
            subType = "违规追责——违规核查——获取违规报告列表指定记录详细信息"
    )
    @GetMapping("/shbgXQList")
    @Operation(summary = "违规报告列表详情")
    public JsonBean getBySHBGXQList(@Parameter(description="token")@RequestHeader("token")String token,
                                  @Parameter(description="id")@RequestParam("id")BigDecimal id){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.getBySHBGXQList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规报告列表新增修改",
            busType = "整改追责",
            fail = "违规报告列表新增修改",
            operationType = OperationType.ADD,
            subType = "违规追责——违规核查——违规报告列表新增修改记录"
    )
    @RequestMapping(value = "/shbgSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "违规报告新增/修改")
    public JsonBean getBywghcaddlist(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "tblWgzzShbg", description = "实体", required = false) TblWgzzShbg tblWgzzShbg)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.getBySHBGaddlist(token, tblWgzzShbg,tblWgzzShbg.getAttIds());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规报告列表删除",
            busType = "整改追责",
            fail = "违规报告列表删除",
            operationType = OperationType.DELETE,
            subType = "违规追责——违规核查——违规报告列表删除指定记录信息"
    )
    @PostMapping("/shbgRemove")
    @Operation(summary = "违规报告删除")
    public JsonBean getBywghcremoveList(@Parameter(description="token")@RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id
    )
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.getBySHBGremoveList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    @OperationLog(
            success = "违规报告附件删除",
            busType = "整改追责",
            fail = "违规报告附件删除",
            operationType = OperationType.DELETE,
            subType = "违规追责——违规报告删除指定附件"
    )
    @PostMapping("/shbgRemovefilue")
    @Operation(summary = "违规报告附件删除")
    public R removeAttInfoByAttId(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attId") @RequestParam("attId")String attId) throws Exception {

        return this.tblWgzzWghsService.removeAttInfoByAttId(token,attId);
    }

    @OperationLog(
            success = "上传会议文件列表",
            busType = "整改追责",
            fail = "上传会议文件列表",
            operationType = OperationType.UPLOAD,
            subType = "违规追责——初步核实上传会议文件列表"
    )
    @GetMapping("/gethyfilelist")
    @Operation(summary = "初步核实-上传会议文件列表")
    public JsonBean gethyfilelist(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="id") @RequestParam("id")BigDecimal id) throws Exception {
    	JsonBean jsonBean =null;
        try {
        	jsonBean=tblWgzzWghsService.getByidyhList(token, id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0, "获取失败", null);
		}
        return jsonBean;
    }

    @OperationLog(
            success = "删除会议文件",
            busType = "整改追责",
            fail = "删除会议文件",
            operationType = OperationType.DELETE,
            subType = "违规追责——初步核实删除会议文件"
    )
    @PostMapping("/deleteatthyfile")
    @Operation(summary = "初步核实-删除会议文件")
    public JsonBean deleteatthyfile(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attid") @RequestParam("attid")BigDecimal attid) throws Exception {
    	JsonBean jsonBean =null;
        try {
        	jsonBean=tblWgzzWghsService.delattbyid(attid, token);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0, "删除失败", null);
		}
        return jsonBean;
    }

    @OperationLog(
            success = "上传会议文件",
            busType = "整改追责",
            fail = "上传会议文件",
            operationType = OperationType.UPLOAD,
            subType = "违规追责——初步核实上传会议文件"
    )
    @RequestMapping(value="/importyhFile",method=RequestMethod.POST,produces = "application/html; charset=utf-8")
	@Operation(summary = "初步核实-上传会议文件")
	public String importyhFile(@Parameter(name = "file", description = "file", required = true)MultipartFile file,
										@Parameter(description = "id", required = true) @RequestParam("id")BigDecimal id,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			  return ResponseFormat.retParam(0, 20006, null).toString();
		}
		 String attPath = "";
		 JsonBean jsonBean =null;
		try {
			TblAttachment tblAttachmentEntity = new TblAttachment();
			 InputStream inputStream = file.getInputStream();
             long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
             String fileName = new String(file.getOriginalFilename().getBytes()); //重新编码
             String name=fileName.substring(fileName.lastIndexOf("."), fileName.length());
             attPath = FtpUtil.uploadFilePath(imageName+name+"", inputStream);
             if (StrUtil.isEmpty(attPath)) {
            	 return ResponseFormat.retParam(0, "上传会议文件失败", null).toString();
             }
             //tblAttachmentEntity.setAttid(new BigDecimal(snowflakeIdWorker.nextId()));
             tblAttachmentEntity.setAttpath(attPath + imageName+name);
             tblAttachmentEntity.setAttsize(file.getSize() / 1024);
             tblAttachmentEntity.setUploadtime(new Date());
             tblAttachmentEntity.setUploader(user.getRealname());
             tblAttachmentEntity.setAttname(fileName);
             tblAttachmentService.saveEntity(tblAttachmentEntity);
             jsonBean = this.tblWgzzWghsService.uploadattbyid(id, tblAttachmentEntity.getAttid().toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean.toString();
	}
    
    
    
    //查询违规核实审批已完成的编号和内容
    @OperationLog(
            success = "查询违规核实审批已完成的编号和内容",
            busType = "整改追责",
            fail = "查询违规核实审批已完成的编号和内容",
            operationType = OperationType.SELECT,
            subType = "违规追责——查询违规核实审批已完成的编号和内容"
    )
    @GetMapping("/wghsListBY")
    @Operation(summary = "查询违规核实审批已完成的编号和内容")
    public JsonBean getByWghsBHlist(@Parameter(name="token",required=false)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  @Parameter(name="clueNaber",description="clueNaber",required=false)@RequestParam(value="clueNaber",required=false) String clueNaber){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.getByWghsBHlist(token,pageNumber,pageSize,clueNaber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 违规报告-附件列表
     */
    @OperationLog(
            success = "违规报告附件列表",
            busType = "整改追责",
            fail = "违规报告附件列表",
            operationType = OperationType.SELECT,
            subType = "违规追责——查询违规报告的附件列表"
    )
    @GetMapping("/wgbg_file_list")
    @Operation(summary = "违规报告-附件列表")
    public JsonBean wgbg_file_list(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "id", description = "业务主键", required = true) @RequestParam(value = "id", required = true) BigDecimal id) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzWghsService.wgbgFileList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    
    /**
     * 违规报告-附件列表
     */
    @OperationLog(
            success = "违规核实附件列表",
            busType = "整改追责",
            fail = "违规核实附件列表",
            operationType = OperationType.SELECT,
            subType = "违规追责——查询违规核实的附件列表"
    )
    @GetMapping("/wghs_file_list")
    @Operation(summary = "违规核实-附件列表")
    public JsonBean wghs_file_list(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "id", description = "业务主键", required = true) @RequestParam(value = "id", required = true) BigDecimal id) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzWghsService.wghsFileList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "查询违规核实审批已完成的编号和内容",
            busType = "整改追责",
            fail = "查询违规核实审批已完成的编号和内容",
            operationType = OperationType.SELECT,
            subType = "违规追责——查询违规核实审批已完成的编号和内容-移交函进行选择"
    )
    @GetMapping("/wghsListBYyj")
    @Operation(summary = "查询违规核实审批已完成的编号和内容-移交函进行选择")
    public JsonBean wghsListBYyj(@Parameter(name="token",required=false)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  @Parameter(name="clueNaber",description="clueNaber",required=false)@RequestParam(value="clueNaber",required=false) String clueNaber){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.getByWghsBHlist(token,pageNumber,pageSize,clueNaber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
}
