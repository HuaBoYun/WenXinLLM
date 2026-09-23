<template>
  <!-- 建设项目投资完成情况 新增/修改 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="70%"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="通知名称" prop="name">
            <el-input
              v-model="formData.name"
              clearable
              placeholder="请输入通知名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="通知单位" prop="organizationName">
            <el-input
              v-model="formData.organizationName"
              clearable
              placeholder="请选择通知单位"
              :style="{ width: '80%' }"
              readonly
            />
            <el-button
              @click="handleObject"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :rows="4"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="内容" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :templates="templates"
              template="gzrz"
              :height="300"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-divider>立项建议表</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="handleAddTableData1">
              新增
            </el-button>
          </div>
          <el-table :data="tableData1">
            <el-table-column
              align="center"
              label="序号"
              prop="id"
              width="100"
            ></el-table-column>
            <el-table-column
              align="center"
              label="审计事项"
              prop="projectName"
            />
            <el-table-column
              align="center"
              label="立项理由及审计目的"
              prop="projectPurpose"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
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
        </el-col> -->

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + uploadApi"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableDataUpload"
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
              <div v-if="!disabled" style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
          <el-table :data="fileList">
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
                  @click="handleDeleteFile(row)"
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
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary" :loading="loading">确定</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <tableLxjybList ref="tableLxjybList" @changeTableData1="changeTableData1" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import {
    projectSuggestionSaveOrUpdate,
    projectSuggestionDetail,
  } from '@/oapi/audit/plan'
  import SelectDepartment from './department.vue'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  import store from '@/store'
  import UEditor from '@/components/UEditor'
  const token = store.getters['user/token']
  const { baseURL } = require('@/config')
  export default {
    name: 'jsxmtzwcqkEdit',
    inheritAttrs: false,
    props: [],
    components: {
      SelectDepartment,
      tableLxjybList: () => import('./table/tableLxjybList.vue'),
      UEditor,
      ProcessList: () =>
        import('@/views/contract/contractManage/components/ProcessList'),
    },
    data() {
      return {
        loading: false,
        baseApi: baseURL,
        // uploadApi: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          name: '',
          orgIds: '',
          remark: '',
          createUser: '',
          createTime: '',
          organizationName: '',
          content: '',
        },
        formDisabled: true,
        tableData1: [],
        rules: {},
        dialogFormVisible: false,
        title: '新增',
        templates: [],
        editId: '',
      }
    },
    methods: {
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.organizationName = val.label
        this.formData.orgIds = val.id
      },
      async showEdit(row, disabled) {
        // 打开编辑
        this.dialogFormVisible = true
        this.formDisabled = disabled
        if (row) {
          this.editId = row.id
          this.title = disabled ? '详细' : '编辑'
          projectSuggestionDetail({ id: row.id }).then((res) => {
            Object.assign(this.formData, res.data)
            this.formData.createUser = res.data.createUser
              ? res.data.createUser.realname
              : ''
            this.formData.organizationName = res.data.organizations
              ? res.data.organizations[0].orgname
              : ''
            this.tableData1 = res.data.projectSuggestions || []
            this.fileList = res.data.attachments || []
          })
        } else {
          //新增
          this.formData.createUser = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          this.formData.createTime = new Date()
        }
      },

      close() {
        this.formData = {
          name: '',
          orgIds: '',
          remark: '',
          createUser: '',
          createTime: '',
          organizationName: '',
          content: '',
        }
        this.dialogFormVisible = false
        this.tableData1 = []
        this.fileList = [] // 父编辑的上传文件集合
        this.formDisabled = true
        this.editId = ''
        this.$emit('fetchData')
      },
      add() {
        // 子编辑 保存
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const psIds = this.tableData1.map((item) => item.id).join(',')
            const attids = this.fileList.map((item) => item.attid).join(',')

            const param = { ...this.formData }
            delete param.createUser
            delete param.createTime
            delete param.attachments
            const res = await projectSuggestionSaveOrUpdate({
              ...param,
              psIds,
              attids,
            })
            if (res.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.editId = res.data.id
              this.$emit('fetchData')
              this.close()
            } else {
              this.$baseMessage(res.msg, 'error')
            }
            this.loading = false
          }
        })
      },
      // async handleDown(row) {
      //   // 下载
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
      async handleDeleteFile(row) {
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
            let list = this.fileList
            list = list.filter((item) => item.attid != row.attid)
            this.fileList = list
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

      handleAddTableData1() {
        this.$refs['tableLxjybList'].showEdit(this.tableData1)
      },
      handleDelete(row) {
        this.tableData1 = this.tableData1.filter((item) => item.id !== row.id)
      },
      changeTableData1(tableList) {
        this.tableData1 = tableList
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(126, this.editId)
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
