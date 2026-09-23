<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      v-if="dialogFormVisible"
      append-to-body
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="100px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <!-- <el-col :span="12" v-if="showMJ">
            <el-form-item label="密级" prop="secrectLevelId"
            :rules="[{ required: true, trigger: 'change', message: '请选择密级' }]">
              <el-select
                v-model="formData.secrectLevelId"
                clearable
                placeholder="密级"
                style="width: 100%"
                :disabled="footer"
                @change="handleSecretLevelChange"
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
            <el-form-item label="知悉范围"  prop="staffScopeNames"
            :rules="[{ required: true, trigger: 'change', message: '请选择知悉范围' }]">
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
                :disabled="!formData.secrectLevelId || footer"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showMJ">
            <el-divider>基本信息</el-divider>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="项目名称" prop="mattername">
              <el-input
                v-model="formData.mattername"
                disabled
                placeholder="请输入"
                style="width: 70%; margin-right: 8px"
              ></el-input>
              <el-button type="primary" @click="handleDetail">
                查看详情
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <UEditor
              ref="ueditor"
              v-model="formData.mattercontext"
              :height="300"
              :templates="templates"
              template="FXSCYJ"
            />
          </el-col>
          <el-col :span="24">
            <el-divider>附件</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="!footer">
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
                width="120"
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
                    v-if="!footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer v-if="!footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
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
    <ReviewEdit ref="edit" />
  </div>
</template>
<script>
  import {
    insertRiskReview,
    insertRiskReviewOpinion,
    riskReviewOpinionDetails,
  } from '@/api/risk/riskReview'
  import { getAutoCode, downFieldById } from '@/api/risk/riskEvents'
  import UEditor from '@/components/UEditor'
  // import Tinymce from '@/components/Tinymce'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  import VabUpload from '@/extra/VabUpload'
  import store from '@/store'
  import { formatDate } from '@/utils/index'
  const { baseURL } = require('@/config')
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'

  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import projectCode from '@/views/fwgl/new/components/selectProjectCode.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { couldMJ, hasMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  import ZXPerson from '@/components/selectPerson'
  import ReviewEdit from '../../riskReviewBook/components/ReviewEdit.vue'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'NormalEdit',
    components: {
      projectCode,
      VabUpload,
      UEditor,
      CompanyTreeModel,
      CompanySelectUserByTree,
      CandidateUserSelect,
      SelectDepartment,
      ZXPerson,
      ReviewEdit,
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
        },
        showRow: {},
        list: [],
        tableData: [],
        reportid: undefined,
        rules: {
          mattername: [
            { required: true, message: '请输入事项名称', trigger: 'blur' },
          ],
          matterprojectcode: [
            { required: true, message: '请选择项目编码', trigger: 'change' },
          ],
          // riskreviewcode: [
          //   {
          //     required: true,
          //     message: '请输入风险审查报告编码',
          //   },
          // ],
          // mattercode: [
          //   { required: true, message: '请输入事项编码', trigger: 'blur' },
          // ],
        },
        uploadLoading: false,
        footer: false,
        isaotoStatus: false,
        reviewid: '',
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        removeIds: [],
        tableDataFile: [],
        //提交

        status: 0,
        jurisdictionCode: 0,
        showMJ: false,
        MJoption: [],
      }
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('MonitorCompetitors')
        this.menuId = res[0].menuid
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      handleSecretLevelChange() {
        // 清空知悉范围
        this.formData.staffScopeNames = ''
        this.formData.staffScopeIds = ''
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
          let params = { ...this.formData, reviewid: this.reviewid }
          delete params.createtime
          if (valid) {
            insertRiskReviewOpinion({ ...params }).then((res) => {
              if (res.code === 1) {
                this.$baseMessage('保存成功', 'success')
                this.$emit('fetchData')
                this.close()
              }
            })
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
      async showEdit(row, type) {
        this.rowData = row
        this.fileIdList = []
        this.removeIds = []
        this.tableDataFile = []
        console.log(row.isaoto)
        this.formData.mattername = row.mattername
        if (type == 'add') {
          this.title = '添加'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.$set(this.formData, 'staffidname', userInfo.realname)
          this.$set(this.formData, 'staffid', userInfo.staffid)
          this.$set(this.formData, 'staffdeptname', userInfo.linkDetp.orgname)
          this.$set(this.formData, 'staffdept', userInfo.linkDetp.orgid)
          this.$set(this.formData, 'createtime', new Date())
        } else if (type == 'edit') {
          this.getInfo(row.reviewid)
          this.reviewid = row.reviewid
          this.title = '编辑'
          if (row.isaoto == 2) {
            this.isaotoStatus = true
          } else {
            this.isaotoStatus = false
          }
        } else {
          this.reviewid = row.reviewid
          this.getInfo(row.reviewid)
          this.footer = true
          this.isaotoStatus = true
          this.title = '查看'
        }
        this.dialogFormVisible = true
      },
      /**
       * @description: 获取数据接口
       * @return {*}
       */
      async getInfo(id) {
        const res = await riskReviewOpinionDetails({ id: id })
        this.formData = { ...this.formData, ...res.data.opinion }
        this.tableDataFile = res.data.attachments || []
        console.log('🚀 ~ getInfo ~ this.tableDataFile:', this.tableDataFile)
        this.fileIdList = res.data.attachments.map((item) => item.attid)
      },
      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
        this.footer = false
        this.reviewid = ''
        this.formData.secrectLevelId = ''
        this.formData.staffScopeNames = ''
        this.formData.staffScopeIds = ''
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
      //查看详情
      handleDetail(row) {
        this.$refs['edit'].showEdit(this.rowData, 'detail')
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
