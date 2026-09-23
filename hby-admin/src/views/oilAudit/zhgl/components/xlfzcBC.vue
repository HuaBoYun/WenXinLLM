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
        <el-col :span="12" style="height: 29px">
          <el-form-item label="单位名称" prop="repairBelongGroupName">
            <el-input
              v-model="formData.repairBelongGroupName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请输入单位名称"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.audiTree.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车号" prop="vin">
            <el-input
              v-model="formData.vin"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入车号"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="时间" prop="repairTime">
            <el-date-picker
              v-model="formData.repairTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计支出金额" prop="amount">
            <el-input
              v-model="formData.amount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入预计支出金额"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="修理厂家" prop="repairManufacturer">
            <el-input
              v-model="formData.repairManufacturer"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入修理厂家"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办人" prop="transactorName">
            <el-input
              v-model="formData.transactorName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择经办人"
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
          <el-form-item label="故障情况" prop="faultCondition">
            <el-input
              v-model="formData.faultCondition"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="修理项目" prop="repairProject">
            <el-input
              v-model="formData.repairProject"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际支出金额" prop="actamount">
            <el-input
              v-model="formData.actamount"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入实际支出金额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="couldEdit">
          <el-form-item label="委托单号" prop="commissionNumber">
            <el-input
              v-model="formData.commissionNumber"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入委托单号"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12" v-if="couldEdit">
          <el-form-item label="维修用料" prop="maintenanceMaterials">
            <el-input
              v-model="formData.maintenanceMaterials"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入维修用料"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12" v-if="couldEdit">
          <el-form-item label="修理费合计" prop="totalRepairCost">
            <el-input
              v-model="formData.totalRepairCost"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入修理费合计"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12" v-if="couldEdit">
          <el-form-item label="用料费合计" prop="totalMaterialCost">
            <el-input
              v-model="formData.totalMaterialCost"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入用料费合计"
            />
          </el-form-item>
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
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <!-- 选择人员弹窗 -->
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'

  import ExecutorOptions from '@/views/oilAudit/report/components/options/executor.vue'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/xlfyzc'
  import SelectDepartment from '../../jhlx/components/department.vue'
  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { ExecutorOptions, SelectDepartment },
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
          repairBelongGroupName: '',
          repairBelongGroup: '',
          vin: '',
          repairTime: '',
          amount: '',
          repairManufacturer: '',
          transactor: '',
          transactorName: '',
          faultCondition: '',
          repairProject: '',
          commissionNumber: '',
          maintenanceMaterials: '',
          totalRepairCost: '',
          totalMaterialCost: '',
          actamount: '',
          id: '',
        },
        couldEdit: false, //根据身份来判断是否能编辑
        footer: true,
        rules: {
          repairBelongGroupName: [
            {
              required: true,
              message: '请选择单位名称',
              trigger: ['blur', 'change'],
            },
          ],
          vin: [
            {
              required: true,
              message: '请输入车号',
              trigger: 'blur',
            },
          ],
          repairTime: [
            {
              required: true,
              message: '选择时间',
              trigger: ['blur', 'change'],
            },
          ],
          amount: [
            {
              required: true,
              message: '请输入支出金额',
              trigger: 'blur',
            },
          ],
          repairManufacturer: [
            {
              required: true,
              message: '请输入修理厂家',
              trigger: 'blur',
            },
          ],
          transactorName: [
            {
              required: true,
              message: '请选择经办人',
              trigger: ['blur', 'change'],
            },
          ],
          faultCondition: [
            {
              required: true,
              message: '请输入故障情况',
              trigger: 'blur',
            },
          ],
          repairProject: [
            {
              required: true,
              message: '请输入修理项目',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    created() {},
    mounted() {},
    methods: {
      // 选择单位
      getDepartmentInfo(node) {
        this.$set(this.formData, 'repairBelongGroupName', node.name)
        this.$set(this.formData, 'repairBelongGroup', node.id)
      },
      // 选择经办人
      handleExecutorSelected(node) {
        this.$set(this.formData, 'transactorName', node.realname)
        this.$set(this.formData, 'transactor', node.staffid)
        this.$forceUpdate()
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        //判断用户身份，是否显示字段
        const info = JSON.parse(localStorage.getItem('userInfo')).roleNames
        if (info.includes('费用管理人员')) {
          this.couldEdit = true
        }
        if (row) {
          const res = await getInfoDetail({ id: row.id })
          Object.assign(this.formData, res.data.data)
          this.tableData = res.data.file || []
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData.repairBelongGroupName = ''
        this.formData.repairBelongGroup = ''
        this.formData.vin = ''
        this.formData.repairTime = ''
        this.formData.amount = ''
        this.formData.repairManufacturer = ''
        this.formData.transactor = ''
        this.formData.transactorName = ''
        this.formData.faultCondition = ''
        this.formData.repairProject = ''
        this.formData.commissionNumber = ''
        this.formData.maintenanceMaterials = ''
        this.formData.totalRepairCost = ''
        this.formData.totalMaterialCost = ''
        this.formData.actamount = ''
        this.formData.id = ''
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
        this.couldEdit = false
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const fileIds = this.tableData.map((x) => x.attid).join(',')
            const params = JSON.parse(JSON.stringify(this.formData))
            params.fileIds = fileIds
            const res = await editInfor(params)
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
      // // 附件上传成功
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
