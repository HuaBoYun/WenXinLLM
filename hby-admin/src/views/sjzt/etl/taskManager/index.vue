<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
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
                v-model="queryForm.trademarkName"
                clearable
                placeholder="转换名称"
                style="width: 140px; margin-right: 20px"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="getTrans"
              >
                搜索
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel :span="24">
        <el-button
          type="success"
          @click="handleAdd"
          style="margin-bottom: 10px; margin-right: 10px"
          v-if="hasAuth('ETLZHadd')"
        >
          新增转换
        </el-button>
        <!-- <el-button
          type="primary"
          @click="handleAdd"
          style="margin-bottom: 10px; margin-right: 10px"
        >
          执行一次
        </el-button>
        <el-button
          type="primary"
          @click="handleAdd"
          style="margin-bottom: 10px; margin-right: 10px"
        >
          全部启动
        </el-button>
        <el-button
          type="danger"
          @click="handleAdd"
          style="margin-bottom: 10px; margin-right: 10px"
        >
          全部停止
        </el-button> -->
      </vab-query-form-right-panel>
      <!-- @selection-change="handleSelectionChange" -->
      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column type="selection" width="55" /> -->
        <el-table-column align="center" label="转换名称" prop="transName" />
        <el-table-column
          align="center"
          label="转换描述"
          prop="transDescription"
        />
        <el-table-column align="center" label="执行方式">
          <template #default="{ row }">
            {{ row.transType == 'File' ? '文件' : '数据库' }}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="所属资源库"
          prop="transRepository"
        />
        <!-- <el-table-column align="center" label="转换路径" prop="transPath" /> -->
        <el-table-column align="center" label="cron" prop="cron" />
        <el-table-column align="center" label="cron状态" prop="cronStatus" />
        <el-table-column align="center" label="日志级别">
          <template #default="{ row }">
            {{
              row.transLogLevel == 'basic'
                ? '基本日志'
                : row.transLogLevel == 'error'
                ? '错误日志'
                : row.transLogLevel == 'minimal'
                ? '最小日志'
                : row.transLogLevel == 'detail'
                ? '详细日志'
                : row.transLogLevel == 'debug'
                ? '调试日志'
                : row.transLogLevel == 'rowlevel'
                ? '行级日志'
                : 'nothing'
            }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="转换状态" prop="transStatus" />
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">操作</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :disabled="row.cronStatus == '运行中'"
                  @click.native="runStop(row)"
                  v-if="hasAuth('ETLZHstart')"
                >
                  启动
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="row.cronStatus == '未运行'"
                  @click.native="runStop(row)"
                  v-if="hasAuth('ETLZHstop')"
                >
                  停止
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleEdit2(row)"
                  v-if="hasAuth('ETLZHlog')"
                >
                  查看日志
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="row.cronStatus == '运行中'"
                  @click.native="handleEdit(row)"
                  v-if="hasAuth('ETLZHedit')"
                >
                  编辑
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="row.cronStatus == '运行中'"
                  @click.native="gotoKettle(row)"
                  v-if="hasAuth('ETLZHjump')"
                >
                  跳转到kettle
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="row.cronStatus == '运行中'"
                  @click.native="runOnce(row)"
                  v-if="hasAuth('ETLZHonce')"
                >
                  执行一次
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="row.cronStatus == '运行中'"
                  @click.native="handleDelete(row)"
                  v-if="hasAuth('ETLZHdelete')"
                >
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <newTransformation ref="edit" @fetch-data="fetchData" />
    <transLog ref="log" />
  </div>
</template>

<script>
  import { getAccountCate } from '@/api/workbench/accountData/accountData'
  import {
    getTransList,
    deleteTrans,
    getTransByName,
    transRunOnce,
    transRunStop,
  } from '@/api/sjzt/etl/etl'
  import newTransformation from './components/newTransformation.vue'
  import transLog from './components/transLog.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'taskManager',
    components: { newTransformation, transLog },
    data() {
      return {
        multipleSelection: [],
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      resetSearch() {
        this.queryForm.trademarkName = ''
        this.fetchData()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      goBack() {
        this.$router.back(-1)
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

      runStop(row) {
        transRunStop(row).then((res) => {
          if (res.code == '200') {
            this.fetchData()
          }

          //..todo
        })
      },
      runOnce(row) {
        transRunOnce(row).then((res) => {
          //..todo
        })
      },
      gotoKettle(row) {
        const res = gotoKettle()

        this.$baseConfirm(
          `请保存到${row.baseDir}资源库，文件名为${row.jobName}。否则无法执行！`,
          null,
          async () => {
            const { data } = await gotoKettle()
            window.open('https://www.baidu.com')
            this.$baseMessage(data, 'success', 'vab-hey-message-success')
          }
        )
      },
      async getTrans() {
        this.listLoading = true
        const { data } = await getTransByName(this.queryForm.trademarkName)
        var listOne = [data]
        this.list = listOne

        this.total = 1
        this.listLoading = false
      },
      async fetchData() {
        this.listLoading = true
        const { rows, total } = await getTransList(this.queryForm)
        this.list = rows
        this.total = total
        // const list = [{ data: 'aaa' }]
        // this.list = list
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit('edit', row)
      },
      handleEdit2(row) {
        this.$refs['log'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteTrans({ ids: row.id })
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
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
