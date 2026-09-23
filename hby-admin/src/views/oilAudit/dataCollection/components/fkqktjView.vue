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
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="计划文号" prop="planId">
            <el-input
              v-model="formData.planId"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入计划文号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractNo">
            <el-input
              v-model="formData.contractNo"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入合同编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="施工单位" prop="orgName">
            <el-input
              v-model="formData.orgName"
              clearable
              placeholder="请选择施工单位"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('orgName')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额（元）" prop="contractAmount">
            <el-input
              v-model="formData.contractAmount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入合同金额（元）"
              @input="inputMoney($event, 'contractAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="结算金额（元）" prop="settlementAmount">
            <el-input
              v-model="formData.settlementAmount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入结算金额（元）"
              @input="inputMoney($event, 'settlementAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="甲供物资金额（元）" prop="materialAmount">
            <el-input
              v-model="formData.materialAmount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入甲供物资金额（元）"
              @input="inputMoney($event, 'materialAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="已付款总额（元）" prop="paidAmount">
            <el-input
              v-model="formData.paidAmount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入已付款总额（元）"
              @input="inputMoney($event, 'paidAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="尾款（元）" prop="balancePayment">
            <el-input
              v-model="formData.balancePayment"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入尾款（元）"
              @input="inputMoney($event, 'balancePayment')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="一审报审、结束时间" prop="firstTrialTime">
            <el-date-picker
              v-model="formData.firstTrialTime"
              placeholder="请输入一审报审、结束时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="一审报审金额" prop="firstTrialAmount">
            <el-input
              v-model="formData.firstTrialAmount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入一审报审金额"
              @input="inputMoney($event, 'firstTrialAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="二审报审、结束时间" prop="secondTrialTime">
            <el-date-picker
              v-model="formData.secondTrialTime"
              placeholder="请输入二审报审、结束时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="二审报审金额" prop="secondTrialAmount">
            <el-input
              v-model="formData.secondTrialAmount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入二审报审金额"
              @input="inputMoney($event, 'secondTrialAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="施工图纸设计时间" prop="drawingDesignTime">
            <el-date-picker
              v-model="formData.drawingDesignTime"
              placeholder="请输入施工图纸设计时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同开、竣工时间" prop="dateS1">
            <el-date-picker
              v-model="formData.dateS1"
              clearable
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
              @input="$forceUpdate()"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="实际开、竣工时间" prop="dateS2">
            <el-date-picker
              v-model="formData.dateS2"
              clearable
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
              @input="$forceUpdate()"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="延期次数" prop="delayTimes">
            <el-input
              v-model="formData.delayTimes"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入延期次数"
              @input="inputMoney($event, 'delayTimes')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="延期天数" prop="delayDays">
            <el-input
              v-model="formData.delayDays"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入延期天数"
              @input="inputMoney($event, 'delayDays')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="延期原因" prop="delayReason">
            <el-input
              v-model="formData.delayReason"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入延期原因"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="竣工资料存档时间" prop="archiveTime">
            <el-date-picker
              v-model="formData.archiveTime"
              placeholder="请输入竣工资料存档时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="招标情况（招标方式）" prop="biddingSituation">
            <el-input
              v-model="formData.biddingSituation"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入招标情况（招标方式）"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="合同施工内容与计划内容是否一致"
            prop="isConsistentConPlan"
          >
            <el-select
              v-model="formData.isConsistentConPlan"
              placeholder="请输入合同施工内容与计划内容是否一致"
              :style="{ width: '100%' }"
            >
              <el-option label="一致" value="1"></el-option>
              <el-option label="不一致" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="合同施工内容与实际施工是否一致"
            prop="isConsistentConWork"
          >
            <el-select
              v-model="formData.isConsistentConWork"
              placeholder="请输入合同施工内容与实际施工是否一致"
              :style="{ width: '100%' }"
            >
              <el-option label="一致" value="1"></el-option>
              <el-option label="不一致" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入备注"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
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
      <el-button @click="save" type="primary">确定</el-button>
    </div>

    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import {
    designPlanInfoDetail,
    designPlanInfoSaveOrUpdate,
  } from '@/oapi/audit/information'
  import { download, deleteFile } from '@/oapi/audit/report'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { DepartmentOptions },
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
          orgName: '',
          dateS1: [],
          dateS2: [],
          planId: '',
          contractNo: '',
          contractAmount: '',
          settlementAmount: '',
          materialAmount: '',
          paidAmount: '',
          balancePayment: '',
          firstTrialTime: '',
          firstTrialAmount: '',
          secondTrialTime: '',
          secondTrialAmount: '',
          drawingDesignTime: '',
          dateS1: '',
          dateS2: '',
          delayTimes: '',
          delayDays: '',
          delayTimes: '',
          delayReason: '',
          archiveTime: '',
          biddingSituation: '',
          isConsistentConPlan: '',
          isConsistentConWork: '',
          remark: '',
        },
        footer: true,
        rules: {
          planId: [
            {
              required: true,
              message: '请输入计划文号',
              trigger: 'blur',
            },
          ],
          contractNo: [
            {
              required: true,
              message: '请输入合同编号',
              trigger: 'blur',
            },
          ],
          orgId: [
            {
              required: true,
              message: '请选择施工单位',
              trigger: 'blur',
            },
          ],
          contractAmount: [
            {
              required: true,
              message: '请输入合同金额',
              trigger: 'blur',
            },
          ],
          settlementAmount: [
            {
              required: true,
              message: '请输入结算金额',
              trigger: 'blur',
            },
          ],
          materialAmount: [
            {
              required: true,
              message: '请输入甲供物资金额',
              trigger: 'blur',
            },
          ],
          paidAmount: [
            {
              required: true,
              message: '请输入已付款总额',
              trigger: 'blur',
            },
          ],
          balancePayment: [
            {
              required: true,
              message: '请输入尾款',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
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
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      openDep(type) {
        this.$refs.department.show({
          labelKey: 'orgName',
          idKey: 'orgId',
          title: '单位',
        })
      },
      handleDepartmentSelected(node) {
        this.$set(this.formData, 'orgName', node.label)
        this.$set(this.formData, `orgId`, node.id)
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await designPlanInfoDetail({ id: row.id })
          // this.formData.createType = res.data.createType
          // this.formData.dataBaseConnectionAddress =
          //   res.data.dataBaseConnectionAddress
          // this.formData.dataBaseOwnership = res.data.dataBaseOwnership
          // this.formData.dataBasePassWord = res.data.dataBasePassWord
          Object.assign(this.formData, res.data.data)

          this.tableData = res.data.data.attachments || []
          this.formData.orgName = res.data.data.org.orgname
          this.formData.orgId = res.data.data.org.orgid
          this.formData.dateS1 = [
            res.data.data.contractStartTime || '',
            res.data.data.contractEndTime || '',
          ]
          this.formData.dateS2 = [
            res.data.data.workStartTime || '',
            res.data.data.workEndTime || '',
          ]
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          id: '',
          orgName: '',
          dateS1: [],
          dateS2: [],
          planId: '',
          contractNo: '',
          contractAmount: '',
          settlementAmount: '',
          materialAmount: '',
          paidAmount: '',
          balancePayment: '',
          firstTrialTime: '',
          firstTrialAmount: '',
          secondTrialTime: '',
          secondTrialAmount: '',
          drawingDesignTime: '',
          dateS1: '',
          dateS2: '',
          delayTimes: '',
          delayDays: '',
          delayTimes: '',
          delayReason: '',
          archiveTime: '',
          biddingSituation: '',
          isConsistentConPlan: '',
          isConsistentConWork: '',
          remark: '',
        }
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
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
            params.contractStartTime = params.dateS1[0] || ''
            params.contractEndTime = params.dateS1[1] || ''
            params.workStartTime = params.dateS2[0] || ''
            params.workEndTime = params.dateS2[1] || ''
            delete params.attachments
            delete params.dateS1
            delete params.dateS2
            delete params.org
            const data = await designPlanInfoSaveOrUpdate({
              ...params,
              attIds,
            })
            if (data.code == 1) {
              this.$baseMessage(data.msg, 'success')
              this.$emit('fetchData')
              this.close()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
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
