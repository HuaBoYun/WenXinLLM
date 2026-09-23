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
        <el-col :span="24">
          <el-form-item label="申请单位" prop="applyBelongGroupName">
            <el-input
              v-model="formData.applyBelongGroupName"
              :style="{ width: '90%' }"
              clearable
              disabled
              placeholder="请选择申请单位"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="chooseDept('onUse')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="支出原因" prop="reason">
            <el-input
              v-model="formData.reason"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入支出原因"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="支出明细" prop="details">
            <el-input
              v-model="formData.details"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入支出明细"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="预计支出金额">
            <el-input
              v-model="formData.amount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入预计支出金额"
              @input="inputMoney($event, 'amount')"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="实际支出金额" prop="actamount">
            <el-input
              v-model="formData.actamount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入实际支出金额"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="时间" prop="officeExpensesTime">
            <el-date-picker
              v-model="formData.officeExpensesTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="供货单位">
            <el-input
              v-model="formData.supplyBelongGroupName"
              clearable
              placeholder="请选择供货单位"
            />
            <!-- <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.audiTree.showEdit()"
            >
              选择
            </el-button> -->
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
              @click="showType('transactor')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="科室负责人" prop="departmentHeadName">
            <el-input
              v-model="formData.departmentHeadName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择科室负责人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="showType('dept')"
            >
              选择
            </el-button>
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
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <!-- 选择单位 -->
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/bgjfzc'
  import { deleteFileInfo } from '@/oapi/ypns_zhgl/filePublic'
  import { download } from '@/api/audit/implement'
  import SelectDepartment from '../../jhlx/components/department.vue'
  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: {
      DepartmentOptions,
      ExecutorOptions,
      SelectDepartment,
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
          reason: '',
          details: '',
          amount: '',
          officeExpensesTime: '',
          supplyBelongGroup: '',
          supplyBelongGroupName: '',
          transactor: '',
          transactorName: '',
          departmentHead: '',
          departmentHeadName: '',
          fileIds: '',
          applyBelongGroup: '',
          applyBelongGroupName: '',
        },
        peopleType: '',
        radio: '',
        footer: true,
        rules: {
          applyBelongGroupName: [
            {
              required: true,
              message: '请选择申请单位',
              trigger: 'blur',
            },
          ],
          reason: [
            {
              required: true,
              message: '请输入支出原因',
              trigger: 'blur',
            },
          ],
          details: [
            {
              required: true,
              message: '请输入支出明细',
              trigger: 'blur',
            },
          ],
          amount: [
            {
              required: true,
              message: '请输入支出金额',
              trigger: 'blur',
            },
          ],
          officeExpensesTime: [
            {
              required: true,
              message: '请选择时间',
              trigger: ['blur', 'change'],
            },
          ],
          supplyBelongGroupName: [
            {
              required: true,
              message: '请选择供货单位',
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
          departmentHeadName: [
            {
              required: true,
              message: '请选择科室负责人',
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
      //金额输入
      inputMoney(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        if (
          sanitizedValue.startsWith('0') &&
          sanitizedValue.length > 1 &&
          sanitizedValue[1] !== '.'
        ) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        if (sanitizedValue === '' || sanitizedValue === '0') {
          sanitizedValue = ''
        }
        // 更新输入框的值
        const keys = key.split('.')
        let formDataRef = this.formData
        for (let i = 0; i < keys.length - 1; i++) {
          formDataRef = formDataRef[keys[i]]
        }
        formDataRef[keys[keys.length - 1]] = sanitizedValue
      },
      // 选择单位
      getDepartmentInfo(node) {
        this.$set(this.formData, 'applyBelongGroupName', node.label)
        this.$set(this.formData, 'applyBelongGroup', node.id)
      },
      showType(type) {
        this.peopleType = type
        this.$refs.executor.showEdit()
      },
      // 选择经办人
      handleExecutorSelected(node) {
        if (this.peopleType === 'dept') {
          this.$set(this.formData, 'departmentHeadName', node[0].realname)
          this.$set(this.formData, 'departmentHead', node[0].staffid)
        } else if (this.peopleType === 'transactor') {
          this.$set(this.formData, 'transactorName', node[0].realname)
          this.$set(this.formData, 'transactor', node[0].staffid)
        }
        this.$forceUpdate()
      },
      // 选择部门
      handleDepartmentSelected(node) {
        this.$set(this.formData, `supplyBelongGroupName`, node.name)
        this.$set(this.formData, `supplyBelongGroup`, node.id)
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
      // 附件上传成功
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
          this.formData.reason = res.data.data.reason
          this.formData.details = res.data.data.details
          this.formData.amount = res.data.data.amount
          this.formData.officeExpensesTime = res.data.data.officeExpensesTime
          this.formData.supplyBelongGroup = res.data.data.supplyBelongGroup
          this.formData.supplyBelongGroupName =
            res.data.data.supplyBelongGroupName
          this.formData.transactor = res.data.data.transactor
          this.formData.transactorName = res.data.data.transactorName
          this.formData.departmentHead = res.data.data.departmentHead
          this.formData.departmentHeadName = res.data.data.departmentHeadName
          this.formData.applyBelongGroup = res.data.data.applyBelongGroup
          this.formData.applyBelongGroupName =
            res.data.data.applyBelongGroupName
          this.formData.fileIds = res.data.data.fileIds
          this.formData.actamount = res.data.data.actamount
          this.formData.id = res.data.data.id
          this.tableData = res.data?.file || []
          // this.formData = JSON.parse(JSON.stringify(row))
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let info = JSON.parse(localStorage.getItem('userInfo'))
          console.log(info)
          this.formData = {
            ...this.formData,
            transactorName: info.realname,
            transactor: info.staffid,
            applyBelongGroupName: info.linkOrg.orgname,
            applyBelongGroup: info.linkOrg.orgid,
            // createdUser: resL,
            // createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          id: '',
          reason: '',
          details: '',
          amount: '',
          officeExpensesTime: '',
          supplyBelongGroup: '',
          supplyBelongGroupName: '',
          transactor: '',
          transactorName: '',
          departmentHead: '',
          departmentHeadName: '',
          fileIds: '',
          applyBelongGroup: '',
          applyBelongGroupName: '',
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
      chooseDept() {
        this.$refs.audiTree.showEdit()
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(40, this.editId)
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
