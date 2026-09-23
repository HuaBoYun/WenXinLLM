<template>
  <div class="system-log-container">
    <div>
      <div>
        <el-table
          v-loading="listLoading"
          :data="list"
          ref="multipleTable"
          :row-key="getRowKeys"
          @selection-change="handleSelectionChange"
        >
          <el-table-column align="center" label="资料编号">
            <template #default="{ row }">
              <el-button type="text" @click="handleAdd(row, true)">
                {{ row.projectDataPreId }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="资料名称" prop="dataName" />

          <el-table-column
            align="center"
            label="所属项目"
            prop="projectname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="创建人"
            prop="username"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="创建时间"
            prop="dataDate"
            show-overflow-tooltip
          />
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
      </div>
    </div>
    <project-data-info ref="projectDataInfo" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { whetherLeader } from '@/oapi/audit/implement'
  import {
    dataProjectDel,
    findOrganizationByTreeAllss,
    getDataprojectList,
  } from '@/oapi/audit/preparation'
  import { UTCformat } from '@/utils'

  import ProjectDataInfo from '@/views/oilAudit/prepare/components/ProjectDataInfo'

  export default {
    name: 'Download',
    components: { ProjectDataInfo },
    data() {
      return {
        list: [],
        listLoading: true,
        dataTree: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
        selectProjectId: [],
        showButton: true,
      }
    },
    async created() {
      this.fetchData()
      let res11 = await whetherLeader() //判断是否为组长
      if (res11.data.ifLeader) {
        this.showButton = true
      } else {
        this.showButton = false
      }
    },
    methods: {
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getDataprojectList(this.queryForm)
        this.list = tlist
        this.list.forEach((item) => {
          item.dataDate = UTCformat(item.dataDate)
        })
        this.total = totalRecord
        const res = await findOrganizationByTreeAllss(this.queryForm)
        this.dataTree = res
        this.listLoading = false
      },
      handleAdd(row, falg) {
        this.$refs['projectDataInfo'].showEdit(row, false)
      },
      // handleEdit(row) {
      //   this.$refs['edit'].showEdit(row, true)
      // },
      handleFile(row) {
        this.$refs['file'].showEdit(row)
      },
      hadnlePush() {
        this.$refs['person'].showEdit(this.select, this.selectProjectId)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await dataProjectDel({ dataId: row.dataId })
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
      // 2、设置row-key
      getRowKeys(row) {
        return row.dataId
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        this.select = selection.map((item) => item.dataId)
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
    width: 100%;
  }
</style>
