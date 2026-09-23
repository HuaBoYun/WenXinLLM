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
                v-model="queryForm.templeteName"
                clearable
                placeholder="模板名称"
                v-if="item.name === '模板名称'"
              />
              <el-select
                v-model="queryForm.status"
                placeholder="状态"
                v-if="item.name === '状态'"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in statusList"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
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
          <el-button type="success" @click="handleAdd">新建</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="模板编号" prop="templeteCode">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.templeteCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <!-- <el-table-column align="center" label="模板编号" prop="templeteCode" /> -->
          <el-table-column align="center" v-if="item.name === '模板名称'" label="模板名称" prop="templeteName" />
          <el-table-column align="center" v-if="item.name === '审计类型'" label="审计类型" prop="templeteType" />
          <el-table-column align="center" v-if="item.name === '适用机构'" label="适用机构" prop="temorgname" />
          <el-table-column align="center" v-if="item.name === '创建人'" label="创建人" prop="createstaffname" />
          <el-table-column align="center" v-if="item.name === '创建日期'" label="创建日期" prop="createDate" :formatter="formatDate" />
          <el-table-column align="center" v-if="item.name === '状态'" label="状态" prop="status">
            <template #default="{ row }">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </template>
          </el-table-column>
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-dropdown style="margin-left: 10px" @command="handleCommand">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu>
                <el-dropdown-item @click.native="handleDelete(row)">
                  删除
                </el-dropdown-item>
                <el-dropdown-item @click.native="changeStatus(row)">
                  {{ row.status === 1 ? '禁用' : '启用' }}
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleCopy(row, 0)">
                  <!-- 复制到审计模板 -->
                  复制到实施审计指引
                </el-dropdown-item>
                <!-- <el-dropdown-item @click.native="handleCopy(row, 1)">
                  复制到指引模板
                </el-dropdown-item> -->
              </el-dropdown-menu>
            </el-dropdown>
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
    <LcdyEdit ref="edit" @fetch-data="fetchData" />
    <Tables ref="table" />
  </div>
</template>

<script>
  import {
    copyTemplete,
    deleteTempleteInfo,
    getNbsjjyTempletePageList,
    updateTempleteStatus,
  } from '@/api/workbench/auditTools'
  import { UTCformat } from '@/utils/index'
  import Tables from '@/views/workbench/auditTools/components/options/table'
  import LcdyEdit from './components/guideView.vue'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Consult',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, LcdyEdit, Tables },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          status: '',
          templeteName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        statusList: [
          { label: '启用', value: '1' },
          { label: '禁用', value: '-1' },
        ],
        // 筛选列表配置
        filedAll: [
          { name: '模板名称' },
          { name: '审计类型' },
          { name: '适用机构' },
          { name: '创建人' },
          { name: '创建日期' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-controlLib-guide-search',
        tableKey: 'workbench-controlLib-guide-list',
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
          { name: '模板名称', key: 'templeteName' },
          { name: '状态', key: 'status' },
        ]
      },
      handleDetail(row) {
        this.$refs['table'].show(row.templeteId)
      },
      resetQueryForm() {
        this.queryForm = {
          status: '',
          templeteName: '',
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
        return UTCformat(data)
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
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getNbsjjyTempletePageList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteTempleteInfo(row.templeteId)
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      changeStatus(row) {
        this.$baseConfirm('你确定要修改当前项状态吗', null, async () => {
          const { msg } = await updateTempleteStatus(row.templeteId)
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      handleCopy(row, num) {
        this.$baseConfirm('你确定要复制当前项吗', null, async () => {
          const { msg } = await copyTemplete({
            copytype: num,
            templeteId: row.templeteId,
          })
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
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
