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
                v-model="queryForm.topic"
                clearable
                placeholder="主题"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '主题'"
              ></el-input>
              <el-date-picker
                v-model="queryForm.createTime"
                type="daterange"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                range-separator=":"
                placeholder="学习时间"
                style="width: 340px; margin-right: 20px"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-if="item.name === '学习时间'"
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
        <el-dropdown style="margin-right: 10px">
          <el-button type="success">
            新建
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="handleAdd('中心组学习')">
              中心组学习
            </el-dropdown-item>
            <el-dropdown-item @click.native="handleAdd('培训研讨')">
              培训研讨
            </el-dropdown-item>
            <el-dropdown-item @click.native="handleAdd('普法宣传')">
              普法宣传
            </el-dropdown-item>
            <el-dropdown-item @click.native="handleAdd('其他')">
              其他
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <!-- <el-button @click="handleExport">导出</el-button> -->
      </vab-query-form-right-panel>
      <el-table :data="list">
        <el-table-column align="center" label="序号" prop="data">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="单位"
            prop="workUnitName"
            v-if="item.name === '单位'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row.leaderType, row)">
                {{ row.workUnitName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="主题"
            prop="centralGroupTopic"
            v-if="item.name === '主题'"
          >
            <template #default="{ row }">
              {{
                row.discussTopic ||
                row.centralGroupTopic ||
                row.propagandaTopic ||
                row.otherTopic
              }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="学习时间"
            prop="createdTime"
            v-if="item.name === '学习时间'"
          >
            <template #default="{ row }">
              {{ formatDate(row.createdTime) }}
            </template>
          </el-table-column>
        </div>
        <el-table-column
          align="center"
          label=""
          prop="data"
          width="1"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row.leaderType, row)"
              v-if="String(userInfo.staffid) === String(row.creator)"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="String(userInfo.staffid) === String(row.creator)"
            >
              删除
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

    <ProcessList ref="process" @fetchData="fetchData" />
    <ZxzxxVue ref="zxzxx" @fetch-data="fetchData" />
    <PxytVue ref="pxyt" @fetch-data="fetchData" />
    <PfxcVue ref="pfxc" @fetch-data="fetchData" />
    <OtherVue ref="other" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getLDXFList, deleteLDXFList } from '@/api/fwgl/pfpx'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { downloadFile } from '@/utils/otherUtils'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import ZxzxxVue from './components/ldxfEdit/zxzxx.vue'
  import PxytVue from './components/ldxfEdit/pxyt.vue'
  import PfxcVue from './components/ldxfEdit/pfxc.vue'
  import OtherVue from './components/ldxfEdit/other.vue'
  import { formatDate } from '@/utils'

  export default {
    name: 'NormalReportList',
    components: {
      filterTable,
      filterSearch,
      ProcessList,
      ZxzxxVue,
      PxytVue,
      PfxcVue,
      OtherVue,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          topic: '',
          createTime: '',
          pageNumber: 1,
          pageSize: 20,
        },
        formatDate,
        filedAll: [{ name: '单位' }, { name: '主题' }, { name: '学习时间' }], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-pfpx-ldxf-search',
        tableKey: 'fwgl-pfpx-ldxf-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        userInfo: JSON.parse(localStorage.getItem('userInfo')),
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
          { name: '主题', key: 'topic' },
          { name: '学习时间', key: 'createTime' },
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
          annualPlanName: '',
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
        this.queryForm.pageNumber = 1
        this.fetchData()
      },

      async fetchData() {
        this.listLoading = false
        if (this.queryForm.createTime) {
          this.queryForm.startTime = this.queryForm.createTime[0] || 0
          this.queryForm.endTime = this.queryForm.createTime[1] || 0
        }
        const {
          data: { tlist, totalRecord },
        } = await getLDXFList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      /**
       * @description: 导出
       * @return {*}
       */      
      async handleExport() {
        this.listLoading = true
        const res = await exportNDJH(this.queryForm)
        downloadFile(res, '年度计划列表.xlsx')
        this.listLoading = false
      },
      handleAdd(type) {
        if (type == '中心组学习') {
          this.$refs['zxzxx'].showEdit()
        }
        if (type == '培训研讨') {
          this.$refs['pxyt'].showEdit()
        }
        if (type == '普法宣传') {
          this.$refs['pfxc'].showEdit()
        }
        if (type == '其他') {
          this.$refs['other'].showEdit()
        }
      },
      handleDetail(type, row) {
        if (type == '1') {
          this.$refs['zxzxx'].showEdit('detail', row)
        }
        if (type == '2') {
          this.$refs['pxyt'].showEdit('detail', row)
        }
        if (type == '3') {
          this.$refs['pfxc'].showEdit('detail', row)
        }
        if (type == '4') {
          this.$refs['other'].showEdit('detail', row)
        }
      },
      async handleEdit(type, row) {
        if (type == '1') {
          this.$refs['zxzxx'].showEdit('edit', row)
        }
        if (type == '2') {
          this.$refs['pxyt'].showEdit('edit', row)
        }
        if (type == '3') {
          this.$refs['pfxc'].showEdit('edit', row)
        }
        if (type == '4') {
          this.$refs['other'].showEdit('edit', row)
        }
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteLDXFList({ id: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
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
