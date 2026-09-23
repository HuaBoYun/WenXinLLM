<template>
  <el-dialog
    title="填报"
    :visible.sync="dialogVisible"
    width="80%"
    append-to-body
    :close-on-click-modal="false"
    @close="close"
  >
    <vab-query-form>
      <!-- <el-card shadow="never"> -->
      <vab-query-form-top-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.issueNumber"
              clearable
              placeholder="问题清单里面的编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.unitName"
              clearable
              placeholder="问题所属单位名称"
            />
          </el-form-item>
          <el-form-item>
            <el-select
              v-model="queryForm.dqzgzt"
              placeholder="当期整改状态"
              :style="{ width: '100%' }"
              clearable
            >
              <el-option
                v-for="(item, index) in Options"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
              <!-- <el-option label="是" :value="'1'" />
              <el-option label="否" :value="'0'" /> -->
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select
              v-model="queryForm.rectClass"
              placeholder="请选择整改分类"
              :style="{ width: '100%' }"
            >
              <el-option label="立行立改" value="立行立改"></el-option>
              <el-option label="分阶段整改" value="分阶段整改"></el-option>
              <el-option label="持续整改" value="持续整改"></el-option>
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
      <!-- </el-card> -->
    </vab-query-form>
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      :max-height="600"
    >
      <el-table-column align="center" label="序号" type="index" />
      <el-table-column
        align="center"
        label="在报告中的对应编号"
        prop="tblIssueEntity.issueNumber"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row, true)">
            {{ row.tblIssueEntity.issueNumber }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="问题所属单位名称"
        prop="tblIssueEntity.unitName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="报告中的问题定性"
        prop="tblIssueEntity.problemQualitative"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="问题金额（元）"
        prop="tblIssueEntity.money"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="定性（定性词典）"
        prop="tblIssueEntity.qualitative"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="定性法规依据"
        prop="tblIssueEntity.qualitativeRule"
        show-overflow-tooltip
      />
      <!-- <el-table-column
            align="center"
            label="处理意见或整改建议 "
            prop="tblIssueEntity.qualitativeRule2"
            v-if="item.name === '处理意见或整改建议 '"
          ></el-table-column> -->
      <el-table-column
        align="center"
        label="整改时限"
        prop="tblIssueEntity.timeLimit"
      ></el-table-column>
      <!-- <el-table-column
            align="center"
            label="整改督促牵头部门或单位"
            prop="zgqtbm"
            v-if="item.name === '整改督促牵头部门或单位'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="整改责任人"
            prop="zrr"
            v-if="item.name === '整改责任人'"
          ></el-table-column> -->
      <el-table-column align="center" label="当期整改状态" prop="dqzgzt">
        <template #default="{ row }">
          {{
            row.dqzgzt == 3
              ? '整改完毕'
              : row.dqzgzt == 2
              ? '正在整改'
              : row.dqzgzt == 1
              ? '无法整改'
              : ''
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="状态"
        prop="hxspstatus"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{
            row.hxspstatus == 1
              ? '审批中'
              : row.hxspstatus == 2
              ? '已退回'
              : row.hxspstatus == 3
              ? '已撤回'
              : row.hxspstatus == 4
              ? '已终止'
              : row.hxspstatus == 5
              ? '已跟踪'
              : row.hxspstatus == 6
              ? '已完成'
              : '未审批'
          }}
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        #default="{ row }"
        fixed="right"
        align="center"
      >
        <el-button
          type="text"
          @click="handleEdit(row)"
          :disabled="setEdit(row)"
        >
          整改
        </el-button>
        <el-dropdown style="margin-left: 10px">
          <el-button type="text">更多</el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <el-button
                type="text"
                @click="handleManage(row)"
                :disabled="!row.hxspstatus"
              >
                办理
              </el-button>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-button
                type="text"
                @click="handleApproval(row)"
                :disabled="!!row.hxspstatus || !row.dqzgzt"
              >
                提交审批
              </el-button>
            </el-dropdown-item>
            <!-- <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="!!row.hfspstatus"
                  >
                    删除
                  </el-button>
                </el-dropdown-item> -->
          </el-dropdown-menu>
        </el-dropdown>
        <!-- <el-button type="text" @click="handleEdit2(row)">审批</el-button> -->
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
    <!-- <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>  -->
    <jjzrsjjgbgView ref="table7View" />
    <wtzgEdit ref="edit" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import {
    jjzrsjjgbgList,
    jjzrsjjgbgDelete,
    reportExport,
  } from '@/oapi/audit/report'
  import jjzrsjjgbgView from './jjzrsjjgbgView.vue'
  import { fillInList } from '@/oapi/yqns_sjzg/hxzg'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import wtzgEdit from './wtzgEdit'

  export default {
    components: {
      jjzrsjjgbgView,
      wtzgEdit,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          issueNumber: '',
          unitName: '',
          dqzgzt: '',
          wtzgfl: '',
          pageNumber: 1,
          pageSize: 20,
        },
        Options: [
          {
            value: '1',
            label: '无法整改',
          },
          {
            value: '2',
            label: '正在整改',
          },
          {
            value: '3',
            label: '整改完毕',
          },
        ],
      }
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      setEdit(row) {
        // if(row.status == 6&&(row.zgzt==1||row.zgzt == 2)&&(!row.dqzgzt||row.dqzgzt == 2)){
        //   return false
        // }else if(!row.dqzgzt && row.status == 6&&(row.zgzt==1||row.zgzt == 2)){
        //   return false
        // }else
        if (row.hxspstatus == 0 || (row.hxspstatus == 6 && row.dqzgzt == 2)) {
          return false
        } else {
          return true
        }
      },
      handleApproval(row) {
        this.$refs['process'].save(198, row.wtzgid)
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.wtzgid,
          tableId: 198,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      handleDetailZ(row) {
        this.$refs['table7View'].showEdit(row, 'detail')
      },
      async showEdit(row) {
        this.projectId = row.tblIssueEntity.projectId
        this.fetchData()
        this.dialogVisible = true
      },
      resetQueryForm() {
        this.queryForm = {
          issueNumber: '',
          unitName: '',
          dqzgzt: '',
          wtzgfl: '',
          pageNumber: 1,
          pageSize: 20,
        }
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
          data: { tlist, totalRecord },
        } = await fillInList({ ...this.queryForm, projectId: this.projectId })
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      async handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      async save() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择一条数据')
          return
        }
        this.$emit('seTtable', this.multipleSelection)
        this.dialogVisible = false
      },
      close() {
        this.queryForm = {
          issueNumber: '',
          unitName: '',
          dqzgzt: '',
          wtzgfl: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
