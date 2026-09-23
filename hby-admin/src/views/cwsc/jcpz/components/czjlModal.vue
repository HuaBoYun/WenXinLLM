<template>
  <div>
    <el-dialog
      :visible.sync="visible"
      title="操作记录"
      width="80%"
      :close-on-click-modal="false"
      @close="close"
      :append-to-body="true"
    >
      <div class="table-container">
        <div style="margin-bottom: 10px; display: flex">
          <el-input
            v-model="queryForm.recordname"
            placeholder="请输入采集名称"
            style="width: 300px; margin-right: 10px"
          />
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button type="primary" @click="reset">重置</el-button>
        </div>
        <el-table :data="tableData" style="width: 100%" height="400">
          <el-table-column label="主键" prop="recordid" />
          <el-table-column label="采集名称" prop="recordname" />
          <el-table-column label="开始时间" prop="startdate" />
          <el-table-column label="结束时间" prop="enddate" />
          <el-table-column label="采集类型" prop="recordtype">
            <template #default="{ row }">
              {{ row.recordtype == 1 ? '手动' : '自动' }}
            </template>
          </el-table-column>
          <el-table-column label="是否完成" prop="iscompleted">
            <template #default="{ row }">
              {{
                row.iscompleted == 0
                  ? '未开始'
                  : row.iscompleted == 1
                  ? '采集中'
                  : '已完成'
              }}
            </template>
          </el-table-column>
          <el-table-column label="采集结果" prop="isresult">
            <template #default="{ row }">
              {{ row.isresult == 1 ? '成功' : '失败' }}
            </template>
          </el-table-column>
          <el-table-column label="访问地址" prop="recordip" />
          <el-table-column label="创建人" prop="creatname" />
          <el-table-column align="center" label="操作" width="120">
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">结果</el-button>
            </template>
          </el-table-column>
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
      </div>
      <resultModal ref="result" />
    </el-dialog>
  </div>
</template>

<script>
  import { CaiJiLog } from '@/api/cwsc'
  import resultModal from './resultCW.vue'
  export default {
    components: {
      resultModal,
    },
    data() {
      return {
        visible: false,
        columns: [],
        tableData: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          tableId: '',
          recordname: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    methods: {
      show(result) {
        this.queryForm.tableId = result.fid
        this.fetchData()
        this.visible = true
      },
      fetchData() {
        CaiJiLog({
          ...this.queryForm,
        }).then((res) => {
          this.tableData = res.data.records
          this.total = res.data.total
        })
      },
      close() {
        this.visible = false
        this.tableData = []
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          recordname: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleDetail(row) {
        this.$refs.result.show(row)
      },
    },
  }
</script>

<style scoped></style>
