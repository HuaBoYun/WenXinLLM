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
                v-model="queryForm.conferenceName"
                clearable
                placeholder="会议名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '会议名称'"
              ></el-input>

              <!-- <el-date-picker
                v-model="queryForm.conferenceBeginDate"
                placeholder="会议开始时间"
                type="datetime"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                v-if="item.name === '会议开始时间'"
              />
              <el-date-picker
                v-model="queryForm.conferenceEndDate"
                placeholder="会议结束时间"
                type="datetime"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                v-if="item.name === '会议结束时间'"
              /> -->

              <el-input
                v-model="queryForm.compere"
                clearable
                placeholder="会议主持人"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '会议主持人'"
              ></el-input>

              <el-date-picker
                align="right"
                end-placeholder="会议开始时间"
                range-separator="至"
                format="yyyy-MM-dd"
                start-placeholder="会议结束时间"
                type="daterange"
                unlink-panels
                v-model="queryForm.Date"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 360px; margin-right: 20px"
                v-if="item.name === '会议时间'"
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
        <el-table-column align="center" label="会议名称" prop="conferenceName">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.conferenceName }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="会议主持人"
            prop="compereName"
            v-if="item.name === '会议主持人'"
          />
          <el-table-column
            align="center"
            label="会议时间"
            prop="conferenceTime"
            v-if="item.name === '会议时间'"
          />
          <!-- <el-table-column
            align="center"
            label="创建人"
            prop="creator"
            v-if="item.name === '创建人'"
          /> -->
          <el-table-column
            align="center"
            label="创建时间"
            prop="createdTime"
            v-if="item.name === '创建时间'"
          />
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
            <el-button type="text" @click="handlePush(row)">推送</el-button>
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
    <hyglView ref="hyglView" :fetchData="fetchData" />
  </div>
</template>

<script>
  import hyglView from './components/hyglView.vue'
  import filterTable from '@/components/filterTable.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import { dailyManagementConference, fetchApi } from '@/api/fwgl/api'
  import { pushRCGLInfo } from '@/api/fwgl/gsls'
  import { downloadFile } from '@/utils/otherUtils'

  const { getList, doDelete, exportDate } = dailyManagementConference

  export default {
    name: 'NormalReportList',
    components: { hyglView, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          compere: '',
          conferenceBeginDate: '',
          conferenceEndDate: '',
          Date: '',
          conferenceName: '',
          pageNumber: 1,
          pageSize: 20,
          isHome: 0,
        },
        filedAll: [
          { name: '会议主持人' },
          { name: '会议时间' },
          { name: '创建人' },
          { name: '创建时间' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-rcgl-hygl-search',
        tableKey: 'fwgl-rcgl-hygl-list',
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
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '会议名称', key: 'conferenceName' },
          { name: '会议时间', key: 'Date' },

          { name: '会议主持人', key: 'compere' },
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
          compere: '',
          conferenceBeginDate: '',
          conferenceEndDate: '',
          Date: '',
          conferenceName: '',
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
      /**
       * @description: 数据请求
       * @param {*} type
       * @return {*}
       */
      async fetchData(type) {
        this.listLoading = false
        const { Date } = this.queryForm

        if (Date) {
          this.queryForm.conferenceBeginDate = Date[0]
          this.queryForm.conferenceEndDate = Date[1]
        }

        const {
          data: { tlist, totalRecord },
        } = await fetchApi(getList, this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 导出
       * @return {*}
       */
      async handleExport() {
        this.listLoading = true

        const exportParams = {
          // 可选参数：根据查询条件传递
          conferenceName: this.queryForm.conferenceName || '',
          compere: this.queryForm.compere || '',
          // 导出固定分页参数
          pageNumber: 1,
          pageSize: 20000,
          // 会议时间范围处理
          conferenceBeginDate: this.queryForm.conferenceBeginDate || '',
          conferenceEndDate: this.queryForm.conferenceEndDate || '',
          // 是否在首页显示（0=是，1=否）
          isHome: 0,
          // 注意：token 不在此处传递，transData 函数会自动添加
        }

        const res = await fetchApi(exportDate, exportParams, true)
        downloadFile(res, '会议管理列表.xlsx')
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['hyglView'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleDetail(row) {
        await this.$refs['hyglView'].showEdit('detail', {
          id: row.conferenceId,
        })
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        await this.$refs['hyglView'].showEdit('edit', {
          id: row.conferenceId,
        })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          await fetchApi(doDelete, {
            id: row.conferenceId,
          })
          this.$baseMessage('操作成功', 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      async handlePush(row) {
        await pushRCGLInfo({
          id: row.conferenceId,
        })
        this.$baseMessage('推送成功', 'success', 'vab-hey-message-success')
        await this.fetchData()
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
