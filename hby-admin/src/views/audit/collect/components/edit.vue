<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="140px"
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
              :disabled="!footer && title === '详细'"
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
              :disabled="
                !formData.secrectLevelId || (!footer && title === '详细')
              "
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题来源" prop="issuesType">
            <el-select
              style="width: 100%"
              v-model="formData.issuesType"
              placeholder="选择问题来源"
              @change="typeChange"
              :disabled="!footer && title === '详细'"
            >
              <el-option label="审计" :value="1" />
              <el-option label="内控" :value="2" />
              <el-option label="非系统实施" :value="3" />
              <el-option label="外部审计" :value="4" />
              <el-option label="风险" :value="5" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务编号" prop="issuesCode">
            <el-input
              v-model="formData.issuesCode"
              clearable
              placeholder="请输入业务编号"
              :style="{
                width:
                  formData.issuesType == '1' ||
                  formData.issuesType == '2' ||
                  formData.issuesType == '5'
                    ? '78%'
                    : '100%',
              }"
              :disabled="!footer && formData.issuesType !== 1"
            />
            <el-button
              v-if="formData.issuesType == '1'"
              @click="handleShenji"
              style="margin-left: 10px"
              type="primary"
              size="mini"
              :disabled="!footer && title === '详细'"
            >
              选择
            </el-button>
            <el-button
              v-if="formData.issuesType == '2'"
              @click="handleNeikong"
              style="margin-left: 10px"
              type="primary"
              size="mini"
              :disabled="!footer && title === '详细'"
            >
              选择
            </el-button>
            <el-button
              v-if="formData.issuesType == '5'"
              @click="handleFengxian"
              style="margin-left: 10px"
              type="primary"
              size="mini"
              :disabled="!footer && title === '详细'"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="问题编号" prop="proCode">
            <el-input
              v-model="formData.proCode"
              clearable
              placeholder="请输入问题编号"
              :style="{ width: '100%' }"
              :disabled="!footer && title === '详细'"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="项目编号" prop="projectNo">
            <el-input
              v-model="formData.projectNo"
              clearable
              placeholder="请输入项目编号"
              :style="{
                width: formData.issuesType == '1' || formData.issuesType == '5' ? '78%' : '100%',
              }"
            />
            <el-button
              v-if="formData.issuesType == '1'"
              @click="handleFangan"
              style="margin-left: 10px"
              type="primary"
              size="mini"
              :disabled="!footer && title === '详细'"
            >
              选择
            </el-button>
            <el-button
              v-if="formData.issuesType == '5'"
              @click="handleFengxianProject"
              style="margin-left: 10px"
              type="primary"
              size="mini"
              :disabled="!footer && title === '详细'"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="问题名称" prop="issuesName">
            <el-input
              v-model="formData.issuesName"
              clearable
              placeholder="请输入问题名称"
              :style="{ width: '76%' }"
              :disabled="!footer && formData.issuesType !== 1"
            />
            <el-button
              v-if="formData.issuesType != '3'"
              @click="issuesDetail"
              style="margin-left: 10px"
              type="success"
              size="mini"
              :disabled="false"
            >
              详细
            </el-button>
          </el-form-item>
        </el-col>
        <el-col
          :span="12"
          v-if="
            formData.issuesType == '1' &&
            (formData.auditObjectType == '1' || formData.auditObjectType == '2')
          "
        >
          <el-form-item label="被审计/评价对象" prop="auditObjectId">
            <el-select
              style="width: 100%"
              v-model="formData.auditObjectId"
              placeholder="选择被审计/评价对象"
              :disabled="!footer && title === '详细'"
            >
              <el-option
                v-for="item in auditObjectOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-else-if="formData.issuesType == '1' && isFa">
          <el-form-item label="被审计/评价对象" prop="auditObjectId">
            <el-select
              style="width: 100%"
              v-model="formData.auditObjectId"
              placeholder="选择被审计/评价对象"
              :disabled="!footer && title === '详细'"
            >
              <el-option
                v-for="item in auditObjectOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-else>
          <el-form-item label="被审计/评价对象" prop="auditObjectName">
            <el-input
              v-model="formData.auditObjectName"
              clearable
              placeholder="请选择被审计/评价对象"
              style="width: 78%"
              disabled
            />
            <el-button
              @click="handleObject"
              style="margin-left: 10px"
              type="primary"
              size="mini"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="事项" prop="issuesItem">
            <el-input
              v-model="formData.issuesItem"
              clearable
              placeholder="请输入事项"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="经办人员" prop="createStaffName">
            <el-input
              v-model="formData.createStaffName"
              clearable
              placeholder="请输入经办人员"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任人" prop="responsiblePersonName">
            <el-input
              v-model="formData.responsiblePersonName"
              clearable
              placeholder="请选择责任人"
              style="width: 80%"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="selectPerson"
              :disabled="!footer && title === '详细'"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任部门" prop="responsibleDeptName">
            <el-input
              v-model="formData.responsibleDeptName"
              clearable
              placeholder="请选择责任部门"
              style="width: 80%"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="selectDept"
              :disabled="!footer && title === '详细'"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              style="width: 100%"
              v-model="formData.createTime"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              disabled
            />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="12">
          <el-form-item label="是否发现问题" prop="riskLevel">
            <el-select
              style="width: 100%"
              v-model="formData.riskLevel"
              placeholder="是否发现问题"
              @change="risklevelChange"
              :disabled="!footer"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col> -->

        <!-- v-if="formData.riskLevel === '是'" -->
        <el-col :span="24">
          <el-form-item label="问题标题" prop="issuesTitle">
            <el-input
              v-model="formData.issuesTitle"
              clearable
              placeholder="请输入问题标题"
              :style="{ width: '100%' }"
              :disabled="!footer && title === '详细'"
            />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="24">
          <el-form-item label="程序执行过程" prop="programProcess">
            <el-input
              v-model="formData.programProcess"
              clearable
              placeholder="请输入程序执行过程"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->

        <!-- v-if="formData.riskLevel === '是'" -->
        <el-col :span="24">
          <el-form-item label="问题详情" prop="questionMemo">
            <el-input
              v-model="formData.questionMemo"
              clearable
              placeholder="请输入问题详情"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="!footer && title === '详细'"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="意见及建议" prop="opinions">
            <el-input
              v-model="formData.opinions"
              clearable
              placeholder="请输入意见及建议"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="!footer && title === '详细'"
            />
          </el-form-item>
        </el-col> -->

        <template v-if="showHistory">
          <el-col :span="24">
            <el-divider>历史版本</el-divider>
          </el-col>
          <el-col>
            <el-table :data="formData.relaList">
              <el-table-column
                align="center"
                label="业务编号"
                prop="issuesCode"
                width="100"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleDetailContent(row)">
                    {{ row.issuesCode }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="问题名称"
                prop="issuesName"
              />
              <el-table-column
                align="center"
                label="问题标题"
                prop="issuesTitle"
                #default="{ row }"
              >
                <el-tooltip placement="top">
                  <div
                    slot="content"
                    style="max-width: 600px; white-space: pre-wrap"
                  >
                    {{ row.issuesTitle }}
                  </div>
                  <div class="showOverFlow">
                    {{ row.issuesTitle }}
                  </div>
                </el-tooltip>
              </el-table-column>
              <el-table-column
                align="center"
                label="拟稿人"
                prop="createStaffName"
              />
              <el-table-column
                align="center"
                label="整改方案"
                prop="rectificationPlan"
                #default="{ row }"
              >
                <el-tooltip placement="top">
                  <div
                    slot="content"
                    style="max-width: 600px; white-space: pre-wrap"
                  >
                    {{ row.rectificationPlan }}
                  </div>
                  <div class="showOverFlow">
                    {{ row.rectificationPlan }}
                  </div>
                </el-tooltip>
              </el-table-column>
              <el-table-column
                align="center"
                label="整改措施"
                prop="rectificationMeasures"
              />
              <el-table-column
                align="center"
                label="成果体现"
                prop="resultMemo"
              />
              <!-- <el-table-column align="center" label="拟稿日期" prop="createTime" :formatter="formatDate" /> -->
            </el-table>
          </el-col>
        </template>

        <el-col :span="24" style="margin-top: 20px">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-top: 5px"
            v-if="footer || title !== '详细'"
          >
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
                <!-- v-if="footer" -->
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="footer || title !== '详细'"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
      <!-- 复核人选择 -->
      <shenjiModal ref="shenji" @selectList="shenjiSelect" />
      <fanganModal ref="fangan" @selectList="fanganSelect" />
      <neikongModal ref="neikong" @selectList="neikongSelect" />
      <fengxianModal ref="fengxian" @selectList="fengxianSelect" />
      <Executor ref="Executor" @projectManage="selectedPerson" />
      <DepartmentOption ref="DepartmentOption" @submit="selectedDept" />
      <Company
        ref="companyTreeModel"
        @submit="selectCompany"
        :lable="'被审计/评价对象'"
        :searchAllCompany="true"
      />
    </el-row>
    <MyDraftInfo
      ref="MyDraftInfo"
      v-if="showMyDraftInfo"
      @fetch-data="showMyDraftInfo = false"
    />
    <issueView ref="issueView" />

    <!-- v-if="footer" -->
    <template #footer v-if="footer || title !== '详细'">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import { deleteFile, download } from '@/api/audit/implement'
  import { testtaskProfindDetail } from '@/api/internal/tack'
  import {
    saveIssues,
    getIssuesAllDetailInfo,
    getAuditedObjectList,
  } from '@/api/zgzz/index.js'
  import { currSsProject } from '@/api/audit/preparation'
  import { riskResult } from '@/api/systemLog'
  import store from '@/store'
  import shenjiModal from './shenjiModal.vue'
  import fanganModal from './fanganModal.vue'
  import neikongModal from './neikongModal.vue'
  import fengxianModal from './fengxianModal.vue'
  import DepartmentOption from '@/components/departmentSelect.vue'
  import CompanyTreeModel from '@/components/CompanyTreeModel/index.vue'
  import Company from '@/components/Company.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import Executor from '@/components/danxuanPerson.vue'
  import * as dayjs from 'dayjs'
  import { myDraftDetail } from '@/api/audit/implement'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'
  import issueView from '@/views/internal/internalTest/components/issueView.vue'

  const { baseURL } = require('@/config')
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'

  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'zgqdEdit',
    components: {
      shenjiModal,
      fanganModal,
      DepartmentOption,
      CompanyTreeModel,
      Company,
      neikongModal,
      fengxianModal,
      Executor,
      MyDraftInfo,
      issueView,
      ZXPerson,
    },
    inheritAttrs: false,
    props: ['showHistory'],
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        dialogFormVisible: false,
        formData: {
          attIds: null, // 当前页面新保存的附件主键数组
          attList: [], // 附件列表
          relaList: [], // 历史版本
          auditObjectName: '', // 被审计对象名称
          auditObjectId: '', // 被审计对象主键
          auditObjectType: '', // 被审计对象类型
          createStaffName: '', // 创建人
          createTime: '', // 创建时间
          historyStatus: '', // 历史状态用于还原
          issuesCode: '', // 业务编号
          issuesId: '', // 整改清单主键
          issuesItem: '', // 事项
          issuesName: '', // 问题名称
          issuesParent: '', // 变更前的主键
          issuesTitle: '', // 问题标题
          issuesType: '', // 问题来源
          issuesVersion: '', // 历史版本
          linkDeptId: '', // 所属部门
          linkOrgId: '', // 所属公司
          opinions: '', // 审计意见及建议
          programProcess: '', // 审计执行过程
          projectId: '', // 关联项目主键
          projectName: '', // 项目名称
          projectNo: '', // 项目编号
          // proCode: '', // 问题编号
          quesitionId: '', // 审计内控关联表单外键
          questionMemo: '', // 问题详情
          mainorg: '',
          responsibleDept: '', // 责任部门
          responsibleDeptName: '', // 责任部门
          responsiblePerson: '', // 责任人
          responsiblePersonName: '', // 责任人
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },
        footer: true,
        tableData: [],
        rules: {
          issuesType: [
            {
              required: true,
              message: '请选择问题来源',
              trigger: 'blur',
            },
          ],
          issuesCode: [
            {
              required: true,
              message: '请输入业务编号',
              trigger: 'blur',
            },
          ],
          issuesName: [
            {
              required: true,
              message: '请输入问题名称',
              trigger: 'blur',
            },
          ],
          projectNo: [
            {
              required: true,
              message: '请输入项目编号',
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
          auditObjectName: [
            {
              required: true,
              message: '请输入被审计/评价对象',
              trigger: 'blur',
            },
          ],
          auditObjectId: [
            {
              required: true,
              message: '请输入被审计/评价对象',
              trigger: 'blur',
            },
          ],
          responsibleDeptName: [
            {
              required: true,
              message: '请选择责任部门',
              trigger: 'change',
            },
          ],
          // createStaffName: [
          //   {
          //     required: true,
          //     message: '请输入人员',
          //     trigger: 'blur',
          //   },
          // ],
          riskLevel: [
            {
              required: true,
              message: '是否发现问题',
              trigger: 'blur',
            },
          ],
          programProcess: [
            {
              required: true,
              message: '请输入程序执行过程',
              trigger: 'blur',
            },
          ],
          questionMemo: [
            {
              required: true,
              message: '请输入问题详情',
              trigger: 'blur',
            },
          ],
        },
        title: '新增',
        reviewType: '',
        reportData: [],
        currentProject: {},
        quesition: {},
        showMyDraftInfo: false,
        auditObjectOptions: [],
        tempAuditObjectId: '',
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
        isFa: false,
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
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('Zgqd')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      changeMJ(selectedValue) {
        // selectedValue 就是选中的 value（即 levelId）
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
          }
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      // 获取当前实施项目
      async getCurrentProject() {
        let obj = {}
        await currSsProject().then((res) => {
          if (res.code === 1) {
            obj = res.data.pj
          } else {
            obj = undefined
          }
        })

        return obj
      },
      addTable() {
        const obj = {
          reportConcent: '',
          sjdeptNames: '',
          sjdeptIds: '',
        }
        this.reportData.push(obj)
      },
      projectManager1() {
        this.$refs['manage'].showEdit('firststaffid')
      },
      projectManager2() {
        this.$refs['manage'].showEdit('secondstaffid')
      },
      risklevelChange(index) {
        this.formData.riskLevel = index
        if (index == '是') {
          this.rules.questionMemo = [
            {
              required: true,
              message: '请输入问题详情',
              trigger: 'blur',
            },
          ]
        } else {
          this.rules.questionMemo = []
        }
        this.$forceUpdate()
      },
      async showEdit(title, row) {
        this.dialogFormVisible = true
        this.formData.createStaffName = JSON.parse(
          localStorage.getItem('userInfo')
        ).realname
        this.formData.createTime = new Date()
        if (row) {
          if (row.auditObjectType == '1' || row.auditObjectType == '2') {
            this.tempAuditObjectId = row.auditObjectId
            await getAuditedObjectList({
              sheetId: row.quesitionId,
              auditOrgId: row.auditObjectId,
            }).then((res) => {
              const data = res.data || []
              this.auditObjectOptions = data.map((x) => {
                return {
                  label: x.orgname,
                  value: x.orgid,
                }
              })
            })
          }
          Object.assign(this.formData, row)
          this.formData.relaList = this.formData.relaList.map((x) => {
            const { issues, ...other } = x
            return {
              ...issues,
              ...other,
            }
          })
          // 附件容器换成tableData
          this.tableData = JSON.parse(JSON.stringify(this.formData.attList))
          // this.typeChange(row.issuesType)
          if (row.issuesType == '3' || row.issuesType == '4') {
            this.footer = true
          } else {
            this.footer = false
          }
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        }
        this.$forceUpdate()
      },
      close() {
        this.dialogFormVisible = false
        this.$emit('closeDialog')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            if (this.reportData && this.reportData.length) {
              this.formData.srJson = JSON.stringify(this.reportData)
            }

            let obj = {
              ...this.formData,
            }

            if (this.tableData && this.tableData.length) {
              const attidArr = []
              this.tableData.map((item) => {
                if (this.formData.attList.some((x) => x.attid === item.attid)) {
                } else attidArr.push(item.attid)
              })
              const attIds = attidArr.join(',')
              obj.attIds = attIds
            } else {
              delete obj.attIds
            }

            delete obj.relaList
            delete obj.createStaffName
            delete obj.createTime
            delete obj.updateTime
            delete obj.attList
            // const { createTime, state, ...other } = this.formData
            // console.log('params', {
            //   ...obj,
            // })
            const data = await saveIssues({
              ...obj,
            })

            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetch-data')
              this.close()
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },

      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
        this.$message.success('删除成功')
      },

      handleObject() {
        this.$refs['companyTreeModel'].showEdit()
      },
      handleShenji() {
        this.$refs['shenji'].showEdit()
      },
      handleFangan() {
        this.$refs['fangan'].showEdit()
      },
      handleNeikong() {
        this.$refs['neikong'].showEdit()
      },
      handleFengxian() {
        this.$refs['fengxian'].showEdit()
      },
      handleFengxianProject() {
        this.$refs['fengxian'].showEdit()
      },
      selectCompany(val, type) {
        this.formData.auditObjectType = type === 'left' ? 1 : 3
        let orgidnames = ''
        let auditObjectId = ''
        if (type === 'left') {
          orgidnames = val.label
          auditObjectId = val.id
        }
        if (type === 'right') {
          orgidnames = val[0].realname
          auditObjectId = val[0].staffid
        }
        this.$set(this.formData, 'auditObjectName', orgidnames)
        this.$set(this.formData, 'auditObjectId', auditObjectId)
      },
      typeChange(e) {
        if (e == '3' || e == '4') {
          this.footer = true
        } else {
          this.footer = false
        }
        // 切换问题来源清空数据
        this.formData.auditObjectName = '' // 被审计对象名称
        this.formData.auditObjectId = '' // 被审计对象主键
        this.formData.auditObjectType = '' // 被审计对象类型
        this.formData.historyStatus = '' // 历史状态用于还原
        this.formData.issuesCode = '' // 业务编号
        this.formData.issuesItem = '' // 事项
        this.formData.issuesName = '' // 问题名称
        this.formData.issuesTitle = '' // 问题标题
        this.formData.opinions = '' // 审计意见及建议
        this.formData.programProcess = '' // 审计执行过程
        this.formData.projectId = '' // 关联项目主键
        this.formData.projectName = '' // 项目名称
        this.formData.projectNo = '' // 项目编号
        this.formData.questionMemo = '' // 问题详情
        this.formData.quesitionId = '' // 问题详情
        this.auditObjectOptions = [] // 审计-选择被审计/评价人员
      },
      async shenjiSelect(val) {
        this.isFa = false
        if (val[0].auditObjectType == '1' || val[0].auditObjectType == '2') {
          this.formData.auditObjectName = '' //被审计对象
          this.formData.auditObjectId = '' //被审计对象id
          await getAuditedObjectList({
            sheetId: val[0].sheetId,
            auditOrgId: this.tempAuditObjectId || undefined,
          }).then((res) => {
            const data = res.data || []
            this.auditObjectOptions = data.map((x) => {
              return {
                label: x.orgname,
                value: x.orgid,
              }
            })
          })
        } else {
          this.formData.auditObjectName = val[0].orgIdNames //被审计对象
          this.formData.auditObjectId = val[0].orgIds //被审计对象id
        }
        this.formData.auditObjectType = val[0].auditObjectType // 被审计对象类型
        this.formData.issuesCode = val[0].sheetCode //业务编号
        this.formData.projectNo = val[0].projectCode //项目编号
        this.formData.projectName = val[0].projectName //项目名称
        this.formData.issuesName = val[0].sheetName //问题名称
        this.formData.issuesItem = val[0].businessAffiliation //事项
        this.formData.createStaffName = val[0].realname //人员
        this.formData.issuesTitle = val[0].quesTitle //问题标题
        this.formData.programProcess = val[0].auditDesc //程序执行过程
        this.formData.questionMemo = val[0].auditDiscoverable //问题详情
        this.formData.opinions = val[0].auditCourse //问题详情
        this.formData.projectId = val[0].projectId //id
        this.formData.createTime = new Date()
        this.formData.quesitionId = val[0].sheetId
        this.quesition = val[0]
      },
      async fanganSelect(val) {
        console.log(val)
        this.isFa = true
        this.formData.projectNo = val[0].projectCode //项目编号
        this.formData.projectName = val[0].prjoectName //项目名称
        this.formData.projectId = val[0].projectId //id
        await getAuditedObjectList({
          sheetId: val[0].sheetId || undefined,
          projectId: val[0].projectId || undefined,
          auditOrgId: this.tempAuditObjectId || undefined,
        }).then((res) => {
          const data = res.data || []
          this.auditObjectOptions = data.map((x) => {
            return {
              label: x.orgname,
              value: x.orgid,
            }
          })
        })
      },
      neikongSelect(val) {
        this.isFa = false
        this.formData.auditObjectType = val[0].auditObjectType // 被审计对象类型
        this.formData.projectNo = val[0].plannumber //项目编号
        this.formData.projectName = val[0].planname //项目名称
        this.formData.issuesCode = val[0].risknumber
        this.formData.issuesName = val[0].problemtype
        this.formData.auditObjectName = val[0].orgname
        this.formData.auditObjectId = val[0].mainorg
        this.formData.issuesItem = val[0].oneprocess
        this.formData.programProcess = val[0].defectmemo
        this.formData.issuesTitle = val[0].problemmemo
        this.formData.questionMemo = val[0].defectmemo
        this.formData.createStaffName = val[0].realname //人员
        this.formData.projectId = val[0].planid //id
        this.formData.mainorg = val[0].mainorg
        this.formData.createTime = new Date()
        this.formData.quesitionId = val[0].findid
        this.quesition = val[0]
      },
      // 风险选择回调
      async fengxianSelect(val) {
        this.isFa = false
        // 从风险台账中获取数据
        this.formData.issuesCode = val[0].risknumber || '' //业务编号（风险编号）
        this.formData.issuesName = val[0].riskname || '' //问题名称（风险名称）
        this.formData.auditObjectName = val[0].zrbmName || '' //被审计对象（牵头责任部门）
        this.formData.auditObjectId = val[0].belongsto || '' //被审计对象id
        this.formData.auditObjectType = '3' // 被审计对象类型
        this.formData.issuesTitle = val[0].riskname || '' //问题标题
        this.formData.questionMemo = `风险类型: ${val[0].riskcatname || ''}` //问题详情
        this.formData.createStaffName = JSON.parse(
          localStorage.getItem('userInfo')
        ).realname //人员
        this.formData.quesitionId = val[0].riskid || '' //问题id
        this.quesition = val[0]

        // 根据风险ID获取评估计划信息
        if (val[0].riskid) {
          try {
            const { data } = await riskResult({ riskid: val[0].riskid })
            // 接口返回结构: { code: 1, data: { riskid: xxx, assPlanList: [...] } }
            if (data && data.assPlanList && data.assPlanList.length > 0) {
              const plan = data.assPlanList[0].assplan || data.assPlanList[0]
              // 取第一条评估计划的编号和名称
              this.formData.projectNo = plan.plancode || '' //项目编号（评估计划编号）
              this.formData.projectName = plan.planName || '' //项目名称（评估计划名称）
              this.formData.projectId = plan.assplanid || '' //项目id
              console.log('评估计划信息:', { plancode: plan.plancode, planName: plan.planName })
            } else {
              console.warn('未找到评估计划信息')
            }
          } catch (error) {
            console.error('获取评估计划信息失败:', error)
          }
        }

        this.formData.createTime = new Date()
      },
      selectPerson() {
        this.$refs['Executor'].showEdit()
      },
      selectedPerson(val) {
        this.formData.responsiblePerson = val[0].staffid
        this.formData.responsiblePersonName = val[0].realname
      },
      selectDept() {
        this.$refs['DepartmentOption'].showEdit()
      },
      selectedDept(e) {
        this.formData.responsibleDept = e.id
        this.formData.responsibleDeptName = e.label
      },
      async handleDetailContent(row) {
        const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
        this.$showAllDataDetailDialog(res.data)
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return dayjs(data).format('YYYY-MM-DD')
      },
      async issuesDetail() {
        if (!this.formData.quesitionId)
          return this.$message.error('未选择关联问题！')
        // 审计
        if (this.formData.issuesType == '1') {
          const data = await myDraftDetail({
            sheetid: this.formData.quesitionId,
          })
          this.showMyDraftInfo = true
          this.$nextTick(() => {
            this.$refs.MyDraftInfo.showEdit('detail', data.data, null, 'Zgqd')
          })
        }

        // 内控
        if (this.formData.issuesType == '2') {
          const data = await testtaskProfindDetail({
            findid: this.formData.quesitionId,
          })
          this.$refs.issueView.show(data.data.profind, 'detail')
        }

        // 风险
        if (this.formData.issuesType == '5') {
          this.$message.info('风险评估详情暂不支持查看')
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
          // 更新文件列表
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
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
  .showOverFlow {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.5em;
    max-height: 3em;
    white-space: pre-wrap;
  }
</style>
