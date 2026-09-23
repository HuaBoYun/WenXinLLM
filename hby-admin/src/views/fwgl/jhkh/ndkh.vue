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
                v-model="queryForm.annualExamineName"
                clearable
                placeholder="考核名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '考核名称'"
              ></el-input>

              <el-select
                v-model="queryForm.type"
                placeholder="考核类型"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '考核类型'"
              >
                <el-option label="外部监管考核" :value="1" />
                <el-option label="子单位考核" :value="2" />
              </el-select>

              <el-date-picker
                v-model="queryForm.Date"
                placeholder="考核时间"
                type="daterange"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                style="width: 140px; margin-right: 20px"
                value-format="yyyy-MM-dd HH:mm:ss"
                v-if="item.name === '时间'"
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
        <el-button @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="考核名称"
          prop="annualExamineName"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.annualExamineName }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="考核类型"
            prop="type"
            v-if="item.name === '考核类型'"
          >
            <template #default="{ row }">
              <span v-if="row.type === 1">外部监管考核</span>
              <span v-if="row.type === 2">子单位考核</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="考核时间"
            prop="examineTime"
            :formatter="formatDate"
            v-if="item.name === '考核时间'"
          />
          <el-table-column
            align="center"
            label="创建人"
            prop="creatorName"
            v-if="item.name === '创建人'"
          />
          <el-table-column
            align="center"
            label="创建时间"
            prop="createdTime"
            :formatter="formatDate"
            v-if="item.name === '创建时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="状态"
            prop="state"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '已退回'
                  : row.state == 3
                  ? '已撤回'
                  : row.state == 4
                  ? '已终止'
                  : row.state == 5
                  ? '已跟踪'
                  : row.state == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleApproval(row)"
              :disabled="row.state"
            >
              审批
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.state != 0 && row.state != 2 && row.state != 3"
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
            <!-- <el-button type="text">导出</el-button> -->
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
    <ndkhView ref="ndkhView" @fentch="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
  </div>
</template>

<script>
  import { deleteNDKHList, exportNDKH, getNDKHList } from '@/api/fwgl/jhkh'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { downloadFile } from '@/utils/otherUtils'
  import ndkhView from './components/ndkhView.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { formatDay } from '@/utils'

  export default {
    name: 'NormalReportList',
    components: { ndkhView, filterTable, filterSearch, ProcessList },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          annualExamineName: '',
          Date: [],
          type: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '考核类型' },
          { name: '考核时间' },
          { name: '创建人' },
          { name: '创建时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-jhkh-ndkh-search',
        tableKey: 'fwgl-jhkh-ndkh-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      /**
       * @description: 下面四句：控制筛选项、表格的位置以及显示隐藏
       */
      this.initTable() //初始化表格 //初始化表格
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
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '考核名称', key: 'name' },
          { name: '考核类型', key: 'type' },
          { name: '时间', key: 'time' },
        ]
        return fields
      },
      /**
       * @description: 从上一次缓存中获取搜索项初始化
       * @return {*}
       */
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
      /**
       * @description: 展开收起查询条件
       * @return {*}
       */
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },

      // 动态表格开始
      /**
       * @description: 从上一次缓存中初始化表头
       * @return {*}
       */
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
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(36, row.annualExamineId)
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description: 重置查询条件
       * @return {*}
       */      
      resetQueryForm() {
        this.queryForm = {
          annualExamineName: '',
          examineBeginDate: '',
          examineEndDate: '',
          type: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      /**
       * @description: 重置并请求
       * @return {*}
       */      
      resetSearch() {
        this.resetQueryForm()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 请求数据
       * @return {*}
       */      
      async fetchData() {
        this.listLoading = false
        if (this.queryForm.type !== '')
          this.queryForm.type = Number(this.queryForm.type)
        let { Date } = this.queryForm

        if (Date) {
          this.queryForm.examineBeginDate = Date[0]
          this.queryForm.examineEndDate = Date[1]
        }
        const {
          data: { tlist, totalRecord },
        } = await getNDKHList(this.queryForm)
        this.listLoading = false
        this.list = tlist
        this.total = totalRecord
      },
      /**
       * @description: 导出
       * @return {*}
       */      
      async handleExport() {
        const res = await exportNDKH(this.queryForm)
        downloadFile(res, '年度计划列表.xlsx')
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['ndkhView'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleDetail(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['ndkhView'].showEdit('detail', row)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['ndkhView'].showEdit('edit', row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteNDKHList({ id: row.annualExamineId })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
