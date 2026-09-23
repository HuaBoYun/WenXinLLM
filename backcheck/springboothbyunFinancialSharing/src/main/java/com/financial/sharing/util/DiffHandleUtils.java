package com.financial.sharing.util;

import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DiffHandleUtils {

	/**
	 * 对比两文件的差异，返回原始文件+diff格式
	 *
	 * @param original 原文件内容
	 * @param revised  对比文件内容
	 */
	public static List<String> diffString(List<String> original, List<String> revised) {
		return diffString(original, revised, null, null);
	}

	/**
	 * 对比两文件的差异，返回原始文件+diff格式
	 *
	 * @param original         原文件内容
	 * @param revised          对比文件内容
	 * @param originalFileName 原始文件名
	 * @param revisedFileName  对比文件名
	 */
	public static List<String> diffString(List<String> original, List<String> revised, String originalFileName, String revisedFileName) {
		originalFileName = originalFileName == null ? "原始文件" : originalFileName;
		revisedFileName = revisedFileName == null ? "对比文件" : revisedFileName;
		//两文件的不同点
		Patch<String> patch = com.github.difflib.DiffUtils.diff(original, revised);
		//生成统一的差异格式
		List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff(originalFileName, revisedFileName, original, patch, 0);
		if (unifiedDiff.size() == 0) {
			//如果两文件没差异则插入如下
			unifiedDiff.add("--- " + originalFileName);
			unifiedDiff.add("+++ " + revisedFileName);
			unifiedDiff.add("@@ -0,0 +0,0 @@");
		} else if (unifiedDiff.size() >= 3 && !unifiedDiff.get(2).contains("@@ -1,")) {
			//如果第一行没变化则插入@@ -0,0 +0,0 @@
			unifiedDiff.add(2, "@@ -0,0 +0,0 @@");
		}
		//原始文件中每行前加空格
		List<String> original1 = original.stream().map(v -> " " + v).collect(Collectors.toList());
		//差异格式插入到原始文件中
		return insertOrig(original1, unifiedDiff);
	}


	/**
	 * 对比两文件的差异，返回原始文件+diff格式
	 *
	 * @param filePathOriginal 原文件路径
	 * @param filePathRevised  对比文件路径
	 */
	public static List<String> diffString(String filePathOriginal, String filePathRevised,String originalName,
			String revisedName) {
		//原始文件
		List<String> original = null;
		//对比文件
		List<String> revised = null;
		File originalFile = new File(filePathOriginal);
		File revisedFile = new File(filePathRevised);
		try {
			original = Files.readAllLines(originalFile.toPath());
			revised = Files.readAllLines(revisedFile.toPath());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return diffString(original, revised, originalName, revisedName);
	}

	/**
	 * 通过两文件的差异diff生成 html文件，打开此 html文件便可看到文件对比的明细内容
	 *
	 * @param diffString 调用上面 diffString方法获取到的对比结果
	 * @param htmlPath   生成的html路径，如:/user/var/mbos/ent/21231/diff.html
	 * HTML输出接受一个Javascript对象，该对象可能有以下配置项:
	 *
	 * inputFormat: 输入数据的格式: 'diff' 或者 'json', 默认是'diff'
	 * outputFormat: 输出数据的格式: 'line-by-line' 或者 'side-by-side', 默认是'line-by-line'
	 * showFiles: 在对比之前查看文件列表，true 或者false,默认是false
	 * matching: 匹配level: 'lines'用于匹配行, 'words' 用于匹配行和单词，或者设置为'none',默认为none
	 * matchWordsThreshold: 单词相似度下限, 默认是0.25
	 * matchingMaxComparisons: 为了匹配一组变化最多执行的比较次数，默认是2500
	 * maxLineLengthHighlight: 如果行数小于此值，则仅仅执行差异突出显示，默认是10000
	 * templates: 使用预备好的编译的模板替换部分html的对象。
	 * rawTemplates: 具有原始未编译模板的对象替换部分html。
	 * 更多参考 https://github.com/rtfpessoa/diff2html/tree/master/src/templates
	 */
	public static void generateDiffHtml(List<String> diffString, String htmlPath) {
		StringBuilder builder = new StringBuilder();
		for (String line : diffString) {
			builder.append(line);
			builder.append("\n");
		}
		log.info("temp:{}",builder.toString());
		String template = "<!DOCTYPE html>\n" +
				"<html lang=\"en-us\">\n" +
				"  <head>\n" +
				"    <meta charset=\"utf-8\" />\n" +
				"    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.1/styles/github.min.css\" />\n" +
				"     <link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.jsdelivr.net/npm/diff2html/bundles/css/diff2html.min.css\" />\n" +
				"    <script type=\"text/javascript\" src=\"https://cdn.jsdelivr.net/npm/diff2html/bundles/js/diff2html-ui.min.js\"></script>\n" +
				"  </head>\n" +
				"  <script>\n" +
				"    const diffString = `\n" +
				"temp\n" +
				"`;\n" +
				"\n" +
				"\n" +
				"     document.addEventListener('DOMContentLoaded', function () {\n" +
				"      var targetElement = document.getElementById('diffElement');\n" +
				"      var configuration = {\n" +
				"        drawFileList: true,\n" +
				"        fileListToggle: false,\n" +
				"        fileListStartVisible: false,\n" +
				"        fileContentToggle: false,\n" +
				"        matching: 'words',\n" +
				"        outputFormat: 'side-by-side',\n" +
				"        synchronisedScroll: true,\n" +
				"        highlight: true,\n" +
				"        renderNothingWhenEmpty: true,\n" +
				"      };\n" +
				"      var diff2htmlUi = new Diff2HtmlUI(targetElement, diffString, configuration);\n" +
				"      diff2htmlUi.draw();\n" +
				"      diff2htmlUi.highlightCode();\n" +
				"    });\n" +
				"  </script>\n" +
				"  <body>\n" +
				"    <div id=\"diffElement\"></div>\n" +
				"  </body>\n" +
				"</html>";
		String string = builder.toString();
		string = string.replace("/", "\\/");
		template = template.replace("temp", string);
		FileWriter f = null; //文件读取为字符流
		try {
			f = new FileWriter(htmlPath);
			BufferedWriter buf = new BufferedWriter(f); //文件加入缓冲区
			buf.write(template); //向缓冲区写入
			buf.close(); //关闭缓冲区并将信息写入文件
			f.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 通过两文件的差异diff生成 string，打开此 html文件便可看到文件对比的明细内容
	 * @param diffString
	 * @return
	 */
	public static String generateDiffString(List<String> diffString) {
		StringBuilder builder = new StringBuilder();
		builder.append("`");
		for (String line : diffString) {
			//对比页面开始符号冲突
			if (line.contains("`")) {
				line = line.replace("`","\\`");
			}
			//</script>结束标志</冲突
			if (line.contains("</")) {
				line = line.replace("</", "<\\/");
			}
			//正则\冲突
			if (line.contains("\\") && line.contains("^")) {
				line = line.replace("\\", "\\\\");
			}
			//页面取值冲突 ${}
			if (line.contains("${")) {
				line = line.replace("${", "$\\{");
			}
			builder.append(line);
			builder.append("\n");
		}
		builder.append("`");
		String string = builder.toString();
		return string;
	}
	//统一差异格式插入到原始文件
	public static List<String> insertOrig(List<String> original, List<String> unifiedDiff) {
		List<String> result = new ArrayList<>();
		//unifiedDiff中根据@@分割成不同行，然后加入到diffList中
		List<List<String>> diffList = new ArrayList<>();
		List<String> d = new ArrayList<>();
		for (int i = 0; i < unifiedDiff.size(); i++) {
			String u = unifiedDiff.get(i);
			if (u.startsWith("@@") && !"@@ -0,0 +0,0 @@".equals(u) && !u.contains("@@ -1,")) {
				List<String> twoList = new ArrayList<>();
				twoList.addAll(d);
				diffList.add(twoList);
				d.clear();
				d.add(u);
				continue;
			}
			if (i == unifiedDiff.size() - 1) {
				d.add(u);
				List<String> twoList = new ArrayList<>();
				twoList.addAll(d);
				diffList.add(twoList);
				d.clear();
				break;
			}
			d.add(u);
		}

		//将diffList和原始文件original插入到result，返回result
		for (int i = 0; i < diffList.size(); i++) {
			List<String> diff = diffList.get(i);
			List<String> nexDiff = i == diffList.size() - 1 ? null : diffList.get(i + 1);
			//含有@@的一行
			String simb = i == 0 ? diff.get(2) : diff.get(0);
			String nexSimb = nexDiff == null ? null : nexDiff.get(0);
			//插入到result
			insert(result, diff);
			//解析含有@@的行，得到原文件从第几行开始改变，改变了多少（即增加和减少的行）
			Map<String, Integer> map = getRowMap(simb);
			if (null != nexSimb) {
				Map<String, Integer> nexMap = getRowMap(nexSimb);
				int start = 0;
				if (map.get("orgRow") != 0) {
					start = map.get("orgRow") + map.get("orgDel") - 1;
				}
				int end = nexMap.get("revRow") - 2;
				//插入不变的
				insert(result, getOrigList(original, start, end));
			}

			if (simb.contains("@@ -1,") && null == nexSimb) {
				insert(result, getOrigList(original, 0, original.size() - 1));
			} else if (null == nexSimb && map.get("orgRow") < original.size()) {
				insert(result, getOrigList(original, map.get("orgRow"), original.size() - 1));
			}
		}
		return result;
	}

	//将源文件中没变的内容插入result
	public static void insert(List<String> result, List<String> noChangeContent) {
		for (String ins : noChangeContent) {
			result.add(ins);
		}
	}

	//解析含有@@的行得到修改的行号删除或新增了几行
	public static Map<String, Integer> getRowMap(String str) {
		Map<String, Integer> map = new HashMap<>();
		if (str.startsWith("@@")) {
			String[] sp = str.split(" ");
			String org = sp[1];
			String[] orgSp = org.split(",");
			//源文件要删除行的行号
			map.put("orgRow", Integer.valueOf(orgSp[0].substring(1)));
			//源文件删除的行数
			map.put("orgDel", Integer.valueOf(orgSp[1]));

			String[] revSp = org.split(",");
			//对比文件要增加行的行号
			map.put("revRow", Integer.valueOf(revSp[0].substring(1)));
			map.put("revAdd", Integer.valueOf(revSp[1]));
		}
		return map;
	}

	//从原文件中获取指定的部分行
	public static List<String> getOrigList(List<String> original1, int start, int end) {
		List<String> list = new ArrayList<>();
		if (start <= end && end < original1.size()) {
			for (; start <= end; start++) {
				list.add(original1.get(start));
			}
		}
		return list;
	}
}
