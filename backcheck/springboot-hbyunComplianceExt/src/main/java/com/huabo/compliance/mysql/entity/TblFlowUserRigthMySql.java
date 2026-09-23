package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;

/**
 * TblFlowUserRigth entity. @author MyEclipse Persistence Tools
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FLOW_USER_RIGTH")
@Schema(name="TblFlowUserRigthMySql")
public class TblFlowUserRigthMySql implements java.io.Serializable {

    // Fields
    @Transient
    private TblFlowUserRigthIdMySql id;
    @Transient
    private TblStaffMySql tblStaff;
    @Transient
    private TblFlowMySql tblFlow;

    // Constructors

    /**
     * default constructor
     */
    public TblFlowUserRigthMySql() {
    }

    /**
     * minimal constructor
     */
    public TblFlowUserRigthMySql(TblFlowUserRigthIdMySql id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public TblFlowUserRigthMySql(TblFlowUserRigthIdMySql id, TblStaffMySql tblStaff,
                                 TblFlowMySql tblFlow) {
        this.id = id;
        this.tblStaff = tblStaff;
        this.tblFlow = tblFlow;
    }

    // Property accessors

    public TblFlowUserRigthIdMySql getId() {
        return this.id;
    }

    public void setId(TblFlowUserRigthIdMySql id) {
        this.id = id;
    }

    public TblStaffMySql getTblStaff() {
        return this.tblStaff;
    }

    public void setTblStaff(TblStaffMySql tblStaff) {
        this.tblStaff = tblStaff;
    }

    public TblFlowMySql getTblFlow() {
        return this.tblFlow;
    }

    public void setTblFlow(TblFlowMySql tblFlow) {
        this.tblFlow = tblFlow;
    }

}