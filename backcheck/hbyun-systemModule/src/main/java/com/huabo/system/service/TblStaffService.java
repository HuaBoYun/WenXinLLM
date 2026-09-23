package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huabo.system.entity.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.system.vo.result.StaffResult;

import javax.servlet.http.HttpServletRequest;


public interface TblStaffService {

    JsonBean dealLoginSystem(String userName, String password, String loginType, String verid, HttpServletRequest request) throws Exception;

  	JsonBean dealOaLoginSystem(String userName, String verid,HttpServletRequest request) throws Exception;

  	String dealGWLoginSystem(String userName, String verid) throws Exception;


  	TblStaff selectUniqueStaffInfo(String username) throws Exception;


  	//新方法
    boolean validateOrgExistStaff(List<TblOrganization> findChildrenByOrgid);


    TblStaff get(BigDecimal var1);


    TblStaff findByUsername(String username);


    Map<String, Object> add(TblStaff user);


    List<TblStaff> findByEmail(String email);


    void modify(TblStaff user);


    TblStaff findById(String id);


    Map<String, Object> findAll(String staffId, Find find, Integer pageNumber, Integer pageSize, BigDecimal pid);


    Map<String, Object> findAllPid(String staffId, Find find, Integer pageNumber, Integer pageSize, String token);


    TblStaff findByStaffid(BigDecimal staffid) throws Exception;

	TblStaff findByNewStaffid(String operationData) throws Exception;


    Map<String, Object> update(TblStaff user);


    Map<String, Object> findByAll(String pid, Integer pageNumber, Integer pageSize, String token, String staffId);

    Map<String, Object> findAllPageBean(Integer pageNumber, Integer pageSize, String token, String staffId);

    Map<String, Object> findUserByZbsjForQx(PageInfo<TblStaff> pageInfo) throws Exception;

    TblStaff getExpert(BigDecimal staffid);

    void modifyStaff(TblStaff staff);

    JsonBean findUserInfoByToken(String token) throws Exception;

    TblStaff findByOragn(String cystaffid);

    Map<String, Object> findAllPageBeanPid(String pid, Integer pageNumber, Integer pageSize, String token);

    void updateStaff(TblStaff staff);


    List<TblManageRight> findMansgeUserRight(String staffid);


    List<TblStaff> findByJobid(BigDecimal jobid);


    List<BigDecimal> findUserByRole(String string, BigDecimal orgid);


    TblStaff selectProjectGroupLeader(Integer projectId);

    List<BigDecimal> findUserByProject(Integer projectId);

    JsonBean modifyUserPassWord(String token, BigDecimal staffId, String oldPassWord, String newPassWord, String twoPassWord) throws Exception;

    JsonBean mergeStaffManageInfo(String data, String attids, String birth, String workDate) throws Exception;

    JsonBean getStaffList(BigDecimal orgid, String token, Integer pageNumber, Integer pageSize, TblStaff staff) throws Exception;

    JsonBean findSjStaffDetail(String token, String staffid) throws Exception;

    JsonBean saveOrUpdateTrain(TblPersonalTrain train, String staffid, String trainattIds) throws Exception;

    JsonBean deleteTrain(String token, String trainid) throws Exception;

    JsonBean getStaffAttInfo(String token, String staffid) throws Exception;

    JsonBean deleStaffAttInfo(String token, String attid) throws Exception;

    JsonBean deleTrainAttInfo(String token, String attid) throws Exception;

    JsonBean getAuditorInformationList(String token, String orgid) throws Exception;

    JsonBean updateStaff(String token, String staffId) throws Exception;

    Map<String, Object> syncUser(Integer operaType, String data) throws Exception;

    void syncPerson(String data) throws Exception;

    Integer selectUniqueCountUserName(String username, BigDecimal staffid) throws Exception;

    Integer selectUniqueCountFgld(String fgorgs, BigDecimal staffid) throws Exception;

    Integer selectUniqueCountBmfzr(String manageorgs, BigDecimal staffid) throws Exception;

    JsonBean findAllLeader(String token, String orgid, Integer pageNumber, Integer pageSize) throws Exception;

    // TblStaff findByOrag(String cystaffid);


//    TblStaff getSTAFFID(BigDecimal userid);

    /**
     * 获取不同类型角色 基础数据
     *
     * @param staffId
     * @return
     */
    StaffResult getUserInfoExam(BigDecimal staffId, BigDecimal orgid, String type);

    public JsonBean setLoginUserOrgInfo(String token, BigDecimal orgId, BigDecimal deptId) throws Exception;


	void updateuserRole(String id, String roles) throws Exception;


	Map<String, Object> selectAllListByroleid(String roleid, String username, String realname, Integer pageNumber,
			Integer pageSize) throws Exception;


	/**
	 * 主题仓库用户下发，获取取消下发的用户集合
	 * @param token
	 * @param pageIds
	 * @param realName
	 * @param userName
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	JsonBean getListByThemeHouse(String token, String[] pageIds, Integer pageNumber, Integer pageSize, String userName, String realName) throws Exception;


	JsonBean dealUserOrgRelation(String token) throws Exception;


	/**
	 * 根据小程序unionId 获取 用户信息进行登录
	 * @param unionId
	 * @return
	 * @throws Exception
	 */
	JsonBean findStaffByUnionId(String unionId) throws Exception;

	/**
	 * 微信小程序注册新用户方法
	 * @param user 微信存储用户信息
	 * @return
	 * @throws Exception
	 */
	JsonBean wxXcxRegisterUser(String user) throws Exception;


	JsonBean wxXcxInviteRegisterUser(String user) throws Exception;


	TblStaff selectWxappAdmin(BigDecimal orgId) throws Exception;


	void dealWxUserInfo(Map<String, String> userInfoMap, TblWxUserInfo userInfo) throws Exception;

	JsonBean wxXcxModifyUser(TblWxUserInfo userInfo) throws Exception;

	void importStaffInfo(XSSFSheet sheet, String token) throws Exception;

	//中核
	void syncZhUser(String data, TblSynchronizationRecord record) throws Exception;
	
	void syncZhNewUser(String data, TblSynchronizationRecord record) throws Exception;

	void syncZhPersonPostCode(String data) throws Exception;

	Map<String,Object> importStaff(String token,String excelPath) throws Exception;

	Map<String,Object> importZHRole(String token,String excelPath) throws Exception;

	JsonBean dealUserRightRelation(String token) throws Exception;

	JsonBean getUserPartDept(String token, BigDecimal staffId) throws Exception;

    void addLoginLog(UserLoginLog loginLog);

	void dealConfirmRelationInfo(TblStaff tblStaff) throws Exception;

	JsonBean logout(String ip, HttpServletRequest request) throws Exception;
	
	JsonBean userSx(String ip, String username,HttpServletRequest request) throws Exception;

	List<String> selectRealNameListByStaffIds(String staffids) throws Exception;
}
