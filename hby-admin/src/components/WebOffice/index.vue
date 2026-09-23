<template>
  <div class="main">
    <!-- <el-button type="primary" @click="WebOpenLocal">保存</el-button> -->
    <div>
      <navbar class="navbar" />
    </div>
    <div class="WebOffice" v-html="WebOfficeStr"></div>
  </div>

  <!--  v-html="WebOfficeStr" -->
  <!-- <p>
      /**
      *
      * @author 陈益特
      * @time 2020-08
      */
  </p>-->
</template>

<script>
  import { LoadStr, detectOS } from './iWebOffice2015.js'
  import { WebOffice2015 } from './WebOffice.js'
  import { baseURL } from '@/config/net.config'

  // const { baseURL } = require('@/config')
  import navbar from './navbar.vue'
  import { mapState } from 'vuex'
  const WebOffice = new WebOffice2015() //创建WebOffice对象

  const WebOfficeStr = LoadStr()
  const detectOSFlag = detectOS()

  export default {
    components: {
      navbar,
    },
    computed: {
      ...mapState({
        user: (state) => state.contractid,
      }),
    },
    data() {
      return {
        WebOfficeStr,
        contractid: '',
        baseApi: baseURL,
      }
    },

    created() {
      this.contractid = this.$store.state.acl.contractid
      this.contractid2 = this.$store.state.acl.contractid2

      //bus。js实现组件通信
      this.$bus.on('WebOpenLocal', (value) => {
        this.WebOpenLocal()
      })
      this.$bus.on('WebSaveLocal', (value) => {
        this.WebSaveLocal()
      })
      this.$bus.on('CreateFile', (value) => {
        this.CreateFile()
      })
      this.$bus.on('WebOpenPrint', (value) => {
        this.WebOpenPrint()
      })
      this.$bus.on('WebInsertImage', (value) => {
        this.$nextTick(() => {
          // this.$nextTick()它是一个异步事件，当渲染结束 之后 ，它的回调函数才会被执行
          // 弹出窗口打开之后 ，需要加载Dom, 就需要花费一点时间，我们就应该等待它加载完dom之后，再进行调
          this.WebInsertImage()
        })
      })
      this.$bus.on('WebSavePdfFile', (value) => {
        this.WebSavePdfFile()
      })
      this.$bus.on('ShowRevision', (value) => {
        this.ShowRevision(value)
      })
      this.$bus.on('AcceptAllRevisions', (value) => {
        this.AcceptAllRevisions()
      })
      this.$bus.on('WebUseTemplate', (value) => {
        this.WebUseTemplate(value)
      })
      this.$bus.on('WebSetBookMark', (value) => {
        this.WebSetBookMark()
      })
      this.$bus.on('WebGetBookMark', (value) => {
        this.WebGetBookMark()
      })
      this.$bus.on('WebProtect', (value) => {
        this.WebProtect(value)
      })
      this.$bus.on('WebEnableCopy', (value) => {
        this.WebEnableCopy(value)
      })
      this.$bus.on('SendMessage', (value) => {
        this.SendMessage(value)
      })
      this.$bus.on('SaveDocument', (value) => {
        this.SaveDocument()
      })
      this.$bus.on('WebClose', async (value) => {
        await this.SaveDocument()
        await this.UnLoad()
        await this.closeEdit()
      })
      this.$bus.on('WebClose2', async (value) => {
        await this.SaveDocument()
        await this.UnLoad()
      })
      this.$bus.on('WebDownLoadFile', (value) => {
        this.WebDownLoadFile()
      })
    },
    watch: {
      '$store.state.acl.contractid': {
        handler: function (val, oldVal) {
          if (val) {
            this.contractid = val

            this.initOffice()
          } else {
            this.UnLoad()
          }

          // this.WebOpenLocal()
        },
        deep: true,
      },
    },

    mounted() {
      // $("p").click(function() {
      //   $(this).hide();
      // });
      this.initOffice()
    },
    methods: {
      closeEdit() {
        this.$bus.$emit('closeEdit', 2)
      },
      //初始化控件
      initOffice() {
        WebOffice.setObj(document.getElementById('WebOffice2015')) //给2015对象赋值
        // const httpip = '192.0.2.16:8064'

        WebOffice.ServerUrl = `${this.baseApi}/contract/fwb/fwbEditServlet`
        // WebOffice.ServerUrl = `http://192.0.2.16:8064/fwb/fwbEditServlet`
        // WebOffice.ServerUrl =
        // 'http://59.110.x.142:8064/fwb/fwbEditServlet?contractid='+contractid
        // `http://192.0.2.16:8064/fwb/fwbEditServlet?contractid=${this.contractid}`
        // WebOffice.ServerUrl = 'http://192.0.2.200:10001/iWebOffice2015Jsp-Simple'

        // ;('http://192.0.2.200:8080/iWebOffice2015Jsp-Simple')
        // this.WebUrl = this.ServerUrl + this.SaveServlet; //SaveServlet默认是/OfficeServer
        //  WebOffice.UserName = this.contractid
        WebOffice.UserName = this.contractid

        // WebOffice.UserName = '测试账号'
        // WebOffice.contracid = `${this.contracid}`
        WebOffice.FileName = 'sample.doc'
        WebOffice.FileType = '.doc' //FileType:文档类型  .doc  .xls
        WebOffice.EditType = '2' //设置加载文档类型 0 锁定文档，1无痕迹模式，2带痕迹模式
        WebOffice.ShowWindow = true //true显示进度条//false隐藏进度条
        WebOffice.obj.Style.ShowOpenProgress = true //开启、关闭打开文档时的进度条
        //WebOfficeObj.obj.WebCreateProcess();          //创建空进程避免打开慢
        WebOffice.ShowToolBars(true) //显示Office工具栏
        WebOffice.ShowMenu = 0 //不显示工具栏
        WebOffice.ShowToolBar = 0
        if (WebOffice.WebOpen()) {
          StatusMsg(WebOffice.Status)
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
      //作用：退出iWebOffice
      UnLoad() {
        try {
          var ret = WebOffice.WebClose()
          // alert(ret)
          if (ret) {
            if (detectOSFlag.indexOf('Win') > -1) {
            } else {
              WebOffice.setPluginType(0)
            }
          } else {
          }
        } catch (e) {
          //alert(e.description);
        }
      },

      //作用：打开本地文件
      WebOpenLocal() {
        try {
          if (detectOSFlag.indexOf('Win') > -1) {
            WebOffice.WebOpenLocal()
          } else {
            var ret = WebOffice.WebOpenLocal()
          }
        } catch (e) {
          WebOffice.Alert(e.description)
        }
      },

      ////作用：创建空白文档
      CreateFile() {
        if (WebOffice.CreateFile()) {
          return true
        } else {
          return false
        }
      },
      SendMessage(v) {
        WebOffice.HidePlugin(0)
        // var info = window.prompt(
        //   '请输入要传到服务器处理页面上的内容:',
        //   '参数内容'
        // )
        let info = '11111'
        //如果非ie浏览器调用IsModify隐藏插件避免窗体被遮挡
        if (info == null) {
          return false
        }

        WebOffice.WebSetMsgByName('TESTINFO', info) //USERNAME在后获取

        if (WebOffice.WebSendMessage()) {
          // 交互信息为INPORTTEXT
          WebOffice.Alert(WebOffice.WebGetMsgByName('TESTINFO')) //USERNAME值为对应后台的key
        } else {
          WebOffice.Alert('客户端Web发送数据包命令没有合适的处理函数')
        }
        WebOffice.HidePlugin(1)
      },
      //作用：保存文档
      SaveDocument() {
        this.contractid = this.$store.state.acl.contractid
        this.contractid2 = this.$store.state.acl.contractid2

        if (!this.contractid) {
          this.$baseMessage(
            '请先保存合同主体信息!',
            'error',
            'vab-hey-message-error'
          )

          return
        } else {
          if (this.contractid2) {
            WebOffice.UserName = this.contractid2
          } else {
            WebOffice.UserName = this.contractid
          }

          WebOffice.FileName = 'sample.doc'
          WebOffice.FileType = '.doc' //FileType:文档类型  .doc  .xls
          WebOffice.EditType = '2' //设置加载文档类型 0 锁定文档，1无痕迹模式，2带痕迹模式
        }

        //alert(WebOffice.Modify);                  //判断文档是否修改，false表明文档没有修改，true表明文档被修改
        var ret = WebOffice.WebSave() //交互OfficeServer的OPTION="SAVEFILE"

        if (ret) {
          if (detectOSFlag.indexOf('Win') > -1) {
            //去掉保存弹框
            // WebOffice.Alert(WebOffice.Status)
            // window.location.reload() //刷新当前页.
            return true
          } else {
            // WebOffice.Alert(WebOffice.Status)
            // window.location.reload() //刷新当前页.
            WebOffice.WebClose()
            WebOffice.setPluginType(0)
            return true
          }
        } else {
          //alert(WebOffice.Status);
          return false
        }
      },

      //作用：显示或隐藏痕迹[隐藏痕迹时修改文档没有痕迹保留]  true表示隐藏痕迹  false表示显示痕迹
      ShowRevision(mValue) {
        //WebOffice.Alert(mValue)
        if (mValue) {
          WebOffice.WebShow(true)
          //WebOffice.setScreenFocus();      //设置焦点到文档中
        } else {
          WebOffice.WebShow(false)
          //WebOffice.setScreenFocus();
        }
      },

      //作用：接受文档所有痕迹
      AcceptAllRevisions() {
        if (detectOSFlag.indexOf('Win') > -1) {
          WebOffice.WebObject.Application.ActiveDocument.AcceptAllRevisions()
          var mCount =
            WebOffice.WebObject.Application.ActiveDocument.Revisions.Count
          if (mCount > 0) {
            return false
          } else {
            return true
          }
        } else {
          //var app = WebOffice.KGGetObject("Application");   //获取WPS对象
          //app.ActiveDocument.AcceptAllRevisions();          //调用WPS接口接受文档中所有痕迹
          var ret = WebOffice.ClearRevisions() //接受当前文档中所有的痕迹
          WebOffice.setScreenFocus() ////设置焦点到文档中
          return ret
        }
      },

      //作用：存为本地文件,弹出对话框
      WebSaveLocal() {
        try {
          WebOffice.WebSaveLocal()
          //alert(ret);
        } catch (e) {
          WebOffice.Alert(e.description)
        }
      },

      //文档保存为PDF
      WebSavePdfFile() {
        try {
          //alert(WebOffice.WebSavePDF());
          WebOffice.Alert('WebSavePDF')
          if (WebOffice.WebSavePDF()) {
            //调用时发生交互OPTION值为：SAVEPDF
            WebOffice.Alert('另存为PDF成功')
          } else {
            WebOffice.Alert(另存为PDF失败)
          }

          //alert(WebOffice.Status);
        } catch (e) {
          WebOffice.Alert(e.description) //显示出错误信息
        }
      },

      //作用：插入远程服务器图片
      WebInsertImage() {
        try {
          if (detectOSFlag.indexOf('Win') > -1) {
            WebOffice.WebInsertImage('Content', 'GoldgridLogo.jpg', true, 5) //交互OfficeServer的OPTION="INSERTIMAGE"  参数1表示标签名称  参数2表示图片文件名  参数3为true透明  false表示不透明  参数4为4表示浮于文字上方  5表示衬于文字下方
          } else {
            var ret = WebOffice.WebInsertImage(
              'Content',
              'GoldgridLogo.jpg',
              true,
              4
            ) //交互OfficeServer的OPTION="INSERTIMAGE"    参数表示图片文件名，在当前光标位置插入图片
            //var ret = WebOffice.WebInsertImage("/tmp/金格科技电子签章公司4X4.png");   //插入本地图片，参数表示本地图片全路径，在当前光标位置插入图片
          }
          var status = WebOffice.WebGetMsgByName('STATUS')
          //alert(status);
        } catch (e) {
          //WebOffice.Alert(e.description);
        }
      },

      //作用：模板套红功能
      WebUseTemplate(TemplateName) {
        WebOffice.Alert(TemplateName)
        WebOffice.Template = TemplateName
        WebOffice.WebUseTemplate()
      },

      //作用：打印文档
      WebOpenPrint() {
        try {
          var falg = WebOffice.WebOpenPrint()
          //alert(falg);
        } catch (e) {
          WebOffice.Alert(e.description)
        }
      },

      //作用：保护与解除  参数1为true表示保护文档  false表示解除保护
      WebProtect(value) {
        try {
          WebOffice.WebSetProtect(value, '') //""表示密码为空
          WebOffice.Alert('控制文档成功')
        } catch (e) {
          WebOffice.Alert(e.description)
        }
      },

      //作用：允许与禁止拷贝功能  参数1为true表示允许拷贝  false表示禁止拷贝
      WebEnableCopy(value) {
        try {
          WebOffice.CopyType = value
          WebOffice.Alert('控制文档成功')
        } catch (e) {
          WebOffice.Alert(e.description)
        }
      },

      //书签填充
      WebSetBookMark() {
        WebOffice.Alert('默认向文档中Content书签填充Value')
        if (detectOSFlag.indexOf('Win') > -1) {
          if (
            !WebOffice.WebSetBookmarks('Content', '这是填充书签Content内容测试')
          ) {
          } else {
          }
        } else {
          if (
            !WebOffice.WebSetBookMarks('Content', '这是填充书签Content内容测试')
          ) {
          } else {
          }
        }
      },

      //获取书签内容
      WebGetBookMark() {
        WebOffice.Alert('获取文档中Content书签的Value')
        WebOffice.Alert(WebOffice.WebGetBookMarks('Content'))
      },

      //下载URL地址文档，并打开
      WebDownLoadFile() {
        //下载URL地址的文档到本地
        if (
          !WebOffice.WebDownLoadFile(
            'http://www.kinggrid.com:8080/iWebOffice2015/Document/Temone.doc',
            'c://abc.doc'
          )
        ) {
          //使用控件的弹窗避免出现浏览器崩溃
          WebOffice.Alert(WebOffice.Status)
          //状态值返回到页面
          return false
        } else {
          //打开下载到本地的文档
          WebOffice.WebOpenLocalFile('c://abc.doc')
          return true
        }
      },
    },
  }
</script>

<style scoped>
  .main {
    width: 100%;
    height: 1500px;
    padding: 10px;
    overflow-y: auto;
    background-color: white;
    /* border: 1px solid #cccccc; */
  }

  .navbar {
    position: absolute;
    width: 230px;
    top: 50px;
    left: 0px;
    bottom: 0px;
    overflow-y: auto;
    background-color: #545c64;
  }

  /* 右侧主区域 */
  .WebOffice {
    position: absolute;
    top: 50px;
    left: 230px;
    right: 0px;
    bottom: 0px;
    padding: 10px;
    overflow-y: auto;
    background-color: white;
    border: 1px solid #cccccc;
  }
</style>
