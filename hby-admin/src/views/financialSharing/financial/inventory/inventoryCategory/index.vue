<template>
  <div class="inventory-category-container">
    <div class="page-header">
      <h2>存货分类管理</h2>
      <p>管理存货分类体系，支持多级分类结构</p>
    </div>
    
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增分类</el-button>
      <el-button type="success" @click="handleExpandAll">展开全部</el-button>
      <el-button type="warning" @click="handleCollapseAll">收起全部</el-button>
    </div>

    <!-- 分类树表格 -->
    <div class="table-container">
      <el-table
        :data="treeData"
        v-loading="loading"
        row-key="categoryId"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        border
      >
        <el-table-column prop="categoryCode" label="分类编码" width="150" />
        <el-table-column prop="categoryName" label="分类名称" min-width="200" />
        <el-table-column prop="level" label="层级" width="80" align="center" />
        <el-table-column prop="inventoryCount" label="存货数量" width="100" align="right" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="updateTime" label="更新时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleAddChild(scope.row)">
              新增子类
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              class="danger-text"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="$refs.form && $refs.form.resetFields()"
    >
      <el-form
        ref="form"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-form-item label="分类编码" prop="categoryCode">
          <el-input v-model="formData.categoryCode" placeholder="请输入分类编码" />
        </el-form-item>
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="formData.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="计价方法">
          <el-select v-model="formData.pricingMethod" placeholder="请选择计价方法" clearable>
            <el-option label="移动平均法" :value="1" />
            <el-option label="先进先出法" :value="2" />
            <el-option label="加权平均法" :value="3" />
            <el-option label="个别计价法" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序号">
          <el-input-number v-model="formData.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCategoryTree,
  getCategoryDetail,
  addCategory,
  updateCategory,
  deleteCategory,
  batchDeleteCategory,
  updateCategorySort
} from '@/api/financialSharing/inventory'

export default {
  name: 'InventoryCategory',
  data() {
    return {
      loading: false,
      treeData: [],
      dialogVisible: false,
      dialogTitle: '',
      formData: {
        categoryId: null,
        categoryCode: '',
        categoryName: '',
        parentId: 0,
        categoryLevel: 1,
        pricingMethod: null,
        sortOrder: 0,
        status: 1,
        remark: ''
      },
      formRules: {
        categoryCode: [
          { required: true, message: '请输入分类编码', trigger: 'blur' }
        ],
        categoryName: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    // 转换字段名（后端大写转前端小驼峰）
    convertFields(item) {
      return {
        categoryId: item.CATEGORYID,
        categoryCode: item.CATEGORYCODE,
        categoryName: item.CATEGORYNAME,
        parentId: item.PARENTID,
        level: item.LEVEL,
        inventoryCount: item.INVENTORYCOUNT,
        status: item.STATUS,
        sort: item.SORT,
        updateTime: item.UPDATETIME
      }
    },

    // 构建树形结构
    buildTree(flatArray) {
      const map = {}
      const roots = []

      // 先转换所有字段并建立映射
      const converted = flatArray.map(item => this.convertFields(item))
      converted.forEach(item => {
        map[item.categoryId] = { ...item, children: [] }
      })

      // 构建树形结构
      converted.forEach(item => {
        if (item.parentId === '0') {
          roots.push(map[item.categoryId])
        } else if (map[item.parentId]) {
          map[item.parentId].children.push(map[item.categoryId])
        }
      })

      // 清理空的 children 数组
      const cleanEmptyChildren = (nodes) => {
        nodes.forEach(node => {
          if (node.children && node.children.length === 0) {
            delete node.children
          } else if (node.children) {
            cleanEmptyChildren(node.children)
          }
        })
      }
      cleanEmptyChildren(roots)

      return roots
    },

    async loadData() {
      this.loading = true
      try {
        const res = await getCategoryTree()
        if (res.code === 1) {
          // 将扁平数组转换为树形结构
          this.treeData = this.buildTree(res.data || [])
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    handleAdd() {
      this.dialogTitle = '新增分类'
      this.formData = {
        categoryId: null,
        categoryCode: '',
        categoryName: '',
        parentId: 0,
        categoryLevel: 1,
        pricingMethod: null,
        sortOrder: 0,
        status: 1,
        remark: ''
      }
      this.dialogVisible = true
    },

    handleAddChild(row) {
      this.dialogTitle = `新增"${row.categoryName}"子分类`
      this.formData = {
        categoryId: null,
        categoryCode: '',
        categoryName: '',
        parentId: row.categoryId,
        categoryLevel: row.level + 1,
        pricingMethod: null,
        sortOrder: 0,
        status: 1,
        remark: ''
      }
      this.dialogVisible = true
    },

    async handleEdit(row) {
      this.dialogTitle = `编辑"${row.categoryName}"`
      this.loading = true
      try {
        const res = await getCategoryDetail(row.categoryId)
        if (res.code === 1) {
          this.formData = { ...res.data }
          this.dialogVisible = true
        } else {
          this.$message.error(res.msg || '查询详情失败')
        }
      } catch (error) {
        this.$message.error('查询详情失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    handleDelete(row) {
      this.$confirm(`确定要删除"${row.categoryName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        this.loading = true
        try {
          const res = await deleteCategory(row.categoryId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            // 重新加载数据
            await this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        } finally {
          this.loading = false
        }
      }).catch(() => {})
    },

    async handleSave() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            const apiFunc = this.formData.categoryId ? updateCategory : addCategory
            const res = await apiFunc(this.formData)
            if (res.code === 1) {
              this.$message.success(this.formData.categoryId ? '更新成功' : '新增成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.msg || '保存失败')
            }
          } catch (error) {
            this.$message.error('保存失败：' + error.message)
          }
        }
      })
    },

    handleExpandAll() {
      // Element UI的树形表格默认展开所有节点需要通过ref操作
      this.$message.info('请点击每行前的展开图标查看子分类')
    },

    handleCollapseAll() {
      this.$message.info('请刷新页面收起所有分类')
    }
  }
}
</script>

<style lang="scss" scoped>
.inventory-category-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  
  h2 {
    margin: 0 0 8px 0;
    color: #303133;
  }
  
  p {
    margin: 0;
    color: #606266;
    font-size: 14px;
  }
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.danger-text {
  color: #f56c6c;
}
</style>
