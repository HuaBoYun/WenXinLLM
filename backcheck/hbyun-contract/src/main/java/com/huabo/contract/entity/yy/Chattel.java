package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;
import java.util.List;
/**
 * 动产抵押
 * @author Administrator
 *
 */
@Data
public class Chattel {
	private Integer chattelid;
	private String baseInfo;
	private String changeInfoList;
	private String pawnInfoList;
	private String peopleInfo;
	
	private BaseInfos baseInfos;
	private List<ChangeInfo> changeInfos;
	private List<PawnInfo> pawnInfos;
	private List<PeopleInfos> peopleInfos;
	private Integer companyid;
	private Date createdate;//保存数据时间

}
