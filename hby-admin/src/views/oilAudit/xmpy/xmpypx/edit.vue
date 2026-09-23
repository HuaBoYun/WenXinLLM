<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="项目名称" prop="implementationProjectName">
            <el-input
              v-model="formData.implementationProjectName"
              clearable
              placeholder="请输通知标题"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              @click="handleProject"
              style="margin-left: 15px"
              type="primary"
              size="mini"
              :disabled="!disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申报单位" prop="approvalBelongGroupName">
            <el-input
              v-model="formData.approvalBelongGroupName"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择申报单位"
            />
            <el-button
              @click="handleObject"
              style="margin-left: 15px"
              type="primary"
              size="mini"
              :disabled="!disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="完成单位" prop="completeBelongGroupName">
            <el-input
              v-model="formData.completeBelongGroupName"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择完成单位"
            />
            <el-button
              @click="handleObject1"
              style="margin-left: 15px"
              type="primary"
              size="mini"
              :disabled="!disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组长" prop="implementationPlanTeamLeader">
            <el-input
              v-model="formData.implementationPlanTeamLeader"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择组长"
            />
            <!-- <el-button
              type="primary"
              style="margin-left: 20px"
              @click="showPeople('implementationPlanTeamLeader')"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主审" prop="implementationPlanMainReviewer">
            <el-input
              v-model="formData.implementationPlanMainReviewer"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择主审"
            />
            <!-- <el-button
              type="primary"
              style="margin-left: 20px"
              @click="showPeople('implementationPlanMainReviewer')"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参审人" prop="implementationPlanReviewers">
            <el-input
              v-model="formData.implementationPlanReviewers"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择参审人"
            />
            <!-- <el-button
              type="primary"
              style="margin-left: 20px"
              @click="showPeople('implementationPlanReviewers')"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="主要特点" prop="mainfeatures">
            <el-input
              v-model="formData.mainfeatures"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入主要特点"
              type="textarea"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计成果" prop="content">
            <UEditor ref="ueditor" v-model="formData.content" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>分数</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="list" style="width: 100%">
            <el-table-column
              label="用户真实名"
              prop="userName"
              align="center"
            ></el-table-column>
            <el-table-column
              align="center"
              prop="userGrade"
              label="排序"
            ></el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <el-form-item label="计划科排序结果" prop="resultSort">
            <el-input
              v-model="formData.resultSort"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入主要特点"
              type="number"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <!-- <div style="text-align: right; margin-bottom: 5px" v-if="!disabled">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div> -->
          <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :show-file-list="false"
            action=""
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="handleSuccess"
            :file-list="fileList"
            :before-upload="handleBeforeUpload"
            :multiple="true"
          >
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDowns(row)">下载</el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFile(row)"
                  :disabled="false"
                >
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <SelectDepartment ref="audiTree1" @submit="getDepartmentInfo1" />
    <Project ref="project" @selected="getProject" />
    <!-- 选择人员弹窗 -->
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <template #footer v-if="disabled">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    jhyjgSaveOrUpdate,
    jhyjgList,
    jhyjgDetail,
    download,
    deleteReportFile,
  } from '@/oapi/audit/report'
  import { getXmpysbDetail, addOrUpdateXmpypx } from '@/oapi/audit/xmpy'
  import UEditor from '@/components/UEditor'
  import { implementPlanList } from '@/oapi/audit/project'
  import store from '@/store'
  import { formatDate, formatDay } from '@/utils/index'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import ExecutorOptions from '@/views/oilAudit/report/components/options/executor.vue'
  import Project from '@/views/oilAudit/xmpy/components/selectProject.vue'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'xmpysbEdit',
    inheritAttrs: false,
    props: [],
    components: { UEditor, SelectDepartment, ExecutorOptions, Project },
    data() {
      return {
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          approvalBelongGroupName: undefined,
          approvalBelongGroup: undefined,
          completeBelongGroupName: undefined,
          completeBelongGroup: undefined,
          implementationPlanTeamLeader: 'werw',
          // implementationPlanReviewers: [],
          implementationPlanMainReviewer: '23123',
          mainfeatures: undefined,
          content: undefined,
          implementationPlanId: '',
          id: '',
          resultSort: '',
        },
        auditProjectSelectList: [],
        tableData: [],
        disabled: true,
        rules: {
          title: [
            {
              required: true,
              message: '请输入标题',
              trigger: 'blur',
            },
          ],
          resultSort: [
            {
              required: true,
              message: '请输入计划科排序',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        peopleType: '',
        list: [],
      }
    },
    methods: {
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getXmpysbDetail({ id: row.id })
          this.tableData = res.data.file || []
          this.formData = res.data.data
          this.formData.implementationPlanReviewers =
            res.data.data.implementationPlanReviewers
              .map((res) => res.realName)
              .toString()
          this.list = res.data.data.reviewTeamList
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.disabled = false
        } else {
          this.title = '新增'
          this.disabled = true
          let userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData = {
            ...this.formData,
            cjr: userInfo.realname,
            cjsj: new Date().toJSON().split('T')[0],
          }
        }
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.disabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)
            let params = { ...this.formData }
            const data = await addOrUpdateXmpypx({
              // fileIds: attIds.toString(),
              resultSort: params.resultSort,
              id: params.id,
            })
            if (data.code == 200) {
              this.$baseMessage('保存成功', 'success')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
      },
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      handleDelete(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteReportFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableData
            list = list.filter((item) => item.attid != row.attid)
            this.tableData = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.result == '200') {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      handleObject1() {
        this.$refs['audiTree1'].showEdit()
      },
      handleProject() {
        this.$refs['project'].show()
      },
      getDepartmentInfo(val) {
        this.formData.approvalBelongGroup = val.id
        this.formData.approvalBelongGroupName = val.name
      },
      getDepartmentInfo1(val) {
        this.formData.completeBelongGroup = val.id
        this.formData.completeBelongGroupName = val.name
      },
      showPeople(type) {
        this.peopleType = type
        this.$refs.executor.show()
      },
      // 选择人员
      handleExecutorSelected(node) {
        if (this.peopleType == 'implementationPlanTeamLeader') {
          this.$set(
            this.formData,
            'implementationPlanTeamLeader',
            node.realname
          )
        } else if (this.peopleType == 'implementationPlanMainReviewer') {
          this.$set(
            this.formData,
            'implementationPlanMainReviewer',
            node.realname
          )
        } else if (this.peopleType == 'implementationPlanReviewers') {
          this.$set(this.formData, 'implementationPlanReviewers', node.realname)
        }
        this.$forceUpdate()
      },
      getProject(val) {
        this.formData.implementationProjectName = val.planName
        this.formData.implementationPlanId = val.id
        this.formData.implementationPlanReviewers = val.teamMembersList
          .map((res) => res.realName)
          .toString()
        this.formData.implementationPlanTeamLeader = val.teamLeaderName
        this.formData.implementationPlanMainReviewer = val.zsname
      },
      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }

        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccess(file) {
        if (file.code == 200) {
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          // 当文件是加密状态时，使用指定的在线预览链接
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
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
