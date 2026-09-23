<template>
  <div>
    <el-row :gutter="15">
      <el-form
        ref="elForm"
        :disabled="disabled"
        label-width="125px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="所属审计计划" prop="planname">
            <el-select
              v-model="formData.planname"
              placeholder="请选择"
              style="width: 266px"
              @change="handleChange"
              :disabled="true"
            >
              <el-option
                v-for="item in auditPlanArr"
                :key="item.value"
                :label="item.planname"
                :value="item.planid"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="计划项目" prop="pprojectName">
            <el-input
              v-model="formData.pprojectName"
              clearable
              style="width: 266px"
              placeholder="请选择计划名称"
              disabled
            />
            <el-button
              @click="handleChoicePlan"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="项目编号" prop="projectCode">
            <el-input
              v-model="formData.projectCode"
              clearable
              placeholder="请输入项目编号"
              style="width: 266px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="prjoectName">
            <el-input
              v-model="formData.prjoectName"
              clearable
              placeholder="请输入项目名称"
              style="width: 266px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类别" prop="projecttype">
            <el-select
              v-model="formData.projecttype"
              placeholder="请选择项目类别"
              style="width: 266px"
            >
              <el-option label="计划内" value="计划内"></el-option>
              <el-option label="计划外" value="计划外"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计对象" prop="orgName">
            <el-input
              v-model="formData.orgName"
              clearable
              placeholder="请选择被审计对象"
              style="width: 266px"
              disabled
            />
            <el-button
              @click="handleObject"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划年度" prop="field133">
            <el-date-picker
              v-model="formData.planYear"
              type="year"
              value-format="yyyy"
              placeholder="请选择计划年度"
              style="width: 266px"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计类型" prop="auditType">
            <el-select
              v-model="formData.auditType"
              placeholder="请选择"
              style="width: 266px"
            >
              <el-option
                v-for="item in auditTypeArr"
                :key="item.typeId"
                :label="item.auditType"
                :value="item.typeId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="项目来源" prop="projectSource">
            <el-select
              :disabled="disabled"
              v-model="formData.projectSource"
              placeholder="请选择项目来源"
              style="width: 266px"
            >
              <el-option label="年度审计计划" value="年度审计计划"></el-option>
              <el-option label="临时指派" value="临时指派"></el-option>
            </el-select>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="项目负责人" prop="realName">
            <el-input
              v-model="formData.realName"
              clearable
              placeholder="请选择项目负责人"
              style="width: 266px"
              disabled
            />
            <el-button
              @click="projectManager"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="项目计划时间" prop="dateSection">
            <el-date-picker
              v-model="formData.dateSection"
              clearable
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              style="width: 266px"
              type="daterange"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计方式" prop="proSjfs">
            <el-select
              v-model="formData.proSjfs"
              placeholder="请选择审计方式"
              style="width: 266px"
            >
              <el-option
                v-for="item in auditMethodArr"
                :key="item.key"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="统计类型" prop="cntType">
            <el-select
              :disabled="disabled"
              v-model="formData.cntType"
              placeholder="请选择统计类型"
              style="width: 266px"
            >
              <el-option
                v-for="item in questionTypeList"
                :key="item.typeId"
                :label="item.auditType"
                :value="item.typeId"
              >
                {{ item.auditType }}
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-table
            border
            :data="planTableData"
            fit
            ref="multipleTable"
            tooltip-effect="dark"
            @select="handleSelection"
            highlight-current-row
            style="width: 100%; margin-bottom: 25px"
          >
            <el-table-column
              type="selection"
              width="55"
              v-if="!disabled"
            ></el-table-column>
            <el-table-column
              align="center"
              label="计划编号"
              prop="plancode"
            ></el-table-column>

            <el-table-column
              align="center"
              label="计划名称"
              prop="planname"
            ></el-table-column>

            <el-table-column
              align="center"
              label="计划时间"
              prop="starttime"
            ></el-table-column>
            <el-table-column
              align="center"
              label="计划类别"
              prop="plantype"
            ></el-table-column>
            <el-table-column
              align="center"
              label="计划年度"
              prop="palnyear"
            ></el-table-column>
          </el-table>
        </el-col> -->
        <!-- <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="step1add" :disabled="disabled">
              保存
            </el-button>
          </div>
        </el-col> -->

        <!-- <el-col :span="24">
          <el-divider></el-divider>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="项目费用估算(元)" prop="field108">
            <el-input
              v-model="formData.costs"
              clearable
              placeholder="请输入项目费用估算"
              style="width: 266px"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="是否外委" prop="externAlassig">
            <el-select
              v-model="formData.externAlassig"
              placeholder="请选择"
              style="width: 266px"
            >
              <el-option
                v-for="item in externAlassigArr"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计模板" prop="templeteName">
            <el-input
              v-model="formData.templeteName"
              clearable
              placeholder="请选择审计模板"
              style="width: 266px"
              disabled
            />
            <el-button
              @click="auditTemplate(0)"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计指引" prop="tbltempletezy">
            <el-input
              v-model="formData.tbltempletezy"
              clearable
              placeholder="请选择审计指引"
              disabled
              style="width: 266px"
            />
            <el-button
              @click="auditGuidelines(1)"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施主体" prop="implementaion">
            <!-- <el-input
                v-model="formData.implementaion"
                clearable
                :disabled="disabled"
                placeholder="请选择实施主体"
                style="width: 266px"
              /> -->
            <el-select
              :disabled="disabled"
              v-model="formData.implementaion"
              placeholder="请选择实施主体"
              style="width: 266px"
            >
              <el-option label="自主实施" value="自主实施"></el-option>
              <el-option label="委外实施" value="委外实施"></el-option>
              <el-option
                label="联合外部机构实施"
                value="联合外部机构实施"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="协办部门">
            <el-input
              v-model="formData.cospomsordepartmentName"
              clearable
              placeholder="请输入报告部门"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
              :disabled="disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计要求" prop="auditrequirements">
            <el-input
              :disabled="disabled"
              v-model="formData.auditrequirements"
              clearable
              placeholder="请选择审计要求"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="具体实施步骤" prop="implementaionsteps">
            <el-input
              :disabled="disabled"
              v-model="formData.implementaionsteps"
              clearable
              placeholder="请选择实施步骤"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计目标和范围" prop="field132">
            <el-input
              type="textarea"
              v-model="formData.purpose"
              clearable
              placeholder="请输入审计目标和范围"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计内容和重点" prop="field132">
            <el-input
              type="textarea"
              v-model="formData.scope"
              clearable
              placeholder="请输入审计内容和重点"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计程序和方法" prop="field132">
            <el-input
              type="textarea"
              v-model="formData.pursuant"
              clearable
              placeholder="请输入审计程序和方法"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="对专家和外部审计结果的利用" prop="field132">
            <el-input
              type="textarea"
              v-model="formData.comment"
              clearable
              placeholder="请输入对专家和外部审计结果的利用"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="其他有关内容" prop="field132">
            <el-input
              type="textarea"
              v-model="formData.proDesc"
              clearable
              placeholder="请输入其他有关内容"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>项目小组</el-divider>
        </el-col>
        <el-col :span="24">
          <div v-if="!disabled" style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="handleAdd">增加一行</el-button>
          </div>
          <!-- 新增可编辑表格 -->
          <el-table
            border
            :data="tableData"
            fit
            highlight-current-row
            style="width: 100%; margin-bottom: 25px"
          >
            <el-table-column align="center" label="小组名称" prop="teamName">
              <template slot-scope="scope">
                <el-input
                  v-show="scope.row.show"
                  v-model="scope.row.teamName"
                  size="mini"
                  style="width: 90%"
                />
                <span v-show="!scope.row.show">
                  {{ scope.row.teamName }}
                </span>
              </template>
            </el-table-column>

            <el-table-column align="center" label="组长" prop="leaderName">
              <template slot-scope="scope">
                <el-input
                  disabled
                  v-model="scope.row.leaderName"
                  size="mini"
                  style="width: 70%"
                />
                <el-button
                  :disabled="disabled"
                  type="primary"
                  size="mini"
                  style="margin-left: 3px"
                  @click="showGroupLeader(scope.$index)"
                >
                  选择
                </el-button>
              </template>
            </el-table-column>

            <el-table-column align="center" label="组员" prop="zyNames">
              <template slot-scope="scope">
                <el-input
                  disabled
                  v-model="scope.row.zyNames"
                  size="mini"
                  style="width: 70%"
                />
                <el-button
                  :disabled="disabled"
                  type="primary"
                  size="mini"
                  style="margin-left: 3px"
                  @click="showTeamMembers(scope.$index)"
                >
                  选择
                </el-button>
              </template>
            </el-table-column>

            <el-table-column
              v-if="!disabled"
              align="center"
              label="操作"
              min-width="80"
            >
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleDelete(scope.row, scope.$index)"
                >
                  删除
                </el-button>

                <!-- <el-button type="text" @click="scope.row.show = true">
                  编辑
                </el-button>

                <el-button
                  type="text"
                  @click="saveProject(scope.row, scope.$index)"
                >
                  保存
                </el-button> -->
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
      <el-col :span="24">
        <el-divider>文件上传</el-divider>
      </el-col>
      <el-col :span="24">
        <el-upload
          style="text-align: right; margin-bottom: 5px"
          class="upload-demo"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
          :show-file-list="false"
          multiple
          :file-list="fileList"
          :before-upload="handleBeforeUpload"
        >
          <div v-if="!disabled" style="margin-right: 10px">
            <el-button type="success">点击上传</el-button>
          </div>
        </el-upload>
        <el-table :data="tableDataFile">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template slot-scope="scope">
              <el-button type="text" @click="handleDown(scope.row)">
                下载
              </el-button>
              <el-button
                v-if="!disabled"
                type="text"
                @click="handleDeleteFile(scope.row, scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col style="position: relative">
        <el-button
          v-if="!disabled"
          @click="submitForm"
          type="primary"
          style="position: absolute; top: 126px; left: 40px; z-index: 10000"
        >
          保存
        </el-button>
      </el-col>
    </el-row>

    <!-- 计划项目子组件 -->
    <planned-project
      ref="planned"
      @plannedList="getChildlist"
    ></planned-project>
    <!-- 项目对象子组件 -->
    <project-object @objList="getChildlistObj" ref="object"></project-object>
    <!-- 项目经理子组件 -->
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <!-- 审计模板子组件 -->
    <audit-template
      @templateList="getChildlistTem"
      ref="template"
    ></audit-template>
    <!-- 审计指引子组件 -->
    <audit-guidelines
      @guidelinesList="getChildlistGui"
      ref="guidelines"
    ></audit-guidelines>
    <!-- 选择组长组员子组件 -->
    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>

    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
  </div>
</template>

<script>
  import { download } from '@/oapi/audit/implement'
  import {
    deleteProjectFile,
    getNbsjAuditPlanDateInfo,
    getNbsjAuditPlanListForMerge,
    getNbsjTypeListForMerge,
    getPlanProjectListByPlanId,
    getProjectDetail,
    getProjectPjteamList,
    getQuestionTypeList,
    projectAdd,
    projectFileList,
    proPjteamDel,
    seachTeamplateCnt,
  } from '@/oapi/audit/project'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import Auditee from '../../plan/components/childCom/Auditee.vue'
  import auditGuidelines from './formComponents/auditGuidelines.vue'
  import auditTemplate from './formComponents/auditTemplate.vue'
  import plannedProject from './formComponents/plannedProject.vue'
  import projectManage from './formComponents/projectManage.vue'
  import projectObject from './formComponents/projectObject.vue'
  import selectTeam from './formComponents/selectTeam.vue'

  import store from '@/store'
  const { baseURL } = require('@/config')

  export default {
    name: 'MaintainEdit',
    components: {
      plannedProject,
      projectObject,
      projectManage,
      auditTemplate,
      auditGuidelines,
      selectTeam,
      Auditee,
      DepartmentOptions,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        disabled: false,
        plannameFlag: false,
        tempId: 0,
        pdDx: '',
        proTemPid: 0,
        leaderId: '',
        zyStaffids: '',
        staffId: '',
        fileArrStr: '',
        tempZyId: 0,
        zcsId: 0,
        projectId: 0,
        handleAddFlag: false,
        isBmAuditFlag: false,
        formData: {
          planname: '',
          pprojectName: '',
          projectCode: '',
          prjoectName: '',
          targetName: '',
          realName: '', //需要接口重置
          orgName: '',
          planYear: '',
          auditType: '',
          projectSource: '',
          dateSection: [],
          templeteName: '', //需要接口重置
          costs: '',
          tbltempletezy: '', //需要接口重置
          proSjfs: '',
          externAlassig: '',
          purpose: '',
          scope: '',
          pursuant: '',
          comment: '',
          proDesc: '',
          cntType: '',
        },
        planId: '',
        planTableData: [],
        createPerson: '',
        fileList: [],
        auditPlanArr: [],
        auditTypeArr: [],
        externAlassigArr: [
          { value: 1, label: '是', key: 1 },
          { value: 0, label: '否', key: 2 },
        ],
        auditMethodArr: [
          {
            label: '现场',
            value: '现场',
            key: 0,
          },
          {
            label: '非现场',
            value: '非现场',
            key: 1,
          },
          {
            label: '非现场与现场结合',
            value: '非现场与现场结合',
            key: 2,
          },
        ],
        rules: {
          planname: [
            {
              message: '请选择所属审计计划',
              required: true,
              trigger: 'change',
            },
          ],
          projectCode: [
            {
              required: true,
              message: '请输入项目编号',
              trigger: 'change',
            },
          ],
          prjoectName: [
            {
              required: true,
              message: '请输入项目名称',
              trigger: 'change',
            },
          ],
          targetName: [
            {
              required: true,
              message: '请输入工作目标',
              trigger: 'change',
            },
          ],
          orgName: [
            {
              required: true,
              message: '请选择项目对象',
              trigger: 'change',
            },
          ],
          auditType: [
            {
              required: true,
              message: '请选择审计类型',
              trigger: 'change',
            },
          ],
          projectSource: [
            {
              required: true,
              message: '请输入项目来源',
              trigger: 'change',
            },
          ],
          realName: [
            {
              required: true,
              message: '请选择选择项目经理',
              trigger: 'change',
            },
          ],
          dateSection: [
            {
              required: true,
              message: '请选择项目计划时间',
              trigger: 'change',
            },
          ],
          templeteName: [
            {
              required: true,
              message: '请选择审计模板',
              trigger: 'change',
            },
          ],
          tbltempletezy: [
            {
              required: true,
              message: '请选择审计指引',
              trigger: 'change',
            },
          ],
          proSjfs: [
            {
              required: true,
              message: '请选择审计方式',
              trigger: 'change',
            },
          ],
          externAlassig: [
            {
              required: true,
              message: '请选择是否外委',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        tableData: [],
        tableDataFile: [],
        sIndex: 0,
        resPlanProjectArr: [],
        current: [],
        questionTypeList: [], //统计类型数据
      }
    },
    methods: {
      handleDepartmentSelected(node) {
        //保存名称
        this.$set(this.formData, `cospomsordepartmentName`, node.name)
        //保存名称对应的ID
        this.$set(this.formData, `cospomsordepartment`, node.id)
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showDetail(row, type, isEdit) {
        this.isEdit = isEdit
        //没有驳回时候，正常不可编辑
        if (!this.isEdit) {
          row.projectId = this.getQueryVariable(row.cyurl, 'spid')
          this.$nextTick(() => {
            this.showEdit(row, true, 0, false)
          })
        } else {
          //驳回时候，可编辑
          row.projectId = this.getQueryVariable(row.cyurl, 'spid')
          this.$nextTick(() => {
            this.showEdit(row, false, 0, false)
          })
        }
      },
      getQueryVariable(url, variable) {
        var query = url.substring(1)
        var vars = query.split('?')
        for (var i = 0; i < vars.length; i++) {
          var pair = vars[i].split('=')
          if (pair[0] == variable) {
            return pair[1]
          }
        }
        return false
      },
      async showEdit(row, disabled, planNum) {
        //获取统计类型下拉数据
        getQuestionTypeList().then((res) => {
          this.questionTypeList = res.data.pageInfo.tlist
        })
        if (!row) {
          this.title = '添加'
          let planNumRes =
            planNum.split('-')[0] + '-' + (parseInt(planNum.split('-')[1]) + 1)
          this.disabled = false
          this.tableData = []
          this.formData = {
            projectCode: planNumRes,
            auditType: '专项审计',
          }
          //获取所属审计计划集合
          let res = await getNbsjAuditPlanListForMerge()
          this.auditPlanArr = res.data
          //获取审计类型集合
          let resType = await getNbsjTypeListForMerge()
          this.auditTypeArr = resType.data.typeList
        } else {
          if (!disabled) {
            this.title = '编辑'
            this.disabled = false
            this.projectId = row.projectId
            let res = await getProjectDetail({
              projectid: row.projectId,
            })
            this.planId = res.data.pj.planId
            let resTeam = await getProjectPjteamList({
              projectid: row.projectId,
            })
            this.tableData = resTeam.data.listTeam
            //获取所属审计计划集合
            let resPlan = await getNbsjAuditPlanListForMerge()
            this.auditPlanArr = resPlan.data
            this.tempId = res.data.pj.tbltemplete.templeteId
            this.tempZyId = res.data.pj.tbltempletezy.templeteId
            this.formData = {
              // planname: res.data.pj.tblnbsjPlan.planname,
              planname: res.data.pj.tblnbsjPlan
                ? res.data.pj.tblnbsjPlan.planid
                : '',
              pprojectName: res.data.pj.pprojectName,
              projectCode: res.data.pj.projectCode,
              prjoectName: res.data.pj.prjoectName,
              targetName: res.data.pj.targetName,
              realName: res.data.pj.pmStaff.realname, //需要接口重置
              // orgName: res.data.pj.pmStaff.realname,
              planYear: res.data.pj.planYear,
              auditType: res.data.pj.auditType,
              projectSource: res.data.pj.projectSource,
              dateSection: [res.data.pj.startDate, res.data.pj.endDate],
              templeteName: res.data.pj.tbltemplete
                ? res.data.pj.tbltemplete.templeteName
                : '', //需要接口重置
              costs: res.data.pj.costs,
              tbltempletezy: res.data.pj.tbltempletezy
                ? res.data.pj.tbltempletezy.templeteName
                : '', //需要接口重置
              proSjfs: res.data.pj.proSjfs,
              externAlassig: res.data.pj.externAlassig,
              purpose: res.data.pj.purpose,
              scope: res.data.pj.scope,
              pursuant: res.data.pj.pursuant,
              comment: res.data.pj.comment,
              proDesc: res.data.pj.proDesc,
              implementaion: res.data.pj.implementaion,
              cospomsordepartment: res.data.pj.cospomsordepartment,
              cospomsordepartmentName: res.data.pj.cosdepartemnt
                ? res.data.pj.cosdepartemnt.orgname
                : '',
              projecttype: res.data.pj.projecttype,
              implementaionsteps: res.data.pj.implementaionsteps,
              auditrequirements: res.data.pj.auditrequirements,
              cntType: +res.data.pj.cntType,
            }
            this.tableData.forEach((item) => {
              item.show = true
            })
            this.staffId = res.data.pj.pmId
            if (res.data.pj.isBmAudit === 0) {
              this.pdDx = 'yh'
              this.formData.auditStaffId = res.data.pj.auditStaffId
              this.formData.orgName = res.data.pj.auditStaffName
            } else {
              this.pdDx = 'bm'
              this.formData.auditOrgId = res.data.pj.auditOrgId
              this.formData.orgName = res.data.pj.auditOrgName
            }
          } else {
            this.title = '查看'
            this.disabled = true
            this.projectId = row.projectId
            let resFile = await projectFileList({
              projectId: row.projectId,
            })
            this.tableDataFile = resFile.data.data || []
            let res = await getProjectDetail({
              projectid: row.projectId,
            })
            let resTeam = await getProjectPjteamList({
              projectid: row.projectId,
            })
            this.tableData = resTeam.data.listTeam
            this.formData = {
              planname: res.data.pj.tblnbsjPlan
                ? res.data.pj.tblnbsjPlan.planname
                : '',
              pprojectName: res.data.pj.pprojectName,
              projectCode: res.data.pj.projectCode,
              prjoectName: res.data.pj.prjoectName,
              targetName: res.data.pj.targetName,
              realName: res.data.pj.pmStaff.realname, //需要接口重置
              // orgName: res.data.pj.pmStaff.realname,
              planYear: res.data.pj.planYear,
              auditType: res.data.pj.auditType,
              projectSource: res.data.pj.projectSource,
              dateSection: [res.data.pj.startDate, res.data.pj.endDate],
              templeteName: res.data.pj.tbltemplete
                ? res.data.pj.tbltemplete.templeteName
                : '', //需要接口重置
              costs: res.data.pj.costs,
              tbltempletezy: res.data.pj.tbltempletezy
                ? res.data.pj.tbltempletezy.templeteName
                : '', //需要接口重置
              proSjfs: res.data.pj.proSjfs,
              externAlassig: res.data.pj.externAlassig,
              purpose: res.data.pj.purpose,
              scope: res.data.pj.scope,
              pursuant: res.data.pj.pursuant,
              comment: res.data.pj.comment,
              proDesc: res.data.pj.proDesc,
              implementaion: res.data.pj.implementaion,
              cospomsordepartment: res.data.pj.cospomsordepartment,
              cospomsordepartmentName: res.data.pj.cosdepartemnt
                ? res.data.pj.cosdepartemnt.orgname
                : '',
              projecttype: res.data.pj.projecttype,
              implementaionsteps: res.data.pj.implementaionsteps,
              auditrequirements: res.data.pj.auditrequirements,
              cntType: +res.data.pj.cntType,
            }
            let arr = [row.startDate, row.endDate]
            this.formData.dateSection = arr
            if (res.data.pj.isBmAudit === 0) {
              this.pdDx = 'yh'
              this.formData.auditStaffId = res.data.pj.auditStaffId
              this.formData.orgName = res.data.pj.auditStaffName
            } else {
              this.pdDx = 'bm'
              this.formData.auditOrgId = res.data.pj.auditOrgId
              this.formData.orgName = res.data.pj.auditOrgName
            }
          }
        }
        this.dialogFormVisible = true

        // this.$nextTick(() => {
        //   this.$refs['elForm'].clearValidate()
        // })
      },
      close() {
        this.$refs['elForm'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
        this.$emit('fetch-data')
      },
      async handleChange(val) {
        this.$set(this.formData, 'pprojectName', '')
        this.$set(this.formData, 'orgName', '')
        //选择所属审计计划获取计划时间
        let res = await getNbsjAuditPlanDateInfo({ planId: val })
        let arr = [res.data.planStartDate, res.data.planEndDate]
        this.$set(this.formData, 'dateSection', arr)
        //选择所属审计计划获取计划项目列表
        let resPlanProject = await getPlanProjectListByPlanId({ planId: val })
        this.resPlanProjectArr = resPlanProject.data.planList
      },

      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      handleChoicePlan() {
        if (!this.formData.planname) {
          this.$baseMessage('请选择所属审计计划', 'error')
        } else {
          this.$refs['planned'].showEdit(this.resPlanProjectArr)
        }
      },
      handleObject() {
        this.$refs['object'].showEdit()
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      auditTemplate(tempType) {
        if (!this.formData.orgName) {
          this.$baseMessage('请选择项目对象', 'error')
        } else {
          this.$refs['template'].showEdit(tempType)
        }
      },
      auditGuidelines(tempType) {
        if (!this.formData.orgName) {
          this.$baseMessage('请选择项目对象', 'error')
        } else {
          this.$refs['guidelines'].showEdit(tempType)
        }
      },
      showGroupLeader(sIndex) {
        this.sIndex = sIndex
        this.$refs['select'].showEdit('leader')
      },
      showTeamMembers(sIndex) {
        this.sIndex = sIndex
        this.$refs['select'].showEdit('members')
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
      selectTeamList(val, flagTitle) {
        if (flagTitle) {
          this.tableData[this.sIndex].leaderId = val[0].staffid
          this.tableData[this.sIndex].leaderName = val[0].realname
        } else {
          let arrStr = ''
          let arr = []
          arr = val.map((item) => item.realname)
          arrStr = arr.join(',')
          this.tableData[this.sIndex].zyNames = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          arrZy = val.map((item) => item.staffid)
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
          this.tableData[this.sIndex].zystaffids = arrStrZy
        }
      },
      formatDate(format) {
        var oDate = new Date(format)
        const oYear = oDate.getFullYear()
        const oMonth = oDate.getMonth() + 1
        const oDay = oDate.getDate()
        var oTime = ''
        oTime = oYear + '-' + this.getzf(oMonth) + '-' + this.getzf(oDay)

        return oTime
      },
      async submitForm() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            // let params = {
            //   planId: this.formData.planname,
            //   planProjectId: this.planprojectid,
            //   projectCode: this.formData.projectCode,
            //   projectName: this.formData.prjoectName,
            //   targetName: this.formData.targetName,
            //   pprojectName: this.formData.pprojectName,
            //   examineType: 1,
            //   pd_dx: this.pdDx,
            //   // [this.pdDx == 'yh' ? 'auditStaffId' : 'auditOrgId']: this.zcsId,
            //   planYear: this.formData.planYear,
            //   auditType: this.formData.auditType,
            //   projectSource: this.formData.projectSource,
            //   pmId: this.staffId,
            //   planStartDate: this.formatDate(this.formData.dateSection[0]),
            //   planEndDate: this.formatDate(this.formData.dateSection[1]),
            //   tempid: this.tempId,
            //   protempid: this.tempId,
            //   costs: this.formData.costs,
            //   attids: this.fileArrStr,
            //   tempzyId: this.tempZyId,
            //   proSjfs: this.formData.proSjfs,
            //   externalAssig: this.formData.externAlassig,
            //   purpose: this.formData.purpose,
            //   scope: this.formData.scope,
            //   pursuant: this.formData.pursuant,
            //   comment: this.formData.comment,
            //   proDesc: this.formData.proDesc,
            //   implementaion: this.formData.implementaion,
            //   cospomsordepartment: this.formData.cospomsordepartment,
            //   implementaionsteps: this.formData.implementaionsteps,
            //   auditrequirements: this.formData.auditrequirements,
            //   projecttype: this.formData.projecttype,
            // }
            let params = {
              planId: this.formData.planname,
              planProjectId: this.planprojectid,
              projectCode: this.formData.projectCode,
              prjoectName: this.formData.prjoectName,
              targetName: this.formData.targetName,
              pprojectName: this.formData.pprojectName,
              examineType: 1,
              pd_dx: this.pdDx,
              // [this.pdDx == 'yh' ? 'auditStaffId' : 'auditOrgId']: this.zcsId,
              planYear: this.formData.planYear,
              auditType: this.formData.auditType,
              projectSource: this.formData.projectSource,
              pmId: this.staffId,
              planStartDate: this.formatDate(this.formData.dateSection[0]),
              planEndDate: this.formatDate(this.formData.dateSection[1]),
              tempId: this.tempId,
              protempid: this.tempId,
              costs: this.formData.costs,
              attids: this.fileArrStr,
              tempzyId: this.tempZyId,
              proSjfs: this.formData.proSjfs,
              externAlassig: this.formData.externAlassig,
              purpose: this.formData.purpose,
              scopes: this.formData.scopes,
              pursuant: this.formData.pursuant,
              comments: this.formData.comments,
              proDesc: this.formData.proDesc,
              implementaion: this.formData.implementaion,
              cospomsordepartment: this.formData.cospomsordepartment,
              implementaionsteps: this.formData.implementaionsteps,
              auditrequirements: this.formData.auditrequirements,
              projecttype: this.formData.projecttype,
              cntType: this.formData.cntType,
            }
            // if (this.formData.orgName && this.isBmAuditFlag) {
            //   params.auditOrgId = this.zcsId
            // } else if (this.formData.orgName && !this.isBmAuditFlag) {
            //   params.auditStaffId = this.zcsId
            // } else if (this.pdDx == 'yh') {
            //   params.auditStaffId = this.zcsId
            // } else if (this.pdDx == 'bm') {
            //   params.auditOrgId = this.zcsId
            // }
            // let res = await projectAdd(params)

            // if (res.code == 1) {
            //   this.handleAddFlag = true
            //   this.projectId = res.data.WorkReport.projectId
            //   this.$baseMessage(res.msg, 'success')
            // } else {
            //   this.handleAddFlag = false
            // }
            if (this.pdDx === 'bm') {
              params.auditOrgId = this.formData.auditOrgId
              params.auditStaffId = undefined
            } else {
              params.auditStaffId = this.formData.auditStaffId
              params.auditOrgId = undefined
            }
            if (this.projectId) {
              params.projectId = this.projectId
            }
            params.pjTeamJson = []

            let flage = false
            if (this.tableData && this.tableData.length) {
              this.tableData.map((v) => {
                // let n = {}
                // n = {
                //   teamName: v.teamName,
                //   leaderid: v.leaderId,
                //   zystaffids: v.zyStaffids,
                //   projectid: this.projectId,
                //   teamId: v.teamId,
                // }
                // params.pjTeamJson.push(JSON.stringify(n))

                if (v.teamName === '') {
                  flage = false
                  return this.$message.error('请输入小组名称')
                } else if (v.leaderName === '') {
                  flage = false
                  return this.$message.error('请添加组长')
                } else if (v.zyNames === '') {
                  flage = false
                  return this.$message.error('请添加小组成员')
                } else {
                  flage = true
                  params.pjTeamJson.push({
                    teamName: v.teamName,
                    leaderid: v.leaderId,
                    zystaffids: v.zystaffids,
                    projectid: this.projectId,
                    teamId: v.teamId,
                  })
                }
              })
            } else {
              flage = false
              return this.$message.error('请添加小组')
            }

            if (flage) {
              params.pjTeamJson = JSON.stringify(params.pjTeamJson)

              projectAdd(params).then((res) => {
                if (res.code == 1) {
                  this.handleAddFlag = true
                  this.projectId = res.data.WorkReport.projectId
                  this.$baseMessage(res.msg, 'success')
                  this.close()
                } else {
                  this.handleAddFlag = false
                }
              })
            }
          }
        })
      },
      // 保存
      async saveProject(row, sIndex) {
        row.show = false
        await projectPjteamList({
          teamName: this.tableData[sIndex].teamName,
          leaderid: this.leaderId,
          zystaffids: this.zyStaffids,
          projectid: this.projectId,
          teamId: row.teamId,
        })
      },

      // 添加点击按钮
      handleAdd() {
        if (!this.handleAddFlag) {
          this.$baseMessage('请保存表单信息后再进行当前操作', 'error')
        } else {
          this.tableData.push({
            leaderName: '',
            zyNames: '',
            teamName: '',
            show: true,
          })
        }
      },
      handleDelete(row, index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            if (row.teamId) {
              proPjteamDel({ teamId: row.teamId }).then((res) => {
                if (res.code) {
                  this.tableData.splice(index, 1)
                  this.$message({
                    type: 'success',
                    message: '删除成功!',
                  })
                }
              })
            } else {
              this.tableData.splice(index, 1)
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      handleDeleteFile(row, index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(async () => {
            const res = await deleteProjectFile({ attId: row.attid })
            this.tableDataFile.splice(index, 1)
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      handleSuccess(response, file, fileList) {
        if (file.response.result == '200') {
          file.createPerson = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          this.tableDataFile.push(file.response.data)
          this.fileList = fileList
          let fileArr = []
          this.fileList.forEach((item) => {
            fileArr.push(item.response.data.attid)
          })
          this.fileArrStr = fileArr.join(',')
          this.$baseMessage(file.response.msg, 'success')
        } else {
          this.$baseMessage(file.response.msg, 'error')
        }
      },
      getChildlist(val) {
        // auditOrgId
        let pdDx = val[0].bsjtype
        if (pdDx === 'bm') {
          this.$set(this.formData, 'auditOrgId', val[0].orgids)
          this.pdDx = 'bm'
        } else {
          this.$set(this.formData, 'auditStaffId', val[0].orgids)
          this.pdDx = 'yh'
        }
        this.planprojectid = val[0].planprojectid
        this.$set(this.formData, 'targetName', val[0].targetname)
        this.$set(this.formData, 'pprojectName', val[0].projectname)
        this.$set(this.formData, 'prjoectName', val[0].projectname)
        this.$set(this.formData, 'orgName', val[0].orgidnames)
        this.$set(this.formData, 'externAlassig', val[0].externalassig)
      },
      getChildlistObj(val, flag) {
        if (flag == 'right') {
          this.pdDx = 'yh'
          // this.zcsId = val[0].staffid
          this.$set(this.formData, 'orgName', val[0].realname)
          this.$set(this.formData, 'auditStaffId', val[0].staffid)
        } else {
          this.pdDx = 'bm'
          // this.zcsId = val.id
          this.$set(this.formData, 'auditOrgId', val.id)
          this.$set(this.formData, 'orgName', val.name)
        }
        this.$forceUpdate()
      },
      async getChildlistPro(val) {
        // const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })
        //
        // if (res.data.cnt === 0) {
        //   return this.$message.error('该模板下没有业务单元')
        // }
        this.staffId = val[0].staffid
        this.$set(this.formData, 'realName', val[0].realname)
      },
      async getChildlistTem(val) {
        const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })

        if (res.data.cnt === 0) {
          return this.$message.error('该模板下没有业务单元')
        }
        this.tempId = val[0].templeteId
        this.$set(this.formData, 'templeteName', val[0].templeteName)
      },
      async getChildlistGui(val) {
        const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })
        if (res.data.cnt === 0) {
          return this.$message.error('该模板下没有业务单元')
        }
        this.tempZyId = val[0].templeteId
        this.$set(this.formData, 'tbltempletezy', val[0].templeteName)
      },
      //补0操作
      getzf(num) {
        if (parseInt(num) < 10) {
          num = '0' + num
        }
        return num
      },
    },
  }
</script>
<style scoped lang="scss">
  ::v-deep .el-form.disabled {
    input {
      border: 0;
      background-color: #ffffff;
    }
  }
</style>
