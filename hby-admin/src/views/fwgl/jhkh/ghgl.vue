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
                v-model="queryForm.planManagementName"
                clearable
                placeholder="名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '名称'"
              ></el-input>

              <el-select
                :style="{ width: '100%' }"
                v-model="queryForm.type"
                placeholder="规划类型"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '规划类型'"
              >
                <el-option label="法治建设规划" value="法治建设规划" />
                <el-option label="普法规划" value="普法规划" />
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
        <el-table-column align="center" label="规划类型" prop="type" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="登记类型"
            prop="registerType"
            v-if="item.name === '登记类型'"
          />
          <el-table-column
            align="center"
            label="名称"
            prop="planManagementName"
            v-if="item.name === '名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.planManagementName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="创建人"
            prop="planManagementCreator"
            v-if="item.name === '创建人'"
          />
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
            <!-- <el-button type="text">导出</el-button> -->
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <ghglView ref="ghglView" @fentch="fetchData" />
  </div>
</template>

<script>
  import { deleteGHGLList, getGHGLList, exportGHGL } from '@/api/fwgl/jhkh'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ghglView from './components/ghglView.vue'
  import { downloadFile } from '@/utils/otherUtils'

  export default {
    name: 'NormalReportList',
    components: { ghglView, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          type: '',
          planManagementName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '登记类型' },
          { name: '名称' },
          { name: '创建人' },
          { name: '创建时间' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-jhkh-ghgl-search',
        tableKey: 'fwgl-jhkh-ghgl-list',
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
          { name: '名称', key: 'planManagementName' },
          { name: '规划类型', key: 'type' },
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
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 重置查询条件
       * @return {*}
       */      
      resetQueryForm() {
        this.queryForm = {
          type: '',
          planManagementName: '',
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
        const {
          data: { tlist, totalRecord },
        } = await getGHGLList(this.queryForm)
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
        const res = await exportGHGL(this.queryForm)
        downloadFile(res, '规划管理列表.xlsx')
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['ghglView'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleDetail(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['ghglView'].showEdit('detail', row)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['ghglView'].showEdit('edit', row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteGHGLList({ id: row.planManagementId })
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
