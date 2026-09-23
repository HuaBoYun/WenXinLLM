<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="文号" prop="document">
            <el-input
              v-model="formData.document"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入文号"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="标题" prop="title">
            <el-input
              v-model="formData.title"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入标题"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请选择审计项目名称"
              style="width: 266px; height: 30px"
              disabled
            />
            <el-button
              @click="$refs.project.show()"
              style="margin-left: 10px; height: 30px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="cjr">
            <el-input
              v-model="formData.cjr"
              clearable
              placeholder="请输创建人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" label-width="140px" prop="cjsj">
            <el-date-picker
              v-model="formData.cjsj"
              placeholder="选择创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-form>
      <el-col :span="24">
        <el-divider>文件上传</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px" v-if="footer">
          <!-- <el-upload
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
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
        </div>
        <el-table :data="tableData">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDown(row)" :disabled="false">
                下载
              </el-button>
              <el-button
                type="text"
                @click="handlePreviewFile(row)"
                :disabled="false"
              >
                预览
              </el-button>
              <el-button type="text" @click="handleDelete(row)" v-if="footer">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <projectName ref="project" @selected="selectedPro" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import {
    jjzrsjjgbgSaveOrUpdate,
    jjzrsjjgbgDetail,
    download,
    deleteReportFile,
  } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import projectName from '@/views/oilAudit/report/components/options/projectName.vue'

  import { formatDate, formatDay } from '@/utils/index'
  const token = store.getters['user/token']
  import { implementPlanList } from '@/oapi/audit/project'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { projectName },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: { token: token },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        tableData: [],
        formData: {
          document: '',
          title: '',
          jjzrsjjgbgid: '',
          projectId: '',
          projectName: '',
        },
        radio: '',
        footer: true,
        rules: {
          document: [
            {
              required: true,
              message: '请输入文号',
              trigger: 'blur',
            },
          ],
          title: [
            {
              required: true,
              message: '请输入标题',
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
      selectedPro(val) {
        this.formData.projectName = val[0].projectName
        this.formData.projectId = val[0].id
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        // const {
        //   data: { tlist },
        // } = await implementPlanList({ pageNumber: 1, pageSize: 9999 })
        // if (tlist.length) {
        //   this.auditProjectSelectList = tlist.map((item) => {
        //     return {
        //       value: item.id,
        //       label: item.projectName,
        //     }
        //   })
        // }
        if (row) {
          const res = await jjzrsjjgbgDetail({ jjzrsjjgbgid: row.jjzrsjjgbgid })
          // this.formData.createType = res.data.createType
          // this.formData.dataBaseConnectionAddress =
          //   res.data.dataBaseConnectionAddress
          // this.formData.dataBaseOwnership = res.data.dataBaseOwnership
          // this.formData.dataBasePassWord = res.data.dataBasePassWord
          // this.formData.dataBaseType = res.data.dataBaseType
          // this.formData.dataBaseUsers = res.data.dataBaseUsers
          this.tableData = res.data.attachments
          this.formData = res.data
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
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
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)
            let params = { ...this.formData }
            delete params.attachments
            const data = await jjzrsjjgbgSaveOrUpdate({
              ...params,
              attIds,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetchData',params)
              this.close()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
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
