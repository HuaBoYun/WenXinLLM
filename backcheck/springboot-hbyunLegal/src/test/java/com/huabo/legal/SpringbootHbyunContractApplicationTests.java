package com.huabo.legal;

import com.huabo.legal.util.DiffHandleUtils;
import com.huabo.legal.util.PdfUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class SpringbootHbyunContractApplicationTests {

	private static String path = "C:\\Users\\W\\Desktop\\txt\\" + System.currentTimeMillis() + ".html";

	@Test
	void contextLoads() {

		/*DiffContent.diff();
        ShowDiff.show(path);*/
		//对比 F:\n1.txt和 F:\n2.txt 两个文件，获得不同点
		List<String> diffString = DiffHandleUtils.diffString("C:\\Users\\W\\Desktop\\a.txt", "C:\\Users\\W\\Desktop\\b.txt", "null", "null");
		//在F盘生成一个diff.html文件，打开便可看到两个文件的对比
		DiffHandleUtils.generateDiffHtml(diffString, path);
		System.out.println("success");
	}

	@Test
	void testPdf() {
		List<String> pathList = new ArrayList<String>();
		String targetPDFPath = "C:\\Users\\W\\Desktop\\txt\\target.pdf";
		pathList.add("C:\\Users\\W\\Desktop\\txt\\a.pdf");
		pathList.add("C:\\Users\\W\\Desktop\\txt\\c.pdf");

		try {
			PdfUtils.MergePdf(pathList, targetPDFPath);
		} catch (Exception e) {
			log.error(e.getMessage() + e);
		}
	}

	@Test
	void testPdf1(){
		List<File> files = new ArrayList();
		File file = new File("C:\\Users\\W\\Desktop\\txt");
		File[] tempList = file.listFiles();
		//获取该文件夹下的文件（文件都是PDF）
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				files.add(tempList[i]);
			}
		}
		try {
			File f = PdfUtils.mulFile2One(files, "C:\\Users\\W\\Desktop\\txt\\合成PDF.pdf");
			System.out.println(f.length());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
