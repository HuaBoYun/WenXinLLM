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
          <el-form-item label="编号" prop="code">
            <el-input
              v-model="formData.code"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入编号"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="名称" prop="bgname">
            <el-input
              v-model="formData.bgname"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="项目内容" prop="projectname">
            <el-input
              v-model="formData.projectname"
              clearable
              placeholder="请选择项目内容"
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
          <el-form-item label="下发时间" label-width="140px" prop="issueDate">
            <el-date-picker
              v-model="formData.issueDate"
              placeholder="选择下发时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="编制人" prop="createname">
            <el-input
              v-model="formData.createname"
              clearable
              placeholder="请输编制人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制时间" label-width="140px" prop="createdate">
            <el-date-picker
              v-model="formData.createdate"
              placeholder="选择编制时间"
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
            :on-success="(response, file) => handleSuccess(response, file, '1')"
            :file-list="tableData"
            :before-upload="handleBeforeUpload"
          >
            <el-button type="success">文件上传</el-button>
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
            <div v-if="footer" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
        </div>
        <el-table :data="tableData" class="mb30">
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
              <el-button
                type="text"
                @click="handleDowns(row)"
                :disabled="false"
              >
                下载
              </el-button>
              <el-button
                type="text"
                @click="handlePreviewFile(row)"
                :disabled="false"
              >
                预览
              </el-button>
              <el-button
                type="text"
                @click="handleDelete(row, '1')"
                v-if="footer"
              >
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
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>

    <projectName ref="project" @selected="selectedPro" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import store from '@/store'
  import {
    sjjysSaveOrUpdate,
    sjjysDetail,
    download,
    deleteSjjysFileList,
    getSjjysFileList,
  } from '@/oapi/audit/report'
  import { formatDate, formatDay } from '@/utils/index'
  import { implementPlanList } from '@/oapi/audit/project'
  import projectName from '@/views/oilAudit/report/components/options/projectName.vue'
  const { baseURL } = require('@/config')
  const token = store.getters['user/token']
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { projectName, ProcessList },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        uploadShow: false,
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
        tableData: [],
        tableData1: [],
        tableData2: [],
        formData: {
          code: '',
          bgname: '',
          createdate: '',
          createname: '',
          issueDate: '',
          createstaffid: '',
          bgid: '',
          projectname: '',
          projectid: '',
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
        editId: '',
      }
    },
    methods: {
      handleInput1(event, key) {
        // 允许数字和小数点
        const regex = /^\d*\.?\d*$/
        // 如果输入不匹配，就撤回到上一个合法的值
        if (!regex.test(event)) {
          // 这里假设你已经有一个变量 value 来绑定输入的值
          this.formData[key] = this.formData[key]
            .replace(/[^0-9.]/g, '')
            .replace(/(\..*)\./g, '$1')
        }
      },
      selectedPro(val) {
        this.$set(this.formData, 'projectname', val[0].projectName)
        this.$set(this.formData, 'projectid', val[0].id)
      },
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 10)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          this.editId = row.bgid
          const res = await sjjysDetail({ perid: row.bgid })
          this.formData = res.data.data
          const arr = await getSjjysFileList({ perid: row.bgid })
          this.tableData = arr.data.data
        }
        if (title == 'edit') {
          this.title = '编辑'
          this.uploadShow = true
        } else if (title == 'detail') {
          this.uploadShow = true
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo'))
          this.formData = {
            ...this.formData,
            createdate: this.getCurrentDate(),
            createname: resL.realname,
            createstaffid: resL.staffid,
          }
        }
      },
      close() {
        this.formData = {}
        this.tableData = []
        this.tableData1 = []
        this.tableData2 = []
        this.dialogFormVisible = false
        this.footer = true
        this.editId = ''
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = this.tableData
              .map((item) => {
                return item.attid
              })
              .join(',')
            const data = await sjjysSaveOrUpdate({
              ...this.formData,
              attids,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.editId = data.data.data.bgid
              this.formData.bgid = data.data.data.bgid
              this.$emit('fetchData')
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
      //   let url = window.URL.createObjectURL(blob, {})
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      handleDelete(row, type) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteSjjysFileList({ attid: row.attid })
          let list = this.tableData
          list = list.filter((item) => item.attid != row.attid)
          this.tableData = list
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      // handlePreview(file) {},
      // handleSuccess(response, file, type) {
      //   if (response.result == '200') {
      //     switch (type) {
      //       case '1':
      //         this.tableData.push(response.data)
      //         break
      //       case '2':
      //         this.tableData1.push(response.data)
      //         break
      //       case '3':
      //         this.tableData2.push(response.data)
      //         break
      //     }
      //     this.$baseMessage(response.msg, 'success')
      //   } else {
      //     this.$baseMessage(response.msg, 'error')
      //   }
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
      handleApproval() {
        //提交审批
        this.$refs['process'].save(192, this.editId)
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
  .mb30 {
    margin-bottom: 30px;
  }
</style>
