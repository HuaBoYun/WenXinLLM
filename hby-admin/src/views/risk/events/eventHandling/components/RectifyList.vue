<template>
  <el-dialog
    :close-on-click-modal="false"
    title="整改列表"
    :visible.sync="dialogListVisible"
    width="1000px"
    @close="close"
  >
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
              v-model="queryForm.code"
              clearable
              placeholder="整改编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="整改名称"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.userName"
              clearable
              placeholder="整改负责人"
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
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" @click="reset">重置</el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column
        align="center"
        label="整改编号"
        prop="solutioncode"
        width="100"
      />
      <el-table-column
        align="center"
        label="整改名称"
        prop="solutionname"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="机构" prop="rectifydepart" />
      <el-table-column
        align="center"
        label="整改建议"
        prop="riskevent"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        align="center"
        label="被整改部门"
        prop="rectifydepart"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        align="center"
        label="整改负责人"
        prop="rectifyhead"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column align="center" label="操作">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
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
    <RectifyEdit ref="edit" />
  </el-dialog>
</template>

<script>
  import { getList, findByeventid } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import RectifyEdit from './RectifyEdit.vue'

  export default {
    name: 'EventHandling',
    components: { RectifyEdit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          eventid: '',
          pageNo: 1,
          pageSize: 20,
        },
        dialogListVisible: false,
      }
    },
    created() {
      // this.fetchData()
    },
    methods: {
      reset() {
        this.queryForm.code = null
        this.queryForm.name = null
        this.queryForm.userName = null
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageBean: { records },
            total,
          },
        } = await findByeventid(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleView(row) {
        console.log('view', row)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      async showList(row) {
        console.log(row)
        this.queryForm.eventid = row.riseveid
        this.fetchData()
        this.dialogListVisible = true
      },
      close() {
        this.dialogListVisible = false
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
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
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 250px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
