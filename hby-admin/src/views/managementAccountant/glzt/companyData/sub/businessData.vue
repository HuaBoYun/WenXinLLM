<template>
  <div class="wrapper">
    <div style="padding: 20px">
      <el-page-header content="业务数据" @back="goBack" />
    </div>
    <div class="lr-layout">
      <div class="left">
        <BusinessTree @changeNode="changeNode" />
      </div>
      <div class="right">
        <template v-if="list && list.length > 0">
          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              type="index"
              width="55"
              label="序号"
            ></el-table-column>
            <el-table-column
              v-for="(item, index) in columns"
              :key="index"
              :prop="item.mappingColumn"
              :label="item.columnName"
            ></el-table-column>
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
        </template>
        <template v-else>
          <p style="text-align: center">暂无数据</p>
        </template>
      </div>
    </div>
    <!-- <iframe
      :src="url"
      style="width: 100%; height: 700px; border: none"
    ></iframe> -->
    <Dialogtable ref="tableref"></Dialogtable>
  </div>
</template>

<script>
  import { getTableData } from '@/api/workbench/businessData'
  import Dialogtable from '@/views/znfx/ai/table.vue'
  import BusinessTree from './components/businessTree.vue'
  export default {
    components: { Dialogtable, BusinessTree },
    name: 'BusinessData',
    data() {
      return {
        // url: 'http://192.0.2.12:9001/reportCenter?pack_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzaWduVGltZSI6MTYzMjg1MTE4NywiZXhwIjoxNjMyODcyNzg3LCJ1c2VybmFtZSI6IjJGNTU2RjE5NDU4MjQ5MjVCNzUzRTg3NjA5RDk2M0MyIn0.XWFjvm2FTbcjRUykK7FGaCDIlQEB--sZy3_XWuk0LLU&pack_cookieAge=900000',
        queryForm: {
          pageNo: 1,
          pageSize: 10,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        listLoading: false,
        list: [],
        total: 0,
        columns: [],
      }
    },
    created() {
      // this.getData()
    },
    methods: {
      goBack() {
        this.$router.back(-1)
      },
      async getData(data) {
        this.listLoading = true
        const params = JSON.parse(
          JSON.stringify({ ...this.queryForm, tableId: data.tableId })
        )
        const {
          data: { records, total },
        } = await getTableData(params)
        this.list = records
        this.total = total
        setTimeout(() => {
          this.listLoading = false
        }, 1000)
      },
      // 改变当前页
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.getData()
      },
      // 改变当前页数
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getData()
      },
      checkDetail(data) {
        this.$refs.tableref.open(data.id)
      },
      changeNode(data) {
        console.log(data)

        this.columns = data.settings
        this.getData(data)
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
    gap: 20;
    flex: 1;
  }

  .lr-layout > .left {
    width: 300px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
    height: calc(100vh - 300px);
    overflow-y: scroll;
  }
  .wrapper {
    display: flex;
    flex-direction: column;
  }
  .lr-layout > .right {
    flex: 1;
    padding: 20px;
  }
</style>
