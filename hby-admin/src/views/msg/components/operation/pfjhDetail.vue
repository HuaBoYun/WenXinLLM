<template>
  <div>
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        size="mini"
        :rules="rules"
      >
        <el-col :lg="12" :md="12" :sm="24">
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

        <el-col :lg="12" :md="12" :sm="24">
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
        <el-col :lg="12" :md="12" :sm="24">
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

        <el-col :lg="12" :md="12" :sm="24">
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
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="footer && !$store.state.work.processMobile"
          >
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
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div> -->
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
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import store from '@/store'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { uploadApi, deleteFile, download } from '@/api/fwgl/zzxx'
  import { addPFJH, getPFJHDefaultInfo } from '@/api/fwgl/pfpx'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import { formatDate } from '@/utils'
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'

  export default {
    components: { CandidateUserSelect, Resubmit },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          planYear: '',
          popularizeLawCreatedTime: '',
          popularizeLawCreator: '',
          popularizeLawPlanName: '',
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
        ymFromId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        chooseStatus: true,
        executorType: '',
        contractid: '',
        status: 0,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      showEdit(title, row, formId, flowtaskinfoflowid, ymFromId, status) {
        this.dialogFormVisible = true
        this.fileList = []
        if (row) {
          getPFJHDefaultInfo({
            id: row.popularizeLawPlanId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.popularizeLawPlan)
            if (res.data.files) {
              this.fileList = res.data.files
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

          this.formData.popularizeLawCreatedTime = formatDate(new Date())
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
          type: '合同审查',
        }

        this.$bus.$emit('updateMsg', 0)
        this.dialogFormVisible = false

        this.footer = true
      },
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
                this.$baseMessage('成功', 'success')
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
