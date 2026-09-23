<template>
  <div class="app-container">
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-user-solid" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.totalOrganizations || 0 }}</div>
              <div class="statistics-label">党组织总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-user" style="color: #409eff"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.totalMembers || 0 }}</div>
              <div class="statistics-label">党员总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-calendar" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.monthlyActivities || 0 }}</div>
              <div class="statistics-label">本月活动</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-star-on" style="color: #e6a23c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.avgScore || 0 }}</div>
              <div class="statistics-label">平均评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能标签页 -->
    <el-card>
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 党组织管理 -->
        <el-tab-pane label="党组织管理" name="organizations">
          <div class="tab-content">
            <!-- 查询表单 -->
            <el-form :model="organizationsQuery" ref="organizationsForm" :inline="true" label-width="100px" class="mb-20">
              <el-form-item label="组织名称">
                <el-input v-model="organizationsQuery.organizationName" placeholder="请输入组织名称" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="组织类型">
                <el-select v-model="organizationsQuery.organizationType" placeholder="请选择组织类型" clearable style="width: 150px">
                  <el-option label="党委" value="PARTY_COMMITTEE"></el-option>
                  <el-option label="党总支" value="PARTY_BRANCH"></el-option>
                  <el-option label="党支部" value="PARTY_CELL"></el-option>
                  <el-option label="党小组" value="PARTY_GROUP"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="组织状态">
                <el-select v-model="organizationsQuery.status" placeholder="请选择状态" clearable style="width: 120px">
                  <el-option label="正常" value="ACTIVE"></el-option>
                  <el-option label="停用" value="INACTIVE"></el-option>
                  <el-option label="筹建中" value="PREPARING"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getOrganizationsList" icon="el-icon-search">查询</el-button>
                <el-button @click="resetOrganizationsQuery" icon="el-icon-refresh">重置</el-button>
                <el-button type="success" @click="handleAddOrganization" icon="el-icon-plus">新建组织</el-button>
                <el-button type="info" @click="handleExportOrganizations" icon="el-icon-download">导出</el-button>
              </el-form-item>
            </el-form>

            <!-- 组织列表表格 -->
            <el-table v-loading="organizationsLoading" :data="organizationsList" stripe border style="width: 100%">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column prop="organizationName" label="组织名称" min-width="200" show-overflow-tooltip></el-table-column>
              <el-table-column prop="organizationType" label="组织类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getOrganizationTypeTag(scope.row.organizationType)">
                    {{ getOrganizationTypeText(scope.row.organizationType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="memberCount" label="党员人数" width="100" align="center"></el-table-column>
              <el-table-column prop="secretary" label="书记" width="120" align="center"></el-table-column>
              <el-table-column prop="establishDate" label="成立时间" width="120" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTag(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="lastActivityTime" label="最近活动" width="160" align="center"></el-table-column>
              <el-table-column label="操作" width="280" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewOrganization(scope.row)" icon="el-icon-view">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEditOrganization(scope.row)" icon="el-icon-edit">编辑</el-button>
                  <el-button size="mini" type="success" @click="handleManageMembers(scope.row)" icon="el-icon-user">成员管理</el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteOrganization(scope.row)" icon="el-icon-delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <pagination
              v-show="organizationsTotal > 0"
              :total="organizationsTotal"
              :page.sync="organizationsQuery.pageNum"
              :limit.sync="organizationsQuery.pageSize"
              @pagination="getOrganizationsList"
            />
          </div>
        </el-tab-pane>

        <!-- 党员管理 -->
        <el-tab-pane label="党员管理" name="members">
          <div class="tab-content">
            <!-- 查询表单 -->
            <el-form :model="membersQuery" ref="membersForm" :inline="true" label-width="100px" class="mb-20">
              <el-form-item label="党员姓名">
                <el-input v-model="membersQuery.memberName" placeholder="请输入党员姓名" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="所属组织">
                <el-select v-model="membersQuery.organizationId" placeholder="请选择组织" clearable style="width: 200px">
                  <el-option
                    v-for="org in organizationsList"
                    :key="org.id"
                    :label="org.organizationName"
                    :value="org.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="党员类型">
                <el-select v-model="membersQuery.memberType" placeholder="请选择类型" clearable style="width: 150px">
                  <el-option label="正式党员" value="FORMAL"></el-option>
                  <el-option label="预备党员" value="PROBATIONARY"></el-option>
                  <el-option label="入党积极分子" value="ACTIVIST"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getMembersList" icon="el-icon-search">查询</el-button>
                <el-button @click="resetMembersQuery" icon="el-icon-refresh">重置</el-button>
                <el-button type="success" @click="handleAddMember" icon="el-icon-plus">新增党员</el-button>
                <el-button type="info" @click="handleExportMembers" icon="el-icon-download">导出</el-button>
              </el-form-item>
            </el-form>

            <!-- 党员列表表格 -->
            <el-table v-loading="membersLoading" :data="membersList" stripe border style="width: 100%">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column prop="memberName" label="姓名" width="120" align="center"></el-table-column>
              <el-table-column prop="memberCode" label="党员编号" width="150" align="center"></el-table-column>
              <el-table-column prop="organizationName" label="所属组织" min-width="200" show-overflow-tooltip></el-table-column>
              <el-table-column prop="memberType" label="党员类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getMemberTypeTag(scope.row.memberType)">
                    {{ getMemberTypeText(scope.row.memberType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="position" label="职务" width="120" align="center"></el-table-column>
              <el-table-column prop="joinDate" label="入党时间" width="120" align="center"></el-table-column>
              <el-table-column prop="phone" label="联系电话" width="130" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTag(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="240" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewMember(scope.row)" icon="el-icon-view">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEditMember(scope.row)" icon="el-icon-edit">编辑</el-button>
                  <el-button size="mini" type="warning" @click="handleTransferMember(scope.row)" icon="el-icon-sort">转移</el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteMember(scope.row)" icon="el-icon-delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <pagination
              v-show="membersTotal > 0"
              :total="membersTotal"
              :page.sync="membersQuery.pageNum"
              :limit.sync="membersQuery.pageSize"
              @pagination="getMembersList"
            />
          </div>
        </el-tab-pane>

        <!-- 党建活动 -->
        <el-tab-pane label="党建活动" name="activities">
          <div class="tab-content">
            <!-- 查询表单 -->
            <el-form :model="activitiesQuery" ref="activitiesForm" :inline="true" label-width="100px" class="mb-20">
              <el-form-item label="活动名称">
                <el-input v-model="activitiesQuery.activityName" placeholder="请输入活动名称" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="活动类型">
                <el-select v-model="activitiesQuery.activityType" placeholder="请选择活动类型" clearable style="width: 150px">
                  <el-option label="学习教育" value="STUDY"></el-option>
                  <el-option label="组织生活" value="ORGANIZATION"></el-option>
                  <el-option label="志愿服务" value="VOLUNTEER"></el-option>
                  <el-option label="主题党日" value="THEME_DAY"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="活动状态">
                <el-select v-model="activitiesQuery.status" placeholder="请选择状态" clearable style="width: 120px">
                  <el-option label="计划中" value="PLANNED"></el-option>
                  <el-option label="进行中" value="ONGOING"></el-option>
                  <el-option label="已完成" value="COMPLETED"></el-option>
                  <el-option label="已取消" value="CANCELLED"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getActivitiesList" icon="el-icon-search">查询</el-button>
                <el-button @click="resetActivitiesQuery" icon="el-icon-refresh">重置</el-button>
                <el-button type="success" @click="handleAddActivity" icon="el-icon-plus">新建活动</el-button>
                <el-button type="info" @click="handleExportActivities" icon="el-icon-download">导出</el-button>
              </el-form-item>
            </el-form>

            <!-- 活动列表表格 -->
            <el-table v-loading="activitiesLoading" :data="activitiesList" stripe border style="width: 100%">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column prop="activityName" label="活动名称" min-width="200" show-overflow-tooltip></el-table-column>
              <el-table-column prop="activityType" label="活动类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getActivityTypeTag(scope.row.activityType)">
                    {{ getActivityTypeText(scope.row.activityType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="organizationName" label="主办组织" min-width="180" show-overflow-tooltip></el-table-column>
              <el-table-column prop="activityDate" label="活动时间" width="120" align="center"></el-table-column>
              <el-table-column prop="participantCount" label="参与人数" width="100" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getActivityStatusTag(scope.row.status)">
                    {{ getActivityStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="organizer" label="组织者" width="120" align="center"></el-table-column>
              <el-table-column label="操作" width="280" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewActivity(scope.row)" icon="el-icon-view">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEditActivity(scope.row)" icon="el-icon-edit">编辑</el-button>
                  <el-button size="mini" type="success" @click="handleManageParticipants(scope.row)" icon="el-icon-user">参与管理</el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteActivity(scope.row)" icon="el-icon-delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <pagination
              v-show="activitiesTotal > 0"
              :total="activitiesTotal"
              :page.sync="activitiesQuery.pageNum"
              :limit.sync="activitiesQuery.pageSize"
              @pagination="getActivitiesList"
            />
          </div>
        </el-tab-pane>

        <!-- 党建评估 -->
        <el-tab-pane label="党建评估" name="evaluations">
          <div class="tab-content">
            <!-- 评估统计 -->
            <el-row :gutter="20" class="mb-20">
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>评估得分分布</span>
                  </div>
                  <div ref="scoreDistributionChart" style="height: 300px;"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>评估趋势</span>
                  </div>
                  <div ref="evaluationTrendChart" style="height: 300px;"></div>
                </el-card>
              </el-col>
            </el-row>

            <!-- 评估列表 -->
            <el-card>
              <div slot="header" class="card-header">
                <span>评估记录</span>
                <el-button type="primary" @click="handleCreateEvaluation">创建评估</el-button>
              </div>
              
              <el-table :data="evaluationsList" stripe border style="width: 100%;">
                <el-table-column prop="evaluationName" label="评估名称" min-width="200" show-overflow-tooltip></el-table-column>
                <el-table-column prop="evaluationType" label="评估类型" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getEvaluationTypeTag(scope.row.evaluationType)">
                      {{ getEvaluationTypeText(scope.row.evaluationType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="organizationName" label="评估对象" min-width="180" show-overflow-tooltip></el-table-column>
                <el-table-column prop="evaluationPeriod" label="评估期间" width="120" align="center"></el-table-column>
                <el-table-column prop="totalScore" label="总分" width="80" align="center"></el-table-column>
                <el-table-column prop="evaluationDate" label="评估时间" width="120" align="center"></el-table-column>
                <el-table-column prop="evaluator" label="评估人" width="120" align="center"></el-table-column>
                <el-table-column label="操作" width="200" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="handleViewEvaluation(scope.row)" icon="el-icon-view">查看</el-button>
                    <el-button size="mini" type="primary" @click="handleEditEvaluation(scope.row)" icon="el-icon-edit">编辑</el-button>
                    <el-button size="mini" type="success" @click="handleExportEvaluation(scope.row)" icon="el-icon-download">导出</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 对话框组件 -->
    <PartyOrganizationDialog
      :visible.sync="organizationDialogVisible"
      :organization-data="currentOrganization"
      :dialog-type="dialogType"
      @refresh="getOrganizationsList"
    />

    <PartyMemberDialog
      :visible.sync="memberDialogVisible"
      :member-data="currentMember"
      :dialog-type="dialogType"
      @refresh="getMembersList"
    />

    <PartyActivityDialog
      :visible.sync="activityDialogVisible"
      :activity-data="currentActivity"
      :dialog-type="dialogType"
      @refresh="getActivitiesList"
    />

    <PartyEvaluationDialog
      :visible.sync="evaluationDialogVisible"
      :evaluation-data="currentEvaluation"
      :dialog-type="dialogType"
      @refresh="getEvaluationsList"
    />
  </div>
</template>

<script>
import { getPartyBuildingStatistics, getPartyOrganizationsList, getPartyMembersList, getPartyActivitiesList, getPartyEvaluationsList } from '@/api/stateAssets/partyBuilding'
import Pagination from '@/components/Pagination'
import PartyOrganizationDialog from './components/PartyOrganizationDialog'
import PartyMemberDialog from './components/PartyMemberDialog'
import PartyActivityDialog from './components/PartyActivityDialog'
import PartyEvaluationDialog from './components/PartyEvaluationDialog'
import * as echarts from 'echarts'

export default {
  name: 'PartyBuilding',
  components: {
    Pagination,
    PartyOrganizationDialog,
    PartyMemberDialog,
    PartyActivityDialog,
    PartyEvaluationDialog
  },
  data() {
    return {
      activeTab: 'organizations',
      statistics: {},
      
      // 党组织相关
      organizationsLoading: false,
      organizationsList: [],
      organizationsTotal: 0,
      organizationsQuery: {
        pageNum: 1,
        pageSize: 10,
        organizationName: '',
        organizationType: '',
        status: ''
      },
      
      // 党员相关
      membersLoading: false,
      membersList: [],
      membersTotal: 0,
      membersQuery: {
        pageNum: 1,
        pageSize: 10,
        memberName: '',
        organizationId: '',
        memberType: ''
      },
      
      // 党建活动相关
      activitiesLoading: false,
      activitiesList: [],
      activitiesTotal: 0,
      activitiesQuery: {
        pageNum: 1,
        pageSize: 10,
        activityName: '',
        activityType: '',
        status: ''
      },
      
      // 党建评估相关
      evaluationsList: [],
      
      // 对话框相关
      organizationDialogVisible: false,
      memberDialogVisible: false,
      activityDialogVisible: false,
      evaluationDialogVisible: false,
      dialogType: 'add',
      currentOrganization: {},
      currentMember: {},
      currentActivity: {},
      currentEvaluation: {}
    }
  },
  created() {
    this.getStatistics()
    this.getOrganizationsList()
    this.initCharts()
  },
  methods: {
    // 获取统计数据
    getStatistics() {
      getPartyBuildingStatistics().then(response => {
        if (response.code === 1) {
          this.statistics = response.data || {}
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
      })
    },

    // 党组织相关方法
    getOrganizationsList() {
      this.organizationsLoading = true
      getPartyOrganizationsList(this.organizationsQuery).then(response => {
        if (response.code === 1) {
          this.organizationsList = response.data.list || []
          this.organizationsTotal = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取党组织列表失败')
        }
        this.organizationsLoading = false
      }).catch(error => {
        console.error('获取党组织列表异常:', error)
        this.$message.warning('获取数据暂未开放')
        this.organizationsLoading = false
      })
    },

    resetOrganizationsQuery() {
      this.$refs.organizationsForm.resetFields()
      this.organizationsQuery = {
        pageNum: 1,
        pageSize: 10,
        organizationName: '',
        organizationType: '',
        status: ''
      }
      this.getOrganizationsList()
    },

    handleAddOrganization() {
      this.currentOrganization = {}
      this.dialogType = 'add'
      this.organizationDialogVisible = true
    },

    handleViewOrganization(row) {
      this.currentOrganization = { ...row }
      this.dialogType = 'view'
      this.organizationDialogVisible = true
    },

    handleEditOrganization(row) {
      this.currentOrganization = { ...row }
      this.dialogType = 'edit'
      this.organizationDialogVisible = true
    },

    handleManageMembers(row) {
      this.$message.success(`正在管理${row.organizationName}的成员`)
    },

    handleDeleteOrganization(row) {
      this.$confirm(`确定要删除组织"${row.organizationName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getOrganizationsList()
      }).catch(() => {
          // 用户取消操作
        })
      },

    handleExportOrganizations() {
      this.$message.success('正在导出党组织数据...')
    },

    // 党员相关方法
    getMembersList() {
      this.membersLoading = true
      getPartyMembersList(this.membersQuery).then(response => {
        if (response.code === 1) {
          this.membersList = response.data.list || []
          this.membersTotal = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取党员列表失败')
        }
        this.membersLoading = false
      }).catch(error => {
        console.error('获取党员列表异常:', error)
        this.$message.warning('获取数据暂未开放')
        this.membersLoading = false
      })
    },

    resetMembersQuery() {
      this.$refs.membersForm.resetFields()
      this.membersQuery = {
        pageNum: 1,
        pageSize: 10,
        memberName: '',
        organizationId: '',
        memberType: ''
      }
      this.getMembersList()
    },

    handleAddMember() {
      this.currentMember = {}
      this.dialogType = 'add'
      this.memberDialogVisible = true
    },

    handleViewMember(row) {
      this.currentMember = { ...row }
      this.dialogType = 'view'
      this.memberDialogVisible = true
    },

    handleEditMember(row) {
      this.currentMember = { ...row }
      this.dialogType = 'edit'
      this.memberDialogVisible = true
    },

    handleTransferMember(row) {
      this.$message.success(`正在转移党员：${row.memberName}`)
    },

    handleDeleteMember(row) {
      this.$confirm(`确定要删除党员"${row.memberName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getMembersList()
      }).catch(() => {
          // 用户取消操作
        })
      },

    handleExportMembers() {
      this.$message.success('正在导出党员数据...')
    },

    // 党建活动相关方法
    getActivitiesList() {
      this.activitiesLoading = true
      getPartyActivitiesList(this.activitiesQuery).then(response => {
        if (response.code === 1) {
          this.activitiesList = response.data.list || []
          this.activitiesTotal = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取党建活动列表失败')
        }
        this.activitiesLoading = false
      }).catch(error => {
        console.error('获取党建活动列表异常:', error)
        this.$message.warning('获取数据暂未开放')
        this.activitiesLoading = false
      })
    },

    resetActivitiesQuery() {
      this.$refs.activitiesForm.resetFields()
      this.activitiesQuery = {
        pageNum: 1,
        pageSize: 10,
        activityName: '',
        activityType: '',
        status: ''
      }
      this.getActivitiesList()
    },

    handleAddActivity() {
      this.currentActivity = {}
      this.dialogType = 'add'
      this.activityDialogVisible = true
    },

    handleViewActivity(row) {
      this.currentActivity = { ...row }
      this.dialogType = 'view'
      this.activityDialogVisible = true
    },

    handleEditActivity(row) {
      this.currentActivity = { ...row }
      this.dialogType = 'edit'
      this.activityDialogVisible = true
    },

    handleManageParticipants(row) {
      this.$message.success(`正在管理活动"${row.activityName}"的参与者`)
    },

    handleDeleteActivity(row) {
      this.$confirm(`确定要删除活动"${row.activityName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getActivitiesList()
      }).catch(() => {
          // 用户取消操作
        })
      },

    handleExportActivities() {
      this.$message.success('正在导出党建活动数据...')
    },

    // 党建评估相关方法
    getEvaluationsList() {
      getPartyEvaluationsList().then(response => {
        if (response.code === 1) {
          this.evaluationsList = response.data.list || []
        }
      }).catch(error => {
        console.error('获取党建评估列表失败:', error)
      })
    },

    handleCreateEvaluation() {
      this.currentEvaluation = {}
      this.dialogType = 'add'
      this.evaluationDialogVisible = true
    },

    handleViewEvaluation(row) {
      this.currentEvaluation = { ...row }
      this.dialogType = 'view'
      this.evaluationDialogVisible = true
    },

    handleEditEvaluation(row) {
      this.currentEvaluation = { ...row }
      this.dialogType = 'edit'
      this.evaluationDialogVisible = true
    },

    handleExportEvaluation(row) {
      this.$message.success(`正在导出评估报告：${row.evaluationName}`)
    },

    // 图表初始化
    initCharts() {
      this.$nextTick(() => {
        this.initScoreDistributionChart()
        this.initEvaluationTrendChart()
      })
    },

    initScoreDistributionChart() {
      if (!this.$refs.scoreDistributionChart) return
      const chart = echarts.init(this.$refs.scoreDistributionChart)
      const option = {
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          radius: '70%',
          data: [
            { value: 15, name: '优秀(90-100)' },
            { value: 25, name: '良好(80-89)' },
            { value: 35, name: '合格(70-79)' },
            { value: 25, name: '待改进(<70)' }
          ]
        }]
      }
      chart.setOption(option)
    },

    initEvaluationTrendChart() {
      if (!this.$refs.evaluationTrendChart) return
      const chart = echarts.init(this.$refs.evaluationTrendChart)
      const option = {
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: { type: 'value' },
        series: [{
          name: '平均得分',
          type: 'line',
          data: [82, 85, 88, 86, 89, 91],
          smooth: true
        }]
      }
      chart.setOption(option)
    },

    // 工具方法
    getOrganizationTypeTag(type) {
      const tagMap = {
        'PARTY_COMMITTEE': 'danger',
        'PARTY_BRANCH': 'primary',
        'PARTY_CELL': 'success',
        'PARTY_GROUP': 'warning'
      }
      return tagMap[type] || 'info'
    },

    getOrganizationTypeText(type) {
      const textMap = {
        'PARTY_COMMITTEE': '党委',
        'PARTY_BRANCH': '党总支',
        'PARTY_CELL': '党支部',
        'PARTY_GROUP': '党小组'
      }
      return textMap[type] || type
    },

    getStatusTag(status) {
      const tagMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'PREPARING': 'warning'
      }
      return tagMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        'ACTIVE': '正常',
        'INACTIVE': '停用',
        'PREPARING': '筹建中'
      }
      return textMap[status] || status
    },

    getMemberTypeTag(type) {
      const tagMap = {
        'FORMAL': 'success',
        'PROBATIONARY': 'warning',
        'ACTIVIST': 'primary'
      }
      return tagMap[type] || 'info'
    },

    getMemberTypeText(type) {
      const textMap = {
        'FORMAL': '正式党员',
        'PROBATIONARY': '预备党员',
        'ACTIVIST': '入党积极分子'
      }
      return textMap[type] || type
    },

    getActivityTypeTag(type) {
      const tagMap = {
        'STUDY': 'primary',
        'ORGANIZATION': 'success',
        'VOLUNTEER': 'warning',
        'THEME_DAY': 'danger'
      }
      return tagMap[type] || 'info'
    },

    getActivityTypeText(type) {
      const textMap = {
        'STUDY': '学习教育',
        'ORGANIZATION': '组织生活',
        'VOLUNTEER': '志愿服务',
        'THEME_DAY': '主题党日'
      }
      return textMap[type] || type
    },

    getActivityStatusTag(status) {
      const tagMap = {
        'PLANNED': 'info',
        'ONGOING': 'warning',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return tagMap[status] || 'info'
    },

    getActivityStatusText(status) {
      const textMap = {
        'PLANNED': '计划中',
        'ONGOING': '进行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },

    getEvaluationTypeTag(type) {
      const tagMap = {
        'ANNUAL': 'primary',
        'QUARTERLY': 'success',
        'SPECIAL': 'warning'
      }
      return tagMap[type] || 'info'
    },

    getEvaluationTypeText(type) {
      const textMap = {
        'ANNUAL': '年度评估',
        'QUARTERLY': '季度评估',
        'SPECIAL': '专项评估'
      }
      return textMap[type] || type
    }
  }
}
</script>

<style scoped>
.statistics-card {
  margin-bottom: 20px;
}

.statistics-content {
  display: flex;
  align-items: center;
}

.statistics-icon {
  font-size: 40px;
  margin-right: 20px;
}

.statistics-info {
  flex: 1;
}

.statistics-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.statistics-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.tab-content {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
