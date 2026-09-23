<template>
  <div>
    <el-row :gutter="24">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="考核名称" prop="annualExamineName">
            <el-input
              v-model="formData.annualExamineName"
              clearable
              placeholder="请输入考核名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="考核类型" prop="type">
            <el-select
              :style="{ width: '78%' }"
              v-model="formData.type"
              placeholder="规划类型"
              :disabled="!footer"
            >
              <el-option label="外部监管考核" value="1" />
              <el-option label="子单位考核" value="2" />
            </el-select>
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openRating()"
            >
              评分表
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="创建人" prop="annualExamineCreator">
            <el-input
              v-model="formData.annualExamineCreator"
              clearable
              placeholder="请选择创建人"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>

        <el-col :lg="12" :md="12" :sm="24" v-if="formData.type === '2'">
          <el-form-item label="子单位" prop="sonCompanyName">
            <el-input
              v-model.trim="formData.sonCompanyName"
              placeholder="请选择子单位"
              style="width: 82%"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="showGroupLeader()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="考核时间" prop="examineTime">
            <!-- <el-date-picker
                :style="{ width: '100%' }"
                v-model="formData.examineTime"
                placeholder="考核时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                :disabled="!footer"
              /> -->

            <el-date-picker
              style="width: 100%"
              v-model="formData.examineTime"
              placeholder="考核时间"
              type="datetime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>相关附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="footer && !$store.state.work.processMobile"
          >
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
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
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>

    <ratingScale ref="ratingScale" @on-save-success="handleSaveSuccess" />
    <ratingScale2 ref="ratingScale2" @on-save-success="handleSaveSuccess" />
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />

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
  import { addNDKH, deleteFile, getNDKHDefaultInfo } from '@/api/fwgl/jhkh'
  import { ymWorkCandidates, ymWorkSubmit } from '@/api/contract/manage'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { uploadApi, download } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  import store from '@/store'
  import ratingScale from '@/views/fwgl/jhkh/components/ratingScale.vue'
  import ratingScale2 from '@/views/fwgl/jhkh/components/ratingScale2.vue'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const token = store.getters['user/token']
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'

  export default {
    props: [],
    components: {
      ratingScale,
      ratingScale2,
      CompanySelectModal,
      CandidateUserSelect,
      Resubmit,
    },
    data() {
      return {
        dialogFormVisible: false,
        formData: {
          annualExamineId: '',
          annualExamineCreator: '',
          examineTime: '',
          annualExamineName: '',
          examineTime: '',
          fileIds: '',
          type: '1',
          sonCompanyId: '',
          sonCompanyName: '',
        },
        footer: true,
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
        fileList: [],
        title: '新增',
        rules: {
          name: [
            {
              required: true,
              message: '请输入商标名称',
              trigger: 'blur',
            },
          ],
        },
        totalScore: '',
        //提交

        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: 0,
      }
    },
    created() {},
    computed: {
      userInfo() {
        return JSON.parse(localStorage.getItem('userInfo'))
      },
    },
    methods: {
      showEdit(title, row, formId, flowtaskinfoflowid, ymFromId, status) {
        this.dialogFormVisible = true
        this.fileList = []
        if (row) {
          getNDKHDefaultInfo({
            id: row.annualExamineId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.annualExamine)
            this.formData.annualExamineCreator = this.formData.creatorName
            this.formData.type = String(this.formData.type)
            this.fileList = res.data.files || []
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = {
            annualExamineCreator: this.userInfo.realname,
            sonCompanyId: '',
            sonCompanyName: '',
            examineTime: '',
            annualExamineName: '',
            examineTime: '',
            fileIds: '',
            type: '1',
          }
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
      openRating() {
        if (this.formData.type == '1') {
          this.$refs['ratingScale'].show(
            this.formData.scoreTransaction,
            this.formData.annualExamineId,
            this.footer
          )
        } else {
          this.$refs['ratingScale2'].show(
            this.formData.scoreTransaction,
            this.formData.annualExamineId,
            this.footer
          )
        }
      },
      close() {
        this.formData = {
          type: '1',
        }

        this.$bus.$emit('updateMsg', 0)
        this.footer = true
      },
      async add() {
        const ids = this.fileList.map((res) => res.fileId)
        this.formData.fileIds = ids.toString()
        if (this.formData.type !== '')
          this.formData.type = Number(this.formData.type)
        if (this.formData.type === '1') {
          this.formData.sonCompanyId = undefined
          this.formData.sonCompanyName = undefined
        }
        // this.formData.totalScore = this.totalScore
        const res = await addNDKH(this.formData)
        if (res && res.msg == '成功') {
          this.$baseMessage('保存成功', 'success', 'vab-hey-message-success')

          this.$emit('fentch')
        }
      },
      //文件上传
      handleSuccess(res) {
        if (res.code == 200) {
          this.fileList.push(res.data.fileIds[0])
        }
      },
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
      handleSaveSuccess(data) {
        this.formData.totalScore = data.totalScore || ''
        this.formData.scoreTransaction = data.id
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
      handleCompanyTreeSelected(val) {
        this.$set(this.formData, val.idKey, val.id)
        this.$set(this.formData, val.labelKey, val.label)
      },
      showGroupLeader() {
        this.$refs.companySelect.show({
          labelKey: 'sonCompanyName',
          idKey: 'sonCompanyId',
          title: '选择子单位',
        })
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
