package com.huabo.legal.exam.modules.user.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
* <p>
* 考试记录实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-09-21 15:13
*/
@Data
@TableName("tbl_fwgl_user_exam")
public class UserExam extends Model<UserExam> {

    private static final long serialVersionUID = 1L;

	/**
	 * 题目ID
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private String id;
    
    /**
    * 用户ID
    */
    @TableField("userid")
    private String userId;
    
    /**
    * 考试ID
    */
    @TableField("examid")
    private String examId;
    
    /**
    * 考试次数
    */
    @TableField("trycount")
    private Integer tryCount;
    
    /**
    * 最高分数
    */
    @TableField("maxscore")
    private Integer maxScore;
    
    /**
    * 是否通过
    */
    private Boolean passed;
    
    /**
    * 创建时间
    */
    @TableField("createtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**
    * 更新时间
    */
    @TableField("updatetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
}
