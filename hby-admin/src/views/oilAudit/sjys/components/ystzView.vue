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
        <el-col :span="12">
          <el-form-item label="序号" prop="tznumber">
            <el-input
              v-model="formData.tznumber"
              style="width: 100%"
              placeholder="请输入序号"
            ></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="审计项目名称"
            label-width="140px"
            prop="projectname"
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
            label="审计实施单位"
            label-width="140px"
            prop="imporgname"
          >
            <el-input
              v-model="formData.imporgname"
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
          <el-form-item label="移交时间" prop="yjtime">
            <el-date-picker
              v-model="formData.yjtime"
              placeholder="移交时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="问题线索" prop="problemclue">
            <el-input
              v-model="formData.problemclue"
              clearable
              placeholder="问题线索"
              type="textarea"
              :rows="6"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="核查结论" prop="checkresult">
            <el-input
              v-model="formData.checkresult"
              clearable
              placeholder="核查结论"
              type="textarea"
              :rows="6"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="处理情况" prop="situation">
            <el-input
              v-model="formData.situation"
              clearable
              placeholder="处理情况"
              type="textarea"
              :rows="6"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="memo">
            <el-input
              v-model="formData.memo"
              clearable
              placeholder="备注"
              type="textarea"
              :rows="6"
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
      <el-button type="primary" @click="save" v-loading="loading">
        确定
      </el-button>
    </div>
    <!-- 公司选择 -->
    <SelectDepartment ref="unit" @submit="handleUnitSelected" />
    <!-- 项目选择 -->
    <ssfaList ref="project" @selected="projectSelect" />
  </el-dialog>
</template>
<script>
  import {
    ystzSave,
    deleteReportFile,
    ystzDetail,
    ystzFileList,
  } from '@/oapi/audit/wgzrzj.js'
  import { download } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ssfaList from '@/views/oilAudit/prepare/components/options/ssfaList.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'

  import store from '@/store'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { ssfaList, SelectDepartment },
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
          id: '',
          yjtime: '',
          editstaffname: '',
          tznumber: '',
          situation: '',
          checkresult: '',
          memo: '',
          projectid: '',
          projectname: '',
          imporgid: '',
          imporgname: '',
          problemclue: '',
        },
        rules: {
          projectname: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'change',
            },
          ],
        },
        templates: [],
        tableData: [],
        disabled: false,
      }
    },
    mounted() {},
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
        }
      },
      async getData(row) {
        const { data } = await ystzDetail({ id: row.id })
        Object.assign(this.formData, data.tblYqnsWgzzYstz)
      },
      async getFileData(row) {
        const { data } = await ystzFileList({ id: row.id })
        this.tableData = data.attList
      },
      /**
       * @description: 选择项目
       * @param {*}
       * @return {*}
       */
      projectSelect(val) {
        this.$set(this.formData, `projectid`, val[0].id)
        this.$set(this.formData, `projectname`, val[0].projectName)
      },
      /**
       * @description: 选择单位
       * @param {*}
       * @return {*}
       */
      handleUnitSelected(node) {
        this.formData.imporgname = node.label
        this.formData.imporgid = node.id
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
            const { data, code, msg } = await ystzSave({
              ...params,
              attIds,
            })
            if (code == 1) {
              this.formData.id = data.tblYqnsWgzzYstz.id
              this.$message.success('保存成功')
            }
            this.loading = false
            this.close()
            this.$emit('fetchData')
          }
        })
      },
      close() {
        ;(this.formData = {
          attIds: '',
          id: '',
          yjtime: '',
          editstaffname: '',
          tznumber: '',
          situation: '',
          checkresult: '',
          memo: '',
          projectid: '',
          projectname: '',
          imporgid: '',
          imporgname: '',
          problemclue: '',
        }),
          this.$refs['ruleForm'].resetFields()
        this.tableData = []
        this.dialogFormVisible = false
        this.loading = false
        this.disabled = false
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
