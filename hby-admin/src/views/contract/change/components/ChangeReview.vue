<template>
  <el-dialog
    title="变更审核"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab" type="card">
      <!-- 变更信息 -->
      <el-tab-pane label="变更信息" name="info">
        <el-form
          ref="infoForm"
          :model="changeInfo"
          label-width="120px"
          :disabled="true"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="项目名称">
                <el-input v-model="changeInfo.projectName" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="变更类型">
                <el-select v-model="changeInfo.changeType" style="width: 100%">
                  <el-option label="范围变更" :value="1" />
                  <el-option label="时间变更" :value="2" />
                  <el-option label="成本变更" :value="3" />
                  <el-option label="质量变更" :value="4" />
                  <el-option label="资源变更" :value="5" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="变更ID">
                <el-input v-model="changeInfo.changeId" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请人">
                <el-input v-model="changeInfo.applicantName" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="影响程度">
                <el-select v-model="changeInfo.impactLevel" style="width: 100%">
                  <el-option label="低" :value="1" />
                  <el-option label="中" :value="2" />
                  <el-option label="高" :value="3" />
                  <el-option label="极高" :value="4" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="紧急程度">
                <el-select v-model="changeInfo.urgencyLevel" style="width: 100%">
                  <el-option label="低" :value="1" />
                  <el-option label="中" :value="2" />
                  <el-option label="高" :value="3" />
                  <el-option label="紧急" :value="4" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预估成本">
                <el-input-number
                  v-model="changeInfo.estimatedCost"
                  :precision="2"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预估工期(天)">
                <el-input-number
                  v-model="changeInfo.estimatedDuration"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="变更原因">
            <el-input
              v-model="changeInfo.changeReason"
              type="textarea"
              :rows="3"
            />
          </el-form-item>

          <el-form-item label="变更内容">
            <el-input
              v-model="changeInfo.changeContent"
              type="textarea"
              :rows="4"
            />
          </el-form-item>

          <el-form-item label="影响分析">
            <el-input
              v-model="changeInfo.impactAnalysis"
              type="textarea"
              :rows="3"
            />
          </el-form-item>

          <el-form-item label="风险评估">
            <el-input
              v-model="changeInfo.riskAssessment"
              type="textarea"
              :rows="3"
            />
          </el-form-item>

          <el-form-item label="实施方案">
            <el-input
              v-model="changeInfo.implementationPlan"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- 审核记录 -->
      <el-tab-pane label="审核记录" name="history">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddReview">
            添加审核记录
          </el-button>
        </div>

        <el-table :data="reviewHistory" border style="width: 100%">
          <el-table-column label="审核人" prop="reviewerName" width="120" />
          <el-table-column label="审核时间" prop="reviewTime" width="150" />
          <el-table-column label="审核结果" prop="reviewResult" width="100">
            <template #default="{ row }">
              <el-tag :type="getReviewResultType(row.reviewResult)">
                {{ getReviewResultName(row.reviewResult) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="审核意见" prop="reviewComments" min-width="200" />
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteReview($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 审核决策 -->
      <el-tab-pane label="审核决策" name="decision">
        <el-form
          ref="decisionForm"
          :model="decisionForm"
          :rules="decisionRules"
          label-width="120px"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="审核人" prop="reviewerId">
                <el-select v-model="decisionForm.reviewerId" placeholder="请选择审核人" style="width: 100%">
                  <el-option label="张三" :value="1" />
                  <el-option label="李四" :value="2" />
                  <el-option label="王五" :value="3" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="审核时间" prop="reviewTime">
                <el-date-picker
                  v-model="decisionForm.reviewTime"
                  type="datetime"
                  placeholder="选择审核时间"
                  style="width: 100%"
                  format="yyyy-MM-dd HH:mm:ss"
                  value-format="yyyy-MM-dd HH:mm:ss"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="审核结果" prop="reviewResult">
            <el-radio-group v-model="decisionForm.reviewResult">
              <el-radio :label="1">通过</el-radio>
              <el-radio :label="2">有条件通过</el-radio>
              <el-radio :label="3">拒绝</el-radio>
              <el-radio :label="4">需要补充材料</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="审核意见" prop="reviewComments">
            <el-input
              v-model="decisionForm.reviewComments"
              type="textarea"
              :rows="4"
              placeholder="请输入审核意见"
            />
          </el-form-item>

          <el-form-item label="修改建议" prop="modificationSuggestions">
            <el-input
              v-model="decisionForm.modificationSuggestions"
              type="textarea"
              :rows="3"
              placeholder="请输入修改建议"
            />
          </el-form-item>

          <el-form-item label="风险提示" prop="riskWarnings">
            <el-input
              v-model="decisionForm.riskWarnings"
              type="textarea"
              :rows="2"
              placeholder="请输入风险提示"
            />
          </el-form-item>

          <el-form-item label="后续要求" prop="followUpRequirements">
            <el-input
              v-model="decisionForm.followUpRequirements"
              type="textarea"
              :rows="2"
              placeholder="请输入后续要求"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>

    <!-- 审核记录编辑弹窗 -->
    <el-dialog
      title="添加审核记录"
      :visible.sync="reviewEditDialogVisible"
      width="50%"
      append-to-body
    >
      <el-form
        ref="reviewEditForm"
        :model="reviewEditForm"
        :rules="reviewEditRules"
        label-width="120px"
      >
        <el-form-item label="审核人" prop="reviewerId">
          <el-select v-model="reviewEditForm.reviewerId" placeholder="请选择审核人" style="width: 100%">
            <el-option label="张三" :value="1" />
            <el-option label="李四" :value="2" />
            <el-option label="王五" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="审核时间" prop="reviewTime">
          <el-date-picker
            v-model="reviewEditForm.reviewTime"
            type="datetime"
            placeholder="选择审核时间"
            style="width: 100%"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="审核结果" prop="reviewResult">
          <el-select v-model="reviewEditForm.reviewResult" placeholder="请选择审核结果" style="width: 100%">
            <el-option label="通过" :value="1" />
            <el-option label="有条件通过" :value="2" />
            <el-option label="拒绝" :value="3" />
            <el-option label="需要补充材料" :value="4" />
          </el-select>
        </el-form-item>

        <el-form-item label="审核意见" prop="reviewComments">
          <el-input
            v-model="reviewEditForm.reviewComments"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="reviewEditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveReviewEdit">确定</el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmitReview">提交审核</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    submitProjectChangeReview,
    getProjectChangeReviewHistory
  } from '@/api/contract/change'

  export default {
    name: 'ChangeReview',
    data() {
      return {
        dialogVisible: false,
        activeTab: 'info',
        changeInfo: {},
        reviewHistory: [],
        decisionForm: {
          reviewerId: null,
          reviewTime: '',
          reviewResult: null,
          reviewComments: '',
          modificationSuggestions: '',
          riskWarnings: '',
          followUpRequirements: ''
        },
        decisionRules: {
          reviewerId: [
            { required: true, message: '请选择审核人', trigger: 'change' }
          ],
          reviewTime: [
            { required: true, message: '请选择审核时间', trigger: 'change' }
          ],
          reviewResult: [
            { required: true, message: '请选择审核结果', trigger: 'change' }
          ],
          reviewComments: [
            { required: true, message: '请输入审核意见', trigger: 'blur' }
          ]
        },
        reviewEditDialogVisible: false,
        reviewEditForm: {
          reviewerId: null,
          reviewTime: '',
          reviewResult: null,
          reviewComments: ''
        },
        reviewEditRules: {
          reviewerId: [
            { required: true, message: '请选择审核人', trigger: 'change' }
          ],
          reviewTime: [
            { required: true, message: '请选择审核时间', trigger: 'change' }
          ],
          reviewResult: [
            { required: true, message: '请选择审核结果', trigger: 'change' }
          ],
          reviewComments: [
            { required: true, message: '请输入审核意见', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      async showEdit(data) {
        this.dialogVisible = true
        this.changeInfo = { ...data }
        await this.loadReviewHistory()
      },

      async loadReviewHistory() {
        try {
          const response = await getProjectChangeReviewHistory({
            changeId: this.changeInfo.id
          })
          if (response.code === 200) {
            this.reviewHistory = response.data || []
          }
        } catch (error) {
          console.error('加载审核记录失败：', error)
        }
      },

      handleClose() {
        this.dialogVisible = false
        this.changeInfo = {}
        this.reviewHistory = []
        this.activeTab = 'info'
        this.resetDecisionForm()
      },

      resetDecisionForm() {
        this.decisionForm = {
          reviewerId: null,
          reviewTime: '',
          reviewResult: null,
          reviewComments: '',
          modificationSuggestions: '',
          riskWarnings: '',
          followUpRequirements: ''
        }
      },

      handleAddReview() {
        this.reviewEditForm = {
          reviewerId: null,
          reviewTime: '',
          reviewResult: null,
          reviewComments: ''
        }
        this.reviewEditDialogVisible = true
      },

      handleDeleteReview(index) {
        this.$confirm('确定要删除这条审核记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.reviewHistory.splice(index, 1)
          this.$message.success('删除成功')
        })
      },

      async handleSaveReviewEdit() {
        try {
          await this.$refs.reviewEditForm.validate()

          // 添加审核人姓名
          const reviewerMap = {
            1: '张三',
            2: '李四',
            3: '王五'
          }

          this.reviewHistory.push({
            ...this.reviewEditForm,
            reviewerName: reviewerMap[this.reviewEditForm.reviewerId]
          })

          this.reviewEditDialogVisible = false
          this.$message.success('添加成功')
        } catch (error) {
          console.error('保存失败：', error)
        }
      },

      async handleSubmitReview() {
        try {
          await this.$refs.decisionForm.validate()

          const reviewData = {
            changeId: this.changeInfo.id,
            ...this.decisionForm
          }

          const response = await submitProjectChangeReview(reviewData)
          if (response.code === 200) {
            this.$message.success('审核提交成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.message || '审核提交失败')
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('审核提交失败：' + error.message)
          }
        }
      },

      getReviewResultName(result) {
        const resultMap = {
          1: '通过',
          2: '有条件通过',
          3: '拒绝',
          4: '需要补充材料'
        }
        return resultMap[result] || '未知'
      },

      getReviewResultType(result) {
        const typeMap = {
          1: 'success',
          2: 'warning',
          3: 'danger',
          4: 'info'
        }
        return typeMap[result] || 'info'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
