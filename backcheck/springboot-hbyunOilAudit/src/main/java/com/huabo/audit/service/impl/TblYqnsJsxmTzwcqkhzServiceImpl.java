package com.huabo.audit.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcgGL;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqk;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqkhz;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblYqnsJsxmTzwcqkhzMapper;
import com.huabo.audit.service.TblYqnsJsxmTzwcqkService;
import com.huabo.audit.service.TblYqnsJsxmTzwcqkhzService;
import com.huabo.audit.util.PageResult;

@Service
public class TblYqnsJsxmTzwcqkhzServiceImpl extends ServiceImpl<TblYqnsJsxmTzwcqkhzMapper, TblYqnsJsxmTzwcqkhz>
        implements TblYqnsJsxmTzwcqkhzService {
	
    TblStaffUtil loginStaff;
    
    private TblYqnsJsxmTzwcqkService tblYqnsJsxmTzwcqkService;
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    
    @Resource
    private UserProvider userProvider;
 

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
    public JsonBean saveOrUpdate(String token, TblYqnsJsxmTzwcqkhz vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
//        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        if (vo.getHzid() != null) {
        	
        } else {
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
        }
       
        if(vo.getHzid()==null) {
            vo.setHzid(RandomUtil.uuLongId());
          }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        
        //保存关系表信息
        baseMapper.deleteLine(vo.getHzid());
        String jsxmtzwcqkids = vo.getJsxmtzwcqkids();
        if(null!=jsxmtzwcqkids) {
        	String[] jsxmtzwcqkidz = jsxmtzwcqkids.split(",");
        	for (String jsxmtzwcqkid:jsxmtzwcqkidz) {
        		baseMapper.saveLine(vo.getHzid(), jsxmtzwcqkid);
			}
        }
        

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
    public JsonBean detail(String token, TblYqnsJsxmTzwcqkhz vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsJsxmTzwcqkhz bean = this.getById(vo.getHzid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }

        List<TblYqnsJsxmTzwcqk> listJsxmTzwcqk = baseMapper.selectSubListById(vo.getHzid());
        if (listJsxmTzwcqk != null && listJsxmTzwcqk.size() > 0) {
            bean.setListJsxmTzwcqk(listJsxmTzwcqk);
        }
        
        if(bean.getOrgid() != null) {
        	bean.setOrgname(tblOrganizationMapper.findNameById(bean.getOrgid()));
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
    public JsonBean delete(String token, TblYqnsJsxmTzwcqkhz vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        boolean ret = this.removeById(vo.getHzid());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        
        //删除子记录
        List<String> subIds = baseMapper.selectSubIdsListById(vo.getHzid());
        if(null!=subIds && subIds.size()>0) {
        	TblYqnsJsxmTzwcqk cond = new TblYqnsJsxmTzwcqk();
            cond.setIds(subIds);
            tblYqnsJsxmTzwcqkService.delete(token, cond);
        }
        
        //删除中间表
        baseMapper.deleteLine(vo.getHzid());
        
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

    /**
     * 汇总查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean hzlist(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmTzwcqkhz vo) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        IPage<TblYqnsJsxmTzwcqkhz> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsJsxmTzwcqkhz> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getHzname())) {
            wrapper.lambda().like(TblYqnsJsxmTzwcqkhz::getHzname, vo.getHzname());
        }
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
       	 wrapper.and(q -> q.eq("cjr", staff.getRealname()).or().inSql("cjr", "SELECT REALNAME from TBL_STAFF where staffid in  (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()+")"));
       }else {
       	 wrapper.and(q -> q.eq("cjr", staff.getRealname()));
       }
        
        //倒序
        wrapper.orderByDesc(true, "HZID");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsJsxmTzwcqkhz> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsJsxmTzwcqkhz> page = new PageResult<TblYqnsJsxmTzwcqkhz>().build(pageInfo);
        List<TblYqnsJsxmTzwcqkhz> tlist = page.getTlist();
        if (null != tlist && !tlist.isEmpty()) {
            tlist.forEach(it -> {
                if (null != it.getOrgid()) {
                    String orgname = tblOrganizationMapper.findNameById(it.getOrgid());
                    it.setOrgname(orgname);
                    //构建预留字段返回
                    reservePropertyService.buildReserveProperty(it);
                }
            });
        }
        return ResponseFormat.retParam(1, 200, page);
    }
    
    @Override
	public JsonBean getJsxmtzList(String token, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        List<TblYqnsJhglJhcgGL> list = this.baseMapper.selectJsxmtzPage();
		return ResponseFormat.retParam(1, 200, list);
	}

    @Override
    public JsonBean getJsxmjgjsList(String token, Integer pageNumber, Integer pageSize,String year) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        List<TblYqnsJhglJhcgGL> list = this.baseMapper.getJsxmjgjsPage(year);
        return ResponseFormat.retParam(1, 200, list);
    }


}