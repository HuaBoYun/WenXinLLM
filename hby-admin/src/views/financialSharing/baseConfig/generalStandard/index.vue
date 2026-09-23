<template>
  <div class="general-standard-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="标准名称" prop="standardName">
          <el-input
            v-model="searchForm.standardName"
            placeholder="请输入标准名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="标准类型" prop="standardType">
          <el-select
            v-model="searchForm.standardType"
            placeholder="请选择标准类型"
            clearable
            style="width: 150px"
          >
            <el-option label="差旅标准" value="TRAVEL" />
            <el-option label="招待标准" value="ENTERTAINMENT" />
            <el-option label="通讯标准" value="COMMUNICATION" />
            <el-option label="办公标准" value="OFFICE" />
            <el-option label="培训标准" value="TRAINING" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select
            v-model="searchForm.isEnabled"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="true" />
            <el-option label="禁用" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
    </div>

    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="standardCode" label="标准编码" width="150" />
        <el-table-column prop="standardName" label="标准名称" min-width="200" />
        <el-table-column prop="standardTypeName" label="标准类型" width="120" />
        <el-table-column prop="levelCount" label="级别数量" width="100" />
        <el-table-column prop="applicableScope" label="适用范围" min-width="180" show-overflow-tooltip />
        <el-table-column prop="effectiveDate" label="生效日期" width="120" />
        <el-table-column prop="expiryDate" label="失效日期" width="120" />
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orgName" label="所属组织" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="400" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="info" @click="handleLevelConfig(scope.row)">级别配置</el-button>
            <el-button size="mini" type="success" @click="handleConditions(scope.row)">适用条件</el-button>
            <el-button size="mini" type="warning" @click="handleTest(scope.row)">测试</el-button>
            <el-button size="mini" type="primary" @click="handleCopy(scope.row)">复制</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-button 
              size="mini" 
              :type="scope.row.isEnabled ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标准编码" prop="standardCode">
              <el-input v-model="formData.standardCode" placeholder="请输入标准编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标准名称" prop="standardName">
              <el-input v-model="formData.standardName" placeholder="请输入标准名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标准类型" prop="standardType">
              <el-select v-model="formData.standardType" placeholder="请选择标准类型" style="width: 100%">
                <el-option label="差旅标准" value="TRAVEL" />
                <el-option label="招待标准" value="ENTERTAINMENT" />
                <el-option label="通讯标准" value="COMMUNICATION" />
                <el-option label="办公标准" value="OFFICE" />
                <el-option label="培训标准" value="TRAINING" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="级别数量" prop="levelCount">
              <el-input-number v-model="formData.levelCount" :min="1" :max="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="formData.effectiveDate"
                type="date"
                placeholder="请选择生效日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker
                v-model="formData.expiryDate"
                type="date"
                placeholder="请选择失效日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="适用范围" prop="applicableScope">
          <el-input v-model="formData.applicableScope" placeholder="请输入适用范围" />
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="formData.isEnabled" />
        </el-form-item>
        <el-form-item label="标准描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入标准描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 测试对话框 -->
    <el-dialog
      title="通用标准测试"
      :visible.sync="testDialogVisible"
      width="600px"
    >
      <el-form :model="testForm" ref="testFormRef" label-width="120px">
        <el-form-item label="测试数据" prop="testData">
          <el-input
            v-model="testForm.testData"
            type="textarea"
            :rows="8"
            placeholder="请输入JSON格式的测试数据，如：{&quot;level&quot;: 1, &quot;amount&quot;: 1000, &quot;city&quot;: &quot;北京&quot;}"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmTest" :loading="testLoading">执行测试</el-button>
      </div>
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog
      title="复制通用标准"
      :visible.sync="copyDialogVisible"
      width="500px"
    >
      <el-form :model="copyForm" ref="copyFormRef" label-width="120px">
        <el-form-item label="新标准名称" prop="standardName" :rules="[{required: true, message: '请输入新标准名称'}]">
          <el-input v-model="copyForm.standardName" placeholder="请输入新标准名称" />
        </el-form-item>
        <el-form-item label="新标准编码" prop="standardCode" :rules="[{required: true, message: '请输入新标准编码'}]">
          <el-input v-model="copyForm.standardCode" placeholder="请输入新标准编码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmCopy" :loading="copyLoading">确定复制</el-button>
      </div>
    </el-dialog>

    <!-- 级别配置对话框 -->
    <el-dialog
      title="级别配置"
      :visible.sync="levelDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div v-if="currentStandard" style="margin-bottom: 15px;">
        <el-tag type="info">标准名称：{{ currentStandard.standardName }}</el-tag>
        <el-tag type="success" style="margin-left: 10px;">级别数量：{{ currentStandard.levelCount }}</el-tag>
      </div>

      <el-table
        :data="levelData"
        border
        v-loading="levelLoading"
        style="width: 100%"
      >
        <el-table-column prop="levelNumber" label="级别" width="80" align="center" />
        <el-table-column label="级别名称" width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.levelName" placeholder="请输入级别名称" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="标准金额" width="150">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.standardAmount"
              :precision="2"
              :step="100"
              :min="0"
              placeholder="金额"
              size="small"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column label="描述" min-width="200">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.description"
              type="textarea"
              :rows="2"
              placeholder="请输入描述"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column label="备注" width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.remark" placeholder="备注" size="small" />
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="levelDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveLevels" :loading="levelLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 适用条件配置对话框 -->
    <el-dialog
      title="适用条件配置"
      :visible.sync="conditionDialogVisible"
      width="1000px"
      :close-on-click-modal="false"
    >
      <div v-if="currentStandard" style="margin-bottom: 15px;">
        <el-tag type="info">标准名称：{{ currentStandard.standardName }}</el-tag>
        <el-tag type="success" style="margin-left: 10px;">标准类型：{{ getStandardTypeLabel(currentStandard.standardType) }}</el-tag>
      </div>

      <div style="margin-bottom: 10px;">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddCondition">添加条件</el-button>
      </div>

      <el-table
        :data="conditionList"
        border
        v-loading="conditionLoading"
        style="width: 100%"
      >
        <el-table-column label="优先级" width="80" align="center">
          <template slot-scope="scope">
            {{ scope.row.priority }}
          </template>
        </el-table-column>
        <el-table-column label="条件表达式" min-width="250">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.conditionExpression"
              type="textarea"
              :rows="2"
              placeholder="如：level == 1 && city == '北京'"
            />
          </template>
        </el-table-column>
        <el-table-column label="条件描述" min-width="200">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.conditionDesc"
              placeholder="请输入条件描述"
            />
          </template>
        </el-table-column>
        <el-table-column label="备注" min-width="150">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.remark"
              placeholder="请输入备注"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" align="center">
          <template slot-scope="scope">
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-delete"
              @click="handleDeleteCondition(scope.$index)"
            />
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="conditionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveConditions" :loading="conditionLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { generalStandardApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'GeneralStandard',
  data() {
    return {
      loading: false,
      saveLoading: false,
      testLoading: false,
      copyLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        standardName: '',
        standardType: '',
        isEnabled: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增通用标准',
      formData: {
        standardId: null,
        standardCode: '',
        standardName: '',
        standardType: '',
        levelCount: 3,
        applicableScope: '',
        effectiveDate: '',
        expiryDate: '',
        isEnabled: true,
        description: ''
      },
      formRules: {
        standardCode: [
          { required: true, message: '请输入标准编码', trigger: 'blur' }
        ],
        standardName: [
          { required: true, message: '请输入标准名称', trigger: 'blur' }
        ],
        standardType: [
          { required: true, message: '请选择标准类型', trigger: 'change' }
        ],
        effectiveDate: [
          { required: true, message: '请选择生效日期', trigger: 'change' }
        ]
      },
      testDialogVisible: false,
      testForm: {
        testData: ''
      },
      currentTestRow: null,
      copyDialogVisible: false,
      copyForm: {
        standardName: '',
        standardCode: ''
      },
      currentCopyRow: null,
      // 级别配置相关
      levelDialogVisible: false,
      currentStandard: null,
      levelData: [],
      levelLoading: false,
      // 适用条件相关
      conditionDialogVisible: false,
      conditionList: [],
      conditionLoading: false
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    // 标准类型映射
    getStandardTypeName(type) {
      const typeMap = {
        'TRAVEL': '差旅标准',
        'ENTERTAINMENT': '招待标准',
        'COMMUNICATION': '通讯标准',
        'OFFICE': '办公标准',
        'TRAINING': '培训标准',
        'OTHER': '其他标准'
      }
      return typeMap[type] || type
    },
    // 标准类型标签（用于适用条件对话框）
    getStandardTypeLabel(type) {
      return this.getStandardTypeName(type)
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await generalStandardApi.getList(params)
        if (response.code === 1) {
          // 为每条数据添加 standardTypeName 字段
          const dataList = response.data.tlist || []
          this.tableData = dataList.map(item => ({
            ...item,
            standardTypeName: this.getStandardTypeName(item.standardType)
          }))
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },
    handleAdd() {
      this.dialogTitle = '新增通用标准'
      this.formData = {
        standardId: null,
        standardCode: '',
        standardName: '',
        standardType: '',
        levelCount: 3,
        applicableScope: '',
        effectiveDate: '',
        expiryDate: '',
        isEnabled: true,
        description: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑通用标准'
      // 将后端返回的数据转换为表单需要的格式
      this.formData = {
        ...row,
        // 将数字类型的 isEnabled (1/0) 转换为布尔类型 (true/false)
        isEnabled: row.isEnabled === 1 || row.isEnabled === true
      }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        const response = await generalStandardApi.save(this.formData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.saveLoading = false
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该通用标准吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await generalStandardApi.delete(row.standardId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    async handleBatchDelete() {
      try {
        await this.$confirm('确定要删除选中的通用标准吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const standardIds = this.multipleSelection.map(item => item.standardId)
        const response = await generalStandardApi.batchDelete(standardIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },
    async handleToggleStatus(row) {
      try {
        const response = await generalStandardApi.updateStatus(row.standardId, !row.isEnabled)
        if (response.code === 1) {
          this.$message.success('状态更新成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '状态更新失败')
        }
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
      }
    },
    handleLevelConfig(row) {
      // 打开级别配置对话框
      this.currentStandard = { ...row }
      this.levelDialogVisible = true
      this.loadLevelData(row.standardId)
    },
    async loadLevelData(standardId) {
      this.levelLoading = true
      try {
        const response = await generalStandardApi.getLevels(standardId)
        if (response.code === 1) {
          this.levelData = response.data || []
          // 如果没有数据，根据级别数量初始化
          if (this.levelData.length === 0 && this.currentStandard) {
            const levelCount = this.currentStandard.levelCount || 3
            this.levelData = Array.from({ length: levelCount }, (_, index) => ({
              levelId: null,
              standardId: standardId,
              levelNumber: index + 1,
              levelName: `${index + 1}级标准`,
              standardAmount: null,
              description: '',
              remark: ''
            }))
          }
        } else {
          this.$message.error(response.msg || '加载级别配置失败')
        }
      } catch (error) {
        this.$message.error('加载级别配置失败：' + error.message)
      } finally {
        this.levelLoading = false
      }
    },
    async handleSaveLevels() {
      this.levelLoading = true
      try {
        const response = await generalStandardApi.saveLevels(this.currentStandard.standardId, this.levelData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.levelDialogVisible = false
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.levelLoading = false
      }
    },
    handleConditions(row) {
      // 打开适用条件配置对话框
      this.currentStandard = row
      this.conditionDialogVisible = true
      this.loadConditions(row.standardId)
    },
    async loadConditions(standardId) {
      try {
        this.conditionLoading = true
        const response = await generalStandardApi.getConditions(standardId)
        if (response.code === 1) {
          this.conditionList = response.data || []
        } else {
          this.$message.error('加载适用条件失败：' + response.msg)
        }
      } catch (error) {
        console.error('加载适用条件失败:', error)
        this.$message.error('加载适用条件失败：' + (error.message || error.msg))
      } finally {
        this.conditionLoading = false
      }
    },
    handleAddCondition() {
      // 添加新的适用条件
      this.conditionList.push({
        conditionId: null,
        standardId: this.currentStandard.standardId,
        conditionExpression: '',
        conditionDesc: '',
        priority: this.conditionList.length + 1,
        remark: ''
      })
    },
    handleDeleteCondition(index) {
      this.$confirm('确定要删除这条适用条件吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.conditionList.splice(index, 1)
        // 重新调整优先级
        this.conditionList.forEach((item, idx) => {
          item.priority = idx + 1
        })
      }).catch(() => {})
    },
    async handleSaveConditions() {
      try {
        // 验证数据
        for (let i = 0; i < this.conditionList.length; i++) {
          const condition = this.conditionList[i]
          if (!condition.conditionExpression || condition.conditionExpression.trim() === '') {
            this.$message.warning(`第 ${i + 1} 条适用条件的表达式不能为空`)
            return
          }
        }

        this.conditionLoading = true
        const response = await generalStandardApi.saveConditions(
          this.currentStandard.standardId,
          this.conditionList
        )

        if (response.code === 1) {
          this.$message.success('保存成功')
          this.conditionDialogVisible = false
        } else {
          this.$message.error('保存失败：' + response.msg)
        }
      } catch (error) {
        console.error('保存适用条件失败:', error)
        this.$message.error('保存失败：' + (error.message || error.msg))
      } finally {
        this.conditionLoading = false
      }
    },
    handleTest(row) {
      this.currentTestRow = row
      this.testForm = {
        testData: '{\n  "level": 1,\n  "amount": 1000,\n  "city": "北京"\n}'
      }
      this.testDialogVisible = true
    },
    async handleConfirmTest() {
      try {
        this.testLoading = true
        let testData
        try {
          testData = JSON.parse(this.testForm.testData)
        } catch (e) {
          this.$message.error('测试数据格式错误，请输入有效的JSON格式')
          return
        }
        
        const response = await generalStandardApi.test(this.currentTestRow.standardId, testData)
        if (response.code === 1) {
          const result = response.data
          this.$alert(
            `测试结果：${result.testResult}\n测试消息：${result.testMessage}\n匹配级别：${result.matchedLevel}\n标准金额：${result.standardAmount}`,
            '测试结果',
            { type: result.testResult === 'SUCCESS' ? 'success' : 'warning' }
          )
          this.testDialogVisible = false
        } else {
          this.$message.error(response.msg || '测试失败')
        }
      } catch (error) {
        this.$message.error('测试失败：' + error.message)
      } finally {
        this.testLoading = false
      }
    },
    handleCopy(row) {
      this.currentCopyRow = row
      this.copyForm = {
        standardName: row.standardName + '_副本',
        standardCode: row.standardCode + '_COPY'
      }
      this.copyDialogVisible = true
    },
    async handleConfirmCopy() {
      try {
        await this.$refs.copyFormRef.validate()
        this.copyLoading = true
        const response = await generalStandardApi.copy(this.currentCopyRow.standardId, this.copyForm)
        if (response.code === 1) {
          this.$message.success('复制成功')
          this.copyDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      } finally {
        this.copyLoading = false
      }
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },
    handleDialogClose() {
      this.$refs.formRef.resetFields()
    }
  }
}
</script>

<style scoped>
.general-standard-container {
  padding: 20px;
}

.search-container {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
