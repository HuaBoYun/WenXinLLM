<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.code"
                clearable
                placeholder="模板编号"
                v-if="item.name === '模板编号'"
              />
              <el-input
                v-model="queryForm.name"
                clearable
                placeholder="模板名称"
                v-if="item.name === '模板名称'"
              />
              <el-input
                v-model="queryForm.auditype"
                clearable
                placeholder="审计类型"
                v-if="item.name === '审计类型'"
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
              <el-button native-type="submit" type="primary" @click="resetSearch">
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
            <el-form-item style="cursor: pointer">
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

    <el-card shadow="never">
      <vab-query-form>
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
              <el-button
                slot="reference"
                icon="el-icon-s-grid"
                class="biaoge"
                style="margin-bottom: 10px; margin-right: 10px"
              ></el-button>
            </el-popover>
          </el-tooltip>
          <el-button type="success" @click="handleEdit(false, '新建', null)">
            新建
          </el-button>        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column width="1" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="模板编码"
            v-if="item.name === '模板编码'"
            prop="mbcode"
            width="200"
          />
          <el-table-column
            align="center"
            label="模板名称"
            v-if="item.name === '模板名称'"
            prop="mbname"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <el-button @click="handleEdit(true, '详情', scope.row)" type="text">
                {{ scope.row.mbname }}
              </el-button>
            </template>
          </el-table-column>
    
          <el-table-column
            align="center"
            width="100"
            label="模板类型"
            v-if="item.name === '模板类型'"
            prop="audittype"
            v-model="queryForm.audittype"
          />
          <el-table-column
            align="center"
            label="人员信息"
            v-if="item.name === '人员信息'"
            prop="createStaff"
            v-model="queryForm.createStaff"
          >
            <template slot-scope="scope">
              {{ scope.row.createStaff.realname }}
            </template>
          </el-table-column>
        </div>
  
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(false, '修改', scope.row)">
              修改
            </el-button>
            <el-button type="text" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <Edit ref="edit" @fetch-data="fetchData" />
  </div>
</template>
<script>
  import { getSJMBKList, sjmbkDelete } from '@/api/workbench/auditTools'
  import Edit from './components/sjmbkEdit.vue'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, Edit },
    name: 'xxx',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          code: '',
          name: '',
          auditype: '',
        },
        list: [],
        listLoading: true,
        // 筛选列表配置
        filedAll: [
          { name: '模板编码' },
          { name: '模板名称' },
          { name: '模板类型' },
          { name: '人员信息' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-controlLib-sjmbk-search',
        tableKey: 'workbench-controlLib-sjmbk-list',
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [
          { name: '模板编号', key: 'code' },
          { name: '模板名称', key: 'name' },
          { name: '审计类型', key: 'auditype' },
        ]
      },
      handleDetail(row) {},

      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getSJMBKList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleEdit(disabled, type, row) {
        this.$refs['edit'].showEdit(disabled, type, row)
      },

      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await sjmbkDelete({ mbid: row.mbid })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
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
  .margin-b0 {
    margin-bottom: 0;
  }
</style>