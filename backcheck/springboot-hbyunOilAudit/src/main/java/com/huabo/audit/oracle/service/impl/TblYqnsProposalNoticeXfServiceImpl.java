package com.huabo.audit.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblYqnsProposalNoticeXfEntity;
import com.huabo.audit.oracle.mapper.TblYqnsProposalNoticeXfMapper;
import com.huabo.audit.oracle.service.TblYqnsProposalNoticeXfService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CGJ
 * @description 针对表【-审计立项建议通知-分发表】的数据库操作service
 * @Entity TblYqnsProposalNoticeXfServiceImpl
 */
@Service
public class TblYqnsProposalNoticeXfServiceImpl extends ServiceImpl<TblYqnsProposalNoticeXfMapper, TblYqnsProposalNoticeXfEntity>
        implements TblYqnsProposalNoticeXfService {

    @Resource
    private TblYqnsProposalNoticeXfMapper tblYqnsProposalNoticeXfMapper;
    
    @Resource
    private UserProvider userProvider;

    /** 
     * 通过分管领导汇总表id 查询分发人员
     * @param token
     * @param noticeId
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getListByNoticeId(String token,String noticeId) throws Exception{
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblYqnsProposalNoticeXfEntity> query = new LambdaQueryWrapper<TblYqnsProposalNoticeXfEntity>()
                .eq(TblYqnsProposalNoticeXfEntity::getNoticeId, noticeId);
        List<TblYqnsProposalNoticeXfEntity> entityList = tblYqnsProposalNoticeXfMapper.selectList(query);
        result.put("List",entityList);
        return ResponseFormat.retParam(1, "查询成功", result);
    }

    /**
     * 进行人员 分发
     * @param token
     * @param noticeId 1
     * @param userIds [1,2]
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, String noticeId, String userIds) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if(noticeId!=null && noticeId.length()>0) {
        	String[] strings = noticeId.split(",");
        	for (String string : strings) {
				
//        		this.delete(token,string);
//
//                List<Long> useridList = Arrays.stream(userIds.split(","))
//                        .map(String::trim)    // 可选，移除任何多余的空白
//                        .map(Long::parseLong) // 将字符串转换为Long
//                        .collect(Collectors.toList());
//                // 进行分发保存
//                useridList.stream().forEach(userId ->{
//                    TblYqnsProposalNoticeXfEntity  vo=new TblYqnsProposalNoticeXfEntity();
//                    try {
//                        if (null != vo.getId()) {
//                            vo.setUpdateTime(new Date());
//                            vo.setUpdateUser(loginStaff.getStaffid().toString());
//                        } else {
//                            vo.setCreateUser(loginStaff.getStaffid().toString());
//                        }
//                        vo.setNoticeId(new BigDecimal(string));
//                        vo.setUserId(String.valueOf(userId));
//                        boolean ret = this.saveOrUpdate(vo);
//                        if (!ret) {
//                            throw new RuntimeException("Failed to save");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        throw new RuntimeException(e);
//                    }
//                });
			}
        	
        }
        
        
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 删除所有分发人员
     *
     * @param noticeId 1
     * @return
     */
    @Override
    @Transactional
    public JsonBean delete(String token, String noticeId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        LambdaQueryWrapper<TblYqnsProposalNoticeXfEntity> query = new LambdaQueryWrapper<TblYqnsProposalNoticeXfEntity>()
                .eq(TblYqnsProposalNoticeXfEntity::getNoticeId,new BigDecimal(noticeId));
        // 判断是否删除成功
        if(!StringUtils.isEmpty(noticeId)) {
            Integer result = this.tblYqnsProposalNoticeXfMapper.delete(query);
            return result == 0 ? ResponseFormat.retParam(0, 50001,"未删除成功") : ResponseFormat.retParam(1, 200);
        }
        return ResponseFormat.retParam(0,10002,null);
    }


}




