<template>
  <div>
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
          <el-form-item
            label="审计项目名称"
            label-width="140px"
            prop="projectname"
            :rules="[
              {
                required: true,
                message: '请选择审计项目名称',
                trigger: ['change', 'blur'],
              },
            ]"
          >
            <el-input
              v-model="formData.projectname"
              disabled
              placeholder="请选择"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="$refs.project.showEdit()">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计通知书编号"
            label-width="140px"
            prop="advicecoed"
          >
            <el-input
              v-model="formData.advicecoed"
              clearable
              placeholder="请输入审计通知书编号"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="$refs.list.show()">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计通知书名称"
            label-width="140px"
            prop="advicename"
          >
            <el-input
              v-model="formData.advicename"
              clearable
              placeholder="请输入审计通知书名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计项目实施部门"
            label-width="140px"
            prop="department"
          >
            <el-input
              v-model="formData.department"
              disabled
              placeholder="请输入"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="$refs.department.show()">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="被审计单位全称"
            label-width="140px"
            prop="orgName"
          >
            <el-input
              v-model="formData.orgName"
              disabled
              placeholder="请输入"
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="$refs.unit.showEdit()">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计实施时间"
            label-width="140px"
            prop="sjsstime"
          >
            <el-date-picker
              v-model="formData.sjsstime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
              placeholder="选择审计实施时间"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组长" label-width="140px" prop="teamleader">
            <el-input
              v-model="formData.teamleader"
              clearable
              placeholder="请选择"
              disabled
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="selectPeople('teamleader')">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="副组长" label-width="140px" prop="fznames">
            <el-input
              v-model="formData.fznames"
              clearable
              placeholder="请选择"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主审" label-width="140px" prop="mainreviewer">
            <el-input
              v-model="formData.mainreviewer"
              clearable
              placeholder="请选择"
              disabled
              :style="{ width: '100%' }"
            />
            <!-- <el-button type="primary" @click="selectPeople('mainreviewer')">
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="助审" label-width="140px" prop="helpreviewer">
            <el-input
              v-model="formData.helpreviewer"
              clearable
              placeholder="请选择"
              disabled
              :style="{ width: '100%' }"
            />
            <!-- <el-button type="primary" @click="selectPeople('helpreviewer')">
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办人" label-width="140px" prop="operator">
            <el-input
              v-model="formData.operator"
              clearable
              placeholder="请选择"
              disabled
              :style="{ width: '80%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="selectPeople('operator')">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="内容" prop="content">
            <UEditor ref="ueditor" v-model="formData.content" :height="300" />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="24">
          <el-form-item label="审批意见" prop="proposal">
            <el-input
              type="textarea"
              placeholder="请输入审批意见"
              :rows="5"
              v-model="formData.proposal"
            ></el-input>
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-divider>变更信息</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="tbTable">
            <el-table-column align="center" label="变更事项" prop="changething">
              <template #default="{ row }">
                <el-button type="text" @click="$refs.bg.showEdit(row, true)">
                  {{ row.changething }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="变更原因"
              prop="changereason"
            />
            <el-table-column
              align="center"
              label="变更前内容"
              prop="changebefore"
            />
            <el-table-column
              align="center"
              label="变更后内容"
              prop="changeafter"
            />
          </el-table>
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
          <el-table :data="tableDataFile">
            <el-table-column align="center" label="附件名称" prop="name" />
            <el-table-column align="center" label="文件大小(KB)" prop="size" />
            <el-table-column
              align="center"
              label="创建人"
              prop="createPerson"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
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

    <!-- <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" v-if="!disabled" @click="save">确定</el-button>
    </div> -->
    <div style="text-align: right; margin-top: 10px">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save" v-if="!disabled">确 定</el-button>
      <el-button type="primary" @click="ymsubmit" v-if="!disabled">
        提 交
      </el-button>
    </div>
    <oaList ref="oaList" @selected="handleOA" />
    <!-- 选择人员弹窗 -->
    <!-- <executor-options ref="executor" @selected="handleExecutorSelected" /> -->
    <!-- 部门 -->
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <!-- 单位选择 -->
    <SelectDepartment ref="unit" @submit="handleUnitSelected" />
    <danxuanPerson
      ref="danxuanPerson"
      @projectManage="handleExecutorSelected"
    />
    <sjtzspList ref="project" @selected="projectSelect" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <projectList ref="list" @selected="listSelect" />
  </div>
</template>

<script>
  // import Tinymce from '@/components/Tinymce'
  import danxuanPerson from '@/components/danxuanPerson.vue'

  import { getOaurl } from '@/oapi/contract/manage'
  import { download } from '@/oapi/audit/implement'
  import ExecutorOptions from '@/views/oilAudit/report/components/options/executor.vue'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import oaList from '@/views/oilAudit/prepare/components/oaList.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import NoticeBiangengView from '@/views/oilAudit/prepare/components/noticeBiangengView.vue'
  import sjtzspList from '@/views/oilAudit/prepare/components/sjtzspList.vue'
  import { deleteReportFile } from '@/oapi/audit/report'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import {
    createNoticeCode,
    deleteNoticeFile,
    getNoticeDefaultData,
    noticeAdd,
    noticeFileList,
    addNotice,
    notice_disp,
    getbglist,
  } from '@/oapi/audit/preparation'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import * as dayjs from 'dayjs'
  const { baseURL } = require('@/config')
  import projectList from '@/views/oilAudit/prepare/components/options/projectList.vue'
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
      SelectDepartment,
      Resubmit,
      danxuanPerson,
      NoticeBiangengView,
      sjtzspList,
      projectList,
    },
    data() {
      return {
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        footerFlag: false,
        formData: {
          adviceaprid: '',
          advicecoed: '',
          advicename: '',
          adviceid: '',
          attids: '',
          helpreviewer: '',
          mainreviewer: '',
          operator: '',
          progectid: '',
          proposal: '',
          sjsstime: '',
          teamleader: '',
          content: '',
          title: '',
          oaid: '',
          h5url: '',
          url: '',
          orgName: '',
          orgid: '',
          department: '',
          orgids: '',
          projectname: '',
        },
        templates: [],
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        disabled: false,
        rules: {},
        dialogFormVisible: false,
        title: '',
        adviceid: '',
        rules: {
          advicecoed: [
            { required: true, message: '请输入通知书编号', trigger: 'change' },
          ],
          advicename: [
            { required: true, message: '请输入通知书名称', trigger: 'change' },
          ],
          helpreviewer: [
            {
              required: true,
              message: '请选择审计助审',
              trigger: ['change', 'blur'],
            },
          ],
          mainreviewer: [
            {
              required: true,
              message: '请选择审计主审',
              trigger: ['change', 'blur'],
            },
          ],
          operator: [
            {
              required: true,
              message: '请选择经办人',
              trigger: ['change', 'blur'],
            },
          ],
          progectid: [
            {
              required: false,
              message: '请选择审计组长',
              trigger: ['change', 'blur'],
            },
          ],
          proposal: [
            {
              required: true,
              message: '请选择审批意见',
              trigger: 'blur',
            },
          ],
          sjsstime: [
            {
              required: true,
              message: '请选择审计实施时间',
              trigger: ['change', 'blur'],
            },
          ],
          teamleader: [
            {
              required: true,
              message: '请选择审计助审',
              trigger: ['change', 'blur'],
            },
          ],
        },
        selectPeopleType: undefined,
        // 流程相关
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
        tbTable: [],
      }
    },
    computed: {},
    watch: {
      'formData.content'(val) {
        if (this.$refs['ueditor'].editor.openTemplate) {
          this.$refs['ueditor'].editor.openTemplate = false
          let s = val
          const arr = [
            ['$[contract.contractno]', 'contractno'],
            ['$[contract.contractname]', 'contractname'],
            ['$[contract.contractamount]', 'contractmoney'],
            ['$[contract.contractItem]', 'contractitem'],
            ['$[contract.executor]', 'realname'],
            ['$[contract.rmbinwords]', 'hzsumowing'],

            ['$[counterpart.coupersion]', 'counterpartcode'],
            ['$[counterpart.personincharge]', 'contractbd'],
            ['$[counterpart.counterpartHank]', 'bankkhyh'],
            ['$[counterpart.counumber]', 'counterpartno'],
            ['$[counterpart.couname]', 'budgetname'],
            ['$[counterpart.couaddress]', 'counterpartaddress'],
            ['$[counterpart.coupersion]', 'contacts'],
            ['$[counterpart.contactsPhone]', 'contactsphone'],
            ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
            ['$[counterpart.legarepresentative]', 'contacts'],
            ['$[counterpart.pctelephonenumber]', 'contractzd'],
            // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
          ]
          arr.forEach((i) => {
            if (this.formData[i[1]]) {
              s = s.replace(i[0], this.formData[i[1]])
            }
          })
          this.formData.content = s
        }
      },
    },
    created() {},
    mounted() {},
    methods: {
      selectPeople(type) {
        this.selectPeopleType = type
        this.$refs.danxuanPerson.showEdit()
      },
      // 选择经办人
      handleExecutorSelected(node) {
        if (this.selectPeopleType == 'teamleader') {
          this.$set(this.formData, 'teamleader', node[0].realname)
          this.$set(this.formData, 'zcstaffid', node[0].staffid)
        } else if (this.selectPeopleType == 'mainreviewer') {
          this.$set(this.formData, 'mainreviewer', node[0].realname)
          this.$set(this.formData, 'zsstaffid', node[0].staffid)
        } else if (this.selectPeopleType == 'helpreviewer') {
          this.$set(this.formData, 'helpreviewer', node[0].realname)
          this.$set(this.formData, 'assistapproverid', node[0].staffid)
        } else if (this.selectPeopleType == 'operator') {
          this.$set(this.formData, 'operator', node[0].realname)
        }
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showEdit(
        row,
        disabled,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        console.log(disabled, 'disabled')
        this.disabled = disabled != 'edit'
        //流程相关参数
        this.fromId = row.adviceid
        this.fromIdcopy = row.adviceid
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        //新建按钮入口
        if (!row && !disabled) {
          // createNoticeCode().then((res) => {
          //   this.$set(this.formData, 'advicecoed', res.data.autoCode.toString())
          // })
          //新建
          this.title = '新建'
          this.disabled = false
          this.tableDataFile = []
          this.fileIds = []
          this.fileList = []
        } else if (row) {
          this.getbglist(row.adviceid)
          const { data } = await notice_disp({
            adviceid: row.adviceid,
          })
          Object.assign(this.formData, data.advice)
          this.formData.sjsstime = data.advice.sjsstime
            ? dayjs(data.advice.sjsstime).format('YYYY-MM-DD')
            : ''
          this.formData.orgids = data.advice?.organization?.orgid
          //回填上传文件表格
          const arr = data.advice.tblNoteAtts
          const arr1 = arr.map((item) => {
            return {
              ...item,
              name: item.attname,
              size: item.attsize,
              createPerson: item.uploader,
            }
          })
          const arr2 = arr.map((res) => {
            return res.attid
          })
          //收集id
          this.fileIds = arr2
          this.tableDataFile = arr1
          //收集id
          this.fileIds = arr2
          this.tableDataFile = arr1
        }
        this.title = disabled ? '详细' : '修改'
        this.dialogFormVisible = true
      },
      async getbglist(id) {
        const {
          data: { list },
        } = await getbglist({
          adviceid: id,
        })
        this.tbTable = list || []
      },
      projectSelect(val) {
        let data = val[0]
        this.formData.adviceaprid = data.adviceid
        delete data.adviceid
        Object.assign(this.formData, data)
        this.formData.sjsstime = dayjs(data.sjsstime).format('YYYY-MM-DD')
        this.formData.assistApproverId = data.assistApproverId
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
      //     this.tableDataFile.push(arr1)

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
      handleDeleteFile(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          let res = await deleteReportFile({ attId: row.attid })
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
      close() {
        this.$refs['elForm'].resetFields()
        this.$bus.$emit('updateMsg', 0)
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let fileIds = ''
            this.tableDataFile.map((item) => {
              fileIds += item.attid
              fileIds += ','
            })
            delete this.formData.organization
            delete this.formData.tblNoteAtts
            fileIds = fileIds.substring(0, fileIds.length - 1)
            let { code, msg } = await addNotice({
              ...this.formData,
              adviceid: this.formData.adviceid,
              attids: fileIds || '',
            })
            if (code == 1) {
              this.$baseMessage(
                '保存成功',
                'success',
                'vab-hey-message-success'
              )
              // this.close()
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
          }
        })
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
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        // 触发点击
        document.body.appendChild(link)
        link.click()
        // 移除
        document.body.removeChild(link)
      },
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
      // 部门
      handleDepartmentSelected(node) {
        // //单位（科室）名称
        this.$set(this.formData, 'department', node.label)
      },
      /**
       * @description: 选择单位
       * @param {*}
       * @return {*}
       */
      handleUnitSelected(node) {
        console.log('node', node)
        this.formData.orgName = node.label
        this.formData.orgids = node.id
        // this.formData.unitRangeId = node.id
      },
      async ymsubmit() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      listSelect(val) {
        this.formData.advicecoed = val.advicecoed
        this.formData.advicename = val.advicename
        this.tableData = val.tblNoteAtts
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
