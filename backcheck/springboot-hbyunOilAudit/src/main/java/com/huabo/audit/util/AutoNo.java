package com.huabo.audit.util;

import java.math.BigDecimal;

public class AutoNo {
	
	public static BigDecimal getAutoNo(Integer preStr,BigDecimal no) throws Exception {
		BigDecimal genNo = null;
		if(no != null) {
			genNo = no.add(BigDecimal.valueOf(1));
		}else {
			String pre = preStr.toString();
			String genNoStr = pre + "0001";
			genNo = new BigDecimal(genNoStr);
		}
		return genNo;
	}
}
