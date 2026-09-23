<template>
  <el-dialog
    :close-on-click-modal="false"
    title="选择现行标准"
    :visible.sync="dialogVisible"
    width="1200px"
    @close="close"
    append-to-body
  >
    <div class="selector-container">
      <!-- 搜索条件 -->
      <el-form :model="queryForm" :inline="true" @submit.native.prevent>
        <el-form-item>
          <el-input
            v-model="queryForm.ruleName"
            placeholder="请输入文件名称"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="queryForm.ruleNumber"
            placeholder="请输入发文文号"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="queryForm.summaryInfo"
            placeholder="请输入摘要内容"
            clearable
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <!-- 表格 -->
      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
        ref="table"
      >
        <el-table-column
          type="selection"
          width="55"
          :reserve-selection="true"
          :selectable="checkSelectable"
        />

        <el-table-column
          align="center"
          label="文件名称"
          prop="ruleName"
          width="200"
          show-overflow-tooltip
        />

        <el-table-column
          align="center"
          label="文件编号"
          prop="ruleCode"
          show-overflow-tooltip
        />

        <el-table-column
          align="center"
          label="发文文号"
          prop="ruleNumber"
          show-overflow-tooltip
        />

        <el-table-column
          align="center"
          label="发文部门"
          prop="publishOrg"
          show-overflow-tooltip
        />

        <el-table-column align="center" label="发文日期" prop="publishDate" />

        <el-table-column
          align="center"
          label="生效日期"
          prop="takeEffectTime"
        />

        <el-table-column align="center" label="时效性" prop="timeLiness" />

        <el-table-column
          align="center"
          label="摘要"
          prop="summaryInfo"
          show-overflow-tooltip
        />

        <el-table-column align="center" label="录入人" prop="enteringPerson" />

        <el-table-column align="center" label="录入时间" prop="createTime">
          <template #default="{ row }">
            {{ row.createTime ? row.createTime.split(' ')[0] : '' }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        background
        class="pager"
        :current-page="queryForm.pageNumber"
        layout="total, sizes, prev, pager, next, jumper"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>

    <template slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button
        type="primary"
        @click="confirm"
        :disabled="selectedItems.length === 0"
      >
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getList } from '@/api/internal/nowModo.js'

  export default {
    name: 'NowModoSelector',
    data() {
      return {
        dialogVisible: false,
        list: [],
        listLoading: false,
        total: 0,
        queryForm: {
          ruleName: '',
          ruleNumber: '',
          summaryInfo: '',
          pageNumber: 1,
          pageSize: 20,
        },
        selectedItems: [], // 跨页选中的项目
        currentPageSelection: [], // 当前页选中的项目
      }
    },
    methods: {
      show() {
        this.dialogVisible = true
        this.resetQuery()
        this.fetchData()
      },

      close() {
        this.dialogVisible = false
        this.selectedItems = []
        this.currentPageSelection = []
        this.$refs.table.clearSelection()
      },

      async fetchData() {
        this.listLoading = true
        try {
          const { data, code } = await getList({ ...this.queryForm })
          if (code === 1) {
            this.list = data.list || []
            this.total = data.total || 0

            // 恢复当前页的选中状态
            this.$nextTick(() => {
              this.restoreSelection()
            })
          } else {
            this.$message.error(data.msg || '获取数据失败')
            this.list = []
            this.total = 0
          }
        } catch (error) {
          console.error('获取现行标准列表失败:', error)
          this.$message.error('获取数据失败')
          this.list = []
          this.total = 0
        }
        this.listLoading = false
      },

      // 恢复当前页的选中状态
      restoreSelection() {
        this.list.forEach((row) => {
          const isSelected = this.selectedItems.some(
            (item) => item.id === row.id
          )
          if (isSelected) {
            this.$refs.table.toggleRowSelection(row, true)
          }
        })
      },

      // 检查行是否可选
      checkSelectable(row) {
        return true // 所有行都可选，可根据需要添加逻辑
      },

      // 处理选择变化
      handleSelectionChange(selection) {
        this.currentPageSelection = selection

        // 移除当前页之前选中的项目
        this.selectedItems = this.selectedItems.filter(
          (item) => !this.list.some((row) => row.id === item.id)
        )

        // 添加当前页新选中的项目
        this.selectedItems = [...this.selectedItems, ...selection]
      },

      // 清空选择
      clearSelection() {
        this.selectedItems = []
        this.currentPageSelection = []
        this.$refs.table.clearSelection()
      },

      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },

      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },



      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },

      resetQuery() {
        this.queryForm = {
          ruleName: '',
          ruleNumber: '',
          summaryInfo: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.selectedItems = []
        this.currentPageSelection = []
        this.fetchData()
      },

      confirm() {
        if (this.selectedItems.length === 0) {
          this.$message.warning('请至少选择一项')
          return
        }

        this.$emit('confirm', this.selectedItems)
        this.close()
      },
    },
  }
</script>

<style scoped>
  .selector-container {
    max-height: 600px;
  }

  .selected-info {
    margin-bottom: 10px;
    padding: 8px 12px;
    background-color: #f0f9ff;
    border: 1px solid #b3d8ff;
    border-radius: 4px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .pager {
    margin-top: 20px;
    text-align: right;
  }
</style>
