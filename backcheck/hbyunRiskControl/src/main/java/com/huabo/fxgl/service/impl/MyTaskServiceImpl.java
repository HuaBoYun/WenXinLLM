package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.MyTask;
import com.huabo.fxgl.mapper.MyTaskMapper;
import com.huabo.fxgl.service.IMyTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-17
 */
@Service
public class MyTaskServiceImpl extends ServiceImpl<MyTaskMapper, MyTask> implements IMyTaskService {

}
