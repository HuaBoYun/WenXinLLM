<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="18">
        <!-- <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.counterpartno"
              clearable
              placeholder="编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.budgetname"
              clearable
              placeholder="名称"
            />
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
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form> -->
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="6">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @current-change="handleSelected"
    >
      <template>
        <el-table-column align="center" label="合同编号" prop="contractno" />
        <el-table-column align="center" label="合同名称" prop="contractname" />
        <el-table-column
          align="center"
          label="印章名称"
          prop="counterpartcode"
        />
        <el-table-column
          align="center"
          label="印章所属主体"
          prop="counterparthank"
        />
      </template>
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
  </el-dialog>
</template>
<script>
  import { getSealedContractList } from '@/api/contract/manage'

  export default {
    name: 'XdfOptions',
    components: {},
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          recordtype: 'HTGL003',
          pageNumber: 1,
          pageSize: 20,
        },
        recordType: 'HTGL001', // 存储recordType，以防搜索重置时重置成相对方的搜索
      }
    },
    created() {},
    methods: {
      show(contracttype) {
        this.dialogFormVisible = true
        this.queryForm.contracttype = contracttype
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        // this.resetQueryForm()
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getSealedContractList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
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
      handleSelected(val) {
        this.current = val
        // this.$emit('selected', val, this.field)
        // this.dialogFormVisible = false
      },
      //选择的前置校验
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择！', 'error', 'vab-hey-message-error')
          return
        }

        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
    },
  }
</script>
