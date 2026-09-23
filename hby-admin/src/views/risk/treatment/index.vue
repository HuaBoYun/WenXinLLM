<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <type-tree
          @fetch-data="queryData"
          :editable="false"
          :tableData="list"
        />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-top-panel>
              <el-form
                ref="form"
                checkable
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item
                  v-for="(item, index) in searchItem"
                  :key="index"
                  :prop="item.key"
                >
                  <el-input
                    v-model="queryForm.risknumber"
                    clearable
                    placeholder="风险编号"
                    v-if="item.name === '风险编号'"
                  />
                  <el-input
                    v-model="queryForm.riskname"
                    clearable
                    placeholder="风险名称"
                    v-if="item.name === '风险名称'"
                  />
                  <el-select
                    v-model="queryForm.status"
                    clearable
                    placeholder="请选择审批状态"
                    style="width: 140px; margin-right: 20px"
                    v-if="item.name === '审批状态'"
                  >
                    <el-option
                      v-for="item in aprstatusList"
                      :key="item.value"
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
                    @click="
                      queryForm.pageNo = 1
                      fetchData(queryForm.riskcatid)
                    "
                  >
                    查询
                  </el-button>
                </el-form-item>
                <el-form-item>
                  <el-button
                    type="primary"
                    native-type="submit"
                    @click="fetchData('reset')"
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
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
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

        <el-card shadow="never" class="secondCard">
          <vab-query-form-right-panel class="option-row">
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
          </vab-query-form-right-panel>

          <el-table v-loading="listLoading" :data="list">
            <el-table-column align="center" label="风险编号" prop="risknumber">
              <template #default="{ row }">
                <el-button type="text" @click="handleRead(row)">
                  {{ row.risknumber }}
                </el-button>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="风险名称"
                prop="riskname"
                show-overflow-tooltip
                v-if="item.name === '风险名称'"
              />
              <el-table-column
                align="center"
                label="风险描述"
                prop="riskdes"
                show-overflow-tooltip
                v-if="item.name === '风险描述'"
              />
              <el-table-column
                align="center"
                label="归属单位"
                prop="unit"
                v-if="item.name === '归属单位'"
              />
              <el-table-column
                align="center"
                label="风险等级"
                prop="level"
                show-overflow-tooltip
                v-if="item.name === '风险等级'"
              />
              <el-table-column
                align="center"
                label="风险状态"
                prop="copingPlot"
                show-overflow-tooltip
                v-if="item.name === '风险状态'"
              >
                <template #default="{ row }">
                  <span>
                    {{ row.copingPlot == '0' ? '未应对' : '已应对' }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="审批状态"
                prop="status"
                v-if="item.name === '审批状态'"
              >
                <template #default="{ row }">
                  {{
                    row.copingStatus == 1
                      ? '审批中'
                      : row.copingStatus == 2
                      ? '需调整'
                      : row.copingStatus == 3
                      ? '已撤销'
                      : row.copingStatus == 4
                      ? '已终止'
                      : row.copingStatus == 5
                      ? '已跟踪'
                      : row.copingStatus == 6
                      ? '已完成'
                      : '未审批'
                  }}
                </template>
              </el-table-column>
            </div>
            <el-table-column align="center" label="操作">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  :disabled="row.copingStatus && row.copingStatus != 0"
                >
                  风险应对
                </el-button>
                <el-dropdown style="margin-left: 10px">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      @click.native="handleManage(row)"
                      :disabled="!+row.copingStatus"
                    >
                      <el-button type="text" :disabled="!+row.copingStatus">
                        办理
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleApproval(row)"
                      :disabled="!!+row.copingStatus"
                    >
                      <el-button
                        type="text"
                        :disabled="!!+row.copingStatus || btnLoading"
                      >
                        提交审批
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleComment(row)"
                      :disabled="row.status != '6'"
                    >
                      <el-button
                        type="text"
                        :disabled="row.copingStatus != '6'"
                      >
                        一体化运行评价
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <!-- <el-button
                        type="text"
                        :disabled="!!row.status"
                        @click.native="handleCommand({ row, type: 'del' })"
                      >
                        删除
                      </el-button> -->
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          background
          class="pager"
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <el-dialog title="一体化运行评价" :visible.sync="dialogVisible" width="50%">
      <el-form ref="elForm" label-width="120px" :model="formData" size="medium">
        <el-form-item label="评价标准及要点" prop="evalimp">
          <el-input
            :autosize="{ minRows: 5 }"
            v-model="formData.evalimp"
            clearable
            placeholder="请输入评价标准及要点"
            type="textarea"
            :style="{ width: '100%' }"
          />
        </el-form-item>
        <el-form-item label="文档" prop="evalfile">
          <el-input
            :autosize="{ minRows: 5 }"
            v-model="formData.evalfile"
            clearable
            placeholder="请输入文档"
            type="textarea"
            :style="{ width: '100%' }"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveComment()">确 定</el-button>
      </span>
    </el-dialog>
    <TreatEdit ref="edit" @fetch-data="fetchData(queryForm.riskcatid)" />
    <TreatRead ref="read" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { getList } from '@/api/systemLog'
  import { fxydList } from '@/api/risk'
  import TypeTree from '@/views/risk/identify/components/TypeTree.vue'
  import TreatEdit from './TreatEdit.vue'
  import TreatRead from './TreatRead.vue'
  import { zgjkLeft } from '@/api/setting/org'
  import { formatOptions } from '@/utils/validate'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import {
    getContractTypes,
    getFlowPkInfo,
    goReplyInfo,
    getReplyInfo,
  } from '@/api/contract/manage'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'TreatmentEdit',
    components: {
      TypeTree,
      TreatEdit,
      TreatRead,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        // 评价
        formData: {
          evalimp: '',
          evalfile: '',
          riskcopingid: '',
        },
        dialogVisible: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        riskcategory: {},
        queryForm: {
          status: '',
          risknumber: '',
          riskname: '',
          belongsto: '',
          belongstoModel: [],
          pageNo: 1,
          pageSize: 20,
          riskcatid: '',
        },
        aprstatusList: [
          {
            label: '未审批',
            value: '0',
          },
          {
            label: '审批中',
            value: '1',
          },
          {
            label: '需调整',
            value: '2',
          },
          {
            label: '已撤销',
            value: '3',
          },
          {
            label: '已完成',
            value: '6',
          },
        ],
        belongstoTextOptions: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-identify-treatment-search',
        tableKey: 'risk-identify-treatment-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '风险名称' },
          { name: '风险描述' },
          { name: '归属单位' },
          { name: '风险等级' },
          { name: '风险状态' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [],
        btnLoading: false,
      }
    },
    created() {
      this.getBelongstoTextOptions()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    /**
     * @description: 流程提交回调
     * @return {*}
     */
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData('reset')
        }
      })
    },
    methods: {
      /**
       * @description: 提交审批
       * @return {*}
       */
      handleApproval(row) {
        try {
          this.btnLoading = true
          //提交审批
          if (row.riskcopingid) {
            this.$refs['process'].save(92, row.riskcopingid)
          } else {
            this.$message.error('风险未应对!')
          }
        } catch (error) {
          this.btnLoading = false
        }
      },
      /**
       * @description: 办理页面
       * @return {*}
       */
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.riskcopingid,
          tableId: 92,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      /**
       * @description:  打开 一体化运行评价
       * @return {*}
       */
      async handleComment(row) {
        //评价
        this.dialogVisible = true
        this.formData.evalimp = ''
        this.formData.evalfile = ''
        this.formData.riskcopingid = row.riskcopingid
        let res = await getReplyInfo({ riskid: row.riskid })
        if (res.code == 200) {
          this.formData.evalimp = res.data.copings.evalimp
          this.formData.evalfile = res.data.copings.evalfile
        }
        this.$forceUpdate()
      },
      /**
       * @description: 一体化运行评价 确定
       * @return {*}
       */
      async saveComment() {
        let res = await goReplyInfo(this.formData)
        if (res.code == 200) {
          this.dialogVisible = false
          this.$baseMessage('成功', 'success')
          this.fetchData('reset')
        }
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '风险编号', key: 'risknumber' },
          { name: '风险名称', key: 'riskname' },
          { name: '审批状态', key: 'status' },
        ]
        return fields
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
                this.queryForm[x.key] = ''
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
      showMore() {
        this.searchMore = !this.searchMore
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData(this.queryForm.riskcatid)
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData(this.queryForm.riskcatid)
      },
      /**
       * @description: 分页 初始化
       * @return {*}
       */
      queryData(type) {
        this.queryForm.pageNo = 1
        this.queryForm.riskcatid = type
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData(id) {
        this.btnLoading = false
        this.listLoading = true
        if (id && id === 'reset') {
          this.queryForm.risknumber = ''
          this.queryForm.riskname = ''
          this.queryForm.belongsto = ''
          this.queryForm.status = ''
          this.queryForm.belongstoModel = []
          this.queryForm.pageNo = 1
          this.queryForm.pageSize = 20
        }
        const {
          data: {
            data: { list, total },
          },
        } = await fxydList({
          riskcatid: this.queryForm.riskcatid,
          belongsto: this.queryForm.belongsto,
          risknumber: this.queryForm.risknumber,
          riskname: this.queryForm.riskname,
          status: this.queryForm.status,
          pageNo: this.queryForm.pageNo,
          pageSize: this.queryForm.pageSize,
        })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 获取 左侧树
       * @return {*}
       */
      async getBelongstoTextOptions() {
        const result = await zgjkLeft()
        let newValue = formatOptions(result, 'name', 'id')
        this.belongstoTextOptions = newValue
      },
      /**
       * @description: 打开 编辑
       * @return {*}
       */
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      /**
       * @description:打开 详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['edit'].showEdit(row, null, true)
        // this.$refs['read'].showRead(row)
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
