<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="存货编码" prop="inventoryCode">
            <el-input v-model="formData.inventoryCode" :disabled="isViewMode" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="存货名称" prop="inventoryName">
            <el-input v-model="formData.inventoryName" :disabled="isViewMode" />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="存货分类" prop="categoryId">
            <el-select v-model="formData.categoryId" :disabled="isViewMode" style="width: 100%" filterable>
              <el-option
                v-for="category in categoryList"
                :key="category.categoryId"
                :label="category.displayName || category.categoryName"
                :value="category.categoryId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仓库" prop="warehouseId">
            <el-select v-model="formData.warehouseId" :disabled="isViewMode" style="width: 100%" filterable>
              <el-option
                v-for="warehouse in warehouseList"
                :key="warehouse.warehouseId"
                :label="warehouse.warehouseName"
                :value="warehouse.warehouseId"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计价方法" prop="pricingMethod">
            <el-select v-model="formData.pricingMethod" :disabled="isViewMode" style="width: 100%">
              <el-option label="移动平均法" :value="1" />
              <el-option label="先进先出法" :value="2" />
              <el-option label="加权平均法" :value="3" />
              <el-option label="个别计价法" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计量单位" prop="unit">
            <el-input v-model="formData.unit" :disabled="isViewMode" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="单位成本" prop="unitCost">
            <el-input-number
              v-model="formData.unitCost"
              :min="0"
              :precision="4"
              :disabled="isViewMode"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="当前库存" prop="currentQuantity">
            <el-input-number
              v-model="formData.currentQuantity"
              :min="0"
              :precision="2"
              :disabled="isViewMode"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="最低库存" prop="minQuantity">
            <el-input-number
              v-model="formData.minQuantity"
              :min="0"
              :precision="2"
              :disabled="isViewMode"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最高库存" prop="maxQuantity">
            <el-input-number
              v-model="formData.maxQuantity"
              :min="0"
              :precision="2"
              :disabled="isViewMode"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status" :disabled="isViewMode">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否批次管理" prop="batchManaged">
            <el-switch
              v-model="formData.batchManaged"
              :disabled="isViewMode"
              active-text="是"
              inactive-text="否"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          :disabled="isViewMode"
          placeholder="请输入备注信息"
        />
      </el-form-item>

      <!-- 计价方法说明 -->
      <el-form-item label="计价方法说明">
        <div class="pricing-method-info">
          <el-alert
            v-if="formData.pricingMethod === 1"
            title="移动平均法：每次收货后重新计算平均单价，发货时按最新平均单价计价"
            type="info"
            :closable="false"
            show-icon
          />
          <el-alert
            v-if="formData.pricingMethod === 2"
            title="先进先出法：按照先入库的存货先发出的原则计价"
            type="success"
            :closable="false"
            show-icon
          />
          <el-alert
            v-if="formData.pricingMethod === 3"
            title="加权平均法：按照期初存货和本期入库存货的加权平均单价计价"
            type="warning"
            :closable="false"
            show-icon
          />
          <el-alert
            v-if="formData.pricingMethod === 4"
            title="个别计价法：按照各批存货的实际成本计价"
            type="error"
            :closable="false"
            show-icon
          />
        </div>
      </el-form-item>

      <!-- 成本信息展示（查看模式） -->
      <div v-if="isViewMode && formData.valuationId">
        <el-divider content-position="left">成本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="库存总值">
              <span class="amount-display">{{ formatAmount(formData.totalValue) }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最后更新时间">
              <span>{{ formData.updateTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建时间">
              <span>{{ formData.createTime }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ isViewMode ? '关闭' : '取消' }}</el-button>
      <el-button v-if="!isViewMode" type="primary" @click="handleSave">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getInventoryCategoryTree, getWarehouseList } from '@/api/financialSharing/inventory'

export default {
  name: 'ValuationEditDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    isViewMode: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      categoryList: [],
      warehouseList: [],
      formRules: {
        inventoryCode: [
          { required: true, message: '请输入存货编码', trigger: 'blur' }
        ],
        inventoryName: [
          { required: true, message: '请输入存货名称', trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: '请选择存货分类', trigger: 'change' }
        ],
        warehouseId: [
          { required: true, message: '请选择仓库', trigger: 'change' }
        ],
        pricingMethod: [
          { required: true, message: '请选择计价方法', trigger: 'change' }
        ],
        unit: [
          { required: true, message: '请输入计量单位', trigger: 'blur' }
        ],
        unitCost: [
          { required: true, message: '请输入单位成本', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      if (this.isViewMode) return '查看计价方法'
      return this.formData.valuationId ? '编辑计价方法' : '新增计价方法'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.loadCategoryList()
        this.loadWarehouseList()
        this.$nextTick(() => {
          if (this.$refs.formRef) {
            this.$refs.formRef.clearValidate()
          }
        })
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    async loadCategoryList() {
      try {
        const response = await getInventoryCategoryTree()
        if (response.code === 1) {
          // 转换数据格式并添加层级显示
          const rawData = response.data || []
          this.categoryList = this.transformCategoryData(rawData)
        }
      } catch (error) {
        console.error('加载存货分类失败：', error)
      }
    },

    /**
     * 转换存货分类数据
     * 1. 将大写字段名转换为小写驼峰命名
     * 2. 根据层级添加缩进显示
     * 3. 构建树形结构并排序
     */
    transformCategoryData(rawData) {
      if (!rawData || rawData.length === 0) return []

      // 第一步: 转换字段名并创建映射
      const dataMap = {}
      const transformed = rawData.map(item => {
        const node = {
          categoryId: item.CATEGORYID,
          categoryCode: item.CATEGORYCODE,
          categoryName: item.CATEGORYNAME,
          parentId: item.PARENTID,
          level: item.LEVEL || 1,
          sort: item.SORT || 0,
          status: item.STATUS,
          inventoryCount: item.INVENTORYCOUNT,
          updateTime: item.UPDATETIME
        }
        dataMap[node.categoryId] = node
        return node
      })

      // 第二步: 构建树形结构
      const rootNodes = []
      const childrenMap = {}

      transformed.forEach(node => {
        if (node.parentId === '0' || !node.parentId) {
          rootNodes.push(node)
        } else {
          if (!childrenMap[node.parentId]) {
            childrenMap[node.parentId] = []
          }
          childrenMap[node.parentId].push(node)
        }
      })

      // 第三步: 递归构建扁平列表(保持树形顺序)
      const flatList = []
      const buildFlatList = (nodes, level = 1) => {
        // 按 sort 字段排序
        const sortedNodes = nodes.sort((a, b) => a.sort - b.sort)

        sortedNodes.forEach(node => {
          // 根据层级添加缩进前缀
          const indent = '　'.repeat(level - 1) // 使用全角空格
          const prefix = level > 1 ? '├─ ' : ''

          flatList.push({
            ...node,
            displayName: `${indent}${prefix}${node.categoryName}`,
            level: level
          })

          // 递归处理子节点
          const children = childrenMap[node.categoryId]
          if (children && children.length > 0) {
            buildFlatList(children, level + 1)
          }
        })
      }

      buildFlatList(rootNodes)
      return flatList
    },

    async loadWarehouseList() {
      try {
        const response = await getWarehouseList()
        if (response.code === 1) {
          this.warehouseList = response.data || []
        }
      } catch (error) {
        console.error('加载仓库列表失败：', error)
      }
    },

    handleSave() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          // 准备保存数据，自动填充分类名称和仓库名称
          const saveData = { ...this.formData }

          // 根据 categoryId 填充 categoryName
          if (saveData.categoryId) {
            const category = this.categoryList.find(c => c.categoryId === saveData.categoryId)
            if (category) {
              saveData.categoryName = category.categoryName || category.displayName
            }
          }

          // 根据 warehouseId 填充 warehouseName
          if (saveData.warehouseId) {
            const warehouse = this.warehouseList.find(w => w.warehouseId === saveData.warehouseId)
            if (warehouse) {
              saveData.warehouseName = warehouse.warehouseName
            }
          }

          this.$emit('save', saveData)
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    }
  }
}
</script>

<style lang="scss" scoped>
.pricing-method-info {
  margin-top: 10px;
}

.amount-display {
  color: #f56c6c;
  font-weight: 600;
  font-size: 16px;
}

.dialog-footer {
  text-align: right;
}
</style>
