<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="项目"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-table
      v-loading="listLoading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="项目编号"
        prop="qdcode"
        width="100"
      >
        <!-- <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row, true)">
            {{ row.qdcode }}
          </el-button>
        </template> -->
      </el-table-column>
      <el-table-column align="center" label="项目名称" prop="projectName" />
      <el-table-column
        align="center"
        label="项目负责人"
        prop="projectOrderName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审计类型"
        prop="sjlxName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="计划开始时间"
        prop="planStarttime"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="计划结束时间"
        prop="planEndtime"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="计划年度"
        prop="planYear"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="项目状态">
        <template #default="{ row }">
          {{ row.status == 1 ? '启动' : row.status == 2 ? '实施' : '未启动' }}
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>
<script>
  import { getListcCompleted } from '@/oapi/audit/implement'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        multipleSelection: [],
      }
    },
    mounted() {},
    methods: {
      show() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      async fetchData() {
        const { data } = await getListcCompleted(this.queryForm)
        this.list = data.tlist
        this.total = data.totalRecord
        this.listLoading = false
      },
      handleSelectionChange(val) {
        console.log('🚀 ~ handleSelectionChange ~ val:', val)
        this.multipleSelection = val
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      save() {
        this.$emit('save', this.multipleSelection)
        this.dialogFormVisible = false
      },
      close() {
        this.multipleSelection = []
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style lang="scss" scoped></style>
