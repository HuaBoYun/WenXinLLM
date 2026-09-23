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
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="index"
              :prop="item.key"
            >
              <el-input
                v-model="queryForm.name1"
                clearable
                placeholder="预警模型名称"
                v-if="item.name === '预警模型名称'"
              />

              <el-select
                v-model="queryForm.status"
                placeholder="预警状态"
                v-if="item.name === '预警状态'"
              >
                <el-option label="已处理" :value="1" />
                <el-option label="未处理" :value="0" />
              </el-select>
              <el-input
                v-model="queryForm.content"
                clearable
                placeholder="警告内容"
                v-if="item.name === '警告内容'"
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
              <el-button @click="fetchData('reset')" type="primary">
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
      <vab-query-form-left-panel>
        <el-button
          type="primary"
          @click="handle"
          :disabled="select.length === 0"
        >
          批量处理
        </el-button>
      </vab-query-form-left-panel>
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
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        ref="multipleTable"
        :row-key="getRowKeys"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          width="48"
          type="selection"
          :reserve-selection="true"
        ></el-table-column>
        <el-table-column align="center" label="序号" type="index" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="警告内容"
            prop="content"
            v-if="item.name === '警告内容'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="预警模型ID"
            prop="id"
            v-if="item.name === '预警模型ID'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="预警模型名称"
            prop="name1"
            v-if="item.name === '预警模型名称'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="客户名称"
            prop="name2"
            v-if="item.name === '客户名称'"
          ></el-table-column>

          <el-table-column
            align="center"
            label="预警值"
            prop="name3"
            v-if="item.name === '预警值'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="风险值"
            prop="name4"
            v-if="item.name === '风险值'"
          ></el-table-column>

          <el-table-column
            v-if="item.name === '预警发生时间'"
            align="center"
            label="预警发生时间"
            prop="creatrtime"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            label="预警状态"
            prop="status"
            v-if="item.name === '预警状态'"
          >
            <template #default="{ row }">
              {{ row.status == 1 ? '已处理' : '未处理' }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="预警处理人"
            prop="creator"
            v-if="item.name === '预警处理人'"
          />

          <el-table-column
            v-if="item.name === '预警处理时间'"
            align="center"
            label="预警处理时间"
            prop="handletime"
            show-overflow-tooltip
            :formatter="formatDate"
          />
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleView(row)">查看</el-button>
            <el-button type="text" @click="handle(row)">处理</el-button>
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
    <Views ref="edit" @fetchData="fetchData"></Views>
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import Views from './components/fxyjglView.vue'
  import { UTCformat } from '@/utils'
  import { formatDay } from '@/utils/index'

  export default {
    name: 'fxyjgl',
    components: { filterSearch, filterTable, Views },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          name1: '',
        },
        filedAll: [
          { name: '警告内容' },
          { name: '预警模型ID' },
          { name: '预警模型名称' },
          { name: '客户名称' },
          { name: '预警值' },
          { name: '风险值' },
          { name: '预警发生时间' },
          { name: '预警状态' },
          { name: '预警处理人' },
          { name: '预警处理时间' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'monitor-yjgl-fxyjgl-search',
        tableKey: 'monitor-yjgl-fxyjgl-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        select: [],
      }
    },
    async created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      //格式化时间
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '预警模型名称', key: 'name1' },
          { name: '预警状态', key: 'status' },
          { name: '警告内容', key: 'content' },
        ]
        return fields
      },
      //初始化搜索栏
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
      // 查看更多
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
      //获取数据
      fetchData(type) {
        this.listLoading = true
        if (type && type === 'reset') {
          this.$refs['form'].resetFields()
        }

        // getReportList(this.queryForm).then((res) => {
        // this.list = res.data.tlist
        // this.total = res.data.totalRecord
        this.list = [
          {
            id: 1,
            name1: 'Ting提现',
            name2: '京东供应商',
            name3: '40',
            name4: '120',
            content: '京东供应商已被Ting提现提现检测到并报警，请及时处理',
            status: 0,
            creator: '星光',
            creatrtime: '2023-10-24',
            handletime: '',
          },
          {
            id: 2,
            name1: 'Ting提现',
            name2: '客户A',
            name3: '40',
            name4: '120',
            content: '客户A已被Ting提现提现检测到并报警，请及时处理',
            status: 1,
            creator: '星光',
            creatrtime: '2023-10-24',
            handletime: '',
          },
          {
            id: 3,
            name1: 'Ting提现',
            name2: '中科软科技有限公司',
            name3: '40',
            name4: '120',
            content: '客户A已被Ting提现提现检测到并报警，请及时处理',
            status: 0,
            creator: '星光',
            creatrtime: '2023-10-24',
            handletime: '',
          },
        ]
        this.listLoading = false
        // })
      },
      //分页大小
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      // 改变当前页数
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      //查看
      handleView(row) {
        this.$refs['edit'].showEdit(row)
      },
      //删除
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await reportDelete({ id: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      // 处理
      handle() {},

      // 2、设置row-key
      getRowKeys(row) {
        return row.id
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        this.select = selection.map((item) => item.id)
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
