<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="问题来源" prop="issuesType">
            <el-select
              style="width: 100%"
              v-model="formData.issuesType"
              placeholder="选择业务类别"
              :disabled="true"
            >
              <el-option label="审计" :value="1" />
              <el-option label="内控" :value="2" />
              <el-option label="非系统实施" :value="3" />
              <el-option label="外部审计" :value="4" />
              <el-option label="风险" :value="5" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题编号" prop="issuesCode">
            <el-input
              v-model="formData.issuesCode"
              clearable
              placeholder="请输入业务编号"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目编号">
            <el-input
              v-model="formData.projectNo"
              clearable
              placeholder="请输入项目编号"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="问题名称" prop="issuesName">
            <el-input
              v-model="formData.issuesName"
              clearable
              placeholder="请输入问题名称"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计/评价对象" prop="auditObjectName">
            <el-input
              v-model="formData.auditObjectName"
              clearable
              placeholder="请选择被审计/评价对象"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="事项" prop="issuesItem">
            <el-input
              v-model="formData.issuesItem"
              clearable
              placeholder="请输入事项"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="经办人员" prop="createStaffName">
            <el-input
              v-model="formData.createStaffName"
              clearable
              placeholder="请输入经办人员"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任人" prop="responsiblePersonName">
            <el-input
              v-model="formData.responsiblePersonName"
              clearable
              placeholder="请选择责任人"
              style="width: 100%"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任部门" prop="responsibleDeptName">
            <el-input
              v-model="formData.responsibleDeptName"
              clearable
              placeholder="请选择责任部门"
              style="width: 100%"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              style="width: 100%"
              v-model="formData.createTime"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="问题标题" prop="issuesTitle">
            <el-input
              v-model="formData.issuesTitle"
              clearable
              placeholder="请输入问题标题"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="问题详情" prop="questionMemo">
            <el-input
              v-model="formData.questionMemo"
              clearable
              placeholder="请输入问题详情"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="意见及建议" prop="opinions">
            <el-input
              v-model="formData.opinions"
              clearable
              placeholder="请输入意见及建议"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="true"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="整改通知" prop="rectificationPlan">
            <el-input
              v-model="formData.rectificationPlan"
              clearable
              placeholder="请输入整改通知"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
              :disabled="!footer && title === '详细'"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="整改措施" prop="rectificationMeasures">
            <el-input
              v-model="formData.rectificationMeasures"
              clearable
              placeholder="请输入整改措施"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
              :disabled="!footer && title === '详细'"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="成果体现" prop="resultMemo">
            <el-input
              v-model="formData.resultMemo"
              clearable
              placeholder="请输入成果体现"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
              :disabled="!footer && title === '详细'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="预计完成时间" prop="deadline">
            <el-date-picker
              v-model="formData.deadline"
              value-format="yyyy-MM-dd"
              :style="{ width: '256px' }"
              type="date"
              :disabled="!footer && title === '详细'"
            />
          </el-form-item>
        </el-col>

        <template v-if="formData.reimpl && showModels['reimpl']">
          <el-col :span="24">
            <el-divider>整改落实信息</el-divider>
          </el-col>

          <el-col :span="24">
            <el-form-item label="整改措施" prop="rectificationMeasures">
              <el-input
                v-model="formData.reimpl.rectificationMeasures"
                clearable
                placeholder="请输入整改措施"
                type="textarea"
                rows="4"
                disabled
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="整改情况概述" prop="situationoverView">
              <el-input
                v-model="formData.reimpl.situationoverView"
                clearable
                placeholder="请输入整改情况概述"
                type="textarea"
                rows="4"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="成果体现" prop="achivement">
              <el-input
                v-model="formData.reimpl.achivement"
                clearable
                placeholder="请输入成果体现"
                type="textarea"
                rows="4"
                disabled
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="完成时间" prop="deadline">
              <el-date-picker
                v-model="formData.reimpl.deadline"
                placeholder="选择完成时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="整改结论" prop="conclusion">
              <el-select
                v-model="formData.reimpl.conclusion"
                placeholder="请选择整改结论"
                clearable
                disabled
              >
                <el-option label="未整改" value="未整改" />
                <el-option label="已整改未到位" value="已整改未到位" />
                <el-option label="已整改到位" value="已整改到位" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="formData.reimpl.conclusion != '已整改到位'">
            <el-form-item label="下一步整改措施" prop="nextMeasures">
              <el-input
                v-model="formData.reimpl.nextMeasures"
                clearable
                placeholder="请输入下一步整改措施"
                type="textarea"
                rows="4"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="formData.reimpl.conclusion != '已整改到位'">
            <el-form-item label="计划完成整改时间" prop="finishTime">
              <el-date-picker
                v-model="formData.reimpl.finishTime"
                placeholder="选择计划完成整改时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                disabled
              />
            </el-form-item>
          </el-col>
        </template>

        <template v-if="formData.valua && showModels['valua']">
          <el-col :span="24">
            <el-divider>整改评价信息</el-divider>
          </el-col>

          <el-col :span="24">
            <el-form-item label="整改评价结果" prop="resultStatus">
              <el-select
                v-model="formData.valua.resultStatus"
                placeholder="请选择整改评价结果"
                clearable
                disabled
              >
                <el-option label="未整改" :value="1" />
                <el-option label="已整改未到位" :value="2" />
                <el-option label="已整改到位" :value="3" />
                <el-option label="关闭" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="formData.valua.resultStatus != '4'">
            <el-form-item label="检查过程" prop="inspectionProcess">
              <el-input
                v-model="formData.valua.inspectionProcess"
                clearable
                placeholder="请输入检查过程"
                type="textarea"
                rows="4"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="formData.valua.resultStatus == '4'">
            <el-form-item label="关闭原因" prop="inspectionProcess">
              <el-input
                v-model="formData.valua.inspectionProcess"
                clearable
                placeholder="请输入关闭原因"
                type="textarea"
                rows="4"
                disabled
              />
            </el-form-item>
          </el-col>
        </template>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-top: 5px"
            v-if="footer || title !== '详细'"
          >
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
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="footer || title !== '详细'"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>

    <template #footer v-if="footer || title !== '详细'">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import { delPlanIssuesFile, saveIssuesRelaPlan } from '@/api/zgzz/index.js'
  import store from '@/store'
  import * as dayjs from 'dayjs'
  import { getPrivewAttInfo } from '@/api/contract/manage'

  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'MyDraftInfo',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        dialogFormVisible: false,
        formData: {
          attIds: '', // 当前页面新保存的附件主键数组
          attList: [], // 附件列表
          auditObjectName: '', // 被审计对象名称
          auditObjectId: '', // 被审计对象主键
          auditObjectType: '', // 被审计对象类型
          createStaffName: '', // 创建人
          createTime: '', // 创建时间
          historyStatus: '', // 历史状态用于还原
          issuesCode: '', // 问题编号
          issuesId: '', // 整改清单主键
          issuesItem: '', // 事项
          issuesName: '', // 问题名称
          issuesParent: '', // 变更前的主键
          issuesTitle: '', // 问题标题
          issuesType: '', // 问题来源
          issuesVersion: '', // 历史版本
          linkDeptId: '', // 所属部门
          linkOrgId: '', // 所属公司
          opinions: '', // 审计意见及建议
          programProcess: '', // 审计执行过程
          projectId: '', // 关联项目主键
          projectName: '', // 项目名称
          projectNo: '', // 项目编号
          quesitionId: '', // 审计内控关联表单外键
          questionMemo: '', // 问题详情
          mainorg: '',
          rectificationPlan: '',
          rectificationMeasures: '',
          resultMemo: '',
          deadline: '',
          evalId: undefined,
          evaluaTime: undefined,
          evaluator: undefined,
          implId: undefined,
          inspectionProcess: undefined,
          resultStatus: undefined,
        },
        footer: true,
        tableData: [],
        rules: {
          issuesType: [
            {
              required: true,
              message: '请选择业务类别',
              trigger: 'blur',
            },
          ],
          yjfh: [
            {
              required: true,
              message: '请选择复核人',
              trigger: 'blur',
            },
          ],
          ejfh: [
            {
              required: true,
              message: '请选择二级复核人',
              trigger: 'blur',
            },
          ],
          sheetId: [
            {
              required: true,
              message: '请输入业务编号',
              trigger: 'blur',
            },
          ],
          issuesName: [
            {
              required: true,
              message: '请输入问题名称',
              trigger: 'blur',
            },
          ],
          auditObjectName: [
            {
              required: true,
              message: '请输入单位',
              trigger: 'blur',
            },
          ],
          // createStaffName: [
          //   {
          //     required: true,
          //     message: '请输入人员',
          //     trigger: 'blur',
          //   },
          // ],
          riskLevel: [
            {
              required: true,
              message: '是否发现问题',
              trigger: 'blur',
            },
          ],
          programProcess: [
            {
              required: true,
              message: '请输入程序执行过程',
              trigger: 'blur',
            },
          ],
          questionMemo: [
            {
              required: true,
              message: '请输入问题详情',
              trigger: 'blur',
            },
          ],
          rectificationPlan: [
            {
              required: true,
              message: '请输入整改方案',
              trigger: 'blur',
            },
          ],
          rectificationMeasures: [
            {
              required: true,
              message: '请输入整改措施',
              trigger: 'blur',
            },
          ],
          resultMemo: [
            {
              required: true,
              message: '请输入成果体现',
              trigger: 'blur',
            },
          ],
          deadline: [
            {
              required: true,
              message: '请输入预计完成时间',
              trigger: 'blur',
            },
          ],
        },
        title: '新增',
        reviewType: '',
        reportData: [],
        currentProject: {},
        issuesId: '',
        planId: '',
        relaId: '',
        showModels: {},
      }
    },
    methods: {
      async showEdit(title, row) {
        this.dialogFormVisible = true
        this.formData.createStaffName = JSON.parse(
          localStorage.getItem('userInfo')
        ).realname
        this.formData.createTime = new Date()

        // 保存当前编辑状态，避免被新数据覆盖
        let currentEditState = null
        if (
          this.formData.issuesId &&
          this.formData.issuesId === row.issues?.issuesId
        ) {
          currentEditState = {
            rectificationPlan: this.formData.rectificationPlan,
            resultMemo: this.formData.resultMemo,
            deadline: this.formData.deadline,
            attList: this.tableData,
          }
        }

        // 如果传入了currentEditState，优先使用它
        if (row.currentEditState) {
          currentEditState = row.currentEditState
        }

        if (row) {
          Object.assign(this.formData, row.issues)
          this.formData.createTime = new Date(row.issues.createTime)
          this.issuesId = row.issues.issuesId
          this.planId = row.planId
          this.relaId = row.relaId

          // 如果有当前编辑状态，优先使用它
          if (currentEditState) {
            this.formData.rectificationPlan =
              currentEditState.rectificationPlan || row.rectificationPlan
            this.formData.resultMemo =
              currentEditState.resultMemo || row.resultMemo
            this.formData.deadline =
              currentEditState.deadline ||
              (row.deadline
                ? dayjs(row.deadline).format('YYYY-MM-DD')
                : undefined)
            this.tableData =
              currentEditState.attList ||
              (row.attList ? JSON.parse(JSON.stringify(row.attList)) : [])
          } else {
            this.formData.deadline = row.deadline
              ? dayjs(row.deadline).format('YYYY-MM-DD')
              : undefined
            this.formData.rectificationMeasures = row.rectificationMeasures
            this.formData.rectificationPlan = row.rectificationPlan
            this.formData.resultMemo = row.resultMemo
            this.formData.attList = row.attList || []
            // 附件容器换成tableData
            if (row.attList)
              this.tableData = JSON.parse(JSON.stringify(row.attList))
          }

          this.showModels = row.showModels || {}
          if (row.valua && JSON.stringify(row.valua) !== '{}') {
            this.formData.valua = row.valua
          }
          if (row.reimpl && JSON.stringify(row.reimpl) !== '{}') {
            this.formData.reimpl = row.reimpl
          }

          if (this.formData.issuesType == '3') {
            this.footer = true
          } else {
            this.footer = false
          }
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        }
      },
      close() {
        this.dialogFormVisible = false
        this.$emit('closeDialog')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const attidArr = []
            this.tableData.map((item) => {
              if (this.formData.attList.some((x) => x.attid === item.attid)) {
              } else attidArr.push(item.attid)
            })
            const attIds = attidArr.join(',')

            let obj = {
              attIds,
              issuesId: this.issuesId,
              planId: this.planId,
              relaId: this.relaId,
              deadline: dayjs(this.formData.deadline).format('YYYY-MM-DD'),
              rectificationMeasures: this.formData.rectificationMeasures,
              rectificationPlan: this.formData.rectificationPlan,
              resultMemo: this.formData.resultMemo,
            }

            if (this.relaId) {
              const res = await saveIssuesRelaPlan(obj)
              if (res.code == 1) {
                this.$baseMessage('保存成功', 'success')
                this.$emit('update-data', {
                  issuesId: this.issuesId,
                  rectificationPlan: this.formData.rectificationPlan,
                  resultMemo: this.formData.resultMemo,
                  deadline: this.formData.deadline,
                  relaId: this.relaId,
                  attList: this.tableData,
                })
                this.$emit('fetch-data')
                this.close()
              }
            } else {
              console.log('Emitting edit-data with issuesId:', this.issuesId)
              this.$emit('edit-data', {
                issuesId: this.issuesId,
                rectificationPlan: this.formData.rectificationPlan,
                resultMemo: this.formData.resultMemo,
                deadline: this.formData.deadline,
                attIds,
                attList: this.tableData,
              })
              this.close()
            }
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },

      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        const res = await delPlanIssuesFile({
          attId: row.attid,
          relaId: this.relaId,
        })
        this.$baseMessage('删除成功！', 'success')
        this.tableData = list
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
      handleSuccess(file) {
        if (file.code == 200) {
          // 更新文件列表
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          this.$baseMessage(file.msg, 'success')
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
