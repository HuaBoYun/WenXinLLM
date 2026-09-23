<template>
  <div class="system-log-container">
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
              <el-select
                v-model="queryForm.reminderId"
                placeholder="催办类型"
                v-if="item.name === '催办类型'"
              >
                <el-option label="手动催办" value="2"></el-option>
                <el-option label="自动催办" value="1"></el-option>
              </el-select>
              <el-select
                v-model="queryForm.state"
                placeholder="催办状态"
                v-if="item.name === '催办状态'"
              >
                <el-option label="已催办" value="1"></el-option>
                <el-option label="未催办" value="0"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                type="primary"
                @click="queryData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="fetchData('reset')">
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="催办类型" prop="reminderId">
          <template #default="{ row }">
            <el-button type="text" @click="handleCBDetail(row)">
              {{ row.reminderId == '1' ? '自动催办' : '手动催办' }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="催办周期类型"
            prop="reminderType"
            v-if="item.name === '催办周期类型'"
          >
            <template #default="{ row }">
              {{ row.reminderType == '1' ? '月' : '周' }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="催办状态"
            prop="state"
            v-if="item.name === '催办状态'"
          >
            <template #default="{ row }">
              {{ row.state == '1' ? '已催办' : '未催办' }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="是否停止自动催办"
            prop="isStop"
            v-if="item.name === '是否停止自动催办'"
          >
            <template #default="{ row }">
              {{ row.isStop == '1' ? '停止催办' : '正常催办' }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="催办内容"
            prop="reminderContent"
            show-overflow-tooltip
            v-if="item.name === '催办内容'"
          />
        </div>
        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.state == '1' || createId != row.creator"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="handleCB(row)">
                  <el-button type="text">催办</el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleStop(row)">
                  <el-button
                    type="text"
                    :disabled="row.isStop == '1'"
                    v-if="row.reminderId == '1'"
                  >
                    停止
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleDelete(row)">
                  <el-button
                    type="text"
                    :disabled="row.state == '1' || createId != row.creator"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
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
    <Cuifa ref="cuifa" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getCbTableList,
    handleCuiBan,
    stopBList,
    deleteCBList,
  } from '@/api/risk/report'
  import { formatDay } from '@/utils/index'
  import Cuifa from './components/cuifa.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  export default {
    name: 'NormalReportList',
    components: {
      filterSearch,
      filterTable,
      Cuifa,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          state: '',
          reminderId: '',
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-cbgl-cbxx-search',
        tableKey: 'risk-cbgl-cbxx-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '催办类型' },
          { name: '催办周期类型' },
          { name: '催办状态' },
          { name: '是否停止自动催办' },
          { name: '催办内容' },
        ], //所有表格项
        filedNow: [],
        btnLoading: false,
        moduleRoute: 'zzfx', //智能分线
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    /**
     * @description: 流程提交回调
     * @return {*}
     */
    mounted() {},
    /**
     * @description: 提交审批
     * @return {*}
     */
    methods: {
      handleAdd() {
        this.$refs['cuifa'].showEdit(null, false, this.moduleRoute)
      },
      handleCBDetail(row) {
        this.$refs['cuifa'].showEdit(row, 'detail')
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '催办类型', key: 'reminderId' },
          { name: '催办状态', key: 'state' },
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
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      /**
       * @description: 下载附件
       * @return {*}
       */

      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description: 分页，初始化
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 重置数据
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          state: '',
          reminderId: '',
        }
      },
      /**
       * @description: 重置
       * @return {*}
       */
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      reloadTable() {
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      fetchData(type) {
        this.btnLoading = false
        this.listLoading = true
        if (type && type == 'reset') this.$refs['form'].resetFields()
        const { Date, ...other } = this.queryForm
        // let startDate = ''
        // let endDate = ''
        // if (Date) {
        //   startDate = Date[0]
        //   endDate = Date[1]
        // }
        getCbTableList({
          ...other,
          // startDate,
          // endDate,
          moduleRoute: this.moduleRoute,
        }).then((res) => {
          this.list = res.data.tlist
          this.total = res.data.totalRecord
          this.listLoading = false
        })
      },

      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteReport({ ids: row.reportid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleEdit(row) {
        // if (this.createId != row.creator) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$refs['cuifa'].showEdit(row, false)
      },
      handleCB(row) {
        handleCuiBan({ id: row.id }).then((res) => {
          if (res.code == 200) {
            this.$message.success('催办成功')
            this.fetchData()
          }
        })
      },
      async handleStop(row) {
        this.$baseConfirm('你确定要停止当前项吗', null, async () => {
          const { msg } = await stopBList({ id: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteCBList({ id: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
    },
  }
</script>
<style scoped lang="scss">
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
</style>
