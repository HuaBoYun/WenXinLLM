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
        <el-table-column type="selection"></el-table-column>
        <el-table-column align="center" label="序号" prop="no">
          <!-- <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.title }}
            </el-button>
          </template> -->
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="标题"
            prop="title"
            v-if="item.name === '标题'"
            min-width="400"
          />
          <el-table-column
            align="center"
            label="文件编号？"
            prop="documento"
            v-if="item.name === '文件编号？'"
          />
          <el-table-column
            align="center"
            label="拟稿人"
            prop="drafter"
            v-if="item.name === '拟稿人'"
          />
          <el-table-column
            align="center"
            label="发文单位"
            prop="issuingUnit"
            v-if="item.name === '发文单位'"
          />
          <!-- <el-table-column
            align="center"
            label="发文单位"
            prop="issuingUnit"
            v-if="item.name === '发文单位'"
          ></el-table-column> -->
          <el-table-column
            align="center"
            label="公文状态"
            prop="officialDocumentStatus"
            v-if="item.name === '公文状态'"
          />
          <el-table-column
            align="center"
            label="当前环节"
            prop="currentLink"
            v-if="item.name === '当前环节'"
          />

          <el-table-column
            align="center"
            label="当前处理人"
            prop="currentProcessor"
            v-if="item.name === '当前处理人'"
          >
            <!-- <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '已退回'
                  : row.state == 3
                  ? '已撤销'
                  : row.state == 4
                  ? '已终止'
                  : row.state == 5
                  ? '已跟踪'
                  : row.state == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template> -->
          </el-table-column>
        </div>

        <!-- <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleApproval(row)"
              :disabled="row.state"
            >
              提交审批
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.state != 0 && row.state != 2&& row.state != 3"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="row.state"
            >
              删除
            </el-button>
           
          </template>
        </el-table-column> -->
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
            title: '编辑权限测试咯',
            documento: '2023-01-17',
            drafter: '张孝昆',
            issuingUnit: '财务中心',
            officialDocumentStatus: '发布',
            currentLink: '	结束节点',
            currentProcessor: '<无>',
          },
          {
            no: 2,
            title: '编辑权限测试咯',
            documento: '2023-01-17',
            drafter: '张孝昆',
            issuingUnit: '财务中心',
            officialDocumentStatus: '发布',
            currentLink: '	结束节点',
            currentProcessor: '<无>',
          },
          {
            no: 3,
            title: '编辑权限测试咯',
            documento: '2023-01-17',
            drafter: '张孝昆',
            issuingUnit: '财务中心',
            officialDocumentStatus: '发布',
            currentLink: '	结束节点',
            currentProcessor: '<无>',
          },
          {
            no: 4,
            title: '编辑权限测试咯',
            documento: '2023-01-17',
            drafter: '张孝昆',
            issuingUnit: '财务中心',
            officialDocumentStatus: '发布',
            currentLink: '	结束节点',
            currentProcessor: '<无>',
          },
          {
            no: 5,
            title: '编辑权限测试咯',
            documento: '2023-01-17',
            drafter: '张孝昆',
            issuingUnit: '财务中心',
            officialDocumentStatus: '发布',
            currentLink: '	结束节点',
            currentProcessor: '<无>',
          },
          {
            no: 6,
            title: '编辑权限测试咯',
            documento: '2023-01-17',
            drafter: '张孝昆',
            issuingUnit: '财务中心',
            officialDocumentStatus: '发布',
            currentLink: '	结束节点',
            currentProcessor: '<无>',
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
</style>
