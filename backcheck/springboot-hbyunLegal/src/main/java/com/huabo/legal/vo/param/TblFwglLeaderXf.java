package com.huabo.legal.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglLeaderXf implements Serializable {

	@Schema(name = "主键ID")
	private Long id;

	@NotNull(message = "leaderType 类型：1-中心组学习 2-培训研讨 3-普法宣传 4-其他 不能为空")
	@Schema(name = "类型：1-中心组学习 2-培训研讨 3-普法宣传 4-其他")
	private Integer leaderType;

	@Schema(name = "中心组学习-学习主题")
	private String centralGroupTopic;

	@Schema(name = "中心组学习-学习安排")
	private String centralGroupContent;

	@Schema(name = "中心组学习-参加对象")
	private String centralGroupParticipant;

	@Schema(name = "中心组学习-学习时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date centralGroupTime;

	@Schema(name = "中心组学习-新闻链接")
	private String centralGroupNewsLink;

	@Schema(name = "培训研讨-研讨主题")
	private String discussTopic;

	@Schema(name = "培训研讨-研讨主题内容")
	private String discussContent;

	@Schema(name = "培训研讨-参加对象")
	private String discussParticipant;

	@Schema(name = "培训研讨-培训时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date discussTime;

	@Schema(name = "培训研讨-新闻链接")
	private String discussNewsLink;

	@Schema(name = "普法宣传-活动主题")
	private String propagandaTopic;

	@Schema(name = "普法宣传-活动内容")
	private String propagandaContent;

	@Schema(name = "普法宣传-活动对象")
	private String propagandaParticipant;

	@Schema(name = "普法宣传-活动时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date propagandaTime;

	@Schema(name = "普法宣传-新闻链接")
	private String propagandaNewslink;

	@Schema(name = "其他-活动主题")
	private String otherTopic;

	@Schema(name = "其他-活动内容")
	private String otherContent;

	@Schema(name = "其他-活动对象")
	private String otherParticipant;

	@Schema(name = "其他-活动时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date otherTime;

	@Schema(name = "其他-新闻链接")
	private String otherNewslink;

	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Schema(name="状态",hidden=true)
	private Integer state;

	@Schema(name="创建人ID",hidden=true)
	private String creator;

	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

	@Schema(name="工作单位ID",hidden=true)
	private String workUnit;

	@Schema(name="工作单位名称",hidden=true)
	private String workUnitName;

	@Schema(name="所属集团ID",hidden=true)
	private String belongGroup;

	@Schema(name="创建时间",hidden=true)
	private Date createdTime;

	@Schema(name="更新时间",hidden=true)
	private Date updatedTime;

	private static final long serialVersionUID = 1L;
}
