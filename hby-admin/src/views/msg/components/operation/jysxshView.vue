<template>
  <div>
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
        <el-col :lg="12" :md="12" :sm="24">
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
        <el-col :lg="12" :md="12" :sm="24">
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
            <el-input
              v-model="formData.mattersInformedPersonnel"
              clearable
              placeholder="请输入事项知会人员"
              :style="{ width: '80%' }"
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
              :disabled="!footer"
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
              :disabled="!footer"
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
              :disabled="!footer"
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
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="footer && !$store.state.work.processMobile"
          >
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
                <el-button type="text" @click="handleDown(row)">下载</el-button>
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
      </el-form>
    </el-row>
    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div> -->
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save" v-if="footer">确 定</el-button>
      <el-button type="primary" @click="ymsubmit" v-if="footer">
        提 交
      </el-button>
    </div>

    <project-manages
      @projectManages="getChildlistPros"
      ref="manages"
    ></project-manages>

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import store from '@/store'
  import { baseURL } from '@/config'
  import { uploadApi } from '@/api/fwgl/api'
  import { legalReviewInstitution, fetchApi } from '@/api/fwgl/api'
  import { download } from '@/api/fwgl/zzxx'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const { saveOrUpdate, detail } = legalReviewInstitution
  import projectManages from '@/views/fwgl/rcgl/components/selectPersons.vue'
  const token = store.getters['user/token']
  import { formatDate } from '@/utils'
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'

  export default {
    components: { CandidateUserSelect, projectManages, Resubmit },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        isOnwer: false,
        loading: false,
        baseURL: baseURL,
        uploadApi: uploadApi,
        headers: { token: token },
        tableData: [],
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

          // mattersInformedPersonnel: [
          //   {
          //     required: true,
          //     message: '请输入事项通知人员',
          //     trigger: 'blur',
          //   },
          // ],
        },
        dialogFormVisible: false,
        title: '新增',

        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async getChildlistPros(val) {
        const info = val.map((res) => res.realname)
        const arr = val.map((res) => res.staffid)

        this.$set(this.formData, 'mattersInformedPersonnel', info.toString())
        this.$set(this.formData, 'mattersInformedPersonnelId', arr.toString())
      },
      projectManagers() {
        this.$refs['manages'].showEdit()
      },
      handlePreview() {},
      handleSuccess(res, b, c) {
        if (res && res.data) {
          this.tableData = this.tableData.concat(res.data.fileIds || [])
        }
      },
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
      handleDelete(row) {
        const { fileId } = row
        const i = this.tableData.findIndex((x) => x.fileId === fileId)
        if (i > -1) {
          this.tableData.splice(i, 1)
        }
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        userId,
        isWfqdedit,
        status
      ) {
        this.formData = {
          creatorName: '',
          staffid: '',
          workUnitName: '',
          orgid: '',
          createdTime: '',
        }
        this.dialogFormVisible = true
        if (formId) {
          this.loading = true
          const res = await fetchApi(detail, { id: formId })
          if (res && res.code === 200 && res.data) {
            this.formData = res.data.institutionAudit
            this.tableData = res.data.files || []
          }
          this.loading = false
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          if (userId == userInfo.staffid && isWfqdedit) {
            this.isOnwer = true
          }
        } else {
          this.title = '新增'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.creatorName = userInfo.realname
          this.formData.staffid = userInfo.staffid
          this.formData.workUnitName = userInfo.linkDetp.orgname
          this.formData.orgid = userInfo.linkDetp.orgid

          this.formData.createdTime = formatDate(new Date())
        }
        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status

        if (this.$store.state.work.processMobile) {
          this.$nextTick(() => {
            changeFormSizeStyleFunc()
          })
        }
      },
      close() {
        this.formData = {}

        this.$bus.$emit('updateMsg', 0)
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
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
            } else {
              this.$message({ message: res.message, type: 'error' })
            }
          } else {
            return false
          }
        })
      },

      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },

      //提交
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
