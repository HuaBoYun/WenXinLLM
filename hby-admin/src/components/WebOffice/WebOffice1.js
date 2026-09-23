function base64toBlob(base64Data, contentType) {
	contentType = contentType || '';
	var sliceSize = 1024;
	var byteCharacters = atob(base64Data);
	var bytesLength = byteCharacters.length;
	var slicesCount = Math.ceil(bytesLength / sliceSize);
	var byteArrays = new Array(slicesCount);

	for (var sliceIndex = 0; sliceIndex < slicesCount; ++sliceIndex) {
		var begin = sliceIndex * sliceSize;
		var end = Math.min(begin + sliceSize, bytesLength);

		var bytes = new Array(end - begin);
		for (var offset = begin, i = 0; offset < end; ++i, ++offset) {
			bytes[i] = byteCharacters[offset].charCodeAt(0);
		}
		byteArrays[sliceIndex] = new Uint8Array(bytes);
	}
	return new Blob(byteArrays, { type: contentType });
}

function WebOffice2015() {
	this.SaveServlet = "/OfficeServer";
	this.ServerUrl = "ServerUrl";
	this.dVersion = "5.0.0.10";
	this.dUpdateDate = "2019-01-29";
	//高级功能：全文�?��?、文档�?�比

	// *****************************************************************************************************************************
	// 内部变量和方�?
	this.PASSWORD = "123456";
	var ISO;								// 服务器返回�?
	var root;
	var rootValue;
	this.Ashell = null;
	this.BookMark = "";						// 书�?�名�?
	this.ImageName = "";					// 图片名称
	this.obj; 								// Office控件对象,�?直接调用控件接口及属�?
	this.DownloadedFileTempPathName = "";	// 下载保存的临时文件路径名
	this.DOWN = "\\WebOffice\\Down\\"; 		// 指定隐藏�?径并设置名称
	this.UP = "\\WebOffice\\UP\\";			// 指定隐藏�?径并设置名称
	this.FilePath = null; 					// 文档�?�?
	this.TmpFile = null; 					// 临时下载的文档名称（�?完整�?径）
	this.tempInsertName = ""; 				// 插入文件的文件名�?
	this.DocTypeValue = {
		DOC: 0,
		DOCX: 16,
		XLS: 55,
		XLSX: 51,
		WPS: 1,
		ET: 56,
		PDF: 41,
		OFD: 42

	}; 										// 枚举所有的文档类型值用户保存用
	this.DocSuffixType = {
		0: ".doc",
		12: ".docx",
		55: ".xls",
		51: ".xlsx",
		1: ".wps",
		56: ".et",
		41: ".pdf",
		42: ".ofd"
	}; 										// 根据打开文档类型，来获取后缀名称
	this.setVersion = -1;					// Office版本�?
	this.OfficeVersion = {
		v2003: 1,
		vOther: 0
	}; 										// 判断Office版本，这�?�?要用于保存到�?�?
	this.sendMode = null;					// 设置异�?�调用模�?
	this.iWebOfficeTempName = "iWebOfficeTempName.doc"
	// *****************************************************************************************************************************

	// *****************************************************************************************************************************
	// 对�?�公共属�?
	this.WebUrl = ""; 						// 服务器应用程序Url�?�?
	this.RecordID = ""; 					// 文档的纪录号
	this.Template = ""; 					// 模板编号
	this.SingleFileName = "";
	this.FileName = ""; 					// 文档名称
	this.UserName = ""; 					// 操作文档用户�?
	this.OfficeUserName = ""; 				// 记录�?地office用户�?
	this.FileType = ""; 					// 文档类型 .doc .xls .wps
	this.EditType = ""; 					//文档编辑类型
	this.WebObject = null; 					// 设置WebObject对象，方便调用VBA 方法（）
	// 设置�?否显示整�?控件工具栏，包括OFFICE的工具栏
	// 0 : �?定义工具�?=false, Office工具�?=true;
	// 1 : �?定义工具�?=true,  Office工具�?=true;
	// 2 : �?定义工具�?=false, Office工具�?=false;
	// 3 : �?定义工具�?=true,  Office工具�?=false;
	this.ShowToolBar = 1;		//
	this.CopyType = "1"; 					// 设置拷贝类型 "1": 允�?�拷�? "0": 禁�?�拷�?
	// 该属性控制的�?操作系统的粘贴板，一旦�?�置为�?��?�拷贝则整个操作系统的拷贝也�?禁�??�?
	this.ShowMenu = "1"; 					// 设置�?否显示整�?菜单 "1": 显示菜单 "0": 不显示菜�?
	this.Status = ""; 						// Status：状态信�?
	// “工具栏空间”即当所有OFFICE工具栏都隐藏时控件故意产生一�?灰色条占住空间，在OFFICE2000�?2003下有此功�?
	this.MaxFileSize = 8 * 1024; 			// 设置文件最大允许值，单位k，默认为8M
	this.ShowWindow = true; 				// 设置�?否显示保存及打开窗口 True: 显示 False: 不显�?
	// 设置为显示，在打开文档和保存文档时会出现进度窗�?
	this.RibbonUIXML = ""; 					// 用来详细设置Office2007选项卡工具界�?
	this.UIControl = false; 				// 控制OFFICE2010�?境中�?否可以使用另存为和保存功能，用于控制客户使用�?定义�?捷键和快捷工具栏的另存为功能下载文件
	this.DebugMode = false; 				// �?否开�?调试模式，true：开�? false：不开�?，默�?false
	this.HiddenDirectory = false;			// �?否开�?将文档保存在隐藏�?录中  true：开�?  false�? 不开�? 默�?�：false
	this.DelFileAfterSave = true;			// 保存文档后删除�?�目录中的文�?   true：删�?  false：不删除  默�?�：true
	this.Charset = true;					//后台数据编码,true为utf-8编码,false为gb2312编码
	// ******************************************************************************************************************************

	// ******************************************************************************************************************************//
	// ---------------------------------------------------对�?�接�?-------------------------------------------------------------------//
	// 设置控件标�??
	this.SetCaption = function (captionName) {
		this.obj.Caption = captionName;// this.Caption;
	}

	this.GetFileType = function (downloadLink) {
		var filename = downloadLink;
		var index1 = filename.lastIndexOf(".");
		var index2 = filename.length;
		var postf = filename.substring(index1, index2);//后缀�?

		this.FileType = postf;
	}
	this.GetSingleFileName = function (downloadLink) {
		var filename = downloadLink;
		var index1 = filename.lastIndexOf("/");
		var index2 = filename.length;
		var postf = filename.substring(index1 + 1, index2);//后缀�?

		this.SingleFileName = postf;
	}

	this.WebOpen2 = function (downloadLink) {
		this.ConsoleOut("<WebOpen2> 开�?...");
		this.Status = "成功";
		var httpclient = this.obj.Http; // 设置http对象

		this.ConsoleOut("<WebOpen2> 开始下载文�?...");

		this.FileName = downloadLink;
		this.GetFileType(downloadLink);
		this.GetSingleFileName(downloadLink);
		//alert("want to download: " + this.ServerUrl + downloadLink);
		if (httpclient.Open(this.HttpMethod.Get, this.ServerUrl + downloadLink, false)) {
			this.ConsoleOut("<WebOpen2> httpclient.Open = true...");
			if (httpclient.Send()) {
				this.ConsoleOut("<WebOpen2> httpclient.Send = true...");
				if (this.hiddenSaveLocal(httpclient, this, false, false)) {
					this.ConsoleOut("<LOADFILE> 后台文档保存到本地成功，开始打开�?地文档，�?径为�?"
						+ this.DownloadedFileTempPathName + "...");

					var mSaveResult = this.WebOpenLocalFile(this.DownloadedFileTempPathName);
					if (mSaveResult == 0) { // 打开�?地�?�盘文件
						this.ConsoleOut("<LOADFILE> 文档打开成功...");
						this.getOfficeVersion();// 打开文档后，判断当前office版本
						return true;
					} else if (mSaveResult == 1) {
						var windows = window.confirm("�?能当前授权码错�??，�?�确�?iWebOffice2015.js的授权是否�?�确（或乱码�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "�?能当前授权码错�??，�?�确�?iWebOffice2015.js的授权是否�?�确（或乱码�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 2) {
						var windows = window.confirm("没有找到文档，�?�确�?WebOpenLocalFile打开文档的路径是否�?�确"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "有找到文档，请确�?WebOpenLocalFile打开文档的路径是否�?�确";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 3) {
						var windows = window.confirm("没有权限导致文档打开失败，�?�用管理员身份运行浏览器后重�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "没有权限导致文档打开失败，�?�用管理员身份运行浏览器后重�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 4) {
						var windows = window.confirm("文件�?能损坏，请确定服务器文档�?否已经损�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "文件�?能损坏，请确定服务器文档�?否已经损�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 5) {
						var windows = window.confirm("�?安�?�Office或者注册表有损�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "�?安�?�Office或者注册表有损�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 6) {
						var windows = window.confirm("文件�?占用，�?�结束Office进程后重�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "文件�?占用，�?�结束Office进程后重�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else {
						this.ConsoleOut("<LOADFILE> 打开文档时未知错�?！错�?码为�? "
							+ mSaveResult);
						var windows = window.confirm("打开文档时未知错�?！错�?码为�? "
							+ mSaveResult
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "打开文档时未知错�?！错�?码为�? "
							+ mSaveResult;
						if (windows == 1) {
							window.close();
							return false;
						}
					}
				} else {
					// 失败后，this.Status的值由hiddenSaveLocal返回
					this.Status = "保存文档到本�? 失败";
					return false;
				}
			}
		}
	}
	this.WebOpen3 = function (downloadLink) {
		this.ConsoleOut("<WebOpen3> 开�?...");
		this.Status = "成功";
		var httpclient = this.obj.Http; // 设置http对象

		this.ConsoleOut("<WebOpen3> 开始下载文�?...");
		this.ShowMenuBar(this.ShowMenu);  //控制菜单栏是否可以显�?
		this.NewShowToolBar(this.ShowToolBar); //控制Office工具栏和�?定义工具�?


		this.GetFileType(this.FileName);
		this.GetSingleFileName(this.FileName);
		//alert("want to download: " + downloadLink);
		if (httpclient.Open(this.HttpMethod.Get, downloadLink, false)) {
			this.ConsoleOut("<WebOpen2> httpclient.Open = true...");
			if (httpclient.Send()) {
				this.ConsoleOut("<WebOpen2> httpclient.Send = true...");
				if (this.hiddenSaveLocal(httpclient, this, false, false)) {
					this.ConsoleOut("<LOADFILE> 后台文档保存到本地成功，开始打开�?地文档，�?径为�?"
						+ this.DownloadedFileTempPathName + "...");

					var mSaveResult = this.WebOpenLocalFile(this.DownloadedFileTempPathName);
					if (mSaveResult == 0) { // 打开�?地�?�盘文件
						this.ConsoleOut("<LOADFILE> 文档打开成功...");
						this.getOfficeVersion();// 打开文档后，判断当前office版本
						return true;
					} else if (mSaveResult == 1) {
						var windows = window.confirm("�?能当前授权码错�??，�?�确�?iWebOffice2015.js的授权是否�?�确（或乱码�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "�?能当前授权码错�??，�?�确�?iWebOffice2015.js的授权是否�?�确（或乱码�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 2) {
						var windows = window.confirm("没有找到文档，�?�确�?WebOpenLocalFile打开文档的路径是否�?�确"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "有找到文档，请确�?WebOpenLocalFile打开文档的路径是否�?�确";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 3) {
						var windows = window.confirm("没有权限导致文档打开失败，�?�用管理员身份运行浏览器后重�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "没有权限导致文档打开失败，�?�用管理员身份运行浏览器后重�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 4) {
						var windows = window.confirm("文件�?能损坏，请确定服务器文档�?否已经损�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "文件�?能损坏，请确定服务器文档�?否已经损�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 5) {
						var windows = window.confirm("�?安�?�Office或者注册表有损�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "�?安�?�Office或者注册表有损�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 6) {
						var windows = window.confirm("文件�?占用，�?�结束Office进程后重�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "文件�?占用，�?�结束Office进程后重�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else {
						this.ConsoleOut("<LOADFILE> 打开文档时未知错�?！错�?码为�? "
							+ mSaveResult);
						var windows = window.confirm("打开文档时未知错�?！错�?码为�? "
							+ mSaveResult
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "打开文档时未知错�?！错�?码为�? "
							+ mSaveResult;
						if (windows == 1) {
							window.close();
							return false;
						}
					}
				} else {
					// 失败后，this.Status的值由hiddenSaveLocal返回
					this.Status = "保存文档到本�? 失败";
					return false;
				}
			}
		}
	}
	/* 从服务器上取文档并打开，打开RecordID指定的文�? */   //该功能已完整实现
	this.WebOpen = function (mBoolean) {
		this.ConsoleOut("<WebOpen> 开�?...");
		this.Status = "成功"
		debugger;
		var httpclient = this.obj.Http; // 设置http对象
		console.info(this.obj);
		// httpclient.Clear();
		this.GetSingleFileName(this.FileName);
		this.WebSetMsgByName("USERNAME", this.UserName); // 加载UserName
		this.WebSetMsgByName("FILENAME", this.FileName); // 加载FileName
		this.WebSetMsgByName("FILETYPE", this.FileType); // 加载FileType
		this.WebSetMsgByName("RECORDID", this.RecordID); // 加载RecordID
		this.WebSetMsgByName("EDITTYPE", this.EditType); // 加载RecordID
		this.WebSetMsgByName("OPTION", "LOADFILE"); 	 // 发送�?�求LOADFILE
		httpclient.AddForm("FormData", this.GetMessageString()); // 这里�?�?定义json
		// 传输格式�?
		this.WebClearMessage(); 						 // 清除所有WebSetMsgByName参数
		this.sendMode = "OpenFile";
		this.ConsoleOut("<WebOpen> 开始下载文�?...");
		this.ShowMenuBar(this.ShowMenu);  //控制菜单栏是否可以显�?
		this.NewShowToolBar(this.ShowToolBar); //控制Office工具栏和�?定义工具�?
		if (this.LOADFILE(httpclient)) // Http下载服务器文�?
		{
			this.NewCopyType(this.CopyType);  			 // 控制�?否可以�?�制
			this.NewUIControl(this.UIControl);  		 // 控制 2010保存跟另存为
			this.ConsoleOut("<WebOpen> 成功...");
			if (this.FileType != ".ppt" && this.FileType != ".pptx") {
				//this.VBASetUserName(this.UserName);		 // 设置Office用户�?
			}
			this.setEditType(this.EditType);			 // 设置文档编辑权限   0 、只读不能�?�制  1、无痕迹打开 2、有痕迹打开
			this.Status = "打开文档成功"; 				 // Status：状态信�?
			return true;
		} else {
			//this.Status = "打开文档失败"; 			 // Status：状态信�? 由This.LOADFILE返回
			return false;
		}
	}

	/* 从服务器上取文档并打开，打开RecordID指定的文�? */   //该功能已完整实现
	this.WebOpenBase64 = function () {
		var jsWebOffice = this;
		var officeobj = this.obj;
		var filename = this.FileName;
		var tmpDownPath = this.DownFilePath();

		var send = "OPTION=LOADFILE&FILENAME=" + this.FileName + "&FILETYPE=" + this.FileType + "&RECORDID=" + this.RecordID + "&EDITTYPE=" + this.EditType;
		/*		$.post("../../AJAXServer", send, function(data) {
					console.log(data);
				});*/
		$.ajax({
			type: 'POST',
			url: "../../AJAXServer",
			data: send,
			cache: false,
			processData: false,
			xhr: function () { //这是关键 获取原生的xhr对象 做以前做的所有事�?
				console.log("xhr function eee");
				var xhr = jQuery.ajaxSettings.xhr();
				console.log(xhr);
				xhr.onprogress = function (evt) {
					if (evt.lengthComputable) {
						var percentComplete = evt.loaded / evt.total;
						console.log(percentComplete);
					}
				}
				return xhr;
			},
			success: function (data) {
				//console.log(data);
				var filePathName = tmpDownPath;
				filePathName += "\\";
				filePathName += filename;
				officeobj.Base64Text2File(data, filePathName);
				jsWebOffice.WebOpenLocalFile(filePathName);
			}

		});

		this.ConsoleOut("<WebOpenBase64> 结束...");
		return true;
	}

	this.WebSave2 = function (callback) {
		this.ConsoleOut("<WebSave2> 开�?...");
		var rands = Math.round(Math.random() * 1000000);
		var fileUpPathName = this.getFilePath() + rands + this.FileName;
		//alert(fileUpPathName);
		var mSaveResult = this.WebSaveLocalFile(fileUpPathName);
		if (!(mSaveResult == 0)) {
			this.ConsoleOut("<WebSave> 保存�?地文档失败！错�??代码为：" + mSaveResult);
			this.Status = "保存�?地文档失败！错�??代码为：" + mSaveResult;
			return false;
		}
		else {
			//			alert("文档没有内�?�，�?否确定保�?");
		}

		var jsWebOffice = this;
		var officeobj = this.obj;
		var filename = this.FileName;

		var base64file = officeobj.File2Base64Text(fileUpPathName);
		var formData = new FormData();

		var timestamp1 = new Date().getTime();
		var myfff = base64toBlob(base64file);

		var timestamp2 = new Date().getTime();
		this.ConsoleOut("<WebSave2> base64toBlob用时�?..." + (timestamp2 - timestamp1));

		//alert(myfff);
		formData.append("FileData", myfff, "myFileName");
		var aaa = '${pageContext.request.contextPath}';

		var strUrl = jsWebOffice.WebUrl + '/AJAXServer?OPTION=MultiPartUpload&FILENAME=' + filename;

		$.ajax({
			//url:  '${pageContext.request.contextPath}/AJAXServer?OPTION=MultiPartUpload',
			url: strUrl,
			type: "POST",
			data: formData,
			contentType: false,
			cache: false,
			processData: false,
			xhr: function () { //这是关键 获取原生的xhr对象 做以前做的所有事�?
				console.log("xhr function");
				var xhr = jQuery.ajaxSettings.xhr();
				console.log(xhr);
				xhr.upload.onprogress = function (evt) {
					if (evt.lengthComputable) {
						var percentComplete = evt.loaded / evt.total;
						console.log(percentComplete);
						//callback(0);
					}
				}
				return xhr;
			},
			success: function (data, textStatus) {
				console.log('success');
				console.log('textStatus: ' + textStatus);
				console.log('data: ' + data);
				callback(0);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				console.log('essor');
				console.log(XMLHttpRequest);
				console.log('textStatus: ' + textStatus);
				console.log('errorThrown: ' + errorThrown);
				callback(1);
			}
		});


		this.ConsoleOut("<WebSave2> 结束...");
		return true;
	}

	this.WebSaveBase64 = function () {
		this.ConsoleOut("<WebSaveBase64> 开�?...");
		//alert("this.FileName = " + this.FileName);
		var rands = Math.round(Math.random() * 1000000);
		var fileUpPathName = this.getFilePath() + rands + this.FileName;

		var mSaveResult = this.WebSaveLocalFile(fileUpPathName);
		if (!(mSaveResult == 0)) {
			this.ConsoleOut("<WebSave> 保存�?地文档失败！错�??代码为：" + mSaveResult);
			this.Status = "保存�?地文档失败！错�??代码为：" + mSaveResult;
			return false;
		}
		else {
			//			alert("文档没有内�?�，�?否确定保�?");
		}

		var jsWebOffice = this;
		var officeobj = this.obj;
		var filename = this.FileName;

		var base64file = officeobj.File2Base64Text(fileUpPathName);
		//alert(base64file);
		var newBase64 = base64file.replace(/\+/g, "%2B");
		//alert(newBase64);

		//创建XMLHttpRequest对象
		if (window.XMLHttpRequest) {
			//针�?�FireFox,Mozillar,Opera,Safari,IE7,IE8
			xmlhttp = new XMLHttpRequest();

			//对某些特定版�?的mozillar浏�?�器的bug进�?�修�?
			if (xmlhttp.overrideMineType) {
				xmlhttp.overrideMineType("text/xml");
			}
		}
		else if (window.ActiveXObject) {
			//针�?�IE5，IE5.5，IE6
			//两个�?以用于创建XMLHTTPRequest对象的控件名称。保存在一个JS数组�?�?
			var activexName = ["MSXML2.XMLHTTP", "Microsoft.XMLHTTP"];
			for (var i = 0; i < activeName.length; i++) {
				//取出一�?控件名进行创建，如果成功就终止循�?
				try {
					xmlhttp = new ActiveXObject(activexName[i]);
					break;
				} catch (e) { }
			}
		}

		if (xmlhttp) {
			// alert("XMLHttpRequest对象创建成功�?");
		}
		else {
			//alert("XMLHttpRequest对象创建失败�?");
			return false;
		}

		var jsWebOffice = this;
		var officeobj = this.obj;
		var filename = this.FileName;

		//	    xmlhttp.onreadystatechange = function()
		//	    {
		//	    	   //判断对象的状态是否交互完�?
		//	    	   if(xmlhttp.readyState == 4)
		//	    	   {
		//	    	      //判断http的交互是否成�?
		//	    	      if(xmlhttp.status == 200)
		//	    	      {
		//	    	      }
		//
		//	    	   }
		//	   	};

		var postdata = "OPTION=SAVEFILE&FILENAME=" + this.FileName + "&FILETYPE=" + this.FileType + "&RECORDID=" + this.RecordID + "&EDITTYPE=" + this.EditType
			+ "&TEMPLATE=" + this.Template + "&SUBJECT=" + this.Subject + "&AUTHOR=" + this.Author + "&BASE64FILE=" + newBase64;
		//alert("aaa.length = " +  aaa.length);
		xmlhttp.open("POST", "../../AJAXServer?name=", false);   // false �?同�??
		//xmlhttp.setRequestHeader("Content-Length",aaa.length);
		xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");   //POST 必须要写这句，GET不用�?
		//发送数�?，开始和服务器进行交互�?
		//xmlhttp.send("OPTION=SAVEFILE&FILENAME="+this.FileName+"&FILETYPE="+this.FileType+"&RECORDID="+this.RecordID+"&EDITTYPE="+this.EditType
		//		+"&TEMPLATE="+this.Template+"&SUBJECT="+this.Subject+"&AUTHOR="+this.Author+"&BASE64FILE="+newBase64);
		xmlhttp.send(postdata);
		this.Status = "成功将文档保存到服务�?";
		return true;
	}

	/* 保存文件 */		//（�?�功能已完整实现�?
	this.WebSave = function () {
		this.ConsoleOut("<WebSave> 开�?...");
		this.Status = "";
		var httpclient = this.obj.Http; // 设置http对象
		httpclient.SetHttpTimeOut(50 * 1000);
		httpclient.Clear();
		this.WebSetMsgByName("USERNAME", this.UserName);
		this.WebSetMsgByName("RECORDID", this.RecordID);
		this.WebSetMsgByName("TEMPLATE", this.Template);
		this.WebSetMsgByName("FILETYPE", this.FileType);
		this.WebSetMsgByName("OPTION", "SAVEFILE");
		this.WebSetMsgByName("FILENAME", this.FileName); // 加载FileName
		this.ConsoleOut("<WebSave> 开始将文档保存到本�?...");
		if (this.WebSetAllowEmpty()) {
			var FilePathName = this.getFilePath() + Math.random() * 100000 + this.FileName;
			var mSaveResult = this.WebSaveLocalFile(FilePathName);
			if (!(mSaveResult == 0)) {
				this.Status = "保存�?地文档失败！错�??代码为：" + mSaveResult;
				return false;
			}
		} else {
			alert("文档没有内�?�，�?否确定保�?");
		}
		this.sendMode = "SaveFile";
		// 判断�?地文件是否大于指定的文件大小，�?�果大于不保�?
		if (this.WebSetMaxFileSize(FilePathName)) {
			if (this.SAVEFILE(httpclient, FilePathName)) {
				this.ConsoleOut("<WebSave> 成功将文档保存到服务�?...");
				this.Status = "成功将文档保存到服务�?";
				return true;
			} else {
				//STATUS 由this.SAVEFILE返回
				return false;
			}
		} else {
			this.Status = "保存失败：MaxFileSize�?能允许保存：<" + this.MaxFileSize / 1024
				+ ">" + "M";
			return false;
		}
	}

	/* 关闭文档 */
	this.WebClose = function () {
		if (this.Close()) {
			return true;
		}
		return false;
	}

	/* 保存上传到服务器的数�?*/
	this.WebSetMsgByName = function (FieldName, FieldValue) {
		this.ht.Add(FieldName, FieldValue);
	}

	/* 获取从服务器得到的数�?*/
	this.WebGetMsgByName = function (FieldName) {
		return this.ht.Get(FieldName);
	}

	/* 按json格式发送数�? */	//
	this.WebSendMessage = function () {
		this.ConsoleOut("<SendMessage> 开�?...");
		var httpclient = this.obj.Http; // 设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("OPTION", "SENDMESSAGE");
		this.sendMode = "SendMessage";
		httpclient.ShowProgressUI = this.ShowWindow;
		httpclient.AddForm("FormData", this.GetMessageString());
		this.ConsoleOut("<SendMessage> 开始发到服务器...");
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) // true 异�?�方�? false同�??
		{
			if (httpclient.Send()) {
				this.ConsoleOut("<SendMessage> 开始获取服务器上的数据");
				this.GetDataToSend(); //得到服务器setMsgByName的值并发送到前台
				this.ConsoleOut("<SendMessage> 获取成功，发送到前台");
				httpclient.Clear();
				this.Status = this.WebGetMsgByName("STATUS");
				return true;
			} else {
				this.ConsoleOut("<SendMessage> 数据包发送失�?");
				this.Status = this.WebGetMsgByName("STATUS");
				return false;
			}
		} else {
			this.ConsoleOut("<SendMessage> 网络通信异常...");
			this.Status = "网络通信异常";               //Status：状态信�?
			return false;
		}
		return this.ht.toString();
	}


	/* 清除由WebSetMsgByName设置的�? */
	this.WebClearMessage = function () {
		this.ht.Clear();
	}

	/* 保存为PDF文件并上传至服务�? */
	this.WebSavePDF = function () {
		this.ConsoleOut("<WebSavePDF> 开�?...");
		var httpclient = this.obj.Http; // 设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("RECORDID", this.RecordID);
		this.WebSetMsgByName("OPTION", "SAVEPDF");
		this.WebSetMsgByName("FILENAME", this.RecordID + ".pdf");// 加载FileName
		this.ConsoleOut("<WebSavePDF> 开始保存PDF到本�?...");
		this.SaveAsPdf(this.getFilePath() + this.RecordID + ".pdf");
		this.ConsoleOut("<WebSavePDF> 开始保存PDF到服务器...");
		if (this.SAVEFILE(httpclient, this.getFilePath() + this.RecordID
			+ ".pdf")) {
			this.ConsoleOut("<WebSavePDF> 保存保存PDF到服务器...");
			this.Status = "保存PDF文件成功";
			return true;
		} else {
			this.ConsoleOut("<WebSavePDF> 保存PDF失败...");
			this.Status = "保存PDF文件失败";
			return false;
		}
	}


	/* 保存HTML到服务器          需要V12.4.0.450以上版本*/
	this.WebSaveAsHtml = function () {
		this.ConsoleOut("<WebSaveAsHtml> 开�?...");
		if (this.SaveAsHtml(this.getFilePath() + this.RecordID + ".html")) {
			this.ConsoleOut("<WebSaveAsHtml> Html保存到本地成�?...");
			var strDirPath = this.getFilePath() + this.RecordID + '.files'; //获取html files�?�?
			var FindFile = this.obj.FileSystem.FindToDirAsFileEx(strDirPath);//获取html文件夹里的内�?
			var DirPath = FindFile.split("\r\n");
			var httpclient = this.obj.Http; // 设置http对象
			httpclient.Clear();
			this.WebSetMsgByName("RECORDID", this.RecordID);
			this.WebSetMsgByName("OPTION", "SAVEHTML");
			this.WebSetMsgByName("DIRECTORY", this.RecordID + ".files");
			this.ConsoleOut("<WebSaveAsHtml> �?�?保存files文件夹内�?...");
			for (var i = 0; i < DirPath.length; i++) {
				var FilesName = DirPath[i];
				this.WebSetMsgByName("FILENAME", FilesName);// 加载FileName
				if (!this.SAVEFILE(httpclient, strDirPath + "\\" + FilesName)) {
					this.Status = "保存HTML文件失败";
					return false;
				}
			}
			this.WebSetMsgByName("DIRECTORY", ""); //作用�?html和files区分开
			this.WebSetMsgByName("FILENAME", this.RecordID + ".html");// 加载FileName
			this.ConsoleOut("<WebSaveAsHtml> files文件夹内容成�?,开始保存Html文件夹内�?...");
			if (this.SAVEFILE(httpclient, this.getFilePath() + this.RecordID + ".html")) {
				this.ConsoleOut("<WebSaveAsHtml> 保存Html文件夹内容成�?...");
				this.Status = "保存HTML文件成功";
				return true;
			} else {
				this.Status = "保存HTML文件失败";
				return false;
			}
		}
		this.ConsoleOut("<WebSaveAsHtml> 保存Html文件夹内容失�?...");
		this.Status = "保存HTML文件失败!";
		this.ConsoleOut("<WebSaveAsHtml> 结束...");
		return false;
	}

	/* 保存模板文件 */
	this.WebSaveTemplate = function () {
		this.ConsoleOut("<WebSaveTemplate> 开�?...");
		this.Status = "";
		var httpclient = this.obj.Http; //设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("OPTION", "SAVETEMPLATE");
		this.WebSetMsgByName("TEMPLATE", this.Template);
		this.ConsoleOut("<WebSaveTemplate> 开始将文档保存到本�?...");
		var mSaveResult = this.WebSaveLocalFile(this.getFilePath() + this.FileName);
		if (!(mSaveResult == 0)) {
			this.ConsoleOut("<WebSaveTemplate> 保存�?地文档失败！错�??代码为：" + mSaveResult);
			this.Status = "保存�?地文档失败！错�??代码为：" + mSaveResult;
			return false;
		}
		this.ConsoleOut("<WebSaveTemplate> 将文档保存到�?地成�?...");

		this.sendMode = "WebSaveTemplate";
		this.ConsoleOut("<WebSaveTemplate> 开始将文档保存到服务器...");
		if (this.SAVEFILE(httpclient, this.FilePath + this.FileName)) {
			this.ConsoleOut("<WebSaveTemplate> 成功将文档保存到服务�?...");
			this.Status = "保存模板文件成功";
			return true;
		}
		else {
			//STATUS 由this.SAVEFILE返回
			return false;
		}
	}

	/* 调入由Template指定的模板，该功能主要用于模板�?�理或模板�?�红 */
	this.WebUseTemplate = function () {
		this.ConsoleOut("<WebUseTemplate> 开�?...");
		this.ClearRevisions(); //清除正文痕迹的目的是为了避免痕迹状态下出现内�?�异常问题�?

		this.DeleteFile(this.getFilePath() + this.iWebOfficeTempName);	//删除临时文件
		this.ConsoleOut("<WebUseTemplate> 将当前文档保存到�?�?...");
		var mSaveResult = this.WebSaveLocalFile(this.getFilePath() + this.iWebOfficeTempName);//将当前文档保存下�?
		if (!(mSaveResult == 0)) {
			this.ConsoleOut("<WebUseTemplate> 保存�?地文档失败！错�??代码为：" + mSaveResult);
			this.Status = "保存�?地文档失败！错�??代码为：" + mSaveResult;
			this.ConsoleOut("<WebUseTemplate> 结束...");
			return false;
		}
		var httpclient = this.obj.Http; //设置http对象
		this.WebSetMsgByName("OPTION", "LOADTEMPLATE");
		this.WebSetMsgByName("TEMPLATE", this.Template);
		httpclient.AddForm("FormData", this.GetMessageString()); //这里�?�?定义json 传输格式�?
		this.WebClearMessage(); //清除所有WebSetMsgByName参数

		this.ConsoleOut("<WebUseTemplate> 开始打开套红文档...");
		if (this.LOADFILE(httpclient)) {
			this.ConsoleOut("<WebUseTemplate> 打开套红文档成功...");
			this.ConsoleOut("<WebUseTemplate> 开始插入�?�文...");
			if (this.VBAInsertFile("Content", this.getFilePath() + this.iWebOfficeTempName)) {
				this.ConsoleOut("<WebUseTemplate> 插入正文成功...");
				this.Status = "套红成功";
				this.ConsoleOut("<WebUseTemplate> 结束...");
				return true;
			} else {
				this.ConsoleOut("<WebUseTemplate> 插入正文失败...");
				//this.Status = "套红失败"; //STATUS 由this.VBAInsertFile返回
				return false;
			}
		} else {
			this.ConsoleOut("<WebUseTemplate> 打开套红文档失败...");
			this.Status = "打开套红文档失败";               //Status：状态信�?
			this.ConsoleOut("<WebUseTemplate> 结束...");
			return false;
		}
	}

	/* 将当前文档作为图片格式保存在服务器上  WebSaveImage(".gif","abc.gif"); */
	this.WebSaveImage = function (ImageType, ImageName) {
		this.ConsoleOut("<WebSaveAsHtml> 开�?...");
		if (this.FileType == ".doc" || this.FileType == ".docx" || this.FileType == ".wps") {
			this.ConsoleOut("<WebSaveImage> Html保存到本�?...");
			var iFilePath = this.getFilePath() + this.RecordID + ".htm";
			var strDirPath = this.getFilePath() + this.RecordID + '.files'; //获取html files�?�?
			this.obj.ActiveDocument.SaveAs(iFilePath, 10, false, "", false, "", false, false, false, false, false, 0); //保存到本地html
			this.obj.ActiveDocument.Application.ActiveWindow.View.type = 3;	//3�?页面视图
			this.ConsoleOut("<WebSaveImage> Html保存到本地成�?...");
			var DirPath = new VBArray(this.obj.FileSystem.FindToDirAsFile(strDirPath)).toArray(); //获取html文件夹里的内�?
			var httpclient = this.obj.Http; 							// 设置http对象
			httpclient.Clear();
			this.WebSetMsgByName("RECORDID", this.RecordID);
			this.WebSetMsgByName("OPTION", "SAVEIMAGE");
			this.WebSetMsgByName("DIRECTORY", this.RecordID + ".files");
			this.ConsoleOut("<WebSaveImage> �?�?保存files文件夹内�?...");
			for (var i = 0; i < DirPath.length; i++) {
				var FilesName = DirPath[i];
				this.WebSetMsgByName("FILENAME", FilesName);			// 加载FileName
				if (!this.SAVEFILE(httpclient, strDirPath + "\\" + FilesName)) {
					this.Status = "保存HTML图片文件失败";
					return false;
				}
			}
			this.WebSetMsgByName("DIRECTORY", ""); 						//作用把html和files区分开
			this.WebSetMsgByName("FILENAME", this.RecordID + ".htm");	// 加载FileName
			this.ConsoleOut("<WebSaveImage> files文件夹内容成�?,开始保存Html文件夹内�?...");
			if (this.SAVEFILE(httpclient, this.getFilePath() + this.RecordID + ".htm")) {
				this.ConsoleOut("<WebSaveImage> 保存Html文件夹内容成�?...");
				this.Status = "保存HTML图片文件成功";
				return true;
			} else {
				this.Status = "保存HTML图片文件失败";
				return false;
			}
		}
		this.ConsoleOut("<WebSaveImage> 保存Html文件夹内容失�?...");
		this.Status = "保存HTML图片文件失败!";
		this.ConsoleOut("<WebSaveImage> 结束...");
		return false;
	}
	/*  插入服务器上执�?�的文档 */
	this.WebInsertFile = function () {
		var httpclient = this.obj.Http;
		this.ConsoleOut("<WebInsertFile> 开�?...");
		this.WebSetMsgByName("TEMPLATE", this.Template); 					//在接口获取模板名�?
		this.WebSetMsgByName("OPTION", "INSERTFILE");     					//发送�?�求LOADFILE
		httpclient.AddForm("FormData", this.GetMessageString()); 			//这里�?�?定义json 传输格式�?
		this.WebClearMessage();                         					//清除所有WebSetMsgByName参数
		var URL = this.WebUrl.substring(0, this.WebUrl.lastIndexOf("/"));
		httpclient.ShowProgressUI = this.ShowWindow;						//隐藏进度�?
		this.ConsoleOut("<WebInsertFile> 开始打开连接...");
		if (httpclient.Open(this.HttpMethod.Post, URL + "/Document/" + this.Template, false)) 									//Http下载服务器文�?
		{
			if (httpclient.Send()) {
				this.ConsoleOut("<WebInsertFile> 数据包发送成�?...");
				this.ConsoleOut("<WebInsertFile> 判断接收数据�?否异�?...");
				if (httpclient.Status == 200) {
					this.ConsoleOut("<WebInsertFile> 开始将后台文件保存到本�?...");
					if (this.hiddenSaveLocal(httpclient, this, false, false, this.Template)) {
						this.ConsoleOut("<WebInsertFile> 后台文件保存到本地成�?...");
						this.ConsoleOut("<WebInsertFile> 插入书�?��?�文...")
						if (this.obj.ActiveDocument.BookMarks.Exists(this.BookMark)) {
							if (this.VBAInsertFile(this.BookMark, this.DownloadedFileTempPathName)) {
								this.ConsoleOut("<WebInsertFile> 插入正文成功...");
								this.Status = "插入成功";               //Status：状态信�?
								httpclient.Clear();
								this.ConsoleOut("<WebInsertFile> 结束...");
								return true;
							} else {
								this.ConsoleOut("<WebInsertFile> 插入正文失败...");
								//状态信�?�? this.VBAInsertFILE返回
								this.ConsoleOut("<WebInsertFile> 结束...");
								return false;
							}
						} else {
							this.ConsoleOut("<WebInsertFile> 文档里没有�?�应书�??...");
							this.Status = "文档里没有�?�应书�??";
							this.ConsoleOut("<WebInsertFile> 结束...");
							return false;
						}
					} else {
						this.ConsoleOut("<WebInsertFile> 文档保存到本地失�?...");
						this.Status = "文档保存到本地失�?";
						this.ConsoleOut("<WebInsertFile> 结束...");
						return false;
					}
				} else {
					this.ConsoleOut("<WebInsertFile> 数据接收�?...");
					this.Status = "数据接收错�??";
					this.ConsoleOut("<WebInsertFile> 结束...");
					return false;
				}
			} else {
				this.ConsoleOut("<WebInsertFile> 数据包发送失�?...");
				this.Status = "数据包发送失�?";               //Status：状态信�?
				this.ConsoleOut("<WebInsertFile> 结束...");
				return false;
			}
		} else {
			this.ConsoleOut("<WebInsertFile> 打开连接失败...");
			this.Status = "打开连接失败";               //Status：状态信�?
			this.ConsoleOut("<WebInsertFile> 结束...");
			return false;
		}
	}

	/* 在指定的标�?�位�?插入服务器上的图片，并决定是否做透明处理 */
	this.WebInsertImage = function (BookMark, ImageName, Transparent, ZOrder) //插入远程图片：BookMark插入的书签的位置;ImageName远程图片的名�?
	{
		this.ConsoleOut("<WebInsertImage> 开�?...");
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				var httpclient = this.obj.Http;
				this.BookMark = BookMark;
				this.ImageName = ImageName;
				this.WebSetMsgByName("OPTION", "INSERTIMAGE");
				this.WebSetMsgByName("IMAGENAME", this.ImageName);
				httpclient.AddForm("FormData", this.GetMessageString()); //这里�?�?定义json 传输格式�?
				this.WebClearMessage(); //清除所有WebSetMsgByName参数
				httpclient.ShowProgressUI = this.ShowWindow;//隐藏进度�?
				this.ConsoleOut("<WebInsertImage> 开始打开连接...");
				if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) {
					this.ConsoleOut("<WebInsertImage> 开始向后台发送数�?�?...");
					if (httpclient.Send()) {
						this.ConsoleOut("<WebInsertImage> 数据包发送成�?...");
						this.ConsoleOut("<WebInsertImage> 判断接收数据�?否异�?...");
						if (httpclient.Status == 200) {
							this.ConsoleOut("<WebInsertImage> 开始将后台文件保存到本�?...");
							if (this.hiddenSaveLocal(httpclient, this, false, false, this.ImageName)) {
								this.ConsoleOut("<WebInsertImage> 后台文件保存到本地成�?...");
								this.ConsoleOut("<WebInsertImage> 运�?�书签插入图�?...");
								if (this.InsertImageByBookMark(Transparent, ZOrder)) {
									this.ConsoleOut("<WebInsertImage> �?地内容加载到控件成功...");
									this.ImageName = null;
									this.BookMark = null;
									httpclient.Clear();
									this.Status = "插入服务器图片成�?";
									return true;
								} else {
									this.ConsoleOut("<WebInsertImage> 书�?�插入图片失�?...");
									this.Status = "书�?�插入图片失�?";
									this.ConsoleOut("<WebInsertImage> 结束...");
									return false;
								}
							} else {
								this.ConsoleOut("<WebInsertImage> 文档保存到本地失�?...");
								this.Status = "文档保存到本地失�?";
								this.ConsoleOut("<WebInsertImage> 结束...");
								return false;
							}
						} else {
							this.ConsoleOut("<WebInsertImage> 数据接收�?...");
							this.Status = "数据接收错�??";
							this.ConsoleOut("<WebInsertImage> 结束...");
							return false;
						}
					} else {
						this.ConsoleOut("<WebInsertImage> 发送数�?包失�?...");
						this.Status = "发送数�?包失�?";               //Status：状态信�?
						this.ConsoleOut("<WebInsertImage> 结束...");
						return false;
					}
				} else {
					this.ConsoleOut("<WebInsertImage> 打开连接失败...");
					this.Status = "打开连接失败";               //Status：状态信�?
					this.ConsoleOut("<WebInsertImage> 结束...");
					return false;
				}
			} else {
				this.Status = "文档锁定，插入失�?";
				return false;
			}
		} else {
			this.Status = "非Word文档，插入失�?";
			return false;
		}
	}

	/* 保存当前文档�?所有的书�?�名称。�?�功能主要把当前文档�?所使用的书签都保存到数�?库里 */
	this.WebSaveBookMarks = function () {
		this.ConsoleOut("<WebSaveBookMarks> 开�?...");
		var httpclient = this.obj.Http;
		httpclient.Clear();
		var BKCount = this.obj.ActiveDocument.Bookmarks.Count; //获取书�?�数�?
		for (var i = 1; i <= BKCount; i++) {
			var BookName = this.obj.ActiveDocument.Bookmarks.Item(i).Name;
			var BookValue = this.obj.ActiveDocument.Bookmarks.Item(BookName).Range.text;
			this.WebSetMsgByName(BookName, BookValue); //存入书�?�名和�?
		}
		this.WebSetMsgByName("OPTION", "SAVEBOOKMARKS");
		this.WebSetMsgByName("TEMPLATE", this.RecordID); //在接口获取模板名�?
		httpclient.AddForm("FormData", this.GetMessageString()); //这里�?�?定义json 传输格式�?
		this.WebClearMessage();
		httpclient.ShowProgressUI = this.ShowWindow;//隐藏进度�?
		this.ConsoleOut("<WebSaveBookMarks> 开始打开连接...");
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) {
			this.ConsoleOut("<WebSaveBookMarks> 开始向后台发送数�?�?...");
			if (httpclient.Send()) {
				this.ConsoleOut("<WebSaveBookMarks> 数据包发送成�?...");
				this.Status = "书�?�保存成�?";
				this.ConsoleOut("<WebSaveBookMarks> 结束...");
				httpclient.Clear();
				return true;
			} else {
				this.ConsoleOut("<WebSaveBookMarks> 数据包发送失�?...");
				this.Status = "数据包发送失�?";
				this.ConsoleOut("<WebSaveBookMarks> 结束...");
				httpclient.Clear();
				return false;
			}
		} else {
			this.ConsoleOut("<WebSaveBookMarks> 打开连接失败...");
			this.Status = "打开连接失败";
			this.ConsoleOut("<WebSaveBookMarks> 结束...");
			httpclient.Clear();
			return false;
		}
	}

	/* 打开书�?��?�理窗口，显示书签列�?，可以用于添加、删除和定位书�??	*/
	this.WebOpenBookMarks = function () {
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				this.obj.ActiveDocument.Application.Dialogs.Item(168).Show();
				return true;
			}
			this.Status = "文档�?锁定，操作失�?";
			return false;
		}
		this.Status = "非Word文档，操作失�?";
		return false;
	}

	/* 取数�?库内容填充文档中书�?�的�? */
	this.WebLoadBookMarks = function () {
		this.ConsoleOut("<WebLoadBookMarks> 开�?...");
		var httpclient = this.obj.Http;
		httpclient.Clear();
		this.WebSetMsgByName("RECORDID", this.RecordID); 			//在接口获取RecordID�?
		this.WebSetMsgByName("TEMPLATE", this.Template); 			//在接口获取模板名�?
		this.WebSetMsgByName("FILENAME", this.FileName); 			//在接口获取文件名�?
		this.WebSetMsgByName("FILETYPE", this.FileType); 			//在接口获取文件类�?
		this.WebSetMsgByName("OPTION", "LOADBOOKMARKS"); 			//在接口获取模板名�?
		httpclient.AddForm("FormData", this.GetMessageString()); 	//这里�?�?定义json 传输格式�?
		this.WebClearMessage();                         			//清除所有WebSetMsgByName参数
		this.sendMode = "WebLoadBookMarks";
		httpclient.ShowProgressUI = this.ShowWindow;				//隐藏进度�?
		this.ConsoleOut("<WebLoadBookMarks> 开始打开连接...");
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) {
			this.ConsoleOut("<WebLoadBookMarks> 开始向后台发送数�?�?...");
			if (httpclient.Send()) {
				this.ConsoleOut("<WebLoadBookMarks> 获取书�?�内容插入到书�??...");
				var ReturnValue = httpclient.GetResponseHeader("RName");// 获取返回�?
				var jsonObj = eval('(' + ReturnValue + ')');
				try {
					for (var i in jsonObj) {
						var BookName = i;
						var BookValue = jsonObj[i];
						if (this.obj.ActiveDocument.BookMarks.Exists(BookName)) {
							var mBookRange = this.obj.ActiveDocument.Bookmarks.Item(BookName).Range;
							mBookRange.text = BookValue;
						} else {
							this.ConsoleOut("<WebLoadBookMarks> 没有找到" + BookName + "书�??...");
							this.Status = "没有找到" + BookName + "书�??";
							return false;
						}
					}
				} catch (e) {
					this.ConsoleOut("<WebLoadBookMarks> 插入书�?�出现异�?" + e.description);
					this.Status = e.description;
					return false;
				}
				this.ConsoleOut("<WebLoadBookMarks> 书�?�内容已插入到书�?...");
				this.Status = "书�?�内容已插入到书�?";
				return true;
			} else {
				this.ConsoleOut("<WebLoadBookMarks> 向后台发送数�?包错�?...");
				this.Status = "向后台发送数�?包错�?";
				return false;
			}
		} else {
			this.ConsoleOut("<WebLoadBookMarks> 打开链接错�??...");
			this.Status = "打开链接错�??";
			return false;
		}
	}

	/* 打开签名窗口，允许用户输入密码，来获取已经保存起来的印章或�?�名信息。确定�?�名后的信息将保存在服务器的数据库中以便将来验证使用*/
	this.WebOpenSignature = function () {
		var FunExt = this.obj.FuncExtModule;
		FunExt.SetServerType(1);
		if (this.Charset != true) {
			FunExt.SetCharset("UTF-8");					//根据后台传输数据的编�?,判定�?否启�?,默�?�为gb2312
		}
		FunExt.WebUrl = this.WebUrl;
		FunExt.UserName = this.UserName;
		FunExt.FileName = this.FileName;
		FunExt.RecordID = this.RecordID;
		FunExt.FileType = this.FileType;
		FunExt.SetDocument(this.obj.ActiveDocument);
		FunExt.WebSetMsgByName("USERID", "123456");
		FunExt.WebSetMsgByName("USERIDONE", "123456");
		FunExt.WebSetMsgByName("USERIDTWO", "123456");
		FunExt.WebOpenSignature();
		this.Status = FunExt.Status;
		return true;
	}

	/* 不打开签名或印章�?�验窗口，检验�?�名或印章是否合法有�? */
	this.WebCheckSignature = function () {
		var FunExt = this.obj.FuncExtModule;
		FunExt.SetServerType(1);
		if (this.Charset != true) {
			FunExt.SetCharset("UTF-8");					//根据后台传输数据的编�?,判定�?否启�?,默�?�为gb2312
		}
		FunExt.WebUrl = this.WebUrl;
		FunExt.UserName = this.UserName;
		FunExt.FileName = this.FileName;
		FunExt.RecordID = this.RecordID;
		FunExt.FileType = this.FileType;
		FunExt.SetDocument(this.obj.ActiveDocument);
		var i = FunExt.WebCheckSignature();
		FunExt.Alert("检测结果：" + i + "\r\n 注释: (=-1 有非法印�?) (=0 没有任何印章) (>=1 有�?�个合法印章)");
		this.Status = FunExt.Status;
		return true;
	}

	/* 打开签名或印章�?�验窗口，检验�?�名或印章是否合法有�? */
	this.WebShowSignature = function () {
		var FunExt = this.obj.FuncExtModule;
		FunExt.SetServerType(1);
		if (this.Charset != true) {
			FunExt.SetCharset("UTF-8");					//根据后台传输数据的编�?,判定�?否启�?,默�?�为gb2312
		}
		FunExt.WebUrl = this.WebUrl;
		FunExt.UserName = this.UserName;
		FunExt.FileName = this.FileName;
		FunExt.RecordID = this.RecordID;
		FunExt.FileType = this.FileType;
		FunExt.SetDocument(this.obj.ActiveDocument);
		FunExt.WebShowSignature();
		this.Status = FunExt.Status;
		return true;
	}

	/*下载服务器上的文件并保存在本�? */
	this.WebGetFile = function (LocalFile, RemoteFile) {
		this.ConsoleOut("<WebGetFile> 开�?...");
		var httpclient = this.obj.Http; 							//设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("RECORDID", this.RecordID);			//加载RecordID
		this.WebSetMsgByName("LOCALFILE", LocalFile); 				//取得�?地文件名�?
		this.WebSetMsgByName("REMOTEFILE", RemoteFile); 			//取得远程文件名称
		this.WebSetMsgByName("OPTION", "GETFILE");     				//发送�?�求LOADFILE
		httpclient.AddForm("FormData", this.GetMessageString()); 	//这里�?�?定义json 传输格式�?
		this.WebClearMessage();                         			//清除所有WebSetMsgByName参数
		httpclient.ShowProgressUI = true;   						//显示进度�?
		this.ConsoleOut("<WebGetFile> 开始打开链接...");
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) {	//这里采用同�?�方式接收文档数�?�?
			this.ConsoleOut("<WebGetFile> 开始向后台发送数�?�?...");
			if (httpclient.Send()) {
				this.ConsoleOut("<WebGetFile> 数据包发送成�?...");
				if (httpclient.GetResponseHeader("MsgError") == "404") { 	//判断服务器是否存在文�?
					this.ConsoleOut("<WebGetFile> 后台�?找到对应文档");
					this.Status = "后台�?找到对应文档";
					httpclient.Clear();
					this.ConsoleOut("<WebGetFile> 结束...");
					return false;
				}
				this.ConsoleOut("<WebGetFile> 文档下载到本�?");
				httpclient.ResponseSaveToFile(LocalFile);
				this.ConsoleOut("<WebGetFile> 文档下载成功");
				this.Status = "文档下载到本地成�?";
				httpclient.Clear();
				this.ConsoleOut("<WebGetFile> 结束...");
				return true;
			} else {
				this.ConsoleOut("<WebGetFile> 数据包发送失�?...");
				this.Status = "数据包发送失�?";            //Status：状态信�?
				this.ConsoleOut("<WebGetFile> 结束...");
				return false;
			}
		} else {
			this.ConsoleOut("<WebGetFile> 打开连接失败...");
			this.Status = "打开连接失败";               	//Status：状态信�?
			httpclient.Clear();
			this.ConsoleOut("<WebGetFile> 结束...");
			return false;
		}
	}

	/* 将本地文件上传到服务器上，并保存为远程文�?  */
	this.WebPutFile = function (LocalFile, RemoteFile) {
		this.ConsoleOut("<WebPutFile> 开�?...");
		var httpclient = this.obj.Http; 					//设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("REMOTEFILE", RemoteFile); 	//取得远程文件名称
		this.WebSetMsgByName("OPTION", "PUTFILE");     		//发送�?�求LOADFILE
		httpclient.AddForm("FormData", this.GetMessageString());
		httpclient.AddFile("FileData", LocalFile);    		//需要上传的文件 无法判断�?地文档是否存�?，�?�致上传时找不到文档情况下会生成空白文档)
		this.WebClearMessage();
		httpclient.ShowProgressUI = false;           		//隐藏进度�?
		this.ConsoleOut("<WebPutFile> 开始打开链接...");
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) //true 异�?�方�? false同�??
		{
			this.ConsoleOut("<WebPutFile> 开始向后台发送数�?�?...");
			if (!httpclient.Send()) {
				this.ConsoleOut("<WebPutFile> 数据包发送失�?...");
				this.Status = "文档上传失败";
				httpclient.Clear();
				this.ConsoleOut("<WebPutFile> 结束...");
				return false;
			} else {
				this.ConsoleOut("<WebPutFile> 文档上传成功...");
				this.Status = "文档上传成功";
				httpclient.Clear();
				this.ConsoleOut("<WebPutFile> 结束...");
				return true;
			}
		} else {
			this.ConsoleOut("<WebPutFile> 打开链接失败...");
			this.Status = "打开链接失败";
			httpclient.Clear();
			this.ConsoleOut("<WebGetFile> 结束...");
			return false;
		}
	}

	/* 删除指定的本地文件或服务器上的文�? */
	this.WebDelFile = function (LocalFile, RemoteFile) {
		this.ConsoleOut("<WebDelFile> 开�?...");
		if (LocalFile == "") {
			if (RemoteFile != "") {
				this.ConsoleOut("<WebDelFile> 开始删除远程文�?...");
				var httpclient = this.obj.Http; 				//设置http对象
				httpclient.Clear();
				this.WebSetMsgByName("REMOTEFILE", RemoteFile); //取得远程文件名称
				this.WebSetMsgByName("OPTION", "DELFILE");     	//发送�?�求LOADFILE
				httpclient.AddForm("FormData", this.GetMessageString());
				this.WebClearMessage();
				httpclient.ShowProgressUI = false;           	//隐藏进度�?
				this.ConsoleOut("<WebDelFile> 开始打开链接...");
				if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) //true 异�?�方�? false同�??
				{
					this.ConsoleOut("<WebDelFile> 连接成功，开始向后台发送数�?�?...");
					if (!httpclient.Send()) {
						this.ConsoleOut("<WebDelFile> 向后台发送数�?包失�?...");
						this.Status = "向后台发送数�?包失�?";
						httpclient.Clear();
						this.ConsoleOut("<WebDelFile> 结束...");
						return false;
					} else {
						this.ConsoleOut("<WebDelFile> 连接数据包成�?...");
						if (this.WebGetMsgByName("DelFileState") == "成功") {
							this.ConsoleOut("<WebDelFile> 文档删除成功...");
							this.Status = "文档删除成功";
							httpclient.Clear();
							this.ConsoleOut("<WebDelFile> 结束...");
							return true;
						} else {
							this.ConsoleOut("<WebDelFile> 文档不存�?...");
							this.Status = "文档不存�?";
							httpclient.Clear();
							this.ConsoleOut("<WebDelFile> 结束...");
							return false;
						}
					}
				} else {
					this.ConsoleOut("<WebDelFile> 打开链接失败...");
					this.Status = "打开链接失败";
					this.ConsoleOut("<WebDelFile> 结束...");
					return false;
				}
			} else {
				this.ConsoleOut("<WebDelFile> 文件名为空，请输入�?�删除的文件�?...");
				this.Status = "文件名为空，请输入�?�删除的文件�?";
				this.ConsoleOut("<WebDelFile> 结束...");
				return false;
			}
		} else {
			var fs = this.obj.FileSystem;
			this.ConsoleOut("<WebDelFile> 删除�?地文�?...");
			if (fs.DeleteFile(LocalFile)) {
				this.ConsoleOut("<WebDelFile> 删除�?地文档成�?...");
				this.Status = "删除�?地文件成�?";
				this.ConsoleOut("<WebDelFile> 结束...");
				return true;
			} else {
				this.ConsoleOut("<WebDelFile> 该文档不存在或者没有权限删�?...");
				this.Status = "该文档不存在或者没有权限删�?";
				this.ConsoleOut("<WebDelFile> 结束...");
				return false;
			}
		}
	}

	/* 建立新文�? */
	this.CreateFile = function () {
		var docType = this.getDocType(this.FileType); 			// 获取文档类型
		switch (docType) {
			case this.DocType.WORD:
				this.obj.CreateNew("Word.Document"); 				// 创建word
				this.Status = "新建WORD成功";
				break;
			case this.DocType.EXECL:
				this.obj.CreateNew("Excel.Sheet"); 					// 创建execl
				this.Status = "新建EXECL成功";
				break;
			case this.DocType.PICTURE:
				this.obj.CreateNew("iWebPicture.iWebPictureMain"); 	// 创建tif
				this.Status = "新建TIF成功";
				break;
			case this.DocType.PPT:
				this.obj.CreateNew("PowerPoint.Show"); 				// 创建ppt
				this.Status = "新建PPT成功";
				break;
			default:
				this.obj.CreateNew("Word.Document"); 				// 默�?�创建word文档
				this.Status = "新建WORD成功";
				break;
		}
	}

	/* 使Office控件全屏 */
	this.FullSize = function (mValue) {
		this.obj.FullSize = mValue;		//true为全�?,false为关�?全屏
	}

	/*	 文档保存到本地（有�?�话框）
		this.WebSaveLocal = function()
		{
			this.ConsoleOut("<WebSaveLocal> 开�?...");
			this.setShowDialog(this.ShowDialog.DialogSaveCopyAs);
			this.ConsoleOut("<WebSaveLocal> 文档保存到本地成�?...");
			this.Status = "文档保存到本地成�?";
		}*/

	this.WebSaveLocal = function () {
		var fileNamePath;
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			var exts = "所有支持的文件格式(*.docx;*.doc)|*.docx;*.doc";
			exts += "|Word 文档(*.docx)|*.docx";
			exts += "|Word 97-2003文档(*.doc)|*.doc";
			exts += "||";
		} else if (this.FileType == ".xls" || this.FileType == ".xlsx") {
			var exts = "所有支持的文件格式(*.xlsx;*.xls)|*.xlsx;*.xls";
			exts += "|Excel 工作�?(*.xlsx)|*.xlsx";
			exts += "|Excel 97-2003工作�?(*.xls)|*.xls";
			exts += "||";
		}
		fileNamePath = this.obj.FileSystem.FileSaveAs(exts, this.FileName); //this.FileName 显示名称
		var ext = fileNamePath.substring(fileNamePath.length - 3, fileNamePath.length);
		if (fileNamePath == "") {
			return true;
		}
		try {
			this.obj.Save(fileNamePath, 0);
		}
		catch (e) {
			this.Status = e.description;
			alert(e.description);
			return false;
		}
		return true;
	}



	/* 打开�?地文档（有�?�话框）*/
	this.WebOpenLocal = function () {
		this.ConsoleOut("<WebOpenLocal> 开�?...");
		this.setShowDialog(this.ShowDialog.DialogOpen, this.getOpenDocSuffix(this.FileType));
		this.ConsoleOut("<WebOpenLocal> 打开�?地文件成�?...");
		this.Status = "打开�?地文档成�?";
	}


	/* 文档保存到本�? */
	this.WebSaveLocalFile = function (FileName) {
		this.ConsoleOut("<WebSaveLocalFile> 开�?...");
		try {
			this.ConsoleOut("<WebSaveLocalFile> 开始删除之前的文件...");
			this.WebDelLocalFile(FileName);
			this.ConsoleOut("<WebSaveLocalFile> 删除之前的文档成�?...");
			if (this.FileType != ".pdf") {
				var saveState = this.Save(FileName, this.getOfficeVersion(), this.FileType.substring(1).toUpperCase());
			} else {
				var saveState = this.Save(FileName, 0, this.FileType.substring(1).toUpperCase());
			}
			return saveState;
			this.ConsoleOut("<WebSaveLocalFile> 保存�?地文件成�?...");
			this.Status = "保存�?地文件成�?";
		} catch (e) {
			this.Status = e.description;
			this.ConsoleOut("<WebSaveLocalFile> 保存�?地文件失�?..." + e.description);
			this.Status = "保存�?地文档失�?";
			return -1;
		}
	}

	/* 打开�?地文�?	*/
	this.WebOpenLocalFile = function (filePath) {
		this.ConsoleOut("<WebOpenLocalFile> 开�?...");
		if (this.getDocType(this.FileType) == this.DocType.PICTURE) {
			try {
				this.obj.ActiveDocument.WebOpenLocalFile(filePath);
				this.Status = "打开�?地文档成�?";
				return 0;
			} catch (e) {
				this.Status = "打开�?地文档失�?";
				return -1;
			}
		}
		return this.obj.Open(filePath);
		this.ConsoleOut("<WebOpenLocalFile> 打开�?地文件成�?...");
	}

	/* 取得书�?�的�?	*/
	this.WebGetBookMarks = function (BMarksName) {
		this.ConsoleOut("<WebGetBookMarks> 开�?...");
		this.ConsoleOut("<WebGetBookMarks> 判断书�?�是否存�?...");
		if (this.obj.ActiveDocument.BookMarks.Exists(BMarksName)) // 判断�?否存在�?�书�?
		{
			return this.obj.ActiveDocument.Bookmarks.Item(BMarksName).Range.Text;
			this.ConsoleOut("<WebGetBookMarks> 获取书�?�的值成�?...");
		} else {
			this.ConsoleOut("<WebGetBookMarks> " + "名称�?" + BMarksName + "的书签在文档�?不存�?");
			this.Status = "名称�?" + BMarksName + "的书签在文档�?不存�?";
			return "";
		}
	}

	/* 光标处添加书�?(新�?�功�?)	*/
	this.WebAddBookMarks = function (BMarksName, BMarksValue) {
		this.ConsoleOut("<WebAddBookMarks> 开�?...");
		this.ConsoleOut("<WebAddBookMarks> 判断书�?�是否存�?...");
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				if (!this.obj.ActiveDocument.BookMarks.Exists(BMarksName)) // 判断�?否存在�?�书�?
				{
					this.ConsoleOut("<WebAddBookMarks> 获取书�?�的长度...");
					var BMVLength = BMarksValue.length;
					this.ConsoleOut("<WebAddBookMarks> 插入内�??...");
					this.obj.ActiveDocument.Application.Selection.TypeText(BMarksValue);// 插入内�??
					this.obj.ActiveDocument.Application.Selection.MoveLeft(Unit = 1,
						Count = BMVLength);
					this.ConsoleOut("<WebAddBookMarks> 获取书�?�的原�?�位�?...");
					var StartR = this.obj.ActiveDocument.Application.Selection.Start;
					this.ConsoleOut("<WebAddBookMarks> 获取书�?�的最终位�?...");
					var EndR = this.obj.ActiveDocument.Application.Selection.Start + BMVLength;
					this.obj.ActiveDocument.Range(Start = StartR, End = EndR).Select();
					this.ConsoleOut("<WebAddBookMarks> 添加书�??...");
					this.obj.ActiveDocument.Bookmarks.Add(BMarksName);// 添加书�??
					this.ConsoleOut("<WebAddBookMarks> 结束...");
					this.Status = "光标处添加书签成�?";
					return true;
				} else {
					this.ConsoleOut("<WebAddBookMarks> " + "名称�?" + BMarksName + "的书签在文档�?不存�?");
					this.Status = "名称�?" + BMarksName + "的书签在文档�?已存�?";
					this.WebFindBookMarks(BMarksName);
					return false;
				}
			}
			this.Status = "文档�?锁定，操作失�?";
			return false;
		}
		this.Status = "非Word文档，操作失�?";
		return false;
	}

	/* 书�?�定�?	*/
	this.WebFindBookMarks = function (BMarksName) {
		this.ConsoleOut("<WebFindBookMarks> 开�?...");
		this.ConsoleOut("<WebFindBookMarks> 判断书�?�是否存�?...");
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				if (this.obj.ActiveDocument.BookMarks.Exists(BMarksName)) // 判断�?否存在�?�书�?
				{
					var range = this.obj.Range;
					range = this.obj.ActiveDocument.Bookmarks.Item(BMarksName).Range;
					range.Select();
					this.ConsoleOut("<WebFindBookMarks> 书�?�定位成�?...");
					this.Status = "已定位到书�??";
					return true;
				} else {
					this.ConsoleOut("<WebFindBookMarks> " + "名称�?" + BMarksName + "的书签在文档�?不存�?");
					this.Status = "名称�?" + BMarksName + "的书签在文档�?不存�?";
					return false;
				}
			}
			this.Status = "文档�?锁定，操作失�?";
			return false;
		}
		this.Status = "非Word文档，操作失�?";
		return false;
	}

	/* 删除书�??(新�?�功�?) */
	this.WebDelBookMarks = function (BMarksName) {
		this.ConsoleOut("<WebDelBookMarks> 开�?...");
		this.ConsoleOut("<WebDelBookMarks> 判断书�?�是否存�?...");
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				if (this.obj.ActiveDocument.BookMarks.Exists(BMarksName)) 		// 判断�?否存在�?�书�?
				{
					this.ConsoleOut("<WebDelBookMarks> 开始删除书�?...");
					this.obj.ActiveDocument.Bookmarks.Item(BMarksName).Delete();// 删除书�??
					this.ConsoleOut("<WebDelBookMarks> 删除书�?�成�?...");
					this.Status = "删除书�?�成�?";
					return true;
				} else {
					this.ConsoleOut("<WebDelBookMarks> " + "名称�?" + BMarksName + "的书签在文档�?不存�?");
					this.Status = "名称�?" + BMarksName + "的书签在文档�?不存�?";
					return false;
				}
			}
			this.Status = "文档�?锁定，操作失�?";
			return false;
		}
		this.Status = "非Word文档，操作失�?";
		return false;
	}


	/* 将vbmValue值�?�置到书签vbmName位置 */
	this.WebSetBookmarks = function (vbmName, vbmValue) {
		if (this.FileType == ".doc" || this.FileType == ".docx" || this.FileType == ".wps") {
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				try {
					if (this.obj.ActiveDocument.BookMarks.Exists(vbmName)) {				// 判断�?否存在�?�书�?
						var vRange = this.obj.ActiveDocument.Bookmarks.Item(vbmName).Range;
						vRange.text = vbmValue;
						this.obj.ActiveDocument.Bookmarks.Add(vbmName, vRange);
						this.Status = "书�?�赋值成�?";
						return true;
					}
					this.Status = "名称�?" + vbmName + "的书签在文档�?不存�?";
					return false;
				} catch (e) {
					this.Status = e.description;
					return false;
				}
			} else {
				this.Status = "插入失败，文档�??锁定";
				return false;
			}
		} else {
			this.Status = "非Word文档，插入失�?";
			return false;
		}
	}

	this.SetBookMarksFont = function (bMarksName, fColor, fSize, fName, fBold, fItalic) {
		this.WebObject.ActiveDocument.Bookmarks.Item(bMarksName).Range.Select();
		var Selection = this.WebObject.ActiveDocument.Application.Selection;
		Selection.Font.Color = fColor; //字体红色
		Selection.Font.Size = fSize;  //字体大小
		Selection.Font.Name = fName; //字体类型
		Selection.Font.Bold = fBold; // �?否加粗（True加粗，False正常�?
		Selection.Font.Italic = fItalic; // �?否斜体（True斜体，False正常�?
	}

	/* 设置显示或隐藏工具栏				（需要修改控件代码） */
	this.WebToolsVisible = function (ToolName, Visibled) {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 设置工具栏按�?�?否有�?			（需要修改控件代码） */
	this.WebToolsEnable = function (ToolName, ToolIndex, Enabled) {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 打印文档 */
	this.WebOpenPrint = function () {
		this.setShowDialog(this.ShowDialog.DialogPrint);
	}

	/* 打开插入图片窗口 */
	this.WebOpenPicture = function () {
		if (this.FileType == ".doc" || this.FileType == ".docx" || this.FileType == ".wps") {
			var spDlg = this.obj.ActiveDocument.Application.Dialogs.Item(163);
			spDlg.Show();
		} else if (this.FileType == ".xls" || this.FileType == ".xlsx" || this.FileType == ".et") {
			var spDlg = this.obj.ActiveDocument.Application.Dialogs.Item(342);
			spDlg.Show();
		}
	}

	/* 使文档重新获得焦点刷新文�? */
	this.WebRefresh = function () {
		this.obj.Activate(true);
	}

	/* 设置文档痕迹保留的状�? */
	this.WebSetRevision = function (Show, Track, Print, Tool) {
		var strCustomUI;
		strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
		<ribbon startFromScratch="false"> \
			<tabs> \
	    		<tab idMso="TabReviewWord" visible="false" /> \
			</tabs> \
		</ribbon> \
		</customUI>';
		if (this.getDocType(this.FileType) == this.DocType.WORD
			&& this.obj.ActiveDocument.ProtectionType == "-1") {
			this.obj.ActiveDocument.TrackRevisions = Track; // 显示标�?�和隐藏标�??
			this.obj.ActiveDocument.ShowRevisions = Show; // 显示痕迹或隐�?
			this.obj.ActiveDocument.PrintRevisions = Print; //控制�?否打印痕�?
			if (!Tool) {
				this.obj.RibbonCustomUI = strCustomUI;
				this.Status = "设置痕迹成功";
				return true;
			} else {
				this.Status = "设置痕迹成功";
				return true;
			}
		} else {
			this.Status = "非word文档，无法执�?";
			return false;
		}
	}

	/* 使文档保护状�? */
	this.WebSetProtect = function (Boolean, password) {
		var docType = this.getDocType(this.FileType);
		if (password == "") {
			password = this.PASSWORD;
		}
		Boolean ? this.VBAProtectDocument(docType, password) : this
			.VBAUnProtectDocument(docType, password);
	}

	/* 下载由Url指定的文件，保存为FileName�?径的�?地文�? */
	this.WebDownLoadFile = function (Url, FileName) {
		var kwoHttpGet = 0;
		var kwoHttpPost = 1;
		var httpclient = this.obj.Http;
		httpclient.Clear();
		httpclient.ShowProgressUI = true;
		httpclient.Hidden = false;
		// 异�?�下载先调用OnSendEnd(),再调用OnRecEnd()
		this.ConsoleOut("<WebDownLoadFile> 开�?...");
		this.ConsoleOut("<WebDownLoadFile> 开始打开服务�?...");
		var info = httpclient.Open(kwoHttpGet, Url, false);
		if (info) {
			var send = httpclient.Send();
			if (send) {
				if (httpclient.Status == 200) {
					httpclient.ResponseSaveToFile(FileName);
					this.ConsoleOut("<WebDownLoadFile>下载成功");
					this.Status = "下载成功";
					return true;
				} else {
					this.ConsoleOut("<WebDownLoadFile>下载失败");
					this.Status = "下载失败请�?��?URL�?否�?�确";
					return false;
				}
			} else {
				this.ConsoleOut("<WebDownLoadFile> 数据包发送失�?...");
				this.Status = "数据包发送失�?";               // Status：状态信�?
				this.ConsoleOut("<WebDownLoadFile> 结束...");
				return false;
			}
		} else {
			this.ConsoleOut("<WebDownLoadFile> 打开连接失败...");
			this.Status = "打开连接失败";               // Status：状态信�?
			httpclient.Clear();
			this.ConsoleOut("<WebDownLoadFile> 结束...");
			return false;
		}
	}

	/* 建立�?地目�?	*/
	this.WebMkDirectory = function (DirName) {
		this.ConsoleOut("<WebMkDirectory> 开始建立本地目�?...");
		var fs = this.obj.FileSystem; // 创建file对象�?
		if (fs.CreateDirectory(DirName)) {
			this.ConsoleOut("<WebMkDirectory> 建立�?地目录成�?...");
			this.Status = "创建�?录成�?";
			return true;
		} else {
			this.ConsoleOut("<WebMkDirectory> 建立�?地目录失�?...");
			this.Status = "创建�?录失败�?�查是否已经有此目录或者路径不�?";
			return false;
		}
	}

	/* 删除�?地目�? */
	this.WebRmDirectory = function (DirName) {
		this.ConsoleOut("<WebRmDirectory> 删除�?地目�?...");
		var fs = this.obj.FileSystem; // 创建file对象�?
		fs.ClearDirectory(DirName);  //此接口无返回�?
		this.ConsoleOut("<WebRmDirectory> 删除�?地目录成�?...");
		this.Status = "删除�?录成�?";
		return true;
	}

	/* 判断�?地文件是否存�?		(需要控件修改代�?) */
	this.WebFileExists = function (FileName) {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 得到某文件的大小 */
	this.WebFileSize = function (FileName) {
		this.ConsoleOut("<WebFileSize> 得到某文件的大小...");
		var fs = this.obj.FileSystem; // 创建file对象�?
		var fsize = fs.GetFileSize(FileName);
		if (fsize == 0) {
			this.ConsoleOut("<WebFileSize> 获取文件失败，�?��?�查�?�应�?�?...");
			this.Status = "请�?�查文件路径是否有�?";
			return 0;
		} else {
			this.ConsoleOut("<WebFileSize> 获取文件成功大小�?" + fsize + "字节");
			this.Status = "获取成功大小�?" + fsize + "字节";
			return fsize;
		}
	}

	/* 得到某文件的�?改日�?		（需要控件修改代码） */
	this.WebFileDate = function (FileName) {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 禁�?�指定菜单项 */
	this.DisableMenu = function (MenuName) {
		var custommenu = this.obj.CustomMenu;
		return custommenu.DisableMenu(MenuFile, MenuName);
	}

	/* 允�?�菜单项有效 */
	this.EnableMenu = function (MenuName) {
		var custommenu = this.obj.CustomMenu;
		return custommenu.EnableMenu(MenuFile, MenuName);
	}

	/* 禁�?�指定快捷键的功�?		（需要控件修改代码） */
	this.DisableKey = function (KeyName) {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 建立注册表�?�象 */
	this.WebOpenKey = function (mRoot, mValue) {
		this.ConsoleOut("<WebOpenKey> 开�?...");
		root = mRoot;
		rootValue = mValue;
		var register = this.obj.Register;
		this.ConsoleOut("<WebOpenKey> 建立注册表�?�象...");
		var res = register.QueryStringValue(root, rootValue, "");
		if (!res) {
			this.ConsoleOut("<WebOpenKey> 建立注册对象失败，注册表对象不存�?，开始新建注册表对象...");
			register.SetStringValue(root, rootValue, "", "");
			this.ConsoleOut("<WebOpenKey> 新建注册表�?�象成功...");
			this.Status = "建立注册对象失败，已新建注册表�?�象";
			this.ConsoleOut("<WebOpenKey> 结束...");
			return res;
		}
		this.ConsoleOut("<WebOpenKey> 建立注册对象成功...");
		this.Status = "建立注册对象成功";
		this.ConsoleOut("<WebOpenKey> 结束...");
		return true;
	}

	/* 写入由Name指定的注册表项的值Value（字符串类型的值） */
	this.WebWriteString = function (mKey, mValue) {
		this.ConsoleOut("<WebWriteString> 开�?...");
		var register = this.obj.Register;
		if (root != undefined && rootValue != undefined) {
			this.ConsoleOut("<WebWriteString> 开始写入注册表项的�?...");
			var res = register.SetStringValue(root, rootValue, mKey, mValue);
			this.ConsoleOut("<WebWriteString> 写入注册表成�?...");
			this.Status = "写入注册表成�?";
			this.ConsoleOut("<WebWriteString> 结束...");
			return res;
		}
		this.ConsoleOut("<WebWriteString> 写入注册表失败，请先调用WebOpenKey接口...");
		this.Status = "写入注册表失败，请先调用WebOpenKey接口";
		this.ConsoleOut("<WebWriteString> 结束...");
		return false;
	}

	/* 读取由Name指定的注册表项的值（字�?�串类型的值） */
	this.WebReadString = function (mKey) {
		this.ConsoleOut("<WebReadString> 开�?...");
		var register = this.obj.Register;
		if (root != undefined && rootValue != undefined) {
			this.ConsoleOut("<WebReadString> 开始�?�取由注册表项的�?...");
			var res = register.QueryStringValue(root, rootValue, mKey);
			if (res != null || res != "") {
				this.ConsoleOut("<WebReadString> 读取注册表成�?...");
				this.Status = "读取注册表成�?";
				this.ConsoleOut("<WebReadString> 结束...");
				return res;
			} else {
				this.ConsoleOut("<WebReadString> 读取失败，没有�?�应的注册表...");
				this.Status = "读取失败，没有�?�应的注册表";
				this.ConsoleOut("<WebReadString> 结束...");
				return res;
			}
		}
		this.ConsoleOut("<WebReadString> 写入注册表失败，请先调用WebOpenKey接口...");
		this.Status = "读取注册表失败，请先调用WebOpenKey接口";
		this.ConsoleOut("<WebReadString> 结束...");
		return false;
	}


	/* 写入由Name指定的注册表项的值Value */
	this.WebWriteInteger = function (mKey, mValue) {
		this.ConsoleOut("<WebWriteInteger> 开�?...");
		var register = this.obj.Register;
		if (root != undefined && rootValue != undefined) {
			this.ConsoleOut("<WebWriteInteger> 开始写入注册表项的�?...");
			var res = register.SetDWordValue(root, rootValue, mKey, mValue);
			this.ConsoleOut("<WebWriteInteger> 写入注册表成�?...");
			this.Status = "写入注册表成�?";
			this.ConsoleOut("<WebWriteInteger> 结束...");
			return res;
		}
		this.ConsoleOut("<WebWriteInteger> 写入注册表失败，请先调用WebOpenKey接口...");
		this.Status = "写入注册表失败，请先调用WebOpenKey接口";
		this.ConsoleOut("<WebWriteInteger> 结束...");
		return false;
	}

	/* 读取由Name指定的注册表项的值Value */
	this.WebReadInteger = function (mKey) {
		this.ConsoleOut("<WebReadInteger> 开�?...");
		var register = this.obj.Register;
		if (root != undefined && rootValue != undefined) {
			this.ConsoleOut("<WebReadInteger> 开始�?�取注册表项的�?...");
			var res = register.QueryDWORDValue(root, rootValue, mKey);
			if (res != null || res != "") {
				this.ConsoleOut("<WebReadInteger> 读取注册表成�?...");
				this.Status = "读取注册表成�?";
				this.ConsoleOut("<WebReadInteger> 结束...");
				return res;
			} else {
				this.ConsoleOut("<WebReadInteger> 读取失败，没有�?�应的注册表...");
				this.Status = "读取失败，没有�?�应的注册表";
				this.ConsoleOut("<WebReadInteger> 结束...");
				return res;
			}
		}
		this.ConsoleOut("<WebReadInteger> 读取注册表失败，请先调用WebOpenKey接口...");
		this.Status = "读取注册表失败，请先调用WebOpenKey接口";
		this.ConsoleOut("<WebReadInteger> 结束...");
		return false;
	}

	/* 盖章时自动载入日期信�?附加在所盖的印章�?	（需要控件修改代码） */
	this.ShowDate = function (DateString, Align) {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 将文档中的印章变为黑白或彩色		（需要控件修改代码） */
	this.SignatureColor = function (Flag) {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 返回当前文档�?的印章或签名�?�?		（需要控件修改代码） */
	this.SignatureCount = function (Flag) {
		this.Status = "该功能暂�?提供!";
		return 0;
	}

	/* 用来定位某用户的最后一�?印章。光标定位到该印章上	（需要控件修改代码） */
	this.GetSignature = function (UserName) {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 接受当前文档�?所有的痕迹 */
	this.ClearRevisions = function () {
		if (this.getDocType(this.FileType) == this.DocType.WORD && this.obj.ActiveDocument.ProtectionType == "-1") {
			try {
				this.obj.Activate(true);
				this.obj.ActiveDocument.AcceptAllRevisions();
				this.obj.ActiveDocument.Revisions.AcceptAll();
				this.Status = "痕迹接受成功";
				return this.obj.ActiveDocument.Revisions.Count >= 0 ? true : false;
			}
			catch (e) {
				this.Status = "痕迹接受失败，错�?原因�?" + e.description;
				return false;
			}
		} else {
			this.Status = "痕迹接受失败，错�?原因�?" + e.description;
			return false;
		}
	}


	/* 用于删除�?录下的文�? */
	this.WebDelTree = function (Directory) {
		this.ConsoleOut("<WebDelTree> 删除�?录下的文�?...");
		var fs = this.obj.FileSystem;
		fs.ClearDirectory(Directory); //此接口无返回�?
		this.ConsoleOut("<WebDelTree> 删除成功...");
		return true;
	}

	/* 用于取得当前控件的版�?号信�? */
	this.Version = function () {
		return this.obj.Version;
	}

	// 用于取得当前控件的生产版�?信息		（需要控件修改代码） */
	this.VersionEx = function () {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 按�?�置的打印份数进行打�? */
	this.PrintByCopies = function (Copies, Show) {
		parseInt(Copies);
		this.ConsoleOut("<PrintByCopies> 开�?...");
		var mAPIObj;
		var mApiName;
		var mAppName;
		mAppName = this.obj.ActiveDocument.Application.Name;
		this.ConsoleOut("<PrintByCopies> 判断文档�?否是word类型...");
		if (mAppName == "Microsoft Word") {
			this.ConsoleOut("<PrintByCopies> 调用打印机功�?...");
			mAPIObj = this.obj.ActiveDocument.Application.Dialogs.Item(88);
			this.ConsoleOut("<PrintByCopies> �?否调用打印机窗口...");
			if (Show) {
				this.ConsoleOut("<PrintByCopies> 设置带窗口的打印份数...");
				mAPIObj.NumCopies = Copies;
				this.ConsoleOut("<PrintByCopies> �?用打印机窗口...");
				if (mAPIObj.Display == -1) {
					this.ConsoleOut("<PrintByCopies> 开始打�?...");
					mAPIObj.Execute;
					this.ConsoleOut("<PrintByCopies> 打印成功...");
					this.Status = "打印成功";
					this.ConsoleOut("<PrintByCopies> 结束...");
					return true;
				} else {
					this.ConsoleOut("<PrintByCopies> �?用打印机窗口失败...");
					this.Status = "�?用打印机窗口失败";
					this.ConsoleOut("<PrintByCopies> 结束...");
					return false;
				}
			} else {
				this.ConsoleOut("<PrintByCopies> 不启用打印机窗口...");
				this.ConsoleOut("<PrintByCopies> 设置不带窗口打印份数...");
				mAPIObj.NumCopies = Copies;
				this.ConsoleOut("<PrintByCopies> 开始打�?...");
				mAPIObj.Execute;
				this.Status = "打印成功";
				this.ConsoleOut("<PrintByCopies> 结束...");
				return true;
			}
		} else {
			this.ConsoleOut("<PrintByCopies> 文档不是word类型...");
			this.Status = "文档不是word类型";
			this.ConsoleOut("<PrintByCopies> 结束...");
			return false;
		}
	}

	/* 用于获取�?�?处理器CUPID			（需要控件修改代码） */
	this.WebGetCpuID = function () {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 用于获取�?盘序列号IDEID			（需要控件修改代码） */
	this.WebGetIdeID = function () {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 将HTML文本生成Word�?识别的格式，之后带格式可插入Word文档	（需要控件修改代码） */
	this.BuildContentFromHTML = function (HTMLText) {
		this.Status = "该功能暂�?提供!";
		return false;
	}

	/* 执�?�宏 */
	this.WebRunMacro = function (MarcroName, MacroValue) {
		try {
			var VBAStr = MacroValue;
			var VBCom = this.obj.ActiveDocument.VBProject.VBComponents.Add(1);
			VBCom.CodeModule.AddFromString(VBAStr);
			this.obj.ActiveDocument.Application.Run(MarcroName);
			return true;
		} catch (e) {
			return false;
		}
	}

	/* 文档对比 */
	this.WebDocumentCompare = function (FileName1, FileName2) {
		// 与当前打开的文档进行�?�比
		this.ConsoleOut("<WebDocumentCompare> 文档对比开�?...");
		this.Status = '';
		var fs = this.obj.FileSystem; // 创建file对象�?
		var filePath = this.DownFilePath(); //获取文件保存临时�?�?
		var saveFilePath = filePath + Math.round(Math.random() * 100000000)
			+ '.doc';
		if ((FileName1 == null) || (FileName1 == '')) {
			if ((FileName2 == null) || (FileName2 === '')) {
				this.Status = '【FileName2】不能为�?!';
				return false;
			} else {
				// 开始下载FileName2
				this.ConsoleOut("<WebDocumentCompare>与当前文档进行�?�比 ");
				this.ConsoleOut("<WebDocumentCompare> 开始下�?..." + FileName2);
				if (this.WebDownloadFile_int(FileName2)) {
					this.ConsoleOut("<WebDocumentCompare> " + '文件�?' + FileName2 + '】下载成�?');
					FileName1 = this.FileName;
					this.WebSaveLocalFile(filePath + FileName1);
					this.ConsoleOut("<WebDocumentCompare> 开始把当前文档下载到本�?...");
					this.WebClose();
					this.ConsoleOut("<WebDocumentCompare> 开始文档�?�比...");
					var bRet = this.obj.FuncExtModule.WebDocumentCompare(
						filePath + FileName1, this.DownloadedFileTempPathName,
						saveFilePath);
					if (bRet == true) {
						this.ConsoleOut("<WebDocumentCompare> 文档对比成功");
						this.obj.Open(saveFilePath);
						this.ConsoleOut("<WebDocumentCompare> 文档对比成功打开最终文�?...");
						this.obj.ActiveDocument.Application.ActiveWindow.View.type = 3;
						this.obj.ActiveDocument.Application.ActiveWindow.View.SplitSpecial = 20;
						return true;
					} else {
						this.ConsoleOut("<WebDocumentCompare> 文档对比失败,请确认待对比的文档是否能够�?�常打开!");
						this.Status = '文档对比失败,请确认待对比的文档是否能够�?�常打开!';
						return false;
					}
				} else {
					this.ConsoleOut("<WebDocumentCompare> " + '文件�?' + FileName2 + '】下载失败，该文档可能在服务器不存在!');
					this.Status = '文件�?' + FileName2 + '】下载失败，该文档可能在服务器不存在!';
					return false;
				}
			}
		}
		// 与指定的后台两个文档进�?��?�比
		else {
			this.ConsoleOut("<WebDocumentCompare> 后台两个文档进�?��?�比 ");
			if ((FileName2 == null) || (FileName2 == '')) {
				this.Status = '【FileName2】不能为�?!';
				return false;
			} else {
				// 开始分�?下载指定的两�?文件
				this.ConsoleOut("<WebDocumentCompare> 分别下载两个文档 ");
				if (this.WebDownloadFile_int(FileName1)) {
					this.ConsoleOut("<WebDocumentCompare> " + '文件�?' + FileName1 + '】下载成�?');
					if ((this.WebDownloadFile_int(FileName2))) {
						this.ConsoleOut("<WebDocumentCompare> " + '文件�?' + FileName2 + '】下载成�?');
						this.WebClose();
						var bRet = this.obj.FuncExtModule.WebDocumentCompare(
							filePath + FileName1, filePath + FileName2,
							saveFilePath);
						this.ConsoleOut("<WebDocumentCompare> 开始文档�?�比 ");
						if (bRet == true) {
							this.ConsoleOut("<WebDocumentCompare> 文档对比成功...");
							this.obj.Open(saveFilePath);
							this.ConsoleOut("<WebDocumentCompare> 文档对比成功打开最终文�?...");
							this.obj.ActiveDocument.Application.ActiveWindow.View.type = 3 //
							this.obj.ActiveDocument.Application.ActiveWindow.View.SplitSpecial = 20;
							return true;
						} else {
							this.ConsoleOut("<WebDocumentCompare> 文档对比失败,请确认待对比的文档是否能够�?�常打开!");
							this.Status = '文档对比失败,请确认待对比的文档是否能够�?�常打开!';
							return false;
						}
					} else {
						this.ConsoleOut("<WebDocumentCompare> " + '文件�?' + FileName2 + '】下载失败，该文档可能在服务器不存在!');
						this.Status = '文件�?' + FileName2 + '】下载失败，该文档可能在服务器不存在!';
						return false;
					}
				} else {
					this.ConsoleOut("<WebDocumentCompare> " + '文件�?' + FileName1 + '】下载失败，该文档可能在服务器不存在!');
					this.Status = '文件�?' + FileName1 + '】下载失败，该文档可能在服务器不存在!';
					return false;
				}
			}
		}
	}

	/* 设置控件�?�? �?:颜色格式为BGR格式*/
	this.WebSetSkin = function (titleBarColor/* 控件标�?�栏颜色 */,
		menuBarStartColor/* �?定义菜单开始�?�色 */,
		menuBarButtonStartColor/* �?定义工具栏按�?开始�?�色 */,
		menuBarButtonEndColor/* �?定义工具栏按�?结束颜色 */,
		menuBarButtonFrameColor/* �?定义工具栏按�?边�?��?�色 */,
		CustomToolbarStartColor/* �?定义工具栏开始�?�色 */, TitleBarTextColor/* 控件标�?�栏文本颜色 */) {
		this.Status = '';
		var style = this.obj.Style;
		try {
			style.TitleBarColor = titleBarColor;
			if ((TitleBarTextColor == undefined) || (TitleBarTextColor == '')) // 设置默�?�标题文字�?�色
			{
				style.TitleBarTextColor = 0x000000; // 黑色
			} else {
				style.TitleBarTextColor = TitleBarTextColor;
			}
			titleBarColor = 0xf2f9ff;
			CustomToolbarStartColor = 0xf2f9ff;
			menuBarStartColor = 0xf2f9ff;
			menuBarButtonStartColor = 0x90cbf4;
			menuBarButtonEndColor = 0x90cbf4;
			menuBarButtonFrameColor = 0x90cbf4;
			style.MenuBarStartColor = menuBarStartColor;
			style.MenuBarEndColor = 0xFFFFFF;
			style.MenuBarTextColor = 0x000000;
			style.MenuBarHighlightTextColor = 0x000000;
			style.MenuBarButtonStartColor = menuBarButtonStartColor;
			style.MenuBarButtonEndColor = menuBarButtonEndColor;
			style.MenuBarButtonFrameColor = menuBarButtonFrameColor;
			style.CustomToolbarStartColor = CustomToolbarStartColor;
			style.CustomToolbarEndColor = CustomToolbarStartColor;
			style.Invalidate();
			return true;
		} catch (e) {
			this.Status = '�?肤�?�置错�??，错�?信息为：' + e.description;
			return false;
		}
	}

	/* �?否启用iWebOffice对象内的拷贝功能。非控件的不受影�? */
	this.WebEnableCopy = function (mValue) {
		this.ConsoleOut("<WebEnableCopy> 开�? ");
		switch (mValue) {
			case 0:
			case false:
			case "0":
				this.obj.CopyEnabled = false;
				this.ConsoleOut("<WebEnableCopy> 不允许Copy ");
				this.Status = "禁�?��?�制";
				break; // �?�?
			case 1:
			case "1":
			case true:
				this.obj.CopyEnabled = true;
				this.ConsoleOut("<WebEnableCopy> 允�?�Copy ");
				this.Status = "允�?��?�制";
				break; // 关闭
			default:
				;
				return;
		}
	}

	/* 显示痕迹和隐藏痕�? */
	this.VBAShowRevisions = function (mValue) {
		this.ConsoleOut("<VBAShowRevisions> 开始隐藏痕迹。。�? ");
		if (this.getDocType(this.FileType) == this.DocType.WORD
			&& this.obj.ActiveDocument.ProtectionType == "-1") {
			this.obj.ActiveDocument.TrackRevisions = mValue; 	// 显示标�?�和隐藏标�??
			this.obj.ActiveDocument.ShowRevisions = mValue; 	// 显示痕迹或隐�?
			this.ConsoleOut("<VBAShowRevisions> 隐藏痕迹成功 ");
			this.Status = "隐藏痕迹成功";
			return true;
		} else {
			this.ConsoleOut("<VBAShowRevisions> 非Office文档或文档已�?锁定，无法执行操�? ");
			this.Status = "非Office文档或文档已�?锁定，无法执行操�?";
			return false;
		}
	}

	/* 保护文档 */
	this.VBAProtectDocument = function (docType, password) {
		this.ConsoleOut("<VBAProtectDocument> 开始。。�? ");
		if (docType == this.DocType.WORD) // word 保护模式
		{
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				this.obj.ActiveDocument.Protect(2, false, password); //�?一�?参数�? 3�?以�?�制  2不能复制
				this.ConsoleOut("<VBAProtectDocument> 文档锁定成功。。�? ");
				this.Status = "文档已�??锁定";
				return true;
			} else {
				this.ConsoleOut("<VBAProtectDocument> 文档锁定失败。。�? ");
				this.Status = "文档锁定失败";
				return false;
			}
		} else if (docType == this.DocType.EXECL) // Excel
		// 保护模式，这里只保护表单1,其他的按�?己需求编�?
		{
			if (!this.obj.ActiveDocument.Application.Sheets(1).ProtectContents) // 判断表单�?否是保护�?
			{
				this.obj.ActiveDocument.Application.Sheets(1).Protect(password);
				this.ConsoleOut("<VBAProtectDocument> 文档锁定成功。。�? ");
				this.Status = "文档锁定成功";
			} else {
				this.ConsoleOut("<VBAProtectDocument> 文档已锁定。。�? ");
				this.Status = "文档已锁�?";
				return false;
			}
		} else {
			this.ConsoleOut("<VBAProtectDocument> 非Office文档，无法执行锁定操作。。�? ");
			this.Status = "非Office文档，无法执行锁定操�?";
			return false;
		}
	}

	/* 根据密码解除保护 */
	this.VBAUnProtectDocument = function (docType, password) {
		this.ConsoleOut("<VBAUnProtectDocument> 开始。。�? ");
		var docType = this.getDocType(this.FileType);
		if (docType == this.DocType.WORD) // word 保护模式
		{
			this.obj.ActiveDocument.Unprotect(password);
			this.ConsoleOut("<VBAUnProtectDocument> WORD解除保护。。�? ");
			this.Status = "WORD解除保护";
			return true;
		} else if (docType == this.DocType.EXECL) {
			this.obj.ActiveDocument.Application.Sheets(1).Unprotect(password);
			this.ConsoleOut("<VBAUnProtectDocument> EXCEL解除保护。。�? ");
			this.Status = "解除保护";
			return true;
		} else {
			this.ConsoleOut("<VBAUnProtectDocument> 非Office文档，无法执行解锁操作。。�? ");
			this.Status = "非Office文档，无法执行解锁操�?";
			return false;
		}
	}

	/* 把文档插入到指定的书签位�? */
	this.VBAInsertFile = function (Position, FileName) {
		try {
			var docType = this.getDocType(this.FileType);
			if (docType == this.DocType.WORD) {
				this.obj.Activate(true);
				if (this.obj.ActiveDocument.BookMarks.Exists(Position)) {
					setTimeout(this.obj.ActiveDocument.Application.Selection.GoTo(-1, 0, 0, Position), 200);
				}
				this.obj.Activate(true);
				this.obj.ActiveDocument.Application.Selection.InsertFile(FileName, "", false, false, false);
				return true;
			} else {
				this.Status = "非Office文档，无法执行插入文档操�?";
				return false;
			}
		} catch (e) {
			this.Status = "插入文档失败，错�?原因�?" + e.description;
			return false;
		}
	}

	/* 接受�?地文档中的所有痕�? */
	this.WebAcceptAllRevisions = function (FileName) {
		this.obj.FuncExtModule.WebAcceptAllRevisions(FileName);
		this.Status = "接受所有痕迹成�?!";
		return false;
	}

	/* 设置WORD用户�? */
	this.VBASetUserName = function (UserName) {
		if (this.FileType != ".ppt" && this.FileType != ".pptx") {
			try {
				this.WebObject.ActiveDocument.Application.Options.UseLocalUserInfo = true;	//设置不�?�是否登录Office都�?�终使用UserName�?
				this.OfficeUserName = this.obj.ActiveDocument.Application.UserName;			//保存office�?�?用户名以做还原用
				this.obj.ActiveDocument.Application.UserName = UserName; 					//痕迹名称
				this.obj.ActiveDocument.Application.UserInitials = UserName; 				//批注名称
			} catch (e) {
				this.OfficeUserName = this.obj.ActiveDocument.Application.UserName;			//保存office�?�?用户名以做还原用
				this.obj.ActiveDocument.Application.UserName = UserName; 					//痕迹名称
				this.obj.ActiveDocument.Application.UserInitials = UserName; 				//批注名称
			}
		}
	}

	/* 设置�? */
	this.ShowField = function () {
		try {
			this.obj.ActiveDocument.ActiveWindow.View.ShowDrawings = true;
			this.obj.ActiveDocument.ActiveWindow.View.ShowFieldCodes = false;
			return true;
		} catch (e) {
			this.Status = "设置域失败，错�??原因�?" + e.description;
			return false;
		}
	}

	/* 区域保护 */
	this.WebAreaProtect = function (BMarksName) {
		this.ConsoleOut("<WebAreaProtect> 开始。。�? ");
		if (this.FileType == ".doc" || this.FileType == ".docx") // word 保护模式
		{
			if (!this.obj.ActiveDocument.BookMarks.Exists(BMarksName)) {
				this.ConsoleOut("<WebAreaProtect> 文件内没有名称为'" + BMarksName + "'的书�? ,请先添加书�??!");
				this.Status = "文件内没有名称为'" + BMarksName + "'的书�? ,请先添加书�??!";
				return false;
			}

			var range = this.obj.Range;
			range = this.obj.ActiveDocument.Bookmarks.Item(BMarksName).Range;
			range.Select();
			this.ConsoleOut("<WebAreaProtect> 选中书�?�位�?。。�? ");
			this.obj.ActiveDocument.bookmarks.Item(BMarksName).range.editors.add(-1); 	// 常量：wdeditoreveryone=-1
			this.obj.ActiveDocument.Protect(3, false, "123", false, false);				// 常量：wdAllowOnlyReading=3
			this.ConsoleOut("<WebAreaProtect> 保护书�?�内容。。�? ");
			this.obj.ActiveDocument.Application.Selection.MoveLeft(Unit = 1,
				Count = 1);
			this.obj.ActiveDocument.ActiveWindow.View.ShadeEditableRanges = false;		// 取消"突出显示�?编辑区域"
			this.ConsoleOut("<WebAreaProtect> 突出显示�?编辑区域。。�? ");
			this.Status = "操作成功，书签区域可编辑";
			return true;
		} else {
			this.ConsoleOut("<WebAreaProtect> 非Office文档，无法执行区域保护操�?! ");
			this.Status = "非Office文档，无法执行区域保护操�?!";
			return false;
		}
	}

	/* 取消区域保护 */
	this.WebAreaUnprotect = function (BMarksName) {
		this.ConsoleOut("<WebAreaUnprotect> 开始。。�? ");
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			this.ConsoleOut("<WebAreaUnprotect> 判断书�?�是否存在。。�? ");
			if (this.obj.ActiveDocument.BookMarks.Exists(BMarksName)) 		// 判断�?否存在�?�书�?
			{
				try {
					this.ConsoleOut("<WebAreaUnprotect> 解保护。。�? ");
					this.obj.ActiveDocument.Unprotect("123");				// 解保�?
					var range = this.obj.Range;
					range = this.obj.ActiveDocument.Bookmarks.Item(BMarksName).Range;
					range.Select();											// 选定书�?�内�?
					this.ConsoleOut("<WebAreaUnprotect> 选中书�?�内容。。�? ");
					this.obj.ActiveDocument.DeleteAllEditableRanges(-1); 	// 去掉突出显示
					this.ConsoleOut("<WebAreaUnprotect> 去掉突出显示。。�? ");
					this.Status = "书�?�区�?" + BMarksName + "�?以编�?";
					return true;
				} catch (e) {
					this.ConsoleOut("<WebAreaUnprotect> 执�?�取消区域保护时出现错�??，错�?原因�?" + e.description);
					this.Status = "执�?�取消区域保护时出现错�??，错�?原因�?" + e.description;
					return false;
				}
			} else {
				this.ConsoleOut("<WebAreaUnprotect> 文档�?不存�?<" + BMarksName + ">的书�?");
				this.Status = "文档�?不存�?<" + BMarksName + ">的书�?";
				return false;
			}
		} else {
			this.ConsoleOut("<WebAreaUnprotect> 非Office文档，无法执行取消区域保护操�?!");
			this.Status = "非Office文档，无法执行取消区域保护操�?!";
			return false;
		}
	}

	/* 获取焦点 */
	this.Activate = function (blnValue) {
		this.obj.Activate(blnValue);
	}

	/* 设置word的页�? */
	this.WebPageCode = function () {
		this.obj.ActiveDocument.Application.Dialogs(294).Show();
	}

	/* 控制2015标�?�栏 */
	this.ShowTitleBar = function (mValue) {
		var style = this.obj.Style;
		style.ShowTitleBar = mValue;
	}

	/* 控制2015�?定义菜单�? */
	this.ShowCustomToolBar = function (mValue) {
		var style = this.obj.Style;
		style.ShowCustomToolbar = mValue;
	}

	/* 控制2015菜单�? */
	this.ShowMenuBar = function (mValue) {
		var style = this.obj.Style;
		switch (mValue) {
			case 0:
			case false:
			case "0": style.ShowMenuBar = false; break; //隐藏菜单�?
			case 1:
			case "1":
			case true: style.ShowMenuBar = true; break; //显示菜单�?
			default: ; return;
		}
	}

	/* 控制Office工具�? */
	this.ShowToolBars = function (mValue) {
		var style = this.obj.Style;
		style.ShowToolBars = mValue;
		style.ShowToolSpace = mValue;
	}

	/* 控制2015状态栏 */
	this.ShowStatusBar = function (mValue) {
		var style = this.obj.Style;
		style.ShowStatusBar = mValue;
	}

	/* 显示/隐藏手写签批工具�? */
	this.ShowCustomToolbar = function (bVal) {
		this.obj.Style.ShowCustomToolbar = bVal;
	}

	/* 显示和隐藏痕�? 隐藏痕迹时之前的痕迹不受影响 */
	this.WebShow = function (blnValue) {
		if (this.getDocType(this.FileType) == this.DocType.WORD) {
			this.VBAShowRevisions(blnValue);
		}
	}

	/* �?否启用iWebOffice对象内文档的保存功能 */
	this.SaveEnabled = function (mBoolean) {
		this.obj.SaveEnabled = mBoolean;
	}

	/* �?否启用iWebOffice对象内文档的打印功能 */
	this.PrintEnabled = function (mBoolean) {
		this.obj.PrintEnabled = mBoolean;
	}

	/* 解决在Firefox和chrome浏�?�器下调用iWebPlugin崩溃的问题需要在load()函数下加如下代码 */
	this.HookEnabled = function () {
		if (this.getDocType(this.FileType) == this.DocType.WORD) {
			this.obj.Style.ShowToolSpace = true;
			this.obj.SelectionInformationEnabled = false;
		}
		if (!((window.ActiveXObject != undefined)
			|| (window.ActiveXObject != null) || "ActiveXObject" in window)) {
			this.obj.HookEnabled = false;
		}
	}
	// ---------------------------------------------------对�?�接�?-------------------------------------------------------------------//
	// ******************************************************************************************************************************


	// ******************************************************************************************************************************//
	// ---------------------------------------------------内部方法-------------------------------------------------------------------//

	/* 清理�?地临时文�? */
	this.ClearDirectory = function () {
		var fs = this.obj.FileSystem;
		/*老版�?插件临时�?�?*/
		fs.ClearDirectory(this.DownFilePath());
		fs.ClearDirectory(this.getFilePath());  //此接口无返回�?
	}

	/* 删除�?地文�? */
	this.WebDelLocalFile = function (FileName) {
		var fs = this.obj.FileSystem;
		fs.DeleteFile(FileName);
	}

	/* 获取json格式数据�? */
	this.GetMessageString = function () {
		return this.ht.toString();
	}

	/* 设置控件Ribbon */
	this.WebSetRibbonUIXML = function (strCustomUI) {
		this.obj.RibbonCustomUI = strCustomUI;
	}

	/* 设置控件保存文档的限�? */
	this.WebSetMaxFileSize = function (mFileName) {
		if (this.WebFileSize(mFileName) > this.MaxFileSize * 1024) {
			return false;
		} else {
			return true;
		}
	}

	/* 设置临时�?录路�?(DOWN) */
	this.DownFilePath = function () {
		var fs = this.obj.FileSystem;					// 获取file对象
		var NewDownFilePath;
		if (this.HiddenDirectory)						//将文档保存在隐藏�?�?
		{
			NewDownFilePath = fs.GetSpecialFolderLocation(0x20) + this.DOWN
				+ fs.GetFolderEncryptCode(1) + "\\"; 	// 设置临时�?�?
		}
		else {
			NewDownFilePath = fs.GetSpecialFolderPath(0x1a) + this.DOWN; //设置临时�?�?
		}
		return NewDownFilePath;
	}

	/* 设置临时�?录路�?(UP) */
	this.getFilePath = function () {
		var fs = this.obj.FileSystem; 					// 获取file对象
		var filePath;
		if (this.HiddenDirectory)						//将文档保存在隐藏�?�?
		{
			filePath = fs.GetSpecialFolderLocation(0x20) + this.UP
				+ fs.GetFolderEncryptCode(1) + "\\"; 	// 设置临时�?�?
		}
		else {
			filePath = fs.GetSpecialFolderLocation(0x1a) + this.UP;
		}

		fs.CreateDirectory(filePath); 					// 创建�?�?
		this.FilePath = filePath; 						// 给�?�象赋值，方便删除和打开
		return this.FilePath;
	}

	/* 判断当前文档�?否为空文�?	*/
	this.WebSetAllowEmpty = function () {
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			var WebText = this.WebObject.ActiveDocument.Content.Text; //获取word文档内�??
			if (WebText.length != 1) {
				return true;   	//文档有内�?
			}
			return false;   	//文档没�??
		}
		return true;  			//不是word
	}

	/* 控制�?否可以�?�制 */
	this.NewCopyType = function (mValue) {
		switch (mValue) {
			case 0:
			case false:
			case "0": this.obj.CopyEnabled = false; break; //�?�?
			case 1:
			case "1":
			case true: this.obj.CopyEnabled = true; break; //关闭
			default: ; return;
		}
	}

	/* 设置�?否显示整�?控件工具栏，包括OFFICE的工具栏 */
	this.NewShowToolBar = function (mValue) {
		var style = this.obj.Style;
		switch (mValue) {
			case false:
			case "0": style.ShowCustomToolbar = false; style.ShowToolBars = true; break; 		//�?定义工具栏隐藏，office工具栏显�?
			case true:
			case "1": style.ShowCustomToolbar = true; style.ShowToolBars = true; break; 		//�?定义工具栏显示，office工具栏显�?
			case "2": style.ShowCustomToolbar = false; style.ShowToolBars = false; break; 	//�?定义工具栏隐藏，office工具栏隐�?
			case "3": style.ShowCustomToolbar = true; style.ShowToolBars = false; break; 		//�?定义工具栏显示，office工具栏隐�?
		}
	}

	/* 控制OFFICE另存为和保存功能 */
	this.NewUIControl = function (mValue) {
		var strCustomUI;
		switch (mValue) {
			case false: strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
			            <commands> \
			      	    	<command idMso="FileSave" enabled="false" /> \
			      	  		<command idMso="FileSaveAs" enabled="false" /> \
			            </commands> \
		    		</customUI>'; break;
			case true: strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
				        <commands> \
				  	    	<command idMso="FileSave" enabled="true" /> \
				  	  		<command idMso="FileSaveAs" enabled="true" /> \
				        </commands> \
					</customUI>'; break;

		}
		this.obj.RibbonCustomUI = strCustomUI;
	}

	//禁用�?�?，更�?
	//ReviewShowMarkupMenu显示标�??
	//ReviewReviewingPaneMenu审阅窗格
	//ReviewTrackChangesMenu�?�?
	//ReviewAcceptChangeMenu接受
	//ReviewRejectChangeMenu拒绝
	//ReviewNewComment新建批注
	//ReviewDeleteCommentsMenu删除批注
	this.ModifyReview = function () {
		var strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
	        <commands> \
	    	    <command idMso="ReviewReviewingPaneMenu" enabled="false"/> \
	    		<command idMso="ReviewShowMarkupMenu" enabled="false"/> \
	        	<command idMso="ReviewTrackChangesMenu" enabled="false"/> \
	        	<command idMso="ReviewRejectChangeMenu" enabled="false"/> \
				<command idMso="ReviewAcceptChangeMenu" enabled="false"/> \
	        	<command idMso="ReviewNewComment" enabled="false"/> \
	    		<command idMso="ReviewDeleteCommentsMenu" enabled="false"/> \
	        </commands> \
	      </customUI>';
		for (i = 1; i <= this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Count; i++) {
			if (this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).ID == 1715)
				this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).Enabled = false;
			if (this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).ID == 1716)
				this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).Enabled = false;
			if (this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).ID == 2041)
				this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).Enabled = false;
		}
		this.obj.RibbonCustomUI = strCustomUI;
	}

	/* 禁用offcie小图�? */
	this.OfficeFastUI = function () {
		var strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
	        <commands> \
 				<command idMso="PrintPreviewAndPrint" enabled="false" /> \
	        	<command idMso="TabReviewWord" enabled="false" /> \
	  	    	<command idMso="FileSave" enabled="false" /> \
	  	  		<command idMso="FileSaveAs" enabled="false" /> \
	  	  		<command idMso="FileNewDefault" enabled="false" /> \
	  	  		<command idMso="FileOpen" enabled="false" /> \
	  	  		<command idMso="FilePrintQuick" enabled="false" /> \
	  	  		<command idMso="RedoOrRepeat" enabled="false" /> \
	  	  		<command idMso="Undo" enabled="false" /> \
	        </commands> \
		</customUI>';
		this.obj.RibbonCustomUI = strCustomUI;
	}
	//  startFromScratch不显示所有选项卡控�? false显示选项卡；true不显示选项�?
	//TabReviewWord关闭视图工具�?//TabInsert关闭插入工具�?//TabHome关闭开始工具栏
	this.WebSetRibbonUIXML = function () {
		var strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
    		<ribbon startFromScratch="false"> \
    			<tabs> \
		    		<tab idMso="TabReviewWord" visible="false" /> \
    				<tab idMso="TabInsert" visible="false" /> \
    				<tab idMso="TabHome" visible="false" /> \
    			</tabs> \
    		</ribbon> \
    		</customUI>';
		this.obj.RibbonCustomUI = strCustomUI;
		/*
			最常用的内�?选项卡名�?
			选项卡名�?      idMso（Excel�?      idMso（Word�?       idMso（Access�?
			开�?            TabHome             TabHome             TabHomeAccess
			插入            TabInsert           TabInsert           （none�?
			页面布局        TabPageLayoutExcel  TabPageLayoutWord   （none�?
			�?�?            TabFormulas         （none�?            （none�?
			数据            TabData             （none�?            （none�?
			审阅            TabReview           TabReviewWord       （none�?
			创建            （none�?            （none�?            TabCreate
			外部数据        （none�?            （none�?            TabExternalData
			数据库工�?      （none�?            （none�?            TabDatabaseTools
			iWebOffice控件的RibbonUIXML属性，�?基于OFFICE2007的RibbonX的应用。关于RibbonX的相关资料，需要自己另行查�?�?
	*/
	}

	/* 枚举显示系统和控件定义的相关对话框内�? */
	this.ShowDialog =
	{
		DialogNew: 0, 	// 新建对象
		DialogOpen: 1, 	// 打开
		DialogSaveAs: 2, 	// 另存�?
		DialogSaveCopyAs: 3, 	// 另存为拷�?
		DialogPrint: 4, 	// 打印
		DialogPageSetup: 5, 	// 打印设置
		DialogProperties: 6		// 文档属�?
	}

	/* 设置2015对象 */
	this.setObj = function (object) {

		this.obj = object;
		console.info(this.obj);
		this.WebObject = this.obj; // 设置VBA调用对象
	}

	/* 枚举所有文档类型这里只列举word 0,execl 1 */
	this.DocType =
	{
		WORD: 0,
		EXECL: 1,
		PPT: 2,
		PDF: 3,
		OFD: 4
	};

	/* Http对象post方式 */
	this.HttpMethod =
	{
		Get: 0, 		// Http对象get方式
		Post: 1
	};

	/* 另存为pdf文件 */
	this.SaveAsPdf = function (FilePath) {
		this.ConsoleOut("<WebSavePDF> 开始把PDF保存到本�?...");
		var OfficeVersion = this.obj.ActiveDocument.Application.Version;
		if (OfficeVersion != "11.0") {
			if ((this.FileType == ".doc") || (this.FileType == ".docx")
				|| (this.FileType == ".wps")) {
				try {
					this.obj.ActiveDocument.ExportAsFixedFormat(FilePath, 17, true);
					/*this.obj.ActiveDocument.ExportAsFixedFormat(FilePath, 17,
							false, 0, 0, 1, 1, 0, true, true, 0, true, true, true);*/
					this.ConsoleOut("<WebSavePDF> 保存PDF到本地成�?...");
					this.Status = "保存PDF到本地成�?";
					return true;
				} catch (e) {
					this.ConsoleOut("<WebSavePDF> 保存PDF异常..." + e.description);
					this.Status = e.description;
					alert("保存PDF异常,�?能Office没有保存PDF功能，建�?安�?�SaveAsPDFandXPS插件");
					return false;
				}
			} else if ((this.FileType == ".xls") || (this.FileType == ".xlsx") || (this.FileType == ".et")) {
				try {
					this.obj.ActiveDocument.Application.ActiveSheet.ExportAsFixedFormat(0, FilePath, 0);
					this.ConsoleOut("<WebSavePDF> 保存PDF到本地成�?...");
					this.Status = "保存PDF到本地成�?";
					return true;
				} catch (e) {
					this.ConsoleOut("<WebSavePDF> 保存异常..." + e.description);
					this.Status = e.description;
					alert("保存PDF异常,�?能Office没有保存PDF功能，建�?安�?�SaveAsPDFandXPS插件");
					return false;
				}
			} else {
				this.ConsoleOut("<WebSavePDF> 保存PDF异常,文档格式不�??");
			}
		} else {
			this.ConsoleOut("<WebSavePDF> 保存PDF异常,Office没有保存PDF功能");
			this.Status = "保存PDF异常,Office没有保存PDF功能";
			alert("保存PDF异常,Office没有保存PDF功能");
		}
	}

	/* 保存为html */
	this.SaveAsHtml = function (FilePath) {
		if ((this.FileType == ".doc") || (this.FileType == ".docx")
			|| (this.FileType == ".wps")) {
			try {
				var ret = this.obj.ActiveDocument.SaveAs(FilePath, 8, false,
					"", false, "", false, false, false, false, false, 0);
				this.obj.ActiveDocument.Application.ActiveWindow.View.type = 3;//3�?页面视图
				return true;
			} catch (e) {
				this.Status = e.description;
				return false;
			}
		}
	}

	/* 用来存储Http发送的表单数据 */
	this.ArrayList = function () {
		this.ObjArr = {}; // 列表
		this.Count = 0; // 数量
		this.Add = function (key, value) // 添加
		{
			this.ObjArr[key] = value;
			this.Count++;
			return true;
		}

		this.Get = function (key) {
			return this.ObjArr[key];
		}

		this.Clear = function () // 清空
		{
			this.ObjArr = {};
			this.Count = 0;
		}

		// 按json格式输出
		this.toString = function () {
			var newArray = new Array(); // 存储json字�?�串
			var i = 0;
			for (var i in this.ObjArr) {
				newArray.push("'" + i + "':'" + this.ObjArr[i] + "'");
			}
			return "{" + newArray + "}";
		}
	}
	this.ht = new this.ArrayList();

	/* 获取打开窗口的后缀 */
	this.getOpenDocSuffix = function (fileType) {
		if (fileType.length == 5) {
			fileType = fileType.substring(0, 4);
		}
		var exts;
		exts = "";
		if (this.isWPS() || !this.getOfficeVersion()) // 如果是office2003�?不支持x格式的文�?
		{
			exts += "*" + fileType + "x|*" + fileType + "x|";
		}
		exts += "*" + fileType + "|*" + fileType + "|";
		exts += this.getOpenSuffixName(fileType) + "(*" + fileType;
		if (this.isWPS() || !this.getOfficeVersion()) // 如果是office2003�?不支持x格式的文�?
		{
			exts += ";*" + fileType + "x";
		}
		exts += ")|*" + fileType;
		if (this.isWPS() || !this.getOfficeVersion()) // 如果是office2003�?不支持x格式的文�?
		{
			exts += ";*" + fileType + "x";
		}
		exts += "|";
		return exts;
	}

	/* 对比文档里面所用到�? 下载文档方法 */
	this.WebDownloadFile_int = function (fn) {
		this.Status = '';
		var httpclient = this.obj.Http; 					// 设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("USERNAME", this.UserName); 	// 加载UserName
		this.WebSetMsgByName("FILENAME", fn); 				// 加载FileName
		this.WebSetMsgByName("FILETYPE", this.FileType); 	// 加载FileType
		this.WebSetMsgByName("RECORDID", this.RecordID); 	// 加载RecordID
		this.WebSetMsgByName("OPTION", "LOADFILE"); 		// 发送�?�求LOADFILE
		httpclient.AddForm("FormData", this.GetMessageString()); // 这里�?�?定义json传输格式�?
		this.WebClearMessage(); 							// 清除所有WebSetMsgByName参数
		httpclient.ShowProgressUI = this.ShowWindow;		// 显示进度�?
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) // 这里采用同�?�方式打开文档�?
		{
			if (httpclient.Send()) {
				if (httpclient.GetResponseHeader("MsgError") == "404") // 判断服务器是否存在文�?
				{
					this.Status = '文档�?' + fn + '】下载失败，请确认�?�文档在服务器上�?否存�?';
					httpclient.Clear();
					return false;
				}
				httpclient.Clear();
				if (this.hiddenSaveLocal(httpclient, this, false, false, fn)) // 下载成功�?
				{
					this.Status = '文档下载成功';
					return true;
				} else {
					this.Status = '文档下载失败';
					return false;
				}
			}
		}
	}

	/* 保存文件至服�? */
	this.SAVEFILE = function (httpclient, FileName) // 与后台发生交互OPTION值为：SAVEFILE
	{
		httpclient.AddForm("FormData", this.GetMessageString());
		httpclient.AddFile("FileData", FileName); // 需要上传的文件
		this.WebClearMessage();
		httpclient.ShowProgressUI = this.ShowWindow; // 隐藏进度�?
		this.ConsoleOut("<SAVEFILE> 开始打开链接...");
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) //true 异�?�方�? false同�??
		{
			this.ConsoleOut("<SAVEFILE> 开始向后台发送数�?�?...");
			if (!httpclient.Send()) {
				this.ConsoleOut("数据包发送失败！请�?�查链�?<" + this.WebUrl
					+ ">�?否�?�确或网络是否畅通�?");
				//this.Status = "数据包发送失败！请�?�查链�?<" + this.WebUrl + ">�?否�?�确或网络是否畅通�?";
				this.Status = httpclient.GetResponseHeader("STATUS");
				return false;
			} else {
				this.ConsoleOut("<SAVEFILE> 数据包发送成功，文档已成功保存到后台...");
				this.Status = httpclient.GetResponseHeader("STATUS");
				alert(this.Status + "t");
				return true;
			}
		} else {
			this.ConsoleOut("打开链接<" + this.WebUrl + ">失败！�?��?�查网络是否畅通�?");
			//this.Status = "打开链接<" + this.WebUrl + ">失败！�?��?�查网络是否畅通�?";
			this.Status = httpclient.GetResponseHeader("STATUS");
			alert(this.Status + "f");
			return false;
		}
	}

	/* 加载服务上的文档 */
	this.LOADFILE = function (httpclient) {
		this.Status = "";
		httpclient.ShowProgressUI = this.ShowWindow;
		this.WebUrl = this.ServerUrl + this.SaveServlet;
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) // true 异�?�方�? false同�??
		{
			if (httpclient.Send()) {
				console.info(httpclient.GetResponse())
				// console.info(httpclient.GetResponseHeader("MsgError"));
				// console.info(httpclient.GetResponse());
				// aaa = httpclient.GetResponseHeader("MsgError");
				// if (aaa == "404") {
				// 	this.CreateFile();
				// 	this.getOfficeVersion();// 打开文档后，判断当前office版本
				// 	httpclient.Clear();
				// 	return true;
				// }
				// else if (aaa == "405") {
				// 	this.CreateFile();
				// 	this.getOfficeVersion();// 打开文档后，判断当前office版本
				// 	httpclient.Clear();
				// 	return true;
				// }
				if (this.hiddenSaveLocal(httpclient, this, false, false)) {
					var mSaveResult = this.WebOpenLocalFile(this.DownloadedFileTempPathName);
					if (mSaveResult == 0) { // 打开�?地�?�盘文件
						this.getOfficeVersion();// 打开文档后，判断当前office版本
						return true;
					} else if (mSaveResult == 1) {
						var windows = window.confirm("�?能当前授权码错�??，�?�确�?iWebOffice2015.js的授权是否�?�确（或乱码�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "�?能当前授权码错�??，�?�确�?iWebOffice2015.js的授权是否�?�确（或乱码�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 2) {
						var windows = window.confirm("没有找到文档，�?�确�?WebOpenLocalFile打开文档的路径是否�?�确"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "有找到文档，请确�?WebOpenLocalFile打开文档的路径是否�?�确";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 3) {
						var windows = window.confirm("没有权限导致文档打开失败，�?�用管理员身份运行浏览器后重�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "没有权限导致文档打开失败，�?�用管理员身份运行浏览器后重�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 4) {
						var windows = window.confirm("文件�?能损坏，请确定服务器文档�?否已经损�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "文件�?能损坏，请确定服务器文档�?否已经损�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 5) {
						var windows = window.confirm("�?安�?�Office或者注册表有损�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "�?安�?�Office或者注册表有损�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else if (mSaveResult == 6) {
						var windows = window.confirm("文件�?占用，�?�结束Office进程后重�?"
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "文件�?占用，�?�结束Office进程后重�?";
						if (windows == 1) {
							window.close();
							return false;
						}
					} else {
						this.ConsoleOut("<LOADFILE> 打开文档时未知错�?！错�?码为�? "
							+ mSaveResult);
						var windows = window.confirm("打开文档时未知错�?！错�?码为�? "
							+ mSaveResult
							+ "\r\r单击“确定”关�?。单击“取消”继�?�?");
						this.Status = "打开文档时未知错�?！错�?码为�? "
							+ mSaveResult;
						if (windows == 1) {
							window.close();
							return false;
						}
					}
				} else {
					// 失败后，this.Status的值由hiddenSaveLocal返回
					this.Status = "保存文档到本�? 失败";
					return false;
				}
			} else {
				this.Status = "数据包发送失败！请�?�查链�?<" + this.WebUrl + ">�?否�?�确或网络是否畅通�?";
				return false;
			}
		} else {
			this.Status = "打开链接<" + this.WebUrl + ">失败！�?��?�查网络是否畅通�?";
			return false;
		}
	}

	/* 保存到本�? isHidden �?否隐藏文件；isInsertFile�?否是插入文件 */
	this.hiddenSaveLocal = function (httpclient, webOffice, isHidden,
		isInsertFile, OtherName) {
		try {
			this.Status = "";
			if (isHidden) {
				httpclient.Hidden = true; 		// 隐藏文件
			}
			var tempName = "";
			var fs = webOffice.obj.FileSystem; 	// WebOffice外面对象名称�?
			var filePath = this.DownFilePath(); // 获取临时文件保存�?�?
			fs.CreateDirectory(filePath);		// 创建生成指定�?�?

			this.FilePath = filePath; 			// 这个保存的路径方便打开的时候再取�?
			if (isInsertFile == undefined || isInsertFile == true) {
				tempName = "temp" + webOffice.RecordID;
				this.tempInsertName = tempName + this.FileName;
			}
			if (OtherName == undefined || OtherName == "") {
				OtherName = this.FileName;
				OtherName = Math.random() * 100000 + this.FileType;
			}
			this.DownloadedFileTempPathName = filePath + tempName + OtherName;
			httpclient.ResponseSaveToFile(this.DownloadedFileTempPathName);
			httpclient.Clear();
			return true;
		} catch (e) {
			return false;
		}
	}

	/* office2003的接口和其他的不一样，所有保存到�?地�?�区分开�? */
	this.Save = function (FileName, is2003, FileType) {
		if (this.getDocType(this.FileType) == this.DocType.PICTURE) {
			if (this.obj.ActiveDocument.WebSaveLocalFile(FileName)) {
				return 0;
			} else {
				return -1;
			}
		}
		if (is2003) {
			return this.obj.Save(FileName);
		}
		var SaveFalg = this.obj.Save(FileName, eval("this.DocTypeValue." + FileType),
			true);
		if (SaveFalg == 80) {
			return "文件�?径无�?";
		} else if (SaveFalg == 81) {
			return "参数无效";
		} else if (SaveFalg == 82) {
			return "文件创建失败";
		} else {
			return SaveFalg;
		}
	}

	/* 判断浏�?�器类型 */
	this.blnIE = function () {
		return (window.ActiveXObject != undefined)
			|| (window.ActiveXObject != null)
			|| ("ActiveXObject" in window)
	}

	/* 判断�?否是WPS */
	this.isWPS = function () {
		return this.FileType.toUpperCase() == ".WPS"
			|| this.FileType.toUpperCase() == ".ET";
	}

	/* 获取office版本信息 */
	this.getOfficeVersion = function () {
		var getVersion = 0.0;
		try {
			if (this.setVersion == -1) {
				getVersion = parseFloat(this.obj.ActiveDocument.Application.Version);
				this.setVersion = getVersion;
			} else {
				getVersion = this.setVersion;
			}
			if (getVersion == 11.0) {
				return this.OfficeVersion.v2003;
			} else {
				return this.OfficeVersion.vOther;
			}
		} catch (e) {
			return this.OfficeVersion.v2003;
		}
	}

	/* 获取文档类型 */
	this.getDocType = function (fileType) {
		if (fileType == ".doc" || fileType == ".docx" || fileType == ".wps") {
			return this.DocType.WORD;
		}
		if (fileType == ".xls" || fileType == ".xlsx" || fileType == ".et") {
			return this.DocType.EXECL;
		}

		if (fileType == ".tif" || fileType == ".jpg") {
			return this.DocType.PICTURE;
		}
		if (fileType == ".ppt" || fileType == ".pptx") {
			return this.DocType.PPT;
		}
		if (fileType == ".pdf") {
			return this.DocType.PDF;
		}
		if (fileType == ".ofd") {
			return this.DocType.OFD;
		}
	}

	// 获取打开文档类型名称
	this.getOpenSuffixName = function (fileType) {
		var openSuffixName;
		switch (fileType) {
			case this.DocType.WORD:
				openSuffixName = "Word Files";
				break; // 创建word后缀名称
			case this.DocType.EXECL:
				openSuffixName = "Excel Files";
				break; // 创建execl后缀名称
			default:
				openSuffixName = "Word Files";
				break;
		}
		return openSuffixName;
	}

	/* 设置打开窗口的类�? */
	this.setShowDialog = function (thisType, exts) {
		switch (thisType) {
			case this.ShowDialog.DialogOpen:
				this.obj.ShowDialog(thisType, exts, 0); break; 	// 打开�?地文�?
			case this.ShowDialog.DialogNew: 					// 新建对象
			case this.ShowDialog.DialogPageSetup: 				// 打印设置
			case this.ShowDialog.DialogPrint: 					// 打印
			case this.ShowDialog.DialogProperties: 				// 文档属�?
			case this.ShowDialog.DialogSaveCopyAs: 				// 另存为拷�?
			case this.ShowDialog.DialogSaveAs: 					// 另存�?
				this.obj.ShowDialog(thisType); break;			// 打开窗口
			default:
				break;
		}
	}

	/* 设置文档的保护状�? */
	this.setEditType = function (type) {
		try {
			switch (type) {
				case 0:
				case "0":
					this.VBAProtectDocument(this.getDocType(this.FileType), "123");
					break;
				case 1:
				case "1":
					this.WebShow(false);
					break;
				case 2:
				case "2":
					this.WebShow(true);
					break;
				default:
					;
			}
		} catch (e) {
			return false;
		}
	}

	/* 返回当前编辑�? */
	this.getEditVersion = function () {
		return this.obj.AppName;
	}

	/* 设置手写签批用户 */
	this.SetUser = function (username) {
		this.obj.User = username;
	}

	/* 检测异常文件的接口 */
	this.CheckFile = function (vaul) {
		this.obj.bCheckFile = vaul;
	}

	/* 禁用wps混合签章按钮 */
	this.EnableSignature = function (vaul) {
		try {
			this.obj.ActiveDocument.Application.COMAddIns
				.Item("iSignatureWord.WordApp").Connect = vaul;
		} catch (e) {
			return;
		}
	}

	/* --------------------------------------------水印功能 --------------------------------------------*/
	/* 添加水印 */
	this.AddWaterMark = function (WaterMarkNmae) {
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			if (this.blnIE()) {
				var intPageTotal = this.WebObject.ActiveDocument.Application.ActiveDocument.BuiltInDocumentProperties(14);
			} else {
				var intPageTotal = this.WebObject.ActiveDocument.Application.ActiveDocument.BuiltInDocumentProperties.Item(14).Value();
			}
			intPage = parseInt(intPageTotal);
			var selection = this.WebObject.ActiveDocument.Application.Selection;
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				this.DelWaterMark(WaterMarkNmae);
				for (var i = 1; i <= intPage; i++) {
					selection.GoTo(What = 1, Which = 1, Count = i);
					try {
						//插入水印前需更改视图样式为页眉�?�图
						this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 9; //wdSeekCurrentPageHeader
						this.WebObject.ActiveDocument.Application.Selection.ClearFormatting(); //去页面横�?
						//由于一�?文档�?�?允�?�添加一�?水印，因此在添加水印之前，需检测文档中�?否存在水印，如果存在，则先删�?
						//设置插入水印，�??法：表达�?.AddTextEffect(预�?�文字效果�?0..49�?, 文字内�??, 字体�?, 字体大小, �?否粗�?, �?否斜�?, 左侧位置, 顶部位置)
						selection.HeaderFooter.Shapes.AddTextEffect(0, '金格科技', '宋体', 36, false, false, 0, 0).Select();
						var shapeRange = selection.ShapeRange;
						shapeRange.Name = WaterMarkNmae + i;  				//水印对象�?
						shapeRange.TextEffect.NormalizedHeight = false 		//文字效果
						shapeRange.Line.Visible = false;					//线条�?否可�?
						shapeRange.Fill.Visible = false;					//�?充是否可�?
						shapeRange.Fill.Solid();							//�?充类型（�?例为�?色）
						shapeRange.Fill.ForeColor.RGB = 0x0000FF; 			//设定�?充的颜色RGB�?
						shapeRange.Fill.Transparency = 0.5;					//设置透明�?50%
						shapeRange.Rotation = -45;							//设置旋转角度
						shapeRange.LockAspectRatio = true;					//锁定纵横�?
						//shapeRange.Height = WebOffice.WebObject.ActiveDocument.Application.CentimetersToPoints(2); //高度
						//shapeRange.Width = WebOffice.WebObject.ActiveDocument.Application.CentimetersToPoints(16); //宽度
						shapeRange.Height = 2 * 72;
						shapeRange.Width = 8 * 72;
						shapeRange.WrapFormat.AllowOverlap = true;			//�?否允许重�?
						shapeRange.WrapFormat.Side = 3; 					//�?否�?�置文字�?�?(wdWrapNone)
						shapeRange.WrapFormat.type = 3;						//设置折回样式（本例�?�为不折回）
						//设置水平位置与纵向页边距关联 wdRelativeVerticalPositionMargin
						shapeRange.RelativeHorizontalPosition = 0;
						//设置垂直位置与横向页边距关联 wdRelativeVerticalPositionMargin
						shapeRange.RelativeVerticalPosition = 0;
						//水平居中  wdShapeCenter
						shapeRange.Left = -999995;
						//垂直居中
						shapeRange.Top = -999995;
						//恢�?��?�图样式到原来样�?
						this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 0; //wdSeekMainDocument
						this.Status = "插入水印成功";
					} catch (e) {
						this.Status = "插入水印失败" + e.description;
						return false;
					}
				}
				return true;
			} else {
				this.Status = "文档�?锁定，插入水印失�?";
				return false;
			}
		}
		this.Status = "非Word文档，插入水印失�?";
		return false;
	}

	/* 删除水印 */
	this.DelWaterMark = function (WaterMarkNmae) {
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 9;
				var selection = this.WebObject.ActiveDocument.Application.Selection;
				//查找文档�?�?否存在名称为【WaterMarkObjectName】的水印对象，�?�果存在，则删除
				var WaterCount = selection.HeaderFooter.Shapes.Count;
				if (WaterCount > 0) {
					for (var i = WaterCount; i >= 1; i--) {
						if (selection.HeaderFooter.Shapes.Item(i).Name = WaterMarkNmae + i) {
							selection.HeaderFooter.Shapes.Item(i).Delete();
						}
					}
				}
				this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 0;
				this.Status = "删除水印成功";
				return true;
			}
			this.Status = "文档�?锁定，删除水印失�?";
			return false;
		}
		this.Status = "非Word文档，删除水印失�?";
		return false;
	}

	/* 插入图片水印 */
	this.AddGraphicWaterMark = function (WaterMarkNmae) {
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			if (this.blnIE()) {
				var intPageTotal = this.WebObject.ActiveDocument.Application.ActiveDocument.BuiltInDocumentProperties(14);
			} else {
				var intPageTotal = this.WebObject.ActiveDocument.Application.ActiveDocument.BuiltInDocumentProperties.Item(14).Value();
			}
			intPage = parseInt(intPageTotal);
			var selection = this.WebObject.ActiveDocument.Application.Selection;
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				this.DelWaterMark(WaterMarkNmae);
				for (var i = 1; i <= intPage; i++) {
					selection.GoTo(What = 1, Which = 1, Count = i);
					try {
						this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 9; //wdSeekCurrentPageHeader
						this.WebObject.ActiveDocument.Application.Selection.ClearFormatting(); //去页面横�?
						/////获取服务器上的图�?///////
						var ImagePath = this.DownFilePath();  //项目�?径下Document�?"WaterMark.jpg"
						var ImageName = "WaterMark.jpg";
						this.DownloadToFile(ImageName, ImagePath);
						/////获取服务器上的图�?///////
						selection.HeaderFooter.Shapes.AddPicture(ImagePath + ImageName, false, true).Select(); //水印位置

						var shapeRange = selection.ShapeRange;

						shapeRange.Name = WaterMarkNmae + i;  		//水印对象�?
						shapeRange.PictureFormat.Brightness = 0.85;
						shapeRange.PictureFormat.Contrast = 0.15;
						shapeRange.LockAspectRatio = true;

						shapeRange.Height = 60.95 * 72; 			//高度
						shapeRange.Width = 40.63 * 72; 				//宽度

						shapeRange.WrapFormat.AllowOverlap = true;
						shapeRange.WrapFormat.Side = 3;				//wdWrapNone
						shapeRange.WrapFormat.Type = 3;
						shapeRange.RelativeHorizontalPosition = 0;	//wdRelativeVerticalPositionMargin
						shapeRange.RelativeVerticalPosition = 0;	//wdRelativeVerticalPositionMargin
						shapeRange.Left = -999995;					//wdShapeCenter
						shapeRange.Top = -999995;					//wdShapeCenter
						this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 0; //wdSeekMainDocument
						this.Status = "水印添加成功";
					} catch (e) {
						this.Status = "水印添加成功" + e.description;
						return false;
					}
				}
				return true;
			} else {
				this.Status = "文档�?锁定，插入水印失�?";
				return false;
			}
		}
		this.Status = "非Word文档，插入水印失�?";
		return false;
	}

	//设置段落
	this.ParagraphSettings = function () {
		var paragraphFormat = this.WebObject.ActiveDocument.Application.Selection.ParagraphFormat;
		paragraphFormat.Alignment = 3; //常�?�：对齐方式,0：左对齐�?1：居�?�?2：右对齐，以此类�?
		paragraphFormat.OutlineLevel = 10; //常�?�：大纲级别,值为1-10�?10为�?�文文本�?

		//paragraphFormat.LeftIndent =  28.3 / 2; 	//缩进：左侧，28.3等于1cm需要�?�少根据比例�?/�?
		//paragraphFormat.RightIndent =  28.3 * 2;	//缩进：右侧，28.3等于1cm需要�?�少根据比例�?/�?
		//paragraphFormat.FirstLineIndent = 28.3 * 2;	//缩进：缩进值，28.3等于1cm需要�?�少根据比例�?/�?
		paragraphFormat.CharacterUnitLeftIndent = 0;//缩进：左侧，按照字�?�缩�?
		paragraphFormat.CharacterUnitRightIndent = 0;//缩进：右侧，按照字�?�缩�?
		paragraphFormat.CharacterUnitFirstLineIndent = 0;//缩进：缩进值，按照字�?�缩�?
		paragraphFormat.MirrorIndents = false; //缩进：�?�称缩进设置false不勾选true勾�?
		paragraphFormat.AutoAdjustRightIndent = true; //缩进:如果定义了文档网格，则自动调整右缩进 false不勾选true勾�?

		//paragraphFormat.SpaceBefore = 2.5;  //间距段前 ：按照�?��?�置
		//paragraphFormat.SpaceBeforeAuto = false; //设置间距段前非自�?，改ture的话间距段前设置无效默�?�为�?�?
		//paragraphFormat.SpaceAfter = 2.5;//间距:段后 ：按照�?��?�置
		//paragraphFormat.SpaceAfterAuto = false; //间距:�?后非�?�?，改ture的话间距段后设置无效默�?�为�?�?
		paragraphFormat.LineUnitBefore = 0;//间距:段前：按照�?��?�置
		paragraphFormat.LineUnitAfter = 0;//间距:段后：按照�?��?�置
		paragraphFormat.LineSpacingRule = 1; // 设置行距�?0：单行，1�?1.5倍，2�?2倍，3：最小，以�?�类�?
		paragraphFormat.DisableLineHeightGrid = false; //间距:如果定义了文档网格，则�?�齐到网�? false勾选true不勾�?

		paragraphFormat.WidowControl = false; //分页：弧形控�? false不勾选true勾�?
		paragraphFormat.KeepWithNext = false; //分页：与下�?�同�? false不勾选true勾�?
		paragraphFormat.KeepTogether = false; //分页：�?�中不分�? false不勾选true勾�?
		paragraphFormat.PageBreakBefore = false; //分页：�?�前分页 false不勾选true勾�?

		paragraphFormat.NoLineNumber = false; //格式化例外向：取消�?�号 false不勾选true勾�?
		paragraphFormat.Hyphenation = true; //格式化例外向：取消断�? false勾选true不勾�?

		paragraphFormat.FarEastLineBreakControl = true;//换�?�：按中文习�?控制尾�?�字�? false不勾选true勾�?
		paragraphFormat.WordWrap = true;//换�?�：允�?�西文在单词�?间换�? false勾选true不勾�?
		paragraphFormat.HangingPunctuation = true; //换�?�：允�?�标点溢出边�? true勾选false不勾�?

		paragraphFormat.HalfWidthPunctuationOnTopOfLine = false; //字�?�间距：允�?��?��?�标点压缩true勾选false不勾�?
		paragraphFormat.AddSpaceBetweenFarEastAndAlpha = true;//字�?�间距：�?动调整中文与西文的间�? true勾选false不勾�?
		paragraphFormat.AddSpaceBetweenFarEastAndDigit = true;//字�?�间距：�?动调整中文与数字的间�? true勾选false不勾�?
		paragraphFormat.BaseLineAlignment = 4; //字�?�间距：文本对齐方式�? 0：顶�? �?1：居�?�?2：基线，以�?�类�?
	}

	this.CleanBackground = function () {
		this.WebObject.ActiveDocument.Application.Selection.WholeStory(); //选中全文
		//清理文字
		var sfShading = this.WebObject.ActiveDocument.Application.Selection.Font.Shading;
		sfShading.Texture = 0;
		sfShading.ForegroundPatternColor = -16777216;
		sfShading.BackgroundPatternColor = -16777216;
		//清理段落
		var pfShading = this.WebObject.ActiveDocument.Application.Selection.ParagraphFormat.Shading;
		pfShading.Texture = 0;
		pfShading.ForegroundPatternColor = -16777216;
		pfShading.BackgroundPatternColor = -16777216;
	}

	/* ----------------------------------------------水印功能end----------------------------------------*/

	/* 关闭文档 */
	this.Close = function () {
		if (this.obj.Close() != 0) {
			return false;
		}
		return true;
	}

	// 控制台日志输�?
	this.ConsoleOut = function (value) {
		if (this.DebugMode) {
			console.log(value);
		}
	}

	// ******************************************************************************************************************************//
	// ******************************************************************************************************************************//
	// ******************************************************************************************************************************//

	/** ****以下�?打开�?地窗口代�?***** */
	/** ****End 打开�?地窗口代�?***** */

	/* 2015�?带有窗口的页面�?�置 */
	this.WebPageSetup = function () {
		this.setShowDialog(this.ShowDialog.DialogPageSetup);
	}

	/* 2015�?带有窗口的打印�?�置 */
	this.WebOpenPrint = function () {
		this.setShowDialog(this.ShowDialog.DialogPrint);
	}

	/* 打印预�?? */
	this.PrintPreview = function () {
		this.WebObject.ActiveDocument.PrintPreview();
		//this.obj.PrintPreview();
	}

	/* 退出打印�?��?? */
	this.PrintPreviewExit = function () {

		//this.obj.PrintPreviewExit();
		this.WebObject.ActiveDocument.ClosePrintPreview();
		this.obj.ActiveDocument.ActiveWindow.View.ShowDrawings = true;
		this.obj.ActiveDocument.ActiveWindow.View.ShowFieldCodes = false;
	}

	/* 增加�?定义工具栏按�? */
	this.AppendTools = function (Index, Caption, Icon) {
		parseInt(Index);
		var customtoolbar = this.obj.CustomToolbar;
		customtoolbar.AddToolButton(Index, Caption, Icon, Caption, 0); //Icon为图片路�?
	}

	/* 按钮�?否有�? */
	this.DisableTools = function (Caption, Flag) {
		var customtoolbar = this.obj.CustomToolbar;
		customtoolbar.DisableToolsButton(Caption, Flag);
		this.obj.Style.Invalidate();
	}

	/* �?定义工具栏按�?�?否显�? */
	this.VisibleTools = function (Caption, Flag) {
		var customtoolbar = this.obj.CustomToolbar;
		customtoolbar.VisibleToolsButton(Caption, Flag);
		this.obj.Style.Invalidate();
	}

	/* 增加菜单 */
	var MenuFile;
	this.AppendMenu = function (Index, Caption) {
		var custommenu = this.obj.CustomMenu;
		if (MenuFile == undefined || MenuFile == null) {
			custommenu.Clear();
			MenuFile = custommenu.CreatePopupMenu();
			custommenu.Add(MenuFile, "文件(&F)");
		}
		custommenu.AppendMenu(MenuFile, Index, false, Caption);
		custommenu.Update();
	}

	this.AddCustomMenu = function () {
		var custommenu = this.obj.CustomMenu;
		//创建文件菜单的条�?
		var menufile = custommenu.CreatePopupMenu();
		var menufilelv2 = custommenu.CreatePopupMenu();
		custommenu.AppendMenu(menufilelv2, 6, false, "�?定义二级菜单一");
		custommenu.AppendMenu(menufilelv2, 7, false, "�?定义二级菜单�?");
		custommenu.AppendMenu(menufilelv2, 0, false, "-");
		var menufilelv3 = custommenu.CreatePopupMenu();
		custommenu.AppendMenu(menufilelv3, 8, false, "�?定义三级菜单一");
		custommenu.AppendMenu(menufilelv3, 0, false, "-");
		custommenu.AppendMenu(menufilelv3, 9, false, "�?定义三级菜单�?");

		custommenu.AppendMenu(menufilelv2, menufilelv3, true, "�?定义三级菜单");

		custommenu.AppendMenu(menufilelv2, 10, false, "�?定义二级菜单�?");
		custommenu.AppendMenu(menufile, menufilelv2, true, "�?定义二级菜单");
		//将文件菜单添加到顶级主菜�?
		custommenu.AppendMenu(menufile, 17, false, "�?用保�?");
		custommenu.AppendMenu(menufile, 18, false, "禁�??保存");
		custommenu.AppendMenu(menufile, 0, false, "-");
		custommenu.AppendMenu(menufile, 19, false, "�?用打�?");
		custommenu.AppendMenu(menufile, 20, false, "禁�?�打�?");
		custommenu.Add(menufile, "编辑(&E)");

		//创建�?言
		/*  var menuLang = custommenu.CreatePopupMenu();
			custommenu.AppendMenu(menuLang, 22, false, "简体中�?");
			custommenu.AppendMenu(menuLang, 23, false, "繁体�?�?(TW)");
			custommenu.AppendMenu(menuLang, 24, false, "繁体�?�?(HK)")
			custommenu.AppendMenu(menuLang, 25, false, "英文");
			custommenu.Add(menuLang, "多�??言(&N)");*/

		//通知系统更新菜单
		custommenu.Update();
	}

	/* 获取当前文档打开类型，以后缀名来区别 */
	this.WebGetDocSuffix = function () {
		try {
			var docType = this.getDocType(this.FileType); 	// 判断�?文档还是表格
			var FileTypeValue = 0; 							// 判断打开文档的�? 0：doc�?12：docx�?51：xls�?56：xlsx
			if (docType == this.DocType.WORD) 				// word 获取vba值的方法
			{
				this.Activate(true);
				FileTypeValue = this.obj.ActiveDocument.SaveFormat;
			}
			if (docType == this.DocType.EXECL) { 			// Execl 获取方法
				this.obj.ExitExcelEditMode(); 				// 退出当前编辑模�?
				FileTypeValue = this.obj.ActiveDocument.FileFormat;
				if (FileTypeValue < 0) { 					// 2003不支持�?�属�?
					FileTypeValue = 56;
				}
			}
			return this.DocSuffixType[FileTypeValue];
		} catch (e) {
			return this.FileType;
		}
	}

	/* iWebOffice打开的文档全�?�? */
	this.WebFullName = function () {
		return this.obj.FullName;
	}

	/* 更加书�?�插入图�? */
	this.InsertImageByBookMark = function (Transparent, ZOrder) {
		if (this.obj.ActiveDocument.BookMarks.Exists(this.BookMark)) {
			this.obj.ActiveDocument.Bookmarks.Item(this.BookMark).Select();
		}
		var SelectionImage = this.obj.ActiveDocument.Application.Selection.InlineShapes.AddPicture(this.DownloadedFileTempPathName);
		SelectionImage.Select();
		var ShapeImage = SelectionImage.ConvertToShape(); 	// �?为浮动型
		ShapeImage.WrapFormat.Type = 3;
		ShapeImage.ZOrder(ZOrder); 							// 5：衬于文字下�? 4：浮于文字上�?
		return true;
	}

	/* 下载文档	*/
	this.DownloadToFile = function (DownFileName, SavePathName) {
		var httpclient = this.obj.Http;
		var URL = this.WebUrl.substring(0, this.WebUrl.lastIndexOf("/"));
		httpclient.ShowProgressUI = this.ShowWindow;// 隐藏进度�?
		if (httpclient.Open(this.HttpMethod.Get, URL + "/Document/"
			+ DownFileName, false)) {			// 指定下载模板的名�?
			if (httpclient.Send()) {
				if (httpclient.Status == 200) {
					httpclient.ResponseSaveToFile(SavePathName + DownFileName);
					httpclient.Clear();
					return true;
				}
			}
		}
		httpclient.Close();
		return false;
	}

	/* 手写签批	*/
	this.HandWriting = function (penColor, penWidth) {
		var handwritting = this.obj.Handwritting;
		var handsetting = handwritting.DrawingSetting;
		handsetting.PenThicker = penWidth;
		handsetting.PenColor = penColor;
		handwritting.AnnotateDraw();
		this.ShowMenuBar(false);	// 签批时隐藏菜单栏
		this.ShowToolBars(false);	// 签批时隐藏工具栏
	}

	/* 停�?�手写�?�批	*/
	this.StopHandWriting = function () {
		var handwritting = this.obj.Handwritting;
		handwritting.StopAnnotate();
		this.ShowMenuBar(true);		// 停�?��?�批时显示菜单栏
		this.ShowToolBars(true);	// 停�?��?�批时显示工具栏
	}

	/* 文字签名	*/
	this.TextWriting = function () {
		var handwritting = this.obj.Handwritting;
		var textsetting = handwritting.TextSetting;
		textsetting.TextSize = 32;
		textsetting.TextColor = 0xbb00ff;
		textsetting.FontName = "宋体";
		handwritting.AnnotateText();
		this.ShowMenuBar(false);	// 签批时隐藏菜单栏
		this.ShowToolBars(false);	// 签批时隐藏工具栏
	}

	/* 图形签批	*/
	this.ShapeWriting = function () {
		var handwritting = this.obj.Handwritting;
		var shapesetting = handwritting.ShapeSetting;
		shapesetting.ShapeType = 0;
		shapesetting.BackgroundColor = 0xffffff;
		shapesetting.BorderColor = 0xff0000;
		shapesetting.BorderWidth = 6;
		handwritting.AnnotateShape();
		this.ShowMenuBar(false);	// 签批时隐藏菜单栏
		this.ShowToolBars(false);	// 签批时隐藏工具栏
	}

	/* 取消上一次�?�批 */
	this.RemoveLastWriting = function () {
		var handwritting = this.obj.Handwritting;
		handwritting.RemoveLast();
	}

	/* 显示某用户的签批 */
	this.ShowWritingUser = function (bVal, username) {
		var strxml = this.obj.GetAnnotations();
		var json = eval('(' + strxml + ')');
		if (username != "" && username != null && username != undefined) {
			for (var i = 0; i < json.Annotations.length; i++) {
				if (json.Annotations[i].Annotation.User != username) {
					var id = json.Annotations[i].Annotation.ID;
					this.obj.GetAnnotationByID(id).Visible = bVal;
				}
			}
		} else {
			for (var i = 0; i < json.Annotations.length; i++) {
				var id = json.Annotations[i].Annotation.ID;
				this.obj.GetAnnotationByID(id).Visible = bVal;
			}
		}
	}

	/* 得到服务器setMsgByName的值并发送到前台 */
	this.GetDataToSend = function () {
		var httpclient = this.obj.Http; 						// 设置http对象
		httpclient.Clear();
		var ReturnValue = httpclient.GetResponseHeader("RName");// 获取返回�?
		var jsonObj = eval('(' + ReturnValue + ')');
		for (var i in jsonObj) {
			this.WebSetMsgByName(i, jsonObj[i]);
		}
	}

	//手写签批控件属性和方法
	this.IWR = null;
	/* 创建手写签批控件对象 */
	this.CreateRevision = function () {
		//判断iWebOffice2015对象�?否存�?
		if (this.obj == null) {
			this.Status = "iWebOffice2015对象�?初�?�化";
			return false;
		}
		//判断手写签批控件�?否已创建
		if (this.IWR != null) {
			this.Status = "手写组件对象已存�?";
			return false;
		}
		//创建手写签批控件
		var ret = this.obj.CreateNew("iWebRevision.iWebRevisionCtrl.1");
		if (ret != 0) {
			this.Status = "创建手写组件对象失败";
			return false;
		}
		//隐藏标�?�栏、状态栏、菜单栏、自定义工具�?
		this.ShowTitleBar(false);
		this.ShowMenuBar(false);
		this.ShowStatusBar(false);
		this.ShowCustomToolbar(false);
		this.obj.Style.BorderStyle = 0;
		//获取手写控件对象
		this.IWR = this.obj.Application;
		return true;
	}

	/* 解决打印预�?�里打印按钮没反应问�? */
	this.OleDialogPrint = function (OLEFlag, bCancel) {
		if (OLEFlag == 1) {
			this.obj.ShowDialog(0);	//新建文档
		}
		else if (OLEFlag == 2) {
			var exts;
			exts = "Word Files(*.doc;*.docx;*.docm;*.dot;*.dotx;*.dotm;*.rtf)|*.doc;*.docx;*.docm;*.dot;*.dotx;*.dotm;*.rtf";
			exts += "|Excel Files(*.xls;*.xlsx;*.xlsm;*.xlt;*.xltx;*.xltm)|*.xls;*.xlsx;*.xlsm;*.xlt;*.xltx;*.xltm";
			exts += "|PowerPoint Files(*.ppt;*.pptx;*.pptm;*.pot;*.potx;*.potm)|*.ppt;*.pptx;*.pptm;*.pot;*.potx;*.potm";
			exts += "|Project Files(*.mpp;*.mpt)|*.mpp;*.mpt";
			exts += "|Visio Files(*.vsd;*.vdx;*.vss;*.vsx;*.vst;*.vtx;*.vsw;*.vdw;*.vsdx;*.vssx;*.vstx;*.vsdm;*.vssm;*.vstm)|*.vsd;*.vdx;*.vss;*.vsx;*.vst;*.vtx;*.vsw;*.vdw;*.vsdx;*.vssx;*.vstx;*.vsdm;*.vssm;*.vstm";
			exts += "|All File(*.*)|*.*";
			exts += "||";
			this.obj.ShowDialog(1, exts, 0);//打开�?地文�?(弹窗)
		}
		else if (OLEFlag == 4) {
			this.obj.ShowDialog(2);	//另存为本地文�?
		}
		else if (OLEFlag == 8) {
			this.obj.ShowDialog(3);	//另存为拷�?
		}
		else if (OLEFlag == 16) {
			this.obj.ShowDialog(4);	//打印
		}
		else if (OLEFlag == 32) {
			this.obj.ShowDialog(5);	//打印设置
		}
		else if (OLEFlag == 64) {
			this.obj.ShowDialog(6);	//文档属�?
		}
		else if (OLEFlag == 128) {
			this.obj.ActiveDocument.PrintPreview();	//打印预�??
		}
	}
}


export default WebOffice2015
