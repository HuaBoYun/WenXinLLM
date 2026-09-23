<template>
  <!-- 工程结算审计汇总 -->
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.settlementcode"
                clearable
                v-if="item.name === '合同编号'"
                placeholder="合同编号"
              />
              <el-input
                v-model="queryForm.settlementname"
                clearable
                v-if="item.name === '工程名称'"
                placeholder="工程名称"
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
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                重置
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-tooltip
                class="item"
                effect="dark"
                content="搜索筛选"
                placement="top"
              >
                <el-popover placement="left" trigger="click">
                  <filter-search
                    v-if="true"
                    :list="searchAll"
                    :name="localKey"
                    @updateSearchShow="initSearch"
                  />
                  <el-button slot="reference" style="height: 32px">
                    <vab-icon icon="filter" :is-custom-svg="true" />
                  </el-button>
                </el-popover>
              </el-tooltip>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>
    <el-card shadow="never">
      <vab-query-form-right-panel :span="24">
        <el-tooltip
          class="item"
          effect="dark"
          content="表格筛选"
          placement="top"
        >
          <el-popover placement="right" trigger="click">
            <filter-table
              :list="filedAll"
              :name="tableKey"
              @updateTableShow="initTable"
            />
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleEdit('add', null)">
          新增
        </el-button>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
        >
          <el-button type="success">导入</el-button>
        </el-upload>
        <el-button type="success" @click="handleExport">导出</el-button>
        <el-button type="success" @click="handleTB()">
          同步
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column
          align="center"
          label="序号"
          prop="sortindex"
          width="100"
        />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="合同编号"
            prop="settlementcode"
            v-if="item.name === '合同编号'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit('detail', row)">
                {{ row.settlementcode }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '工程名称'"
            align="center"
            label="工程名称"
            prop="settlementname"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '建设单位'"
            align="center"
            label="建设单位"
            prop="settlementunitname"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '二审审查金额'"
            align="center"
            label="二审审查金额"
            prop="settlementinstanceamount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '施工单位'"
            align="center"
            label="施工单位"
            prop="settlementconsttunitname"
            show-overflow-tooltip
          />
        </div>
        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit('edit', row)">
              修改
            </el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <gcjssjxmhzView ref="edit" @fetchData="fetchData" />
  </div>
</template>

<script>
  import {
    getSettlementList,
    deleteSettlement,
    settlementExportData,
    syncConstructionProject,
  } from '@/oapi/audit/jhbz'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import gcjssjxmhzView from './components/gcjssjxmhzView.vue'
  import { downloadFile } from '@/utils/otherUtils'
  import store from '@/store'
  const token = store.getters['user/token']
  const { baseURL } = require('@/config')
  export default {
    name: 'cwzxpxb',
    components: {
      filterSearch,
      filterTable,
      gcjssjxmhzView,
    },

    mixins: [searchTableMixis],
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/audit/settlement/importData',
        headers: { token: token },
        select: [],
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          settlementname: '',
          settlementcode: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '合同编号' },
          { name: '工程名称' },
          { name: '建设单位' },
          { name: '二审审查金额' },
          { name: '施工单位' },
          { name: '建议科室' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-jhlx-fgldhz-search',
        tableKey: 'oilAudit-jhlx-fgldhz-list',
        searchMore: true,
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) this.fetchData()
      })
    },
    methods: {
      async  handleTB(){
        const data = await syncConstructionProject( )
        if(data.code == 1){
          this.$baseMessage(data.data, 'success')
        }  
      },
      handleSelectionChange(val) {
        this.select = val;
      },
      //导出
      async handleExport() {
        this.listLoading = true
        const ids = this.select.map((res) => res.settlementid)
        const data = await settlementExportData({...this.queryForm, ids: ids.join()})
        downloadFile(data, '工程结算审计项目汇总.xlsx')
        this.listLoading = false
      },
      //导入
      handleSuccess(response) {
        if (response.code == 1) {
          this.$baseMessage('导入成功', 'success')
          this.fetchData()
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      getFiled() {
        return [
          { name: '合同编号', key: 'settlementcode' },
          { name: '工程名称', key: 'settlementname' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          settlementname: '',
          settlementcode: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
        this.listLoading = false
        const {
          data: { tlist, totalRecord },
        } = await getSettlementList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      async handleEdit(type, row) {
        this.$refs['edit'].showEdit(type, row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteSettlement({ id: row.settlementid })
          if (code == 1) {
            this.$message.success(msg)
            await this.fetchData()
          }
        })
      },
      // 自定义索引方法
      getIndex(index) {
        return index + 1 // 使索引从1开始递增
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
