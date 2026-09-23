<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="评优年度" prop="appraisingYear">
            <!-- <el-date-picker
              v-model="formData.appraisingYear"
              type="year"
              placeholder="选择年"
              style="width:100%"
              :disabled="!disabled"
              >
            </el-date-picker> -->
            <el-input
              v-model="formData.appraisingYear"
              clearable
              placeholder="请输年度"
              :disabled="!disabled"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评优名称" prop="appraisingName">
            <el-input
              v-model="formData.appraisingName"
              clearable
              placeholder="请输评优名称"
              :disabled="!disabled"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申报单位" prop="declareBelongGroupName">
            <el-input
              v-model="formData.declareBelongGroupName"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择申报单位"
            />
            <el-button
              @click="handleObject"
              style="margin-left: 15px"
              type="primary"
              size="mini"
              :disabled="!disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="contactName">
            <el-input
              v-model="formData.contactName"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择联系人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="showPeople"
              :disabled="!disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input
              v-model="formData.contactNumber"
              clearable
              placeholder="请输联系电话"
              style="width: 100%"
              :disabled="!disabled"
              @input="inputMoney($event, 'contactNumber')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="项目状态的参评要求"
            label-width="140px"
            prop="projectStateRequirement"
          >
            <el-select
              v-model="formData.projectStateRequirement"
              placeholder="请选择状态"
              style="width: 100%"
              :disabled="!disabled"
            >
              <el-option label="已完成" value="已完成" />
              <el-option label="未完成" value="未完成" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="项目启动时间的参评要求"
            prop="projectStartRequirement"
            label-width="140px"
          >
            <el-input
              v-model="formData.projectStartRequirement"
              clearable
              placeholder="项目启动时间的参评要求"
              style="width: 100%"
              :disabled="!disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>申报项目材料维护</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="disabled">
            <el-button type="success" @click="openRelate">选择项目</el-button>
          </div>
          <el-table :data="tableData1">
            <el-table-column
              align="center"
              label="项目名称"
              prop="implementationProjectName"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row)">
                  {{ row.implementationProjectName }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="项目类别"
              prop=" implementationProjectType"
            />
            <el-table-column align="center" label="立项单位" prop="uploader" />
            <el-table-column
              align="center"
              label="实施审计机构"
              prop="uploader"
            />
            <el-table-column
              align="center"
              label="被审计单位"
              prop="uploader"
            />
            <el-table-column
              align="center"
              label="项目经理"
              prop="implementationProjectOrderName"
            />
            <el-table-column
              align="center"
              label="主审"
              prop="implementationPlanMainReviewer"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="deleteTable1(row)"
                  v-if="disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
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
                <el-button type="text" @click="handleDowns(row)">
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
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />

    <!-- 人员 -->
    <project-manage @projectManage="getChildlistPro" ref="manage" />

    <RelateSB ref="sb" @selected="handleRelate" :noIds="noIds" />
    <Edit ref="edit" @fetch-data="fetchData" />
    <template #footer v-if="disabled">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary" :loading="loading">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    jhyjgSaveOrUpdate,
    jhyjgList,
    jhyjgDetail,
    download,
    deleteReportFile,
  } from '@/oapi/audit/report'
  import { getXmpypyDetail, addOrUpdateXmpypy } from '@/oapi/audit/xmpy'
  import { implementPlanList } from '@/oapi/audit/project'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage from '@/components/selectPerson.vue'
  import RelateSB from '@/views/oilAudit/xmpy/components/relateSB.vue'
  import Edit from '@/views/oilAudit/xmpy/xmpysb/edit.vue'
  import store from '@/store'
  import { formatDate, formatDay } from '@/utils/index'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'jtxmpyEdit',
    inheritAttrs: false,
    components: { SelectDepartment, projectManage, RelateSB, Edit },
    props: [],
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
        formData: {
          appraisingYear: undefined,
          appraisingName: undefined,
          contactName: undefined,
          contactId: undefined,
          declareBelongGroupName: undefined,
          declareBelongGroup: undefined,
          contactNumber: undefined,
          projectStartRequirement: undefined,
          projectStateRequirement: undefined,
          id: '',
        },
        auditProjectSelectList: [],
        tableData: [],
        tableData1: [],
        disabled: true,
        rules: {
          appraisingName: [
            {
              required: true,
              message: '请输评优名称',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
      //金额输入
      inputMoney(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        if (
          sanitizedValue.startsWith('0') &&
          sanitizedValue.length > 1 &&
          sanitizedValue[1] !== '.'
        ) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        if (sanitizedValue === '' || sanitizedValue === '0') {
          sanitizedValue = ''
        }
        // 更新输入框的值
        const keys = key.split('.')
        let formDataRef = this.formData
        for (let i = 0; i < keys.length - 1; i++) {
          formDataRef = formDataRef[keys[i]]
        }
        formDataRef[keys[keys.length - 1]] = sanitizedValue
      },
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      async showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getXmpypyDetail({ id: row.id })
          this.tableData = res.data.file || []
          this.tableData1 = res.data.data.implementationPlanList || []
          this.formData = res.data.data
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.disabled = false
        } else {
          this.title = '新增'
          this.disabled = true
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
        this.loading = false
        this.tableData = []
        this.tableData1 = []
        this.disabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })

            attIds = attIds.substring(0, attIds.length - 1)
            const ids = this.tableData1.map((res) => res.id).toString()
            let params = { ...this.formData }
            delete params.attachments
            const data = await addOrUpdateXmpypy({
              ...params,
              fileIds: attIds.toString(),
              implementationPlanId: ids,
            })
            if (data.code == 200) {
              this.$baseMessage('保存成功', 'success')
            }
            this.$emit('fetch-data')
            this.loading = false
            this.close()
          } else {
            return false
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
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      showPeople(type) {
        this.$refs['manage'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.declareBelongGroup = val.id
        this.formData.declareBelongGroupName = val.label
      },
      // 选择人员
      getChildlistPro(val) {
        this.$set(this.formData, 'contactName', val[0].realname)
        this.$set(this.formData, 'contactId', val[0].staffid)
      },
      openRelate() {
        const noIds = this.tableData1.map((res) => res.id)
        this.$refs['sb'].show(noIds)
      },
      handleRelate(val) {
        const arr = this.tableData1
        arr.push(val)
        this.tableData1 = arr
      },
      deleteTable1(row) {
        let list = this.tableData1
        list = list.filter((item) => item.id != row.id)
        this.tableData1 = list
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit('detail', row)
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
