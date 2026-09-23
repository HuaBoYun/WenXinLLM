package com.huabo.system.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblContractTypeof;
import com.huabo.system.entity.TblSystemRight;
import com.huabo.system.mapper.TblContractTypeofMapper;
import com.huabo.system.service.TblContractTypeofService;

@Service
@Transactional
public class TblContractTypeofServiceImpl implements TblContractTypeofService {
    @Resource
    private TblContractTypeofMapper tblContractTypeofMapper;
    
    @Resource
    private UserProvider userProvider;


    @Override
    public Map<String, Object> findPageInfoList(Integer pageNumber, Integer pageSize, String choiceTypeName, String token, String staffId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();

                
                List<TblContractTypeof> fatherList = this.tblContractTypeofMapper.seletAllFatherType();
                
                QueryWrapper<TblContractTypeof> wapper = new  QueryWrapper<TblContractTypeof>();
                
                if(StringUtils.isNotBlank(choiceTypeName)) {
            		wapper.like("TYPENAME", choiceTypeName);
            	}
                wapper.isNotNull("PARENTID");
                List<TblContractTypeof> childrenList = this.tblContractTypeofMapper.selectList(wapper);
                
                
                for (TblContractTypeof ft : fatherList) {
                	ft.setChildren(childrenList.stream().filter(obj -> ft.getTypeId().toString().equals(obj.getParentid().toString())).collect(Collectors.toList()));
        		}
            resultMap.put("data", fatherList);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public TblContractTypeof findByid(String typeId) {
            List<TblContractTypeof> list = this.tblContractTypeofMapper.selectAllList(typeId);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        return null;
    }

    @Override
    public void updateContractTypeof(TblContractTypeof typeof) {
        this.tblContractTypeofMapper.updateContractTypeof(typeof);
    }

}