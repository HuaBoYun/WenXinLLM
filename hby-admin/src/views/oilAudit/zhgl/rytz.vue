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
                v-model="queryForm.realName"
                clearable
                placeholder="员工姓名"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '员工姓名'"
              ></el-input>
              <el-select
                v-model="queryForm.onDutyStatus"
                v-if="item.name === '在岗状态'"
              >
                <el-option label="在岗闲置" :value="1" />
                <el-option label="在岗项目内" :value="2" />
                <el-option label="请假" :value="3" />
                <el-option label="外派" :value="4" />
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
            </el-form-item>
            <el-form-item>
              <el-button @click="resetSearch()" type="primary">重置</el-button>
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
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          align="center"
          label="用户名"
          prop="userName"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="真实姓名"
            prop="realName"
            v-if="item.name === '真实姓名'"
          />
          <el-table-column
            align="center"
            label="手机号码"
            prop="miblePhone"
            v-if="item.name === '手机号码'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="固定电话"
            prop="fixedPhone"
            v-if="item.name === '固定电话'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="邮箱"
            prop="email"
            v-if="item.name === '邮箱'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="所属部门"
            prop="orgname"
            v-if="item.name === '所属部门'"
          ></el-table-column>

          <el-table-column
            align="center"
            label="状态"
            prop="onDutyStatus"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.onDutyStatus == 1
                  ? '在岗闲置'
                  : row.onDutyStatus == 2
                  ? '在岗项目内'
                  : row.onDutyStatus == 3
                  ? '请假'
                  : row.onDutyStatus == 4
                  ? '外派'
                  : ''
              }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="备注"
            prop="memo"
            v-if="item.name === '备注'"
          ></el-table-column>
        </div>
        <el-table-column width="1"></el-table-column>
      </el-table>
    </el-card>
    <ryqjdViews ref="edit" @fetchData="fetchData"></ryqjdViews>
    <!-- 提交流程 -->
    <ProcessList ref="process" @fetchData="fetchData" />
    <!-- 办理查看 -->
    <WfqdDeal ref="wfqddeal" />

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
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { getList, exportList } from '@/oapi/ypns_zhgl/ryqjtz'
  import { downloadFile } from '@/utils/otherUtils'
  export default {
    components: {
      filterTable,
      filterSearch,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          onDutyStatus: '',
          realName: null,
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '真实姓名' },
          { name: '手机号码' },
          { name: '固定电话' },
          { name: '邮箱' },
          { name: '所属部门' },
          { name: '状态' },
          { name: '备注' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-zhgl-rytz-search',
        tableKey: 'oilAudit-zhgl-rytz-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        select: [],
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      // 获取路由参数,如果存在type，则将type赋值给queryForm.onDutyStatus
      const type = this.$route.query.type
      if (type) {
        this.queryForm.onDutyStatus = +type
        this.fetchData()
      }
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '员工姓名', key: 'realName' },
          { name: '在岗状态', key: 'onDutyStatus' },
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
        this.setCheckedRows()
      },
      async handleExport() {
        const ids = this.select.map((res) => res.staffId)
        const res = await exportList({ ...this.queryForm, ids })
        downloadFile(res, '人员台账.xlsx')
      },

      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          onDutyStatus: '',
          realName: null,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.staffId == row.staffId)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.staffId == row.staffId)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.staffId == row.staffId)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.staffId == item.staffId
              }),
              true
            )
          })
        })
      },
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
