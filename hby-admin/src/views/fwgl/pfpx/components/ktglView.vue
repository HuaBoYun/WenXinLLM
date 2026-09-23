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
          :rules="rules"
          :model="formData"
          size="mini"
        >
          <el-col :span="12">
            <el-form-item label="课题名称" prop="subjectManagementName">
              <el-input
                v-model="formData.subjectManagementName"
                clearable
                placeholder="请输入课题名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="课题类型" prop="type">
              <el-select
                :style="{ width: '100%' }"
                v-model="formData.type"
                placeholder="课题类型"
                disabled
              >
                <el-option label="开题报告" :value="1" />
                <el-option label="结题报告" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位名称" prop="creationUnit">
              <el-input
                v-model="formData.creationUnit"
                clearable
                placeholder="请输入单位名称"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登记时间" prop="registerTime">
              <el-date-picker
                style="width: 100%"
                v-model="formData.registerTime"
                placeholder="选择登记时间"
                type="datetime"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课题背景和意义" prop="background">
              <el-input
                v-model="formData.background"
                clearable
                type="textarea"
                rows="3"
                placeholder="请输入课题背景和意义"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课题研究目标" prop="target">
              <el-input
                v-model="formData.target"
                clearable
                type="textarea"
                rows="3"
                placeholder="请输入课题研究目标"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课题主要内容" prop="content">
              <el-input
                v-model="formData.content"
                clearable
                type="textarea"
                rows="3"
                placeholder="请输入课题主要内容"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课题初步提纲" prop="preliminaryOutline">
              <el-input
                v-model="formData.preliminaryOutline"
                clearable
                type="textarea"
                rows="3"
                placeholder="请输入课题初步提纲"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="拟采取的研究方法和手段" prop="proposedMethod">
              <el-input
                v-model="formData.proposedMethod"
                clearable
                type="textarea"
                rows="3"
                placeholder="请输入拟采取的研究方法和手段"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="研究工作进度安排" prop="studyScheduling">
              <el-input
                v-model="formData.studyScheduling"
                clearable
                type="textarea"
                rows="3"
                placeholder="请输入研究工作进度安排"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="研究工作人员安排" prop="studyPersonnelArrange">
              <el-input
                v-model="formData.studyPersonnelArrange"
                clearable
                type="textarea"
                rows="3"
                placeholder="请输入研究工作人员安排"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="需总部统筹支持事项" prop="supportIssues">
              <el-input
                v-model="formData.supportIssues"
                clearable
                type="textarea"
                rows="3"
                placeholder="请输入需总部统筹支持事项"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="课题成果" prop="subjectOutcome">
              <el-input
                v-model="formData.subjectOutcome"
                clearable
                type="textarea"
                rows="3"
                placeholder="请输入课题成果"
                :style="{ width: '100%' }"
                :disabled="formData.type == 1"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="其他" prop="other">
              <el-input
                v-model="formData.other"
                clearable
                type="textarea"
                rows="6"
                placeholder="请输入其他"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>

          <!-- <el-col :span="12">
            <el-form-item label="课题名称" prop="subjectManagementName">
              <el-input
                v-model="formData.subjectManagementName"
                clearable
                placeholder="请输入课题名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>


          <el-col :span="12">
            <el-form-item label="创建单位" prop="creationUnit">
              <el-input
                v-model="formData.creationUnit"
                clearable
                placeholder="请输入发布单位"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关键字" prop="keyword">
              <el-input
                v-model="formData.keyword"
                clearable
                placeholder="请输入发布单位"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="概要" prop="summary">
              <el-input
                type="textarea"
                :rows="2"
                placeholder="请输入所属单位"
                v-model="formData.summary"
                :disabled="!footer"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                type="textarea"
                :rows="2"
                placeholder="请输入所属单位"
                v-model="formData.remark"
                :disabled="!footer"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人" prop="subjectManagementCreator">
              <el-input
                v-model="formData.subjectManagementCreator"
                clearable
                placeholder="请输入发布单位"
                :disabled="!footer"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间" prop="registerTime">
              <el-date-picker
                style="width: 100%"
                v-model="formData.registerTime"
                placeholder="选择拟稿时间"
                type="datetime"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
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
                :on-success="handleAvatarSuccess"
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
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { addKTGL, getKTGLDefaultInfo } from '@/api/fwgl/pfpx'
  import { deleteFile, uploadApi, download } from '@/api/fwgl/zzxx'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { baseURL } from '@/config'
  import { formatDate } from '@/utils'
  import store from '@/store'
  const token = store.getters['user/token']
  export default {
    props: ['title'],
    data() {
      return {
        dialogFormVisible: false,
        title: '新增',
        footer: true,
        formData: {
          type: '',
        },
        rules: {
          subjectManagementName: [
            { required: true, message: '请输入课题名称', trigger: 'blur' },
          ],
          creationUnit: [
            { required: true, message: '请输入创建单位', trigger: 'blur' },
          ],
          registerTime: [
            { required: true, message: '请选择登记时间', trigger: 'blur' },
          ],
          background: [
            { required: true, message: '请输入背景', trigger: 'blur' },
          ],
          target: [{ required: true, message: '请输入目标', trigger: 'blur' }],
          content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
          preliminaryOutline: [
            { required: true, message: '请输入初步概要', trigger: 'blur' },
          ],
          proposedMethod: [
            { required: true, message: '请输入提出方法', trigger: 'blur' },
          ],
          studyScheduling: [
            { required: true, message: '请输入研究进度安排', trigger: 'blur' },
          ],
          studyPersonnelArrange: [
            { required: true, message: '请输入研究人员安排', trigger: 'blur' },
          ],
          supportIssues: [
            {
              required: true,
              message: '请输入需总部统筹支持事项',
              trigger: 'blur',
            },
          ],
        },
        tableData: [],
        fileList: [],
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
      }
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
        this.formData = {}
        this.fileList = []
        //
        if (row) {
          getKTGLDefaultInfo({
            id: row.subjectManagementId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.activityManagement)
            if (title == 'end') {
              this.formData.type = 2
            }
            if (title == 'detail') {
              this.formData.type = 1
            }
            if (res.data.files) {
              this.fileList = res.data.files
            }
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.$nextTick(() => {
            this.$refs('ruleForm').clearValidate()
          })
          this.title = '详细'
          this.footer = false
        } else if (title == 'end') {
          this.title = '结题'
          this.formData.type = 2
        } else {
          this.title = '新增'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.creationUnit = userInfo.linkOrg.orgname
          this.formData.registerTime = formatDate(new Date())
          this.formData.type = 1
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.formData = {
          type: '',
        }
        this.dialogFormVisible = false

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
            // this.$emit('add', this.formData)
            addKTGL(this.formData).then((res) => {
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
    },
  }
</script>
