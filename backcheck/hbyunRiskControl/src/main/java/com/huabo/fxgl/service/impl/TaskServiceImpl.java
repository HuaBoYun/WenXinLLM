package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.fxgl.entity.Task;
import com.huabo.fxgl.mapper.TaskMapper;
import com.huabo.fxgl.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-08
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void addList(List<Task> list) {

        for (Task tblTask : list) {
            taskMapper.insert(tblTask);
        }
    }


}
