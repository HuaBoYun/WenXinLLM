<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.templeteName"
              clearable
              placeholder="模板名称"
            />
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.status" placeholder="请选择">
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="-1" />
            </el-select>
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
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="模板编号" prop="templeteCode">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.templeteCode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="模板名称" prop="templeteName">
        <!-- <template #default="{ row }">
          <el-button @click="handleView(row, true)" type="text">
            {{ row.templeteName }}
          </el-button>
        </template> -->
      </el-table-column>
      <el-table-column align="center" label="审计类型" prop="templeteType" />
      <el-table-column align="center" label="适用机构" prop="temorgname" />
      <el-table-column align="center" label="创建人" prop="createstaffname" />
      <el-table-column
        align="center"
        :formatter="formatDate"
        label="创建日期"
        prop="createDate"
      />
      <el-table-column align="center" label="状态" prop="status">
        <template #default="{ row }">
          {{ row.status === 1 ? '启用' : '禁用' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-dropdown style="margin-left: 10px">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleDelete(row)">
                删除
              </el-dropdown-item>
              <el-dropdown-item
                v-if="row.status == -1"
                @click.native="changeStatus(row)"
              >
                启用
              </el-dropdown-item>
              <el-dropdown-item
                v-if="row.status == 1"
                @click.native="changeStatus(row)"
              >
                禁用
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleCopy(row)">
                复制
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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
    <GuideEdit ref="edit" @fetch-data="fetchData" />
    <Table ref="table" />
  </div>
</template>

<script>
  import {
    copyTemplete,
    deleteTempleteInfo,
    getNbsjZyTempletePageList,
    updateTempleteStatus,
  } from '@/api/workbench/auditTools'
  import { parseTime } from '@/utils/index'
  import GuideEdit from '@/views/workbench/auditTools/components/GuideEdit'
  import Table from '@/views/workbench/auditTools/components/options/table'

  export default {
    name: 'Consult',
    components: { GuideEdit, Table },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          status: '',
          templeteName: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
      handleDetail(row) {
        this.$refs['table'].show(row.templeteId)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getNbsjZyTempletePageList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleView(row, flag) {
        this.$refs['edit'].showEdit(row, flag)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteTempleteInfo(row.templeteId)
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
      changeStatus(row) {
        this.$baseConfirm('你确定要修改当前项状态吗', null, async () => {
          const { msg } = await updateTempleteStatus(row.templeteId)
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleCopy(row) {
        this.$baseConfirm('是否复制到审计经验库', null, async () => {
          const { msg } = await copyTemplete({
            copytype: 2,
            templeteId: row.templeteId,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
    },
  }
</script>
