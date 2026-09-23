package com.financial.sharing.vo.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 凭证保存参数
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
public class VoucherSaveParam {

    /**
     * 凭证ID（更新时必填）
     */
    private Long voucherId;

    /**
     * 凭证编号
     */
    @NotBlank(message = "凭证编号不能为空")
    @Size(max = 50, message = "凭证编号长度不能超过50个字符")
    private String voucherNo;

    /**
     * 凭证类型ID
     */
    @NotNull(message = "凭证类型不能为空")
    private Long voucherTypeId;

    /**
     * 凭证类型ID（前端字段兼容）
     */
    @JsonProperty("voucherType")
    private Long voucherType;

    /**
     * 凭证日期
     */
    @NotNull(message = "凭证日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate voucherDate;

    /**
     * 会计期间
     */
    @NotBlank(message = "会计期间不能为空")
    @Size(max = 20, message = "会计期间长度不能超过20个字符")
    private String accountingPeriod;

    /**
     * 凭证摘要
     */
    @Size(max = 500, message = "凭证摘要长度不能超过500个字符")
    private String voucherDesc;

    /**
     * 来源事项ID
     */
    private Long sourceTransactionId;

    /**
     * 来源系统
     */
    @Size(max = 50, message = "来源系统长度不能超过50个字符")
    private String sourceSystem;

    /**
     * 附件数量
     */
    private Integer attachmentCount;

    /**
     * 备注
     */
    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    private String remark;

    /**
     * 账簿ID
     */
    @NotNull(message = "账簿ID不能为空")
    private Long bookId;

    /**
     * 凭证状态(0草稿1已生成2已过账3已取消)
     */
    private Integer voucherStatus;

    /**
     * 凭证分录列表
     */
    @NotEmpty(message = "凭证分录不能为空")
    private List<Map<String, Object>> entries;

    // 显式的getter/setter方法，确保Lombok正确生成
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}