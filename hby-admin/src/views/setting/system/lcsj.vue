<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-top-panel>
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
              >
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-select
                    v-model="queryForm.tableType"
                    placeholder="模块名称"
                    v-if="item.name === '模块名称'"
                  >
                    <!-- <el-option label="系统设置" value="xtsz" />
              <el-option label="合同管理" value="htgl" />
              <el-option label="法务管理" value="fwgl" />
              <el-option label="智能监控" value="znjk" />
              <el-option label="内控合规" value="nkhg" />
              <el-option label="内部审计" value="znsj" />
              <el-option label="智能分析" value="znfx" />
              <el-option label="风险管控" value="fxgk" /> -->
                    <el-option
                      v-for="item in moduleLists"
                      :key="item.id"
                      :label="item.projectName"
                      :value="item.uniqueIdentification"
                    />
                  </el-select>
                  <el-input
                    v-model="queryForm.ymWorkName"
                    placeholder="表单名称"
                    clearable
                    v-if="item.name === '表单名称'"
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button
                    icon="el-icon-search"
                    type="primary"
                    @click="fetchData"
                  >
                    查询
                  </el-button>
                  <el-button type="primary" @click="resetSearch">
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
                <el-form-item>
                  <span
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
                    @click="showMore"
                  >
                    <span>{{ searchMore ? '收起' : '展开' }}</span>
                    <i class="el-icon-arrow-down"></i>
                  </span>
                </el-form-item>
              </el-form>
            </vab-query-form-top-panel>
          </el-card>
        </vab-query-form>
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
                <!-- <i class="el-icon-delete" slot="reference"></i> -->
                <el-button
                  slot="reference"
                  icon="el-icon-s-grid"
                  class="biaoge"
                  style="margin-bottom: 10px; margin-right: 10px"
                ></el-button>
              </el-popover>
            </el-tooltip>
          </vab-query-form-right-panel>
          <el-table
            v-loading="listLoading"
            :data="list"
            @sort-change="sortChange"
          >
            <el-table-column
              type="index"
              label="序号"
              align="center"
              sortable="custom"
            >
              <template slot-scope="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="表单名称"
                prop="ymWorkName"
                show-overflow-tooltip
                v-if="item.name === '表单名称'"
              />
              <el-table-column
                align="center"
                label="工作流数量"
                prop="workCount"
                show-overflow-tooltip
                v-if="item.name === '工作流数量'"
              />
            </div>
            <el-table-column align="center" label="操作" show-overflow-tooltip>
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="jump(row)"
                  v-if="hasAuth('XTLCSJlcjsj')"
                >
                  流程设计
                </el-button>
                <el-button
                  type="text"
                  @click="openModal(row)"
                  v-if="hasAuth('XTLCSJlcList')"
                >
                  流程列表
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <!-- <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="'total, sizes, prev, pager, next, jumper'"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
        <ProcessList ref="process" @fetchData="fetchData" />
      </div>
    </div>
  </div>
</template>

<script>
  import { getJNFDList, getLCSJData, SSOToJNFD } from '@/api/setting/system'
  import store from '@/store'
  import ProcessList from './components/ProcessList.vue'
  import { getModuleList } from '@/api/setting/system'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    components: { ProcessList, filterSearch, filterTable },
    data() {
      return {
        list: [],
        listLoading: false,
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          tableType: 'xtsz',
          ymWorkName: '',
        },
        options: [],
        moduleLists: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-system-lcsj-search',
        tableKey: 'setting-system-lcsj-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [{ name: '表单名称' }, { name: '工作流数量' }], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    async created() {
      // await this.getOptions();
      await this.fetchData()
      this.moduleList()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      async sortChange(column) {
        let { order, prop } = column
        let p = prop
        this.sortFields = p || ''
        if (order === 'ascending') {
          this.sortFlag = 'asc'
        } else if (order === 'descending') {
          this.sortFlag = 'desc'
        } else {
          this.sortFlag = ''
        }
        await this.fetchData()
      },
      async getOptions() {
        const { data } = await getLCSJData()

        this.options = data.tableTypeNameLista
      },
      jump(row) {
        const token = store.getters['user/token']
        SSOToJNFD()
          .then((result) => {
            window.open(
              // 'http://localhost:3000/workFlow/flowEngine?token=' +
              // 'https://192.0.2.14:9100/workFlow/flowEngine?token=' +
              'https://www.example.com/workFlow/flowEngine?token=' +
                result.data.ymToken +
                '&showzdy=zdy' +
                '&tableId=' +
                row.tableId +
                '&xtoken=' +
                token
            )
          })
          .catch((err) => {})
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
          pageNumber: 1,
          pageSize: 10,
          tableType: 'xtsz',
          ymWorkName: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list },
        } = await getJNFDList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = list
        this.total = list.length
        this.listLoading = false
      },
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          tableType: 'xtsz',
          ymWorkName: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      openModal(row) {
        this.$refs['process'].show({}, row.tableId)
      },
      moduleList() {
        getModuleList({}).then((res) => {
          this.moduleLists = res.data
        })
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '模块名称', key: 'tableType' },
          { name: '表单名称', key: 'ymWorkName' },
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
      showMore() {
        this.searchMore = !this.searchMore
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 250px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .right {
    flex: 1;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
