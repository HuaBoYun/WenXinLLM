<template>
  <el-dialog
    title="被审计单位"
    :visible.sync="dialogVisible"
    width="1400px"
    :append-to-body="true"
    :close-on-click-modal="false"
    v-if="dialogVisible"
  >
    <div class="system-log-container">
      <div class="">
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          v-loading="listLoading"
          ref="multipleTable"
          :data="list"
          tooltip-effect="dark"
          @select="handleSelection"
          style="width: 100%"
        >
          <!-- <div v-for="(item, index) in filedNow" :key="index"> -->
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column
            align="center"
            label="被审计单位名称"
            prop="auditeeName"
          >
            <!-- <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.auditeeName }}
              </el-button>
            </template> -->
          </el-table-column>

          <el-table-column
            align="center"
            label="实施审计时间"
            prop="implementationTime"
          />
          <el-table-column
            align="center"
            label="审计项目名称"
            prop="projectName"
          ></el-table-column>
          <el-table-column
            align="center"
            label="分工负责审计内容"
            prop="responsibleContent"
          />
          <el-table-column
            align="center"
            label="审计内容和目标"
            prop="contentObjectives"
          />
          <el-table-column
            align="center"
            label="执行的审计程序和工作过程"
            prop="executedProceduresProcesses"
          />
          <el-table-column
            align="center"
            label="发现的疑点、线索及查证情况"
            prop="verificationSituation"
          />
          <el-table-column
            align="center"
            label="审计线索及数据来源"
            prop="cluesSources"
          />
          <!-- </div> -->
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
  </el-dialog>
</template>
<script>
  import { workRecordsList } from '@/oapi/audit/implement'
  export default {
    name: 'WorkRecord',
    data() {
      return {
        listLoading: false,
        selectTree: {},
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',

        dialogVisible: false,
        list: [],
        dataTree: [],
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
          isLeaf: 'isParent',
        },
        queryForm: {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        currentTree: undefined,
        filedNow: [],
      }
    },
    methods: {
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
      },
      showEdit() {
        this.dialogVisible = true
        this.initTable() //初始化表格
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
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await workRecordsList(this.queryForm)
        this.list = list || []
        this.total = total || 0
        this.listLoading = false
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      save() {
        if (!this.current) {
          this.$baseMessage(
            '请选择关联工作记录！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('submit', this.multipleSelection, 'right')
        this.dialogVisible = false
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
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 70px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
