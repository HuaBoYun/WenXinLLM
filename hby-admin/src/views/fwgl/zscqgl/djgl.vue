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
              <el-select
                v-model="queryForm.type"
                placeholder="类型"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '类型'"
              >
                <el-option label="商标" value="1"></el-option>
                <el-option label="版权" value="2"></el-option>
                <el-option label="专利" value="3"></el-option>
              </el-select>
              <el-input
                placeholder="名称"
                v-model="queryForm.registerName"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '名称'"
              />
              <el-date-picker
                v-model="queryForm.registerDate"
                end-placeholder="注册结束时间"
                range-separator="-"
                start-placeholder="注册开始时间"
                type="daterange"
                style="width: 240px; margin-right: 20px"
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
              <el-button native-type="submit" @click="resetQueryForm">
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
        <!-- <el-button type="success" @click="handleAdd">新建</el-button> -->
        <el-dropdown style="margin-right: 10px">
          <el-button type="success">
            新建
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="handleAdd(1)">
              商标
            </el-dropdown-item>
            <el-dropdown-item @click.native="handleAdd(2)">
              版权
            </el-dropdown-item>
            <el-dropdown-item @click.native="handleAdd(3)">
              专利
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-button @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="序号" type="index" />
        <el-table-column align="center" label="类别" prop="category">
          <template #default="{ row }">
            {{ row.type == '1' ? '商标' : row.type == '2' ? '版权' : '专利' }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="名称" prop="registerName">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleDetail(row)"
              style="color: #0f98fa"
            >
              {{ row.registerName }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="注册公告日期"
          prop="registerTime"
        />

        <el-table-column align="center" label="创建人" prop="creatorName" />
        <el-table-column align="center" label="创建时间" prop="createdTime" />

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
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
    <editModal ref="djglView" @fetchData="fetchData" />
  </div>
</template>

<script>
  import { getDJGLList, deleteDJGLList, exportDJGL } from '@/api/fwgl/zscq'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { downloadFile } from '@/utils/otherUtils'
  import editModal from './components/djglEdit.vue'

  export default {
    name: 'NormalReportList',
    components: { editModal, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 0,
          pageSize: 20,
          registerName: '',
          type: '',
          registerDate: [],
        },
        title: '',
        filedAll: [
          { name: '注册公告日期' },
          { name: '商标类别' },
          { name: '商标分类' },
          { name: '所属单位' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-zscqgl-djgl-search',
        tableKey: 'fwgl-zscqgl-djgl-list',
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
          { name: '类别', key: 'type' },
          { name: '名称', key: 'name' },
          { name: '注册时间', key: 'registerBeginDate' },
          { name: '创建人', key: 'people' },
          { name: '创建时间', key: 'peopleDate' },
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
       * @description: 重置查询条件
       * @return {*}
       */      
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 0,
          pageSize: 20,
          registerDate: [],
          type: '',
          trademarkName: '',
        }
        this.fetchData()
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 请求数据
       * @return {*}
       */      
      async fetchData() {
        const { registerDate, ...other } = this.queryForm
        let registerBeginDate = ''
        let registerEndDate = ''
        if (registerDate) {
          registerBeginDate = registerDate[0]
          registerEndDate = registerDate[1]
        }
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getDJGLList({ registerBeginDate, registerEndDate, ...other })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleRead(row) {
        this.$refs['read'].showRead(row)
      },
      handleAdd(type) {
        this.$refs['djglView'].showEdit('add', null, type)
      },
      /**
       * @description: 导出
       * @return {*}
       */      
      async handleExport() {
        this.listLoading = true
        const res = await exportDJGL(this.queryForm)
        downloadFile(res, '登记管理列表.xlsx')
        this.listLoading = false
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        await this.$refs['djglView'].showEdit('edit', row, row.type)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleDetail(row) {
        await this.$refs['djglView'].showEdit('detail', row, row.type)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteDJGLList({ id: row.registerManagementId })
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
