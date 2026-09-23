<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="缺陷级别" prop="bugcrilevel">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.bugcrilevel }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="缺陷类别" prop="bugtype" />
      <el-table-column align="center" label="定义" prop="bugcridefine" />
      <el-table-column align="center" label="定量标准" prop="bugcriration" />
      <el-table-column align="center" label="定性标准" prop="bugcristability" />
      <el-table-column align="center" label="状态" prop="status">
        <template #default="{ row }">
          {{ row.status === 1 ? '禁用' : '正常' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleEdit(row)"
            :disabled="createId != row.createstaffid"
          >
            修改
          </el-button>
          <el-button
            type="text"
            @click="handleDelete(row)"
            :disabled="createId != row.createstaffid"
          >
            删除
          </el-button>
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
    <FlawEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getNbsjBugCriterionPageList,
    quexianDel,
  } from '@/api/workbench/auditTools'
  import FlawEdit from '@/views/workbench/auditTools/components/FlawEdit'

  export default {
    name: 'Consult',
    components: { FlawEdit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, true)
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
          data: { pageInfo, total },
        } = await getNbsjBugCriterionPageList(this.queryForm)
        this.list = pageInfo.tlist
        this.total = pageInfo.totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },

      handleEdit(row) {
        // if (this.createId != row.createStaffId) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        // if (this.createId != row.createStaffId) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await quexianDel({ bugcriid: row.bugcriid })
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
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
    },
  }
</script>
