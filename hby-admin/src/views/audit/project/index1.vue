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
              v-model="queryForm.prjoectName"
              clearable
              placeholder="项目名称"
            />
          </el-form-item>
          <!-- <el-form-item>
            <el-input
              v-model="queryForm.pmId"
              clearable
              placeholder="项目经理"
            />
          </el-form-item> -->
          <el-form-item>
            <el-input
              v-model="queryForm.pmId"
              clearable
              placeholder="项目负责人"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="showGroupLeader"
            >
              选择
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.projectSource"
              clearable
              placeholder="项目来源"
            />
          </el-form-item>
          <el-form-item>
            <el-select
              v-model="queryForm.status"
              clearable
              filterable
              placeholder="项目目前状态"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in statusArr"
                :key="item.key"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              clearable
              v-model="queryForm.Date"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
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
        <el-button type="success" @click="handleEdit(false, false)">
          新建
        </el-button>
        <!-- <el-button type="primary">导出</el-button> -->
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="项目编号"
        prop="projectCode"
        width="170"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleEdit(row, true)"
            :style="`color:${row.projectId == currProjectId ? '#10d06d' : ''}`"
          >
            {{ row.projectCode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="项目名称" prop="prjoectName" />
      <!-- <el-table-column
        align="center"
        label="项目来源"
        prop="projectSource"
        show-overflow-tooltip
      /> -->
      <el-table-column
        align="center"
        label="项目负责人"
        prop="pmStaff.realname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="项目目前状态"
        prop="status"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{
            row.status == '0'
              ? '未启动'
              : row.status == '1'
              ? '启动'
              : row.status == '2'
              ? '实施 '
              : row.status == '3'
              ? '完成'
              : '归档'
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="计划开始时间"
        prop="startDate"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="计划结束时间"
        prop="endDate"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="项目实施期间(天)"
        prop="days"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="批复总投资(经费:万元)"
        prop="costs"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审批状态"
        prop="examineType"
        show-overflow-tooltip
      >
        <!-- <template #default="{ row }">
          {{
            row.examineType == 1 || !row.examineType
              ? '未审批'
              : row.examineType == 2
              ? '审批中'
              : row.examineType == 3
              ? '已终止'
              : row.examineType == 4
              ? '已完成'
              : row.examineType == 5
              ? '已退回'
              : '已中断'
          }}
        </template> -->
        <template #default="{ row }">
          {{
            row.examineType == 1
              ? '审批中'
              : row.examineType == 2
              ? '已退回'
              : row.examineType == 3
              ? '已撤回'
              : row.examineType == 4
              ? '已终止'
              : row.examineType == 5
              ? '已跟踪'
              : row.examineType == 6
              ? '已完成'
              : '未审批'
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            @click="handleEdit(scope.row, false)"
            v-if="
              /**
               * @description 删除按钮触发，删除当前行数据
               * @param {*}
               * @return {*}
               */ scope.row.examineType === 1 ||
              scope.row.examineType == 5 ||
              !scope.row.examineType
            "
          >
            修改
          </el-button>
          <el-dropdown style="margin-left: 10px">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                :disabled="scope.row.projectId == currProjectId"
                @click.native="implementation(scope.row)"
              >
                实施
              </el-dropdown-item>
              <el-dropdown-item
                :disabled="scope.row.examineType > 0"
                @click.native="submitApproval(scope.row)"
              >
                提交审批
              </el-dropdown-item>
              <el-dropdown-item
                :disabled="scope.row.examineType > 0"
                @click.native="handleDelete(scope.row)"
              >
                删除
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
    <IndexEdit ref="edit" @fetch-data="fetchData" />
    <!-- 选择组长组员子组件 -->
    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>
    <ProcessList ref="process" />
  </div>
</template>

<script>
  import {
    projectDel,
    submitProjectApproval,
    xmProjectPlan,
  } from '@/api/audit/project'
  import { projectList } from '@/api/audit/rectify'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'

  export default {
    name: 'Download',
    components: {
      ProcessList,
      IndexEdit: () => import('./components/IndexEdit'),
      selectTeam: () => import('./components/formComponents/selectTeam.vue'),
    },
    data() {
      return {
        list: [],
        statusArr: [
          { key: '1', label: '启动', value: 1 },
          { key: '0', label: '未启动', value: 0 },
        ],
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          Date: [],
          pmId: '',
        },
        currProjectId: '',
      }
    },
    created() {
      this.fetchData()
    },
    /**
     * @description 流程提交后，回调，刷新列表
     * @param {*}
     * @return {*}
     */
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      /**
       * @description 选择组员回调，处理数据，数组转化字符串，把数据保存tableData
       * @param {*}
       * @return {*}
       */
      selectTeamList(val, flagTitle) {
        if (flagTitle) {
          this.leaderId = val[0].staffid
          this.queryForm.pmId = val[0].realname
        } else {
          let arrStr = ''
          let arr = []
          val.forEach((item) => {
            arr.push(item.realname)
          })
          arrStr = arr.join(',')
          this.tableData[this.sIndex].zyNames = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          val.forEach((item) => {
            arrZy.push(item.staffid)
          })
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
        }
      },
      /**
       * @description 唤起选择组长组件
       * @param {*}
       * @return {*}
       */
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      /**
       * @description 重置筛选，把筛选条件清空
       * @param {*}
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      /**
       * @description 重置按钮，点击触发的函数
       * @param {*}
       * @return {*}
       */
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description 分页，选择每页几条数据，查询每页多少条数据
       * @param {*}
       * @return {*}
       */
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
       * @description 分页，选择页码，查询第几页的数据
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description 查询按钮，回到第一页，查询数据
       * @param {*}
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        let { Date, pmId, ...other } = this.queryForm
        //
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        if (pmId) {
          pmId = this.leaderId
        } else {
          pmId = ''
        }

        const {
          data: {
            pageInfo: { tlist, totalRecord },
            currProjectId,
          },
        } = await projectList({
          ...other,
          pmId,
          startDate,
          endDate,
        })
        tlist.forEach((item) => {
          item.endDate = item.endDate ? item.endDate.split('T')[0] : ''
          item.startDate = item.startDate ? item.startDate.split('T')[0] : ''
          item.days = ''
          item.days = this.days(item.startDate, item.endDate)
        })
        this.currProjectId = currProjectId

        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        this.planNum = tlist[0].projectCode
      },
      /**
       * @description 计算时间差（天）
       * @param {*}
       * @return {*}
       */
      days(start, end) {
        let s = new Date(start)
        let e = new Date(end)
        let hours = (e - s) / (1000 * 60 * 60 * 24)
        return hours + '天'
      },
      /**
       * @description 切换实施项目
       * @param {*}
       * @return {*}
       */
      async implementation(row) {
        let res = await xmProjectPlan({
          projectid: row.projectId,
        })
        if (res.code == 1) {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      /**
       * @description 流程提交接口
       * @param {*}
       * @return {*}
       */
      async submitApproval(row) {
        const tableId = 8
        const fromId = row.projectId
        this.$refs['process'].save(tableId, fromId)
        // let res = await submitProjectApproval({
        //   projectId: row.projectId,
        // })
        // this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
        // if (res.code === 1) {
        //   this.fetchData()
        // }
      },
      /**
       * @description 编辑按钮，唤起编辑页面
       * @param {*}
       * @return {*}
       */
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await projectDel({ projectid: row.projectId })
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      /**
       * @description 无意义
       * @param {*}
       * @return {*}
       */
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      /**
       * @description 无意义
       * @param {*}
       * @return {*}
       */
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>
