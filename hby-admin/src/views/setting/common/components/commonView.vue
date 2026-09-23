<template>
  <div>
    <el-dialog :close-on-click-modal="false" :append-to-body="true" :title="title" :visible.sync="dialogFormVisible"
      width="80%" @close="close">
      <el-row :gutter="24" v-loading="loading">
        <el-form ref="ruleForm" label-width="140px" :model="formData" :rules="rules" size="mini"
          :disabled="allDisabled">
          <el-col :span="24">
            <el-divider>会计期间</el-divider>
          </el-col>
          <el-col :span="6">
            <el-form-item label="会计年度" prop="year">
              <el-date-picker style="width: 100%" v-model="formData.year" placeholder="会计年度" type="year"
                :disabled="!footer" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="会计期间个数" prop="num">
              <el-input v-model.trim="formData.num" placeholder="会计期间个数" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="会计半年个数" prop="yearNum">
              <el-input v-model.trim="formData.yearNum" placeholder="会计半年个数" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="会计季度个数" prop="quarterNum">
              <el-input v-model.trim="formData.quarterNum" placeholder="会计季度个数" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="起始日期" prop="test">
              <el-date-picker style="width: 100%" v-model="formData.year" placeholder="起始日期" type="date"
                :disabled="!footer" format="yyyy-MM-dd" value-format="yyyy-MM-dd" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="终止日期" prop="endTime">
              <el-date-picker style="width: 100%" v-model="formData.endTime" placeholder="终止日期" type="date"
                :disabled="!footer" format="yyyy-MM-dd" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="期间个数小于12">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="margin-top: 20px">
            <el-divider>会计期间映射</el-divider>
          </el-col>
          <el-col :span="6">
            <el-form-item label="映射编码" prop="test">
              <el-input v-model.trim="formData.code" placeholder="映射编码" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="目标会计期间方案" prop="test">
              <el-select :style="{ width: '100%' }" v-model="formData.target" placeholder="目标会计期间方案"
                :disabled="!footer">

              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="来源会计期间方案" prop="test">
              <el-select :style="{ width: '100%' }" v-model="formData.source" placeholder="来源会计期间方案"
                :disabled="!footer">

              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="目标会计年度">
              <el-select :style="{ width: '100%' }" v-model="formData.targetYear" placeholder="目标会计年度"
                :disabled="!footer">

              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="创建人">
              <el-input v-model.trim="formData.code" placeholder="创建人" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="创建时间">
              <el-input v-model.trim="formData.code" placeholder="创建时间" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="最后修改人">
              <el-input v-model.trim="formData.code" placeholder="最后修改人" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="最后修改时间">
              <el-input v-model.trim="formData.code" placeholder="最后修改时间" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-table :data="lawyerIdList">
              <el-table-column align="center" label="序号" prop="phone" />
              <el-table-column align="center" label="目标方案会计月" prop="position" />
              <el-table-column align="center" label="开始日期" prop="education" />
              <el-table-column align="center" label="结束日期" prop="phone" />
              <el-table-column align="center" label="来源方案起始会计月" prop="position" />
              <el-table-column align="center" label="开始日期" prop="education" />
              <el-table-column align="center" label="来源方案截止会计月" prop="phone" />
              <el-table-column align="center" label="结束日期" prop="position" />
            </el-table>
          </el-col>
          <el-col :span="24" style="margin-top: 20px">
            <el-divider>币种</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-button type="success" @click="handleAdd">新建</el-button>
              <el-button type="success">保存</el-button>
            </div>
          </el-col>
          <el-col :span="24">
            <el-table :data="currencyList">
              <el-table-column align="center" label="序号" prop="id" />
              <el-table-column align="center" label="所属组织" prop="organization" />
              <el-table-column align="center" label="币种编码" prop="code">
                <template #default="{ row }">
                  <el-input v-model.trim="row.code" />
                </template>
              </el-table-column>
              <el-table-column align="center" label="币种名称" prop="name">
                <template #default="{ row }">
                  <el-input v-model.trim="row.name" />
                </template>
              </el-table-column>
              <el-table-column align="center" label="币种币符" prop="symbol">
                <template #default="{ row }">
                  <el-input v-model.trim="row.symbol" />
                </template>
              </el-table-column>
              <el-table-column align="center" label="全局本位币" prop="isOrganization" width="120">
                <template #default="{ row }">
                  <el-switch v-model="row.isOrganization">
                  </el-switch>
                </template>
              </el-table-column>
              <el-table-column align="center" label="金额小数位数" prop="num" width="120" />
              <el-table-column align="center" label="金额进舍规则" prop="rule" width="120" />
              <el-table-column align="center" label="单价小数位数" prop="num1" width="120" />
              <el-table-column align="center" label="单价进舍规则" prop="rule1" width="120" />
              <el-table-column align="center" label="操作" show-overflow-tooltip width="120">
                <template #default="{ row }">
                  <el-button type="text" v-if="footer">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="24" style="margin-top: 20px">
            <el-divider>单价精度设置</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-button type="success" @click="handleAdd">新建</el-button>
              <el-button type="success">保存</el-button>
            </div>
          </el-col>
          <el-col :span="24">
            <el-table :data="currencyList">
              <el-table-column align="center" label="序号" prop="id" />
              <el-table-column align="center" label="币种编码" prop="code">
                <template #default="{ row }">
                  <!-- <el-input v-model.trim="row.code" /> -->
                  {{ row.code }}
                </template>
              </el-table-column>
              <el-table-column align="center" label="币种名称" prop="name">
                <template #default="{ row }">
                  <!-- <el-input v-model.trim="row.name" /> -->
                  {{ row.name }}
                </template>
              </el-table-column>
              <el-table-column align="center" label="单价小数位数" prop="num1" />
              <el-table-column align="center" label="单价进舍规则" prop="rule1" />
              <el-table-column align="center" label="集团单价小数位数" prop="" />
              <el-table-column align="center" label="集团单价进舍规则" prop="" />
            </el-table>
          </el-col>
          <el-col :span="24" style="margin-top: 20px">
            <el-divider>外币汇率-全局</el-divider>
          </el-col>
          <el-col :span="24" style="margin-top: 20px">
            <el-divider>工作日历规则-全局</el-divider>
          </el-col>
          <el-col :span="6">
            <el-form-item label="所属组织" prop="test">
              <el-input placeholder="全局" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="工作日历规则编码" prop="test">
              <el-input placeholder="工作日历规则编码" v-model="formData.workCode" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="工作日历规则名称" prop="test">
              <el-input placeholder="工作日历规则名称" v-model="formData.workName" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="上班时间">
              <el-time-picker v-model="formData.workStart" placeholder="选择时间" style="width: 100%;"></el-time-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="下班时间">
              <el-time-picker v-model="formData.workEnd" placeholder="选择时间" style="width: 100%;"></el-time-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="备注">
              <el-input placeholder="备注" v-model="formData.workRemarks" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider content-position="left">公休日设置</el-divider>
            <el-checkbox-group v-model="formData.workCheckList">
              <el-col :span="6" style="padding: 0 0 10px 140px;"><el-checkbox label="周日"></el-checkbox></el-col>
              <el-col :span="6" style="padding: 0 0 10px 140px;"><el-checkbox label="周一"></el-checkbox></el-col>
              <el-col :span="6" style="padding: 0 0 10px 140px;"><el-checkbox label="周二"></el-checkbox></el-col>
              <el-col :span="6" style="padding: 0 0 10px 140px;"><el-checkbox label="周三"></el-checkbox></el-col>
              <el-col :span="6" style="padding: 0 0 10px 140px;"><el-checkbox label="周四"></el-checkbox></el-col>
              <el-col :span="6" style="padding: 0 0 10px 140px;"><el-checkbox label="周五"></el-checkbox></el-col>
              <el-col :span="6" style="padding: 0 0 10px 140px;"><el-checkbox label="周六"></el-checkbox></el-col>
            </el-checkbox-group>
          </el-col>
          <el-col :span="24" style="margin-top: 20px">
            <el-divider>工作日历-全局</el-divider>
          </el-col>
          <el-col :span="6">
            <el-form-item label="所属组织">
              <el-input placeholder="全局" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="工作日历编码" prop="test">
              <el-input placeholder="工作日历规则编码" v-model="formData.workCode" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="工作日历名称" prop="test">
              <el-input placeholder="工作日历规则名称" v-model="formData.workName" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="依据日历规则" prop="test">
              <el-input placeholder="依据日历规则" v-model="formData.workName" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="起始日">
              <el-date-picker style="width: 100%" v-model="formData.year" placeholder="起始日期" type="date"
                :disabled="!footer" format="yyyy-MM-dd" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="结束日">
              <el-date-picker style="width: 100%" v-model="formData.year" placeholder="起始日期" type="date"
                :disabled="!footer" format="yyyy-MM-dd" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="假日类别">
              <el-select :style="{ width: '100%' }" v-model="formData.source" placeholder="假日类别"
                :disabled="!footer">
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="启用状态">
              <el-select :style="{ width: '100%' }" v-model="formData.source" placeholder="已启用" disabled>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="默认工作日历">
              <el-switch v-model="formData.isWork"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="margin-top: 20px">
            <el-divider>数据格式</el-divider>
          </el-col>
          <el-col :span="6">
            <el-form-item label="编码" prop="test">
              <el-input placeholder="编码" v-model="formData.dataCode" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="名称" prop="test">
              <el-input placeholder="名称" v-model="formData.dataName" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="描述">
              <el-input placeholder="描述" v-model="formData.dataDescribe" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="数字格式">
              <el-input placeholder="数字格式" v-model="formData.dataDescribe" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="日期格式">
              <el-input placeholder="日期格式" v-model="formData.dataDescribe" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="时间格式">
              <el-input placeholder="时间格式" v-model="formData.dataDescribe" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="地址格式">
              <el-input placeholder="地址格式" v-model="formData.dataDescribe" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="默认格式">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="margin-top: 20px">
            <el-divider>国家地区</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="footer">
              <el-button type="success" @click="handleAdd">新建</el-button>
              <el-button type="success">保存</el-button>
            </div>
          </el-col>
          <el-col :span="24">
            <el-table :data="currencyList">
              <el-table-column align="center" label="序号" prop="id">
                <template #default="{ row }">
                  1
                </template>
              </el-table-column>
              <el-table-column align="center" label="编码" prop="organization" >
                <template #default="{ row }">
                  AD
                </template>
              </el-table-column>
              <el-table-column align="center" label="三位代码" prop="code">
                <template #default="{ row }">
                  AND
                </template>
              </el-table-column>
              <el-table-column align="center" label="数字代码" prop="name">
                <template #default="{ row }">
                  020
                </template>
              </el-table-column>
              <el-table-column align="center" label="名称" prop="symbol">
                <template #default="{ row }">
                  安道尔
                </template>
              </el-table-column>
              <el-table-column align="center" label="英文名称" prop="isOrganization" width="120">
                <template #default="{ row }">
                  Andorra
                </template>
              </el-table-column>
              <el-table-column align="center" label="全称" prop="code">
                <template #default="{ row }">
                  安道尔公国
                </template>
              </el-table-column>
              <el-table-column align="center" label="电话代码" prop="name">
                <template #default="{ row }">
                  33
                </template>
              </el-table-column>
              <el-table-column align="center" label="描述" prop="symbol">
                <template #default="{ row }">
                  
                </template>
              </el-table-column>
              <el-table-column align="center" label="时区" prop="isOrganization" width="120">
                <template #default="{ row }">
                  中欧时间
                </template>
              </el-table-column>
              <el-table-column align="center" label="数据格式" prop="name">
                <template #default="{ row }">
                  英国英语
                </template>
              </el-table-column>
              <el-table-column align="center" label="语种" prop="symbol">
                <template #default="{ row }">
                  english
                </template>
              </el-table-column>
              <el-table-column align="center" label="本位币" prop="isOrganization" width="120">
                <template #default="{ row }">
                  欧元
                </template>
              </el-table-column>
              <el-table-column align="center" label="欧盟国家" prop="name">
                <template #default="{ row }">
                  <el-switch v-model="row.isOrganization"></el-switch>
                </template>
              </el-table-column>
              <el-table-column align="center" label="IBAN总长度" prop="symbol">
                <template #default="{ row }">
                  24
                </template>
              </el-table-column>
              <el-table-column align="center" label="BBAN规则" prop="isOrganization" width="120">
                <template #default="{ row }">
                  8n,12c
                </template>
              </el-table-column>
              <el-table-column align="center" label="IBAN域规则" prop="symbol">
                <template #default="{ row }">
                  ADkkbb
                </template>
              </el-table-column>
              <el-table-column align="center" label="操作" prop="isOrganization" width="120">
                <template #default="{ row }">
                  <el-button type="text" v-if="footer">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="24" style="margin-top: 20px">
            <el-divider>行政区划</el-divider>
          </el-col>
          <el-col :span="6">
            <el-form-item label="国家地区">
              <el-select :style="{ width: '100%' }" placeholder="国家地区">
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="行政区划编码" prop="test">
              <el-input placeholder="行政区划编码" v-model="formData.administrationCode" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="行政区划名称" prop="test">
              <el-input placeholder="行政区划名称" v-model="formData.administrationName" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="行政区划全称">
              <el-input placeholder="行政区划全称" v-model="formData.administrationName1" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="简称">
              <el-input placeholder="简称" v-model="formData.administrationName2" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="助记码">
              <el-input placeholder="助记码" v-model="formData.administrationCode1" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="上级行政区划">
              <el-select :style="{ width: '100%' }" placeholder="上级行政区划">
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="时区">
              <el-select :style="{ width: '100%' }" placeholder="时区">
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="邮编">
              <el-input placeholder="邮编" v-model="formData.email" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="语种">
              <el-select :style="{ width: '100%' }" placeholder="语种">
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="数据格式">
              <el-select :style="{ width: '100%' }" placeholder="数据格式">
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="启用状态">
              <el-select :style="{ width: '100%' }" v-model="formData.source" placeholder="已启用" disabled>
              </el-select>
            </el-form-item>
          
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button type="primary" v-if="!allDisabled">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- <experienceView ref="experienceView" @on-save-success="onSaveSuccess" />
    <workView ref="workView" @on-save-success="onSaveSuccess" />
    <workStatementView
      ref="workStatementView"
      @on-save-success="onSaveSuccess"
    />
    <evaluationEdit ref="evaluationEdit" @on-save-success="onSaveSuccess" /> -->
    <!-- <company-select-modal ref="companySelect" @selected="handleCompanyTreeSelected" /> -->
    <!-- <project-manage @projectManage="getChildlistPro" ref="manage" /> -->
  </div>
</template>
<script>
// import experienceView from './experienceView.vue'
// import workView from './workView.vue'
// import workStatementView from './workStatementView.vue'
// import scoreView from './scoreView.vue'
// import evaluationEdit from './evaluationEdit.vue'
import CompanySelectModal from '@/components/CampanySelectModal'
import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
import { formatDay } from '@/utils/index'

import {
  legalService,
  legalServiceLawyer,
  legalServiceWorkRecord,
  legalServiceWorkReport,
  legalServiceGrade,
  fetchApi,
} from '@/api/fwgl/api'

const { saveOrUpdate, detail } = legalService
const { doDelete: legalServiceLawyerDoDelete } = legalServiceLawyer
const { doDelete: legalServiceWorkRecordDoDelete } = legalServiceWorkRecord
const { doDelete: legalServiceWorkReportDoDelete } = legalServiceWorkReport
const { doDelete: legalServiceGradeDoDelete } = legalServiceGrade

export default {
  props: [],
  components: {
    // experienceView,
    // workView,
    // workStatementView,
    // scoreView,
    // evaluationEdit,
    CompanySelectModal,
    projectManage,
  },
  data() {
    return {
      loading: false,
      dialogFormVisible: false,
      title: '新增',
      footer: true,
      formData: {
        workCheckList: ['周一']
      },
      lawyerIdList: [], // 律师信息
      workRecordIdList: [], // 工作记录
      workReportIdList: [], // 工作报告
      gradeIdList: [], // 评分
      rules: {
        year: [
          {
            required: true,
            message: '请选择会计年度',
            trigger: 'change',
          },
        ],
        num: [
          {
            required: true,
            message: '请输入',
            trigger: 'blur',
          },
        ],
        yearNum: [
          {
            required: true,
            message: '请输入',
            trigger: 'blur',
          },
        ],
        quarterNum: [
          {
            required: true,
            message: '请输入',
            trigger: 'blur',
          },
        ],
        startTime: [
          {
            required: true,
            message: '请选择',
            trigger: 'change',
          },
        ],
        endTime: [
          {
            required: true,
            message: '请选择',
            trigger: 'change',
          },
        ],
        test: [
          {
            required: true,
            message: ' ',
            trigger: 'blur',
          },
        ],
      },
      legalServiceLawyerDoDelete: legalServiceLawyerDoDelete,
      legalServiceWorkRecordDoDelete: legalServiceWorkRecordDoDelete,
      legalServiceWorkReportDoDelete: legalServiceWorkReportDoDelete,
      legalServiceGradeDoDelete: legalServiceGradeDoDelete,
      allDisabled: false,

      currencyList: [
        { id: 1, organization: '全局', code: 'CNY', name: '人民币', symbol: '￥', isOrganization: true, num: 2, rule: '四舍五入', num1: 2, rule1: '四舍五入' },
        { id: 2, organization: '全局', code: 'EUR', name: '欧元', symbol: '&', isOrganization: false, num: 2, rule: '四舍五入', num1: 2, rule1: '四舍五入' }
      ],
    }
  },
  methods: {
    /**
     * @description: 外部打开dialog
     * @param {*} title 类型
     * @param {*} row 行数据
     * @return {*}
     */
    async showEdit(title, row) {
      this.footer = true
      this.dialogFormVisible = true
      this.lawyerIdList = []
      this.workRecordIdList = []
      this.workReportIdList = []
      this.gradeIdList = []
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.formData.belongGroupName = userInfo.currentOrg.orgname
      if (title == 'edit') {
        this.title = '编辑'
      } else if (title == 'detail') {
        this.title = '详细'
        this.footer = false
        this.allDisabled = true
      } else {
        this.title = '新增'
        this.formData.creatorName = userInfo.realname
        this.formData.createdTime = new Date()
      }
    },
    /**
     * @description: 
     * @return {*}
     */
    handleAdd() {
      this.currencyList.push({ id: 3, organization: '全局', code: '', name: '', symbol: '', isOrganization: false, num: 2, rule: '四舍五入', num1: 2, rule1: '四舍五入' })
    },
    close() {
      this.formData = {}
      this.dialogFormVisible = false
      this.allDisabled = false
    },
  },
}
</script>
