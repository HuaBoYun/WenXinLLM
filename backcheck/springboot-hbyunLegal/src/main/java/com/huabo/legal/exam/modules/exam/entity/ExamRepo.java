package com.huabo.legal.exam.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
* <p>
* 考试题库实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-09-05 11:14
*/
@Data
@TableName("tbl_fwgl_exam_repo")
public class ExamRepo extends Model<ExamRepo> {

    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    
    /**
    * 考试ID
    */
    @TableField("examid")
    private String examId;
    
    /**
    * 题库ID
    */
    @TableField("repoid")
    private String repoId;
    
    /**
    * 单选题数量
    */
    @TableField("radiocount")
    private Integer radioCount;
    
    /**
    * 单选题分数
    */
    @TableField("radioscore")
    private Integer radioScore;
    
    /**
    * 多选题数量
    */
    @TableField("multicount")
    private Integer multiCount;
    
    /**
    * 多选题分数
    */
    @TableField("multiscore")
    private Integer multiScore;
    
    /**
    * 判断题数量
    */
    @TableField("judgecount")
    private Integer judgeCount;
    
    /**
    * 判断题分数
    */
    @TableField("judgescore")
    private Integer judgeScore;
    
}
