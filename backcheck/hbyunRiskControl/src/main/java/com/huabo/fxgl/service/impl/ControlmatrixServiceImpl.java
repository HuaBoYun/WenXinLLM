package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.config.SysConfig;
import com.huabo.fxgl.entity.Controlmatrix;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.TblControlEntries;
import com.huabo.fxgl.entity.TblOrganization;
import com.huabo.fxgl.mapper.ControlmatrixMapper;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.TblControlEntriesMapper;
import com.huabo.fxgl.service.IControlmatrixService;

import cn.hutool.json.JSONObject;
import net.sf.json.JSONArray;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
@Service
public class ControlmatrixServiceImpl extends ServiceImpl<ControlmatrixMapper, Controlmatrix> implements IControlmatrixService {

@Autowired
private ControlmatrixMapper controlmatrixMapper;

@Autowired
private TblControlEntriesMapper tblControlEntriesMapper;
@Autowired
private OrganizationMapper organizationMapper;


    @Override
    public Controlmatrix getControlmatrix(String conmatid) {
        //根据conmatid查询控制措施列表
        List<Controlmatrix> controlmatrix = controlmatrixMapper.getControlmatrix(conmatid);
        // 如果查询结果不为空且至少有一个元素，说明找到了控制措施
        if (controlmatrix != null && controlmatrix.size()>0) {
            // 返回列表中的第一个控制措施对象
        	List<TblControlEntries> entitys=tblControlEntriesMapper.getList(controlmatrix.get(0).getConmatid());
        	controlmatrix.get(0).setEntries(entitys);
            return controlmatrix.get(0);
        }
        return null;
    }

    @Override
    public Controlmatrix getByFlowId(String flowid) {
        List<Controlmatrix> list = baseMapper.selectByFlowId(flowid);
        return list!=null && list.size()>0 ? list.get(0) : null;
    }

    @Override
    public List<Controlmatrix> findTblControlmatrixByRiskCoping(String copingId) {
    	return baseMapper.selectByCopingId(copingId);
    }

	@Override
/**
 * 获取管控矩阵统计数据的方法
 * @param orgid 组织ID
 * @return 返回包含统计结果、组织名称、标签和组织ID的Map对象
 */
	public Map<String, Object> getControlmatrixCount(String orgid) {
		// TODO Auto-generated method stub
    // 创建结果Map对象
		Map<String, Object> result = new HashMap<String, Object>();
    // 创建JSONArray对象
		JSONArray arr = new JSONArray();
		try {
			if(StringUtils.isNotBlank(orgid)){
				int wc = 0;
				int wwc = 0;
				int yq=0;
				TblControlEntries entity = new TblControlEntries();
				entity.setLinkOrgId(new BigDecimal(orgid));
				List<TblControlEntries>   list = tblControlEntriesMapper.getGroupList2(entity);
				for (TblControlEntries e : list) {
					if (StringUtils.isNotBlank(e.getField4())&&e.getField4().equals("是")) {
						wc++;
						if (StringUtils.isNotBlank(e.getField11())&&e.getField11().equals("是")) {
							yq++;
						}
					} else if (StringUtils.isNotBlank(e.getField4())&&e.getField4().equals("否")) { // 存在历史数据，当时不是存储的是否完成，需要排除这些数据
						wwc++;
						if (StringUtils.isNotBlank(e.getField11())&&e.getField11().equals("是")) {
							yq++;
						}
					} 
					
				}
				result.put("wc", wc);
				result.put("wwc", wwc );
				result.put("yq", yq );
			}else{
        // 获取集团公司的ID
			String groupCompanyID = SysConfig.get("groupCompanyID");
        // 创建数据列表
			List<Map<String, String>> dataList = new ArrayList<>();
			// 需要统计的标签
			List<String> tags = Arrays.asList("wc", "wwc", "yq");
			// 总公司
			TblControlEntries entity = new TblControlEntries();
			entity.setLinkOrgId(new BigDecimal(groupCompanyID));
			List<TblControlEntries> list = tblControlEntriesMapper.getGroupList(entity);
			List<BigDecimal> orgids = list.stream().map(TblControlEntries::getField3DeptId).distinct()
					.collect(Collectors.toList());
			List<String> orgName = list.stream().map(TblControlEntries::getField3DeptName).distinct()
					.collect(Collectors.toList());
			Map<String, String> map = new LinkedHashMap();
			int i=0;
			for (TblControlEntries e : list) {
				if (StringUtils.isNotBlank(e.getField4())&&e.getField4().equals("是")) {
					map.put(e.getField3DeptName()+(i++), "wc");
					if (StringUtils.isNotBlank(e.getField11())&&e.getField11().equals("是")) {
						map.put(e.getField3DeptName()+(i++), "yq");
					}
				} else if (StringUtils.isNotBlank(e.getField4())&&e.getField4().equals("否")) { // 存在历史数据，当时不是存储的是否完成，需要排除这些数据
					map.put(e.getField3DeptName()+(i++), "wwc");
					if (StringUtils.isNotBlank(e.getField11())&&e.getField11().equals("是")) {
						map.put(e.getField3DeptName()+(i++), "yq");
					}
				}  
			}
			dataList.add(map);
			List<TblOrganization> orgList2 = controlmatrixMapper.getControlmatrixOrg(groupCompanyID);
			orgName.addAll(orgList2.stream().map(TblOrganization::getOrgname).collect(Collectors.toList()));
			orgids.addAll(orgList2.stream().map(TblOrganization::getOrgid).collect(Collectors.toList()));
			orgName = orgName.stream().distinct().collect(Collectors.toList());
			orgids = orgids.stream().distinct().collect(Collectors.toList());
			 map = new HashMap<>();
			for (TblOrganization m : orgList2) {
				entity.setLinkOrgId(m.getOrgid());
				list = tblControlEntriesMapper.getGroupList2(entity);
				for (TblControlEntries e : list) {
					if (StringUtils.isNotBlank(e.getField4())&&e.getField4().equals("是")) {
						map.put(e.getField3DeptName()+(i++), "wc");
						if (StringUtils.isNotBlank(e.getField11())&&e.getField11().equals("是")) {
							map.put(e.getField3DeptName()+(i++), "yq");
						}
					} else if (StringUtils.isNotBlank(e.getField4())&&e.getField4().equals("否")) { // 存在历史数据，当时不是存储的是否完成，需要排除这些数据
						map.put(e.getField3DeptName()+(i++), "wwc");
						if (StringUtils.isNotBlank(e.getField11())&&e.getField11().equals("是")) {
							map.put(e.getField3DeptName()+(i++), "yq");
						}
					} 
					
				}
			}
			dataList.add(map);
			Map<String, List<Integer>> results = countData(dataList, orgName, tags);
			result.put("results", results);
			result.put("orgnames", orgName);
			result.put("tags", tags);
			result.put("orgids", orgids);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	
 
	
	
	@Override
	public Map<String, Object> getControlmatrixList(String orgid, String type) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String typeName="";
			TblControlEntries e=new TblControlEntries();
			if(type.equals("wc")){
				e.setField4("是");
			}else if(type.equals("wwc")){
				e.setField4("否");
			}else{
				e.setField11("是");
			}
			String groupCompanyID = SysConfig.get("groupCompanyID");
			Integer number=organizationMapper.checkDeptByOrgid(groupCompanyID,orgid);
			List<TblControlEntries> list=null;
			if(number>0){ //传递的是有限公司的部门id
				e.setLinkDeptId(new BigDecimal(orgid));
			  list=tblControlEntriesMapper.getGroupList(e);
			}else{//传递的是分公司id
				e.setLinkOrgId(new BigDecimal(orgid));
				  list=tblControlEntriesMapper.getGroupList2(e);
			}
			result.put("data",  list.toArray());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	public static Map<String, List<Integer>> countData(
            List<Map<String, String>> data,
            List<String> xName,
            List<String> tags) {
        
        // 初始化结果Map
        Map<String, List<Integer>> result = new LinkedHashMap();
        for (String tag : tags) {
            List<Integer> counts = new ArrayList<>();
            for (int i = 0; i < xName.size(); i++) {
                counts.add(0);
            }
            result.put(tag, counts);
        }

        // 遍历数据并统计
        for (Map<String, String> entry : data) {
            for (Map.Entry<String, String> item : entry.entrySet()) {
                String department = item.getKey();
                String tag = item.getValue();
                
                // 检查是否是我们要统计的tag
                if (tags.contains(tag)) {
                    // 查找部门在xName中的索引
                    int index = xName.indexOf(department.substring(0, department.length() - 1));
                    for(int i=0;i<xName.size();i++){
                    	 if (department.contains(xName.get(i))) {
                    		  List<Integer> counts = result.get(tag);
                              // 增加对应位置的计数
                              counts.set(i, counts.get(i) + 1);
                    		 break;
                         }
                    }
                }
            }
        }

        return result;
    }
}
