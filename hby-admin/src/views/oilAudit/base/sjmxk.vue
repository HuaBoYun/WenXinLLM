<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <manage-tree @changeNode="changeNode"></manage-tree>
      </div>
      <div class="right">
        <vab-query-form>
          <!-- <vab-query-form-top-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm.rulecode"
                  clearable
                  placeholder="制度编号"
                />
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
                  native-type="submit"
                  type="primary"
                  @click="resetSearch"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-top-panel> -->
          <vab-query-form-left-panel>
            <span></span>
          </vab-query-form-left-panel>
          <vab-query-form-right-panel>
            <el-button type="success" @click="handleAdd">新建</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table v-loading="listLoading" :data="list">
          <el-table-column align="center" label="模型编号" prop="stepno" />
          <el-table-column align="center" label="模型名称" prop="steptitle" />
          <el-table-column
            align="center"
            label="关联数据源"
            prop="stepcontent"
          />

          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="180"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit(row)">修改</el-button>
              <el-button type="text" @click="handleDelete(row)">删除</el-button>
              <!-- <el-button type="text" @click="handleExecute(row)">
                执行
              </el-button>
              <el-button type="text" @click="handleResult(row)">结果</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <!-- <el-pagination
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        /> -->
        <Edit ref="edit" @fetchData="fetchData"></Edit>
        <Result ref="result" @fetchData="fetchData"></Result>
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
  } from '@/oapi/setting/org'
  import { UTCformat } from '@/utils/index'

  import ManageTree from './components/tree'
  import Edit from './components/editsjmxk.vue'
  import Result from './components/results.vue'

  export default {
    name: 'Consult',
    components: { ManageTree, Edit, Result },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          typeId: '',
        },
        nodeId: '',
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      changeNode(node) {
        this.queryForm.typeId = node.id
        this.nodeId = node.id
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return UTCformat(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { data },
        } = await getSJMXKList(this.queryForm)
        this.list = data
        this.listLoading = false
      },

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
      handleAdd() {
        if (!this.nodeId) {
          this.$message({
            type: 'error',
            message: '请先选择节点',
          })
          return
        }
        this.$refs['edit'].show({ nodeId: this.nodeId }, '新增')
      },
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
