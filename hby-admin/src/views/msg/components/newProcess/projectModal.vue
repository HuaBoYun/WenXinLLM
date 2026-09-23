<template>
  <div>
    <div>
      <el-row :gutter="15" v-loading="loading">
        <el-form
          ref="elForm"
          :class="{ disabled: disabled }"
          label-width="135px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <!-- <el-col :span="12">
            <el-form-item label="所属审计计划" prop="planname">
              <el-select
                v-model="formData.planname"
                placeholder="请选择"
                style="width: 266px"
                @change="handleChange"
                :disabled="disabled"
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
          <el-col :span="12">
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
                :disabled="disabled"
                style="margin-left: 10px"
                type="primary"
                size="small"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col> -->
          <el-col :span="12" v-if="this.formData.projectCode">
            <el-form-item label="项目编号" prop="projectCode">
              <el-input
                v-model="formData.projectCode"
                disabled
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
                :disabled="disabled"
                clearable
                placeholder="请输入项目名称"
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目类别" prop="projecttype">
              <!-- <el-input
                v-model="formData.targetName"
                :disabled="disabled"
                clearable
                placeholder="请输入项目类别"
                style="width: 266px"
              /> -->
              <el-select
                :disabled="disabled"
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
                :disabled="disabled"
                size="small"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划年度" prop="planYear">
              <el-date-picker
                v-model="formData.planYear"
                type="year"
                value-format="yyyy"
                placeholder="请选择计划年度"
                :disabled="disabled"
                style="width: 266px"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审计类型(审计厅)" prop="auditType">
              <el-select
                v-model="formData.auditType"
                placeholder="请选择"
                :disabled="disabled"
                style="width: 266px"
              >
                <el-option
                  v-for="item in auditTypeArr"
                  :key="item.typeId"
                  :label="item.auditType"
                  :value="item.auditType"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目来源" prop="projectSource">
              <!-- <el-input
                v-model="formData.projectSource"
                :disabled="disabled"
                clearable
                placeholder="请输入项目来源"
                style="width: 266px"
              /> -->
              <el-select
                :disabled="disabled"
                v-model="formData.projectSource"
                placeholder="请选择项目来源"
                style="width: 266px"
              >
                <el-option
                  label="年度审计计划"
                  value="年度审计计划"
                ></el-option>
                <el-option label="临时指派" value="临时指派"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="审计类型(国资委)" prop="cntType">
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
          <el-col :span="24">
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
                align="center"
                label="计划编号"
                prop="plancode"
              ></el-table-column>

              <el-table-column
                align="center"
                label="计划名称"
                prop="planname"
              ></el-table-column>

              <!-- <el-table-column
                align="center"
                label="计划时间"
                prop="starttime"
              ></el-table-column> -->
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
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
              <el-button
                type="success"
                @click="step1add"
                :disabled="disabled"
                v-if="showSaveButton"
              >
                保存
              </el-button>
            </div>
          </el-col>

          <el-col :span="24">
            <el-divider></el-divider>
          </el-col>
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
                size="small"
                :disabled="disabled"
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
                :disabled="disabled"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审计方式" prop="proSjfs">
              <el-select
                v-model="formData.proSjfs"
                placeholder="请选择审计方式"
                style="width: 266px"
                :disabled="disabled"
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
            <el-form-item label="项目费用估算(元)" prop="field108">
              <el-input
                v-model="formData.costs"
                clearable
                placeholder="请输入项目费用估算"
                :disabled="disabled"
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
                :disabled="disabled"
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
                :disabled="disabled"
                size="small"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
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
                :disabled="disabled"
                size="small"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col> -->
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
                size="small"
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
                :disabled="disabled"
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
                v-model="formData.scopes"
                :disabled="disabled"
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
                :disabled="disabled"
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
                v-model="formData.comments"
                :disabled="disabled"
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
                :disabled="disabled"
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
                    :disabled="disabled"
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
                    v-model="scope.row.leaderName"
                    size="mini"
                    disabled
                    style="width: 70%"
                  />
                  <!-- :disabled="!scope.row.show" -->

                  <el-button
                    type="primary"
                    size="mini"
                    style="margin-left: 3px"
                    @click="showGroupLeader(scope.$index)"
                    :disabled="disabled"
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
                  <!-- :disabled="!scope.row.show" -->

                  <el-button
                    type="primary"
                    size="mini"
                    style="margin-left: 3px"
                    @click="showTeamMembers(scope.$index)"
                    :disabled="disabled"
                  >
                    选择
                  </el-button>
                </template>
              </el-table-column>
              <!-- v-if="!disabled" -->

              <el-table-column align="center" label="操作" min-width="80">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    @click="handleDelete(scope.row, scope.$index)"
                    :disabled="disabled"
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
                <template slot-scope="scope">
                  <el-button
                    :disabled="false"
                    type="text"
                    @click="handleDown(scope.row)"
                  >
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
        </el-form>
      </el-row>

      <div
        style="text-align: right; margin-top: 10px; margin-right: 10px"
        v-if="!disabled"
      >
        <!-- <el-button @click="close">取 消</el-button> -->
        <el-button type="primary" @click="add">确 定</el-button>

        <el-button type="primary" @click="ymsubmit">提 交</el-button>
      </div>
      <!-- <template v-if="!disabled" #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template> -->
    </div>
    <!-- 计划项目子组件 -->
    <planned-project
      ref="planned"
      @plannedList="getChildlist"
    ></planned-project>
    <!-- 被审计对象子组件 -->
    <!-- <project-object @objList="getChildlistObj" ref="object"></project-object> -->
    <Company ref="audiTree" @submit="getChildlistObj"></Company>
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
    <!-- 选择组长组员子组件 -->
    <special-select
      ref="special_select"
      @selectTeamList="selectTeamList"
    ></special-select>
    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />

    <!-- 提交 -->
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
  import { download } from '@/api/audit/implement'
  import {
    deleteProjectFile,
    getNbsjAuditPlanDateInfo,
    getNbsjAuditPlanListForMerge,
    getNbsjTypeListForMerge,
    getPlanProjectListByPlanId,
    getPlanTableData,
    getProjectDetail,
    getProjectPjteamList,
    getQuestionTypeList,
    projectAdd,
    projectFileList,
    proPjteamDel,
    seachTeamplateCnt,
  } from '@/api/audit/project'
  import store from '@/store'
  import DepartmentOptions from '@/views/audit/report/components/options/department.vue'
  import Auditee from './formComponents/Auditee.vue'
  import auditGuidelines from './formComponents/auditGuidelines.vue'
  import auditTemplate from './formComponents/auditTemplate.vue'
  import plannedProject from './formComponents/plannedProject.vue'
  import projectManage from './formComponents/projectManage.vue'
  import selectTeam from './formComponents/selectTeam.vue'
  import specialSelect from './formComponents/specialSelectTeam.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const { baseURL } = require('@/config')
  export default {
    name: 'MaintainEdit',
    components: {
      plannedProject,
      projectManage,
      auditTemplate,
      auditGuidelines,
      selectTeam,
      DepartmentOptions,
      Company: () => import('@/components/Company.vue'),
      Auditee,
      specialSelect,
      Resubmit,
    },
    data() {
      return {
        loading: false,
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
        isBmAuditStr: '',
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
          scopes: '',
          pursuant: '',
          comments: '',
          proDesc: '',
          cntType: '',
        },

        planId: '',
        createPerson: '',
        fileList: [],
        planTableData: [],
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
          pprojectName: [
            {
              message: '请选择计划项目',
              required: true,
              trigger: 'change',
            },
          ],
          projecttype: [
            {
              message: '请选择项目类别',
              required: true,
              trigger: 'change',
            },
          ],
          planYear: [
            {
              message: '请选择计划年度',
              required: true,
              trigger: 'change',
            },
          ],
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
              message: '请选择被审计对象',
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
              trigger: 'change',
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
        showSaveButton: true,
        //提交
        ymFromId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
      }
    },
    methods: {
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleDepartmentSelected(node) {
        //保存名称
        this.$set(this.formData, `cospomsordepartmentName`, node.name)
        //保存名称对应的ID
        this.$set(this.formData, `cospomsordepartment`, node.id)
      },
      async showDetail(row, type, isEdit) {
        this.isEdit = isEdit
        if (!this.isEdit) {
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
      async showEdit(
        row,
        disabled,
        fromId,
        flowtaskinfoflowid,
        ymFromId,
        status
      ) {
        console.log('进入方法啦')
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
        this.showSaveButton = true
        this.tableData = []
        this.formData = {}
        this.planTableData = []
        this.projectId = 0
        // this.$nextTick(() => {
        //   this.$refs['elForm'].clearValidate()
        // })
        //获取统计类型下拉数据
        getQuestionTypeList().then((res) => {
          this.questionTypeList = res.data.pageInfo.tlist
        })
        let resType = await getNbsjTypeListForMerge()
        this.auditTypeArr = resType.data.tlist
        if (!row) {
          this.title = '添加'
          let planNumRes =
            planNum.split('-')[0] + '-' + (parseInt(planNum.split('-')[1]) + 1)
          this.disabled = false
          this.tableData = []
          this.formData = {
            // projectCode: planNumRes,     // 自动生成的项目编号
            auditType: this.auditTypeArr[0].auditType,
          }
          //获取所属审计计划集合
          let res = await getNbsjAuditPlanListForMerge()
          this.auditPlanArr = res.data
          this.tableDataFile = []
          //获取审计类型集合
        } else {
          if (!disabled) {
            this.loading = true
            this.title = '编辑'
            this.disabled = false
            this.handleAddFlag = true
            this.projectId = row.projectId
            // let res = await getProjectDetail({
            //   projectid: row.projectId,
            // })
            getProjectDetail({
              projectid: row.projectId,
            }).then((res) => {
              projectFileList({
                projectId: row.projectId,
              }).then((file) => {
                this.tableDataFile = file.data.data
              })
              if (res.code === 1) {
                if (res.data.pj.tblnbsjPlan) {
                  //获取计划列表
                  getPlanTableData({
                    planId: res.data.pj.tblnbsjPlan.planid,
                  }).then((item) => {
                    this.planTableData = item.data.data
                  })
                } else {
                  this.planTableData = []
                }

                getProjectPjteamList({
                  projectid: row.projectId,
                }).then((resTeam) => {
                  if (resTeam && resTeam.data.listTeam.length) {
                    this.tableData = resTeam.data.listTeam
                  }
                })
                if (res.data.pj.tbltemplete) {
                  this.showSaveButton = false
                } else {
                  this.showSaveButton = true
                }

                getNbsjAuditPlanListForMerge().then((resPlan) => {
                  this.auditPlanArr = resPlan.data
                  this.tempId = res.data.pj.tbltemplete
                    ? res.data.pj.tbltemplete.templeteId
                    : null
                  this.tempZyId = res.data.pj.tbltempletezy
                    ? res.data.pj.tbltempletezy.templeteId
                    : ''
                  this.formData = {
                    // planname: res.data.pj.tblnbsjPlan.planname,
                    planname: res.data.pj.tblnbsjPlan
                      ? res.data.pj.tblnbsjPlan.planid
                      : '',
                    pprojectName: res.data.pj.pprojectName,
                    projectCode: res.data.pj.projectCode,
                    prjoectName: res.data.pj.prjoectName,
                    targetName: res.data.pj.targetName,
                    realName: res.data.pj.pmStaff
                      ? res.data.pj.pmStaff.realname
                      : '', //需要接口重置
                    planYear: res.data.pj.planYear,
                    auditType: res.data.pj.auditType,
                    projectSource: res.data.pj.projectSource,
                    projecttype: res.data.pj.projecttype,
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
                    scopes: res.data.pj.scopes,
                    pursuant: res.data.pj.pursuant,
                    comments: res.data.pj.comments,
                    proDesc: res.data.pj.proDesc,
                    implementaion: res.data.pj.implementaion,
                    cospomsordepartment: res.data.pj.cospomsordepartment,
                    cospomsordepartmentName: res.data.pj.cosdepartemnt
                      ? res.data.pj.cosdepartemnt.orgname
                      : '',
                    implementaionsteps: res.data.pj.implementaionsteps,
                    auditrequirements: res.data.pj.auditrequirements,
                    cntType: +res.data.pj.cntType,
                    // projectId: row.projectId,
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
                })
                this.loading = false
              }
            })
          } else {
            this.title = '查看'
            this.disabled = true
            this.loading = true
            // 000000
            this.$nextTick(() => {
              // this.$refs['elForm'].clearValidate()
              this.rules = {}
            })
            let resFile = await projectFileList({
              projectId: row.projectId,
            })
            this.tableDataFile = resFile.data.data

            let res = await getProjectDetail({
              projectid: row.projectId,
            })
            if (res.data.pj.tblnbsjPlan) {
              //获取计划列表
              getPlanTableData({ planId: res.data.pj.tblnbsjPlan.planid }).then(
                (item) => {
                  this.planTableData = item.data.data
                }
              )
            } else {
              this.planTableData = []
            }

            let resTeam = await getProjectPjteamList({
              projectid: row.projectId,
            })
            if (resTeam && resTeam.data.listTeam.length) {
              this.tableData = resTeam.data.listTeam
            }
            // this.$set(this.formData, 'targetName', val[0].targetname)
            // this.$set(this.formData, 'pprojectName', val[0].projectname)
            // this.$set(this.formData, 'prjoectName', val[0].projectname)
            // this.$set(this.formData, 'orgName', val[0].orgidnames)
            // this.$set(this.formData, 'externAlassig', val[0].externalassig)
            this.formData = {
              planname: res.data.pj.tblnbsjPlan
                ? res.data.pj.tblnbsjPlan.planname
                : '',
              pprojectName: res.data.pj.pprojectName,
              projectCode: res.data.pj.projectCode,
              prjoectName: res.data.pj.prjoectName,
              targetName: res.data.pj.targetName,
              realName: res.data.pj.pmStaff ? res.data.pj.pmStaff.realname : '', //需要接口重置
              orgName: res.data.pj.pmStaff ? res.data.pj.pmStaff.realname : '',
              planYear: res.data.pj.planYear,
              auditType: res.data.pj.auditType,
              projectSource: res.data.pj.projectSource,
              dateSection: [],
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
              scopes: res.data.pj.scopes,
              pursuant: res.data.pj.pursuant,
              comments: res.data.pj.comments,
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
            let arr = [row.startDate, row.endDate]
            this.formData.dateSection = arr
            this.loading = false
          }
        }
        this.$forceUpdate()
      },
      close() {
        this.title = '添加'
        this.$refs['elForm'].resetFields()

        this.$bus.$emit('updateMsg', 0)
        this.form = this.$options.data().form
        // this.dialogFormVisible = false
        // this.$emit('fetch-data')
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
        this.$refs['audiTree'].showEdit()
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      auditTemplate(tempType) {
        if (!this.formData.orgName) {
          this.$baseMessage('请选择被审计对象', 'error')
        } else {
          this.$refs['template'].showEdit(tempType, this.formData.auditType)
        }
      },
      auditGuidelines(tempType) {
        if (!this.formData.orgName) {
          this.$baseMessage('请选择被审计对象', 'error')
        } else {
          this.$refs['guidelines'].showEdit(tempType, this.formData.auditType)
        }
      },
      showGroupLeader(sIndex) {
        this.sIndex = sIndex
        this.$refs['special_select'].showEdit('leader')
      },
      showTeamMembers(sIndex) {
        this.sIndex = sIndex
        this.$refs['special_select'].showEdit('members')
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
      async add() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
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
              tempzyId: this.tempId,
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
                  // this.close()
                } else {
                  this.handleAddFlag = false
                }
              })
            }
          }
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
        this.tableDataFile = []
      },
      // 保存
      async saveProject(row, sIndex) {
        // row.show = false
        // let data = await projectPjteamList({
        //   teamName: this.tableData[sIndex].teamName,
        //   leaderid: this.leaderId,
        //   zystaffids: this.zyStaffids,
        //   projectid: this.projectId,
        //   teamId: row.teamId,
        // })
        // this.$baseMessage(data.msg, 'success')
      },

      // 添加点击按钮
      handleAdd() {
        // if (!this.handleAddFlag) {
        //   this.$baseMessage('请保存表单信息后再进行当前操作', 'error')
        // } else {
        //   this.tableData.push({
        //     leaderName: '',
        //     zyNames: '',
        //     teamName: '',
        //     show: true,
        //   })
        // }
        this.tableData.push({
          leaderName: '',
          zyNames: '',
          teamName: '',
          show: true,
        })
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
          this.$set(this.formData, 'orgName', val[0].realname)
          this.$set(this.formData, 'auditStaffId', val[0].staffid)
        } else {
          this.pdDx = 'bm'
          this.$set(this.formData, 'auditOrgId', val.id)
          this.$set(this.formData, 'orgName', val.name)
        }
        this.$forceUpdate()
      },

      getChildlistPro(val) {
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
      formatDate(format) {
        var oDate = new Date(format)
        const oYear = oDate.getFullYear()
        const oMonth = oDate.getMonth() + 1
        const oDay = oDate.getDate()
        var oTime = ''
        oTime = oYear + '-' + this.getzf(oMonth) + '-' + this.getzf(oDay)

        return oTime
      },
      //补0操作
      getzf(num) {
        if (parseInt(num) < 10) {
          num = '0' + num
        }
        return num
      },
      step1add() {
        this.rules.externAlassig = []
        this.rules.templeteName = []
        this.rules.tbltempletezy = []
        this.rules.realName = []
        this.rules.dateSection = []
        this.rules.proSjfs = []
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let params = {
              projectCode: this.formData.projectCode,
              prjoectName: this.formData.prjoectName,
              pd_dx: this.pdDx,
              planYear: this.formData.planYear,
              auditType: this.formData.auditType,
              projectSource: this.formData.projectSource,
              pmId: this.staffId,
              // planStartDate: this.formatDate(this.formData.dateSection[0]),
              // planEndDate: this.formatDate(this.formData.dateSection[1]),
              // proSjfs: this.formData.proSjfs,
              projecttype: this.formData.projecttype,
              cntType: +this.formData.cntType,
            }

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
            projectAdd(params).then((res) => {
              if (res.code === 1) {
                this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
              }
              //存在计划外的时候没有计划返回
              this.planTableData = res.data.plan && [res.data.plan]
              this.formData.planname = res.data.plan && res.data.plan.planid
              this.formData.projectCode =
                res.data.WorkReport && res.data.WorkReport.projectCode
              this.projectId = res.data.WorkReport.projectId
              ;(this.rules.externAlassig = [
                {
                  required: true,
                  message: '请选择是否外委',
                  trigger: 'change',
                },
              ]),
                (this.rules.templeteName = [
                  {
                    required: true,
                    message: '请选择审计模板',
                    trigger: 'change',
                  },
                ])
              this.rules.tbltempletezy = [
                {
                  required: true,
                  message: '请选择审计指引',
                  trigger: 'change',
                },
              ]
              this.rules.realName = [
                {
                  required: true,
                  message: '请选择项目负责人',
                  trigger: 'change',
                },
              ]
              this.rules.dateSection = [
                {
                  required: true,
                  message: '请选择项目计划时间',
                  trigger: 'change',
                },
              ]
              this.rules.proSjfs = [
                {
                  required: true,
                  message: '请选择审计方式',
                  trigger: 'change',
                },
              ]
            })
          }
        })
      },
      handleSelection(val) {
        this.current = val
        // if (this.flagTitle) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.formData.planname = val[0].planid
        // } else {
        //   this.multipleSelection = val
        // }
      },
      //提交
      async ymsubmit() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
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
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
