package com.huabo.cybermonitor.task;

import com.huabo.cybermonitor.task.base.Task;
import org.springframework.stereotype.Service;

@Service
public class WarningTask implements Task {

	@Override
	public void run() {
		System.out.println("统计执行结果--------------------------------------");
	}

}
