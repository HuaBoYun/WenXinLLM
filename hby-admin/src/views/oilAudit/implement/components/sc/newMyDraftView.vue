<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="
      type === 'edit' ? '编辑' : type === 'detail' ? '详细' : '添加' + '底稿'
    "
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="200px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="底稿编号" prop="draftNumber">
            <el-input
              v-model="formData.draftNumber"
              clearable
              placeholder="请输入底稿编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="底稿名称" prop="draftName">
            <el-input
              v-model="formData.draftName"
              clearable
              placeholder="请输入底稿名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位名称" prop="auditeeName">
            <el-input
              v-model="formData.auditeeName"
              clearable
              placeholder="请选择被审计单位名称"
              :style="{ width: '206px' }"
              disabled
            />
            <el-button
              style="margin-left: 10px"
              type="primary"
              @click="$refs.company.showEdit()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计项目名称"
            prop="projectName"
            style="height: 32px"
          >
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入审计项目名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="专业科室人员" prop="thedeptstaffname">
            <el-input
              v-model="formData.thedeptstaffname"
              clearable
              placeholder="请输入专业科室人员"
              readonly
              :style="{ width: '206px' }"
            />
            <el-button
              @click="
                projectManagetype = 'thedeptstaffname'
                $refs['manage'].showEdit()
              "
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否是问题底稿" prop="probleMdraft">
            <el-select
              v-model="formData.probleMdraft"
              clearable
              style="width: 100%"
            >
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否是汇总底稿标识" prop="summaryDraftMark">
            <el-select
              v-model="formData.summaryDraftMark"
              clearable
              style="width: 100%"
            >
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计事项" prop="auditMatters">
            <el-input
              v-model="formData.auditMatters"
              clearable
              placeholder="请输入审计事项"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="复核人" prop="fhstaffname">
            <el-input
              v-model="formData.fhstaffname"
              clearable
              placeholder="请输入复核人"
              readonly
              :style="{ width: '206px' }"
            />
            <el-button
              @click="
                projectManagetype = 'fhstaffname'
                $refs['manage'].showEdit()
              "
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="基础工作" prop="basicWork">
            <el-input
              type="textarea"
              :rows="6"
              v-model="formData.basicWork"
              clearable
              placeholder="请输入基础工作"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="审计证据是否准确、真实、合法"
            prop="evidenceAccurate"
          >
            <el-input
              type="textarea"
              :rows="6"
              v-model="formData.evidenceAccurate"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="审计查证事实描述是否详尽、充分"
            prop="verificationDescription"
          >
            <el-input
              type="textarea"
              :rows="6"
              v-model="formData.verificationDescription"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计结论是否客观、公正" prop="auditConclusion">
            <el-input
              type="textarea"
              :rows="6"
              v-model="formData.auditConclusion"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="审计处理意见及建议是否正确、具有可操作性和建设性"
            prop="handlingOpinions"
          >
            <el-input
              type="textarea"
              :rows="6"
              v-model="formData.handlingOpinions"
              clearable
            />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="12">
          <el-form-item label="关联工作记录" prop="auditeeName">
            <el-input
              v-model="formData.auditeeName"
              clearable
              placeholder="请选择关联工作记录"
              :style="{ width: '206px' }"
              disabled
            />
            <el-button
              style="margin-left: 10px"
              type="primary"
              @click="$refs.workRecord.showEdit()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
      </el-form>
      <el-col :span="24" style="margin-bottom: 20px">
        <div style="text-align: right; margin-bottom: 5px" v-if="!formDisabled">
          <el-button type="success" @click="handleAdd">增加一行</el-button>
        </div>
        <!-- 新增可编辑表格 -->
        <el-table border :data="formData.auditMyManuVerifyEntityList">
          <el-table-column
            align="center"
            label="审计查证事实"
            prop="verificationDescription"
          >
            <template slot-scope="{ row }">
              <el-input
                :disabled="formDisabled"
                v-model="row.verificationDescription"
                size="mini"
                style="width: 90%"
              />
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="审计结论及依据"
            prop="auditConclusion"
          >
            <template slot-scope="{ row }">
              <el-input
                :disabled="formDisabled"
                v-model="row.auditConclusion"
                size="mini"
                style="width: 90%"
              />
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="审计处理意见及建议"
            prop="handlingOpinions"
          >
            <template slot-scope="{ row }">
              <el-input
                :disabled="formDisabled"
                v-model="row.handlingOpinions"
                size="mini"
                style="width: 90%"
              />
            </template>
          </el-table-column>
          <el-table-column align="center" label="备注" prop="remarks">
            <template slot-scope="{ row }">
              <el-input
                :disabled="formDisabled"
                v-model="row.remarks"
                size="mini"
                style="width: 90%"
              />
            </template>
          </el-table-column>

          <el-table-column align="center" label="操作" v-if="!formDisabled">
            <template slot-scope="{ row, $index }">
              <el-button type="text" @click="removeItem(row, $index)">
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
        <div style="text-align: right; margin-top: 5px" v-if="!formDisabled">
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
                @click="handlePreview(row)"
                :disabled="false"
              >
                预览
              </el-button>
              <el-button
                type="text"
                @click="handleDelete(row)"
                v-if="!formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </div>
    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <SelectDepartment ref="company" @submit="selectedCompany" />
    <WorkRecord ref="workRecord" @selected="handleWorkRecordSelected" />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
      :multiple="false"
    ></project-manage>
  </el-dialog>
</template>

<script>
  import {
    getDetail,
    addOrUpdate,
    handleDeleteSub,
  } from '@/oapi/audit/newMyDraft'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import WorkRecord from '@/views/oilAudit/implement/components/options/workRecord.vue'
  const { baseURL } = require('@/config')
  import store from '@/store'
  import projectManage from '@/components/selectPerson.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import { getCurrSsProject } from '@/oapi/audit/project'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: {
      DepartmentOptions,
      WorkRecord,
      projectManage,
      SelectDepartment,
    },
    data() {
      return {
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        type: 'edit',
        loading: false,
        dialogFormVisible: false,
        formDisabled: false,
        formData: {
          auditeeName: '',
          auditeeNameId: '',
          projectName: '',
          auditMatters: '',
          basicWork: '',
          evidenceAccurate: '',
          verificationDescription: '',
          auditConclusion: '',
          handlingOpinions: '',
          remarks: '',
          probleMdraft: '',
          auditMyManuVerifyEntityList: [],
          summaryDraftMark: '',
          draftNumber: '',
          draftName: '',
          thedeptstaffname: '',
          thedeptstaffid: '',
          fhstaffname: '',
          fhstaffid: '',
        },
        id: '',
        rules: {
          draftNumber: [
            {
              required: true,
              message: '底稿编号不能为空',
              trigger: 'change',
            },
          ],
          draftName: [
            {
              required: true,
              message: '底稿名称不能为空',
              trigger: 'change',
            },
          ],
          auditeeName: [
            {
              required: true,
              message: '被审计单位名称不能为空',
              trigger: 'change',
            },
          ],
          projectName: [
            {
              required: true,
              message: '审计项目名称不能为空',
              trigger: 'blur',
            },
          ],
          auditMatters: [
            {
              required: true,
              message: '审计事项不能为空',
              trigger: 'blur',
            },
          ],
          basicWork: [
            {
              required: true,
              message: '基础工作不能为空',
              trigger: 'blur',
            },
          ],
          evidenceAccurate: [
            {
              required: true,
              message: '审计证据是否准确、真实、合法不能为空',
              trigger: 'blur',
            },
          ],
          verificationDescription: [
            {
              required: true,
              message: '审计查证事实描述是否详尽、充分不能为空',
              trigger: 'blur',
            },
          ],
          auditConclusion: [
            {
              required: true,
              message: '审计结论是否客观、公正不能为空',
              trigger: 'blur',
            },
          ],
          handlingOpinions: [
            {
              required: true,
              message:
                '审计处理意见及建议是否正确、具有可操作性和建设性不能为空',
              trigger: 'blur',
            },
          ],
          probleMdraft: [
            {
              required: true,
              message: '是否是问题底稿不能为空',
              trigger: 'change',
            },
          ],
        },
        typeId: '', //从我的任务页面打开时需要
        tableData: [],
        projectManagetype: '',
      }
    },
    methods: {
      // 获取当前实施项目
      async getCurrentProject() {
        let obj = {}
        await getCurrSsProject().then((res) => {
          if (res.code === 1) {
            obj = res.data.pj
          } else {
            obj = undefined
          }
        })
        return obj
      },
      //是否从我的任务页面打开,isFromMyTask:boolean
      async showModal(row, isFromMyTask = false, disabled, typeRow) {
        this.formDisabled = disabled
        this.typeRow = typeRow
        this.dialogFormVisible = true
        const currentProject = await this.getCurrentProject()
        this.formData.thedeptstaffname = currentProject.zyksryrwnames
        this.formData.thedeptstaffid = currentProject.zyksryids
        // console.log("row",row)
        if (isFromMyTask) {
          //从我的任务页面打开
          this.typeId = row.operateid || row.typeId
          this.type = 'add'
        } else {
          this.type = row ? (disabled ? 'detail' : 'edit') : 'add'
          if (row) {
            const res = await getDetail({ id: row.id })
            Object.assign(this.formData, res.data)
            this.tableData = res.data.attList
          }
        }
      },
      close() {
        this.$refs.ruleForm.resetFields()
        this.formData.auditeeName = ''
        this.formData.auditeeNameId = ''
        this.formData.projectName = ''
        this.formData.auditMatters = ''
        this.formData.basicWork = ''
        this.formData.evidenceAccurate = ''
        this.formData.verificationDescription = ''
        this.formData.auditConclusion = ''
        this.formData.handlingOpinions = ''
        this.formData.remarks = ''
        this.formData.probleMdraft = ''
        this.formData.auditMyManuVerifyEntityList = []
        this.formData.summaryDraftMark = ''
        this.formData.draftNumber = ''
        this.formData.draftName = ''
        this.formData.thedeptstaffname = ''
        this.formData.thedeptstaffid = ''
        this.formData.fhstaffname = ''
        this.formData.fhstaffid = ''
        this.typeId = ''
        this.dialogFormVisible = false
        this.tableData = []
      },
      async handleSubmit() {
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            this.loading = true
            let param = Object.assign({}, this.formData)
            //从我的任务页面打开
            if (this.typeId) {
              param.typeId = this.typeId
              param.templateId = this.typeRow && this.typeRow.templateId
            }
            let attIds = []
            this.tableData.map((v) => {
              attIds.push(v.attid)
            })
            delete param.attList
            // 过滤掉空对象，如果列表中的对象所有字段都为空则移除
            if (
              param.auditMyManuVerifyEntityList &&
              param.auditMyManuVerifyEntityList.length > 0
            ) {
              param.auditMyManuVerifyEntityList =
                param.auditMyManuVerifyEntityList.filter((item) => {
                  return Object.values(item).some(
                    (val) => val && String(val).trim()
                  )
                })
            }
            addOrUpdate({ ...param, attIds: attIds.toString() })
              .then(() => {
                this.$baseMessage('保存成功', 'success')
                this.$emit('queryData')
                this.close()
              })
              .catch((res) => {
                this.$baseMessage(res.msg, 'error')
              })
              .finally(() => {
                this.loading = false
              })
          }
        })
      },
      // 添加点击按钮
      handleAdd() {
        this.formData.auditMyManuVerifyEntityList.push({
          verificationDescription: '',
          auditConclusion: '',
          handlingOpinions: '',
        })
      },
      removeItem(row, index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            if (row.id) {
              handleDeleteSub({ id: row.id }).then(() => {
                this.formData.auditMyManuVerifyEntityList.splice(index, 1)
                this.$message({
                  type: 'success',
                  message: '删除成功!',
                })
              })
            } else {
              this.formData.auditMyManuVerifyEntityList.splice(index, 1)
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      handleWorkRecordSelected() {},
      handleDepartmentSelected(node) {
        //保存名称和对应的ID
        this.$set(this.formData, `auditeeName`, node.label)
        this.$set(this.formData, `auditeeNameId`, node.id)
      },
      selectedCompany(node) {
        //保存名称和对应的ID
        this.$set(this.formData, `auditeeName`, node.name)
        this.$set(this.formData, `auditeeNameId`, node.id)
      },
      async getChildlistPro(val) {
        const ids = val.map((res) => res.staffid)
        const names = val.map((res) => res.realname)
        if (this.projectManagetype == 'thedeptstaffname') {
          this.$set(this.formData, 'thedeptstaffid', ids.toString())
          this.$set(this.formData, 'thedeptstaffname', names.toString())
        } else {
          this.$set(this.formData, 'fhstaffid', ids.toString())
          this.$set(this.formData, 'fhstaffname', names.toString())
        }
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
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // async handlePreview(row) {
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
