<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-if="item.name === '线索编号'"
                v-model="queryForm.clueNaber"
                clearable
                placeholder="线索编号"
              />
              <el-input
                v-model="queryForm.cluesource"
                v-if="item.name === '线索来源'"
                clearable
                placeholder="线索来源"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
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
        </vab-query-form-left-panel>
      </el-card>

      <!-- <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel> -->
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
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '线索编号'"
            align="center"
            label="线索编号"
            prop="cluenaber"
            #default="{ row }"
          >
            <el-button type="text" @click="handleDetail(row)">
              {{ row.cluenaber }}
            </el-button>
          </el-table-column>

          <el-table-column
            align="center"
            v-if="item.name === '涉及单位'"
            label="涉及单位"
            prop="clueunitnename"
          />
          <el-table-column
            align="center"
            v-if="item.name === '涉及责任人'"
            label="涉及责任人"
            prop="cluehandlingname"
          />
          <el-table-column
            align="center"
            v-if="item.name === '报送时间'"
            label="报送时间"
            prop="messagetime"
          />

          <el-table-column
            align="center"
            label="报送方式"
            prop="messagemanner"
            v-if="item.name === '报送方式'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="线索来源"
            v-if="item.name === '线索来源'"
            prop="cluesource"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="发生时间"
            v-if="item.name === '发生时间'"
            prop="occurrencetime"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="主要问题线索"
            v-if="item.name === '主要问题线索'"
            prop="mainclue"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="是否受理"
            v-if="item.name === '是否受理'"
            prop="isaccepted"
            show-overflow-tooltip
            #default="{ row }"
          >
            {{ row.isaccepted == 1 ? '是' : '否' }}
          </el-table-column>
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="180"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.isaccepted != 0"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleAcceptance(row)"
                    :disabled="row.isaccepted != 0"
                  >
                    受理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="row.isaccepted != 0"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      <wgzzEdit ref="edit" @fetch-data="fetchData" />
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
  </div>
</template>

<script>
  import { wgzzList, wgzzXQList, deletewgzz, wgzzAdd } from '@/api/audit/wgzz'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import wgzzEdit from './components/wgzzEdit'
  export default {
    name: 'wgzz',
    components: { filterSearch, filterTable, wgzzEdit },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          clueNaber: '',
          // cluesource: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '线索编号' },
          { name: '涉及单位' },
          { name: '涉及责任人' },
          { name: '报送时间' },
          { name: '报送方式' },
          { name: '线索来源' },
          { name: '发生时间' },
          { name: '主要问题线索' },
          { name: '是否受理' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-standingbook-search',
        tableKey: 'audit-rectify-standingbook-list',
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
      // 定义表单所有项
      getFiled() {
        return [
          { name: '线索编号', key: 'clueNaber' },
          // { name: '线索来源', key: 'cluesource' },
        ]
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          // cluesource: '',
          clueNaber: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await wgzzList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        console.log('row', row)
        const res = await wgzzXQList({ clueid: row.clueid })
        await this.$refs['edit'].showEdit('detail', res.data.pan)
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        console.log(111, row)
        this.$refs['edit'].showEdit('edit', row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deletewgzz({ clueid: row.clueid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleAcceptance(row) {
        this.$baseConfirm('你确定要受理当前项吗', null, async () => {
          this.listLoading = true
          const res = await wgzzAdd({
            isaccepted: 1,
            clueid: row.clueid,
          })
          this.listLoading = false
          if (res.code == 1) {
            this.$message.success('受理成功！')
            this.fetchData()
          }
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
