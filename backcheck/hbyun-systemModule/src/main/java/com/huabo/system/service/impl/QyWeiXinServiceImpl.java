package com.huabo.system.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.EncryptUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.redis.RedisUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblMyTask;
import com.huabo.system.entity.TblRole;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.service.QyWeiXinService;
import com.huabo.system.utils.QyWeiXinUtil;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Service
public class QyWeiXinServiceImpl implements QyWeiXinService {

	@Resource
	TblStaffMapper tblStaffMapper;
	
	@Resource
	private UserProvider userProvider;
	
	@Resource
	private RedisUtil redisUtil;


	@Override
	public int sendMessage(String val) {
		try {
			if (sendMsg2User(val, null))
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int sendMessage(TblMyTask tblMyTask) {
		try {
			if (tblMyTask == null || tblMyTask.getHandle() == null || ("无").equals(tblMyTask.getHandle())) {
				return -1;
			}
			if (sendMsg2User(tblMyTask.getHandle(), tblMyTask.getCurrentOrg()))
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int sendMessage(List<TblMyTask> tasks) {
		try {
			TblMyTask tblMyTask = tasks.stream().reduce((a, b) -> b).orElse(null);
			if (tblMyTask == null || tblMyTask.getHandle() == null) {
				return -1;
			}
			if (sendMsg2User(tblMyTask.getHandle(), tblMyTask.getCurrentOrg()))
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private boolean sendMsg2User(String val, String companyId) {
		TblStaff staff = getNextStaff(val, companyId);
		if (staff == null) {
			return false;
		}
		String username = staff.getUsername();
		String localSystemToken = this.getLocalSystemToken(username);
		if (localSystemToken != null) {
			String ret = QyWeiXinUtil.sendTxtHrefMessage(username, localSystemToken);
			if (ret != null && "0".equals(ret)) {
				return Boolean.TRUE;
			}
		}
		return false;
	}

	@Override
	public TblStaff getNextStaff(String val, String companyId) {
		TblStaff staff = null;
		try {
			if ("无".equals(val)) {
				return null;
			}
			String val1 = val.replaceAll("(\\r\\n|\\n)", "");
			TblRole role = this.tblStaffMapper.findRoleByName(val1, companyId);
			if (role != null) {
				BigDecimal rid = role.getRid();
				staff = tblStaffMapper.findByRoleName(rid.toString());
			} else {
				if (NumberUtil.isNumber(val)) {
					staff = this.tblStaffMapper.findByStaffid(new BigDecimal(val));
				} else {
					staff = this.tblStaffMapper.findByRealname(val);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return staff;
	}

	@Override
	public String getLocalSystemToken(String userName) {
		try (Jedis jedis = JedisUtil.getJedis()) {
			TblStaff staff = this.tblStaffMapper.findByUsername(userName);
			if (staff == null) {
				return null;
			}
			TblStaffUtil staffUtil = new TblStaffUtil();
			staffUtil.setStaffid(staff.getStaffid());
			staffUtil.setRealname(staff.getRealname());
			staffUtil.setUsername(staff.getUsername());
			staffUtil.setAddress(staff.getAddress());
			staffUtil.setEmail(staff.getEmail());
			staffUtil.setMiblephone(staff.getMiblephone());
			staffUtil.setMemo(staff.getMemo());
			staffUtil.setStatus(staff.getStatus());
			staffUtil.setJobid(staff.getJobid());
			staffUtil.setOutSideId(staff.getOutSideId());
			staffUtil.setOutSideOpenId(staff.getOutSideOpenId());
			staffUtil.setFixedphone(staff.getFixedphone());
			staffUtil.setRoleIdStrs(staff.getRoleIdStrs());
			String token = EncryptUtil.getInstance().AESencode(staff.getStaffid().toString(), EncryptUtil.DESKEY);
			String key = staff.getStaffid() + JedisUtil.USERINFOKEY;
			
			if(!redisUtil.exists(key)) {
				userProvider.add(staffUtil);
        	}
			
			return token;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
