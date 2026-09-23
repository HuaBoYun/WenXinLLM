package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 失信人
 * @author tj
 *
 */
@Data
public class Dishonest {
	private Integer honestid;
	private String iname; //失信人名称
    private String businessentity; //法定代表人
    private String gistid; //执行依据文号
    private String areaname; //省份
    private String cardnum; //身份证号／组织机构代码
    private String courtname; //执行法院
    private String publishdate; //发布时间 毫秒数
    private String type; //1-公司 2-人
    private String id; //人或公司id
    private String gistunit; //做出执行依据单位
    private String duty;//法律生效文书确定的义务
    private String performance; //被执行人的履行情况
    private String regdate; //立案时间
    private String casecode;//案号
    private String disrupttypename;//失信被执行人行为具体情形
    private String dishonestid;
    private String _type;
    private String partyTypeName;
    private String focusNumber;
    private String eventTime;
    private String searchType;
    private String uni;
    
    private String publishtime;//发布时间 日期
    private Integer companyid;
  	private Date createdate;//保存数据时间

	private String unperformPart;
	private List<Staff> staff;
	private String performedPart;
	private String redgate;

}
