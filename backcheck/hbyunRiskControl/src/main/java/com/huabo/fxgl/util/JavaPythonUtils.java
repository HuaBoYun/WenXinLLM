package com.huabo.fxgl.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class JavaPythonUtils {
	
	public static void test1(List<String> list) throws Exception {
            //proc = Runtime.getRuntime().exec("python "+url);// 执行py文件
            //String[] args1 = new String[] { "python", url,unit,map};
			String[] args1 = (String[])list.toArray(new String[list.size()]);
			log.info(list.toString());
            Process proc=Runtime.getRuntime().exec(args1);  
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
	}
}
