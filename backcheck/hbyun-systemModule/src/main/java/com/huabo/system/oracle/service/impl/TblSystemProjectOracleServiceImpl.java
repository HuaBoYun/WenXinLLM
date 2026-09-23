package com.huabo.system.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblSystemProjectOracle;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblSystemProjectOracleMapper;
import com.huabo.system.oracle.service.TblSystemProjectOracleService;
import com.huabo.system.vo.param.TblSystemProjectQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TblSystemProjectOracleServiceImpl implements TblSystemProjectOracleService {

	@Resource
	private TblSystemProjectOracleMapper tblSystemProjectOracleMapper;
	
	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	
	@Resource
    private UserProvider userProvider;

	@Value("${application.admin-name:}")
	private String adminName;

	/**
	 * 系统项目表列表 查询
	 * @return
	 */
	@Override
	public List<TblSystemProjectOracle> getList(TblSystemProjectQueryParam project) {
		if (StringUtils.equals(project.getCreatorName(), adminName)) {
			project.setBelongGroup(null);
		}
		return tblSystemProjectOracleMapper.getList(project);
	}

	/**
	 * 系统项目表 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblSystemProjectOracle saveOrUpdate(TblSystemProjectOracle param) {
		if (StringUtils.equals(param.getCreatorName(), adminName)) {
			param.setCreator(null);
			param.setWorkUnit(null);
			param.setBelongGroup(null);
		}
		Date now = new Date();
		if (param.getId() == null) {
			if (param.getProjectType() == 1) {
				QueryWrapper<TblSystemProjectOracle> wapper = new QueryWrapper<TblSystemProjectOracle>();
				wapper.eq("UNIQUEIDENTIFICATION", param.getUniqueIdentification());
				wapper.eq("PROJECTTYPE", 1);
				wapper.eq("BELONGGROUP", param.getBelongGroup());
				int count = tblSystemProjectOracleMapper.selectCount(wapper).intValue();
				if (count > 0) {
					throw new ServiceException(400, "唯一标识已存在");
				}
			}
			if (param.getProjectType() == 2) {
				QueryWrapper<TblSystemProjectOracle> wapper = new QueryWrapper<TblSystemProjectOracle>();
				wapper.eq("UNIQUEIDENTIFICATION", param.getUniqueIdentification());
				wapper.eq("PROJECTTYPE", 2);
				wapper.eq("BELONGGROUP", param.getBelongGroup());
				int count = tblSystemProjectOracleMapper.selectCount(wapper).intValue();
				if (count > 0) {
					throw new ServiceException(400, "项目已存在");
				}
			}
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			param.setId(RandomUtil.uuBigDecimalId());
			tblSystemProjectOracleMapper.insert(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setUpdatedTime(now);
			tblSystemProjectOracleMapper.updateById(param);
		}
		return findById(param.getId());
	}

	/**
	 * 系统项目表详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblSystemProjectOracle findById(BigDecimal id) {
		TblSystemProjectOracle systemProject = tblSystemProjectOracleMapper.selectById(id);
		if (systemProject == null) {
			throw new ServiceException(400, 50001);
		}
		return systemProject;
	}

	/**
	 * 系统项目表 刪除
	 * @param id
	 */
	@Override
	public void delete(BigDecimal id) {
		tblSystemProjectOracleMapper.deleteById(id);
	}

	@Override
	public Boolean isUniqueIdentification(String uniqueIdentification) {
		QueryWrapper<TblSystemProjectOracle> wapper = new QueryWrapper<TblSystemProjectOracle>();
		wapper.eq("UNIQUEIDENTIFICATION",uniqueIdentification);
		wapper.eq("PROJECTTYPE",2);
		int count = tblSystemProjectOracleMapper.selectCount(wapper).intValue();
		return count > 0;
	}

	/**
	 * 根据id查询 系统项目表 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(BigDecimal id) {
		QueryWrapper<TblSystemProjectOracle> wapper = new QueryWrapper<TblSystemProjectOracle>();
		wapper.eq("ID", id);
		int count = tblSystemProjectOracleMapper.selectCount(wapper).intValue();
		if (count == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public com.hbfk.util.JsonBean getThemeModuleList(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return com.hbfk.util.ResponseFormat.retParam(0, 20006, null);
        }
		
        //查询 主题仓库报表下功能模块列表 并 筛选掉其他第三方系统的功能模块
        List<TblSystemProjectOracle> projectList = this.tblSystemProjectOracleMapper.selectModuleListByTheme(loginStaff.getCurrentOrg().getOrgid());
		
		return com.hbfk.util.ResponseFormat.retParam(1, 200, projectList);
	}

	@Override
	public void syncSystemModuleInit() throws Exception {
		List<TblOrganization> companyIdList = this.tblOrganizationMapper.selectAllCompanyList();
		List<TblSystemProjectOracle> projectList = this.tblSystemProjectOracleMapper.selectList(null);
		Integer count = 0;
		for (TblSystemProjectOracle project : projectList) {
			for (TblOrganization company : companyIdList) {
				count = this.tblOrganizationMapper.selectSystemOrgPorjectAuth(company.getOrgid(),project.getId());
				if(count == 0){
					this.tblOrganizationMapper.insertSystemProjectAuth(company.getOrgid(),new BigDecimal(30),project.getId(),RandomUtil.uuBigDecimalId());
					System.out.println("新僧数据："+RandomUtil.uuBigDecimalId());
				}
			}
		}
		
	}
}
