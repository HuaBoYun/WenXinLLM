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
              v-model="queryForm.projectname"
              clearable
              placeholder="项目名称"
            />
          </el-form-item>
          <el-form-item prop="fhstaffname">
            <el-input
              clearable
              placeholder="请选择人员"
              v-model="queryForm.staffid"
              disabled
              v-if="false"
            />
            <el-input
              clearable
              placeholder="请选择人员"
              v-model="queryForm.realname"
              disabled
            />
          </el-form-item>
          <el-form-item>
            <el-button
              @click="openPersonModal"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
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
        <el-button type="success" @click="handleEdit('新增', null)">
          新建
        </el-button>
        <!-- <el-button type="primary">导出</el-button> -->
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="审计人员"
        width="100"
        prop="auditor.realname"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail('查看', row)">
            {{ row.auditor.realname }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="参与审计项目名称"
        prop="auditProjectName"
      />

      <el-table-column
        align="center"
        label="总分"
        prop="totalScore"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        align="center"
        label="考核结果"
        prop="totalScore"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <div v-if="scope.row.totalScore > 90">{{ '优秀' }}</div>
          <div v-if="scope.row.totalScore < 90 && scope.row.totalScore > 80">
            {{ '良好' }}
          </div>
          <div v-if="scope.row.totalScore < 80 && scope.row.totalScore > 60">
            {{ '合格' }}
          </div>
          <div v-if="scope.row.totalScore < 60">{{ '不合格' }}</div>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="状态"
        prop="status"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{ statusName[row.status] || '未审核' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="180"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            :disabled="row.status != 0"
            @click="handleEdit('编辑', row)"
          >
            修改
          </el-button>
          <el-button
            type="text"
            @click="handleShenPi(row)"
            :disabled="row.status"
          >
            审批
          </el-button>
          <el-button
            type="text"
            :disabled="row.status"
            @click="deleteData(row)"
          >
            删除
          </el-button>
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
    <personsModal ref="person" @handlePersonInfo="handlePersonInfo" />
    <evaluationEdit ref="edit" @handleReload="handleReload" />
    <ProcessList ref="process" />
  </div>
</template>
<script>
  import personsModal from './components/components/selectPersonModal.vue'
  import evaluationEdit from './components/evaluationEdit.vue'

  import {
    deletePersonInfo,
    evaluationShenPi,
    loadEvaluationData,
  } from '@/oapi/audit/structure'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    components: { evaluationEdit, personsModal, ProcessList },
    name: 'xxx',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        listLoading: false,
        list: [],
        statusName: [
          '未审批',
          '审批中',
          '已退回',
          '已撤回',
          '已终止',
          '',
          '已完成',
        ],
      }
    },
    created() {
      this.fetchData()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      async fetchData() {
        this.listLoading = true
        const { ...other } = this.queryForm
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await loadEvaluationData({ ...other })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleDetail(a, b) {
        await this.$refs['edit'].showEdit(a, b)
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleEdit(a, b) {
        this.$refs['edit'].showEdit(a, b)
      },
      openPersonModal() {
        this.$refs['person'].showEdit()
      },
      handlePersonInfo(v) {
        this.$set(this.queryForm, `staffid`, v[0].staffid)
        this.$set(this.queryForm, `realname`, v[0].realname)
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      deleteData(row) {
        deletePersonInfo({
          staffScoreid: row.staffScoreid,
        }).then((res) => {
          if (res.code == 1) {
            this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
      },
      handleReload() {
        this.fetchData()
      },
      handleShenPi(row) {
        const tableId = 15
        const fromId = row.staffScoreid
        this.$refs['process'].save(tableId, fromId)
        // if (this.statusName[row.status] != '未审批' && row.aprStatus) {
        //   this.$baseMessage('流程进行中', 'error')
        // } else {
        //   this.$refs['process'].show(row, 15)
        // }
        // evaluationShenPi({
        //   staffScoreid: row.staffScoreid,
        // }).then((res) => {
        //   if (res.codes === '1') {
        //     this.$baseMessage('操作成功', 'success')
        //     this.fetchData()
        //   }
        // })
      },
    },
  }
</script>
