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
          label-width="100px"
          :model="formData"
          :rules="rules"
          size="mini"
          :disabled="allDisabled"
        >
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
                >
                  {{ item.auditType }}
                </el-checkbox>
              </el-checkbox-group>
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
                prop="projectName"
              />
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
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-success="handleSuccess"
                :file-list="tableData"
                :before-upload="handleBeforeUpload"
              >
                <el-button type="success">上传</el-button>
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
      <div slot="footer" v-if="footer">
        <el-button @click="close">取消</el-button>
        <el-button @click="add" type="primary">确定</el-button>
      </div>
      <template #footer v-if="footer">
        <el-button @click="close">关 闭</el-button>
        <el-button @click="add" type="primary">确定</el-button>
        <!-- <el-button
          v-if="
            (formData.state == 2 || formData.state == 3) &&
            jurisdictionCode == 1
          "
          @click="ymsubmit"
          type="primary"
        >
          提交
        </el-button> -->
      </template>
    </el-dialog>

    <hzdg ref="edit" @getInfoFromModal="getInfoFromModal" />
    <QRSModal ref="table" @selected="setTable2" />
    <YWDYModal ref="YWDY" @selected="YWDYInfo" />
    <BugModal ref="bug" @bugdata="bugdata" />

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
  } from '@/oapi/audit/implement'
  import { currSsProject } from '@/oapi/audit/preparation'
  import { defectDel } from '@/oapi/audit/question'
  import store from '@/store'
  import { formatDate, formatDay } from '@/utils'
  import projectManage from '@/views/oilAudit/project/components/formComponents/projectManage.vue'
  import BugModal from '@/views/oilAudit/question/components/FlawInfo.vue'
  import hzdg from './hZDGmodal.vue'
  import DepartmentOption from './options/department.vue'
  import YWDYModal from './options/YWDYModal.vue'
  import QRSModal from './QRSModal.vue'
  const { baseURL } = require('@/config')
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
    getPrivewAttInfo,
  } from '@/oapi/contract/manage'
  import { getFaqiInfo } from '@/oapi/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'

  export default {
    name: '',
    inheritAttrs: false,
    components: { CandidateUserSelect },
    async created() {},

    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          sheetCode: undefined,
          sheetName: undefined,
          sheettype: undefined,
          orgIdNames: undefined,
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
          realname: undefined,
          hzdg: '是',
          relationsheetids: '',
          internalType: [],
          state: 0,
          orgIds: '',
          ejfh: '',
          yjfh: '',
        },
        footer: true,
        allDisabled: false,
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
        projectId: '',
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
    },

    methods: {
      // 获取当前实施项目
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
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
      async showEdit(title, row) {
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
        if (row && row.sheet) {
          this.sheetID = row.sheet.sheetId
          //获取取证单列表信息
          getQZSInfo({ sheetid: row.sheet.sheetId }).then(
            (res) => (this.tableData2 = res.data.data)
          )

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
          this.formData.internalType = row.sheet.internalType
            ? row.sheet.internalType.split(',').map((item) => {
                return +item
              })
            : []
          this.$forceUpdate()
          this.reportData = row.listSP
          this.getFileList(row.sheet.sheetId)
        }

        if (title == 'edit') {
          this.title = '编辑'
          //获取关联缺陷列表
          let bugList = await getBugList({ sheetid: row.sheet.sheetId })
          this.BugtableData = bugList.data.data

          // if (row.sheet.state == 2 || row.sheet.state == 3) {
          //   const res2 = await getFlowTaskInfo({
          //     tableId: 10,
          //     formId: row.sheet.sheetId,
          //   })
          //   this.jurisdictionCode = res2.data.isFlowInfo
          //   if (res2.data.isFlowInfo) {
          //     this.flowtaskinfoflowid = res2.data.flowId
          //     this.fromId = row.sheet.sheetId
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
        } else if (title == 'detail') {
          //获取关联缺陷列表
          let bugList = await getBugList({ sheetid: row.sheet.sheetId })
          this.BugtableData = bugList.data.data
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
      close() {
        this.$refs['ruleForm'].resetFields()
        this.clearType = true
        this.$emit('fetch-data')
        this.dialogFormVisible = false
        this.tableData = []
        this.tableData2 = []
        this.footer = true
        this.showType = false
        this.allDisabled = false
        this.sheetID = ''
        this.formData.auditDesc = ''
        this.formData.suditProcess = ''
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
              internalType: this.formData.internalType
                ? this.formData.internalType.toString()
                : '',
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
        this.$message.success('删除成功')
      },
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
        this.$set(this.formData, 'businessAffiliation', info.riskSource)
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
      //提交
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
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
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
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
