<template>
  <div>
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
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
              :disabled="!footer"
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
              disabled
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || !footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>

        <el-col :span="24">
          <el-form-item label="项目名称" label-width="140px" prop="prjoectName">
            <el-input
              v-model="formData.prjoectName"
              clearable
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="被审计（调查）单位或个人"
            label-width="140px"
            prop="orgIdNames"
          >
            <el-input
              v-model="formData.orgIdNames"
              clearable
              placeholder="请输入被审计（调查）单位或个人"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="审计（调查）事项标题"
            label-width="140px"
            prop="auditMatter"
          >
            <el-input
              v-model="formData.auditMatter"
              clearable
              placeholder="请输入审计（调查）事项标题"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="审计（调查）事项概述"
            label-width="140px"
            prop="auditAbstract"
          >
            <el-input
              v-model="formData.auditAbstract"
              clearable
              placeholder="请输入被审计（调查）事项概述"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计人员"
            label-width="140px"
            prop="auditUserName"
          >
            <el-input
              v-model="formData.auditUserName"
              clearable
              placeholder="请输入审计人员"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="一级复核" label-width="140px" prop="yjfh">
            <el-input
              v-model="formData.yjfh"
              clearable
              placeholder="请选择一级复核人"
              style="width: 266px"
              disabled
            />
            <el-button
              @click="projectManager1"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="二级复核" label-width="140px" prop="ejfh">
            <el-input
              v-model="formData.ejfh"
              clearable
              placeholder="请选择二级复核人"
              style="width: 266px"
              disabled
            />
            <el-button
              @click="projectManager2"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计日期"
            label-width="140px"
            prop="certificateDate"
          >
            <el-input
              v-model="formData.certificateDate"
              clearable
              placeholder="请输入审计日期"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="被审计单位办事员"
            label-width="140px"
            prop="certificateUser"
          >
            <el-input
              v-model="formData.certificateUser"
              clearable
              placeholder="被审计单位办事员"
              disabled
              :style="{ width: '80%' }"
            />
            <el-button
              @click="assistecertificateUser"
              style="margin-left: 10px"
              size="small"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="被审计单位科长"
            prop="assistedbmfzr"
            label-width="140px"
          >
            <el-input
              v-model="formData.assistedbmfzr"
              clearable
              placeholder="被审计单位科长"
              disabled
              :style="{ width: '80%' }"
            />
            <el-button
              @click="assistedbmfzrManager"
              style="margin-left: 10px"
              size="small"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="被审计单位公司主管"
            prop="assistedfgld"
            label-width="140px"
          >
            <el-input
              v-model="formData.assistedfgld"
              clearable
              placeholder="被审计单位公司主管"
              disabled
              :style="{ width: '80%' }"
            />
            <el-button
              @click="assistedfgldManager"
              style="margin-left: 10px"
              size="small"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="业务人员"
            prop="assistedzbuser"
            label-width="140px"
          >
            <el-input
              v-model="formData.assistedzbuser"
              clearable
              placeholder="业务人员"
              disabled
              :style="{ width: '80%' }"
            />
            <el-button
              @click="assistedzbuserManager"
              style="margin-left: 10px"
              size="small"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="证据提供单位意见"
            label-width="140px"
            prop="evidenceOpinion"
          >
            <el-input
              v-model="formData.evidenceOpinion"
              clearable
              placeholder="请输入证据提供单位意见"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="nextNodeName != '业务人员'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>盖章附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="nextNodeName == '业务人员'"
          >
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess2"
              :file-list="stampedList"
              :before-upload="handleBeforeUpload2"
            >
              <el-button
                type="success"
                :disabled="showMJ && !formData.secrectLevelId"
              >
                上传盖章附件
              </el-button>
            </el-upload>
          </div>
          <el-table :data="stampedTableData">
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
                  @click="handleStampedDelete(row)"
                  v-if="nextNodeName == '业务人员'"
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
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
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
                <el-button
                  type="success"
                  :disabled="showMJ && !formData.secrectLevelId"
                >
                  点击上传
                </el-button>
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
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px">
      <el-button type="primary" @click="add" v-if="footer">确 定</el-button>
      <el-button
        type="primary"
        @click="ymsubmit"
        :disabled="btnLoading"
        v-if="footer"
      >
        提 交
      </el-button>
    </div>
    <!-- <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add()" type="primary">确定</el-button>
    </template> -->
    <project-manage
      @projectManage="getChildlistPro1"
      ref="manage1"
    ></project-manage>
    <project-manage
      @projectManage="getChildlistPro2"
      ref="manage2"
    ></project-manage>
    <SelectPersonModal
      @projectManage="selectPerson"
      ref="manage3"
    ></SelectPersonModal>
    <!-- 复核人选择 -->
    <projectManage
      :modal="false"
      ref="manage"
      @reviewTypeSelect="reviewTypeSelect"
    />

    <project-manage
      @projectManage="getChildlistPro4"
      ref="manage4"
    ></project-manage>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import {
    createImPlementDetail,
    createImPlementOrder,
    deleteFile,
    download,
    imPlementOrderFiles,
    getStampedDocumentList,
    saveStampedDocument,
    removeStampedDocument,
  } from '@/api/audit/implement'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  import projectManage from '@/components/danxuanPerson.vue'
  import SelectPersonModal from '@/views/audit/implement/components/options/selectZBSJRY.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'

  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  import { baseURL } from '@/config'
  export default {
    name: 'effectDetail11',
    inheritAttrs: false,
    components: {
      projectManage,
      SelectPersonModal,
      Resubmit,
      ZXPerson,
    },
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        stampedList: [],
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          prjoectName: '',
          auditDate: '',
          auditUserName: '',
          auditUserId: '',
          orgIdNames: '',
          certificateDate: '',
          certificateUser: '',
          certificateStaffId: '',
          projectId: '',
          orgId: '',
          auditMatter: '',
          evidenceOpinion: '',
          auditAbstract: '',
          assistedbmfzr: '', //协办部门负责人
          assistedbmfzrid: '', //协办部门负责人id
          assistedfgld: '', //协办部门分管领导
          assistedfgldid: '', //协办部门分管领导id
          assistedzbuser: '', //总部审计部门人员
          assistedzbuserid: '', //总部审计部门人员id
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        tableData: [],
        stampedTableData: [], // 新增盖章附件数据
        footer: true,
        rules: {
          auditMatter: [
            {
              required: true,
              message: '请输入审计（调查）事项',
              trigger: 'blur',
            },
          ],
          yjfh: [
            {
              required: true,
              message: '请选择一级复核人',
              trigger: 'blur',
            },
          ],
          ejfh: [
            {
              required: true,
              message: '请选择二级复核人',
              trigger: 'blur',
            },
          ],
          certificateUser: [
            {
              required: true,
              message: '请选择证据提供者',
              trigger: 'blur',
            },
          ],
          assistedbmfzr: [
            {
              required: true,
              message: '请选择被审计单位科长',
              trigger: 'blur',
            },
          ],
          assistedfgld: [
            {
              required: true,
              message: '请选择被审计单位主管',
              trigger: 'blur',
            },
          ],
          assistedzbuser: [
            {
              required: true,
              message: '请选择总部审计部门人员',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',

        //提交
        ymFromId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        nextNodeName: '',
        btnLoading: false,
        showMJ: false,
        MJoption: [],
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
    // async created() {
    //   this.showMJ = couldMJ()
    //   if (this.showMJ) {
    //     // 获取密级,菜单id
    //     const res = await hasMJ('Implement/evidence')
    //     this.menuId = res[0].menuid
    //     // 请求密级下拉数据
    //     const res2 = await getMJ({ rightId: res[0].menuid })
    //     this.MJoption = res2.data
    //   }
    // },
    methods: {
      changeMJ(selectedValue) {
        // 切换密级时清空知悉范围
        this.formData.staffScopeIds = ''
        this.formData.staffScopeNames = ''
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
        // 切换密级时清空附件列表
        this.tableData = []
        this.stampedTableData = []
        this.fileList = []
        this.stampedList = []
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      handleBeforeUpload(file) {
        const isLt50M = file.size / 1024 / 1024 < 50
        if (!isLt50M) {
          this.$message.error('上传文件大小不能超过 50MB!')
          return false
        }
        this.customUploadWrapper(file)
        return false
      },
      // 盖章附件上传前检查
      handleBeforeUpload2(file) {
        const isLt50M = file.size / 1024 / 1024 < 50
        if (!isLt50M) {
          this.$message.error('盖章附件大小不能超过 50MB!')
          return false
        }
        this.customStampedUploadWrapper({ file })
        return false
      },
      // 自定义上传封装
      // customUploadWrapper(file) {
      //   customUpload(file, this.headers)
      //     .then((response) => {
      //       this.handleSuccess(response)
      //     })
      //     .catch((error) => {
      //       console.error('上传失败:', error)
      //       this.$message.error('文件上传失败')
      //     })
      // },
      // 自定义盖章附件上传封装
      // customStampedUploadWrapper(file) {
      //   customUpload(file, this.headers)
      //     .then((response) => {
      //       this.handleSuccess2(response)
      //     })
      //     .catch((error) => {
      //       console.error('盖章附件上传失败:', error)
      //       this.$message.error('盖章附件上传失败')
      //     })
      // },
      async showEdit(
        title,
        row,
        fromId,
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
        this.nextNodeName = nextNodeName
        if (fromId) {
          this.fromId = fromId
          this.fromIdcopy = fromId // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid
        }
        if (ymFromId) {
          this.ymFromId = ymFromId
        }
        this.status = status
        this.dialogFormVisible = true
        if (row) {
          this.$nextTick(() => {
            this.formData = row
            // 保存审批用的密级id
            if (row.secrectLevelId) {
              localStorage.setItem('SPsecrectLevelId', row.secrectLevelId)
            }
            this.formData.certificateDate = formatDay(row.createDate)
            this.formData.auditUserName = row.auditUserName
            this.formData.auditUserId = row.auditUserId
            this.formData.orgIdNames = row.orgIdNames
            this.formData.prjoectName = row.prjoectname

            this.getFileList(row.certificateId)
            this.getStampedFileList(row.certificateId) // 获取盖章附件列表
          })
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.editor = userInfo.realname
          this.formData.orgid = userInfo.staffid
        }
        if (title == 'edit') {
          this.title = '编辑'
          this.footer = true
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.getProjectDetail()
        }
      },
      async getFileList(certificateId) {
        const data = await imPlementOrderFiles({ certificateId })
        this.tableData = data.data.data || []
      },
      async getProjectDetail() {
        const {
          data: { pj },
        } = await createImPlementDetail({
          token: this.headers.token,
        })
        let formData = this.formData
        this.formData = null

        formData.prjoectName = pj.prjoectName
        formData.orgIdNames =
          pj.isBmAudit == 1 ? pj.orgIdNames : pj.auditStaffName
        this.formData = formData
      },
      close() {
        this.$refs['ruleForm'].resetFields()
        // this.formData = {}

        this.$bus.$emit('updateMsg', 0)
        this.tableData = []
        this.footer = true
        this.dialogFormVisible = false
        //
      },
      add() {
        // 返回 Promise 以便父组件可以等待操作完成
        return new Promise((resolve) => {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              let attids = '',
                stampedAttids = [], // 新增盖章附件ID字符串
                result = null
              this.tableData.map((item) => {
                attids += item.attid
                attids += ','
              })
              attids = attids.substring(0, attids.length - 1)

              // 处理盖章附件ID
              this.stampedTableData.map((item) => {
                stampedAttids.push(item.attid)
              })
              const { certificateDate, createDate, ...other } = this.formData

              result = await createImPlementOrder({
                ...other,
                attids,
              })

              // 如果有盖章附件，在这里调用saveStampedDocument
              console.log('🚀 ~ add ~ stampedAttids:', stampedAttids)
              if (stampedAttids.length > 0 && this.formData.certificateId) {
                await this.saveStampedDocument(
                  stampedAttids,
                  this.formData.certificateId
                )
              }

              result.code == 1 ? this.$baseMessage('保存成功', 'success') : ''
              this.$emit('fetch-data')
              resolve(true)
            } else {
              resolve(false)
            }
          })
        })
      },
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
        this.$message.success('删除成功')
      },
      assistedbmfzrManager() {
        this.$refs['manage1'].showEdit()
      },
      getChildlistPro1(val) {
        this.$set(this.formData, 'assistedbmfzr', val[0].realname)
        this.$set(this.formData, 'assistedbmfzrid', val[0].staffid)
      },
      assistedfgldManager() {
        this.$refs['manage2'].showEdit()
      },
      getChildlistPro2(val) {
        this.$set(this.formData, 'assistedfgld', val[0].realname)
        this.$set(this.formData, 'assistedfgldid', val[0].staffid)
      },
      assistedzbuserManager() {
        this.$refs['manage3'].showEdit([], this.formData.projectId)
      },
      getChildlistPro3(val) {
        this.$set(this.formData, 'assistedzbuser', val[0].realname)
        this.$set(this.formData, 'assistedzbuserid', val[0].staffid)
      },

      assistecertificateUser() {
        this.$refs['manage4'].showEdit()
      },
      getChildlistPro4(val) {
        this.$set(this.formData, 'certificateUser', val[0].realname)
        this.$set(this.formData, 'certificateStaffId', val[0].staffid)
      },
      projectManager1() {
        this.$refs['manage'].showEdit('firststaffid')
      },
      projectManager2() {
        this.$refs['manage'].showEdit('secondstaffid')
      },
      reviewTypeSelect(e) {
        let name = e.reviewType === 'firststaffid' ? 'yjfh' : 'ejfh'
        this.$set(this.formData, `${name}`, e.id[0].realname)
        this.$set(this.formData, `${e.reviewType}`, e.id[0].staffid)
      },
      selectPerson(info, type) {
        if (type == 'assistedzbuser') {
          this.$set(this.formData, 'assistedzbuser', info[0].staff.realname)
          this.$set(this.formData, 'assistedzbuserid', info[0].staffid)
        } else if (type == 'firststaffid') {
          this.$set(this.formData, 'yjfh', info[0].staff.realname)
          this.$set(this.formData, 'firststaffid', info[0].staffid)
        } else if (type == 'secondstaffid') {
          this.$set(this.formData, 'ejfh', info[0].staff.realname)
          this.$set(this.formData, 'secondstaffid', info[0].staffid)
        }
      },

      // 获取盖章附件列表
      async getStampedFileList(certificateId) {
        const { code, data, msg } = await getStampedDocumentList({
          certificateId,
        })
        console.log('🚀 ~ getStampedFileList ~ msg:', msg)
        console.log('🚀 ~ getStampedFileList ~ data:', data)
        console.log('🚀 ~ getStampedFileList ~ code:', code)
        if (code == 1) {
          this.stampedTableData = data
        } else {
          this.$baseMessage(msg, 'error')
        }
      },

      // 盖章附件上传成功处理
      handleSuccess2(file) {
        if (file.code == 200) {
          this.stampedList = [...this.stampedList, ...file.data]
          this.stampedTableData = [...this.stampedTableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },

      // 保存盖章附件信息 - 修改此方法，不再单独调用
      async saveStampedDocument(attIds, certificateId) {
        const { result, msg, data } = await saveStampedDocument({
          certificateId: certificateId,
          attids: attIds,
        })
        if (result != 500) {
          console.log('🚀 ~ saveStampedDocument ~ result:', result)
        } else {
          this.$baseMessage(msg, 'error')
        }
      },
      // 盖章附件删除
      async handleStampedDelete(row) {
        let list = this.stampedTableData
        list = list.filter((item) => item.attid != row.attid)
        this.stampedTableData = list
        let res = await removeStampedDocument({
          certificateId: this.formData.certificateId,
          attid: row.attid,
        })
        if (res.code == 1) {
          this.$message.success('删除成功')
        } else {
          this.$message.error('删除失败')
        }
      },
      //提交
      async ymsubmit() {
        try {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      customStampedUploadWrapper(options) {
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
              this.handleSuccess2(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
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
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
