<template>
  <div class="upload-container">
    <el-upload
      ref="upload"
      accept="image/gif, image/jpeg, image/jpg, image/png"
      :action="uploadHost"
      :before-upload="uploading"
      :data="uploadData"
      :on-error="fileError"
      :on-success="
        (response, file, fileList) => {
          fileSuccess(file, 1)
        }
      "
      :show-file-list="false"
      type="drag"
    >
      <el-button icon="el-icon-upload" size="mini" type="primary">
        上传图片
      </el-button>
    </el-upload>

    <el-upload
      ref="upload"
      accept="video/*"
      :action="uploadHost"
      :before-upload="uploading"
      :data="uploadData"
      :on-error="fileError"
      :on-success="
        (response, file, fileList) => {
          fileSuccess(file, 2)
        }
      "
      :show-file-list="false"
      type="drag"
    >
      <el-button icon="el-icon-upload" size="mini" type="warning">
        上传视频
      </el-button>
    </el-upload>

    <el-dialog :visible.sync="dialogVisible" :close-on-click-modal="false">
      <el-upload
        accept="image/gif, image/jpeg, image/jpg, image/png, video/*"
        :action="uploadHost"
        :before-upload="beforeUpload"
        class="editor-slide-upload"
        :data="uploadData"
        list-type="picture-card"
        :multiple="true"
        :on-remove="handleRemove"
        :on-success="handleSuccess"
        :show-file-list="false"
      >
        <el-button size="small" type="primary">点击上传</el-button>
      </el-upload>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确认</el-button>
    </el-dialog>
  </div>
</template>

<script>
  import { oss } from '@/utils/alioss'
  export default {
    name: 'EditorSlideUpload',
    data() {
      return {
        dialogVisible: false,
        listObj: {},
        fileList: [],
        uploadHost: '',
        uploadData: {},
      }
    },
    methods: {
      //图片上传  上传之前的事件
      uploading(info) {
        this.loading = true
        return oss(info.name).then((res) => {
          this.uploadHost = res.host
          this.uploadData = res
        })
      },
      //文件上传   上传成功函数
      async fileSuccess(file, uploadType) {
        const host = this.uploadData.host
        const path = this.uploadData.key
        const url = host + '/' + path
        this.loading = false
        this.uploadData = {}
        this.$emit('successCBK', { url: url, type: uploadType })
      },
      //文件上传  上传失败函数
      fileError() {
        this.loading = false
        this.$message.error('上传文件失败,请重新尝试', 'error')
      },

      checkAllSuccess() {
        return Object.keys(this.listObj).every(
          (item) => this.listObj[item].hasSuccess
        )
      },
      handleSubmit() {
        const arr = Object.keys(this.listObj).map((v) => this.listObj[v])
        if (!this.checkAllSuccess()) {
          this.$message(
            '请等待所有图片上传成功。如果有网络问题，请刷新页面并重新上传!'
          )
          return
        }
        this.$emit('successCBK', arr)
        this.fileList = []
        this.dialogVisible = false
      },
      handleSuccess(response, file) {
        const uid = file.uid
        const objKeyArr = Object.keys(this.listObj)

        // for (let i = 0, len = objKeyArr.length; i < len; i++) {
        //   if (this.listObj[objKeyArr[i]].uid === uid) {
        //     this.listObj[objKeyArr[i]].url = response.files.file
        //     this.listObj[objKeyArr[i]].hasSuccess = true
        //     return
        //   }
        // }
        const host = this.uploadData.host
        const path = this.uploadData.key
        const url = host + '/' + path
        this.listObj[uid].uid = uid
        this.listObj[uid].url = url
        this.listObj[uid].hasSuccess = true
      },
      handleRemove(file) {
        const uid = file.uid
        const objKeyArr = Object.keys(this.listObj)
        for (let i = 0, len = objKeyArr.length; i < len; i++) {
          if (this.listObj[objKeyArr[i]].uid === uid) {
            delete this.listObj[objKeyArr[i]]
            return
          }
        }
      },
      beforeUpload(file) {
        return oss(file.name).then((res) => {
          this.uploadHost = res.host
          this.uploadData = res
        })
        // const _self = this
        // const _URL = window.URL || window.webkitURL
        // const fileName = file.uid
        // this.listObj[fileName] = {}
        // return new Promise((resolve, reject) => {
        //   const img = new Image()
        //   img.src = _URL.createObjectURL(file)
        //   img.onload = function() {
        //     _self.listObj[fileName] = { hasSuccess: false, uid: file.uid, width: this.width, height: this.height }
        //   }
        //   resolve(true)
        // })
      },
    },
  }
</script>

<style lang="scss" scoped>
  .editor-slide-upload {
    margin-bottom: 20px;
    .el-upload--picture-card {
      width: 100%;
    }
  }
  .upload-container {
    display: flex !important;
  }
  .upload-container > div {
    margin-left: 10px;
  }
</style>
