<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
  >
    <div class="lr-layout">
      <div class="left">
        <el-tree
          ref="tree"
          :check-strictly="true"
          :data="data"
          default-expand-all
          :expand-on-click-node="false"
          highlight-current
          node-key="id"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">设置人员</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @current-change="handleSelected"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column align="center" label="风险描述" prop="RISKTYPE" />
          <el-table-column align="center" label="检查方法" prop="CHECKMETHOD" />
          <el-table-column
            align="center"
            label="控制方法"
            prop="CONTROLMETHOD"
          />
          <el-table-column align="center" label="控制类型" prop="CONTROLTYPE" />
          <el-table-column align="center" label="控制频率" prop="CONTROLREQ" />
          <el-table-column align="center" label="测试人" prop="REALNAME" />
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
    <!-- <CompanySelectUserByTree
      ref="executor"
      @selected="handleExecutorSelected"
    /> -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
  </el-dialog>
</template>
<script>
  import ExecutorOptions from '@/components/danxuanPerson.vue'

  // import { findOrganizationByTreeAllss, user2list } from '@/api/audit/implement'
  // import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import { getLeftTree, getRighLlist, savePlanUser } from '@/api/internal/plan'
  export default {
    components: {
      // CompanySelectUserByTree,
      ExecutorOptions,
    },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
        queryForm: {
          planid: undefined,
          templId: undefined,
          node: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
        rowData: {},
      }
    },
    created() {},
    methods: {
      show(row) {
        this.queryForm.planid = row.testplanid
        this.queryForm.templId = row.testtemid
        this.multipleSelection = []
        this.dialogFormVisible = true
        this.getExecutorTree(row.testplanid)
        this.getExecutorList({ planid: row.testplanid, templId: row.testtemid })
        this.rowData = row
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      async getExecutorTree(planid) {
        const res = await getLeftTree({ planid })
        this.data = res.data.tree
      },
      async getExecutorList(params = {}) {
        this.listLoading = true
        const {
          data: {
            pageBean: { list, total },
          },
        } = await getRighLlist({ ...this.queryForm, ...params })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.node = val.id
        this.getExecutorList()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      confirm() {
        if (this.multipleSelection.length == 0) {
          this.$baseMessage(
            '请选择测试方案！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        console.log(
          '🚀 ~ confirm ~  this.$refs.executor:',
          this.$refs.executor.showEdit()
        )
        this.$refs.executor.showEdit(null, this.rowData.secrectLevelId)
        // this.$emit('selected', this.current)
        // this.dialogFormVisible = false
      },
      async handleExecutorSelected(node) {
        const { msg, code } = await savePlanUser({
          planid: this.queryForm.planid,
          userid: node[0].staffid,
          task: this.multipleSelection.map((item) => item.ELEMENTID).join(','),
        })
        if (code == 1) {
          this.$baseMessage('设置成功', 'success')
        }
        this.getExecutorList({
          planid: this.rowData.testplanid,
          templId: this.rowData.testtemid,
        })
      },
      close() {
        this.list = []
        this.total = 0
        this.dialogFormVisible = false
        this.$emit('fetch-data')
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 20%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 80%;
  }

  .left /deep/ .el-tree {
    overflow: auto;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
