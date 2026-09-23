<template>
  <!-- 质量分析报告 edit -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="文件上传"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <!-- <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api"
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="handleSuccess"
            :file-list="tableData"
          >
            <el-button type="success">上传</el-button>
          </el-upload> -->
          <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :show-file-list="false"
            action=""
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="handleSuccess"
            :file-list="fileList"
            :before-upload="handleBeforeUpload"
            :multiple="true"
          >
            <div style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
        </div>
        <el-table :data="tableData">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDowns(row)">下载</el-button>
              <el-button type="text" @click="handlePreviewFile(row)">
                预览
              </el-button>
              <el-button type="text" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { handleDeleteAnnex } from '@/oapi/audit/task'
  import { myTaskgetIdbyattlist } from '@/api/audit/implement'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { download } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']

  export default {
    data() {
      return {
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        row: {},
        tableData: [],
        dialogFormVisible: false,
      }
    },
    methods: {
      async showEdit(row) {
        console.log('row:', row)
        this.dialogFormVisible = true
        this.row = row
        this.getList(row)
        // this.tableData = row.attachments.map((item) => item)
      },
      async getList(row) {
        const res = await myTaskgetIdbyattlist({
          id: row.id,
          attids: row.attIds.toString(),
        })
        this.tableData = res.data.data
      },
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },

      // async handlePreviewFile(row) {
      //   console.log('row', row)
      //   const { data } = await getPrivewAttInfo({
      //     //此接口通用？
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   window.open(
      //     data.previewurl +
      //       '?url=' +
      //       encodeURIComponent(Base64.encode(data.ftpUrl))
      //   )
      // },
      async handleDelete(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            handleDeleteAnnex({ attId: row.attid }).then(() => {
              this.tableData = this.tableData.filter(
                (item) => item.attid != row.attid
              )
              console.log(this.row.attachments)
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      // handlePreview(file) {},
      handleSuccess(file) {
        console.log(file)
        if (file.result == '200') {
          this.tableData.push(file.data)
          console.log(this.row.attachments)
          this.$baseMessage('上传成功', 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      close() {
        this.tableData = []
        this.dialogFormVisible = false
      },
      async save() {
        const obj = Object.assign({}, this.row, {
          attIds: this.tableData.map((item) => item.attid),
          attachments: this.tableData,
        })
        this.$emit('updateList', obj)
        this.close()
      },

      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }

        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccess(file) {
        if (file.code == 200) {
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          // 当文件是加密状态时，使用指定的在线预览链接
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
