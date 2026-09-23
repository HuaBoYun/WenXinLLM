<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
    class="control-measures-dialog"
  >
    <div class="dialog-content">
      <!-- 管控措施列表 -->
      <div class="table-section">
        <el-table
          :data="tableData"
          border
          stripe
          style="width: 100%"
          max-height="400"
        >
          <el-table-column prop="controlMeasure" label="具体控制措施" align="center" />
          <el-table-column prop="expectedCompletionTime" label="预计完成时间" align="center" />
          <el-table-column prop="responsiblePerson" label="责任人" align="center" />
          <el-table-column
            prop="responsibleDepartment"
            label="有限公司责任领导"
            align="center"
          />
          <el-table-column prop="cooperatingUnit" label="配合单位或部门" align="center" />
          <el-table-column prop="isCompleted" label="是否完成" align="center" />
          <el-table-column prop="actualCompletionTime" label="措施完成时间" align="center" />
          <el-table-column
            prop="isOverdue"
            label="管控措施是否逾期"
            align="center"
          />
          <el-table-column
            prop="currentMonthMeasures"
            label="本月风险管控措施及实施情况"
            align="center"
          />
          <el-table-column prop="nextMonthMeasures" label="下月风险管控措施" align="center" />
        </el-table>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    name: 'ControlMeasuresList',
    data() {
      return {
        dialogVisible: false,
        dialogTitle: '管控措施列表',
        companyName: '',
        statusName: '',
        tableData: [],
      }
    },
    methods: {
      // 显示弹窗
      show(data, companyName, statusName) {
        this.dialogVisible = true
        this.companyName = companyName || ''
        this.statusName = statusName || ''
        // 处理数据
        this.tableData = this.processData(data || [])
      },

      // 处理数据格式
      processData(data) {
        return data.map((item) => ({
          controlMeasure: item.field1 || '', // 具体控制措施
          responsiblePerson: item.field3 || '', // 责任人
          responsibleDepartment: item.field6 || '', // 有限公司责任领导
          cooperatingUnit: item.field7 || '', // 配合单位或部门
          expectedCompletionTime: item.field2 || '', // 预计完成时间
          actualCompletionTime: item.field5 || '', // 措施完成时间
          isCompleted: item.field4 || '', // 是否完成
          isOverdue: item.field11 || '', // 管控措施是否逾期
          currentMonthMeasures: item.field10 || '', // 本月风险管控措施及实施情况
          nextMonthMeasures: item.field12 || '', // 下月风险管控措施
          status: this.getStatusFromData(item), // 根据数据判断状态
          remark: item.field10 || '', // 备注
        }))
      },

      // 根据数据判断状态
      getStatusFromData(item) {
        // 根据field4（是否完成）和field11（管控措施是否逾期）判断状态
        if (item.field4 === '是') {
          return '已完成'
        } else if (item.field4 === '逾期' || item.field11 === '是') {
          return '逾期'
        } else if (item.field4 === '否') {
          // 检查是否超过预计完成时间
          if (item.field2) {
            const now = new Date()
            const expectedDate = new Date(item.field2)
            if (expectedDate < now) {
              return '逾期'
            }
          }
          return '进行中'
        } else {
          return '未开始'
        }
      },

      // 获取状态类型
      getStatusType(status) {
        const statusMap = {
          已完成: 'success',
          进行中: 'warning',
          未开始: 'info',
          逾期: 'danger',
        }
        return statusMap[status] || 'info'
      },

      // 获取状态文本
      getStatusText(status) {
        return status || '未知'
      },

      // 关闭弹窗
      handleClose() {
        this.dialogVisible = false
        this.companyName = ''
        this.statusName = ''
        this.tableData = []
      },
    },
  }
</script>

<style scoped>
  .control-measures-dialog {
    .dialog-content {
      padding: 0;
    }

    .info-section {
      background-color: #f5f7fa;
      padding: 16px;
      margin-bottom: 16px;
      border-radius: 4px;

      .info-item {
        display: flex;
        align-items: center;
        margin-bottom: 8px;

        .label {
          font-weight: 500;
          color: #606266;
          margin-right: 8px;
          min-width: 80px;
        }

        .value {
          color: #303133;
          font-weight: 600;
        }
      }
    }

    .table-section {
      .el-table {
        font-size: 14px;
      }
    }

    .dialog-footer {
      text-align: right;
    }
  }

  /* 深度选择器修改表格样式 */
  ::v-deep .el-table {
    .el-table__header {
      background-color: #fafafa;

      th {
        background-color: #fafafa !important;
        color: #606266;
        font-weight: 600;
      }
    }

    .el-table__row {
      &:hover {
        background-color: #f5f7fa;
      }
    }
  }
</style>
