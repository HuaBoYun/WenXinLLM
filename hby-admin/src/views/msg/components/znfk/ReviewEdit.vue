<template>
  <div>
    <el-row :gutter="15" v-loading="loading">
      <el-form
        ref="elForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              :disabled="isaotoStatus"
              @change="changeMJ"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="formData.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || isaotoStatus"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider>基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="mattername">
            <el-input
              v-model="formData.mattername"
              :disabled="isaotoStatus"
              clearable
              placeholder="请输入"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="项目编码" prop="matterprojectcode">
            <el-input
              v-model="formData.matterprojectcode"
              disabled
              placeholder="请选择"
              style="width: 75%; margin-right: 8px"
            ></el-input>
            <el-button
              type="primary"
              @click="handleCode"
              :disabled="formDisabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="风险审查报告编码" prop="riskreviewcode">
            <el-input
              v-model="formData.riskreviewcode"
              clearable
              :disabled="isaotoStatus"
              placeholder="请输入"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="三重一大事项编码" prop="mattercode">
            <el-input
              v-model="formData.mattercode"
              :disabled="isaotoStatus"
              clearable
              placeholder="请输入"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请时间" prop="createtime">
            <el-date-picker
              v-model="formData.createtime"
              clearable
              disabled
              format="yyyy-MM-dd"
              placeholder="请选择申请时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="经办人" prop="staffidname">
            <el-input
              v-model="formData.staffidname"
              disabled
              style="width: 75%; margin-right: 8px"
            ></el-input>
            <el-button
              type="primary"
              @click="handleShowUser"
              :disabled="formDisabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办部门" prop="staffdeptname">
            <el-input
              v-model="formData.staffdeptname"
              disabled
              style="width: 75%; margin-right: 8px"
            ></el-input>
            <el-button
              type="primary"
              @click="handleShowCompent"
              :disabled="formDisabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办公司" prop="staffunitname">
            <el-input
              v-model="formData.staffunitname"
              placeholder="请选择经办公司"
              style="width: 75%; margin-right: 8px"
              readonly
              disabled
            />
            <el-button
              type="primary"
              :disabled="formDisabled"
              @click="$refs.audiTree.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目决策主体" prop="decisionmaking">
            <el-input
              v-model="formData.decisionmaking"
              :disabled="isaotoStatus"
              clearable
              placeholder="请输入"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.mattercontext"
              :height="300"
              :templates="templates"
              :disabled="formDisabled"
              :key="editorKey"
              template="XMFXSC"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
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
          <el-table :data="tableDataFile">
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
                <el-button type="text" @click="handlePreviewFile(row)">
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
      </el-form>
    </el-row>
    <div style="text-align: right" v-if="!formDisabled">
      <el-button type="primary" @click="save">确定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
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
    <vab-upload
      ref="vabUpload"
      :limit="50"
      name="file"
      :size="2"
      url="/upload"
    />
    <!-- 部门选择 -->
    <CompanyTreeModel ref="comTreeRef" @selected="handleSelectCompany" />
    <CompanySelectUserByTree
      ref="userTreeRef"
      @selected="handleExecutorSelected"
    />
    <projectCode
      @getProjectCode="getProjectCode"
      ref="projectCode"
    ></projectCode>
    <SelectDepartment ref="audiTree" @submit="handleUnitSelected" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>
<script>
  import {
    getDefaultReportInfo,
    saveReportData,
    addReportData,
  } from '@/api/risk/report'
  import {
    insertRiskReview,
    updateRiskReview,
    riskReviewDetails,
  } from '@/api/risk/riskReview'
  import { getAutoCode, downFieldById } from '@/api/risk/riskEvents'
  // import Tinymce from '@/components/Tinymce'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  import VabUpload from '@/extra/VabUpload'
  import store from '@/store'
  import { formatDate } from '@/utils/index'
  const { baseURL } = require('@/config')
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'

  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import projectCode from '@/views/fwgl/new/components/selectProjectCode.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import ZXPerson from '@/components/selectPerson'
  import { couldMJ, hasMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'NormalEdit',
    components: {
      projectCode,
      VabUpload,
      UEditor: () => import('@/components/UEditor'),
      CompanyTreeModel,
      CompanySelectUserByTree,
      CandidateUserSelect,
      Resubmit,
      SelectDepartment,
      ZXPerson,
    },
    props: [],
    data() {
      return {
        title: '',
        templates: [],
        fileIdList: [],
        dialogFormVisible: false,
        formData: {
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
          decisionmaking: '',
        },
        showRow: {},
        list: [],
        tableData: [],
        reportid: undefined,
        rules: {
          mattername: [
            { required: true, message: '请输入项目名称', trigger: 'blur' },
          ],
          matterprojectcode: [
            { required: true, message: '请选择项目编码', trigger: 'blur' },
          ],
          // riskreviewcode: [
          //   {
          //     required: true,
          //     message: '请输入风险审查报告编码',
          //     trigger: 'blur',
          //   },
          // ],
          // mattercode: [
          //   { required: true, message: '请输入事项编码', trigger: 'blur' },
          // ],
        },
        uploadLoading: false,
        isaotoStatus: false,
        reviewid: '',
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        removeIds: [],
        tableDataFile: [],
        //提交

        status: 0,
        jurisdictionCode: 0,
        editorKey: new Date().getTime(),
        formDisabled: false,
        loading: false,
        // 流程
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        showMJ: false,
        MJoption: [],
        btnLoading: false,
      }
    },
    computed: {
      getFormLevel() {
        if (!this.formData.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.formData.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      /**
       * @description: 打开详细
       * @return {*}
       */
      handleCode() {
        this.$refs['projectCode'].showEdit()
      },
      /**
       * @description: 获取编码
       * @return {*}
       */
      getProjectCode(val) {
        const projectcode = val[0].projectcode
        this.$set(this.formData, 'matterprojectcode', projectcode)
        this.getCode(projectcode)
      },
      /**
       * @description: 编码接口
       * @return {*}
       */
      async getCode(val) {
        const params = {
          projectCode: val,
        }
        const res = await getAutoCode(params)
        console.log(res, 'res')
        this.$set(this.formData, 'riskreviewcode', res.data)
      },
      // 调起部门选择
      handleShowCompent() {
        this.$refs['comTreeRef'].show()
      },
      // 部门选择赋值
      handleSelectCompany(e) {
        this.$set(this.formData, 'staffdeptname', e.name)
        this.$set(this.formData, 'staffdept', e.id)
      },
      // 部门人员选择
      handleShowUser() {
        this.$refs['userTreeRef'].show()
      },
      handleExecutorSelected(e) {
        this.$set(this.formData, 'staffidname', e.realname)
        this.$set(this.formData, 'staffid', e.staffid)
      },
      handleEdit2(row) {
        console.log('rrr', row)
      },
      /**
       * @description: 保存
       * @return {*}
       */
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          this.formData.matterfileids = this.fileIdList.join(',')
          delete this.formData.createtime
          if (valid) {
            if (this.reviewid) {
              updateRiskReview({ ...this.formData }).then((res) => {
                if (res.code === 1) {
                  this.$baseMessage('保存成功', 'success')
                  this.$emit('reloadTable')
                }
              })
            } else {
              insertRiskReview({ ...this.formData }).then((res) => {
                this.$baseMessage('保存成功', 'success')
                this.$emit('reloadTable')
                this.close()
              })
            }
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      /**
       * @description: 页面初始化，获取数据
       * @return {*}
       */
      async ymsubmit() {
        try {
          this.$refs['elForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName,
        flowType
      ) {
        // 拿到类型传给getMJData获取审批的密级的下拉数据
        if (flowType) {
          this.getMJData(flowType)
        }
        this.fileIdList = []
        this.removeIds = []
        this.tableDataFile = []
        this.loading = true
        console.log('title', title)

        this.reviewid = formId
        await this.getInfo(formId)
        this.loading = false
        if (title == 'detail') {
          this.formDisabled = true
          this.isaotoStatus = true
        }
        this.editorKey = new Date().getTime()
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
      },
      /**
       * @description: 获取数据接口
       * @return {*}
       */
      async getInfo(id) {
        const res = await riskReviewDetails({ reviewId: id })
        if (res.data.riskReview.isaoto == 2) {
          this.isaotoStatus = true
        } else {
          this.isaotoStatus = false
        }
        if (res.data.riskReview?.secrectLevelId) {
          localStorage.setItem(
            'SPsecrectLevelId',
            res.data.riskReview.secrectLevelId
          )
        }
        this.formData = { ...this.formData, ...res.data.riskReview }
        this.tableDataFile = res.data.attachments || []
        this.fileIdList = this.tableDataFile.map((item) => item.attid)
      },
      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.$refs['elForm'].resetFields()
        this.formData = {}
        this.tableDataFile = []
        this.fileIdList = []
        this.$bus.$emit('updateMsg', 0)
      },

      /**
       * @description: 删除附件
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, () => {
          const deleteId = row.attid
          this.removeIds.push(deleteId)
          //删除formData要返回给后端的id
          this.fileIdList = this.fileIdList.filter((item) => {
            return item != deleteId
          })
          //删除tableDataFile，假删除
          this.tableDataFile = this.tableDataFile.filter((item) => {
            return item != row
          })
          this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      handleUnitSelected(node) {
        this.$set(this.formData, 'staffunitname', node.label)
        this.$set(this.formData, 'staffunit', node.id)
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

        // 获取 el-upload 的 data 参数
        const formData = {
          formlevel: this.getFormLevel,
        }

        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            formData: formData, // 传递额外的表单数据
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
          // 确保 file.data 是数组格式
          const uploadedFiles = Array.isArray(file.data)
            ? file.data
            : [file.data]

          // 更新文件列表
          this.fileList = [...this.fileList, ...uploadedFiles]
          this.tableDataFile = [...this.tableDataFile, ...uploadedFiles]

          // 保存上传文件的attid到fileIdList
          uploadedFiles.forEach((fileItem) => {
            if (fileItem.attid) {
              this.fileIdList.push(fileItem.attid)
            }
          })

          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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
<style></style>
