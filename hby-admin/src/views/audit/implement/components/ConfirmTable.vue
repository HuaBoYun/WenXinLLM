<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1300px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :inline="true"
      label-width="0"
      :model="queryForm"
      @submit.native.prevent
    >
      <el-form-item>
        <el-input
          v-model="queryForm.businessAffiliation"
          clearable
          placeholder="审计事项"
        />
      </el-form-item>
      <el-form-item>
        <el-input
          :disabled="true"
          v-model="queryForm.findPeopleName"
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
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          native-type="submit"
          type="primary"
          @click="fetchData"
        >
          查询
        </el-button>
        <el-button
          icon="el-icon-search"
          native-type="submit"
          type="primary"
          @click="reset"
        >
          重置
        </el-button>
      </el-form-item>
      <el-form-item style="margin-left: 400px">
        <el-button type="primary" @click="confirm">确认</el-button>
      </el-form-item>
    </el-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="审计事项"
        prop="nbsjSheet.auditDiscoverable"
      />
      <el-table-column
        align="center"
        label="被审计对象"
        prop="nbsjSheet.organization.orgname"
      />
      <el-table-column
        align="center"
        label="审计发现"
        prop="nbsjSheet.auditDiscoverable"
      />
      <el-table-column align="center" label="关联工作底稿编号" prop="sheetId" />
      <el-table-column
        align="center"
        label="发现人"
        prop="nbsjSheet.createStaff.realname"
      />
      <el-table-column align="center" label="是否分组汇总" prop="groupStatus">
        <template #default="{ row }">
          {{ row.groupStatus == '1' ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否事实确认" prop="status">
        <template #default="{ row }">
          {{ row.status == '1' ? '否' : row.status == '2' ? '是' : '未确认' }}
        </template>
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
    <executor-options ref="executor" @selected="handleExecutorSelected" />
  </el-dialog>
</template>
<script>
  import { confirmationQuestionList } from '@/api/audit/implement'
  import ExecutorOptions from './options/executor.vue'
  export default {
    name: 'ConfirmTable',
    components: { ExecutorOptions },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        listLoading: false,
        list: [],
        dialogFormVisible: false,
        title: '审计发现',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          businessAffiliation: undefined,
          findPeople: undefined,
          findPeopleName: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleSelectionChange(e) {
        this.multipleSelection = e
      },
      reset() {
        this.queryForm = {
          businessAffiliation: undefined,
          findPeople: undefined,
          findPeopleName: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleExecutorSelected(node) {
        this.queryForm.findPeople = node.staffid
        this.queryForm.findPeopleName = node.realname
        // this.queryForm.staffid = node.staffid
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await confirmationQuestionList(this.queryForm)
        this.list = list
        this.list = list
        this.total = total
        this.listLoading = false
      },
      showEdit() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.tableData = []
        this.queryForm.solutionid = undefined
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      confirm() {
        if (!this.multipleSelection.length) {
          this.$baseMessage('请选择事项！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.multipleSelection)
        this.dialogFormVisible = false
      },
    },
  }
</script>

<style></style>
