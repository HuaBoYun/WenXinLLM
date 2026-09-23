package com.management.accountant.util.excel;

import com.vip.vjtools.vjkit.collection.MapUtil;

import java.util.HashMap;
import java.util.Map;

public class DictMapUtil {

	public static final String STATE_TYPE = "state_type"; // 会议管理 状态类型 1-启用
	public static final String SHSTATE_TYPE = "shstate_type"; // 审核状态 状态0-未审批、1-审批中、2-需调整、3-已通过、 "4-已终止、5-已跟踪、6-已完成
	public static final String SEX_TYPE = "sex_type"; // 性别类型
	public static final String ISHIRE_TYPE = "isHire_type"; // 是否聘用类型
	public static final String EDUCATION_TYPE = "education_type"; // 学历类型 最高学历1-博士研究生 2-硕士研究生 3-大学本科 4-大学专科 5-中专 6-大学及以下-7
	public static final String SPECIALTY_TYPE = "specialty_type"; // 是否法律专业
	public static final String OCCUPATIONAL_TYPE = "occupational_type"; // 是否法律职业资格
	public static final String ADVISER_TYPE = "adviser_type"; // 是否法律顾问资格
	public static final String EMLOYMENTTERM_TYPE = "employmentTerm_type"; //  聘期类型
	public static final String LAWSERVICE_TYPE = "lawService_type"; //   法律服务类型 1-常年 2-专项
	public static final String ASSESS_TYPE = "assess_type"; //   考核类型 1-外部监管考核 2-子单位考核
	public static final String INSTITUTION_TYPE = "institution_type"; // 制度分类（制度审核） 1-经营管理类-一般制度 2-经营管理类-基本制度 3-经营管理类-重要制度 4-非经营管理类
	public static final String ACTIVITY_TYPE = "activity_type"; // 活动类别 2-文章发表 3-法律培训 5-法律尽调
	public static final String REGISTER_TYPE = "register_type"; // 登记管理表-类别 1-商标 2-版权 3-专利
	public static final String SUBJECT_TYPE = "SUBJECT_type"; // 课题管理表-课题类型 1-开题报告 2-结题报告
	public static final String ASSET_TYPE = "asset_type"; // 类别：上市，未上市
	public static final String HAND_STATUS_TYPE = "hand_status_type"; // 办理状态,0=未办理；1=已办理
	public static final String PERSONNEL_STATUS_TYPE = "personnel_status_type"; // 人员状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派


	private static Map<String, Map<Object, String>> dictMap = new HashMap<>();

	static {
		// 人员状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派
		Map<Object, String> personnelStatusTypeMap = MapUtil.newHashMap(new Integer[]{1, 2, 3, 4}, new String[]{"在岗闲置", "在岗项目内", "请假", "外派"});
		dictMap.put(PERSONNEL_STATUS_TYPE, personnelStatusTypeMap);

		// 办理状态,0=未办理；1=已办理
		Map<Object, String> handStatusTypeMap = MapUtil.newHashMap(new Integer[]{0, 1, 2}, new String[]{"未办理", "已办理", "已办理"});
		dictMap.put(HAND_STATUS_TYPE, handStatusTypeMap);

		// 类别 0-上移资产，1-审计部上市 2-审计部未上市 3-审计中心上市 4-审计中心未上市
		Map<Object, String> assetTypeMap = MapUtil
				.newHashMap(new String[]{"0", "1", "2", "3", "4"}, new String[]{"上移资产", "审计部上市", "审计部未上市", "审计中心上市", "审计中心未上市"});
		dictMap.put(ASSET_TYPE, assetTypeMap);

		// 状态类型 1-启用
		Map<Object, String> prizeTypeMap1 = MapUtil.newHashMap(new Integer[]{1}, new String[]{"启用"});
		dictMap.put(STATE_TYPE, prizeTypeMap1);

		// 审核状态 状态0-未审批、1-审批中、2-需调整、3-已通过、 "4-已终止、5-已跟踪、6-已完成
		Map<Object, String> prizeTypeMap16 = MapUtil
				.newHashMap(new Integer[]{0, 1, 2, 3, 4, 5, 6}, new String[]{"未审批", "审批中", "需调整", "已通过", "已终止", "已跟踪", "已完成"});
		dictMap.put(SHSTATE_TYPE, prizeTypeMap16);


		// 性别类型
		Map<Object, String> prizeTypeMap2 = MapUtil.newHashMap(new Integer[]{0, 1}, new String[]{"女", "男"});
		dictMap.put(SEX_TYPE, prizeTypeMap2);

		// 是否聘用类型
		Map<Object, String> prizeTypeMap3 = MapUtil.newHashMap(new Integer[]{0, 1}, new String[]{"否", "是"});
		dictMap.put(ISHIRE_TYPE, prizeTypeMap3);

		// 学历类型	最高学历:1-博士研究生 2-硕士研究生 3-大学本科 4-大学专科 5-中专 6-大学及以下
		Map<Object, String> prizeTypeMap4 = MapUtil
				.newHashMap(new String[]{"1", "2", "3", "4", "5", "6"}, new String[]{"博士研究生", "硕士研究生", "大学本科", "大学专科", "中专", "大学及以下"});
		dictMap.put(EDUCATION_TYPE, prizeTypeMap4);

		// 是否法律专业
		Map<Object, String> prizeTypeMap5 = MapUtil.newHashMap(new Integer[]{0, 1}, new String[]{"否", "是"});
		dictMap.put(SPECIALTY_TYPE, prizeTypeMap3);

		// 是否法律职业资格
		Map<Object, String> prizeTypeMap6 = MapUtil.newHashMap(new Integer[]{0, 1}, new String[]{"否", "是"});
		dictMap.put(OCCUPATIONAL_TYPE, prizeTypeMap3);

		// 是否法律顾问资格
		Map<Object, String> prizeTypeMap7 = MapUtil.newHashMap(new Integer[]{0, 1}, new String[]{"否", "是"});
		dictMap.put(ADVISER_TYPE, prizeTypeMap3);

		// 聘期类型
		Map<Object, String> prizeTypeMap8 = MapUtil.newHashMap(new Integer[]{0, 1}, new String[]{"一", "二"});
		dictMap.put(EMLOYMENTTERM_TYPE, prizeTypeMap8);

		// 法律服务类型1-常年 2-专项
		Map<Object, String> prizeTypeMap9 = MapUtil.newHashMap(new Integer[]{1, 2}, new String[]{"常年", "专项"});
		dictMap.put(LAWSERVICE_TYPE, prizeTypeMap9);

		// 考核类型 1-外部监管考核 2-子单位考核
		//		Map<Object, String> prizeTypeMap10 = MapUtil.newHashMap(new Integer[]{1,2}, new String[]{"常年","专项"});
		Map<Object, String> prizeTypeMap10 = MapUtil.newHashMap(new Integer[]{1, 2}, new String[]{"外部监管考核", "子单位考核"});
		dictMap.put(ASSESS_TYPE, prizeTypeMap10);

		// 制度分类（制度审核） 1-经营管理类-一般制度 2-经营管理类-基本制度 3-经营管理类-重要制度 4-非经营管理类
		Map<Object, String> prizeTypeMap11 = MapUtil
				.newHashMap(new Integer[]{1, 2, 3, 4}, new String[]{"经营管理类-一般制度", "经营管理类-基本制度", "经营管理类-重要制度", "非经营管理类"});
		dictMap.put(INSTITUTION_TYPE, prizeTypeMap11);

		// 活动类别 1-合同审查 2-诉讼代理 3-文章发表 4-法律培训 5-法律审核 6-法律尽调
		Map<Object, String> prizeTypeMap12 = MapUtil.newHashMap(new Integer[]{2, 3, 5}, new String[]{"文章发表", "法律培训", "法律尽调"});
		dictMap.put(ACTIVITY_TYPE, prizeTypeMap12);

		// 登记管理表-类别 1-商标 2-版权 3-专利
		Map<Object, String> prizeTypeMap13 = MapUtil.newHashMap(new Integer[]{1, 2, 3}, new String[]{"商标", "版权", "专利"});
		dictMap.put(REGISTER_TYPE, prizeTypeMap13);

		// 课题管理表-课题类型 1-开题报告 2-结题报告
		Map<Object, String> prizeTypeMap14 = MapUtil.newHashMap(new Integer[]{1, 2}, new String[]{"开题报告", "结题报告"});
		dictMap.put(SUBJECT_TYPE, prizeTypeMap14);


	}

	public static Map<Object, String> getDictMap(String dict) {
		return dictMap.get(dict);
	}


}
