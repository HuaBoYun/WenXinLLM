<template>
  <div>
    <el-dialog
      :append-to-body="true"
      :title="'复核'"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-tabs v-model="activeName" type="card" @tab-click="handleTabClick">
        <el-tab-pane label="基本信息" name="first">
          <h3>{{ '复核' }}-审批</h3>
          <el-row :gutter="24">
            <el-form
              ref="ruleForm"
              label-width="100px"
              :model="formData"
              :rules="rules"
              size="mini"
            >
              <el-col :span="12">
                <el-form-item
                  label="底稿编号"
                  label-width="140px"
                  prop="sheetCode"
                >
                  <el-input
                    v-model="formData.sheetCode"
                    clearable
                    :disabled="!footer"
                    placeholder="请输入底稿编号"
                    :style="{ width: '100%' }"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="底稿名称"
                  label-width="140px"
                  prop="sheetName"
                >
                  <el-input
                    v-model="formData.sheetName"
                    clearable
                    :disabled="!footer"
                    placeholder="请输入底稿名称"
                    :style="{ width: '100%' }"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="被审计单位"
                  label-width="140px"
                  prop="orgname"
                >
                  <el-input
                    v-model="formData.orgname"
                    clearable
                    placeholder="请输入被审计单位"
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
                <el-form-item
                  label="审计人员"
                  label-width="140px"
                  prop="realname"
                >
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
                    :style="{ width: '100%' }"
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
              <el-form-item
                label="审计目的"
                label-width="140px"
                prop="sheetTarget"
              >
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
                    :style="{ width: '100%' }"
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
                  label="所属类型"
                  label-width="140px"
                  prop="belongType"
                >
                  <el-select
                    v-model="formData.belongType"
                    placeholder="请选择所属类型"
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
                    :disabled="!footer"
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
                    :disabled="!footer"
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
                  <el-input
                    v-model="formData.relatedMoney"
                    clearable
                    placeholder="请输入金额"
                    :style="{ width: '100%' }"
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
              <el-form-item
                label="审计分项"
                label-width="140px"
                prop="targetName"
              >
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
                    style="width: 260px"
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
                    style="width: 260px"
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
                    style="width: 256px"
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
                  label="业务单元"
                  label-width="140px"
                  prop="businessType"
                >
                  <el-input
                    v-model="formData.businessType"
                    clearable
                    placeholder="请选择业务单元"
                    style="width: 256px"
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
              <el-col :span="24">
                <el-form-item
                  label="内部问题类型"
                  label-width="140px"
                  prop="internalType"
                >
                  <el-checkbox-group v-model="formData.internalType">
                    <el-checkbox
                      v-for="item in SJWTData"
                      :key="item.typeId"
                      :label="item.typeId"
                      :disabled="!footer"
                    >
                      {{ item.auditType }}
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-if="formData.riskLevel == '是'">
                <el-form-item
                  label="问题标题"
                  label-width="140px"
                  prop="problemTitle"
                >
                  <el-input
                    v-model="formData.problemTitle"
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
                <el-form-item
                  label="审计备忘录"
                  label-width="140px"
                  prop="sjbwl"
                >
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
                      v-model="row.reportConcent"
                      :disabled="!footer"
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
                <el-table-column label="操作" v-if="!footer" width="80px">
                  <template slot-scope="scope">
                    <el-button
                      type="text"
                      :disabled="!footer"
                      @click="handleDeletByid(scope.row, scope.$index)"
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
                    prop="projectName"
                  />
                  <el-table-column
                    align="center"
                    label="审计（调查）事项标题"
                    prop="auditMatter"
                  />
                  <el-table-column
                    align="center"
                    label="审计（调查）事项概述"
                    prop="auditAbstract"
                  />
                  <el-table-column
                    align="center"
                    label="证据提供者"
                    prop="certificateUser"
                  />
                  <el-table-column
                    align="center"
                    label="日期"
                    prop="certificateDate"
                    :formatter="formatDate"
                  />
                  <el-table-column
                    align="center"
                    label="操作"
                    show-overflow-tooltip
                    width="120"
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
                  <el-table-column
                    align="center"
                    label="缺陷性质"
                    prop="bugproperty"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    align="center"
                    label="缺陷级别"
                    prop="bugcrilevel"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    align="center"
                    label="操作"
                    show-overflow-tooltip
                    width="120"
                  >
                    <template #default="{ row }">
                      <el-button
                        v-if="footer"
                        type="text"
                        @click="$refs['bug'].showEdit('edit', row)"
                      >
                        修改
                      </el-button>
                      <el-button
                        type="text"
                        @click="handleDeleteBug(row)"
                        v-if="footer"
                      >
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
                <div
                  style="text-align: right; margin-bottom: 5px"
                  v-if="footer"
                >
                  <el-upload
                    class="upload-demo"
                    :show-file-list="false"
                    :action="baseApi + api"
                    :headers="headers"
                    :on-preview="handlePreview"
                    :on-success="handleSuccess"
                    :file-list="tableData"
                    :before-upload="handleBeforeUpload"
                  >
                    <el-button type="success">上传</el-button>
                  </el-upload>
                </div>
                <el-table :data="tableData">
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
                      <el-button type="text" @click="handleDown(row)">
                        下载
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
                <el-col
                  :span="24"
                  v-if="aoptionList.length"
                  style="margin-bottom: 16px"
                >
                  <h3>审批意见</h3>
                  <el-table :data="aoptionList">
                    <el-table-column
                      align="center"
                      label="审批人"
                      width="200px"
                      prop="staffidName"
                    />
                    <el-table-column
                      align="center"
                      label="意见"
                      prop="optDesc"
                    />
                    <el-table-column
                      align="center"
                      label="时间"
                      prop="createDate"
                      :formatter="
                        (e) => {
                          return dayjs(e.createDate).format(
                            'YYYY-MM-DD HH:mm:ss'
                          )
                        }
                      "
                    />
                    <el-table-column
                      align="center"
                      label="结果"
                      prop="optState"
                    />
                  </el-table>
                </el-col>

                <el-col :span="24">
                  <el-divider>审批意见</el-divider>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="审批意见" prop="optDesc">
                    <el-input
                      type="textarea"
                      :rows="2"
                      :disabled="false"
                      placeholder="请输入审批意见"
                      v-model="formData.optDesc"
                    ></el-input>
                  </el-form-item>
                </el-col>
              </el-col>
            </el-form>
            <!-- 复核人选择 -->
            <projectManage ref="manage" @reviewTypeSelect="reviewTypeSelect" />
            <DepartmentOption
              ref="DepartmentOptions"
              :multiSelect="true"
              @selectByTable="selected"
            />
          </el-row>
          <!-- <repeat-user :show-repeat="showRepeat"></repeat-user> -->
        </el-tab-pane>
        <el-tab-pane label="审批查看" name="second">
          <img :src="imgSrc" alt="" />
          <el-table :data="evaluationTableData">
            <el-table-column
              align="center"
              label="流程ID"
              width="100"
              prop="processName"
            ></el-table-column>
            <el-table-column align="center" label="办理人" prop="approver" />

            <el-table-column
              align="center"
              label="办理角色"
              prop="approvalrole"
              show-overflow-tooltip
              width="150"
            />
            <el-table-column
              align="center"
              label="办理结果"
              prop="result"
              show-overflow-tooltip
              width="120"
            />
            <el-table-column
              align="center"
              label="办理意见"
              prop="examination"
              show-overflow-tooltip
              width="120"
            />
            <el-table-column
              align="center"
              label="办理时间"
              prop="approvaldate"
              show-overflow-tooltip
              width="120"
              :formatter="formatDate"
            />
            <el-table-column
              align="center"
              label="下一步:办理人/办理角色"
              prop="handle"
              show-overflow-tooltip
              width="120"
            />
          </el-table>
        </el-tab-pane>
      </el-tabs>

      <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div> -->
      <template #footer v-if="activeName === 'first'">
        <div style="text-align: left">
          <el-button type="primary" v-if="footer" @click="add">保存</el-button>
          <el-button
            v-for="item in btnList"
            :key="item"
            type="primary"
            @click="handleBtn(item)"
          >
            {{ item === null ? '提交' : item }}
          </el-button>
        </div>
      </template>
    </el-dialog>
    <hzdg ref="edit" @getInfoFromModal="getInfoFromModal" />
    <QRSModal ref="table" @selected="setTable2" />
    <YWDYModal ref="YWDY" @selected="YWDYInfo" />
    <BugModal ref="bug" @bugdata="bugdata" />
  </div>
</template>

<script>
  import {
    deleteSheetReport,
    download,
    getBugList,
    getQZSInfo,
    getSheetApprovalInfo,
    getSJWTTypeDatas,
    myDraftFileList,
    myDraftSave,
    saveDealRecordApporvalInfo,
    whetherLeader,
  } from '@/api/audit/implement'
  import { defectDel } from '@/api/audit/question'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { formatDay, paramObj } from '@/utils/index'
  import hzdg from '@/views/audit/implement/components/hZDGmodal.vue'
  import DepartmentOption from '@/views/audit/implement/components/options/department.vue'
  import YWDYModal from '@/views/audit/implement/components/options/YWDYModal.vue'
  import QRSModal from '@/views/audit/implement/components/QRSModal.vue'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import BugModal from '@/views/audit/question/components/FlawInfo.vue'
  import * as dayjs from 'dayjs'
  export default {
    name: 'PapersDetails111',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        dayjs: dayjs,
        dialogFormVisible: false,
        baseApi:
          process.env.NODE_ENV === 'development'
            ? '/vab-mock-server/audit'
            : process.env.VUE_APP_BASE_API,
        api: '/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        rules: {
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
          orgname: [
            {
              required: true,
              message: '请输入被审计单位',
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
          optDesc: [
            { required: true, message: '请输入审批意见', trigger: 'change' },
          ],
          auditDiscoverable: [],
        },
        baseURL: baseURL,
        taskId: '',
        imgSrc: '',
        formData: {
          sheetCode: undefined,
          sheetName: undefined,
          orgname: undefined,
          businessAffiliation: undefined,
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
          hzdg: '是',
          relationsheetids: undefined,
          internalType: [],
        },
        cyObj: {},
        footer: true,
        tableData: [],
        activeName: 'first',
        title: '复核',
        reviewType: '',
        btnList: [],
        formDisable: true,
        sheetid: '',
        aoptionList: [],
        reportData: [],
        showHZDGButton: true,
        showHZDG: false,
        showBelongType: false, //是否显示所属类型
        showDetailType: false, //是否显示问题类型
        showOtherType: false,
        showMoneyInput: false, //是否显示金额输入框
        showhgDetailType: false, //是否显示合规性分类
        SJWTData: [], //审计问题的数据
        tableData2: [], //审计取证单
        BugtableData: [],
        processDefinitionId: '',
        processInstanceId: '',
        staffID: '',
        taskID: '',
        cyId: '',
        evaluationTableData: [],
      }
    },

    components: {
      projectManage,
      DepartmentOption,
      hzdg,
      QRSModal,
      YWDYModal,
      BugModal,
    },
    computed: {
      userInfo() {
        let userInfo = window.localStorage.getItem('userInfo')
        return userInfo ? JSON.parse(userInfo) : undefined
      },
    },
    mounted() {},
    methods: {
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
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
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
      handleSelect(e) {
        this.$refs['DepartmentOptions'].show(e)
      },
      async handleBtn(item) {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let obj = {
              cyId: +this.cyId,
              sheetid: +this.staffID,
              optDesc: this.formData.optDesc,
              taskId: this.taskID,
              transition: item || '提交',
              processDefinitionId: this.processDefinitionId,
              processInstanceId: +this.processInstanceId,
            }
            const res = await saveDealRecordApporvalInfo(obj)
            if (res.code === 1) {
              this.$message.success(res.msg)
            } else {
              this.$message.error(res.msg)
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
      },
      async getImg(taskId) {
        this.imgSrc =
          await `${this.baseURL}/audit/nbsjapproval/picture?taskId=${taskId}`
      },
      handleTabClick(e) {
        // this.activeName = e
      },
      reviewTypeSelect(e) {
        this.$set(this.formData, `${e.reviewType}Name`, e.id[0].realname)
        this.$set(this.formData, `${e.reviewType}`, e.id[0].staffid)
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
      async showEdit(title, row) {
        //获取审计问题类型数据
        let resss = await getSJWTTypeDatas()
        this.SJWTData = resss.data.data
        let res11 = await whetherLeader() //判断是否为组长
        if (res11.data.ifLeader) {
          this.showHZDG = true
          this.showHZDGButton = true
          this.formData.hzdg = '是'
        } else {
          this.showHZDG = false
          this.showHZDGButton = false
        }

        this.reportData = []
        this.dialogFormVisible = true
        this.activeName = 'first'
        this.sheetid = paramObj(row.cyurl).spid
        await this.getImg(row.taskid)
        this.footer = false
        let obj = {
          cyId: row.cyid,
          sheetid: this.sheetid,
          // taskId: row.taskid,
        }
        const form = await getSheetApprovalInfo(obj)
        //保存参数，用于审批
        this.processDefinitionId = form.data.task
          ? form.data.task.processDefinitionId
          : ''
        this.processInstanceId = form.data.task
          ? form.data.task.processInstanceId
          : ''

        //保存数据用于通过或者驳回按钮
        this.staffID = form.data.cy.taskid
        this.taskID = form.data.task ? form.data.task.taskId : ''
        this.cyId = form.data.cy.cyid
        this.taskId = row.taskid
        //获取流程图以及流程列表
        this.imgSrc = form.data.url
        this.evaluationTableData = form.data.taskList
        // //获取关联缺陷列表
        let bugList = await getBugList({ sheetid: form.data.sheet.sheetId })
        this.BugtableData = bugList.data.data
        //获取取证单列表信息
        getQZSInfo({ sheetid: form.data.sheet.sheetId }).then((res) => {
          this.tableData2 = res.data.data || []
        })
        //是否发现问题判断
        if (form.data.sheet.riskLevel === '是') {
          this.showDetailType = true
          this.showBelongType = true
        } else {
          this.showBelongType = false
          this.showDetailType = false
          this.showMoneyInput = false
          this.showhgDetailType = false
        }
        //问题类型判断
        if (form.data.sheet.detailType === 2) {
          this.showhgDetailType = true
        } else if (form.data.sheet.detailType === 1) {
          this.showhgDetailType = false
        }
        //判断所属类型
        if (form.data.sheet.belongType === 2) {
          this.showOtherType = true
          this.showDetailType = false
          this.showMoneyInput = false
        } else if (form.data.sheet.belongType === 1) {
          this.showMoneyInput = true
          this.showOtherType = false
          this.showDetailType = true
        }
        this.formData = form.data.sheet
        this.formData.businessType =
          form.data.sheet.businessType &&
          form.data.sheet.businessType.toString()
        this.formData.belongType =
          form.data.sheet.belongType && form.data.sheet.belongType.toString()
        this.formData.detailType =
          form.data.sheet.detailType && form.data.sheet.detailType.toString()
        this.formData.hgDetailType =
          form.data.sheet.hgDetailType &&
          form.data.sheet.hgDetailType.toString()
        this.formData.internalType =
          form.data.sheet.internalType &&
          form.data.sheet.internalType.split(',').map((item) => {
            return +item
          })

        this.reportData = form.data.listSP
        this.cyObj = form.data.cy
        this.btnList = form.data.btnList ? form.data.btnList : ['提交']
        this.aoptionList = form.data.aoptionList || []
        this.formData.orgname =
          form.data.project.auditStaffName || form.data.project.auditOrgName

        if (
          Number(this.cyObj.cyStaffid) === this.userInfo.staffid &&
          this.cyObj.cystate === '需调整'
        ) {
          this.footer = true
        }
        this.getFileList(this.formData.sheetId)
      },
      async getFileList(sheetId) {
        const data = await myDraftFileList({ sheetid: sheetId })
        this.tableData = data.data.data || []
      },
      close() {
        this.$refs['ruleForm'].resetFields()
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
        this.tableData2 = []
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
            attids = attids.substring(0, attids.length - 1)
            certificateIds = certificateIds.substring(
              0,
              certificateIds.length - 1
            )
            const bugInfo = this.BugtableData.map((item) => item.bugid)
            // const { createTime, state, ...other } = this.formData
            let obj = { ...this.formData }
            delete obj.createStaff
            delete obj.createTime
            const data = await myDraftSave({
              ...obj,
              sheetId: this.sheetid,
              attids,
              internalType: this.formData.internalType.toString(),
              certificateIds,
              bugIds: bugInfo.toString(),
            })
            if (data.msg == '成功') {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
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
      handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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
      },
      selectDetailType(e) {
        if (e === '2') {
          this.showhgDetailType = true
        } else {
          this.showhgDetailType = false
        }
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
        this.$refs['table'].showEdit()
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
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          //删除对应的id
          this.tableData2.splice(index, 1)
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
        })
      },
      selectYWDY() {
        this.$refs['YWDY'].show()
      },
      YWDYInfo(info) {
        this.targetId = info.operateid
        this.$set(this.formData, 'businessType', info.businessType)
        this.$set(this.formData, 'suditProcess', info.suditProcess)
      },
      addBug() {
        this.$refs['bug'].showEdit('add', null, this.formData.businessType)
      },
      bugdata(info) {
        this.BugtableData = info
        this.bugId = info[0].bugid
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
