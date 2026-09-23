<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form class="margin-b0">
      <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
      >
        <el-form-item>
          <el-date-picker
            v-model="queryForm.testYear"
            type="year"
            placeholder="年度"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="queryForm.oneprocess"
            clearable
            placeholder="一级流程"
            style="width: 140px; margin-right: 20px"
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
      </el-form>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="list"
      ref="multipleTable"
      tooltip-effect="dark"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="一级流程"
        prop="oneprocess"
        show-overflow-tooltip
      ></el-table-column>

      <el-table-column
        align="center"
        label="问题概述"
        prop="problemmemo"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="缺陷具体描述"
        prop="defectmemo"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="问题类别"
        prop="problemtype"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="缺陷等级"
        prop="defectlevel"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="定性依据"
        prop="quabasis"
        show-overflow-tooltip
      />
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
  </el-dialog>
</template>

<script>
  import { problemLedgerList } from '@/api/zgzz/index.js'
  export default {
    name: 'modal',
    components: {},
    data() {
      return {
        dialogFormVisible: false,
        title: '关联内控',
        queryForm: {
          testYear: '',
          oneprocess: '',
          defectlevel: '',
          problemtype: '',
          defectlevel: '',
          pageNumber: 1,
          pageSize: 10,
        },
        list: [],
        current: [],
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
      }
    },
    methods: {
      async showEdit() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      async fetchData() {
        console.log(111)
        const {
          data: { tlist, totalRecord },
        } = await problemLedgerList({
          ...this.queryForm,
          testYear: this.queryForm.testYear
            ? new Date(this.queryForm.testYear).getFullYear()
            : undefined,
          zfstatus: 1,
        })
        this.list = tlist
        this.total = totalRecord
      },
      resetSearch() {
        this.queryForm = {
          defectlevel: '',
          problemtype: '',
          defectlevel: '',
          testYear: '',
          pageNumber: 1,
          pageSize: 10,
        }
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },

      close() {
        this.dialogFormVisible = false
        this.formData = {}
        this.tableData = []
        this.footer = true
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
      },
      save() {
        if (this.current.length == 0) {
          this.$baseMessage('请选择内控！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selectList', this.current)
        this.dialogFormVisible = false
      },
    },
  }
</script>
