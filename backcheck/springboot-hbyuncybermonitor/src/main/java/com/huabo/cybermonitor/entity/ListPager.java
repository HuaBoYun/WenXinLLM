package com.huabo.cybermonitor.entity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.support.PagedListHolder;


public class ListPager {
	private PagedListHolder plh;
	private String optSelected = "1";
	
	
	public ListPager(List list){
		plh = new PagedListHolder(list);
		plh.setPageSize(15);
	}
	
	public void setPageSize(int size){
		plh.setPageSize(size);
	}
	
	public int getCurrPage() {
		return plh.getPage() + 1;
	}
	public int getPageCount() {
		return plh.getPageCount();
	}

	public int getElementCount(){
		return plh.getNrOfElements();
	}
	public List getPageList(){
		return plh.getPageList();
	}

	public void navigate(String page) {
		if(page.equals("next")){
			plh.nextPage();
		}else if(page.equals("pre")){
			plh.previousPage();
		}else{
			int pnum = Integer.parseInt(page);
			this.optSelected = page;
			plh.setPage(pnum - 1);
		}
	}

	public Map getOptionList(){
		//HashMap<Integer, String> map = new HashMap<Integer, String>();
		HashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
		for(int i = 0; i < plh.getPageCount(); i++){
			map.put(i + 1, (i + 1) + "");
		}
		return map;
	}


	public String getOptSelected() {
		return optSelected;
	}

	public void setOptSelected(String optSelected) {
		this.optSelected = optSelected;
	}
}
