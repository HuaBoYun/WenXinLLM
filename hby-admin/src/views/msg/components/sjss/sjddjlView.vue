<template>
  <div>
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
          <el-form-item label="序号" prop="sxnumber">
            <el-input
              v-model="formData.sxnumber"
              clearable
              placeholder="请输入序号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入项目名称"
              style="width: 78%"
              disabled
            />
            <el-button
              :disabled="false"
              @click="toProjectDetails"
              style="margin-left: 15px"
              type="primary"
            >
              详情
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主审" prop="chiefAuditor">
            <el-input
              v-model="formData.chiefAuditor"
              clearable
              placeholder="请选择主审"
              style="width: 78%"
              disabled
            />
            <el-button
              @click="projectManager('chiefAuditor')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组长" prop="assessors">
            <el-input
              v-model="formData.assessors"
              clearable
              placeholder="请选择组长"
              style="width: 78%"
              disabled
            />
            <el-button
              @click="$refs['manage1'].showEdit()"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="经责科负责人"
            prop="economicName"
            style="height: 29px"
          >
            <el-input
              v-model="formData.economicName"
              clearable
              placeholder="请选择经责科负责人"
              style="width: 78%"
              disabled
            />
            <el-button
              @click="projectManager('economicName')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="督导日期" prop="supervisionDate">
            <el-date-picker
              v-model="formData.supervisionDate"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="督导方式" prop="supervision">
            <el-input
              v-model="formData.supervision"
              clearable
              placeholder="请输入督导方式"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参与督导人员" prop="supervisionParticipants">
            <el-input
              v-model="formData.supervisionParticipants"
              clearable
              placeholder="请选择人员"
              style="width: 78%"
              disabled
            />
            <el-button
              @click="projectManager('supervisionParticipants')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位" prop="orgName" style="height: 29px">
            <el-input
              v-model="formData.orgName"
              clearable
              placeholder="请选择单位"
              :style="{ width: '78%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '15px' }"
              type="primary"
              @click="$refs.department.show()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划时间安排" prop="plannedTime">
            <el-date-picker
              v-model="formData.plannedTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计人员" prop="staffName">
            <el-input
              v-model="formData.staffName"
              clearable
              placeholder="请选择人员"
              style="width: 78%"
              disabled
            />
            <el-button
              @click="projectManager('staffName')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 29px">
          <el-form-item label="审计组是否采纳" prop="isAdopt">
            <el-select v-model="formData.isAdopt" style="width: 100%">
              <el-option label="是" value="是"></el-option>
              <el-option label="否" value="否"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计任务执行情况" prop="executionSituation">
            <el-input
              v-model="formData.executionSituation"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入现场情况"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计组发现问题" prop="discoverProblems">
            <el-input
              v-model="formData.discoverProblems"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入现场情况"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="实施审计方案中审计内容及重点"
            prop="auditContentAndFocus"
          >
            <el-input
              v-model="formData.auditContentAndFocus"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入现场情况"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="现场情况" prop="onsiteCondition">
            <el-input
              v-model="formData.onsiteCondition"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入现场情况"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="存在问题及需协调解决的问题"
            prop="issuesAndCoordination"
          >
            <el-input
              v-model="formData.issuesAndCoordination"
              :style="{ width: '100%' }"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入存在问题及需协调解决的问题"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="督导意见" prop="supervisionOpinions">
            <el-input
              v-model="formData.supervisionOpinions"
              :style="{ width: '100%' }"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入督导意见"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="未采纳原因或采纳结果" prop="isAdoptResult">
            <el-input
              v-model="formData.isAdoptResult"
              :style="{ width: '100%' }"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入督导意见"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              v-model="formData.remarks"
              :style="{ width: '100%' }"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入备注"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              disabled
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-col :span="24">
      <el-divider>文件上传</el-divider>
    </el-col>
    <el-col :span="24">
      <div style="text-align: right; margin-bottom: 5px" v-if="footer">
        <!-- <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseURL + api"
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
          <div v-if="!disabled" style="margin-right: 10px">
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
            <el-button type="text" @click="handleDelete(row)" v-if="footer">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button type="primary" @click="ymsubmit" :disabled="btnLoading">
        提 交
      </el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />

    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div> -->
    <!-- 项目详细 -->
    <newsjddjlViewDetails ref="sc" />
    <guideDetails ref="guideDetails" />
    <!-- 人员 -->
    <project-manage @projectManage="getChildlistPro" ref="manage" />
    <project-manage1 @projectManage="getChildlistPro1" ref="manage1" />
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import projectManage from '@/components/selectPerson.vue'
  import projectManage1 from '@/components/danxuanPerson.vue'
  import {
    overseeRecordsDetail,
    overseeRecordsSaveOrUpdate,
  } from '@/oapi/audit/implement'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { deleteFile, download } from '@/oapi/audit/report'
  import DepartmentOptions from '@/views/audit/report/components/options/department.vue'
  const token = store.getters['user/token']
  import { getCurrSsProject } from '@/oapi/audit/project'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: {
      projectManage,
      projectManage1,
      Resubmit,
      DepartmentOptions,
      newsjddjlViewDetails: () =>
        import(
          '@/views/oilAudit/implement/components/newsjddjlViewDetails.vue'
        ),
      guideDetails: () =>
        import('@/views/oilAudit/implement/components/guideDetails.vue'),
    },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        // baseURL: baseURL,
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
          projectName: '',
          chiefAuditor: '',
          supervisionParticipants: '',
          supervisionDate: '',
          onsiteCondition: '',
          issuesAndCoordination: '',
          supervisionOpinions: '',
          remarks: '',
          createUser: '',
          createTime: '',
          auditContentAndFocus: '',
          assessors: '',
          economicName: '',
          orgId: '',
          orgName: '',
          plannedTime: '',
          auditStaffId: '',
          executionSituation: '',
          discoverProblems: '',
          isAdopt: '',
          isAdoptResult: '',
          supervision: '',
          sxnumber: '',
        },
        footer: true,
        rules: {
          chiefAuditor: [
            {
              required: true,
              message: '请输入主审',
              trigger: 'blur',
            },
          ],
          supervisionParticipants: [
            {
              required: true,
              message: '请输入参与督导人员',
              trigger: 'blur',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请输入项目名称',
              trigger: 'blur',
            },
          ],
          supervisionDate: [
            {
              required: true,
              message: '请输入督导日期',
              trigger: 'blur',
            },
          ],
          onsiteCondition: [
            {
              required: true,
              message: '请输入现场情况',
              trigger: 'blur',
            },
          ],
          issuesAndCoordination: [
            {
              required: true,
              message: '请输入存在问题及需协调解决的问题',
              trigger: 'blur',
            },
          ],
          supervisionOpinions: [
            {
              required: true,
              message: '请输入督导意见',
              trigger: 'blur',
            },
          ],
          // remarks: [
          //   {
          //     required: true,
          //     message: '请输入备注',
          //     trigger: 'blur',
          //   },
          // ],
        },
        dialogFormVisible: false,
        title: '新增',
        proType: '',
        currentProject: '',
        // 流程相关
        fromId: '',
        fromIdcopy: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        tableData: [],
        btnLoading: false,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async ymsubmit() {
        try {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      projectManager(type) {
        this.proType = type
        this.$refs['manage'].showEdit()
      },
      getChildlistPro(val) {
        if (this.proType == 'staffName') {
          this.$set(this.formData, 'auditStaffId', val[0].staffid)
          this.$set(this.formData, 'staffName', val[0].realname)
        } else {
          this.$set(this.formData, this.proType, val[0].realname)
        }
      },
      getChildlistPro1(val) {
        this.$set(this.formData, 'assessorsId', val[0].staffid)
        this.$set(this.formData, 'assessors', val[0].realname)
      },
      toProjectDetails() {
        if (!this.currentProject) {
          return false
        }
        // 31 工程结算  32 建设工程
        if (this.currentProject.xmqd.gljhxmlx == '31') {
          this.$refs['sc'].showEdit(this.currentProject.xmqd)
        } else {
          this.$refs['guideDetails'].showEdit(this.currentProject, 'SJDD')
        }
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.dialogFormVisible = true
        // 流程相关
        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        // this.currentProject = await this.getCurrentProject()
        if (formId) {
          const { data } = await overseeRecordsDetail({ id: formId })
          Object.assign(this.formData, data.bean)
          this.currentProject = data.bean
          this.formData.auditStaffId = data.bean.tblStaff?.staffid
          this.formData.staffName = data.bean.tblStaff?.realname
          this.formData.orgId = data.bean.tblOrganization?.orgid
          this.formData.orgName = data.bean.tblOrganization?.orgname
          this.tableData = data.attlist
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData.createUser = resL
          this.formData.createTime = new Date().toJSON().split('T')[0]
        }
      },
      // 获取当前实施项目
      async getCurrentProject() {
        let obj = {}
        await getCurrSsProject().then((res) => {
          if (res.code === 1) {
            obj = res.data.pj
          } else {
            obj = undefined
          }
        })
        return obj
      },
      close() {
        this.formData = this.$options.data().formData
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
        this.$bus.$emit('updateMsg', 0)
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            delete this.formData.createUser
            delete this.formData.createTime
            delete this.formData.orgName
            delete this.formData.staffName
            delete this.formData.tblOrganization
            delete this.formData.tblStaff
            const data = await overseeRecordsSaveOrUpdate(
              { ...this.formData },
              attids
            )
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetchData')
              // this.close()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          }
        })
      },
      async checkLink() {
        return
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
              })
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      handleDepartmentSelected(node) {
        this.formData.orgId = node.id
        this.formData.orgName = node.name
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
  .el-form-item__contractname span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
