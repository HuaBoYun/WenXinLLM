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
        <el-button type="success" @click="handleAdd()">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column 
            align="center"
            label="线索编号"
            prop="cluenaber"
            #default="{ row }"
          >
            <el-button type="text" @click="handleDetail(row)">
              {{ row.cluenaber }}
            </el-button>
          </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index"> 
          <el-table-column
            align="center"
            v-if="item.name === '核实内容'"
            label="核实内容"
            prop="verifycontent"
          />
          <el-table-column
            align="center"
            v-if="item.name === '涉及部门'"
            label="涉及部门"
            prop="departmentname"
          />
          <el-table-column
            align="center"
            v-if="item.name === '涉及人员'"
            label="涉及人员"
            prop="personnel"
          />

          <el-table-column
            align="center"
            label="状态"
            prop="ysstuts"
            v-if="item.name === '状态'"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.ysstuts == 1
                  ? '审批中'
                  : row.ysstuts == 2
                  ? '需调整'
                  : row.ysstuts == 3
                  ? '已撤销'
                  : row.ysstuts == 4
                  ? '已终止'
                  : row.ysstuts == 5
                  ? '已跟踪'
                  : row.ysstuts == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="320"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)"  :disabled="!!row.ysstuts" >修改</el-button>
           
            <!-- <el-button type="text" @click="handleStop(row)">结束</el-button>-->
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(row)"
                    :disabled="!row.ysstuts"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" :disabled="!!row.ysstuts"  @click.native="handleApproval(row)">
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  
                  <el-button type="text" :disabled="!!row.ysstuts"  @click.native="handleDelete(row)">删除</el-button>
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

    <checkModel ref="checkModel" @fetch-data="fetchData" />
    <sendModel ref="sendModel" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />

  </div>
</template>

<script>
  import { wghcList, wghcJs, wghcDelete } from '@/api/audit/wgzz'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import checkModel from './components/options/wghcCheck'
  import sendModel from './components/options/wghcSend'
  import { searchTableMixis } from '@/mixis/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import {  getFlowPkInfo } from '@/api/contract/manage'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'

  export default {
    name: 'wghc',
    components: {
      filterSearch,
      filterTable,
      checkModel,
      sendModel,
      ProcessList,
      WfqdDeal,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '线索编号' },
          { name: '核实内容' },
          { name: '涉及部门' },
          { name: '涉及人员' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-wghc-search',
        tableKey: 'audit-rectify-wghc-list',
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
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(78, row.id)
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId:row.id,
          tableId:78
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        return [{ name: '线索编号', key: 'clueNaber' }]
      },
      handleDepartmentSelected(node) {
        this.queryForm.auditorg = node.id
        this.queryForm.orgname = node.name
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
          orgname: '',
          auditorg: '',
          code: '',
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
        } = await wghcList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async evaluate(row) {
        await this.$refs['table'].showEdit(row.solutionid)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      // async handleDetail(row) {
      //   const data = await getSolutionDetail({ solutinid: row.solutionid })
      //   await this.$refs['edit'].showEdit('detail', data.data)
      // },
      handleStop(row) {
        this.$baseConfirm('你确定要结束当前项吗', null, async () => {
          const { msg } = await wghcJs({ id: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleCheck(row) {
        this.$refs['checkModel'].showEdit(row)
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['sendModel'].showEdit('新增', null)
      },
      handleDetail(row) {
        this.$refs['sendModel'].showEdit('详细', row)
      },
      handleEdit(row) {
        this.$refs['sendModel'].showEdit('编辑', row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await wghcDelete({ id: row.id })
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
