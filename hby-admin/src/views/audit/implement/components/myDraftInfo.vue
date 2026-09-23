<template>
  <div>
    <el-dialog
      v-if="dialogFormVisible"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-row :gutter="14">
        <el-form
          ref="ruleForm"
          label-width="140px"
          :model="formData"
          :rules="rules"
          size="mini"
          :disabled="allDisabled"
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
                :disabled="!footer"
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
                :disabled="!formData.secrectLevelId || !footer"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider></el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="底稿编号" label-width="140px" prop="sheetCode">
              <el-input
                v-model="formData.sheetCode"
                clearable
                placeholder="请输入底稿编号"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="底稿名称" label-width="140px" prop="sheetName">
              <el-input
                v-model="formData.sheetName"
                clearable
                placeholder="请输入底稿名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="底稿类型" label-width="140px" prop="sheettype">
              <el-input
                v-model="formData.sheettype"
                clearable
                placeholder="请输入底稿类型"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="被审计对象"
              label-width="140px"
              prop="orgIdNames"
            >
              <el-input
                v-model="formData.orgIdNames"
                clearable
                placeholder="请输入被审计对象"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="审计事项"
              label-width="140px"
              prop="businessAffiliation"
            >
              <el-input
                v-model="formData.businessAffiliation"
                clearable
                placeholder="请输入审计事项"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审计人员" label-width="140px" prop="realname">
              <el-input
                v-model="formData.realname"
                clearable
                placeholder="请输入审计人员"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="底稿创建时间"
              label-width="140px"
              prop="createTime"
            >
              <el-date-picker
                style="width: 100%"
                v-model="formData.createTime"
                placeholder="请输入底稿创建时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                disabled
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
          <el-form-item label="审计目的" label-width="140px" prop="sheetTarget">
            <el-input
              v-model="formData.sheetTarget"
              clearable
              placeholder="请输入审计目的"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
          <el-col :span="12">
            <el-form-item
              label="是否发现问题"
              label-width="140px"
              prop="riskLevel"
            >
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
          </el-col>
          <el-col :span="12" v-if="showBelongType">
            <el-form-item
              label="是否涉及资金"
              label-width="140px"
              prop="belongType"
            >
              <el-select
                v-model="formData.belongType"
                placeholder="请选择是否涉及资金"
                :style="{ width: '100%' }"
                :disabled="!footer"
                @change="selectBelongType"
              >
                <el-option label="金额类" value="1" />
                <el-option label="非金额类" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="showDetailType">
            <el-form-item
              label="问题类型"
              label-width="140px"
              prop="detailType"
            >
              <el-select
                v-model="formData.detailType"
                placeholder="请选择问题类型"
                :style="{ width: '100%' }"
                @change="selectDetailType"
              >
                <el-option label="绩效类问题金额" value="1" />
                <el-option label="合规性问题金额" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="showOtherType">
            <el-form-item
              label="问题类型"
              label-width="140px"
              prop="detailType"
            >
              <el-select
                v-model="formData.detailType"
                placeholder="请选择问题类型"
                :style="{ width: '100%' }"
              >
                <el-option label="国家政策措施落实方面" value="21" />
                <el-option label="发展规划与战略决策方面" value="22" />
                <el-option label="内部控制与风险管理方面" value="23" />
                <el-option label="其他" value="24" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="showMoneyInput">
            <el-form-item
              label="涉及金额"
              label-width="140px"
              prop="relatedMoney"
            >
              <el-input-number
                v-model="formData.relatedMoney"
                clearable
                placeholder="请输入金额(万元)"
                style="width: 100%"
                :min="0"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="showhgDetailType">
            <el-form-item
              label="合规性分类"
              label-width="140px"
              prop="hgDetailType"
            >
              <el-select
                v-model="formData.hgDetailType"
                placeholder="请选择合规性分类"
                :style="{ width: '100%' }"
                :disabled="!footer"
              >
                <el-option label="会计核算方面" value="1" />
                <el-option label="违规使用资金" value="2" />
                <el-option label="截留、沉淀资金" value="3" />
                <el-option label="损失浪费" value="4" />
                <el-option label="挪用资金" value="5" />
                <el-option label="偷漏税费" value="6" />
                <el-option label="违规取得收入" value="7" />
                <el-option label="其他" value="8" />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
          <el-form-item label="审计分项" label-width="140px" prop="targetName">
            <el-input
              v-model="formData.targetName"
              clearable
              placeholder="请输入审计分项"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item
              label="业务单元"
              label-width="140px"
              prop="businessType"
            >
              <el-input
                v-model="formData.businessType"
                clearable
                placeholder="请输入业务单元"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col> -->
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
              label="是否汇总底稿"
              label-width="140px"
              prop="hzdg"
              v-if="showHZDG"
            >
              <el-select
                v-model="formData.hzdg"
                placeholder="请选择"
                style="width: 266px"
                @change="selectHZDG"
              >
                <el-option label="是" value="是" />
                <el-option label="否" value="否" />
              </el-select>
              <el-button
                @click="selectSFDG()"
                style="margin-left: 10px"
                type="primary"
                v-if="showHZDGButton"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="问题单元"
              label-width="140px"
              prop="businessType"
            >
              <el-input
                v-model="formData.businessType"
                clearable
                placeholder="请选择问题单元"
                style="width: 266px"
                disabled
              />
              <el-button
                @click="selectYWDY"
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
              label="内部问题类型"
              label-width="140px"
              prop="internalType"
            >
              <el-select v-model="formData.internalType" style="width: 100%">
                <el-option
                  v-for="item in SJWTData"
                  :key="item.typeId"
                  :value="item.typeId"
                  :label="item.auditType"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="formData.riskLevel === '是'">
            <el-form-item label="问题标题" label-width="140px" prop="quesTitle">
              <el-input
                v-model="formData.quesTitle"
                clearable
                placeholder="请输入问题标题"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="审计程序"
              label-width="140px"
              prop="suditProcess "
            >
              <el-input
                v-model="formData.suditProcess"
                clearable
                placeholder="请输入审计程序"
                :style="{ width: '100%' }"
                type="textarea"
                :rows="4"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="审计程序执行过程"
              label-width="140px"
              prop="auditDesc"
            >
              <el-input
                v-model="formData.auditDesc"
                clearable
                placeholder="请输入审计程序执行过程"
                :style="{ width: '100%' }"
                type="textarea"
                :rows="4"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="24" v-if="formData.risklevel === '是'">
          <el-form-item
            label="审计发现"
            label-width="140px"
            prop="auditDiscoverable "
          ></el-form-item>
        </el-col> -->
          <el-col :span="24" v-if="formData.riskLevel === '是'">
            <el-form-item
              label="审计发现"
              label-width="140px"
              prop="auditDiscoverable"
            >
              <el-input
                v-model="formData.auditDiscoverable"
                clearable
                placeholder="请输入审计发现"
                :style="{ width: '100%' }"
                type="textarea"
                :rows="4"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="审计意见及建议"
              label-width="140px"
              prop="auditCourse"
            >
              <el-input
                v-model="formData.auditCourse"
                clearable
                placeholder="请输入审计意见及建议"
                :style="{ width: '100%' }"
                type="textarea"
                :rows="4"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审计备忘录" label-width="140px" prop="sjbwl">
              <el-input
                v-model="formData.sjbwl"
                clearable
                placeholder="请输入审计备忘录"
                :style="{ width: '100%' }"
                type="textarea"
                :rows="4"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="24">
          <el-divider>报告内容/问题描述</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="addTable" v-if="footer">
              新增一行
            </el-button>
          </div>
          <el-table :data="reportData">
            <el-table-column label="报告内容/问题描述" prop="reportConcent">
              <template slot-scope="{ row }">
                <el-input
                  type="textarea"
                  :disabled="!footer"
                  v-model="row.reportConcent"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column label="涉及部门" prop="sjdeptNames">
              <template slot-scope="scope">
                <el-input
                  style="width: 80%; margin-right: 5px"
                  v-model="scope.row.sjdeptNames"
                  :disabled="true"
                ></el-input>
                <el-button
                  type="primary"
                  @click="handleSelect(scope.$index)"
                  :disabled="!footer"
                >
                  选择
                </el-button>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80px">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleDeletByid(scope.row, scope.$index)"
                  :disabled="!footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->
          <el-col :span="24">
            <el-divider>关联审计取证单</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
              <el-button type="success" v-if="footer" @click="openTable">
                添加审计取证单
              </el-button>
            </div>
            <el-table :data="tableData2">
              <!-- <el-table-column align="center" label="选择" prop="name" /> -->
              <el-table-column
                align="center"
                label="项目名称"
                prop="prjoectname"
                #default="{ row }"
              >
                <el-button
                  type="text"
                  @click="onProjectDetail(row)"
                  :disabled="false"
                >
                  {{ row.prjoectname }}
                </el-button>
              </el-table-column>
              <el-table-column
                align="center"
                label="审计（调查）事项标题 "
                prop="auditMatter"
              />
              <el-table-column
                align="center"
                label="审计（调查）事项概述"
                prop="auditAbstract"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="证据提供者"
                prop="certificateUser"
              />
              <!-- <el-table-column
                align="center"
                label="日期"
                prop="certificateDate"
                :formatter="formatDate"
              /> -->
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
                v-if="footer"
              >
                <template #default="{ row, $index }">
                  <el-button
                    type="text"
                    @click="handleEditDelte(row, $index)"
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>

          <el-col :span="24">
            <el-divider>关联缺陷</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-top: 5px" v-if="footer">
              <el-button type="success" @click="addBug">新建</el-button>
            </div>
            <el-table :data="BugtableData">
              <el-table-column
                align="center"
                label="缺陷编号"
                prop="bugnumber"
                #default="{ row }"
              >
                <el-button
                  type="text"
                  @click="onFlawDetail(row)"
                  :disabled="false"
                >
                  {{ row.bugnumber }}
                </el-button>
              </el-table-column>
              <el-table-column
                align="center"
                label="缺陷名称"
                prop="defectsname"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="缺陷描述"
                prop="bugdescripte"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="发现时间"
                prop="discovertime"
                show-overflow-tooltip
                :formatter="formatDate"
              />
              <!-- <el-table-column
                align="center"
                label="缺陷性质"
                prop="bugproperty"
                show-overflow-tooltip
              /> -->
              <el-table-column
                align="center"
                label="缺陷级别"
                prop="bugcrilevel"
                show-overflow-tooltip
              />
              <!-- <el-table-column
                align="center"
                label="业务单元"
                prop="businessType"
                show-overflow-tooltip
              /> -->
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
                v-if="footer"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="$refs['bug'].showEdit('edit', row)"
                  >
                    修改
                  </el-button>
                  <el-button type="text" @click="handleDeleteBug(row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="24">
            <el-divider>文件上传</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-top: 5px" v-if="footer">
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
                width="120"
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
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    v-if="footer"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
        <!-- 复核人选择 -->
        <SelectPersonModal1
          @projectManage="selectPerson"
          ref="manage3"
        ></SelectPersonModal1>
        <projectManage
          :modal="false"
          ref="manage"
          @reviewTypeSelect="reviewTypeSelect"
        />
        <DepartmentOption
          ref="DepartmentOptions"
          :multiSelect="true"
          @selectByTable="selected"
        />
      </el-row>
      <template #footer v-if="footer">
        <el-button @click="close">关 闭</el-button>
        <el-button @click="add" type="primary">确定</el-button>
      </template>
    </el-dialog>

    <hzdg ref="edit" @getInfoFromModal="getInfoFromModal" />
    <QRSModal ref="table" @selected="setTable2" />
    <YWDYModal ref="YWDY" @selected="YWDYInfo" />
    <BugModal ref="bug" @bug-data="bugdata" scene="draft" />
    <DoubtfulInfo ref="DoubtfulInfo" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import SelectPersonModal1 from './options/selectZBSJRY.vue'
  import {
    createDrafCode,
    deleteFile,
    deleteSheetReport,
    download,
    getBugList,
    getQZSInfo,
    getSJWTTypeDatas,
    myDraftFileList,
    myDraftSave,
    whetherLeader,
    imPlementOrderDetail,
  } from '@/api/audit/implement'
  import { currSsProject } from '@/api/audit/preparation'
  import { defectDel } from '@/api/audit/question'
  import store from '@/store'
  import { formatDate, formatDay } from '@/utils'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import BugModal from '@/views/audit/question/components/FlawInfo.vue'
  import hzdg from './hZDGmodal.vue'
  import DepartmentOption from './options/department.vue'
  import YWDYModal from './options/YWDYModal.vue'
  import QRSModal from './QRSModal.vue'
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import DoubtfulInfo from '@/views/audit/implement/components/auditEvidenceInfo'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: '',
    inheritAttrs: false,
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          sheetCode: undefined,
          sheetName: undefined,
          sheettype: undefined,
          orgIdNames: undefined,
          businessAffiliation: undefined,
          quesTitle: undefined,
          auditStaffId: undefined,
          sheetTarget: undefined,
          riskLevel: '否',
          targetName: undefined,
          businessType: undefined,
          suditProcess: undefined,
          auditDiscoverable: undefined,
          auditDesc: undefined,
          auditCourse: undefined,
          sjbwl: undefined,
          belongType: undefined,
          detailType: undefined,
          relatedMoney: undefined,
          hgDetailType: undefined,
          realname: undefined,
          hzdg: '是',
          relationsheetids: '',
          internalType: '',
          state: 0,
          orgIds: '',
          ejfh: '',
          yjfh: '',
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },
        footer: true,
        allDisabled: false,
        fileList: [],
        tableData: [],
        rules: {
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
          sheetId: [
            {
              required: true,
              message: '请输入底稿编号',
              trigger: 'blur',
            },
          ],
          sheetName: [
            {
              required: true,
              message: '请输入底稿名称',
              trigger: 'blur',
            },
          ],
          orgIdNames: [
            {
              required: true,
              message: '请输入被审计对象',
              trigger: 'blur',
            },
          ],
          realname: [
            {
              required: true,
              message: '请输入审计人员',
              trigger: 'blur',
            },
          ],
          riskLevel: [
            {
              required: true,
              message: '是否发现问题',
              trigger: 'blur',
            },
          ],
          auditDesc: [
            {
              required: true,
              message: '请输入审计程序执行过程',
              trigger: 'blur',
            },
          ],
          auditDiscoverable: [],
        },
        dialogFormVisible: false,
        title: '新增',
        sheetID: '',
        reviewType: '',
        reportData: [],
        showHZDGButton: true,
        showHZDG: false,
        showBelongType: false, //是否显示所属类型
        showDetailType: false, //是否显示问题类型
        showOtherType: false,
        showMoneyInput: false, //是否显示金额输入框
        showhgDetailType: false, //是否显示合规性分类
        SJWTData: [], //审计问题的数据,
        tableData2: [], //审计取证单
        targetId: '', //要传给后端
        BugtableData: [],
        bugId: '',
        projectId: '',
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
      }
    },
    components: {
      projectManage,
      DepartmentOption,
      hzdg,
      QRSModal,
      YWDYModal,
      BugModal,
      SelectPersonModal1,
      DoubtfulInfo,
      ZXPerson,
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
        const res = await hasMJ('ImplementMyDraft')
        // this.menuId = res[0].menuid
        // 请求密级下拉数据
        // const res2 = await getMJ({ rightId: res[0].menuid })
        const res2 = await getMJ({ rightId: 800095 })
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
      async onProjectDetail(row) {
        const data = await imPlementOrderDetail({
          certificateId: row.certificateId,
        })
        this.$refs['DoubtfulInfo'].showEdit('detail', data.data.certificate)
      },
      onFlawDetail(row) {
        this.$refs['bug'].showEdit('detail', row)
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
      async handleDeletByid(row, index) {
        let cypData = [...this.reportData]
        if (row && row.reportid) {
          let index
          cypData.map((item, i) => {
            if (item.reportid === row.reportid) {
              index = i
            }
          })
          await deleteSheetReport({ reportid: row.reportid })
          cypData.splice(index, 1)
        } else {
          cypData.splice(index, 1)
        }
        this.reportData = cypData
      },
      addTable() {
        const obj = {
          reportConcent: '',
          sjdeptNames: '',
          sjdeptIds: '',
        }
        this.reportData.push(obj)
      },
      selected(e) {
        if (e.row.length) {
          const name = e.row.map((item) => item.name)
          const ids = e.row.map((item) => item.id)

          this.reportData[e.index].sjdeptNames = name.join(',')
          this.reportData[e.index].sjdeptIds = ids.join(',')
        }
      },
      handleSelect(e) {
        this.$refs['DepartmentOptions'].show(e)
      },
      reviewTypeSelect(e) {
        let name = e.reviewType === 'firststaffid' ? 'yjfh' : 'ejfh'
        this.$set(this.formData, `${name}`, e.id[0].realname)
        this.$set(this.formData, `${e.reviewType}`, e.id[0].staffid)
        this.$refs['ruleForm'].clearValidate()
      },
      projectManager1() {
        // this.$refs['manage'].showEdit('firststaffid')
        this.$refs['manage3'].showEdit([], this.projectId, 'firststaffid')
      },
      projectManager2() {
        // this.$refs['manage'].showEdit('secondstaffid')
        this.$refs['manage3'].showEdit([], this.projectId, 'secondstaffid')
      },
      risklevelChange(index) {
        this.formData.riskLevel = index
        if (index === '是') {
          //所属类型，问题类型默认赋值
          this.showBelongType = true
          this.showDetailType = true
          this.formData.belongType = ''
          this.formData.detailType = ''
          this.formData.relatedMoney = ''
          this.formData.hgDetailType = ''
          this.rules.auditDiscoverable = [
            {
              required: true,
              message: '请输入审计发现',
              trigger: 'blur',
            },
          ]
        } else {
          this.rules.auditDiscoverable = []
          this.showBelongType = false
          this.showDetailType = false
          this.showMoneyInput = false
          this.showhgDetailType = false
          this.formData.belongType = ''
          this.formData.detailType = ''
          this.formData.relatedMoney = ''
          this.formData.hgDetailType = ''
          this.formData.auditDiscoverable = '' //审计发现置空
        }
        this.$forceUpdate()
      },
      async getBugList() {
        let bugList = await getBugList({ sheetid: this.sheetID })
        this.BugtableData = bugList.data.data
      },
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @param {*} hamId 整改清单跳转专用
       * @return {*}
       */
      async showEdit(title, row, data, hamId) {
        //整改清单跳转专用
        if (hamId) {
          if (this.showMJ && this.MJoption.length == 0) {
            // 获取密级,菜单id
            const res = await hasMJ(hamId)
            this.menuId = res[0].menuid
            // 请求密级下拉数据
            const res2 = await getMJ({ rightId: res[0].menuid })
            this.MJoption = res2.data
          }
        }

        this.BugtableData = []
        //获取审计问题类型数据
        let resss = await getSJWTTypeDatas()
        this.SJWTData = resss.data.data || []
        let res11 = await whetherLeader() //判断是否为组长
        if (res11.data.ifLeader) {
          this.showHZDG = true
          this.showHZDGButton = true
          this.formData.hzdg = '是'
        } else {
          this.showHZDG = false
          this.showHZDGButton = false
        }

        this.dialogFormVisible = true
        this.formData.belongType = ''
        this.formData.detailType = ''
        this.formData.relatedMoney = ''
        this.formData.hgDetailType = ''
        const currentProject = await this.getCurrentProject()
        // 疑点发送过来的数据时，data有值
        if (data) {
          let list = []
          list.push(data.attachment)
          this.tableData = list
        }
        if (row && row.sheet) {
          this.sheetID = row.sheet.sheetId
          //获取取证单列表信息
          getQZSInfo({ sheetid: row.sheet.sheetId }).then((res) => {
            const array = res.data.data || []
            this.tableData2 = array.map((x) => {
              if (x.projectName) {
                x.prjoectname = x.projectName
              }
              return x
            })
          })

          //是否发现问题判断
          if (row.sheet.riskLevel === '是') {
            this.showDetailType = true
            this.showBelongType = true
          } else {
            this.showBelongType = false
            this.showDetailType = false
            this.showMoneyInput = false
            this.showhgDetailType = false
          }
          //问题类型判断
          if (row.sheet.detailType === 2) {
            this.showhgDetailType = true
          } else if (row.sheet.detailType === 1) {
            this.showhgDetailType = false
          }
          //判断所属类型
          if (row.sheet.belongType === 2) {
            this.showOtherType = true
            this.showDetailType = false
            this.showMoneyInput = false
          } else if (row.sheet.belongType === 1) {
            this.showMoneyInput = true
            this.showOtherType = false
            this.showDetailType = true
          }

          this.formData = Object.assign(this.formData, row.sheet)
          this.formData.internalType = row.sheet.internalType
            ? Number(row.sheet.internalType)
            : null
          this.formData.businessType = row.sheet.businessType
            ? row.sheet.businessType.toString()
            : ''
          this.formData.belongType = row.sheet.belongType
            ? row.sheet.belongType.toString()
            : ''
          this.formData.detailType = row.sheet.detailType
            ? row.sheet.detailType.toString()
            : ''
          this.formData.hgDetailType = row.sheet.hgDetailType
            ? row.sheet.hgDetailType.toString()
            : ''
          this.formData.staffScopeIds = row.sheet.staffScopeIds
            ? row.sheet.staffScopeIds.toString()
            : ''
          this.formData.staffScopeNames = row.sheet.staffScopeNames
            ? row.sheet.staffScopeNames.toString()
            : ''
          // this.formData.secrectLevelId = row.sheet.secrectLevelId
          //   ? row.sheet.secrectLevelId.toString()
          //   : ''
          // this.formData.internalType = row.sheet.internalType
          //   ? row.sheet.internalType.split(',').map((item) => {
          //       return +item
          //     })
          //   : []
          this.$forceUpdate()
          this.reportData = row.listSP
          this.getFileList(row.sheet.sheetId)
        }

        if (title == 'edit') {
          this.title = '编辑'
          this.getBugList()
          //获取关联缺陷列表
        } else if (title == 'detail') {
          //获取关联缺陷列表
          this.getBugList()
          this.title = '详细'
          this.footer = false
          this.allDisabled = true
        } else {
          if (title === '我的任务') {
            this.formData.operateId = row.operateid
            this.targetId = row.operateid
            this.formData.businessType = row.businessType
            this.formData.suditProcess = row.suditProcess
          }
          if (title === '疑点') {
            this.formData = { ...row.project, ...row.pamas }
            this.tableData = [row.attachment]
          }
          this.title = '新增'
          createDrafCode().then((res) => {
            this.$set(this.formData, 'sheetCode', res.data.autoCode.toString())
          })
          this.reportData = []
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.realname = userInfo.realname
          this.formData.staffid = userInfo.staffid

          this.formData.createTime = formatDate(new Date())
        }
        this.formData.orgIdNames =
          currentProject.auditStaffName || currentProject.orgIdNames
        this.formData.orgIds = currentProject.orgIds
        this.projectId = currentProject.projectId
        this.$forceUpdate()
      },
      async getFileList(sheetId) {
        const data = await myDraftFileList({ sheetid: sheetId })
        this.tableData = data.data.data || []
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      // close() {
      //   this.dialogFormVisible = false
      //   this.$refs['ruleForm'].resetFields()
      //   this.$emit('fetch-data')
      //   this.tableData = []
      //   this.tableData2 = []
      //   this.footer = true
      //   this.showType = false
      //   this.allDisabled = false
      //   this.sheetID = ''
      //   this.formData.auditDesc = ''
      //   this.formData.suditProcess = ''
      // },
      close() {
        // 重置表单字段
        this.$refs['ruleForm'].resetFields()
        // 重置表单数据
        this.formData = {
          sheetCode: undefined,
          sheetName: undefined,
          sheettype: undefined,
          orgIdNames: undefined,
          businessAffiliation: undefined,
          quesTitle: undefined,
          auditStaffId: undefined,
          sheetTarget: undefined,
          riskLevel: '否',
          targetName: undefined,
          businessType: undefined,
          suditProcess: undefined,
          auditDiscoverable: undefined,
          auditDesc: undefined,
          auditCourse: undefined,
          sjbwl: undefined,
          belongType: undefined,
          detailType: undefined,
          relatedMoney: undefined,
          hgDetailType: undefined,
          realname: undefined,
          hzdg: '是',
          relationsheetids: '',
          internalType: '',
          state: 0,
          orgIds: '',
          ejfh: '',
          yjfh: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        // 重置表格数据
        this.tableData = []
        this.tableData2 = []
        this.reportData = []
        this.BugtableData = []
        // 重置状态
        this.footer = true
        this.allDisabled = false
        this.showBelongType = false
        this.showDetailType = false
        this.showOtherType = false
        this.showMoneyInput = false
        this.showhgDetailType = false
        this.showHZDG = false
        this.showHZDGButton = true
        this.sheetID = ''
        this.targetId = ''
        this.projectId = ''
        this.SJWTData = []
        // 关闭对话框
        this.dialogFormVisible = false
        // 触发父组件的 fetch-data 事件
        this.$emit('fetch-data')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            let certificateIds = ''
            this.tableData2.map((item) => {
              certificateIds += item.certificateId
              certificateIds += ','
            })
            if (this.reportData && this.reportData.length) {
              this.formData.srJson = JSON.stringify(this.reportData)
            }
            attids = attids.substring(0, attids.length - 1)
            certificateIds = certificateIds.substring(
              0,
              certificateIds.length - 1
            )
            const bugInfo = this.BugtableData.map((item) => item.bugid)
            let obj = {
              ...this.formData,
            }
            delete obj.createStaff
            delete obj.createTime
            // const { createTime, state, ...other } = this.formData
            const data = await myDraftSave({
              ...obj,
              attids,
              // internalType: this.formData.internalType
              //   ? this.formData.internalType.toString()
              //   : '',
              certificateIds,
              targetId: this.targetId,
              bugIds: bugInfo.toString(),
            })

            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.close()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          } else {
            return false
          }
        })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
        this.$message.success('删除成功')
      },
      selectBelongType(e) {
        if (e === '1') {
          //当所属类型为金额时，显示问题类型第一项
          // this.formData.detailType = '1'
          this.showMoneyInput = true
          this.showDetailType = true
          this.showOtherType = false
          this.formData.detailType = ''
          this.formData.relatedMoney = ''
          this.formData.hgDetailType = ''
        } else if (e === '2') {
          // this.showType = false
          this.formData.detailType = ''
          this.showDetailType = false
          this.showOtherType = true
          this.showMoneyInput = false
          this.showhgDetailType = false
        }
        this.$forceUpdate()
      },
      selectDetailType(e) {
        if (e === '2') {
          this.showhgDetailType = true
        } else {
          this.showhgDetailType = false
        }
        this.$forceUpdate()
      },
      selectSFDG() {
        this.$refs['edit'].showEdits()
      },
      selectHZDG(e) {
        if (e === '是') {
          this.showHZDGButton = true
        } else {
          this.showHZDGButton = false
        }
      },
      getInfoFromModal(e) {
        const ids = e.map((item) => item.sheetId)
        this.formData.relationsheetids = ids.toString()
        //审计过程字段
        const a = e.map((item, index) => {
          return index + 1 + ':' + item.suditProcess + ';'
        })
        const aInfo = a.reduce((cur, pre) => {
          return cur + pre
        })
        this.formData.suditProcess = aInfo
        //审计程序执行过程
        const b = e.map((item, index) => {
          return index + 1 + ':' + item.auditDesc + ';'
        })
        const bInfo = a.reduce((cur, pre) => {
          return cur + pre
        })
        this.formData.auditDesc = bInfo
        //审计程序执行过程
        const c = e.map((item, index) => {
          return index + 1 + ':' + item.auditDiscoverable + ';'
        })
        const cInfo = a.reduce((cur, pre) => {
          return cur + pre
        })
        if (this.formData.riskLevel === '是') {
          this.formData.auditDiscoverable = cInfo
        } else {
          this.formData.auditDiscoverable = ''
        }
      },
      openTable() {
        this.$refs['table'].showEdit(this.sheetID)
      },
      setTable2(e) {
        this.tableData2 = e
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      handleEditDelte(row, index) {
        this.tableData2.splice(index, 1)
        this.$message({
          type: 'success',
          message: '删除成功!',
        })
      },
      selectYWDY() {
        this.$refs['YWDY'].show()
      },
      YWDYInfo(info) {
        this.targetId = info.operateid
        this.$set(this.formData, 'businessType', info.businessType)
        this.$set(this.formData, 'suditProcess', info.suditProcess)
        this.$set(this.formData, 'businessAffiliation', info.riskSource)
      },
      addBug() {
        this.$refs['bug'].showEdit(
          'add',
          null,
          this.formData.businessType,
          null,
          'ImplementMyDraft'
        )
      },
      bugdata(info) {
        console.log('🚀 ~ bugdata ~ info:', info)
        let arr = JSON.parse(JSON.stringify(this.BugtableData))
        const index = arr.findIndex((item) => item.bugid === info.bugid)
        if (index !== -1) {
          // 如果找到了相同 id 的对象，则替换它
          arr[index] = info
        } else {
          // 如果没有找到，则添加到数组中
          arr.push(info)
        }
        console.log(arr, 'arr')
        this.BugtableData = arr
      },
      handleDeleteBug(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          let list = this.BugtableData
          list = list.filter((item) => item.bugid != row.bugid)
          this.BugtableData = list
          const { msg, code } = await defectDel({
            bugid: row.bugid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
        })
      },
      selectPerson(info, type) {
        if (type == 'firststaffid') {
          this.$set(this.formData, 'yjfh', info[0].staff.realname)
          this.$set(this.formData, 'firststaffid', info[0].staffid)
        } else if (type == 'secondstaffid') {
          this.$set(this.formData, 'ejfh', info[0].staff.realname)
          this.$set(this.formData, 'secondstaffid', info[0].staffid)
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.formData, 'staffScopeIds', ids)
        this.$set(this.formData, 'staffScopeNames', names)
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
          this.tableData = [...this.tableData, ...file.data]
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
