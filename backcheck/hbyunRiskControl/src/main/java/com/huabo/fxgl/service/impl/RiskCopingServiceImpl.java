package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.Controlmatrix;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Report;
import com.huabo.fxgl.entity.RiskCoping;
import com.huabo.fxgl.entity.TblControlEntries;
import com.huabo.fxgl.mapper.ControlmatrixMapper;
import com.huabo.fxgl.mapper.RiskCopingMapper;
import com.huabo.fxgl.mapper.TblControlEntriesMapper;
import com.huabo.fxgl.service.IRiskCopingService;

import cn.hutool.json.ObjectMapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Service
public class RiskCopingServiceImpl extends ServiceImpl<RiskCopingMapper, RiskCoping> implements IRiskCopingService {
	
	@Resource
	private RiskCopingMapper riskCopingMapper;
	
	@Resource
	private ControlmatrixMapper controlmatrixMapper;
	
	@Resource
	private TblControlEntriesMapper tblControlEntriesMapper;
	@Resource
    private UserProvider userProvider;
	
	@Override
    public boolean save(RiskCoping riskCoping , String  attids) {
        //保存 Report 数据
		riskCopingMapper.insert(riskCoping);
        if (StringUtils.isNotBlank(attids)) {
            String[] ids = attids.split(",");
            for (int i = 0; i < ids.length; i++) {
                baseMapper.insertRCAtt(riskCoping.getRiskcopingid(), new BigDecimal(ids[i]));
            }
        }
        return true;
    }

    @Override
    public boolean updateById(RiskCoping riskCoping, String attids) {
        //更新 Report 数据
    	riskCopingMapper.updateById(riskCoping);
        if (StringUtils.isNotBlank(attids)) {
            baseMapper.deleteRCAtt(riskCoping.getRiskcopingid());
            String[] ids = attids.split(",");
            for (int i = 0; i < ids.length; i++) {
                baseMapper.insertRCAtt(riskCoping.getRiskcopingid(), new BigDecimal(ids[i]));
            }
        }
        return true;
    }
    
    @Override
    public boolean updateByEval(RiskCoping riskCoping) {
        //更新 Report 数据
    	riskCopingMapper.updateById(riskCoping);
        return true;
    }

	@Override
	public JsonBean findRiskCopyingCountByRiskId(String token, BigDecimal riskid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		return ResponseFormat.retParam(1, 200, this.riskCopingMapper.selectRiskCopyingCountByRiskId(riskid));
	}

	@Override
	public Map<String, Object> getCompanyRiskResponse(String token, String year) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map=new HashMap<String, Object>();
		 try {
			List<Organization> orgList=riskCopingMapper.getCopingCompanyList();
			int[] yList=new int[orgList.size()];
			String[] xList=new String[orgList.size()];
			for(int i=0;i<orgList.size();i++){
				Organization o=orgList.get(i);
				Integer s=riskCopingMapper.getCopingCountByCompanyYear(o.getOrgid(),year);
				yList[i]=s;
				xList[i]=o.getOrgname();
			}
			map.put("x", xList);
			map.put("y", yList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public void convertControlMeasures(String token) throws Exception {
		// TODO Auto-generated method stub
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<Controlmatrix> list = controlmatrixMapper.getControlmatrixList();
		int i=0;
            for(Controlmatrix c:list){
				String json=c.getExtJson();
				com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
	            List<Map<String, Object>> result = mapper.readValue(json, List.class);
	            if (!result.isEmpty()) {
	            	for(Map<String, Object> m:result){
	            		  Date field2=null;
	            		  Date field5=null;
        				  String field1=(m.get("field1")!=null?m.get("field1").toString():"");
        				  if(m.get("field2")!=null&&isValidDate(m.get("field2").toString(), "yyyy-MM-dd")){
	            		     field2=(m.get("field2")!=null?sdf.parse(m.get("field2").toString()):null);
        				  }
	            		  String field3=(m.get("field3")!=null?m.get("field3").toString():"");
	            		  BigDecimal field13=(m.get("field13")!=null?new BigDecimal(m.get("field13").toString()):null);
	            		  String field6=(m.get("field6")!=null?m.get("field6").toString():"");
	            		  String field14=(m.get("field14")!=null?m.get("field14").toString():null);
	            		  String field7=(m.get("field7")!=null?m.get("field7").toString():"");
	            		  String field15=( m.get("field15")!=null?m.get("field15").toString():null);
	            		  String field4= (m.get("field4")!=null?m.get("field4").toString():"");
	            		  String field11=(m.get("field11")!=null?m.get("field11").toString():"");
	            		  if(m.get("field5")!=null&&isValidDate(m.get("field5").toString(), "yyyy-MM-dd")){
		            		  field5=(m.get("field5")!=null?sdf.parse(m.get("field5").toString()):null);
	        				  }
	            		  String field10=(m.get("field10")!=null?m.get("field10").toString():"");
	            		  String field12=(m.get("field12")!=null?m.get("field12").toString():"");
	            		  TblControlEntries entity=new  TblControlEntries(RandomUtil.uuBigDecimalId(),c.getConmatid(),field1,field2,field3,
	            						  field13,field6, field14,field7,field15,field4,field11,field5,field10,field12,"old");
	            		  tblControlEntriesMapper.insert(entity);
	            	i++;
	            	}
	            }
			}
            System.out.println("总个数"+i);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public static boolean isValidDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // 设置为严格模式
        
        try {
            Date date = sdf.parse(dateStr);
            // 额外检查：解析后的日期再格式化成字符串，看是否匹配原始输入
            return dateStr.equals(sdf.format(date));
        } catch (ParseException e) {
            return false;
        }
    }

  
}
