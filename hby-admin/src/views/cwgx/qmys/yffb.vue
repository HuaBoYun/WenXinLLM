<!-- 收入分摊规则 -->
<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <czggTree @iconClick="iconClick" />
      </div>
      <div class="right">
        <el-card shadow="never" class="secondCard">
          <vab-query-form>
            <vab-query-form-right-panel style="width: 100%">
              <!-- <el-tooltip
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
              </el-tooltip> -->
              <el-button
                type="success"
                style="margin-right: 10px"
                @click="openDialog1"
              >
                新增
              </el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
          <el-form label-width="150px" class="form-section">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-form-item label="应用编码">
                  <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="应用名称">
                  <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="应用状态">
                  <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="节点类型">
                  <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="所属分类">
                  <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="预算任务">
                  <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="创建组织">
                  <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="表单范围">
                  <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="可编辑的业务方案">
                  <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-table :data="tableData1">
                  <el-table-column
                    label="参数范围"
                    prop="paramRange"
                    width="200"
                  ></el-table-column>
                  <el-table-column label="范围" prop="range"></el-table-column>
                </el-table>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
        <el-dialog title="新增" :visible.sync="dialogVisible1" width="60%">
          <div class="dialog-content">
            <el-form label-width="150px">
              <el-row :gutter="10">
                <el-col :span="12">
                  <el-form-item label="应用编码">
                    <el-input
                      v-model="taskForm.taskName"
                      placeholder="请输入应用编码"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="应用名称">
                    <el-input
                      v-model="taskForm.taskName"
                      placeholder="请输入应用名称"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="应用状态">
                    <el-input
                      v-model="taskForm.taskName"
                      placeholder="请输入应用状态"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="节点类型">
                    <el-input
                      v-model="taskForm.taskName"
                      placeholder="请输入节点类型"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="所属分类">
                    <el-input
                      v-model="taskForm.taskName"
                      placeholder="请输入所属分类"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="预算任务">
                    <el-input
                      v-model="taskForm.taskName"
                      placeholder="请输入预算任务"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创建组织">
                    <el-input
                      v-model="taskForm.taskName"
                      placeholder="请输入创建组织"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="表单范围">
                    <el-input
                      v-model="taskForm.taskName"
                      placeholder="请输入表单范围"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="可编辑的业务方案">
                    <el-input
                      v-model="taskForm.taskName"
                      placeholder="请输入可编辑的业务方案"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-table :data="tableData1">
                    <el-table-column
                      label="参数范围"
                      prop="paramRange"
                      width="200"
                    ></el-table-column>
                    <el-table-column
                      label="范围"
                      prop="range"
                    ></el-table-column>
                  </el-table>
                </el-col>
              </el-row>
            </el-form>
          </div>

          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible1 = false">取消(N)</el-button>
            <el-button type="danger" @click="submitForm">确定(Y)</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
  import { proposalNoticeList } from '@/api/monitor/question'
  import czggTree from './components/czggTree.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'lysgzqj',
    components: {
      filterSearch,
      filterTable,
      czggTree,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
        options: [
          {
            value: '选项1',
            label: '开启',
          },
          {
            value: '选项2',
            label: '关闭',
          },
        ],
        filedAll: [{ name: '名称' }, { name: '应用主体' }, { name: '创建人' }], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'cwgl-srgl-lysgzqj-search',
        tableKey: 'cwgl-srgl-lysgzqj-list',
        searchMore: false,
        showEdit: false,
        dialogVisible: false,
        form: {
          ruleName: '',
          controlType: 'rigid',
          controlSymbol: '>=',
          controlPercentage: '100',
        },
        dialogVisible1: false,
        activeTab: 'basic', // 控制当前显示的标签页
        controlForm: {
          // 控制规则表单数据
          businessSystem: 'marketing',
          startDate: '',
          documentType: '',
          dateType: '',
          accumulate: 'no',
          attribute: 'expenseAmount',
          endDate: '',
          direction: '',
        },
        tableData: [], // 控制规则表格数据
        // 新增任务表单数据
        taskForm: {
          taskName: '',
          taskCategory: 'dfs',
          taskType: '编制任务',
          organizationSystem: '',
          twoStageApproval: false,
          autoStartWhenCompiling: false,
          associatedFormSet: '',
        },
        searchKeyword: '',
        includeSubordinates: false,
        selectAll: false,
        tableData1: [
          {
            paramRange: '年',
            range: '',
          },
          {
            paramRange: '业务方案',
            range: '',
          },
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
      getFiled() {
        return [
          { name: '分摊比例', key: 'name' },
          { name: '编码', key: 'code' },
          { name: '启用状态', key: 'type' },
        ]
      },

      async fetchData() {
        this.listLoading = false
        const { date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (date) {
          startDate = date[0]
          endDate = date[1]
        }
        const {
          data: { tlist, totalRecord },
        } = await proposalNoticeList({ ...other, startDate, endDate })
        this.listLoading = false
        this.total = totalRecord
        // this.list = tlist.map((item) => ({
        //   ...item,
        //   createUser: item.createUser ? item.createUser.realname : "",
        // }));
      },
      iconClick() {
        this.showEdit = true
      },
      insertFunction(func) {
        this.formulaElements.splice(this.cursorPosition, 0, {
          value: func,
          type: 'function',
        })
        this.cursorPosition++
      },
      insertOperator(op) {
        this.formulaElements.splice(this.cursorPosition, 0, {
          value: op,
          type: 'operator',
        })
        this.cursorPosition++
      },
      addRow() {
        this.dialogVisible = true
      },
      setCursorPosition(index) {
        this.cursorPosition = index
      },
      removeElement(index) {
        this.formulaElements.splice(index, 1)
        if (this.cursorPosition > index) {
          this.cursorPosition--
        }
      },
      handleKeydown(event) {
        const { key } = event
        if (key === 'ArrowLeft') {
          this.cursorPosition = Math.max(0, this.cursorPosition - 1)
        } else if (key === 'ArrowRight') {
          this.cursorPosition = Math.min(
            this.formulaElements.length,
            this.cursorPosition + 1
          )
        } else if (key === 'Backspace') {
          this.removeElement(this.cursorPosition - 1)
        }
      },
      clearFormula() {
        this.formulaElements = []
        this.cursorPosition = 0
      },
      openDialog1() {
        this.dialogVisible1 = true
      },
      openDialog2() {
        // 新增预占数弹窗逻辑
        this.$message.info('新增预占数功能待实现')
      },
      submitForm() {
        // 表单验证
        if (!this.form.ruleName) {
          this.$message.error('请输入控制规则名称')
          return
        }
        if (!this.form.controlPercentage) {
          this.$message.error('请输入控制百分比')
          return
        }

        // 提交表单逻辑
        console.log('提交表单数据:', this.form)
        this.$message.success('提交成功')
        this.dialogVisible1 = false

        // 重置表单
        this.form = {
          ruleName: '',
          controlType: 'rigid',
          controlSymbol: '>=',
          controlPercentage: '100',
        }
      },
      addTableRow() {
        // 新增控制规则表格行
        this.tableData.push({
          serial: this.tableData.length + 1,
          archiveType: 'expenseOrg', // 默认选择"费用承担组织"
          archive: '',
          includeSub: false, // 包含下级默认为关闭状态
          mainOrg: true, // 主组织默认为开启状态
        })
      },
      deleteRow(index) {
        // 删除控制规则表格行
        this.tableData.splice(index, 1)
        this.tableData.forEach((row, i) => {
          row.serial = i + 1
        })
      },
      submitForm() {
        // 表单验证
        if (!this.taskForm.taskName) {
          this.$message.error('请输入任务名称')
          return
        }
        if (!this.taskForm.taskType) {
          this.$message.error('请选择任务类型')
          return
        }
        if (!this.taskForm.organizationSystem) {
          this.$message.error('请选择组织体系')
          return
        }
        if (!this.taskForm.associatedFormSet) {
          this.$message.error('请选择关联套表')
          return
        }

        // 提交表单逻辑
        console.log('提交任务表单数据:', this.taskForm)
        this.$message.success('提交成功')
        this.dialogVisible1 = false

        // 重置表单
        this.taskForm = {
          taskName: '',
          taskCategory: 'dfs',
          taskType: '编制任务',
          organizationSystem: '',
          twoStageApproval: false,
          autoStartWhenCompiling: false,
          associatedFormSet: '',
        }
        this.searchKeyword = ''
        this.includeSubordinates = false
        this.selectAll = false
        this.tableData = []
      },
    },
    computed: {
      formulaText() {
        return this.formulaElements.map((item) => item.value).join('')
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
    height: 80vh;
  }

  /* 移除el-input边框 */
  .el-input .el-input__inner {
    border: none !important;
    background: transparent !important;
  }

  .el-input .el-input__inner:focus {
    border: none !important;
    box-shadow: none !important;
  }

  .el-input .el-input__inner:hover {
    border: none !important;
  }

  .margin-b0 {
    margin-bottom: 0;
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    min-width: 250px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
    height: 80vh;
  }

  .lr-layout > .right {
    width: 80%;
    height: 80vh;
  }

  .formula-editor {
    display: flex;
    flex-direction: column;
    height: 100%;
  }

  .toolbar {
    display: flex;
    flex-direction: column;
    background-color: #f5f7fa;
    padding: 10px;
    border-radius: 4px;
    margin-bottom: 10px;
    border: 1px solid #e4e7ed;
  }

  .function-row,
  .operator-row {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
    margin-bottom: 8px;
    align-items: flex-start;
  }

  .function-row:last-child,
  .operator-row:last-child {
    margin-bottom: 0;
  }

  .function-btn {
    background-color: #e1f3ff !important;
    border-color: #91d5ff !important;
    color: #1890ff !important;
    font-size: 12px;
    padding: 4px 8px;
    min-width: 60px;
    margin-bottom: 2px;

    &:hover {
      background-color: #bae7ff !important;
      border-color: #69c0ff !important;
      color: #096dd9 !important;
    }
  }

  .operator-btn {
    background-color: #f5f5f5 !important;
    border-color: #d9d9d9 !important;
    color: #595959 !important;
    font-size: 12px;
    padding: 4px 8px;
    min-width: 40px;
    margin-bottom: 2px;

    &:hover {
      background-color: #e6f7ff !important;
      border-color: #91d5ff !important;
      color: #1890ff !important;
    }
  }

  .input-area {
    flex: 1;
    display: flex;
    flex-direction: column;
    background-color: #fff;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    overflow: hidden;
  }

  .formula-display {
    flex: 1;
    padding: 15px 15px 15px 80px; /* 左侧留出空间给固定按钮 */
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 5px;
    min-height: 80px;
    position: relative;
  }

  .formula-input-area {
    flex: 1;
    padding: 15px;
    display: flex;
    align-items: center;
    min-height: 80px;
    position: relative;
    background-color: #fff;
    gap: 15px;
  }

  .fixed-left-buttons {
    display: flex;
    gap: 5px;
    min-width: 120px;
  }

  .divider {
    width: 1px;
    height: 60px;
    background-color: #dcdfe6;
    margin: 0 10px;
  }

  .dynamic-formula-elements {
    flex: 1;
    display: flex;
    align-items: center;
    gap: 5px;
    min-height: 40px;
  }

  .formula-element {
    display: inline-block;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;
    cursor: pointer;
    user-select: none;
    transition: all 0.2s;
  }

  .function-element {
    background-color: #e1f3ff;
    border: 1px solid #91d5ff;
    color: #1890ff;
  }

  .operator-element {
    background-color: #f5f5f5;
    border: 1px solid #d9d9d9;
    color: #595959;
  }

  .cursor {
    width: 2px;
    height: 20px;
    background-color: #1890ff;
    animation: blink 1s infinite;
    display: inline-block;
    margin-left: 2px;
  }

  .formula-actions {
    padding: 10px;
    border-top: 1px solid #dcdfe6;
    background-color: #f5f7fa;
    text-align: right;
  }

  .fixed-buttons {
    position: absolute;
    left: 15px;
    top: 50%;
    transform: translateY(-50%);
    display: flex;
    gap: 5px;
    z-index: 1;
  }

  @keyframes blink {
    0%,
    50% {
      opacity: 1;
    }
    51%,
    100% {
      opacity: 0;
    }
  }

  /* 弹窗样式 */
  .dialog-content {
    padding: 20px 0;
  }

  .tab-navigation {
    display: flex;
    border-bottom: 1px solid #e4e7ed;
    margin-bottom: 20px;
  }

  .tab-item {
    padding: 10px 20px;
    cursor: pointer;
    border-bottom: 2px solid transparent;
    transition: all 0.3s;
    font-size: 14px;
    color: #606266;
  }

  .tab-item.active {
    color: #409eff;
    border-bottom-color: #409eff;
  }

  .tab-item:hover {
    color: #409eff;
  }

  .control-form {
    .form-row {
      display: flex;
      gap: 20px;
      margin-bottom: 20px;

      .el-form-item {
        flex: 1;
        margin-bottom: 0;
      }
    }
  }

  .dialog-footer {
    text-align: right;
    padding-top: 20px;
    border-top: 1px solid #e4e7ed;
  }

  /* 必填字段样式 */
  .el-form-item.is-required .el-form-item__label:before {
    content: '*';
    color: #f56c6c;
    margin-right: 4px;
  }

  /* 表格部分样式 */
  .table-section {
    margin-top: 20px;
    // border: 1px solid #e4e7ed;
    border-radius: 4px;
  }

  .table-section .el-table {
    margin: 0;
  }

  .table-section .el-table th {
    background-color: #f5f7fa;
  }

  /* 表格内控件样式 */
  .table-section .el-select {
    width: 100%;
  }

  .table-section .el-input {
    width: 100%;
  }

  .table-section .el-switch {
    margin: 0;
  }

  /* 表格行内控件对齐 */
  .table-section .el-table .cell {
    padding: 8px;
  }
  /* 表格行内控件对齐 */
  .table-section .el-table .cell {
    padding: 8px;
  }

  /* 新增任务弹窗样式 */
  .form-section {
    margin-bottom: 20px;
    padding: 20px;
    background-color: #fafafa;
    border-radius: 4px;
  }

  .form-section .el-form-item {
    margin-bottom: 15px;
  }

  .form-section .el-form-item__label {
    font-weight: 500;
    color: #333;
  }

  .form-section .el-form-item.is-required .el-form-item__label:before {
    content: '*';
    color: #f56c6c;
    margin-right: 4px;
  }

  .content-section {
    margin-top: 20px;
  }

  .left-panel,
  .right-panel {
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    background-color: #fff;
    height: 400px;
    display: flex;
    flex-direction: column;
  }

  .search-area {
    padding: 15px;
    border-bottom: 1px solid #e4e7ed;
    display: flex;
    align-items: center;
    gap: 20px;
  }

  .search-input {
    width: 100%;
  }

  .checkbox-area {
    padding: 10px 15px;
    border-bottom: 1px solid #e4e7ed;
    display: flex;
    gap: 20px;
  }

  .list-area {
    flex: 1;
    padding: 15px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .empty-list {
    text-align: center;
    color: #909399;
  }

  .empty-list i {
    font-size: 48px;
    margin-bottom: 10px;
    display: block;
  }

  .table-header {
    padding: 15px;
    border-bottom: 1px solid #e4e7ed;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 500;
  }

  .table-container {
    flex: 1;
    position: relative;
  }

  .right-panel .el-table {
    height: 100%;
  }

  .empty-table {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
    color: #909399;
  }

  .empty-table i {
    font-size: 48px;
    margin-bottom: 10px;
    display: block;
  }

  .dialog-footer {
    text-align: right;
    padding-top: 20px;
    border-top: 1px solid #e4e7ed;
  }

  .dialog-footer .el-button {
    margin-left: 10px;
  }

  /* 确保开关默认关闭状态 */
  .el-switch.is-checked .el-switch__core {
    border-color: #409eff;
    background-color: #409eff;
  }

  .el-switch .el-switch__core {
    border-color: #dcdfe6;
    background-color: #dcdfe6;
  }
  .form-section {
    /* 移除el-input边框 */
    :deep(.el-input .el-input__inner) {
      border: none !important;
      background: transparent !important;
    }

    :deep(.el-input .el-input__inner:focus) {
      border: none !important;
      box-shadow: none !important;
    }

    :deep(.el-input .el-input__inner:hover) {
      border: none !important;
    }
  }
</style>
