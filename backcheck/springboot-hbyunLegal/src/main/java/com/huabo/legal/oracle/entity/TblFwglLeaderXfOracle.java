package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 领导学法
 */
@Schema(name="TblFwglLeaderXf")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_FWGL_LEADER_XF")
public class TblFwglLeaderXfOracle implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long id;

	/**
	 * 类型：1-中心组学习 2-培训研讨 3-普法宣传 4-其他
	 */
	@Column(name = "LEADERTYPE")
	@Schema(name = "类型：1-中心组学习 2-培训研讨 3-普法宣传 4-其他")
	private Integer leaderType;

	/**
	 * 中心组学习-学习主题
	 */
	@Column(name = "CENTRALGROUPTOPIC")
	@Schema(name = "中心组学习-学习主题")
	private String centralGroupTopic;

	/**
	 * 中心组学习-学习安排
	 */
	@Column(name = "CENTRALGROUPCONTENT")
	@Schema(name = "中心组学习-学习安排")
	private String centralGroupContent;

	/**
	 * 中心组学习-参加对象
	 */
	@Column(name = "CENTRALGROUPPARTICIPANT")
	@Schema(name = "中心组学习-参加对象")
	private String centralGroupParticipant;

	/**
	 * 中心组学习-学习时间
	 */
	@Column(name = "CENTRALGROUPTIME")
	@Schema(name = "中心组学习-学习时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date centralGroupTime;

	/**
	 * 中心组学习-新闻链接
	 */
	@Column(name = "CENTRALGROUPNEWSLINK")
	@Schema(name = "中心组学习-新闻链接")
	private String centralGroupNewsLink;

	/**
	 * 培训研讨-研讨主题
	 */
	@Column(name = "DISCUSSTOPIC")
	@Schema(name = "培训研讨-研讨主题")
	private String discussTopic;

	/**
	 * 培训研讨-研讨主题内容
	 */
	@Column(name = "DISCUSSCONTENT")
	@Schema(name = "培训研讨-研讨主题内容")
	private String discussContent;

	/**
	 * 培训研讨-参加对象
	 */
	@Column(name = "DISCUSSPARTICIPANT")
	@Schema(name = "培训研讨-参加对象")
	private String discussParticipant;

	/**
	 * 培训研讨-培训时间
	 */
	@Column(name = "DISCUSSTIME")
	@Schema(name = "培训研讨-培训时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date discussTime;

	/**
	 * 培训研讨-新闻链接
	 */
	@Column(name = "DISCUSSNEWSLINK")
	@Schema(name = "培训研讨-新闻链接")
	private String discussNewsLink;

	/**
	 * 普法宣传-活动主题
	 */
	@Column(name = "PROPAGANDATOPIC")
	@Schema(name = "普法宣传-活动主题")
	private String propagandaTopic;

	/**
	 * 普法宣传-活动内容
	 */
	@Column(name = "PROPAGANDACONTENT")
	@Schema(name = "普法宣传-活动内容")
	private String propagandaContent;

	/**
	 * 普法宣传-活动对象
	 */
	@Column(name = "PROPAGANDAPARTICIPANT")
	@Schema(name = "普法宣传-活动对象")
	private String propagandaParticipant;

	/**
	 * 普法宣传-活动时间
	 */
	@Column(name = "PROPAGANDATIME")
	@Schema(name = "普法宣传-活动时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date propagandaTime;

	/**
	 * 普法宣传-新闻链接
	 */
	@Column(name = "PROPAGANDANEWSLINK")
	@Schema(name = "普法宣传-新闻链接")
	private String propagandaNewslink;

	/**
	 * 其他-活动主题
	 */
	@Column(name = "OTHERTOPIC")
	@Schema(name = "其他-活动主题")
	private String otherTopic;

	/**
	 * 其他-活动内容
	 */
	@Column(name = "OTHERCONTENT")
	@Schema(name = "其他-活动内容")
	private String otherContent;

	/**
	 * 其他-活动对象
	 */
	@Column(name = "OTHERPARTICIPANT")
	@Schema(name = "其他-活动对象")
	private String otherParticipant;

	/**
	 * 其他-活动时间
	 */
	@Column(name = "OTHERTIME")
	@Schema(name = "其他-活动时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date otherTime;

	/**
	 * 其他-新闻链接
	 */
	@Column(name = "OTHERNEWSLINK")
	@Schema(name = "其他-新闻链接")
	private String otherNewslink;

	/**
	 * 上传文件ids 多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name = "状态")
	private Integer state;

	/**
	 * 创建人ID
	 */
	@Column(name = "CREATOR")
	@Schema(name = "创建人ID")
	private String creator;

	@Transient
	@Schema(name = "创建人名称")
	private String creatorName;

	/**
	 * 工作单位ID
	 */
	@Column(name = "WORKUNIT")
	@Schema(name = "工作单位ID")
	private String workUnit;

	@Transient
	@Schema(name="工作单位名称")
	private String workUnitName;

	/**
	 * 所属集团ID
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name = "所属集团ID")
	private String belongGroup;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATEDTIME")
	@Schema(name = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATEDTIME")
	@Schema(name = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblFwglLeaderXfOracle ofId(Long id) {
		TblFwglLeaderXfOracle tblFwglLeaderXfOracle = new TblFwglLeaderXfOracle();
		tblFwglLeaderXfOracle.setId(id);
		return tblFwglLeaderXfOracle;
	}
}