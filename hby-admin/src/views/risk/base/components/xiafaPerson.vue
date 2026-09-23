<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal="modal"
    width="500px"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-form
          ref="form"
          :model="queryForm"
          :inline="true"
          @keyup.enter.native="queryData"
          style="width: 100%;"
        >
          <el-form-item>
            <el-input
              v-model="queryForm.realname"
              placeholder="请输入用户名"
              clearable
              @clear="queryData"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              type="primary"
              @click="queryData"
            >
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table
      :data="tableData"
      style="width: 100%"
      @select="handleSelection"
      @select-all="handleSelectAll"
      ref="multipleTable"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="realname" label="用户姓名" />
      <!-- <el-table-column prop="orgname" label="所属部门" /> -->
    </el-table>

    <div style="padding-top: 20px; text-align: right;">
      <el-button @click="dialogVisible = false" size="mini">取 消</el-button>
      <el-button type="primary" @click="save" size="mini">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
  import {
    querySendRiskModelList,
  } from '@/api/risk'
  import { cancelSendRiskModel } from '@/api/risk'
  export default {
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        select: [],
        stepId: '',
        queryForm: {
          realname: '',
        },
      }
    },
    methods: {
      showEdit(row, stepId) {
        this.dialogVisible = true
        this.tableData = row
        this.stepId = stepId
        this.fetchData()
      },
      queryData() {
        this.fetchData()
      },
      resetSearch() {
        this.queryForm = this.$options.data().queryForm
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { data },
        } = await querySendRiskModelList({...this.queryForm, stepId: this.stepId})
        this.tableData = data
        this.listLoading = false
        // this.handleSelectAll()
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.staffid == row.staffid)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        console.log(val)
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.staffid == row.staffid)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.staffid == row.staffid)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },
      save() {
        if (!this.select.length) {
          this.$baseMessage(
            '请选择下发人员！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        const ids = this.select.map((item) => item.staffid).join(',')
        cancelSendRiskModel({ staffids: ids, stepId: this.stepId }).then(
          (res) => {
            this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
            this.$emit('fetchData')
          }
        )
        this.dialogVisible = false
      },
    },
  }
</script>
