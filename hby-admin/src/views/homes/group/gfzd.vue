<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.title"
                clearable
                placeholder="标题"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '标题'"
              />

              <el-input
                v-model="queryForm.drafter"
                clearable
                placeholder="拟稿人"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '拟稿人'"
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
              <el-button @click="resetSearch">重置</el-button>
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
            <el-form-item>
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">新建</el-button>
        <!-- <el-button @click="handleExport">导出</el-button> -->
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" label="全选"></el-table-column>
        <!-- <el-table-column align="center" label="序号" prop="no"> -->
        <!-- </el-table-column> -->
        <el-table-column align="left" min-width="400">
          <template #default="{ row }">
            <div>
              <el-button type="text" size="medium" width="100%">
                {{ row.title }}
              </el-button>
            </div>
            <div class="items">
              <span class="mag10">文件编号：{{ row.no }}</span>
              <span class="mag10">录入者：{{ row.inputer }}</span>
              <span class="mag10">所属部门：{{ row.department }}</span>
              <span class="mag10">生效时间：{{ row.entryIntoForceTime }}</span>
              <span class="mag10">标签：{{ row.tabs }}</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

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
    <!-- <flgwView ref="flgwView" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" /> -->
  </div>
</template>

<script>
  import { deleteFLGWList, getFLGWList, exportFLGW } from '@/api/fwgl/zzxx'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  // import flgwView from './components/flgwView.vue'
  import { downloadFile } from '@/utils/otherUtils'

  export default {
    name: 'NormalReportList',
    components: { filterTable, filterSearch, ProcessList },
    // components: { flgwView, filterTable, filterSearch, ProcessList },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          title: '',
          drafter: '',
          pageSize: 20,
          pageNumber: 1,
        },
        filedAll: [
          { name: '标题' },
          { name: '文件编号？' },
          { name: '拟稿人' },
          { name: '发文单位' },
          { name: '公文状态' },
          { name: '当前环节' },
          { name: '当前处理人' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-zzxx-flgw-search',
        tableKey: 'fwgl-zzxx-flgw-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleSelectionChange() {},
      handleApproval(row) {
        //提交审批
        // this.$refs['process'].save(24, row.adviserId)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '标题', key: 'title' },
          { name: '拟稿人', key: 'drafter' },
        ]
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
                this.queryForm[x.key] = null
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
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },

      // 动态表格开始
      initTable() {
        this.loading = true
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
          this.loading = false
        })
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          title: '',
          drafter: '',
          pageSize: 20,
          pageNumber: 1,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        // const {
        //   data: { tlist, totalRecord },
        // } = await getFLGWList(this.queryForm)
        // this.list = tlist
        this.list = [
          {
            no: 1,
            title: '乐于助人',
            inputer: '张孝昆',
            department: '培训部',
            entryIntoForceTime: '2023-02-05',
            tabs: '乐于助人',
          },
          {
            no: 2,
            title: '乐于助人',
            inputer: '张孝昆',
            department: '培训部',
            entryIntoForceTime: '2023-02-05',
            tabs: '乐于助人',
          },
          {
            no: 3,
            title: '乐于助人',
            inputer: '张孝昆',
            department: '培训部',
            entryIntoForceTime: '2023-02-05',
            tabs: '乐于助人',
          },
          {
            no: 4,
            title: '乐于助人',
            inputer: '张孝昆',
            department: '培训部',
            entryIntoForceTime: '2023-02-05',
            tabs: '乐于助人',
          },
          {
            no: 5,
            title: '乐于助人',
            inputer: '张孝昆',
            department: '培训部',
            entryIntoForceTime: '2023-02-05',
            tabs: '乐于助人',
          },
          {
            no: 6,
            title: '乐于助人',
            inputer: '张孝昆',
            department: '培训部',
            entryIntoForceTime: '2023-02-05',
            tabs: '乐于助人',
          },
          {
            no: 7,
            title: '乐于助人',
            inputer: '张孝昆',
            department: '培训部',
            entryIntoForceTime: '2023-02-05',
            tabs: '乐于助人',
          },
          {
            no: 8,
            title: '乐于助人',
            inputer: '张孝昆',
            department: '培训部',
            entryIntoForceTime: '2023-02-05',
            tabs: '乐于助人',
          },
        ]
        // this.total = totalRecord
        this.listLoading = false
      },
      async handleExport() {
        this.listLoading = true
        const res = await exportFLGW(this.queryForm)
        downloadFile(res, '总法律顾问列表.xlsx')
        this.listLoading = false
      },
      handleAdd() {
        // this.$refs['flgwView'].showEdit('add', null)
      },
      async handleDetail(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        // await this.$refs['flgwView'].showEdit('detail', row)
      },
      async handleEdit(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        // await this.$refs['flgwView'].showEdit('edit', row)
      },
      handleDelete(row) {
        // this.$baseConfirm('你确定要删除当前项吗', null, async () => {
        //   const { msg, code } = await deleteFLGWList({ id: row.adviserId })
        //   if (code == 200) {
        //     this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        //     await this.fetchData()
        //   }
        // })
      },
      handleRowClick(e) {},
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
  .items .mag10 {
    margin: 0 0 0 10px;
  }
</style>
