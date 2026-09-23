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
      <el-table-column align="center" label="纠纷名称" prop="disputeitem" />
      <el-table-column align="center" label="纠纷类型" prop="disputetype" />
      <el-table-column align="center" label="诉讼阶段" prop="disputecours" />
      <el-table-column
        align="center"
        label="涉诉金额（万元）"
        prop="subjectamount"
      />
      <el-table-column align="center" label="立案时间" prop="casetime" />
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
  import { getlitigationSettlement } from '@/api/fwgl/legal'
  export default {
    name: 'GlzcOptions',
    data() {
      return {
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        listLoading: false,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          flowid: '698869',
          pageNumber: 1,
          pageSize: 10,
        },
      }
    },
    created() {},
    methods: {
      /**
       * @description: 打开弹窗
       * @return {*}
       */      
      show() {
        this.dialogFormVisible = true
        this.getXdf()
      },
      async getXdf() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getlitigationSettlement(this.queryForm)
        this.list = tlist.map((i) => {
          return {
            ...i,
            disputeItem: i.disputeitem,
          }
        })

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
