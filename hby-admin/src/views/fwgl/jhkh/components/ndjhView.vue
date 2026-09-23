<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000"
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
          <!-- <el-col :span="12">
            <el-form-item label="序号" prop="name">
              <el-input
                v-model="formData.name"
                clearable
                placeholder="请输入序号"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="计划名称" prop="annualPlanName">
              <el-input
                v-model="formData.annualPlanName"
                clearable
                placeholder="请输入计划名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="创建人" prop="annualPlanCreator">
              <el-input
                v-model="formData.annualPlanCreator"
                clearable
                placeholder="请选择创建人"
                :style="{ width: '100%' }"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="创建时间" prop="annualPlanCreatedTime">
              <el-date-picker
                :style="{ width: '100%' }"
                v-model="formData.annualPlanCreatedTime"
                placeholder="创建时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col> -->
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
      <template #footer>
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
  </div>
</template>
<script>
  import { addNDJH, deleteFile, getNDJHDefaultInfo } from '@/api/fwgl/jhkh'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { uploadApi, download } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  import store from '@/store'
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
    props: { CandidateUserSelect, Resubmit },
    data() {
      return {
        dialogFormVisible: false,
        formData: {
          annualPlanCreatedTime: '',
          annualPlanCreator: '',
          annualPlanId: '',
          annualPlanName: '',
          fileIds: '',
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
          getNDJHDefaultInfo({
            id: row.annualPlanId,
          }).then(async (res) => {
            this.formData = Object.assign({}, res.data.annualPlan)
            this.fileList = res.data.files || []

            if (
              res.data.annualPlan.state == 2 ||
              res.data.annualPlan.state == 3
            ) {
              const res2 = await getFlowTaskInfo({
                tableId: 34,
                formId: row.annualPlanId,
              })
              this.jurisdictionCode = res2.data.isFlowInfo
              if (res2.data.isFlowInfo) {
                this.flowtaskinfoflowid = res2.data.flowId + ''
                this.fromId = row.annualPlanId + ''
                this.fromIdcopy = row.annualPlanId + ''
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
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.annualPlanCreator = userInfo.realname
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {
          annualPlanCreatedTime: '',
          annualPlanCreator: '',
          annualPlanId: '',
          annualPlanName: '',
          fileIds: '',
        }
        this.clearType = true
        this.$emit('fentch')
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        const ids = this.fileList.map((res) => res.fileId)
        this.formData.fileIds = ids.toString()
        addNDJH(this.formData).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage('保存成功', 'success', 'vab-hey-message-success')
            this.dialogFormVisible = false
            this.$emit('fentch')
          }
        })
      },
      //文件上传
      handleSuccess(res) {
        if (res.code == 200) {
          this.fileList.push(res.data.fileIds[0])
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
