<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000"
      @close="close"
    >
      <el-row :gutter="24">
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseURL + uploadApi"
              :headers="headers"
              :on-success="(res) => handleSuccess(res)"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="fileList">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
      <template #footer>
        <!-- <el-button @click="close">关 闭</el-button> -->
        <el-button @click="add" type="primary">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import {
    addNDJH,
    deleteFile,
    getScoreFileData,
    addNDJHFile,
  } from '@/api/fwgl/jhkh'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { download } from '@/api/fwgl/zzxx'
  import store from '@/store'
  import { baseURL } from '@/config'
  import { uploadApi } from '@/api/fwgl/api'
  const token = store.getters['user/token']
  export default {
    props: [],
    data() {
      return {
        dialogFormVisible: false,
        footer: true,
        // baseURL: 'http://cn-gz-txy.starryfrp.example:28691',
        baseURL: baseURL,
        uploadApi: uploadApi,
        headers: { token: token },
        fileList: [],
        title: '附件列表',
        annualExamineTopicExtId: '',
        annualExamineScoreExtId: '',
      }
    },
    created() {},
    methods: {
      showEdit(id, editId, footer) {
        this.footer = footer

        this.annualExamineTopicExtId = id
        this.dialogFormVisible = true
        this.fileList = []
        if (editId) {
          this.annualExamineScoreExtId = editId
          getScoreFileData({
            id: editId,
          }).then((res) => {
            this.fileList = res.data
          })
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        const ids = this.fileList.map((res) => res.fileId).toString()

        addNDJHFile({
          fileIds: ids,
          annualExamineTopicExtId: this.annualExamineTopicExtId,
          annualExamineScoreExtId: this.annualExamineScoreExtId,
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage('保存成功', 'success', 'vab-hey-message-success')
            this.dialogFormVisible = false
            this.$emit(
              'getFileInfo',
              res.data.annualExamineScoreExtId,
              res.data.annualExamineTopicExtId
            )
          }
        })
      },
      //文件上传
      handleSuccess(res) {
        if (res && res.data && res.data.fileIds && res.data.fileIds.length) {
          this.$message({
            message: '上传成功',
            type: 'success',
          })
          let list = this.fileList || []
          list.push(res.data.fileIds[0])
          this.fileList = list

          const ids = res.data.fileIds.map((x) => x.fileId).join(',')
          // this.files[id] = ids
        }
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */      
      async handleDown(row) {
        const data = await download({ fileId: row.fileId })
        let filename = row.fileName
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList.splice(
              this.fileList.findIndex((x) => x.fileId == row.fileId),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */      
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
    },
  }
</script>
