package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 著作权
 * @author tjj
 *
 */
@Data
public class Copyright {
	private Integer copyrightid;
	private  String id;
    private  String _type;
    private  String regtime;
    private  String publishtime;
    private  String authorNationality;
    private  String simplename;
    private  String connList;
    private  String regnum;
    private  String catnum;
    private  String searchType;
    private  String uni;
    private  String eventTime;
    private  String fullname;
    private  String version;
    private String publishdate;
    private Integer companyid;
 	private Date createdate;//保存数据时间

	private String finishTime;
	private String type;


    
}
