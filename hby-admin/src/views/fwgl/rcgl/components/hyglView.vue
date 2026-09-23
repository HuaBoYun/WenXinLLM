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
        <el-col :span="12">
          <el-form-item label="会议名称" prop="conferenceName">
            <el-input
              v-model="formData.conferenceName"
              clearable
              placeholder="请输入会议名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="会议主持人 " prop="compereName">
            <el-input
              v-model="formData.compereName"
              clearable
              placeholder="请输入会议主持人"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              @click="projectManager"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参会人员" prop="participantsNames">
            <el-input
              v-model="formData.participantsNames"
              clearable
              placeholder="请输入参会人员"
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
        <el-col :span="12">
          <el-form-item label="会议时间" prop="conferenceTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.conferenceTime"
              placeholder="会议时间"
              type="datetime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="会议内容" prop="content">
            <el-input
              v-model="formData.content"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入会议内容"
              :style="{ width: '100%' }"
              :disabled="!footer"
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
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <project-manages
      @projectManages="getChildlistPros"
      ref="manages"
    ></project-manages>
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import { uploadApi } from '@/api/fwgl/api'
  import store from '@/store'
  import { dailyManagementConference, fetchApi } from '@/api/fwgl/api'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import projectManages from './selectPersons.vue'
  import { download } from '@/api/fwgl/zzxx'
  const token = store.getters['user/token']
  const { saveOrUpdate, detail } = dailyManagementConference

  export default {
    components: { projectManage, projectManages },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: uploadApi,
        headers: { token: token },
        tableData: [],
        formData: {
          conferenceName: '',
          compere: '',
          compereName: '',
          conferenceTime: '',
          content: '',
          fileIds: '',
          participantsNames: '',
        },
        footer: true,
        rules: {
          conferenceName: [
            {
              required: true,
              message: '请输入会议名称',
              trigger: 'blur',
            },
          ],
          compere: [
            {
              required: true,
              message: '请输入会议主持人',
              trigger: 'blur',
            },
          ],
          conferenceTime: [
            {
              required: true,
              message: '请选择会议时间',
              trigger: 'blur',
            },
          ],
          content: [
            {
              required: true,
              message: '请输入会议内容',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handlePreview() {},
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
        this.dialogFormVisible = true
        if (row) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })
          this.loading = false
          if (res && res.code === 200 && res.data) {
            this.formData = res.data.conferenceManagement
            this.tableData = res.data.files || []
          } else {
            this.$message({
              message: '获取详情失败',
              type: 'error',
            })
          }
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.formData.fileIds = this.tableData
              .map((x) => x.fileId)
              .join(',')
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await fetchApi(saveOrUpdate, params)
            if (res && res.code === 200) {
              this.close()
              this.fetchData()
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
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
       * @description: 打开选择人员组件
       * @return {*}
       */      
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      /**
       * @description: 打开选择组件
       * @return {*}
       */      
      projectManagers() {
        this.$refs['manages'].showEdit()
      },
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      async getChildlistPro(val) {
        this.staffid = val[0].staffid
        this.$set(this.formData, 'compere', val[0].staffid)
        this.$set(this.formData, 'compereName', val[0].realname)
      },
      async getChildlistPros(val) {
        const info = val.map((res) => res.realname)
        const arr = val.map((res) => res.staffid)

        this.$set(this.formData, 'participantsNames', info.toString())
        this.$set(this.formData, 'participants', arr.toString())
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
