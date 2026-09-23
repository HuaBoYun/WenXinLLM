<template>
  <div>
    <el-dialog
      :visible.sync="dialogVisible"
      @close="beforeClose"
      width="1400px"
      :close-on-click-modal="false"
    >
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button
            @click="
              () => {
                this.$emit('close')
              }
            "
          >
            取 消
          </el-button>
          <el-button type="primary" @click="save">分 配</el-button>
          <el-button type="primary" @click="close">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-if="type == '31'"
        v-loading="listLoading"
        ref="multipleTable"
        :data="list"
        tooltip-effect="dark"
        @select="handleSelection"
        @select-all="handleSelectionAll"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          label="合同编号"
          prop="tblYqnsGcxmzj.htbh"
        ></el-table-column>
        <el-table-column
          prop="tblYqnsGcxmzj.gcmc"
          label="工程名称"
        ></el-table-column>
        <el-table-column
          prop="tblYqnsGcxmzj.sgdw"
          label="实施单位"
        ></el-table-column>

        <el-table-column
          prop="tblYqnsGcxmzj.esscje"
          label="金额"
        ></el-table-column>
        <el-table-column prop="rwnames" label="人员"></el-table-column>
      </el-table>
      <el-table
        v-else
        v-loading="listLoading"
        ref="multipleTable"
        :data="list"
        tooltip-effect="dark"
        @select="handleSelection"
        @select-all="handleSelectionAll"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="合同编号" prop="htbh"></el-table-column>
        <el-table-column prop="gchfymc" label="工程名称"></el-table-column>
        <el-table-column prop="ssdw" label="实施单位"></el-table-column>

        <el-table-column prop="htje" label="金额"></el-table-column>
        <el-table-column prop="rwnames" label="人员"></el-table-column>
      </el-table>
      <!-- <el-pagination
              background
              :current-page="queryForm.pageNumber"
              :layout="layout"
              :page-size="queryForm.pageSize"
              :total="total"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            /> -->
    </el-dialog>
    <task-allocation-child
      @getExecutorList="getExecutorList"
      ref="allocationChild"
    ></task-allocation-child>
  </div>
</template>
<script>
  import { getProjectRwList, getTreeTask } from '@/api/audit/project'
  import { getGcxmjsListByhz, getJsxmtzListByhz } from '@/api/audit/implement'
  import taskAllocationChild from './taskAllocationChild/taskAllocationChild.vue'
  export default {
    components: { taskAllocationChild },
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        rootId: '', //根节点ID
        dialogVisible: false,
        list: [],
        dataTree: [],
        rowData: {},
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        type: '31',
        projectId: '',
        row: null,
        ZSinfo: {},
      }
    },
    methods: {
      beforeClose() {
        this.$emit('close')
      },

      showEdit(row) {
        this.dialogVisible = true
        this.current = undefined
        this.row = row
        this.projectId = row.id
        this.ZSinfo = { name: row.zsname, staffid: row.zsstaffid }
        console.log('row,', row)
        if (row.xmqd.gljhxmlx == '31') {
          this.type = '31'
          this.get31()
        } else {
          this.type = '32'
          this.get32()
        }
      },
      get31() {
        this.listLoading = true
        getGcxmjsListByhz({ id: this.row.xmqd.gljhxmid }).then((res) => {
          if (res.code == 1) {
            this.list = res.data
          }
          this.listLoading = false
        })
      },
      get32() {
        this.listLoading = true
        getJsxmtzListByhz({ id: this.row.xmqd.gljhxmid }).then((res) => {
          if (res.code == 1) {
            this.list = res.data
          }
          this.listLoading = false
        })
      },
      getExecutorList(e) {
        if (e == '31') {
          this.type = '31'
          this.get31()
        } else {
          this.type = '32'
          this.get32()
        }
      },
      // getExecutorList() {
      //   this.listLoading = true
      //   let data = {
      //     projectId: this.projectId,
      //     targetId: this.targetId,
      //   }
      //   getProjectRwList({ ...data, ...this.queryForm }).then((res) => {
      //     if (res.code == 1) {
      //       this.list = res.data.pageInfo.tlist
      //       this.total = res.data.pageInfo.totalRecord
      //     }
      //     this.listLoading = false
      //   })
      // },
      // handleSizeChange(val) {
      //   this.queryForm.pageSize = val
      //   this.getExecutorList()
      // },
      // handleCurrentChange(val) {
      //   this.queryForm.pageNumber = val
      //   this.getExecutorList()
      // },
      handleNodeClick(val) {
        if (val.id === this.rootId) {
          this.targetId = undefined
        } else {
          this.targetId = val.id
        }
        // this.queryForm.nodeId = val.id
        // this.getExecutorTree()
        this.getExecutorList()
      },
      handleSelection(val) {
        this.current = val
        this.multipleSelection = val
      },
      handleSelectionAll(val) {
        //
        this.multipleSelection = val
      },
      save() {
        if (this.multipleSelection.length == 0) {
          this.$baseMessage('请选择！', 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['allocationChild'].showEdit(
          this.multipleSelection.map((item) => {
            return {
              ...item,
              programid:
                this.type == '31' ? item.gcxmzjzjbid : item.jsxmtzwcqkid,
            }
          }),
          this.projectId,
          this.type,
          this.ZSinfo
        )
      },
      close() {
        this.$emit('close')
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  // ::v-deep thead {
  //   .el-table-column--selection {
  //     .el-checkbox__inner {
  //       display: none !important;
  //     }
  //   }
  // }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
