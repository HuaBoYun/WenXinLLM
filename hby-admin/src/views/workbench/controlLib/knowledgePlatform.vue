<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel class="option-row">
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
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="文件夹名称"
          prop="folderName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="文件夹路径"
          prop="folderPath"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="120">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="showDetails(scope.row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      class="pager"
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <!-- 详情弹窗 -->
    <folder-details
      :visible.sync="detailsVisible"
      :folder-id="currentFolderId"
    />
  </div>
</template>

<script>
  import { knowledge } from '@/api/setting/knowledge.js'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import FolderDetails from './components/folderDetails.vue'

  export default {
    name: 'NormalReportList',
    components: {
      filterSearch,
      filterTable,
      FolderDetails,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          reminderStaffName: '',
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-cbgl-fwjl-search',
        tableKey: 'risk-cbgl-fwjl-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [{ name: '文件夹路径' }, { name: '文件夹名称' }], //所有表格项
        filedNow: [],
        detailsVisible: false, // 详情弹窗显示
        currentFolderId: '', // 当前选中的文件夹ID
      }
    },
    created() {
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
      this.fetchData()
    },

    methods: {

      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        try {
          const {
            data: { folderOptCountList, totalCount },
            code,
            msg,
          } = await knowledge({
            pageNumber: this.queryForm.pageNumber,
            pageSize: this.queryForm.pageSize,
          })
          this.list = folderOptCountList
          this.total = totalCount
        } catch (error) {
          console.error('获取数据失败:', error)
          this.$message.error('获取数据失败，请稍后重试')
          this.list = []
          this.total = 0
        } finally {
          this.listLoading = false
        }
      },
      // 定义表单所有项
      getFiled() {
        let fields = []
        return fields
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = ''
              }
            }
          })

          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      // 动态表格开始
      initTable() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
        })
      },

      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      showDetails(row) {
        console.log('showDetails - folderId:', row.folderId)
        this.currentFolderId = row.folderId
        this.detailsVisible = true
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }

  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }

  .pager {
    margin-bottom: 20px !important;
  }
</style>
