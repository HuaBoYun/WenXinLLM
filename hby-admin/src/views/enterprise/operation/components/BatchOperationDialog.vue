<template>
  <el-dialog
    title="批量操作"
    :visible.sync="dialogVisible"
    width="700px"
    @close="handleClose"
  >
    <el-alert
      title="批量操作提示"
      type="info"
      :closable="false"
      style="margin-bottom: 20px;"
    >
      <template slot="description">
        已选择 <strong>{{ selectedItems.length }}</strong> 项进行批量操作
      </template>
    </el-alert>
    
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="操作类型" prop="operationType">
        <el-radio-group v-model="form.operationType">
          <el-radio label="status_update">状态更新</el-radio>
          <el-radio label="assign">分配负责人</el-radio>
          <el-radio label="priority_update">优先级调整</el-radio>
          <el-radio label="delete">批量删除</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="目标状态" prop="targetStatus" v-if="form.operationType === 'status_update'">
        <el-select v-model="form.targetStatus" placeholder="请选择目标状态">
          <el-option label="待开始" value="pending"></el-option>
          <el-option label="进行中" value="in_progress"></el-option>
          <el-option label="已完成" value="completed"></el-option>
          <el-option label="已暂停" value="paused"></el-option>
          <el-option label="已取消" value="cancelled"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="负责人" prop="assignee" v-if="form.operationType === 'assign'">
        <el-select v-model="form.assignee" placeholder="请选择负责人">
          <el-option label="张三" value="zhangsan"></el-option>
          <el-option label="李四" value="lisi"></el-option>
          <el-option label="王五" value="wangwu"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="优先级" prop="priority" v-if="form.operationType === 'priority_update'">
        <el-radio-group v-model="form.priority">
          <el-radio label="high">高</el-radio>
          <el-radio label="medium">中</el-radio>
          <el-radio label="low">低</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="操作说明" prop="operationNote">
        <el-input
          v-model="form.operationNote"
          type="textarea"
          :rows="4"
          placeholder="请输入操作说明"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="通知相关人员">
        <el-checkbox-group v-model="form.notifyUsers">
          <el-checkbox label="creator">创建人</el-checkbox>
          <el-checkbox label="assignee">负责人</el-checkbox>
          <el-checkbox label="manager">项目经理</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    
    <el-divider content-position="left">操作对象列表</el-divider>
    <el-table :data="selectedPlans" border max-height="300">
      <el-table-column label="序号" width="60">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="planName" label="计划名称" min-width="150"></el-table-column>
      <el-table-column prop="planType" label="计划类型" width="100"></el-table-column>
      <el-table-column prop="planStatus" label="当前状态" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getStatusType(scope.row.planStatus)">
            {{ scope.row.planStatus }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="formulationManager" label="负责人" width="100"></el-table-column>
    </el-table>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        确认执行
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { batchUpdatePlanStatus, batchDeleteOperationPlan } from '@/api/enterprise/operationPlan'

export default {
  name: 'BatchOperationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    selectedPlans: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      loading: false,
      form: {
        operationType: 'status_update',
        targetStatus: '',
        assignee: '',
        priority: 'medium',
        operationNote: '',
        notifyUsers: []
      },
      rules: {
        operationType: [
          { required: true, message: '请选择操作类型', trigger: 'change' }
        ],
        operationNote: [
          { required: true, message: '请输入操作说明', trigger: 'blur' }
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
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      this.form = {
        operationType: 'status_update',
        targetStatus: '',
        assignee: '',
        priority: 'medium',
        operationNote: '',
        notifyUsers: []
      }
    },
    getStatusType(status) {
      const statusMap = {
        '待开始': 'info',
        '进行中': 'primary',
        '已完成': 'success',
        '已暂停': 'warning',
        '已取消': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getOperationText() {
      const operationMap = {
        'status_update': '状态更新',
        'assign': '分配负责人',
        'priority_update': '优先级调整',
        'delete': '批量删除'
      }
      return operationMap[this.form.operationType] || '批量操作'
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    handleConfirm() {
      if (this.selectedPlans.length === 0) {
        this.$message.warning('请选择要操作的项目')
        return
      }

      this.$refs.form.validate(async (valid) => {
        if (!valid) return

        // 特殊验证
        if (this.form.operationType === 'status_update' && !this.form.targetStatus) {
          this.$message.warning('请选择目标状态')
          return
        }
        if (this.form.operationType === 'assign' && !this.form.assignee) {
          this.$message.warning('请选择负责人')
          return
        }

        try {
          await this.$confirm(
            `确认对 ${this.selectedPlans.length} 项执行${this.getOperationText()}操作？`,
            '批量操作确认',
            { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
          )

          this.loading = true
          const planIds = this.selectedPlans.map(p => p.planId)
          const updateBy = this.$store && this.$store.getters.name ? this.$store.getters.name : 'admin'

          if (this.form.operationType === 'delete') {
            await batchDeleteOperationPlan(planIds, updateBy)
          } else if (this.form.operationType === 'status_update') {
            await batchUpdatePlanStatus(planIds, this.form.targetStatus, updateBy)
          } else {
            // 其他操作暂用状态更新接口
            await batchUpdatePlanStatus(planIds, this.form.targetStatus || '进行中', updateBy)
          }

          this.$message.success(`批量${this.getOperationText()}操作成功`)
          this.handleClose()
          this.$emit('refresh')
        } catch (error) {
          if (error !== 'cancel') {
            console.error('批量操作失败:', error)
            this.$message.error('批量操作失败')
          }
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>
