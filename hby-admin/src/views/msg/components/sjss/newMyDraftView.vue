<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="200px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="底稿编号" prop="draftNumber">
            <el-input
              v-model="formData.draftNumber"
              clearable
              placeholder="请输入底稿编号"
              :style="{ width: '100%' }"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="底稿名称" prop="draftName">
            <el-input
              v-model="formData.draftName"
              clearable
              placeholder="请输入底稿名称"
              :style="{ width: '100%' }"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位名称" prop="auditeeName">
            <el-input
              v-model="formData.auditeeName"
              clearable
              placeholder="请选择被审计单位名称"
              :style="{ width: '206px' }"
              disabled
            />
            <el-button
              style="margin-left: 10px"
              type="primary"
              @click="$refs.department.show()"
              size="small"
              disabled
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计项目名称"
            prop="projectName"
            style="height: 32px"
          >
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入审计项目名称"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="专业科室人员" prop="thedeptstaffname">
            <el-input
              v-model="formData.thedeptstaffname"
              clearable
              placeholder="请输入专业科室人员"
              readonly
              :style="{ width: '206px' }"
            />
            <el-button
              @click="
                projectManagetype = 'thedeptstaffname'
                $refs['manage'].showEdit()
              "
              style="margin-left: 10px"
              type="primary"
              :disabled="footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否是问题底稿" prop="probleMdraft">
            <el-select
              v-model="formData.probleMdraft"
              clearable
              style="width: 100%"
              :disabled="footer"
            >
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否是汇总底稿标识" prop="summaryDraftMark">
            <el-select
              v-model="formData.summaryDraftMark"
              clearable
              style="width: 100%"
              :disabled="footer"
            >
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计事项" prop="auditMatters">
            <el-input
              v-model="formData.auditMatters"
              clearable
              placeholder="请输入审计事项"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="复核人" prop="fhstaffname">
            <el-input
              v-model="formData.fhstaffname"
              clearable
              placeholder="请输入复核人"
              readonly
              :style="{ width: '206px' }"
            />
            <el-button
              @click="
                projectManagetype = 'fhstaffname'
                $refs['manage'].showEdit()
              "
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="审计查证事实" prop="verificationDescription">
            <el-input
              v-model="formData.verificationDescription"
              placeholder="请输入审计查证事实"
              type="textarea"
              :rows="8"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计结论及依据" prop="auditConclusion">
            <el-input
              v-model="formData.auditConclusion"
              placeholder="请输入审计结论及依据"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计处理意见及建议" prop="handlingOpinions">
            <el-input
              v-model="formData.handlingOpinions"
              placeholder="请输入审计处理意见及建议"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              v-model="formData.remarks"
              placeholder="请输入备注"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
      </el-form>
      <!-- <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px" v-if="isWfqdedit">
          <el-button type="success" @click="handleAdd">增加一行</el-button>
        </div>
        <el-table
          border
          :data="formData.auditMyManuVerifyEntityList"
          :disabled="!footer"
        >
          <el-table-column
            align="center"
            label="审计查证事实"
            prop="verificationDescription"
          >
            <template slot-scope="{ row }">
              <el-input
                type="textarea"
                :rows="4"
                v-model="row.verificationDescription"
                size="mini"
                style="width: 90%"
              />
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="审计结论及依据"
            prop="auditConclusion"
          >
            <template slot-scope="{ row }">
              <el-input
                type="textarea"
                :rows="4"
                v-model="row.auditConclusion"
                size="mini"
                style="width: 90%"
              />
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="审计处理意见及建议"
            prop="handlingOpinions"
          >
            <template slot-scope="{ row }">
              <el-input
                type="textarea"
                :rows="4"
                v-model="row.handlingOpinions"
                size="mini"
                style="width: 90%"
              />
            </template>
          </el-table-column>

          <el-table-column align="center" label="备注" prop="remarks">
            <template slot-scope="{ row }">
              <el-input
                type="textarea"
                :rows="4"
                v-model="row.remarks"
                size="mini"
                style="width: 90%"
              />
            </template>
          </el-table-column>

          <el-table-column align="center" label="操作" v-if="!footer">
            <template slot-scope="{ row, $index }">
              <el-button type="text" @click="removeItem(row, $index)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col> -->
      <el-col :span="24">
        <el-form
          label-width="200px"
          :model="formData"
          :rules="rules"
          size="mini"
          style="margin-top: 20px"
        >
          <el-col :span="24">
            <el-form-item label="基础工作" prop="basicWork">
              <el-input
                type="textarea"
                :rows="6"
                v-model="formData.basicWork"
                clearable
                placeholder="请输入基础工作"
                :disabled="!isApprover && footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="审计证据是否准确、真实、合法"
              prop="evidenceAccurate"
            >
              <el-input
                type="textarea"
                :rows="6"
                v-model="formData.evidenceAccurate"
                clearable
                :disabled="!isApprover && footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="审计查证事实描述是否详尽、充分"
              prop="dgverificationDescription"
            >
              <el-input
                type="textarea"
                :rows="6"
                v-model="formData.dgverificationDescription"
                clearable
                :disabled="!isApprover && footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="审计结论是否客观、公正"
              prop="dgauditConclusion"
            >
              <el-input
                type="textarea"
                :rows="6"
                v-model="formData.dgauditConclusion"
                clearable
                :disabled="!isApprover && footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="审计处理意见及建议是否正确、具有可操作性和建设性"
              prop="dghandlingOpinions"
            >
              <el-input
                type="textarea"
                :rows="6"
                v-model="formData.dghandlingOpinions"
                clearable
                :disabled="!isApprover && footer"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-col>
      <!-- 审计工作记录 -->
      <el-col :span="24">
        <el-divider>审计工作记录</el-divider>
      </el-col>
      <el-col :span="24" style="margin-bottom: 20px">
        <div style="text-align: right; margin-bottom: 5px" v-if="!footer">
          <el-button type="success" @click="handleAddWork">新增</el-button>
        </div>
        <el-table :data="workList">
          <el-table-column align="center" label="索引号" prop="indexno">
            <template #default="{ row }">
              <el-button type="text" @click="handleDetailWork(row)">
                {{ row.indexno }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="被审计单位名称"
            prop="auditeeName"
          />
          <el-table-column
            align="center"
            label="实施审计时间"
            prop="implementationTime"
          />
          <el-table-column
            align="center"
            label="审计项目名称"
            prop="projectName"
          />
          <el-table-column
            align="center"
            label="审计内容和目标"
            prop="contentObjectives"
          />
          <el-table-column
            align="center"
            label="执行的审计程序和工作过程"
            prop="executedProceduresProcesses"
          />
          <el-table-column
            align="center"
            label="发现的疑点、线索及查证情况"
            prop="verificationSituation"
          />
          <el-table-column
            align="center"
            label="审计线索及数据来源"
            prop="cluesSources"
          />
          <el-table-column
            align="center"
            label="操作"
            width="120"
            v-if="!footer"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDeleteWork(row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="24">
        <el-divider>文件上传</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-top: 5px" v-if="!footer">
          <!-- <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api"
            :headers="headers"
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
              <el-button type="text" @click="handleDelete(row)" v-if="!footer">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

    <div style="text-align: right; margin-top: 10px">
      <el-button
        type="primary"
        @click="handleSubmit"
        v-if="isApprover || isWfqdedit || isFQRFH"
      >
        确 定
      </el-button>
      <el-button type="primary" @click="ymsubmit" v-if="!footer">
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
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />

    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
      :multiple="false"
    ></project-manage>
    <!-- 审计工作记录组件 -->
    <SJGZJLview ref="sjgzjlView"></SJGZJLview>
    <workList ref="workList" @fetch="addWork" />
  </div>
</template>

<script>
  import {
    getDetail,
    addOrUpdate,
    handleDeleteSub,
    delWorkRecordRela,
  } from '@/oapi/audit/newMyDraft'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import store from '@/store'
  import projectManage from '@/components/selectPerson.vue'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  import SJGZJLview from '@/views/oilAudit/implement/components/sjgzjlView.vue'
  import workList from '@/views/oilAudit/implement/components/workList'
  export default {
    components: {
      DepartmentOptions,
      Resubmit,
      projectManage,
      SJGZJLview,
      workList,
    },
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
        type: 'edit',
        loading: false,
        dialogFormVisible: false,
        formData: {
          auditeeName: '',
          auditeeNameId: '',
          projectName: '',
          auditMatters: '',
          basicWork: '',
          evidenceAccurate: '',
          verificationDescription: '',
          auditConclusion: '',
          handlingOpinions: '',
          dgverificationDescription: '',
          dgauditConclusion: '',
          dghandlingOpinions: '',
          probleMdraft: '',
          auditMyManuVerifyEntityList: [],
          summaryDraftMark: '',
          draftNumber: '',
          draftName: '',
          thedeptstaffname: '',
          thedeptstaffid: '',
          fhstaffname: '',
          fhstaffid: '',
          remarks: '',
        },
        tableData: [],
        id: '',
        workList: [],
        workList2: [],
        rules: {
          draftNumber: [
            {
              required: true,
              message: '底稿编号不能为空',
              trigger: 'change',
            },
          ],
          draftName: [
            {
              required: true,
              message: '底稿名称不能为空',
              trigger: 'change',
            },
          ],
          auditeeName: [
            {
              required: true,
              message: '被审计单位名称不能为空',
              trigger: 'change',
            },
          ],
          projectName: [
            {
              required: true,
              message: '审计项目名称不能为空',
              trigger: 'blur',
            },
          ],
          auditMatters: [
            {
              required: true,
              message: '审计事项不能为空',
              trigger: 'blur',
            },
          ],
          // basicWork: [
          //   {
          //     required: true,
          //     message: '基础工作不能为空',
          //     trigger: 'blur',
          //   },
          // ],
          // evidenceAccurate: [
          //   {
          //     required: true,
          //     message: '审计证据是否准确、真实、合法不能为空',
          //     trigger: 'blur',
          //   },
          // ],
          // verificationDescription: [
          //   {
          //     required: true,
          //     message: '审计查证事实描述是否详尽、充分不能为空',
          //     trigger: 'blur',
          //   },
          // ],
          // auditConclusion: [
          //   {
          //     required: true,
          //     message: '审计结论是否客观、公正不能为空',
          //     trigger: 'blur',
          //   },
          // ],
          // handlingOpinions: [
          //   {
          //     required: true,
          //     message:
          //       '审计处理意见及建议是否正确、具有可操作性和建设性不能为空',
          //     trigger: 'blur',
          //   },
          // ],
          probleMdraft: [
            {
              required: true,
              message: '是否是问题底稿不能为空',
              trigger: 'blur',
            },
          ],
        },
        typeId: '', //从我的任务页面打开时需要
        templateId: '', //从我的任务页面打开时需要
        footer: false,
        isApprover: false, // 是否为'专业科室人员'
        isWfqdedit: false, // 我发起的
        // 流程相关
        fromId: '',
        fromIdcopy: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        projectManagetype: '',
        isFQRFH: false, // 是否为'发起人复核'
      }
    },
    methods: {
      // 审计工作记录相关方法
      addWork(val) {
        val.map((item) => {
          this.workList.push(item)
        })
      },
      handleAddWork() {
        this.$refs.workList.showEdit()
      },
      handleDetailWork(row) {
        this.$refs['sjgzjlView'].showEdit(row, 'detail')
      },
      handleDeleteWork(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          await delWorkRecordRela({
            workReprotId: row.id,
            myDraftId: this.fromId,
          })
          this.$message({ type: 'success', message: '删除成功!' })
          this.workList = this.workList.filter((item) => item.id != row.id)
        })
      },
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      //是否从我的任务页面打开,isFromMyTask:boolean
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        flowTaskInfo,
        isFQRFH // 是否为'发起人复核'
      ) {
        this.type = title ? 'edit' : 'detail'
        title === 'detail' ? (this.footer = true) : (this.footer = false)
        this.isApprover =
          flowTaskInfo &&
          (flowTaskInfo.thisStep === '专业科室人员' ||
            flowTaskInfo.thisStep === '专业科室')
        this.isWfqdedit = isWfqdedit
        this.isFQRFH = isFQRFH // 是否为'发起人复核'

        console.log(
          'this.isApprover',
          this.isApprover,
          flowTaskInfo,
          isWfqdedit,
          isFQRFH
        )

        // 流程相关
        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (formId) {
          const res = await getDetail({ id: formId })
          Object.assign(this.formData, res.data)
          this.tableData = res.data.attList
          // 获取审计工作记录
          this.workList = res.data.workList || []
          this.workList2 = JSON.parse(JSON.stringify(res.data.workList || []))
          // 取最后一条数据赋值
          const verifyList = res.data.auditMyManuVerifyEntityList || []
          const lastItem =
            verifyList.length > 0 ? verifyList[verifyList.length - 1] : null
          this.formData.verificationDescription = lastItem
            ? lastItem.verificationDescription
            : ''
          this.formData.auditConclusion = lastItem
            ? lastItem.auditConclusion
            : ''
          this.formData.handlingOpinions = lastItem
            ? lastItem.handlingOpinions
            : ''
          this.formData.remarks = lastItem ? lastItem.remarks : ''
        }
      },
      close() {
        this.$bus.$emit('updateMsg', 0)
        this.$refs.ruleForm.resetFields()
        this.formData.auditeeName = ''
        this.formData.auditeeNameId = ''
        this.formData.projectName = ''
        this.formData.auditMatters = ''
        this.formData.basicWork = ''
        this.formData.evidenceAccurate = ''
        this.formData.verificationDescription = ''
        this.formData.auditConclusion = ''
        this.formData.handlingOpinions = ''
        this.formData.remarks = ''
        this.formData.probleMdraft = ''
        this.formData.auditMyManuVerifyEntityList = []
        this.formData.summaryDraftMark = ''
        this.formData.draftNumber = ''
        this.formData.draftName = ''
        this.formData.thedeptstaffname = ''
        this.formData.thedeptstaffid = ''
        this.formData.fhstaffname = ''
        this.formData.fhstaffid = ''
        this.typeId = ''
        this.templateId = ''
        this.tableData = []
        this.workList = []
        this.workList2 = []
        this.dialogFormVisible = false
      },
      async handleSubmit() {
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            this.loading = true
            let param = Object.assign({}, this.formData)
            //从我的任务页面打开
            if (this.typeId && this.templateId) {
              param.typeId = this.typeId
              param.templateId = this.templateId
            }
            let attIds = []
            this.tableData.map((v) => {
              attIds.push(v.attid)
            })
            delete param.attList
            // 发起人复核时，type为1
            if (this.isFQRFH) {
              param.type = 1
            }
            // 检查是否有实际内容，如果都为空则传空数组
            const verifyItem = {
              verificationDescription: this.formData.verificationDescription,
              auditConclusion: this.formData.auditConclusion,
              handlingOpinions: this.formData.handlingOpinions,
              remarks: this.formData.remarks,
            }
            const hasContent = Object.values(verifyItem).some(
              (val) => val && val.trim()
            )
            param.auditMyManuVerifyEntityList = hasContent ? [verifyItem] : []
            addOrUpdate({ ...param, attIds: attIds.toString() })
              .then(() => {
                this.$baseMessage('保存成功', 'success')
                this.$emit('queryData')
              })
              .catch((res) => {
                this.$baseMessage(res.msg, 'error')
              })
              .finally(() => {
                this.loading = false
              })
          }
        })
      },
      // 添加点击按钮
      handleAdd() {
        this.formData.auditMyManuVerifyEntityList.push({
          verificationDescription: '',
          auditConclusion: '',
          handlingOpinions: '',
        })
      },
      removeItem(row, index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            if (row.id) {
              handleDeleteSub({ id: row.id }).then(() => {
                this.formData.auditMyManuVerifyEntityList.splice(index, 1)
                this.$message({
                  type: 'success',
                  message: '删除成功!',
                })
              })
            } else {
              this.formData.auditMyManuVerifyEntityList.splice(index, 1)
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      handleDepartmentSelected(node) {
        //保存名称和对应的ID
        this.$set(this.formData, `auditeeName`, node.label)
        this.$set(this.formData, `auditeeNameId`, node.id)
      },
      async getChildlistPro(val) {
        const ids = val.map((res) => res.staffid)
        const names = val.map((res) => res.realname)
        if (this.projectManagetype == 'thedeptstaffname') {
          this.$set(this.formData, 'thedeptstaffid', ids.toString())
          this.$set(this.formData, 'thedeptstaffname', names.toString())
        } else {
          this.$set(this.formData, 'fhstaffid', ids.toString())
          this.$set(this.formData, 'fhstaffname', names.toString())
        }
      },
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
