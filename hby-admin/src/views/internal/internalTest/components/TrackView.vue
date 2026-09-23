<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
  >
    <div class="lr-layout">
      <div class="left">
        <el-tree
          :data="data"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div>
      <div class="right">
        <el-table
          v-loading="listLoading"
          :data="list"
          @select-all="selectAll"
          @select="selectAnalysis"
          ref="multipleTable"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column align="center" label="编号" prop="ELEMENTCODE">
            <template #default="{ row }">
              <el-button
                style="color: red"
                type="text"
                @click="handleShowView(row)"
              >
                {{ row.ELEMENTCODE }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="业务描述"
            prop="BUSINESSDESC"
          />
          <el-table-column align="center" label="风险描述" prop="RISKTYPE" />
          <el-table-column align="center" label="检查方法" prop="CHECKMETHOD" />
          <el-table-column
            align="center"
            label="控制目标"
            prop="CONTROLTARGET"
          />
          <el-table-column
            align="center"
            label="控制方法"
            prop="CONTROLMETHOD"
          />
          <el-table-column align="center" label="控制类型" prop="CONTROLTYPE" />
          <el-table-column align="center" label="状态" prop="STA" />
          <el-table-column align="center" label="是否提交">
            <template #default="{ row }">
              {{ row.COMPLETESTAUS == 1 ? '已提交' : '未提交' }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="测试人" prop="REALNAME" />
        </el-table>
        <el-pagination
          background
          :current-page="queryForm.pageNumber"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <TaskInfo ref="TaskInfo" />
    <TaskForm ref="edit" />
    <ProcessList ref="process" @fetchData="fetchData" />
  </el-dialog>
</template>
<script>
  // import { getList } from '@/api/systemLog'
  import { getLeftTree, getDefgzTree, saveall } from '@/api/internal/tack'
  // import { doDelete } from '@/api/table'
  import TaskInfo from '@/views/internal/internalTest/components/TaskInfo.vue'
  import TaskForm from '@/views/internal/internalTest/components/TaskForm.vue'
  import SendBtn from '@/views/internal/components/SendBtn.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  export default {
    name: 'TaskView',
    components: { TaskInfo, SendBtn, TaskForm, ProcessList },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name',
        },
        data: [
          {
            id: 1,
            name: '12.27csmb-1',
            children: [],
          },
        ],
        title: '测试跟踪',
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          node: '',
          planid: '',
          templId: '',
          pageNumber: 1,
          pageSize: 20,
        },
        dialogFormVisible: false,
        current: '',
        planid: '',
        templId: '',
      }
    },
    created() {
      // this.fetchData()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.queryData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(41, row.TESTTASKID)
        // this.$refs['process'].save(42, row.TESTTASKID)
      },
      selectAnalysis(selection, row) {
        if (selection.length > 0) {
          this.current = row
        } else {
          this.current = ''
        }

        if (selection.length > 1) {
          let del_row = selection.shift()
          this.$refs.multipleTable.toggleRowSelection(del_row, false)
          // 用于多选表格，切换某一行的选中状态，如果使用了第二个参数，则是设置这一行选中与否（selected 为 true 则选中）
        }
      },
      selectAll() {
        this.$refs.multipleTable.clearSelection()
      },

      handleNodeClick(data) {
        this.queryForm.node = data.id
        // console.log(data)
        this.queryData()
      },
      showEdit(row) {
        // console.log(row)
        this.queryForm.planid = row.testplanid
        this.queryForm.templId = row.testtemid
        this.getExecutorTree(row.testplanid)
        this.queryData({ planid: row.testplanid, templId: row.testtemid })
        this.dialogFormVisible = true
      },
      async getExecutorTree(testplanid) {
        const res = await getLeftTree({ planid: testplanid })
        this.data = res.data.tree
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData(params) {
        this.queryForm.pageNumber = 1
        this.fetchData(params)
      },
      async fetchData(params = {}) {
        this.listLoading = true
        const {
          data: {
            pageBean: { list, total },
            planid,
            templId,
          },
        } = await getDefgzTree({ ...this.queryForm, ...params })
        this.list = list
        this.total = total
        this.planid = planid
        this.templId = templId
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handlePreview() {},
      handleReport() {},
      handleExport() {},
      handleEdit() {
        if (this.current == '') {
          this.$baseMessage('请选择一条数据', 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['edit'].showEdit(
          this.current,
          'edit',
          this.planid,
          this.templId,
          this.queryForm.node
        )
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      showModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      close() {
        this.list = []
        this.queryForm.node = ''
        this.dialogFormVisible = false
      },
      handleShowView(row) {
        this.$refs['edit'].showEdit(
          row,
          'view',
          this.planid,
          this.templId,
          this.queryForm.node
        )
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
