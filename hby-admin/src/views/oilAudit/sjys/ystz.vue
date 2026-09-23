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
            <el-input
              v-if="item.name === '问题线索'"
              v-model="queryForm.problemclue"
              clearable
              placeholder="问题线索"
            ></el-input>
            <el-input
              v-if="item.name === '核查结论'"
              v-model="queryForm.checkresult"
              clearable
              placeholder="核查结论"
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
          label="序号"
          prop="tznumber"
          show-overflow-tooltip
          v-if="item.name === '序号'"
        />
        <el-table-column
          align="center"
          label="移交时间"
          prop="yjtime"
          show-overflow-tooltip
          v-if="item.name === '移交时间'"
        />
        <el-table-column
          align="center"
          label="审计项目"
          prop="projectname"
          show-overflow-tooltip
          v-if="item.name === '审计项目'"
          width="200px"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit('详情', row)">
              {{ row.projectname }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="审计实施单位"
          prop="imporgname"
          show-overflow-tooltip
          v-if="item.name === '审计实施单位'"
        />
        <el-table-column
          align="center"
          label="问题线索"
          prop="problemclue"
          show-overflow-tooltip
          v-if="item.name === '问题线索'"
        />
        <el-table-column
          align="center"
          label="核查结论"
          prop="checkresult"
          show-overflow-tooltip
          v-if="item.name === '核查结论'"
        />

        <el-table-column
          align="center"
          label="处理情况"
          prop="situation"
          show-overflow-tooltip
          v-if="item.name === '处理情况'"
        />
        <el-table-column
          align="center"
          label="备注"
          prop="memo"
          show-overflow-tooltip
          v-if="item.name === '备注'"
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
    <ystzView ref="edit" @fetchData="fetchData" />
  </div>
</template>
<script>
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import ystzView from './components/ystzView.vue'
  import { ystzList, ystzDelete } from '@/oapi/audit/wgzrzj.js'
  export default {
    components: { ystzView, filterSearch, filterTable },
    name: 'ystz',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        listLoading: false,
        list: [],
        filedAll: [
          { name: '序号' },
          { name: '移交时间' },
          { name: '审计项目' },
          { name: '审计实施单位' },
          { name: '问题线索' },
          { name: '核查结论' },
          { name: '处理情况' },
          { name: '备注' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-sjys-ystz-search',
        tableKey: 'oilAudit-sjys-ystz-list',
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
          { name: '问题线索', key: 'problemclue' },
          { name: '核查结论', key: 'checkresult' },
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
        console.log('🚀 ~ handleEdit ~ name:', name)
        this.$refs.edit.show(name, row)
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await ystzList({ ...this.queryForm })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleDetail(a, b) {
        await this.$refs['edit'].showEdit(a, b)
      },
      resetQueryForm() {
        this.queryForm = {
          projectname: '',
          pageNumber: 1,
          pageSize: 20,
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
          const { msg, code } = await ystzDelete({
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
    },
  }
</script>
