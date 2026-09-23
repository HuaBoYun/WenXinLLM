<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    destroy-on-close
  >
    <el-table
      :data="tableData"
      :inline="true"
      label-width="0"
      style="width: 100%"
    >
      <el-table-column
        align="center"
        prop="realname"
        label="执行人"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="savetime"
        label="执行时间"
      ></el-table-column>
      <el-table-column align="center" prop="address" label="详细结果">
        <template #default="{ row }">
          <el-button type="text" @click="handleCheck(row)">结果</el-button>
        </template>
      </el-table-column>
    </el-table>

    <RuleCheck ref="check"></RuleCheck>
  </el-dialog>
</template>

<script>
  import {
    Resultmgmtlist,
    Resultmgmt,
    resultMgmtyj,
    SjmxZxReslut,
    SjmxGetDatelist,
  } from '@/api/monitor/rule/index'
  import RuleCheck from './RuleCheck.vue'
  export default {
    name: 'Resultmgmtlist',
    components: { RuleCheck },
    data() {
      return {
        dialogFormVisible: false,
        tableData: [],
        title: '结果',
        ruleid: '',
      }
    },
    methods: {
      showEdit(row, e) {
        this.dialogFormVisible = true

        if (e == '规则预警') {
          const params = {
            // fhtype: '',
            // modelId: '',
            orgId: row.orgid,
            // solutionResultId: '',
            soultionId: row.solutionid,
            source: '',
            pageNumber: 1,
            pageSize: 20,
          }
          resultMgmtyj(params).then((res) => {
            this.tableData = res.data.pageBean.records
          })
        } else if (e == '规则管理') {
          const params = {
            // orgId: row.orgid,
            // pageNumber: 1,
            // pageSize: 20,
            ruleid: row.ruleid,
          }
          this.ruleid = row.ruleid
          SjmxZxReslut(params).then((res) => {
            this.tableData = res.data.data
          })
        } else if (e == '指标预警') {
        }
      },
      close() {
        this.$emit('close')
      },
      handleCheck(row) {
        const params = {
          resultid: row.resultid,
          // resultid: 829643,
          pageNumber: 1,
          pageSize: 20,
          ruleid: this.ruleid,
        }
        SjmxGetDatelist(params).then((res) => {
          this.$refs['check'].show(res.data.pageInfo)
        })
      }
    },
  }
</script>

<style></style>
