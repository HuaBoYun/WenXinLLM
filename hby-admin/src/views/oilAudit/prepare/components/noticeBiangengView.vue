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
        ref="elForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="disabled"
      >
        <el-col :span="12">
          <el-form-item label="单位（科室）" label-width="140px" prop="orgName">
            <el-input
              v-model="formData.orgName"
              clearable
              disabled
              placeholder="请选择"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="$refs.department.showEdit()">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="关联审计通知"
            label-width="140px"
            prop="advicename"
          >
            <el-input
              v-model="formData.advicename"
              clearable
              disabled
              placeholder="请选择"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="$refs.project.show()">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item
            label="关联审计通知"
            label-width="140px"
            prop="progectid"
          >
            <el-input
              v-model="formData.projectName"
              clearable
              disabled
              placeholder="请选择"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="$refs.sjtz.show()">
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="经办人" label-width="140px" prop="jbr">
            <el-input
              v-model="formData.jbr"
              clearable
              disabled
              placeholder="请选择经办人"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="selectPeople('jbr')">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="width: 100%"></el-col>
        <el-col :span="24">
          <el-form-item label="变更事项" label-width="140px" prop="changething">
            <el-input
              type="textarea"
              placeholder="请输入变更事项"
              :rows="5"
              v-model="formData.changething"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="变更前内容"
            label-width="140px"
            prop="changebefore"
          >
            <el-input
              type="textarea"
              placeholder="请输入变更前内容"
              :rows="5"
              v-model="formData.changebefore"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="变更后内容"
            label-width="140px"
            prop="changeafter"
          >
            <el-input
              type="textarea"
              placeholder="请输入变更后内容"
              :rows="5"
              v-model="formData.changeafter"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="变更原因"
            label-width="140px"
            prop="changereason"
          >
            <el-input
              type="textarea"
              placeholder="请输入变更原因"
              :rows="5"
              v-model="formData.changereason"
            ></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <!-- <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :action="baseApi + api"
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

    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button
        type="primary"
        v-if="!disabled"
        @click="save"
        :loading="loading"
      >
        确定
      </el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
    <!-- <oaList ref="oaList" @selected="handleOA" /> -->
    <!-- 选择人员弹窗 -->
    <!-- <executor-options ref="executor" @selected="handleExecutorSelected" /> -->
    <danxuanPerson
      ref="danxuanPerson"
      @projectManage="handleExecutorSelected"
    />
    <ProcessList ref="process" @fetchData="close" />
    <!-- 部门 -->
    <department-options ref="department" @submit="handleDepartmentSelected" />
    <!-- 项目 -->
    <project-options ref="project" @selected="handleProjectSelected" />
    <!-- 审计通知 -->
    <RelateSjtz ref="sjtz" @selected="handleSjtzSelected" />
  </el-dialog>
</template>

<script>
  // import Tinymce from '@/components/Tinymce'
  import { getOaurl } from '@/oapi/contract/manage'
  import { download } from '@/oapi/audit/implement'
  import danxuanPerson from '@/components/danxuanPerson.vue'
  import ExecutorOptions from '@/views/oilAudit/report/components/options/executor.vue'
  import DepartmentOptions from '@/views/oilAudit/jhlx/components/department.vue'
  import ProjectOptions from '@/views/oilAudit/prepare/components/options/projectList.vue'
  import oaList from './oaList.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { NoticechangeAdd, NoticechangeDisp } from '@/oapi/audit/preparation'
  import RelateSjtz from '@/views/oilAudit/prepare/components/options/relateSJTZ.vue'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'ProjectDataInfo',
    inheritAttrs: false,
    props: [],
    components: {
      oaList,
      UEditor,
      ExecutorOptions,
      DepartmentOptions,
      ProjectOptions,
      RelateSjtz,
      danxuanPerson,
      ProcessList,
    },
    data() {
      return {
        // baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        footerFlag: false,
        formData: {
          advicename: '',
          adviceid: '',
          attids: '',
          changeafter: '',
          changebefore: '',
          changeid: '',
          changereason: '',
          changething: '',
          jbr: '',
          orgid: '',
          progectid: '',
          projectName: '',
          orgName: '',
        },
        templates: [],
        fileList: [],
        tableData: [],
        fileIds: [],
        disabled: false,
        loading: false,
        rules: {},
        dialogFormVisible: false,
        title: '',
        adviceid: '',

        rules: {
          orgid: [
            {
              required: true,
              message: '请选择单位（科室）',
              trigger: ['change', 'blur'],
            },
          ],
          advicename: [
            {
              required: false,
              message: '请选择关联审计通知',
              trigger: ['change', 'blur'],
            },
          ],
          changething: [
            {
              required: true,
              message: '请输入变更事项',
              trigger: 'blur',
            },
          ],
          changebefore: [
            {
              required: true,
              message: '请输入变更前内容',
              trigger: 'blur',
            },
          ],
          changeafter: [
            {
              required: true,
              message: '请输入变更后内容',
              trigger: 'blur',
            },
          ],
          changereason: [
            {
              required: true,
              message: '请输入变更原因',
              trigger: 'blur',
            },
          ],
          jbr: [
            {
              required: true,
              message: '请选择经办人',
              trigger: ['change', 'blur'],
            },
          ],
        },
        selectPeopleType: undefined,
        editId: '',
      }
    },
    methods: {
      selectPeople(type) {
        this.selectPeopleType = type
        this.$refs.danxuanPerson.showEdit()
      },
      // 选择经办人
      handleExecutorSelected(node) {
        console.log(node, 'jbr')
        if (this.selectPeopleType == 'teamleader') {
          this.$set(this.formData, 'teamleader', node[0].realname)
        } else if (this.selectPeopleType == 'mainreviewer') {
          this.$set(this.formData, 'mainreviewer', node[0].realname)
        } else if (this.selectPeopleType == 'helpreviewer') {
          this.$set(this.formData, 'helpreviewer', node[0].realname)
        } else if (this.selectPeopleType == 'jbr') {
          this.$set(this.formData, 'jbr', node[0].realname)
        }
        this.$forceUpdate()
      },
      handleProjectSelected(node) {
        console.log(node, 222)
        //项目名称
        this.$set(this.formData, `advicename`, node.advicename)
        //项目名称对应的ID
        this.$set(this.formData, `adviceid`, node.adviceid)
      },
      // 选择单位
      handleDepartmentSelected(node) {
        //单位（科室）名称
        this.formData.orgName = node.label
        //单位（科室）名称对应的ID
        this.formData.orgid = node.id
      },
      // 选择经办人
      // handleExecutorSelected(node) {
      //   this.$set(this.formData, 'jbr', node.realname)
      // },
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      async showEdit(row, disabled) {
        //新建按钮入口
        if (!row && !disabled) {
          //新建
          this.title = '新建'
          this.disabled = false
          this.tableData = []
          this.fileIds = []
          this.fileList = []
        } else if (row) {
          this.editId = row.changeid
          let res = await NoticechangeDisp({
            changeid: row.changeid,
          })
          let obj = res.data.Doubtfulpoint
          this.title = disabled ? '详细' : '修改'
          this.disabled = disabled
          Object.assign(this.formData, obj)
          this.formData.orgName = obj.organization.orgname
          this.tableData = obj.tblNoteAtts
        }
        this.dialogFormVisible = true
      },
      // handleSuccess(response, file, fileList) {
      //   if (file.response.result == '200') {
      //     file.createPerson = JSON.parse(
      //       localStorage.getItem('userInfo')
      //     ).realname
      //     let arr = file.response.data
      //     const arr1 = {
      //       name: arr.attname,
      //       size: arr.attsize,
      //       createPerson: arr.uploader,
      //       attid: arr.attid,
      //     }
      //     this.tableData.push(arr1)

      //     this.fileList = fileList
      //     let fileArr = []
      //     this.fileList.forEach((item) => {
      //       fileArr.push(item.response.data.attid)
      //     })
      //     this.fileIds = [...this.fileIds, ...fileArr]
      //     this.$baseMessage(file.response.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.response.msg, 'error')
      //   }
      // },
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
            this.tableData.splice(index, 1)
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      close() {
        this.formData.advicename = ''
        this.formData.adviceid = ''
        this.formData.attids = ''
        this.formData.changeafter = ''
        this.formData.changebefore = ''
        this.formData.changeid = ''
        this.formData.changereason = ''
        this.formData.changething = ''
        this.formData.jbr = ''
        this.formData.orgid = ''
        this.formData.progectid = ''
        this.formData.projectName = ''
        this.$refs['elForm'].resetFields()
        this.tableData = []
        this.dialogFormVisible = false
        this.editId = ''
        this.$emit('fetch-data')
      },
      save() {
        if (this.loading) return
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let fileIds = ''
            this.tableData.map((item) => {
              fileIds += item.attid
              fileIds += ','
            })
            fileIds = fileIds.substring(0, fileIds.length - 1)
            delete this.formData.creatrtime
            delete this.formData.organization
            delete this.formData.tblYqnsAdvicenote
            delete this.formData.tblNoteAtts
            let { code, msg, data } = await NoticechangeAdd({
              ...this.formData,
              attids: fileIds,
            })
            if (code == 1) {
              this.$baseMessage(
                '保存成功',
                'success',
                'vab-hey-message-success'
              )
              this.editId = data.Doubtfulpoint.changeid
              this.formData.changeid = data.Doubtfulpoint.changeid
              this.$emit('fetch-data')
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
            this.loading = false
          }
        })
      },
      // async handleDownload(row) {
      //   const res = await download({ attId: row.attid })
      //   this.downloadFileByBlob(res, row.name)
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
      openOA() {
        this.$refs.oaList.show()
      },
      handleOA(e) {
        this.formData.title = e[0].subject
        this.formData.oaid = e[0].id
        this.formData.h5url = e[0].h5Url
        this.formData.url = e[0].url
      },
      async detail() {
        const res = await getOaurl()
        this.ticket = res.data.ticket
        this.oaurl = res.data.oaurl.substring(0, res.data.oaurl.length - 1)
        window.open(`${this.oaurl}${this.formData.url}&ticket=${this.ticket}`)
      },
      handleSjtzSelected() {},
      handleApproval() {
        //提交审批
        this.$refs['process'].save(171, this.editId)
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
  ::v-deep .el-textarea__inner {
    resize: none;
  }
</style>
