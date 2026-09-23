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
          <el-form-item label="编号" prop="num">
            <el-input
              v-model="formData.num"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="IP地址" prop="ipAddress">
            <el-input
              v-model="formData.ipAddress"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入IP地址"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="办公楼层" prop="officeFloor">
            <el-input
              v-model="formData.officeFloor"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入办公楼层"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="使用人" prop="usePeopleName">
            <el-input
              v-model="formData.usePeopleName"
              :style="{ width: '75%' }"
              clearable
              placeholder="请输入使用人"
              disabled
            />
            <el-button
              @click="openPerson('zsname')"
              style="margin-left: 10px"
              type="primary"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位名称" prop="useBelongGroupName">
            <el-input
              v-model="formData.useBelongGroupName"
              :style="{ width: '75%' }"
              disabled
              placeholder="请输入单位名称"
            />
            <el-button
              @click="handleObject"
              style="margin-left: 10px"
              type="primary"
              :disabled="disabled"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="用途" prop="purpose">
            <el-input
              v-model="formData.purpose"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入用途"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入备注"
              type="textarea"
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
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <!-- 申请人 -->
    <danxuanPerson ref="danxuanPerson" @projectManage="danxuanSelect" />
    <Company ref="audiTree" @submit="getChildlistObj"></Company>
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/views/oilAudit/report/components/options/executor.vue'
  import { editIp, getInfoDetail } from '@/oapi/ypns_zhgl/ipqd.js'
  import { download } from '@/api/audit/implement'
  import danxuanPerson from '@/components/danxuanPerson.vue'
  import Company from '@/views/oilAudit/jhlx/components/department.vue'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']

  export default {
    components: {
      danxuanPerson,
      DepartmentOptions,
      Company,
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
          fileIds: '',
          id: '',
          ipAddress: '',
          num: '',
          officeFloor: '',
          purpose: '',
          remark: '',
          useBelongGroupId: '',
          useBelongGroupName: '',
          usePeopleId: '',
          usePeopleName: '',
        },
        applyPeriod: [],
        radio: '',
        footer: true,
        rules: {
          ipAddress: [
            {
              required: true,
              message: '请输入IP地址',
              trigger: ['blur', 'change'],
            },
          ],
          num: [
            {
              required: true,
              message: '请输入编号',
              trigger: ['blur', 'change'],
            },
          ],
          officeFloor: [
            {
              required: true,
              message: '请输入办公楼层',
              trigger: ['blur', 'change'],
            },
          ],
          purpose: [
            {
              required: true,
              message: '请输入用途',
              trigger: 'blur',
            },
          ],
          useBelongGroupName: [
            {
              required: true,
              message: '请选择使用单位',
              trigger: 'blur',
            },
          ],
          // usePeopleName: [
          //   {
          //     required: true,
          //     message: '请选择使用人',
          //     trigger: 'blur',
          //   },
          // ],
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
      selectDept() {
        this.$refs.department.show()
      },
      changeApplyPeriod(val) {
        this.$set(this.formData, 'applyPeriodTimeStart', val[0])
        this.$set(this.formData, 'applyPeriodTimeEnd', val[1])
      },
      handleExecutorSelected(node) {
        this.$set(this.formData, 'applyPeopleName', node.realname)
        this.$set(this.formData, 'applyPeople', node.staffid)
        this.$forceUpdate()
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getInfoDetail({ id: row.id })
          this.formData = res.data.data
          // this.formData = JSON.parse(JSON.stringify(row))
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
        }
      },
      close() {
        this.dialogFormVisible = false
        this.formData = {
          fileIds: '',
          id: '',
          ipAddress: '',
          num: '',
          officeFloor: '',
          purpose: '',
          remark: '',
          useBelongGroupId: '',
          useBelongGroupName: '',
          usePeopleId: '',
          usePeopleName: '',
        }
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editIp(params)
            if (res && res.code == 200) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
          }
        })
      },

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
      // 附件列表 删除附件
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        await deleteFileInfo({ id: row.attid })
        this.tableData = list
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
      openPerson() {
        this.$refs.danxuanPerson.showEdit()
      },
      danxuanSelect(val) {
        this.$set(this.formData, `usePeopleId`, val[0].staffid)
        this.$set(this.formData, `usePeopleName`, val[0].realname)
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getChildlistObj(val) {
        this.$set(this.formData, 'useBelongGroupId', val.id)
        this.$set(this.formData, 'useBelongGroupName', val.label)
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
