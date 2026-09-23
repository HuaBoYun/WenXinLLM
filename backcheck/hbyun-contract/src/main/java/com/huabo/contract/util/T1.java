package com.huabo.contract.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class T1 {
	
	private static int groupnum = 3;//分组
	private static int personnum = 2;//每组人数
	private static int persontotal = 6;//总人数
	

	public static void main(String[] args) {
		List<String> users = new ArrayList<String>();
		users.add("虾条");
		users.add("宝宝");
		users.add("熊凯");
		users.add("林夕");
		users.add("鸡翅");
		users.add("8912");
		
		/*Random random = new Random();
		int index = 0;//随机下标
		String result = null;
		for(int i = 1 ; i <= groupnum  ; i ++) {
			result = i+"组：";
			for(int k = 1 ; k <= personnum ; k++) {
				index = random.nextInt(persontotal) + 1;
				result += users.get(index-1)+"、";
				users.remove(index-1);
				persontotal--;
			}
			System.out.println(result);
		}*/
		/*String[] strs = new String[]{"1"};
		System.out.println(strs[0]);*/
		
		String contractno = "GZ-HGFB-202406-19";
		
		contractno = contractno.substring(0,contractno.lastIndexOf("-")-2);
		System.out.println(contractno);
	}

}
