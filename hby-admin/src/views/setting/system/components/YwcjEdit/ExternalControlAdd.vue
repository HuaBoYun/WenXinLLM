<!--
 * @Date: 2022-05-05 14:50:15
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-06 16:10:58
 * @FilePath: /hb-admin/src/views/setting/system/components/YwcjEdit/ExternalControlAdd.vue
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
      <el-table-column align="center" label="发文机构" prop="publishorg" />
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
  import { addExternal, getExternalChooseList } from '@/api/setting/system'
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
        title: '外规',
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
        } = await getExternalChooseList(this.queryForm)
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
        const outrulid = this.multipleSelection.map((item) => item.outrulid)
        const param = {
          flowid: this.curRow.flowid,
          outrulid: outrulid,
        }
        const { code, msg } = await addExternal(param)
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
