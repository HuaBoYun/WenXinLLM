<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
  >
    <div class="lr-layout">
      <!-- <div class="left">
        <el-tree
          :data="data"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div> -->
      <div class="right">
        <el-table v-loading="listLoading" :data="list">
          <el-table-column type="selection" width="55" />
          <el-table-column align="center" label="底稿编号" prop="data" />
          <el-table-column align="center" label="审计目标" prop="data" />
          <el-table-column align="center" label="被审计单位" prop="data" />
          <el-table-column align="center" label="拟稿人" prop="data" />
          <el-table-column align="center" label="拟稿日期" prop="data" />
        </el-table>
        <el-pagination
          background
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary">选 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { getList } from '@/api/systemLog'

  export default {
    name: 'Send2',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [
          {
            id: 1,
            label: '123',
            children: [],
          },
        ],
        title: '工作底稿',
        dialogFormVisible: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 10,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleNodeClick(data) {
        console.log(data)
      },
      showEdit() {
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style lang="scss" scoped>
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
  .el-table thead.is-group th.el-table__cell {
    background: #fff;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type:before {
    content: '日期';
    text-align: center;
    position: absolute;
    width: 152px;
    height: 1px;
    bottom: 30px;
    right: 0;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type:after {
    content: '配送新增';
    text-align: center;
    position: absolute;
    width: 152px;
    top: 10px;
    left: 0;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type .cell {
    position: absolute;
    top: 0;
    left: 0;
    width: 152px;
    height: 1px;
    background-color: #ebeef5;
    display: block;
    text-align: center;
    transform: rotate(38deg);
    transform-origin: top left;
    -ms-transform: rotate(38deg);
    -ms-transform-origin: top left;
    -webkit-transform: rotate(38deg);
    -webkit-transform-origin: top left;
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
