<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="160px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-tabs v-model="activeName" type="card">
          <el-tab-pane label="整改落实" name="first">
            <el-col :span="12">
              <el-form-item label="问题编号" prop="issues.issuesCode">
                <el-input
                  v-model="formData.issues.issuesCode"
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
                  v-model="formData.issues.auditObjectName"
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
                  v-model="formData.issues.issuesType"
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
                  v-model="formData.issues.createStaffName"
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
                  v-model="formData.issues.questionMemo"
                  clearable
                  placeholder="请输入问题详情"
                  disabled
                  type="textarea"
                  rows="4"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="整改通知" prop="plan.rectificationPlan">
                <el-input
                  v-model="formData.plan.rectificationPlan"
                  clearable
                  placeholder="请输入整改通知"
                  :style="{ width: '100%' }"
                  type="textarea"
                  rows="4"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="成果体现" prop="plan.resultMemo">
                <el-input
                  v-model="formData.plan.resultMemo"
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
                  v-model="formData.plan.deadline"
                  value-format="yyyy-MM-dd"
                  :style="{ width: '256px' }"
                  type="date"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider></el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="问题来源" prop="reimpl.problemsrc">
                <el-input
                  placeholder="请输入问题来源"
                  v-model="formData.reimpl.problemsrc"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="审计报告出具年份" prop="reimpl.reportyear">
                <el-date-picker
                  :style="{ width: '100%' }"
                  v-model="formData.reimpl.reportyear"
                  placeholder="审计报告出具年份"
                  type="year"
                  format="yyyy"
                  value-format="yyyy"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="问题类别" prop="reimpl.problemtype">
                <!-- <el-input
                  placeholder="请输入问题类别"
                  v-model="formData.reimpl.problemtype"
                ></el-input> -->
                <el-select
                  v-model="formData.reimpl.problemtype"
                  style="width: 100%"
                >
                  <el-option
                    v-for="(item, index) in questionTypeList"
                    :key="index"
                    :label="item.auditType"
                    :value="item.auditType"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>单位名称</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="具体责任单位">
                <el-input
                  v-model="formData.issues.auditObjectName"
                  clearable
                  placeholder="请输入具体责任单位"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="一级单位" prop="reimpl.oneorgname">
                <el-input
                  v-model="formData.reimpl.oneorgname"
                  clearable
                  placeholder="请输入一级单位"
                  style="width: 78%"
                  disabled
                />
                <el-button
                  @click="$refs.audiTree.showEdit()"
                  style="margin-left: 10px"
                  type="primary"
                  :disabled="!footer"
                  size="small"
                >
                  选择
                </el-button>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-divider>问题在审计报告中的序号及描述</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="一级标题" prop="reimpl.onetitle">
                <el-input
                  placeholder="请输入一级标题"
                  v-model="formData.reimpl.onetitle"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="二级标题" prop="reimpl.twotitle">
                <el-input
                  placeholder="请输入二级标题"
                  v-model="formData.reimpl.twotitle"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="三级标题" prop="reimpl.threetitle">
                <el-input
                  placeholder="请输入三级标题"
                  v-model="formData.reimpl.threetitle"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否销号" prop="reimpl.isxh">
                <el-select v-model="formData.reimpl.isxh" style="width: 100%">
                  <el-option label="否" value="0" />
                  <el-option label="是" value="1" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item
                label="在审计报告中的表述"
                prop="reimpl.reportexpression"
              >
                <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="请输入在审计报告中的表述"
                  v-model="formData.reimpl.reportexpression"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-divider>整改责任清单</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="具体责任单位">
                <el-input
                  v-model="formData.issues.auditObjectName"
                  clearable
                  placeholder="请输入具体责任单位"
                  :style="{ width: '256px' }"
                  disabled
                />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="问题金额(万元)" prop="reimpl.quemoney">
                <el-input
                  placeholder="请输入问题金额"
                  v-model="formData.reimpl.quemoney"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="负责监督管理责任的主管部门"
                prop="reimpl.supervision"
              >
                <el-input
                  placeholder="请输入负责监督管理责任的主管部门"
                  v-model="formData.reimpl.supervision"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="具体问题表述" prop="reimpl.queexpression">
                <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="请输入具体问题表述"
                  v-model="formData.reimpl.queexpression"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>整改目标清单</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="整改类型" prop="reimpl.recttype">
                <el-select
                  v-model="formData.reimpl.recttype"
                  style="width: 100%"
                >
                  <el-option label="立行立改" value="立行立改" />
                  <el-option label="分阶段整改" value="分阶段整改" />
                  <el-option label="持续整改" value="持续整改" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="法规政策依据" prop="reimpl.legalbasis">
                <el-input
                  placeholder="请输入法规政策依据"
                  v-model="formData.reimpl.legalbasis"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="整改时限" prop="reimpl.recttimelimit">
                <el-date-picker
                  :style="{ width: '100%' }"
                  v-model="formData.reimpl.recttimelimit"
                  placeholder="整改时限"
                  type="date"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="整改要求" prop="reimpl.rectdemand">
                <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="请输入整改要求"
                  v-model="formData.reimpl.rectdemand"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>具体责任单位细化的整改要求</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="整改完成标准"
                prop="reimpl.situationoverView"
              >
                <el-select
                  v-model="formData.reimpl.situationoverView"
                  style="width: 100%"
                >
                  <el-option
                    label="落实审计意见和建议"
                    value="落实审计意见和建议"
                  />
                  <el-option
                    label="完善制度与优化流程"
                    value="完善制度与优化流程"
                  />
                  <el-option
                    label="收回资金或挽回损失"
                    value="收回资金或挽回损失"
                  />
                  <el-option label="完成追责问责" value="完成追责问责" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="对应的完成时间" prop="reimpl.deadline">
                <el-date-picker
                  :style="{ width: '100%' }"
                  v-model="formData.reimpl.deadline"
                  placeholder="对应的完成时间"
                  type="date"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item
                label="细化的整改措施"
                prop="reimpl.rectificationMeasures"
              >
                <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="细化整改措施"
                  v-model="formData.reimpl.rectificationMeasures"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>具体责任单位整改责任人</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="整改第一责任人"
                prop="reimpl.firstresponstaffname"
              >
                <template slot-scope="scope">
                  <el-input
                    disabled
                    v-model="formData.reimpl.firstresponstaffname"
                    size="mini"
                    style="width: 78%"
                    placeholder="请选择整改第一责任人"
                  />
                  <el-button
                    type="primary"
                    size="mini"
                    style="margin-left: 10px"
                    @click="showGroupLeader(scope.$index)"
                    :disabled="!footer"
                  >
                    选择
                  </el-button>
                </template>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="协助整改工作的领导"
                prop="reimpl.assistleader"
              >
                <el-input
                  placeholder="请输入协助整改工作的领导"
                  v-model="formData.reimpl.assistleader"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="牵头整改部门责任人及联系电话"
                prop="reimpl.maindeptheadtel"
              >
                <el-input
                  placeholder="请输入牵头整改部门责任人及联系电话"
                  v-model="formData.reimpl.maindeptheadtel"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item
                label="配合整改部门责任人及联系电话"
                prop="reimpl.assistdeptheadtel"
              >
                <el-input
                  placeholder="请输入配合整改部门责任人及联系电话"
                  v-model="formData.reimpl.assistdeptheadtel"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="审计部门责任人及联系电话"
                prop="reimpl.auditdeptheadtel"
              >
                <el-input
                  placeholder="请输入审计部门责任人及联系电话"
                  v-model="formData.reimpl.auditdeptheadtel"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-divider>具体责任单位整改要求</el-divider>
            </el-col>

            <el-col :span="12">
              <el-form-item label="项目数(个)" prop="reimpl.pjcnt">
                <el-input
                  placeholder="请输入项目数"
                  v-model="formData.reimpl.pjcnt"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="问题整改金额(万元)" prop="reimpl.rectmoney">
                <el-input
                  placeholder="请输入问题整改金额"
                  v-model="formData.reimpl.rectmoney"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="土地,森林等面积(公顷)"
                prop="reimpl.landarea"
              >
                <el-input
                  placeholder="请输入土地,森林等面积"
                  v-model="formData.reimpl.landarea"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="矿产资源,产能等(万吨)"
                prop="reimpl.minerals"
              >
                <el-input
                  placeholder="请输入矿产资源,产能等"
                  v-model="formData.reimpl.minerals"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位(个)" prop="reimpl.orgcnt">
                <el-input
                  placeholder="请输入单位"
                  v-model="formData.reimpl.orgcnt"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="家庭(户)" prop="reimpl.familycnt">
                <el-input
                  placeholder="请输入家庭"
                  v-model="formData.reimpl.familycnt"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="人数(人)" prop="reimpl.personcnt">
                <el-input
                  placeholder="请输入人数"
                  v-model="formData.reimpl.personcnt"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="住房(套)" prop="reimpl.housecnt">
                <el-input
                  placeholder="请输入住房"
                  v-model="formData.reimpl.housecnt"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否已完成整改" prop="reimpl.conclusion">
                <el-select
                  v-model="formData.reimpl.conclusion"
                  placeholder="请选择整改结论"
                  clearable
                  style="width: 100%"
                >
                  <el-option label="未整改" value="未整改" />
                  <el-option label="已整改" value="已整改" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="已采取的整改措施" prop="reimpl.achivement">
                <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="请输入已采取的整改措施"
                  v-model="formData.reimpl.achivement"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" v-if="formData.reimpl.conclusion != '已整改'">
              <el-form-item
                label="未整改到位问题原因及下一步计划"
                prop="reimpl.nextMeasures"
              >
                <el-input
                  placeholder="请输入具体问题表述"
                  v-model="formData.reimpl.nextMeasures"
                  type="textarea"
                  :rows="4"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>(一)其中</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="追缴资金(万元)" prop="reimpl.recoverymoney">
                <el-input
                  placeholder="请输入追缴资金"
                  v-model="formData.reimpl.recoverymoney"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="归还原渠道(万元)" prop="reimpl.backmoney">
                <el-input
                  placeholder="请输入归还原渠道"
                  v-model="formData.reimpl.backmoney"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="统筹盘活(万元)" prop="reimpl.overallmoney">
                <el-input
                  placeholder="请输入统筹盘活"
                  v-model="formData.reimpl.overallmoney"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="加快拨付(万元)"
                prop="reimpl.acceleratemoney"
              >
                <el-input
                  placeholder="请输入加快拨付"
                  v-model="formData.reimpl.acceleratemoney"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="退抵税费或补缴补发(万元)"
                prop="reimpl.retrievemoney"
              >
                <el-input
                  placeholder="请输入退抵税费或补缴补发"
                  v-model="formData.reimpl.retrievemoney"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" style="height: 85px">
              <el-form-item label="调整账表(万元)" prop="reimpl.adjustmoney">
                <el-input
                  placeholder="请输入调整账表"
                  v-model="formData.reimpl.adjustmoney"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="补办手续,重签协议,停止收费等加强管理(万元)"
                prop="reimpl.reissuemoney"
              >
                <el-input
                  placeholder="请输入补办手续,重签协议,停止收费等加强管理"
                  v-model="formData.reimpl.reissuemoney"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="终止或调整金融业务服务(万元)"
                prop="reimpl.stopmoney"
              >
                <el-input
                  placeholder="请输入终止或调整金融业务服务"
                  v-model="formData.reimpl.stopmoney"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-divider>(二)其它</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="方式" prop="reimpl.otherway">
                <el-input
                  placeholder="请输入方式"
                  v-model="formData.reimpl.otherway"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="金额(万元)" prop="reimpl.othermoney">
                <el-input
                  placeholder="请输入金额"
                  v-model="formData.reimpl.othermoney"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>(三)追责问题情况</el-divider>
              <el-col :span="12">
                <el-form-item label="情形" prop="reimpl.accountabilityinfo">
                  <el-input
                    placeholder="请输入情形"
                    v-model="formData.reimpl.accountabilityinfo"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="人数" prop="reimpl.accountabilitycnt">
                  <el-input
                    placeholder="请输入人数"
                    v-model="formData.reimpl.accountabilitycnt"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-col>
            <el-col :span="24">
              <el-divider>(四)完善制度情况</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="数量(个)" prop="reimpl.institutioncnt">
                <el-input
                  placeholder="请输入数量"
                  v-model="formData.reimpl.institutioncnt"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="分修订,制定问题名称"
                prop="reimpl.institutioninfo"
              >
                <el-input
                  placeholder="请输入分修订,制定问题名称"
                  v-model="formData.reimpl.institutioninfo"
                ></el-input>
              </el-form-item>
            </el-col>

            <template v-if="routerType == 1">
              <el-col :span="24">
                <el-divider>整改评价信息</el-divider>
              </el-col>

              <el-col :span="24">
                <el-form-item label="是否整改完成" prop="resultStatus">
                  <el-select
                    v-model="formData.resultStatus"
                    placeholder="请选择是否整改完成"
                    clearable
                  >
                    <el-option label="未整改" :value="1" />
                    <!-- <el-option label="已整改未到位" :value="2" /> -->
                    <el-option label="已整改" :value="3" />
                    <!-- <el-option label="关闭" :value="4" /> -->
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-if="formData.resultStatus == 1">
                <el-form-item label="整改截止时间" prop="rectendTime">
                  <el-date-picker
                    v-model="formData.rectendTime"
                    placeholder="选择整改截止时间"
                    value-format="yyyy-MM-dd"
                    :style="{ width: '256px' }"
                    type="date"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24" v-if="formData.resultStatus != '4'">
                <el-form-item label="检查过程" prop="inspectionProcess">
                  <el-input
                    v-model="formData.inspectionProcess"
                    clearable
                    placeholder="请输入检查过程"
                    type="textarea"
                    rows="4"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24" v-if="formData.resultStatus == '4'">
                <el-form-item label="关闭原因" prop="inspectionProcess">
                  <el-input
                    v-model="formData.inspectionProcess"
                    clearable
                    placeholder="请输入关闭原因"
                    type="textarea"
                    rows="4"
                  />
                </el-form-item>
              </el-col>
            </template>

            <el-col :span="24">
              <el-divider>上传见证材料</el-divider>
            </el-col>
            <el-col :span="24">
              <div style="text-align: right; margin-bottom: 5px" v-if="footer">
                <el-upload
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
          </el-tab-pane>
          <el-tab-pane label="整改方案" name="second">
            <el-col :span="24">
              <el-form-item label="方案类别" prop="planType">
                <el-select
                  style="width: 100%"
                  v-model="formData.plan.planType"
                  placeholder="选择方案类别"
                  :style="{ width: '256px' }"
                  disabled
                >
                  <el-option label="审计" :value="1" />
                  <el-option label="内控" :value="2" />
                  <el-option label="非系统实施" :value="3" />
                  <el-option label="外部审计" :value="4" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="方案编号" prop="planCode">
                <el-input
                  v-model="formData.plan.planCode"
                  clearable
                  placeholder="请输入方案编号"
                  :style="{ width: '256px' }"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="方案名称" prop="planName">
                <el-input
                  v-model="formData.plan.planName"
                  clearable
                  placeholder="请输入方案名称"
                  :style="{ width: '256px' }"
                  disabled
                />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="关联项目" prop="projectName">
                <el-input
                  v-model="formData.plan.projectName"
                  clearable
                  placeholder="请选择关联项目"
                  :style="{ width: '256px' }"
                  disabled
                />
                <el-button
                  :style="{ marginLeft: '10px' }"
                  type="primary"
                  disabled
                  @click="$refs.list.show(formData.plan.planType)"
                >
                  选择
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="整改经办人" prop="handlerName">
                <el-input
                  v-model="formData.plan.handlerName"
                  clearable
                  placeholder="整改经办人"
                  :style="{ width: '256px' }"
                  disabled
                />
                <el-button
                  :style="{ marginLeft: '10px' }"
                  type="primary"
                  @click="$refs.executor.show('handlerName')"
                  disabled
                >
                  选择
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="创建人" prop="createStaffName">
                <el-input
                  v-model="formData.plan.createStaffName"
                  clearable
                  placeholder="请输入创建人"
                  :style="{ width: '256px' }"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="截止时间" prop="deadlineTime">
                <el-date-picker
                  v-model="formData.plan.deadlineTime"
                  value-format="yyyy-MM-dd"
                  :style="{ width: '256px' }"
                  type="date"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注" prop="planMemo">
                <el-input
                  v-model="formData.plan.planMemo"
                  clearable
                  placeholder="请输入备注"
                  :style="{ width: '100%' }"
                  type="textarea"
                  rows="4"
                  disabled
                />
              </el-form-item>
            </el-col>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-row>

    <template #footer v-if="footer && activeName === 'first'">
      <el-button @click="close">关闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <!-- 单位组件 -->
    <company-select-modal ref="audiTree" @submit="handleCompanyTreeSelected" />
    <!-- 人员组件 -->
    <select-team
      ref="select"
      @selectTeamList="selectTeamList"
      :defaultExpandedH="3"
    ></select-team>
  </el-dialog>
</template>

<script>
  import { download, getSJWTTypeDatas } from '@/api/audit/implement'
  import {
    saveRectificationImpl,
    saveHxRectificationImpl,
    removeRectificationImplAtt,
  } from '@/api/zgzz/index.js'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import * as dayjs from 'dayjs'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import CompanySelectModal from '@/views/oilAudit/jhlx/components/department.vue'
  import selectTeam from '@/views/audit/plan/components/selectTeam.vue'

  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'FlawInfo',
    components: { CompanySelectModal, selectTeam },
    inheritAttrs: false,
    props: {
      //整改分派,整改落实传入值为1
      routerType: {
        type: Number,
        default: 0,
      },
      // 是否是后续整改页面打开
      isHxzg: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      // 金额校验规则（整数或两位小数）
      const moneyValidator = (rule, value, callback) => {
        if (value && !/^[0-9]+(\.[0-9]{1,2})?$/.test(value)) {
          callback(
            new Error('请输入正确的金额格式，只能输入整数或保留两位小数')
          )
        } else {
          callback()
        }
      }

      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        activeName: 'first',
        formData: {
          reimpl: {
            problemsrc: '', // 问题来源
            reportyear: '', // 审计报告出具年份
            problemtype: '', // 问题类别
            oneorgname: '', // 一级单位
            oneorgid: '', // 一级单位ID
            onetitle: '', // 一级标题
            twotitle: '', // 二级标题
            threetitle: '', // 三级标题
            isxh: '', // 是否销号
            reportexpression: '', // 在审计报告中的表述
            queexpression: '', // 具体问题表述
            quemoney: '', // 问题金额
            supervision: '', // 负责监督管理责任的主管部门
            recttype: '', // 整改类型
            legalbasis: '', // 法规政策依据
            rectdemand: '', // 整改要求
            recttimelimit: '', // 整改时限
            situationoverView: '', // 整改完成标准
            rectificationMeasures: '', // 细化的整改措施
            deadline: '', // 对应的完成时间
            firstresponstaffname: '', // 整改第一责任人
            assistleader: '', // 协助整改工作的领导
            maindeptheadtel: '', // 牵头整改部门责任人及联系电话
            assistdeptheadtel: '', // 配合整改部门责任人及联系电话
            auditdeptheadtel: '', // 审计部门责任人及联系电话
            achivement: '', // 已采取的整改措施
            pjcnt: '', // 项目数
            rectmoney: '', // 问题整改金额
            landarea: '', // 土地,森林等面积
            minerals: '', // 矿产资源,产能等
            orgcnt: '', // 单位数
            familycnt: '', // 家庭数
            personcnt: '', // 人数
            housecnt: '', // 住房数
            conclusion: '', // 是否已完成整改
            nextMeasures: '', // 未整改到位问题原因及下一步计划
            recoverymoney: '', // 追缴资金
            backmoney: '', // 归还原渠道
            overallmoney: '', // 统筹盘活
            acceleratemoney: '', // 加快拨付
            retrievemoney: '', // 退抵税费或补缴补发
            adjustmoney: '', // 调整账表
            stopmoney: '', // 终止或调整金融业务服务
            reissuemoney: '', // 补办手续,重签协议,停止收费等加强管理
            otherway: '', // 其他方式
            othermoney: '', // 其他金额
            accountabilityinfo: '', // 追责问题情况-情形
            accountabilitycnt: '', // 追责问题情况-人数
            institutioncnt: '', // 完善制度情况-数量
            institutioninfo: '', // 完善制度情况-分修订,制定问题名称
            attList: [], // 附件列表
          },
          issues: {},
          plan: {},
        },
        footer: true,
        tableData: [],
        rules: {
          'reimpl.problemsrc': [
            { required: true, message: '请输入问题来源', trigger: 'blur' },
          ],
          'reimpl.reportyear': [
            {
              required: true,
              message: '请选择审计报告出具年份',
              trigger: 'change',
            },
          ],
          'reimpl.problemtype': [
            { required: true, message: '请输入问题类别', trigger: 'blur' },
          ],
          'reimpl.reportexpression': [
            {
              required: true,
              message: '请输入在审计报告中的表述',
              trigger: 'blur',
            },
          ],
          'reimpl.isxh': [
            {
              required: true,
              message: '请选择是否销号',
              trigger: 'change',
            },
          ],
          'reimpl.queexpression': [
            { required: true, message: '请输入具体问题表述', trigger: 'blur' },
          ],
          'reimpl.recttype': [
            { required: true, message: '请输入整改类型', trigger: 'blur' },
          ],
          'reimpl.rectdemand': [
            { required: true, message: '请输入整改要求', trigger: 'blur' },
          ],
          'reimpl.recttimelimit': [
            { required: true, message: '请选择整改时限', trigger: 'change' },
          ],
          'reimpl.situationoverView': [
            { required: true, message: '请输入整改完成标准', trigger: 'blur' },
          ],
          'reimpl.achivement': [
            {
              required: true,
              message: '请输入已采取的整改措施',
              trigger: 'blur',
            },
          ],
          'reimpl.deadline': [
            {
              required: true,
              message: '请选择对应的完成时间',
              trigger: 'change',
            },
          ],
          'reimpl.firstresponstaffname': [
            {
              required: true,
              message: '请选择整改第一责任人',
              trigger: 'change',
            },
          ],
          'reimpl.maindeptheadtel': [
            {
              required: true,
              message: '请输入牵头整改部门责任人及联系电话',
              trigger: 'blur',
            },
          ],
          'reimpl.auditdeptheadtel': [
            {
              required: true,
              message: '请输入审计部门责任人及联系电话',
              trigger: 'blur',
            },
          ],
          'reimpl.nextMeasures': [
            {
              required: true,
              message: '请输入未整改到位问题原因及下一步计划',
              trigger: 'blur',
            },
          ],
          'reimpl.conclusion': [
            {
              required: true,
              message: '请选择是否已完成整改',
              trigger: 'change',
            },
          ],
          'reimpl.quemoney': [{ validator: moneyValidator, trigger: 'blur' }], // 问题金额

          'reimpl.pjcnt': [{ validator: moneyValidator, trigger: 'blur' }], // 项目数
          'reimpl.landarea': [{ validator: moneyValidator, trigger: 'blur' }], // 土地,森林等面积
          'reimpl.minerals': [{ validator: moneyValidator, trigger: 'blur' }], // 矿产资源,产能等
          'reimpl.orgcnt': [{ validator: moneyValidator, trigger: 'blur' }], // 单位
          'reimpl.familycnt': [{ validator: moneyValidator, trigger: 'blur' }], // 家庭
          'reimpl.personcnt': [{ validator: moneyValidator, trigger: 'blur' }], // 人数
          'reimpl.housecnt': [{ validator: moneyValidator, trigger: 'blur' }], // 住房
          'reimpl.accountabilitycnt': [
            { validator: moneyValidator, trigger: 'blur' },
          ], // 人数
          'reimpl.institutioncnt': [
            { validator: moneyValidator, trigger: 'blur' },
          ], // 数量

          'reimpl.rectmoney': [{ validator: moneyValidator, trigger: 'blur' }], // 问题整改金额
          'reimpl.recoverymoney': [
            { validator: moneyValidator, trigger: 'blur' },
          ], // 追缴资金
          'reimpl.backmoney': [{ validator: moneyValidator, trigger: 'blur' }], // 归还原渠道
          'reimpl.overallmoney': [
            { validator: moneyValidator, trigger: 'blur' },
          ], // 统筹盘活
          'reimpl.acceleratemoney': [
            { validator: moneyValidator, trigger: 'blur' },
          ], // 加快拨付
          'reimpl.retrievemoney': [
            { validator: moneyValidator, trigger: 'blur' },
          ], // 退抵税费或补缴补发
          'reimpl.adjustmoney': [
            { validator: moneyValidator, trigger: 'blur' },
          ], // 调整账表
          'reimpl.stopmoney': [{ validator: moneyValidator, trigger: 'blur' }], // 终止或调整金融业务服务
          'reimpl.reissuemoney': [
            { validator: moneyValidator, trigger: 'blur' },
          ], // 补办手续等
          'reimpl.othermoney': [{ validator: moneyValidator, trigger: 'blur' }], // 其他金额
        },
        dialogFormVisible: false,
        title: '',
        questionTypeList: [],
      }
    },
    methods: {
      async getQuestionType() {
        const { data } = await getSJWTTypeDatas()
        this.questionTypeList = data.data
      },
      showEdit(title, row) {
        this.dialogFormVisible = true
        this.title = title
        this.footer = title === 'edit'
        // 如果有传入数据，则进行数据填充
        this.getQuestionType()
        if (row) {
          // 处理 issues 数据
          if (row.issues) {
            this.formData.issues = {
              ...row.issues,
              issuesType: ['审计', '内控', '非系统实施', '外部审计'][
                Number(row.issues.issuesType) - 1
              ],
            }
          }

          // 处理 reimpl 数据
          if (row.reimpl) {
            const safeParseJson = (val) => {
              if (typeof val !== 'string') return val
              try {
                return JSON.parse(val)
              } catch (e) {
                return val
              }
            }
            const reimpl = safeParseJson(row.reimpl) || {}
            this.formData.reimpl = {
              ...this.formData.reimpl, // 保留初始化的字段
              ...reimpl, // 用传入的数据覆盖
              finishTime: reimpl.finishTime
                ? dayjs(reimpl.finishTime).format('YYYY-MM-DD')
                : '',
              deadline: reimpl.deadline
                ? dayjs(reimpl.deadline).format('YYYY-MM-DD')
                : '',
            }
            // 处理附件列表
            const attList = safeParseJson(reimpl.attList)
            this.tableData = Array.isArray(attList)
              ? JSON.parse(JSON.stringify(attList))
              : []
            this.attList = [...this.tableData]
          }
          if (row.valua) {
            // 追加评价附件到现有 tableData（不覆盖整改附件）
            const valuaAttList = Array.isArray(row.valua.attList)
              ? row.valua.attList
              : []
            this.tableData = [...this.tableData, ...valuaAttList]
            this.formData.evalId = row.valua.evalId
            this.formData.evaluaTime = row.valua.evaluaTime
              ? dayjs(row.valua.evaluaTime).format('YYYY-MM-DD')
              : undefined
            this.formData.evaluator = row.valua.evaluator
            this.formData.inspectionProcess = row.valua.inspectionProcess
            this.formData.resultStatus = row.valua.resultStatus
          }
          // 处理 plan 数据
          if (row.plan) {
            const { issuesList, attList, deadlineTime, ...other } = row.plan
            this.formData.plan = other
            this.formData.plan.deadline = deadlineTime
              ? dayjs(deadlineTime).format('YYYY-MM-DD')
              : undefined
            this.formData.plan.deadlineTime = deadlineTime
              ? dayjs(deadlineTime).format('YYYY-MM-DD')
              : undefined
            this.formData.plan.rectificationPlan = row.rectificationPlan
            this.formData.plan.rectificationMeasures = row.rectificationMeasures
            this.formData.plan.resultMemo = row.resultMemo
          }

          // 保存关联ID
          if (row.relaId) {
            this.formData.relaid = row.relaId
          }
        }

        // 表单重置后，清除验证信息
        this.$nextTick(() => {
          if (this.$refs.elForm) {
            this.$refs.elForm.clearValidate()
          }
        })
      },
      close() {
        this.$emit('closeDialog')
      },
      add() {
        this.$refs.elForm.validate(async (valid) => {
          if (valid) {
            // 处理附件
            const attListArr = []
            const existed = Array.isArray(this.attList) ? this.attList : []
            this.tableData.forEach((item) => {
              if (!existed.some((x) => x.attid === item.attid)) {
                attListArr.push(item.attid)
              }
            })
            const attIds = attListArr.join(',')
            let { createTime, updateTime, ...other } = this.formData.reimpl
            // 构建提交对象
            const obj = {
              attIds,
              implId: this.formData.implId,
              relaid: this.formData.relaid,
              ...other, // 直接解构所有 reimpl 字段
            }

            try {
              let res
              if (this.isHxzg) {
                res = await saveHxRectificationImpl(obj)
              } else {
                res = await saveRectificationImpl(obj)
              }
              if (res.code === 1) {
                this.$baseMessage('保存成功', 'success')
                this.$emit('fetch-data')
                this.close()
              } else {
                this.$baseMessage(res.msg || '保存失败', 'error')
              }
            } catch (error) {
              console.error('保存失败:', error)
              this.$baseMessage('保存失败', 'error')
            }
          } else {
            return false
          }
        })
      },

      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        await removeRectificationImplAtt({
          attId: row.attid,
          implId: this.formData.implId,
        })
        this.$baseMessage('删除成功', 'success')
        this.tableData = list
      },
      //选择单位
      handleCompanyTreeSelected(val) {
        this.$set(this.formData.reimpl, 'oneorgname', val.label)
        this.$set(this.formData.reimpl, 'oneorgid', val.id)
      },
      //选择人员
      showGroupLeader(sIndex) {
        this.sIndex = sIndex
        this.$refs['select'].showEdit('leader')
      },
      selectTeamList(val, flagTitle) {
        if (flagTitle) {
          // this.tableData[this.sIndex].leaderId = val[0].staffid
          this.$set(
            this.formData.reimpl,
            'firstresponstaffname',
            val[0].realname
          )
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
          // 更新文件列表
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
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
