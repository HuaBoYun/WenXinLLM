<template>
  <el-dialog
    :visible.sync="dialogFormVisible"
    :title="title"
    width="1000px"
    style="width: 100%"
    :close-on-click-modal="false"
    append-to-body
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="list"
      v-loading="listLoading"
      ref="multipleTable"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column align="center" label="模型编号" prop="stepno" />
      <el-table-column align="center" label="模型名称" prop="steptitle" />
      <el-table-column align="center" label="关联数据源" prop="stepcontent" />
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
  import { querySendRiskModelListByStaff } from '@/api/risk'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '',
        list: [],
        listLoading: false,
        queryForm: {
          stepno: '',
          steptitle: '',
          stepcontent: '',
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
      }
    },
    methods: {
      showEdit() {
        this.title = '关联风险模型'
        this.fetchData()
        this.dialogFormVisible = true
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await querySendRiskModelListByStaff(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.select = val
      },
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
      save() {
        if (!this.select.length) {
          this.$baseMessage(
            '请选择风险模型！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selected', this.select)
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped></style>
