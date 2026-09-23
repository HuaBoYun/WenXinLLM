package com.huabo.system.constant;

import com.huabo.system.entity.TblSystemDistribution;
import com.huabo.system.vo.TblSystemDistributionTypeVo;
import com.huabo.system.vo.TblSystemDistributionVo;

public class DistributionTypeFunc {
	
	/**
	 * 新增下发功能时，TblSystemDistribution 实体类新增对应的 *TYPE 下发类型、 *URL下发判断条件、 *TEXT下发类型名称；
	 * setDistributionTypeInfo 方法同步新增   case when 新增判断条件 通过*url判断 保存对应的 type,url,text; case *url == formtype, distribution.set对应的type、url
	 * 	getDistributionTypeInfo 方法同步新增 case when 新增判断条件 通过*type 获取对应的*text ; case *type == dist.getdistype ,vo.settextname(*text) ,放入对应文本
	 */

	//下发数据保存时 保存下发单据类型
	public static void setDistributionTypeInfo(String formType, TblSystemDistribution distribution) {
		switch (formType) {
		case "666":
			//审计立项建议通知
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.SJLXJYTZURL);
			distribution.setDistributionType(TblSystemDistribution.SJLXJYTZTYPE);
			break;
		case "1511":
			//立项建议表
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.LXJYBURL);
			distribution.setDistributionType(TblSystemDistribution.LXJYBTYPE);
			break;
		case "1386":
			//需求建议表
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.XQJYBURL);
			distribution.setDistributionType(TblSystemDistribution.XQJYBTYPE);
			break;
		case "1388":
			//分管领导汇总
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.FGLDHZURL);
			distribution.setDistributionType(TblSystemDistribution.FGLDHZTYPE);
			break;
		case "1387":
			//服务需求表
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.FWXQBURL);
			distribution.setDistributionType(TblSystemDistribution.FWXQBTYPE);
			break;
		case "1396":
			//二级单位及成员单位离任审计
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.EJDWJCYLRSJURL);
			distribution.setDistributionType(TblSystemDistribution.EJDWJCYLRSJTYPE);
			break;
		case "1495":
			//未委托及预计离任
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.WWTJYJLRURL);
			distribution.setDistributionType(TblSystemDistribution.WWTJYJLRTYPE);
			break;
		case "1395":
			//三级单位离任审计
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.SJDWLRSJURL);
			distribution.setDistributionType(TblSystemDistribution.SJDWLRSJTYPE);
			break;
		case "1497":
			//工作方案
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.GZFAJURL);
			distribution.setDistributionType(TblSystemDistribution.GZFATYPE);
			break;
		case "1524":
			//审计项目表
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.SJQKBURL);
			distribution.setDistributionType(TblSystemDistribution.SJQKBTYPE);
			break;
		case "627":
			//审计项目制度
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.SJXMZDURL);
			distribution.setDistributionType(TblSystemDistribution.SJXMZDTYPE);
			break;
		case "663":
			//审计项目制度
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.SJDWLRSJURL);
			distribution.setDistributionType(TblSystemDistribution.SJDWLRSJTYPE);
			break;
		case "569272297635909":
			//IP清单
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.IPQDURL);
			distribution.setDistributionType(TblSystemDistribution.IPQDTYPE);
			break;
		case "80010":
			//优秀项目评选
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.YXXMPYXURL);
			distribution.setDistributionType(TblSystemDistribution.YXXMPYXTYPE);
			break;
		case "1465":
			//工程审计项目安排
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.GCXMAPBURL);
			distribution.setDistributionType(TblSystemDistribution.GCXMAPBTYPE);
			break;
		case "1466":
			//财务审计项目安排
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.CWXMAPBURL);
			distribution.setDistributionType(TblSystemDistribution.CWXMAPBTYPE);
			break;
		case "579594840944709":
			//财务督导分工
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.CWDDFGURL);
			distribution.setDistributionType(TblSystemDistribution.CWDDFGTYPE);
			break;
		case "579594969821253":
			//工程督导分工
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.GCDDFGURL);
			distribution.setDistributionType(TblSystemDistribution.GCDDFGTYPE);
			break;
		case "573766053322821":
			//理论研讨通知
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.LLYTTZURL);
			distribution.setDistributionType(TblSystemDistribution.LLYTTZTYPE);
			break;
		case "800022":
			//理论研讨通知
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.WTDZURL);
			distribution.setDistributionType(TblSystemDistribution.WTDZTYPE);
			break;
		case "80051":
			//获奖通知
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.HJTZURL);
			distribution.setDistributionType(TblSystemDistribution.HJTZTYPE);
			break;
			
		case "1356":
			//理论研讨通知
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.SJTZSURL);
			distribution.setDistributionType(TblSystemDistribution.SJTZSTYPE);
			break;
		case "617366174068805":
			//项目评优-获奖通知
			distribution.setModuleType("yqns");
			distribution.setPageUrl(TblSystemDistribution.XMHJTZURL);
			distribution.setDistributionType(TblSystemDistribution.XMHJTZTYPE);
			break;
		case "95":
			//整改追责下的 整改方案下发
			distribution.setModuleType("zhjd");
			distribution.setPageUrl(TblSystemDistribution.ZGFAURL);
			distribution.setDistributionType(TblSystemDistribution.ZGFATYPE);
			break;
		case "97":
			//整改追责下的 整改分派下发
			distribution.setModuleType("zhjd");
			distribution.setPageUrl(TblSystemDistribution.ZGFPURL);
			distribution.setDistributionType(TblSystemDistribution.ZGFPTYPE);
			break;
		case "219":
			//智能审计-审计通知
			distribution.setModuleType("znsj");
			distribution.setPageUrl(TblSystemDistribution.ZNSJSJTZSURL);
			distribution.setDistributionType(TblSystemDistribution.ZNSJSJTZSTYPE);
			break;
		case "231":
			//智能审计-审计结果文书
			distribution.setModuleType("znsj");
			distribution.setPageUrl(TblSystemDistribution.SJJGWSURL);
			distribution.setDistributionType(TblSystemDistribution.SJJGWSTYPE);
			break;
		case "263":
			//风险-评估计划
			distribution.setModuleType("fxgk");
			distribution.setPageUrl(TblSystemDistribution.PGJHURL);
			distribution.setDistributionType(TblSystemDistribution.PGJHTYPE);
			break;
		case "633657419219013":
			//风险-//重大风险创建
			distribution.setModuleType("fxgk");
			distribution.setPageUrl(TblSystemDistribution.ZDFXCJURL);
			distribution.setDistributionType(TblSystemDistribution.ZDFXCJTYPE);
			break;
		case "182":
			//内控--测试方案
			distribution.setModuleType("nkhg");
			distribution.setPageUrl(TblSystemDistribution.CSFAURL);
			distribution.setDistributionType(TblSystemDistribution.CSFATYPE);
			break;	
		case "183":
			//内控-测试方案处室负责人分派给业务人员后
			distribution.setModuleType("nkhg");
			distribution.setPageUrl(TblSystemDistribution.CSRWURL);
			distribution.setDistributionType(TblSystemDistribution.CSRWTYPE);
			break;	
		case "647125403299909":
			//内控-集团测试计划下发
			distribution.setModuleType("nkhg");
			distribution.setPageUrl(TblSystemDistribution.JTCSJHURL);
			distribution.setDistributionType(TblSystemDistribution.JTCSJHTYPE);
			break;	
		case "177":
			//内控-评价立项
			distribution.setModuleType("nkhg");
			distribution.setPageUrl(TblSystemDistribution.PJLXURL);
			distribution.setDistributionType(TblSystemDistribution.PJLXTYPE);
			break;	
		case "642506393923653":
			//风险-集团评估计划
			distribution.setModuleType("fxgk");
			distribution.setPageUrl(TblSystemDistribution.JTPGJHURL);
			distribution.setDistributionType(TblSystemDistribution.JTPGJHTYPE);
			break;	
		case "172049951177045":
			//风险-风险监测指标创建
			distribution.setModuleType("fxgk");
			distribution.setPageUrl(TblSystemDistribution.FXJCZBCJURL);
			distribution.setDistributionType(TblSystemDistribution.FXJCZBCJTYPE);
			break;	
		case "217":
			//项目资料下发后--跳转页面：项目资料
			distribution.setModuleType("znsj");
			distribution.setPageUrl(TblSystemDistribution.XMZLURL);
			distribution.setDistributionType(TblSystemDistribution.XMZLTYPE);
			break;	
		case "213":
			//任务分配下发后--跳转页面：我的任务
			distribution.setModuleType("znsj");
			distribution.setPageUrl(TblSystemDistribution.RWFPURL);
			distribution.setDistributionType(TblSystemDistribution.RWFPTYPE);
			break;	
		case "53":
			//问题台账下发后--跳转页面：问题台账
			distribution.setModuleType("nkhg");
			distribution.setPageUrl(TblSystemDistribution.WTTZURL);
			distribution.setDistributionType(TblSystemDistribution.WTTZTYPE);
			break;	
		default:
			break;
		}
	}
	
	//获取下发类型时，通过判断获取下发类型名称
	public static TblSystemDistributionTypeVo getDistributionTypeInfo(TblSystemDistributionVo dist) {
		TblSystemDistributionTypeVo vo = new TblSystemDistributionTypeVo();
		vo.setTextValue(dist.getDistributionType());
		switch (dist.getDistributionType()) {
			case TblSystemDistribution.SJLXJYTZTYPE:
				vo.setTextName(TblSystemDistribution.SJLXJYTZTEXT);
				break;
			case TblSystemDistribution.LXJYBTYPE:
				//立项建议表
				vo.setTextName(TblSystemDistribution.LXJYBTEXT);
				break;
			case TblSystemDistribution.XQJYBTYPE:
				//需求建议表
				vo.setTextName(TblSystemDistribution.XQJYBTEXT);
				break;
			case TblSystemDistribution.FGLDHZTYPE:
				//分管领导汇总
				vo.setTextName(TblSystemDistribution.FGLDHZTEXT);
				break;
			case TblSystemDistribution.FWXQBTYPE:
				//服务需求表
				vo.setTextName(TblSystemDistribution.FWXQBTEXT);
				break;
			case TblSystemDistribution.EJDWJCYLRSJTYPE:
				//二级单位及成员单位离任审计
				vo.setTextName(TblSystemDistribution.EJDWJCYLRSJTEXT);
				break;
			case TblSystemDistribution.WWTJYJLRTYPE:
				//未委托及预计离任
				vo.setTextName(TblSystemDistribution.WWTJYJLRTEXT);
				break;
			case TblSystemDistribution.SJDWLRSJTYPE:
				//三级单位离任审计
				vo.setTextName(TblSystemDistribution.SJDWLRSJTEXT);
				break;
			case TblSystemDistribution.GZFATYPE:
				//工作方案
				vo.setTextName(TblSystemDistribution.GZFAJTEXT);
				break;
			case TblSystemDistribution.SJQKBTYPE:
				//审计项目表
				vo.setTextName(TblSystemDistribution.SJQKBTEXT);
				break;
			case TblSystemDistribution.SJXMZDTYPE:
				//审计项目制度
				vo.setTextName(TblSystemDistribution.SJXMZDTEXT);
				break;
			case TblSystemDistribution.IPQDTYPE:
				//IP清单
				vo.setTextName(TblSystemDistribution.IPQDTEXT);
				break;
			case TblSystemDistribution.YXXMPYXTYPE:
				//优秀项目评选
				vo.setTextName(TblSystemDistribution.YXXMPYXTEXT);
				break;
			case TblSystemDistribution.GCXMAPBTYPE:
				//工程审计项目安排
				vo.setTextName(TblSystemDistribution.GCXMAPBTEXT);
				break;
			case TblSystemDistribution.CWXMAPBTYPE:
				//财务审计项目安排
				vo.setTextName(TblSystemDistribution.CWXMAPBTEXT);
				break;
			case TblSystemDistribution.CWDDFGTYPE:
				//财务督导分工
				vo.setTextName(TblSystemDistribution.CWDDFGTEXT);
				break;
			case TblSystemDistribution.GCDDFGTYPE:
				//工程督导分工
				vo.setTextName(TblSystemDistribution.GCDDFGTEXT);
				break;
			case TblSystemDistribution.LLYTTZTYPE:
				//理论研讨通知
				vo.setTextName(TblSystemDistribution.LLYTTZTEXT);
				break;
			case TblSystemDistribution.WTDZTYPE:
				//理论研讨通知
				vo.setTextName(TblSystemDistribution.WTDZTEXT);
				break;
			case TblSystemDistribution.HJTZTYPE:
				//获奖通知
				vo.setTextName(TblSystemDistribution.HJTZTEXT);
				break;
			case TblSystemDistribution.SJTZSTYPE:
				//理论研讨通知
				vo.setTextName(TblSystemDistribution.SJTZSTEXT);
				break;
			case TblSystemDistribution.XMHJTZTYPE:
				//项目评优-获奖通知
				vo.setTextName(TblSystemDistribution.XMHJTZTEXT);
				break;
			case TblSystemDistribution.ZGFATYPE:
				//整改追责下的 整改方案下发
				vo.setTextName(TblSystemDistribution.ZGFATEXT);
				break;
			case TblSystemDistribution.ZGFPTYPE:
				//整改追责下的 整改分派下发
				vo.setTextName(TblSystemDistribution.ZGFPTEXT);
				break;
			case TblSystemDistribution.ZNSJSJTZSTYPE:
				//智能审计-审计通知
				vo.setTextName(TblSystemDistribution.ZNSJSJTZSTEXT);
				break;
			case TblSystemDistribution.SJJGWSTYPE:
				//智能审计-审计结果文书
				vo.setTextName(TblSystemDistribution.SJJGWSTEXT);
				break;
			case TblSystemDistribution.PGJHTYPE:
				//风险-评估计划
				vo.setTextName(TblSystemDistribution.PGJHTEXT);
				break;
			case TblSystemDistribution.ZDFXCJTYPE:
				//风险-//重大风险创建
				vo.setTextName(TblSystemDistribution.ZDFXCJTEXT);
				break;
			case TblSystemDistribution.CSFATYPE:
				//内控--测试方案
				vo.setTextName(TblSystemDistribution.CSFATEXT);
				break;	
			case TblSystemDistribution.CSRWTYPE:
				//内控-测试方案处室负责人分派给业务人员后
				vo.setTextName(TblSystemDistribution.CSRWTEXT);
				break;	
			case TblSystemDistribution.JTCSJHTYPE:
				//内控-集团测试计划下发
				vo.setTextName(TblSystemDistribution.JTCSJHTEXT);
				break;	
			case TblSystemDistribution.PJLXTYPE:
				//内控-评价立项
				vo.setTextName(TblSystemDistribution.PJLXTEXT);
				break;	
			case TblSystemDistribution.JTPGJHTYPE:
				//风险-集团评估计划
				vo.setTextName(TblSystemDistribution.JTPGJHTEXT);
				break;	
			case TblSystemDistribution.FXJCZBCJTYPE:
				//风险-风险监测指标创建
				vo.setTextName(TblSystemDistribution.FXJCZBCJTEXT);
				break;	
			case TblSystemDistribution.XMZLTYPE:
				//项目资料下发后--跳转页面：项目资料
				vo.setTextName(TblSystemDistribution.XMZLTEXT);
				break;	
			case TblSystemDistribution.RWFPTYPE:
				//任务分配下发后--跳转页面：我的任务
				vo.setTextName(TblSystemDistribution.RWFPTEXT);
				break;	
			case TblSystemDistribution.WTTZTYPE:
				//问题台账下发后--跳转页面：问题台账
				vo.setTextName(TblSystemDistribution.WTTZTEXT);
				break;	
			default:
				break;
		}
		return vo;
	}
}
