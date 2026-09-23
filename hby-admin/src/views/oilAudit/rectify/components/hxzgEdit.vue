<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="200px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <!-- <el-row> -->
        <el-col :span="24">
          <el-divider>问题清单</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计报告定稿" prop="sjbgdgTitle">
            <el-input
              v-model="formData.sjbgdgTitle"
              :style="{ width: '100%' }"
              disabled
              placeholder="审计报告定稿"
            />
            <!-- <el-button @click="handlePlan" style="margin-left: 15px" type="primary" size="mini">
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题所属单位名称" prop="unitName">
            <el-input
              v-model="formData.unitName"
              clearable
              placeholder="请输入问题所属单位名称"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="在报告中的对应编号" prop="issueNumber">
            <el-input
              v-model="formData.issueNumber"
              clearable
              placeholder="请输入在报告中的对应编号"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改人" prop="rectPerName">
            <el-input
              v-model="formData.rectPerName"
              :style="{ width: '100%' }"
              clearable
              placeholder="整改人"
              disabled
            />
            <!--  <el-button @click="openPerson('rectPerName')" style="margin-left: 10px; height: 30px" type="primary">
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="事实表述" prop="issueDetail">
            <el-input
              v-model="formData.issueDetail"
              clearable
              placeholder="请输入事实表述"
              :style="{ width: '100%' }"
              disabled
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题金额（元）" prop="money">
            <el-input
              v-model="formData.money"
              clearable
              placeholder="请输入问题金额（元）"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审减金额（元）" prop="reviewMoney">
            <el-input
              v-model="formData.reviewMoney"
              clearable
              placeholder="请输入审减金额（元）"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="定性" prop="qualitative">
            <el-input
              v-model="formData.qualitative"
              clearable
              placeholder="请输入定性"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="问题定性" prop="problemQualitative">
            <el-input
              v-model="formData.problemQualitative"
              clearable
              placeholder="请输入问题定性"
              :style="{ width: '100%' }"
              disabled
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="定性法规依据" prop="qualitativeRule">
            <el-input
              v-model="formData.qualitativeRule"
              clearable
              placeholder="请输入定性法规依据"
              :style="{ width: '100%' }"
              disabled
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="处理意见或整改建议" prop="correctPropose">
            <el-input
              v-model="formData.correctPropose"
              clearable
              placeholder="请输入处理意见或整改建议"
              :style="{ width: '100%' }"
              disabled
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="问题发生年度" prop="problemYear">
            <el-date-picker
              v-model="formData.problemYear"
              placeholder="请选择问题发生年度"
              style="width: 100%"
              type="year"
              format="yyyy"
              value-format="yyyy"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改时限" prop="timeLimit">
            <el-select
              v-model="formData.timeLimit"
              placeholder="请选择整改时限"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option
                label="发审计意见及决定书之日起一个月内"
                value="发审计意见及决定书之日起一个月内"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改分类" prop="rectClass">
            <el-select
              v-model="formData.rectClass"
              placeholder="请选择整改分类"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option label="立行立改" value="立行立改"></el-option>
              <el-option label="分阶段整改" value="分阶段整改"></el-option>
              <el-option label="持续整改" value="持续整改"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="整改督促牵头部门或单位"
            prop="urgeDepartment"
            label-width="180px"
          >
            <el-input
              v-model="formData.urgeDepartment"
              clearable
              placeholder="请输入整改督促牵头部门或单位"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改责任人" prop="head">
            <el-input
              v-model="formData.head"
              clearable
              placeholder="请输入整改责任人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="operator">
            <el-input
              v-model="formData.operator"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="addTime">
            <el-date-picker
              v-model="formData.addTime"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联底稿" prop="draft">
            <el-input
              v-model="formData.draft"
              clearable
              placeholder="请选择关联底稿"
              :style="{ width: '100%' }"
              disabled
            />
            <!-- <el-button :style="{ marginLeft: '10px' }" type="primary" @click="handleDraft">
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <!-- </el-row> -->
        <el-col :span="24">
          <el-divider>问题整改</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="整改督促牵头部门或单位"
            prop="zgqtbm"
            label-width="180px"
          >
            <el-input
              v-model="formData.zgqtbm"
              clearable
              placeholder="请输入整改督促牵头部门或单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="整改时限" prop="zgsx">
            <!-- <el-input
              v-model="formData.zgsx"
              clearable
              placeholder="请输入整改时限"
              :style="{ width: '100%' }"
            /> -->
            <el-date-picker
              v-model="formData.zgsx"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="请选择整改时限"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="整改分类" prop="zgfl">
            <el-input
              v-model="formData.zgfl"
              clearable
              placeholder="请输入整改分类"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="资产损失（万元）" prop="zcss">
            <el-input
              v-model="formData.zcss"
              clearable
              placeholder="请输入资产损失（万元）"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'zcss')"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="风险程度" prop="fxcd">
            <el-input
              v-model="formData.fxcd"
              clearable
              placeholder="请输入风险程度"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="已填报累计整改金额（元）" prop="ljzgje">
            <el-input
              v-model="formData.ljzgje"
              clearable
              placeholder="请输入已填报累计整改金额（元）"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'ljzgje')"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="已填报累计经济成果（元）" prop="ljjjcg">
            <el-input
              v-model="formData.ljjjcg"
              clearable
              placeholder="请输入已填报累计经济成果（元）"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'ljjjcg')"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="当期整改状态" prop="zgzt" label-width="180px">
            <el-select
              v-model="formData.zgzt"
              placeholder="请选择当期整改状态"
              :style="{ width: '100%' }"
            >
              <el-option label="整改完毕" value="3" />
              <el-option label="正在整改" value="2" />
              <el-option label="尚未开始整改" value="1" />
              <el-option label="不接受审计意见" value="0" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="当期整改金额（元）"
            prop="dqzgje"
            label-width="180px"
          >
            <el-input
              v-model="formData.dqzgje"
              clearable
              placeholder="请输入当期整改金额（元）"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'dqzgje')"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="当期直接经济成果类型"
            prop="dqzjjjcgtype"
            label-width="180px"
          >
            <el-select
              v-model="formData.dqzjjjcgtype"
              placeholder="请选择当期直接经济成果类型"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in typeOptions"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="当期直接经济成果（元）"
            prop="dqzjjjcg"
            label-width="180px"
          >
            <el-input
              v-model="formData.dqzjjjcg"
              clearable
              placeholder="请输入当期直接经济成果（元）"
              :style="{ width: '100%' }"
              @input="setdqzjjjcg"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="当期其他经济成果类型"
            prop="dqqtjjcgtype"
            label-width="180px"
          >
            <el-input
              v-model="formData.dqqtjjcgtype"
              clearable
              placeholder="请输入当期其他经济成果类型"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="当期其他经济成果（元）"
            prop="dqqtjjcg"
            label-width="180px"
          >
            <el-input
              v-model="formData.dqqtjjcg"
              clearable
              placeholder="请输入当期其他经济成果（元）"
              :style="{ width: '100%' }"
              @input="setdqqtjjcg"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="当期整改描述" prop="dqzgms">
            <el-input
              v-model="formData.dqzgms"
              clearable
              placeholder="请输入当期整改描述"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否移送" prop="sfys" label-width="180px">
            <el-select
              v-model="formData.sfys"
              placeholder="请选择是否移送"
              :style="{ width: '100%' }"
            >
              <el-option label="是" :value="'1'" />
              <el-option label="否" :value="'0'" />
            </el-select>
          </el-form-item>
        </el-col>
        <template v-if="formData.sfys == 1">
          <el-col :span="24">
            <el-divider>移送内容</el-divider>
          </el-col>

          <el-col :span="12">
            <el-form-item
              label="移送处理事项"
              prop="tblYqnsSjzgYsnr.ysclsx"
              :rules="rules.ysclsx"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.ysclsx"
                clearable
                placeholder="请输入移送处理事项"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item
              label="其中涉及向司法机关移送或报告事项"
              prop="tblYqnsSjzgYsnr.sjsfjgyssx"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjsfjgyssx"
                clearable
                placeholder="请输入其中涉及向司法机关移送或报告事项"
                :style="{ width: '100%' }"
                @input="handleInput('sjsfjgyssx', $event)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="移送处理涉及金额（元）"
              prop="tblYqnsSjzgYsnr.ysclsjje"
            >
              <el-input
                type="number"
                v-model="formData.tblYqnsSjzgYsnr.ysclsjje"
                clearable
                placeholder="请输入移送处理涉及金额（元）"
                :style="{ width: '100%' }"
                @input="inputMoney($event, 'tblYqnsSjzgYsnr.ysclsjje')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="移送处理人员（人）"
              prop="tblYqnsSjzgYsnr.ysclr"
              :rules="rules.ysclr"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.ysclr"
                clearable
                placeholder="请输入移送处理人员（人） "
                :style="{ width: '100%' }"
                @input="handleInput('ysclr', $event)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="其中涉及向司法机关移送或报告事项涉及人员（人）"
              prop="tblYqnsSjzgYsnr.sjsfjgyssxr"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjsfjgyssxr"
                clearable
                placeholder="请输入其中涉及向司法机关移送或报告事项涉及人员（人） "
                :style="{ width: '100%' }"
                @input="handleInput('sjsfjgyssxr', $event)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="落实移送处理事项"
              prop="tblYqnsSjzgYsnr.lsysclsx"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.lsysclsx"
                clearable
                placeholder="请输入落实移送处理事项"
                :style="{ width: '100%' }"
                @input="handleInput('lsysclsx', $event)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="移送处理落实情况（人）"
              prop="tblYqnsSjzgYsnr.yscllsqk"
              :rules="rules.yscllsqk"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.yscllsqk"
                clearable
                placeholder="请输入移送处理落实情况（人）"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="其中涉及党纪处分（人）"
              prop="tblYqnsSjzgYsnr.sjdjcf"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjdjcf"
                clearable
                placeholder="请输入其中涉及党纪处分（人）"
                :style="{ width: '100%' }"
                @input="handleInput('sjdjcf', $event)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="其中涉及政务处分（人）"
              prop="tblYqnsSjzgYsnr.sjzwcf"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjzwcf"
                clearable
                placeholder="请输入其中涉及政务处分（人）"
                :style="{ width: '100%' }"
                @input="handleInput('sjzwcf', $event)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="其中涉及内部纪律处分（人）"
              prop="tblYqnsSjzgYsnr.sjnbjlcf"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjnbjlcf"
                clearable
                placeholder="请输入其中涉及内部纪律处分（人）"
                :style="{ width: '100%' }"
                @input="handleInput('sjnbjlcf', $event)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="其他经济处分（人）"
              prop="tblYqnsSjzgYsnr.qtjjcf"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.qtjjcf"
                clearable
                placeholder="请输入其他经济处分（人）"
                :style="{ width: '100%' }"
                @input="handleInput('qtjjcf', $event)"
              />
            </el-form-item>
          </el-col>
        </template>
        <el-col :span="24">
          <el-divider>上传审计整改报告及相关附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              v-if="!formDisabled"
              @click="handleEditTable1(null)"
            >
              新增
            </el-button>
          </div>
          <el-table :data="tableData1">
            <el-table-column align="center" label="序号" type="index" />
            <el-table-column align="center" label="文号" prop="document">
              <template #default="{ row, $index }">
                <el-button type="text" @click="handleEditTable1(row, true)">
                  {{ row.document }}
                </el-button>
                <!-- <span v-else>
                  {{ row.document }}
                </span> -->
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="标题"
              prop="title"
            ></el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  @click="handleEditTable1(row)"
                  :disabled="formDisabled"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handletDeleteTable1(row, $index)"
                  :disabled="formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>审计整改统计信息维护</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              v-if="!formDisabled"
              @click="handleEditTable2(null)"
            >
              新增
            </el-button>
          </div>
          <el-table :data="tableData2">
            <el-table-column align="center" label="序号" type="index" />
            <el-table-column
              align="center"
              label="制订规章制度数"
              prop="zdgzzds"
            >
              <!-- <template #default="{ row, $index }">
                <el-button type="text" @click="handleEditTable2(row, true)">
                  {{ row.zdgzzds }}
                </el-button>
              </template> -->
              <template #default="{ row, $index }">
                <el-button type="text" @click="handleEditTable2(row, true)">
                  {{ row.zdgzzds }}
                </el-button>
                <!-- <span v-else>
                  {{ row.zdgzzds }}
                </span> -->
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="修订规章制度数"
              prop="xggzzds"
            ></el-table-column>

            <el-table-column
              align="center"
              label="移送处理事项"
              prop="ysclsx"
            ></el-table-column>
            <el-table-column
              align="center"
              label="其中涉及向司法机关移送或报告事项"
              prop="sjsfjgysbg"
            ></el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <!-- <el-button
                  type="text"
                  @click="handleEditTable2( row, $index)"
                  :disabled="formDisabled"
                >
                  编辑
                </el-button> -->
                <el-button
                  type="text"
                  @click="handleDeleteTable2(row, $index)"
                  :disabled="formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24" class="title">
          <el-divider>制/修订规章制度情况说明及文件</el-divider>
        </el-col>
        <el-col :span="24">
          <el-form-item label="制/修订规章制度情况说明" prop="dqzgms">
            <el-input
              v-model="formData.gzzdqksm"
              clearable
              placeholder="请输入制/修订规章制度情况说明"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccessGzzdqksm"
              :file-list="tableDataGzzdqksm"
            >
              <el-button type="success">上传</el-button>
            </el-upload> -->
            <el-upload
              style="text-align: right; margin-bottom: 5px"
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccessFile"
              :file-list="fileList"
              :before-upload="handleBeforeUploadFile"
              :multiple="true"
            >
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
          <el-table :data="tableDataGzzdqksm">
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
                  @click="handleDeleteGzzdqksm(row)"
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!-- <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              v-if="!formDisabled"
              @click="handleEditTable3(null)"
            >
              新增
            </el-button>
          </div>
          <el-table :data="tableData3">
            <el-table-column align="center" label="序号" type="index" />
            <el-table-column
              align="center"
              label="制/修订规章制度情况说明"
              prop="gzzdqksm"
            >
              <template #default="{ row, $index }">
                <el-button type="text" @click="handleEditTable3(row, true)">
                  {{ row.gzzdqksm }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }"> 
                <el-button
                  type="text"
                  @click="handleDeleteTable3(row, $index)"
                  :disabled="formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload> -->
            <el-upload
              style="text-align: right; margin-bottom: 5px"
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccessFile2"
              :file-list="fileList2"
              :before-upload="handleBeforeUploadFile2"
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
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDowns(row)">下载</el-button>
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
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>

    <scsjbgEdit ref="table1" @setTable="setTable1" />
    <sjzgtjxxEdit ref="table2" @setTable="setTable2" />
    <sjzgtjxxEditNext ref="table3" @setTable="setTable3" />
  </el-dialog>
</template>

<script>
  import {
    editInfo,
    getDetailInfor,
    deleteFileInfo,
    deleteYsAttach,
  } from '@/oapi/yqns_sjzg/wtzg'
  import { getDetailInfo } from '@/oapi/yqns_sjzg/wtqd'
  import { deleteInfo as deleteInfosjzgbg } from '@/oapi/yqns_sjzg/sjzgbg'
  import { deleteInfo as deleteInfosjzgtjxx } from '@/oapi/yqns_sjzg/sjzgtjxx'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { download } from '@/oapi/audit/report'
  import scsjbgEdit from './scsjbgEdit'
  import sjzgtjxxEdit from './sjzgtjxxEdit'
  import sjzgtjxxEditNext from './sjzgtjxxEditNext'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'wtzgEdit',
    components: { scsjbgEdit, sjzgtjxxEdit, sjzgtjxxEditNext },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        fileList2: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          reportnum: '',
          unitName: '',
          wtdx: '',
          ssms: '',
          clyj: '',
          wtje: '',
          gldg: '',
          dx: '',
          dxfgyj: '',
          zgqtbm: '',
          zgsx: '',
          zgfl: '',
          zcss: '',
          fxcd: '',
          ljzgje: '',
          ljjjcg: '',
          dqzgzt: '',
          dqzgje: '',
          dqzjjjcgtype: '',
          dqzjjjcg: '',
          dqqtjjcgtype: '',
          dqqtjjcg: '',
          dqzgms: '',
          sfys: '',
          tblYqnsSjzgYsnr: {
            sjsfjgyssx: '',
            ysclsx: '',
            ysclsjje: '',
            ysclr: '',
            sjsfjgyssxr: '',
            lsysclsx: '',
            yscllsqk: '',
            sjdjcf: '',
            sjzwcf: '',
            sjnbjlcf: '',
            qtjjcf: '',
            cjr: '',
            cjsj: '',
          },
          wtzgid: '',
          attIds: [],
        },
        formDisabled: true,
        tableData: [],
        tableDataGzzdqksm: [],
        // rules: {
        //   sfys: [
        //     {
        //       required: true,
        //       message: '请选择是否移送',
        //       trigger: 'change',
        //     },
        //   ],
        // },
        dialogFormVisible: false,
        title: '新增',
        typeOptions: [
          {
            label: '节约各类开支',
            value: '节约各类开支',
          },
          {
            label: '建设工程项目审减',
            value: '建设工程项目审减',
          },
          {
            label: '经济合同审减',
            value: '经济合同审减',
          },
          {
            label: '物资采购项目审减',
            value: '物资采购项目审减',
          },
          {
            label: '招投标审减',
            value: '招投标审减',
          },
          {
            label: '剔除联合账簿不合理费用',
            value: '剔除联合账簿不合理费用',
          },
          {
            label: '挽回各类损失',
            value: '挽回各类损失',
          },
          {
            label: '挽回债券性和股权性投资损失',
            value: '挽回债券性和股权性投资损失',
          },
          {
            label: '挽回货币性资产损失',
            value: '挽回货币性资产损失',
          },
          {
            label: '挽回非货币性资产损失',
            value: '挽回非货币性资产损失',
          },
          {
            label: '内部收缴',
            value: '内部收缴',
          },
          {
            label: '罚款金额',
            value: '罚款金额',
          },
          {
            label: '收缴小金库',
            value: '收缴小金库',
          },
          {
            label: '账外资金',
            value: '账外资金',
          },
          {
            label: '收缴/罚没其他违规违纪资金',
            value: '收缴/罚没其他违规违纪资金',
          },
        ],
        tableData1: [],
        tableData2: [],
        tableData3: [],
        row: {},
      }
    },
    computed: {
      rules() {
        return {
          sfys: [
            {
              required: true,
              message: '请选择是否移送',
              trigger: 'change',
            },
          ],
          ysclsx: !!this.formData.tblYqnsSjzgYsnr.sjsfjgyssx
            ? [
                {
                  required: true,
                  message: '请输入移送处理事项',
                  trigger: 'blur',
                },
                { validator: this.isNumber, trigger: 'blur' },
              ]
            : [],
          sjsfjgyssx: [
            {
              required: true,
              message: '请输入其中涉及向司法机关移送或报告事项',
              trigger: 'blur',
            },
            { validator: this.isNumber, trigger: 'blur' },
          ],
          ysclsjje: [
            {
              required: true,
              message: '请输入移送处理涉及金额（元）',
              trigger: 'blur',
            },
          ],
          ysclr: !!this.formData.tblYqnsSjzgYsnr.sjsfjgyssx
            ? [
                {
                  required: true,
                  message: '请输入移送处理人员（人）',
                  trigger: 'blur',
                },
                { validator: this.isNumber, trigger: 'blur' },
              ]
            : [],
          sjsfjgyssxr: [
            {
              required: true,
              message: '请输入其中涉及向司法机关移送或报告事项涉及人员（人）',
              trigger: 'blur',
            },
            { validator: this.isNumber, trigger: 'blur' },
          ],
          lsysclsx: [
            {
              required: true,
              message: '请输入落实移送处理事项',
              trigger: 'blur',
            },
            { validator: this.isNumber, trigger: 'blur' },
          ],
          yscllsqk:
            !!this.formData.tblYqnsSjzgYsnr.sjdjcf ||
            !!this.formData.tblYqnsSjzgYsnr.sjzwcf ||
            !!this.formData.tblYqnsSjzgYsnr.sjnbjlcf ||
            !!this.formData.tblYqnsSjzgYsnr.qtjjcf
              ? [
                  {
                    required: true,
                    message: '请输入移送处理落实情况（人）',
                    trigger: 'blur',
                  },
                  { validator: this.isNumber, trigger: 'blur' },
                ]
              : [],
          sjdjcf: [
            {
              required: true,
              message: '请输入其中涉及党纪处分（人）',
              trigger: 'blur',
            },
            { validator: this.isNumber, trigger: 'blur' },
          ],
          sjzwcf: [
            {
              required: true,
              message: '请输入其中涉及政务处分（人））',
              trigger: 'blur',
            },
            { validator: this.isNumber, trigger: 'blur' },
          ],
          sjnbjlcf: [
            {
              required: true,
              message: '其中涉及内部纪律处分（人）',
              trigger: 'blur',
            },
            { validator: this.isNumber, trigger: 'blur' },
          ],
          qtjjcf: [
            {
              required: true,
              message: '其他经济处分（人）',
              trigger: 'blur',
            },
            { validator: this.isNumber, trigger: 'blur' },
          ],
        }
      },
    },
    watch: {
      'formData.tblYqnsSjzgYsnr.ysclsx': {
        handler(newValue) {
          this.$nextTick(() => {
            this.$refs['ruleForm'].validate()
          })
        },
        immediate: true, // 立即执行一次
      },
      'formData.tblYqnsSjzgYsnr.ysclr': {
        handler(newValue) {
          this.$nextTick(() => {
            this.$refs['ruleForm'].validate()
          })
        },
        immediate: true, // 立即执行一次
      },
      'formData.tblYqnsSjzgYsnr.yscllsqk': {
        handler(newValue) {
          this.$nextTick(() => {
            this.$refs['ruleForm'].validate()
          })
        },
        immediate: true, // 立即执行一次
      },
    },
    created() {},
    mounted() {},
    methods: {
      //金额输入
      inputMoney(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        if (
          sanitizedValue.startsWith('0') &&
          sanitizedValue.length > 1 &&
          sanitizedValue[1] !== '.'
        ) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        if (sanitizedValue === '' || sanitizedValue === '0') {
          sanitizedValue = ''
        }
        // 更新输入框的值
        const keys = key.split('.')
        let formDataRef = this.formData
        for (let i = 0; i < keys.length - 1; i++) {
          formDataRef = formDataRef[keys[i]]
        }
        formDataRef[keys[keys.length - 1]] = sanitizedValue
      },
      isNumber(rule, value, callback) {
        const reg = /^([1-9]\d*)$/
        if (value === '') {
          callback()
        } else if (!reg.test(value)) {
          callback(new Error('请输入大于0的正整数'))
        } else {
          callback()
        }
      },
      handleInput(field, value) {
        const regex = /^(?:[1-9]\d*)$/
        if (!regex.test(value)) {
          // 如果输入的值不符合规则，则将输入值重置为上一个合法的值
          if (value.length > 0) {
            this.formData.tblYqnsSjzgYsnr[field] = value.substring(
              0,
              value.length - 1
            )
          } else {
            // 如果输入值为空或无效，重置为上一个合法的值
            this.formData.tblYqnsSjzgYsnr[field] = '' // 或者你可以设置为默认值
          }
        }
      },
      //判断tblYqnsSjzgYsnr是否都为空
      isObjectEmptyStrings(obj) {
        for (let key in obj) {
          if (obj.hasOwnProperty(key)) {
            // 如果属性值是数组，则跳过检查
            if (Array.isArray(obj[key])) {
              continue
            }
            // 检查属性值是否为空字符串
            if (obj[key] !== '') {
              return false
            }
          }
        }
        return true
      },
      setdqzjjjcg(value) {
        // 使用正则表达式来确保输入的是非负数
        const regex = /^[1-9]\d*(\.\d+)?$|^0(\.\d+)?$/
        if (!regex.test(value) && value !== '') {
          // 如果输入的值不是大于0的数，则将输入重置为初始值
          this.formData.dqzjjjcg = this.formData.dqzjjjcg.substring(
            0,
            this.formData.dqzjjjcg.length - 1
          )
        }
        if (parseInt(value) > parseInt(this.formData.dqzgje)) {
          this.formData.dqzjjjcg = this.formData.dqzjjjcg.substring(
            0,
            this.formData.dqzjjjcg.length - 1
          )
        }
        this.$forceUpdate()
      },
      setdqqtjjcg(value) {
        // 使用正则表达式来确保输入的是非负数
        const regex = /^[1-9]\d*(\.\d+)?$|^0(\.\d+)?$/
        if (!regex.test(value) && value !== '') {
          // 如果输入的值不是大于0的数，则将输入重置为初始值
          this.formData.dqqtjjcg = this.formData.dqqtjjcg.substring(
            0,
            this.formData.dqqtjjcg.length - 1
          )
        }
        if (parseInt(value) > parseInt(this.formData.dqzgje)) {
          this.formData.dqqtjjcg = this.formData.dqqtjjcg.substring(
            0,
            this.formData.dqqtjjcg.length - 1
          )
        }
        this.$forceUpdate()
      },
      handletDeleteTable1(row) {
        this.tableData1.splice(this.tableData1.indexOf(row), 1)
        this.$message.success('删除成功')
        if (row.zgbgid) {
          deleteInfosjzgbg({
            zgbgid: row.zgbgid,
            wtzgid: this.formData.wtzgid,
          }).then((res) => {})
        }
      },
      handleDeleteTable2(row) {
        this.tableData2.splice(this.tableData2.indexOf(row), 1)
        this.$message.success('删除成功')
        if (row.sjzgtjid) {
          deleteInfosjzgtjxx({
            sjzgtjid: row.sjzgtjid,
            wtzgid: this.formData.wtzgid,
          }).then((res) => {})
        }
      },
      handletDeleteTable3(row) {
        this.tableData3.splice(this.tableData3.indexOf(row), 1)
        // if(row.zgbgid){
        //   deleteYsAttach({ id: row.zgbgid,zgbgid:row.zgbgid,wtzgid:this.formData.wtzgid }).then(res => {
        //   })
        // }
      },
      setTable1(row) {
        this.tableData1.push(row)
      },
      setTable2(row) {
        this.tableData2.push(row)
      },
      setTable3(row) {
        this.tableData3.push(row)
      },
      async handleEditTable1(row, disabled) {
        this.$refs['table1'].showEdit(row, disabled)
      },
      async handleEditTable2(row, disabled) {
        this.$refs['table2'].showEdit(row, disabled)
      },
      async handleEditTable3(row, disabled) {
        this.$refs['table3'].showEdit(row, disabled)
      },
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = disabled
        this.row = row

        const resZ = await getDetailInfo({ issueId: row.tblIssueEntity.id })
        console.log('zzz', resZ)
        if (resZ.code == 1) {
          console.log(resZ.data.baseData)
          this.formData.sjbgdgTitle = resZ.data.baseData.sjbgdgTitle
          this.formData.issuesId = resZ.data.baseData.id
          this.formData.sjbgdgid = resZ.data.baseData.sjbgdgid
          this.formData.unitName = resZ.data.baseData.unitName
          this.formData.unitOrgId = resZ.data.baseData.unitOrgId
          this.formData.issueNumber = resZ.data.baseData.issueNumber
          this.formData.rectPerName = resZ.data.baseData.rectPerName
          this.formData.rectPerson = resZ.data.baseData.rectPerson
          this.formData.issueDetail = resZ.data.baseData.issueDetail
          this.formData.money = resZ.data.baseData.money
          this.formData.reviewMoney = resZ.data.baseData.reviewMoney
          this.formData.qualitative = resZ.data.baseData.qualitative
          this.formData.problemQualitative =
            resZ.data.baseData.problemQualitative
          this.formData.qualitativeRule = resZ.data.baseData.qualitativeRule
          this.formData.correctPropose = resZ.data.baseData.correctPropose
          this.formData.problemYear = resZ.data.baseData.problemYear
          this.formData.timeLimit = resZ.data.baseData.timeLimit
          this.formData.rectClass = resZ.data.baseData.rectClass
          this.formData.urgeDepartment = resZ.data.baseData.urgeDepartment
          this.formData.head = resZ.data.baseData.head
          this.formData.operator = resZ.data.baseData.operator
          this.formData.operatorId = resZ.data.baseData.operatorId
          this.formData.addTime = resZ.data.baseData.addTime
          this.formData.draft = resZ.data.baseData.draft
          this.formData.draftIdStrs = resZ.data.baseData.draftIdStrs
          this.$forceUpdate()
        }
        if (row.wtzgid) {
          const res = await getDetailInfor({ wtzgid: row.wtzgid })
          // this.formData = {
          this.formData.reportnum = res.data.reportnum || ''
          this.formData.wtdx = res.data.wtdx || ''
          this.formData.ssms = res.data.ssms || ''
          this.formData.clyj = res.data.clyj || ''
          this.formData.wtje = res.data.wtje || ''
          this.formData.gldg = res.data.gldg || ''
          this.formData.dx = res.data.dx || ''
          this.formData.dxfgyj = res.data.dxfgyj || ''
          this.formData.zgqtbm = res.data.zgqtbm || ''
          this.formData.zgsx = res.data.zgsx || ''
          this.formData.zgfl = res.data.zgfl || ''
          this.formData.zcss = res.data.zcss || ''
          this.formData.fxcd = res.data.fxcd || ''
          this.formData.ljzgje = res.data.ljzgje || ''
          this.formData.ljjjcg = res.data.ljjjcg || ''
          this.formData.dqzgzt = res.data.dqzgzt || ''
          this.formData.zgzt = res.data.zgzt || ''
          this.formData.dqzgje = res.data.dqzgje || ''
          this.formData.dqzjjjcgtype = res.data.dqzjjjcgtype || ''
          this.formData.dqzjjjcg = res.data.dqzjjjcg || ''
          this.formData.dqqtjjcgtype = res.data.dqqtjjcgtype || ''
          this.formData.dqqtjjcg = res.data.dqqtjjcg || ''
          this.formData.dqzgms = res.data.dqzgms || ''
          this.formData.sfys = res.data.sfys + ''
          this.formData.tblYqnsSjzgYsnr = res.data.tblYqnsSjzgYsnr || ''
          // cjr: res.data.cjr
          // cjsj: res.data.cjsj
          this.formData.wtzgid = res.data.wtzgid
          // }
          this.tableData =
            res.data.attachments && res.data.attachments.length
              ? res.data.attachments
              : []

          this.tableData1 = res.data.zgbgList || []
          this.tableData2 = res.data.zgtjList || []
          // this.tableData3 = res.data.tblYqnsSjzgGzzdqksm|| []
          if (res.data.tblYqnssjzgGzzdqksm) {
            this.formData.gzzdqksm = res.data.tblYqnssjzgGzzdqksm.gzzdqksm
            this.tableDataGzzdqksm = res.data.tblYqnssjzgGzzdqksm.attachments
          }
        }

        if (row && !disabled) {
          this.title = '编辑'
        } else if (row && disabled) {
          this.title = '详细'
        }
      },
      close() {
        this.formData = {
          reportnum: '',
          unitname: '',
          wtdx: '',
          ssms: '',
          clyj: '',
          wtje: '',
          gldg: '',
          dx: '',
          dxfgyj: '',
          zgqtbm: '',
          zgsx: '',
          zgfl: '',
          zcss: '',
          fxcd: '',
          ljzgje: '',
          ljjjcg: '',
          dqzgzt: '',
          dqzgje: '',
          dqzjjjcgtype: '',
          dqzjjjcg: '',
          dqqtjjcgtype: '',
          dqqtjjcg: '',
          dqzgms: '',
          sfys: '',
          tblYqnsSjzgYsnr: {
            sjsfjgyssx: '',
            ysclsx: '',
            ysclsjje: '',
            ysclr: '',
            sjsfjgyssxr: '',
            lsysclsx: '',
            yscllsqk: '',
            sjdjcf: '',
            sjzwcf: '',
            sjnbjlcf: '',
            qtjjcf: '',
            cjr: '',
            cjsj: '',
          },
          wtzgid: '',
          attIds: [],
        }
        this.dialogFormVisible = false
        this.tableData = []
        this.tableData1 = []
        this.tableData2 = []
        this.tableData3 = []
        this.tableDataGzzdqksm = []
        this.formDisabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attIds = []
            this.tableData.map((item) => {
              attIds.push(item.attid)
              // attIds += item.attid
              // attIds += ','
            })
            let attIds2 = []
            this.tableDataGzzdqksm.map((item) => {
              attIds2.push(item.attid)
              // attIds += item.attid
              // attIds += ','
            })
            // attIds = attIds.substring(0, attIds.length - 1)
            let zgbgList = []
            this.tableData1.map((item) => {
              if (item.zgbgid == '') {
                zgbgList.push(item)
              }
            })
            let zgtjList = []
            this.tableData2.map((item) => {
              if (item.sjzgtjid == '') {
                zgtjList.push(item)
              }
            })
            let gzzdqksm = []
            this.tableData3.map((item) => {
              if (item.id == '') {
                gzzdqksm.push(item)
              }
            })
            const { addTime, ...other } = this.formData
            const data = await editInfo({
              ...other,
              tblYqnsSjzgYsnr:
                this.formData.sfys == 1 ? this.formData.tblYqnsSjzgYsnr : null,
              attIds,
              zgbgList,
              zgtjList,
              tblYqnssjzgGzzdqksm: {
                gzzdqksm: this.formData.gzzdqksm,
                attIds: attIds2,
              },
            })
            if (data.code == 1) {
              this.$message({
                message: '成功',
                type: 'success',
              })
              this.$emit('fetch-data')
              this.close()
            }
          } else {
            return false
          }
        })
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFileInfo({ attid: row.attid })
      },
      async handleDeleteGzzdqksm(row) {
        let list = this.tableDataGzzdqksm
        list = list.filter((item) => item.attid != row.attid)
        this.tableDataGzzdqksm = list
        await deleteFileInfo({ attid: row.attid })
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.result == '200') {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      handleSuccessGzzdqksm(file) {
        if (file.result == '200') {
          let list = this.tableDataGzzdqksm
          list.push(file.data)
          this.tableDataGzzdqksm = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.apiFile ||
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
            api: this.apiFile,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccessFile(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccessFile(file) {
        if (file.code == 200) {
          this.fileList = [...this.fileList, ...file.data]
          this.tableDataGzzdqksm = [...this.tableDataGzzdqksm, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleBeforeUploadFile(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      // 第二个上传
      customUploadWrapper2(options) {
        if (
          !this.baseApi ||
          !this.apiFile ||
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
            api: this.apiFile,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccessFile2(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccessFile2(file) {
        if (file.code == 200) {
          this.fileList2 = [...this.fileList2, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleBeforeUploadFile2(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper2({ file })
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
