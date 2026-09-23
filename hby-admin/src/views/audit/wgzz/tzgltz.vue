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
        <!-- <el-button type="success" @click="handleAdd">新建</el-button> -->
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="线索编号"
            v-if="item.name === '线索编号'"
            prop="cluenaber"
            show-overflow-tooltip
            #default="{ row }"
          >   
          <el-button type="text" @click="handleDetail(row)">
              {{ row.cluenaber }}
            </el-button>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '报告主体'"
            label="报告主体"
            prop="cluebgzt"
          />
          <el-table-column
            align="center"
            v-if="item.name === '报送时间'"
            label="报送时间"
            prop="messagetime"
          />

          <el-table-column
            align="center"
            v-if="item.name === '涉及单位'"
            label="涉及单位"
            prop="clueunitnename"
          />

          <el-table-column
            align="center"
            label="问题线索概述"
            v-if="item.name === '问题线索概述'"
            prop="verifycontentnew"
            show-overflow-tooltip
          ></el-table-column>

          <el-table-column
            align="center"
            label="是否完结"
            v-if="item.name === '是否完结'"
            prop="cluesfwj"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.cluesfwj == 1 ? '是' : '否' }}
            </template>
          </el-table-column>



          <!-- <el-table-column
            v-if="item.name === '来源方式'"
            align="center"
            label="来源方式"
            prop="cluesource"
            width="120"
          >
            <template #default="{ row }">
              {{ row.cluesource == 1 ? '单位一' : '单位二' }}
            </template>
          </el-table-column>

         

        

          <el-table-column
            align="center"
            label="报送方式"
            prop="messagemanner"
            v-if="item.name === '报送方式'"
            show-overflow-tooltip
          />
      
          <el-table-column
            align="center"
            label="级次"
            v-if="item.name === '级次'"
            prop="cluejc"
            show-overflow-tooltip
          />
        
          <el-table-column
            align="center"
            label="发生时间"
            v-if="item.name === '发生时间'"
            prop="occurrenceTime"
            :formatter="formatDate"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="问题类别"
            v-if="item.name === '问题类别'"
            prop="cluetype"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="资产损失程度或不良后果"
            v-if="item.name === '资产损失程度或不良后果'"
            prop="consequences"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="涉及责任人员"
            v-if="item.name === '涉及责任人员'"
            prop="cluehandlingname"
            show-overflow-tooltip
          ></el-table-column>
          
          <el-table-column
            align="center"
            label="处理情况"
            v-if="item.name === '处理情况'"
            prop="clueclqk"
            show-overflow-tooltip
          ></el-table-column> -->
         
        </div>
        <el-table-column width="1" />
      </el-table>
      <!-- <wgzzEdit ref="edit" @fetch-data="fetchData" /> -->
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
    <tzgltzInfo ref="tzgltzInfo"/>
  </div>
</template>

<script>
  import { wgzzArreyByList } from '@/api/audit/wgzz'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import tzgltzInfo from './components/tzgltzInfo'
  // import wgzzEdit from './components/wgzzEdit'
  export default {
    name: 'tzgltz',
    components: { filterSearch, filterTable ,tzgltzInfo},
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          clueNaber: '',
          // cluesfwj: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
        { name: '线索编号' },
          { name: '来源方式' },
          { name: '报告主体' },
          { name: '报送时间' },
          { name: '报送方式' },
          { name: '涉及单位' },
          { name: '级次' },
          { name: '问题线索概述' },
          { name: '发生时间' },
          { name: '问题类别' },
          { name: '资产损失程度或不良后果' },
          { name: '涉及责任人员' },
          { name: '处理情况' },
          { name: '是否完结' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-standingbook-search',
        tableKey: 'audit-rectify-standingbook-list',
        searchMore: true,
        cluesourceList: [
          { name: '单位一', id: 1 },
          { name: '单位二', id: 2 },
        ],
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
          // { name: '是否完结', key: 'cluesfwj' },
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
          clueNaber: '',
          // cluesfwj: '',
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
        } = await wgzzArreyByList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleDetail(row) { 
        this.$refs['tzgltzInfo'].showEdit("detail",row)
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
          const { msg } = await doDelete({ ids: row.id })
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
