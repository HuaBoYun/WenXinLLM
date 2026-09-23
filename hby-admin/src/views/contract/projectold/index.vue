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
              v-model="queryForm.projectcode"
              clearable
              placeholder="项目编码"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.projectname"
              clearable
              placeholder="项目名称"
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
      <el-table-column align="center" label="项目编码" prop="projectcode" />
      <el-table-column align="center" label="项目名称" prop="projectname">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.projectname }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="承办人"
        prop="undertakestaff.realname"
      />
      <el-table-column align="center" label="项目预算" prop="reservednum1" />
      <!-- <el-table-column
        align="center"
        label="申报时间"
        prop="createtime"
        :formatter="formatDate"
      /> -->
      <el-table-column
        align="center"
        label="创建时间"
        prop="createtime"
        :formatter="formatDate"
      />
      <!-- <el-table-column align="center" label="审批状态" prop="state" /> -->
      <el-table-column align="center" label="操作" show-overflow-tooltip>
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
          <!-- <el-dropdown style="margin-left: 10px" @command="handleCommand">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>办理</el-dropdown-item>
              <el-dropdown-item>提交审批</el-dropdown-item>
              <el-dropdown-item @click.native="handleDelete(row)">
                删除
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown> -->
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
    <ProjectEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    contractProList,
    contractProDetail,
    contractProDel,
  } from '@/api/contract/project'
  import ProjectEdit from '@/views/contract/projectold/components/ProjectEdit'
  import { formatDate } from '@/utils/index'

  export default {
    name: 'Download',
    components: { ProjectEdit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectname: '',
          projectcode: '',
          pageNumber: 1,
          pageSize: 20,
        },
        pickerOptions: {
          shortcuts: [
            {
              text: '最近一周',
              onClick(picker) {
                const end = new Date()
                const start = new Date()
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
                picker.$emit('pick', [start, end])
              },
            },
            {
              text: '最近一个月',
              onClick(picker) {
                const end = new Date()
                const start = new Date()
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
                picker.$emit('pick', [start, end])
              },
            },
            {
              text: '最近三个月',
              onClick(picker) {
                const end = new Date()
                const start = new Date()
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
                picker.$emit('pick', [start, end])
              },
            },
          ],
        },
        value1: '',
        value2: '',
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await contractProList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        const data = await contractProDetail({ projectid: row.projectid })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      async handleEdit(row) {
        const data = await contractProDetail({ projectid: row.projectid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await contractProDel({
            projectid: row.projectid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
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
