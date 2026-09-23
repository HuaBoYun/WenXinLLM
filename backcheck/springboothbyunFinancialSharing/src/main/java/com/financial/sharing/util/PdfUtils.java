package com.financial.sharing.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PdfUtils {

	/**
	 *
	 * @param pathList
	 * @param targetPDFPath
	 * @throws Exception
	 */
	public static void MergePdf(List<String> pathList, String targetPDFPath) throws Exception {
		List<InputStream> inputStreams = new ArrayList<>();
		for (String path : pathList) {
			inputStreams.add(new FileInputStream(new File(path)));
		}
		PDFMergerUtility mergePdf = new PDFMergerUtility();
		File file = new File(targetPDFPath);

		if (!file.exists()) {
			file.delete();
		}

		mergePdf.addSources(inputStreams);
		mergePdf.setDestinationFileName(targetPDFPath);
		mergePdf.mergeDocuments();
		for (InputStream in : inputStreams) {
			if (in != null) {
				in.close();
			}
		}
	}

	// pdf合并工具类
	public static File mulFile2One(List<File> files, String targetPath) throws Exception {
		PDFMergerUtility mergePdf = new PDFMergerUtility();
		for (File f : files) {
			if (f.exists() && f.isFile()) {
				// 循环添加要合并的pdf
				mergePdf.addSource(f);
			}
		}
		// 设置合并生成pdf文件名称
		mergePdf.setDestinationFileName(targetPath);
		// 合并pdf
		mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
		return new File(targetPath);
	}

}
