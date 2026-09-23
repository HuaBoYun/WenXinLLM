<template>
  <div class="dimension-configuration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>维度配置</h2>
      <p>管理预算维度的定义、层级结构、属性配置和关联关系，支持多维度预算分析</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建维度</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">导出配置</el-button>
            <el-button type="info" icon="el-icon-upload2" @click="handleImport">导入配置</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="dimensionSettingsVisible = true">维度设置</el-button>
            <el-button icon="el-icon-connection" @click="relationshipConfigVisible = true">关联配置</el-button>
            <el-button icon="el-icon-help" @click="helpVisible = true">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 维度统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dimensionStats.totalDimensions }}</div>
            <div class="stat-label">维度总数</div>
            <div class="stat-description">已配置的维度数量</div>
            <div class="stat-trend">
              <i class="el-icon-s-grid"></i>
              <span>多维分析</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-grid"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dimensionStats.activeDimensions }}</div>
            <div class="stat-label">启用维度</div>
            <div class="stat-description">当前启用的维度</div>
            <div class="stat-trend">
              <i class="el-icon-check"></i>
              <span>正常运行</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card levels-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dimensionStats.totalLevels }}</div>
            <div class="stat-label">层级总数</div>
            <div class="stat-description">所有维度层级数</div>
            <div class="stat-trend">
              <i class="el-icon-share"></i>
              <span>层次清晰</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-share"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card relations-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dimensionStats.totalRelations }}</div>
            <div class="stat-label">关联关系</div>
            <div class="stat-description">维度间关联数量</div>
            <div class="stat-trend">
              <i class="el-icon-connection"></i>
              <span>关联完整</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-connection"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 维度类型选择 -->
    <el-card class="dimension-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>维度类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshDimensionTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="4" v-for="dimensionType in dimensionTypes" :key="dimensionType.id">
          <el-card
            class="dimension-type-item"
            shadow="hover"
            @click.native="handleSelectDimensionType(dimensionType)"
            :class="{ 'selected': selectedDimensionType === dimensionType.value }"
          >
            <div class="dimension-type-icon">
              <i :class="dimensionType.icon"></i>
            </div>
            <div class="dimension-type-title">{{ dimensionType.name }}</div>
            <div class="dimension-type-description">{{ dimensionType.description }}</div>
            <div class="dimension-type-stats">
              <span class="usage-count">{{ dimensionType.usageCount }} 个维度</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 主要内容区域 -->
    <el-row :gutter="20">
      <!-- 左侧维度树 -->
      <el-col :span="8">
        <el-card class="tree-card" shadow="never">
          <div slot="header" class="card-header">
            <span>维度树</span>
            <div class="header-tools">
              <el-button icon="el-icon-refresh" size="mini" @click="refreshDimensionTree">刷新</el-button>
              <el-button icon="el-icon-zoom-in" size="mini" @click="expandAll">展开</el-button>
              <el-button icon="el-icon-zoom-out" size="mini" @click="collapseAll">收起</el-button>
            </div>
          </div>
          <el-tree
            ref="dimensionTree"
            :data="dimensionTreeData"
            :props="treeProps"
            node-key="dimensionId"
            :expand-on-click-node="false"
            :highlight-current="true"
            @node-click="handleNodeClick"
            @node-contextmenu="handleNodeRightClick"
          >
            <span class="tree-node" slot-scope="{ node, data }">
              <span class="node-icon">
                <i :class="getNodeIcon(data)"></i>
              </span>
              <span class="node-label">{{ node.label }}</span>
              <span class="node-status">
                <el-tag :type="data.isActive ? 'success' : 'danger'" size="mini">
                  {{ data.isActive ? '启用' : '停用' }}
                </el-tag>
              </span>
            </span>
          </el-tree>
        </el-card>
      </el-col>
      
      <!-- 右侧详情区域 -->
      <el-col :span="16">
        <el-card class="detail-card" shadow="never">
          <div slot="header" class="card-header">
            <span>{{ currentDimension ? currentDimension.dimensionName : '维度详情' }}</span>
            <div class="header-tools" v-if="currentDimension">
              <el-button icon="el-icon-edit" size="mini" @click="handleEdit(currentDimension)">编辑</el-button>
              <el-button icon="el-icon-plus" size="mini" @click="handleAddChild(currentDimension)">添加子维度</el-button>
              <el-button icon="el-icon-delete" size="mini" @click="handleDelete(currentDimension)">删除</el-button>
            </div>
          </div>
          
          <div v-if="currentDimension" class="dimension-detail">
            <el-tabs v-model="detailActiveTab" type="card">
              <el-tab-pane label="基本信息" name="basic">
                <el-descriptions title="维度基本信息" :column="2" border>
                  <el-descriptions-item label="维度名称">{{ currentDimension.dimensionName }}</el-descriptions-item>
                  <el-descriptions-item label="维度编码">{{ currentDimension.dimensionCode }}</el-descriptions-item>
                  <el-descriptions-item label="维度类型">{{ getDimensionTypeText(currentDimension.dimensionType) }}</el-descriptions-item>
                  <el-descriptions-item label="维度状态">
                    <el-tag :type="currentDimension.isActive ? 'success' : 'danger'" size="mini">
                      {{ currentDimension.isActive ? '启用' : '停用' }}
                    </el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="层级级别">{{ currentDimension.dimensionLevel || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="排序序号">{{ currentDimension.sortOrder }}</el-descriptions-item>
                  <el-descriptions-item label="是否必填">
                    <el-tag :type="currentDimension.isRequired ? 'danger' : 'info'" size="mini">
                      {{ currentDimension.isRequired ? '是' : '否' }}
                    </el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="是否叶子节点">
                    <el-tag :type="currentDimension.isLeaf ? 'success' : 'warning'" size="mini">
                      {{ currentDimension.isLeaf ? '是' : '否' }}
                    </el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="创建人">{{ currentDimension.creatorName }}</el-descriptions-item>
                  <el-descriptions-item label="创建时间">{{ currentDimension.createTime }}</el-descriptions-item>
                  <el-descriptions-item label="维度描述" :span="2">{{ currentDimension.dimensionDescription }}</el-descriptions-item>
                </el-descriptions>
              </el-tab-pane>
              
              <el-tab-pane label="属性配置" name="attributes">
                <el-table :data="dimensionAttributes" border size="mini">
                  <el-table-column prop="attributeName" label="属性名称" width="150" />
                  <el-table-column prop="attributeType" label="属性类型" width="120">
                    <template slot-scope="scope">
                      <el-tag :type="getAttributeTypeColor(scope.row.attributeType)" size="mini">
                        {{ getAttributeTypeText(scope.row.attributeType) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="defaultValue" label="默认值" width="120" />
                  <el-table-column prop="isRequired" label="必填" width="80" align="center">
                    <template slot-scope="scope">
                      <el-tag :type="scope.row.isRequired ? 'danger' : 'info'" size="mini">
                        {{ scope.row.isRequired ? '是' : '否' }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="attributeDescription" label="属性描述" />
                  <el-table-column label="操作" width="120">
                    <template slot-scope="scope">
                      <el-button type="text" size="mini" @click="handleEditAttribute(scope.row)">编辑</el-button>
                      <el-button type="text" size="mini" @click="handleDeleteAttribute(scope.row)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="attribute-actions">
                  <el-button type="primary" size="mini" @click="handleAddAttribute">添加属性</el-button>
                  <el-button size="mini" @click="getDimensionAttributes(currentDimension.dimensionId)">刷新</el-button>
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="关联关系" name="relations">
                <el-table :data="dimensionRelations" border size="mini">
                  <el-table-column prop="relatedDimensionName" label="关联维度" width="150" />
                  <el-table-column prop="relationType" label="关联类型" width="120">
                    <template slot-scope="scope">
                      <el-tag :type="getRelationTypeColor(scope.row.relationType)" size="mini">
                        {{ getRelationTypeText(scope.row.relationType) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="relationRule" label="关联规则" />
                  <el-table-column prop="isActive" label="状态" width="80" align="center">
                    <template slot-scope="scope">
                      <el-tag :type="scope.row.isActive ? 'success' : 'danger'" size="mini">
                        {{ scope.row.isActive ? '启用' : '停用' }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="120">
                    <template slot-scope="scope">
                      <el-button type="text" size="mini" @click="handleEditRelation(scope.row)">编辑</el-button>
                      <el-button type="text" size="mini" @click="handleDeleteRelation(scope.row)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="relation-actions">
                  <el-button type="primary" size="mini" @click="handleAddRelation">添加关联</el-button>
                  <el-button size="mini" @click="getDimensionRelations(currentDimension.dimensionId)">刷新</el-button>
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="子维度" name="children">
                <el-table :data="childDimensions" border size="mini">
                  <el-table-column prop="dimensionName" label="维度名称" width="150" />
                  <el-table-column prop="dimensionCode" label="维度编码" width="120" />
                  <el-table-column prop="dimensionType" label="维度类型" width="120">
                    <template slot-scope="scope">
                      <el-tag :type="getDimensionTypeColor(scope.row.dimensionType)" size="mini">
                        {{ getDimensionTypeText(scope.row.dimensionType) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="isActive" label="状态" width="80" align="center">
                    <template slot-scope="scope">
                      <el-tag :type="scope.row.isActive ? 'success' : 'danger'" size="mini">
                        {{ scope.row.isActive ? '启用' : '停用' }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
                  <el-table-column prop="createTime" label="创建时间" width="150" />
                  <el-table-column label="操作" width="120">
                    <template slot-scope="scope">
                      <el-button type="text" size="mini" @click="handleViewChild(scope.row)">查看</el-button>
                      <el-button type="text" size="mini" @click="handleEditChild(scope.row)">编辑</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
            </el-tabs>
          </div>
          
          <div v-else class="empty-state">
            <el-empty description="请选择左侧维度树中的维度查看详情" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 新增/编辑维度对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="dimensionForm"
        :model="dimensionForm"
        :rules="dimensionRules"
        label-width="120px"
        size="small"
      >
        <el-form-item label="维度名称" prop="dimensionName">
          <el-input v-model="dimensionForm.dimensionName" placeholder="请输入维度名称" />
        </el-form-item>
        <el-form-item label="维度编码" prop="dimensionCode">
          <el-input v-model="dimensionForm.dimensionCode" placeholder="请输入维度编码" />
        </el-form-item>
        <el-form-item label="维度类型" prop="dimensionType">
          <el-select v-model="dimensionForm.dimensionType" placeholder="请选择维度类型" style="width: 100%">
            <el-option value="ORGANIZATION" label="组织维度" />
            <el-option value="TIME" label="时间维度" />
            <el-option value="ACCOUNT" label="科目维度" />
            <el-option value="PROJECT" label="项目维度" />
            <el-option value="PRODUCT" label="产品维度" />
            <el-option value="CUSTOM" label="自定义维度" />
          </el-select>
        </el-form-item>
        <el-form-item label="上级维度" prop="parentId">
          <el-select
            v-model="dimensionForm.parentId"
            placeholder="请选择上级维度（不选则为顶级）"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in parentDimensionFlatList"
              :key="item.dimensionId"
              :label="item.dimensionName"
              :value="item.dimensionId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序序号" prop="sortOrder">
          <el-input-number v-model="dimensionForm.sortOrder" :min="1" :max="999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="是否必填" prop="isRequired">
          <el-switch v-model="dimensionForm.isRequired" />
        </el-form-item>
        <el-form-item label="维度描述" prop="dimensionDescription">
          <el-input
            v-model="dimensionForm.dimensionDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入维度描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 维度设置弹窗 -->
    <el-dialog title="维度设置" :visible.sync="dimensionSettingsVisible" width="500px" :close-on-click-modal="false">
      <el-form label-width="120px" size="small">
        <el-form-item label="默认维度类型">
          <el-select placeholder="请选择" style="width:100%">
            <el-option value="ORGANIZATION" label="组织维度" />
            <el-option value="TIME" label="时间维度" />
            <el-option value="ACCOUNT" label="科目维度" />
          </el-select>
        </el-form-item>
        <el-form-item label="维度层级限制">
          <el-input-number :min="1" :max="10" :value="5" style="width:100%" />
        </el-form-item>
        <el-form-item label="启用多维分析">
          <el-switch :value="true" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dimensionSettingsVisible = false">关闭</el-button>
        <el-button type="primary" @click="dimensionSettingsVisible = false">保存</el-button>
      </div>
    </el-dialog>

    <!-- 关联配置弹窗 -->
    <el-dialog title="关联配置" :visible.sync="relationshipConfigVisible" width="600px" :close-on-click-modal="false">
      <el-alert type="info" :closable="false" title="配置维度之间的关联规则，用于多维度联动分析" style="margin-bottom:16px" />
      <el-table :data="[]" border size="mini">
        <el-table-column label="源维度" prop="sourceDimension" />
        <el-table-column label="目标维度" prop="targetDimension" />
        <el-table-column label="关联类型" prop="relationType" />
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button type="text" size="mini">编辑</el-button>
            <el-button type="text" size="mini">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="relationshipConfigVisible = false">关闭</el-button>
        <el-button type="primary">新增关联</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="帮助文档" :visible.sync="helpVisible" width="600px">
      <el-collapse>
        <el-collapse-item title="什么是维度配置？" name="1">
          <p>维度配置用于定义预算分析的多个维度，如组织、时间、科目、项目等，支持层级结构和关联关系。</p>
        </el-collapse-item>
        <el-collapse-item title="如何新建维度？" name="2">
          <p>点击左上角「新建维度」按钮，填写维度名称、编码、类型等信息，可选择上级维度构建层级结构。</p>
        </el-collapse-item>
        <el-collapse-item title="维度类型说明" name="3">
          <ul>
            <li><b>组织维度</b>：对应公司组织架构</li>
            <li><b>时间维度</b>：年/季/月等时间周期</li>
            <li><b>科目维度</b>：会计科目体系</li>
            <li><b>项目维度</b>：项目分类管理</li>
            <li><b>产品维度</b>：产品线分类</li>
            <li><b>自定义维度</b>：用户自定义分析维度</li>
          </ul>
        </el-collapse-item>
      </el-collapse>
      <div slot="footer">
        <el-button type="primary" @click="helpVisible = false">知道了</el-button>
      </div>
    </el-dialog>

    <!-- 添加属性弹窗 -->
    <el-dialog :title="editingAttributeId ? '编辑属性' : '添加属性'" :visible.sync="attributeDialogVisible" width="480px" :close-on-click-modal="false" @close="resetAttributeForm">
      <el-form ref="attributeForm" :model="attributeForm" :rules="attributeRules" label-width="100px" size="small">
        <el-form-item label="属性名称" prop="attributeName">
          <el-input v-model="attributeForm.attributeName" placeholder="请输入属性名称" />
        </el-form-item>
        <el-form-item label="属性类型" prop="attributeType">
          <el-select v-model="attributeForm.attributeType" placeholder="请选择属性类型" style="width:100%">
            <el-option value="STRING" label="字符串" />
            <el-option value="NUMBER" label="数字" />
            <el-option value="DATE" label="日期" />
            <el-option value="BOOLEAN" label="布尔值" />
            <el-option value="LIST" label="列表" />
          </el-select>
        </el-form-item>
        <el-form-item label="默认值">
          <el-input v-model="attributeForm.defaultValue" placeholder="请输入默认值（可选）" />
        </el-form-item>
        <el-form-item label="是否必填">
          <el-switch v-model="attributeForm.isRequired" />
        </el-form-item>
        <el-form-item label="属性描述">
          <el-input v-model="attributeForm.attributeDescription" type="textarea" :rows="2" placeholder="请输入属性描述（可选）" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="attributeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAttributeForm" :loading="attributeSubmitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 添加关联弹窗 -->
    <el-dialog :title="editingRelationId ? '编辑关联关系' : '添加关联关系'" :visible.sync="relationDialogVisible" width="480px" :close-on-click-modal="false" @close="resetRelationForm">
      <el-form ref="relationForm" :model="relationForm" :rules="relationRules" label-width="100px" size="small">
        <el-form-item label="关联维度" prop="targetDimensionId">
          <el-select v-model="relationForm.targetDimensionId" placeholder="请选择关联维度" style="width:100%" filterable>
            <el-option
              v-for="item in parentDimensionFlatList"
              :key="item.dimensionId"
              :label="item.dimensionName"
              :value="item.dimensionId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="关联类型" prop="relationType">
          <el-select v-model="relationForm.relationType" placeholder="请选择关联类型" style="width:100%">
            <el-option value="ONE_TO_ONE" label="一对一" />
            <el-option value="ONE_TO_MANY" label="一对多" />
            <el-option value="MANY_TO_MANY" label="多对多" />
            <el-option value="HIERARCHY" label="层级关系" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联规则">
          <el-input v-model="relationForm.relationRule" placeholder="请输入关联规则（可选）" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="relationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRelationForm" :loading="relationSubmitLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

// ==================== 本地API调用方法 ====================
// 由于公共API文件路径不一致,在组件内部直接定义正确的API调用

/**
 * 创建维度
 */
function createDimensionLocal(data) {
  return request({
    url: '/glkj/accountant/budget/dimension/create',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

/**
 * 查询维度详情
 */
function getDimensionLocal(dimensionId) {
  return request({
    url: `/glkj/accountant/budget/dimension/detail/${dimensionId}`,
    method: 'get'
  })
}

/**
 * 更新维度
 */
function updateDimensionLocal(dimensionId, data) {
  return request({
    url: `/glkj/accountant/budget/dimension/update/${dimensionId}`,
    method: 'put',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

/**
 * 删除维度
 */
function deleteDimensionLocal(dimensionId) {
  return request({
    url: `/glkj/accountant/budget/dimension/delete/${dimensionId}`,
    method: 'delete'
  })
}

/**
 * 获取父级维度列表（扁平）
 */
function getParentDimensionsLocal() {
  return request({
    url: '/glkj/accountant/budget/dimension/parents',
    method: 'get'
  })
}

/**
 * 按类型统计维度数量
 */
function getDimensionPageLocal(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/dimension/page',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: { pageNum: current, pageSize: size, ...params }
  })
}

/**
 * 获取维度树
 */
function getDimensionTreeLocal() {
  return request({
    url: '/glkj/accountant/budget/dimension/tree',
    method: 'get'
  })
}

/**
 * 获取子维度列表
 */
function getDimensionChildrenLocal(parentId) {
  return request({
    url: `/glkj/accountant/budget/dimension/children/${parentId}`,
    method: 'get'
  })
}

/**
 * 获取维度属性
 */
function getDimensionAttributesLocal(dimensionId) {
  return request({
    url: `/glkj/accountant/budget/dimension/attributes/${dimensionId}`,
    method: 'get'
  })
}

/**
 * 获取维度关联关系
 */
function getDimensionRelationsLocal(dimensionId) {
  return request({
    url: `/glkj/accountant/budget/dimension/relations/${dimensionId}`,
    method: 'get'
  })
}

/**
 * 批量删除维度
 */
function batchDeleteDimensionsLocal(ids) {
  return request({
    url: '/glkj/accountant/budget/dimension/batch-delete',
    method: 'delete',
    data: { ids }
  })
}

/**
 * 获取维度统计数据
 */
function getDimensionStatsLocal() {
  return request({
    url: '/glkj/accountant/budget/dimension/stats',
    method: 'get'
  })
}

/**
 * 更新维度属性
 */
function updateDimensionAttributeLocal(attributeId, data) {
  return request({
    url: `/glkj/accountant/budget/dimension/attribute/update/${attributeId}`,
    method: 'put',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

/**
 * 删除维度属性
 */
function deleteDimensionAttributeLocal(attributeId) {
  return request({
    url: `/glkj/accountant/budget/dimension/attribute/delete/${attributeId}`,
    method: 'delete'
  })
}

/**
 * 更新维度关联
 */
function updateDimensionRelationLocal(relationId, data) {
  return request({
    url: `/glkj/accountant/budget/dimension/relation/update/${relationId}`,
    method: 'put',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

/**
 * 删除维度关联
 */
function deleteDimensionRelationLocal(relationId) {
  return request({
    url: `/glkj/accountant/budget/dimension/relation/delete/${relationId}`,
    method: 'delete'
  })
}

/**
 * 导入维度配置
 */
function importDimensionLocal(data) {
  return request({
    url: '/glkj/accountant/budget/dimension/import',
    method: 'post',
    data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export default {
  name: 'DimensionConfiguration',
  data() {
    return {
      // 统计数据
      dimensionStats: {
        totalDimensions: 0,
        activeDimensions: 0,
        totalLevels: 0,
        totalRelations: 0
      },

      // 维度类型
      dimensionTypes: [
        { id: 1, name: '组织维度', description: '组织架构维度', icon: 'el-icon-office-building', usageCount: 0, value: 'ORGANIZATION' },
        { id: 2, name: '时间维度', description: '时间周期维度', icon: 'el-icon-date', usageCount: 0, value: 'TIME' },
        { id: 3, name: '科目维度', description: '会计科目维度', icon: 'el-icon-document', usageCount: 0, value: 'ACCOUNT' },
        { id: 4, name: '项目维度', description: '项目分类维度', icon: 'el-icon-folder', usageCount: 0, value: 'PROJECT' },
        { id: 5, name: '产品维度', description: '产品分类维度', icon: 'el-icon-goods', usageCount: 0, value: 'PRODUCT' },
        { id: 6, name: '自定义维度', description: '用户自定义维度', icon: 'el-icon-setting', usageCount: 0, value: 'CUSTOM' }
      ],
      selectedDimensionType: null,
      
      // 维度树
      dimensionTreeData: [],
      treeProps: {
        children: 'children',
        label: 'dimensionName'
      },
      
      // 当前选中的维度
      currentDimension: null,
      detailActiveTab: 'basic',
      
      // 维度属性
      dimensionAttributes: [],
      
      // 维度关联
      dimensionRelations: [],
      
      // 子维度
      childDimensions: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      
      // 表单数据
      dimensionForm: {
        dimensionName: '',
        dimensionCode: '',
        dimensionType: '',
        parentId: null,
        sortOrder: 1,
        isRequired: false,
        dimensionDescription: ''
      },
      
      // 表单验证规则
      dimensionRules: {
        dimensionName: [
          { required: true, message: '请输入维度名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        dimensionCode: [
          { required: true, message: '请输入维度编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' }
        ],
        dimensionType: [
          { required: true, message: '请选择维度类型', trigger: 'change' }
        ]
      },
      
      // 上级维度选项（扁平列表，用于下拉）
      parentDimensionOptions: [],
      parentDimensionFlatList: [],

      // 三个功能弹窗
      dimensionSettingsVisible: false,
      relationshipConfigVisible: false,
      helpVisible: false,

      // 添加属性弹窗
      attributeDialogVisible: false,
      attributeSubmitLoading: false,
      editingAttributeId: null,
      attributeForm: {
        attributeName: '',
        attributeType: '',
        defaultValue: '',
        isRequired: false,
        attributeDescription: ''
      },
      attributeRules: {
        attributeName: [{ required: true, message: '请输入属性名称', trigger: 'blur' }],
        attributeType: [{ required: true, message: '请选择属性类型', trigger: 'change' }]
      },

      // 添加关联弹窗
      relationDialogVisible: false,
      relationSubmitLoading: false,
      editingRelationId: null,
      relationForm: {
        targetDimensionId: '',
        relationType: '',
        relationRule: ''
      },
      relationRules: {
        targetDimensionId: [{ required: true, message: '请选择关联维度', trigger: 'change' }],
        relationType: [{ required: true, message: '请选择关联类型', trigger: 'change' }]
      }
    }
  },
  
  created() {
    this.getDimensionTree()
    this.getDimensionStats()
    this.getParentDimensionOptions()
  },
  
  methods: {
    // 获取维度树
    async getDimensionTree() {
      try {
        const response = await getDimensionTreeLocal()
        if (response.code === 1) {
          this.dimensionTreeData = response.data || []
        }
      } catch (error) {
        this.$message.error('获取维度树失败：' + error.message)
      }
    },

    // 获取统计数据（含各类型计数）
    async getDimensionStats() {
      try {
        const response = await getDimensionStatsLocal()
        if (response.code === 1 && response.data) {
          this.dimensionStats = {
            totalDimensions: response.data.totalDimensions || 0,
            activeDimensions: response.data.activeDimensions || 0,
            totalLevels: response.data.hierarchyDimensions || 0,
            totalRelations: response.data.totalMembers || 0
          }
        }
        // 并行查询各维度类型的数量
        const types = ['ORGANIZATION', 'TIME', 'ACCOUNT', 'PROJECT', 'PRODUCT', 'CUSTOM']
        const counts = await Promise.all(
          types.map(t => getDimensionPageLocal(1, 1, { dimensionType: t }).catch(() => null))
        )
        types.forEach((t, i) => {
          const idx = this.dimensionTypes.findIndex(d => d.value === t)
          if (idx !== -1 && counts[i] && counts[i].code === 1) {
            this.$set(this.dimensionTypes[idx], 'usageCount', counts[i].data ? counts[i].data.total || 0 : 0)
          }
        })
      } catch (error) {
        this.$message.error('获取统计数据失败')
      }
    },

    // 获取上级维度选项（扁平列表）
    async getParentDimensionOptions() {
      try {
        const response = await getParentDimensionsLocal()
        if (response.code === 1) {
          this.parentDimensionFlatList = response.data || []
          this.parentDimensionOptions = response.data || []
        }
      } catch (error) {
        this.$message.error('获取上级维度选项失败')
      }
    },

    // 节点点击 — 调 detail 接口获取完整字段（含 isActive、creatorName 等）
    async handleNodeClick(data) {
      try {
        const res = await getDimensionLocal(data.dimensionId)
        this.currentDimension = (res.code === 1 && res.data) ? res.data : data
      } catch (e) {
        this.currentDimension = data
      }
      this.detailActiveTab = 'basic'
      const id = data.dimensionId
      await this.getDimensionAttributes(id)
      await this.getDimensionRelations(id)
      await this.getChildDimensions(id)
    },

    // 获取维度属性
    async getDimensionAttributes(dimensionId) {
      try {
        const response = await getDimensionAttributesLocal(dimensionId)
        this.dimensionAttributes = response.data || []
      } catch (error) {
        this.dimensionAttributes = []
      }
    },

    // 获取维度关联
    async getDimensionRelations(dimensionId) {
      try {
        const response = await getDimensionRelationsLocal(dimensionId)
        this.dimensionRelations = response.data || []
      } catch (error) {
        this.dimensionRelations = []
      }
    },

    // 获取子维度
    async getChildDimensions(dimensionId) {
      try {
        const response = await getDimensionChildrenLocal(dimensionId)
        this.childDimensions = response.data || []
      } catch (error) {
        this.childDimensions = []
      }
    },
    
    // 新增维度
    handleAdd() {
      this.dialogTitle = '新建维度'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑维度
    handleEdit(dimension) {
      this.dialogTitle = '编辑维度'
      this.dialogVisible = true
      this.dimensionForm = { ...dimension }
    },
    
    // 添加子维度
    handleAddChild(dimension) {
      this.dialogTitle = '添加子维度'
      this.dialogVisible = true
      this.resetForm()
      this.dimensionForm.parentId = dimension.dimensionId
    },

    // 删除维度
    handleDelete(dimension) {
      this.$confirm('确定删除该维度吗？删除后将无法恢复！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteDimensionLocal(dimension.dimensionId)
          this.$message.success('删除成功')
          this.getDimensionTree()
          this.getDimensionStats()
          this.currentDimension = null
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 提交表单
    async handleSubmitForm() {
      this.$refs.dimensionForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            const formData = { ...this.dimensionForm }
            // isRequired 转为 0/1
            formData.isRequired = formData.isRequired ? 1 : 0
            if (formData.dimensionId) {
              await updateDimensionLocal(formData.dimensionId, formData)
              this.$message.success('更新成功')
            } else {
              await createDimensionLocal(formData)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getDimensionTree()
            this.getDimensionStats()
            this.getParentDimensionOptions()
          } catch (error) {
            this.$message.error('操作失败：' + error.message)
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    
    // 重置表单
    resetForm() {
      this.dimensionForm = {
        dimensionName: '',
        dimensionCode: '',
        dimensionType: '',
        parentId: null,
        sortOrder: 1,
        isRequired: false,
        dimensionDescription: ''
      }
      this.$nextTick(() => {
        this.$refs.dimensionForm && this.$refs.dimensionForm.clearValidate()
      })
    },
    
    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },
    
    // 刷新
    handleRefresh() {
      this.getDimensionTree()
      this.getDimensionStats()
    },
    
    // 导出配置
    async handleExport() {
      try {
        const treeData = this.dimensionTreeData || []
        if (treeData.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        // 扁平化树数据
        const flatData = []
        const flatten = (nodes, level) => {
          nodes.forEach(node => {
            flatData.push(node)
            if (node.children && node.children.length > 0) {
              flatten(node.children, level + 1)
            }
          })
        }
        flatten(treeData, 0)
        const header = ['维度名称', '维度编码', '维度类型', '排序号', '是否必填', '维度描述']
        const filterVal = ['dimensionName', 'dimensionCode', 'dimensionType', 'sortOrder', 'isRequired', 'dimensionDescription']
        const data = flatData.map(item => filterVal.map(key => {
          if (key === 'isRequired') return item[key] === 1 ? '是' : '否'
          return item[key] || ''
        }))
        const excel = await import('@/utils/excel')
        excel.export_json_to_excel({
          header,
          data,
          filename: '维度配置数据',
          autoWidth: true,
          bookType: 'xlsx'
        })
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 导入配置
    handleImport() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        const formData = new FormData()
        formData.append('file', file)
        try {
          const response = await importDimensionLocal(formData)
          if (response.code === 1) {
            this.$message.success('导入成功')
            this.getDimensionTree()
            this.getDimensionStats()
          } else {
            this.$message.error(response.msg || '导入失败')
          }
        } catch (error) {
          this.$message.error('导入失败：' + error.message)
        }
      }
      input.click()
    },
    
    // 维度设置 — 打开弹窗
    handleDimensionSettings() {
      this.dimensionSettingsVisible = true
    },

    // 关联配置 — 打开弹窗
    handleRelationshipConfig() {
      this.relationshipConfigVisible = true
    },

    // 帮助 — 打开弹窗
    handleHelp() {
      this.helpVisible = true
    },
    
    // 刷新维度类型
    refreshDimensionTypes() {
      this.getDimensionStats()
    },

    // 选择维度类型 — 切换时筛选树
    handleSelectDimensionType(dimensionType) {
      if (this.selectedDimensionType === dimensionType.value) {
        // 再次点击取消筛选
        this.selectedDimensionType = null
        this.getDimensionTree()
      } else {
        this.selectedDimensionType = dimensionType.value
        this.filterTreeByType(dimensionType.value)
      }
    },

    // 按类型筛选树（重新拉取 page 接口，构建扁平树）
    async filterTreeByType(type) {
      try {
        const res = await getDimensionPageLocal(1, 999, { dimensionType: type })
        if (res.code === 1 && res.data) {
          const records = res.data.records || []
          // 构建树结构
          const map = {}
          records.forEach(r => { map[r.dimensionId] = { ...r, children: [] } })
          const roots = []
          records.forEach(r => {
            if (r.parentId && map[r.parentId]) {
              map[r.parentId].children.push(map[r.dimensionId])
            } else {
              roots.push(map[r.dimensionId])
            }
          })
          this.dimensionTreeData = roots
        }
      } catch (e) {
        this.$message.error('筛选失败')
      }
    },

    // 刷新维度树
    refreshDimensionTree() {
      this.selectedDimensionType = null
      this.getDimensionTree()
    },

    // 展开所有节点
    expandAll() {
      const nodesMap = this.$refs.dimensionTree.store.nodesMap
      Object.keys(nodesMap).forEach(key => {
        nodesMap[key].expanded = true
      })
    },

    // 收起所有节点
    collapseAll() {
      const nodesMap = this.$refs.dimensionTree.store.nodesMap
      Object.keys(nodesMap).forEach(key => {
        nodesMap[key].expanded = false
      })
    },
    
    // 节点右键菜单
    handleNodeRightClick(event, data) {
      // 预留右键菜单功能
    },

    // 添加属性 — 打开弹窗
    handleAddAttribute() {
      if (!this.currentDimension) {
        this.$message.warning('请先选择一个维度')
        return
      }
      this.resetAttributeForm()
      this.attributeDialogVisible = true
    },

    // 编辑属性
    handleEditAttribute(attribute) {
      this.editingAttributeId = attribute.attributeId
      this.attributeForm = {
        attributeName: attribute.attributeName || '',
        attributeType: attribute.attributeType || '',
        defaultValue: attribute.defaultValue || '',
        isRequired: !!attribute.isRequired,
        attributeDescription: attribute.attributeDescription || ''
      }
      this.attributeDialogVisible = true
    },

    // 删除属性
    async handleDeleteAttribute(attribute) {
      this.$confirm('确定删除该属性吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteDimensionAttributeLocal(attribute.attributeId || attribute.id)
          this.$message.success('删除成功')
          this.getDimensionAttributes(this.currentDimension.dimensionId)
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 提交属性表单
    async submitAttributeForm() {
      this.$refs.attributeForm.validate(async (valid) => {
        if (!valid) return
        this.attributeSubmitLoading = true
        try {
          const payload = {
            ...this.attributeForm,
            dimensionId: this.currentDimension.dimensionId,
            isRequired: this.attributeForm.isRequired ? 1 : 0
          }
          let res
          if (this.editingAttributeId) {
            res = await updateDimensionAttributeLocal(this.editingAttributeId, payload)
          } else {
            res = await request({
              url: '/glkj/accountant/budget/dimension/attribute/create',
              method: 'post',
              headers: { 'Content-Type': 'application/json' },
              data: payload
            })
          }
          if (res.code === 1) {
            this.$message.success(this.editingAttributeId ? '编辑属性成功' : '添加属性成功')
            this.attributeDialogVisible = false
            this.getDimensionAttributes(this.currentDimension.dimensionId)
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + error.message)
        } finally {
          this.attributeSubmitLoading = false
        }
      })
    },

    // 重置属性表单
    resetAttributeForm() {
      this.editingAttributeId = null
      this.attributeForm = {
        attributeName: '',
        attributeType: '',
        defaultValue: '',
        isRequired: false,
        attributeDescription: ''
      }
      this.$nextTick(() => {
        this.$refs.attributeForm && this.$refs.attributeForm.clearValidate()
      })
    },

    // 添加关联 — 打开弹窗
    handleAddRelation() {
      if (!this.currentDimension) {
        this.$message.warning('请先选择一个维度')
        return
      }
      this.resetRelationForm()
      this.relationDialogVisible = true
    },

    // 编辑关联
    handleEditRelation(relation) {
      this.editingRelationId = relation.relationId
      this.relationForm = {
        targetDimensionId: relation.targetDimensionId || '',
        relationType: relation.relationType || '',
        relationRule: relation.relationRule || ''
      }
      this.relationDialogVisible = true
    },

    // 删除关联
    async handleDeleteRelation(relation) {
      this.$confirm('确定删除该关联吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteDimensionRelationLocal(relation.relationId || relation.id)
          this.$message.success('删除成功')
          this.getDimensionRelations(this.currentDimension.dimensionId)
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 提交关联表单
    async submitRelationForm() {
      this.$refs.relationForm.validate(async (valid) => {
        if (!valid) return
        this.relationSubmitLoading = true
        try {
          const payload = {
            ...this.relationForm,
            sourceDimensionId: this.currentDimension.dimensionId
          }
          let res
          if (this.editingRelationId) {
            res = await updateDimensionRelationLocal(this.editingRelationId, payload)
          } else {
            res = await request({
              url: '/glkj/accountant/budget/dimension/relation/create',
              method: 'post',
              headers: { 'Content-Type': 'application/json' },
              data: payload
            })
          }
          if (res.code === 1) {
            this.$message.success(this.editingRelationId ? '编辑关联成功' : '添加关联成功')
            this.relationDialogVisible = false
            this.getDimensionRelations(this.currentDimension.dimensionId)
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + error.message)
        } finally {
          this.relationSubmitLoading = false
        }
      })
    },

    // 重置关联表单
    resetRelationForm() {
      this.editingRelationId = null
      this.relationForm = {
        targetDimensionId: '',
        relationType: '',
        relationRule: ''
      }
      this.$nextTick(() => {
        this.$refs.relationForm && this.$refs.relationForm.clearValidate()
      })
    },
    
    // 查看子维度
    handleViewChild(child) {
      this.handleNodeClick(child)
    },
    
    // 编辑子维度
    handleEditChild(child) {
      this.handleEdit(child)
    },
    
    // 获取节点图标
    getNodeIcon(data) {
      const iconMap = {
        'ORGANIZATION': 'el-icon-office-building',
        'TIME': 'el-icon-date',
        'ACCOUNT': 'el-icon-document',
        'PROJECT': 'el-icon-folder',
        'PRODUCT': 'el-icon-goods',
        'CUSTOM': 'el-icon-setting'
      }
      return iconMap[data.dimensionType] || 'el-icon-folder'
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'DRAFT': 'info',
        'ARCHIVED': 'danger'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '启用',
        'INACTIVE': '停用',
        'DRAFT': '草稿',
        'ARCHIVED': '归档'
      }
      return textMap[status] || status
    },
    
    // 获取维度类型颜色
    getDimensionTypeColor(type) {
      const colorMap = {
        'ORGANIZATION': 'primary',
        'TIME': 'success',
        'ACCOUNT': 'warning',
        'PROJECT': 'info',
        'PRODUCT': 'danger',
        'CUSTOM': 'primary'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取维度类型文本
    getDimensionTypeText(type) {
      const textMap = {
        'ORGANIZATION': '组织维度',
        'TIME': '时间维度',
        'ACCOUNT': '科目维度',
        'PROJECT': '项目维度',
        'PRODUCT': '产品维度',
        'CUSTOM': '自定义维度'
      }
      return textMap[type] || type
    },
    
    // 获取属性类型颜色
    getAttributeTypeColor(type) {
      const colorMap = {
        'STRING': 'primary',
        'NUMBER': 'success',
        'DATE': 'warning',
        'BOOLEAN': 'info',
        'LIST': 'danger'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取属性类型文本
    getAttributeTypeText(type) {
      const textMap = {
        'STRING': '字符串',
        'NUMBER': '数字',
        'DATE': '日期',
        'BOOLEAN': '布尔值',
        'LIST': '列表'
      }
      return textMap[type] || type
    },
    
    // 获取关联类型颜色
    getRelationTypeColor(type) {
      const colorMap = {
        'ONE_TO_ONE': 'primary',
        'ONE_TO_MANY': 'success',
        'MANY_TO_MANY': 'warning',
        'HIERARCHY': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取关联类型文本
    getRelationTypeText(type) {
      const textMap = {
        'ONE_TO_ONE': '一对一',
        'ONE_TO_MANY': '一对多',
        'MANY_TO_MANY': '多对多',
        'HIERARCHY': '层级关系'
      }
      return textMap[type] || type
    }
  }
}
</script>

<style lang="scss" scoped>
.dimension-configuration {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .toolbar-card,
  .dimension-types-card {
    margin-bottom: 20px;
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.total-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.active-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.levels-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.relations-card {
        background: linear-gradient(135deg, #909399, #B3B6BC);
        color: white;
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 2px;
        }

        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }

        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
        }
      }

      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }
    }
  }

  .dimension-types-card {
    .dimension-type-item {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      }

      &.selected {
        border-color: #409EFF;
        box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
      }

      .dimension-type-icon {
        font-size: 32px;
        color: #409EFF;
        margin-bottom: 12px;
      }

      .dimension-type-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .dimension-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
        line-height: 1.4;
      }

      .dimension-type-stats {
        .usage-count {
          font-size: 11px;
          color: #909399;
          background: #F5F7FA;
          padding: 2px 8px;
          border-radius: 10px;
        }
      }
    }
  }

  .tree-card,
  .detail-card {
    height: 600px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        align-items: center;
        gap: 8px;
      }
    }
  }

  .tree-card {
    .tree-node {
      display: flex;
      align-items: center;
      flex: 1;
      justify-content: space-between;
      font-size: 14px;
      padding-right: 8px;

      .node-icon {
        margin-right: 8px;
        color: #409EFF;
      }

      .node-label {
        flex: 1;
      }

      .node-status {
        margin-left: 8px;
      }
    }
  }

  .detail-card {
    .dimension-detail {
      height: calc(100% - 60px);
      overflow-y: auto;

      .attribute-actions,
      .relation-actions {
        margin-top: 16px;
        text-align: center;
      }
    }

    .empty-state {
      height: calc(100% - 60px);
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .text-right {
    text-align: right;
  }
}
</style>
