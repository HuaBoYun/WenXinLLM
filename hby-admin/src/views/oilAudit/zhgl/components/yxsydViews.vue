<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12" style="height: 29px">
          <el-form-item label="用印名称" prop="sealName">
            <el-select
              v-model="formData.sealName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请选择用印名称"
            >
              <el-option value="审计部公章">审计部公章</el-option>
              <el-option value="审计专用章">审计专用章</el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用印事由" prop="sealReasons">
            <el-input
              v-model="formData.sealReasons"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入用印事由"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="用印枚数" prop="sealNum">
            <el-input
              v-model="formData.sealNum"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入用印枚数"
              type="number"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用印部门" prop="sealWorkUnitName">
            <el-input
              v-model="formData.sealWorkUnitName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择用印单位"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="showDept('dept')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办人" prop="transactorName">
            <el-input
              v-model="formData.transactorName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择经办人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审批部门" prop="auditWorkUnitName">
            <el-input
              v-model="formData.auditWorkUnitName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择审批部门"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="showDept('applyDept')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发往单位" prop="sendorg">
            <el-input
              v-model="formData.sendorg"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入发往单位"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
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
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">
        确定
      </el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
    <!-- 选择人员弹窗 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 选择部门弹窗 -->
    <department-options ref="department" @submit="handleDepartmentSelected" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import { download } from '@/oapi/audit/report'
  import { deleteFileInfo } from '@/oapi/ypns_zhgl/filePublic'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/yxsyd'
  import DepartmentOptions from '@/views/oilAudit/jhlx/components/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'

  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: {
      DepartmentOptions,
      ExecutorOptions,
      ProcessList,
    },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
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
        tableData: [],
        formData: {
          id: '',
          sealName: '',
          sealReasons: '',
          sealNum: '',
          sealWorkUnit: '',
          sealWorkUnitName: '',
          transactor: '',
          transactorName: '',
          auditWorkUnit: '',
          auditWorkUnitName: '',
          fileIds: '',
          remark: '',
          sendorg: '',
        },
        deptType: '',
        footer: true,
        rules: {
          sealName: [
            {
              required: true,
              message: '请输入用印名称',
              trigger: 'blur',
            },
          ],
          sealReasons: [
            {
              required: true,
              message: '请输入用印事由',
              trigger: 'blur',
            },
          ],
          sealNum: [
            {
              required: true,
              message: '请输入用印枚数',
              trigger: 'blur',
            },
          ],
          sealWorkUnitName: [
            {
              required: true,
              message: '请选择用印单位',
              trigger: ['blur', 'change'],
            },
          ],
          transactorName: [
            {
              required: true,
              message: '请选择经办人',
              trigger: ['blur', 'change'],
            },
          ],
          auditWorkUnitName: [
            {
              required: true,
              message: '请选择审批部门',
              trigger: ['blur', 'change'],
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        editId: '',
      }
    },
    methods: {
      // 点击唤起部门弹窗
      showDept(type) {
        this.deptType = type
        this.$refs.department.showEdit()
      },
      // 选择经办人
      handleExecutorSelected(node) {
        this.$set(this.formData, 'transactorName', node[0].realname)
        this.$set(this.formData, 'transactor', node[0].staffid)
        this.$forceUpdate()
      },
      // 选择部门
      handleDepartmentSelected(node) {
        if (this.deptType === 'dept') {
          this.$set(this.formData, `sealWorkUnitName`, node.label)
          this.$set(this.formData, `sealWorkUnit`, node.id)
        } else if (this.deptType === 'applyDept') {
          this.$set(this.formData, `auditWorkUnitName`, node.label)
          this.$set(this.formData, `auditWorkUnit`, node.id)
        }
        this.$forceUpdate()
      },
      // 附件列表 下载附件
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
      // 附件列表 删除附件
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        await deleteFileInfo({ id: row.attid })
        this.tableData = list
      },
      // handlePreview(file) {},
      // // 附件上传成功
      // handleSuccess(file) {
      //   if (file.result == 200) {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          this.editId = row.id
          const res = await getInfoDetail({ id: row.id })
          this.formData.sealName = res.data.data.sealName
          this.formData.sealReasons = res.data.data.sealReasons
          this.formData.sealNum = res.data.data.sealNum
          this.formData.sealWorkUnit = res.data.data.sealWorkUnit
          this.formData.sealWorkUnitName = res.data.data.sealWorkUnitName
          this.formData.transactor = res.data.data.transactor
          this.formData.transactorName = res.data.data.transactorName
          this.formData.auditWorkUnit = res.data.data.auditWorkUnit
          this.formData.auditWorkUnitName = res.data.data.auditWorkUnitName
          this.formData.fileIds = res.data.data.fileIds
          this.formData.remark = res.data.data.remark
          this.formData.sendorg = res.data.data.sendorg
          this.formData.id = res.data.data.id
          this.tableData = res.data.file || []
          // this.formData = JSON.parse(JSON.stringify(row))
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.transactorName = resL.realname
          this.formData.transactor = resL.staffid
          this.formData = {
            ...this.formData,
            createdUser: resL.realname,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          id: '',
          sealName: '',
          sealReasons: '',
          sealNum: '',
          sealWorkUnit: '',
          sealWorkUnitName: '',
          transactor: '',
          transactorName: '',
          auditWorkUnit: '',
          auditWorkUnitName: '',
          fileIds: '',
          remark: '',
          sendorg: '',
        }
        this.editId = ''
        this.tableData = []
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            if (this.formData.sendorg && this.tableData.length == 0) {
              this.$message({
                message: '请上传附件',
                type: 'error',
              })
              return
            }
            this.loading = true
            const params = JSON.parse(JSON.stringify(this.formData))
            let fileIds = ''
            this.tableData.map((item) => {
              fileIds += item.attid
              fileIds += ','
            })
            fileIds = fileIds.substring(0, fileIds.length - 1)
            const res = await editInfor({
              ...params,
              fileIds,
            })
            if (res && res.code == 200) {
              this.editId = res.data.id
              this.formData.id = res.data.id
              this.$message({
                message: '成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '失败',
                type: 'error',
              })
            }
            this.loading = false
          }
        })
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(38, this.editId)
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
