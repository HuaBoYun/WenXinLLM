<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="210px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="索引号" prop="indexno">
            <el-input
              v-model="formData.indexno"
              clearable
              placeholder="请输入索引号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编号" prop="no">
            <el-input
              v-model="formData.no"
              clearable
              placeholder="请输入索引号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位名称" prop="auditeeName">
            <el-input
              v-model="formData.auditeeName"
              clearable
              placeholder="请选择被审计单位名称"
              :style="{ width: '196px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
              size="small"
              disabled
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计实施时间" prop="implementationTime">
            <el-date-picker
              v-model="formData.implementationTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="请选择审计实施时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入审计项目名称"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="分工负责审计内容" prop="responsibleContent">
            <el-input
              v-model="formData.responsibleContent"
              clearable
              placeholder="请输入分工负责审计内容"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="7"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="审计内容和目标" prop="contentObjectives">
            <el-input
              v-model="formData.contentObjectives"
              clearable
              placeholder="请输入审计内容和目标"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="7"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="执行的审计程序和工作过程"
            prop="executedProceduresProcesses"
          >
            <el-input
              v-model="formData.executedProceduresProcesses"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入执行的审计程序和工作过程"
              type="textarea"
              :rows="7"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="发现的疑点、线索及查证情况"
            prop="verificationSituation"
          >
            <el-input
              v-model="formData.verificationSituation"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入发现的疑点、线索及查证情况"
              type="textarea"
              :rows="7"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计线索及数据来源" prop="cluesSources">
            <el-input
              v-model="formData.cluesSources"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入审计线索及数据来源"
              type="textarea"
              :rows="7"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              disabled
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <!-- <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :action="baseApi + uploadApi"
            :headers="headers"
            :on-success="handleSuccess"
            :show-file-list="false"
            multiple
            :file-list="fileList"
            :before-upload="handleBeforeUpload"
          >
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">上传</el-button>
            </div>
          </el-upload> -->
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
          <el-table :data="tableDataFile">
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
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFile(scope.row)"
                  :disabled="false"
                >
                  预览
                </el-button>
                <el-button
                  v-if="!disabled"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row)"
                >
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
      <el-button @click="save" type="primary">确定</el-button>
    </div> -->
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save" v-if="footer">确 定</el-button>
      <el-button
        type="primary"
        @click="ymsubmit"
        v-if="footer"
        :disabled="btnLoading"
      >
        提 交
      </el-button>
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
    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import { download } from '@/oapi/audit/implement'
  import {
    workRecordsDetail,
    workRecordsSaveOrUpdate,
  } from '@/oapi/audit/implement'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const token = store.getters['user/token']
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { DepartmentOptions, Resubmit },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        baseApi: baseURL,
        loading: false,
        // uploadApi: '/oiaudit/fileManage/upload',
        headers: { token: token },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        tableData: [],
        formData: {
          id: '',
          auditeeName: undefined,
          implementationTime: undefined,
          projectName: undefined,
          responsibleContent: undefined,
          contentObjectives: undefined,
          executedProceduresProcesses: undefined,
          verificationSituation: undefined,
          cluesSources: undefined,
          createUser: undefined,
          createTime: undefined,
          indexno: undefined,
        },
        footer: true,
        rules: {
          indexno: [
            {
              required: true,
              message: '请输入索引号',
              trigger: 'blur',
            },
          ],
          auditeeName: [
            {
              required: true,
              message: '请输入被审计单位名称',
              trigger: 'blur',
            },
          ],
          implementationTime: [
            {
              required: true,
              message: '请输入原职务实施审计时间',
              trigger: 'blur',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          responsibleContent: [
            {
              required: true,
              message: '请输入分工负责审计内容',
              trigger: 'blur',
            },
          ],
          contentObjectives: [
            {
              required: true,
              message: '请输入审计内容和目标',
              trigger: 'blur',
            },
          ],
          executedProceduresProcesses: [
            {
              required: true,
              message: '请输入执行的审计程序和工作过程',
              trigger: 'blur',
            },
          ],
          verificationSituation: [
            {
              required: true,
              message: '请输入发现的疑点、线索及查证情况',
              trigger: 'blur',
            },
          ],
          cluesSources: [
            {
              required: true,
              message: '请输入审计线索及数据来源',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        // 流程相关
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
        btnLoading: false,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      // async handleDownload(row) {
      //   const res = await download({ attId: row.attid })
      //   this.downloadFileByBlob(res, row.name)
      // },
      // downloadFileByBlob(blob, fileName = 'file') {
      //   let blobUrl = window.URL.createObjectURL(blob)
      //   let link = document.createElement('a')
      //   link.download = fileName || 'defaultName'
      //   link.style.display = 'none'
      //   link.href = blobUrl
      //   // 触发点击
      //   document.body.appendChild(link)
      //   link.click()
      //   // 移除
      //   document.body.removeChild(link)
      // },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // async handlePreviewFile(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   const url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   this.$iFrameDialog({ iframeUrl: url })
      // },
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      handleSuccess(response, file, fileList) {
        if (file.response.result == '200') {
          file.createPerson = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          let arr = file.response.data
          this.tableDataFile.push(arr)

          this.fileList = fileList
          let fileArr = []
          this.fileList.forEach((item) => {
            fileArr.push(item.response.data.attid)
          })
          this.fileIds = [...this.fileIds, ...fileArr]
          this.$baseMessage(file.response.msg, 'success')
        } else {
          this.$baseMessage(file.response.msg, 'error')
        }
      },
      handleDeleteFile(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          let res = await deleteNoticeFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.fileIds.splice(index, 1)
            this.tableDataFile.splice(index, 1)
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      async ymsubmit() {
        try {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      handleDepartmentSelected(node) {
        //保存名称和对应的ID
        this.$set(this.formData, `auditeeName`, node.label)
        this.$set(this.formData, `auditeeNameId`, node.id)
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.dialogFormVisible = true
        // 流程相关
        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (formId) {
          const res = await workRecordsDetail({ id: formId })
          Object.assign(this.formData, res.data)
          this.formData.createUser = res.data.realname
          this.tableDataFile = res.data.tblNoteAtts
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData.createUser = resL
          this.formData.createTime = new Date().toJSON().split('T')[0]
        }
      },
      close() {
        this.formData.id = ''
        this.formData.auditeeName = undefined
        this.formData.implementationTime = undefined
        this.formData.projectName = undefined
        this.formData.responsibleContent = undefined
        this.formData.contentObjectives = undefined
        this.formData.executedProceduresProcesses = undefined
        this.formData.verificationSituation = undefined
        this.formData.cluesSources = undefined
        this.formData.createUser = undefined
        this.formData.createTime = undefined
        this.formData.indexno = undefined
        this.dialogFormVisible = false
        this.footer = true
        this.fileList = []
        this.tableDataFile = []
        this.$bus.$emit('updateMsg', 0)
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = this.tableDataFile.map((x) => x.attid)
            attids = attids.join(',')
            delete this.formData.tblNoteAtts
            const data = await workRecordsSaveOrUpdate({
              ...this.formData,
              attids: attids,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetchData')
              // this.close()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          }
        })
      },
      async checkLink() {
        return
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
              })
            }
          }
        })
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
          this.tableDataFile = [...this.tableDataFile, ...file.data]
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
