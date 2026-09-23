<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <div class="system-log-container lr-layout">
      <div class="left">
        <flow-category-list @node-change="handleNodeChange" />
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button type="primary" @click="confirm">确定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          v-loading="listLoading"
          :data="list"
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            v-if="curNode && !curNode.children"
            key="selection"
            type="selection"
            width="55"
          />
          <el-table-column
            align="center"
            label="流程编号"
            prop="flownumber"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="流程名称"
            prop="flowname"
            show-overflow-tooltip
          />
          <el-table-column align="center" label="机构" prop="company" />
          <el-table-column
            align="center"
            label="主责部门"
            prop="deparChargeName"
          />
          <el-table-column align="center" label="创建时间" prop="createtime" />
          <el-table-column
            v-if="curNode && !curNode.children"
            align="center"
            label="流程状态"
            prop="firingStatus"
          >
            <template #default="{ row }">{{ mapFlowStatus(row) }}</template>
          </el-table-column>
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
  </el-dialog>
</template>

<script>
  import { getFlows } from '@/api/setting/system'
  import FlowCategoryList from '@/views/setting/system/components/FlowCategoryList'

  export default {
    name: 'FlowSelect',
    components: {
      FlowCategoryList,
    },
    data() {
      return {
        dialogVisible: false,
        title: '',
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          faflowid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        curNode: null,
        multipleSelection: [],
      }
    },
    created() {},
    methods: {
      show() {
        this.dialogVisible = true
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      mapFlowStatus(row) {
        const { firingStatus } = row
        if (!firingStatus) {
          return '未启用'
        }
        if (firingStatus == 1) {
          return '已启用'
        }
        if (firingStatus == 2) {
          return '已弃用'
        }
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getFlows(this.queryForm)
        this.listLoading = false
        this.list = tlist
        this.total = totalRecord
      },
      handleNodeChange(val) {
        this.curNode = val
        this.queryForm.faflowid = val.id
        this.fetchData()
      },
      handleSelectionChange(data) {
        this.multipleSelection = data
      },
      confirm() {
        this.$emit('select', this.multipleSelection)
        this.dialogVisible = false
      },
      close() {
        this.dialogVisible = false
      },
    },
  }
</script>
<style scoped>
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
