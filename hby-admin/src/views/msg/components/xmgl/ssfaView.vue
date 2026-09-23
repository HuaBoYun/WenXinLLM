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
          <el-col :span="12">
            <el-form-item label="项目编号" prop="qdcode">
              <el-input
                v-model="formData.qdcode"
                clearable
                :disabled="disabled"
                placeholder="请输入项目编号"
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目名称" prop="projectName">
              <el-input
                v-model="formData.projectName"
                disabled
                placeholder="请输入项目名称"
                style="width: 266px"
              />
              <el-button
                @click="handlePlanRelate"
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
            <el-form-item label="项目类别" prop="projectType">
              <el-select
                :disabled="disabled"
                v-model="formData.projectType"
                placeholder="请选择项目类别"
                style="width: 266px"
              >
                <el-option label="计划内" :value="1"></el-option>
                <el-option label="计划外" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="项目经理" prop="projectOrderName">
              <el-input
                v-model="formData.projectOrderName"
                clearable
                placeholder="请选择项目经理"
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
            <el-form-item label="被审计单位" prop="auditOrgName">
              <el-input
                v-model="formData.auditOrgName"
                clearable
                placeholder="请选择被审计单位"
                style="width: 266px"
                disabled
              />
              <!-- <el-button
                @click="handleObject"
                style="margin-left: 10px"
                type="primary"
                :disabled="disabled"
                size="small"
              >
                选择
              </el-button> -->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主审" prop="zsname">
              <el-input
                v-model="formData.zsname"
                style="width: 266px"
                clearable
                placeholder="主审"
                disabled
              />
              <el-button
                @click="openPerson('zsname')"
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
            <el-form-item label="助审" prop="fzname">
              <el-input
                v-model="formData.fzname"
                style="width: 266px"
                clearable
                placeholder="助审"
                disabled
              />
              <el-button
                @click="openPerson('fzname')"
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
            <el-form-item label="是否工程项目" prop="isgc">
              <el-select
                v-model="formData.isgc"
                placeholder="请选择"
                style="width: 266px"
                disabled
              >
                <el-option
                  v-for="item in wwArr1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item
              label="审计模板"
              prop="tempName"
              v-if="formData.isgc == 0"
            >
              <el-input
                v-model="formData.tempName"
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
          <el-col :span="12">
            <el-form-item
              label="工程模板"
              prop="projecttempName"
              v-if="formData.isgc == 1"
            >
              <el-input
                v-model="formData.projecttempName"
                clearable
                placeholder="请选择工程模板"
                disabled
                style="width: 266px"
              />
              <el-button
                @click="handleProject"
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
            <el-form-item label="是否境外" prop="isjy">
              <el-radio-group v-model="formData.isjy" :disabled="disabled">
                <el-radio label="是">是</el-radio>
                <el-radio label="否">否</el-radio>
              </el-radio-group>
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
                @change="yearChange"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审计类型" prop="sjlxName">
              <el-input
                v-model="formData.sjlxName"
                clearable
                placeholder="请选择审计类型"
                :style="{ width: '266px' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.SJType.showEdit()"
                :disabled="disabled"
                size="small"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <!--
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
          </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item label="时间安排" prop="planTime">
              <el-input
                v-model="formData.planTime"
                clearable
                placeholder="请输入项目编号"
                style="width: 266px"
              />
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item
              label="实施时间"
              prop="implementtime"
              v-if="title != 'edit'"
            >
              <el-date-picker
                v-model="formData.implementtime"
                format="yyyy-MM-dd"
                start-placeholder="实施时间"
                style="width: 266px"
                type="date"
                value-format="yyyy-MM-dd"
                disabled
              />
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item label="延期时间" prop="yqdate" v-if="title != 'edit'">
              <el-date-picker
                v-model="formData.yqdate"
                clearable
                format="yyyy-MM-dd"
                start-placeholder="延期时间"
                style="width: 266px"
                type="date"
                value-format="yyyy-MM-dd"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="title != 'edit'">
            <el-form-item label="专业科室人员" prop="zyksryrwnames">
              <el-input
                v-model="formData.zyksryrwnames"
                clearable
                :disabled="disabled"
                style="width: 266px"
              />
            </el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item label="项目概述" prop="projectSummary">
              <el-input
                v-model="formData.projectSummary"
                type="textarea"
                clearable
                placeholder="请输入项目编号"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>

          <!-- <el-col :span="24">
            <el-divider></el-divider>
          </el-col> -->

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
          <!-- <el-col :span="12">
            <el-form-item label="审计方式" prop="auditMethod">
              <el-select
                v-model="formData.auditMethod"
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
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="项目费用估算(元)" prop="costEstimation">
              <el-input
                v-model="formData.costEstimation"
                clearable
                placeholder="请输入项目费用估算"
                :disabled="disabled"
                style="width: 266px"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="是否外委" prop="isWw">
              <el-select
                v-model="formData.isWw"
                placeholder="请选择"
                style="width: 266px"
                :disabled="disabled"
              >
                <el-option
                  v-for="item in wwArr"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <!-- <el-col :span="12">
            <el-form-item label="实施主体" prop="implementType">
              <el-select
                :disabled="disabled"
                v-model="formData.implementType"
                placeholder="请选择实施主体"
                style="width: 266px"
              >
                <el-option label="自主实施" :value="1"></el-option>
                <el-option label="委外实施" :value="2"></el-option>
                <el-option label="联合外部机构实施" :value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item label="协办部门">
              <el-input
                v-model="formData.deptName"
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
          </el-col> -->
          <el-col :span="24">
            <el-form-item label="审计要求" prop="auditRequirement">
              <el-input
                :disabled="disabled"
                v-model="formData.auditRequirement"
                clearable
                placeholder="请选择审计要求"
                type="textarea"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="具体实施步骤" prop="implementSteps">
              <el-input
                :disabled="disabled"
                v-model="formData.implementSteps"
                clearable
                placeholder="请选择实施步骤"
                type="textarea"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审计目标和范围" prop="auditReason">
              <el-input
                type="textarea"
                v-model="formData.auditReason"
                :disabled="disabled"
                clearable
                placeholder="请输入审计目标和范围"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审计内容和重点" prop="auditContent">
              <el-input
                type="textarea"
                v-model="formData.auditContent"
                :disabled="disabled"
                clearable
                placeholder="请输入审计内容和重点"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审计程序和方法" prop="auditProcess">
              <el-input
                type="textarea"
                v-model="formData.auditProcess"
                :disabled="disabled"
                clearable
                placeholder="请输入审计程序和方法"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="对专家和外部审计结果的利用"
              prop="auditResultUse"
            >
              <el-input
                type="textarea"
                v-model="formData.auditResultUse"
                :disabled="disabled"
                clearable
                placeholder="请输入对专家和外部审计结果的利用"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="其他有关内容" prop="otherContent">
              <el-input
                type="textarea"
                v-model="formData.otherContent"
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
              <!-- <el-button type="success" @click="handleAdd">增加一行</el-button> -->
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
                  <el-button
                    type="primary"
                    size="mini"
                    style="margin-left: 3px"
                    @click="showGroupLeader(scope.$index, 'leader')"
                    :disabled="disabled"
                  >
                    选择
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="副组长" prop="fzzname">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.fzzname"
                    size="mini"
                    disabled
                    style="width: 70%"
                  />
                  <el-button
                    type="primary"
                    size="mini"
                    style="margin-left: 3px"
                    @click="showGroupLeader(scope.$index, 'fzLeader')"
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

              <!-- <el-table-column align="center" label="操作" width="80">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    @click="handleDelete(scope.row, scope.$index)"
                    :disabled="disabled"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column> -->
            </el-table>
          </el-col>
          <el-col :span="24">
            <el-divider>文件上传</el-divider>
          </el-col>
          <el-col :span="24">
            <!-- <el-upload
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
                    @click="handleDowns(scope.row)"
                  >
                    下载
                  </el-button>
                  <el-button :disabled="false" type="text" @click="handlePreviewFile(scope.row)">
                    预览
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

      <!-- <template v-if="!disabled">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="ymsubmit" type="primary">提交</el-button>
      </template> -->
      <div
        slot="footer"
        style="text-align: right; margin-top: 10px"
        v-if="!disabled"
      >
        <el-button @click="close">取消</el-button>
        <el-button @click="submitForm" type="primary">确 定</el-button>
        <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
          提 交
        </el-button>
      </div>
    </div>
    <!-- 计划名称子组件 -->
    <planNamePro ref="planNameRef" @planNamList="getNamelist"></planNamePro>
    <!-- 计划项目子组件 -->
    <planned-project
      ref="planned"
      @plannedList="getChildlist"
    ></planned-project>
    <!-- 被审计对象子组件 -->
    <!-- <project-objectgetNamelist @objList="getChildlistObj" ref="object"></project-objectgetNamelist> -->
    <Company ref="audiTree" @select="getChildlistObj"></Company>
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
    <!-- 工程模板子组件 -->
    <projectTemplate
      ref="projectRef"
      @projectList="getProjectReflist"
    ></projectTemplate>

    <!-- 审计指引子组件 -->
    <audit-guidelines
      @guidelinesList="getChildlistGui"
      ref="guidelines"
    ></audit-guidelines>

    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>

    <!-- 选择组员子组件 -->
    <special-select2
      ref="special_select2"
      @projectManage="selectTeamList2"
    ></special-select2>
    <!-- 选择组长子组件 -->
    <special-select
      ref="special_select"
      @projectManage="selectTeamList"
    ></special-select>
    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <danxuanPerson ref="danxuanPerson" @projectManage="danxuanSelect" />
    <xmqdModal ref="xmqdModal" @selected="xmqdModalSelect" />
    <cwModal ref="cwModal" @selected="cwModalSelect" />
    <gcModal ref="gcModal" @selected="gcModalSelect" />
    <SJType ref="SJType" @submit="SJTypeSelect" />
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
  import { download, deleteFile } from '@/oapi/audit/report'
  import { jhgljhList, jhgljhDetail } from '@/api/monitor/question'
  import { getList } from '@/oapi/baseConfig/gcsjmb'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import SJType from '@/views/oilAudit/project/components/tree.vue'
  import {
    implementPlanDetail,
    implementPlanSaveOrUpdate,
    proPjteamDel,
    seachTeamplateCnt,
    getNbsjTypeListForMerge,
  } from '@/oapi/audit/project'
  import store from '@/store'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import Auditee from '@/views/oilAudit/plan/components/childCom/Auditee.vue'
  import auditGuidelines from '@/views/oilAudit/project/components/formComponents/auditGuidelines.vue'
  import auditTemplate from '@/views/oilAudit/project/components/formComponents/auditTemplate.vue'
  import plannedProject from '@/views/oilAudit/project/components/formComponents/plannedProject.vue'
  import planNamePro from '@/views/oilAudit/project/components/formComponents/planNamePro.vue'
  import projectTemplate from '@/views/oilAudit/project/components/formComponents/projectTemplate.vue'
  import projectManage from '@/components/danxuanPerson.vue'
  import selectTeam from '@/views/oilAudit/project/components/formComponents/selectTeam.vue'
  import specialSelect from '@/components/danxuanPerson.vue'
  import specialSelect2 from '@/components/selectPerson.vue'
  import danxuanPerson from '@/components/danxuanPerson.vue'
  import xmqdModal from '@/views/oilAudit/project/components/formComponents/xmqdModal.vue'
  import cwModal from '@/views/oilAudit/project/components/formComponents/cwModal.vue'
  import gcModal from '@/views/oilAudit/project/components/formComponents/gcModal.vue'
  import Company from '@/components/departments.vue'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  const { baseURL } = require('@/config')

  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'MaintainEdit',
    components: {
      plannedProject,
      projectManage,
      auditTemplate,
      auditGuidelines,
      selectTeam,
      DepartmentOptions,
      Company,
      Auditee,
      specialSelect,
      specialSelect2,
      CandidateUserSelect,
      planNamePro,
      projectTemplate,
      Resubmit,
      danxuanPerson,
      xmqdModal,
      SJType,
      cwModal,
      gcModal,
    },
    data() {
      return {
        loading: false,
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        disabled: true,
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
          projectName: '',
          qdcode: '',
          projectType: '',
          auditOrgName: '',
          zsname: '',
          fzname: '',
          isjy: '否',
          planYear: '',
          sjlxId: '',
          sjlxName: '',
          cntType: '',
          planTime: '',
          projectSummary: '',
          projectOrderName: '',
          dateSection: '',
          auditMethod: '',
          costEstimation: '',
          isWw: 0,
          isgc: '',
          tempName: '',
          projecttempName: '',
          implementType: '',
          auditRequirement: '',
          implementSteps: '',
          auditReason: '',
          auditContent: '',
          auditProcess: '',
          auditResultUse: '',
          otherContent: '',
          yqdate: '',
          implementtime: '',
          teamName: '',
          planName: undefined,
          dateSection: [],
          zykstype: '',
          xmapbid: '',
        },
        personType: '',
        planId: '',
        createPerson: '',
        fileList: [],
        planTableData: [],
        auditPlanArr: [],
        proPlanArr: [],
        auditTypeArr: [],
        projectArr: [],
        wwArr: [
          { value: 1, label: '是', key: 1 },
          { value: 0, label: '否', key: 2 },
        ],
        wwArr1: [
          { value: '1', label: '是', key: '1' },
          { value: '0', label: '否', key: '2' },
        ],
        auditMethodArr: [
          {
            label: '现场',
            value: 1,
            key: 0,
          },
          {
            label: '非现场',
            value: 2,
            key: 1,
          },
          {
            label: '非现场与现场结合',
            value: 3,
            key: 2,
          },
        ],
        rules: {
          planName: [
            {
              message: '请选择计划名称',
              required: true,
              trigger: 'change',
            },
          ],
          planProjectName: [
            {
              message: '请选择计划项目',
              required: true,
              trigger: 'change',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请输入项目名称',
              trigger: 'change',
            },
          ],
          projectType: [
            {
              message: '请选择项目类别',
              required: true,
              trigger: 'change',
            },
          ],
          auditOrgName: [
            {
              required: true,
              message: '请选择被审计对象',
              trigger: 'blur',
            },
          ],
          planYear: [
            {
              message: '请选择计划年度',
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
          projectOrderName: [
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
          tempName: [
            {
              required: true,
              message: '请选择审计模板',
              trigger: 'change',
            },
          ],
          projecttempName: [
            {
              required: true,
              message: '请选择工程模板',
              trigger: 'change',
            },
          ],
          // auditMethod: [
          //   {
          //     required: true,
          //     message: '请选择审计方式',
          //     trigger: 'change',
          //   },
          // ],
          isWw: [
            {
              required: true,
              message: '请选择是否外委',
              trigger: 'change',
            },
          ],
          sjlxName: [
            {
              required: true,
              message: '请选择审计类型',
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
        clearType: false,
        mxArr: ['mx11', 'mx12', 'mx21', 'mx22', 'mx23', 'mx31', 'mx32'],

        //提交
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        btnLoading: false,
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
      yearChange(val) {
        console.log(val, this.formData.planYear)
      },
      handleSjlx(val) {
        let obj = this.auditTypeArr.find((v) => {
          return v.typeId === val
        })
        this.$set(this.formData, `sjlxName`, obj.auditType)
      },
      handleDepartmentSelected(node) {
        //保存名称
        this.$set(this.formData, `deptName`, node.label)
        //保存名称对应的ID
        this.$set(this.formData, `deptId`, node.id)
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
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        this.dialogFormVisible = true
        this.showSaveButton = true
        this.tableData = []
        // this.formData = {}
        this.planTableData = []
        this.projectId = 0
        this.title = title
        this.$nextTick(() => {
          this.$refs['elForm'].clearValidate()
        })
        if (title == 'edit') {
          this.disabled = false
        }
        // 获取审计类型集合
        let resType = await getNbsjTypeListForMerge()
        console.log(resType)
        this.auditTypeArr = resType.data.tlist

        getList().then((res) => {
          this.projectArr = res.data.tlist
        })

        //获取计划名称集合
        let res = await jhgljhList()
        this.auditPlanArr = res.data.tlist || []
        if (formId) {
          this.loading = true
          this.handleAddFlag = true
          implementPlanDetail({
            id: formId,
          }).then(async (res) => {
            console.log('🚀 ~ res:', res)
            let teams = res.data.data.teams || []
            if (teams && teams.length > 0) {
              teams.forEach((v) => {
                v.leaderName = v.teamLeader.realname
                if (v.teamMembers && v.teamMembers.length > 0) {
                  let arr = []
                  let info = []
                  v.teamMembers.forEach((k) => {
                    arr.push(k.realname)
                    info.push(k.staffid)
                  })
                  v.zyNames = arr.join()
                  v.zystaffids = info.toString()
                } else {
                  v.zyNames = ''
                  v.zystaffids = ''
                }
              })
            }
            this.tableData = teams
            this.tableDataFile = res.data.data.attachments || []
            Object.assign(this.formData, res.data.data)
            // this.formData = res.data.data
            this.formData.planYear = res.data.data.planYear + ''
            // this.formData.auditOrgName = res.data.data
            if (res.data.data.planStarttime && res.data.data.planEndtime) {
              this.formData.dateSection = [
                res.data.data.planStarttime,
                res.data.data.planEndtime,
              ]
            }
            if (res.data.data.planId) {
              //获取计划项目列表
              jhgljhDetail({ jhid: res.data.data.planId }).then((item) => {
                this.setProArr(item.data)
              })
            } else {
              this.proPlanArr = []
            }
          })
          this.loading = false
        }

        this.$forceUpdate()
      },
      close() {
        this.$refs['elForm'].resetFields()
        this.form = this.$options.data().form
        this.clearType = true
        // this.$emit('fetch-data')
        this.$bus.$emit('updateMsg', 0)
      },
      setProArr(item) {
        let arr = this.mxArr
        let arr2 = []
        for (const key in item) {
          if (arr.indexOf(key) > -1 && item[key].length > 0) {
            item[key].map((v) => {
              arr2.push(v)
            })
          }
        }
        this.proPlanArr = arr2
        console.log(this.proPlanArr)
      },
      async getNamelist(val) {
        this.$set(this.formData, 'planId', val[0].jhid)
        this.$set(this.formData, 'planName', val[0].jhmc)
        this.$set(this.formData, 'planProjectName', '')
        this.$set(this.formData, 'planProjectId', '')

        //获取计划项目列表
        jhgljhDetail({ jhid: val[0].jhid }).then((item) => {
          this.setProArr(item.data)
        })
      },
      async handleChange(val) {
        let obj = this.auditPlanArr.find((v) => {
          return v.jhid === val
        })
        this.$set(this.formData, 'planId', val)
        this.$set(this.formData, 'planName', obj.jhmc)
        this.$set(this.formData, 'planProjectName', '')
        this.$set(this.formData, 'planProjectId', '')

        //获取计划项目列表
        jhgljhDetail({ jhid: val }).then((item) => {
          this.setProArr(item.data)
        })
        // //选择所属审计计划获取计划时间
        // let res = await getNbsjAuditPlanDateInfo({ planId: val })
        // let arr = [res.data.planStartDate, res.data.planEndDate]
        // this.$set(this.formData, 'dateSection', arr)
        // //选择所属审计计划获取计划项目列表
        // let resPlanProject = await getPlanProjectListByPlanId({ planId: val })
        // this.resPlanProjectArr = resPlanProject.data.planList
      },
      handlePlan(val) {
        this.$set(this.formData, 'planProjectId', val)
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
          }
        })
      },
      handlePlanName() {
        this.$refs['planNameRef'].showEdit(this.auditPlanArr)
      },
      handleProject() {
        this.$refs['projectRef'].showEdit(this.projectArr)
      },
      handleChoicePlan() {
        if (!this.formData.planName) {
          this.$baseMessage('请选择计划名称', 'error')
        } else {
          this.$refs['planned'].showEdit(this.proPlanArr)
        }
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      auditTemplate(tempType) {
        if (!this.formData.auditOrgName) {
          this.$baseMessage('请选择被审计对象', 'error')
        } else {
          this.$refs['template'].showEdit(tempType, null)
        }
      },
      auditGuidelines(tempType) {
        if (!this.formData.auditOrgName) {
          this.$baseMessage('请选择被审计对象', 'error')
        } else {
          this.$refs['guidelines'].showEdit(tempType, null)
        }
      },
      showGroupLeader(sIndex, type) {
        this.sIndex = sIndex
        this.leaderType = type
        this.$refs['special_select'].showEdit('leader')
      },
      showTeamMembers(sIndex) {
        this.sIndex = sIndex
        this.$refs['special_select2'].showEdit('members')
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
      // 项目小组组长
      selectTeamList(val) {
        console.log(val)
        if (this.leaderType == 'leader') {
          this.tableData[this.sIndex].teamLeaderId = val[0].staffid
          this.tableData[this.sIndex].leaderName = val[0].realname
        } else {
          this.tableData[this.sIndex].fzzstaffid = val[0].staffid
          this.tableData[this.sIndex].fzzname = val[0].realname
        }
      },
      selectTeamList2(val, flagTitle) {
        console.log(val, flagTitle)
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
      },
      async submitForm() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let params = { ...this.formData }
            delete params.attachments
            delete params.teams
            params.planStarttime = params.dateSection[0]
            params.planEndtime = params.dateSection[1]
            delete params.dateSection

            let attIds = ''
            this.tableDataFile.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)

            let flage = false
            if (this.tableData && this.tableData.length) {
              params.teams = []
              this.tableData.map((v) => {
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
                  params.teams.push({
                    teamName: v.teamName,
                    teamLeaderId: v.teamLeaderId,
                    teamMembersIds: v.zystaffids,
                    fzzstaffid: v.fzzstaffid,
                    fzzname: v.fzzname || '',
                  })
                }
              })
            } else {
              flage = false
              return this.$message.error('请添加小组')
            }

            const data = await implementPlanSaveOrUpdate({
              ...params,
              attIds,
            })
            if (data.code == 1) {
              this.handleAddFlag = true
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetch-data')
            } else {
              this.handleAddFlag = false
              this.$baseMessage(data.msg, 'error')
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
            const res = await deleteFile({ attId: row.attid })
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
      // handleSuccess(response, file, fileList) {
      //   if (file.response.result == '200') {
      //     file.createPerson = JSON.parse(
      //       localStorage.getItem('userInfo')
      //     ).projectOrderName
      //     this.tableDataFile.push(file.response.data)
      //     this.fileList = fileList
      //     let fileArr = []
      //     this.fileList.forEach((item) => {
      //       fileArr.push(item.response.data.attid)
      //     })
      //     this.fileArrStr = fileArr.join(',')
      //     this.$baseMessage(file.response.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.response.msg, 'error')
      //   }
      // },
      getChildlist(val) {
        this.$set(this.formData, 'planProjectName', val[0].xmmc)
        this.$set(this.formData, 'planProjectId', val[0].jhmxid)

        // this.planprojectid = val[0].planprojectid
        // this.$set(this.formData, 'targetName', val[0].targetname)
        // this.$set(this.formData, 'planProjectName', val[0].projectname)
        // this.$set(this.formData, 'projectName', val[0].projectname)
        // this.$set(this.formData, 'auditOrgName', val[0].auditOrgName)
        // this.$set(this.formData, 'isWw', val[0].isWw)
      },
      getChildlistObj(val, flag) {
        const ids = val.map((res) => res.id).toString()
        const names = val.map((res) => res.name).toString()
        this.$set(this.formData, 'auditOrgId', ids)
        this.$set(this.formData, 'auditOrgName', names)
        this.$forceUpdate()
      },
      // 项目经理
      getChildlistPro(val) {
        this.staffId = val[0].staffid
        this.$set(this.formData, 'projectOrderId', val[0].staffid)
        this.$set(this.formData, 'projectOrderName', val[0].realname)
      },
      // 工程模板
      getProjectReflist(val) {
        this.$set(this.formData, 'projecttempId', val[0].id)
        this.$set(this.formData, 'projecttempName', val[0].templateName)
      },
      async getChildlistTem(val) {
        console.log(val)
        const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })
        if (res.data.cnt === 0) {
          return this.$message.error('该模板下没有业务单元')
        }
        this.tempId = val[0].templeteId
        this.$set(this.formData, 'tempName', val[0].templeteName)
        this.$set(this.formData, 'tempId', val[0].templeteId)
      },
      async getChildlistGui(val) {
        const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })
        if (res.data.cnt === 0) {
          return this.$message.error('该模板下没有业务单元')
        }
        this.tempZyId = val[0].templeteId
        this.$set(this.formData, 'tbltempletezy', val[0].tempName)
      },
      //提交
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
      danxuanSelect(val) {
        if (this.personType == 'zsname') {
          this.$set(this.formData, `zsstaffid`, val[0].staffid)
          this.$set(this.formData, `zsname`, val[0].realname)
        } else {
          this.$set(this.formData, `fzstaffid`, val[0].staffid)
          this.$set(this.formData, `fzname`, val[0].realname)
        }
      },
      openPerson(type) {
        this.personType = type
        this.$refs.danxuanPerson.showEdit()
      },
      handlePlanRelate() {
        if (this.formData.isgc == 1) {
          this.$refs.gcModal.showEdit()
        } else {
          this.$refs.cwModal.showEdit()
        }
      },
      xmqdModalSelect(val) {
        this.$set(this.formData, `xmdqid`, val[0].xmdqid)
        this.$set(this.formData, `projectName`, val[0].xmname)
        this.$set(this.formData, `qdcode`, val[0].qdcode)
        this.$set(this.formData, 'projectOrderId', val[0].xmjlstaffid)
        this.$set(this.formData, 'projectOrderName', val[0].xmjlname)
        this.$set(this.formData, 'auditOrgId', val[0].borgid)
        this.$set(this.formData, 'auditOrgName', val[0].borgname)
        this.$set(this.formData, `zsstaffid`, val[0].zsstaffid)
        this.$set(this.formData, `zsname`, val[0].zsname)
        this.$set(this.formData, `sjlxName`, val[0].xmtype)
        this.$set(this.formData, `isgc`, val[0].isgc)
      },
      SJTypeSelect(val) {
        console.log(val, 'val')
        this.$set(this.formData, `sjlxName`, val.auditType)
        this.$set(this.formData, `sjlxId`, val.typeId)
      },
      cwModalSelect(val) {
        console.log(val, 'val')
        this.formData.xmapbid = val[0].id
        this.formData.projectName = val[0].name
        this.formData.zsname = val[0].approver
        this.formData.zsstaffid = val[0].approverId
        this.formData.auditOrgName = val[0].auditUnit

        const info = [
          {
            leaderName: val[0].groupLeader,
            teamLeaderId: val[0].groupLeaderId,
            fzzname: val[0].fzzName,
            fzzstaffid: val[0].fzzStafffId,
            zyNames: val[0].assistApprover,
            zystaffids: val[0].assistApproverId,
            teamName: val[0].auditGroup,
            show: true,
          },
        ]
        this.tableData = info
      },
      gcModalSelect(val) {
        console.log(val, 'val')
        this.formData.xmapbid = val[0].id
        this.formData.projectName = val[0].name
        this.formData.zsname = val[0].approver
        this.formData.zsstaffid = val[0].approverId
        this.formData.auditOrgName = val[0].auditUnit
        const info = [
          {
            leaderName: val[0].groupLeader,
            teamLeaderId: val[0].groupLeaderId,
            fzzname: val[0].fzzName,
            fzzstaffid: val[0].fzzStafffId,
            zyNames: val[0].assistApprover,
            zystaffids: val[0].assistApproverId,
            teamName: val[0].auditGroup,
            show: true,
          },
        ]
        this.tableData = info
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
          this.tableDataFile = [...this.tableDataFile, ...file.data]
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
