<template>
  <el-dialog
    :close-on-click-modal="false"
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @row-click="handleSelected"
    >
      <el-table-column align="center" label="纠纷主题" prop="disputeitem" />
      <el-table-column align="center" label="纠纷类型" prop="disputetype" />
      <el-table-column align="center" label="合同名称" prop="contractname" />
      <el-table-column align="center" label="合同编号" prop="contractno" />
      <el-table-column align="center" label="对方谈判人" prop="counterpart" />
      <el-table-column align="center" label="是否协商一致" prop="isaggree">
        <template slot-scope="scope">
          <span>{{ scope.row.isaggree === 1 ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建日期" prop="createtime" />
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
  import { findNegotiatedSettlemenA } from '@/api/fwgl/legal'
  export default {
    name: 'XdfOptions',
    data() {
      return {
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        listLoading: false,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          judicialsettlement: undefined,
          pageNumber: 1,
          pageSize: 10,
        },
      }
    },
    created() {},
    methods: {
      show(judicialsettlement) {
        this.dialogFormVisible = true
        this.queryForm.judicialsettlement = judicialsettlement
        this.getXdf()
      },
      async getXdf() {
        this.listLoading = true
        const {
          date: { tlist, totalRecord },
        } = await findNegotiatedSettlemenA(this.queryForm)

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
        this.getXdf()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getXdf()
      },
      /**
       * @description: 选择回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleSelected(val) {
        this.$emit('selected', val)
        this.dialogFormVisible = false
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {},
    },
  }
</script>
