package com.huabo.finance.thread;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.huabo.finance.entity.BdFinancedate;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.entity.TblConfigTableInfo;
import com.huabo.finance.service.impl.GatherFinanceDateServiceImpl;
import com.huabo.finance.vr.BdPlanSqlconfigVr;

@Service
public class ThreadManagementService {
    
    private final ExecutorService executor;
    private final Semaphore semaphore;
    private final Map<String, ThreadControl> taskControls = new ConcurrentHashMap<>();
    private final Queue<Runnable> pendingTasks = new ConcurrentLinkedQueue<>();
    
    
    // 内部类用于控制线程
    private static class ThreadControl {
        Future<?> future;
        volatile boolean running = true;
    }
    
    public ThreadManagementService(ExecutorService executor, Semaphore semaphore) {
        this.executor = executor;
        this.semaphore = semaphore;
    }
    
    /**
     * 初始化任务
     * @param tableList 
     */
    public void initTasks(BdFinancedate dataConfig, BdFinanceplan plan, List<BdPlanSqlconfigVr> sqlList,List<TblConfigTableInfo> tableList, GatherFinanceDateServiceImpl gatherFinanceDateServiceImpl) {
       
    	if(plan.getFinanceRange() == 1 || plan.getFinanceRange() == 3) {
    		for (int i = 0; i < sqlList.size(); i++) {
            	BdPlanSqlconfigVr config = sqlList.get(i);
            	String taskId = StringUtils.isNotBlank(config.getFid())?config.getFid()+":"+plan.getFid():config.getSqlconfigid()+":"+plan.getFid();
                pendingTasks.add(() -> {
                    ThreadControl control = taskControls.get(taskId);
                    try {
                        if (!control.running) return;
                        System.out.printf("任务 %d 开始执行 [%s]%n", 
                                        taskId, Thread.currentThread().getName());
                        gatherFinanceDateServiceImpl.executeBatchFinance(config.getFr(),config,dataConfig,plan);
                        if (control.running) {
                            System.out.printf("任务 %d 正常完成%n", taskId);
                        } else {
                            System.out.printf("任务 %d 被终止%n", taskId);
                        }
                    } catch (Exception e) {
                        System.out.printf("任务 %d 被中断%n", taskId);
                        Thread.currentThread().interrupt();
                    } finally {
                        taskControls.remove(taskId);
                        semaphore.release(); // 释放信号量许可
                        startNextTask();     // 尝试启动下一个任务
                    }
                });
            }
    	}
    	
    	if(plan.getFinanceRange() == 2 || plan.getFinanceRange() == 3) {
	        for (TblConfigTableInfo tblInfo : tableList) {
	        	if(StringUtils.isBlank(tblInfo.getDataConfig())) {
					continue;
				}
	        	pendingTasks.add(() -> {
	                ThreadControl control = taskControls.get(tblInfo.getFid());
	                try {
	                    if (!control.running) return;
	                    System.out.printf("任务 %d 开始执行 [%s]%n", 
	                    		tblInfo.getFid(), Thread.currentThread().getName());
	                    gatherFinanceDateServiceImpl.gatherBussinessDataBatch(tblInfo);
	                    if (control.running) {
	                        System.out.printf("任务 %d 正常完成%n", tblInfo.getFid());
	                    } else {
	                        System.out.printf("任务 %d 被终止%n", tblInfo.getFid());
	                    }
	                } catch (Exception e) {
	                    System.out.printf("任务 %d 被中断%n", tblInfo.getFid());
	                    Thread.currentThread().interrupt();
	                } finally {
	                    taskControls.remove(tblInfo.getFid());
	                    semaphore.release(); // 释放信号量许可
	                    startNextTask();     // 尝试启动下一个任务
	                }
	            });
			}
    	}
        
    }

    /**
     * 开始执行任务（先启动5个）
     * @param tableList 
     */
    public synchronized void startExecution(BdFinancedate dataConfig, BdFinanceplan plan, List<BdPlanSqlconfigVr> sqlList,List<TblConfigTableInfo> tableList, GatherFinanceDateServiceImpl gatherFinanceDateServiceImpl) {
        if (!taskControls.isEmpty()) {
            throw new IllegalStateException("任务已在执行中");
        }

        if(plan.getFinanceRange() == 1 || plan.getFinanceRange() == 3) {
        	// 初始化任务控制结构
            for (int i = 0; i < sqlList.size(); i++) {
            	BdPlanSqlconfigVr config = sqlList.get(i);
            	String taskId = StringUtils.isNotBlank(config.getFid())?config.getFid()+":"+plan.getFid():config.getSqlconfigid()+":"+plan.getFid();
                ThreadControl control = new ThreadControl();
                taskControls.put(taskId, control);
            }
        }
        
        
        if(plan.getFinanceRange() == 2 || plan.getFinanceRange() == 3) {
	        for (TblConfigTableInfo tblInfo : tableList) {
	        	if(StringUtils.isBlank(tblInfo.getDataConfig())) {
					continue;
				}
	        	ThreadControl control = new ThreadControl();
	            taskControls.put(tblInfo.getFid(), control);
			}
        }
        
        

        // 启动前5个任务
        for (int i = 0; i < 5 && !pendingTasks.isEmpty(); i++) {
            startNextTask();
        }
    }

    private void startNextTask() {
    	try {
            if (semaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                Runnable task = pendingTasks.poll();
                if (task != null) {
                    // 检查 taskControls 是否为空
                    if (taskControls == null || taskControls.isEmpty()) {
                        semaphore.release();
                        return;
                    }
                    
                    // 查找可用的 ThreadControl
                    ThreadControl control = taskControls.values().stream()
                            .filter(c -> c != null && c.future == null)
                            .findFirst()
                            .orElse(null);
                    
                    if (control != null) {
                        control.future = executor.submit(task);
                    } else {
                        semaphore.release(); // 没有可用的 ThreadControl，释放许可
                    }
                } else {
                    semaphore.release(); // 没有任务了，释放许可
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 停止指定任务
     */
    public boolean stopTask(String taskId) {
        ThreadControl control = taskControls.get(taskId);
        if (control != null) {
            control.running = false;
            if (control.future != null) {
                return control.future.cancel(true);
            }
            return true; // 还未启动的任务也被标记为停止
        }
        return false;
    }

    /**
     * 停止所有任务
     */
    public void stopAll() {
        taskControls.values().forEach(control -> {
            control.running = false;
            if (control.future != null) {
                control.future.cancel(true);
            }
        });
        pendingTasks.clear();
    }

    /**
     * 获取任务状态
     */
    public Map<String, String> getTaskStatuses() {
        Map<String, String> statuses = new LinkedHashMap<>();
        
        taskControls.forEach((id, control) -> {
            if (control.future == null) {
                statuses.put(id, control.running ? "等待中" : "已取消(未开始)");
            } else if (control.future.isDone()) {
                statuses.put(id, control.future.isCancelled() ? "已取消" : "已完成");
            } else {
                statuses.put(id, control.running ? "运行中" : "取消中");
            }
        });
        
        return statuses;
    }

    @PreDestroy
    public void cleanup() {
        stopAll();
        executor.shutdownNow();
    }

	/***
	 * 
	 * private final ThreadManagerService threadManager;

    public ThreadController(ThreadManagerService threadManager) {
        this.threadManager = threadManager;
    }

    @PostMapping("/init")
    public String init() {
        threadManager.initTasks();
        return "已初始化20个任务";
    }

    @PostMapping("/start")
    public String start() {
        threadManager.startExecution();
        return "已开始执行任务（先启动5个）";
    }

    @PostMapping("/stop/{taskId}")
    public String stopTask(@PathVariable String taskId) {
        boolean stopped = threadManager.stopTask(taskId);
        return stopped ? "任务 " + taskId + " 已标记为停止" : "任务不存在";
    }

    @PostMapping("/stop-all")
    public String stopAll() {
        threadManager.stopAll();
        return "所有任务已标记为停止";
    }

    @GetMapping("/status")
    public Map<String, String> status() {
        return threadManager.getTaskStatuses();
    }
	 * 
	 */
}
