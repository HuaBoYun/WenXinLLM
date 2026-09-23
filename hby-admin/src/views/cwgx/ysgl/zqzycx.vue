<!-- 债券摘要查询 -->
<template>
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
                placeholder="财务组织"
                v-if="item.name === '财务组织'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="转出对象"
                v-if="item.name === '转出对象'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="币种"
                v-if="item.name === '币种'"
              />
              <el-date-picker
                v-model="queryForm.day"
                type="date"
                align="right"
                v-if="item.name === '并账日期'"
              ></el-date-picker>
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="原币金额"
                v-if="item.name === '原币金额'"
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
      <div class="nav_box">
        <div class="title_box">
          <div class="title_item">
            <span class="title_text">转移记录</span>
            <span class="title_text">(0)</span>
          </div>
          <div class="title_item">
            <span class="title_text">已选0条</span>
          </div>
        </div>
        <div>
          <vab-query-form-right-panel style="width: 100%">
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
            <!-- <el-button type="success" @click="handleEdit(false, false)">
          新建
        </el-button> -->
            <el-button type="primary">取消转移</el-button>
            <el-button type="primary">联查凭证</el-button>
          </vab-query-form-right-panel>
        </div>
      </div>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="序号"
          prop="qdcode"
          width="100"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '处理日期'"
            align="center"
            label="处理日期"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '交易类型'"
            align="center"
            label="交易类型"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '单据编号'"
            align="center"
            label="单据编号"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '转出对象类型'"
            align="center"
            label="转出对象类型"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '转出对象'"
            align="center"
            label="转出对象"
            prop="sjlxName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            v-if="item.name === '转入对象类型'"
            align="center"
            label="转入对象类型"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '转入对象'"
            align="center"
            label="转入对象"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '币种'"
            align="center"
            label="币种"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '原币金额'"
            align="center"
            label="原币金额"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '本币金额'"
            align="center"
            label="本币金额"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '处理人'"
            align="center"
            label="处理人"
            prop="planYear"
            show-overflow-tooltip
          />
        </div>
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
    <Edit ref="edit" @fetch-data="fetchData"></Edit>
  </div>
</template>

<script>
  import {
    implementPlanList,
    implementPlanDelete,
    fpzyksry,
  } from '@/oapi/audit/project'
  import Edit from './components/ysdjxtszEdit'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'zqzycx',
    mixins: [searchTableMixis],
    components: {
      Edit,
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
        },
        currProjectId: '',
        filedAll: [
          { name: '处理日期' },
          { name: '交易类型' },
          { name: '单据编号' },
          { name: '转出对象类型' },
          { name: '转出对象' },
          { name: '转入对象类型' },
          { name: '转入对象' },
          { name: '币种' },
          { name: '原币金额' },
          { name: '本币金额' },
          { name: '处理人' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'cwgx-ysgl-zqzycx-search',
        tableKey: 'cwgx-ysgl-zqzycx-list',
        searchMore: false,
        select: [],
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
        return [
          { name: '所属组织', key: 'sxName' },
          { name: '转出对象', key: 'startStatus' },
          { name: '币种', key: 'startStatus1' },
          { name: '并账日期', key: 'startStatus2' },
          { name: '原币金额', key: 'startStatus2' },
        ]
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
  .title_box {
    display: flex;
    margin: 10px 0;
  }
  .title_item {
    margin-right: 15px;
  }
  .title_text {
    color: #7dc4e8;
  }
  .nav_box {
    display: flex;
    justify-content: space-between;
  }
</style>
