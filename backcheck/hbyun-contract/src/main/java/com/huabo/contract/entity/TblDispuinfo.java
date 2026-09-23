package com.huabo.contract.entity;

import lombok.Data;


@Data
public class TblDispuinfo {

    private String DISPUINFO;

    public String getDISPUINFO() {
        return DISPUINFO;
    }

    public void setDISPUINFO(String DISPUINFO) {
        this.DISPUINFO = DISPUINFO;
    }
}
