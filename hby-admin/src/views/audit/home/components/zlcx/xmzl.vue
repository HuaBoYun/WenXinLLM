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
                {{ row.projectDatapreId }}
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
  import { whetherLeader } from '@/api/audit/implement'
  import {
    dataProjectDel,
    findOrganizationByTreeAllss,
    getDataprojectList,
  } from '@/api/audit/preparation'
  import { UTCformat } from '@/utils'

  import ProjectDataInfo from '@/views/audit/prepare/components/ProjectDataInfo'

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
        this.listLoading = true // 开始加载
        try {
          // 获取数据项目列表
          const {
            data: {
              pageInfo: { tlist, totalRecord },
            },
          } = await getDataprojectList(this.queryForm)

          this.list = tlist || [] // 如果 tlist 为 null 或 undefined，设置为空数组
          this.list.forEach((item) => {
            if (item && item.dataDate) {
              item.dataDate = UTCformat(item.dataDate) // 格式化时间
            }
          })
          this.total = totalRecord || 0 // 如果 totalRecord 为 null 或 undefined，设置为 0

          // 获取组织树数据
          const res = await findOrganizationByTreeAllss(this.queryForm)
          this.dataTree = res || [] // 如果 res 为 null 或 undefined，设置为空数组
        } catch (error) {
          console.error('Error fetching data:', error) // 捕获并打印错误
          // 你可以在这里处理错误，比如显示错误提示
        } finally {
          this.listLoading = false // 无论成功或失败，都关闭加载状态
        }
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
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
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
