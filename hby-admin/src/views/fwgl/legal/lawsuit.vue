<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never" v-if="false">
        <vab-query-form-left-panel :span="24">
          <el-form ref="form" :inline="true" label-width="0">
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <!-- <el-input
                v-model="queryForm.firstcourt"
                clearable
                placeholder="一审法院"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '一审法院'"
              /> -->

              <!-- <el-input
                v-model="queryForm.fillunit"
                clearable
                placeholder="填报单位"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '填报单位'"
              /> -->
              <el-table-column
                align="center"
                label="纠纷名称"
                prop="disputeitem"
                v-if="item.name === '纠纷名称'"
              />
              <el-table-column
                align="center"
                label="纠纷类型"
                prop="disputetype"
                v-if="item.name === '纠纷类型'"
              />
              <el-date-picker
                v-model="queryForm.startdate"
                placeholder="受理开始日期"
                style="width: 150px"
                type="date"
                value-format="yyyy-MM-dd"
                v-if="item.name === '受理时间'"
              />
              {{ item.name === '受理时间' ? '至' : '' }}
              <el-date-picker
                v-model="queryForm.enddate"
                placeholder="受理结束日期"
                style="width: 150px"
                type="date"
                value-format="yyyy-MM-dd"
                v-if="item.name === '受理时间'"
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
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column align="center" label="一审法院" prop="firstcourt" /> -->
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="纠纷名称"
            prop="disputeitem"
            v-if="item.name === '纠纷名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.disputeitem }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="纠纷类型"
            prop="disputetype"
            v-if="item.name === '纠纷类型'"
          />
          <el-table-column
            align="center"
            label="对方当事人"
            prop="casebasicinfo"
            v-if="item.name === '对方当事人'"
          />
          <el-table-column
            align="center"
            label="案由"
            prop="causecase"
            v-if="item.name === '案由'"
          />
          <el-table-column
            align="center"
            label="诉讼阶段"
            prop="disputecours"
            v-if="item.name === '诉讼阶段'"
          />

          <el-table-column
            align="center"
            label="填报单位"
            prop="fillunit"
            v-if="item.name === '填报单位'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="立案时间"
            prop="casetime"
            v-if="item.name === '立案时间'"
          />
          <!-- <el-table-column
            align="center"
            label="业务发生时间"
            prop="biztime"
            v-if="item.name === '业务发生时间'"
          /> -->
          <el-table-column
            align="center"
            label="涉诉金额（万元）"
            prop="subjectamount"
            v-if="item.name === '涉诉金额（万元）'"
          />
        </div>
        <!--      <el-table-column-->
        <!--        align="center"-->
        <!--        label="描述"-->
        <!--        prop="data"-->
        <!--        show-overflow-tooltip-->
        <!--      />-->
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              v-if="userInfo.staffid === row.createstaff"
              @click="handleTimeLine(row)"
            >
              过程
            </el-button>
            <el-button
              type="text"
              v-if="userInfo.staffid === row.createstaff"
              @click="handleEdit(row)"
            >
              修改
            </el-button>
            <el-button
              type="text"
              v-if="userInfo.staffid === row.createstaff"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      class="pagination"
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <LawsuitEdit ref="edit" @fetch-data="fetchData" />
    <timeLine ref="timeLine" />
  </div>
</template>

<script>
  import { getlitigationSettlement, Removelitigation } from '@/api/fwgl/legal'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import LawsuitEdit from '@/views/fwgl/legal/components/LawsuitEdit'
  import timeLine from './components/timeLine.vue'
  export default {
    name: 'Download',
    components: { LawsuitEdit, filterTable, filterSearch, timeLine },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        localKey: 'fwgl-legal-lawsuit-search',
        tableKey: 'fwgl-legal-lawsuit-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '纠纷名称' },
          { name: '纠纷类型' },
          { name: '对方当事人' },
          { name: '案由' },
          { name: '诉讼阶段' },
          { name: '填报单位' },
          { name: '立案时间' },
          // { name: '业务发生时间' },
          { name: '涉诉金额（万元）' },
        ],
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          flowid: '698869',
          firstcourt: undefined,
          fillunit: undefined,
          startdate: undefined,
          enddate: undefined,
        },
        userInfo: JSON.parse(localStorage.getItem('userInfo')),
      }
    },
    created() {
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      /**
       * @description: 下面四句：控制筛选项、表格的位置以及显示隐藏
       */
      this.initTable() //初始化表格 //初始化表格
      this.initSearch()
    },
    methods: {
      /**
       * @description: 重置查询条件
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          flowid: '698869',
          firstcourt: undefined,
          fillunit: undefined,
          startdate: undefined,
          enddate: undefined,
        }
      },
      getFiled() {
        let fields = [
          { name: '纠纷名称', key: 'disputeitem' },
          { name: '纠纷类型', key: 'disputetype' },
          // { name: '填报单位', key: 'fillunit' },
          // { name: '受理时间', key: 'time_date' },
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
      /**
       * @description: 重置并请求
       * @return {*}
       */
      resetSearch() {
        this.resetQueryForm()
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
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getlitigationSettlement(this.queryForm)
        this.list = tlist.map((i) => {
          return {
            ...i,
            disputeItem: i.disputeitem,
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      handleDetail(row) {
        this.$refs['edit'].showDetail(row)
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await Removelitigation({
            litigationId: row.litigationid,
          })
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
      handleTimeLine(row) {
        this.$refs['timeLine'].open(row)
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
</style>
