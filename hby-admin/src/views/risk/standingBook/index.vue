<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <type-tree @fetch-data="treeData" :editable="false" :tableData="list" />
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
                    v-model="queryForm.cxlevel"
                    clearable
                    placeholder="风险等级"
                    v-if="item.name === '风险等级'"
                  >
                    <el-option label="未评估" value="0" />
                    <el-option label="很低" value="1" />
                    <el-option label="较低" value="2" />
                    <el-option label="中等" value="3" />
                    <el-option label="较高" value="4" />
                    <el-option label="很高" value="5" />
                  </el-select>
                  <div
                    v-if="item.name === '责任科室'"
                    style="display: flex; align-items: center"
                  >
                    <el-input
                      v-model="queryForm.linkDeptName"
                      disabled
                      placeholder="责任科室"
                      style="width: 180px; margin-right: 8px"
                    />
                    <el-button type="primary" @click="handleShowLinkDept">
                      选择
                    </el-button>
                  </div>
                  <el-input
                    v-model="queryForm.riskcatidname"
                    clearable
                    placeholder="风险类型"
                    v-if="item.name === '风险类型'"
                  />
                  <el-input
                    v-model="queryForm.riskcatname"
                    clearable
                    placeholder="风险领域"
                    v-if="item.name === '风险领域'"
                  />
                  <el-select
                    v-model="queryForm.pgStatus"
                    clearable
                    placeholder="风险状态"
                    v-if="item.name === '风险状态'"
                  >
                    <el-option label="已评估" value="ypg" />
                    <el-option label="未评估" value="wpg" />
                  </el-select>
                  <el-select
                    v-model="queryForm.gbStatus"
                    clearable
                    placeholder="风险关闭状态"
                    v-if="item.name === '风险关闭状态'"
                  >
                    <el-option label="已关闭" value="0" />
                    <el-option label="未关闭" value="1" />
                  </el-select>
                  <div
                    v-if="item.name === '牵头责任部门'"
                    style="display: flex; align-items: center"
                  >
                    <el-input
                      v-model="queryForm.zrbmName"
                      disabled
                      placeholder="牵头责任部门"
                      style="width: 180px; margin-right: 8px"
                    />
                    <el-button type="primary" @click="handleShowZrbm">
                      选择
                    </el-button>
                  </div>
                </el-form-item>
                <el-form-item>
                  <el-date-picker
                    v-model="queryForm.Date"
                    clearable
                    end-placeholder="结束日期"
                    format="yyyy-MM-dd"
                    range-separator="-"
                    start-placeholder="开始日期"
                    type="daterange"
                    value-format="yyyy-MM-dd"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button
                    icon="el-icon-search"
                    native-type="submit"
                    type="primary"
                    @click="fetchData(queryForm.riskcatid)"
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

                  <!-- <i class="el-icon-delete" slot="reference"></i> -->
                  <el-button
                    slot="reference"
                    icon="el-icon-s-grid"
                    class="biaoge"
                    style="margin-bottom: 10px; margin-right: 10px"
                  ></el-button>
                </el-popover>
              </el-tooltip>
              <el-button type="success" @click="handlerExport">导出</el-button>
              <el-button type="primary" v-if="isUEditor" @click="UEClose">
                取消
              </el-button>
              <el-button type="primary" v-if="isUEditor" @click="UESubmit">
                确定
              </el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
          <el-table
            v-loading="listLoading"
            :data="list"
            @selection-change="handleSelectionChange"
            ref="multipleTable"
          >
            <el-table-column
              type="selection"
              width="55"
              v-if="isUEditor"
            ></el-table-column>
            <el-table-column align="center" label="风险编号" prop="risknumber">
              <template #default="{ row }">
                <el-button type="text" @click="handleRiskRead(row)">
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
                label="责任单位"
                prop="zrdw"
                v-if="item.name === '责任单位'"
              />
              <el-table-column
                align="center"
                label="责任部门"
                prop="zrbm"
                v-if="item.name === '责任部门'"
              />
              <el-table-column
                align="center"
                label="责任科室"
                prop="linkDeptName"
                v-if="item.name === '责任科室'"
              />
              <el-table-column
                align="center"
                label="牵头责任部门"
                prop="zrbmName"
                v-if="item.name === '牵头责任部门'"
              />
              <el-table-column
                align="center"
                label="风险等级"
                prop="level"
                show-overflow-tooltip
                v-if="item.name === '风险等级'"
              >
                <template #default="{ row }">
                  <div
                    :class="{
                      calCount1: row.level === '很低',
                      calCount2: row.level === '较低',
                      calCount3: row.level === '中等',
                      calCount4: row.level === '较高',
                      calCount5: row.level === '很高',
                    }"
                  >
                    {{ row.level }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="风险关闭状态"
                prop="gbStatus"
                v-if="item.name === '风险关闭状态'"
              >
                <template #default="{ row }">
                  {{ row.riskstatus == 0 ? '已关闭' : '未关闭' }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="创建时间"
                prop="riskcreatedt"
                show-overflow-tooltip
                v-if="item.name === '创建时间'"
              />
              <el-table-column
                align="center"
                label="风险排序"
                prop="riskorder"
                show-overflow-tooltip
                v-if="item.name === '风险排序'"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    v-if="!isUEditor"
                    @click="handleRiskOrderEdit(row)"
                  >
                    {{ row.riskorder || '-' }}
                  </el-button>
                  <span v-else>{{ row.riskorder || '-' }}</span>
                </template>
              </el-table-column>
            </div>
            <el-table-column align="center" label="操作">
              <template #default="{ row }">
                <el-button type="text" @click="handleTaskEdit(row)">
                  评估信息
                </el-button>
                <!-- <el-button @click="copyData(row)" type="text" v-if="isUEditor">
                  复制
                </el-button> -->
                <!-- <el-button type="text" @click="handleRead(row)">
                  风险应对
                </el-button> -->
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

    <el-dialog
      title="编辑风险排序"
      :visible.sync="riskOrderDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      append-to-body
      @close="resetRiskOrderForm"
    >
      <el-form
        ref="riskOrderFormRef"
        :model="riskOrderForm"
        :rules="riskOrderRules"
        label-width="100px"
        size="mini"
      >
        <el-form-item label="风险编号">
          <el-input v-model="riskOrderForm.risknumber" readonly />
        </el-form-item>
        <el-form-item label="风险名称">
          <el-input v-model="riskOrderForm.riskname" readonly />
        </el-form-item>
        <el-form-item label="风险描述">
          <el-input v-model="riskOrderForm.riskdes" readonly type="textarea" />
        </el-form-item>
        <el-form-item label="风险排序" prop="riskorder">
          <el-input
            v-model="riskOrderForm.riskorder"
            clearable
            placeholder="请输入正整数"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="mini" @click="riskOrderDialogVisible = false">
          取消
        </el-button>
        <el-button
          type="primary"
          size="mini"
          :loading="riskOrderSaving"
          @click="handleRiskOrderSave"
        >
          保存
        </el-button>
      </template>
    </el-dialog>

    <TaskEdit ref="taskEdit" />
    <RiskRead ref="riskRead" />
    <WfqdDeal ref="wfqddeal" />
    <CompanyTreeModel ref="comTreeRef" @selected="handleSelectZrbm" />
    <CompanyTreeModel ref="linkDeptTreeRef" @selected="handleSelectLinkDept" />
  </div>
</template>

<script>
  import { fxydListTZ, exportRiskTzlist } from '@/api/risk'
  import { updateRiskOrder } from '@/api/risk/home'
  import TypeTree from '@/views/risk/identify/components/TypeTree.vue'

  import { zgjkLeft } from '@/api/setting/org'
  import { formatOptions } from '@/utils/validate'
  import { parseTime } from '@/utils/index'

  import TaskEdit from './components/TaskEdit.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import RiskRead from '@/views/risk/identify/creation/components/RiskEdit.vue'
  // import RiskRead from './components/RiskRead.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import { downloadFile } from '@/utils/otherUtils'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  export default {
    props: {
      //UE编辑器模板传值,判断是否有复制按钮
      isUEditor: {
        type: Boolean,
        default: false,
      },
    },
    name: 'TreatmentEdit',
    components: {
      TypeTree,

      filterSearch,
      filterTable,
      TaskEdit,
      RiskRead,
      WfqdDeal,
      CompanyTreeModel,
    },
    data() {
      const validateRiskOrder = (rule, value, callback) => {
        if (value === '' || value === null || value === undefined) {
          callback()
          return
        }
        if (/^[1-9]\d*$/.test(String(value))) {
          callback()
          return
        }
        callback(new Error('请输入正整数'))
      }

      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        riskcategory: {},
        queryForm: {
          risknumber: '',
          riskname: '',
          belongsto: '',
          belongstoModel: [],
          pageNo: 1,
          pageSize: 20,
          riskcatid: '',
          cxlevel: '',
          linkDeptName: '',
          Date: [],
          riskcatidname: '',
          riskcatname: '',
          pgStatus: '',
          gbStatus: '',
          zrbmName: '',
        },
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
          { name: '责任单位' },
          { name: '责任部门' },
          { name: '责任科室' },
          { name: '牵头责任部门' },
          { name: '风险等级' },
          { name: '风险关闭状态' },
          { name: '创建时间' },
          { name: '风险排序' },
        ], //所有表格项
        filedNow: [],
        multipleSelection: [],
        riskOrderDialogVisible: false,
        riskOrderSaving: false,
        riskOrderForm: {
          id: '',
          riskid: '',
          evaluationid: '',
          risknumber: '',
          riskname: '',
          riskdes: '',
          riskorder: '',
        },
        riskOrderRules: {
          riskorder: [
            { validator: validateRiskOrder, trigger: ['blur', 'change'] },
          ],
        },
      }
    },
    created() {
      this.getBelongstoTextOptions()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    methods: {
      //编辑器多选
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '风险编号', key: 'risknumber' },
          { name: '风险名称', key: 'riskname' },
          { name: '风险等级', key: 'cxlevel' },
          { name: '责任科室', key: 'zrbm' },
          { name: '风险类型', key: 'riskcatidname' },
          { name: '风险领域', key: 'riskcatname' },
          { name: '风险状态', key: 'pgStatus' },
          { name: '风险关闭状态', key: 'gbStatus' },
          { name: '牵头责任部门', key: 'zrbmName' },
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
       * @description: 分页，初始化
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData(this.queryForm.riskcatid)
      },
      treeData(id) {
        console.log('🚀 ~ treeData ~ id:', id)
        this.queryForm.pageNo = 1
        this.queryForm.riskcatid = id
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData(id) {
        console.log('id', id)
        this.listLoading = true
        if (id && id === 'reset') {
          this.queryForm.Date = []
          this.queryForm.risknumber = ''
          this.queryForm.riskname = ''
          this.queryForm.belongsto = ''
          this.queryForm.cxlevel = ''
          this.queryForm.linkDeptName = ''
          this.queryForm.belongstoModel = []
          this.queryForm.pageNo = 1
          this.queryForm.pageSize = 20
          this.queryForm.riskcatidname = ''
          this.queryForm.riskcatname = ''
          this.queryForm.pgStatus = ''
          this.queryForm.gbStatus = ''
          this.queryForm.zrbmName = ''
        }
        const { Date } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        const {
          data: {
            data: { list, total },
            // riskcategory,
          },
        } = await fxydListTZ({
          riskcatid: this.queryForm.riskcatid,
          belongsto: this.queryForm.belongsto,
          risknumber: this.queryForm.risknumber,
          cxlevel: this.queryForm.cxlevel,
          linkDeptName: this.queryForm.linkDeptName,
          riskname: this.queryForm.riskname,
          pageNo: this.queryForm.pageNo,
          pageSize: this.queryForm.pageSize,
          startDate,
          endDate,
          riskcatidname: this.queryForm.riskcatidname,
          riskcatname: this.queryForm.riskcatname,
          pgStatus: this.queryForm.pgStatus,
          gbStatus: this.queryForm.gbStatus,
          zrbmName: this.queryForm.zrbmName,
        })

        this.list = list
        this.list.forEach((item) => {
          if (item.riskcreatedt) {
            item.riskcreatedt = parseTime(item.riskcreatedt, '{y}-{m}-{d}')
          }
        })
        this.total = total
        // this.riskcategory = riskcategory
        this.listLoading = false
      },
      /**
       * @description: 获取左侧树
       * @return {*}
       */
      async getBelongstoTextOptions() {
        const result = await zgjkLeft()
        let newValue = formatOptions(result, 'name', 'id')
        this.belongstoTextOptions = newValue
      },
      /**
       * @description: 打开 评估信息
       * @return {*}
       */
      handleTaskEdit(row) {
        this.$refs['taskEdit'].showEdit(row, 1)
      },
      handleRiskOrderEdit(row) {
        this.riskOrderForm = {
          id: row.id || '',
          riskid: row.riskid || '',
          evaluationid: row.evaluationid || '',
          risknumber: row.risknumber || '',
          riskname: row.riskname || '',
          riskdes: row.riskdes || '',
          riskorder: row.riskorder ? String(row.riskorder) : '',
        }
        this.riskOrderDialogVisible = true
      },
      handleRiskOrderSave() {
        this.$refs.riskOrderFormRef.validate(async (valid) => {
          if (!valid) return
          this.riskOrderSaving = true
          try {
            const riskOrder = this.riskOrderForm.riskorder
            const payload = {
              riskId: this.riskOrderForm.riskid,
              riskOrder: riskOrder === '' ? '' : Number.parseInt(riskOrder, 10),
            }
            const res = await updateRiskOrder(payload)
            if (res && res.code == 200) {
              this.riskOrderDialogVisible = false
              this.fetchData(this.queryForm.riskcatid)
            } else {
              this.$message.error((res && res.msg) || '保存失败')
            }
          } catch (error) {
            console.error('保存风险排序失败:', error)
            this.$message.error('保存失败')
          } finally {
            this.riskOrderSaving = false
          }
        })
      },
      resetRiskOrderForm() {
        if (this.$refs.riskOrderFormRef) {
          this.$refs.riskOrderFormRef.clearValidate()
        }
      },
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['read'].showRead(row)
      },
      /**
       * @description: 打开 风险详情审批
       * @return {*}
       */
      async handleRiskRead(row) {
        if (row && row.status > 1) {
          const res = await getFlowPkInfo({
            formId: row.riskid,
            tableId: 90,
          })
          this.$refs.wfqddeal.show(res.data, false)
        } else {
          this.$refs['riskRead'].showEdit(row, this.queryForm.riskcatid, true)
        }
        // this.$refs['riskRead'].showRead(row, this.queryForm.riskcatid)
      },
      UEClose() {
        this.$emit('close')
      },
      UESubmit() {
        this.$emit('submit', this.multipleSelection)
      },
      clearSelection() {
        this.$refs.multipleTable.clearSelection()
        this.multipleSelection = []
      },

      async handlerExport() {
        const res = await exportRiskTzlist({
          ...this.queryForm,
          startDate: this.queryForm.Date[0] || '',
          endDate: this.queryForm.Date[1] || '',
          riskcatidname: this.queryForm.riskcatidname,
          riskcatname: this.queryForm.riskcatname,
          pgStatus: this.queryForm.pgStatus,
          gbStatus: this.queryForm.gbStatus,
          zrbmName: this.queryForm.zrbmName,
        })
        downloadFile(res, '风险数据库.xlsx')
      },
      // 调起部门选择
      handleShowZrbm() {
        this.$refs['comTreeRef'].show(false, [])
      },
      // 选择部门回调
      handleSelectZrbm(e) {
        this.queryForm.zrbmName = e.name
      },
      // 调起责任科室选择
      handleShowLinkDept() {
        this.$refs['linkDeptTreeRef'].show(false, [])
      },
      // 选择责任科室回调
      handleSelectLinkDept(e) {
        this.queryForm.linkDeptName = e.name
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
    min-width: 200px;
    flex-shrink: 0;
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
  .calCount1 {
    color: #52ffb7;
  }
  .calCount2 {
    color: #33d73b;
  }
  .calCount3 {
    color: #ffb500;
  }
  .calCount4 {
    color: #ff7f00;
  }
  .calCount5 {
    color: #e92129;
  }
</style>
