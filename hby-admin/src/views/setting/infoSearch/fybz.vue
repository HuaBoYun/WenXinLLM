<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="100px"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.interfacename"
              clearable
              placeholder="接口名称"
            />
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
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="接口名称"
        prop="interfacename"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="收费规则"
        prop="accountrule"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="单价"
        prop="hbprice"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="包年"
        prop="annualprice"
        show-overflow-tooltip
      />
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
  </div>
</template>

<script>
  import { getCostStandard } from '@/api/setting/infoSearch'

  export default {
    name: 'SystemLog',
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          interfacename: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getCostStandard(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
    },
  }
</script>
