<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        size="mini"
        :rules="rules"
      >
        <el-col :span="12">
          <el-form-item label="单位名称" prop="workUnitName">
            <el-input
              v-model="formData.workUnitName"
              clearable
              disabled
              placeholder="工作单位"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划名称" prop="popularizeLawPlanName">
            <el-input
              v-model="formData.popularizeLawPlanName"
              clearable
              placeholder="请输入计划名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划主题" prop="topic">
            <el-input
              v-model="formData.topic"
              clearable
              placeholder="请输入计划主题"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年份" prop="planYear">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.planYear"
              placeholder="年份"
              type="year"
              format="yyyy"
              value-format="yyyy"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="popularizeLawCreator">
            <el-input
              v-model="formData.popularizeLawCreator"
              clearable
              placeholder="请选择创建人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="创建时间" prop="popularizeLawCreatedTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.popularizeLawCreatedTime"
              placeholder="创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
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
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="fileList">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
            />
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
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
      <el-button
        v-if="
          (formData.state == 2 || formData.state == 3) && jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </template>
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
  import { uploadApi, deleteFile, download } from '@/api/fwgl/zzxx'
  import { addPFJH, getPFJHDefaultInfo } from '@/api/fwgl/pfpx'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import { formatDate } from '@/utils'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    components: {},
    inheritAttrs: false,
    components: { CandidateUserSelect, Resubmit },
    data() {
      return {
        formData: {
          planYear: '',
          popularizeLawCreatedTime: '',
          popularizeLawCreator: '',
          popularizeLawPlanName: '',
          topic: '',
          state: '',
        },
        rules: {
          popularizeLawPlanName: [
            { required: true, message: '请输入计划名称', trigger: 'blur' },
          ],
          planYear: [
            { required: true, message: '请选择年份', trigger: 'blur' },
          ],
          popularizeLawCreator: [
            { required: true, message: '请选择创建人', trigger: 'blur' },
          ],
          popularizeLawCreatedTime: [
            { required: true, message: '请选择创建时间', trigger: 'blur' },
          ],
        },

        footer: true,
        dialogFormVisible: false,
        title: '新增',
        tableData: [],
        fileList: [],
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
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
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.dialogFormVisible = true
        this.fileList = []
        if (row) {
          getPFJHDefaultInfo({
            id: row.popularizeLawPlanId,
          }).then(async (res) => {
            this.formData = Object.assign({}, res.data.popularizeLawPlan)
            if (res.data.files) {
              this.fileList = res.data.files
            }
            if (
              res.data.popularizeLawPlan.state == 2 ||
              res.data.popularizeLawPlan.state == 3
            ) {
              const res2 = await getFlowTaskInfo({
                tableId: 34,
                formId: row.popularizeLawPlanId,
              })
              this.jurisdictionCode = res2.data.isFlowInfo
              if (res2.data.isFlowInfo) {
                this.flowtaskinfoflowid = res2.data.flowId + ''
                this.fromId = row.popularizeLawPlanId + ''
                this.fromIdcopy = row.popularizeLawPlanId + ''
                this.ymFromId = res2.data.id + ''

                const res3 = await getFaqiInfo({
                  id: res2.data.id,
                  flowId: res2.data.flowId,
                })
                if (res3.code == 1) {
                  this.status = res3.data.dataJson.flowTaskInfo.status + ''
                }
              }
            }
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = this.$options.data().formData

          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.popularizeLawCreator = userInfo.realname
          this.formData.workUnitName = userInfo.linkOrg.orgname
          this.formData.popularizeLawCreatedTime = formatDate(new Date())
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {
          type: '合同审查',
        }
        this.dialogFormVisible = false
        this.clearType = true
        this.$emit('fetchData')
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        const aa = []
        this.fileList.forEach((e) => {
          aa.push(e.fileId)
        })
        this.formData.fileIds = aa.toString()
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            addPFJH(this.formData).then((res) => {
              if (res.msg == '成功') {
                this.dialogFormVisible = false
                this.$emit('fetchData')
              }
            })
          } else {
            return false
          }
        })
      },
      handleAvatarSuccess(res) {
        if (res.code == 200) {
          this.fileList.push(res.data.fileIds[0])
        }
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList.splice(
              this.fileList.findIndex((x) => x.fileId == row.fileId),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
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
