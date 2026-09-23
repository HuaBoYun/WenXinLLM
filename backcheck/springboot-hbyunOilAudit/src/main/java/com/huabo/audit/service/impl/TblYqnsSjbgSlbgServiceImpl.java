package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSjbgdg;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSlbg;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjbgSlbgMapper;
import com.huabo.audit.service.ImplementPlanService;
import com.huabo.audit.service.TblYqnsSjbgSlbgService;
import com.huabo.audit.util.PageResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangxilu
 * @description 针对表【TTBL_YQNS_SJBG_JHYJG(交换意见稿表)】的数据库操作Service实现
 */
@Service
public class TblYqnsSjbgSlbgServiceImpl extends ServiceImpl<TblYqnsSjbgSlbgMapper, TblYqnsSjbgSlbg>
        implements TblYqnsSjbgSlbgService {
    TblStaffUtil loginStaff;
    @Resource
    private ImplementPlanMapper implementPlanMapper;
    
    
    @Resource
    private ImplementPlanService implementPlanService;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjbgSlbg vo) throws Exception {
        /*JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;*/
        /*PageInfo<TblYqnsSjbgSlbg> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        //创建分页对象
        IPage<TblYqnsSjbgSlbg> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsSjbgSlbg> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getDocument())) {
            wrapper.lambda().like(TblYqnsSjbgSlbg::getDocument, vo.getDocument());
        }
        if (StringUtils.isNotBlank(vo.getTitle())) {
            wrapper.lambda().like(TblYqnsSjbgSlbg::getTitle, vo.getTitle());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsSjbgSlbg::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsSjbgSlbg::getEndDate, vo.getEndDate());
        }
        
//        if(vo.getProjectId()==null){
//          	 ImplementPlanEntity tnp = implementPlanService.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
//          	 vo.setProjectId(tnp.getId());
//          } 
          if(vo.getProjectId() != null) {
          	wrapper.lambda().eq(TblYqnsSjbgSlbg::getProjectId, vo.getProjectId());
          }
          if (StringUtils.isNotBlank(vo.getProjectName())) {
       	   	wrapper.and(q -> q.inSql("PROJECT_ID", " select ID from TBL_YQNS_IMPLEMENT_PLAN where PROJECT_NAME like '%"+vo.getProjectName()+"%' "));
          }
        
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        PageInfo<TblYqnsSjbgSlbg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsSjbgSlbg> page = new PageResult<TblYqnsSjbgSlbg>().build(pageInfo);
        List<TblYqnsSjbgSlbg> tlist = page.getTlist();
        if (null != tlist && !tlist.isEmpty()) {
            tlist.forEach(it -> {
                if (null != it.getProjectId()) {
					try {
						ImplementPlanEntity entity = implementPlanMapper.selectById(it.getProjectId().toString());
						it.setProjectName(entity.getProjectName());
	                    it.setZsname(entity.getZsname());

                        //构建预留字段返回
                        reservePropertyService.buildReserveProperty(it);
					} catch (Exception e) {
						e.printStackTrace();
					}
                    
                }
            });
        }
        return ResponseFormat.retParam(1, 200, page);
    }


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsSjbgSlbg vo) throws Exception {
        /*JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;*/
    	 if(vo.getSlbgid()==null) {
         	vo.setSlbgid(RandomUtil.uuLongId());
         }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttByPk(vo.getSlbgid().toString());
        List<String> attIds = vo.getAttIds();
        List<Map<String, String>> mapList = vo.getAttList();
        if(null != mapList){
            mapList.forEach(it ->{
                String ids = it.get("id");
                String[] idStr = ids.split(",");
                for (String id: idStr) {
                    String type = it.get("type");
                    this.baseMapper.saveAtt2(vo.getSlbgid().toString(), id, type);
                }

            });
        }
//        if (attIds != null && attIds.size() > 0) {
//            for (String attId : attIds) {
//                this.baseMapper.saveAtt(vo.getSlbgid().toString(), attId);
//            }
//        }
        return ResponseFormat.retParam(1, 200, vo);
    }

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean detail(String token, TblYqnsSjbgSlbg vo) throws Exception {
        /*JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;*/
        TblYqnsSjbgSlbg bean = this.getById(vo.getSlbgid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getSlbgid().toString());
        bean.setAttachments(attachments);
        if (null != bean.getProjectId()) {
            String projectName = implementPlanMapper.selectProjectNameById(bean.getProjectId());
            bean.setProjectName(projectName);
        }

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);

        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean delete(String token, TblYqnsSjbgSlbg vo) throws Exception {
        /*JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;*/
        boolean ret = this.removeByIds(vo.getIds());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 校验登录token有效性
     *
     * @param token
     * @return null or not null
     * @throws Exception
     */
    private JsonBean validToken(String token) throws Exception {
        loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        return null;
    }
}




