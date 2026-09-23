<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <project-data-tree @getChildParam="setTree" />
      </div>
      <div class="right">
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
                  v-model="queryForm.risknumber"
                  clearable
                  placeholder="风险编号"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryForm.riskname"
                  clearable
                  placeholder="风险名称"
                />
              </el-form-item>
              <el-form-item>
                <el-date-picker
                  v-model="queryForm.Date"
                  clearable
                  end-placeholder="发现结束日期"
                  format="yyyy-MM-dd"
                  range-separator="-"
                  start-placeholder="发现开始日期"
                  :style="{ width: '100%' }"
                  type="daterange"
                  value-format="yyyy-MM-dd"
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
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch"
                >
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
          <el-table-column
            align="center"
            label="风险编号"
            prop="risknumber"
            width="170"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.risknumber }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="风险名称" prop="riskname" />
          <el-table-column
            align="center"
            label="发现时间"
            prop="discovereddate"
          />
          <el-table-column
            align="center"
            label="责任部门"
            prop="tblOrganiDem.orgname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="发现人"
            prop="tblStaff.realname"
            show-overflow-tooltip
            width="120"
          />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit(row)">修改</el-button>
              <el-button type="text" @click="handleDelete(row)">删除</el-button>
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
      </div>
    </div>
    <risk-info ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { riskDel, riskDetail, riskList } from '@/api/audit/question'
  import { parseTime } from '@/utils/index'
  import ProjectDataTree from './components/ProjectDataTree'
  import RiskInfo from './components/RiskInfo'
  export default {
    name: 'Download',
    components: { RiskInfo, ProjectDataTree },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          risknumber: '',
          riskname: '',
          orgid: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      setTree(node) {
        let id = node.pId === 1 ? undefined : node.id
        this.queryForm.orgid = id
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
        } = await riskList({ ...other, startDate, endDate })
        this.list = list
        // this.list.forEach((item) => {
        //   item.occureddate = parseTime(item.occureddate, '{y}-{m}-{d}')
        //   item.discovereddate = parseTime(item.discovereddate, '{y}-{m}-{d}')
        // })
        this.total = total
        this.listLoading = false
      },
      handleExport(row) {},
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const data = await riskDetail({ riskid: row.riskid })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        const data = await riskDetail({ riskid: row.riskid })
        data.data.risk.occureddate = parseTime(
          data.data.risk.occureddate,
          '{y}-{m}-{d}'
        )
        data.data.risk.discovereddate = parseTime(
          data.data.risk.discovereddate,
          '{y}-{m}-{d}'
        )
        console.dir(data)
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await riskDel({
            riskid: row.riskid,
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
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    /* width: 200px; */
    width: 15%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 85%;
  }
</style>
