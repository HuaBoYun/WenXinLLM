<template>
  <div class="navbar">
    <!-- @open="handleOpen"
      @close="handleClose"
    @select="handleSelect"-->
    <!-- mode="horizontal" -->
    <el-menu
      default-active="1"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      @select="handleSelect"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
    >
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-menu"></i>
          <span>通用功能</span>
        </template>
        <el-menu-item index="1-1">打开本地文档（弹窗）</el-menu-item>
        <el-menu-item index="1-2">另存为本地（弹窗）</el-menu-item>
        <el-menu-item index="1-3">新建空白文档</el-menu-item>
        <el-menu-item index="1-4">打印当前文档</el-menu-item>
        <!-- <el-menu-item index="1-5">插入远程图片</el-menu-item> -->
        <!-- <el-menu-item index="1-6">文档另存为PDF</el-menu-item> -->
        <!-- <el-submenu index="1-4">
          <template slot="title">选项4</template>
          <el-menu-item index="1-4-1">选项1</el-menu-item>
          <el-menu-item index="1-4-2">选项2</el-menu-item>
          <el-menu-item index="1-4-3">选项3</el-menu-item>
        </el-submenu>-->
      </el-submenu>

      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-edit"></i>
          <span>痕迹功能</span>
        </template>
        <el-menu-item index="2-1">显示痕迹</el-menu-item>
        <el-menu-item index="2-2">隐藏痕迹</el-menu-item>
        <el-menu-item index="2-3">接受痕迹</el-menu-item>
      </el-submenu>

      <!-- <el-submenu index="3">
        <template slot="title">
          <i class="el-icon-document"></i>
          <span>套红功能</span>
        </template>
        <el-menu-item index="3-1">模板套红一</el-menu-item>
        <el-menu-item index="3-2">模板套红二</el-menu-item>
      </el-submenu> -->

      <!-- <el-submenu index="4">
        <template slot="title">
          <i class="el-icon-document-add"></i>
          <span>书签功能</span>
        </template>
        <el-menu-item index="4-1">书签填充</el-menu-item>
        <el-menu-item index="4-2">取得书签值</el-menu-item>
      </el-submenu> -->

      <!-- <el-submenu index="5">
        <template slot="title">
          <i class="el-icon-folder"></i>
          <span>权限功能</span>
        </template>
        <el-menu-item index="5-1">保护文档</el-menu-item>
        <el-menu-item index="5-2">取消保护</el-menu-item>
        <el-menu-item index="5-3">允许拷贝</el-menu-item>
        <el-menu-item index="5-4">禁止拷贝</el-menu-item>
      </el-submenu> -->

      <!-- <el-submenu index="6">
        <template slot="title">
          <i class="el-icon-files"></i>
          <span>文档操作</span>
        </template>
        <el-menu-item index="6-1">保存到服务器</el-menu-item>
        <el-menu-item index="6-2">关闭当前文档</el-menu-item>
       <el-menu-item index="6-5">打开URL地址文档</el-menu-item>  
       <el-menu-item index="6-6">1111111</el-menu-item>  
      </el-submenu> -->
      <el-menu-item index="7">
        <i class="el-icon-document-add"></i>
        <span>保存到服务器</span>
      </el-menu-item>
      <el-menu-item index="8">
        <i class="el-icon-files"></i>
        <span>关闭当前文档</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        Revisionflag: true, //痕迹是否显示默认true显示痕迹
        templateName: '模板一.doc',
        ProtectFlag: true,
        CopyFlag: true,
      }
    },
    methods: {
      handleOpen(key, keyPath) {},
      handleClose(key, keyPath) {},
      handleSelect(key, keyPath) {
        switch (key) {
          case '1-1':
            this.WebOpenLocal()
            break //打开本地文件
          case '1-2':
            this.WebSaveLocal()
            break //另存本地文件
          case '1-3':
            this.CreateFile()
            break //新建文件
          case '1-4':
            this.WebOpenPrint()
            break //打印文件
          case '1-5':
            this.WebInsertImage()
            break //插入远程图片
          case '1-6':
            this.WebSavePdfFile()
            break //文档另存为PDF
          case '1-7':
            this.ExportToOfd()
            break //文档另存为OFD
          case '2-1':
            this.Revisionflag = true
            this.ShowRevision(this.Revisionflag)
            break //痕迹控制//true显示 false隐藏
          case '2-2':
            this.Revisionflag = false
            this.ShowRevision(this.Revisionflag)
            break //同上
          case '2-3':
            this.AcceptAllRevisions()
            break //清除痕迹
          case '3-1':
            this.templateName = '模板一.doc'
            this.WebUseTemplate(this.templateName)
            break //模板套红一
          case '3-2':
            this.templateName = '模板二.doc'
            this.WebUseTemplate(this.templateName)
            break //模板套红二
          case '4-1':
            this.WebSetBookMark()
            break //书签填充
          case '4-2':
            this.WebGetBookMark()
            break //取得书签值
          case '5-1':
            this.ProtectFlag = true
            this.WebProtect(this.ProtectFlag)
            break //保护文档//true保护 false取消
          case '5-2':
            this.ProtectFlag = false
            this.WebProtect(this.ProtectFlag)
            break //同上
          case '5-3':
            this.CopyFlag = true
            this.WebEnableCopy(this.CopyFlag)
            break //允许拷贝//true 允许 false 禁止
          case '5-4':
            this.CopyFlag = false
            this.WebEnableCopy(this.CopyFlag)
            break //同上
          case '6-1':
            this.SaveDocument()
            break //保存文件到服务器
          case '6-2':
            this.WebClose()
            break //关闭进程
          case '6-5':
            this.WebDownLoadFile()
            break //下载URL路径文件并打开
          case '6-6':
            this.SendMessage()

            break //下载URL路径文件并打开
          case '7':
            this.SaveDocument()
            break //保存文件到服务器
          case '8':
            this.WebClose()
            break //关闭进程
          default:
            return
        }
      },
      WebOpenLocal() {
        this.$bus.emit('WebOpenLocal', 'WebOpenLocal')
      },
      WebSaveLocal() {
        this.$bus.emit('WebSaveLocal', 'WebSaveLocal')
      },
      CreateFile() {
        this.$bus.emit('CreateFile', 'CreateFile')
      },
      WebOpenPrint() {
        this.$bus.emit('WebOpenPrint', 'WebOpenPrint')
      },
      WebInsertImage() {
        this.$bus.emit('WebInsertImage', 'WebInsertImage')
      },
      WebSavePdfFile() {
        this.$bus.emit('WebSavePdfFile', 'WebSavePdfFile')
      },
      ExportToOfd() {
        this.$bus.emit('ExportToOfd', 'ExportToOfd')
      },
      ShowRevision(Revisionflag) {
        this.$bus.emit('ShowRevision', Revisionflag)
      },
      AcceptAllRevisions() {
        this.$bus.emit('AcceptAllRevisions', 'AcceptAllRevisions')
      },
      WebUseTemplate(templateName) {
        this.$bus.emit('WebUseTemplate', templateName)
      },
      WebSetBookMark() {
        this.$bus.emit('WebSetBookMark', 'WebSetBookMark')
      },
      WebGetBookMark() {
        this.$bus.emit('WebGetBookMark', 'WebGetBookMark')
      },
      WebProtect(ProtectFlag) {
        this.$bus.emit('WebProtect', ProtectFlag)
      },
      WebEnableCopy(CopyFlag) {
        this.$bus.emit('WebEnableCopy', CopyFlag)
      },
      SaveDocument() {
        this.$bus.emit('SaveDocument', 'SaveDocument')
      },
      WebClose() {
        this.$bus.emit('WebClose', 'WebClose')
      },
      WebDownLoadFile() {
        this.$bus.emit('WebDownLoadFile', 'WebDownLoadFile')
      },
      SendMessage() {
        this.$bus.emit('SendMessage', 'SendMessage')
      },
    },
  }
</script>
<style scoped>
  .el-menu {
    border-right: none;
    height: 30px;
    line-height: 30px;
  }
</style>
