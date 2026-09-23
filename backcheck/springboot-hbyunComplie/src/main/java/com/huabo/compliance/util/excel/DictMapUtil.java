package com.huabo.compliance.util.excel;

import com.vip.vjtools.vjkit.collection.MapUtil;

import java.util.HashMap;
import java.util.Map;

public class DictMapUtil {

	public static final String COMPLIANCE_IM_STATE_TYPE = "compliance_im_state_type";
	public static final String STATE_TYPE = "state_type";

	private static Map<String, Map<Object, String>> dictMap = new HashMap<>();

	static {
		Map<Object, String> complianceImStateTypeMap = MapUtil
				.newHashMap(new Integer[]{0, 1, 2, 3, 4, 5, 6}, new String[]{"未审批", "审批中", "已退回", "已撤销", "已终止", "已跟踪", "已完成"});
		dictMap.put(COMPLIANCE_IM_STATE_TYPE, complianceImStateTypeMap);

		Map<Object, String> stateTypeMap = MapUtil.newHashMap(new Integer[]{0, 1}, new String[]{"否", "是"});
		dictMap.put(STATE_TYPE, stateTypeMap);
	}

	public static Map<Object, String> getDictMap(String dict) {
		return dictMap.get(dict);
	}


}
