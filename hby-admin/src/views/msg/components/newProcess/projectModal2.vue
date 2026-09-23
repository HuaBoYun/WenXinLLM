<template>
  <div>
    <!-- <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
    > -->
    <el-row :gutter="15" v-loading="loading">
      <el-form
        ref="elForm"
        :class="{ disabled: disabled }"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="medium"
        style="display: flex; flex-wrap: wrap"
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
              style="width: 266px"
              :disabled="disabled"
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
              style="width: 266px"
              :disabled="disabled"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || disabled"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="12">
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
        </el-col>
        <el-col :span="12">
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
          <el-form-item label="工作目标" prop="targetName">
            <el-input
              v-model="formData.targetName"
              :disabled="disabled"
              clearable
              placeholder="请输入工作目标"
              style="width: 266px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" prop="orgIdNames">
            <el-input
              v-model="formData.orgIdNames"
              clearable
              placeholder="请选择被审计单位"
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
          <el-form-item label="审计类型" prop="auditType">
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
            <el-input
              v-model="formData.projectSource"
              :disabled="disabled"
              clearable
              placeholder="请输入项目来源"
              style="width: 266px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目主审" prop="realName">
            <el-input
              v-model="formData.realName"
              clearable
              placeholder="请选择项目主审"
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
          <el-form-item label="计划审计时间" prop="planStartDate">
            <el-date-picker
              v-model="formData.planStartDate"
              clearable
              placeholder="选择日期"
              format="yyyy-MM-dd"
              style="width: 266px"
              type="date"
              value-format="yyyy-MM-dd"
              :disabled="disabled"
              @change="handleStartDateChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划验收时间" prop="planEndDate">
            <el-date-picker
              v-model="formData.planEndDate"
              clearable
              placeholder="选择日期"
              format="yyyy-MM-dd"
              style="width: 266px"
              type="date"
              value-format="yyyy-MM-dd"
              :disabled="disabled"
            />
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
        <el-col :span="12">
          <el-form-item label="批复总投资(经费:万元)" prop="field108">
            <el-input-number
              v-model="formData.costs"
              clearable
              placeholder="请输入批复总投资(经费:万元)"
              :disabled="disabled"
              style="width: 266px"
              :min="0"
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
          <el-form-item label="是否外委" prop="externAlassig">
            <el-select
              v-model="formData.externAlassig"
              placeholder="请选择"
              style="width: 266px"
              :disabled="disabled"
            >
              <el-option
                v-for="item in externAlassigArr"
                :disabled="disabled"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目单位" prop="projectorgname">
            <el-input
              v-model="formData.projectorgname"
              clearable
              placeholder="请选择项目单位"
              style="width: 266px"
              disabled
            />
            <el-button
              @click="$refs.audiTree.showEdit()"
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
          <el-form-item label="批复的项目起止年限" prop="appproyear">
            <el-date-picker
              v-model="formData.appproyear"
              clearable
              placeholder="选择日期"
              style="width: 266px"
              type="monthrange"
              value-format="yyyy-MM"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际的起止年限" prop="actproyear">
            <el-date-picker
              v-model="formData.actproyear"
              clearable
              placeholder="选择日期"
              style="width: 266px"
              type="monthrange"
              value-format="yyyy-MM"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目主管部门" prop="projectmgdeptname">
            <el-input
              v-model="formData.projectmgdeptname"
              clearable
              placeholder="请选择项目主管部门"
              style="width: 266px"
              disabled
            />
            <el-button
              @click="$refs.department.show()"
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
          <el-form-item label="项目单位地址" prop="projectorgaddress">
            <el-input
              v-model="formData.projectorgaddress"
              :disabled="disabled"
              clearable
              placeholder="请输入项目单位地址"
              style="width: 266px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目联系人" prop="projectlinkman">
            <el-input
              v-model="formData.projectlinkman"
              :disabled="disabled"
              clearable
              placeholder="请输入项目联系人"
              style="width: 266px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="projectlinktel">
            <el-input
              v-model="formData.projectlinktel"
              :disabled="disabled"
              clearable
              placeholder="请输入联系电话"
              style="width: 266px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="projecttype">
            <el-input
              v-model="formData.projecttype"
              :disabled="disabled"
              clearable
              placeholder="请输入项目类型"
              style="width: 266px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计目标和范围" prop="purpose">
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
          <el-form-item label="审计内容和重点" prop="scopes">
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
          <el-form-item label="审计程序和方法" prop="pursuant">
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
          <el-form-item label="对专家和外部审计结果的利用" prop="comments">
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
          <el-form-item label="备注" prop="proDesc">
            <el-input
              type="textarea"
              v-model="formData.proDesc"
              :disabled="disabled"
              clearable
              placeholder="请输入备注"
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
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handlePreviewFile(scope.row)"
                >
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

    <div style="text-align: right" v-if="!disabled">
      <el-button @click="submitForm" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>
    <!-- 计划项目子组件 -->
    <planned-project
      ref="planned"
      @plannedList="getChildlist"
    ></planned-project>

    <CompanyTreeModel
      ref="companyTreeModel"
      @selected="selectCompany"
      :title="'被审计单位'"
    />
    <!-- 项目主审子组件 -->
    <project-manage
      @selectTeamList="getChildlistPro"
      :defaultExpandedH="3"
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
    <select-team
      ref="select"
      @selectTeamList="selectTeamList"
      :defaultExpandedH="3"
    ></select-team>
    <AuditeeDialog ref="auditee" @projectManage="getAuditee" />
    <company-select-modal ref="audiTree" @select="handleCompanyTreeSelected" />
    <!-- 选择项目主管部门弹窗 -->
    <DepartmentOptions ref="department" @submit="handleDepartmentSelected" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { download } from '@/api/audit/implement'
  import {
    deleteProjectFile,
    getNbsjAuditPlanDateInfo,
    getNbsjAuditPlanListForMerge,
    getNbsjTypeListForMerge,
    getPlanProjectListByPlanId,
    getProjectDetail,
    getProjectPjteamList,
    projectAdd,
    projectFileList,
    proPjteamDel,
    seachTeamplateCnt,
    getAutoCodeByXmgl,
  } from '@/api/audit/project'
  import { getOrgTreeByDepartment } from '@/api/common'
  import Auditee from '@/views/audit/plan/components/childCom/Auditee.vue'
  import auditGuidelines from '@/views/audit/project/components/formComponents/auditGuidelines.vue'
  import auditTemplate from '@/views/audit/project/components/formComponents/auditTemplate.vue'
  import plannedProject from '@/views/audit/project/components/formComponents/plannedProject.vue'
  import projectManage from '@/views/audit/plan/components/selectTeam.vue'
  import selectTeam from '@/views/audit/plan/components/selectTeam.vue'
  import CompanyTreeModel from '@/components/CompanyTreeModel/index.vue'
  import AuditeeDialog from '@/views/audit/project/components/formComponents/AuditeeDialog.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import CompanySelectModal from '@/components/departments.vue'
  import DepartmentOptions from '@/components/departmentSelect.vue'
  import { isPhone } from '@/utils/validate'
  import { getSPMJ } from '@/api/setting/mjsz'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const validatePhone = (rule, value, callback) => {
    if (!isPhone(value)) {
      callback(new Error(this.translateTitle('请输入正确的手机号')))
    } else {
      callback()
    }
  }
  export default {
    name: 'MaintainEdit',
    components: {
      plannedProject,
      projectManage,
      auditTemplate,
      auditGuidelines,
      selectTeam,
      Company: () => import('@/components/Company.vue'),
      Auditee,
      CompanyTreeModel,
      AuditeeDialog,
      Resubmit,
      CompanySelectModal,
      DepartmentOptions,
      ZXPerson,
    },
    data() {
      return {
        getOrgTreeByDepartment,
        loading: false,
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        disabled: false,
        plannameFlag: false,
        tempId: 0,
        pdDx: '',
        proTemPid: 0,
        leaderId: '',
        zyStaffids: '',
        staffId: '',
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
          orgIdNames: '',
          orgIds: '',
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
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },
        planId: '',
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
          pprojectName: [
            {
              message: '请选择计划项目',
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
          orgIdNames: [
            {
              required: true,
              message: '请选择被审计单位',
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
              message: '请选择选择项目主审',
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
          // tbltempletezy: [
          //   {
          //     required: true,
          //     message: '请选择审计指引',
          //     trigger: 'change',
          //   },
          // ],
          proSjfs: [
            {
              required: true,
              message: '请选择审计方式',
              trigger: 'change',
            },
          ],
          externAlassig: [
            {
              required: false,
              message: '请选择是否外委',
              trigger: 'change',
            },
          ],
          planStartDate: [
            {
              required: true,
              message: '请选择计划审计时间',
              trigger: 'change',
            },
          ],
          planEndDate: [
            {
              required: true,
              message: '请选择计划验收时间',
              trigger: 'change',
            },
            {
              validator: (rule, value, callback) => {
                if (value && this.formData.planStartDate) {
                  if (
                    new Date(value) <= new Date(this.formData.planStartDate)
                  ) {
                    callback(new Error('计划验收时间必须大于计划审计时间'))
                  } else {
                    callback()
                  }
                } else {
                  callback()
                }
              },
              trigger: 'change',
            },
          ],
          // projectlinktel: [
          //   {
          //     pattern: /^1[3-9]\d{9}$/,
          //     message: '手机号格式错误',
          //     trigger: 'blur',
          //   },
          // ],
        },
        title: '',
        dialogFormVisible: false,
        tableData: [],
        tableDataFile: [],
        sIndex: 0,
        resPlanProjectArr: [],
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        btnLoading: false,
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
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
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      async showDetail(row, type, isEdit) {
        console.log('row, type, isEdit', row, type, isEdit)
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
        formId,
        flowtaskinfoflowid,
        ymFromId,
        status,
        flowType
      ) {
        // 拿到类型传给getMJData获取审批的密级的下拉数据
        if (flowType) {
          this.getMJData(flowType)
        }
        this.dialogFormVisible = true
        // 流程相关
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status

        this.tableData = []
        this.formData = {}
        this.$nextTick(() => {
          this.$refs['elForm'].clearValidate()
        })
        let resType = await getNbsjTypeListForMerge()
        this.auditTypeArr = resType.data
        console.log('🚀 ~ this.auditTypeArr:', this.auditTypeArr)
        if (!row) {
          this.title = '添加'
          this.disabled = false
          this.tableData = []
          // this.formData = {
          //   // projectCode: planNumRes,     // 自动生成的项目编号
          //   auditType: this.auditTypeArr[0] && this.auditTypeArr[0].auditType,
          // }
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
                // 保存审批用的密级id
                if (res.data.pj.tblnbsjPlan.secrectLevelId) {
                  localStorage.setItem(
                    'SPsecrectLevelId',
                    res.data.pj.tblnbsjPlan.secrectLevelId
                  )
                }
                this.planId = res.data.pj.tblnbsjPlan.planid
                getPlanProjectListByPlanId({ planId: this.planId }).then(
                  (resPlanProject) => {
                    this.resPlanProjectArr = resPlanProject.data.planList

                    getProjectPjteamList({
                      projectid: row.projectId,
                    }).then((resTeam) => {
                      if (resTeam && resTeam.data.listTeam.length) {
                        let listTeam = resTeam.data.listTeam
                        listTeam.forEach((k) => {
                          let a1 = [],
                            a2 = []
                          k.teamStaffs.forEach((v) => {
                            if (v.stafftype == 0) {
                              k.leaderId = v.staffid
                              k.leaderName = v.staff.realname
                            } else {
                              a1.push(v.staffid)
                              a2.push(v.staff.realname)
                            }
                          })
                          k.zystaffids = a1.join()
                          k.zyNames = a2.join()
                        })
                        this.tableData = listTeam
                      }
                    })
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
                        planname: res.data.pj.tblnbsjPlan.planid,
                        pprojectName: res.data.pj.pprojectName,
                        projectCode: res.data.pj.projectCode,
                        prjoectName: res.data.pj.prjoectName,
                        targetName: res.data.pj.targetName,
                        realName: res.data.pj.pmStaff.realname, //需要接口重置
                        planYear: res.data.pj.planYear,
                        auditType: res.data.pj.auditType,
                        projectSource: res.data.pj.projectSource,
                        planEndDate: res.data.pj.endDate,
                        planStartDate: res.data.pj.startDate,
                        templeteName: res.data.pj.tbltemplete
                          ? res.data.pj.tbltemplete.templeteName
                          : null, //需要接口重置
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
                        // projectId: row.projectId,
                        orgIds: res.data.pj.orgIds,
                        orgIdNames: res.data.pj.orgIdNames
                          ? res.data.pj.orgIdNames
                          : '',
                        projectorgid: res.data.pj.projectorgid,
                        projectorgname: res.data.pj.projectorgname,
                        // appproyear: res.data.pj.appproyear,
                        // actproyear: res.data.pj.actproyear,
                        projectmgdeptid: res.data.pj.projectmgdeptid,
                        projectmgdeptname: res.data.pj.projectmgdeptname,
                        projectorgaddress: res.data.pj.projectorgaddress,
                        projectlinkman: res.data.pj.projectlinkman,
                        projectlinktel: res.data.pj.projectlinktel,
                        projecttype: res.data.pj.projecttype,
                        secretlevel: res.data.pj.secretlevel,
                        secrectLevelId: res.data.pj.secrectLevelId,
                        staffScopeIds: res.data.pj.staffScopeIds,
                        staffScopeNames: res.data.pj.staffScopeNames,
                        appproyear: [
                          res.data.pj.appproyearstart,
                          res.data.pj.appproyearend,
                        ],
                        actproyear: [
                          res.data.pj.actproyearstart,
                          res.data.pj.actproyearend,
                        ],
                      }
                      this.tableData.forEach((item) => {
                        item.show = true
                      })
                      this.staffId = res.data.pj.pmId
                      // if (res.data.pj.isBmAudit === 0) {
                      //   this.pdDx = 'yh'
                      //   this.formData.auditStaffId = res.data.pj.auditStaffId
                      //   this.formData.orgName = res.data.pj.auditStaffName
                      // } else {
                      //   this.pdDx = 'bm'
                      // }
                    })
                    this.loading = false
                  }
                )
              }
            })
          } else {
            this.title = '查看'
            this.disabled = true
            this.loading = true
            // 000000
            let resFile = await projectFileList({
              projectId: row.projectId,
            })
            this.tableDataFile = resFile.data.data
            console.log(resFile, 888777)
            let res = await getProjectDetail({
              projectid: row.projectId,
            })
            let resTeam = await getProjectPjteamList({
              projectid: row.projectId,
            })
            if (resTeam && resTeam.data.listTeam.length) {
              let listTeam = resTeam.data.listTeam
              listTeam.forEach((k) => {
                let a1 = [],
                  a2 = []
                k.teamStaffs.forEach((v) => {
                  if (v.stafftype == 0) {
                    k.leaderId = v.staffid
                    k.leaderName = v.staff.realname
                  } else {
                    a1.push(v.staffid)
                    a2.push(v.staff.realname)
                  }
                })
                k.zystaffids = a1.join()
                k.zyNames = a2.join()
              })
              this.tableData = listTeam
            }
            // this.$set(this.formData, 'targetName', val[0].targetname)
            // this.$set(this.formData, 'pprojectName', val[0].projectname)
            // this.$set(this.formData, 'prjoectName', val[0].projectname)
            // this.$set(this.formData, 'orgName', val[0].orgidnames)
            // this.$set(this.formData, 'externAlassig', val[0].externalassig)

            // 保存审批用的密级id
            if (res.data.pj.tblnbsjPlan.secrectLevelId) {
              localStorage.setItem(
                'SPsecrectLevelId',
                res.data.pj.tblnbsjPlan.secrectLevelId
              )
            }
            this.formData = {
              planname: res.data.pj.tblnbsjPlan.planname,
              pprojectName: res.data.pj.pprojectName,
              projectCode: res.data.pj.projectCode,
              prjoectName: res.data.pj.prjoectName,
              targetName: res.data.pj.targetName,
              realName: res.data.pj.pmStaff.realname, //需要接口重置
              // orgName: res.data.pj.pmStaff.realname,
              planYear: res.data.pj.planYear,
              auditType: res.data.pj.auditType,
              projectSource: res.data.pj.projectSource,
              dateSection: [],
              planStartDate: res.data.pj.startDate,
              planEndDate: res.data.pj.endDate,
              templeteName: res.data.pj.tbltemplete.templeteName, //需要接口重置
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
              projectorgid: res.data.pj.projectorgid,
              projectorgname: res.data.pj.projectorgname,
              // appproyear: res.data.pj.appproyear,
              // actproyear: res.data.pj.actproyear,
              projectmgdeptid: res.data.pj.projectmgdeptid,
              projectmgdeptname: res.data.pj.projectmgdeptname,
              projectorgaddress: res.data.pj.projectorgaddress,
              projectlinkman: res.data.pj.projectlinkman,
              projectlinktel: res.data.pj.projectlinktel,
              projecttype: res.data.pj.projecttype,
              secretlevel: res.data.pj.secretlevel,
              secrectLevelId: res.data.pj.secrectLevelId,
              staffScopeIds: res.data.pj.staffScopeIds,
              staffScopeNames: res.data.pj.staffScopeNames,
              appproyear: [
                res.data.pj.appproyearstart,
                res.data.pj.appproyearend,
              ],
              actproyear: [
                res.data.pj.actproyearstart,
                res.data.pj.actproyearend,
              ],
            }
            this.staffId = res.data.pj.pmId

            this.formData.orgIds = res.data.pj.orgIds
            this.formData.orgIdNames = res.data.pj.orgIdNames || ''

            this.loading = false
          }
        }

        this.$forceUpdate()
      },
      close() {
        this.$refs['elForm'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
        this.$emit('fetch-data')
        this.projectId = 0
        this.$bus.$emit('updateMsg', 0)
      },
      async getCode(planid) {
        const res = await getAutoCodeByXmgl({ planid })
        this.formData.projectCode = res.data.autoCode

        this.$forceUpdate()
      },
      async handleChange(val) {
        let tV = this.auditPlanArr.filter((item) => {
          if (item.planid == val) {
            return item
          }
        })
        console.log('zz', tV)
        this.getCode(val)
        this.$set(this.formData, 'pprojectName', '')
        this.$set(this.formData, 'orgIdNames', '')
        this.$set(this.formData, 'planYear', tV[0].palnyear)

        //选择所属审计计划获取计划时间
        let res = await getNbsjAuditPlanDateInfo({ planId: val })
        this.$set(this.formData, 'planStartDate', res.data.planStartDate)
        this.$set(this.formData, 'planEndDate', res.data.planEndDate)
        //选择所属审计计划获取计划项目列表
        let resPlanProject = await getPlanProjectListByPlanId({ planId: val })
        this.resPlanProjectArr = resPlanProject.data.planList
        console.log(this.resPlanProjectArr, 99)
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
      handleChoicePlan() {
        if (!this.formData.planname) {
          this.$baseMessage('请选择所属审计计划', 'error')
        } else {
          this.$refs['planned'].showEdit(this.resPlanProjectArr)
        }
      },
      handleObject() {
        // this.$refs['audiTree'].showEdit()
        // this.$refs['companyTreeModel'].show(true)
        this.$refs['auditee'].showEdit()
      },
      projectManager() {
        this.$refs['manage'].showEdit('leader')
      },
      auditTemplate(tempType) {
        if (!this.formData.orgIdNames) {
          this.$baseMessage('请选择被审计单位', 'error')
        } else {
          this.$refs['template'].showEdit(tempType, this.formData.auditType)
        }
      },
      auditGuidelines(tempType) {
        if (!this.formData.orgIdNames) {
          this.$baseMessage('请选择被审计单位', 'error')
        } else {
          this.$refs['guidelines'].showEdit(tempType, this.formData.auditType)
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
      selectTeamList(val, flagTitle) {
        console.log(val)
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
      async submitForm() {
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
              planStartDate: this.formData.planStartDate,
              planEndDate: this.formData.planEndDate,
              tempId: this.tempId,
              protempid: this.tempId,
              costs: this.formData.costs,
              attids: (this.tableDataFile || []).map((x) => x.attid).join(','),
              tempzyId: this.tempZyId,
              proSjfs: this.formData.proSjfs,
              externAlassig: this.formData.externAlassig,
              purpose: this.formData.purpose,
              scopes: this.formData.scopes,
              pursuant: this.formData.pursuant,
              comments: this.formData.comments,
              proDesc: this.formData.proDesc,
              orgIds: this.formData.orgIds,
              orgIdNames: this.formData.orgIdNames,

              projectorgid: this.formData.projectorgid,
              projectorgname: this.formData.projectorgname,
              // appproyear: this.formData.appproyear,
              // actproyear: this.formData.actproyear,
              projectmgdeptid: this.formData.projectmgdeptid,
              projectmgdeptname: this.formData.projectmgdeptname,
              projectorgaddress: this.formData.projectorgaddress,
              projectlinkman: this.formData.projectlinkman,
              projectlinktel: this.formData.projectlinktel,
              projecttype: this.formData.projecttype,
              secretlevel: this.formData.secretlevel,
              secrectLevelId: this.formData.secrectLevelId,
              staffScopeIds: this.formData.staffScopeIds,
              staffScopeNames: this.formData.staffScopeNames,
              actproyearstart: this.formData.actproyear[0],
              actproyearend: this.formData.actproyear[1],
              appproyearstart: this.formData.appproyear[0],
              appproyearend: this.formData.appproyear[1],
            }

            // if (this.pdDx === 'bm') {
            //   params.auditOrgId = this.formData.auditOrgId
            //   params.auditStaffId = undefined
            // } else {
            //   params.auditStaffId = this.formData.auditStaffId
            //   params.auditOrgId = undefined
            // }
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
      },
      async handleDeleteFile(row, index) {
        const res = await deleteProjectFile({ attId: row.attid })
        this.tableDataFile.splice(index, 1)
        this.$message({
          type: 'success',
          message: '删除成功!',
        })
      },

      getChildlist(val) {
        console.log(val, 9999)
        // auditOrgId
        let pdDx = val[0].bsjtype
        if (pdDx === 'bm') {
          this.$set(this.formData, 'orgIds', val[0].orgids)
          this.pdDx = 'bm'
        } else {
          this.$set(this.formData, 'orgIds', val[0].orgids)
          this.pdDx = 'yh'
        }

        this.planprojectid = val[0].planprojectid
        this.$set(this.formData, 'targetName', val[0].targetname)
        this.$set(this.formData, 'pprojectName', val[0].projectname)
        this.$set(this.formData, 'prjoectName', val[0].projectname)
        this.$set(this.formData, 'orgIdNames', val[0].orgidnames)
        // this.$set(this.formData, 'orgIds', val[0].orgidnames)
        this.$set(this.formData, 'externAlassig', val[0].externalassig)
      },
      getChildlistObj(val, flag) {
        if (flag == 'right') {
          this.pdDx = 'yh'
          this.$set(this.formData, 'orgIdNames', val[0].realname)
          this.$set(this.formData, 'orgIds', val[0].staffid)
        } else {
          this.pdDx = 'bm'
          this.$set(this.formData, 'orgIds', val.id)
          this.$set(this.formData, 'orgIdNames', val.name)
        }
        this.$forceUpdate()
        console.log('this.formData', this.formData)
      },
      selectCompany(val) {
        console.log('🚀 ~ selectCompany ~ val:', val)
        let orgidnames = val.map((item) => item.name).join(',')
        let orgids = val.map((item) => item.id).join(',')
        this.formData.orgIdNames = orgidnames
        // this.$set(this.formData, 'orgName', orgidnames)
        this.$set(this.formData, 'orgIds', orgids)
      },
      //被审计对象
      getAuditee(val, data) {
        if (data == 'left') {
          const names = val.map((res) => res.label).toString()
          const ids = val.map((res) => res.id).toString()
          this.$set(this.formData, 'orgIdNames', names)
          this.$set(this.formData, 'orgIds', ids)
        } else {
          this.$set(this.formData, 'orgIdNames', val[0].realname)
          this.$set(this.formData, 'orgIds', val[0].staffid)
        }
      },
      getChildlistPro(val) {
        console.log('val', val)
        this.staffId = val[0].staffid
        this.$set(this.formData, 'realName', val[0].realname)
      },
      async getChildlistTem(val) {
        const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })
        console.log(res)
        if (res.data.cnt === 0) {
          return this.$message.error('该模板下没有业务单元')
        }
        this.tempId = val[0].templeteId
        this.$set(this.formData, 'templeteName', val[0].templeteName)
      },
      async getChildlistGui(val) {
        const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })
        console.log(res)
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
      handleStartDateChange() {
        // 当计划审计时间改变时，重新验证计划验收时间
        if (this.formData.planEndDate) {
          this.$refs['elForm'].validateField('planEndDate')
        }
      },
      async ymsubmit() {
        try {
          this.btnLoading = true
          this.$refs['elForm'].validate(async (valid) => {
            if (valid) {
              this.loading = true
              console.log('this.loading', this.loading)
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      //选择项目单位
      handleCompanyTreeSelected(val) {
        const names = val.map((item) => item.name).join(',')
        const ids = val.map((item) => item.id).join(',')
        this.$set(this.formData, 'projectorgname', names)
        this.$set(this.formData, 'projectorgid', ids)
      },
      // 选择部门
      handleDepartmentSelected(node) {
        this.$set(this.formData, `projectmgdeptname`, node.label)
        this.$set(this.formData, `projectmgdeptid`, node.id)
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
          this.tableDataFile = [...this.tableDataFile, ...file.data]
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
<style scoped lang="scss">
  ::v-deep .el-form.disabled {
    input {
      border: 0;
      background-color: #ffffff;
    }
  }
</style>
