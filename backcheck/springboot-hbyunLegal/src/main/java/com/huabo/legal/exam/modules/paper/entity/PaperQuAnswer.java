package com.huabo.legal.exam.modules.paper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
* <p>
* 试卷考题备选答案实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 17:31
*/
@Data
@TableName("tbl_fwgl_paper_qu_answer")
public class PaperQuAnswer extends Model<PaperQuAnswer> {


    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 试卷ID
     */
    @TableField("paperid")
    private String paperId;

    /**
     * 回答项ID
     */
    @TableField("answerid")
    private String answerId;

    /**
     * 题目ID
     */
    @TableField("quid")
    private String quId;

    /**
     * 是否正确项
     */
    @TableField("isright")
    private Boolean isRight;

    /**
     * 是否选中
     */
    private Boolean checked;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 选项标签
     */
    private String abc;
    
}
