<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item v-for="(item, index) in searchItem" :key="index">
            <div
              style="display: flex; align-items: center"
              v-if="item.name === '移交单位'"
            >
              <el-input
                v-model="queryForm.cluename"
                clearable
                placeholder="移交单位"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.company.showEdit()"
                size="small"
              >
                选择
              </el-button>
            </div>
            <el-date-picker
              v-model="queryForm.impcreateusername"
              placeholder="移交时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              v-if="item.name === '移交时间'"
            />
            <el-input
              v-model="queryForm.verifycontent"
              placeholder="事项"
              v-if="item.name === '事项'"
            ></el-input>
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
        </el-form>
      </vab-query-form-top-panel>
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
        <el-button type="success" @click="handleEdit('新增', null)">
          新建
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <div v-for="(item, index) in filedNow" :key="index">
        <el-table-column
          align="center"
          label="移交单位"
          prop="cluename"
          show-overflow-tooltip
          v-if="item.name === '移交单位'"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit('详情', row)">
              {{ row.cluename }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="移交时间"
          prop="impcreateusername"
          show-overflow-tooltip
          v-if="item.name === '移交时间'"
        />
        <el-table-column
          align="center"
          label="事项"
          prop="verifycontent"
          show-overflow-tooltip
          v-if="item.name === '事项'"
        />
        <el-table-column
          align="center"
          label="事项概要"
          prop="cluehsfw"
          show-overflow-tooltip
          v-if="item.name === '事项概要'"
        />
        <el-table-column
          align="center"
          label="初步核实结果"
          prop="cluegzzz"
          show-overflow-tooltip
          v-if="item.name === '初步核实结果'"
        />
        <el-table-column
          align="center"
          label="分类处置意见"
          prop="cluezrdx"
          show-overflow-tooltip
          v-if="item.name === '分类处置意见'"
        />
        <el-table-column
          align="center"
          label="领导机构负责人审批"
          prop="cluessrd"
          show-overflow-tooltip
          v-if="item.name === '领导机构负责人审批'"
        />
      </div>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="180"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit('编辑', row)">
            修改
          </el-button>
          <el-button type="text" @click="deleteData(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <flczyjView ref="edit" @fetchData="fetchData" />
    <SelectDepartment ref="company" @submit="selectedCompany" />
  </div>
</template>
<script>
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import flczyjView from './components/flczyjView.vue'
  import { flczyjList, flczyjDelete } from '@/oapi/audit/wgzrzj.js'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'

  export default {
    components: { filterTable, filterSearch, flczyjView, SelectDepartment },
    name: 'flczyj',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          cluename: '',
          clueid: '',
          impcreateusername: '',
          verifycontent: '',
        },
        listLoading: false,
        list: [],
        filedAll: [
          { name: '移交单位' },
          { name: '移交时间' },
          { name: '事项' },
          { name: '事项概要' },
          { name: '初步核实结果' },
          { name: '分类处置意见' },
          { name: '领导机构负责人审批' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-wgzrzj-flczyj-search',
        tableKey: 'oilAudit-wgzrzj-flczyj-list',
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {},
    methods: {
      getFiled() {
        return [
          { name: '移交单位', key: 'cluename' },
          { name: '移交时间', key: 'impcreateusername' },
          { name: '事项', key: 'verifycontent' },
        ]
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
      handleEdit(name, row) {
        this.$refs.edit.show(name, row)
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await flczyjList({ ...this.queryForm })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleDetail(a, b) {
        await this.$refs['edit'].showEdit(a, b)
      },
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          cluename: '',
          clueid: '',
          impcreateusername: '',
          verifycontent: '',
        }
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      deleteData(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await flczyjDelete({
            id: row.id,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      selectedCompany(node) {
        console.log('🚀 ~ node:', node)
        this.queryForm.cluename = node.label
        this.queryForm.clueid = node.id
      },
    },
  }
</script>
