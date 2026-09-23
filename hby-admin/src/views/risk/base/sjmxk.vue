<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <manage-tree @changeNode="changeNode"></manage-tree>
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-left-panel :span="24">
            <el-form :model="queryForm" :inline="true">
              <el-form-item>
                <el-input
                  v-model="queryForm.stepno"
                  placeholder="请输入模型编号"
                  clearable
                  style="width: 200px"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryForm.steptitle"
                  placeholder="请输入模型名称"
                  clearable
                  style="width: 200px"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="queryData">搜索</el-button>
                <el-button @click="resetSearch">重置</el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
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
            <el-button type="success" @click="showRuleDraw(row, 'add')">
              新建规则拖拽
            </el-button>
            <el-button type="success" @click="handleAdd">新建</el-button>
            <el-button type="success" @click="handleSend">下发</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          v-loading="listLoading"
          :data="list"
          @select-all="handleSelectAll"
          @select="handleSelection"
          ref="multipleTable"
        >
          <el-table-column width="1" />
          <el-table-column
            type="selection"
            width="55"
            :selectable="checkSelectable"
          ></el-table-column>
          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              align="center"
              v-if="item.name === '模型编号'"
              label="模型编号"
              prop="stepno"
            />
            <el-table-column
              align="center"
              v-if="item.name === '模型名称'"
              label="模型名称"
              prop="steptitle"
            />
            <el-table-column
              align="center"
              v-if="item.name === '关联数据源'"
              label="关联数据源"
              prop="stepcontent"
            />
            <el-table-column
              align="center"
              v-if="item.name === '状态'"
              label="状态"
              prop="stepcontent"
            >
              <template #default="{ row }">
                <el-tag type="danger" v-if="row.qystatus == 1">禁用</el-tag>
                <el-tag type="success" v-else-if="row.qystatus == 0">
                  启用
                </el-tag>
                <el-tag type="info" v-else>未启用</el-tag>
              </template>
            </el-table-column>
          </div>

          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="180"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit(row)">修改</el-button>
              <el-button
                type="text"
                @click="handleStart(row)"
                v-if="row.qystatus == 0"
              >
                禁用
              </el-button>
              <el-button type="text" @click="handleStart(row)" v-else>
                启用
              </el-button>
              <el-button type="text" @click="handleCancelSend(row)">
                取消下发
              </el-button>
              <el-button type="text" @click="handleDelete(row)">删除</el-button>
              <!-- <el-button type="text" @click="handleExecute(row)">
                执行
              </el-button>
              <el-button type="text" @click="handleResult(row)">结果</el-button> -->
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
        <Edit ref="edit" @fetchData="fetchData"></Edit>
        <Result ref="result" @fetchData="fetchData"></Result>
        <selectPerson
          ref="selectPerson"
          @projectManage="handleSendRiskModel"
        ></selectPerson>
        <xiafaPerson ref="xiafaPerson" @fetchData="fetchData"></xiafaPerson>
      </div>
    </div>
  </div>
</template>

<script>
  import {
    getSJMXKList,
    deleteSJMXKInfo,
    getSJMXKDetailInfo,
    executeSql,
  } from '@/api/setting/org'
  import { UTCformat } from '@/utils/index'
  import {
    enableOrDisableRiskModel,
    sendRiskModel,
    querySendRiskModelList,
  } from '@/api/risk'
  import ManageTree from './components/tree'
  import Edit from './components/editsjmxk.vue'
  import Result from './components/results.vue'
  import { searchTableMixis } from '@/mixis/index'
  import filterTable from '@/components/filterTable'
  import selectPerson from '@/components/selectPerson.vue'
  import xiafaPerson from './components/xiafaPerson.vue'
  export default {
    name: 'Consult',
    mixins: [searchTableMixis],
    components: {
      filterTable,
      ManageTree,
      Edit,
      Result,
      selectPerson,
      xiafaPerson,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          typeId: '',
          stepno: '',
          steptitle: '',
          mpdeltype: 'FXCT',
          pageNumber: 1,
          pageSize: 20,
        },
        nodeId: '',
        // 筛选列表配置
        filedAll: [
          { name: '模型编号' },
          { name: '模型名称' },
          { name: '关联数据源' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        localKey: 'risk-base-sjmxk-search',
        tableKey: 'risk-base-sjmxk-list',
        select: [],
      }
    },
    created() {
      this.fetchData()
      this.initTable()
    },
    methods: {
      // 检查行是否可选择（只有启用状态的行可选择）
      checkSelectable(row) {
        return row.qystatus === 0 // 只有qystatus为0表示启用，才可选择
      },
      resetQueryForm() {
        const currentTypeId = this.queryForm.typeId
        this.queryForm = this.$options.data().queryForm
        this.queryForm.typeId = currentTypeId
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      changeNode(node) {
        this.queryForm.typeId = node.id
        this.nodeId = node.id
        this.queryForm.stepno = ''
        this.queryForm.steptitle = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return UTCformat(data)
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
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getSJMXKList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        this.setCheckedRows()
      },

      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteSJMXKInfo({ stepId: row.stepid })
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
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        if (!this.nodeId) {
          this.$message({
            type: 'error',
            message: '请先选择节点',
          })
          return
        }
        this.$refs['edit'].close()
        this.$refs['edit'].show({ nodeId: this.nodeId }, '新增')
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        const res = await getSJMXKDetailInfo({ stepId: row.stepid })
        this.$refs['edit'].show(res.data.data, '编辑')
      },
      async handleExecute(row) {
        executeSql({ stepId: row.stepid }).then((res) => {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
        })
      },
      handleResult(row) {
        //弹框
        this.$refs['result'].show(row)
      },
      showRuleDraw(row) {
        // this.$refs['drawModal'].showEdit(row)
        window.open('http://192.0.2.16:8090/urule/frame')
      },
      handleStart(row) {
        enableOrDisableRiskModel({
          stepId: row.stepid,
          xgstatus: row.qystatus === 0 ? 1 : 0,
        }).then((res) => {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        })
      },
      handleSelection(val, row) {
        // 只有启用状态的行才允许选择
        if (row.qystatus !== 0) {
          return
        }
        const i = this.select.findIndex((x) => x.stepid == row.stepid)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            // 只选择启用状态的行
            if (
              row &&
              row.qystatus === 0 &&
              !this.select.some((x) => x.stepid == row.stepid)
            ) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.stepid == row.stepid)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            const foundItem = this.list.find((item) => {
              return row.stepid == item.stepid
            })
            // 只回显启用状态的行
            if (foundItem && foundItem.qystatus === 0) {
              this.$refs.multipleTable.toggleRowSelection(foundItem, true)
            }
          })
        })
      },
      handleSend() {
        if (this.select.length === 0) {
          this.$baseMessage(
            '请选择要下发的模型',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$refs['selectPerson'].showEdit()
        // sendRiskModel({ ids: this.select.map((item) => item.stepid) }).then(
        //   (res) => {
        //     this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
        //     this.fetchData()
        //   }
        // )
      },
      handleSendRiskModel(val) {
        const ids = this.select.map((item) => item.stepid).join(',')
        const staffids = val.map((item) => item.staffid).join(',')
        sendRiskModel({ stepIds: ids, staffids: staffids }).then((res) => {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        })
      },
      handleCancelSend(row) {
        querySendRiskModelList({ stepId: row.stepid }).then((res) => {
          this.$refs['xiafaPerson'].showEdit(res.data.data, row.stepid)
        })
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
