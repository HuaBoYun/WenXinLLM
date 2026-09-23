<template>
  <el-dialog
    :append-to-body="true"
    title="账套列表"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.bookName"
              clearable
              placeholder="名称"
            />
          </el-form-item>
          <el-button
            icon="el-icon-search"
            native-type="submit"
            type="primary"
            @click="fetchData"
          >
            查询
          </el-button>
        </el-form>
      </vab-query-form-left-panel>
    </vab-query-form>
    <el-table
      ref="multipleTable"
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @select-all="selectAll"
      @select="handleSelectionChange"
      @current-change="handleRowChange"
    >
      <!-- @selection-change="handleSelectionChange" -->
      <el-table-column type="selection" />
      <el-table-column align="center" label="ID" prop="bookId" />
      <el-table-column align="center" label="名称" prop="bookName" />
      <el-table-column align="center" label="公司" prop="orgName" />
      <el-table-column align="center" label="年份" prop="bookYear" />
      <el-table-column align="center" label="描述" prop="boodDesc" />
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    getList,
    checkBook,
    getSelectedBookInfo,
  } from '@/api/workbench/accountManage'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        curUserSelected: {},
        curSelected: {},
        queryForm: {
          bookName: undefined,
          pageNo: 1,
          pageSize: 20,
        },
      }
    },
    mounted() {},
    methods: {
      async showEdit() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        await this.getSelectedBook()
        this.listLoading = false
      },
      async getSelectedBook() {
        const { data } = await getSelectedBookInfo()
        if (data) {
          const row = this.list.find((x) => x.bookId === data.bookId)
          this.curUserSelected = row
          localStorage.setItem('bookInfo', JSON.stringify(data))
          this.$nextTick(() => {
            this.singleSelect(row)
          })
        }
      },
      selectAll() {
        this.singleSelect(this.curUserSelected)
      },
      handleSelectionChange(selection, row) {
        this.singleSelect(row)
      },
      handleRowChange(row) {
        this.singleSelect(row)
      },
      singleSelect(row) {
        this.curSelected = row
        this.$refs.multipleTable.clearSelection()
        this.$refs.multipleTable.toggleRowSelection(row)
      },
      async save() {
        if (!this.curSelected.bookId)
          return this.$message({ type: 'error', message: '请选择要操作的账套' })
        if (this.curSelected.bookId === this.curUserSelected?.bookId)
          return this.$message({ type: 'error', message: '无更改' })

        this.listLoading = true
        const { code } = await checkBook({
          bookId: this.curSelected.bookId,
        })
        if (code === 1) {
          this.$message({
            type: 'success',
            message: '操作成功!',
          })
          await this.getSelectedBook()
          this.close()
          this.$emit('fetch-data')
        } else {
          this.$message({
            type: 'error',
            message: '操作失败!',
          })
        }
        this.listLoading = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
