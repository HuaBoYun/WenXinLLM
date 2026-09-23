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
        :disabled="allDisabled"
      >
        <el-col :span="12">
          <el-form-item label="报告名称" prop="reportName">
            <el-input
              v-model.trim="formData.reportName"
              placeholder="请输入报告名称"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="层级" prop="hierarchy">
            <el-input
              v-model="formData.hierarchy"
              clearable
              placeholder="请输入层级"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="报告类型" prop="reporttType">
            <!-- <el-select
              v-model="formData.reporttType"
              placeholder="请选择报告类型"
              :style="{ width: '100%' }"
            >
              <el-option label="类型1" :value="1"></el-option>
              <el-option label="类型2" :value="2"></el-option>
              <el-option label="类型3" :value="3"></el-option>
            </el-select> -->
            <el-input
              v-model.trim="formData.reporttType"
              placeholder="请输入报告类型"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="报告阶段" prop="reportStage">
            <el-select
              v-model="formData.reportStage"
              placeholder="请选择报告阶段"
              :style="{ width: '100%' }"
            >
              <el-option label="拟定" :value="1"></el-option>
              <el-option label="审批" :value="2"></el-option>
              <el-option label="发布" :value="3"></el-option>
            </el-select>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="部门负责人" prop="departmentHeadName">
            <el-input
              v-model.trim="formData.departmentHeadName"
              placeholder="请选择部门负责人"
              style="width: 75%"
              disabled
            />
            <el-button
              @click="projectManager"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="拟稿人" prop="draftsmanName">
            <el-input
              v-model.trim="formData.draftsmanName"
              placeholder="请选择拟稿人"
              style="width: 75%"
              disabled
            />
            <el-button
              @click="projectManager1"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              :disabled="allDisabled"
              style="margin-left: -100px"
              template="HGBG"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!allDisabled"
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
          <el-table :data="tableDataFile">
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
              <template #default="{ $index, row }">
                <!-- <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button> -->
                <el-button
                  type="text"
                  @click="handleDown(row)"
                  :disabled="false"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row, $index)"
                  v-if="!allDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <project-manage1
      @projectManage="getChildlistPro1"
      ref="manage1"
    ></project-manage1>

    <template #footer v-if="title != '详情'">
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="add">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    editReport,
    getReportDefaultInfo,
    getOrgMain,
    download,
  } from '@/api/hggl/hgjhgl'
  import projectManage from './selectPerson.vue'
  import projectManage1 from './selectPerson.vue'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  export default {
    name: '',
    components: { UEditor, projectManage, projectManage1 },
    data() {
      return {
        baseApi: baseURL,
        api: '/hggl/api-auth/fileManage/upload',
        headers: { token: token },
        loading: false,
        templates: [],
        fileList: [],
        formData: {
          draftsman: '',
          draftsmanName: '',
          departmentHead: '',
          departmentHeadName: '',
          // hierarchy: '',
          id: '',
          reportName: '',
          // reportStage: '',
          reporttType: '',
          content: '',
        },
        rules: {
          reportName: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        tableDataFile: [],
        allDisabled: false,
        fileIdList: [],
      }
    },
    computed: {},

    created() {},
    mounted() {},
    methods: {
      async showEdit(title, row) {
        this.dialogFormVisible = true
        this.title = title
        this.allDisabled = title == '详情'
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.formData.draftsmanName = userInfo.realname
        this.formData.draftsman = userInfo.staffid
        //部门负责人等接口
        const org = { orgid: userInfo.linkDetp.orgid }
        const res = await getOrgMain(org)
        console.log(res, 'res')
        this.formData.departmentHeadName = res.data.realname
        this.formData.departmentHead = res.data.staffid
        if (row) {
          getReportDefaultInfo({
            id: row.id,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.report)
            this.tableDataFile = res.data.file || []
          })
        }
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      projectManager1() {
        this.$refs['manage1'].showEdit()
      },

      close() {
        this.formData = {
          draftsman: '',
          draftsmanName: '',
          departmentHead: '',
          departmentHeadName: '',
          id: '',
          reportName: '',
          reporttType: '',
          content: '',
        }
        this.$refs['ruleForm'].resetFields()
        this.dialogFormVisible = false
        this.fileIdList = []
        this.tableDataFile = []
        this.allDisabled = false
      },
      handleSuccess(e) {
        console.log(e)
        if (e.code == 200) {
          // this.showEdit(this.showRow)
          let attInfo = {}
          attInfo = e.data.fileIds[0]
          this.tableDataFile.push(attInfo)
          console.log(this.tableDataFile)
          this.$baseMessage('上传成功', 'success', 'vab-hey-message-success')
        } else {
          this.$baseMessage('上传失败', 'error', 'vab-hey-message-error')
        }
        this.uploadLoading = false
      },
      getChildlistPro(val) {
        const names = val.map((res) => res.realname).toString()
        const ids = val.map((res) => res.staffid).toString()

        this.$set(this.formData, 'departmentHeadName', names)
        this.$set(this.formData, 'departmentHead', ids)
      },
      getChildlistPro1(val) {
        const names = val.map((res) => res.realname).toString()
        const ids = val.map((res) => res.staffid).toString()
        this.$set(this.formData, 'draftsmanName', names)
        this.$set(this.formData, 'draftsman', ids)
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            editReport({
              ...this.formData,
              fileIds: this.tableDataFile.map((x) => x.fileId).join(','),
            }).then((res) => {
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
      handleDelete(row, index) {
        this.$baseConfirm('你确定要删除当前项吗', null, () => {
          //删除formData要返回给后端的id
          this.tableDataFile.splice(index, 1)
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
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
