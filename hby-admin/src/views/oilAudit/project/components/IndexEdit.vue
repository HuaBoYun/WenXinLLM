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
        <el-col :span="24" style="text-align: right">
          <el-button type="text" @click="openSJDX">审计对象指引</el-button>
        </el-col>
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
              <el-radio-group v-model="formData.isjy">
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
          <!-- <el-col :span="12">
            <el-form-item label="审计类型" prop="sjlxId">
              <el-select
                v-model="formData.sjlxId"
                placeholder="请选择"
                :disabled="disabled"
                style="width: 266px"
                @change="handleSjlx"
              >
                <el-option
                  v-for="item in auditTypeArr"
                  :key="item.typeId"
                  :label="item.auditType"
                  :value="item.typeId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
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
          <el-col :span="12">
            <el-form-item
              label="实施时间"
              prop="implementtime"
              v-if="title == '查看'"
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
          </el-col>
          <el-col :span="12">
            <el-form-item label="延期时间" prop="yqdate" v-if="title == '查看'">
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
          <!-- <el-col :span="12" v-if="disabled">
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
                @input="validateInput"
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
            <el-table :data="fileList">
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
                  <el-button type="text" @click="handlePreviewFile(scope.row)">
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

      <div v-if="!disabled" slot="footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button
          @click="handleApproval"
          type="primary"
          :disabled="!this.editId"
        >
          提交审批
        </el-button>
      </div>
    </el-dialog>
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
    <ProcessList ref="process" @fetchData="close" />
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
    <!-- 审计对象指引 -->
    <sjdxModal ref="sjdx" />
  </div>
</template>

<script>
  import { download, deleteFile } from '@/oapi/audit/report'
  import { jhgljhList, jhgljhDetail } from '@/api/monitor/question'
  import { getList } from '@/oapi/baseConfig/gcsjmb'
  import {
    implementPlanDetail,
    implementPlanSaveOrUpdate,
    getNbsjAuditPlanDateInfo,
    getPlanProjectListByPlanId,
    projectAdd,
    proPjteamDel,
    seachTeamplateCnt,
    getNbsjTypeListForMerge,
    getQuestionTypeList,
  } from '@/oapi/audit/project'
  import store from '@/store'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import Auditee from '../../plan/components/childCom/Auditee.vue'
  import auditGuidelines from './formComponents/auditGuidelines.vue'
  import auditTemplate from './formComponents/auditTemplate.vue'
  import plannedProject from './formComponents/plannedProject.vue'
  import planNamePro from './formComponents/planNamePro.vue'
  import projectTemplate from './formComponents/projectTemplate.vue'
  import projectManage from '@/components/danxuanPerson.vue'
  import selectTeam from './formComponents/selectTeam.vue'
  import specialSelect from '@/components/danxuanPerson.vue'
  import specialSelect2 from '@/components/selectPerson.vue'
  import danxuanPerson from '@/components/danxuanPerson.vue'
  import xmqdModal from './formComponents/xmqdModal.vue'
  import cwModal from './formComponents/cwModal.vue'
  import gcModal from './formComponents/gcModal.vue'
  import SJType from '@/views/oilAudit/project/components/tree.vue'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/oapi/contract/manage'
  import { getFaqiInfo } from '@/oapi/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  const { baseURL } = require('@/config')
  import sjdxModal from '@/components/sjdx/sjdxModal.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
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
      Company: () => import('@/components/departments.vue'),
      Auditee,
      specialSelect,
      specialSelect2,
      CandidateUserSelect,
      planNamePro,
      projectTemplate,
      danxuanPerson,
      xmqdModal,
      SJType,
      cwModal,
      gcModal,
      sjdxModal,
      ProcessList,
    },
    data() {
      return {
        loading: false,
        // baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
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
          projectName: '',
          qdcode: '',
          projectType: '',
          auditOrgName: '',
          zsname: '',
          fzname: '',
          isjy: '否',
          planYear: String(new Date().getFullYear()),
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
          teamName: '',
          planName: undefined,
          dateSection: [],
          implementtime: '',
          yqdate: '',
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
          isgc: [
            {
              required: true,
              message: '请选择是否工程项目',
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
        fileList: [],
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
        leaderType: '',
        mxArr: ['mx11', 'mx12', 'mx21', 'mx22', 'mx23', 'mx31', 'mx32'],
        editId: '',
      }
    },
    methods: {
      validateInput(value) {
        // 只允许输入数字和小数点
        this.formData.costEstimation = value.replace(/[^\d.]/g, '')
        this.formData.costEstimation = this.formData.costEstimation.replace(
          /^(\d*\.\d{0,2}).*/,
          '$1'
        )
      },
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
      async showEdit(row, disabled, planNum, type) {
        this.dialogFormVisible = true
        this.showSaveButton = true
        this.tableData = []
        // this.formData = {}
        this.planTableData = []
        this.projectId = 0
        this.$nextTick(() => {
          this.$refs['elForm'].clearValidate()
        })
        this.formData.isgc = type == '基建' ? '1' : '0'
        this.formData.zykstype = type
        //获取统计类型下拉数据
        // getQuestionTypeList().then((res) => {
        //   this.questionTypeList = res.data.pageInfo.tlist
        // })
        // 获取审计类型集合
        let resType = await getNbsjTypeListForMerge()
        console.log(resType, 'resType')
        this.auditTypeArr = resType.data.tlist

        getList().then((res) => {
          this.projectArr = res.data.pageInfo.tlist
        })

        //获取计划名称集合
        let res = await jhgljhList()
        this.auditPlanArr = res.data.tlist || []

        if (!row) {
          this.title = '添加'
          this.disabled = false
          this.tableData = []
          // let planNumRes =
          //   planNum.split('-')[0] + '-' + (parseInt(planNum.split('-')[1]) + 1)
          // this.formData = {
          //   // projectCode: planNumRes,     // 自动生成的项目编号
          //   auditType: this.auditTypeArr[0].auditType,
          // }
          this.fileList = []
          //获取审计类型集合
        } else {
          this.loading = true
          if (!disabled) {
            this.title = '编辑'
            this.disabled = false
            this.handleAddFlag = true
            // this.projectId = row.projectId
            this.editId = row.id
            implementPlanDetail({
              id: row.id,
            }).then(async (res) => {
              console.log(res)
              let teams = res.data.data ? res.data.data.teams || [] : []
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
              console.log(teams, 'teams')
              this.fileList = res.data.data
                ? res.data.data.attachments || []
                : []
              Object.assign(this.formData, res.data.data)
              // this.formData = res.data.data
              this.formData.planYear = res.data.data
                ? res.data.data.planYear + '' || ''
                : ''
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

              // if (res.data.pj.tbltemplete) {
              //   this.showSaveButton = false
              // } else {
              //   this.showSaveButton = true
              // }
            })
          } else {
            this.title = '查看'
            this.disabled = true
            this.loading = true
            this.$nextTick(() => {
              // this.$refs['elForm'].clearValidate()
              this.rules = {}
            })

            implementPlanDetail({
              id: row.id,
            }).then(async (res) => {
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
              this.fileList = res.data.data.attachments || []
              Object.assign(this.formData, res.data.data)
              // this.formData = res.data.data
              this.formData.planYear = res.data.data.planYear + ''
              this.formData.dateSection = [
                res.data.data.planStarttime,
                res.data.data.planEndtime,
              ]
              if (res.data.data.planId) {
                //获取计划项目列表
                jhgljhDetail({ jhid: res.data.data.planId }).then((item) => {
                  this.setProArr(item.data)
                })
              } else {
                this.proPlanArr = []
              }
            })
          }
          this.loading = false
        }
        this.$forceUpdate()
      },
      close() {
        this.title = '添加'
        this.$refs['elForm'].resetFields()
        this.form = this.$options.data().form
        this.formData.projectName = ''
        this.formData.qdcode = ''
        this.formData.projectType = ''
        this.formData.auditOrgName = ''
        this.formData.zsname = ''
        this.formData.fzname = ''
        this.formData.isjy = '否'
        this.formData.planYear = String(new Date().getFullYear())
        this.formData.sjlxId = ''
        this.formData.sjlxName = ''
        this.formData.cntType = ''
        this.formData.planTime = ''
        this.formData.projectSummary = ''
        this.formData.projectOrderName = ''
        this.formData.dateSection = ''
        this.formData.auditMethod = ''
        this.formData.costEstimation = ''
        this.formData.isWw = 0
        this.formData.isgc = ''
        this.formData.tempName = ''
        this.formData.projecttempName = ''
        this.formData.implementType = ''
        this.formData.auditRequirement = ''
        this.formData.implementSteps = ''
        this.formData.auditReason = ''
        this.formData.auditContent = ''
        this.formData.auditProcess = ''
        this.formData.auditResultUse = ''
        this.formData.otherContent = ''
        this.formData.implementtime = ''
        this.formData.yqdate = ''
        this.formData.teamName = ''
        this.formData.planName = undefined
        this.formData.dateSection = []
        this.dialogFormVisible = false
        this.clearType = true
        this.zykstype = ''
        this.xmapbid = ''
        this.fileList = []
        this.editId = ''
        this.$emit('fetchData')
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
            this.close()
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
        this.$refs['special_select2'].showEdit('')
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
            this.fileList.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)

            if (this.tableData && this.tableData.length) {
              params.teams = []
              this.tableData.map((v) => {
                if (v.teamName === '') {
                  this.$message.error('请输入小组名称')
                  return
                } else if (v.leaderName === '') {
                  this.$message.error('请添加组长')
                  return
                } else if (v.zyNames === '') {
                  this.$message.error('请添加小组成员')
                  return
                } else {
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
              return this.$message.error('请添加小组')
            }

            const data = await implementPlanSaveOrUpdate({
              ...params,
              attIds,
            })
            if (data.code == 1) {
              this.handleAddFlag = true
              this.$baseMessage('保存成功', 'success')
              this.editId = data.data.id
            } else {
              this.handleAddFlag = false
              this.$baseMessage(data.msg, 'error')
            }
          }
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
        this.fileList = []
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
          fzzname: '',
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
            this.fileList.splice(index, 1)
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
      //     this.fileList.push(file.response.data)
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
      getChildlistObj(val) {
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
        // const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })
        // if (res.data.cnt === 0) {
        //   return this.$message.error('该模板下没有业务单元')
        // }
        this.tempId = val[0].templeteId
        this.$set(this.formData, 'tempName', val[0].templeteName)
        this.$set(this.formData, 'tempId', val[0].templeteId)
      },
      async getChildlistGui(val) {
        // const res = await seachTeamplateCnt({ templeteId: val[0].templeteId })
        // if (res.data.cnt === 0) {
        //   return this.$message.error('该模板下没有业务单元')
        // }
        this.tempZyId = val[0].templeteId
        this.$set(this.formData, 'tbltempletezy', val[0].tempName)
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
        this.rules.isWw = []
        this.rules.tempName = []
        this.rules.tbltempletezy = []
        this.rules.projectOrderName = []
        this.rules.dateSection = []
        this.rules.auditMethod = []
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let params = {
              projectCode: this.formData.projectCode,
              projectName: this.formData.projectName,
              pd_dx: this.pdDx,
              planYear: this.formData.planYear,
              auditType: this.formData.sjlxId,
              projectSource: this.formData.projectSource,
              pmId: this.staffId,
              // planStartDate: this.formatDate(this.formData.dateSection[0]),
              // planEndDate: this.formatDate(this.formData.dateSection[1]),
              // auditMethod: this.formData.auditMethod,
              projectType: this.formData.projectType,
              cntType: +this.formData.cntType,
              auditOrgName: this.formData.auditOrgName,
              projectSummary: this.formData.projectSummary,
              planTime: this.formData.sjap,
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
              this.formData.planName = res.data.plan && res.data.plan.planid
              this.formData.projectCode =
                res.data.WorkReport && res.data.WorkReport.projectCode
              this.projectId = res.data.WorkReport.projectId
              ;(this.rules.isWw = [
                {
                  required: true,
                  message: '请选择是否外委',
                  trigger: 'change',
                },
              ]),
                (this.rules.tempName = [
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
              this.rules.projectOrderName = [
                {
                  required: true,
                  message: '请选择项目经理',
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
              // this.rules.auditMethod = [
              //   {
              //     required: true,
              //     message: '请选择审计方式',
              //     trigger: 'change',
              //   },
              // ]
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
        this.formData.planName = val[0].planid
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
        // } else {
        //
        //   return false
        // }
        // })
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
        console.log(this.formData.isgc)
        if (this.formData.isgc == 1) {
          this.$refs.gcModal.showEdit()
        } else {
          this.$refs.cwModal.showEdit()
        }
      },
      xmqdModalSelect(val) {
        console.log(val, 'val')
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
      cwModalSelect(val, name, row) {
        console.log(val, name, row, 'val')
        this.formData.qdcode = val[0].code
        this.formData.xmapbid = val[0].id
        this.formData.projectName = name ? name : val[0].name
        this.formData.zsname = row ? row.zsname : val[0].approver
        this.formData.zsstaffid = row ? row.zsstaffid : val[0].approverId
        this.formData.auditOrgName = val[0].auditUnit
        // this.formData.fzstaffid = val[0].assistApproverId
        // this.formData.fzname = val[0].assistApprover

        this.formData.projectOrderId = val[0].fpksryids
        this.formData.projectOrderName = val[0].fpksrynames
        console.log(this.formData)
        let params = []
        if (row) {
          params.push({
            leaderName: row ? row.zznames : val[0].groupLeader,
            teamLeaderId: row ? row.zzstaffids : val[0].groupLeaderId,
            fzzname: row ? row.fznames : val[0].fzzName,
            fzzstaffid: row ? row.fzstaffids : al[0].fzzStafffId,
            zyNames: row ? row.fzname : val[0].assistApprover,
            zystaffids: row ? row.fzstaffid : val[0].assistApproverId,
            teamName: val[0].auditGroup,
            show: true,
          })
        } else {
          params.push({
            leaderName: val[0].groupLeader,
            teamLeaderId: val[0].groupLeaderId,
            fzzname: val[0].fzzName,
            fzzstaffid: val[0].fzzStafffId,
            zyNames: val[0].assistApprover,
            zystaffids: val[0].assistApproverId,
            teamName: val[0].auditGroup,
            show: true,
          })
        }
        this.tableData = params
      },
      gcModalSelect(val) {
        console.log(val, 'val')
        this.formData.qdcode = val[0].code
        this.formData.xmapbid = val[0].id
        this.formData.projectName = val[0].name
        this.formData.zsname = val[0].approver
        this.formData.zsstaffid = val[0].approverId
        this.formData.auditOrgName = val[0].auditUnit
        // this.formData.fzstaffid = val[0].assistApproverId
        // this.formData.fzname = val[0].assistApprover

        this.formData.projectOrderId = val[0].fpksryids
        this.formData.projectOrderName = val[0].fpksrynames

        const params = [
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
        this.tableData = params
      },
      openSJDX() {
        if (!this.formData.auditOrgName) {
          this.$baseMessage('请选择被审计单位', 'error')
          return
        }
        this.$refs['sjdx'].showEdit({
          companyName: this.formData.auditOrgName,
          guide: 'sjdxzy',
        })
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(163, this.editId)
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
          // this.tableData = [...this.tableData, ...file.data]
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
