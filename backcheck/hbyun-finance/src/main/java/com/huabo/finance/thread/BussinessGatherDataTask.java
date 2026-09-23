package com.huabo.finance.thread;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.finance.service.GatherFinanceDateService;

public class BussinessGatherDataTask implements Runnable  {

	private final String taskName;
	
	private final String ip;
	
	private final TblStaffUtil staff;
	
	private final String tableId;
	
	
	private final GatherFinanceDateService gatherFinanceDateService ;

	public BussinessGatherDataTask() {
        this.taskName = "BussinessTask";
        this.staff = null;
        this.ip = null;
        this.gatherFinanceDateService = null;
        this.tableId = null;
    }
	
    public BussinessGatherDataTask(String taskName,TblStaffUtil staff,String ip,GatherFinanceDateService gatherFinanceDateService,String tableId) {
        this.taskName = taskName;
        this.staff = staff;
        this.ip = ip;
        this.gatherFinanceDateService = gatherFinanceDateService;
        this.tableId = tableId;
    }

    @Override
    public void run() {
    	try {
			gatherFinanceDateService.gatherBussinessDataExecuteSql(taskName,ip,staff,tableId);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
