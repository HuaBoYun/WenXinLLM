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
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="员工名称" prop="staffName">
            <el-input
              v-model="formData.staffName"
              :style="{ width: '75%' }"
              disabled
              clearable
              placeholder="请选择员工名称"
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
        <el-col :span="12">
          <el-form-item label="科室" prop="fillWorkUnitName">
            <el-input
              v-model="formData.fillWorkUnitName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择科室"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="selectDept('keshi')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="人员性质" prop="personnelNature">
            <el-input
              v-model="formData.personnelNature"
              :style="{ width: '100%' }"
              clearable
              placeholder="请选择人员性质"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系方式" prop="contactPhone">
            <el-input
              v-model="formData.contactPhone"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入联系方式"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="身份证号" prop="identityCard">
            <el-input
              v-model="formData.identityCard"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入身份证号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="离庆事由" prop="reason">
            <el-input
              v-model="formData.reason"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入离庆事由"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出发地" prop="placeOfDeparture">
            <el-input
              v-model="formData.placeOfDeparture"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入出发地"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目的地" prop="destination">
            <el-input
              v-model="formData.destination"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入目的地"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="离庆日期" prop="leaveCelebrateStartTime">
            <el-date-picker
              v-model="formData.leaveCelebrateStartTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划返庆日期" prop="leaveCelebrateEndTime">
            <el-date-picker
              v-model="formData.leaveCelebrateEndTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="离庆健康情况" prop="healthSituation">
            <el-input
              v-model="formData.healthSituation"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入离庆健康情况"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="离庆交通工具" prop="vehicle">
            <el-input
              v-model="formData.vehicle"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入离庆交通工具"
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
      <el-button @click="save" type="primary" :loading="loading">
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
    <!-- 申请人 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 部门 -->
    <department-options ref="department" @submit="handleDepartmentSelected" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import DepartmentOptions from '@/views/oilAudit/jhlx/components/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { edityglqsp, getInfoDetail } from '@/oapi/ypns_zhgl/yglqsp.js'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'

  const token = store.getters['user/token']

  export default {
    components: {
      ExecutorOptions,
      DepartmentOptions,
      ProcessList,
    },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
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
        baseApi: baseURL,
        formData: {
          staffId: '',
          staffName: '',
          fillWorkUnitName: '',
          fillWorkUnit: '',
          personnelNature: '',
          contactPhone: '',
          identityCard: '',
          reason: '',
          destination: '',
          placeOfDeparture: '',
          leaveCelebrateStartTime: '',
          leaveCelebrateEndTime: '',
          healthSituation: '',
          vehicle: '',
          id: '',
        },
        applyPeriod: [],
        radio: '',
        footer: true,
        rules: {
          applyPeopleName: [
            {
              required: true,
              message: '请选择申请人',
              trigger: ['blur', 'change'],
            },
          ],
          applyBelongGroupName: [
            {
              required: true,
              message: '请选择部门/单位',
              trigger: ['blur', 'change'],
            },
          ],
          applyWorkUnitName: [
            {
              required: true,
              message: '请选择科室',
              trigger: ['blur', 'change'],
            },
          ],
          networkInterface: [
            {
              required: true,
              message: '请输入网络接口(T)',
              trigger: 'blur',
            },
          ],
          officeArea: [
            {
              required: true,
              message: '请输入办公区',
              trigger: 'blur',
            },
          ],
          roomNumber: [
            {
              required: true,
              message: '请输入房间号',
              trigger: 'blur',
            },
          ],
          equipmentType: [
            {
              required: true,
              message: '请输入设备类型',
              trigger: 'blur',
            },
          ],
          purpose: [
            {
              required: true,
              message: '请输入用途',
              trigger: 'blur',
            },
          ],
          applyPeriodTimeStart: [
            {
              required: true,
              message: '请选择使用期限',
              trigger: ['blur', 'change'],
            },
          ],
          externalNetworkPermissions: [
            {
              required: true,
              message: '请输入外网权限',
              trigger: 'blur',
            },
          ],
          applyTime: [
            {
              required: true,
              message: '选择申请时间',
              trigger: ['blur', 'change'],
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        deptType: '',
        editId: '',
      }
    },

    methods: {
      selectDept(type) {
        this.deptType = type
        this.$refs.department.showEdit()
      },
      handleDepartmentSelected(node) {
        //保存名称
        this.$set(this.formData, `fillWorkUnitName`, node.label)
        //保存名称对应的ID
        this.$set(this.formData, `fillWorkUnit`, node.id)
      },
      changeApplyPeriod(val) {
        if (val && val.length) {
          this.$set(this.formData, 'applyPeriodTimeStart', val[0])
          this.$set(this.formData, 'applyPeriodTimeEnd', val[1])
        } else {
          this.$set(this.formData, 'applyPeriodTimeStart', '')
          this.$set(this.formData, 'applyPeriodTimeEnd', '')
        }
      },
      handleExecutorSelected(node) {
        this.formData.staffId = node[0].staffid
        this.formData.staffName = node[0].realname
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          this.editId = row.id
          const res = await getInfoDetail({ id: row.id })

          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data.data[key]
          })
          this.tableData = res.data?.file || []
        }
        console.log(title, 'title')
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
        this.formData = {
          staffId: '',
          staffName: '',
          fillWorkUnitName: '',
          fillWorkUnit: '',
          personnelNature: '',
          contactPhone: '',
          identityCard: '',
          reason: '',
          destination: '',
          placeOfDeparture: '',
          leaveCelebrateStartTime: '',
          leaveCelebrateEndTime: '',
          healthSituation: '',
          vehicle: '',
          id: '',
        }
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
        this.editId = ''
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await edityglqsp({ ...params, fileIds: attIds })
            if (res && res.code == 200) {
              this.editId = res.data.id
              this.formData.id = res.data.id
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
            this.loading = false
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
        this.tableData = list
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(196, this.editId)
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
