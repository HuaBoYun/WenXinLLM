<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <div class="">
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
        <vab-query-form-left-panel>
          <el-input
            v-model="queryForm.projectName"
            placeholder="请输入项目名称"
            clearable
            style="width: 50%; margin-right: 10px"
          />
          <el-button
            type="primary"
            @click="getExecutorList"
            style="margin-top: 10px !important"
          >
            查询
          </el-button>
          <el-button
            type="primary"
            @click="reset"
            style="margin-top: 10px !important"
          >
            重置
          </el-button>
        </vab-query-form-left-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        @current-change="handleSelected"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" label="项目编号" prop="qdcode" />
        <el-table-column align="center" label="项目名称" prop="projectName" />
        <el-table-column
          align="center"
          label="项目负责人"
          prop="projectOrderName"
        />
        <el-table-column align="center" label="审计类型" prop="sjlxName" />
        <el-table-column
          align="center"
          label="计划开始时间"
          prop="planStarttime"
        />
        <el-table-column
          align="center"
          label="计划结束时间"
          prop="planEndtime"
        />
        <el-table-column align="center" label="计划年度" prop="planYear" />
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
  </el-dialog>
</template>
<script>
  import { projectList } from '@/oapi/audit/xmpy'
  export default {
    name: 'ExecutorOptions',
    props: {
      isCheckout: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        data: [],
        queryForm: {
          projectName: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
      }
    },
    created() {},
    methods: {
      // 列表多选
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      show() {
        this.current = undefined
        this.dialogFormVisible = true
        this.getExecutorList()
      },

      async getExecutorList() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await projectList(this.queryForm)
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
        this.queryForm.orgid = val.id
        this.getExecutorList()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择项目！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
        this.queryForm = {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      reset() {
        this.queryForm.projectName = ''
        this.getExecutorList()
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
</style>
