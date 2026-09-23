package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-15
 */
@TableName("TBL_COURSE")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程id
     */
    private BigDecimal courseid;

    /**
     * 课程名称
     */
    private String coursename;

    /**
     * 课程简介
     */
    private String memo;

    /**
     * 上级id
     */
    private BigDecimal parentid;

    /**
     * 海报路径
     */
    private String picurl;

    /**
     * 视频路径
     */
    private String videorul;

    private BigDecimal userid;

    private BigDecimal orgid;

    /**
     * 创建时间
     */
    private LocalDateTime createdate;

    /**
     * 所属系列分类
     */
    private String coursetype;

    /**
     * 排课计划
     */
    private BigDecimal coursenumber;

    private String type;

    /**
     * 课件文档
     */
    private String courseware;

    /**
     * 课件文档路径
     */
    private String coursewareurl;

    public BigDecimal getCourseid() {
        return courseid;
    }

    public void setCourseid(BigDecimal courseid) {
        this.courseid = courseid;
    }
    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getParentid() {
        return parentid;
    }

    public void setParentid(BigDecimal parentid) {
        this.parentid = parentid;
    }
    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
    public String getVideorul() {
        return videorul;
    }

    public void setVideorul(String videorul) {
        this.videorul = videorul;
    }
    public BigDecimal getUserid() {
        return userid;
    }

    public void setUserid(BigDecimal userid) {
        this.userid = userid;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }
    public String getCoursetype() {
        return coursetype;
    }

    public void setCoursetype(String coursetype) {
        this.coursetype = coursetype;
    }
    public BigDecimal getCoursenumber() {
        return coursenumber;
    }

    public void setCoursenumber(BigDecimal coursenumber) {
        this.coursenumber = coursenumber;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getCourseware() {
        return courseware;
    }

    public void setCourseware(String courseware) {
        this.courseware = courseware;
    }
    public String getCoursewareurl() {
        return coursewareurl;
    }

    public void setCoursewareurl(String coursewareurl) {
        this.coursewareurl = coursewareurl;
    }

    @Override
    public String toString() {
        return "Course{" +
            "courseid=" + courseid +
            ", coursename=" + coursename +
            ", memo=" + memo +
            ", parentid=" + parentid +
            ", picurl=" + picurl +
            ", videorul=" + videorul +
            ", userid=" + userid +
            ", orgid=" + orgid +
            ", createdate=" + createdate +
            ", coursetype=" + coursetype +
            ", coursenumber=" + coursenumber +
            ", type=" + type +
            ", courseware=" + courseware +
            ", coursewareurl=" + coursewareurl +
        "}";
    }
}
