<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <DeepTree @select="handleNodeClick" :isSelectNode="false" />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-top-panel>
              <el-form
                ref="form"
                checkable
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item
                  v-for="(item, index) in searchItem"
                  :key="index"
                  :prop="item.key"
                >
                  <el-input
                    v-model="queryForm.code"
                    clearable
                    placeholder="事件编号"
                    v-if="item.name === '事件编号'"
                  />
                  <el-input
                    v-model="queryForm.name"
                    clearable
                    placeholder="事件名称"
                    v-if="item.name === '事件名称'"
                  />
                  <el-select
                    v-model="queryForm.losseventcategory"
                    placeholder="损失事件定性类别"
                    v-if="item.name === '损失事件定性类别'"
                  >
                    <el-option label="一般事件" value="1" />
                    <el-option label="重大事件" value="2" />
                  </el-select>
                  <el-select
                    v-model="queryForm.riskcatid"
                    placeholder="请选择风险类型"
                    v-if="item.name === '风险类型'"
                  >
                    <el-option
                      v-for="item in selectList"
                      :key="item.key"
                      :label="item.value"
                      :value="item.key"
                    />
                  </el-select>
                  <el-date-picker
                    align="right"
                    end-placeholder="结束日期"
                    range-separator="至"
                    format="yyyy-MM-dd"
                    start-placeholder="开始日期"
                    type="daterange"
                    unlink-panels
                    v-model="queryForm.Date"
                    value-format="yyyy-MM-dd"
                    v-if="item.name === '日期'"
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
                    type="primary"
                    native-type="submit"
                    @click="fetchData('reset')"
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
          </vab-query-form-right-panel>

          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="事件编号"
              prop="riskeventcode"
              width="140"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleRead(row)">
                  {{ row.riskeventcode }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="版本号" prop="version" />

            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="事件名称"
                prop="riskeventname"
                show-overflow-tooltip
                v-if="item.name === '事件名称'"
              />
              <el-table-column
                align="center"
                label="公司名称"
                prop="unitname"
                show-overflow-tooltip
                v-if="item.name === '公司名称'"
              />
              <el-table-column
                align="center"
                label="风险类型"
                prop="riskcatname"
                show-overflow-tooltip
                v-if="item.name === '风险类型'"
              />
              <el-table-column
                align="center"
                label="发生部门"
                prop="occureddepartment"
                show-overflow-tooltip
                v-if="item.name === '发生部门'"
              />
              <el-table-column
                align="center"
                label="发生日期"
                prop="occureddate"
                v-if="item.name === '发生日期'"
              />
              <el-table-column
                align="center"
                label="损失事件定性类别"
                prop="losseventcategory"
                show-overflow-tooltip
                v-if="item.name === '损失事件定性类别'"
              >
                <template slot-scope="{ row }">
                  <span>
                    {{ row.losseventcategory == '1' ? '一般事件' : '重大事件' }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="审批状态"
                prop="status"
                v-if="item.name === '审批状态'"
              >
                <template #default="{ row }">
                  {{
                    row.status == 1
                      ? '审批中'
                      : row.status == 2
                      ? '需调整'
                      : row.status == 3
                      ? '已撤销'
                      : row.status == 4
                      ? '已终止'
                      : row.status == 5
                      ? '已跟踪'
                      : row.status == 6
                      ? '已完成'
                      : '未审批'
                  }}
                </template>
              </el-table-column>
            </div>
            <el-table-column align="center" label="操作">
              <template slot-scope="{ row }">
                <!-- <el-button
                  type="text"
                  @click="handleEdit(row)"
                  :disabled="!!row.status"
                >
                  修改
                </el-button> -->
                <el-button type="text" @click="handleHistory(row)">
                  查看上报记录
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          background
          class="pager"
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <HandleHistory ref="historyList" />
    <EventRead ref="read" />
  </div>
</template>

<script>
  import { disposalmanageMain } from '@/api/risk/riskEvents'
  import TypeTree from '@/views/risk/components/TypeTree.vue'
  import { UTCformat } from '@/utils'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import HandleHistory from './components/HistoryList.vue'
  import EventRead from '@/views/risk/events/eventBase/components/EventRead.vue'
  import DeepTree from './components/DepTree.vue'
  import { getRiskEventsTree } from '@/api/risk/riskEvents'

  export default {
    name: 'Fillin',
    components: {
      TypeTree,
      filterSearch,
      filterTable,
      EventRead,
      HandleHistory,
      DeepTree,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          Date: null,
          pageNo: 1,
          pageSize: 20,
          riskcatid: '',
          losseventcategory: '',
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-event-eventBase-search',
        tableKey: 'risk-event-eventBase-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '事件名称' },
          { name: '公司名称' },
          { name: '风险类型' },
          { name: '发生部门' },
          { name: '发生日期' },
          { name: '损失事件定性类别' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [],
        selectList: [],
      }
    },
    created() {
      this.queryForm.losseventcategory = this.$route.query?.type
      this.queryForm.companyname = this.$route.query?.companyname
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
      this.getRiskEventsTreeData()
      // this.fetchData()
    },
    /**
     * @description: 流程提交回调
     * @return {*}
     */
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      //
      formatTreeData(data) {
        let result = []
        const traverse = (nodes) => {
          nodes.forEach((node) => {
            result.push({
              key: node.riskcatid,
              value: node.riskcatname,
            })
            if (node.children && node.children.length > 0) {
              traverse(node.children)
            }
          })
        }
        traverse(data)
        return result
      },

      async getRiskEventsTreeData() {
        const res = await getRiskEventsTree()
        if (res.code === 1) {
          // 处理树形数据
          this.selectList = this.formatTreeData(res.data.tree)
        }
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '事件编号', key: 'code' },
          { name: '事件名称', key: 'name' },
          { name: '日期', key: 'Date' },
          { name: '风险类型', key: 'riskcatid' },
          { name: '损失事件定性类别', key: 'losseventcategory' },
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
      handleNodeClick(val) {
        this.queryForm.orgId = val.id
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        console.log('handleSizeChange')
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        console.log('handleCurrentChange')
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 分页，初始化
       * @return {*}
       */
      queryData() {
        console.log('queryData')
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData(type) {
        console.log('执行次数')
        this.listLoading = true
        if (type && type == 'reset') this.$refs['form'].resetFields()
        const { Date } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        delete this.queryForm.Date
        const {
          data: { page },
        } = await disposalmanageMain({
          ...this.queryForm,
          startDate,
          endDate,
        })

        page.list = page.list.map((v) => {
          v.discovereddate = UTCformat(v.discovereddate)
          v.occureddate = UTCformat(v.occureddate)
          return v
        })

        this.list = page.list
        this.total = page.total
        this.listLoading = false
      },

      /**
       * @description: 打开详细
       * @return {*}
       */
      handleRead(row) {
        console.log(row)
        this.$refs['read'].showRead(row)
      },

      //查看历史记录
      async handleHistory(val) {
        this.$refs['historyList'].showEdit(val)
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
    width: 300px;
    max-width: 300px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 10px 20px 20px;
    background: #ffffff;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
