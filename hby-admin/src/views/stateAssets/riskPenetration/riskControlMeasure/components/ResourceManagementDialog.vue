<template>
  <el-dialog
    title="资源管理"
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
    top="5vh"
  >
    <div v-loading="loading" class="resource-management">
      <!-- 措施信息 -->
      <div class="measure-info">
        <h4>措施信息</h4>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>企业名称：</label>
              <span>{{ measureData.enterpriseName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>措施名称：</label>
              <span>{{ measureData.measureName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>预算金额：</label>
              <span class="budget-amount">{{ formatAmount(measureData.budgetAmount) }}元</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 资源概览 -->
      <div class="resource-overview">
        <h4>资源概览</h4>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="overview-card human-resources">
              <div class="card-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="card-content">
                <div class="card-number">{{ humanResources.length }}</div>
                <div class="card-label">人力资源</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-card material-resources">
              <div class="card-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-content">
                <div class="card-number">{{ materialResources.length }}</div>
                <div class="card-label">物质资源</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-card budget-used">
              <div class="card-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-content">
                <div class="card-number">{{ calculateTotalCost() }}</div>
                <div class="card-label">已用预算(元)</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-card budget-remaining">
              <div class="card-icon">
                <i class="el-icon-wallet"></i>
              </div>
              <div class="card-content">
                <div class="card-number">{{ calculateRemainingBudget() }}</div>
                <div class="card-label">剩余预算(元)</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 资源管理标签页 -->
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 人力资源 -->
        <el-tab-pane label="人力资源" name="human">
          <div class="resource-section">
            <div class="section-header">
              <h5>人力资源配置</h5>
              <el-button type="primary" size="small" @click="handleAddHumanResource">
                <i class="el-icon-plus"></i>
                添加人员
              </el-button>
            </div>
            
            <el-table :data="humanResources" border stripe>
              <el-table-column prop="personName" label="姓名" width="100" />
              <el-table-column prop="role" label="角色" width="120" />
              <el-table-column prop="department" label="部门" width="150" />
              <el-table-column prop="workload" label="工作量" width="100">
                <template slot-scope="scope">
                  {{ scope.row.workload }}%
                </template>
              </el-table-column>
              <el-table-column prop="startDate" label="开始日期" width="120" />
              <el-table-column prop="endDate" label="结束日期" width="120" />
              <el-table-column prop="status" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getResourceStatusTagType(scope.row.status)" size="small">
                    {{ getResourceStatusLabel(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="handleEditHumanResource(scope.row, scope.$index)">
                    编辑
                  </el-button>
                  <el-button type="text" size="small" @click="handleRemoveHumanResource(scope.$index)">
                    移除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 物质资源 -->
        <el-tab-pane label="物质资源" name="material">
          <div class="resource-section">
            <div class="section-header">
              <h5>物质资源配置</h5>
              <el-button type="primary" size="small" @click="handleAddMaterialResource">
                <i class="el-icon-plus"></i>
                添加资源
              </el-button>
            </div>
            
            <el-table :data="materialResources" border stripe>
              <el-table-column prop="resourceName" label="资源名称" width="150" />
              <el-table-column prop="resourceType" label="资源类型" width="120" />
              <el-table-column prop="quantity" label="数量" width="80" />
              <el-table-column prop="unit" label="单位" width="80" />
              <el-table-column prop="unitCost" label="单价" width="100">
                <template slot-scope="scope">
                  ¥{{ scope.row.unitCost }}
                </template>
              </el-table-column>
              <el-table-column prop="totalCost" label="总价" width="120">
                <template slot-scope="scope">
                  <span class="cost-value">¥{{ scope.row.totalCost }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="supplier" label="供应商" show-overflow-tooltip />
              <el-table-column prop="status" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getResourceStatusTagType(scope.row.status)" size="small">
                    {{ getResourceStatusLabel(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="handleEditMaterialResource(scope.row, scope.$index)">
                    编辑
                  </el-button>
                  <el-button type="text" size="small" @click="handleRemoveMaterialResource(scope.$index)">
                    移除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 预算分析 -->
        <el-tab-pane label="预算分析" name="budget">
          <div class="budget-analysis">
            <div class="budget-chart">
              <h5>预算使用分析</h5>
              <div class="budget-progress">
                <div class="progress-item">
                  <label>人力成本：</label>
                  <div class="progress-bar">
                    <el-progress
                      :percentage="getHumanCostPercentage()"
                      :color="getProgressColor(getHumanCostPercentage())"
                      :show-text="false"
                    />
                    <span class="progress-text">{{ formatAmount(calculateHumanCost()) }}元</span>
                  </div>
                </div>
                <div class="progress-item">
                  <label>物质成本：</label>
                  <div class="progress-bar">
                    <el-progress
                      :percentage="getMaterialCostPercentage()"
                      :color="getProgressColor(getMaterialCostPercentage())"
                      :show-text="false"
                    />
                    <span class="progress-text">{{ formatAmount(calculateMaterialCost()) }}元</span>
                  </div>
                </div>
                <div class="progress-item">
                  <label>总使用率：</label>
                  <div class="progress-bar">
                    <el-progress
                      :percentage="getTotalUsagePercentage()"
                      :color="getProgressColor(getTotalUsagePercentage())"
                      :show-text="false"
                    />
                    <span class="progress-text">{{ getTotalUsagePercentage() }}%</span>
                  </div>
                </div>
              </div>
            </div>

            <div class="cost-breakdown">
              <h5>成本分解</h5>
              <el-table :data="getCostBreakdown()" border stripe>
                <el-table-column prop="category" label="类别" width="120" />
                <el-table-column prop="amount" label="金额" width="120">
                  <template slot-scope="scope">
                    ¥{{ formatAmount(scope.row.amount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="percentage" label="占比" width="100">
                  <template slot-scope="scope">
                    {{ scope.row.percentage }}%
                  </template>
                </el-table-column>
                <el-table-column prop="description" label="说明" show-overflow-tooltip />
              </el-table>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 人力资源对话框 -->
    <el-dialog
      :title="humanDialogType === 'add' ? '添加人员' : '编辑人员'"
      :visible.sync="humanDialogVisible"
      width="600px"
      append-to-body
    >
      <el-form ref="humanForm" :model="humanForm" :rules="humanRules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="personName">
              <el-input v-model="humanForm.personName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色" prop="role">
              <el-select v-model="humanForm.role" placeholder="请选择角色">
                <el-option label="项目经理" value="项目经理" />
                <el-option label="技术负责人" value="技术负责人" />
                <el-option label="业务分析师" value="业务分析师" />
                <el-option label="开发人员" value="开发人员" />
                <el-option label="测试人员" value="测试人员" />
                <el-option label="运维人员" value="运维人员" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门" prop="department">
              <el-input v-model="humanForm.department" placeholder="请输入部门" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工作量" prop="workload">
              <el-input-number
                v-model="humanForm.workload"
                :min="0"
                :max="100"
                placeholder="工作量百分比"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="humanForm.startDate"
                type="date"
                placeholder="选择开始日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="humanForm.endDate"
                type="date"
                placeholder="选择结束日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-select v-model="humanForm.status" placeholder="请选择状态">
            <el-option label="已分配" value="ALLOCATED" />
            <el-option label="待分配" value="PENDING" />
            <el-option label="已释放" value="RELEASED" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="humanDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveHumanResource">确定</el-button>
      </div>
    </el-dialog>

    <!-- 物质资源对话框 -->
    <el-dialog
      :title="materialDialogType === 'add' ? '添加资源' : '编辑资源'"
      :visible.sync="materialDialogVisible"
      width="600px"
      append-to-body
    >
      <el-form ref="materialForm" :model="materialForm" :rules="materialRules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资源名称" prop="resourceName">
              <el-input v-model="materialForm.resourceName" placeholder="请输入资源名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资源类型" prop="resourceType">
              <el-select v-model="materialForm.resourceType" placeholder="请选择资源类型">
                <el-option label="硬件设备" value="硬件设备" />
                <el-option label="软件系统" value="软件系统" />
                <el-option label="办公用品" value="办公用品" />
                <el-option label="服务费用" value="服务费用" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="数量" prop="quantity">
              <el-input-number
                v-model="materialForm.quantity"
                :min="1"
                placeholder="数量"
                style="width: 100%"
                @change="calculateTotalCostForForm"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="materialForm.unit" placeholder="单位" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单价" prop="unitCost">
              <el-input-number
                v-model="materialForm.unitCost"
                :precision="2"
                :min="0"
                placeholder="单价"
                style="width: 100%"
                @change="calculateTotalCostForForm"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="总价" prop="totalCost">
              <el-input-number
                v-model="materialForm.totalCost"
                :precision="2"
                :min="0"
                placeholder="总价"
                style="width: 100%"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="materialForm.status" placeholder="请选择状态">
                <el-option label="已分配" value="ALLOCATED" />
                <el-option label="待分配" value="PENDING" />
                <el-option label="已释放" value="RELEASED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="供应商" prop="supplier">
          <el-input v-model="materialForm.supplier" placeholder="请输入供应商" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="materialDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveMaterialResource">确定</el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveResources">保存资源配置</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateResourceAllocation } from '@/api/stateAssets/riskControlMeasure'

export default {
  name: 'ResourceManagementDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    measureData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'human',
      humanResources: [],
      materialResources: [],
      
      // 人力资源对话框
      humanDialogVisible: false,
      humanDialogType: 'add',
      humanEditIndex: -1,
      humanForm: {
        personName: '',
        role: '',
        department: '',
        workload: 100,
        startDate: '',
        endDate: '',
        status: 'ALLOCATED'
      },
      humanRules: {
        personName: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ],
        department: [
          { required: true, message: '请输入部门', trigger: 'blur' }
        ],
        workload: [
          { required: true, message: '请输入工作量', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ]
      },

      // 物质资源对话框
      materialDialogVisible: false,
      materialDialogType: 'add',
      materialEditIndex: -1,
      materialForm: {
        resourceName: '',
        resourceType: '',
        quantity: 1,
        unit: '',
        unitCost: 0,
        totalCost: 0,
        supplier: '',
        status: 'ALLOCATED'
      },
      materialRules: {
        resourceName: [
          { required: true, message: '请输入资源名称', trigger: 'blur' }
        ],
        resourceType: [
          { required: true, message: '请选择资源类型', trigger: 'change' }
        ],
        quantity: [
          { required: true, message: '请输入数量', trigger: 'blur' }
        ],
        unit: [
          { required: true, message: '请输入单位', trigger: 'blur' }
        ],
        unitCost: [
          { required: true, message: '请输入单价', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadResourceData()
      }
    }
  },
  methods: {
    // 加载资源数据
    loadResourceData() {
      // 模拟加载资源数据
      this.humanResources = [
        {
          personName: '张三',
          role: '项目经理',
          department: '风险管理部',
          workload: 100,
          startDate: '2024-01-01',
          endDate: '2024-03-31',
          status: 'ALLOCATED'
        },
        {
          personName: '李四',
          role: '业务分析师',
          department: '业务部',
          workload: 80,
          startDate: '2024-01-01',
          endDate: '2024-02-28',
          status: 'ALLOCATED'
        }
      ]

      this.materialResources = [
        {
          resourceName: '风险管理软件',
          resourceType: '软件系统',
          quantity: 1,
          unit: '套',
          unitCost: 50000,
          totalCost: 50000,
          supplier: 'ABC软件公司',
          status: 'ALLOCATED'
        },
        {
          resourceName: '服务器设备',
          resourceType: '硬件设备',
          quantity: 2,
          unit: '台',
          unitCost: 15000,
          totalCost: 30000,
          supplier: 'XYZ硬件公司',
          status: 'ALLOCATED'
        }
      ]
    },

    // 人力资源管理
    handleAddHumanResource() {
      this.humanDialogType = 'add'
      this.humanForm = {
        personName: '',
        role: '',
        department: '',
        workload: 100,
        startDate: '',
        endDate: '',
        status: 'ALLOCATED'
      }
      this.humanDialogVisible = true
    },

    handleEditHumanResource(row, index) {
      this.humanDialogType = 'edit'
      this.humanEditIndex = index
      this.humanForm = { ...row }
      this.humanDialogVisible = true
    },

    handleRemoveHumanResource(index) {
      this.$confirm('确定要移除这个人员吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.humanResources.splice(index, 1)
        this.$message.success('移除成功')
      }).catch(() => {})
    },

    async handleSaveHumanResource() {
      try {
        await this.$refs.humanForm.validate()
        
        if (this.humanDialogType === 'add') {
          this.humanResources.push({ ...this.humanForm })
        } else {
          this.$set(this.humanResources, this.humanEditIndex, { ...this.humanForm })
        }
        
        this.humanDialogVisible = false
        this.$message.success(this.humanDialogType === 'add' ? '添加成功' : '编辑成功')
      } catch (error) {
        // 验证失败
      }
    },

    // 物质资源管理
    handleAddMaterialResource() {
      this.materialDialogType = 'add'
      this.materialForm = {
        resourceName: '',
        resourceType: '',
        quantity: 1,
        unit: '',
        unitCost: 0,
        totalCost: 0,
        supplier: '',
        status: 'ALLOCATED'
      }
      this.materialDialogVisible = true
    },

    handleEditMaterialResource(row, index) {
      this.materialDialogType = 'edit'
      this.materialEditIndex = index
      this.materialForm = { ...row }
      this.materialDialogVisible = true
    },

    handleRemoveMaterialResource(index) {
      this.$confirm('确定要移除这个资源吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.materialResources.splice(index, 1)
        this.$message.success('移除成功')
      }).catch(() => {})
    },

    async handleSaveMaterialResource() {
      try {
        await this.$refs.materialForm.validate()
        
        if (this.materialDialogType === 'add') {
          this.materialResources.push({ ...this.materialForm })
        } else {
          this.$set(this.materialResources, this.materialEditIndex, { ...this.materialForm })
        }
        
        this.materialDialogVisible = false
        this.$message.success(this.materialDialogType === 'add' ? '添加成功' : '编辑成功')
      } catch (error) {
        // 验证失败
      }
    },

    calculateTotalCostForForm() {
      this.materialForm.totalCost = (this.materialForm.quantity || 0) * (this.materialForm.unitCost || 0)
    },

    // 保存资源配置
    async handleSaveResources() {
      try {
        this.loading = true

        const resourceData = {
          riskControlMeasureId: this.measureData.riskControlMeasureId,
          humanResources: JSON.stringify(this.humanResources),
          materialResources: JSON.stringify(this.materialResources),
          updateBy: this.$store.getters.userInfo.userName
        }

        const response = await updateResourceAllocation(resourceData)
        
        if (response.code === 200) {
          this.$message.success('资源配置保存成功')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        this.$message.error('保存资源配置失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.activeTab = 'human'
      this.humanResources = []
      this.materialResources = []
    },

    // 计算方法
    calculateTotalCost() {
      return this.formatAmount(this.calculateHumanCost() + this.calculateMaterialCost())
    },

    calculateHumanCost() {
      // 简化计算，实际应该根据工作量和时间计算
      return this.humanResources.length * 10000
    },

    calculateMaterialCost() {
      return this.materialResources.reduce((total, resource) => total + (resource.totalCost || 0), 0)
    },

    calculateRemainingBudget() {
      const budget = this.measureData.budgetAmount || 0
      const used = this.calculateHumanCost() + this.calculateMaterialCost()
      return this.formatAmount(budget - used)
    },

    getHumanCostPercentage() {
      const budget = this.measureData.budgetAmount || 1
      return Math.round((this.calculateHumanCost() / budget) * 100)
    },

    getMaterialCostPercentage() {
      const budget = this.measureData.budgetAmount || 1
      return Math.round((this.calculateMaterialCost() / budget) * 100)
    },

    getTotalUsagePercentage() {
      const budget = this.measureData.budgetAmount || 1
      const used = this.calculateHumanCost() + this.calculateMaterialCost()
      return Math.round((used / budget) * 100)
    },

    getCostBreakdown() {
      const humanCost = this.calculateHumanCost()
      const materialCost = this.calculateMaterialCost()
      const total = humanCost + materialCost

      return [
        {
          category: '人力成本',
          amount: humanCost,
          percentage: total > 0 ? Math.round((humanCost / total) * 100) : 0,
          description: `${this.humanResources.length}名人员`
        },
        {
          category: '物质成本',
          amount: materialCost,
          percentage: total > 0 ? Math.round((materialCost / total) * 100) : 0,
          description: `${this.materialResources.length}项资源`
        }
      ]
    },

    // 工具方法
    formatAmount(amount) {
      if (!amount) return '0'
      return amount.toLocaleString()
    },

    getProgressColor(percentage) {
      if (percentage >= 80) return '#f56c6c'
      if (percentage >= 60) return '#e6a23c'
      return '#67c23a'
    },

    getResourceStatusTagType(status) {
      const statusMap = {
        'ALLOCATED': 'success',
        'PENDING': 'warning',
        'RELEASED': 'info'
      }
      return statusMap[status] || ''
    },

    getResourceStatusLabel(status) {
      const labelMap = {
        'ALLOCATED': '已分配',
        'PENDING': '待分配',
        'RELEASED': '已释放'
      }
      return labelMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.resource-management {
  .measure-info,
  .resource-overview {
    margin-bottom: 24px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 6px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .info-item {
    margin-bottom: 12px;
    
    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }

    .budget-amount {
      font-weight: 600;
      color: #409eff;
      font-size: 16px;
    }
  }

  .resource-overview {
    .overview-card {
      display: flex;
      align-items: center;
      padding: 16px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

      .card-icon {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;

        i {
          font-size: 20px;
          color: white;
        }
      }

      &.human-resources .card-icon {
        background: linear-gradient(135deg, #409eff, #66b1ff);
      }

      &.material-resources .card-icon {
        background: linear-gradient(135deg, #67c23a, #95d475);
      }

      &.budget-used .card-icon {
        background: linear-gradient(135deg, #e6a23c, #f0c78a);
      }

      &.budget-remaining .card-icon {
        background: linear-gradient(135deg, #f56c6c, #f89898);
      }

      .card-content {
        .card-number {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
          margin-bottom: 4px;
        }

        .card-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .resource-section {
    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h5 {
        margin: 0;
        color: #303133;
        font-size: 16px;
        font-weight: 600;
      }
    }

    .cost-value {
      font-weight: 600;
      color: #e6a23c;
    }
  }

  .budget-analysis {
    .budget-chart,
    .cost-breakdown {
      margin-bottom: 24px;
      padding: 16px;
      background: white;
      border-radius: 6px;

      h5 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 16px;
        font-weight: 600;
      }
    }

    .budget-progress {
      .progress-item {
        display: flex;
        align-items: center;
        margin-bottom: 16px;

        label {
          width: 80px;
          font-weight: 600;
          color: #606266;
          margin-right: 16px;
        }

        .progress-bar {
          flex: 1;
          display: flex;
          align-items: center;

          .el-progress {
            flex: 1;
            margin-right: 12px;
          }

          .progress-text {
            min-width: 80px;
            font-size: 14px;
            color: #606266;
            text-align: right;
          }
        }
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
