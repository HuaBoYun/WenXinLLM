<!-- 收入分摊规则 -->
<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <czggTree @iconClick="iconClick" :showType="false" />
      </div>
      <div class="right">
        <el-card shadow="never" class="secondCard">
          <vab-query-form>
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
                  <!-- <i class="el-icon-delete" slot="reference"></i> -->
                  <el-button
                    slot="reference"
                    icon="el-icon-s-grid"
                    class="biaoge"
                    style="margin-bottom: 10px; margin-right: 10px"
                  ></el-button>
                </el-popover>
              </el-tooltip>
              <el-button
                type="success"
                style="margin-right: 10px"
                @click="openDialog1"
              >
                新增执行数
              </el-button>
              <el-button
                type="success"
                style="margin-right: 10px"
                @click="openDialog2"
              >
                新增预占数
              </el-button>
            </vab-query-form-right-panel>

            <el-table>
              <el-table-column prop="name" label="序号" />
              <div v-for="(item, index) in filedNow" :key="index">
                <el-table-column
                  v-if="item.name === '名称'"
                  prop="name"
                  label="名称"
                />
                <el-table-column
                  v-if="item.name === '应用主体'"
                  prop="name"
                  label="应用主体"
                />
                <el-table-column
                  v-if="item.name === '创建人'"
                  prop="name"
                  label="创建人"
                />
              </div>
            </el-table>
          </vab-query-form>
        </el-card>
        <el-dialog
          title="新增执行数"
          :visible.sync="dialogVisible1"
          width="70%"
        >
          <div class="dialog-content">
            <!-- 标签页导航 -->
            <div class="tab-navigation">
              <div
                class="tab-item"
                :class="{ active: activeTab === 'basic' }"
                @click="activeTab = 'basic'"
              >
                基本信息
              </div>
              <div
                class="tab-item"
                :class="{ active: activeTab === 'control' }"
                @click="activeTab = 'control'"
              >
                控制规则
              </div>
            </div>

            <!-- 基本信息标签页 -->
            <div v-show="activeTab === 'basic'">
              <el-form :model="form" label-width="120px" class="control-form">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="控制规则名称" required>
                      <el-input
                        v-model="form.ruleName"
                        placeholder="请输入控制规则名称"
                        style="width: 100%"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="控制类型">
                      <el-select
                        v-model="form.controlType"
                        placeholder="请选择控制类型"
                        style="width: 100%"
                      >
                        <el-option label="刚性控制" value="rigid"></el-option>
                        <el-option
                          label="柔性控制"
                          value="flexible"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="控制符">
                      <el-select
                        v-model="form.controlSymbol"
                        placeholder="请选择控制符"
                        style="width: 100%"
                      >
                        <el-option label=">=" value=">="></el-option>
                        <el-option label="<=" value="<="></el-option>
                        <el-option label="=" value="="></el-option>
                        <el-option label=">" value=">"></el-option>
                        <el-option label="<" value="<"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="控制百分比" required>
                      <el-input
                        v-model="form.controlPercentage"
                        placeholder="100%"
                        suffix="%"
                        style="width: 100%"
                      >
                        <template slot="append">%</template>
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </div>

            <!-- 控制规则标签页 -->
            <div v-show="activeTab === 'control'">
              <el-form
                :model="controlForm"
                label-width="120px"
                class="control-form"
              >
                <el-row>
                  <el-col :span="8">
                    <el-form-item label="业务系统" required>
                      <el-select
                        v-model="controlForm.businessSystem"
                        placeholder="请选择业务系统"
                        style="width: 100%"
                      >
                        <el-option
                          label="营销费用管理"
                          value="marketing"
                        ></el-option>
                        <el-option label="其他系统" value="other"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="起始日期" required>
                      <el-date-picker
                        v-model="controlForm.startDate"
                        type="date"
                        placeholder="选择起始日期"
                        style="width: 100%"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="单据类型" required>
                      <el-select
                        v-model="controlForm.documentType"
                        placeholder="请选择单据类型"
                        style="width: 100%"
                      >
                        <el-option label="费用单据" value="expense"></el-option>
                        <el-option
                          label="报销单据"
                          value="reimburse"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="8">
                    <el-form-item label="日期类型" required>
                      <el-select
                        v-model="controlForm.dateType"
                        placeholder="请选择日期类型"
                        style="width: 100%"
                      >
                        <el-option
                          label="单据日期"
                          value="document"
                        ></el-option>
                        <el-option label="创建日期" value="create"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="累计" required>
                      <el-select
                        v-model="controlForm.accumulate"
                        placeholder="请选择累计方式"
                        style="width: 100%"
                      >
                        <el-option label="不累计" value="no"></el-option>
                        <el-option label="按日累计" value="daily"></el-option>
                        <el-option label="按月累计" value="monthly"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="属性" required>
                      <el-select
                        v-model="controlForm.attribute"
                        placeholder="请选择属性"
                        style="width: 100%"
                      >
                        <el-option
                          label="费用支持金额"
                          value="expenseAmount"
                        ></el-option>
                        <el-option label="其他属性" value="other"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="8">
                    <el-form-item label="结束日期" required>
                      <el-date-picker
                        v-model="controlForm.endDate"
                        type="date"
                        placeholder="选择结束日期"
                        style="width: 100%"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="方向">
                      <el-select
                        v-model="controlForm.direction"
                        placeholder="请选择方向"
                        style="width: 100%"
                      >
                        <el-option label="正向" value="forward"></el-option>
                        <el-option label="反向" value="reverse"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <!-- 表格部分 -->
                    <div class="table-section">
                      <div
                        style="
                          text-align: right;
                          margin-bottom: 10px;
                          margin-top: 10px;
                        "
                      >
                        <el-button type="primary" @click="addTableRow">
                          增行
                        </el-button>
                      </div>

                      <el-table :data="tableData" border style="width: 100%">
                        <el-table-column prop="serial" label="序号" />
                        <el-table-column
                          prop="archiveType"
                          label="基础档案类型"
                        >
                          <template slot-scope="scope">
                            <el-select
                              v-model="scope.row.archiveType"
                              placeholder="请选择基础档案类型"
                              style="width: 100%"
                            >
                              <el-option
                                label="费用承担组织"
                                value="expenseOrg"
                              ></el-option>
                              <el-option
                                label="部门"
                                value="department"
                              ></el-option>
                              <el-option
                                label="项目"
                                value="project"
                              ></el-option>
                              <el-option
                                label="客户"
                                value="customer"
                              ></el-option>
                            </el-select>
                          </template>
                        </el-table-column>
                        <el-table-column prop="archive" label="基础档案">
                          <template slot-scope="scope">
                            <el-input
                              v-model="scope.row.archive"
                              placeholder="请输入基础档案"
                            />
                          </template>
                        </el-table-column>
                        <el-table-column prop="includeSub" label="包含下级">
                          <template slot-scope="scope">
                            <el-switch v-model="scope.row.includeSub" />
                          </template>
                        </el-table-column>
                        <el-table-column prop="mainOrg" label="主组织">
                          <template slot-scope="scope">
                            <el-switch v-model="scope.row.mainOrg" />
                          </template>
                        </el-table-column>
                        <el-table-column label="操作" width="120">
                          <template slot-scope="scope">
                            <el-button
                              type="text"
                              size="small"
                              @click="deleteRow(scope.$index)"
                            >
                              删除
                            </el-button>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                  </el-col>
                </el-row>
              </el-form>
            </div>
          </div>

          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible1 = false">取消</el-button>
            <el-button type="primary" @click="submitForm">确定</el-button>
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
</style>
