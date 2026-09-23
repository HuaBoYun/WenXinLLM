package com.huabo.cybermonitor.mapper.provider;

public class ForoContrlruleProvider {

    public String calculateFormula(String calFormula){
        return "SELECT "+calFormula+" FROM DUAL";

    }

}
