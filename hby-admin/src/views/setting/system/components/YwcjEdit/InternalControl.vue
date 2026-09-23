<!--
 * @Date: 2022-04-29 11:07:16
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-06 16:08:33
 * @FilePath: /hb-admin/src/views/setting/system/components/YwcjEdit/InternalControl.vue
-->
<template>
  <div class="mt-30">
    <div class="mt-10">
      <el-button type="primary" @click="handleAdd">新建</el-button>
    </div>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column align="center" label="制度名称" prop="rulename">
        <template #default="{ row }">
          <el-link type="text" @click="handleDetail(row)">
            {{ row.rulename }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="发文文号" prop="rulenumber" />
      <el-table-column align="center" label="发文机构" prop="orgname" />
      <el-table-column align="center" label="发文日期" prop="publishdate" />
      <el-table-column align="center" label="操作" width="100">
        <template #default="{ row }">
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
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
    <InternalControlAdd ref="list" :cur-row="curRow" @fetch-data="fetchData" />
    <ControlDetail ref="detail" />
  </div>
</template>
<script>
  import InternalControlAdd from './InternalControlAdd.vue'
  import ControlDetail from './ControlDetail.vue'
  import { getInternalExternalList, deleteInternal } from '@/api/setting/system'

  export default {
    components: { InternalControlAdd, ControlDetail },
    props: {
      curRow: {
        type: Object,
        default: () => {},
      },
      fatherflowid: {
        type: [Number, String],
        default: undefined,
      },
    },
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        list: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        total: 0,
      }
    },
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const { code, data } = await getInternalExternalList({
          flowid: this.curRow.flowid,
        })
        this.listLoading = false
        if (code == 1) {
          const { innerrulePageInfo } = data
          const { tlist, totalRecord } = innerrulePageInfo
          this.list = tlist
          this.total = totalRecord
          // this.externalList = outPageInfo
        }
      },
      handleAdd() {
        this.$refs['list'].show()
      },
      handleDetail(row) {
        this.$refs['detail'].show(row)
      },
      async handleDelete(row) {
        const { code, msg } = await deleteInternal({
          flowid: this.curRow.flowid,
          innerid: row.innrulid,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
    },
  }
</script>
<style scoped>
  .mt-10 {
    margin-bottom: 10px;
  }
  .mt-30 {
    margin-bottom: 30px;
  }
</style>
