<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form>
        <vab-query-form-top-panel>
          <el-form
            :inline="true"
            label-width="150px"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-row :gutter="20">
              <el-col>
                <el-form-item label="采集公司:">
                  <span>
                    {{ queryForm.title }}
                  </span>
                </el-form-item>
              </el-col>
              <el-col>
                <el-form-item label="账套名称:">
                  <span>
                    {{ queryForm.accountName }}
                  </span>
                </el-form-item>
              </el-col>
              <el-col>
                <el-form-item label="已有年限:">
                  <span>
                    {{ queryForm.alreadyYears }}
                  </span>
                </el-form-item>
              </el-col>
              <el-col>
                <el-form-item label="可采集年限:">
                  <span>
                    {{ queryForm.collectionYears }}
                  </span>
                </el-form-item>
              </el-col>
              <el-col>
                <el-form-item label="请选择采集时间:">
                  <el-date-picker
                    v-model="form.startYear"
                    class="width-130"
                    :default-value="queryForm.defaultValue"
                    format="yyyy"
                    :picker-options="queryForm.startPickerOptions"
                    type="year"
                    value-format="yyyy"
                  />
                  至
                  <el-date-picker
                    v-model="form.endYear"
                    class="width-130"
                    :default-value="queryForm.defaultValue"
                    format="yyyy"
                    :picker-options="queryForm.endPickerOptions"
                    type="year"
                    value-format="yyyy"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </vab-query-form-top-panel>
      </vab-query-form>
    </el-card>

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
        <el-button
          type="success"
          @click="handleGather"
          v-if="hasAuth('SJCJgather')"
        >
          采集
        </el-button>
      </vab-query-form-right-panel>
      <div class="table-container">
        <el-table
          v-loading="listLoading"
          :data="list"
          @sort-change="sortChange"
          style="width: 100%"
        >
          <el-table-column
            align="center"
            label="采集公司"
            prop="orgName"
            show-overflow-tooltip
            width="230"
          />

          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              align="center"
              label="采集年限"
              prop="recordYear"
              show-overflow-tooltip
              v-if="item.name === '采集年限'"
              width="110"
            />
            <el-table-column
              align="center"
              label="采集开始时间"
              prop="recordStart"
              show-overflow-tooltip
              v-if="item.name === '采集开始时间'"
              sortable="custom"
              width="150"
            />
            <el-table-column
              align="center"
              label="采集结束时间"
              prop="recordEnd"
              show-overflow-tooltip
              v-if="item.name === '采集结束时间'"
              sortable="custom"
              width="150"
            />
            <el-table-column
              align="center"
              label="采集耗时"
              prop="recordTime"
              show-overflow-tooltip
              v-if="item.name === '采集耗时'"
            />
            <el-table-column
              align="center"
              label="采集类型"
              prop="reType"
              show-overflow-tooltip
              v-if="item.name === '采集类型'"
            >
              <template #default="{ row }">
                {{ row.reType == 1 ? '手动采集' : '自动采集' }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="采集IP"
              prop="recordIp"
              show-overflow-tooltip
              v-if="item.name === '采集IP'"
            />
            <el-table-column
              align="center"
              label="采集人"
              prop="staffName"
              show-overflow-tooltip
              v-if="item.name === '采集人'"
            />
            <el-table-column
              label="详情"
              prop="recordMemo"
              show-overflow-tooltip
              v-if="item.name === '详情'"
              width="800"
            >
              <template #default="{ row }">
                <pre>{{ row.recordMemo }}</pre>
              </template>
            </el-table-column>
          </div>
          <el-table-column width="1" />
        </el-table>
      </div>
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
    <el-dialog
      :close-on-click-modal="false"
      title=""
      :visible.sync="dialogVisible"
      width="30%"
    >
      <div class="flex">
        <el-progress :percentage="percentage" type="circle" />
        <p v-show="percentage == 100">数据采集完成</p>
        <el-button
          v-show="percentage == 100"
          type="primary"
          @click="dialogVisible = false"
        >
          完 成
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getDataGatherList, startGather } from '@/api/setting/dataGather'
  import * as dayjs from 'dayjs'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Sjcj',
    components: { filterSearch, filterTable },
    data() {
      return {
        form: {
          startYear: undefined,
          endYear: undefined,
        },
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        // info: {
        //   ztname: '长江集团有限公司',
        //   yearList: 'HBFKCWZT2018',
        //   accBookList: [],
        // },
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          title: '',
          accountName: '',
          alreadyYears: '',
          collectionYears: '',
          collectionTime: '',
          defaultValue: '2024',
          startPickerOptions: '',
          endPickerOptions: '',
        },
        dialogVisible: false,
        percentage: 100,
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-dataGather-index-search',
        tableKey: 'setting-dataGather-index-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '采集年限' },
          { name: '采集开始时间' },
          { name: '采集结束时间' },
          { name: '采集耗时' },
          { name: '采集类型' },
          { name: '采集IP' },
          { name: '采集人' },
          { name: '详情' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
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
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '时间', key: 'jobname' }]
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
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
            accBookList,
            yearList,
            ztname,
            companyName,
          },
        } = await getDataGatherList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.queryForm.accountName = ztname
        this.queryForm.collectionYears = yearList
          .map((item) => item + '年')
          .join('、')
        this.queryForm.alreadyYears = accBookList
          .map((item) => item.bookyear + '年')
          .join('、')
        this.queryForm.title = companyName

        this.list = tlist.map((i) => {
          return {
            ...i,
            recordStart: dayjs(i.recordStart).format('YYYY-MM-DD HH:mm:ss'),
            recordEnd: dayjs(i.recordEnd).format('YYYY-MM-DD HH:mm:ss'),
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      async handleGather() {
        this.dialogVisible = true
        const res = await startGather(this.form)
      },
    },
  }
</script>

<style scoped>
  .fl-right {
    float: right;
  }
  .margin-bottom {
    margin-bottom: 20px;
  }
  .width-130 {
    width: 130px;
  }
  .flex {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

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
  .table-container {
    width: 100%; /* 或者更小的值 */
    margin: 0 auto;
  }
</style>
