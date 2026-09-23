<template>
  <el-dialog
    v-if="dialogFormVisible"
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible="dialogFormVisible"
    width="1000px"
    @close="close"
    destroy-on-close
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        size="mini"
        :disabled="true"
      >
        <el-tabs v-model="activeName" type="card">
          <el-tab-pane label="整改落实" name="reimpl" v-if="reimpl">
            <el-col :span="12">
              <el-form-item label="问题编号" prop="issues.issuesCode">
                <el-input
                  v-model="issues.issuesCode"
                  clearable
                  placeholder="请输入问题编号"
                  :style="{ width: '256px' }"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="被审计单位" prop="issues.auditObjectName">
                <el-input
                  v-model="issues.auditObjectName"
                  clearable
                  placeholder="请输入被审计单位"
                  :style="{ width: '256px' }"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="问题来源" prop="issues.issuesType">
                <el-input
                  v-model="issues.issuesType"
                  clearable
                  placeholder="请输入问题来源"
                  :style="{ width: '256px' }"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发现人" prop="issues.createStaffName">
                <el-input
                  v-model="issues.createStaffName"
                  clearable
                  placeholder="请输入发现人"
                  disabled
                  :style="{ width: '256px' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="问题详情" prop="issues.questionMemo">
                <el-input
                  v-model="issues.questionMemo"
                  clearable
                  placeholder="请输入问题详情"
                  disabled
                  type="textarea"
                  rows="4"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="整改方案" prop="plan.rectificationPlan">
                <el-input
                  v-model="plan.rectificationPlan"
                  clearable
                  placeholder="请输入整改方案"
                  :style="{ width: '100%' }"
                  type="textarea"
                  rows="4"
                  disabled
                />
              </el-form-item>
            </el-col>
            <!-- <el-col :span="24">
            <el-form-item label="整改措施" prop="plan.rectificationMeasures">
              <el-input
                v-model="plan.rectificationMeasures"
                clearable
                placeholder="请输入整改措施"
                :style="{ width: '100%' }"
                type="textarea"
                rows="4"
                disabled
              />
            </el-form-item>
          </el-col> -->
            <el-col :span="24">
              <el-form-item label="成果体现" prop="plan.resultMemo">
                <el-input
                  v-model="plan.resultMemo"
                  clearable
                  placeholder="请输入成果体现"
                  :style="{ width: '100%' }"
                  type="textarea"
                  rows="4"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="预计完成时间" prop="plan.deadline">
                <el-date-picker
                  v-model="plan.deadline"
                  value-format="yyyy-MM-dd"
                  :style="{ width: '256px' }"
                  type="date"
                  disabled
                />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-divider>整改信息</el-divider>
            </el-col>
            <el-col :span="24">
              <el-form-item label="整改措施" prop="rectificationMeasures">
                <el-input
                  v-model="reimpl.rectificationMeasures"
                  clearable
                  placeholder="请输入整改措施"
                  type="textarea"
                  rows="4"
                />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="整改情况概述" prop="situationoverView">
                <el-input
                  v-model="reimpl.situationoverView"
                  clearable
                  placeholder="请输入整改情况概述"
                  type="textarea"
                  rows="4"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="成果体现" prop="achivement">
                <el-input
                  v-model="reimpl.achivement"
                  clearable
                  placeholder="请输入成果体现"
                  type="textarea"
                  rows="4"
                />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="完成时间" prop="deadline">
                <el-date-picker
                  v-model="reimpl.deadline"
                  placeholder="选择完成时间"
                  type="date"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="整改结论" prop="conclusion">
                <el-select
                  v-model="reimpl.conclusion"
                  placeholder="请选择整改结论"
                  clearable
                >
                  <el-option label="未整改" value="未整改" />
                  <el-option label="已整改未到位" value="已整改未到位" />
                  <el-option label="已整改到位" value="已整改到位" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24" v-if="reimpl.conclusion != '已整改到位'">
              <el-form-item label="下一步整改措施" prop="nextMeasures">
                <el-input
                  v-model="reimpl.nextMeasures"
                  clearable
                  placeholder="请输入下一步整改措施"
                  type="textarea"
                  rows="4"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" v-if="reimpl.conclusion != '已整改到位'">
              <el-form-item label="计划完成整改时间" prop="finishTime">
                <el-date-picker
                  v-model="reimpl.finishTime"
                  placeholder="选择计划完成整改时间"
                  type="date"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-divider>文件上传</el-divider>
            </el-col>
            <el-col :span="24">
              <el-table :data="reimpl.attList">
                <el-table-column
                  align="center"
                  label="附件名称"
                  prop="attname"
                />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="attsize"
                />
                <el-table-column
                  align="center"
                  label="创建人"
                  prop="uploader"
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template #default="{ row }">
                    <el-button
                      type="text"
                      @click="handleDown(row)"
                      :disabled="false"
                    >
                      下载
                    </el-button>
                    <el-button
                      type="text"
                      @click="handlePreview(row)"
                      :disabled="false"
                    >
                      预览
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
          </el-tab-pane>

          <el-tab-pane label="整改评价" name="valua" v-if="valua">
            <el-col :span="24">
              <el-form-item label="整改评价结果" prop="resultStatus">
                <el-select
                  v-model="valua.resultStatus"
                  placeholder="请选择整改评价结果"
                  clearable
                >
                  <el-option label="未整改" :value="1" />
                  <el-option label="已整改未到位" :value="2" />
                  <el-option label="已整改到位" :value="3" />
                  <el-option label="关闭" :value="4" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24" v-if="valua.resultStatus != '4'">
              <el-form-item label="检查过程" prop="inspectionProcess">
                <el-input
                  v-model="valua.inspectionProcess"
                  clearable
                  placeholder="请输入检查过程"
                  type="textarea"
                  rows="4"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" v-if="valua.resultStatus == '4'">
              <el-form-item label="关闭原因" prop="inspectionProcess">
                <el-input
                  v-model="valua.inspectionProcess"
                  clearable
                  placeholder="请输入关闭原因"
                  type="textarea"
                  rows="4"
                />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-divider>文件上传</el-divider>
            </el-col>
            <el-col :span="24">
              <el-table :data="valua.attList">
                <el-table-column
                  align="center"
                  label="附件名称"
                  prop="attname"
                />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="attsize"
                />
                <el-table-column
                  align="center"
                  label="创建人"
                  prop="uploader"
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template #default="{ row }">
                    <el-button
                      type="text"
                      @click="handleDown(row)"
                      :disabled="false"
                    >
                      下载
                    </el-button>
                    <el-button
                      type="text"
                      @click="handlePreview(row)"
                      :disabled="false"
                    >
                      预览
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
          </el-tab-pane>

          <el-tab-pane label="整改方案" name="plan" v-if="plan">
            <el-col :span="12">
              <el-form-item label="方案类别" prop="planType">
                <el-select
                  style="width: 100%"
                  v-model="plan.planType"
                  placeholder="选择方案类别"
                >
                  <el-option label="审计" :value="1" />
                  <el-option label="内控" :value="2" />
                  <el-option label="非系统实施" :value="3" />
                  <el-option label="外部审计" :value="4" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="方案编号" prop="planCode">
                <el-input
                  v-model="plan.planCode"
                  clearable
                  placeholder="请输入方案编号"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="方案名称" prop="planName">
                <el-input
                  v-model="plan.planName"
                  clearable
                  placeholder="请输入方案名称"
                />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="关联项目" prop="projectName">
                <el-input
                  v-model="plan.projectName"
                  clearable
                  placeholder="请选择关联项目"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="整改经办人" prop="handlerName">
                <el-input
                  v-model="plan.handlerName"
                  clearable
                  placeholder="整改经办人"
                  disabled
                />
              </el-form-item>
            </el-col>
            <!-- <el-col :span="12">
            <el-form-item label="整改责任人" prop="zrrRealName">
              <el-input
                v-model="plan.zrrRealName"
                clearable
                placeholder="整改责任人"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.executor.show('zrrRealName')"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col> -->
            <el-col :span="12">
              <el-form-item label="创建人" prop="createStaffName">
                <el-input
                  v-model="plan.createStaffName"
                  clearable
                  placeholder="请输入创建人"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="截止时间" prop="deadlineTime">
                <el-date-picker
                  v-model="plan.deadlineTime"
                  value-format="yyyy-MM-dd"
                  type="date"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注" prop="planMemo">
                <el-input
                  v-model="plan.planMemo"
                  clearable
                  placeholder="请输入备注"
                  :style="{ width: '100%' }"
                  type="textarea"
                  rows="4"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" style="margin: 20px 0">
              <UEditor
                ref="ueditor"
                v-model="plan.zgcont"
                :height="300"
                :disabled="true"
              />
            </el-col>
            <!-- <el-col :span="24">
            <el-divider>整改清单</el-divider>
          </el-col>
          <el-col :span="24">
            <el-table :data="plan.issuesList">
              <el-table-column
                align="center"
                label="问题编号"
                prop="issuesCode"
                width="100"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleDetailContent(row)">
                    {{ row.issuesCode }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="问题名称" prop="issuesName" />
              <el-table-column align="center" label="问题标题" prop="issuesTitle" />
              <el-table-column align="center" label="拟稿人" prop="createStaffName" />
              <el-table-column align="center" label="拟稿日期" prop="createTime" :formatter="formatDate" />
            </el-table>
          </el-col> -->
            <el-col :span="24" style="margin-top: 10px">
              <el-divider>文件上传</el-divider>
            </el-col>
            <el-col :span="24">
              <el-table :data="plan.attList">
                <el-table-column
                  align="center"
                  label="附件名称"
                  prop="attname"
                />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="attsize"
                />
                <el-table-column
                  align="center"
                  label="创建人"
                  prop="uploader"
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template #default="{ row }">
                    <el-button
                      type="text"
                      @click="handleDown(row)"
                      :disabled="false"
                    >
                      下载
                    </el-button>
                    <el-button
                      type="text"
                      @click="handlePreview(row)"
                      :disabled="false"
                    >
                      预览
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
          </el-tab-pane>

          <el-tab-pane label="整改清单" name="issues" v-if="issues && false">
            <template>
              <el-col :span="12">
                <el-form-item label="问题来源" prop="issuesType">
                  <el-select
                    style="width: 100%"
                    v-model="issues.issuesType"
                    placeholder="选择问题来源"
                  >
                    <el-option label="审计" :value="1" />
                    <el-option label="内控" :value="2" />
                    <el-option label="非系统实施" :value="3" />
                    <el-option label="外部审计" :value="4" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="问题编号" prop="issuesCode">
                  <el-input
                    v-model="issues.issuesCode"
                    clearable
                    placeholder="请输入问题编号"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="项目编号">
                  <el-input
                    v-model="issues.projectNo"
                    clearable
                    placeholder="请输入项目编号"
                    :style="{ width: '100%' }"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="项目名称">
                  <el-input
                    v-model="issues.projectName"
                    clearable
                    placeholder="请输入项目名称"
                    :style="{ width: '100%' }"
                  />
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="问题名称" prop="issuesName">
                  <el-input
                    v-model="issues.issuesName"
                    clearable
                    placeholder="请输入问题名称"
                    :style="{ width: '100%' }"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="被审计/评价对象" prop="auditObjectName">
                  <el-input
                    v-model="issues.auditObjectName"
                    clearable
                    placeholder="请选择被审计/评价对象"
                  />
                </el-form-item>
              </el-col>
              <!-- <el-col :span="12">
              <el-form-item label="事项" prop="issuesItem">
                <el-input
                  v-model="issues.issuesItem"
                  clearable
                  placeholder="请输入事项"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col> -->
              <el-col :span="12">
                <el-form-item label="经办人员" prop="createStaffName">
                  <el-input
                    v-model="issues.createStaffName"
                    clearable
                    placeholder="请输入经办人员"
                    :style="{ width: '100%' }"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="责任人" prop="responsiblePersonName">
                  <el-input
                    v-model="issues.responsiblePersonName"
                    clearable
                    placeholder="请选择责任人"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="责任部门" prop="responsibleDeptName">
                  <el-input
                    v-model="issues.responsibleDeptName"
                    clearable
                    placeholder="请选择责任部门"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="创建时间" prop="createTime">
                  <el-date-picker
                    style="width: 100%"
                    v-model="issues.createTime"
                    placeholder="请输入创建时间"
                    type="date"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="问题标题" prop="issuesTitle">
                  <el-input
                    v-model="issues.issuesTitle"
                    clearable
                    placeholder="请输入问题标题"
                    :style="{ width: '100%' }"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="问题详情" prop="questionMemo">
                  <el-input
                    v-model="issues.questionMemo"
                    clearable
                    placeholder="请输入问题详情"
                    :style="{ width: '100%' }"
                    type="textarea"
                    :rows="4"
                  />
                </el-form-item>
              </el-col>
              <!-- <el-col :span="24">
              <el-form-item label="意见及建议" prop="opinions">
                <el-input
                  v-model="issues.opinions"
                  clearable
                  placeholder="请输入意见及建议"
                  :style="{ width: '100%' }"
                  type="textarea"
                  :rows="4"
                />
              </el-form-item>
            </el-col> -->
            </template>

            <!-- <template>
            <el-col :span="24">
              <el-divider>历史版本</el-divider>
            </el-col>
            <el-col>
              <el-table :data="issues.relaList">
                <el-table-column
                  align="center"
                  label="问题编号"
                  prop="issuesCode"
                  width="100"
                >
                  <template #default="{ row }">
                    <el-button type="text" @click="handleDetailHistory(row)" :disabled="false">
                      {{ row.issuesCode }}
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="问题名称" prop="issuesName" />
                <el-table-column align="center" label="问题标题" prop="issuesTitle" />
                <el-table-column align="center" label="拟稿人" prop="createStaffName" />
                <el-table-column align="center" label="整改方案" prop="rectificationPlan" />
                <el-table-column align="center" label="整改措施" prop="rectificationMeasures" />
                <el-table-column align="center" label="成果体现" prop="resultMemo" />
              </el-table>
            </el-col>
          </template> -->

            <el-col :span="24" style="margin-top: 20px">
              <el-divider>文件上传</el-divider>
            </el-col>
            <el-col :span="24">
              <el-table :data="formData.attList">
                <el-table-column
                  align="center"
                  label="附件名称"
                  prop="attname"
                />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="attsize"
                />
                <el-table-column
                  align="center"
                  label="创建人"
                  prop="uploader"
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template #default="{ row }">
                    <el-button
                      type="text"
                      @click="handleDown(row)"
                      :disabled="false"
                    >
                      下载
                    </el-button>
                    <el-button
                      type="text"
                      @click="handlePreview(row)"
                      :disabled="false"
                    >
                      预览
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-row>

    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import { getIssuesAllDetailInfo } from '@/api/zgzz/index.js'
  import store from '@/store'
  import DepartmentOption from '@/views/audit/implement/components/options/department.vue'
  import CompanyTreeModel from '@/components/CompanyTreeModel/index.vue'
  import Company from '@/components/Company.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import Executor from '@/views/audit/analyse/components/executor'
  import * as dayjs from 'dayjs'
  import UEditor from '@/components/UEditor'

  const { baseURL } = require('@/config')

  export default {
    name: 'AllDataDetail',
    components: {
      DepartmentOption,
      CompanyTreeModel,
      Company,
      Executor,
      UEditor,
    },
    inheritAttrs: false,
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        dialogFormVisible: false,
        formData: {
          attIds: '', // 当前页面新保存的附件主键数组
          attList: [], // 附件列表
          relaList: [], // 历史版本
          auditObjectName: '', // 被审计对象名称
          auditObjectId: '', // 被审计对象主键
          auditObjectType: '', // 被审计对象类型
          createStaffName: '', // 创建人
          createTime: '', // 创建时间
          historyStatus: '', // 历史状态用于还原
          issuesCode: '', // 问题编号
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
          quesitionId: '', // 审计内控关联表单外键
          questionMemo: '', // 问题详情
          mainorg: '',
          responsibleDept: '', // 责任部门
          responsibleDeptName: '', // 责任部门
          responsiblePerson: '', // 责任人
          responsiblePersonName: '', // 责任人
        },
        title: '详情',
        reviewType: '',
        reportData: [],
        currentProject: {},
        activeName: 'reimpl',
        issues: null,
        plan: null,
        reimpl: null,
        valua: null,
      }
    },
    methods: {
      async showEdit(row) {
        this.dialogFormVisible = true
        this.formData.createStaffName = JSON.parse(
          localStorage.getItem('userInfo')
        ).realname
        this.formData.createTime = new Date()
        if (row) {
          Object.assign(this.formData, row)
          // 整改清单数据
          if (row.issues) {
            this.issues = row.issues
            this.issues.relaList = this.issues.relaList.map((x) => {
              const { issues, ...other } = x
              return {
                ...issues,
                ...other,
              }
            })
          }

          // 整改方案数据
          if (row.plan) {
            this.plan = row.plan
            this.plan.rectificationPlan = row.rectificationPlan
            this.plan.rectificationMeasures = row.rectificationMeasures
            this.plan.resultMemo = row.resultMemo
            this.plan.deadline = row.deadline
            this.plan.issuesList = this.plan.issuesList.map((x) => {
              const { issues, ...other } = x
              return {
                ...issues,
                ...other,
              }
            })
          }

          // 整改落实数据
          if (row.reimpl) {
            this.reimpl = row.reimpl
          }

          // 整改评价数据
          if (row.valua) {
            this.valua = row.valua
          }
        }
      },
      close() {
        this.dialogFormVisible = false
        this.$emit('closeDialog')
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
      async handlePreview(file) {
        const { data } = await getPrivewAttInfo({
          attId: file.attid,
          attType: 2,
        })

        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
      },
      // 历史版本信息
      async handleDetailHistory(row) {
        const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
        this.$showAllDataDetailDialog(res.data)
      },
      // 整改清单信息
      async handleDetailContent(row) {
        const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
        this.$showAllDataDetailDialog(res.data)
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return dayjs(data).format('YYYY-MM-DD')
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
