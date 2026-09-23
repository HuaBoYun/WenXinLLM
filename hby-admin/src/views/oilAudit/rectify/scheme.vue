<template>
  <div class="system-log-container">
    <div>
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
                v-model="queryForm.code"
                clearable
                placeholder="方案编号"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.name"
                clearable
                placeholder="方案名称"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.realname"
                clearable
                placeholder="创建人"
                :style="{ width: '256px' }"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.executor.show()"
              >
                选择
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-select v-model="queryForm.runstatus" placeholder="请选择状态">
                <el-option label="未分派" value="0" />
                <el-option label="开始整改" value="1" />
                <el-option label="整改中" value="2" />
                <el-option label="方案已结束" value="3" />
                <el-option label="整改完成" value="4" />
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
          <!-- <el-button type="primary" @click="handleAdd">导出</el-button> -->
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="方案编号" prop="solutioncode">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.solutioncode }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="方案名称" prop="solutionname" />
        <el-table-column
          align="center"
          label="创建人"
          prop="createStaff.realname"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="创建日期"
          prop="createdate"
          show-overflow-tooltip
          :formatter="formatDate"
        />
        <el-table-column
          align="center"
          label="整改截止日期"
          prop="enddate"
          show-overflow-tooltip
          :formatter="formatDate"
        />
        <el-table-column
          align="center"
          label="整改联络人"
          prop="reformUser.realname"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="状态"
          prop="runstatus"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            {{
              row.runstatus == '0'
                ? '未分派'
                : row.runstatus == '1'
                ? '开始整改'
                : row.runstatus == '2'
                ? '整改中'
                : row.runstatus == '3'
                ? '方案已结束'
                : row.runstatus == '4'
                ? '方案审批中'
                : '整改完成'
            }}
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
                <el-dropdown-item @click.native="issue(row)">
                  下发
                </el-dropdown-item>
                <el-dropdown-item @click.native="start(row)">
                  启动
                </el-dropdown-item>
                <el-dropdown-item @click.native="shut(row)">
                  关闭
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleDelete(row)">
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
    </div>
    <scheme-info ref="edit" @fetch-data="fetchData" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <executor-options ref="issue" @selected="handleIssueSelected" />
    <XiafaEdit ref="xiafa" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    closeSolution,
    deleteSolution,
    getSolutionDetail,
    getZgsolutionmgmtList,
    issuePersonliable,
    startSolution,
  } from '@/oapi/audit/rectify'
  import { formatDay } from '@/utils/index'
  import ProjectDataTree from '@/views/oilAudit/prepare/components/ProjectDataTree'
  import ExecutorOptions from './components/options/executor.vue'
  import XiafaEdit from './components/options/xiafaEdit.vue'
  import SchemeInfo from './components/SchemeInfo'

  export default {
    name: 'Scheme',
    components: { SchemeInfo, ProjectDataTree, ExecutorOptions, XiafaEdit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        issueInfo: null,
        queryForm: {
          code: '',
          name: '',
          realname: '',
          createstaffid: '',
          runstatus: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async handleIssueSelected(node) {
        const { msg, code } = await issuePersonliable({
          solutionid: this.issueInfo.solutionid,
          reformuserid: node.staffid,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success')
        } else {
          this.$baseMessage(msg, 'error')
        }
        this.issueInfo = null
        await this.fetchData()
      },
      handleExecutorSelected(node) {
        this.queryForm.realname = node.realname
        this.queryForm.createstaffid = node.staffid
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
        } = await getZgsolutionmgmtList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        const data = await getSolutionDetail({ solutionid: row.solutionid })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      async handleEdit(row) {
        if (row.runstatus != 0) {
          this.$baseMessage('方案整改中,不能修改', 'error')
          return
        }
        const data = await getSolutionDetail({ solutionid: row.solutionid })

        await this.$refs['edit'].showEdit('edit', data.data)
      },
      async issue(row) {
        if (row.runstatus != 0) {
          this.$baseMessage('方案中有已启动，不能下发', 'error')
          return
        }
        this.issueInfo = row
        // this.$refs['issue'].show()
        const data = await getSolutionDetail({ solutionid: row.solutionid })

        await this.$refs['xiafa'].showEdit('edit', data.data)
      },
      start(row) {
        if (!row.reformUser) {
          this.$baseMessage('请先下发', 'error')
          return
        }
        if (row.runstatus == 0) {
          this.$baseConfirm('你确定要启动当前项吗', null, async () => {
            const { msg, code } = await startSolution({
              solutionid: row.solutionid,
            })
            if (code == 1) {
              this.$baseMessage(msg, 'success')
            } else {
              this.$baseMessage(msg, 'error')
            }
            await this.fetchData()
          })
        } else if (row.runstatus == 1 || row.runstatus == 4) {
          this.$baseMessage('方案已启动', 'error')
        } else if (row.runstatus == 2) {
          this.$baseMessage('启动失败', 'error')
        } else if (row.runstatus == 3) {
          this.$baseMessage('方案已关闭', 'error')
        }
      },
      shut(row) {
        if (row.runstatus == 3) {
          this.$baseMessage('方案已关闭', 'error')
          return
        }
        this.$baseConfirm('整改未完成，确认关闭吗？', null, async () => {
          const { msg, code } = await closeSolution({
            solutionid: row.solutionid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      handleDelete(row) {
        if (row.runstatus != 0) {
          this.$baseMessage('方案整改中', 'error')
          return
        }
        if (row.runstatus == 3) {
          this.$baseMessage('方案已关闭', 'error')
          return
        }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteSolution({
            solutionid: row.solutionid,
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
    width: 90%;
  }
</style>
