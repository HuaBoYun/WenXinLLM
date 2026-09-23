<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-29 10:27:38
 * @FilePath: /hb-admin/src/views/setting/system/components/LcdyView/FlowEdit.vue
-->
<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="80px" :model="form" :rules="rules">
      <el-form-item label="流程名称" prop="processname">
        <el-input v-model.trim="form.processname" />
      </el-form-item>
      <el-form-item label="流程文件" prop="file">
        <el-upload
          ref="upload"
          accept=".zip"
          action="string"
          :auto-upload="false"
          :before-upload="handleBeforeUpload"
          :file-list="fileList"
          :headers="{ 'X-Requested-With': 'XMLHttpRequest', token: token }"
          :http-request="httpRequest"
          :limit="1"
          name="file"
          :on-change="OnChange"
          :on-remove="OnRemove"
          :on-success="handleSuccess"
          :show-file-list="true"
        >
          <el-button type="primary">选择文件</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="handleUpload">上传流程</el-button>
    </template>
  </el-dialog>
</template>

<script>
  // import { saveFlowForView } from '@/api/setting/system'
  import store from '@/store'
  import axios from 'axios'
  const { baseURL } = require('@/config')

  export default {
    name: 'FlowEdit',
    data() {
      return {
        processSetting: undefined,
        form: {
          processid: undefined,
          processname: undefined,
          file: undefined,
        },
        rules: {
          processname: [
            { required: true, trigger: 'blur', message: '请输入流程名称' },
          ],
          file: [
            { required: true, trigger: 'change', message: '请选择流程文件' },
          ],
        },
        title: '流程上传',
        dialogFormVisible: false,
        options: [],
        fileList: [],
        baseApi: baseURL,
        token: store.getters['user/token'],
        api: '/setting/adddeploymentzip',
      }
    },
    created() {},
    methods: {
      showEdit(processSetting, name) {
        // this.processSetting = processSetting
        // const { remark } = processSetting
        this.form.processid = name
        this.form.processname = name
        this.form.file = undefined
        ;(this.fileList = []), (this.dialogFormVisible = true)
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      OnChange(file, fileList) {
        this.form.file = fileList
        this.fileList = fileList
      },
      //文件移除
      OnRemove(file, fileList) {
        this.form.file = fileList
        this.fileList = fileList
      },
      //覆盖默认上传，自定义方法
      httpRequest() {
        let file = this.fileList
        // 使用formdata 由单文件对象变为多文件数组，然后进行遍历处理。
        // 可以通过get(key)与getAll(key)来获取相对应的值
        var formdata = new FormData()
        let set = new Set(file)
        var fill = {}
        set.forEach((fil) => {
          fill = fil.raw
          formdata.append('file', fil.raw)
        })
        formdata.append('processname', this.form.processname)
        formdata.append('processid', this.form.processname)
        if (fill.length != 0) {
          axios
            .post(this.baseApi + this.api, formdata, {
              onUploadProgress(progressEvent) {
                if (progressEvent.lengthComputable) {
                  let val = (
                    (progressEvent.loaded / progressEvent.total) *
                    100
                  ).toFixed(0) //获取百分比
                  //progressEvent.loaded 上传到服务器多少秒
                  //progressEvent.total 图片总大小
                  var percent = parseInt(val)
                }
                //调用文件上传时钩子
                // this.onProgress(percent)
              },
            })
            .then((res) => {
              this.handleSuccess(res.data)
            })
            .catch((err) => {})
        } else if (fill.length == 0) {
          this.$baseMessage('请上传文件', 'error', 'vab-hey-message-error')
        }
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleSuccess(res, file) {
        this.$refs['upload'].clearFiles()
        this.fileList = []
      },
      handleUpload() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.httpRequest()
            // const { msg } = await saveFlowForView(this.form)
            // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            setTimeout(() => {
              this.$emit('fetch-data')
              this.close()
            }, 2000)
          }
        })
      },
    },
  }
</script>
