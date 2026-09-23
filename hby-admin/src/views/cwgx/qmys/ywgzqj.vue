<!-- 收入分摊规则 -->
<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <czggTree @iconClick="iconClick" />
      </div>
      <div class="right">
        <el-card shadow="never">
          <vab-query-form>
            <vab-query-form-left-panel>
              <span></span>
            </vab-query-form-left-panel>
            <vab-query-form-right-panel>
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
              <el-button type="success" v-if="showEdit">保存</el-button>
              <el-button v-if="showEdit">取消</el-button>
              <!-- <el-button type="success" @click="handleSend()">下发</el-button>
              <el-button type="danger" @click="handleBack()">撤回</el-button> -->
            </vab-query-form-right-panel>
            <el-form :model="queryForm" label-width="100px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="规则名称">
                    <el-input
                      v-model="queryForm.name"
                      placeholder="请输入规则名称"
                      v-if="showEdit"
                    />
                    <span v-else></span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="规则说明">
                    <el-input
                      v-model="queryForm.name"
                      placeholder="请输入规则说明"
                      v-if="showEdit"
                    />
                    <span v-else></span>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-divider content-position="center">规则明细</el-divider>
                </el-col>
                <el-col :span="24">
                  <div style="text-align: right; margin-bottom: 10px">
                    <el-button v-if="showEdit" @click="addRow">增行</el-button>
                  </div>
                  <el-table :data="tableData" style="width: 100%">
                    <el-table-column prop="name" label="序号" />
                    <el-table-column prop="name" label="名称" />
                    <el-table-column prop="name" label="指标" />
                    <el-table-column prop="name" label="生效表单" />
                    <el-table-column prop="name" label="描述+公式" />
                  </el-table>
                </el-col>
              </el-row>
            </el-form>
          </vab-query-form>
        </el-card>
      </div>
    </div>
    <fyhjrjgBaseEdit ref="fyhjrjgBaseEdit" @fetchData="fetchData" />
    <el-dialog title="新增" :visible.sync="dialogVisible" width="60%">
      <el-form :model="form" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-divider content-position="center">基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="描述">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider content-position="center">规则配置</el-divider>
          </el-col>
          <el-col :span="24">
            <div class="formula-editor">
              <!-- 工具栏 -->
              <div class="toolbar">
                <!-- 函数按钮行 -->
                <div class="function-row">
                  <el-button
                    v-for="func in functionButtons"
                    :key="func"
                    size="mini"
                    class="function-btn"
                    @click="insertFunction(func)"
                  >
                    {{ func }}
                  </el-button>
                </div>
                <!-- 操作符按钮行 -->
                <div class="operator-row">
                  <el-button
                    v-for="op in operatorButtons"
                    :key="op"
                    size="mini"
                    class="operator-btn"
                    @click="insertOperator(op)"
                  >
                    {{ op }}
                  </el-button>
                </div>
              </div>
              <!-- 输入区域 -->
              <div class="input-area">
                <!-- 底部输入区域 -->
                <div
                  class="formula-input-area"
                  @keydown="handleKeydown"
                  tabindex="0"
                >
                  <!-- 左侧固定按钮 -->
                  <div class="fixed-left-buttons">
                    <div class="formula-element function-element">FIND</div>
                    <div class="formula-element operator-element">=</div>
                  </div>
                  <!-- 中间分割线 -->
                  <div class="divider"></div>
                  <!-- 右侧动态公式元素 -->
                  <div class="dynamic-formula-elements">
                    <div
                      v-for="(item, index) in formulaElements"
                      :key="index"
                      class="formula-element"
                      :class="
                        item.type === 'function'
                          ? 'function-element'
                          : 'operator-element'
                      "
                      @click="setCursorPosition(index)"
                      @dblclick="removeElement(index)"
                    >
                      {{ item.value }}
                    </div>
                    <div class="cursor"></div>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  import {
    proposalNoticeList,
    proposalNoticeDelete,
  } from '@/api/monitor/question'
  import { getFlowPkInfo } from '@/api/contract/manage'
  // import fyhjrjgBaseEdit from './components/fyhjrjgBaseEdit.vue'
  import czggTree from './components/czggTree.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'srftgz',
    components: {
      // fyhjrjgBaseEdit,
      czggTree,
      filterSearch,
      filterTable,
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
        filedAll: [
          { name: '序号' },
          { name: '分摊方式' },
          { name: '分摊比例' },
          { name: '起始日期' },
          { name: '分摊期数' },
          { name: '均摊计算方式' },
          { name: '截止日期' },
          { name: '说明' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'cwgl-srgl-srftgz-search',
        tableKey: 'cwgl-srgl-srftgz-list',
        searchMore: false,
        showEdit: false,
        dialogVisible: false,
        form: {},
        data: ['FIND', 'PLUSF', 'MINUSF', 'MULTIF', 'DIVF', 'MODF', 'POWF'],
        data1: ['=', '+', '-', '*', '/', '%', '^'],
        formulaText: '', // 公式文本
        functionButtons: [
          'FIND',
          'PLUSF',
          'MINUSF',
          'CROSS',
          'EXRATE',
          'WARNING',
          'SUBSTRING',
          'IIF',
          'UFIND',
          'PFIND',
          'UFO',
          'CELL_IUFO',
          '常量',
        ], // 函数按钮
        operatorButtons: [
          '=',
          '+',
          '-',
          '*',
          '/',
          '>',
          '>=',
          '<',
          '<=',
          '(',
          ')',
          'AND',
          'OR',
        ], // 操作符按钮
        formulaElements: [], // 公式元素数组
        cursorPosition: 0, // 光标位置
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
</style>
