package com.huabo.finance.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

import com.huabo.finance.entity.BdFinancedate;
import com.huabo.finance.entity.BdFinancedateRecord;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.service.impl.GatherFinanceDateServiceImpl;
import com.huabo.finance.vr.BdPlanSqlconfigVr;

public class SampleTask implements Runnable {
	
	private final GatherFinanceDateServiceImpl gatherFinanceDateServiceImpl;
	private final BdFinancedateRecord fr;
	private final BdPlanSqlconfigVr config;
    private final BdFinancedate dataConfig;
    private final BdFinanceplan plan;
    
    
    public SampleTask(BdPlanSqlconfigVr config, BdFinancedateRecord fr, GatherFinanceDateServiceImpl gatherFinanceDateServiceImpl, BdFinancedate dataConfig, BdFinanceplan plan) {
    	this.config = config;
    	this.fr = fr;
    	this.gatherFinanceDateServiceImpl = gatherFinanceDateServiceImpl;
    	this.dataConfig = dataConfig;
    	this.plan = plan;
	}


	@Override
    public void run() {
        try {
        	this.gatherFinanceDateServiceImpl.executeBatchFinance(fr,config,dataConfig,plan);
        } catch (Exception e) {
            System.out.println("任务 " + fr.getRecordname() + " 被中断");
            Thread.currentThread().interrupt();
        }
    }
}
