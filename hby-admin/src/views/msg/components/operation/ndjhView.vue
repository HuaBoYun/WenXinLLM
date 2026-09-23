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
        <!-- <el-col :lg="12" :md="12" :sm="24">
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
        <el-col :lg="12" :md="12" :sm="24">
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

        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="创建人" prop="annualPlanCreator">
            <el-input
              v-model="formData.annualPlanCreator"
              clearable
              placeholder="请选择创建人"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="创建时间" prop="createdTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.createdTime"
              placeholder="创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>相关附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer && !$store.state.work.processMobile">
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
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
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
  </div>
</template>
<script>
  import { ymWorkCandidates, ymWorkSubmit } from '@/api/contract/manage'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import { addNDJH, deleteFile, getNDJHDefaultInfo } from '@/api/fwgl/jhkh'
  import { uploadApi,download } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'

  export default {
    props: [],
    components: {
      CandidateUserSelect,
      Resubmit
    },
    data() {
      return {
        dialogFormVisible: false,
        formData: {
          createdTime: '',
          annualPlanCreator: '',
          annualPlanId: '',
          annualPlanName: '',
          fileIds: '',
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
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status:0,
      }
    },
    methods: {
      showEdit(title, formId, flowtaskinfoflowid, ymFromId,status) {
        this.dialogFormVisible = true
        this.fileList = []
        if (formId) {
          getNDJHDefaultInfo({
            id: formId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.annualPlan)
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
        this.formData = {
          createdTime: '',
          annualPlanCreator: '',
          annualPlanId: '',
          annualPlanName: '',
          fileIds: '',
        }
         
        this.$bus.$emit('updateMsg', 0)
      },
      add() {
        const ids = this.fileList.map((res) => res.fileId)
        this.formData.fileIds = ids.toString()
        addNDJH(this.formData).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage('保存成功', 'success', 'vab-hey-message-success')
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
      async handleDown(row){
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

const url = data.previewurl + '?url=' + encodeURIComponent(Base64.encode(data.ftpUrl))
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
