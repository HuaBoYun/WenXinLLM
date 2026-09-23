<template>
  <el-dialog
    title="组织详情"
    :visible.sync="dialogVisible"
    width="900px"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="基本信息" name="basic">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="组织名称">
            {{ orgData.orgName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="组织编码">
            {{ orgData.orgCode || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="组织类型">
            <el-tag :type="getOrgTypeTag(orgData.orgType)">
              {{ getOrgTypeText(orgData.orgType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="组织级别">
            {{ orgData.orgLevel || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="上级组织">
            {{ orgData.parentOrgName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="负责人">
            {{ orgData.manager || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="联系电话">
            {{ orgData.phone || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="邮箱地址">
            {{ orgData.email || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="成立时间">
            {{ orgData.establishDate || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="组织状态">
            <el-tag :type="getStatusType(orgData.status)">
              {{ orgData.status || '-' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <el-divider content-position="left">组织描述</el-divider>
        <div v-html="orgData.description || '暂无描述'"></div>
      </el-tab-pane>
      
      <el-tab-pane label="人员信息" name="personnel">
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="6">
            <el-statistic title="总人数" :value="personnelStats.totalCount || 0" suffix="人">
              <template slot="prefix">
                <i class="el-icon-user" style="color: #409EFF"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="管理人员" :value="personnelStats.managerCount || 0" suffix="人">
              <template slot="prefix">
                <i class="el-icon-s-custom" style="color: #67C23A"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="技术人员" :value="personnelStats.techCount || 0" suffix="人">
              <template slot="prefix">
                <i class="el-icon-cpu" style="color: #E6A23C"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="支持人员" :value="personnelStats.supportCount || 0" suffix="人">
              <template slot="prefix">
                <i class="el-icon-service" style="color: #F56C6C"></i>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
        
        <el-table :data="personnelList" border>
          <el-table-column prop="employeeName" label="姓名" width="120"></el-table-column>
          <el-table-column prop="employeeCode" label="工号" width="120"></el-table-column>
          <el-table-column prop="position" label="职位" width="150"></el-table-column>
          <el-table-column prop="level" label="职级" width="100"></el-table-column>
          <el-table-column prop="department" label="部门" width="150"></el-table-column>
          <el-table-column prop="joinDate" label="入职时间" width="120"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag size="small" :type="getEmployeeStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="viewEmployeeDetail(scope.row)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="下级组织" name="subOrgs">
        <el-button type="primary" size="small" @click="addSubOrg" style="margin-bottom: 15px;">
          添加下级组织
        </el-button>
        
        <el-table :data="subOrgList" border>
          <el-table-column prop="orgName" label="组织名称" width="200"></el-table-column>
          <el-table-column prop="orgCode" label="组织编码" width="150"></el-table-column>
          <el-table-column prop="orgType" label="组织类型" width="120">
            <template slot-scope="scope">
              <el-tag size="small" :type="getOrgTypeTag(scope.row.orgType)">
                {{ getOrgTypeText(scope.row.orgType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="manager" label="负责人" width="120"></el-table-column>
          <el-table-column prop="employeeCount" label="人员数量" width="100"></el-table-column>
          <el-table-column prop="establishDate" label="成立时间" width="120"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag size="small" :type="getStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="viewSubOrgDetail(scope.row)">
                查看详情
              </el-button>
              <el-button type="text" size="small" @click="editSubOrg(scope.row)">
                编辑
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="变更历史" name="history">
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in changeHistory"
            :key="index"
            :timestamp="item.timestamp"
            :type="getChangeType(item.changeType)"
          >
            <el-card>
              <h4>{{ item.changeTitle }}</h4>
              <p>{{ item.changeDescription }}</p>
              <div style="margin-top: 10px;">
                <el-tag size="small" :type="getChangeTypeTag(item.changeType)">
                  {{ item.changeType }}
                </el-tag>
                <span style="margin-left: 10px; color: #909399;">
                  操作人：{{ item.operator }}
                </span>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="editOrganization">编辑</el-button>
      <el-button type="success" @click="exportData">导出数据</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'OrganizationDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    orgData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'basic'
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
    },
    personnelStats() {
      return this.orgData.personnelStats || {
        totalCount: 50,
        managerCount: 5,
        techCount: 35,
        supportCount: 10
      }
    },
    personnelList() {
      return this.orgData.personnelList || [
        {
          employeeName: '张三',
          employeeCode: 'E001',
          position: '技术总监',
          level: 'P7',
          department: '技术部',
          joinDate: '2020-01-15',
          status: '在职'
        }
      ]
    },
    subOrgList() {
      return this.orgData.subOrgList || [
        {
          orgName: '研发一部',
          orgCode: 'RD001',
          orgType: 'department',
          manager: '李四',
          employeeCount: 25,
          establishDate: '2020-01-01',
          status: '正常'
        }
      ]
    },
    changeHistory() {
      return this.orgData.changeHistory || [
        {
          timestamp: '2024-01-15 10:30:00',
          changeTitle: '组织架构调整',
          changeDescription: '新增研发二部，调整人员配置',
          changeType: '结构调整',
          operator: '张三'
        }
      ]
    }
  },
  methods: {
    getOrgTypeTag(type) {
      const typeMap = {
        'company': 'primary',
        'department': 'success',
        'team': 'warning',
        'group': 'info'
      }
      return typeMap[type] || 'info'
    },
    getOrgTypeText(type) {
      const textMap = {
        'company': '公司',
        'department': '部门',
        'team': '团队',
        'group': '小组'
      }
      return textMap[type] || type
    },
    getStatusType(status) {
      const statusMap = {
        '正常': 'success',
        '暂停': 'warning',
        '撤销': 'danger',
        '筹建': 'info'
      }
      return statusMap[status] || 'info'
    },
    getEmployeeStatusType(status) {
      const statusMap = {
        '在职': 'success',
        '离职': 'info',
        '休假': 'warning',
        '停职': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getChangeType(type) {
      const typeMap = {
        '结构调整': 'warning',
        '人员变动': 'info',
        '职能调整': 'primary',
        '状态变更': 'success'
      }
      return typeMap[type] || 'info'
    },
    getChangeTypeTag(type) {
      const typeMap = {
        '结构调整': 'warning',
        '人员变动': 'info',
        '职能调整': 'primary',
        '状态变更': 'success'
      }
      return typeMap[type] || 'info'
    },
    viewEmployeeDetail(row) {
      this.$message.info('查看员工详情：' + row.employeeName)
    },
    addSubOrg() {
      this.$emit('add-sub-org', this.orgData)
    },
    viewSubOrgDetail(row) {
      this.$emit('view-sub-org', row)
    },
    editSubOrg(row) {
      this.$emit('edit-sub-org', row)
    },
    editOrganization() {
      this.$emit('edit', this.orgData)
      this.handleClose()
    },
    exportData() {
      this.$message.success('组织数据导出中...')
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>
