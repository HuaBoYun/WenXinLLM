package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Task;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-08
 */
public interface ITaskService extends IService<Task> {

    public void addList(List<Task> list);

}
