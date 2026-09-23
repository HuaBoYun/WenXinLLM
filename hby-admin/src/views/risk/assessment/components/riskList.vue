<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="选择风险"
      :visible.sync="dialogFormVisible"
      width="1050px"
      append-to-body
      @close="close"
      destroy-on-close
    >
      <div class="system-log-container">
        <div class="lr-layout">
          <div class="left">
            <type-tree
              @fetch-data="fetchData"
              :editable="false"
              :tableData="list"
              :url="`/risk/risk_left`"
            />
          </div>
          <div class="right">
            <vab-query-form>
              <vab-query-form-top-panel>
                <el-form
                  ref="form"
                  checkable
                  :inline="true"
                  label-width="0"
                  :model="queryForm"
                  @submit.native.prevent
                >
                  <el-form-item>
                    <el-input
                      v-model="queryForm.risknumber"
                      clearable
                      placeholder="风险编号"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-input
                      v-model="queryForm.riskname"
                      clearable
                      placeholder="风险名称"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-cascader
                      v-model="queryForm.belongstoModel"
                      clearable
                      placeholder="请选择主责部门"
                      :style="{ width: '100%' }"
                      :options="belongstoTextOptions"
                      :props="{ checkStrictly: true }"
                      :show-all-levels="false"
                    ></el-cascader>
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
                    <el-button
                      type="primary"
                      native-type="submit"
                      @click="resetSearch"
                    >
                      重置
                    </el-button>
                  </el-form-item>
                </el-form>
              </vab-query-form-top-panel>
              <vab-query-form-left-panel>
                <span></span>
              </vab-query-form-left-panel>
            </vab-query-form>

            <el-table
              ref="multipleTable"
              v-loading="listLoading"
              :data="list"
              @select-all="handleSelectAll"
              @select="handleSelection"
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column
                align="center"
                label="风险编号"
                prop="risknumber"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="showRiskDetail(row)">
                    {{ row.risknumber }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="风险名称"
                prop="riskname"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="风险描述"
                prop="riskdes"
                show-overflow-tooltip
              />

              <el-table-column
                align="center"
                label="创建时间"
                prop="riskcreatedt"
                show-overflow-tooltip
              />
            </el-table>
            <el-pagination
              background
              :current-page="queryForm.pageNo"
              :layout="layout"
              :page-size="queryForm.pageSize"
              :total="total"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            />
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template>
    </el-dialog>
    <RiskEdit ref="read" />
  </div>
</template>

<script>
  import { getCreationTestList } from '@/api/risk'
  import TypeTree from '@/views/risk/identify/components/TypeTree.vue'
  import { UTCformat } from '@/utils'
  import { formatOptions } from '@/utils/validate'
  import { zgjkLeft } from '@/api/setting/org'
  import RiskEdit from '@/views/risk/identify/creation/components/RiskEdit.vue'

  export default {
    name: 'PlanEdit-standard',
    components: { TypeTree, RiskEdit },
    data() {
      return {
        dialogFormVisible: false,
        tableData: [],
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        belongstoTextOptions: [],
        // 存储所有已选择的数据，用于跨页多选
        allSelectedData: [],
        queryForm: {
          risknumber: '',
          riskname: '',
          belongsto: '',
          belongstoModel: [],
          pageNo: 1,
          pageSize: 20,
          riskcatid: '',
          closestatus: 1,
          secrectLevelId: '',
        },
      }
    },
    methods: {
      /**
       * @description: 选择风险回调
       * @return {*}
       */
      submitForm() {
        this.$emit('setRisk', this.allSelectedData)
        this.close()
      },
      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.resetQueryForm()
        // 清空所有选中数据
        this.allSelectedData = []
      },
      /**
       * @description: 初始化获取数据
       * @return {*}
       */
      async showEdit(row) {
        this.queryForm.secrectLevelId = row || ''
        this.dialogFormVisible = true
        const result = await zgjkLeft()
        let newValue = formatOptions(result, 'name', 'id')
        this.belongstoTextOptions = newValue
        this.fetchData()
      },
      // async fetchData(id) {
      //   this.listLoading = true
      //   this.queryForm.belongsto = id
      //   const {
      //     data: {
      //       pageBean: { records, total },
      //     },
      //   } = await getCreationList(this.queryForm)

      //   this.list = records
      //   this.total = total
      //   this.listLoading = false
      // },
      /**
       * @description: 重置
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = {
          riskcatid: this.queryForm.riskcatid,
          risknumber: '',
          riskname: '',
          belongsto: '',
          belongstoModel: [],
          secrectLevelId: this.queryForm.secrectLevelId,
          closestatus: 1,
          pageNo: 1,
          pageSize: 20,
        }
      },
      /**
       * @description: 重置
       * @return {*}
       */
      resetSearch() {
        console.log('resetSearch')
        this.resetQueryForm()
        // 重置搜索时清空所有选中数据
        this.allSelectedData = []
        this.fetchData(this.queryForm.riskcatid)
      },
      /**
       * @description: 请求数据
       * @return {*}
       */
      async fetchData(id) {
        this.listLoading = true
        this.queryForm.riskcatid =
          typeof id == 'string' || typeof id == 'number'
            ? id
            : this.queryForm.riskcatid
        this.queryForm.belongsto =
          this.queryForm.belongstoModel.length > 0
            ? this.queryForm.belongstoModel[
                this.queryForm.belongstoModel.length - 1
              ]
            : ''
        const {
          data: {
            pageBean: { list, total },
            riskcategory,
          },
        } = await getCreationTestList({
          riskcatid: this.queryForm.riskcatid,
          belongsto: this.queryForm.belongsto,
          risknumber: this.queryForm.risknumber,
          riskname: this.queryForm.riskname,
          pageNo: this.queryForm.pageNo,
          pageSize: this.queryForm.pageSize,
          closestatus: this.queryForm.closestatus,
          status: 6,
          secrectLevelId: this.queryForm.secrectLevelId,
        })

        // item.endtime = UTCformat(item.endtime)
        this.list = list.map((v) => {
          v.riskcreatedt = UTCformat(v.riskcreatedt)
          return v
        })
        // this.list = records
        this.total = total
        this.riskcategory = riskcategory
        this.listLoading = false

        // 翻页后恢复选中状态
        this.$nextTick(() => {
          this.restoreSelection()
        })
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
      handleSelection(val, row) {
        const i = this.allSelectedData.findIndex((x) => x.risknumber == row.risknumber)
        if (i < 0) {
          this.allSelectedData.push(row)
        } else {
          this.allSelectedData.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.allSelectedData.some((x) => x.risknumber == row.risknumber)) {
              this.allSelectedData.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.allSelectedData.findIndex((x) => x.risknumber == row.risknumber)
            if (i >= 0) {
              this.allSelectedData.splice(i, 1)
            }
          })
        }
      },
      handleSelectionChange(val) {
        console.log('handleSelectionChange - selected:', val)
        this.tableData = val
      },



      /**
       * @description: 翻页的时候回显已勾选的数据
       * @return {*}
       */
      restoreSelection() {
        this.$nextTick(() => {
          this.allSelectedData.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.risknumber == item.risknumber
              }),
              true
            )
          })
        })
      },
      /**
       * @description: 风险详细
       * @return {*}
       */
      showRiskDetail(row) {
        this.$refs['read'].showEdit(row, '', true)
      },
    },
    mounted() {},
  }
</script>

<style scoped>
  .left {
    padding-right: 10px;
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }
</style>
