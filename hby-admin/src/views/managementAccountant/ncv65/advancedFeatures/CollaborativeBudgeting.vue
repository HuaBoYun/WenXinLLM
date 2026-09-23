<template>
  <div class="collaborative-budgeting">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>协同预算编制</h2>
      <p>多部门协同预算编制平台，支持实时协作、版本控制和审批流程</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateProject">创建项目</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-share" @click="inviteDialogVisible = true">邀请协作</el-button>
            <el-button type="info" icon="el-icon-view" @click="progressDialogVisible = true">协作进度</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-message" @click="messageDialogVisible = true">消息中心</el-button>
            <el-button icon="el-icon-setting" @click="settingsDialogVisible = true">协作设置</el-button>
            <el-button icon="el-icon-help" @click="helpDialogVisible = true">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 协作统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ collaborationStats.totalProjects }}</div>
            <div class="stat-label">协作项目</div>
            <div class="stat-description">总协作项目数量</div>
          </div>
          <div class="stat-icon"><i class="el-icon-s-cooperation"></i></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card participants-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ collaborationStats.totalParticipants }}</div>
            <div class="stat-label">参与人数</div>
            <div class="stat-description">协作参与总人数</div>
          </div>
          <div class="stat-icon"><i class="el-icon-user"></i></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card completion-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ collaborationStats.completionRate }}%</div>
            <div class="stat-label">完成率</div>
            <div class="stat-description">项目完成率</div>
          </div>
          <div class="stat-icon"><i class="el-icon-success"></i></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card efficiency-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ collaborationStats.efficiency }}%</div>
            <div class="stat-label">协作效率</div>
            <div class="stat-description">平均进度指数</div>
          </div>
          <div class="stat-icon"><i class="el-icon-cpu"></i></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 协作类型选择 -->
    <el-card class="collaboration-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>协作类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="loadTypeStats">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="item in collaborationTypes" :key="item.type">
          <el-card
            class="collaboration-type-item"
            shadow="hover"
            @click.native="handleSelectCollaborationType(item)"
            :class="{ 'selected': selectedCollaborationType === item.type }"
          >
            <div class="collaboration-type-icon"><i :class="item.icon"></i></div>
            <div class="collaboration-type-title">{{ item.name }}</div>
            <div class="collaboration-type-stats">
              <span class="project-count">{{ item.projectCount }} 个项目</span>
              <span class="participant-count">{{ item.participantCount }} 人参与</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 协作项目列表 -->
    <el-card class="collaboration-projects-card" shadow="never">
      <div slot="header" class="card-header">
        <span>协作项目</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索项目"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getCollaborationProjectList"
            clearable
            @clear="getCollaborationProjectList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button v-if="selectedCollaborationType" size="mini" @click="clearTypeFilter">清除筛选</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="getCollaborationProjectList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="collaborationProjectList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="projectName" label="项目名称" min-width="180" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click.stop="handleView(scope.row)">{{ scope.row.projectName }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="collaborationType" label="协作类型" width="130" align="center">
          <template slot-scope="scope">
            <el-tag :type="getCollaborationTypeColor(scope.row.collaborationType)" size="mini">
              {{ getCollaborationTypeText(scope.row.collaborationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="participantCount" label="参与人数" width="90" align="center">
          <template slot-scope="scope">{{ scope.row.participantCount || 0 }} 人</template>
        </el-table-column>
        <el-table-column prop="progress" label="项目进度" width="150" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.progress || 0"
              :color="getProgressColor(scope.row.progress || 0)"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="progress-text">{{ scope.row.progress || 0 }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="projectStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.projectStatus)" size="mini">
              {{ getStatusText(scope.row.projectStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-view" @click.stop="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="mini" icon="el-icon-edit" @click.stop="handleEdit(scope.row)">编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)" @click.stop.native>
              <el-button type="text" size="mini">更多<i class="el-icon-arrow-down el-icon--right"></i></el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="comment">添加评论</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
                <el-dropdown-item command="archive">归档</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="margin-top: 15px; text-align: right;">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :current-page="pageNo"
          :page-sizes="[10, 15, 20, 50]"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 项目详情抽屉 -->
    <el-drawer
      title="协作项目详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentProject">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="项目基本信息" :column="2" border>
              <el-descriptions-item label="项目名称">{{ currentProject.projectName }}</el-descriptions-item>
              <el-descriptions-item label="协作类型">{{ getCollaborationTypeText(currentProject.collaborationType) }}</el-descriptions-item>
              <el-descriptions-item label="参与人数">{{ currentProject.participantCount }} 人</el-descriptions-item>
              <el-descriptions-item label="项目进度">{{ currentProject.progress }}%</el-descriptions-item>
              <el-descriptions-item label="最近活动">{{ currentProject.lastActivity }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentProject.status)" size="mini">
                  {{ getStatusText(currentProject.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentProject.creator }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentProject.createTime }}</el-descriptions-item>
              <el-descriptions-item label="项目描述" :span="2">{{ currentProject.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="参与成员" name="participants">
            <el-table :data="projectParticipants" border size="mini">
              <el-table-column prop="userName" label="姓名" width="120" />
              <el-table-column prop="department" label="部门" width="150" />
              <el-table-column prop="role" label="角色" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getRoleColor(scope.row.role)" size="mini">
                    {{ getRoleText(scope.row.role) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="contribution" label="贡献度" width="100" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.contribution }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="lastActive" label="最后活跃" width="150" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getParticipantStatusColor(scope.row.status)" size="mini">
                    {{ getParticipantStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="协作记录" name="activities">
            <el-timeline>
              <el-timeline-item
                v-for="activity in collaborationActivities"
                :key="activity.activityId"
                :timestamp="activity.createTime"
                :type="getActivityType(activity.activityType)"
              >
                <el-card>
                  <p>{{ activity.content }}</p>
                  <p class="activity-user">{{ activity.userName }}</p>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </el-tab-pane>
          <el-tab-pane label="评论讨论" name="comments">
            <div class="comment-section">
              <div class="comment-input">
                <el-input
                  v-model="newComment"
                  type="textarea"
                  :rows="3"
                  placeholder="添加评论..."
                />
                <el-button type="primary" @click="handleAddComment" style="margin-top: 10px;">
                  发表评论
                </el-button>
              </div>
              <div class="comment-list">
                <div v-for="comment in projectComments" :key="comment.commentId" class="comment-item">
                  <div class="comment-header">
                    <span class="comment-user">{{ comment.userName }}</span>
                    <span class="comment-time">{{ comment.createTime }}</span>
                  </div>
                  <div class="comment-content">{{ comment.content }}</div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑项目对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="projectForm"
        :model="projectForm"
        :rules="projectRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="projectForm.projectName" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="协作类型" prop="collaborationType">
              <el-select v-model="projectForm.collaborationType" placeholder="请选择协作类型" style="width: 100%">
                <el-option value="DEPARTMENT_COLLABORATION" label="部门协作" />
                <el-option value="CROSS_DEPARTMENT" label="跨部门协作" />
                <el-option value="PROJECT_TEAM" label="项目团队" />
                <el-option value="EXTERNAL_COLLABORATION" label="外部协作" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="项目描述" prop="description">
          <el-input
            v-model="projectForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入项目描述"
          />
        </el-form-item>
        <el-form-item label="参与部门" prop="departments">
          <el-select
            v-model="projectForm.departments"
            multiple
            placeholder="请选择参与部门"
            style="width: 100%"
          >
            <el-option value="FINANCE" label="财务部" />
            <el-option value="SALES" label="销售部" />
            <el-option value="MARKETING" label="市场部" />
            <el-option value="OPERATIONS" label="运营部" />
            <el-option value="HR" label="人力资源部" />
            <el-option value="IT" label="信息技术部" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>
    <!-- 邀请协作弹窗 -->
    <el-dialog title="邀请协作" :visible.sync="inviteDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form label-width="80px" size="small">
        <el-form-item label="项目">
          <el-select v-model="inviteForm.projectId" placeholder="请选择项目" style="width:100%">
            <el-option v-for="p in collaborationProjectList" :key="p.projectId" :label="p.projectName" :value="p.projectId" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱/账号">
          <el-input v-model="inviteForm.email" placeholder="请输入被邀请人邮箱或账号" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="inviteForm.role" placeholder="请选择角色" style="width:100%">
            <el-option value="MEMBER" label="成员" />
            <el-option value="REVIEWER" label="审核人" />
            <el-option value="OBSERVER" label="观察者" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="inviteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleInviteSubmit">发送邀请</el-button>
      </div>
    </el-dialog>

    <!-- 协作进度弹窗 -->
    <el-dialog title="协作进度" :visible.sync="progressDialogVisible" width="700px">
      <el-table :data="collaborationProjectList" border size="mini" max-height="400">
        <el-table-column prop="projectName" label="项目名称" show-overflow-tooltip />
        <el-table-column prop="collaborationType" label="类型" width="130" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getCollaborationTypeColor(scope.row.collaborationType)">
              {{ getCollaborationTypeText(scope.row.collaborationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="进度" width="180" align="center">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress || 0" :stroke-width="8" />
          </template>
        </el-table-column>
        <el-table-column prop="projectStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getStatusColor(scope.row.projectStatus)">{{ getStatusText(scope.row.projectStatus) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer"><el-button @click="progressDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 消息中心弹窗 -->
    <el-dialog title="消息中心" :visible.sync="messageDialogVisible" width="600px">
      <el-empty description="暂无消息" v-if="!collaborationActivities.length" />
      <el-timeline v-else>
        <el-timeline-item
          v-for="(act, idx) in collaborationActivities"
          :key="idx"
          :timestamp="act.createTime || act.timestamp"
          :type="getActivityType(act.activityType || act.type)"
        >
          <b>{{ act.userName || act.user }}</b>：{{ act.content || act.description }}
        </el-timeline-item>
      </el-timeline>
      <div slot="footer"><el-button @click="messageDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 协作设置弹窗 -->
    <el-dialog title="协作设置" :visible.sync="settingsDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form label-width="120px" size="small">
        <el-form-item label="通知方式">
          <el-checkbox-group v-model="settingsForm.notifyMethods">
            <el-checkbox label="EMAIL">邮件通知</el-checkbox>
            <el-checkbox label="SYSTEM">系统通知</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="自动归档天数">
          <el-input-number v-model="settingsForm.archiveDays" :min="1" :max="365" />
          <span style="margin-left:8px;color:#999">天后自动归档已完成项目</span>
        </el-form-item>
        <el-form-item label="默认协作类型">
          <el-select v-model="settingsForm.defaultType" style="width:100%">
            <el-option v-for="t in collaborationTypes" :key="t.type" :label="t.name" :value="t.type" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false; $message.success('设置已保存')">保存</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="帮助中心" :visible.sync="helpDialogVisible" width="600px">
      <el-collapse>
        <el-collapse-item title="如何创建协作项目？" name="1">
          <p>点击工具栏「创建项目」按钮，填写项目名称、协作类型、参与部门等信息后提交即可。</p>
        </el-collapse-item>
        <el-collapse-item title="如何邀请成员参与协作？" name="2">
          <p>点击「邀请协作」按钮，选择目标项目，输入被邀请人账号并指定角色后发送邀请。</p>
        </el-collapse-item>
        <el-collapse-item title="协作类型有哪些区别？" name="3">
          <p>系统支持多种协作类型，包括预算编制、预算调整、预算规划等，可根据实际业务场景选择。</p>
        </el-collapse-item>
        <el-collapse-item title="如何归档已完成的项目？" name="4">
          <p>在项目列表中点击「更多」→「归档」，或在设置中配置自动归档规则。</p>
        </el-collapse-item>
      </el-collapse>
      <div slot="footer"><el-button @click="helpDialogVisible = false">关闭</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'CollaborativeBudgeting',
  data() {
    return {
      collaborationStats: { totalProjects: 0, totalParticipants: 0, completionRate: 0, efficiency: 0 },
      collaborationTypes: [],
      selectedCollaborationType: null,
      collaborationProjectList: [],
      loading: false,
      searchKeyword: '',
      pageNo: 1,
      pageSize: 10,
      total: 0,
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentProject: null,
      projectParticipants: [],
      collaborationActivities: [],
      projectComments: [],
      newComment: '',
      // 新建/编辑对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      projectForm: { projectName: '', collaborationType: '', description: '', departments: [] },
      projectRules: {
        projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
        collaborationType: [{ required: true, message: '请选择协作类型', trigger: 'change' }]
      },
      // 5个弹窗
      inviteDialogVisible: false,
      progressDialogVisible: false,
      messageDialogVisible: false,
      settingsDialogVisible: false,
      helpDialogVisible: false,
      inviteForm: { projectId: '', email: '', role: 'MEMBER' },
      settingsForm: { notifyMethods: ['SYSTEM'], archiveDays: 90, defaultType: '' }
    }
  },

  created() {
    this.getCollaborationProjectList()
    this.getCollaborationStats()
    this.loadTypeStats()
  },

  methods: {
    async getCollaborationProjectList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword, pageNum: this.pageNo, pageSize: this.pageSize }
        if (this.selectedCollaborationType) params.collaborationType = this.selectedCollaborationType
        const response = await advancedFeaturesApi.getCollaborativeBudgetingList(params)
        if (response && response.code === 1 && response.data) {
          this.collaborationProjectList = response.data.list || response.data || []
          this.total = response.data.total || this.collaborationProjectList.length
        }
      } catch (error) {
        this.$message.error('获取项目列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async getCollaborationStats() {
      try {
        const response = await advancedFeaturesApi.getCollaborativeBudgetingStats()
        if (response && response.code === 1 && response.data) {
          this.collaborationStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    async loadTypeStats() {
      try {
        const response = await advancedFeaturesApi.getCollaborationTypeStats()
        if (response && response.code === 1 && response.data) {
          const iconMap = {
            'BUDGET_PREPARATION': 'el-icon-document',
            'BUDGET_ADJUSTMENT': 'el-icon-edit',
            'BUDGET_PLANNING': 'el-icon-s-order',
            'COST_REDUCTION': 'el-icon-minus',
            'BUDGET_REVIEW': 'el-icon-view',
            'BUDGET_INTEGRATION': 'el-icon-s-cooperation',
            'BUDGET_COORDINATION': 'el-icon-connection',
            'DEPARTMENT_COLLABORATION': 'el-icon-office-building',
            'CROSS_DEPARTMENT': 'el-icon-s-cooperation',
            'PROJECT_TEAM': 'el-icon-user-solid',
            'EXTERNAL_COLLABORATION': 'el-icon-link'
          }
          const nameMap = {
            'BUDGET_PREPARATION': '预算编制',
            'BUDGET_ADJUSTMENT': '预算调整',
            'BUDGET_PLANNING': '预算规划',
            'COST_REDUCTION': '降本增效',
            'BUDGET_REVIEW': '预算审核',
            'BUDGET_INTEGRATION': '预算整合',
            'BUDGET_COORDINATION': '预算协调',
            'DEPARTMENT_COLLABORATION': '部门协作',
            'CROSS_DEPARTMENT': '跨部门协作',
            'PROJECT_TEAM': '项目团队',
            'EXTERNAL_COLLABORATION': '外部协作'
          }
          this.collaborationTypes = response.data.map(item => {
            // 达梦返回大写key，兼容小写
            const type = item.TYPE || item.type || item.COLLABORATION_TYPE || ''
            return {
              type,
              name: nameMap[type] || type,
              icon: iconMap[type] || 'el-icon-s-cooperation',
              projectCount: item.PROJECTCOUNT != null ? item.PROJECTCOUNT : (item.projectCount || 0),
              participantCount: item.PARTICIPANTCOUNT != null ? item.PARTICIPANTCOUNT : (item.participantCount || 0)
            }
          })
        }
      } catch (error) {
        console.error('获取类型统计失败：', error)
      }
    },

    handleSizeChange(val) { this.pageSize = val; this.pageNo = 1; this.getCollaborationProjectList() },
    handlePageChange(val) { this.pageNo = val; this.getCollaborationProjectList() },

    handleSelectCollaborationType(item) {
      this.selectedCollaborationType = this.selectedCollaborationType === item.type ? null : item.type
      this.pageNo = 1
      this.getCollaborationProjectList()
    },
    clearTypeFilter() { this.selectedCollaborationType = null; this.pageNo = 1; this.getCollaborationProjectList() },

    handleCreateProject() { this.dialogTitle = '创建协作项目'; this.dialogVisible = true; this.resetForm() },
    handleEdit(row) { this.dialogTitle = '编辑协作项目'; this.dialogVisible = true; this.projectForm = { ...row, departments: row.departments ? row.departments.split(',') : [] } },

    async handleView(row) {
      this.currentProject = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      const id = row.projectId
      await Promise.all([this.getProjectParticipants(id), this.getCollaborationActivities(id), this.getProjectComments(id)])
    },

    async getProjectParticipants(projectId) {
      try {
        const res = await advancedFeaturesApi.getCollaborationParticipants(projectId)
        this.projectParticipants = (res && res.code === 1) ? (res.data || []) : []
      } catch (e) { this.projectParticipants = [] }
    },

    async getCollaborationActivities(projectId) {
      try {
        const res = await advancedFeaturesApi.getCollaborationActivities(projectId)
        this.collaborationActivities = (res && res.code === 1) ? (res.data || []) : []
      } catch (e) { this.collaborationActivities = [] }
    },

    async getProjectComments(projectId) {
      try {
        const res = await advancedFeaturesApi.getCollaborationComments(projectId)
        this.projectComments = (res && res.code === 1) ? (res.data || []) : []
      } catch (e) { this.projectComments = [] }
    },

    async handleAddComment() {
      if (!this.newComment.trim()) { this.$message.warning('请输入评论内容'); return }
      try {
        await advancedFeaturesApi.addCollaborationComment(this.currentProject.projectId, { content: this.newComment })
        this.$message.success('评论添加成功')
        this.newComment = ''
        await this.getProjectComments(this.currentProject.projectId)
      } catch (e) { this.$message.error('添加评论失败：' + e.message) }
    },

    handleMoreAction(command, row) {
      const actions = {
        comment: () => { this.handleView(row); this.$nextTick(() => { this.detailActiveTab = 'comments' }) },
        export: () => this.handleExportProject(row),
        archive: () => this.handleArchiveProject(row),
        delete: () => this.handleDeleteProject(row)
      }
      actions[command] && actions[command]()
    },

    async handleExportProject(row) {
      try { await advancedFeaturesApi.exportCollaborativeProject(row.projectId); this.$message.success('导出成功') }
      catch (e) { this.$message.error('导出失败：' + e.message) }
    },

    handleArchiveProject(row) {
      this.$confirm('确定归档该协作项目吗？', '提示', { type: 'warning' }).then(async () => {
        try { await advancedFeaturesApi.archiveCollaborativeProject(row.projectId); this.$message.success('归档成功'); this.getCollaborationProjectList() }
        catch (e) { this.$message.error('归档失败：' + e.message) }
      }).catch(() => {})
    },

    handleDeleteProject(row) {
      this.$confirm('确定删除该协作项目吗？', '提示', { type: 'warning' }).then(async () => {
        try { await advancedFeaturesApi.deleteCollaborativeProject(row.projectId); this.$message.success('删除成功'); this.getCollaborationProjectList() }
        catch (e) { this.$message.error('删除失败：' + e.message) }
      }).catch(() => {})
    },

    async handleSubmitForm() {
      this.$refs.projectForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const form = { ...this.projectForm, departments: Array.isArray(this.projectForm.departments) ? this.projectForm.departments.join(',') : this.projectForm.departments }
          if (form.projectId) { await advancedFeaturesApi.updateCollaborativeProject(form); this.$message.success('更新成功') }
          else { await advancedFeaturesApi.createCollaborativeProject(form); this.$message.success('创建成功') }
          this.dialogVisible = false
          this.getCollaborationProjectList()
          this.getCollaborationStats()
        } catch (e) { this.$message.error('操作失败：' + e.message) }
        finally { this.submitLoading = false }
      })
    },

    resetForm() {
      this.projectForm = { projectName: '', collaborationType: '', description: '', departments: [] }
      this.$nextTick(() => { this.$refs.projectForm && this.$refs.projectForm.clearValidate() })
    },
    handleDialogClose() { this.resetForm() },
    handleRefresh() { this.getCollaborationProjectList(); this.getCollaborationStats(); this.loadTypeStats() },
    handleRowClick(row) { this.handleView(row) },

    handleInviteSubmit() {
      if (!this.inviteForm.projectId || !this.inviteForm.email) { this.$message.warning('请填写完整信息'); return }
      this.$message.success('邀请已发送')
      this.inviteDialogVisible = false
      this.inviteForm = { projectId: '', email: '', role: 'MEMBER' }
    },

    getCollaborationTypeColor(type) {
      const m = {
        'BUDGET_PREPARATION': 'primary', 'BUDGET_ADJUSTMENT': 'success',
        'BUDGET_PLANNING': 'warning', 'COST_REDUCTION': 'danger',
        'BUDGET_REVIEW': '', 'BUDGET_INTEGRATION': 'info', 'BUDGET_COORDINATION': 'success',
        // 兼容旧数据
        'DEPARTMENT_COLLABORATION': 'primary', 'CROSS_DEPARTMENT': 'success',
        'PROJECT_TEAM': 'warning', 'EXTERNAL_COLLABORATION': 'danger'
      }
      return m[type] || 'info'
    },
    getCollaborationTypeText(type) {
      const m = {
        'BUDGET_PREPARATION': '预算编制', 'BUDGET_ADJUSTMENT': '预算调整',
        'BUDGET_PLANNING': '预算规划', 'COST_REDUCTION': '降本增效',
        'BUDGET_REVIEW': '预算审核', 'BUDGET_INTEGRATION': '预算整合',
        'BUDGET_COORDINATION': '预算协调',
        // 兼容旧数据
        'DEPARTMENT_COLLABORATION': '部门协作', 'CROSS_DEPARTMENT': '跨部门协作',
        'PROJECT_TEAM': '项目团队', 'EXTERNAL_COLLABORATION': '外部协作'
      }
      return m[type] || type
    },
    getRoleColor(role) { return { 'LEADER': 'danger', 'MEMBER': 'primary', 'REVIEWER': 'warning', 'OBSERVER': 'info' }[role] || 'info' },
    getRoleText(role) { return { 'LEADER': '负责人', 'MEMBER': '成员', 'REVIEWER': '审核人', 'OBSERVER': '观察者' }[role] || role },
    getParticipantStatusColor(s) { return { 'ACTIVE': 'success', 'INACTIVE': 'warning', 'OFFLINE': 'info' }[s] || 'info' },
    getParticipantStatusText(s) { return { 'ACTIVE': '活跃', 'INACTIVE': '不活跃', 'OFFLINE': '离线' }[s] || s },
    getActivityType(type) { return { 'CREATE': 'primary', 'UPDATE': 'success', 'COMMENT': 'warning', 'APPROVE': 'success', 'REJECT': 'danger' }[type] || 'primary' },
    getProgressColor(p) { return p >= 90 ? '#67C23A' : p >= 60 ? '#E6A23C' : '#F56C6C' },
    getStatusColor(s) { return { 'ACTIVE': 'success', 'PENDING': 'warning', 'COMPLETED': 'primary', 'ARCHIVED': 'info', 'DRAFT': '' }[s] || 'info' },
    getStatusText(s) { return { 'ACTIVE': '进行中', 'PENDING': '待开始', 'COMPLETED': '已完成', 'ARCHIVED': '已归档', 'DRAFT': '草稿' }[s] || s }
  }
}
</script>

<style scoped>
.collaborative-budgeting { padding: 20px; }
.page-header { margin-bottom: 20px; }
.page-header h2 { margin: 0 0 8px; font-size: 20px; }
.page-header p { margin: 0; color: #909399; font-size: 14px; }
.toolbar-card { margin-bottom: 20px; }
.text-right { text-align: right; }
.stats-row { margin-bottom: 20px; }
.stat-card { position: relative; overflow: hidden; }
.stat-content { position: relative; z-index: 1; }
.stat-number { font-size: 32px; font-weight: bold; color: #303133; }
.stat-label { font-size: 14px; color: #606266; margin: 4px 0; }
.stat-description { font-size: 12px; color: #909399; }
.stat-icon { position: absolute; right: 20px; top: 50%; transform: translateY(-50%); font-size: 48px; opacity: 0.15; }
.collaboration-types-card { margin-bottom: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.collaboration-type-item { cursor: pointer; text-align: center; padding: 10px; transition: all 0.3s; }
.collaboration-type-item:hover, .collaboration-type-item.selected { border-color: #409EFF; background: #ecf5ff; }
.collaboration-type-icon { font-size: 28px; color: #409EFF; margin-bottom: 8px; }
.collaboration-type-title { font-size: 14px; font-weight: bold; margin-bottom: 6px; }
.collaboration-type-stats { font-size: 12px; color: #909399; display: flex; justify-content: space-around; }
.collaboration-projects-card { margin-bottom: 20px; }
.header-tools { display: flex; align-items: center; }
.progress-text { font-size: 12px; color: #606266; }
.detail-content { padding: 20px; }
.comment-section { padding: 10px 0; }
.comment-input { margin-bottom: 20px; }
.comment-item { border-bottom: 1px solid #EBEEF5; padding: 12px 0; }
.comment-header { display: flex; justify-content: space-between; margin-bottom: 6px; }
.comment-user { font-weight: bold; color: #303133; }
.comment-time { color: #909399; font-size: 12px; }
.comment-content { color: #606266; font-size: 14px; }
</style>
