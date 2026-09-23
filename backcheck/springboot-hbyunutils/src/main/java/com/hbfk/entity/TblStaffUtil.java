package com.hbfk.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TblStaffUtil {
	public final static Integer USER_DISABLE=0;//禁用
    public final static Integer USER_ENBLE=1;//启用


    private BigDecimal staffid;//主键ID,自动增长
    private String realname;//真实名字
    private String fixedphone;//固定电话
    private String address;//地址
    private String email;//邮箱
    private String miblephone;//手机号码
    private String memo;//备注
    private String username;//用户名（登录名）
    private String password;//密码
    private BigDecimal jobid;//岗位ID
    private Date createDate;//创建日期
    private Integer status;//状态（1启用，0弃用）
    private TblRoleUtil  trole;//拥有的角色
    private BigDecimal orgid;//组织Id
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    private String outSideOpenId; //外部同步企业来源Id
    private String orgName;//部门名称
    private String jobName;//岗位名称
    private String orgFatherName;//公司名称
    private String rn;//排序
    private String checked;//是否选中
    private String roleIdStrs;//角色主键拼接字符串
	private String roleNames;//角色名称拼接字符串
	private String ymToken;//流程平台单点token
	private String pkYmStaffId;//流程平台当前用户主键
	private String historycode;//外部系统来源 用户主键
	private String deptIds;//数据权限产看的公司主键
	private String orgStrIds;//
    private BigDecimal secrectLevelId;//当前用户所属密级主键
    private String secrectLevelName;//当前用户所属密级名称
    private String secrectScopeIds;//当前用户所能查看密级数据的范围主键
    
    private boolean requireValuedata;
	
	private String jnpfOrigin;//流程平台登录来源 app-移动端 ，pc-电脑端
	
    private TblOrganizationUtil linkDetp;//用户隶属的部门
    private TblOrganizationUtil currentOrg;//用户当前所在的公司
    private TblOrganizationUtil linkOrg;//用户隶属的公司
    private TblOrganizationUtil groupOrg;//用户所属集团
    
    private FaAccbookinfoUtil accbook;//财务账簿信息
    
    private TblLoginTypeUtil loginType;//登录页面信息
    
    private List<TblTransferWorkUtils> workList;//工作移交列表
    
	@Schema(description = "用户兼职公司部门集合")
	private List<TblUserOrgRelationUtil> relaList;
}
