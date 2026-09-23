package com.huabo.finance.thread;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.finance.service.GatherFinanceDateService;

public class GatherDataTask implements Runnable  {

	private final String taskName;
	
	private final String ip;
	
	private final TblStaffUtil staff;
	
	private final String finitsqlid;
	
	private final String finitPlanid;
	
	private final GatherFinanceDateService gatherFinanceDateService ;

	public GatherDataTask() {
        this.taskName = "DefaultTask";
        this.staff = null;
        this.ip = null;
        this.gatherFinanceDateService = null;
        this.finitsqlid = null;
        this.finitPlanid = null;
    }
	
    public GatherDataTask(String taskName,TblStaffUtil staff,String ip,GatherFinanceDateService gatherFinanceDateService,String finitsqlid, String finitPlanid) {
        this.taskName = taskName;
        this.staff = staff;
        this.ip = ip;
        this.gatherFinanceDateService = gatherFinanceDateService;
        this.finitsqlid = finitsqlid;
        this.finitPlanid = finitPlanid;
    }

    @Override
    public void run() {
    	try {
			gatherFinanceDateService.gatherFinanceDataExecuteSql(taskName,ip,staff,finitsqlid,finitPlanid);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
