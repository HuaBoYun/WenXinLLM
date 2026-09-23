package com.huabo.fxgl.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.huabo.fxgl.entity.RiskAssessmentstd;
import com.huabo.fxgl.entity.RiskAssplan;
import com.huabo.fxgl.entity.RiskInfludegree;
import com.huabo.fxgl.entity.RiskLevelmapping;
import com.huabo.fxgl.entity.RiskPossibility;
import com.huabo.fxgl.entity.SpringContextHolder;
import com.huabo.fxgl.mapper.OpenQueryMapperSqlConfig;
import com.huabo.fxgl.mapper.RiskAssessmentstdMapper;
import com.huabo.fxgl.mapper.RiskAssplanMapper;
import com.huabo.fxgl.mapper.RiskInfludegreeMapper;
import com.huabo.fxgl.mapper.RiskLevelmappingMapper;
import com.huabo.fxgl.mapper.RiskMapper;
import com.huabo.fxgl.mapper.RiskPossibilityMapper;
import com.huabo.fxgl.service.IRiskAssessmentstdService;
import com.huabo.fxgl.service.IRiskAssplanRiskService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.fieldActivationVo;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.GeneralEntity;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.user.UserProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Service
public class RiskAssessmentstdServiceImpl extends ServiceImpl<RiskAssessmentstdMapper, RiskAssessmentstd> implements IRiskAssessmentstdService {

    @Autowired
    private RiskAssessmentstdMapper riskAssessmentstdMapper;
    @Autowired
    private RiskMapper riskMapper;
    @Autowired
    private RiskAssplanMapper riskAssplanMapper;
    @Autowired
    private RiskPossibilityMapper riskPossibilityMapper;
    @Autowired
    private RiskInfludegreeMapper riskInfludegreeMapper;
    
    @Autowired
	private RiskLevelmappingMapper riskLevelmappingMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public PageInfo<RiskAssessmentstd> selectEvaluateStandard(String assName, BigDecimal orgid,Integer pageNo,Integer pageSize,TblStaffUtil staffUtil,String secrectLevelld) throws Exception {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("TR.COMPANYID", orgid);
        if (StringUtils.isNotBlank(assName)) {
            wrapper.like("assName", assName);
        }
        wrapper.orderByAsc("TR.ASSSTDID");
        String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), false, "TR.COMPANYID", "TR.LINKDEPTID", "TR.CREATESTAFFID", "TR.SECRECTLEVELID", "TR.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
        StringBuffer buf=new StringBuffer();
        buf.append(sql);
         if(StringUtils.isNotBlank(secrectLevelld)){
         	List<String> levels=riskMapper.getSecrectLevel(secrectLevelld);
         	final String levelsStr=sql+String.join(",", levels);
          	 buf.append(" and (TR.SECRECTLEVELID IN (").append(String.join(",", levels)).append(") ").append(" or ").append("TR.SECRECTLEVELID").append(" IS NULL OR ").append("TR.SECRECTLEVELID").append(" = ''  )");;
         }
        com.github.pagehelper.PageInfo<RiskAssessmentstd> pageInfo=pageInfo = PageMethod.startPage(pageNo, pageSize)
				.doSelectPageInfo(() -> riskAssessmentstdMapper.selectRiskAssessMents(wrapper,buf.toString()));

        return pageInfo;

    }

    @Override
	public PageInfo<RiskAssessmentstd> findByAllList(RiskAssessmentstd risk,Integer pageNo,Integer pageSize,TblStaffUtil staffUtil ,Integer authorityType) throws Exception{
        QueryWrapper wrapper = new QueryWrapper();
		// 如果风险评估标准对象包含非空的评估名称，添加名称模糊匹配条件
        if (StringUtils.isNotBlank(risk.getAssname())) {
            wrapper.like("assName", risk.getAssname());
        }
		// 如果风险评估标准对象包含非空的评估编号，添加编号模糊匹配条件
        if (StringUtils.isNotBlank(risk.getAssnumber())) {
            wrapper.like("assNumber", risk.getAssnumber());
        }
		// 如果风险评估标准对象包含非空的公司ID，添加公司ID精确匹配条件
        if (risk.getCompanyid()!=null) {
            wrapper.eq("COMPANYID", risk.getCompanyid());
        }
		// 按照风险评估标准ID升序排序
        wrapper.orderByDesc("ASSSTDID");
        String sql = GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(staffUtil.getCurrentOrg().getUseSecrect(), false, "COMPANYID", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds(),authorityType));
        //  String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), false, "COMPANYID", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
       
        com.github.pagehelper.PageInfo<RiskAssessmentstd> pageInfo=pageInfo = PageMethod.startPage(pageNo, pageSize)
				.doSelectPageInfo(() -> riskAssessmentstdMapper.getRiskAssessMentList(wrapper,sql));
        return pageInfo;

    }

	@Override
	public Map<String, Object> deleteRisk(String riskid) throws Exception {
		// TODO Auto-generated method stub
        Map<String,Object> result = new HashMap<String,Object>(0);
		String resutl="";
		if (StringUtils.isNotBlank(riskid)) {
			// 将riskid按逗号分隔，得到单个ID列表
			String[] ids = riskid.split(",");
			for (String d : ids) {
				BigDecimal bd = new BigDecimal(d);
				RiskAssessmentstd risk = riskAssessmentstdMapper.select(bd);
				// 如果风险评估记录存在
				if (null != risk) {
					// 查询与该风险评估关联的风险计划列表
					List<RiskAssplan> assplans = riskAssplanMapper.getRiskAssplanByAssessMentId(bd);
					if (assplans.size() == 0) {
						// 删除风险评估记录
						riskAssessmentstdMapper.deleteById(bd);
					} else {
						result.put("code", 0);
						result.put("result", "编号：(" + risk.getAssnumber() + ")正在被使用，无法删除");
						return result;
					}
				}
			}
		}
		result.put("code", 1);
		result.put("result", "删除成功!");
		return result;
	}

	
	@Override
	public Map<String, Object> saveBz(String assNumber, String assName, String assDes,TblStaffUtil staffUitl, String possibilityStr, String infludegreeStr,String levelStr,BigDecimal secrectLevelId,String staffScopeNames,String staffScopeIds,fieldActivationVo vo) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result = new HashMap<String,Object>(0);
		// 检查风险评估标准的必要信息是否齐全
 	     if(StringUtils.isNotBlank(assName)&&StringUtils.isNotBlank(assNumber)&&StringUtils.isNotBlank(assDes)){
          //查询编号重复
 			Integer number = riskAssessmentstdMapper.selectRiskAssessMentsNumber(assNumber,staffUitl.getCurrentOrg().getOrgid());
 			if(number==0){
 				//保存风险评估标准
				// 如果编号不重复，创建并保存风险评估标准
				RiskAssessmentstd assessMentsTd = new RiskAssessmentstd();
				assessMentsTd.setAssdes(assDes);
				assessMentsTd.setAssname(assName);
				assessMentsTd.setAssnumber(assNumber);
				assessMentsTd.setAssstatus(1);
				assessMentsTd.setStaffScopeIds(staffScopeIds);
				assessMentsTd.setStaffScopeNames(staffScopeNames);
				assessMentsTd.setSecrectLevelId(secrectLevelId);
				assessMentsTd.setLinkdeptid(staffUitl.getLinkDetp().getOrgid());
				assessMentsTd.setCreatetime(new Date());
				assessMentsTd.setCreatestaffid(staffUitl.getStaffid());
				assessMentsTd.setCompanyid(staffUitl.getCurrentOrg().getOrgid());
				if(vo!=null){
					assessMentsTd.setFieldActivationCopy(vo);
				}
				assessMentsTd.setAssstdid(RandomUtil.randomBigDecimal());
				baseMapper.insert(assessMentsTd);
				
				//保存风险发生频率
				JSONArray possiArray = JSONArray.parseArray(possibilityStr);
				JSONObject possiObj = null;
				RiskPossibility riskPossibility = null;
				List<RiskPossibility> possiList = new ArrayList<RiskPossibility>(0);
				for (int i = 0; i < possiArray.size() ; i++) {
					riskPossibility = new RiskPossibility();
					possiObj = possiArray.getJSONObject(i);
					riskPossibility.setRplevel(possiObj.getBigDecimal("rplevel"));
					riskPossibility.setPossdes(possiObj.getString("possdes"));
					riskPossibility.setAssstdid(assessMentsTd.getAssstdid());
					riskPossibilityMapper.insert(riskPossibility);
					possiList.add(riskPossibility);
				}
				
				//保存风险影响程度
				JSONArray influArray = JSONArray.parseArray(infludegreeStr);
				JSONObject influObj = null;
				RiskInfludegree riskInfludegree = null;
				List<RiskInfludegree> influList = new ArrayList<RiskInfludegree>(0);
				
				//保存风险级别
				JSONArray levelArray = JSONArray.parseArray(levelStr);
				JSONObject levelArrayObj = null;
				RiskLevelmapping level = null;
				for (int i = 0; i < influArray.size(); i++) {
					influObj = influArray.getJSONObject(i);
					riskInfludegree = new RiskInfludegree();
					riskInfludegree.setRilevel(influObj.getBigDecimal("rilevel"));
					riskInfludegree.setInfludegreedes(influObj.getString("infludegreedes"));
					riskInfludegree.setAssstdid(assessMentsTd.getAssstdid());
					riskInfludegreeMapper.insert(riskInfludegree);
					
					levelArrayObj = levelArray.getJSONObject(i);
					level = new RiskLevelmapping();
					level.setPoss1(levelArrayObj.getString("poss1"));
					level.setPoss2(levelArrayObj.getString("poss2"));
					level.setPoss3(levelArrayObj.getString("poss3"));
					level.setPoss4(levelArrayObj.getString("poss4"));
					level.setPoss5(levelArrayObj.getString("poss5"));
					level.setAssstdid(assessMentsTd.getAssstdid());
					level.setInfludegree(riskInfludegree.getDegreeid().toString());
					level.setRlevelmapid(RandomUtil.randomBigDecimal());
					riskLevelmappingMapper.insert(level);
					riskInfludegree.setRiskLevelMapping(level);
					influList.add(riskInfludegree);
				}
				
				assessMentsTd.setPossibilities(possiList);
				assessMentsTd.setRiskInfludegrees(influList);
				
				result.put("data", assessMentsTd);
				result.put("code", 0);
				result.put("result", "操作成功!");
				return result;
 		}
			result.put("code", 1);
			result.put("result", "风险评估标准编号重复");
			return result;
 	}
		result.put("code", 1);
		result.put("result", "数据不全");
		return result;
	}

	@Override
	public Map<String, Object> getRiskAssessMentstd(BigDecimal assstdid) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result = new HashMap<String,Object>(0);
		if(null!=assstdid){
			// 查询数据库，获取对应的风险评估标准记录
			RiskAssessmentstd assessMentsTd = riskAssessmentstdMapper.select(assstdid);
			// 查询与该风险评估标准关联的风险发生频率列表
			List<RiskPossibility> possiList = this.riskPossibilityMapper.findAllByAssId(assstdid);
			// 查询与该风险评估标准关联的风险影响程度列表
			List<RiskInfludegree> influList = this.riskInfludegreeMapper.findAllByAssId(assstdid);
			assessMentsTd.setRiskInfludegrees(influList);
			assessMentsTd.setPossibilities(possiList);
			
			List<RiskAssplan> assplans = riskAssplanMapper.getRiskAssplanByAssessMentId(assstdid);
			if(null!=assessMentsTd){
				FiexibleNameAssignment ment = new FiexibleNameAssignment();
				// 对灵活字段中的姓名名称及机构名称赋值
				fieldOrgStaffId item = new fieldOrgStaffId();
				BeanUtils.copyProperties(assessMentsTd, item);
				fieldOrgStaffName nameEntity = ment.setOpenName(item);
				BeanUtils.copyProperties(nameEntity, assessMentsTd);
				result.put("data", assessMentsTd);
				if(assplans.size()>0){
					result.put("isModify", false); //该评估标准已被使用  无法修改
				}else{
					result.put("isModify", true);
				}
			}
		}
		result.put("code", 1);
		result.put("result", "访问成功");
		return result;
	}

	@Override
	public Map<String, Object> updateRiskAssessMentstd(RiskAssessmentstd risk, String possibilityStr, String infludegreeStr, String levelStr) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result = new HashMap<String,Object>(0);
		// 检查风险评估标准的必要信息是否齐全
		 if(null!=risk.getAssstdid()&&StringUtils.isNotBlank(risk.getAssdes())&&StringUtils.isNotBlank(risk.getAssname())&&StringUtils.isNotBlank(risk.getAssnumber())){
				RiskAssessmentstd assessMentsTd = riskAssessmentstdMapper.select(risk.getAssstdid());
				if(null!=assessMentsTd){
					riskAssessmentstdMapper.updateById(risk);
					
					//保存风险发生频率
					JSONArray possiArray = JSONArray.parseArray(possibilityStr);
					JSONObject possiObj = null;
					RiskPossibility riskPossibility = null;
					for (int i = 0; i < possiArray.size() ; i++) {
						riskPossibility = new RiskPossibility();
						possiObj = possiArray.getJSONObject(i);
						riskPossibility.setPossid(possiObj.getBigDecimal("possid"));
						riskPossibility.setRplevel(possiObj.getBigDecimal("rplevel"));
						riskPossibility.setPossdes(possiObj.getString("possdes"));
						riskPossibility.setAssstdid(risk.getAssstdid());
						riskPossibilityMapper.updateById(riskPossibility);
					}
					
					//保存风险影响程度
					JSONArray influArray = JSONArray.parseArray(infludegreeStr);
					JSONObject influObj = null;
					RiskInfludegree riskInfludegree = null;
					
					//保存风险级别
					JSONArray levelArray = JSONArray.parseArray(levelStr);
					JSONObject levelArrayObj = null;
					RiskLevelmapping level = null;
					for (int i = 0; i < influArray.size(); i++) {
						influObj = influArray.getJSONObject(i);
						riskInfludegree = new RiskInfludegree();
						riskInfludegree.setDegreeid(influObj.getBigDecimal("degreeid"));
						riskInfludegree.setRilevel(influObj.getBigDecimal("rilevel"));
						riskInfludegree.setInfludegreedes(influObj.getString("infludegreedes"));
						riskInfludegree.setAssstdid(assessMentsTd.getAssstdid());
						riskInfludegreeMapper.updateById(riskInfludegree);
						
						levelArrayObj = levelArray.getJSONObject(i);
						level = new RiskLevelmapping();
						level.setRlevelmapid(levelArrayObj.getBigDecimal("rlevelmapid"));
						level.setPoss1(levelArrayObj.getString("poss1"));
						level.setPoss2(levelArrayObj.getString("poss2"));
						level.setPoss3(levelArrayObj.getString("poss3"));
						level.setPoss4(levelArrayObj.getString("poss4"));
						level.setPoss5(levelArrayObj.getString("poss5"));
						level.setAssstdid(risk.getAssstdid());
						if(null!=riskInfludegree.getDegreeid()) {
							level.setInfludegree(riskInfludegree.getDegreeid().toString());
						}
						riskLevelmappingMapper.updateById(level);
					}
					result.put("code", 1);
					result.put("result", "操作成功!");
					return result;
				}
				
				result.put("code", 1);
				result.put("result", "数据不完整");
				return result;
			}
		     result.put("code", 1);
			result.put("result", "数据不全");
			return result;
	}

	@Override
	public Map<String, Object> v_list_yxcd(BigDecimal assstdid) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result = new HashMap<String,Object>(0);
		if(null!=assstdid){
			List<RiskInfludegree> list = riskInfludegreeMapper.findAllByAssId(assstdid);
			List<RiskAssplan> assplans =  riskAssplanMapper.getRiskAssplanByAssessMentId(assstdid);
			Collections.sort(list,new Comparator<RiskInfludegree>() {
				@Override
				public int compare(RiskInfludegree o1, RiskInfludegree o2) {
					 return o1.getRilevel().compareTo(o2.getRilevel());
				}
			});  
			result.put("list", list);
			result.put("assId",assstdid);
			if(assplans.size()>0){
				result.put("update", "update");
			}else{
				result.put("update", "save");
			}
		}
		return result;
	}
	
	@Override
	public JsonBean get_riskpgbz_no(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        //拼接编号
        String yearStr = String.valueOf(DateUtil.thisYear());
        String assnumber = "风险评估标准-"+yearStr+"-";
        Integer maxno = this.riskAssplanMapper.get_riskpgbz_no("'"+assnumber+"%'");
        if(null == maxno) {
        	maxno = 0;
        }
        maxno = maxno+1;
        assnumber = assnumber+maxno;
        
		return ResponseFormat.retParam(1, 200, assnumber);
	}
	
	
	public String get_NewCode(BigDecimal orgid) throws Exception {
        //拼接编号
        String yearStr = String.valueOf(DateUtil.thisYear());
        String assnumber = "风险评估标准-"+yearStr+"-";
        Integer maxno = this.riskAssplanMapper.get_NewCode("'"+assnumber+"%'",orgid);
        if(null == maxno) {
        	maxno = 0;
        }
        maxno = maxno+1;
        assnumber = assnumber+maxno;
        
		return assnumber;
	}
}
