<template>
  <el-dialog
    :title="`质量检查管理 - ${managementInfo.projectName || '未知项目'}`"
    :visible.sync="dialogVisible"
    width="95%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <!-- 项目信息概览 -->
    <div class="project-info-header" style="margin-bottom: 20px; padding: 15px; background: #f5f7fa; border-radius: 4px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <div><strong>项目名称：</strong>{{ managementInfo.projectName || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>项目ID：</strong>{{ managementInfo.projectId || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>负责人：</strong>{{ managementInfo.managerName || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>管理状态：</strong>
            <el-tag :type="getManagementStatusType(managementInfo.managementStatus)" size="mini">
              {{ getManagementStatusName(managementInfo.managementStatus) }}
            </el-tag>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 查询条件 -->
    <div class="search-form" style="margin-bottom: 20px; padding: 15px; background: #fafafa; border-radius: 4px;">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="检查编号">
          <el-input v-model="queryForm.inspectionNo" placeholder="请输入检查编号" clearable style="width: 150px;" />
        </el-form-item>
        <el-form-item label="检查名称">
          <el-input v-model="queryForm.inspectionName" placeholder="请输入检查名称" clearable style="width: 150px;" />
        </el-form-item>
        <el-form-item label="检查类型">
          <el-select v-model="queryForm.inspectionType" placeholder="请选择检查类型" clearable style="width: 120px;">
            <el-option v-for="item in inspectionTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="检查结论">
          <el-select v-model="queryForm.checkResult" placeholder="请选择检查结论" clearable style="width: 120px;">
            <el-option v-for="item in checkResultOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="整改状态">
          <el-select v-model="queryForm.rectificationStatus" placeholder="请选择整改状态" clearable style="width: 120px;">
            <el-option v-for="item in rectificationStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadQualityInspectionData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQueryForm">重置</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAddInspection">新增检查</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 质量检查列表 -->
    <el-table
      :data="qualityInspectionList"
      border
      style="width: 100%"
      v-loading="loading"
      :height="400"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="检查编号" prop="inspectionNo" width="140" />
      <el-table-column label="检查名称" prop="inspectionName" min-width="180" show-overflow-tooltip />
      <el-table-column label="检查类型" prop="inspectionType" width="100">
        <template #default="{ row }">
          <el-tag size="mini" :type="getInspectionTypeTagType(row.inspectionType)">
            {{ getInspectionTypeName(row.inspectionType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="检查日期" prop="inspectionDate" width="120" />
      <el-table-column label="检查范围" prop="inspectionScope" min-width="150" show-overflow-tooltip />
      <el-table-column label="检查结论" prop="checkResult" width="100">
        <template #default="{ row }">
          <el-tag size="mini" :type="getCheckResultTagType(row.checkResult)">
            {{ getCheckResultName(row.checkResult) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发现问题" prop="identifiedIssues" min-width="200" show-overflow-tooltip />
      <el-table-column label="整改状态" prop="rectificationStatus" width="100">
        <template #default="{ row }">
          <el-tag size="mini" :type="getRectificationStatusTagType(row.rectificationStatus)">
            {{ getRectificationStatusName(row.rectificationStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="整改期限" prop="rectificationDeadline" width="120" />
      <el-table-column label="创建时间" prop="createTime" width="150" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="text" size="small" @click="handleViewDetail(row)">详情</el-button>
          <el-button type="text" size="small" @click="handleEditInspection(row)">编辑</el-button>
          <el-button
            v-if="row.checkResult === 2 && row.rectificationStatus === 1"
            type="text"
            size="small"
            style="color: #E6A23C;"
            @click="handleRectify(row)"
          >
            整改
          </el-button>
          <el-button
            v-if="row.rectificationStatus === 3"
            type="text"
            size="small"
            style="color: #67C23A;"
            @click="handleRecheck(row)"
          >
            复查
          </el-button>
          <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteInspection(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div style="margin-top: 20px; text-align: right;">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </div>

    <!-- 新增/编辑质量检查对话框 -->
    <el-dialog
      :title="inspectionForm.id ? '编辑质量检查' : '新增质量检查'"
      :visible.sync="inspectionDialogVisible"
      width="800px"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form :model="inspectionForm" :rules="inspectionRules" ref="inspectionFormRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检查编号" prop="inspectionNo">
              <el-input v-model="inspectionForm.inspectionNo" placeholder="请输入检查编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查名称" prop="inspectionName">
              <el-input v-model="inspectionForm.inspectionName" placeholder="请输入检查名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检查类型" prop="inspectionType">
              <el-select v-model="inspectionForm.inspectionType" placeholder="请选择检查类型" style="width: 100%;">
                <el-option v-for="item in inspectionTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查日期" prop="inspectionDate">
              <el-date-picker
                v-model="inspectionForm.inspectionDate"
                type="date"
                placeholder="请选择检查日期"
                style="width: 100%;"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检查人ID" prop="inspectorId">
              <el-input v-model="inspectionForm.inspectorId" placeholder="请输入检查人ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查结论" prop="checkResult">
              <el-select v-model="inspectionForm.checkResult" placeholder="请选择检查结论" style="width: 100%;">
                <el-option v-for="item in checkResultOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="检查范围" prop="inspectionScope">
          <el-input v-model="inspectionForm.inspectionScope" type="textarea" :rows="2" placeholder="请输入检查范围" />
        </el-form-item>
        <el-form-item label="检查标准" prop="inspectionStandards">
          <el-input v-model="inspectionForm.inspectionStandards" type="textarea" :rows="2" placeholder="请输入检查标准" />
        </el-form-item>
        <el-form-item label="检查方法" prop="inspectionMethods">
          <el-input v-model="inspectionForm.inspectionMethods" type="textarea" :rows="2" placeholder="请输入检查方法" />
        </el-form-item>
        <el-form-item label="检查结果" prop="inspectionResults">
          <el-input v-model="inspectionForm.inspectionResults" type="textarea" :rows="3" placeholder="请输入检查结果" />
        </el-form-item>
        <el-form-item label="发现问题" prop="identifiedIssues">
          <el-input v-model="inspectionForm.identifiedIssues" type="textarea" :rows="3" placeholder="请输入发现的问题" />
        </el-form-item>
        <el-form-item label="整改要求" prop="rectificationRequirements">
          <el-input v-model="inspectionForm.rectificationRequirements" type="textarea" :rows="2" placeholder="请输入整改要求" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="整改期限" prop="rectificationDeadline">
              <el-date-picker
                v-model="inspectionForm.rectificationDeadline"
                type="date"
                placeholder="请选择整改期限"
                style="width: 100%;"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="整改负责人ID" prop="rectificationPersonId">
              <el-input v-model="inspectionForm.rectificationPersonId" placeholder="请输入整改负责人ID" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="inspectionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveInspection" :loading="saving">保存</el-button>
      </div>
    </el-dialog>

    <!-- 整改对话框 -->
    <el-dialog
      title="质量检查整改"
      :visible.sync="rectifyDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form :model="rectifyForm" :rules="rectifyRules" ref="rectifyFormRef" label-width="120px">
        <el-form-item label="检查名称">
          <span>{{ currentInspection.inspectionName }}</span>
        </el-form-item>
        <el-form-item label="发现问题">
          <span>{{ currentInspection.identifiedIssues }}</span>
        </el-form-item>
        <el-form-item label="整改说明" prop="rectificationDescription">
          <el-input v-model="rectifyForm.rectificationDescription" type="textarea" :rows="4" placeholder="请输入整改说明" />
        </el-form-item>
        <el-form-item label="整改负责人ID" prop="rectificationPersonId">
          <el-input v-model="rectifyForm.rectificationPersonId" placeholder="请输入整改负责人ID" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="rectifyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRectify" :loading="saving">保存</el-button>
      </div>
    </el-dialog>

    <!-- 复查对话框 -->
    <el-dialog
      title="质量检查复查"
      :visible.sync="recheckDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form :model="recheckForm" :rules="recheckRules" ref="recheckFormRef" label-width="120px">
        <el-form-item label="检查名称">
          <span>{{ currentInspection.inspectionName }}</span>
        </el-form-item>
        <el-form-item label="整改说明">
          <span>{{ currentInspection.rectificationDescription }}</span>
        </el-form-item>
        <el-form-item label="复查结果" prop="recheckResult">
          <el-select v-model="recheckForm.recheckResult" placeholder="请选择复查结果" style="width: 100%;">
            <el-option v-for="item in recheckResultOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="复查意见" prop="recheckComments">
          <el-input v-model="recheckForm.recheckComments" type="textarea" :rows="4" placeholder="请输入复查意见" />
        </el-form-item>
        <el-form-item label="复查人ID" prop="recheckPersonId">
          <el-input v-model="recheckForm.recheckPersonId" placeholder="请输入复查人ID" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="recheckDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRecheck" :loading="saving">保存</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog
      title="质量检查详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      append-to-body
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="检查编号">{{ currentInspection.inspectionNo }}</el-descriptions-item>
        <el-descriptions-item label="检查名称">{{ currentInspection.inspectionName }}</el-descriptions-item>
        <el-descriptions-item label="检查类型">{{ getInspectionTypeName(currentInspection.inspectionType) }}</el-descriptions-item>
        <el-descriptions-item label="检查日期">{{ currentInspection.inspectionDate }}</el-descriptions-item>
        <el-descriptions-item label="检查人ID">{{ currentInspection.inspectorId }}</el-descriptions-item>
        <el-descriptions-item label="检查结论">{{ getCheckResultName(currentInspection.checkResult) }}</el-descriptions-item>
        <el-descriptions-item label="检查范围" :span="2">{{ currentInspection.inspectionScope }}</el-descriptions-item>
        <el-descriptions-item label="检查标准" :span="2">{{ currentInspection.inspectionStandards }}</el-descriptions-item>
        <el-descriptions-item label="检查方法" :span="2">{{ currentInspection.inspectionMethods }}</el-descriptions-item>
        <el-descriptions-item label="检查结果" :span="2">{{ currentInspection.inspectionResults }}</el-descriptions-item>
        <el-descriptions-item label="发现问题" :span="2">{{ currentInspection.identifiedIssues }}</el-descriptions-item>
        <el-descriptions-item label="整改要求" :span="2">{{ currentInspection.rectificationRequirements }}</el-descriptions-item>
        <el-descriptions-item label="整改期限">{{ currentInspection.rectificationDeadline }}</el-descriptions-item>
        <el-descriptions-item label="整改状态">{{ getRectificationStatusName(currentInspection.rectificationStatus) }}</el-descriptions-item>
        <el-descriptions-item label="整改说明" :span="2">{{ currentInspection.rectificationDescription }}</el-descriptions-item>
        <el-descriptions-item label="复查结果">{{ getRecheckResultName(currentInspection.recheckResult) }}</el-descriptions-item>
        <el-descriptions-item label="复查日期">{{ currentInspection.recheckDate }}</el-descriptions-item>
        <el-descriptions-item label="复查意见" :span="2">{{ currentInspection.recheckComments }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentInspection.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ currentInspection.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </el-dialog>
</template>


<script>
import {
  getQualityInspectionPage,
  getQualityInspectionDetail,
  createQualityInspection,
  updateQualityInspection,
  deleteQualityInspection,
  rectifyQualityInspection,
  recheckQualityInspection,
  getInspectionTypeOptions,
  getCheckResultOptions,
  getRectificationStatusOptions,
  getRecheckResultOptions
} from '@/api/contract/qualityInspection'

export default {
  name: 'QualityManagement',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      saving: false,
      managementInfo: {},

      // 查询条件
      queryForm: {
        projectId: null,
        inspectionNo: '',
        inspectionName: '',
        inspectionType: null,
        checkResult: null,
        rectificationStatus: null,
        keyword: ''
      },

      // 质量检查列表
      qualityInspectionList: [],

      // 分页
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },

      // 选项数据
      inspectionTypeOptions: getInspectionTypeOptions(),
      checkResultOptions: getCheckResultOptions(),
      rectificationStatusOptions: getRectificationStatusOptions(),
      recheckResultOptions: getRecheckResultOptions(),

      // 新增/编辑对话框
      inspectionDialogVisible: false,
      inspectionForm: {
        id: null,
        projectId: null,
        taskId: null,
        inspectionNo: '',
        inspectionName: '',
        inspectionType: null,
        inspectionDate: '',
        inspectorId: null,
        inspectionScope: '',
        inspectionStandards: '',
        inspectionMethods: '',
        inspectionResults: '',
        checkResult: null,
        identifiedIssues: '',
        rectificationRequirements: '',
        rectificationDeadline: '',
        rectificationPersonId: null,
        rectificationStatus: 1
      },
      inspectionRules: {
        inspectionNo: [
          { required: true, message: '请输入检查编号', trigger: 'blur' }
        ],
        inspectionName: [
          { required: true, message: '请输入检查名称', trigger: 'blur' }
        ],
        inspectionType: [
          { required: true, message: '请选择检查类型', trigger: 'change' }
        ],
        inspectionDate: [
          { required: true, message: '请选择检查日期', trigger: 'change' }
        ],
        inspectorId: [
          { required: true, message: '请输入检查人ID', trigger: 'blur' }
        ],
        checkResult: [
          { required: true, message: '请选择检查结论', trigger: 'change' }
        ]
      },

      // 整改对话框
      rectifyDialogVisible: false,
      rectifyForm: {
        rectificationDescription: '',
        rectificationPersonId: null
      },
      rectifyRules: {
        rectificationDescription: [
          { required: true, message: '请输入整改说明', trigger: 'blur' }
        ],
        rectificationPersonId: [
          { required: true, message: '请输入整改负责人ID', trigger: 'blur' }
        ]
      },

      // 复查对话框
      recheckDialogVisible: false,
      recheckForm: {
        recheckResult: null,
        recheckComments: '',
        recheckPersonId: null
      },
      recheckRules: {
        recheckResult: [
          { required: true, message: '请选择复查结果', trigger: 'change' }
        ],
        recheckComments: [
          { required: true, message: '请输入复查意见', trigger: 'blur' }
        ],
        recheckPersonId: [
          { required: true, message: '请输入复查人ID', trigger: 'blur' }
        ]
      },

      // 详情对话框
      detailDialogVisible: false,
      currentInspection: {}
    }
  },
  methods: {
    // 显示质量管理对话框
    async showEdit(data) {
      this.dialogVisible = true
      this.managementInfo = { ...data }
      this.queryForm.projectId = this.managementInfo.projectId
      await this.loadQualityInspectionData()
    },

    // 加载质量检查数据
    async loadQualityInspectionData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }

        const response = await getQualityInspectionPage(params)
        if (response.code === 1) {
          this.qualityInspectionList = response.data || []
          this.pagination.total = response.result?.total || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载质量检查数据失败：', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 重置查询条件
    resetQueryForm() {
      this.queryForm = {
        projectId: this.managementInfo.projectId,
        inspectionNo: '',
        inspectionName: '',
        inspectionType: null,
        checkResult: null,
        rectificationStatus: null,
        keyword: ''
      }
      this.pagination.pageNum = 1
      this.loadQualityInspectionData()
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.qualityInspectionList = []
      this.managementInfo = {}
      this.queryForm = {
        projectId: null,
        inspectionNo: '',
        inspectionName: '',
        inspectionType: null,
        checkResult: null,
        rectificationStatus: null,
        keyword: ''
      }
      this.pagination = {
        pageNum: 1,
        pageSize: 10,
        total: 0
      }
    },

    // 新增质量检查
    handleAddInspection() {
      this.resetInspectionForm()
      this.inspectionForm.projectId = this.managementInfo.projectId
      this.inspectionDialogVisible = true
    },

    // 编辑质量检查
    handleEditInspection(row) {
      this.inspectionForm = { ...row }
      this.inspectionDialogVisible = true
    },

    // 删除质量检查
    handleDeleteInspection(row) {
      this.$confirm('确定要删除这条质量检查记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteQualityInspection(row.id)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadQualityInspectionData()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除失败：', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 查看详情
    handleViewDetail(row) {
      this.currentInspection = { ...row }
      this.detailDialogVisible = true
    },

    // 整改
    handleRectify(row) {
      this.currentInspection = { ...row }
      this.rectifyForm = {
        rectificationDescription: '',
        rectificationPersonId: null
      }
      this.rectifyDialogVisible = true
    },

    // 复查
    handleRecheck(row) {
      this.currentInspection = { ...row }
      this.recheckForm = {
        recheckResult: null,
        recheckComments: '',
        recheckPersonId: null
      }
      this.recheckDialogVisible = true
    },

    // 重置检查表单
    resetInspectionForm() {
      this.inspectionForm = {
        id: null,
        projectId: null,
        taskId: null,
        inspectionNo: '',
        inspectionName: '',
        inspectionType: null,
        inspectionDate: '',
        inspectorId: null,
        inspectionScope: '',
        inspectionStandards: '',
        inspectionMethods: '',
        inspectionResults: '',
        checkResult: null,
        identifiedIssues: '',
        rectificationRequirements: '',
        rectificationDeadline: '',
        rectificationPersonId: null,
        rectificationStatus: 1
      }
    },

    // 保存质量检查
    async handleSaveInspection() {
      try {
        await this.$refs.inspectionFormRef.validate()
        this.saving = true

        const isEdit = !!this.inspectionForm.id
        const apiMethod = isEdit ? updateQualityInspection : createQualityInspection

        const response = await apiMethod(this.inspectionForm)
        if (response.code === 1) {
          this.$message.success(isEdit ? '更新成功' : '创建成功')
          this.inspectionDialogVisible = false
          this.loadQualityInspectionData()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        console.error('保存失败：', error)
        this.$message.error('保存失败')
      } finally {
        this.saving = false
      }
    },

    // 保存整改
    async handleSaveRectify() {
      try {
        await this.$refs.rectifyFormRef.validate()
        this.saving = true

        const response = await rectifyQualityInspection(this.currentInspection.id, this.rectifyForm)
        if (response.code === 1) {
          this.$message.success('整改保存成功')
          this.rectifyDialogVisible = false
          this.loadQualityInspectionData()
        } else {
          this.$message.error(response.msg || '整改保存失败')
        }
      } catch (error) {
        console.error('整改保存失败：', error)
        this.$message.error('整改保存失败')
      } finally {
        this.saving = false
      }
    },

    // 保存复查
    async handleSaveRecheck() {
      try {
        await this.$refs.recheckFormRef.validate()
        this.saving = true

        const response = await recheckQualityInspection(this.currentInspection.id, this.recheckForm)
        if (response.code === 1) {
          this.$message.success('复查保存成功')
          this.recheckDialogVisible = false
          this.loadQualityInspectionData()
        } else {
          this.$message.error(response.msg || '复查保存失败')
        }
      } catch (error) {
        console.error('复查保存失败：', error)
        this.$message.error('复查保存失败')
      } finally {
        this.saving = false
      }
    },

    // 分页相关
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.pageNum = 1
      this.loadQualityInspectionData()
    },

    handleCurrentChange(val) {
      this.pagination.pageNum = val
      this.loadQualityInspectionData()
    },

    // 获取检查类型名称
    getInspectionTypeName(type) {
      const typeMap = {
        1: '自检',
        2: '互检',
        3: '专检',
        4: '验收'
      }
      return typeMap[type] || '未知'
    },

    // 获取检查类型标签样式
    getInspectionTypeTagType(type) {
      const typeMap = {
        1: 'info',
        2: 'primary',
        3: 'warning',
        4: 'success'
      }
      return typeMap[type] || 'info'
    },

    // 获取检查结论名称
    getCheckResultName(result) {
      const resultMap = {
        1: '合格',
        2: '不合格',
        3: '待整改'
      }
      return resultMap[result] || '未知'
    },

    // 获取检查结论标签样式
    getCheckResultTagType(result) {
      const typeMap = {
        1: 'success',
        2: 'danger',
        3: 'warning'
      }
      return typeMap[result] || 'info'
    },

    // 获取整改状态名称
    getRectificationStatusName(status) {
      const statusMap = {
        1: '待整改',
        2: '整改中',
        3: '已整改',
        4: '已验收'
      }
      return statusMap[status] || '未知'
    },

    // 获取整改状态标签样式
    getRectificationStatusTagType(status) {
      const typeMap = {
        1: 'danger',
        2: 'warning',
        3: 'primary',
        4: 'success'
      }
      return typeMap[status] || 'info'
    },

    // 获取复查结果名称
    getRecheckResultName(result) {
      const resultMap = {
        1: '合格',
        2: '不合格'
      }
      return resultMap[result] || '未知'
    },

    // 获取管理状态名称
    getManagementStatusName(status) {
      const statusMap = {
        1: '正常',
        2: '预警',
        3: '异常',
        4: '暂停'
      }
      return statusMap[status] || '未知'
    },

    // 获取管理状态样式
    getManagementStatusType(status) {
      const statusMap = {
        1: 'success',
        2: 'warning',
        3: 'danger',
        4: 'info'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}

.search-form {
  background: #fafafa;
  border-radius: 4px;
}

.project-info-header {
  background: #f5f7fa;
  border-radius: 4px;
}

.el-table {
  font-size: 12px;
}

.el-table .el-button--text {
  padding: 0;
  margin-right: 8px;
}

.el-descriptions {
  margin-top: 20px;
}

.el-descriptions-item__label {
  font-weight: bold;
}
</style>
