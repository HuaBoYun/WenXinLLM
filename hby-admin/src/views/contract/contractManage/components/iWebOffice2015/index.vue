<template>
  <div>
    <div style="width: 100%">
      <!-- 以MultiPart方式下载文档,设置文件处理Servelet，打开和保存都用这个Servelet处理 -->
      <input
        style="color: Red"
        type="button"
        value="MultiPart方式打开文档"
        v-on:click="Load"
      />
      <!-- URL地址打开文档，WebOpen2的参数是文件在服务器上的相对路径 -->
      <input
        style="color: Red"
        type="button"
        value="URL地址打开文档"
        v-on:click="LoadURL"
      />
      <!-- 服务端Servlet打开文档，WebOpen3的参数是文件在服务器上的相对路径 -->
      <input
        style="color: Red"
        type="button"
        value="服务端Servlet打开文档"
        v-on:click="LoadServlet"
      />
      <input
        style="color: Red"
        type="button"
        value="保存文档到服务器"
        v-on:click="SaveDocument;"
      />
      <input
        style="color: Red"
        type="button"
        value="打开本地文档(有窗口)"
        v-on:click="WebOpenLocal"
      />
      <input
        style="color: Red"
        type="button"
        value="保存本地文档(有窗口)"
        v-on:click="WebSaveLocal"
      />
      <input
        style="color: Red"
        type="button"
        value="前后台交互信息"
        v-on:click="SendMessage"
      />
      <input
        style="color: Red"
        id="StatusBar"
        type="text"
        name="StatusBar"
        readonly
      />
      &nbsp;|←状态信息
    </div>
    <div id="OfficeDiv" style="width: 100%; height: 100%"></div>
  </div>
</template>

<script>
  import { baseURL } from '@/config/net.config'
  // 编辑器
  // import browser from '/static/iWebOffice2015.js'
  // import WebOffice2015 from '/static/WebOffice.js'
  // const WebOfficeObj = new WebOffice2015() //创建WebOffice对象
  // const ServerUrl = baseURL + '/fwb/fwbEdit' //后台的url走接口
  // const mSessionID = '8E0B3D768D2D0FC083A222446C439BE5'

  export default {
    name: 'iWebOffice',
    components: {},
    data() {
      return {}
    },
    computed: {},
    watch: {},
    created() {
      // this.fetchTypes()
      // 编辑器
      // var username=document.cookie
      var x = document.cookie

      debugger
      WebOfficeObj.setObj(document.getElementById('WebOffice')) //给2015对象赋值
      setTimeout(this.Load, 500) //避免页面加载完，控件还没有加载情况
    },
    methods: {
      Load() {
        try {
          this.OnUnLoad()

          WebOfficeObj.ServerUrl = ServerUrl //注意这是后台接口触发/officeJava注意跨越问题。
          WebOfficeObj.UserName = '演示人'
          WebOfficeObj.FileName = 'sample.doc'
          WebOfficeObj.FileType = '.doc' //FileType:文档类型  .doc  .xls
          WebOfficeObj.ShowWindow = true
          //true显示进度条//false隐藏进度条
          WebOfficeObj.obj.style.ShowOpenProgress = true //开启、关闭打开文档时的进度条
          WebOfficeObj.obj.WebCreateProcess()
          console.info(WebOfficeObj.obj) //创建空进程避免打开慢
          WebOfficeObj.EditType = '1' //设置加载文档类型 0 锁定文档，1无痕迹模式，2带痕迹模式
          WebOfficeObj.ShowMenu = 0
          WebOfficeObj.ShowToolBar = 0
          // WebOfficeObj.SetCaption(WebOfficeObj.UserName + "正在编辑文档");
          // this.SetGraySkin();			//设置控件皮肤

          if (browser == 'chrome') {
            // 下载文件时访问OfficeServer与其他网页页面session保持一致的说明 （开始）
            // 为了让/OfficeServer这个Servlet和浏览器的其他页面保持session一致，需要在访问OfficeServer之前把其他页面的cookie发送到服务端去，通过调用
            // INetSetCookie方法，参数是服务器url和cookie值
            var MyCookie = 'JSESSIONID=' + mSessionID
            WebOfficeObj.INetSetCookie(WebOfficeObj.ServerUrl, MyCookie)
            //WebOfficeObj.INetSetCookie(WebOfficeObj.ServerUrl, document.cookie);
            // 但如果浏览器设置了禁止cookie，那么document.cookie则获取不到有效cookie值，这时候可以通过在客户端创建一个
            // 跟cookie内容值一样的字符串，再把这个字符串当成cookie发送给服务器，也能实现session保持一致
            // 创建的字符串是这样的形式：JSESSIONID=XXXXXXXXXXXXXXXXXXXXXX,
            // XXXX...XXX是服务端的sessionid，可以通过jsp变量的形式发送给客户端
            // varMyCookie = "JSESSIONID=" + "<%=mSessionID%>";
            // WebOfficeObj.INetSetCookie(WebOfficeObj.ServerUrl, varMyCookie);
            // 下载文件时访问OfficeServer与其他网页页面session保持一致的说明 （结束）
          }
          if (WebOfficeObj.WebOpen()) {
            this.StatusMsg(WebOfficeObj.Status)
          }
        } catch (e) {
          console.info(e)
          this.StatusMsg(e.description)
        }
      },
      WebOpenLocal() {
        WebOfficeObj.WebOpenLocal()
      },
      WebSaveLocal() {
        WebOfficeObj.WebSaveLocal()
      },

      //URL地址打开文档
      LoadURL() {
        try {
          debugger
          this.OnUnLoad()
          WebOfficeObj.ServerUrl = ServerUrl //服务器地址
          WebOfficeObj.ShowMenu = 0
          WebOfficeObj.ShowToolBar = 0
          this.SetGraySkin() //设置控件皮肤
          if (WebOfficeObj.WebOpen2('/Document/sample.doc')) {
            // 文件在服务器上的相对路径 FileName
            this.StatusMsg(WebOfficeObj.Status)
          }
        } catch (e) {
          this.StatusMsg(e.description)
        }
      },

      //服务端Servlet方式打开文档
      LoadServlet() {
        try {
          this.OnUnLoad()
          WebOfficeObj.ServerUrl = ServerUrl // 用来保存文件的Server
          var downloadLink =
            ServerUrl + 'FileDownload?FileName=' + '/Document/sample.doc'
          WebOfficeObj.ShowMenu = 0
          WebOfficeObj.ShowToolBar = 0
          this.SetGraySkin() //设置控件皮肤
          if (WebOfficeObj.WebOpen3(downloadLink)) {
            // 文件在服务器上的相对路径 FileName
            this.StatusMsg(WebOfficeObj.Status)
          }
        } catch (e) {
          this.StatusMsg(e.description)
        }
      },

      //保存文档
      SaveDocument() {
        WebOfficeObj.FileName = 'sample.doc'
        WebOfficeObj.FileType = '.doc'
        if (WebOfficeObj.WebSave()) {
          //交互OfficeServer的OPTION="SAVEFILE"
          WebOfficeObj.WebClose()
          window.close()
        } else {
          WebOfficeObj.Alert(WebOfficeObj.Status)
          this.StatusMsg(WebOfficeObj.Status)
        }
      },

      //设置页面中的状态值
      StatusMsg(mValue) {
        try {
          document.getElementById('StatusBar').value = mValue
        } catch (e) {
          return false
        }
      },

      //烟枪灰皮肤
      SetGraySkin() {
        //参数顺序依次为：控件标题栏颜色、自定义菜单开始颜色、自定义工具栏按钮开始颜色、自定义工具栏按钮结束颜色、
        //自定义工具栏按钮边框颜色、自定义工具栏开始颜色、控件标题栏文本颜色（默认值为：0x000000）
        if (
          !WebOfficeObj.WebSetSkin(
            0xdbdbdb,
            0xeaeaea,
            0xeaeaea,
            0xdbdbdb,
            0xdbdbdb,
            0xdbdbdb,
            0x000000
          )
        )
          WebOfficeObj.Alert(WebOfficeObj.Status)
      },

      OnUnLoad() {
        WebOfficeObj.WebClose()
      },

      //前后台交互，key在后台接收
      SendMessage() {
        //如果非ie浏览器调用IsModify隐藏插件避免窗体被遮挡
        WebOfficeObj.HidePlugin(0)
        var info = window.prompt(
          '请输入要传到服务器处理页面上的内容:',
          '参数内容'
        )
        //如果非ie浏览器调用IsModify隐藏插件避免窗体被遮挡
        if (info == null) {
          return false
        }
        WebOfficeObj.WebSetMsgByName('TESTINFO', info) //USERNAME在后获取
        if (WebOfficeObj.WebSendMessage()) {
          // 交互信息为INPORTTEXT
          WebOfficeObj.Alert(WebOfficeObj.WebGetMsgByName('RETURNINFO')) //USERNAME值为对应后台的key
        } else {
          WebOfficeObj.Alert('客户端Web发送数据包命令没有合适的处理函数')
        }
        WebOfficeObj.HidePlugin(1)
      },
    },
  }
</script>
<style scoped></style>
