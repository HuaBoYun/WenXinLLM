<template>
  <!-- 审计承诺书 edit -->
  <el-dialog
    v-if="dialogFormVisible"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="disabled"
      >
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectname">
            <el-input
              v-model="formData.projectname"
              clearable
              placeholder="请输入模板编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="编号" prop="lettercode">
            <el-input
              v-model="formData.lettercode"
              clearable
              placeholder="请输入模板编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="名称" prop="lettername">
            <el-input
              v-model="formData.lettername"
              clearable
              placeholder="请输入模板名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="创建人" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createtime">
            <el-input
              v-model="formData.createtime"
              clearable
              placeholder="请输入创建时间"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
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
          <el-table :data="tableData">
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
              width="200"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDowns(row)"
                  :disabled="false"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFile(row)"
                  :disabled="false"
                >
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" v-if="!disabled" @click="save">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    auditCommitmentLetterUpdate,
    auditCommitmentLetterDetail,
    auditCommitmentLetterDeleteAtt,
    auditCommitmentLetterGetattList,
  } from '@/oapi/audit/implement'
  import store from '@/store'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { getCurrSsProject } from '@/oapi/audit/project'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'ProjectDataInfo',
    inheritAttrs: false,
    data() {
      return {
        // baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          projectname: '',
          lettercode: '',
          lettername: '',
          realname: '',
          createtime: '',
        },
        fileList: [],
        tableData: [],
        fileIds: [],
        disabled: false,
        dialogFormVisible: false,
        title: '',
        rules: {
          lettercode: [
            { required: true, message: '请输入模板编号', trigger: 'blur' },
          ],
          lettername: [
            { required: true, message: '请输入模板名称', trigger: 'blur' },
          ],
        },
      }
    },
    methods: {
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 10)
          .replace('T', ' ')
      },
      // 获取当前实施项目
      async getCurrentProject() {
        let obj = {}
        const { data } = await getCurrSsProject()
        obj = data.pj
        return obj
      },
      async showEdit(row, disabled) {
        this.disabled = !!disabled
        if (!row) {
          const currentProject = await this.getCurrentProject()
          this.formData.projectname = currentProject.projectName
          this.formData.projectid = currentProject.id
          this.title = '新建'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            realname: resL,
            createtime: this.getCurrentDate(),
          }
        } else {
          // 编辑/
          const res = await auditCommitmentLetterGetattList({
            letterid: row.letterid,
          })
          const detailData = await auditCommitmentLetterDetail({
            letterid: row.letterid,
          })
          console.log(detailData, res)
          Object.assign(this.formData, detailData.data.data)
          this.tableData = res.data
        }
        this.dialogFormVisible = true
      },
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
      //     this.tableData.push(arr1)

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
      close() {
        this.$refs['elForm'].resetFields()
        this.formData = {
          projectname: '',
          lettercode: '',
          lettername: '',
          realname: '',
          createtime: '',
        }
        this.tableData = []
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)
            let params = { ...this.formData }
            delete params.attachments
            delete params.realname
            delete params.createtime
            const { code } = await auditCommitmentLetterUpdate({
              ...params,
              attIds,
            })

            if (code === 1) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
        })
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
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteReportFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableData
            list = list.filter((item) => item.attid != row.attid)
            this.tableData = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
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
      // downloadFileByBlob(blob, fileName = 'file') {
      //   let blobUrl = window.URL.createObjectURL(blob)
      //   let link = document.createElement('a')
      //   link.download = fileName || 'defaultName'
      //   link.style.display = 'none'
      //   link.href = blobUrl
      //   // 触发点击
      //   document.body.appendChild(link)
      //   link.click()
      //   // 移除
      //   document.body.removeChild(link)
      // },
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
