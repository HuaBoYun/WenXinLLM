<template>
  <div class="system-log-container">
    <el-page-header content="利润表" @back="goBack" />
    <div
      style="display: flex; justify-content: space-between; margin-bottom: 20px"
    >
      <div>单位：{{ this.bookInfo.orgName }}</div>
      <div>
        货币单位：元
        <span style="margin-left: 20px">
          会计期间：{{ this.bookInfo.bookYear }}年
        </span>
      </div>
    </div>
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-select v-model="queryForm.date" placeholder="月份">
              <el-option label="1" value="1" />
              <el-option label="2" value="2" />
              <el-option label="3" value="3" />
              <el-option label="4" value="4" />
              <el-option label="5" value="5" />
              <el-option label="6" value="6" />
              <el-option label="7" value="7" />
              <el-option label="8" value="8" />
              <el-option label="9" value="9" />
              <el-option label="10" value="10" />
              <el-option label="11" value="11" />
              <el-option label="12" value="12" />
              <!-- <el-option label="月份" value="AMONTH" /> -->
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
            >
              查询
            </el-button>
            <el-button native-type="submit" type="default" @click="reset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
    </vab-query-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="index" />
      <el-table-column align="left" label="项目" prop="item" />
      <el-table-column align="right" label="本月数">
        <template #default="{ row }">
          {{ String(row.curyearBys).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column>
      <el-table-column align="right" label="本年累计" prop="curyearBnlj">
        <template #default="{ row }">
          {{ String(row.curyearBnlj).replace(/\d(?=(?:\d{3})+\b)/g, '$&,') }}
        </template>
      </el-table-column>

      <!-- <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-dropdown style="margin-left: 10px" @command="handleCommand(row)">
            <el-button type="text">操作</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>发送至底稿</el-dropdown-item>
              <el-dropdown-item>发送至底稿附件</el-dropdown-item>
              <el-dropdown-item>发送至疑点</el-dropdown-item>
              <el-dropdown-item>发送至缺陷</el-dropdown-item>
              <el-dropdown-item>发送至风险</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column> -->
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
  </div>
</template>

<script>
  import { getProfitList } from '@/api/workbench/companyData/statement'
  import getUserSelectedBookInfo from './../utils/getBookInfo'

  export default {
    name: 'Consult',
    components: {},
    data() {
      return {
        bookInfo: {
          bookYear: '',
          orgName: '',
        },
        currentRow: '',
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNo: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      // this.fetchData()
    },
    async mounted() {
      if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      const bookInfo = localStorage.getItem('bookInfo')
      this.bookInfo = JSON.parse(bookInfo)
      this.fetchData()
    },
    methods: {
      goBack() {
        this.$router.back(-1)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const params = JSON.parse(JSON.stringify(this.queryForm))
        params.bookYear = this.bookInfo.bookYear

        const res = await getProfitList(params)
        const {
          data: { list, total },
        } = await getProfitList(params)
        //
        this.list = list[0].accReportMprofitList
        this.total = total
        this.listLoading = false
      },
      reset() {
        this.queryForm = {
          pageNo: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleCurrentChange(val) {
        this.currentRow = val
      },
    },
  }
</script>
