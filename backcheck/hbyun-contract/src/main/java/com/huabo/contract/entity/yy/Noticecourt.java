package com.huabo.contract.entity.yy;

import java.util.List;

import lombok.Data;

/**
 * 开庭公告
 * @author tj
 *
 */
@Data
public class Noticecourt {
    private String id;
	private String  startDate; //开庭日期（毫秒数）
    private String  judge; //审判长/主审人
    //private String  plaintiff; //原告/上诉人
    private String  courtroom; //法庭
    private String  caseReason; //案由
    private String  contractors; //承办部门
    private String  court; //法院
    private String  caseNo; //案号
    //private String  defendant; //被告/被上诉人
    private String eventTime;
    private String uni;
    private String searchType;
    private String _type;
    private String connList;
    
    private String startTime;//开庭日期 日期
    
    private String litigant;

    private List<Plaintiffs> plaintiff;//原告/上诉人
    private List<Defendants> defendant;//被告/被上诉人


}
