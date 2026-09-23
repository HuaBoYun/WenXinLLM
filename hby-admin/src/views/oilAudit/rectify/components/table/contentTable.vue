<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
  >
    <div>
      <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
      >
        <el-form-item>
          <el-input v-model="queryForm.code" clearable placeholder="问题编号" />
        </el-form-item>
        <!-- <el-form-item>
          <el-input
            v-model="queryForm.realname"
            clearable
            placeholder="发现人"
            :style="{ width: '256px' }"
          />
          <el-button
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="$refs.executor.show()"
          >
            选择
          </el-button>
        </el-form-item> -->
        <el-form-item>
          <el-button
            icon="el-icon-search"
            native-type="submit"
            type="primary"
            @click="getExecutorList"
          >
            查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button native-type="submit" type="primary" @click="resetSearch">
            重置
          </el-button>
        </el-form-item>
      </el-form>
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click.stop="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="项目编号" prop="code" />
        <el-table-column align="center" label="被审计单位" prop="company" />
        <el-table-column
          align="center"
          label="问题详情"
          prop="details"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="问题来源" prop="source" />
        <el-table-column align="center" label="发现人" prop="discoverer" />
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
    <executor-options ref="executor" @selected="handleExecutorSelected" />
  </el-dialog>
</template>
<script>
  import { getZgContentsList } from '@/oapi/audit/rectify'
  import ExecutorOptions from '../options/executor.vue'
  export default {
    name: 'contentTable',
    components: { ExecutorOptions },
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
          // realname: undefined,
          projectid: undefined,
          reporter: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
        tblReforms: [],
      }
    },
    created() {},
    methods: {
      handleExecutorSelected(node) {
        // this.queryForm.realname = node.realname
        this.queryForm.reporter = node.staffid
      },
      show(data) {
        //
        this.queryForm.projectid = data.projectid
        this.tblReforms = data.tblReforms
        this.dialogFormVisible = true
        this.getExecutorList()
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getZgContentsList(this.queryForm)

        if (this.tblReforms) {
          this.list = tlist.filter(
            (item) => !this.tblReforms.some((ele) => ele.code === item.code)
          )
        } else {
          this.list = tlist
        }

        this.total = totalRecord
        this.listLoading = false
      },
      resetSearch() {
        // this.resetQueryForm()
        this.queryForm.code = ''
        this.getExecutorList()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
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
        this.queryForm.pid = val.id
        this.getExecutorList()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      confirm() {
        if (this.multipleSelection.length <= 0) {
          this.$baseMessage(
            '请选择整改内容！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selected', this.multipleSelection)
        this.close()
      },
      close() {
        //
        this.queryForm = {}
        this.dialogFormVisible = false
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
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
