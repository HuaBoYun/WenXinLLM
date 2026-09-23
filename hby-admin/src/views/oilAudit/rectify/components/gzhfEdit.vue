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
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <!-- <el-row> -->
        <el-col :span="24">
          <el-divider>问题清单</el-divider>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="审计报告定稿" prop="sjbgdgTitle">
            <el-input
              v-model="formData.sjbgdgTitle"
              :style="{ width: '100%' }"
              disabled
              placeholder="审计报告定稿"
            /> 
          </el-form-item>
        </el-col> -->
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
          <el-form-item label="问题金额（元）" prop="money">
            <el-input
              type="number"
              v-model="formData.money"
              clearable
              placeholder="请输入问题金额（元）"
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

        <el-col :span="12">
          <el-form-item label="定性法规依据" prop="qualitativeRule">
            <el-input
              v-model="formData.qualitativeRule"
              clearable
              placeholder="请输入定性法规依据"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="整改督促牵头部门或单位" prop="urgeDepartment">
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
          <el-form-item label="整改时限" prop="timeLimit">
            <el-input
              v-model="formData.timeLimit"
              clearable
              placeholder="请输入整改时限"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="12">
          <el-form-item label="整改人" prop="rectPerName">
            <el-input
              v-model="formData.rectPerName"
              :style="{ width: '100%' }"
              clearable
              placeholder="整改人"
              disabled
            /> 
          </el-form-item>
        </el-col>
    
      
        <el-col :span="12">
          <el-form-item label="审减金额（元）" prop="reviewMoney">
            <el-input
            type="number" 
              v-model="formData.reviewMoney"
              clearable
              placeholder="请输入审减金额（元）"
              :style="{ width: '100%' }"
              disabled
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
     -->
        <!-- </el-row> -->
        <el-col :span="24">
          <el-divider>问题整改</el-divider>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="整改督促牵头部门或单位" prop="zgqtbm">
            <el-input
              v-model="formData.zgqtbm"
              clearable
              placeholder="请输入整改督促牵头部门或单位"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="整改分类" prop="zgfl">
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
          <el-form-item label="资产损失（万元）" prop="zcss">
            <el-input
              v-model="formData.zcss"
              clearable
              placeholder="请输入资产损失（万元）"
              :style="{ width: '100%' }"
              disabled
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
              disabled
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
              disabled
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
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="整改状态" prop="zgzt">
            <el-select
              v-model="formData.zgzt"
              placeholder="请选择整改状态"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option label="整改完毕" value="3" />
              <el-option label="正在整改" value="2" />
              <el-option label="尚未开始整改" value="1" />
              <el-option label="不接受审计意见" value="0" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="当期整改金额（元）" prop="dqzgje">
            <el-input
              v-model="formData.dqzgje"
              clearable
              placeholder="请输入当期整改金额（元）"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="当期直接经济成果类型" prop="dqzjjjcgtype">
            <el-select
              v-model="formData.dqzjjjcgtype"
              placeholder="请选择当期直接经济成果类型"
              :style="{ width: '100%' }"
              disabled
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
          <el-form-item label="当期直接经济成果（元）" prop="dqzjjjcg">
            <el-input
              v-model="formData.dqzjjjcg"
              clearable
              placeholder="请输入当期直接经济成果（元）"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="当期其他经济成果类型" prop="dqqtjjcgtype">
            <el-select
              v-model="formData.dqqtjjcgtype"
              placeholder="请选择当期其他经济成果类型"
              :style="{ width: '100%' }"
              @change="$forceUpdate()"
              disabled
            >
              <el-option
                v-for="(item, index) in dqzjjjcgtypeTypeOptions"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="当期其他经济成果（元）" prop="dqqtjjcg">
            <el-input
              v-model="formData.dqqtjjcg"
              clearable
              placeholder="请输入当期其他经济成果（元）"
              :style="{ width: '100%' }"
              disabled
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
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否移送" prop="sfys">
            <el-select
              v-model="formData.sfys"
              placeholder="请选择是否移送"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <template v-if="this.formData.sfys == 1">
          <el-col :span="24">
            <el-divider>移送内容</el-divider>
          </el-col>

          <el-col :span="12">
            <el-form-item label="移送处理事项" prop="ysclsx">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.ysclsx"
                clearable
                placeholder="请输入移送处理事项"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item
              label="其中涉及向司法机关移送或报告事项"
              prop="sjsfjgyssx"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjsfjgyssx"
                clearable
                placeholder="请输入其中涉及向司法机关移送或报告事项"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="移送处理涉及金额（元）" prop="ysclsjje">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.ysclsjje"
                clearable
                placeholder="请输入移送处理涉及金额（元）"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="移送处理人员（人）" prop="ysclr">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.ysclr"
                clearable
                placeholder="请输入移送处理人员（人） "
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="其中涉及向司法机关移送或报告事项涉及人员（人）"
              prop="sjsfjgyssxr"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjsfjgyssxr"
                clearable
                placeholder="请输入其中涉及向司法机关移送或报告事项涉及人员（人） "
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="落实移送处理事项" prop="lsysclsx">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.lsysclsx"
                clearable
                placeholder="请输入落实移送处理事项"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="移送处理落实情况（人）" prop="yscllsqk">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.yscllsqk"
                clearable
                placeholder="请输入移送处理落实情况（人）"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其中涉及党纪处分（人）" prop="sjdjcf">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjdjcf"
                clearable
                placeholder="请输入其中涉及党纪处分（人）"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其中涉及政务处分（人）" prop="sjzwcf">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjzwcf"
                clearable
                placeholder="请输入其中涉及政务处分（人）"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其中涉及内部纪律处分（人）" prop="sjnbjlcf">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjnbjlcf"
                clearable
                placeholder="请输入其中涉及内部纪律处分（人）"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其他经济处分（人）" prop="qtjjcf">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.qtjjcf"
                clearable
                placeholder="请输入其他经济处分（人）"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="创建人" prop="cjr">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.cjr"
                disabled
                placeholder="请输入创建人"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间" prop="cjsj">
              <el-date-picker
                v-model="formData.tblYqnsSjzgYsnr.cjsj"
                placeholder="请输入创建时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col> -->
        </template>
        <el-col :span="24">
          <el-divider>上传审计整改报告及相关附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" disabled @click="handleEditTable1(null)">
              新增
            </el-button>
          </div>
          <el-table :data="tableData1">
            <el-table-column align="center" label="序号" type="index" />
            <el-table-column align="center" label="文号" prop="document">
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handleEditTable1(row, true)"
                  v-if="row.zgbgid"
                >
                  {{ row.document }}
                </el-button>
                <span v-else>
                  {{ row.document }}
                </span>
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
                <!-- <el-button
                  type="text"
                  @click="handleEditTable1(row)"
                  :disabled="formDisabled"
                >
                  编辑
                </el-button> -->
                <el-button
                  type="text"
                  @click="handletDeleteTable1(row, $index)"
                  disabled
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
              disabled
              @click="handleEditTable2(null, false)"
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
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handleEditTable2(row, true)"
                  v-if="row.sjzgtjid"
                >
                  {{ row.zdgzzds }}
                </el-button>
                <span v-else>
                  {{ row.zdgzzds }}
                </span>
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
                <el-button
                  type="text"
                  @click="handleEditTable2(row, false)"
                  disabled
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteTable2(row, $index)"
                  disabled
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
              disabled
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
              <el-button type="success" disabled>上传</el-button>
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
                  @click="handlePreviewFileFile(row)"
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

        <el-col :span="24">
          <el-divider>上传审计移送处理文书</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableDataWS">
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
                  @click="handlePreviewFileFile(row)"
                  :disabled="false"
                >
                  预览
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
          <el-divider>跟踪审计采纳情况</el-divider>
        </el-col>
        <el-col :span="24">
          <!-- <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              v-if="!formDisabled"
              @click="handleEditTable4(null)"
              >
              新增
            </el-button>
          </div> -->
          <el-table :data="tableData4">
            <el-table-column align="center" label="序号" type="index" />
            <el-table-column
              align="center"
              label="建议层级类型"
              prop="hierarchyType"
            ></el-table-column>
            <el-table-column
              align="center"
              label="建议涉及业务类型"
              prop="businessType"
            />
            <el-table-column
              align="center"
              label="建议标题"
              prop="title"
            ></el-table-column>
            <el-table-column
              align="center"
              label="建议描述"
              show-overflow-tooltip
              prop="details"
            ></el-table-column>
            <el-table-column
              align="center"
              label="关联底稿"
              prop="draftIdStrs"
            ></el-table-column>
            <!-- <el-table-column
              align="center"
              label="采纳描述"
              show-overflow-tooltip
              prop="notReason" 
            ></el-table-column>  -->
            <el-table-column
              align="center"
              label="原因"
              prop="notReason"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column align="center" label="是否采纳" prop="isAdopt">
              <template #default="{ row }">
                <span>
                  {{
                    row.isAdopt == '1' ? '是' : row.isAdopt == '0' ? '否' : ''
                  }}
                </span>
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
                  @click="handleCaiNa(row, $index)"
                  :disabled="formDisabled"
                >
                  采纳
                </el-button>
                <el-button
                  type="text"
                  @click="handleBuCaiNa(row, $index)"
                  :disabled="formDisabled"
                >
                  不采纳
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>审计通知书</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <!-- <el-button
              type="success"
              :disabled="formDisabled"
              @click="handleEditTable5()"
            >
              新建
            </el-button> -->
            <el-button
              type="success"
              :disabled="formDisabled"
              @click="handleAddTable5()"
            >
              新增
            </el-button>
          </div>
          <el-table :data="tableData5">
            <el-table-column align="center" label="序号" type="index" />
            <el-table-column
              align="center"
              label="审计通知书编号"
              prop="advicecoed"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEditTable5(row, true)"
                  :disabled="false"
                  style="white-space: pre-line; line-height: 16px"
                >
                  {{ row.advicecoed }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="审计通知书名称"
              prop="advicename"
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
                  @click="handleEditTable5(row, $index)"
                  :disabled="formDisabled"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteTable5(row, $index)"
                  :disabled="formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>审计意见及决定书</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              :disabled="formDisabled"
              @click="handleEditTable6(null, 'add')"
            >
              新建
            </el-button>
            <el-button
              type="success"
              :disabled="formDisabled"
              @click="handleAddTable6()"
            >
              新增
            </el-button>
          </div>
          <el-table :data="tableData6">
            <el-table-column align="center" label="序号" type="index" />
            <el-table-column align="center" label="编号" prop="code">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDetail(row)"
                  :disabled="false"
                >
                  {{ row.code }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="名称" prop="bgname" />
            <!-- <el-table-column
            align="center"
            label="编制人"
            prop="createname" 
          />
          <el-table-column
            align="center"
            label="编制时间"
            prop="createdate" 
          />
          <el-table-column 
            align="center"
            label="状态"
            prop="status"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '已退回'
                  : row.status == 3
                  ? '已撤回'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column> -->
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  @click="handleEditTable6(row, 'edit')"
                  :disabled="formDisabled"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteTable6(row, $index)"
                  :disabled="formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>经济责任审计结果报告</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              :disabled="formDisabled"
              @click="handleEditTable7(null, 'add')"
            >
              新建
            </el-button>
            <el-button
              type="success"
              :disabled="formDisabled"
              @click="handleAddTable7()"
            >
              新增
            </el-button>
          </div>
          <el-table :data="tableData7">
            <el-table-column align="center" label="序号" type="index" />
            <el-table-column align="center" label="文号" prop="document">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDetailZ(row)"
                  :disabled="false"
                >
                  {{ row.document }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="标题" prop="title" />
            <!-- <el-table-column
            align="center"
            label="项目名称"
            prop="projectName"
            show-overflow-tooltip 
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
                  @click="handleEditTable7(row, 'edit')"
                  :disabled="formDisabled"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteTable7(row, $index)"
                  :disabled="formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>上传审计要情</el-divider>
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
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  @click="handleDowns(row)"
                  :disabled="false"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFileFile(row)"
                  :disabled="false"
                >
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row, $index)"
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

    <NoticeInfo ref="table5View" @fetchData="addTable5" />
    <NoticeInfoTable ref="table5" @seTtable="setTable5" />

    <sjjysView ref="table6View" @fetchData="addTable6" />
    <sjjysViewTable ref="table6" @seTtable="setTable6" />

    <jjzrsjjgbgView ref="table7View" @fetchData="addTable7" />
    <jjzrsjjgbgViewTable ref="table7" @seTtable="setTable7" />

    <CaiNaModel ref="CaiNaModel" @fetchData="caiNaFetch" />
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
  import {
    deleteInfo as deleteInfosjzgbg,
    getTotalMoney,
    getWtzgReportInfoList,
    saveWtzgSjtzs,
    removeWtzgSjtzs,
  } from '@/oapi/yqns_sjzg/sjzgbg'
  import { deleteInfo as deleteInfosjzgtjxx } from '@/oapi/yqns_sjzg/sjzgtjxx'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { download } from '@/oapi/audit/report'
  import scsjbgEdit from './scsjbgEdit'
  import sjzgtjxxEdit from './sjzgtjxxEdit2'
  import sjzgtjxxEditNext from './sjzgtjxxEditNext'
  import NoticeInfo from './gzhf/NoticeInfo.vue'
  import NoticeInfoTable from './gzhf/NoticeInfoTable.vue'
  import sjjysView from './gzhf/sjjysView.vue'
  import sjjysViewTable from './gzhf/sjjysViewTable.vue'
  import { formatDate } from '@/utils/index'
  import jjzrsjjgbgView from './gzhf/jjzrsjjgbgView.vue'
  import jjzrsjjgbgViewTable from './gzhf/jjzrsjjgbgViewTable.vue'
  import CaiNaModel from './gzhf/CaiNaModel.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'

  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'wtzgEdit',
    components: {
      scsjbgEdit,
      sjzgtjxxEdit,
      sjzgtjxxEditNext,
      CaiNaModel,
      NoticeInfo,
      NoticeInfoTable,
      sjjysView,
      sjjysViewTable,
      jjzrsjjgbgView,
      jjzrsjjgbgViewTable,
    },
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
        apiFile: 'https://www.wenxin.example.com/api/file/file/upload',
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
          projectId: '',
        },
        dqzjjjcgtypeTypeOptions: [
          {
            label: '促进清理债权债务',
            value: '促进清理债权债务',
          },
          {
            label: '促进清理不良资产',
            value: '促进清理不良资产',
          },
          {
            label: '促进解除担保责任',
            value: '促进解除担保责任',
          },
          {
            label: '促进增效',
            value: '促进增效',
          },
          {
            label: '收回上级投入资金结余',
            value: '收回上级投入资金结余',
          },
          {
            label: '调整账目',
            value: '调整账目',
          },
          {
            label: '其他',
            value: '其他',
          },
        ],
        formDisabled: true,
        tableData: [],
        tableDataGzzdqksm: [],
        rules: {
          sfys: [
            {
              required: true,
              message: '请选择是否移送',
              trigger: 'change',
            },
          ],
        },
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
        tableData4: [],
        tableData5: [],
        tableData6: [],
        tableData7: [],
        tableDataWS: [],
        row: {},
      }
    },
    computed: {},
    watch: {
      'formData.ljzgje'(val) {
        if (!val && this.formData.projectId) {
          this.getLjzgje(this.formData.projectId)
        }
      },
    },
    created() {},
    mounted() {},
    methods: {
      async handlePreviewFile(row) {
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
      handleCaiNa(row) {
        this.$refs['CaiNaModel'].showEdit(row, 1, this.formData.wtzgid)
      },
      handleBuCaiNa(row) {
        this.$refs['CaiNaModel'].showEdit(row, 2, this.formData.wtzgid)
      },
      async caiNaFetch(wtzgid) {
        const res = await getDetailInfor({ wtzgid: wtzgid })
        this.tableData4 = res.data.tblIssueEntity
          ? res.data.tblIssueEntity.proList
          : []
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      async getThreeList(wtzgid) {
        const res = await getWtzgReportInfoList({ wtzgid })
        this.tableData5 = res.data.tzsList
        this.tableData6 = res.data.sjyjList
        this.tableData7 = res.data.sjjgList
      },
      async getLjzgje(projectId) {
        const res = await getTotalMoney({ projectId })
        this.formData.ljjjcg = res.data.ljjjcgMoney
        this.formData.ljzgje = res.data.ljzgMoney
        this.$forceUpdate()
      },
      handleAddTable7() {
        this.$refs['table7'].showEdit()
      },
      setTable7(val) {
        this.saveWtzgSjtzsVal(this.formData.wtzgid, val[0].jjzrsjjgbgid, 3)
        this.tableData7.push(val[0])
      },
      addTable7(row) {
        console.log('reo', row)
        let list = this.tableData7
        let type = false
        list.map((item) => {
          if (item.jjzrsjjgbgid == row.jjzrsjjgbgid) {
            type = true
          }
        })
        if (type) {
          list = list.filter((item) => item.jjzrsjjgbgid != row.jjzrsjjgbgid)
          list.push(row)
          this.tableData7 = list
        } else {
          this.saveWtzgSjtzsVal(this.formData.wtzgid, row.jjzrsjjgbgid, 3)
          this.tableData7.push(row)
        }
      },
      handleDeleteTable7(row) {
        this.removeWtzgSjtzsVal(this.formData.wtzgid, row.jjzrsjjgbgid, 3)
        let list = this.tableData7
        list = list.filter((item) => item.jjzrsjjgbgid != row.jjzrsjjgbgid)
        this.tableData7 = list
      },
      handleDetailZ(row) {
        this.$refs['table7View'].showEdit(row, 'detail')
      },
      handleEditTable7(row, disabled) {
        this.$refs['table7View'].showEdit(row, disabled)
      },
      handleAddTable6() {
        this.$refs['table6'].showEdit()
      },
      setTable6(val) {
        this.saveWtzgSjtzsVal(this.formData.wtzgid, val[0].bgid, 2)
        this.tableData6.push(val[0])
      },
      addTable6(row) {
        console.log('reo', row)
        let list = this.tableData6
        let type = false
        list.map((item) => {
          if (item.bgid == row.bgid) {
            type = true
          }
        })
        if (type) {
          list = list.filter((item) => item.bgid != row.bgid)
          list.push(row)
          this.tableData6 = list
        } else {
          this.saveWtzgSjtzsVal(this.formData.wtzgid, row.bgid, 2)
          this.tableData6.push(row)
        }
      },
      handleDeleteTable6(row) {
        this.removeWtzgSjtzsVal(this.formData.wtzgid, row.bgid, 2)
        let list = this.tableData6
        list = list.filter((item) => item.bgid != row.bgid)
        this.tableData6 = list
      },
      handleEditTable6(row, disabled) {
        this.$refs['table6View'].showEdit(row, disabled)
      },
      handleAddTable5() {
        this.$refs['table5'].showEdit()
      },
      handleDetail(row) {
        this.$refs['table6View'].showEdit(row, 'detail')
      },
      setTable5(val) {
        this.saveWtzgSjtzsVal(this.formData.wtzgid, val[0].adviceid, 1)
        this.tableData5.push(val[0])
      },
      addTable5(row) {
        let list = this.tableData5
        let type = false
        list.map((item) => {
          if (item.adviceid == row.adviceid) {
            type = true
          }
        })
        if (type) {
          list = list.filter((item) => item.adviceid != row.adviceid)
          list.push(row)
          this.tableData5 = list
        } else {
          this.saveWtzgSjtzsVal(this.formData.wtzgid, row.adviceid, 1)
          this.tableData5.push(row)
        }
      },
      handleDeleteTable5(row) {
        this.removeWtzgSjtzsVal(this.formData.wtzgid, row.adviceid, 1)
        let list = this.tableData5
        list = list.filter((item) => item.adviceid != row.adviceid)
        this.tableData5 = list
      },
      handleEditTable5(row, disabled) {
        this.$refs['table5View'].showEdit(row, disabled)
      },
      async saveWtzgSjtzsVal(wtzgid, formid, optype) {
        const res = await saveWtzgSjtzs({ wtzgid, formid, optype })
      },
      async removeWtzgSjtzsVal(wtzgid, formid, optype) {
        const res = await removeWtzgSjtzs({ wtzgid, formid, optype })
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
        this.$refs['table2'].showEdit(
          row,
          disabled,
          this.formData.wtzgid,
          this.formData.projectId,
          this.formData.dqzjjjcgtype,
          this.formData.dqqtjjcgtype
        )
      },
      async handleEditTable3(row, disabled) {
        this.$refs['table3'].showEdit(row, disabled)
      },
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = disabled
        this.row = row

        const resZ = await getDetailInfo({
          issueId: row.tblIssueEntity ? row.tblIssueEntity.id : row.issuesId,
        })
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
          this.formData.draftIdStrs = resZ.data.baseData.draftIdStrs
          this.formData.zcss = resZ.data.baseData.assetLoss
          this.formData.fxcd = resZ.data.baseData.riskLevel

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
          // this.formData.fxcd = res.data.fxcd || ''
          this.formData.ljzgje = res.data.ljzgje || ''
          this.formData.ljjjcg = res.data.ljjjcg || ''
          this.formData.dqzgzt = res.data.dqzgzt || ''
          this.formData.zgzt = res.data.zgzt || ''
          // console.log('🚀 ~ showEdit ~ res.data.dqzgzt:', res.data.dqzgzt)
          this.formData.dqzgje = res.data.dqzgje || ''
          this.formData.dqzjjjcgtype = res.data.dqzjjjcgtype || ''
          this.formData.dqzjjjcg = res.data.dqzjjjcg || ''
          this.formData.dqqtjjcgtype = res.data.dqqtjjcgtype || ''
          this.formData.dqqtjjcg = res.data.dqqtjjcg || ''
          this.formData.dqzgms = res.data.dqzgms || ''
          this.formData.sfys = res.data.sfys
          this.formData.projectId = res.data.projectId || ''
          this.formData.tblYqnsSjzgYsnr = res.data.tblYqnsSjzgYsnr || ''
          // cjr: res.data.cjr
          // cjsj: res.data.cjsj
          this.formData.wtzgid = res.data.wtzgid

          this.tableData4 = res.data.tblIssueEntity
            ? res.data.tblIssueEntity.proList
              ? res.data.tblIssueEntity.proList
              : []
            : []
          this.getThreeList(res.data.wtzgid)
          // }
          this.tableData =
            res.data.attachments && res.data.attachments.length
              ? res.data.attachments
              : []

          this.tableData1 = res.data.zgbgList || []
          this.tableData2 = res.data.zgtjList || []
          // this.tableData3 = res.data.tblYqnsSjzgGzzdqksm|| []
          if (res.data.tblYqnsSjzgYsnr) {
            this.formData.tblYqnsSjzgYsnr = res.data.tblYqnsSjzgYsnr
            this.tableDataWS = res.data.tblYqnsSjzgYsnr.ysAttList
          } else {
            this.tableDataWS = []
          }
          this.formData.gzzdqksm = res.data.tblYqnsSjzgGzzdqksm?.gzzdqksm
          this.tableDataGzzdqksm = res.data.tblYqnsSjzgGzzdqksm?.attachments
          this.getLjzgje(res.data.projectId)
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
          projectId: '',
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
              zgtjList.push(item)
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
      async handleDelete(row, index) {
        await deleteFileInfo({ attid: row.attid })
        // let list = this.tableDataGzzdqksm
        // list = list.filter((item) => item.attid != row.attid)
        this.tableData.splice(index, 1)
      },
      async handleDeleteGzzdqksm(row) {
        let list = this.tableDataGzzdqksm
        list = list.filter((item) => item.attid != row.attid)
        this.tableDataGzzdqksm = list
        await deleteFileInfo({ attid: row.attid })
      },
      // handlePreview(file) {},
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
        console.log(file)
        if (file.code == 200) {
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
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
          this.tableDataGzzdqksm = [...this.tableDataGzzdqksm, ...file.data]
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
      handlePreviewFileFile(row) {
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
