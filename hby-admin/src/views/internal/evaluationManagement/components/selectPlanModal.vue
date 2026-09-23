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
              label="评价计划编号"
              prop="assessid"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="评价计划名称"
              prop="assessname"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="评价期限"
              prop="startdate"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{ row.startdate }} - {{ row.enddate }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="创建时间"
              prop="createtime"
              show-overflow-tooltip
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
  import { getEvaluationPlanList } from '@/api/internal/project'
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
      showEdit(secrectLevelId = '') {
        this.dialogVisible = true
        this.current = undefined
        this.getExecutorList(secrectLevelId)
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
        this.getExecutorList()
      },
      async getExecutorList(secrectLevelId = '') {
        this.listLoading = true
        const {
          data: {
            pageBean: { records, total },
          },
        } = await getEvaluationPlanList({
          status: 6,
          secrectLevelId,
          ...this.queryForm,
        })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
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

  // .lr-layout > .left {
  //   width: 200px;
  //   border-right: 1px solid ghostwhite;
  //   margin-right: 100px;
  //   padding-right: 10px;
  // }

  .lr-layout > .right {
    flex: 1;
  }
</style>
