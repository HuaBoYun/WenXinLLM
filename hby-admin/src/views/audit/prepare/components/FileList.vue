<template>
  <div>
    <el-dialog
      :append-to-body="true"
      title="附件列表"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
      v-if="dialogFormVisible"
    >
      <el-button
        type="success"
        @click="handleTB"
        style="position: absolute; top: 84px; right: 12%; z-index: 999"
      >
        同步项目资料附件
      </el-button>
      <el-row :gutter="14">
        <el-col :span="24">
          <!-- <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :action="baseApi + api"
            :headers="headers"
            :on-success="handleSuccess"
            :show-file-list="false"
            multiple
            :file-list="fileList"
            :data="fileParams"
            :before-upload="handleBeforeUpload"
          >
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">上传</el-button>
            </div>
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
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
          <div>
            <el-button
              type="success"
              @click="handlePerson"
              :disabled="select.length === 0"
            >
              下发
            </el-button>
            <el-button
              type="success"
              @click="handleBatchDownload"
              :disabled="select.length === 0"
            >
              批量下载
            </el-button>
          </div>

          <el-table
            :data="tableDataFile"
            ref="multipleTable"
            :row-key="getRowKeys"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              width="48"
              type="selection"
              :reserve-selection="true"
            ></el-table-column>
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFile(scope.row)"
                  :disabled="false"
                >
                  预览
                </el-button>
                <el-button
                  v-if="!disabled"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
      <FileSelectPerson ref="select" />
    </el-dialog>
  </div>
</template>
<script>
  import {
    dataprojectFileList,
    deleteProjectFile,
    tbdata,
    saveFj,
  } from '@/api/audit/preparation'
  import store from '@/store'
  import FileSelectPerson from './fileSelectPerson.vue'
  import JSZip from 'jszip'
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown, getFileData } from '@/utils/fileHandler'
  export default {
    name: 'xxxxx',
    components: { FileSelectPerson },
    data() {
      return {
        dialogFormVisible: false,
        tableDataFile: [],
        fileIds: [],
        fileList: [],
        baseApi: baseURL,
        // api: '/audit/auditReady/sjzl/dataproject_file_upload',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        fileParams: {},
        select: [],
        disabled: false,
      }
    },
    methods: {
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(row) {
        this.dialogFormVisible = true
        this.fileParams = { dataId: row.id }

        //获取附件列表接口
        let resF = await dataprojectFileList({
          dataId: row.id,
        })
        //回填上传文件表格
        const arr = resF.data.data
        const arr1 = arr.map((item) => {
          return {
            ...item,
            name: item.attname,
            size: item.attsize,
            createPerson: item.uploader,
          }
        })
        const arr2 = arr.map((res) => {
          return res.attid
        })
        //收集id
        this.fileIds = arr2
        this.tableDataFile = arr1
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.select = []
      },
      handleDeleteFile(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          let res = await deleteProjectFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.fileIds.splice(index, 1)
            this.tableDataFile.splice(index, 1)
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
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
      // handleSuccess(response, file, fileList) {
      //   if (file.response.result == '200') {
      //     file.createPerson = JSON.parse(
      //       localStorage.getItem('userInfo')
      //     ).realname
      //     let arr = file.response.data
      //     const arr1 = {
      //       name: arr.attname,
      //       size: arr.attsize,
      //       createPerson: arr.uploader,
      //       attid: arr.attid,
      //     }
      //     this.tableDataFile.push(arr1)

      //     this.fileList = fileList
      //     let fileArr = []
      //     this.fileList.forEach((item) => {
      //       fileArr.push(item.response.data.attid)
      //     })
      //     this.fileIds = [...this.fileIds, ...fileArr]
      //     this.$baseMessage(file.response.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.response.msg, 'error')
      //   }
      // },
      handlePerson() {
        this.$refs['select'].showEdit(this.select)
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.attid
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        // selection.shift()
        this.select = selection.map((item) => item.attid)
      },
      // 4、回显已勾选的数据
      setCheckedRows() {
        let selectItem = []
        this.tableDataFile.forEach((item) => {
          this.select.forEach((id) => {
            if (item.attid === id) {
              selectItem.push(item)
            }
          })
        })
        this.$refs.multipleTable.toggleRowSelection(selectItem)
      },
      // async handlePreview(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   const url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   this.$iFrameDialog({ iframeUrl: url })
      // },
      handleTB() {
        tbdata({
          dataId: this.fileParams.dataId,
        }).then(async (res) => {
          if (res.msg === '成功') {
            //获取附件列表接口
            let resF = await dataprojectFileList({
              dataId: this.fileParams.dataId,
            })
            //回填上传文件表格
            const arr = resF.data.data
            const arr1 = arr.map((item) => {
              return {
                ...item,
                name: item.attname,
                size: item.attsize,
                createPerson: item.uploader,
              }
            })
            this.tableDataFile = arr1
          }
        })
      },
      async handleBatchDownload() {
        if (this.select.length === 0) {
          this.$message.warning('请选择要下载的文件')
          return
        }
        try {
          const zip = new JSZip()
          const promises = this.select.map(async (attId) => {
            try {
              const file = this.tableDataFile.find(
                (item) => item.attid === attId
              )
              if (file) {
                const data = await getFileData(
                  { attid: attId, attname: file.name, isEncrypted: file.isEncrypted, jmurl: file.jmurl, mimeType: file.mimeType },
                  this.headers,
                  this.lodeapi
                )
                zip.file(file.name, data)
              }
            } catch (error) {
              console.error(
                `Failed to download file with attId: ${attId}`,
                error
              )
            }
          })
          await Promise.all(promises)
          const content = await zip.generateAsync({ type: 'blob' })
          const url = window.URL.createObjectURL(content)
          const link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', '批量下载文件.zip')
          document.documentElement.appendChild(link)
          link.click()
          document.documentElement.removeChild(link)
          window.URL.revokeObjectURL(url)
        } catch (error) {
          console.error('Failed to create zip file:', error)
          this.$message.error('下载失败')
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

        // 获取 el-upload 的 data 参数
        const formData = {
          formlevel: this.getFormLevel,
        }

        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            formData: formData, // 传递额外的表单数据
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
      async handleSuccess(file) {
        if (file.code == 200) {
          this.fileList = [...this.fileList, ...file.data]
          this.tableDataFile = [...this.tableDataFile, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')

          // 上传成功后调用saveFj接口
          try {
            const saveFjParams = file.data.map((item) => ({
              ATTID: item.attid,
              ID: this.fileParams.dataId,
            }))

            const saveFjResult = await saveFj(saveFjParams)
            if (saveFjResult.data.code === 200) {
              console.log('saveFj接口调用成功:', saveFjResult.data.msg)
            } else {
              console.error('saveFj接口调用失败:', saveFjResult.data.msg)
            }
          } catch (error) {
            console.error('调用saveFj接口时发生错误:', error)
          }
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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
