<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="24">
        <el-form
          ref="ruleForm"
          label-width="100px"
          :model="formData"
          :rules="rules"
          size="mini"
        >
          <el-col :span="12">
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
          <el-col :span="12">
            <el-form-item label="考核类型" prop="type">
              <el-select
                :style="{ width: '78%' }"
                v-model="formData.type"
                placeholder="规划类型"
                disabled
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

          <el-col :span="12">
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

          <el-col :span="12" v-if="formData.type === '2'">
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

          <el-col :span="12">
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
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>相关附件</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
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
              <el-table-column
                align="center"
                label="附件名称"
                prop="fileName"
              />
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
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handlePreviewFile(row)">
                    预览
                  </el-button>
                  <el-button type="text" @click="handleDown(row)">
                    下载
                  </el-button>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer v-if="footer">
        <el-button @click="close">关 闭</el-button>
        <el-button @click="add" type="primary">确定</el-button>
        <el-button
          v-if="
            (formData.state == 2 || formData.state == 3) &&
            jurisdictionCode == 1
          "
          @click="ymsubmit"
          type="primary"
        >
          提交
        </el-button>
      </template>
    </el-dialog>
    <ratingScale ref="ratingScale" @on-save-success="handleSaveSuccess" />
    <ratingScale2 ref="ratingScale2" @on-save-success="handleSaveSuccess" />
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
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
  </div>
</template>
<script>
  import { addNDKH, deleteFile, getNDKHDefaultInfo } from '@/api/fwgl/jhkh'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { uploadApi, download } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  import store from '@/store'
  import ratingScale from './ratingScale.vue'
  import ratingScale2 from './ratingScale2.vue'
  import CompanySelectModal from '@/components/CampanySelectModal'
  const token = store.getters['user/token']
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

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
          state: '',
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
        jurisdictionCode: 0,
        fromId: 0,
        fromIdcopy: 0,
        ymFromId: 0,
        flowtaskinfoflowid: '',
        status: '',
      }
    },
    created() {},
    computed: {
      userInfo() {
        return JSON.parse(localStorage.getItem('userInfo'))
      },
    },
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
          getNDKHDefaultInfo({
            id: row.annualExamineId,
          }).then(async (res) => {
            this.formData = Object.assign({}, res.data.annualExamine)
            this.formData.annualExamineCreator = this.formData.creatorName
            this.formData.type = String(this.formData.type)
            this.fileList = res.data.files || []

            if (
              res.data.annualExamine.state == 2 ||
              res.data.annualExamine.state == 3
            ) {
              const res2 = await getFlowTaskInfo({
                tableId: 36,
                formId: row.annualExamineId,
              })
              this.jurisdictionCode = res2.data.isFlowInfo
              if (res2.data.isFlowInfo) {
                this.flowtaskinfoflowid = res2.data.flowId + ''
                this.fromId = row.annualExamineId + ''
                this.fromIdcopy = row.annualExamineId + ''
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
          let type = '1'
          this.formData = {
            annualExamineCreator: this.userInfo.realname,
            sonCompanyId: '',
            sonCompanyName: '',
            examineTime: '',
            annualExamineName: '',
            examineTime: '',
            fileIds: '',
            type: type,
          }
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
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {
          type: '1',
        }
        this.clearType = true
        this.$emit('fentch')
        this.dialogFormVisible = false
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
          this.dialogFormVisible = false
          this.$emit('fentch')
        }
      },
      //文件上传
      handleSuccess(res) {
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
      handleSaveSuccess(data) {
        this.formData.totalScore = data.totalScore || ''
        this.formData.scoreTransaction = data.id
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
       * @description: 选择公司部门回调
       * @param {*} val 已选数据
       * @return {*}
       */      
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
