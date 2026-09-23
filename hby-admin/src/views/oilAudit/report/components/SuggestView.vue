<template>
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
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!disabled"
      >
        <el-col :span="12">
          <el-form-item label="文号" label-width="140px" prop="document">
            <el-input
              v-model="formData.document"
              clearable
              placeholder="请输入文号"
              :style="{ width: '100%' }"
              :disabled="!disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="标题" label-width="140px" prop="title">
            <el-input
              v-model="formData.title"
              clearable
              placeholder="请输入标题"
              :style="{ width: '100%' }"
              :disabled="!disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请选择审计项目名称"
              style="width: 266px; height: 30px"
              disabled
            />
            <el-button
              @click="$refs.project.show()"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="cjr">
            <el-input
              v-model="formData.cjr"
              clearable
              placeholder="请输创建人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="创建时间" label-width="140px" prop="cjsj">
            <el-date-picker
              v-model="formData.cjsj"
              placeholder="选择创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
            >
              <el-button v-if="disabled" type="success">上传</el-button>
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
              <div v-if="disabled" style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
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
                  :disabled="false"
                  @click="handleDowns(row)"
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
                  v-if="disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <projectName ref="project" @selected="selectedPro" />
    <ProcessList ref="process" @fetchData="close" />
    <div slot="footer" v-if="disabled">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    sjbgdgSaveOrUpdate,
    sjbgdgDetail,
    download,
    deleteReportFile,
  } from '@/oapi/audit/report'
  import { formatDate, formatDay } from '@/utils/index'
  import { implementPlanList, implementPlanProject } from '@/oapi/audit/project'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/api/contract/manage'
  // import projectName from '@/views/oilAudit/report/components/options/projectName.vue'
  import projectName from '@/views/oilAudit/report/components/options/projectNameNew.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'ProjectDataInfo',
    components: { Tinymce, UEditor, projectName, ProcessList },
    props: [],
    data() {
      return {
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          document: undefined,
          title: undefined,
          projectId: null,
          projectName: '',
        },
        tableData: [],
        disabled: true,
        rules: {
          document: [
            {
              required: true,
              message: '请输入文号',
              trigger: 'blur',
            },
          ],
          title: [
            {
              required: true,
              message: '请输入标题',
              trigger: 'blur',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        editId: '',
      }
    },
    methods: {
      selectedPro(val) {
        this.$set(this.formData, 'projectName', val[0].projectName)
        this.$set(this.formData, 'projectId', val[0].id)
        this.$set(this.formData, 'title', val[0].projectName + '报告')
      },
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        const {
          data: { tlist },
        } = await implementPlanList({ pageNumber: 1, pageSize: 9999 })
        if (tlist.length) {
          this.auditProjectSelectList = tlist.map((item) => {
            return {
              value: item.id,
              label: item.projectName,
            }
          })
        }
        if (row) {
          this.editId = row.sjbgdgid
          const res = await sjbgdgDetail({ sjbgdgid: row.sjbgdgid })
          this.tableData = res.data.attachments
          // this.formData = res.data
          this.formData.sjbgdgid = res.data.sjbgdgid
          this.formData.document = res.data.document
          this.formData.title = res.data.title
          this.formData.cjr = res.data.cjr
          this.formData.cjsj = res.data.cjsj
          this.formData.projectId = res.data.projectId ? res.data.projectId : ''
          this.formData.projectName = res.data.projectName
            ? res.data.projectName
            : ''
        }

        if (title == 'edit') {
          this.disabled = true
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.disabled = false
          this.footer = false
        } else if (title == 'add') {
          this.disabled = true
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            cjr: resL,
            cjsj: new Date().toJSON().split('T')[0],
          }
          const res = await implementPlanProject()
          this.$set(this.formData, 'projectName', res.data.pj.projectName)
          this.$set(this.formData, 'projectId', res.data.pj.id)
          this.$set(this.formData, 'title', res.data.pj.projectName + '报告')
        }
      },
      close() {
        console.log('close')
        this.formData = {
          document: undefined,
          title: undefined,
          projectId: null,
          projectName: '',
        }
        this.dialogFormVisible = false
        this.disabled = true
        this.editId = ''
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)
            let params = { ...this.formData }
            delete params.attachments
            const data = await sjbgdgSaveOrUpdate({
              ...params,
              attIds,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.formData.sjbgdgid = data.data.sjbgdgid
              this.editId = data.data.sjbgdgid
              this.$emit('fetchData')
            } else {
              this.$baseMessage(data.msg, 'error')
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
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.result == '200') {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // async handlePreviewFile(row) {
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
      handleApproval() {
        //提交审批
        this.$refs['process'].save(178, this.editId)
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
