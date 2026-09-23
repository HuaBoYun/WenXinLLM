<template>
  <el-dialog
    title="评价计划"
    :visible.sync="dialogVisible"
    width="1000px"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="false"
    @close="close"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
          </vab-query-form>

          <el-table
            v-loading="listLoading"
            :data="list"
            ref="multipleTable"
            @select="handleSelection"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
              align="center"
              label="计划编号"
              prop="plannumber"
            />
            <el-table-column align="center" label="计划名称" prop="planname" />
            <el-table-column
              align="center"
              label="被测试公司"
              prop="testedorgs"
              width="340"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="计划开始时间"
              prop="starttime"
              show-overflow-tooltip
              :formatter="formatDate"
            />
            <el-table-column
              align="center"
              label="计划结束时间"
              prop="endtime"
              show-overflow-tooltip
              :formatter="formatDate"
            />
          </el-table>

          <el-pagination
            background
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import { getGroupPlanList } from '@/api/internal/plan'
  import { formatDay } from '@/utils/index'
  export default {
    data() {
      return {
        dialogVisible: false,
        dataTree: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
        listLoading: false,
        list: [],
      }
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      showEdit() {
        this.dialogVisible = true
        this.current = undefined
        this.getGroupPlanList()
      },
      save() {
        if (!this.current) {
          this.$baseMessage('请选择评价模板', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.multipleSelection)
        this.dialogVisible = false
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getGroupPlanList()
      },
      async getGroupPlanList() {
        this.listLoading = true
        const {
          data: {
            pageBean: { records, total },
          },
        } = await getGroupPlanList({
          ...this.queryForm,
        })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getGroupPlanList()
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
