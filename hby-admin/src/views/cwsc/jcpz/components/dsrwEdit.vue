<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="700px"
    @close="close"
  >
    <el-row>
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        label-position="right"
      >
        <el-form-item label="任务名称" prop="taskName">
          <el-input
            v-model="form.taskName"
            autocomplete="off"
            :disabled="isDetail"
          ></el-input>
        </el-form-item>
        <el-form-item label="采集方案" prop="planId">
          <el-input
            v-model="form.planName"
            placeholder="请选择采集方案"
            :disabled="true"
            style="width: calc(100% - 100px)"
          ></el-input>
          <el-button
            type="primary"
            @click="openPlanDialog"
            :disabled="isDetail"
            style="margin-left: 10px"
          >
            选择方案
          </el-button>
        </el-form-item>
        <el-form-item label="cron表达式" prop="cornExpression">
          <el-input
            v-model="form.cornExpression"
            autocomplete="off"
            :disabled="isDetail"
          ></el-input>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status" :disabled="isDetail">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="任务描述" prop="descrtiption">
          <el-input
            v-model="form.descrtiption"
            type="textarea"
            :rows="3"
            autocomplete="off"
            :disabled="isDetail"
          ></el-input>
        </el-form-item>
      </el-form>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save" v-if="!isDetail">确 定</el-button>
    </div>

    <!-- 采集方案选择弹窗 -->
    <el-dialog
      title="选择采集方案"
      :visible.sync="planDialogVisible"
      append-to-body
      width="650px"
    >
      <el-form :inline="true" :model="planQueryForm" @submit.native.prevent>
        <!-- <el-form-item>
          <el-input
            v-model="planQueryForm.fname"
            placeholder="方案名称"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchPlanList">查询</el-button>
        </el-form-item> -->
      </el-form>

      <el-table
        v-loading="planListLoading"
        :data="planList"
        @row-click="handlePlanSelect"
        highlight-current-row
      >
        <el-table-column prop="fname" label="方案名称"></el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="planQueryForm.pageNumber"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="planQueryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 10px; text-align: right"
      ></el-pagination>
      <div slot="footer" class="dialog-footer">
        <el-button @click="planDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmSelectPlan">确 定</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
  import {
    saveScheduledTask,
    getScheduledTaskDetail,
    updateScheduledTask,
    getCjfaList,
  } from '@/api/cwsc'

  export default {
    name: 'DsrwEdit',
    data() {
      return {
        dialogFormVisible: false,
        title: '',
        form: {
          taskId: '',
          taskName: '',
          cornExpression: '',
          descrtiption: '',
          planId: '',
          planName: '',
          status: 0,
        },
        rules: {
          taskName: [
            { required: true, message: '请输入任务名称', trigger: 'blur' },
          ],
          planId: [
            { required: true, message: '请选择采集方案', trigger: 'change' },
          ],
        },
        isDetail: false,
        type: '',
        // 方案选择相关
        planDialogVisible: false,
        planQueryForm: {
          fname: '',
          pageNumber: 1,
          pageSize: 10,
        },
        planList: [],
        planListLoading: false,
        selectedPlan: null,
        total: 0,
      }
    },
    methods: {
      showEdit(row, type) {
        this.type = type
        this.dialogFormVisible = true
        this.isDetail = type === 'detail'

        if (type === 'add') {
          this.title = '新增定时任务'
          this.resetForm()
        } else {
          this.title = type === 'edit' ? '修改定时任务' : '查看定时任务'
          this.getDetail(row.taskId)
        }
      },
      async getDetail(taskId) {
        const { data } = await getScheduledTaskDetail({ taskId })
        if (data) {
          this.form = {
            taskId: data.taskId || '',
            taskName: data.taskName || '',
            cornExpression: data.cornExpression || '',
            descrtiption: data.descrtiption || '',
            planId: data.planId || '',
            planName: data.planName || '',
            status: data.status !== undefined ? data.status : 0,
          }
        }
      },
      resetForm() {
        this.form = {
          taskId: '',
          taskName: '',
          cornExpression: '',
          descrtiption: '',
          planId: '',
          planName: '',
          status: 0,
        }
      },
      close() {
        this.dialogFormVisible = false
        this.$refs['form'].resetFields()
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let res
            if (this.type === 'edit') {
              res = await updateScheduledTask(this.form)
            } else {
              res = await saveScheduledTask(this.form)
            }
            if (res.code === 1) {
              this.$baseMessage(
                '保存成功',
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetchData')
              this.close()
            }
          }
        })
      },
      // 打开采集方案选择弹窗
      openPlanDialog() {
        this.planDialogVisible = true
        this.fetchPlanList()
      },
      // 获取采集方案列表
      async fetchPlanList() {
        this.planListLoading = true
        try {
          const { data } = await getCjfaList(this.planQueryForm)
          if (data && data.records) {
            this.planList = data.records
            this.total = data.total || 0
          }
        } catch (error) {
          console.error('获取采集方案列表失败', error)
        }
        this.planListLoading = false
      },
      // 选择方案
      handlePlanSelect(row) {
        this.selectedPlan = row
      },
      // 确认选择方案
      confirmSelectPlan() {
        if (this.selectedPlan) {
          this.form.planId = this.selectedPlan.fid
          this.form.planName = this.selectedPlan.fname
          this.planDialogVisible = false
        } else {
          this.$message.warning('请选择一个采集方案')
        }
      },
      // 处理每页条数变化
      handleSizeChange(val) {
        this.planQueryForm.pageSize = val
        this.fetchPlanList()
      },
      // 处理页码变化
      handleCurrentChange(val) {
        this.planQueryForm.pageNumber = val
        this.fetchPlanList()
      },
    },
  }
</script>

<style lang="scss" scoped></style>
