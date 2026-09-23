<!-- 收入分摊规则 -->
<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <!-- 左侧边栏 -->
        <div class="sidebar">
          <div class="sidebar-header">
            <el-input
              v-model="selectedTask"
              class="task-selector"
              placeholder="请选择预算任务"
            >
              <i slot="suffix" class="el-icon-menu"></i>
            </el-input>
          </div>

          <div class="search-section">
            <el-input
              v-model="searchKeyword"
              class="search-input"
              placeholder="请搜索"
            >
              <i slot="suffix" class="el-icon-search"></i>
            </el-input>
          </div>
        </div>
      </div>
      <div class="right">
        <el-card shadow="never" class="secondCard">
          <div style="text-align: right">
            <el-button>版本复制</el-button>
            <el-button>更多</el-button>
          </div>
          <!-- 主内容区域 -->
          <div class="main-content">
            <!-- 顶部工具栏 -->
            <div class="top-toolbar">
              <div>fx:</div>
            </div>
            <!-- 导航标签 -->
            <div class="navigation-tabs">
              <div class="tabs-container">
                <span class="tab active">默认版本</span>
                <span class="tab">多版本查询</span>
              </div>
              <div class="task-status">
                <span class="status-label">任务状态:</span>
                <span class="status-value">{{ taskStatus }}</span>
              </div>
            </div>

            <!-- 主要内容显示区域 -->
            <div class="content-display">
              <!-- 这里可以放置表格、图表或其他内容 -->
            </div>
          </div>
        </el-card>
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

  .sidebar {
    width: 280px;
    background-color: #fff;
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 20px;
    height: 100%;
  }

  .sidebar-header {
    .task-selector {
      width: 100%;

      :deep(.el-input__inner) {
        border-radius: 4px;
        border: 1px solid #d9d9d9;
      }

      :deep(.el-input__suffix) {
        display: flex;
        align-items: center;
        height: 100%;
      }

      :deep(.el-input__suffix-inner) {
        display: flex;
        align-items: center;
        height: 100%;
      }

      :deep(.el-input__suffix i) {
        line-height: 1;
        vertical-align: middle;
      }
    }
  }

  .search-section {
    .search-input {
      width: 100%;

      :deep(.el-input__inner) {
        border-radius: 4px;
        border: 1px solid #d9d9d9;
      }

      :deep(.el-input__suffix) {
        display: flex;
        align-items: center;
        height: 100%;
      }

      :deep(.el-input__suffix-inner) {
        display: flex;
        align-items: center;
        height: 100%;
      }

      :deep(.el-input__suffix i) {
        line-height: 1;
        vertical-align: middle;
      }
    }
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

  .budget-interface {
    display: flex;
    height: 100vh;
    background-color: #f6f8f9;
  }

  .main-content {
    flex: 1;
    background-color: #ffffff;
    display: flex;
    flex-direction: column;
  }

  .left-controls {
    display: flex;
    align-items: center;
    gap: 15px;
  }

  .collapse-btn {
    background-color: #409eff;
    border-color: #409eff;
    width: 32px;
    height: 32px;
    padding: 0;
  }

  .formula-input {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .formula-label {
    font-size: 14px;
    color: #333;
    font-weight: 500;
  }

  .formula-field {
    width: 200px;

    :deep(.el-input__inner) {
      border-radius: 4px;
      border: 1px solid #d9d9d9;
    }
  }

  .navigation-tabs {
    padding: 15px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #e0e0e0;
    background-color: #ffffff;
  }

  .tabs-container {
    display: flex;
    gap: 30px;
  }

  .tab {
    font-size: 14px;
    color: #666;
    cursor: pointer;
    padding: 5px 0;
    border-bottom: 2px solid transparent;
    transition: all 0.3s;

    &.active {
      color: #409eff;
      border-bottom-color: #409eff;
    }

    &:hover {
      color: #409eff;
    }
  }

  .task-status {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .status-label {
    font-size: 14px;
    color: #666;
  }

  .status-value {
    font-size: 14px;
    color: #333;
    font-weight: 500;
  }

  .content-display {
    // flex: 1;
    background-color: #ffffff;
    // padding: 20px;
    height: 60vh;
    // 主要内容区域，可以根据需要添加内容
  }

  // 响应式设计
  @media (max-width: 768px) {
    .sidebar {
      width: 200px;
    }

    .formula-field {
      width: 150px;
    }

    .tabs-container {
      gap: 20px;
    }
  }
  .top-toolbar {
    background-color: #f6f8f9;
    padding: 5px 20px;
    margin-top: 10px;
  }
</style>
