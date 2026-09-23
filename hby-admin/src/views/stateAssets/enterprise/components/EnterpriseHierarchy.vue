<template>
  <el-dialog
    title="企业层级关系"
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
  >
    <div class="hierarchy-container">
      <!-- 当前企业信息 -->
      <div class="current-enterprise">
        <h4>当前企业：{{ currentEnterprise.enterpriseName }}</h4>
        <p>统一社会信用代码：{{ currentEnterprise.creditCode }}</p>
      </div>

      <el-tabs v-model="activeTab">
        <!-- 层级关系图 -->
        <el-tab-pane label="层级关系图" name="chart">
          <div class="hierarchy-chart" ref="hierarchyChart">
            <!-- 这里可以集成组织架构图组件，如 G6、ECharts 等 -->
            <div class="chart-placeholder">
              <p>层级关系图</p>
              <p>（可集成 G6 或 ECharts 组织架构图）</p>
            </div>
          </div>
        </el-tab-pane>

        <!-- 层级关系列表 -->
        <el-tab-pane label="层级关系列表" name="list">
          <div class="hierarchy-actions">
            <el-button
              type="primary"
              icon="el-icon-plus"
              @click="handleAddHierarchy"
            >
              新增层级关系
            </el-button>
          </div>

          <el-table
            :data="hierarchyList"
            border
            v-loading="listLoading"
          >
            <el-table-column prop="parentEnterpriseName" label="母公司" />
            <el-table-column prop="childEnterpriseName" label="子公司" />
            <el-table-column prop="hierarchyLevel" label="层级级别" />
            <el-table-column prop="relationshipType" label="关系类型">
              <template #default="{ row }">
                {{ getRelationshipTypeLabel(row.relationshipType) }}
              </template>
            </el-table-column>
            <el-table-column prop="effectiveDate" label="生效日期" />
            <el-table-column prop="expiryDate" label="失效日期">
              <template #default="{ row }">
                {{ row.expiryDate || '-' }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEditHierarchy(row)"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  style="color: #f56c6c"
                  @click="handleDeleteHierarchy(row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 新增/编辑层级关系弹框 -->
    <el-dialog
      :title="hierarchyFormTitle"
      :visible.sync="hierarchyFormVisible"
      width="600px"
      append-to-body
    >
      <el-form
        ref="hierarchyForm"
        :model="hierarchyForm"
        :rules="hierarchyRules"
        label-width="120px"
      >
        <el-form-item label="母公司" prop="parentEnterpriseId">
          <el-select
            v-model="hierarchyForm.parentEnterpriseId"
            placeholder="请选择母公司"
            style="width: 100%"
            filterable
          >
            <el-option
              v-for="item in enterpriseOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="子公司" prop="childEnterpriseId">
          <el-select
            v-model="hierarchyForm.childEnterpriseId"
            placeholder="请选择子公司"
            style="width: 100%"
            filterable
          >
            <el-option
              v-for="item in enterpriseOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="关系类型" prop="relationshipType">
          <el-select
            v-model="hierarchyForm.relationshipType"
            placeholder="请选择关系类型"
            style="width: 100%"
          >
            <el-option
              v-for="item in relationshipTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="生效日期" prop="effectiveDate">
          <el-date-picker
            v-model="hierarchyForm.effectiveDate"
            type="date"
            placeholder="请选择生效日期"
            style="width: 100%"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item label="失效日期" prop="expiryDate">
          <el-date-picker
            v-model="hierarchyForm.expiryDate"
            type="date"
            placeholder="请选择失效日期"
            style="width: 100%"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="hierarchyFormVisible = false">取消</el-button>
        <el-button type="primary" @click="saveHierarchy" :loading="hierarchyLoading">
          {{ hierarchyLoading ? '保存中...' : '保存' }}
        </el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getEnterpriseHierarchy,
  addEnterpriseHierarchy,
  updateEnterpriseHierarchy,
  deleteEnterpriseHierarchy,
  getEnterpriseList
} from '@/api/stateAssets/enterprise'

export default {
  name: 'EnterpriseHierarchy',
  data() {
    return {
      dialogVisible: false,
      activeTab: 'chart',
      currentEnterprise: {},
      hierarchyList: [],
      listLoading: false,
      hierarchyFormVisible: false,
      hierarchyFormTitle: '',
      hierarchyLoading: false,
      isEditHierarchy: false,
      hierarchyForm: {
        hierarchyId: '',
        parentEnterpriseId: '',
        childEnterpriseId: '',
        relationshipType: '',
        effectiveDate: '',
        expiryDate: '',
      },
      hierarchyRules: {
        parentEnterpriseId: [
          { required: true, message: '请选择母公司', trigger: 'change' },
        ],
        childEnterpriseId: [
          { required: true, message: '请选择子公司', trigger: 'change' },
        ],
        relationshipType: [
          { required: true, message: '请选择关系类型', trigger: 'change' },
        ],
        effectiveDate: [
          { required: true, message: '请选择生效日期', trigger: 'change' },
        ],
      },
      relationshipTypeOptions: [
        { label: '子公司', value: 'SUBSIDIARY' },
        { label: '分公司', value: 'BRANCH' },
        { label: '控股公司', value: 'HOLDING' },
        { label: '参股公司', value: 'PARTICIPATING' },
      ],
      enterpriseOptions: [],
    }
  },
  methods: {
    async show(row) {
      this.dialogVisible = true
      this.currentEnterprise = row
      this.activeTab = 'chart'
      await this.fetchHierarchyData()
      await this.fetchEnterpriseOptions()
    },
    async fetchHierarchyData() {
      this.listLoading = true
      try {
        const { data } = await getEnterpriseHierarchy({
          enterpriseId: this.currentEnterprise.enterpriseId
        })
        this.hierarchyList = data
      } catch (error) {
        this.$baseMessage('获取层级关系失败', 'error')
      } finally {
        this.listLoading = false
      }
    },
    async fetchEnterpriseOptions() {
      try {
        const { data } = await getEnterpriseList({ pageSize: 1000 })
        this.enterpriseOptions = data.pageInfo.tlist.map(item => ({
          label: item.enterpriseName,
          value: item.enterpriseId,
        }))
      } catch (error) {
        console.error('获取企业列表失败:', error)
      }
    },
    handleAddHierarchy() {
      this.hierarchyFormVisible = true
      this.hierarchyFormTitle = '新增层级关系'
      this.isEditHierarchy = false
      this.resetHierarchyForm()
    },
    handleEditHierarchy(row) {
      this.hierarchyFormVisible = true
      this.hierarchyFormTitle = '编辑层级关系'
      this.isEditHierarchy = true
      this.hierarchyForm = { ...row }
    },
    async handleDeleteHierarchy(row) {
      try {
        await this.$baseConfirm('确定要删除该层级关系吗？')
        await deleteEnterpriseHierarchy({ hierarchyId: row.hierarchyId })
        this.$baseMessage('删除成功', 'success')
        this.fetchHierarchyData()
        this.$emit('refresh')
      } catch (error) {
        if (error !== 'cancel') {
          this.$baseMessage('删除失败', 'error')
        }
      }
    },
    resetHierarchyForm() {
      this.hierarchyForm = {
        hierarchyId: '',
        parentEnterpriseId: '',
        childEnterpriseId: '',
        relationshipType: '',
        effectiveDate: '',
        expiryDate: '',
      }
      this.$nextTick(() => {
        this.$refs.hierarchyForm?.clearValidate()
      })
    },
    async saveHierarchy() {
      try {
        await this.$refs.hierarchyForm.validate()
        this.hierarchyLoading = true
        
        if (this.isEditHierarchy) {
          await updateEnterpriseHierarchy(this.hierarchyForm)
          this.$baseMessage('更新成功', 'success')
        } else {
          await addEnterpriseHierarchy(this.hierarchyForm)
          this.$baseMessage('新增成功', 'success')
        }
        
        this.hierarchyFormVisible = false
        this.fetchHierarchyData()
        this.$emit('refresh')
      } catch (error) {
        if (error !== false) {
          this.$baseMessage(this.isEditHierarchy ? '更新失败' : '新增失败', 'error')
        }
      } finally {
        this.hierarchyLoading = false
      }
    },
    getRelationshipTypeLabel(type) {
      const typeMap = {
        SUBSIDIARY: '子公司',
        BRANCH: '分公司',
        HOLDING: '控股公司',
        PARTICIPATING: '参股公司',
      }
      return typeMap[type] || type
    },
  },
}
</script>

<style lang="scss" scoped>
.hierarchy-container {
  .current-enterprise {
    margin-bottom: 20px;
    padding: 15px;
    background: #f5f7fa;
    border-radius: 4px;
    
    h4 {
      margin: 0 0 5px 0;
      color: #303133;
    }
    
    p {
      margin: 0;
      color: #606266;
      font-size: 14px;
    }
  }
  
  .hierarchy-chart {
    height: 400px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .chart-placeholder {
      text-align: center;
      color: #909399;
      
      p {
        margin: 5px 0;
      }
    }
  }
  
  .hierarchy-actions {
    margin-bottom: 15px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
