package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@TableName("TBL_FORM_MODEL_ITEM")
public class FormModelItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long modelId;

    private Long modularId;

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }
    public Long getModularId() {
        return modularId;
    }

    public void setModularId(Long modularId) {
        this.modularId = modularId;
    }

    @Override
    public String toString() {
        return "FormModelItem{" +
            "modelId=" + modelId +
            ", modularId=" + modularId +
        "}";
    }
}
