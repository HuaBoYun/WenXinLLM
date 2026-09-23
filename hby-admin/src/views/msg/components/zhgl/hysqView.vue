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
          <el-form-item label="会议名称" prop="conferenceName">
            <el-input
              v-model="formData.conferenceName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入会议名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="会议主持人" prop="conferenceCompereName">
            <el-input
              v-model="formData.conferenceCompereName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择会议主持人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="参会人员" prop="attendeesNames">
            <el-input
              v-model="formData.attendeesNames"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择参会人员"
            />
            <el-button type="primary" style="margin-left: 20px" @click="$refs.multipExecutorOptions.show()">选择</el-button>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="参会人员数量" prop="attendeesNum">
            <el-input
              v-model="formData.attendeesNum"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入参会人员数量"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="会议开始时间" prop="conferenceTimeStart">
            <el-date-picker
              v-model="formData.conferenceTimeStart"
              type="datetime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择会议开始时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="会议结束时间" prop="conferenceTimeEnd">
            <el-date-picker
              v-model="formData.conferenceTimeEnd"
              type="datetime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择会议结束时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 29px">
          <el-form-item label="会议地点" prop="conferencePlace">
            <el-input
              v-model="formData.conferencePlace"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入会议地点"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="会议内容" prop="conferenceContent">
            <el-input
              v-model="formData.conferenceContent"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="会议要求" prop="conferenceRequire">
            <el-input
              v-model="formData.conferenceRequire"
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
    <MultipExecutorOptions
      ref="multipExecutorOptions"
      @selected="handleMultipExecutorSelected"
      isCheckout
    />
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import * as dayjs from 'dayjs'
  import { download } from '@/oapi/audit/report'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/hysq.js'
  import { deleteFileInfo } from '@/oapi/ypns_zhgl/filePublic'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import MultipExecutorOptions from '@/views/oilAudit/report/components/options/executor.vue'

  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { ExecutorOptions, Resubmit, MultipExecutorOptions },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        tableData: [],
        formData: {
          id: '',
          conferenceName: '',
          conferenceCompere: '',
          conferenceCompereName: '',
          attendees: '',
          attendeesNames: '',
          conferenceTimeStart: '',
          conferenceTimeEnd: '',
          conferencePlace: '',
          conferenceContent: '',
          conferenceRequire: '',
          fileIds: '',
        },
        timer: [],
        footer: true,
        rules: {
          unit: [
            {
              required: true,
              message: '请输入会议名称',
              trigger: 'blur',
            },
          ],
          conferenceCompereName: [
            {
              required: true,
              message: '请选择参会人员',
              trigger: ['blur', 'change'],
            },
          ],
          attendees: [
            {
              required: false,
              message: '请选择参会人员',
              trigger: ['blur', 'change'],
            },
          ],
          conferenceTimeStart: [
            {
              required: true,
              message: '选择会议开始时间',
              trigger: ['blur', 'change'],
            },
          ],
          conferenceTimeEnd: [
            {
              required: true,
              message: '选择会议结束时间',
              trigger: ['blur', 'change'],
            },
          ],
          conferencePlace: [
            {
              required: true,
              message: '请输入会议地点',
              trigger: 'blur',
            },
          ],
          attendeesNum: [
            {
              required: true,
              message: '请输入参会人员数量',
              trigger: 'blur',
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
      // 选择人员
      handleExecutorSelected(node) {
        this.$set(this.formData, 'conferenceCompereName', node[0].realname)
        this.$set(this.formData, 'conferenceCompere', node[0].staffid)
        this.$forceUpdate()
      },
      handleMultipExecutorSelected(node) {
        this.$set(
          this.formData,
          'attendeesNames',
          node.map((x) => x.realname).join(',')
        )
        this.$set(
          this.formData,
          'attendees',
          node.map((x) => x.staffid).join(',')
        )
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
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // handlePreviewFile(row) {
      //   console.log('row', row)
      //   this.$iFrameDialog({ attid: row.attid })
      // },
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
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.footer = isWfqdedit
        if (formId) {
          const res = await getInfoDetail({ id: formId })
          const result = res.data.data || {}
          this.formData.conferenceName = result.conferenceName
          this.formData.conferenceCompere = result.conferenceCompere
          this.formData.conferenceCompereName = result.conferenceCompereName
          this.formData.attendees = result.attendees
          this.formData.attendeesNum = result.attendeesNum
          this.formData.conferenceTimeStart = dayjs(
            result.conferenceTimeStart
          ).format('YYYY-MM-DD HH:mm:ss')
          this.formData.conferenceTimeEnd = dayjs(
            result.conferenceTimeEnd
          ).format('YYYY-MM-DD HH:mm:ss')
          this.formData.conferencePlace = result.conferencePlace
          this.formData.conferenceContent = result.conferenceContent
          this.formData.conferenceRequire = result.conferenceRequire
          this.formData.id = result.id
          this.formData.attendeesNames = (result.attendeesName || [])
            .map((x) => x.realName)
            .join(',')
          this.formData.attendees = (result.attendeesName || [])
            .map((x) => x.staffId)
            .join(',')
          if (res.data.file) {
            this.tableData = res.data.file || []
          }
        }
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (title == 'detail') {
          this.footer = false
        }
      },
      close() {
        this.loading = false
        this.formData = {
          id: '',
          conferenceName: '',
          conferenceCompere: '',
          conferenceCompereName: '',
          attendees: '',
          conferenceTimeStart: '',
          conferenceTimeEnd: '',
          conferencePlace: '',
          conferenceContent: '',
          conferenceRequire: '',
          fileIds: '',
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
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            console.log('this.loading', this.loading)
            this.$refs.resubmit.ymsubmit()
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
