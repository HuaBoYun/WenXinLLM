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
        <el-col :span="12">
          <el-form-item label="计划文号" prop="planNo">
            <el-input
              v-model="formData.planNo"
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
              @input="inputMoney($event, 'contractNo')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工程名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入工程名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主要工程内容" prop="constructionContent">
            <el-input
              v-model="formData.constructionContent"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入主要工程内容"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
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
          <el-form-item label="甲供料金额（元）" prop="materialAmount">
            <el-input
              v-model="formData.materialAmount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入甲供料金额（元）"
              @input="inputMoney($event, 'materialAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="勘察单位" prop="surveyOrgName">
            <el-input
              v-model="formData.surveyOrgName"
              clearable
              placeholder="请输入勘察单位"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('surveyOrg')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="设计单位" prop="designOrgName">
            <el-input
              v-model="formData.designOrgName"
              clearable
              placeholder="请输入设计单位"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('designOrg')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="施工单位" prop="constructOrgName">
            <el-input
              v-model="formData.constructOrgName"
              clearable
              placeholder="请输入施工单位"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('constructOrg')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监理单位" prop="supervisionOrgName">
            <el-input
              v-model="formData.supervisionOrgName"
              clearable
              placeholder="请输入监理单位"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('supervisionOrg')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建设单位项目经理及电话" prop="buildManagerPhone">
            <el-input
              v-model="formData.buildManagerPhone"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入建设单位项目经理及电话"
              @input="inputMoney($event, 'buildManagerPhone')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="项目物资负责人及电话"
            prop="materialManagerPhone"
          >
            <el-input
              v-model="formData.materialManagerPhone"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入项目物资负责人及电话"
              @input="inputMoney($event, 'materialManagerPhone')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="施工单位项目经理及电话"
            prop="constructManagerPhone"
          >
            <el-input
              v-model="formData.constructManagerPhone"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入施工单位项目经理及电话"
              @input="inputMoney($event, 'constructManagerPhone')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="总监及电话" prop="managerPhone">
            <el-input
              v-model="formData.managerPhone"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入总监及电话"
              @input="inputMoney($event, 'managerPhone')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="设计负责人及电话" prop="designManagerPhone">
            <el-input
              v-model="formData.designManagerPhone"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入设计负责人及电话"
              @input="inputMoney($event, 'designManagerPhone')"
            />
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
      <el-button @click="save" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>

    <!-- 单位 -->
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import {
    balanceProjectDetail,
    balanceProjectSaveOrUpdate,
  } from '@/oapi/audit/information'
  import { download, deleteFile } from '@/oapi/audit/report'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']

  export default {
    components: { SelectDepartment },
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
          contractNo: '',
          planNo: '',
          projectName: '',
          constructionContent: '',
          settlementAmount: '',
          materialAmount: '',
          surveyOrgId: '',
          designOrgId: '',
          constructOrgId: '',
          supervisionOrgId: '',
          surveyOrgName: undefined,
          designOrgName: undefined,
          constructOrgName: undefined,
          supervisionOrgName: undefined,
          id: '',
          buildManagerPhone: '',
          materialManagerPhone: '',
          constructManagerPhone: '',
          managerPhone: '',
          designManagerPhone: '',
          remark: '',
        },
        footer: true,
        rules: {
          contractNo: [
            {
              required: true,
              message: '请输入合同编号',
              trigger: 'blur',
            },
          ],
          planNo: [
            {
              required: true,
              message: '请输入计划文号',
              trigger: 'blur',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请输入工程名称',
              trigger: 'blur',
            },
          ],
          constructionContent: [
            {
              required: true,
              message: '请输入主要工程内容',
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
              message: '请输入甲供料金额',
              trigger: 'blur',
            },
          ],
          surveyOrgId: [
            {
              required: true,
              message: '请输入勘察单位',
              trigger: 'blur',
            },
          ],
          designOrgId: [
            {
              required: true,
              message: '请输入设计单位',
              trigger: 'blur',
            },
          ],
          constructOrgId: [
            {
              required: true,
              message: '请输入施工单位',
              trigger: 'blur',
            },
          ],
          supervisionOrgId: [
            {
              required: true,
              message: '请输入监理单位',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        depType: '',
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
        this.depType = type
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.$set(this.formData, this.depType + 'Name', val.label)
        this.$set(this.formData, this.depType + 'Id', val.id)
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await balanceProjectDetail({ id: row.id })
          // this.formData = res.data.data
          Object.assign(this.formData, res.data.data)
          this.tableData = res.data.data.attachments || []
          this.formData.surveyOrgName = res.data.data.surveyOrg?.orgname || ''
          this.formData.designOrgName = res.data.data.designOrg?.orgname || ''
          this.formData.constructOrgName =
            res.data.data.constructOrg?.orgname || ''
          this.formData.supervisionOrgName =
            res.data.data.supervisionOrg?.orgname || ''
          this.formData.surveyOrgId = res.data.data.surveyOrg?.orgid || ''
          this.formData.designOrgId = res.data.data.designOrg?.orgid || ''
          this.formData.constructOrgId = res.data.data.constructOrg?.orgid || ''
          this.formData.supervisionOrgId =
            res.data.data.supervisionOrg?.orgid || ''
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
          contractNo: '',
          planNo: '',
          projectName: '',
          constructionContent: '',
          settlementAmount: '',
          materialAmount: '',
          surveyOrgId: '',
          designOrgId: '',
          constructOrgId: '',
          supervisionOrgId: '',
          surveyOrgName: undefined,
          designOrgName: undefined,
          constructOrgName: undefined,
          supervisionOrgName: undefined,
          id: '',
          buildManagerPhone: '',
          materialManagerPhone: '',
          constructManagerPhone: '',
          managerPhone: '',
          designManagerPhone: '',
          remark: '',
        }
        this.tableData = []
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)
            let params = { ...this.formData }
            delete params.attachments

            const data = await balanceProjectSaveOrUpdate({
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
            this.loading = false
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
