<template>
  <el-dialog
    title="项目详情"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form :model="form" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目编号">
            <el-input v-model="form.projectCode" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称">
            <el-input v-model="form.projectName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目类型">
            <el-select v-model="form.projectType" placeholder="请选择项目类型">
              <el-option label="技术项目" value="technical"></el-option>
              <el-option label="业务项目" value="business"></el-option>
              <el-option label="基础设施" value="infrastructure"></el-option>
              <el-option label="研发项目" value="research"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目经理">
            <el-input v-model="form.projectManager"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始日期">
            <el-date-picker
              v-model="form.startDate"
              type="date"
              placeholder="选择开始日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期">
            <el-date-picker
              v-model="form.endDate"
              type="date"
              placeholder="选择结束日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="项目预算">
            <el-input v-model="form.budget" type="number">
              <template slot="append">万元</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="实际成本">
            <el-input v-model="form.actualCost" type="number">
              <template slot="append">万元</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="项目进度">
            <el-input :value="form.progress + '%'" disabled>
              <template slot="append">%</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="项目描述">
        <el-input type="textarea" v-model="form.description" :rows="3"></el-input>
      </el-form-item>
      
      <el-form-item label="项目成员">
        <el-table :data="form.members" border>
          <el-table-column prop="memberName" label="成员姓名" width="150"></el-table-column>
          <el-table-column prop="role" label="角色" width="120"></el-table-column>
          <el-table-column prop="department" label="部门" width="150"></el-table-column>
          <el-table-column prop="joinDate" label="加入日期" width="120"></el-table-column>
          <el-table-column prop="workload" label="工作量" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.workload }}%
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getMemberStatusType(scope.row.status)">
                {{ getMemberStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="editMember(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      
      <el-form-item label="项目里程碑">
        <el-table :data="form.milestones" border>
          <el-table-column prop="milestoneName" label="里程碑名称" width="200"></el-table-column>
          <el-table-column prop="plannedDate" label="计划日期" width="120"></el-table-column>
          <el-table-column prop="actualDate" label="实际日期" width="120"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getMilestoneStatusType(scope.row.status)">
                {{ getMilestoneStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述"></el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="editMilestone(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      
      <el-form-item label="项目状态">
        <el-radio-group v-model="form.status">
          <el-radio label="planning">规划中</el-radio>
          <el-radio label="active">进行中</el-radio>
          <el-radio label="completed">已完成</el-radio>
          <el-radio label="paused">已暂停</el-radio>
          <el-radio label="cancelled">已取消</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="备注">
        <el-input type="textarea" v-model="form.remarks" :rows="3"></el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="saveProject">保存项目</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ProjectDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    projectData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      form: {
        projectCode: 'PRJ001',
        projectName: '企业数字化转型项目',
        projectType: 'technical',
        projectManager: '张三',
        startDate: '2024-01-01',
        endDate: '2024-06-30',
        budget: 500,
        actualCost: 350,
        progress: 70,
        description: '通过数字化技术改造提升企业运营效率和竞争力',
        status: 'active',
        remarks: '项目进展顺利，按计划推进',
        members: [
          {
            memberName: '张三',
            role: '项目经理',
            department: '技术部',
            joinDate: '2024-01-01',
            workload: 100,
            status: 'active'
          },
          {
            memberName: '李四',
            role: '技术负责人',
            department: '技术部',
            joinDate: '2024-01-01',
            workload: 80,
            status: 'active'
          },
          {
            memberName: '王五',
            role: '业务分析师',
            department: '业务部',
            joinDate: '2024-01-15',
            workload: 60,
            status: 'active'
          }
        ],
        milestones: [
          {
            milestoneName: '需求分析完成',
            plannedDate: '2024-02-01',
            actualDate: '2024-02-01',
            status: 'completed',
            description: '完成业务需求分析和技术方案设计'
          },
          {
            milestoneName: '系统开发完成',
            plannedDate: '2024-04-30',
            actualDate: '',
            status: 'in_progress',
            description: '完成核心系统开发和单元测试'
          },
          {
            milestoneName: '系统上线',
            plannedDate: '2024-06-30',
            actualDate: '',
            status: 'pending',
            description: '系统正式上线运行'
          }
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
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    getMemberStatusType(status) {
      const statusMap = {
        'active': 'success',
        'inactive': 'warning',
        'left': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getMemberStatusText(status) {
      const textMap = {
        'active': '在职',
        'inactive': '暂停',
        'left': '离职'
      }
      return textMap[status] || status
    },
    getMilestoneStatusType(status) {
      const statusMap = {
        'pending': 'info',
        'in_progress': 'primary',
        'completed': 'success',
        'delayed': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getMilestoneStatusText(status) {
      const textMap = {
        'pending': '待开始',
        'in_progress': '进行中',
        'completed': '已完成',
        'delayed': '已延期'
      }
      return textMap[status] || status
    },
    editMember(row) {
      this.$message.info('编辑成员：' + row.memberName)
    },
    editMilestone(row) {
      this.$message.info('编辑里程碑：' + row.milestoneName)
    },
    saveProject() {
      this.$message.success('保存项目成功')
      this.handleClose()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
