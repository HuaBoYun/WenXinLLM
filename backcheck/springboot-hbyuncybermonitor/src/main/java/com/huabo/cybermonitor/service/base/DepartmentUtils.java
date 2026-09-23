package com.huabo.cybermonitor.service.base;

import com.huabo.cybermonitor.entity.Accbook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class DepartmentUtils {
//
//	/**
//	 * 遍历部门树，把所有部门遍历出来放到同一个集合中，并且其中所有部门的名称都修改，以表示层次
//	 * 主要是现实成为tree状结构，利用递归的方式查询
//	 * @param tolist
//	 * @return
//	 */
//	public static List<TblAcctbook> getAllDepartments(List<TblAcctbook> tolist) {
//		List<TblAcctbook> list = new ArrayList<TblAcctbook>();
//		walkDepartmentList(tolist,"┣",list);
//		return list;
//	}
//
//
	public static List<Accbook> getAllDepartmentss(List<Accbook> tolist) {
		List<Accbook> list = new ArrayList<Accbook>();
		walkDepartmentListall(tolist,"┣",list);
		return list;
	}

	private static void  walkDepartmentListall(Collection<Accbook> toList,String prefix,List<Accbook> list){
		for(Accbook top:toList){
			//顶级部门
            Accbook copy = new Accbook();//使用副本，因为原对象在Session中
			copy.setBookid(top.getBookid());
			copy.setBookname(prefix+top.getBookname());
			list.add(copy);
			//子树
			//walkDepartmentListall(top.getChildren(),"　"+prefix,list); 缺少字段
		}

	}
//
//
//
//	private static void  walkDepartmentList(Collection<TblAcctbook> toList,String prefix,List<TblAcctbook> list){
//		for(TblAcctbook top:toList){
//			//顶级部门
//			TblAcctbook copy = new TblAcctbook();//使用副本，因为原对象在Session中
//			copy.setId(top.getId());
//			copy.setAcctname(prefix+top.getAcctname());
//			list.add(copy);
//			//子树
//			walkDepartmentList(top.getChildren(),"　"+prefix,list);
//		}
//
//	}
//
//
//	/**
//	 * 遍历指标树，把所有指标遍历出来放到同一个集合中，并且其中所有指标的名称都修改，以表示层次
//	 * 主要是现实成为tree状结构，利用递归的方式查询
//	 * @author tjj
//	 * @param tolist
//	 * @return
//	 */
//	public static List<TblBiDatasource> getAllDepartmentcharts(List<TblBiDatasource> tolist) {
//		List<TblBiDatasource> list = new ArrayList<TblBiDatasource>();
//		walkDepartmentListchert(tolist,"┣",list);
//		return list;
//	}
//	private static void  walkDepartmentListchert(Collection<TblBiDatasource> toList,String prefix,List<TblBiDatasource> list){
//		for(TblBiDatasource top:toList){
//			//if(top.getIsleaf()==null){
//			//顶级部门
//			TblBiDatasource copy = new TblBiDatasource();//使用副本，因为原对象在Session中
//			copy.setDsid(top.getDsid());
//			copy.setDsname(prefix+top.getDsname());
//			list.add(copy);
//			//子树
//			walkDepartmentListchert(top.getChildrens(),"　"+prefix,list);
//			//}
//		}
//
//	}
	

}
