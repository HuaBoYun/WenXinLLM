<template>
  <div class="merge-header-container">
    <vab-query-form>
      <vab-query-form-top-panel :span="24">
        <el-button
          :loading="downloadLoading"
          type="primary"
          @click="handleDownload"
        >
          导出
        </el-button>
      </vab-query-form-top-panel>
    </vab-query-form>

    <el-table ref="multipleTable" v-loading="listLoading" border :data="list">
      <el-table-column align="center" label="序号" width="55">
        <template #default="{ $index }">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="Main Information">
        <el-table-column label="Title">
          <template #default="{ row }">
            {{ row.title }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="Author">
          <template #default="{ row }">
            <el-tag>{{ row.author }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="Readings">
          <template #default="{ row }">
            {{ row.pageViews }}
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="Date">
        <template #default="{ row }">
          <span>{{ row.datetime }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { getList } from '@/api/table'
  import { parseTime } from '@/utils'

  export default {
    name: 'ExportMergeHeaderExcel',
    data() {
      return {
        list: [],
        listLoading: true,
        downloadLoading: false,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        this.listLoading = true
        const {
          data: { list },
        } = await getList()
        this.list = list
        this.listLoading = false
      },
      handleDownload() {
        this.downloadLoading = true
        import('@/utils/excel').then((excel) => {
          const multiHeader = [['Id', 'Main Information', '', '', 'Date']]
          const header = ['', 'Title', 'Author', 'Readings', '']
          const filterVal = ['id', 'title', 'author', 'pageViews', 'datetime']
          const list = this.list
          const data = this.formatJson(filterVal, list)
          const merges = ['A1:A2', 'B1:D1', 'E1:E2']
          excel.export_json_to_excel({
            multiHeader,
            header,
            merges,
            data,
          })
          this.downloadLoading = false
        })
      },
      formatJson(filterVal, jsonData) {
        return jsonData.map((v) =>
          filterVal.map((j) => {
            if (j === 'timestamp') {
              return parseTime(v[j])
            } else {
              return v[j]
            }
          })
        )
      },
    },
  }
</script>
