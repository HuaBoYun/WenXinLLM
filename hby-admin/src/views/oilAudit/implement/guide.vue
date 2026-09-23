<template>
  <!-- 审计承诺书 -->
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
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.sheetcode"
                clearable
                placeholder="编号"
                v-if="item.name === '编号'"
              />

              <el-input
                v-model="queryForm.sheetname"
                clearable
                v-if="item.name === '名称'"
                placeholder="名称"
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
                type="primary"
                @click="resetSearch"
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
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>
    <el-card shadow="never">
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
        <el-button v-if="isShow" type="success" @click="handleEdit(null)">
          新增
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="项目名称">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.projectname }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <!-- <el-table-column
            v-if="item.name === '名称'"
            align="center"
            label="名称"
            prop="lettername"
          /> -->
          <el-table-column
            v-if="item.name === '创建人'"
            align="center"
            label="创建人"
            prop="realname"
          />
          <el-table-column
            v-if="item.name === '创建时间'"
            align="center"
            label="创建时间"
            prop="createtime"
          />
        </div>
        <el-table-column width="1" />
        <el-table-column
          v-if="isShow"
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
        >
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <guideView ref="edit" @fetchData="fetchData" />
  </div>
</template>

<script>
  import guideView from './components/guideView'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import {
    auditCommitmentLetterList,
    auditCommitmentLetterDeleteOne,
  } from '@/oapi/audit/implement'

  export default {
    name: 'fwxqb',
    components: {
      guideView,
      filterSearch,
      filterTable,
    },
    mixins: [searchTableMixis],
    props: {
      isShow: {
        type: Boolean,
        default: true,
      },
      projectId: {
        type: Number,
        default: null,
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          sheetcode: '',
          sheetname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [{ name: '名称' }, { name: '创建人' }, { name: '创建时间' }], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-jhlx-sjcns-search',
        tableKey: 'oilAudit-jhlx-sjcns-list',
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
          // { name: '编号', key: 'sheetcode' },
          { name: '名称', key: 'sheetname' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          sheetcode: '',
          sheetname: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const { data, code } = await auditCommitmentLetterList({
          ...this.queryForm,
          projectId: this.projectId,
        })
        if (code === 1) {
          this.list = data.data.tlist || []
          this.total = data.data.totalRecord || 0
          this.listLoading = false
        }
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit(row, true)
      },
      async handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      async handleDelete(row) {
        // 删除
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await auditCommitmentLetterDeleteOne({
            letterid: row.letterid,
          })
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
