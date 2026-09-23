<!--
 * @Date: 2022-04-29 11:07:16
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-06 16:11:55
 * @FilePath: /hb-admin/src/views/setting/system/components/YwcjEdit/InternalControlAdd.vue
-->
<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <div class="mt-30">
      <el-button type="primary" @click="confirm">确 定</el-button>
    </div>
    <el-table :data="list" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column align="center" label="制度名称" prop="rulename" />
      <el-table-column align="center" label="发文文号" prop="rulenumber" />
      <el-table-column align="center" label="发文机构" prop="orgname" />
      <el-table-column align="center" label="发文日期" prop="publishdate" />
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
  import { addInternal, getInternalChooseList } from '@/api/setting/system'

  export default {
    props: {
      curRow: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        dialogFormVisible: false,
        title: '内规',
        layout: 'total, sizes, prev, pager, next, jumper',
        list: [],
        queryForm: {
          flowid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        total: 0,
        multipleSelection: [],
      }
    },
    methods: {
      show() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      async fetchData() {
        const { flowid } = this.curRow
        this.queryForm.flowid = flowid
        const {
          code,
          data: { tlist, totalRecord },
        } = await getInternalChooseList(this.queryForm)
        if (code == 1) {
          this.list = tlist
          this.total = totalRecord
        }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },

      async confirm() {
        const innerid = this.multipleSelection.map((item) => item.innrulid)
        const param = {
          flowid: this.curRow.flowid,
          innerid: innerid,
        }
        const { code, msg } = await addInternal(param)
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')
          this.close()
        }
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
