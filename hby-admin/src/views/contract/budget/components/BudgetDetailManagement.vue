<template>
  <el-dialog
    title="预算明细管理"
    :visible.sync="dialogVisible"
    width="95%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <div style="margin-bottom: 20px;">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">
        添加预算明细
      </el-button>
      <el-button type="success" size="small" @click="handleCalculateTotal">
        重新计算总预算
      </el-button>
    </div>

    <el-table :data="budgetDetailList" border style="width: 100%">
      <el-table-column label="预算科目" width="150">
        <template #default="{ row }">
          {{ row.costCategoryName || '未分类' }}
        </template>
      </el-table-column>
      <el-table-column label="预算项目" width="150">
        <template #default="{ row }">
          {{ row.costItem || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="预算描述" width="180">
        <template #default="{ row }">
          {{ row.costDescription || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="计量单位" prop="unit" width="80" />
      <el-table-column label="数量" prop="quantity" width="80">
        <template #default="{ row }">
          {{ row.quantity || 0 }}
        </template>
      </el-table-column>
      <el-table-column label="单价" prop="unitPrice" width="100">
        <template #default="{ row }">
          {{ row.unitPrice ? row.unitPrice.toFixed(2) : '0.00' }}
        </template>
      </el-table-column>
      <el-table-column label="小计" prop="subtotal" width="120">
        <template #default="{ row }">
          {{ row.subtotal ? row.subtotal.toFixed(2) : '0.00' }}
        </template>
      </el-table-column>
      <el-table-column label="备注" min-width="150">
        <template #default="{ row }">
          {{ row.remarks || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row, $index }">
          <el-button type="text" size="small" @click="handleEdit(row, $index)">编辑</el-button>
          <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete($index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 预算明细编辑弹窗 -->
    <el-dialog
      :title="editTitle"
      :visible.sync="editDialogVisible"
      width="60%"
      append-to-body
    >
      <el-form
        ref="editForm"
        :model="editForm"
        :rules="editRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算科目" prop="budgetSubject">
              <el-select v-model="editForm.budgetSubject" placeholder="请选择预算科目" style="width: 100%">
                <el-option label="人工费" value="人工费" />
                <el-option label="材料费" value="材料费" />
                <el-option label="机械费" value="机械费" />
                <el-option label="其他费用" value="其他费用" />
                <el-option label="管理费" value="管理费" />
                <el-option label="利润" value="利润" />
                <el-option label="税金" value="税金" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算项目" prop="budgetItem">
              <el-input v-model="editForm.budgetItem" placeholder="请输入预算项目" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算子项" prop="budgetSubItem">
              <el-input v-model="editForm.budgetSubItem" placeholder="请输入预算子项" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计量单位" prop="unit">
              <el-input v-model="editForm.unit" placeholder="请输入计量单位" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="数量" prop="quantity">
              <el-input-number
                v-model="editForm.quantity"
                :min="0"
                :precision="3"
                style="width: 100%"
                @change="calculateSubtotal"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单价" prop="unitPrice">
              <el-input-number
                v-model="editForm.unitPrice"
                :min="0"
                :precision="2"
                style="width: 100%"
                @change="calculateSubtotal"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="小计" prop="subtotal">
              <el-input-number
                v-model="editForm.subtotal"
                :min="0"
                :precision="2"
                style="width: 100%"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="预算说明" prop="budgetDescription">
          <el-input
            v-model="editForm.budgetDescription"
            type="textarea"
            :rows="2"
            placeholder="请输入预算说明"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="editForm.remarks"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveEdit">确定</el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <div style="text-align: left; margin-bottom: 10px;">
        <strong>预算汇总：{{ formatMoney(totalAmount) }}</strong>
      </div>
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveAll">保存全部</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    createBudgetDetail,
    getBudgetDetailList,
    updateBudgetDetail
  } from '@/api/contract/budget'

  export default {
    name: 'BudgetDetailManagement',
    data() {
      return {
        dialogVisible: false,
        editDialogVisible: false,
        editTitle: '',
        budgetInfo: {},
        budgetDetailList: [],
        editForm: {
          budgetId: null,
          budgetSubject: '',
          budgetItem: '',
          budgetSubItem: '',
          unit: '',
          quantity: 1,
          unitPrice: null,
          subtotal: null,
          budgetDescription: '',
          remarks: ''
        },
        editIndex: -1,
        editRules: {
          budgetSubject: [
            { required: true, message: '请选择预算科目', trigger: 'change' }
          ],
          budgetItem: [
            { required: true, message: '请输入预算项目', trigger: 'blur' }
          ],
          quantity: [
            { required: true, message: '请输入数量', trigger: 'blur' }
          ],
          unitPrice: [
            { required: true, message: '请输入单价', trigger: 'blur' }
          ]
        }
      }
    },
    computed: {
      totalAmount() {
        return this.budgetDetailList.reduce((sum, item) => {
          return sum + (item.subtotal || 0)
        }, 0)
      }
    },
    methods: {
      async showEdit(data) {
        this.dialogVisible = true
        this.budgetInfo = { ...data }
        await this.loadBudgetDetailList()
      },

      async loadBudgetDetailList() {
        try {
          const response = await getBudgetDetailList({
            budgetId: this.budgetInfo.id
          })
          console.log('预算明细列表响应:', response)
          if (response.code === 1) {  // 1表示成功
            this.budgetDetailList = response.data || []
          } else {
            this.$message.error(response.msg || '加载预算明细失败')
          }
        } catch (error) {
          console.error('加载预算明细失败：', error)
        }
      },
      
      handleClose() {
        this.dialogVisible = false
        this.budgetDetailList = []
        this.budgetInfo = {}
      },

      handleAdd() {
        this.editTitle = '添加预算明细'
        this.editIndex = -1
        this.resetEditForm()
        this.editDialogVisible = true
      },

      handleEdit(row, index) {
        this.editTitle = '编辑预算明细'
        this.editIndex = index

        // 将后端数据映射到前端表单字段
        this.editForm = {
          id: row.id,
          budgetId: row.budgetId,
          budgetSubject: this.getCostCategoryName(row.costCategory), // 根据costCategory获取科目名称
          budgetItem: row.costItem,
          budgetSubItem: row.costDescription,
          unit: row.unit,
          quantity: row.quantity,
          unitPrice: row.unitPrice,
          subtotal: row.subtotal,
          budgetDescription: row.costDescription,
          remarks: row.remarks
        }

        this.editDialogVisible = true
      },

      handleDelete(index) {
        this.$confirm('确定要删除这条预算明细吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.budgetDetailList.splice(index, 1)
          this.$message.success('删除成功')
        })
      },

      resetEditForm() {
        this.editForm = {
          budgetId: this.budgetInfo.id,
          budgetSubject: '',
          budgetItem: '',
          budgetSubItem: '',
          unit: '',
          quantity: 1,
          unitPrice: null,
          subtotal: null,
          budgetDescription: '',
          remarks: ''
        }
      },

      calculateSubtotal() {
        if (this.editForm.quantity && this.editForm.unitPrice) {
          this.editForm.subtotal = this.editForm.quantity * this.editForm.unitPrice
        } else {
          this.editForm.subtotal = null
        }
      },

      // 根据预算科目映射到成本类别
      getCostCategoryBySubject(budgetSubject) {
        const categoryMap = {
          '人工费': 1,
          '材料费': 2,
          '机械费': 3,
          '其他费用': 4,
          '管理费': 5,
          '利润': 6,
          '税金': 7
        }
        return categoryMap[budgetSubject] || 1 // 默认为人工费
      },

      getCostCategoryName(costCategory) {
        const nameMap = {
          1: '人工费',
          2: '材料费',
          3: '机械费',
          4: '其他费用',
          5: '管理费',
          6: '利润',
          7: '税金'
        }
        return nameMap[costCategory] || '人工费' // 默认为人工费
      },

      async handleSaveEdit() {
        try {
          await this.$refs.editForm.validate()

          // 准备数据，映射到前端显示字段
          const quantity = Number(this.editForm.quantity) || 1
          const unitPrice = Number(this.editForm.unitPrice) || 0
          const calculatedSubtotal = quantity * unitPrice

          const displayData = {
            // 前端显示字段
            budgetSubject: this.editForm.budgetSubject,
            budgetItem: this.editForm.budgetItem && this.editForm.budgetItem.trim() ? this.editForm.budgetItem.trim() : '预算项目',
            budgetDescription: this.editForm.budgetDescription || this.editForm.budgetSubItem || '预算描述',
            unit: this.editForm.unit || '个',
            quantity: quantity,
            unitPrice: unitPrice,
            subtotal: Number(this.editForm.subtotal) || calculatedSubtotal,
            remarks: this.editForm.remarks || '',

            // 后端映射字段（用于保存时转换）
            budgetId: this.editForm.budgetId || this.budgetInfo.id,
            costCategory: this.getCostCategoryBySubject(this.editForm.budgetSubject),
            costItem: this.editForm.budgetItem && this.editForm.budgetItem.trim() ? this.editForm.budgetItem.trim() : '预算项目',
            costDescription: this.editForm.budgetDescription || this.editForm.budgetSubItem || '预算描述',
            costCategoryName: this.editForm.budgetSubject, // 显示用的科目名称

            // 标记为未保存的新数据（没有id）
            isNew: this.editIndex === -1
          }

          console.log('准备的数据:', displayData)

          if (this.editIndex === -1) {
            // 添加 - 先添加到本地列表，不立即保存到数据库
            this.budgetDetailList.push(displayData)
            this.$message.success('添加到列表成功，请点击"保存全部"提交到数据库')
          } else {
            // 编辑 - 更新本地数组中的数据
            this.budgetDetailList.splice(this.editIndex, 1, { ...this.budgetDetailList[this.editIndex], ...displayData })
            this.$message.success('编辑成功')
          }

          this.editDialogVisible = false
        } catch (error) {
          console.error('保存失败：', error)
          this.$message.error('保存失败：' + (error.message || '未知错误'))
        }
      },

      handleCalculateTotal() {
        this.$message.success(`重新计算完成，总预算：${this.formatMoney(this.totalAmount)}`)
      },

      async handleSaveAll() {
        try {
          // 检查是否有未保存的新数据（没有id或标记为isNew的数据）
          const unsavedItems = this.budgetDetailList.filter(item => !item.id || item.isNew)

          console.log('当前预算明细列表:', this.budgetDetailList)
          console.log('过滤出的未保存数据:', unsavedItems)

          if (unsavedItems.length === 0) {
            this.$message.warning('没有需要保存的新数据')
            return
          }

          // 准备批量保存的数据（只保存新数据）
          const batchData = unsavedItems.map(item => ({
            budgetId: item.budgetId || this.budgetInfo.id,
            costCategory: item.costCategory || this.getCostCategoryBySubject(item.budgetSubject),
            costItem: item.costItem || (item.budgetItem && item.budgetItem.trim() ? item.budgetItem.trim() : '预算项目'),
            costDescription: item.costDescription || item.budgetDescription || item.budgetSubItem || '预算描述',
            unit: item.unit || '个',
            quantity: Number(item.quantity) || 1,
            unitPrice: Number(item.unitPrice) || 0,
            subtotal: Number(item.subtotal) || 0,
            costBasis: '预算明细',
            calculationMethod: '数量*单价',
            remarks: item.remarks || ''
          }))

          console.log('准备保存到后端的数据:', batchData)

          // 逐个保存新数据
          let successCount = 0
          for (const data of batchData) {
            try {
              console.log('正在保存数据:', data)
              const response = await createBudgetDetail(data)
              console.log('保存响应:', response)

              if (response.code === 1) {
                successCount++
              } else {
                console.error('保存失败:', response.msg)
                this.$message.error(`保存失败: ${response.msg}`)
              }
            } catch (error) {
              console.error('保存单个明细失败:', error)
              this.$message.error(`保存失败: ${error.message}`)
            }
          }

          if (successCount > 0) {
            this.$message.success(`成功保存 ${successCount} 条新预算明细`)
            await this.loadBudgetDetailList() // 重新加载列表
            this.handleClose()
          } else {
            this.$message.error('保存失败，请检查数据')
          }

        } catch (error) {
          console.error('批量保存失败：', error)
          this.$message.error('保存失败：' + (error.message || '未知错误'))
        }
      },

      formatMoney(amount) {
        if (!amount) return '0'
        return (amount / 10000).toFixed(2) + '万元'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
