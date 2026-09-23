<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="模型编号" prop="stepno" />
        <el-table-column align="center" label="模型名称" prop="steptitle" />
        <el-table-column align="center" label="关联数据源" prop="stepcontent" />

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="180"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">预览</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- <Edit ref="edit" @fetchData="fetchData"></Edit> -->
      <!-- <Result ref="result" @fetchData="fetchData"></Result> -->
    </div>
    <SqlModal ref="check"></SqlModal>
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
  import SqlModal from '@/views/oilAudit/base/components/sqlCheck.vue'

  export default {
    name: 'Consult',
    components: { SqlModal },
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
        this.$refs['check'].show(row.sqlstr, row.bookid)
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
