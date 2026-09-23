<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="20">
      <el-form
        :model="formData"
        ref="ruleForm"
        label-width="140px"
        :disabled="!disabled"
        :rules="rules"
      >
        <el-col :span="12" style="height: 29px">
          <el-form-item
            label="编号"
            prop="hcnumber"
            :rules="[
              { required: true, message: '请输入编号', trigger: 'change' },
            ]"
          >
            <el-input
              v-model="formData.hcnumber"
              style="width: 79%; margin-right: 10px"
              placeholder="请输入编号"
              disabled
            />
            <el-button type="primary" @click="$refs.list.show()" size="small">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="名称"
            prop="hcname"
            :rules="[
              { required: true, message: '请输入名称', trigger: 'blur' },
            ]"
          >
            <el-input
              v-model="formData.hcname"
              style="width: 100%"
              placeholder="请输入名称"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 29px">
          <el-form-item label="填报单位" prop="editorgname">
            <el-input
              v-model="formData.editorgname"
              clearable
              placeholder="请选择填报单位"
              :style="{ width: '79%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.company.showEdit()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制时间" prop="edittime">
            <el-date-picker
              v-model="formData.edittime"
              placeholder="编制时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人" prop="editstaffname">
            <el-input
              v-model="formData.editstaffname"
              style="width: 100%"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="disabled">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
              :file-list="tableData"
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
    <div slot="footer" v-if="disabled">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save" :loading="loading">
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
    <SelectDepartment ref="company" @submit="selectedCompany" />
    <wtslList ref="list" @submit="selectList" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>
<script>
  import {
    wthcSave,
    deleteReportFile,
    wthcDetail,
    wthcFileList,
  } from '@/oapi/audit/wgzrzj.js'
  import { download } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import wtslList from '@/views/oilAudit/wgzrzj/components/wtslList.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { SelectDepartment, UEditor, wtslList, ProcessList },
    data() {
      return {
        loading: false,
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
        dialogFormVisible: false,
        title: '新增',
        formData: {
          attIds: '',
          hcname: '',
          editorgid: '',
          editorgname: '',
          id: '',
          edittime: '',
          editstaffname: '',
          hcnumber: '',
          content: '',
        },
        templates: [],
        tableData: [],
        disabled: false,
        rules: {},
        editId: '',
      }
    },
    methods: {
      async show(name, row) {
        this.dialogFormVisible = true
        this.title = name
        if (name == '详情') {
          this.disabled = false
        } else {
          this.disabled = true
        }
        if (row) {
          await this.getData(row)
          await this.getFileData(row)
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.editstaffname = userInfo.realname
        }
      },
      async getData(row) {
        this.editId = row.id
        const { data } = await wthcDetail({ id: row.id })
        Object.assign(this.formData, data.tblYqnsWgzzWthc)
      },
      async getFileData(row) {
        const { data } = await wthcFileList({ id: row.id })
        this.tableData = data.attList
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)
            let params = { ...this.formData }
            delete params.editstaffname
            const { data, code, msg } = await wthcSave({
              ...params,
              attIds,
            })
            if (code == 1) this.$message.success('保存成功')
            this.editId = data.tblYqnsWgzzWthc.id
            this.formData.id = data.tblYqnsWgzzWthc.id
            this.loading = false
             this.$emit('fetchData')
          }
        })
      },
      close() {
        this.$refs['ruleForm'].resetFields()
        this.tableData = []
        this.formData.content = ''
        this.dialogFormVisible = false
        this.loading = false
        this.disabled = false
        this.editId = ''
        this.$emit('fetchData')
      },
      selectList(row) {
        this.formData.hcnumber = row[0].slnumber
        this.formData.slid = row[0].id
      },
      selectedCompany(node) {
        this.formData.editorgname = node.label
        this.formData.editorgid = node.id
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
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
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */
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
      handleApproval() {
        //提交审批
        this.$refs['process'].save(200, this.editId)
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
<style lang="scss" scoped></style>
