<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="24">
          <el-form-item label="事项名称" prop="auditName">
            <el-input
              v-model="formData.auditName"
              clearable
              placeholder="请输入事项名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请人" prop="creatorName">
            <el-input
              v-model="formData.creatorName"
              clearable
              placeholder="请输入申请人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请日期" prop="createdTime">
            <el-date-picker
              style="width: 100%"
              v-model="formData.createdTime"
              placeholder="请输入申请日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="申请部门" prop="workUnitName">
            <el-input
              v-model="formData.workUnitName"
              clearable
              placeholder="请输入申请部门"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="事项知会人员" prop="mattersInformedPersonnel">
            <!-- <el-input
              v-model="formData.mattersInformedPersonnel"
              clearable
              placeholder="请输入事项知会人员"
              :style="{ width: '100%' }"
              disabled
            /> -->
            <!-- <CandidateUserSelect
              @selected="handleCandSelect1"
              :index="0"
              :nodeId="candidateData.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
            /> -->
            <el-input
              v-model="formData.mattersInformedPersonnel"
              clearable
              placeholder="请输入事项知会人员"
              :style="{ width: '90%' }"
              disabled
            />
            <el-button
              @click="projectManagers"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="事项说明" prop="mattersInstructions">
            <el-input
              v-model="formData.mattersInstructions"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入事项说明"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="主办部门意见" prop="hostDepartmentOpinion">
            <el-input
              v-model="formData.hostDepartmentOpinion"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入主办部门意见"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="相关部门意见" prop="relatedDepartmentOpinion">
            <el-input
              v-model="formData.relatedDepartmentOpinion"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入相关部门意见"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="分管领导意见" prop="teamLeaderOpinion">
            <el-input
              v-model="formData.teamLeaderOpinion"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入分管领导意见"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="公司领导意见" prop="firmLeaderOpinion">
            <el-input
              v-model="formData.firmLeaderOpinion"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入公司领导意见"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseURL + uploadApi"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
            >
              <template #default="{ row }">
                <div>
                  {{ row.fileSize / 1000 }}
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDown(row)">下载</el-button>
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
      <el-button @click="close">关 闭</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button
        v-if="
          (formData.status == 2 || formData.status == 3) &&
          jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </div>
    <project-manages
      @projectManages="getChildlistPros"
      ref="manages"
    ></project-manages>
    <!-- 提交 -->
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </el-dialog>
</template>

<script>
  import store from '@/store'
  import { baseURL } from '@/config'
  import { uploadApi } from '@/api/fwgl/api'
  import projectManages from '@/views/fwgl/rcgl/components/selectPersons.vue'
  import { legalReviewInstitution, fetchApi } from '@/api/fwgl/api'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import { download } from '@/api/fwgl/zzxx'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  const { saveOrUpdate, detail } = legalReviewInstitution

  const token = store.getters['user/token']
  import { formatDate } from '@/utils'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    components: { CandidateUserSelect, projectManages, Resubmit },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: uploadApi,
        headers: { token: token },
        tableData: [],
        candidateData: {
          nodeId: 0,
          fromId: 0,
          flowId: 0,
          tableId: 0,
        },
        formData: {
          creatorName: '',
          staffid: '',
          workUnitName: '',
          orgid: '',
          createdTime: '',
        },
        footer: true,
        rules: {
          auditName: [
            {
              required: true,
              message: '请输入事项名称',
              trigger: 'blur',
            },
          ],
          creatorName: [
            {
              required: true,
              message: '请输入申请人',
              trigger: 'blur',
            },
          ],
          workUnitName: [
            {
              required: true,
              message: '请输入申请部门',
              trigger: 'blur',
            },
          ],
          createdTime: [
            {
              required: true,
              message: '请输入申请时间',
              trigger: 'blur',
            },
          ],
          // teamLeaderOpinion: [
          //   {
          //     required: true,
          //     message: '请输入分管领导意见',
          //     trigger: 'blur',
          //   },
          // ],
          // firmLeaderOpinion: [
          //   {
          //     required: true,
          //     message: '请输入公司领导意见',
          //     trigger: 'blur',
          //   },
          // ],
          // mattersInformedPersonnel: [
          //   {
          //     required: true,
          //     message: '请输入事项通知人员',
          //     trigger: 'blur',
          //   },
          // ],
          mattersInstructions: [
            {
              required: true,
              message: '请输入事项说明',
              trigger: 'blur',
            },
          ],
          // hostDepartmentOpinion: [
          //   {
          //     required: true,
          //     message: '请输入主办部门意见',
          //     trigger: 'blur',
          //   },
          // ],
          // relatedDepartmentOpinion: [
          //   {
          //     required: true,
          //     message: '请输入相关部门意见',
          //     trigger: 'blur',
          //   },
          // ],
        },
        dialogFormVisible: false,
        title: '新增',
        //提交
        jurisdictionCode: 0,
        fromId: 0,
        fromIdcopy: 0,
        ymFromId: 0,
        flowtaskinfoflowid: '',
        status: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      async getChildlistPros(val) {
        const info = val.map((res) => res.realname)
        const arr = val.map((res) => res.staffid)

        this.$set(this.formData, 'mattersInformedPersonnel', info.toString())
        this.$set(this.formData, 'mattersInformedPersonnelId', arr.toString())
      },
      /**
       * @description: 打开选择人员组件
       * @return {*}
       */      
      /**
       * @description: 打开选择组件
       * @return {*}
       */      
      projectManagers() {
        this.$refs['manages'].showEdit()
      },
      handlePreview() {},
      /**
       * @description: 上传成功回调
       * @param {*} res
       * @param {*} b
       * @param {*} c
       * @return {*}
       */      
      handleSuccess(res, b, c) {
        if (res && res.data) {
          this.tableData = this.tableData.concat(res.data.fileIds || [])
        }
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */      
      async handleDown(row) {
        const data = await download({ fileId: row.fileId })
        let filename = row.fileName
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        const { fileId } = row
        const i = this.tableData.findIndex((x) => x.fileId === fileId)
        if (i > -1) {
          this.tableData.splice(i, 1)
        }
      },
      /**
       * @description: 外部打开dialog
       * @param {*} title 类型
       * @param {*} row 行数据
       * @return {*}
       */      
      async showEdit(title, row) {
        this.formData = {
          creatorName: '',
          staffid: '',
          workUnitName: '',
          orgid: '',
          createdTime: '',
        }
        this.dialogFormVisible = true
        if (row) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })
          if (res && res.code === 200 && res.data) {
            this.formData = res.data.institutionAudit
            this.tableData = res.data.files || []
          }
          if (
            res.data.institutionAudit.status == 2 ||
            res.data.institutionAudit.status == 3
          ) {
            const res2 = await getFlowTaskInfo({ tableId: 19, formId: row.id })
            this.jurisdictionCode = res2.code
            if (res2.code == 1) {
              this.flowtaskinfoflowid = res2.data.flowId + ''
              this.fromId = row.id + ''
              this.fromIdcopy = row.id + ''
              this.ymFromId = res2.data.id + ''

              const res3 = await getFaqiInfo({
                id: res2.data.id,
                flowId: res2.data.flowId,
              })
              if (res3.code == 1) {
                this.status = res3.data.dataJson.flowTaskInfo.status
              }
            }
          }
          this.loading = false
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.creatorName = userInfo.realname
          this.formData.staffid = userInfo.staffid
          this.formData.workUnitName = userInfo.linkDetp.orgname
          this.formData.orgid = userInfo.linkDetp.orgid

          this.formData.createdTime = formatDate(new Date())
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.tableData = []
        this.clearType = true
        this.$emit('fetch-data')
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        this.loading = true
        this.formData.fileIds = this.tableData.map((x) => x.fileId).join(',')
        this.formData.auditType = 2
        this.loading = false
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await fetchApi(saveOrUpdate, this.formData)

            if (res && res.code === 200) {
              this.$message({ message: '保存成功', type: 'success' })
              // this.$emit('fetch-data')
              this.close()
            } else {
              this.$message({ message: res.message, type: 'error' })
            }
          } else {
            return false
          }
        })
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */      
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
      /**
       * @description: 引迈流程提交
       * @return {*}
       */      
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
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
