package com.huabo.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
@Getter
@Setter
@TableName("TBL_ZSGX_TEMPLATE")
@Schema(name="TblZsgxTemplate对象")
public class TblZsgxTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("TEMPLATE_ID")
    private String templateId;

    @TableField("ATTACHMENT")
    private String attachment;

    @TableField("BRIEF")
    private String brief;

    @TableField("TITLE2")
    private String title2;

    @TableField("GEN")
    private Integer gen;

    @TableField("GOVERNMENT_DATE")
    private Date governmentDate;

    @TableField("GOVERNMENT_NAMES")
    private String governmentNames;

    @TableField("GOVERNMENT_URL")
    private String governmentUrl;

    @TableField("IS_ANONYMOUS")
    private Integer isAnonymous;

    @TableField("IS_BUNDLE")
    private Integer isBundle;

    @TableField("IS_CCD")
    private Integer isCcd;

    @TableField("IS_GOVERNMENT")
    private Integer isGovernment;

    @TableField("IS_MULTIPLE")
    private Integer isMultiple;

    @TableField("IS_OFFICIAL")
    private Integer isOfficial;

    @TableField("IS_PRO")
    private Integer isPro;

    @TableField("IS_PUBLISHED")
    private Integer isPublished;

    @TableField("MEMBER_PRICE")
    private BigDecimal memberPrice;

    @TableField("TEMPLATE_NUMBER")
    private Integer templateNumber;

    @TableField("PREVIEW")
    private String preview;

    @TableField("PRICE")
    private BigDecimal price;

    @TableField("PUBLISHED_ON")
    private Date publishedOn;

    @TableField("REGION")
    private String region;

    @TableField("SUB_AMOUNT")
    private BigDecimal subAmount;

    @TableField("RANK")
    private Integer rank;

    @TableField("TEMPLATE_TYPE_ID")
    private String templateTypeId;

    @TableField("TEMPLATE_TYPE_NAME")
    private String templateTypeName;

    @TableField("TITLE")
    private String title;

    @TableField("VERSION")
    private Integer version;

    @TableField("CONTENT_LEN")
    private Integer contentLen;

    @TableField("MAIN_POINTS")
    private String mainPoints;

    @TableField("TEMPLATE_CATEGORY_IDS")
    private String templateCategoryIds;

    @TableField("TEMPLATE_CATEGORY_NAMES")
    private String templateCategoryNames;

    @TableField("TEMPLATE_CATEGORY_PARENTIDS")
    private String templateCategoryParentids;

    @TableField("CONTENT_TAG")
    private String contentTag;

    @TableField("VERSION_TAG")
    private String versionTag;

    @TableField("PARTY_TAG")
    private String partyTag;

    @TableField("PREFER")
    private String prefer;

    @TableField("SCENE_FOR")
    private String sceneFor;

    @TableField("SCENE_NOT_FOR")
    private String sceneNotFor;


}
