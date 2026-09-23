<!--
 * @Date: 2022-04-19 12:56:59
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-27 21:52:23
 * @FilePath: /hb-admin/src/components/FileUploadManual.vue
-->
<template>
  <div class="upload-container">
    <el-upload
      ref="upload"
      accept=".zip"
      action="baseApi + api"
      :auto-upload="autoUpload"
      :before-upload="beforeUpload"
      class="upload-class"
      :data="data"
      :file-list="fileList"
      :headers="{ 'X-Requested-With': 'XMLHttpRequest', token: token }"
      :http-request="httpRequest"
      :limit="limit"
      :list-type="listType"
      :name="keyName"
      :on-change="OnChange"
      :on-error="onError"
      :on-exceed="handleLimit"
      :on-preview="onPreview"
      :on-remove="OnRemove"
      :on-success="onSuccess"
      :show-file-list="showFileList"
      :with-credentials="false"
      v-bind="$attrs"
    >
      <slot>
        <div v-if="fileList.length <= limit" class="select-file">
          <i class="el-icon-upload" style="color: #409eff"></i>
          <span>点击上传</span>
          <!--          <div class="limit">{{ value?value.split(':').length:0}}/{{limit}}</div>-->
        </div>
      </slot>
    </el-upload>
    <div v-if="previewUrl && false" class="image-preview">
      <div class="image-c" :style="'background-image: url(' + previewUrl + ')'">
        <div class="del-btn">x</div>
      </div>
    </div>
  </div>
</template>
<script>
  import store from '@/store'
  /* eslint-disable */
  export default {
    props: {
      accept: {
        default: '.jpg,.jpeg,.png,.gif,.bmp',
      },
      api: {
        default: '/upload',
      },
      limit: {
        default: 1,
      },
      listType: {
        default: 'text', // text/picture/picture-card
      },
      data: {},
      value: {},
      beforeUpload: null,
      autoUpload: {
        default: true,
      },
      keyName: {
        default: 'file',
      },
      showFileList: {
        default: true,
      },
    },
    data() {
      return {
        baseApi:
          process.env.NODE_ENV === 'development'
            ? '/vab-mock-server/contract'
            : process.env.VUE_APP_BASE_API,
        fileList: [],
        currentValue: this.value || null,
        previewUrl: undefined,
        initialed: false,
        token: store.getters['user/token'],
      }
    },
    watch: {
      currentValue(val) {
        this.$emit('input', val)
      },
      value(val) {
        this.currentValue = val || null
        if (!val) {
          this.$refs.uploader.clearFiles()
        }
        if (!this.initialed) {
          this.init()
        }
      },
    },
    created() {
      this.init()
      if (this.beforeUpload) {
        this.handleBeforeUpload = this.beforeUpload
      }
    },

    methods: {
      init() {
        if (this.value) {
          this.fileList.push(this.value)
        }
        // if (this.value) {
        //   const ar = this.value.split(':')
        //   this.fileList = []
        //   ar.forEach((i) => {
        //     const arr = i.split('/')
        //     const url = this.baseApi + i
        //     this.fileList.push({
        //       name: arr[arr.length - 1],
        //       url: url,
        //     })
        //   })
        // }
      },
      onChange(file, fileList) {
        this.$emit('file-change', fileList)
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleLimit() {
        console.warn('超出允许上传数量限制：', this.limit)
        this.$message({
          message: `最多允许上传 ${this.limit} 个文件`,
          type: 'error',
          duration: 2000,
        })
      },
      onRemove(file, fileList) {
        // console.warn('onRemove', file, fileList)
        // this.previewUrl = null
        this.initialed = true
        this.fileList = fileList
        if (fileList.length) {
          this.currentValue = fileList
            .map((i) => i.url.replace(this.baseApi, ''))
            .join(',')
        } else {
          this.currentValue = null
        }
        console.warn('currentValue', this.currentValue)
      },
      onPreview(file) {},
      onSuccess(response, file, fileList) {
        console.warn('onSuccess', response, fileList)
        this.initialed = true
        this.currentValue = response
        // this.$refs.uploader.clearFiles()
        this.$emit('success', response)
      },
      onError(err) {
        this.$message.error(JSON.parse(err.message).message)
      },
    },
  }
</script>

<style lang="scss">
  .upload-container {
    position: relative;

    .upload-class {
      //width: 100%;
      //display: flex;
      //.el-upload--picture {
      //  width: 160px;
      //}
      //.el-upload-list--picture{
      //  flex: 1;
      //}
      //.el-upload-list__item{
      //  margin-top: 0;
      //  margin-left: 10px;
      //  margin-bottom: 10px;
      //  position: relative;
      //  width: 160px;
      //  height: 100px;
      //  float: left;
      //  text-align: center;
      //  .el-upload-list__item-thumbnail{
      //    float: none !important;
      //  }
      //  .el-upload-list__item-name{
      //    position: absolute;
      //    left: 0;
      //    bottom: 0;
      //    right: 0;
      //    z-index: 2;
      //    width: 100%;
      //    line-height: 20px;
      //    display: block;
      //    background-color: rgba(200, 200,200,0.6);
      //    text-align: left;
      //    overflow: hidden;
      //    text-overflow: ellipsis;
      //    display: -webkit-box;
      //    -webkit-box-orient: vertical;
      //    -webkit-line-clamp: 2;
      //    white-space: normal;
      //    word-break: break-all;
      //  }
      //}

      .select-file {
        width: 160px;
        height: 100px;
        border: 1px gray dotted;
        display: flex;
        font-size: 30px;
        align-items: center;
        justify-content: center;
        border-radius: 5px;
        display: flex;
        position: relative;
        flex-direction: column;
        .limit {
          position: absolute;
          bottom: 0;
          right: 0;
          font-size: 13px;
          border: 0px red solid;
          line-height: 20px;
          padding: 0 8px;
          background-color: rgba(224, 224, 224, 0.5);
          border-radius: 3px 0 3px 0;
        }
        span {
          font-size: 14px;
          color: #5a5e66;
        }
      }
    }

    .image-preview {
      position: absolute;
      left: 0;
      top: 0;
      display: block;
      width: 160px;
      height: 90px;
      border: 1px gray solid;
      border-radius: 5px;

      .image-c {
        position: relative;
        width: 100%;
        height: 100%;
        background-color: #efefef;
        background-position: center;
        background-repeat: no-repeat;
        background-size: contain;

        .del-btn {
          position: absolute;
          right: 5px;
          top: 5px;
          width: 20px;
          height: 20px;
          background-color: #ffffff;
          color: #000000;
          display: flex;
          justify-content: center;
          align-items: center;
          cursor: pointer;
          border: 1px #efefef solid;
          border-radius: 50%;
          display: none;
        }
        .del-btn:hover {
          background-color: #efefef;
        }
      }
    }
  }
</style>
