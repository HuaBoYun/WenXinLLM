<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
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
              placeholder="请输入审计（调查）事项概述"
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

        <!-- <el-col :span="12">
          <el-form-item label="日期" label-width="140px" prop="certificateDate">
            <el-date-picker
              v-model="formData.certificateDate"
              placeholder="请输入日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item
            label="证据提供者"
            label-width="140px"
            prop="certificateUser"
          >
            <el-input
              v-model="formData.certificateUser"
              clearable
              placeholder="证据提供者"
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
            label="协办部门负责人"
            prop="assistedbmfzr"
            label-width="140px"
          >
            <el-input
              v-model="formData.assistedbmfzr"
              clearable
              placeholder="协办部门负责人"
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
            label="协办部门分管领导"
            prop="assistedfgld"
            label-width="140px"
          >
            <el-input
              v-model="formData.assistedfgld"
              clearable
              placeholder="协办部门分管领导"
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
            label="项目负责人"
            prop="assistedzbuser"
            label-width="140px"
          >
            <el-input
              v-model="formData.assistedzbuser"
              clearable
              placeholder="项目负责人"
              disabled
              :style="{ width: '80%' }"
            />
            <el-button
              @click="assistedzbuserManager"
              style="margin-left: 10px"
              size="small"
              type="primary"
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
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
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
                <el-button type="text" @click="handleDown(row)">下载</el-button>
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
      <el-button @click="add()" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add()" type="primary">确定</el-button>
      <!-- <el-button
        v-if="
          (formData.status == 2 || formData.status == 3) &&
          jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button> -->
    </template>
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
    <!-- 提交 -->
    <el-dialog
      @close="currentClose"
      title="选择分支"
      :visible="visible"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form
        label-width="100px"
        ref="fzforms"
        :modal="fzforms"
        :rules="fzRules"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzforms.branchStrs"
            @change="handlefzChange"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :label="item.nodeName"
              :value="item.nodeId"
              :key="item.nodeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in this.runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            prop="transferStaffName"
            v-if="item.hasCandidates"
          >
            <!-- <el-input
              disabled
              placeholder="请选择候选人"
              v-model="formData3[index].transferStaffName"
              style="width: 79%; margin-right: 8px"
            ></el-input>
            <el-button type="primary" @click="handleSelectNew(item, index)">
              请选择
            </el-button> -->
            <CandidateUserSelect
              :clearType="clearType"
              @selected="handleCandSelect"
              :index="index"
              :nodeId="item.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
            />
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="currentClose">取消</el-button>
        <el-button type="primary" @click="save4">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      @close="close1"
      title="选择分支"
      :visible="visible1"
      :append-to-body="true"
      :close-on-click-modal="false"
      v-if="visible1"
    >
      <el-form
        :modal="fzform1"
        label-width="100px"
        ref="fzform1"
        :rules="fzRules1"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzform1.branchStrs"
            @change="selectValue"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :key="item.nodeId"
              :label="item.nodeName"
              :value="item.nodeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in this.runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            prop="transferStaffName"
            v-if="item.hasCandidates"
          >
            <!-- <el-input
              disabled
              placeholder="请选择候选人"
              v-model="formData3[index].transferStaffName"
              style="width: 79%; margin-right: 8px"
            ></el-input>
            <el-button type="primary" @click="handleSelectNew(item, index)">
              请选择
            </el-button> -->
            <CandidateUserSelect
              :clearType="clearType"
              @selected="handleCandSelect"
              :index="index"
              :nodeId="item.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
            />
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="close1">取消</el-button>
        <el-button type="primary" @click="save1">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="选择候选人"
      :visible.sync="visible2"
      :close-on-click-modal="false"
      width="40%"
      :modal="false"
      @close="close2"
      v-if="visible2"
    >
      <el-form
        :model="formData2"
        :rules="rules2"
        ref="ruleForm2"
        label-width="80px"
      >
        <el-form-item label="候选人" prop="transferStaffName">
          <!-- <el-input
            disabled
            placeholder="请选择候选人"
            v-model="formData2.transferStaffName"
            style="width: 79%; margin-right: 8px"
          ></el-input>
          <el-button type="primary" @click="handleSelect">请选择</el-button> -->
          <CandidateUserSelect
            :clearType="clearType"
            @selected="handleCandSelect1"
            :index="0"
            :nodeId="candidateData.nodeId"
            :candidateData="candidateData"
            multiple
            placeholder="请选择候选人"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close2">取 消</el-button>
        <el-button type="primary" @click="save2">确 定</el-button>
      </span>
    </el-dialog>
  </el-dialog>
</template>

<script>
  import {
    createImPlementDetail,
    createImPlementOrder,
    deleteFile,
    download,
    imPlementOrderFiles,
  } from '@/oapi/audit/implement'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  import projectManage from '@/views/oilAudit/project/components/formComponents/projectManage.vue'
  import SelectPersonModal from './options/selectZBSJRY.vue'
  const { baseURL } = require('@/config')
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/oapi/contract/manage'
  import { getFaqiInfo } from '@/oapi/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'

  export default {
    name: 'DoubtfulInfo',
    inheritAttrs: false,
    props: ['effectDetail'],
    components: { projectManage, SelectPersonModal, CandidateUserSelect },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
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
          orgIds: '',
          auditMatter: '',
          evidenceOpinion: '',
          auditAbstract: '',
          assistedbmfzr: '', //协办部门负责人
          assistedbmfzrid: '', //协办部门负责人id
          assistedfgld: '', //协办部门分管领导
          assistedfgldid: '', //协办部门分管领导id
          assistedzbuser: '', //总部审计部门人员
          assistedzbuserid: '', //总部审计部门人员id
          status: 0,
        },
        tableData: [],
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
        visible: false,
        fzforms: {
          branchStrs: [],
        },
        fzRules: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fzform1: {
          branchStrs: [],
        },
        fzRules1: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fzoptions: [],
        runderList: [],
        candidateData: {},
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        visible1: false,
        visible2: false,
        formData2: {
          transferStaffName: '',
          transferStaffId: '',
        },
        status: 0,
        jurisdictionCode: 0,
        clearType: false,
      }
    },
    computed: {},
    watch: {
      effectDetail(newValue, oldValue) {
        this.formData = {}
        let formData = {}
        formData.prjoectName = newValue.prjoectName
        formData.auditDate = formatDay(new Date())
        formData.auditUserName = JSON.parse(
          localStorage.getItem('userInfo')
        ).realname
        formData.auditUserId = JSON.parse(
          localStorage.getItem('userInfo')
        ).staffid
        formData.orgIdNames =
          newValue.isBmAudit == 1
            ? newValue.orgIdNames
            : newValue.auditStaffName
        formData.projectId = newValue.projectId
        formData.orgIds = newValue.orgIds
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        formData.editor = userInfo.realname
        formData.orgid = userInfo.staffid
        formData.certificateDate = formatDay(new Date())
        this.formData = formData
      },
    },
    created() {},
    mounted() {},
    methods: {
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.$nextTick(async () => {
            // for (let key in row) {
            //   this.formData[key] = row[key]
            // }

            this.formData = row
            // this.formData.evidenceOpinion = row.evidenceOpinion
            //   ? row.evidenceOpinion
            //   : ''
            this.formData.certificateDate = formatDay(row.createDate)
            this.formData.auditUserName = row.auditUserName
            this.formData.auditUserId = row.auditUserId
            this.getFileList(row.certificateId)

            // if (row.status == 2 || row.status == 3) {
            //   const res2 = await getFlowTaskInfo({
            //     tableId: 16,
            //     formId: row.certificateId,
            //   })
            //   this.jurisdictionCode = res2.data.isFlowInfo
            //   if (res2.data.isFlowInfo) {
            //     this.flowtaskinfoflowid = res2.data.flowId
            //     this.fromId = row.certificateId
            //     this.ymFromId = res2.data.id

            //     const res3 = await getFaqiInfo({
            //       id: res2.data.id,
            //       flowId: res2.data.flowId,
            //     })
            //     if (res3.code == 1) {
            //       this.status = res3.data.dataJson.flowTaskInfo.status
            //     }
            //   }
            // }
            this.formData.prjoectName = row.projectName
            this.formData.orgIdNames = row.orgIdNames
          })
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.editor = userInfo.realname
          this.formData.orgid = userInfo.staffid
          this.formData.certificateDate = formatDay(new Date())
        }
        if (title == 'edit') {
          this.title = '编辑'
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
        this.formData = {}
        this.tableData = []
        this.footer = true
        this.clearType = true
        this.$emit('fetch-data')
        this.dialogFormVisible = false
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = '',
              result = null
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const { certificateDate, createDate, ...other } = this.formData
            result = await createImPlementOrder({ ...other, attids })
            if (result.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.close()
            }
          } else {
            this.$baseMessage('保存失败', 'error')
          }
        })
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      assistecertificateUser() {
        this.$refs['manage4'].showEdit()
      },
      getChildlistPro4(val) {
        this.$set(this.formData, 'certificateUser', val[0].realname)
        this.$set(this.formData, 'certificateStaffId', val[0].staffid)
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
        this.$refs['manage3'].showEdit(
          [],
          this.formData.projectId,
          'assistedzbuser'
        )
      },
      // getChildlistPro3(val) {
      //   this.$set(this.formData, 'assistedzbuser', val[0].realname)
      //   this.$set(this.formData, 'assistedzbuserid', val[0].staffid)
      // },
      projectManager1() {
        // this.$refs['manage'].showEdit('firststaffid')
        this.$refs['manage3'].showEdit(
          [],
          this.formData.projectId,
          'firststaffid'
        )
      },
      projectManager2() {
        // this.$refs['manage'].showEdit('secondstaffid')
        this.$refs['manage3'].showEdit(
          [],
          this.formData.projectId,
          'secondstaffid'
        )
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

        // this.$set(this.formData, 'assistedzbuserid', info[0].staffid)
        // this.$set(this.formData, 'assistedzbuser', info[0].staff.realname)
      },
      //提交
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await ymWorkCandidates({
              flowId: this.flowtaskinfoflowid,
              fromId: this.fromId,
              flowTaskOperatorId: '',
              id: '',
            })
            this.candidateType = res.data.candidateType
            if (res.data.candidateType == 1) {
              this.fzoptions = res.data.list
              this.visible = true
              let list = []
              res.data.list.map((item) => {
                list.push({
                  value: item.nodeId,
                  label: item.nodeName,
                  hasCandidates: item.hasCandidates,
                })
              })
              this.options = list
              //保存请求人员列表的信息
              let candidateData = {
                // tableId: tableId,
                fromId: this.fromId,
              }
              this.candidateData = candidateData
            } else if (res.data.candidateType == 2) {
              let candidateData = {
                // tableId: tableId,
                fromId: this.fromId,
                nodeId: res.data.list[0].nodeId,
              }
              this.candidateData = candidateData
              this.visible2 = true
            } else {
              const wordres = await ymWorkSubmit({
                flowId: this.flowtaskinfoflowid,
                fromId: this.fromId,
                branchStrs: this.fzforms.branchStrs
                  ? this.fzforms.branchStrs.join(',')
                  : '',
                candidateType: res.data.candidateType,
                ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
                status: this.status,
              })
              if (wordres.code === 1) {
                this.$message.success(wordres.msg)
                this.close()
                this.visible = false
              }
            }
          }
        })
      },
      currentClose() {
        this.visible = false
        this.form = {}
      },
      handlefzChange(e) {
        this.$refs['fzform'].clearValidate()
      },
      handleCandSelect1(index, value) {
        this.formData2.transferStaffName = value
      },
      handleCandSelect(index, value) {
        // this.$set(this.formData3[index], 'transferStaffId', value)
        this.formData3[index].transferStaffId = value
      },
      async save4() {
        if (this.fzforms.branchStrs.length == 0) {
          this.$message.warning('请选择分支')
          return
        }
        let arr = []
        if (this.formData3.length > 0) {
          this.formData3.map((res) => {
            let str = []
            res.transferStaffId.map((item) => {
              str.push(item.id)
            })
            arr.push(str)
          })
        }

        let list = []
        arr.map((item) => {
          let str = item.join(',')
          if (str) {
            list.push(str)
          }
        })
        let candidateList = list.join('~')

        const { data, code } = await ymWorkSubmit({
          // tableId: this.tableId,
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          branchStrs: this.fzforms.branchStrs
            ? this.fzforms.branchStrs.join(',')
            : '',
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
          candidateType: this.candidateType,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close4()
          this.close()
        }
      },
      selectValue(e) {
        let arr = []
        this.fzoptions.forEach((res) => {
          if (res.nodeId == e.split('~')[0]) {
            arr.push(res)
          }
        })
        this.runderList = arr
        this.formData3 = arr.map(() => {
          return { transferStaffName: '', transferStaffId: '' }
        })
      },

      resetINfo() {
        this.flowId = ''
        this.fromId = ''
        this.runderList = []
        this.formData = {
          value: [],
        }
        this.formData2 = {
          transferStaffName: '',
          transferStaffId: '',
        }
      },
      async save1() {
        let branchStrs = ''
        this.fzform1.branchStrs.map((item) => {
          branchStrs = branchStrs + item + ','
        })
        branchStrs = branchStrs.substring(0, branchStrs.length - 1)
        let arr = []
        if (this.formData3.length > 0) {
          this.formData3 &&
            this.formData3.map((res) => {
              let str = []
              res.transferStaffId &&
                res.transferStaffId.map((item) => {
                  str.push(item.id)
                })
              arr.push(str)
            })
        }

        let list = []
        arr.map((item) => {
          let str = item.join(',')
          if (str) {
            list.push(str)
          }
        })
        let candidateList = list.join('~')

        const { data, code } = await ymWorkSubmit({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          branchStrs,
          candidateType: this.candidateType,
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close()
          this.visible1 = false
        }
      },
      close1() {
        this.visible1 = false
        this.resetINfo()
      },
      close2() {
        this.visible2 = false
        this.resetINfo()
      },
      async save2() {
        if (!this.formData2.transferStaffName) {
          this.$message.error('请选择候选人')
          return
        }
      s
        let list = []
        this.formData2.transferStaffName.map((item) => {
          list.push(item.id)
        })
        const { data, code } = await ymWorkSubmit({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          candidateList: list.join(','),
          nodeCode: this.candidateData.nodeId,
          candidateType: this.candidateType,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close2()
          this.close()
        }
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
