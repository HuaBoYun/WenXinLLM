<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12" style="height: 29px">
          <el-form-item label="通知单编号" prop="noticeNumber">
            <el-input
              v-model="formData.noticeNumber"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入通知单编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="通知单名称" prop="noticeName">
            <el-input
              v-model="formData.noticeName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入通知单名称"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="完成时间" prop="noticeTime">
            <el-date-picker
              v-model="formData.noticeTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="督办部门" prop="supervisionWorkUnitName">
            <el-input
              v-model="formData.supervisionWorkUnitName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择督办部门"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.department.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办人" prop="transactorName">
            <el-input
              v-model="formData.transactorName"
              :style="{ width: '75%' }"
              clearable
              placeholder="请选择经办人"
              disabled
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.executor.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
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
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" style="text-align: right" v-if="footer">
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
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
    <!-- 选择人员弹窗 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 选择部门弹窗 -->
    <department-options ref="department" @submit="handleDepartmentSelected" />
  </div>
</template>

<script>
  import { download } from '@/oapi/audit/report'
  import { deleteFileInfo } from '@/oapi/ypns_zhgl/filePublic'
  import store from '@/store'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/dbtzd.js'
  import DepartmentOptions from '@/views/oilAudit/jhlx/components/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  const token = store.getters['user/token']
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: {
      DepartmentOptions,
      ExecutorOptions,
      Resubmit,
    },
    inheritAttrs: false,
    props: ['fetchData'],
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
        tableData: [],
        formData: {
          noticeNumber: '',
          noticeName: '',
          noticeTime: '',
          supervisionWorkUnit: '',
          supervisionWorkUnitName: '',
          transactor: '',
          transactorName: '',
          remark: '',
          fileIds: '',
          id: '',
        },
        footer: true,
        rules: {
          noticeNumber: [
            {
              required: true,
              message: '请输入通知单编号',
              trigger: 'blur',
            },
          ],
          noticeName: [
            {
              required: true,
              message: '请输入通知单名称',
              trigger: 'blur',
            },
          ],
          noticeTime: [
            {
              required: true,
              message: '选择时间',
              trigger: ['blur', 'change'],
            },
          ],
          supervisionWorkUnitName: [
            {
              required: true,
              message: '请选择督办部门',
              trigger: ['blur', 'change'],
            },
          ],
          transactorName: [
            {
              required: true,
              message: '请选择经办人',
              trigger: ['blur', 'change'],
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      //提交审批
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      // 选择经办人
      handleExecutorSelected(node) {
        this.$set(this.formData, 'transactorName', node[0].realname)
        this.$set(this.formData, 'transactor', node[0].staffid)
        this.$forceUpdate()
      },
      // 选择部门
      handleDepartmentSelected(node) {
        this.$set(this.formData, `supervisionWorkUnitName`, node.label)
        this.$set(this.formData, `supervisionWorkUnit`, node.id)
        this.$forceUpdate()
      },
      // 附件列表 下载附件
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
      // 附件列表 删除附件
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        await deleteFileInfo({ id: row.attid })
        this.tableData = list
      },
      // handlePreview(file) {},
      // 附件上传成功
      // handleSuccess(file) {
      //   if (file.result == 200) {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      // 获取当前修改数据的详情
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        if (formId) {
          const res = await getInfoDetail({ id: formId })
          this.formData.noticeNumber = res.data.data.noticeNumber
          this.formData.noticeName = res.data.data.noticeName
          this.formData.noticeTime = res.data.data.noticeTime
          this.formData.supervisionWorkUnit = res.data.data.supervisionWorkUnit
          this.formData.supervisionWorkUnitName =
            res.data.data.supervisionWorkUnitName
          this.formData.transactor = res.data.data.transactor
          this.formData.transactorName = res.data.data.transactorName
          this.formData.remark = res.data.data.remark
          this.formData.fileIds = res.data.data.fileIds
          this.tableData =
            res.data.file && res.data.file.length ? res.data.file : []
          this.formData.id = res.data.data.id
          // this.formData = JSON.parse(JSON.stringify(row))
        }
        this.footer = isWfqdedit
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (title == 'detail') {
          this.footer = false
        }
      },
      // 关闭清除form表单内容
      close() {
        this.loading = false
        this.formData = {
          noticeNumber: '',
          noticeName: '',
          noticeTime: '',
          supervisionWorkUnit: '',
          supervisionWorkUnitName: '',
          transactor: '',
          transactorName: '',
          remark: '',
          fileIds: '',
          id: '',
        }
        this.tableData = []
        this.$bus.$emit('updateMsg', 0)
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const params = JSON.parse(JSON.stringify(this.formData))
            let fileIds = ''
            this.tableData.map((item) => {
              fileIds += item.attid
              fileIds += ','
            })
            fileIds = fileIds.substring(0, fileIds.length - 1)
            const res = await editInfor({
              ...params,
              fileIds,
            })
            if (res && res.code == 200) {
              this.$emit('fetchData')
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
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
