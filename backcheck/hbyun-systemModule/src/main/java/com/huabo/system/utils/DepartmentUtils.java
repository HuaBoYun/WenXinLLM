package com.huabo.system.utils;

import com.huabo.system.entity.TblAccBook;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DepartmentUtils {

    public static List<TblAccBook> getAllDepartmentss(List<TblAccBook> tolist) {
        List<TblAccBook> list = new ArrayList<TblAccBook>();
        walkDepartmentListall(tolist,"┣",list);
        return list;
    }
    
    private static void  walkDepartmentListall(Collection<TblAccBook> toList, String prefix, List<TblAccBook> list){
        for(TblAccBook top:toList){
            //顶级部门
            TblAccBook copy = new TblAccBook();//使用副本，因为原对象在Session中
            copy.setBookid(top.getBookid());
            copy.setBookname(prefix+top.getBookname());
            list.add(copy);
            //子树
            walkDepartmentListall(top.getChildren(),"　"+prefix,list);
        }

    }
    
}
