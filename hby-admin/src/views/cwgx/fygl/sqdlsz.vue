<!-- 授权代理设置 -->
<template>
  <div class="container">
    <div class="container_left">
      <el-input
        placeholder="请搜索"
        v-model="inputValue"
        clearable
        class="search-input"
      ></el-input>
      <el-tree
        :data="treeData"
        :props="defaultProps"
        @node-click="handleNodeClick"
      ></el-tree>
    </div>
    <div class="system-log-container">
      <vab-query-form>
        <el-card shadow="never">
          <vab-query-form-left-panel :span="24">
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item v-for="(item, index) in searchItem" :key="index">
                <el-input
                  v-model="queryForm.jsFinance"
                  clearable
                  placeholder="业务单元"
                  v-if="item.name === '业务单元'"
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
        <div class="top_title">特殊代理</div>
        <el-row>
          <el-col :span="6">
            <el-switch
              inactive-text="代理部门"
              v-model="queryForm.department"
              active-color="#13ce66"
              inactive-color="#ccc"
            ></el-switch>
          </el-col>
          <el-col :span="6">
            <el-switch
              inactive-text="代理所有人"
              v-model="queryForm.owner"
              active-color="#13ce66"
              inactive-color="#ccc"
            ></el-switch>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="never" class="secondCard">
        <div class="top__bpx">
          <div class="title">可代理部门</div>
          <el-button type="success" @click="handleEdit(false, false)">
            新建
          </el-button>
        </div>

        <el-table v-loading="listLoading" :data="list">
          <el-table-column
            align="center"
            label="序号"
            prop="qdcode"
            width="100"
          ></el-table-column>
          <el-table-column
            align="center"
            label="部门"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="所属组织"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template slot-scope="scope">
              <el-button
                type="text"
                @click="handleEdit(scope.row, false)"
                :disabled="!!scope.row.spzt"
              >
                修改
              </el-button>
              <el-dropdown style="margin-left: 10px">
                <el-button type="text">更多</el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>
                    <el-button
                      @click="handleDelete(scope.row)"
                      type="text"
                      :disabled="!!scope.row.spzt"
                    >
                      删除
                    </el-button>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
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

      <el-card shadow="never" class="secondCard_2">
        <div class="top__bpx">
          <div class="title">可代理人员</div>
          <el-button type="success" @click="handlePersonnelEdit(false, false)">
            新建
          </el-button>
        </div>

        <el-table v-loading="listLoading" :data="list">
          <el-table-column
            align="center"
            label="序号"
            prop="qdcode"
            width="100"
          ></el-table-column>
          <el-table-column
            align="center"
            label="业务员"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="所属组织"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template slot-scope="scope">
              <el-button
                type="text"
                @click="handlePersonnelEdit(scope.row, false)"
                :disabled="!!scope.row.spzt"
              >
                修改
              </el-button>
              <el-dropdown style="margin-left: 10px">
                <el-button type="text">更多</el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>
                    <el-button
                      @click="handleDelete(scope.row)"
                      type="text"
                      :disabled="!!scope.row.spzt"
                    >
                      删除
                    </el-button>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
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

      <Edit ref="edit" @fetch-data="fetchData"></Edit>
      <personnelEdit ref="personnel" @fetch-data="fetchData"></personnelEdit>
    </div>
  </div>
</template>

<script>
  import {
    implementPlanList,
    implementPlanDelete,
    fpzyksry,
  } from '@/oapi/audit/project'
  import Edit from './components/departmentEdit'
  import personnelEdit from './components/personnelEdit'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'sqdlsz',
    mixins: [searchTableMixis],
    components: {
      Edit,
      personnelEdit,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          startStatus: undefined,
          sxName: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
          department: '',
          owner: '',
        },
        currProjectId: '',
        filedAll: [
          { name: '报销类型' },
          { name: '部门' },
          { name: '职位' },
          { name: '币种' },
          { name: '金额' },
          { name: '备注' },
          { name: '席位' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'cwgx-fygl-sqdlsz-search',
        tableKey: 'cwgx-fygl-sqdlsz-list',
        searchMore: false,
        select: [],
        treeData: [
          {
            label: '一级 1',
            children: [
              {
                label: '二级 1-1',
                children: [
                  {
                    label: '三级 1-1-1',
                  },
                ],
              },
            ],
          },
          {
            label: '一级 2',
            children: [
              {
                label: '二级 2-1',
                children: [
                  {
                    label: '三级 2-1-1',
                  },
                ],
              },
              {
                label: '二级 2-2',
                children: [
                  {
                    label: '三级 2-2-1',
                  },
                ],
              },
            ],
          },
          {
            label: '一级 3',
            children: [
              {
                label: '二级 3-1',
                children: [
                  {
                    label: '三级 3-1-1',
                  },
                ],
              },
              {
                label: '二级 3-2',
                children: [
                  {
                    label: '三级 3-2-1',
                  },
                ],
              },
            ],
          },
        ],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        inputValue: '',
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      getFiled() {
        return [{ name: '业务单元', key: 'sxName' }]
      },
      selectTeamList(val, flagTitle) {
        console.log(val, flagTitle)
        if (flagTitle) {
          this.queryForm.projectOrderName = val[0].realname
          this.queryForm.projectOrderId = val[0].staffid
        } else {
          let arrStr = ''
          let arr = []
          val.forEach((item) => {
            arr.push(item.realname)
          })
          arrStr = arr.join(',')
          this.tableData[this.sIndex].zyNames = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          val.forEach((item) => {
            arrZy.push(item.staffid)
          })
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
        }
      },
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      resetQueryForm() {
        this.queryForm = {
          projectOrderName: undefined,
          projectOrderId: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
        this.listLoading = false
        let { ...other } = this.queryForm
        const {
          data: { tlist, totalRecord, currProjectId },
        } = await implementPlanList({
          ...other,
        })
        this.listLoading = false
        return
        this.currProjectId = currProjectId

        this.list = tlist
        this.total = totalRecord
        this.planNum = tlist[0].projectCode
      },
      days(start, end) {
        let s = new Date(start)
        let e = new Date(end)
        let hours = (e - s) / (1000 * 60 * 60 * 24)
        return hours + '天'
      },

      handleEdit(row, disabled, type) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum, type)
      },
      handlePersonnelEdit(row, disabled, type) {
        this.$refs['personnel'].showEdit(row, disabled, this.planNum, type)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await implementPlanDelete({ ids: row.id })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error')
          }
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      color(row) {
        if (row.id == this.currProjectId) {
          return { color: '#7fcf7c' }
        } else {
          return { color: '' }
        }
      },
      handleNodeClick(data) {
        console.log(data)
      },
    },
  }
</script>

<style scoped lang="scss">
  .container {
    display: flex;
    justify-content: space-between;
  }
  .container_left {
    width: 250px;
    padding: 15px;
  }
  .system-log-container {
    width: calc(100% - 250px);
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  .search-input {
    margin-bottom: 15px;
  }
  .top_title {
    margin-bottom: 15px;
  }
  .top__bpx {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
  }
  .secondCard_2 {
    margin-top: 20px;
  }
</style>
