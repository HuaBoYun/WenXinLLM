<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :modal="false"
      v-if="dialogFormVisible"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
    >
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
            <el-form-item label="被审计对象" prop="orgIdNames">
              <el-input
                v-model="formData.orgIdNames"
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
          <el-col :span="12">
            <el-form-item label="时间安排" prop="sjap">
              <el-input
                v-model="formData.sjap"
                clearable
                placeholder="请输入项目编号"
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="项目概述" prop="xmgs">
              <el-input
                v-model="formData.xmgs"
                type="textarea"
                clearable
                placeholder="请输入项目编号"
                style="width: 100%"
              />
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
                placeholder="请输入协办部门"
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
            <el-form-item label="其他有关内容" prop="proDesc">
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

      <template v-if="!disabled" #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <!-- <el-button
          v-if="
            (formData.examineType == 2 || formData.examineType == 3) &&
            jurisdictionCode == 1
          "
          @click="ymsubmit"
          type="primary"
        >
          提交
        </el-button> -->
      </template>
    </el-dialog>
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

    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>

    <!-- 选择组员子组件 -->
    <special-select2
      ref="special_select2"
      @selectTeamList="selectTeamList2"
    ></special-select2>
    <!-- 选择组长子组件 -->
    <special-select
      ref="special_select"
      @selectTeamList="selectTeamList"
    ></special-select>
    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />

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
  import Auditee from '../../plan/components/childCom/Auditee.vue'
  import auditGuidelines from './formComponents/auditGuidelines.vue'
  import auditTemplate from './formComponents/auditTemplate.vue'
  import plannedProject from './formComponents/plannedProject.vue'
  import projectManage from './formComponents/projectManage.vue'
  import selectTeam from './formComponents/selectTeam.vue'
  import specialSelect from './formComponents/specialSelectTeam.vue'
  import specialSelect2 from './formComponents/specialSelectALL.vue'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
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
      Company: () => import('./selectPerson.vue'),
      Auditee,
      specialSelect,
      specialSelect2,
      CandidateUserSelect,
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
          orgIdNames: '',
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
          examineType: '',
          sjap: '',
          xmgs: '',
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
          orgIdNames: [
            {
              required: true,
              message: '请选择被审计对象',
              trigger: 'blur',
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
    methods: {
      // 文件上传前的钩子，校验文件大小
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      // 选择部门后，回调，把数据保存在formData
      handleDepartmentSelected(node) {
        //保存名称
        this.$set(this.formData, `cospomsordepartmentName`, node.name)
        //保存名称对应的ID
        this.$set(this.formData, `cospomsordepartment`, node.id)
      },
      // 初始化，详情页面
      async showDetail(row, type, isEdit) {
        this.isEdit = isEdit
        if (!this.isEdit) {
          row.projectId = this.getQueryVariable(row.cyurl, 'spid')
          this.$nextTick(() => {
            this.showEdit(row, false, 0, false)
          })
        }
      },
      // 路径获取参数
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
      // 初始化，编辑页面/新建页面
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(row, disabled, planNum) {
        this.dialogFormVisible = true
        this.showSaveButton = true
        this.tableData = []
        this.formData = {}
        this.planTableData = []
        this.projectId = 0
        this.$nextTick(() => {
          this.$refs['elForm'].clearValidate()
        })
        //获取统计类型下拉数据
        getQuestionTypeList().then((res) => {
          this.questionTypeList = res.data.pageInfo.tlist
        })
        let resType = await getNbsjTypeListForMerge()
        this.auditTypeArr = resType.data.pageInfo.tlist
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
            }).then(async (res) => {
              // if (res.data.pj.examineType == 2 || res.data.pj.examineType == 3) {
              //   const res2 = await getFlowTaskInfo({
              //     tableId: 8,
              //     formId: row.projectId,
              //   })
              //   this.jurisdictionCode = res2.data.isFlowInfo
              //   if (res2.data.isFlowInfo) {
              //     this.flowtaskinfoflowid = res2.data.flowId
              //     this.fromId = row.projectId
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
                    examineType: res.data.pj.examineType,
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
                    xmgs: res.data.pj.xmgs,
                    sjap: res.data.pj.sjap,
                    // projectId: row.projectId,
                  }
                  this.tableData.forEach((item) => {
                    item.show = true
                  })
                  this.staffId = res.data.pj.pmId
                  if (res.data.pj.isBmAudit === 0) {
                    this.pdDx = 'yh'
                    this.formData.auditStaffId = res.data.pj.auditStaffId
                    this.formData.orgIdNames = res.data.pj.auditStaffName
                  } else {
                    this.pdDx = 'bm'
                    this.formData.orgIds = res.data.pj.orgIds
                    this.formData.orgIdNames = res.data.pj.orgIdNames
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
              orgIdNames: res.data.pj.pmStaff
                ? res.data.pj.pmStaff.realname
                : '',
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
              xmgs: res.data.pj.xmgs,
              sjap: res.data.pj.sjap,
            }
            this.staffId = res.data.pj.pmId
            if (res.data.pj.isBmAudit === 0) {
              this.pdDx = 'yh'
              this.formData.auditStaffId = res.data.pj.auditStaffId
              this.formData.orgIdNames = res.data.pj.auditStaffName
            } else {
              this.pdDx = 'bm'
              this.formData.orgIds = res.data.pj.orgIds
              this.formData.orgIdNames = res.data.pj.orgIdNames
            }
            let arr = [row.startDate, row.endDate]
            this.formData.dateSection = arr
            this.loading = false
          }
        }
        this.$forceUpdate()
      },
      // 关闭组件，清空form
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.title = '添加'
        this.$refs['elForm'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
        this.clearType = true
        this.$emit('fetch-data')
      },
      // 获取所属审计计划获取计划时间 和 计划项目列表
      async handleChange(val) {
        this.$set(this.formData, 'pprojectName', '')
        this.$set(this.formData, 'orgIdNames', '')
        //所属审计计划获取计划时间
        let res = await getNbsjAuditPlanDateInfo({ planId: val })
        let arr = [res.data.planStartDate, res.data.planEndDate]
        this.$set(this.formData, 'dateSection', arr)
        //选择所属审计计划获取计划项目列表
        let resPlanProject = await getPlanProjectListByPlanId({ planId: val })
        this.resPlanProjectArr = resPlanProject.data.planList
      },
      // 无意义
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
      // 校验是否选择所属审计计划
      handleChoicePlan() {
        if (!this.formData.planname) {
          this.$baseMessage('请选择所属审计计划', 'error')
        } else {
          this.$refs['planned'].showEdit(this.resPlanProjectArr)
        }
      },
      // 唤起 选人组件
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      // 唤起选择 计划组件
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      // 选择审计模板
      auditTemplate(tempType) {
        if (!this.formData.orgIdNames) {
          this.$baseMessage('请选择被审计对象', 'error')
        } else {
          this.$refs['template'].showEdit(tempType, this.formData.auditType)
        }
      },
      // 选择审计标准
      auditGuidelines(tempType) {
        if (!this.formData.orgIdNames) {
          this.$baseMessage('请选择被审计对象', 'error')
        } else {
          this.$refs['guidelines'].showEdit(tempType, this.formData.auditType)
        }
      },
      // 唤起选择组长组件
      showGroupLeader(sIndex) {
        this.sIndex = sIndex
        this.$refs['special_select'].showEdit('leader')
      },
      // 唤起选择组员组件
      showTeamMembers(sIndex) {
        this.sIndex = sIndex
        this.$refs['special_select2'].showEdit('members')
      },
      // 附件下载
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
      // 选择组员后,回调函数,处理数据数组转化为字符串
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
      // 选择组员后,回调函数,处理数据数组转化为字符串
      selectTeamList2(val, flagTitle) {
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
      // 保存接口
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
              orgIdNames: this.formData.orgIdNames,
            }

            if (!this.disabled) {
              params.examineType = 0
            }

            if (this.pdDx === 'bm') {
              params.orgIds = this.formData.orgIds
              params.auditStaffId = undefined
            } else {
              params.auditStaffId = this.formData.auditStaffId
              params.orgIds = undefined
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
      // 重置
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
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
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
      // 删除项目小组
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
      // 删除附件
      async handleDeleteFile(row, index) {
        const res = await deleteProjectFile({ attId: row.attid })
        this.tableDataFile.splice(index, 1)
        this.$message({
          type: 'success',
          message: '删除成功!',
        })
      },
      // 附件上传成功，保存数据
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
      // 选择 被审计对象，回调把数据保存，区分部门和人
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
        this.$set(this.formData, 'orgIdNames', val[0].orgidnames)
        this.$set(this.formData, 'externAlassig', val[0].externalassig)
      },
      // 选择 被审计对象，回调把数据保存，区分部门和人2
      getChildlistObj(val, flag) {
        if (flag == 'right') {
          this.pdDx = 'yh'
          this.$set(this.formData, 'orgIdNames', val[0].realname)
          this.$set(this.formData, 'auditStaffId', val[0].staffid)
        } else {
          this.pdDx = 'bm'
          const names = val.map((res) => res.name).toString()
          const ids = val.map((res) => res.id).toString()
          this.$set(this.formData, 'orgIds', ids)
          this.$set(this.formData, 'orgIdNames', names)
          // this.$set(this.formData, 'auditOrgId', val.id)
          // this.$set(this.formData, 'orgName', val.name)
        }
        this.$forceUpdate()
      },
      // 选择项目经理 回调函数
      getChildlistPro(val) {
        this.staffId = val[0].staffid

        this.$set(this.formData, 'realName', val[0].realname)
      },
      // 选择 审计模板子组件 回调函数
      async getChildlistTem(val) {
        const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })
        if (res.data.cnt === 0) {
          return this.$message.error('该模板下没有业务单元')
        }
        this.tempId = val[0].templeteId
        this.$set(this.formData, 'templeteName', val[0].templeteName)
      },
      // 选择 审计模板子组件 回调函数
      async getChildlistGui(val) {
        const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })
        if (res.data.cnt === 0) {
          return this.$message.error('该模板下没有业务单元')
        }
        this.tempZyId = val[0].templeteId
        this.$set(this.formData, 'tbltempletezy', val[0].templeteName)
      },
      // 日期格式化
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
      // 保存计划
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
              orgIdNames: this.formData.orgIdNames,
              xmgs: this.formData.xmgs,
              sjap: this.formData.sjap,
            }

            if (this.pdDx === 'bm') {
              params.orgIds = this.formData.orgIds
              params.auditStaffId = undefined
            } else {
              params.auditStaffId = this.formData.auditStaffId
              params.orgIds = undefined
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
      // 选择计划
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
      //关闭 流程提交页面
      currentClose() {
        this.visible = false
        this.form = {}
      },
      // 唤起 候选人组件
      handlefzChange(e) {
        this.$refs['fzform'].clearValidate()
      },
      // 回显数据
      handleCandSelect1(index, value) {
        this.formData2.transferStaffName = value
      },
      // 回显数据
      handleCandSelect(index, value) {
        // this.$set(this.formData3[index], 'transferStaffId', value)
        this.formData3[index].transferStaffId = value
      },
      // 流程提交函数，有分支的情况
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
        // } else {
        //
        //   return false
        // }
        // })
      },
      // 数据处理
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
      // 重置流程提交页面的数据
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
      // 流程提交，有分支，有候选人的情况
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
      // 关闭流程提交页面
      close1() {
        this.visible1 = false
        this.resetINfo()
      },
      // 关闭流程提交页面
      close2() {
        this.visible2 = false
        this.resetINfo()
      },
      // 流程提交，有候选人的情况
      async save2() {
        if (!this.formData2.transferStaffName) {
          this.$message.error('请选择候选人')
          return
        }

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
