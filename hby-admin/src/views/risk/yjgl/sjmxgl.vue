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
                placeholder="数据模型名称"
                v-if="item.name === '数据模型名称'"
              />
              <el-input
                v-model="queryForm.name2"
                clearable
                placeholder="适用行业"
                v-if="item.name === '适用行业'"
              />
              <el-input
                v-model="queryForm.content"
                clearable
                placeholder="导出平台"
                v-if="item.name === '导出平台'"
              />
              <el-select
                v-model="queryForm.status"
                placeholder="模型状态"
                v-if="item.name === '模型状态'"
              >
                <el-option label="启用" :value="1" />
                <el-option label="停用" :value="0" />
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
      <vab-query-form-left-panel></vab-query-form-left-panel>
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
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="序号" type="index" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="模型名称"
            prop="name1"
            v-if="item.name === '模型名称'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="适用行业"
            prop="name2"
            v-if="item.name === '适用行业'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="导出平台"
            prop="content"
            v-if="item.name === '导出平台'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="元数据存储路径"
            prop="name3"
            v-if="item.name === '元数据存储路径'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="目标数据库"
            prop="name4"
            v-if="item.name === '目标数据库'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="目标表"
            prop="name5"
            v-if="item.name === '目标表'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="模型状态"
            prop="status"
            v-if="item.name === '模型状态'"
          >
            <template #default="{ row }">
              <el-tag type="primary" v-if="row.status == 1">启用</el-tag>
              <el-tag type="info" v-else>停用</el-tag>
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleView(row)">查看</el-button>
            <el-button
              type="text"
              @click="handleStart(row)"
              v-if="row.status == 0"
            >
              启用
            </el-button>
            <el-button type="text" @click="handleStop(row)" v-else>
              停用
            </el-button>
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
  import Views from './components/sjmxglView.vue'
  import { UTCformat } from '@/utils'
  import { formatDay } from '@/utils/index'

  export default {
    name: 'sjmxgl',
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
          评估模型名称: '',
        },
        filedAll: [
          { name: '模型名称' },
          { name: '适用行业' },
          { name: '导出平台' },
          { name: '元数据存储路径' },
          { name: '目标数据库' },
          { name: '目标表' },
          { name: '模型状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'monitor-yjgl-sjmxgl-search',
        tableKey: 'monitor-yjgl-sjmxgl-list',
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
          { name: '数据模型名称', key: 'name1' },
          { name: '适用行业', key: 'name2' },
          { name: '导出平台', key: 'content' },
          { name: '模型状态', key: 'status' },
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
          this.queryForm.planName = ''
        }

        // getReportList(this.queryForm).then((res) => {
        // this.list = res.data.tlist
        // this.total = res.data.totalRecord
        this.list = [
          {
            id: 1,
            name1: '评估模型82',
            name2: '批发和零售业',
            content: '天眼查',
            name3: '//192.0.2.200/fileserver/file09.csv',
            name4: '风险库',
            name5: '表13',
            status: 0,
          },
          {
            id: 2,
            name1: '评估模型82',
            name2: '制造业',
            content: '天眼查',
            name3: '//192.0.2.200/fileserver/file03.csv',
            name4: '风险库',
            name5: '表1312',
            status: 1,
          },
          {
            id: 3,
            name1: '评估模型C3',
            name2: '服务业',
            content: '天眼查',
            name3: '//192.0.2.200/fileserver/file01.csv',
            name4: '系统库',
            name5: '表231',
            status: 0,
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
      // 启用
      handleStart() {},
      // 停用
      handleStop() {},
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
