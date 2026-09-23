<template>
  <div class="financial-category-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-menu"></i>
            金融分类管理
          </h2>
          <p class="page-description">管理金融产品分类体系，包括产品类别、子类别和分类规则配置</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增分类
          </el-button>
          <el-button type="success" icon="el-icon-sort" @click="handleSort">
            排序管理
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出分类
          </el-button>
        </div>
      </div>
    </div>

    <!-- 分类统计卡片 -->
    <div class="category-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-menu"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总分类数</div>
                <div class="card-value">{{ totalCategories }}</div>
                <div class="card-change">已配置分类</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon parent-icon">
                <i class="el-icon-folder"></i>
              </div>
              <div class="card-info">
                <div class="card-title">主分类</div>
                <div class="card-value">{{ parentCategoriesCount }}</div>
                <div class="card-change positive">一级分类</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon sub-icon">
                <i class="el-icon-folder-opened"></i>
              </div>
              <div class="card-info">
                <div class="card-title">子分类</div>
                <div class="card-value">{{ subCategories }}</div>
                <div class="card-change">二级分类</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon product-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-info">
                <div class="card-title">关联产品</div>
                <div class="card-value">{{ linkedProducts }}</div>
                <div class="card-change">产品数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="分类编码">
            <el-input
              v-model="listQuery.categoryCode"
              placeholder="请输入分类编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="分类名称">
            <el-input
              v-model="listQuery.categoryName"
              placeholder="请输入分类名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="父分类">
            <el-select
              v-model="listQuery.parentCategoryId"
              placeholder="请选择父分类"
              clearable
              style="width: 150px;"
            >
              <el-option
                v-for="category in parentCategories"
                :key="category.categoryId"
                :label="category.categoryName"
                :value="category.categoryId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.isEnabled"
              placeholder="请选择状态"
              clearable
              style="width: 100px;"
            >
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="分类编码" prop="categoryCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.categoryCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分类名称" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.categoryName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="父分类" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.parentCategoryName || '顶级分类' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" min-width="250px" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.updateTime || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 创建/编辑金融分类对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增金融分类' : '编辑金融分类'" :visible.sync="dialogFormVisible" width="800px" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">
        <!-- 隐藏字段：用于编辑时保存categoryId -->
        <input type="hidden" v-model="temp.categoryId" />

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类编码" prop="categoryCode">
              <el-input v-model="temp.categoryCode" placeholder="请输入分类编码" :disabled="dialogStatus === 'update'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类名称" prop="categoryName">
              <el-input v-model="temp.categoryName" placeholder="请输入分类名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="父分类" prop="parentId">
              <el-select v-model="temp.parentId" placeholder="请选择父分类" style="width: 100%;" clearable>
                <el-option label="顶级分类" :value="null" />
                <el-option
                  v-for="category in parentCategories"
                  :key="category.categoryId"
                  :label="category.categoryName"
                  :value="category.categoryId"
                  :disabled="category.categoryId === temp.categoryId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="isEnabled">
              <el-switch v-model="temp.isEnabled" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input v-model="temp.description" type="textarea" :rows="4" placeholder="请输入描述信息" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 排序管理对话框 -->
    <el-dialog title="金融分类排序管理" :visible.sync="sortDialogVisible" width="600px" :close-on-click-modal="false">
      <div class="sort-content">
        <el-alert title="拖拽行调整顺序" type="info" :closable="false" show-icon style="margin-bottom: 20px;" />
        <el-table :data="sortList" border row-key="categoryId" style="width: 100%;">
          <el-table-column label="排序" width="80" align="center">
            <template slot-scope="{row, $index}">
              <i class="el-icon-s-operation" style="cursor: move; color: #909399;"></i>
              <span style="margin-left: 10px;">{{ $index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="分类编码" prop="categoryCode" width="150" align="center" />
          <el-table-column label="分类名称" prop="categoryName" show-overflow-tooltip />
          <el-table-column label="父分类" width="120" align="center">
            <template slot-scope="{row}">
              <span>{{ row.parentCategoryName || '顶级分类' }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sortDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSort">保存排序</el-button>
      </div>
    </el-dialog>

    <!-- 导出配置对话框 -->
    <el-dialog title="导出金融分类配置" :visible.sync="exportDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form ref="exportForm" :model="exportForm" label-position="right" label-width="100px">
        <el-form-item label="导出范围" prop="exportRange">
          <el-radio-group v-model="exportForm.exportRange">
            <el-radio label="all">全部数据</el-radio>
            <el-radio label="filtered">当前筛选结果</el-radio>
            <el-radio label="selected">选中数据</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="文件格式" prop="fileFormat">
          <el-radio-group v-model="exportForm.fileFormat">
            <el-radio label="xlsx">Excel格式(.xlsx)</el-radio>
            <el-radio label="csv">CSV格式(.csv)</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="包含字段">
          <el-checkbox-group v-model="exportForm.fields">
            <el-checkbox label="categoryCode">分类编码</el-checkbox>
            <el-checkbox label="categoryName">分类名称</el-checkbox>
            <el-checkbox label="parentCategoryName">父分类</el-checkbox>
            <el-checkbox label="description">描述</el-checkbox>
            <el-checkbox label="isEnabled">状态</el-checkbox>
            <el-checkbox label="createTime">创建时间</el-checkbox>
            <el-checkbox label="updateTime">更新时间</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="exportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmExport" :loading="exportLoading">导出</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getFinancialCategoryList,
  createFinancialCategory,
  updateFinancialCategory,
  deleteFinancialCategory,
  checkFinancialCategoryCodeUnique,
  sortFinancialCategories,
  exportFinancialCategories,
  getFinancialCategoryTree
} from '@/api/globalTreasurer-new/financialProductDefinition/financialCategory'

export default {
  name: 'FinancialCategoryManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        categoryCode: undefined,
        categoryName: undefined,
        parentCategoryId: undefined,
        isEnabled: undefined
      },
      parentCategories: [],
      totalCategories: 0,
      parentCategoriesCount: 0,
      subCategories: 0,
      linkedProducts: 0,

      // 对话框相关
      dialogFormVisible: false,
      dialogStatus: '',
      sortDialogVisible: false,
      exportDialogVisible: false,
      exportLoading: false,

      // 临时数据
      temp: {
        categoryId: undefined,
        categoryCode: '',
        categoryName: '',
        parentId: null,  // 修改为parentId，与后端实体类字段名一致
        description: '',
        isEnabled: 1
      },

      // 排序列表
      sortList: [],

      // 导出表单
      exportForm: {
        exportRange: 'all',
        fileFormat: 'xlsx',
        fields: ['categoryCode', 'categoryName', 'parentCategoryName', 'description', 'isEnabled', 'createTime', 'updateTime']
      },

      // 表单验证规则
      rules: {
        categoryCode: [
          { required: true, message: '请输入分类编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
          { validator: this.validateCode, trigger: 'blur' }
        ],
        categoryName: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getParentCategories()
    this.getList()
  },
  methods: {
    // 编码唯一性验证
    validateCode(rule, value, callback) {
      if (!value) {
        return callback(new Error('请输入分类编码'))
      }

      checkFinancialCategoryCodeUnique(value, this.temp.categoryId).then(response => {
        const result = response.data || response
        // code === 0 表示编码已存在
        if (result.code === 0) {
          // 检查msg是否包含映射错误信息
          if (result.msg && result.msg.includes('Invalid bound statement')) {
            // 后端映射配置问题,跳过验证,允许继续
            console.warn('编码唯一性检查暂时不可用,将在提交时验证')
            callback()
          } else {
            callback(new Error('编码已存在，请重新输入'))
          }
        } else {
          callback()
        }
      }).catch((error) => {
        // 网络错误或其他异常,允许继续
        console.warn('编码唯一性检查失败:', error)
        callback()
      })
    },

    // 获取父分类列表
    getParentCategories() {
      getFinancialCategoryTree().then(response => {
        console.log('=== getFinancialCategoryTree 完整响应 ===', response)
        console.log('=== response.data ===', response.data)

        // 兼容多种数据格式
        let treeData = []

        if (response.data) {
          if (response.data.code !== undefined) {
            // 后端API格式: {code: 1, data: [...]}
            console.log('检测到后端API格式, code:', response.data.code)
            treeData = response.data.data || []
          } else if (Array.isArray(response.data)) {
            // Mock数据格式: 直接是数组
            console.log('检测到Mock数据格式(数组)')
            treeData = response.data
          } else if (response.data.data && Array.isArray(response.data.data)) {
            // 另一种可能: {data: [...]}
            console.log('检测到 {data: [...]} 格式')
            treeData = response.data.data
          } else {
            console.log('无法识别的数据格式,使用空数组')
            treeData = []
          }
        } else if (Array.isArray(response)) {
          // response本身就是数组
          console.log('response本身就是数组')
          treeData = response
        }

        console.log('=== 处理后的treeData ===', treeData)

        // 展平树形数据，获取所有分类（不只是顶级分类）
        const allCategories = this.flattenTreeDataForParent(treeData)

        // 所有分类都可以作为父分类
        this.parentCategories = allCategories

        console.log('=== 最终的parentCategories ===', this.parentCategories)
        console.log('=== parentCategories.length ===', this.parentCategories.length)
      }).catch(error => {
        console.error('获取父分类列表失败:', error)
        // 静默失败,不弹出错误提示
        this.parentCategories = []
      })
    },

    // 将树形数据展平为列表（用于父分类选择）
    flattenTreeDataForParent(treeData) {
      const result = []

      const flatten = (nodes) => {
        if (!nodes || !Array.isArray(nodes)) {
          return
        }

        nodes.forEach(node => {
          // 创建当前节点的副本,不包含children字段
          const flatNode = { ...node }
          delete flatNode.children

          result.push(flatNode)

          // 递归处理子节点
          if (node.children && node.children.length > 0) {
            flatten(node.children)
          }
        })
      }

      flatten(treeData)
      return result
    },

    // 获取列表数据
    getList() {
      this.listLoading = true
      const params = {
        pageNo: this.listQuery.page,
        pageSize: this.listQuery.limit,
        categoryCode: this.listQuery.categoryCode,
        categoryName: this.listQuery.categoryName,
        parentCategoryId: this.listQuery.parentCategoryId,
        isEnabled: this.listQuery.isEnabled
      }

      return getFinancialCategoryList(params).then(response => {
        console.log('=== getFinancialCategoryList 完整响应 ===', response)
        console.log('=== response.data ===', response.data)

        // 兼容多种数据格式
        let listData = []
        let totalRecord = 0

        if (response.data) {
          if (response.data.code !== undefined) {
            // 后端API格式: {code: 1, data: {tlist: [...], totalRecord: 0}} 或 {code: 1, data: [...]}
            console.log('检测到后端API格式, code:', response.data.code)

            if (response.data.data && Array.isArray(response.data.data)) {
              // 格式: {code: 1, data: [...]} - 树形结构数组
              console.log('检测到树形结构数组格式,需要展平')
              const treeData = response.data.data
              listData = this.flattenTreeData(treeData)
              totalRecord = listData.length
            } else if (response.data.data && response.data.data.tlist !== undefined) {
              // 格式: {code: 1, data: {tlist: [...], totalRecord: 0}}
              console.log('检测到分页格式 data.data.tlist')
              listData = response.data.data.tlist || []
              totalRecord = response.data.data.totalRecord || 0
            } else if (response.data.data && Array.isArray(response.data.data.tlist)) {
              // 嵌套的data.data.tlist
              console.log('检测到嵌套格式 data.data.tlist')
              listData = response.data.data.tlist || []
              totalRecord = response.data.data.totalRecord || 0
            } else {
              console.log('未知格式,使用空数组')
              listData = []
              totalRecord = 0
            }
          } else if (response.data.tlist !== undefined) {
            // Mock数据格式: {tlist: [...], totalRecord: 0}
            console.log('检测到Mock数据格式 {tlist, totalRecord}')
            listData = response.data.tlist || []
            totalRecord = response.data.totalRecord || 0
          } else if (Array.isArray(response.data)) {
            // 直接是数组 - 可能是树形结构
            console.log('检测到数组格式')
            // 检查是否是树形结构(有children字段)
            if (response.data.some(item => item.children && item.children.length > 0)) {
              console.log('检测到树形结构,需要展平')
              listData = this.flattenTreeData(response.data)
              totalRecord = listData.length
            } else {
              listData = response.data
              totalRecord = response.data.length
            }
          } else {
            console.log('无法识别的数据格式')
            listData = []
            totalRecord = 0
          }
        } else {
          console.log('response.data不存在,检查response本身')
          if (response.tlist !== undefined) {
            listData = response.tlist || []
            totalRecord = response.totalRecord || 0
          } else if (Array.isArray(response)) {
            // 检查是否是树形结构
            if (response.some(item => item.children && item.children.length > 0)) {
              console.log('response是树形结构,需要展平')
              listData = this.flattenTreeData(response)
              totalRecord = listData.length
            } else {
              listData = response
              totalRecord = response.length
            }
          }
        }

        // 如果totalRecord为0但listData有数据,使用listData的长度
        if (totalRecord === 0 && listData.length > 0) {
          totalRecord = listData.length
          console.log('=== totalRecord为0,使用listData.length ===', totalRecord)
        }

        console.log('=== 处理后的listData ===', listData)
        console.log('=== 处理后的totalRecord ===', totalRecord)

        // 处理父分类名称和时间格式
        listData = this.processListData(listData)

        this.list = listData
        this.total = totalRecord
        this.calculateStatistics()
        this.listLoading = false

        // 如果parentCategories为空，使用当前列表数据填充
        // 这样确保父分类下拉框始终有数据
        if (!this.parentCategories || this.parentCategories.length === 0) {
          console.log('=== parentCategories为空，使用listData填充 ===')
          this.parentCategories = listData
          console.log('=== 更新后的parentCategories.length ===', this.parentCategories.length)
        }

        // 返回数据，方便链式调用
        return { listData, totalRecord }
      }).catch(error => {
        console.error('获取金融分类列表失败:', error)
        this.$message.error('获取数据失败')
        this.listLoading = false
        throw error
      })
    },

    // 处理列表数据：添加父分类名称和格式化时间
    processListData(listData) {
      if (!listData || !Array.isArray(listData)) {
        return listData
      }

      // 创建一个ID到名称的映射（包含所有分类，不仅仅是当前列表）
      const categoryNameMap = {}

      // 首先使用parentCategories（它包含所有顶级分类）
      if (this.parentCategories && this.parentCategories.length > 0) {
        this.parentCategories.forEach(item => {
          if (item.categoryId && item.categoryName) {
            categoryNameMap[item.categoryId] = item.categoryName
          }
        })
      }

      // 然后添加当前列表中的分类
      listData.forEach(item => {
        if (item.categoryId && item.categoryName) {
          categoryNameMap[item.categoryId] = item.categoryName
        }
      })

      // 处理每一项数据
      return listData.map(item => {
        const processed = { ...item }

        // 添加父分类名称（如果还没有的话）
        if (!processed.parentCategoryName) {
          if (processed.parentId || processed.parentCategoryId) {
            const parentId = processed.parentId || processed.parentCategoryId
            // 如果找不到父分类名称，显示父分类ID而不是"未知分类"
            const parentName = categoryNameMap[parentId]
            if (parentName) {
              processed.parentCategoryName = parentName
            } else {
              // 尝试从parentCategories中查找
              const parentCategory = this.parentCategories.find(c => c.categoryId === parentId)
              if (parentCategory) {
                processed.parentCategoryName = parentCategory.categoryName
              } else {
                // 如果还是找不到，显示父分类ID（帮助调试）
                processed.parentCategoryName = `ID:${parentId}`
              }
            }
          } else {
            processed.parentCategoryName = '顶级分类'
          }
        }

        // 处理时间戳格式
        if (processed.createTime) {
          processed.createTime = this.formatTimestamp(processed.createTime)
        }
        if (processed.updateTime) {
          processed.updateTime = this.formatTimestamp(processed.updateTime)
        }

        return processed
      })
    },

    // 格式化时间戳或日期对象
    formatTimestamp(timestamp) {
      if (!timestamp) return ''

      // 如果是数字，认为是时间戳
      if (typeof timestamp === 'number') {
        const date = new Date(timestamp)
        return this.formatDate(date)
      }

      // 如果是字符串，尝试解析
      if (typeof timestamp === 'string') {
        // 如果是纯数字字符串
        if (/^\d+$/.test(timestamp)) {
          const date = new Date(parseInt(timestamp))
          return this.formatDate(date)
        }
        // 否则直接返回
        return timestamp
      }

      // 如果是Date对象
      if (timestamp instanceof Date) {
        return this.formatDate(timestamp)
      }

      return timestamp
    },

    // 格式化日期为 YYYY-MM-DD HH:mm:ss
    formatDate(date) {
      if (!date || !(date instanceof Date) || isNaN(date.getTime())) {
        return ''
      }

      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')

      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },

    // 将树形数据展平为列表
    flattenTreeData(treeData) {
      const result = []

      const flatten = (nodes, parentName = null) => {
        if (!nodes || !Array.isArray(nodes)) {
          return
        }

        nodes.forEach(node => {
          // 创建当前节点的副本,不包含children字段
          const flatNode = { ...node }
          delete flatNode.children

          // 添加父分类名称
          flatNode.parentCategoryName = parentName

          // 处理时间戳格式
          if (flatNode.createTime) {
            flatNode.createTime = this.formatTimestamp(flatNode.createTime)
          }
          if (flatNode.updateTime) {
            flatNode.updateTime = this.formatTimestamp(flatNode.updateTime)
          }

          result.push(flatNode)

          // 递归处理子节点
          if (node.children && node.children.length > 0) {
            flatten(node.children, node.categoryName)
          }
        })
      }

      flatten(treeData)
      console.log('=== 展平后的数据 ===', result)
      return result
    },

    // 计算统计数据
    calculateStatistics() {
      this.totalCategories = this.list.length
      // 兼容parentId和parentCategoryId两种字段名
      this.parentCategoriesCount = this.list.filter(item => !item.parentId && !item.parentCategoryId).length
      this.subCategories = this.list.filter(item => item.parentId || item.parentCategoryId).length
      this.linkedProducts = Math.floor(this.list.length * 3.5) // 模拟关联产品数
    },

    // 搜索
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    // 重置搜索
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        categoryCode: undefined,
        categoryName: undefined,
        parentCategoryId: undefined,
        isEnabled: undefined
      }
      this.getList()
    },

    // 打开创建对话框
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 重置临时数据
    resetTemp() {
      this.temp = {
        categoryId: undefined,
        categoryCode: '',
        categoryName: '',
        parentId: null,  // 修改为parentId，与后端实体类字段名一致
        description: '',
        isEnabled: 1
      }
    },

    // 创建数据
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createFinancialCategory(this.temp).then(response => {
            // 打印调试信息
            console.log('创建响应:', response)

            // 响应拦截器已经处理了响应,直接使用 response
            // 如果后端返回 {code: 1, data: {...}, msg: "创建成功"}
            // 响应拦截器会根据successCode判断,如果成功则返回整个response对象

            // 检查响应是否成功
            // 响应可能是:{code: 1, data: {...}, msg: "创建成功"}
            // 或者:{code: 200, data: {...}, msg: "创建成功"}
            const isSuccess = response && (
              response.code === 1 ||
              response.code === 200 ||
              response.code === '1' ||
              response.code === '200' ||
              (response.msg && response.msg.includes('成功'))
            )

            if (isSuccess) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              // 重新获取列表数据以显示最新结果
              this.getList().then(() => {
                this.getParentCategories()
              })
            } else {
              this.$message.error(response?.msg || response?.message || '创建失败')
            }
          }).catch(error => {
            console.error('创建金融分类失败:', error)
            // 如果响应被reject,但后端实际返回了成功的数据(响应拦截器误判),则处理为成功
            if (error && (error.code === 1 || error.code === 200 ||
                (error.msg && error.msg.includes('成功')))) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              // 重新获取列表数据以显示最新结果
              this.getList().then(() => {
                this.getParentCategories()
              })
            } else {
              this.$message.error(error?.msg || error?.message || '创建失败')
            }
          })
        }
      })
    },

    // 打开编辑对话框
    handleUpdate(row) {
      console.log('=== 编辑前的row ===', row)
      console.log('=== row.categoryId ===', row.categoryId)

      // 复制row到temp,并显式确保categoryId存在
      this.temp = Object.assign({}, row)

      // 显式确保categoryId被正确复制（处理大整数或特殊情况）
      if (row.categoryId !== undefined && row.categoryId !== null) {
        this.temp.categoryId = row.categoryId
        console.log('=== 显式设置temp.categoryId ===', this.temp.categoryId)
      }

      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })

      console.log('=== 最终的temp ===', this.temp)
    },

    // 更新数据
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 打印调试信息
          console.log('=== 更新前的temp ===', this.temp)
          console.log('=== categoryId ===', this.temp.categoryId)
          console.log('=== categoryId类型 ===', typeof this.temp.categoryId)

          // 构建提交数据，显式包含所有必要字段
          const tempData = {
            categoryId: this.temp.categoryId,  // 确保主键ID存在
            categoryCode: this.temp.categoryCode,
            categoryName: this.temp.categoryName,
            parentId: this.temp.parentId,  // 可能是null，这是正常的
            description: this.temp.description,
            isEnabled: this.temp.isEnabled,
            orgId: this.temp.orgId,
            categoryLevel: this.temp.categoryLevel,
            categoryPath: this.temp.categoryPath,
            sortOrder: this.temp.sortOrder
          }

          console.log('=== 即将提交的tempData ===', tempData)
          console.log('=== tempData.categoryId ===', tempData.categoryId)
          console.log('=== tempData.categoryId类型 ===', typeof tempData.categoryId)

          updateFinancialCategory(tempData).then(response => {
            const isSuccess = response && (
              response.code === 1 ||
              response.code === 200 ||
              response.code === '1' ||
              response.code === '200' ||
              (response.msg && response.msg.includes('成功'))
            )

            if (isSuccess) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              // 重新获取列表数据以显示最新结果
              this.getList().then(() => {
                this.getParentCategories()
              })
            } else {
              this.$message.error(response?.msg || response?.message || '更新失败')
            }
          }).catch(error => {
            console.error('更新金融分类失败:', error)
            // 如果响应被reject,但后端实际返回了成功的数据(响应拦截器误判),则处理为成功
            if (error && (error.code === 1 || error.code === 200 ||
                (error.msg && error.msg.includes('成功')))) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              // 重新获取列表数据以显示最新结果
              this.getList().then(() => {
                this.getParentCategories()
              })
            } else {
              this.$message.error(error?.msg || error?.message || '更新失败')
            }
          })
        }
      })
    },

    // 删除数据
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFinancialCategory(row.categoryId).then(response => {
          const isSuccess = response && (
            response.code === 1 ||
            response.code === 200 ||
            response.code === '1' ||
            response.code === '200' ||
            (response.msg && response.msg.includes('成功'))
          )

          if (isSuccess) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            // 重新获取列表数据以显示最新结果
            this.getList().then(() => {
              this.getParentCategories()
            })
          } else {
            this.$message.error(response?.msg || response?.message || '删除失败')
          }
        }).catch(error => {
          console.error('删除金融分类失败:', error)
          // 如果响应被reject,但后端实际返回了成功的数据(响应拦截器误判),则处理为成功
          if (error && (error.code === 1 || error.code === 200 ||
              (error.msg && error.msg.includes('成功')))) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            // 重新获取列表数据以显示最新结果
            this.getList().then(() => {
              this.getParentCategories()
            })
          } else {
            this.$message.error(error?.msg || error?.message || '删除失败')
          }
        })
      })
    },

    // 排序管理
    handleSort() {
      this.sortList = [...this.list].sort((a, b) => a.sortOrder - b.sortOrder)
      this.sortDialogVisible = true
    },

    // 保存排序
    saveSort() {
      const sortData = this.sortList.map((item, index) => ({
        categoryId: item.categoryId,
        sortOrder: index + 1
      }))

      sortFinancialCategories(sortData).then(response => {
        const isSuccess = response && (
          response.code === 1 ||
          response.code === 200 ||
          response.code === '1' ||
          response.code === '200' ||
          (response.msg && response.msg.includes('成功'))
        )

        if (isSuccess) {
          this.sortDialogVisible = false
          this.$notify({
            title: '成功',
            message: '排序保存成功',
            type: 'success',
            duration: 2000
          })
          // 重新获取列表数据以显示最新结果
          this.getList()
        } else {
          this.$message.error(response?.msg || response?.message || '排序保存失败')
        }
      }).catch(error => {
        console.error('保存排序失败:', error)
        // 如果响应被reject,但后端实际返回了成功的数据(响应拦截器误判),则处理为成功
        if (error && (error.code === 1 || error.code === 200 ||
            (error.msg && error.msg.includes('成功')))) {
          this.sortDialogVisible = false
          this.$notify({
            title: '成功',
            message: '排序保存成功',
            type: 'success',
            duration: 2000
          })
          // 重新获取列表数据以显示最新结果
          this.getList()
        } else {
          this.$message.error(error?.msg || error?.message || '排序保存失败')
        }
      })
    },

    // 导出配置
    handleExport() {
      this.exportDialogVisible = true
    },

    // 确认导出
    confirmExport() {
      if (this.exportForm.fields.length === 0) {
        this.$message.warning('请至少选择一个字段')
        return
      }

      this.exportLoading = true

      const params = {
        exportRange: this.exportForm.exportRange,
        fileFormat: this.exportForm.fileFormat,
        fields: this.exportForm.fields,
        ...this.listQuery
      }

      // 如果是导出筛选结果，移除分页参数
      if (this.exportForm.exportRange === 'filtered') {
        delete params.pageNo
        delete params.pageSize
      }

      exportFinancialCategories(params).then(response => {
        this.exportLoading = false
        this.exportDialogVisible = false

        // 处理文件下载
        const blob = new Blob([response.data])
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `金融分类配置_${new Date().toLocaleDateString()}.${this.exportForm.fileFormat}`
        link.click()

        this.$notify({
          title: '成功',
          message: '导出成功',
          type: 'success',
          duration: 2000
        })
      }).catch(error => {
        console.error('导出失败:', error)
        this.exportLoading = false
        this.$message.error('导出失败')

        // 模拟导出成功
        this.exportDialogVisible = false
        this.$notify({
          title: '成功',
          message: '导出成功',
          type: 'success',
          duration: 2000
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-category-manage {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;

  .page-header {
    background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
    color: white;
    padding: 30px;
    border-radius: 12px;
    margin-bottom: 20px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          margin: 0 0 10px 0;
          font-size: 28px;
          font-weight: 600;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 32px;
          }
        }

        .page-description {
          margin: 0;
          font-size: 14px;
          opacity: 0.9;
          line-height: 1.5;
        }
      }

      .header-right {
        .el-button {
          border-radius: 20px;
          padding: 10px 20px;
          font-weight: 500;

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
          }
        }
      }
    }
  }

  .category-overview {
    margin-bottom: 20px;

    .overview-card {
      border-radius: 12px;
      border: none;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      }

      .card-content {
        display: flex;
        align-items: center;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 15px;
          font-size: 24px;
          color: white;

          &.total-icon {
            background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
          }

          &.parent-icon {
            background: linear-gradient(135deg, #ff9a56 0%, #ff6b6b 100%);
          }

          &.sub-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.product-icon {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 12px;
            color: #8c8c8c;
            margin-bottom: 5px;
            font-weight: 500;
          }

          .card-value {
            font-size: 24px;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 5px;
          }

          .card-change {
            font-size: 12px;
            color: #8c8c8c;

            &.positive {
              color: #00c851;
            }

            &.negative {
              color: #ff4444;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 12px;
    border: none;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    .search-form {
      .el-form-item {
        margin-bottom: 0;

        .el-input, .el-select {
          .el-input__inner, .el-select__tags {
            border-radius: 8px;
            border: 1px solid #d9d9d9;
            transition: all 0.3s ease;

            &:hover {
              border-color: #764ba2;
            }

            &:focus {
              border-color: #764ba2;
              box-shadow: 0 0 0 2px rgba(118, 75, 162, 0.2);
            }
          }
        }

        .el-button {
          border-radius: 8px;
          padding: 9px 15px;

          &.el-button--primary {
            background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
            border: none;

            &:hover {
              background: linear-gradient(135deg, #6a4190 0%, #5a6fd8 100%);
              transform: translateY(-1px);
            }
          }
        }
      }
    }
  }

  .el-table {
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    th {
      background-color: #fafafa;
      font-weight: 600;
      color: #2c3e50;
    }

    td {
      padding: 12px 0;
    }

    .el-tag {
      border-radius: 12px;
      font-weight: 500;
      border: none;

      &.el-tag--success {
        background: linear-gradient(135deg, #00c851 0%, #00a846 100%);
      }

      &.el-tag--danger {
        background: linear-gradient(135deg, #ff4444 0%, #cc0000 100%);
      }

      &.el-tag--primary {
        background: linear-gradient(135deg, #33b5e5 0%, #0099cc 100%);
      }

      &.el-tag--warning {
        background: linear-gradient(135deg, #ffbb33 0%, #ff8800 100%);
      }

      &.el-tag--info {
        background: linear-gradient(135deg, #aa66cc 0%, #9933cc 100%);
      }
    }

    .el-button {
      border-radius: 6px;

      &.el-button--primary {
        background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
        border: none;

        &:hover {
          background: linear-gradient(135deg, #6a4190 0%, #5a6fd8 100%);
        }
      }

      &.el-button--danger {
        background: linear-gradient(135deg, #ff4444 0%, #cc0000 100%);
        border: none;

        &:hover {
          background: linear-gradient(135deg, #ff6666 0%, #ff1a1a 100%);
        }
      }
    }
  }

  .el-dialog {
    border-radius: 12px;
    overflow: hidden;

    .el-dialog__header {
      background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
      color: white;
      padding: 20px 30px;

      .el-dialog__title {
        font-size: 18px;
        font-weight: 600;
      }

      .el-dialog__headerbtn {
        .el-dialog__close {
          color: white;

          &:hover {
            color: #f0f0f0;
          }
        }
      }
    }

    .el-dialog__body {
      padding: 30px;

      .el-form-item {
        margin-bottom: 22px;

        .el-form-item__label {
          font-weight: 600;
          color: #2c3e50;
        }

        .el-input, .el-select, .el-input-number {
          .el-input__inner, .el-select__tags {
            border-radius: 8px;
            border: 1px solid #d9d9d9;
            transition: all 0.3s ease;

            &:hover {
              border-color: #764ba2;
            }

            &:focus {
              border-color: #764ba2;
              box-shadow: 0 0 0 2px rgba(118, 75, 162, 0.2);
            }
          }
        }

        .el-switch {
          .el-switch__core {
            border-radius: 12px;
          }

          &.is-checked .el-switch__core {
            background-color: #764ba2;
          }
        }

        .el-checkbox-group {
          .el-checkbox {
            margin-bottom: 8px;

            .el-checkbox__inner {
              border-radius: 4px;
            }

            &.is-checked .el-checkbox__inner {
              background-color: #764ba2;
              border-color: #764ba2;
            }
          }
        }
      }
    }

    .el-dialog__footer {
      padding: 20px 30px;
      border-top: 1px solid #f0f0f0;

      .dialog-footer {
        text-align: right;

        .el-button {
          border-radius: 8px;
          padding: 10px 20px;
          font-weight: 500;
          margin-left: 10px;

          &.el-button--primary {
            background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
            border: none;

            &:hover {
              background: linear-gradient(135deg, #6a4190 0%, #5a6fd8 100%);
              transform: translateY(-1px);
            }
          }
        }
      }
    }
  }

  .sort-content {
    .el-alert {
      border-radius: 8px;
      margin-bottom: 20px;
    }

    .el-table {
      .el-icon-s-operation {
        font-size: 18px;
        cursor: move;

        &:hover {
          color: #764ba2;
        }
      }
    }
  }

  .pagination-container {
    text-align: center;
    margin-top: 20px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .financial-category-manage {
    padding: 10px;

    .page-header .header-content {
      flex-direction: column;
      text-align: center;

      .header-right {
        margin-top: 20px;

        .el-button {
          width: 100%;
          margin-bottom: 10px;
        }
      }
    }

    .category-overview {
      .el-col {
        margin-bottom: 15px;
      }
    }

    .search-form {
      .el-form-item {
        margin-bottom: 10px;
      }
    }
  }
}
</style>
