package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


public class TblSjsQuestion implements Serializable{
	public String sjxm;//审计项目
	public String  zdzccssj;//贯彻落实国家重大政策措施审计
	public String  cwszsj; // 财政财务收支审计
	public String  gdzc;//固定资产投资审计 
	public String  fxgl;//内部控制和风险管理审计,
	public String zrsj;//经济责任审计,
	public String xtsj;//信息系统审计,
	public String jwsj;//境外审计,
	public String qtsj;//其他专项审计,
	public String wbsj;//委托外包项目
	 

	public String wtzgjel;// 金额类,
	public String  tzzm;//调整会计账目,
	public String shzj;//收回资金,
	public String whss;//挽回损失,
	public String bjsf;//补缴税费,
	public String  ghqd;//归还原资金渠道,
	public String  qtje;//其他金额,
	public String fje;// 非金额类,
	public String  xzd;//新制定制度,
	public String  xdzd;//修订完善制度,
	public String yhlc;//优化完善业务流程,
	public String  qt;//其他
	 
	public String wtje;// 金额类,
	public String  jxlwtje;//绩效类问题金额,
	public String  hgxwtje;//  合规性问题金额,
	public String  kjhs;//会计核算方面,
	public String wgsy;//违规使用资金,
	public String   jlcd;//截留沉淀资金,
	public String  sslf;//损失浪费,
	public String  nyzj;//挪用资金,
	public String tsls;//偷漏税费,
	public String  wgqd;//违规取得收入,
	public String  wtqtje;//其他金额类,

	public String wtzs;//问题总数,
	public String jelzs;//金额类总数, 
	public String fjelzs;//非金额类总数, 
	public String gjzc;//国家政策措施落实方面,
	public String fzgh;//发展规划与战略决策方面,
	public String nbkz;//内部控制与风险管理方面,
	public String wtqt;//其他

	public String  wtzgfje;// 非金额, 
	public String wtzgxd;//修订完善制度,
	public String  wtzgxzd;// 新制定制度,
	public String  wtzgyh;//优化完善业务流程,
	public String  wtzgqt;//其他,

	public String  sjgzl;//本填报周期内部审计工作量,

	public String getSjxm() {
		return sjxm;
	}

	public void setSjxm(String sjxm) {
		this.sjxm = sjxm;
	}

	public String getZdzccssj() {
		return zdzccssj;
	}

	public void setZdzccssj(String zdzccssj) {
		this.zdzccssj = zdzccssj;
	}

	public String getCwszsj() {
		return cwszsj;
	}

	public void setCwszsj(String cwszsj) {
		this.cwszsj = cwszsj;
	}

	public String getGdzc() {
		return gdzc;
	}

	public void setGdzc(String gdzc) {
		this.gdzc = gdzc;
	}

	public String getFxgl() {
		return fxgl;
	}

	public void setFxgl(String fxgl) {
		this.fxgl = fxgl;
	}

	public String getZrsj() {
		return zrsj;
	}

	public void setZrsj(String zrsj) {
		this.zrsj = zrsj;
	}

	public String getXtsj() {
		return xtsj;
	}

	public void setXtsj(String xtsj) {
		this.xtsj = xtsj;
	}

	public String getJwsj() {
		return jwsj;
	}

	public void setJwsj(String jwsj) {
		this.jwsj = jwsj;
	}

	public String getQtsj() {
		return qtsj;
	}

	public void setQtsj(String qtsj) {
		this.qtsj = qtsj;
	}

	public String getWbsj() {
		return wbsj;
	}

	public void setWbsj(String wbsj) {
		this.wbsj = wbsj;
	}

	public String getWtzgjel() {
		return wtzgjel;
	}

	public void setWtzgjel(String wtzgjel) {
		this.wtzgjel = wtzgjel;
	}

	public String getTzzm() {
		return tzzm;
	}

	public void setTzzm(String tzzm) {
		this.tzzm = tzzm;
	}

	public String getShzj() {
		return shzj;
	}

	public void setShzj(String shzj) {
		this.shzj = shzj;
	}

	public String getWhss() {
		return whss;
	}

	public void setWhss(String whss) {
		this.whss = whss;
	}

	public String getBjsf() {
		return bjsf;
	}

	public void setBjsf(String bjsf) {
		this.bjsf = bjsf;
	}

	public String getGhqd() {
		return ghqd;
	}

	public void setGhqd(String ghqd) {
		this.ghqd = ghqd;
	}

	public String getQtje() {
		return qtje;
	}

	public void setQtje(String qtje) {
		this.qtje = qtje;
	}

	public String getFje() {
		return fje;
	}

	public void setFje(String fje) {
		this.fje = fje;
	}

	public String getXzd() {
		return xzd;
	}

	public void setXzd(String xzd) {
		this.xzd = xzd;
	}

	public String getXdzd() {
		return xdzd;
	}

	public void setXdzd(String xdzd) {
		this.xdzd = xdzd;
	}

	public String getYhlc() {
		return yhlc;
	}

	public void setYhlc(String yhlc) {
		this.yhlc = yhlc;
	}

	public String getQt() {
		return qt;
	}

	public void setQt(String qt) {
		this.qt = qt;
	}

	public String getWtje() {
		return wtje;
	}

	public void setWtje(String wtje) {
		this.wtje = wtje;
	}

	public String getJxlwtje() {
		return jxlwtje;
	}

	public void setJxlwtje(String jxlwtje) {
		this.jxlwtje = jxlwtje;
	}

	public String getHgxwtje() {
		return hgxwtje;
	}

	public void setHgxwtje(String hgxwtje) {
		this.hgxwtje = hgxwtje;
	}

	public String getKjhs() {
		return kjhs;
	}

	public void setKjhs(String kjhs) {
		this.kjhs = kjhs;
	}

	public String getWgsy() {
		return wgsy;
	}

	public void setWgsy(String wgsy) {
		this.wgsy = wgsy;
	}

	public String getJlcd() {
		return jlcd;
	}

	public void setJlcd(String jlcd) {
		this.jlcd = jlcd;
	}

	public String getSslf() {
		return sslf;
	}

	public void setSslf(String sslf) {
		this.sslf = sslf;
	}

	public String getNyzj() {
		return nyzj;
	}

	public void setNyzj(String nyzj) {
		this.nyzj = nyzj;
	}

	public String getTsls() {
		return tsls;
	}

	public void setTsls(String tsls) {
		this.tsls = tsls;
	}

	public String getWgqd() {
		return wgqd;
	}

	public void setWgqd(String wgqd) {
		this.wgqd = wgqd;
	}

	public String getWtqtje() {
		return wtqtje;
	}

	public void setWtqtje(String wtqtje) {
		this.wtqtje = wtqtje;
	}

	public String getWtzs() {
		return wtzs;
	}

	public void setWtzs(String wtzs) {
		this.wtzs = wtzs;
	}

	public String getJelzs() {
		return jelzs;
	}

	public void setJelzs(String jelzs) {
		this.jelzs = jelzs;
	}

	public String getFjelzs() {
		return fjelzs;
	}

	public void setFjelzs(String fjelzs) {
		this.fjelzs = fjelzs;
	}

	public String getGjzc() {
		return gjzc;
	}

	public void setGjzc(String gjzc) {
		this.gjzc = gjzc;
	}

	public String getFzgh() {
		return fzgh;
	}

	public void setFzgh(String fzgh) {
		this.fzgh = fzgh;
	}

	public String getNbkz() {
		return nbkz;
	}

	public void setNbkz(String nbkz) {
		this.nbkz = nbkz;
	}

	public String getWtqt() {
		return wtqt;
	}

	public void setWtqt(String wtqt) {
		this.wtqt = wtqt;
	}

	public String getWtzgfje() {
		return wtzgfje;
	}

	public void setWtzgfje(String wtzgfje) {
		this.wtzgfje = wtzgfje;
	}

	public String getWtzgxd() {
		return wtzgxd;
	}

	public void setWtzgxd(String wtzgxd) {
		this.wtzgxd = wtzgxd;
	}

	public String getWtzgxzd() {
		return wtzgxzd;
	}

	public void setWtzgxzd(String wtzgxzd) {
		this.wtzgxzd = wtzgxzd;
	}

	public String getWtzgyh() {
		return wtzgyh;
	}

	public void setWtzgyh(String wtzgyh) {
		this.wtzgyh = wtzgyh;
	}

	public String getWtzgqt() {
		return wtzgqt;
	}

	public void setWtzgqt(String wtzgqt) {
		this.wtzgqt = wtzgqt;
	}

	public String getSjgzl() {
		return sjgzl;
	}

	public void setSjgzl(String sjgzl) {
		this.sjgzl = sjgzl;
	}
	
	
	
}
