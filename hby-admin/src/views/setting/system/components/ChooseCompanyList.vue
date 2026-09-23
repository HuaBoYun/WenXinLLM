<template>
  <el-dialog
    title="选择公司"
    :close-on-click-modal="false"
    :visible.sync="dialogVisible"
    append-to-body
    width="60%"
    :modal="modal"
    @close="close"
    v-if="dialogVisible"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="18">
        <el-form ref="form" :model="queryForm" label-width="0" :inline="true">
          <el-form-item>
            <el-input
              v-model="queryForm.orgname"
              placeholder="请输入公司名称"
              clearable
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.orgnumber"
              placeholder="请输入公司编号"
              clearable
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="fetchData">查询</el-button>
            <!-- 重置 -->
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
    </vab-query-form>

    <el-table
      :data="list"
      ref="multipleTable"
      highlight-current-row
      @selection-change="handleSelectionChange"
    >
      >
      <el-table-column
        type="selection"
        width="55"
        :selectable="checkSelectable"
      ></el-table-column>
      <el-table-column
        label="序号"
        align="center"
        prop="orgId"
      ></el-table-column>
      <el-table-column
        align="center"
        label="公司名称"
        prop="orgname"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="公司编号"
        prop="orgnumber"
      ></el-table-column>
      <!-- <el-table-column align="center" label="状态" prop="qyStats">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.qyStats == '1'" type="success">启用</el-tag>
          <el-tag v-else type="danger">未启用</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            @click="handleChangeStatus(scope.row)"
          >
            {{ scope.row.qyStats == 1 ? '弃用' : '启用' }}
          </el-button>
          <el-button type="text" size="small" @click="handleCopy(scope.row)">
            复制
          </el-button>
          <el-button type="text" size="small" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button type="text" size="small" @click="handleDelete(scope.row)">
            删除
          </el-button>
          <el-button type="text" size="small" @click="handleDelete(scope.row)">
            流程分配
          </el-button>
          <el-button type="text" size="small" @click="handleDelete(scope.row)">
            取消分配
          </el-button>
        </template>
      </el-table-column> -->
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.currentPage"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import {
    getCompanyList,
    saveOrgWorkFlowInfo,
    getCheckedCompanyList,
    delOrgWorkFlowInfo,
  } from '@/api/setting/system'
  import store from '@/store'

  export default {
    props: {
      modal: {
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        dialogVisible: false,
        type: false,
        flowId: '',
        tableId: '',
        list: [],
        total: 0,
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          flowId: '',
          tableId: '',
          orgname: '',
          orgnumber: '',
          currentPage: 1,
          pageSize: 20,
        },
        multipleSelection: [],
      }
    },
    methods: {
      async save() {
        if (!this.multipleSelection.length)
          return this.$message.warning('请选择公司')

        console.log(this.multipleSelection)
        const current = this.multipleSelection.map((ele) => {
          return ele.orgId
        })

        let param = {
          flowId: this.flowId,
          tableId: this.tableId,
          orgIdStrs: current.join(','),
        }
        const { data, code } = this.type
          ? await saveOrgWorkFlowInfo(param)
          : await delOrgWorkFlowInfo(param)

        if (code == 1) {
          this.$message.success('提交成功')
          this.fetchData()
        }
      },
      resetQueryForm() {
        this.queryForm.currentPage = 1
        this.queryForm.orgname = ''
        this.queryForm.orgnumber = ''
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.currentPage = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.currentPage = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = this.type
          ? await getCompanyList(this.queryForm)
          : await getCheckedCompanyList(this.queryForm)

        this.list = list

        if (this.type) {
          const arr = []
          this.$nextTick(() => {
            list.forEach((row) => {
              if (row.ischecked > 0) {
                arr.push(row.orgId)
                this.$refs.multipleTable.toggleRowSelection(row, true)
              }
            })
          })
        }
        this.total = total
        this.listLoading = false
      },
      async show(e, type) {
        this.dialogVisible = true
        this.type = type
        this.tableId = e.tableId
        this.flowId = e.ymWorkFrom
        this.queryForm.tableId = e.tableId
        this.queryForm.flowId = e.ymWorkFrom
        this.fetchData()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      close() {
        this.multipleSelection = []
        this.list = []
        this.dialogVisible = false
        this.queryForm = this.$options.data().queryForm
        this.$emit('fetchData')
        this.$bus.$emit('updateMsg', 0)
      },
      checkSelectable(row) {
        if (!this.type) return true
        return row.ischecked == 0
      },
    },
  }
</script>

<style lang="scss" scoped></style>
