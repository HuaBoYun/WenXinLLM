<template>
  <div class="financial-common-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-office-building"></i>
          组织架构配置
        </h1>
        <p class="page-description">配置公司组织架构和部门信息</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="syncOrganizations">
          同步组织
        </el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">
          新增组织
        </el-button>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <el-row :gutter="24">
        <!-- 左侧组织树 -->
        <el-col :span="8">
          <el-card class="tree-card">
            <div slot="header" class="clearfix">
              <span>组织架构</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="refreshTree">
                <i class="el-icon-refresh"></i>
              </el-button>
            </div>
            <el-tree
              ref="orgTree"
              :data="organizationTree"
              :props="defaultProps"
              :expand-on-click-node="false"
              :default-expand-all="true"
              node-key="orgId"
              class="org-tree"
              @node-click="handleNodeClick"
            >
              <span class="custom-tree-node" slot-scope="{ node, data }">
                <span>
                  <i :class="getNodeIcon(data)" :style="{color: getNodeColor(data)}"></i>
                  {{ node.label }}
                </span>
                <span class="node-actions">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-plus"
                    @click.stop="handleAddChild(data)"
                  />
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-edit"
                    @click.stop="handleEdit(data)"
                  />
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    @click.stop="handleDelete(data)"
                  />
                </span>
              </span>
            </el-tree>
          </el-card>
        </el-col>

        <!-- 右侧详情 -->
        <el-col :span="16">
          <el-card class="detail-card">
            <div slot="header" class="clearfix">
              <span>{{ currentOrg.orgName ? '组织详情' : '请选择组织' }}</span>
              <el-button
                v-if="currentOrg.orgName"
                style="float: right; padding: 3px 0"
                type="text"
                @click="handleEdit(currentOrg)"
              >
                <i class="el-icon-edit"></i> 编辑
              </el-button>
            </div>

            <div v-if="currentOrg.orgName" class="org-detail">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="组织编码">
                  {{ currentOrg.orgCode }}
                </el-descriptions-item>
                <el-descriptions-item label="组织名称">
                  {{ currentOrg.orgName }}
                </el-descriptions-item>
                <el-descriptions-item label="组织类型">
                  <el-tag :type="getOrgTypeColor(currentOrg.orgType)">
                    {{ getOrgTypeName(currentOrg.orgType) }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="上级组织">
                  {{ currentOrg.parentOrgName || '无' }}
                </el-descriptions-item>
                <el-descriptions-item label="负责人">
                  {{ currentOrg.managerName || '未设置' }}
                </el-descriptions-item>
                <el-descriptions-item label="联系电话">
                  {{ currentOrg.phone || '未设置' }}
                </el-descriptions-item>
                <el-descriptions-item label="启用状态" :span="2">
                  <el-switch
                    v-model="currentOrg.isEnabled"
                    :active-value="1"
                    :inactive-value="0"
                    @change="handleStatusChange"
                  />
                </el-descriptions-item>
                <el-descriptions-item label="组织描述" :span="2">
                  {{ currentOrg.description || '暂无描述' }}
                </el-descriptions-item>
                <el-descriptions-item label="创建时间" :span="2">
                  {{ currentOrg.createTime }}
                </el-descriptions-item>
              </el-descriptions>

              <!-- 统计信息 -->
              <div class="stats-section" style="margin-top: 24px;">
                <h4>统计信息</h4>
                <el-row :gutter="16">
                  <el-col :span="8">
                    <div class="stat-item">
                      <div class="stat-value">{{ currentOrg.childCount || 0 }}</div>
                      <div class="stat-label">下级组织</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="stat-item">
                      <div class="stat-value">{{ currentOrg.deptCount || 0 }}</div>
                      <div class="stat-label">部门数量</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="stat-item">
                      <div class="stat-value">{{ currentOrg.userCount || 0 }}</div>
                      <div class="stat-label">人员数量</div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>

            <div v-else class="empty-state">
              <i class="el-icon-office-building" style="font-size: 64px; color: #c0c4cc;"></i>
              <p>请从左侧选择要查看的组织</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 组织表单对话框 -->
    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form :model="form" :rules="formRules" ref="form" label-width="100px">
        <el-form-item label="上级组织" prop="parentId" v-if="formType !== 'add'">
          <el-cascader
            v-model="form.parentId"
            :options="organizationOptions"
            :props="{ value: 'orgId', label: 'orgName', children: 'children', checkStrictly: true }"
            placeholder="请选择上级组织"
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="组织编码" prop="orgCode">
          <el-input v-model="form.orgCode" placeholder="请输入组织编码" />
        </el-form-item>
        <el-form-item label="组织名称" prop="orgName">
          <el-input v-model="form.orgName" placeholder="请输入组织名称" />
        </el-form-item>
        <el-form-item label="组织类型" prop="orgType">
          <el-select v-model="form.orgType" placeholder="请选择组织类型" style="width: 100%">
            <el-option label="集团" value="GROUP" />
            <el-option label="公司" value="COMPANY" />
            <el-option label="部门" value="DEPARTMENT" />
            <el-option label="项目组" value="PROJECT" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="managerName">
          <el-input v-model="form.managerName" placeholder="请输入负责人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="form.isEnabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="组织描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入组织描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getOrganizationTree,
  getOrganizationById,
  saveOrUpdateOrganization,
  deleteOrganization,
  updateOrganizationStatus,
  syncOrganizations
} from '@/api/financialSharing/system'

export default {
  name: 'Organization',
  data() {
    return {
      // 组织树数据
      organizationTree: [],
      // 当前选中的组织
      currentOrg: {},
      // 表单相关
      formVisible: false,
      formType: 'add', // add/edit
      formTitle: '',
      submitting: false,
      form: {
        parentId: null,
        orgCode: '',
        orgName: '',
        orgType: '',
        managerName: '',
        phone: '',
        isEnabled: 1,
        description: ''
      },
      formRules: {
        orgCode: [
          { required: true, message: '请输入组织编码', trigger: 'blur' }
        ],
        orgName: [
          { required: true, message: '请输入组织名称', trigger: 'blur' }
        ],
        orgType: [
          { required: true, message: '请选择组织类型', trigger: 'change' }
        ]
      },
      // 树形配置
      defaultProps: {
        children: 'children',
        label: 'orgName'
      },
      // 组织类型选项
      orgTypeOptions: {
        GROUP: '集团',
        COMPANY: '公司',
        DEPARTMENT: '部门',
        PROJECT: '项目组'
      }
    }
  },
  computed: {
    organizationOptions() {
      // 构建级联选择器的选项数据
      return this.buildTreeOptions(this.organizationTree)
    }
  },
  mounted() {
    this.loadOrganizationTree()
  },
  methods: {
    /** 加载组织树 */
    async loadOrganizationTree() {
      try {
        // 调用API获取组织树数据
        const response = await getOrganizationTree(1000, 1)
        if (response.code === 1) {
          this.organizationTree = response.data || []
          // 如果有数据，默认选中第一个
          if (this.organizationTree.length > 0) {
            this.$nextTick(() => {
              this.$refs.orgTree.setCurrentKey(this.organizationTree[0].orgId)
              this.handleNodeClick(this.organizationTree[0])
            })
          }
        } else {
          this.$message.error(response.msg || '获取组织架构失败')
          // 接口失败时降级为空状态
          this.loadMockData()
        }
      } catch (error) {
        console.error('加载组织架构失败:', error)
        this.$message.error('加载组织架构失败')
        // 接口失败时降级为空状态
        this.loadMockData()
      }
    },

    /** 加载空状态数据（API 不可用时降级展示） */
    loadMockData() {
      this.organizationTree = []
      this.currentOrg = null
    },

    /** 节点点击事件 */
    handleNodeClick(data) {
      this.currentOrg = { ...data }
    },

    /** 新增组织 */
    handleAdd() {
      this.formType = 'add'
      this.formTitle = '新增组织'
      this.form = {
        parentId: null,
        orgCode: '',
        orgName: '',
        orgType: '',
        managerName: '',
        phone: '',
        isEnabled: 1,
        description: ''
      }
      this.formVisible = true
    },

    /** 新增子组织 */
    handleAddChild(data) {
      this.formType = 'add'
      this.formTitle = '新增下级组织'
      this.form = {
        parentId: data.orgId,
        orgCode: '',
        orgName: '',
        orgType: 'DEPARTMENT',
        managerName: '',
        phone: '',
        isEnabled: 1,
        description: ''
      }
      this.formVisible = true
    },

    /** 编辑组织 */
    handleEdit(data) {
      this.formType = 'edit'
      this.formTitle = '编辑组织'
      this.form = {
        orgId: data.orgId,
        parentId: data.parentId,
        orgCode: data.orgCode,
        orgName: data.orgName,
        orgType: data.orgType,
        managerName: data.managerName,
        phone: data.phone,
        isEnabled: data.isEnabled,
        description: data.description
      }
      this.formVisible = true
    },

    /** 删除组织 */
    async handleDelete(data) {
      try {
        await this.$confirm(`确认删除组织"${data.orgName}"吗？`, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 调用删除API
        const response = await deleteOrganization(data.orgId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.refreshTree()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除组织失败:', error)
          this.$message.error('删除组织失败')
        }
      }
    },

    /** 状态变更 */
    async handleStatusChange() {
      try {
        const statusText = this.currentOrg.isEnabled === 1 ? '启用' : '禁用'
        await this.$confirm(`确认要${statusText}该组织吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 调用状态更新API
        const response = await updateOrganizationStatus(this.currentOrg.orgId, this.currentOrg.isEnabled)

        if (response.code === 1) {
          this.$message.success(`${statusText}成功`)
        } else {
          // 恢复状态
          this.currentOrg.isEnabled = this.currentOrg.isEnabled === 1 ? 0 : 1
          this.$message.error(response.msg || `${statusText}失败`)
        }
      } catch (error) {
        if (error !== 'cancel') {
          // 恢复状态
          this.currentOrg.isEnabled = this.currentOrg.isEnabled === 1 ? 0 : 1
          console.error('状态更新失败:', error)
          this.$message.error('状态更新失败')
        }
      }
    },

    /** 同步组织 */
    async syncOrganizations() {
      try {
        const response = await syncOrganizations()
        if (response.code === 1) {
          this.$message.success('同步成功')
          this.refreshTree()
        } else {
          this.$message.error(response.msg || '同步失败')
        }
      } catch (error) {
        console.error('同步组织失败:', error)
        this.$message.error('同步组织失败')
      }
    },

    /** 刷新树 */
    refreshTree() {
      this.loadOrganizationTree()
    },

    /** 提交表单 */
    async submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            this.submitting = true

            // 准备提交数据
            const submitData = {
              ...this.form,
              orgType: this.form.orgType === 'GROUP' ? 1 :
                      this.form.orgType === 'COMPANY' ? 2 :
                      this.form.orgType === 'DEPARTMENT' ? 3 : 4
            }

            // 调用保存API
            const response = await saveOrUpdateOrganization(submitData)

            if (response.code === 1) {
              this.$message.success('保存成功')
              this.formVisible = false
              this.resetForm()
              this.refreshTree()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          } catch (error) {
            console.error('保存组织失败:', error)
            this.$message.error('保存组织失败')
          } finally {
            this.submitting = false
          }
        }
      })
    },

    /** 重置表单 */
    resetForm() {
      this.$refs.form && this.$refs.form.resetFields()
    },

    /** 获取节点图标 */
    getNodeIcon(data) {
      const iconMap = {
        GROUP: 'el-icon-office-building',
        COMPANY: 'el-icon-school',
        DEPARTMENT: 'el-icon-s-home',
        PROJECT: 'el-icon-s-flag'
      }
      return iconMap[data.orgType] || 'el-icon-folder'
    },

    /** 获取节点颜色 */
    getNodeColor(data) {
      const colorMap = {
        GROUP: '#409eff',
        COMPANY: '#67c23a',
        DEPARTMENT: '#e6a23c',
        PROJECT: '#f56c6c'
      }
      return colorMap[data.orgType] || '#909399'
    },

    /** 获取组织类型名称 */
    getOrgTypeName(type) {
      return this.orgTypeOptions[type] || '未知'
    },

    /** 获取组织类型颜色 */
    getOrgTypeColor(type) {
      const colorMap = {
        GROUP: '',
        COMPANY: 'success',
        DEPARTMENT: 'warning',
        PROJECT: 'danger'
      }
      return colorMap[type] || 'info'
    },

    /** 构建树形选项 */
    buildTreeOptions(tree) {
      return tree.map(node => ({
        ...node,
        children: node.children && node.children.length > 0 ? this.buildTreeOptions(node.children) : undefined
      }))
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-common-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }
}

.content-area {
  .tree-card, .detail-card {
    background: white;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    margin-bottom: 24px;

    &:hover {
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    }
  }
}

.org-tree {
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;

    .node-actions {
      display: none;
    }

    &:hover .node-actions {
      display: inline-flex;
    }
  }
}

.org-detail {
  .stats-section {
    h4 {
      color: #303133;
      font-size: 16px;
      margin-bottom: 16px;
      border-bottom: 1px solid #ebeef5;
      padding-bottom: 8px;
    }

    .stat-item {
      text-align: center;
      background: #f8f9fa;
      border-radius: 8px;
      padding: 16px;

      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: #409eff;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #909399;

  p {
    margin-top: 16px;
    font-size: 14px;
  }
}
</style>