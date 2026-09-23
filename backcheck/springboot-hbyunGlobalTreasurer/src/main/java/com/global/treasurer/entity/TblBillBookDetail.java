package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 票据册明细实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_BOOK_DETAIL")
public class TblBillBookDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "DETAIL_ID", type = IdType.INPUT)
    private Long detailId;

    @TableField("BOOK_ID")
    private Long bookId;

    @TableField("BILL_ID")
    private Long billId;

    @TableField("BILL_NUMBER")
    private String billNumber;

    @TableField("ADD_TIME")
    private Date addTime;

    @TableField("REMOVE_TIME")
    private Date removeTime;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getDetailId() { return detailId; }
    public void setDetailId(Long detailId) { this.detailId = detailId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public Date getAddTime() { return addTime; }
    public void setAddTime(Date addTime) { this.addTime = addTime; }
    public Date getRemoveTime() { return removeTime; }
    public void setRemoveTime(Date removeTime) { this.removeTime = removeTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }

}
