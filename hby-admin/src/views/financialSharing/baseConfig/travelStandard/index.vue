<template>
  <div class="travel-standard-container">
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
        <el-form-item label="城市级别" prop="cityLevel">
          <el-select
            v-model="searchForm.cityLevel"
            placeholder="请选择城市级别"
            clearable
            style="width: 150px"
          >
            <el-option label="一线城市" value="FIRST_TIER" />
            <el-option label="二线城市" value="SECOND_TIER" />
            <el-option label="三线城市" value="THIRD_TIER" />
            <el-option label="其他城市" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="职位级别" prop="positionLevel">
          <el-select
            v-model="searchForm.positionLevel"
            placeholder="请选择职位级别"
            clearable
            style="width: 150px"
          >
            <el-option label="高级管理人员" value="SENIOR_MANAGER" />
            <el-option label="中级管理人员" value="MIDDLE_MANAGER" />
            <el-option label="普通员工" value="EMPLOYEE" />
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
        <el-table-column prop="cityLevelName" label="城市级别" width="100" />
        <el-table-column prop="positionLevelName" label="职位级别" width="120" />
        <el-table-column prop="effectiveDate" label="生效日期" width="120" />
        <el-table-column prop="expiryDate" label="失效日期" width="120" />
        <el-table-column prop="orgName" label="所属组织" width="120" />
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="info" @click="handleViewDetails(scope.row)">明细</el-button>
            <el-button size="mini" type="warning" @click="handleCopy(scope.row)">复制</el-button>
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
      width="700px"
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
            <el-form-item label="城市级别" prop="cityLevel">
              <el-select v-model="formData.cityLevel" placeholder="请选择城市级别" style="width: 100%">
                <el-option label="一线城市" value="FIRST_TIER" />
                <el-option label="二线城市" value="SECOND_TIER" />
                <el-option label="三线城市" value="THIRD_TIER" />
                <el-option label="其他城市" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位级别" prop="positionLevel">
              <el-select v-model="formData.positionLevel" placeholder="请选择职位级别" style="width: 100%">
                <el-option label="高级管理人员" value="SENIOR_MANAGER" />
                <el-option label="中级管理人员" value="MIDDLE_MANAGER" />
                <el-option label="普通员工" value="EMPLOYEE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="formData.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                style="width: 100%"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker
                v-model="formData.expiryDate"
                type="date"
                placeholder="选择失效日期"
                style="width: 100%"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="formData.isEnabled" />
        </el-form-item>
        <el-form-item label="标准描述" prop="remark">
          <el-input
            v-model="formData.remark"
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

    <!-- 复制对话框 -->
    <el-dialog
      title="复制差旅标准"
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

    <!-- 明细对话框 -->
    <el-dialog
      :title="`差旅标准明细 - ${currentStandard ? currentStandard.standardName : ''}`"
      :visible.sync="detailsDialogVisible"
      width="80%"
      top="5vh"
    >
      <div v-loading="detailsLoading">
        <div style="margin-bottom: 10px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddDetail">添加明细</el-button>
        </div>
        <el-table :data="detailsList" border stripe>
          <el-table-column prop="expenseType" label="费用类型" width="150">
            <template slot-scope="scope">
              {{ getExpenseTypeText(scope.row.expenseType) }}
            </template>
          </el-table-column>
          <el-table-column prop="quotaType" label="额度类型" width="120">
            <template slot-scope="scope">
              {{ getQuotaTypeText(scope.row.quotaType) }}
            </template>
          </el-table-column>
          <el-table-column prop="quotaValue" label="额度值" width="120" align="right">
            <template slot-scope="scope">
              {{ scope.row.quotaValue ? scope.row.quotaValue.toFixed(2) : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="unit" label="单位" width="100" />
          <el-table-column prop="maxAmount" label="最高限额" width="120" align="right">
            <template slot-scope="scope">
              {{ scope.row.maxAmount ? scope.row.maxAmount.toFixed(2) : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="description" label="说明" min-width="200" show-overflow-tooltip />
          <el-table-column label="操作" width="150" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleEditDetail(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDeleteDetail(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!detailsList || detailsList.length === 0" description="暂无明细数据" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 明细编辑对话框 -->
    <el-dialog
      :title="detailDialogTitle"
      :visible.sync="detailDialogVisible"
      width="600px"
      append-to-body
    >
      <el-form :model="detailForm" :rules="detailRules" ref="detailFormRef" label-width="120px">
        <el-form-item label="费用类型" prop="expenseType">
          <el-select v-model="detailForm.expenseType" placeholder="请选择费用类型" style="width: 100%">
            <el-option label="住宿费" value="ACCOMMODATION" />
            <el-option label="交通费" value="TRANSPORTATION" />
            <el-option label="餐费" value="MEAL" />
            <el-option label="日补贴" value="ALLOWANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="配额类型" prop="quotaType">
          <el-select v-model="detailForm.quotaType" placeholder="请选择配额类型" style="width: 100%">
            <el-option label="按天" value="DAY" />
            <el-option label="按次" value="TIMES" />
            <el-option label="按月" value="MONTH" />
          </el-select>
        </el-form-item>
        <el-form-item label="配额值" prop="quotaValue">
          <el-input-number v-model="detailForm.quotaValue" :precision="2" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="detailForm.unit" placeholder="请输入单位，如：元/天" />
        </el-form-item>
        <el-form-item label="最高限额" prop="maxAmount">
          <el-input-number v-model="detailForm.maxAmount" :precision="2" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="说明" prop="description">
          <el-input v-model="detailForm.description" type="textarea" :rows="3" placeholder="请输入说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveDetail" :loading="saveDetailLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { travelStandardApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'TravelStandard',
  data() {
    return {
      loading: false,
      saveLoading: false,
      copyLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        standardName: '',
        cityLevel: '',
        positionLevel: '',
        isEnabled: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增差旅标准',
      formData: {
        standardId: null,
        standardCode: '',
        standardName: '',
        cityLevel: '',
        positionLevel: '',
        effectiveDate: '',
        expiryDate: '',
        isEnabled: true,
        remark: ''
      },
      formRules: {
        standardCode: [
          { required: true, message: '请输入标准编码', trigger: 'blur' }
        ],
        standardName: [
          { required: true, message: '请输入标准名称', trigger: 'blur' }
        ],
        cityLevel: [
          { required: true, message: '请选择城市级别', trigger: 'change' }
        ],
        positionLevel: [
          { required: true, message: '请选择职位级别', trigger: 'change' }
        ],
        effectiveDate: [
          { required: true, message: '请选择生效日期', trigger: 'change' }
        ]
      },
      copyDialogVisible: false,
      copyForm: {
        standardName: '',
        standardCode: ''
      },
      currentCopyRow: null,
      // 明细对话框相关
      detailsDialogVisible: false,
      detailsLoading: false,
      currentStandard: null,
      detailsList: [],
      // 明细编辑对话框相关
      detailDialogVisible: false,
      detailDialogTitle: '',
      saveDetailLoading: false,
      detailForm: {
        detailId: null,
        standardId: '',
        expenseType: '',
        quotaType: '',
        quotaValue: null,
        unit: '',
        maxAmount: null,
        description: ''
      },
      detailRules: {
        expenseType: [{ required: true, message: '请选择费用类型', trigger: 'change' }],
        quotaType: [{ required: true, message: '请选择配额类型', trigger: 'change' }],
        quotaValue: [{ required: true, message: '请输入配额值', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await travelStandardApi.getList(params)
        if (response.code === 1) {
          // 城市级别映射
          const cityLevelMap = {
            'FIRST_TIER': '一线城市',
            'SECOND_TIER': '二线城市',
            'THIRD_TIER': '三线城市',
            'OTHER': '其他城市'
          }
          // 职位级别映射
          const positionLevelMap = {
            'SENIOR_MANAGER': '高级管理人员',
            'MIDDLE_MANAGER': '中级管理人员',
            'EMPLOYEE': '普通员工'
          }

          // 转换数据格式
          this.tableData = (response.data.tlist || []).map(item => ({
            ...item,
            isEnabled: item.isEnabled === 1 || item.isEnabled === true,
            cityLevelName: cityLevelMap[item.cityLevel] || item.cityLevel,
            positionLevelName: positionLevelMap[item.positionLevel] || item.positionLevel
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
      this.dialogTitle = '新增差旅标准'
      this.formData = {
        standardId: null,
        standardCode: '',
        standardName: '',
        cityLevel: '',
        positionLevel: '',
        effectiveDate: '',
        expiryDate: '',
        isEnabled: true,
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑差旅标准'
      // 转换数据格式：将整数转换为布尔值
      this.formData = {
        ...row,
        isEnabled: row.isEnabled === 1 || row.isEnabled === true
      }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        // 转换数据格式：将布尔值转换为整数
        const submitData = {
          ...this.formData,
          isEnabled: this.formData.isEnabled ? 1 : 0
        }
        const response = await travelStandardApi.save(submitData)
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
        await this.$confirm('确定要删除该差旅标准吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await travelStandardApi.delete(row.standardId)
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
        await this.$confirm('确定要删除选中的差旅标准吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const standardIds = this.multipleSelection.map(item => item.standardId)
        const response = await travelStandardApi.batchDelete(standardIds)
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
        const response = await travelStandardApi.updateStatus(row.standardId, !row.isEnabled)
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
    async handleViewDetails(row) {
      try {
        this.detailsLoading = true
        this.currentStandard = row
        const response = await travelStandardApi.getDetails(row.standardId)
        if (response.code === 1) {
          this.detailsList = response.data || []
          this.detailsDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取明细失败')
        }
      } catch (error) {
        this.$message.error('获取明细失败：' + error.message)
      } finally {
        this.detailsLoading = false
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
        const response = await travelStandardApi.copy(this.currentCopyRow.standardId, this.copyForm)
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
    },
    // 添加明细
    handleAddDetail() {
      this.detailDialogTitle = '添加明细'
      this.detailForm = {
        detailId: null,
        standardId: this.currentStandard.standardId,
        expenseType: '',
        quotaType: '',
        quotaValue: null,
        unit: '',
        maxAmount: null,
        description: ''
      }
      this.detailDialogVisible = true
    },
    // 编辑明细
    handleEditDetail(row) {
      this.detailDialogTitle = '编辑明细'
      this.detailForm = { ...row }
      this.detailDialogVisible = true
    },
    // 保存明细
    async handleSaveDetail() {
      try {
        await this.$refs.detailFormRef.validate()
        this.saveDetailLoading = true
        const response = await travelStandardApi.saveDetail(this.detailForm)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.detailDialogVisible = false
          // 重新加载明细列表
          await this.loadDetailsList()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        if (error !== false) { // 表单验证失败时不显示错误
          this.$message.error('保存失败：' + error.message)
        }
      } finally {
        this.saveDetailLoading = false
      }
    },
    // 删除明细
    async handleDeleteDetail(row) {
      try {
        await this.$confirm('确定要删除这条明细吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await travelStandardApi.deleteDetail(row.detailId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          // 重新加载明细列表
          await this.loadDetailsList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    // 加载明细列表
    async loadDetailsList() {
      try {
        this.detailsLoading = true
        const response = await travelStandardApi.getDetails(this.currentStandard.standardId)
        if (response.code === 1) {
          this.detailsList = response.data || []
        } else {
          this.$message.error(response.msg || '加载明细失败')
        }
      } catch (error) {
        this.$message.error('加载明细失败：' + error.message)
      } finally {
        this.detailsLoading = false
      }
    },
    // 费用类型文本转换
    getExpenseTypeText(type) {
      const map = {
        'ACCOMMODATION': '住宿费',
        'TRANSPORTATION': '交通费',
        'MEAL': '餐费',
        'ALLOWANCE': '日补贴'
      }
      return map[type] || type
    },
    // 配额类型文本转换
    getQuotaTypeText(type) {
      const map = {
        'DAY': '按天',
        'TIMES': '按次',
        'MONTH': '按月'
      }
      return map[type] || type
    }
  }
}
</script>

<style scoped>
.travel-standard-container {
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
