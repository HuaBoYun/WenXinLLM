<template>
  <div class="system-log-container">
    <div>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="缺陷编号"
          prop="bugnumber"
          width="170"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.bugnumber }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="缺陷描述"
          prop="bugdescripte"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="发现时间"
          prop="discovertime"
          show-overflow-tooltip
          :formatter="formatDate"
        />
        <el-table-column
          align="center"
          label="缺陷类别"
          prop="bugproperty"
          show-overflow-tooltip
        />
      </el-table>
      <el-pagination
        class="pagination"
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>

    <flaw-info ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { findOrganizationByTreeAllss } from '@/oapi/audit/implement'
  import {
    bugcriidList,
    defectDel,
    defectDetail,
    defectFileExport,
    defectList,
  } from '@/oapi/audit/question'
  import { formatDay, parseTime } from '@/utils/index'
  import FlawInfo from '@/views/oilAudit/question/components/FlawInfo'

  export default {
    name: 'Download',
    components: { FlawInfo },
    data() {
      return {
        list: [],
        bugcriidList: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          bugnumber: '',
          bugcriid: '',
          orgid: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        },
        orgId: '',
        current: undefined,
        flagTitle: false,
      }
    },
    created() {
      this.fetchData()
      this.fectchBugcriidList()
      //初始化树结构
      this.getTreeData()
    },
    methods: {
      async getTreeData() {
        let res = await findOrganizationByTreeAllss()
        this.orgId = res[0].id
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      async fectchBugcriidList() {
        const res = await bugcriidList()
        this.bugcriidList = res.data.list
      },
      setTree(node) {
        let id = node.pId === 1 ? undefined : node.id
        this.queryForm.orgid = id
        this.orgId = node.id
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await defectList({ ...other, startDate, endDate })
        this.list = list
        this.list.forEach((item) => {
          item.discovertime = parseTime(item.discovertime, '{y}-{m}-{d}')
        })
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        // const data = await defectDetail({ bugid: row.bugid })
        await this.$refs['edit'].showEdit('detail', row)
      },
      async handleEdit(row) {
        const data = await defectDetail({ bugid: row.bugid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await defectDel({
            bugid: row.bugid,
          })
          if (code == 0) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      async handleExport(row) {
        const data = await defectFileExport({ orgId: this.orgId })
        let fileName = 'test'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
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
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    /* width: 200px; */
    /* width: 15%; */
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 85%;
  }
  .pagination {
    width: 60%;
  }
</style>
