<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="编号" label-width="140px" prop="elementcode">
            <el-input
              v-model="formData.elementcode"
              clearable
              placeholder="请输入编号"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="控制方式"
            label-width="140px"
            prop="controlmethod"
          >
            <el-select
              v-model="formData.controlmethod"
              placeholder="请选择控制方式"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option
                v-for="item in controlmethodList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制类型" label-width="140px" prop="controltype">
            <el-select
              v-model="formData.controltype"
              placeholder="请选择控制类型"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option
                v-for="item in controltypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制频率" label-width="140px" prop="controlreq">
            <el-select
              v-model="formData.controlreq"
              placeholder="请选择控制频率"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option
                v-for="item in controlreqList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="业务归口管理部门"
            label-width="140px"
            prop="dutyorgName"
          >
            <el-input
              v-model="formData.dutyorgName"
              clearable
              placeholder="请输入业务归口管理部门"
              style="width: 75%; margin-right: 8px"
              disabled
            />
            <el-button
              type="primary"
              @click="$refs['comTreeRef'].show(false, [])"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任岗位" label-width="140px" prop="dutystation">
            <el-input
              v-model="formData.dutystation"
              clearable
              placeholder="请输入责任岗位"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="内部制度名称"
            label-width="140px"
            prop="insystemname"
          >
            <el-input
              v-model="formData.insystemname"
              clearable
              placeholder="请输入内部制度名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="缺陷类型" label-width="140px" prop="defecttype">
            <el-select
              v-model="formData.defecttype"
              placeholder="请选择缺陷类型"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in defecttypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷等级" label-width="140px" prop="defectlevel">
            <el-select
              v-model="formData.defectlevel"
              placeholder="请选择缺陷等级"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in defectlevelList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="缺陷概述" label-width="140px" prop="defectmemo">
            <el-input
              v-model="formData.defectmemo"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入缺陷概述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="缺陷具体描述"
            label-width="140px"
            prop="defectdetail"
          >
            <el-input
              v-model="formData.defectdetail"
              clearable
              type="textarea"
              rows="4"
              placeholder="缺陷具体描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->

        <!-- <el-col :span="24">
          <el-form-item
            label="评价程序"
            label-width="140px"
            prop="evaluationpro"
          >
            <el-input
              v-model="formData.evaluationpro"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入评价程序"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-form-item
            label="评价记录"
            label-width="140px"
            prop="evaluationnode"
          >
            <el-input
              v-model="formData.evaluationnode"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入评价记录"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-form-item
            label="评价要点"
            label-width="140px"
            prop="evaluationpoint"
          >
            <el-input
              v-model="formData.evaluationpoint"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入评价要点"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-form-item label="定性依据" label-width="140px" prop="quabasis">
            <el-input
              v-model="formData.quabasis"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入定性依据"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->

        <!-- <el-col :span="24">
          <el-form-item label="一级流程" label-width="140px" prop="oneprocess">
            <el-input
              v-model="formData.oneprocess"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入一级流程"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="二级流程" label-width="140px" prop="twoprocess">
            <el-input
              v-model="formData.twoprocess"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入二级流程"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="三级流程"
            label-width="140px"
            prop="threeprocess"
          >
            <el-input
              v-model="formData.threeprocess"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入三级流程"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="风险描述" label-width="140px" prop="risktype">
            <el-input
              v-model="formData.risktype"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="风险描述" label-width="140px" prop="risktype">
            <el-input
              v-model="formData.risktype"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->

        <el-col :span="24">
          <el-form-item
            label="流程描述"
            label-width="140px"
            prop="businessdesc"
          >
            <el-input
              v-model="formData.businessdesc"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入流程描述"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="控制目标"
            label-width="140px"
            prop="controltarget"
          >
            <el-input
              v-model="formData.controltarget"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入控制目标"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="控制措施"
            label-width="140px"
            prop="controlmeasures"
          >
            <el-input
              v-model="formData.controlmeasures"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入控制措施"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="评价程序" label-width="140px" prop="checkmethod">
            <el-input
              v-model="formData.checkmethod"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入评价程序"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="证明材料" label-width="140px" prop="material">
            <el-input
              v-model="formData.material"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入证明材料"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="测试程序" label-width="140px" prop="procedures">
            <el-input
              v-model="formData.procedures"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入测试程序"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item
            label="内部控制基本规范、应用指引和解读相关要求"
            label-width="140px"
            prop="longString1"
          >
            <el-input
              v-model="formData.longString1"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入内部控制基本规范、应用指引和解读相关要求"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="标准化控制+中核集团内部控制评价补充要求"
            label-width="140px"
            prop="longString2"
          >
            <el-input
              v-model="formData.longString2"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入标准化控制+中核集团内部控制评价补充要求"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="测试结果" label-width="140px" prop="testresult">
            <el-input
              v-model="formData.testresult"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入测试结果"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="设计有效性"
            label-width="140px"
            prop="designpointvalidity"
          >
            <el-select
              v-model="formData.designpointvalidity"
              placeholder="请选择设计有效性"
              :style="{ width: '100%' }"
              :disabled="!footer"
              @change="handleDesignValidityChange"
            >
              <el-option
                v-for="item in designpointvalidityList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="getOptionStyle(item.value)"
                :class="getOptionClass(item.value)"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24" v-if="showExecuteValidity">
          <el-form-item
            label="执行有效性"
            label-width="140px"
            prop="executepointvalidity"
          >
            <el-select
              v-model="formData.executepointvalidity"
              placeholder="请选择执行有效性"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in executepointvalidityList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="getOptionStyle(item.value)"
                :class="getOptionClass(item.value)"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="测试有效性"
            label-width="140px"
            prop="testpointvalidity"
          >
            <el-select
              v-model="formData.testpointvalidity"
              placeholder="请选择测试有效性"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in testpointvalidityList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :style="getOptionStyle(item.value)"
                :class="getOptionClass(item.value)"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" label-width="140px" prop="memo">
            <el-input
              v-model="formData.memo"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>相关规章制度</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="openKnowledgeSelector">
              新增
            </el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="selectedFilesData">
            <el-table-column
              align="center"
              label="文件名称"
              prop="fileName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="部门名称"
              prop="departmentName"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                {{ formatDepartmentName(scope.row.departmentName) }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="文件备注"
              prop="fileRemark"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="文件扩展名"
              prop="extName"
              width="120"
            />
            <el-table-column
              align="center"
              label="版本号"
              prop="fileCurVerNumStr"
              width="100"
            />
            <el-table-column
              align="center"
              label="密级"
              prop="securityLevelName"
            />
            <el-table-column
              label="文件版本编码"
              prop="fileVerCode"
              width="80"
              align="center"
            ></el-table-column>
            <el-table-column align="center" label="操作" width="160">
              <template #default="{ row, $index }">
                <el-button type="text" @click="previewFile(row)">
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="removeFileItem($index)"
                  v-if="footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <el-divider>现行标准</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleSelectStandards">
              新增
            </el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="selectedStandardsData">
            <el-table-column align="center" label="文件名称" prop="ruleName">
              <template #default="{ row }">
                <el-button type="text" @click="viewStandardDetail(row)">
                  {{ row.ruleName }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="文件编号"
              prop="ruleCode"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="发文文号"
              prop="ruleNumber"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="摘要"
              prop="summaryInfo"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="发文日期"
              prop="publishDate"
            />
            <el-table-column align="center" label="操作">
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  @click="removeStandardItem($index)"
                  v-if="footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <!-- 现行标准选择器组件 -->
        <NowModoSelector
          ref="standardSelector"
          @confirm="handleStandardsSelected"
        />

        <!-- 现行标准详情组件 -->
        <NowModoEdit ref="standardDetail" />

        <el-col :span="24">
          <el-divider>问题发现</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd">新建</el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="issueData">
            <el-table-column
              align="center"
              label="一级流程"
              prop="oneprocess"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDeatil(row)">
                  {{ row.oneprocess }}
                </el-button>
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="二级流程"
              prop="problemtype"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="风险编号"
              prop="risknumber"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleRisknumberDeatil(row)">
                  {{ row.risknumber }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="问题概述"
              prop="problemmemo"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="缺陷具体描述"
              prop="defectmemo"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="缺陷等级"
              prop="defectlevel"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="定性依据"
              prop="quabasis"
              show-overflow-tooltip
            />
            <!-- <el-table-column
              align="center"
              label="主责部门"
              prop="orgname"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="反馈意见"
              prop="feedback"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="整改计划"
              prop="reformplan"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="预计完成时间"
              prop="estfinishdate"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="整改落实人"
              prop="realname"
              show-overflow-tooltip
            /> -->
            <el-table-column
              align="center"
              label="内控评价年度"
              prop="testYear"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{ row.testYear ? row.testYear : '' }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="审批状态" prop="status">
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
            </el-table-column>
            <el-table-column
              align="center"
              label="操作"
              v-if="footer"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  :disabled="!!row.status"
                >
                  编辑
                </el-button>
                <el-dropdown style="margin-left: 10px">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-button
                        type="text"
                        @click.native="handleManage(row)"
                        :disabled="!row.status"
                      >
                        办理
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item
                      :disabled="!!row.status"
                      @click.native="handleApproval(row)"
                    >
                      提交审批
                    </el-dropdown-item>
                    <el-dropdown-item
                      :disabled="!!row.status"
                      @click.native="handleIssueDelete(row)"
                    >
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24" v-if="formData.executepointvalidity !== '未发生'">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col
          :span="24"
          v-if="
            formData.executepointvalidity == null ||
            formData.executepointvalidity !== '未发生'
          "
        >
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api + '?projectid=' + task.testtaskid"
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
                <el-button type="text" @click="handleDowns(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div> -->
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <ProcessList ref="process" @fetch-data="fetch" />
    <issueView ref="issue" @fetch-data="fetch2" />
    <WfqdDeal ref="wfqddeal" />
    <RiskEdit ref="read" :fromNK="true" />
    <CompanyTreeModel ref="comTreeRef" @selected="handleSelectCompany" />

    <!-- 知识平台选择弹窗 -->
    <KnowledgeSelector
      :visible.sync="knowledgeSelectorVisible"
      @confirm="handleKnowledgeConfirm"
    />

    <!-- 文件夹详情弹窗 -->
    <FolderDetails
      :visible.sync="folderDetailsVisible"
      :folder-id="selectedFolderId"
      :folder-path="selectedFolderPath"
      @confirm="handleFolderFilesConfirm"
    />
  </el-dialog>
</template>

<script>
  import {
    addtask,
    delFile,
    controlTestImplSave,
    testtaskProfindTesttaskid,
    testtaskProfindDel,
    addXXBZ,
    getXXBZ,
    removeXXBZ,
  } from '@/api/internal/tack'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import issueView from './issueView.vue'
  import { download } from '@/api/internal/score'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import RiskRead from '@/views/risk/identify/creation/components/RiskRead.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import RiskEdit from '@/views/risk/identify/creation/components/RiskEdit.vue'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  import KnowledgeSelector from './KnowledgeSelector.vue'
  import FolderDetails from '@/views/workbench/controlLib/components/folderDetails.vue'
  import NowModoSelector from './NowModoSelector.vue'
  import NowModoEdit from './NowModoEdit.vue'
  import { getFilePreviewUrl } from '@/api/setting/knowledge.js'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  import store from '@/store'
  const { baseURL } = require('@/config')
  export default {
    name: 'TaskForm',
    components: {
      issueView,
      ProcessList,
      WfqdDeal,
      RiskRead,
      RiskEdit,
      CompanyTreeModel,
      KnowledgeSelector,
      FolderDetails,
      NowModoSelector,
      NowModoEdit,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        testtaskid: '',
        baseApi: baseURL,
        // api: '/nkhg/nbkz/csrw/control_test_impl_upload',
        // headers: {
        //   token: store.getters['user/token'],
        // },
        headers: { token: token },
        fileList: [],
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          elementcode: undefined,
          controlmethod: undefined,
        },
        templates: [],
        footer: true,
        tableData: [],
        issueData: [],
        task: '',
        planid: '',
        rules: {
          elementcode: [
            {
              required: true,
              message: '请输入编号',
              trigger: 'blur',
            },
          ],
          testpointvalidity: [
            {
              required: true,
              message: '请选择测试有效性',
              trigger: 'change',
            },
          ],
          designpointvalidity: [
            {
              required: true,
              message: '请选择设计有效性',
              trigger: 'change',
            },
          ],
          executepointvalidity: [
            {
              required: true,
              message: '请选择执行有效性',
              trigger: 'change',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        testpointvalidityList: [
          {
            value: '1',
            label: '有效',
          },
          {
            value: '2',
            label: '无效',
          },
          {
            value: '3',
            label: '不适用',
          },
          // {
          //   value: '4',
          //   label: '部分有效',
          // },
        ],
        controltypeList: [
          {
            value: '预防',
            label: '预防',
          },
          {
            value: '检查',
            label: '检查',
          },
        ],
        controlmethodList: [
          {
            value: '手工',
            label: '手工',
          },
          {
            value: '系统',
            label: '系统',
          },
        ],
        controlreqList: [
          {
            value: '不定期',
            label: '不定期',
          },
          {
            value: '月度',
            label: '月度',
          },
          {
            value: '年度',
            label: '年度',
          },
        ],
        defecttypeList: [
          {
            value: '执行缺陷',
            label: '执行缺陷',
          },
          {
            value: '设计缺陷',
            label: '设计缺陷',
          },
        ],
        defectlevelList: [
          {
            value: '一般',
            label: '一般',
          },
          {
            value: '重要',
            label: '重要',
          },
          {
            value: '重大',
            label: '重大',
          },
        ],
        planid: '',
        templId: '',
        node: '',
        row: {},
        designpointvalidityList: [
          {
            value: '有效',
            label: '有效',
          },
          {
            value: '无效',
            label: '无效',
          },
          {
            value: '不适用',
            label: '不适用',
          },
        ],
        executepointvalidityList: [
          {
            value: '有效',
            label: '有效',
          },
          {
            value: '无效',
            label: '无效',
          },
          {
            value: '未发生',
            label: '未发生',
          },
        ],
        showExecuteValidity: false,
        requireAttachment: false,
        // 知识平台选择相关
        knowledgeSelectorVisible: false,
        selectedKnowledgeData: [], // 选中的文件夹数据
        selectedFilesData: [], // 选中的文件数据
        folderDetailsVisible: false, // 文件夹详情弹窗显示状态
        // 现行标准选择相关
        selectedStandardsData: [], // 选中的现行标准数据
        selectedFolderId: '', // 选中的文件夹ID
        selectedFolderPath: '', // 选中的文件夹路径
        submitData: [], // 提交的数据
      }
    },
    // computed: {
    //   getFormLevel() {
    //     if (!this.formData.secrectLevelId) return ''
    //     const level = this.MJoption.find(
    //       (item) => item.levelId == this.formData.secrectLevelId
    //     )
    //     return level ? level.levelName : ''
    //   },
    // },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetch()
        }
      })
    },
    methods: {
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(47, row.findid)
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.findid,
          tableId: 47,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      handleRisknumberDeatil(row) {
        // this.$refs['read'].showRead(row)
        this.$refs['read'].showEdit(row, '', true)
      },
      handleAdd() {
        if (this.testtaskid) {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              // // 验证问题发现是否必填
              // if (!this.validateIssueRequired()) {
              //   return false
              // }
              // 验证附件上传
              if (!this.validateAttachment()) {
                return false
              }
              const {
                procedures,
                testpointvalidity,
                designpointvalidity,
                executepointvalidity,
                testresult,
                memo,
                ...other
              } = this.formData
              const data = await controlTestImplSave({
                ...this.formData,
                planid: this.planid,
                testtaskid: this.task.testtaskid,
              })
              if (data.code == 200) {
                this.$baseMessage('保存成功', 'success')
              } else {
                this.$baseMessage(data.msg, 'error')
              }
              this.$refs.issue.show({}, 'add', this.testtaskid)
            }
          })
        } else {
          this.$message.error('请先保存')
        }
      },
      handleDeatil(row) {
        this.$refs.issue.show(row, 'detail', this.testtaskid)
      },
      handleEdit(row) {
        this.$refs.issue.show(row, 'edit', this.testtaskid)
      },
      handleIssueDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await testtaskProfindDel({ findid: row.findid })
          if (code == 200) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            await this.getIssue()
          } else {
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          }
        })
      },
      async showEdit(row, type, planid, templId, node) {
        console.log(row)
        this.planid = planid
        this.templId = templId
        this.node = node
        this.row = row
        this.dialogFormVisible = true
        await this.getInfo()
        await this.getIssue()
        await this.getStandardsList()
        this.planid = planid
        if (type == 'test') {
          this.title = '测试'
        } else if (type == 'view') {
          this.title = '查看'
          this.footer = false
        } else {
          this.title = '编辑'
        }
      },
      async fetch() {
        await this.getInfo()
        await this.getIssue()
      },
      async fetch2() {
        await this.getIssue()
      },
      async getIssue() {
        const res = await testtaskProfindTesttaskid({
          testtaskid: this.row.TESTTASKID,
        })
        this.issueData = res.data.profind
        this.$forceUpdate()
      },
      async getInfo() {
        this.testtaskid = this.row.TESTTASKID
        const data = await addtask({
          ementid: this.row.ELEMENTID,
          node: this.node,
          planid: this.planid,
          templId: this.templId,
        })
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.formData = {
          ...data.data.element,
          ...data.data.task,
          risktype: data.data.element.risktype,
        }

        // 如果dutyorg和dutyorgName为空，默认使用userInfo的linkDetp信息
        if (!this.formData.dutyorg && !this.formData.dutyorgName) {
          this.formData.dutyorg = userInfo.linkDetp.orgid
          this.formData.dutyorgName = userInfo.linkDetp.orgname
        }

        // 初始化设计有效性和执行有效性的显示逻辑
        if (this.formData.designpointvalidity) {
          this.showExecuteValidity =
            this.formData.designpointvalidity === '有效'
        }

        // 初始化附件需求状态
        if (this.formData.executepointvalidity) {
          this.requireAttachment = ['有效', '无效', null].includes(
            this.formData.executepointvalidity
          )
        }

        // 处理规章制度数据
        this.selectedFilesData = data.data.task.knowbase
          ? JSON.parse(data.data.task.knowbase)
          : []

        this.$forceUpdate()
        this.tableData = data.data.atts
        this.task = data.data.task
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
        // 清空选中的规章制度数据
        this.selectedFilesData = []
        this.submitData = []
        this.knowledgeSelectorVisible = false
        // 清空选中的现行标准数据
        this.selectedStandardsData = []
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // 验证问题发现是否必填
            if (!this.validateIssueRequired()) {
              return false
            }
            // 验证附件上传
            if (!this.validateAttachment()) {
              return false
            }
            const atts = this.submitData.map((item) => item.attid).join(',')
            const {
              procedures,
              testpointvalidity,
              designpointvalidity,
              executepointvalidity,
              testresult,
              memo,
              ...other
            } = this.formData
            const data = await controlTestImplSave({
              ...this.formData,
              knowbase: JSON.stringify(this.selectedFilesData),
              planid: this.planid,
              testtaskid: this.task.testtaskid,
              attid: atts,
            })
            if (data.code == 200) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            console.log('error submit!!')
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
        // 同步删除submitData中的对应项目
        this.submitData = this.submitData.filter(
          (item) => item.attid != row.attid
        )
        await delFile({ attid: row.attid })
        this.$message.success('删除成功')
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.code == '200') {
      //     let list = this.tableData || []
      //     list.push(file.data.att)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
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
      //   this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      // },
      // 根据选项值返回动态样式
      getOptionStyle(value) {
        switch (value) {
          case '有效': // 有效
            return { color: 'green' }
          case '无效': // 无效
            return { color: 'red' }
          case '不适用': // 不适用
          default:
            return {} // 不适用或其他情况
        }
      },
      // 根据选项值返回动态类
      getOptionClass(value) {
        return 'bold-text'
      },
      handleDesignValidityChange(value) {
        // 设计有效性为"有效"时，可选执行有效性
        // 设计有效性为"无效"或"不适用"时，隐藏执行有效性，认定为有缺陷
        this.showExecuteValidity = value === '有效'

        if (value === '无效' || value === '不适用') {
          // 设置为有缺陷
          this.$set(this.formData, 'hasDefect', true)
          // 隐藏执行有效性
          this.showExecuteValidity = false
          // 清空执行有效性值
          this.$set(this.formData, 'executepointvalidity', null)
          // 重置附件需求标志
          this.requireAttachment = false
        }
      },
      // 验证是否上传了附件
      validateAttachment() {
        // 如果执行有效性不是"有效"或"无效"或者为null，则不需要验证附件
        if (
          this.formData.executepointvalidity == null ||
          !['有效', '无效'].includes(this.formData.executepointvalidity)
        ) {
          return true
        }

        // 只有当执行有效性为"有效"或"无效"时才验证附件
        if (!this.tableData || this.tableData.length === 0) {
          this.$message.error('执行有效性为"有效"或"无效"时，必须上传测试附件')
          return false
        }
        return true
      },

      // 验证问题发现是否必填
      validateIssueRequired() {
        // 检查设计有效性或执行有效性是否为无效
        const designInvalid = this.formData.designpointvalidity === '无效'
        const executeInvalid = this.formData.executepointvalidity === '无效'

        // 如果设计有效性或执行有效性为无效，则问题发现为必填
        if (designInvalid || executeInvalid) {
          if (!this.issueData || this.issueData.length === 0) {
            this.$message.error(
              '执行有效性或设计有效性为无效时，问题发现为必填项'
            )
            return false
          }
        }
        return true
      },
      /**
       * @description:选择部门回调
       * @return {*}
       */
      handleSelectCompany(e) {
        console.log('handleSelectCompany', e)
        this.$set(this.formData, 'dutyorgName', e.name)
        this.$set(this.formData, 'dutyorg', e.id)
      },

      /**
       * @description: 打开知识平台选择弹窗
       */
      openKnowledgeSelector() {
        this.knowledgeSelectorVisible = true
      },

      /**
       * @description: 确认选择文件夹
       * @param {Array} selectedItems 选中的文件夹
       */
      handleKnowledgeConfirm(selectedItems) {
        // 提取所有选中文件夹中的文件
        const allSelectedFiles = []
        selectedItems.forEach((folder) => {
          if (folder.selectedFiles && Array.isArray(folder.selectedFiles)) {
            allSelectedFiles.push(...folder.selectedFiles)
          }
        })

        // 避免重复添加文件
        const existingFileIds = this.selectedFilesData.map(
          (item) => item.fileId
        )
        const newFiles = allSelectedFiles.filter(
          (file) => !existingFileIds.includes(file.fileId)
        )

        // 添加新选中的文件
        this.selectedFilesData = [...this.selectedFilesData, ...newFiles]

        if (newFiles.length > 0) {
          this.$message.success(`成功添加 ${newFiles.length} 个文件`)
        } else {
          this.$message.warning('所选文件已存在，未添加重复项')
        }
      },

      /**
       * @description: 显示文件夹详情
       * @param {Object} row 文件夹信息
       */
      showFolderDetails(row) {
        this.selectedFolderId = row.folderId
        this.selectedFolderPath = row.folderPath
        this.folderDetailsVisible = true
      },

      /**
       * @description: 从列表中删除文件项
       * @param {Number} index 要删除的项目索引
       */
      removeFileItem(index) {
        this.$confirm('确定要删除这个文件吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.selectedFilesData.splice(index, 1)
            this.$message.success('删除成功')
          })
          .catch(() => {
            // 用户取消删除
          })
      },

      /**
       * @description: 获取现行标准列表数据
       */
      async getStandardsList() {
        try {
          const { data, code } = await getXXBZ({
            taskId: this.testtaskid,
          })

          if (code == 200 && data) {
            // 转换数据格式以匹配表格显示，使用与 nowModo.vue 一致的字段名
            const formattedStandards = data.map((standard) => ({
              id: standard.id,
              ruleName: standard.ruleName,
              ruleCode: standard.ruleCode,
              ruleNumber: standard.ruleNumber,
              summaryInfo: standard.summaryInfo,
              publishDate: standard.publishDate,
              // 保留原始数据以备后用
              originalData: standard,
            }))

            this.selectedStandardsData = formattedStandards
          }
        } catch (error) {
          console.error('获取现行标准列表失败:', error)
        }
      },

      /**
       * @description: 预览文件
       * @param {Object} row 文件信息
       */
      async previewFile(row) {
        try {
          const {
            code,
            data: { data },
            msg,
          } = await getFilePreviewUrl({ fileId: row.fileId })
          if (code == 200) {
            // 在新窗口打开预览
            window.open(data, '_blank')
          } else {
            this.$message.error(msg)
          }
        } catch (error) {
          console.error('获取预览链接失败:', error)
          this.$message.error('获取预览链接失败')
        }
      },

      /**
       * @description: 打开现行标准选择弹窗
       */
      handleSelectStandards() {
        this.$refs.standardSelector.show()
      },

      /**
       * @description: 处理现行标准选择确认
       * @param {Array} selectedStandards 选中的现行标准列表
       */
      async handleStandardsSelected(selectedStandards) {
        // 避免重复添加，过滤已存在的标准
        const existingIds = this.selectedStandardsData.map((item) => item.id)
        const newStandards = selectedStandards.filter(
          (standard) => !existingIds.includes(standard.id)
        )

        if (newStandards.length === 0) {
          this.$message.error('现行标准已添加')
          return
        }

        try {
          // 将新选中的现行标准 id 用逗号分隔
          const testIds = newStandards.map((standard) => standard.id).join(',')

          const { code } = await addXXBZ({
            taskId: this.testtaskid,
            testId: testIds,
          })

          // 检查接口调用是否成功
          if (code != 200) {
            return
          }
          // 转换数据格式以匹配表格显示，使用与 nowModo.vue 一致的字段名
          const formattedStandards = newStandards.map((standard) => ({
            id: standard.id,
            ruleName: standard.ruleName,
            ruleCode: standard.ruleCode,
            ruleNumber: standard.ruleNumber,
            summaryInfo: standard.summaryInfo,
            publishDate: standard.publishDate,
            // 保留原始数据以备后用
            originalData: standard,
          }))

          this.selectedStandardsData = [
            ...this.selectedStandardsData,
            ...formattedStandards,
          ]

          this.$message.success(`成功添加 ${newStandards.length} 个现行标准`)
        } catch (error) {
          console.error('调用 addXXBZ 接口失败:', error)
          this.$message.error('添加现行标准失败，请重试')
        }
      },

      /**
       * @description: 查看现行标准详情
       * @param {Object} row 现行标准信息
       */
      viewStandardDetail(row) {
        // 使用原始数据来显示详情
        const originalData = row.originalData || row
        this.$refs.standardDetail.showEdit(originalData, true)
      },

      /**
       * @description: 从列表中删除现行标准项
       * @param {Number} index 要删除的项目索引
       */
      async removeStandardItem(index) {
        this.$confirm('确定要删除这个现行标准吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(async () => {
            try {
              const standardItem = this.selectedStandardsData[index]

              // 调用删除接口
              const { code } = await removeXXBZ({
                taskId: this.testtaskid,
                testId: standardItem.id,
              })

              if (code == 200) {
                // 删除成功后移除前端数据
                this.selectedStandardsData.splice(index, 1)
                this.$message.success('删除成功')
              } else {
                this.$message.error('删除失败，请重试')
              }
            } catch (error) {
              console.error('删除现行标准失败:', error)
              this.$message.error('删除失败，请重试')
            }
          })
          .catch(() => {
            // 用户取消删除
          })
      },

      /**
       * @description: 处理文件夹详情确认选择的文件
       * @param {Array} selectedFiles 选中的文件列表
       */
      handleFolderFilesConfirm(selectedFiles) {
        // 避免重复添加，过滤已存在的文件
        const existingFileIds = this.selectedFilesData.map(
          (item) => item.fileId
        )
        const newFiles = selectedFiles.filter(
          (file) => !existingFileIds.includes(file.fileId)
        )

        // 添加新选中的文件
        this.selectedFilesData = [...this.selectedFilesData, ...newFiles]

        if (newFiles.length > 0) {
          this.$message.success(`成功添加 ${newFiles.length} 个文件`)
        } else {
          this.$message.warning('所选文件已存在，未添加重复项')
        }
      },

      /**
       * @description: 格式化部门名称，移除括号及其内容
       * @param {String} departmentName 原始部门名称
       * @return {String} 格式化后的部门名称
       */
      formatDepartmentName(departmentName) {
        if (!departmentName) return ''
        // 移除英文和中文括号及其内容
        return departmentName.replace(/[\(（][^\)）]*[\)）]/g, '')
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
            // formData: formData, // 传递额外的表单数据
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
          // this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          this.submitData = [...this.submitData, ...file.data]
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
  .bold-text {
    font-weight: bold;
  }
</style>
