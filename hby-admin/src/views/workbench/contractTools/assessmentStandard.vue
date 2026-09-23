<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form>
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
              v-model="queryForm.assNumber"
              clearable
              placeholder="编号"
              v-if="item.name === '编号'"
            />
            <el-input
              v-model="queryForm.assName"
              clearable
              placeholder="名称"
              v-if="item.name === '名称'"
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
            <el-button
              native-type="submit"
              @click="fetchData('reset')"
              type="primary"
            >
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
        <el-button type="success" @click="handleAdd()">
          <!-- v-if="hasAuth('FXPGBZadd')" -->
          新建
        </el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
        <el-table-column
          align="center"
          label="编号"
          prop="assnumber"
          sortable="custom"
        >
          <template #default="{ row }">
            <el-button type="text" @click="editDetail(row)">
              <!-- v-if="hasAuth('FXPGBZdetail')" -->
              {{ row.assnumber }}
            </el-button>
            <!-- <div v-else>{{ row.assnumber }}</div> -->
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="名称"
            prop="assname"
            show-overflow-tooltip
            v-if="item.name === '名称'"
          />
          <el-table-column
            align="center"
            label="描述"
            prop="assdes"
            show-overflow-tooltip
            v-if="item.name === '描述'"
          />
        </div>

        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">
              <!-- v-if="hasAuth('FXPGBZedit')" -->
              修改
            </el-button>
            <el-button type="text" @click="handleDelete(row)">
              <!-- v-if="hasAuth('FXPGBZdelete')" -->
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      class="pager"
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <!-- <StandardEdit ref="edit" @fetch-data="fetchData" /> -->
    <TabDetail ref="tab" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getAssessmentStandardList,
    deleteAssessmentStandardInfo,
  } from '@/api/systemLog'
  import StandardEdit from './components/StandardEdit'
  import TabDetail from './option/TabDetail.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'
  import { type } from 'os'
  import { hasAuth } from '@/utils'

  export default {
    name: 'StandardList',
    components: { StandardEdit, filterSearch, filterTable, TabDetail },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          assNumber: '',
          assName: '',
          pageNo: 1,
          pageSize: 20,
        },
        filedAll: [{ name: '名称' }, { name: '描述' }], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-contractTools-assessmentStandard-search',
        tableKey: 'workbench-contractTools-assessmentStandard-list',
        searchMore: true,
        sortFields: '',
        sortFlag: 'asc',
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
      getFiled() {
        return [
          { name: '编号', key: 'assNumber' },
          { name: '名称', key: 'assName' },
        ]
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData(type) {
        if (type && type === 'reset') {
          this.queryForm = {
            assNumber: '',
            assName: '',
            pageNo: 1,
            pageSize: 20,
          }
        }
        this.listLoading = true
        const {
          data: {
            risk: { list, total },
          },
        } = await getAssessmentStandardList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleEdit(row) {
        this.$refs['tab'].showEdit(row, 'edit')
      },
      handleAdd() {
        this.$refs['tab'].showEdit('', 'add')
      },
      editDetail(row) {
        this.$refs['tab'].showEdit(row, 'detail')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, data } = await deleteAssessmentStandardInfo({
            assstdid: row.assstdid,
          })
          if (data.code == 0) {
            return this.$message.error(data.result)
          }
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
