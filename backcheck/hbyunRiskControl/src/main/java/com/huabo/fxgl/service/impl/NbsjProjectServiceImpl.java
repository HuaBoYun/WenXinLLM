package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.NbsjProject;
import com.huabo.fxgl.entity.NbsjStaffselect;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.mapper.NbsjProjectMapper;
import com.huabo.fxgl.service.INbsjProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.fxgl.service.INbsjStaffselectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@Service
public class NbsjProjectServiceImpl extends ServiceImpl<NbsjProjectMapper, NbsjProject> implements INbsjProjectService {

    @Autowired
    private INbsjStaffselectService nbsjStaffselectService;

    @Override
    public NbsjProject getSelectProject(BigDecimal staffId) {
     if (staffId==null){
         return null;
     }
     List<NbsjStaffselect> list= nbsjStaffselectService.getByUserId(staffId);
     if (list!=null&&list.size()>0){
        return this.getById(list.get(0).getProjectid());
     }
        return null;
     }
}
