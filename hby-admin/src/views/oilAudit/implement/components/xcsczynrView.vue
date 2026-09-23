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
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item
            label="结算项目编号"
            label-width="140px"
            prop="settleProjectNum"
          >
            <el-input
              v-model="formData.settleProjectNum"
              clearable
              placeholder="请输入结算项目编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" label-width="140px" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="计算金额（元）"
            label-width="140px"
            prop="settleAmount"
          >
            <el-input
              v-model.number="formData.settleAmount"
              clearable
              placeholder="请输入计算金额（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建设单位" prop="buildOrgName">
            <el-input
              v-model="formData.buildOrgName"
              clearable
              placeholder="请选择建设单位"
              :style="{ width: '266px' }"
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
          <el-form-item label="施工单位" prop="constructionOrgName">
            <el-input
              v-model="formData.constructionOrgName"
              clearable
              placeholder="请选择施工单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="建设单位项目经理" prop="buildUnitManageName">
            <el-input
              v-model="formData.buildUnitManageName"
              clearable
              disabled
              placeholder="请选择建设单位项目经理"
              style="width: 266px"
            />
            <el-button
              @click="handleObject('buildUnitManageName')"
              style="margin-left: 10px"
              type="primary"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计人员" prop="reviewStaffName">
            <el-input
              v-model="formData.reviewStaffName"
              clearable
              disabled
              placeholder="请选择审计人员"
              style="width: 266px"
            />
            <el-button
              @click="handleObject('reviewStaffName')"
              style="margin-left: 10px"
              type="primary"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="复审人员" prop="recheckStaffName">
            <el-input
              v-model="formData.recheckStaffName"
              clearable
              disabled
              placeholder="请选择复审人员"
              style="width: 266px"
            />
            <el-button
              @click="handleObject('recheckStaffName')"
              style="margin-left: 10px"
              type="primary"
              size="small"
            >
              选择
              <!-- @click="handleObject('recheckStaffName')" -->
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="现场审查时间"
            label-width="140px"
            prop="sceneReviewTime"
          >
            <!-- <el-date-picker
              v-model="formData.sceneReviewTime"
              placeholder="请输入现场审查时间"
              type="datetime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :style="{ width: '100%' }"
            /> -->
            <el-date-picker
              v-model="formData.sceneReviewTime"
              placeholder="请输入现场审查时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="核实主要内容"
            label-width="140px"
            prop="reviewContent"
          >
            <el-input
              v-model="formData.reviewContent"
              clearable
              type="textarea"
              :rows="6"
              placeholder="请输入核实主要内容"
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
              <div v-if="!disabled" style="margin-right: 10px">
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
      <el-button @click="add" type="primary" :loading="loading">确定</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>

    <!-- 人员组件 -->
    <Company ref="audiTree" @selected="getChildlistObj"></Company>

    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />

    <SelectDepartment ref="company" @submit="selectedCompany" />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
      :multiple="false"
    ></project-manage>
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import {
    siteReviewSaveOrUpdate,
    siteReviewFindOneById,
  } from '@/oapi/audit/implement'
  import { deleteFile, download } from '@/oapi/audit/report'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import Company from '@/components/CompanySelectUserByTree'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage from '@/components/selectPerson.vue'
  import { getCurrSsProject } from '@/oapi/audit/project'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: {
      DepartmentOptions,
      Company,
      SelectDepartment,
      projectManage,
      ProcessList,
    },
    name: 'xssczynrView',
    inheritAttrs: false,
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
          settleProjectNum: '',
          projectName: '',
          settleAmount: '',
          siteReviewAttEntityDtoList: [],
          buildOrgName: '',
          buildOrgId: '',
          constructionOrgName: '',
          constructionOrgId: '',
          buildUnitManageName: '',
          reviewStaffName: '',
          recheckStaffName: '',
          buildUnitManageId: '',
          reviewStaffId: '',
          recheckStaffId: '',
          reviewContent: '',
          sceneReviewTime: '',
        },
        tableData: [],
        footer: true,
        rules: {
          settleProjectNum: [
            {
              required: true,
              message: '请输入结算项目编号',
              trigger: 'blur',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请输入项目名称',
              trigger: 'blur',
            },
          ],
          settleAmount: [
            {
              required: true,
              message: '请输入计算金额（元）',
            },
            {
              type: 'number',
              message: '请输入数字',
            },
          ],
          buildOrgName: [
            {
              required: true,
              message: '请选择建设单位',
              trigger: 'blur',
            },
          ],
          constructionOrgName: [
            {
              required: true,
              message: '请选择施工单位',
              trigger: 'blur',
            },
          ],
          buildUnitManageName: [
            {
              required: true,
              message: '请选择建设单位项目经理',
              trigger: 'blur',
            },
          ],
          reviewStaffName: [
            {
              required: true,
              message: '请选择审计人员',
              trigger: 'blur',
            },
          ],
          recheckStaffName: [
            {
              required: true,
              message: '请选择复核人员',
              trigger: 'blur',
            },
          ],
          reviewContent: [
            {
              required: true,
              message: '请输入审核意见',
              trigger: 'blur',
            },
          ],
          sceneReviewTime: [
            {
              required: true,
              message: '请选择现场审核时间',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        depType: '',
        manType: '',
        typeRow: null,
        editId: '',
      }
    },
    methods: {
      openDep(type) {
        this.depType = type
        this.$refs.department.show()
      },
      async getChildlistPro(val) {
        const ids = val.map((res) => res.staffid)
        const names = val.map((res) => res.realname)
        if (this.manType == 'buildUnitManageName') {
          this.$set(this.formData, 'buildUnitManageId', ids.toString())
          this.$set(this.formData, 'buildUnitManageName', names.toString())
        } else if (this.manType == 'reviewStaffName') {
          this.$set(this.formData, 'reviewStaffId', ids.toString())
          this.$set(this.formData, 'reviewStaffName', names.toString())
        } else {
          this.$set(this.formData, 'recheckStaffId', ids.toString())
          this.$set(this.formData, 'recheckStaffName', names.toString())
        }
        // this.$set(this.formData, 'hjstaffid', ids.toString())
        // this.$set(this.formData, 'hjstaffname', names.toString())
      },
      handleDepartmentSelected(node) {
        console.log('node', node)
        if (this.depType == 'buildOrgName') {
          //保存名称和对应的ID
          this.$set(this.formData, `buildOrgName`, node.label)
          this.$set(this.formData, `buildOrgId`, node.id)
        } else {
          this.$set(this.formData, `constructionOrgName`, node.label)
          this.$set(this.formData, `constructionOrgId`, node.id)
        }
      },
      handleObject(type) {
        this.manType = type
        this.$refs['manage'].showEdit()
      },
      getChildlistObj(val) {
        // this.$set(this.formData, this.manType, val[0].realname)
        if (this.manType == 'buildUnitManageName') {
          this.$set(this.formData, 'buildUnitManageId', val.staffid)
          this.$set(this.formData, 'buildUnitManageName', val.realname)
        } else if (this.manType == 'reviewStaffName') {
          this.$set(this.formData, 'reviewStaffId', val.staffid)
          this.$set(this.formData, 'reviewStaffName', val.realname)
        } else {
          this.$set(this.formData, 'recheckStaffId', val.staffid)
          this.$set(this.formData, 'recheckStaffName', val.realname)
        }
      },
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      // 获取当前实施项目
      async getCurrentProject() {
        let obj = {}
        const { data } = await getCurrSsProject()
        obj = data.pj
        return obj
      },
      async showEdit(title, row, type) {
        this.typeRow = type
        this.dialogFormVisible = true
        if (row) {
          // this.formData = row
          this.getInfo(row)
        } else {
          // const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          // this.formData.editor = userInfo.realname
          // this.formData.orgid = userInfo.staffid
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          const currentProject = await this.getCurrentProject()
          this.formData.projectName = currentProject.projectName
          this.formData.projectId = currentProject.id
        }
      },
      async getInfo(row) {
        this.editId = row.id
        const res = await siteReviewFindOneById({ siteReviewId: row.id })
        Object.assign(this.formData, res.data)
        this.tableData = res.data.siteReviewAttEntityDtoList || []
      },
      close() {
        this.formData = {
          settleProjectNum: '',
          projectName: '',
          settleAmount: '',
          siteReviewAttEntityDtoList: [],
          buildOrgName: '',
          buildOrgId: '',
          constructionOrgName: '',
          constructionOrgId: '',
          buildUnitManageName: '',
          reviewStaffName: '',
          recheckStaffName: '',
          buildUnitManageId: '',
          reviewStaffId: '',
          recheckStaffId: '',
          reviewContent: '',
          sceneReviewTime: '',
        }

        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
        this.editId = ''
        this.$emit('fetchData')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let siteReviewAttEntityList = []
            this.tableData.map((item) => {
              console.log(item)
              siteReviewAttEntityList.push({
                attachmentId: item.attid,
              })
              // attids += item.attid
              // attids += ','
            })
            // attids = attids.substring(0, attids.length - 1)
            let params = { ...this.formData }
            delete params.siteReviewAttEntityDtoList
            const data = await siteReviewSaveOrUpdate({
              projectId: this.typeRow?.projectId,
              templateId: this.typeRow?.templateId,
              ...params,
              siteReviewAttEntityList,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.editId = data.data.id
              this.formData.id = data.data.id
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.loading = false
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
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
      // handlePreview(file) {},
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
      selectedCompany(node) {
        this.formData.buildOrgName = node.label
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(157, this.editId)
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
