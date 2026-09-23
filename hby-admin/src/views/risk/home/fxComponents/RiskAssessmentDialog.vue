<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <el-table
      v-loading="listLoading"
      border
      stripe
      :data="riskList"
      style="width: 100%"
    >
      <el-table-column align="center" label="风险编号" prop="risknumber" />
      <el-table-column align="center" label="风险名称" prop="riskname" />
      <el-table-column align="center" label="风险领域" prop="riskcatname" />
      <el-table-column align="center" label="风险类型" prop="riskcatidname" />
      <el-table-column
        align="center"
        label="风险描述"
        prop="riskdes"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="牵头责任部门" prop="zrbmName" />

      <el-table-column align="center" label="所属公司" prop="unitname" />
      <el-table-column align="center" label="所属部门" prop="linkDeptName" />
      <el-table-column align="center" label="风险等级" prop="level">
        <template #default="{ row }">
          <div
            :class="{
              calCount1: row.level === '很低',
              calCount2: row.level === '较低',
              calCount3: row.level === '中等',
              calCount4: row.level === '较高',
              calCount5: row.level === '很高',
            }"
          >
            {{ row.level }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="创建时间"
        prop="riskcreatedt"
        :formatter="formatDate"
      />
    </el-table>

    <!-- 分页 -->
    <div class="pagination-wrapper">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryForm.pageNo"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </div>
  </el-dialog>
</template>

<script>
  import { getTjfxpgjgList } from '@/oapi/risk/index'
  import { formatDay } from '@/utils/index'

  export default {
    name: 'RiskAssessmentDialog',
    props: {
      orgid: {
        type: [String, Number],
        default: '',
      },
    },
    data() {
      return {
        dialogVisible: false,
        dialogTitle: '风险评估结果列表',
        listLoading: false,
        riskList: [],
        total: 0,
        queryForm: {
          pageNo: 1,
          pageSize: 20,
          orgid: '',
          riskcatidname: '',
          riskname: '',
          riskcatname: '',
          linkDeptName: '',
          cxlevel: '',
          pgStatus: 'ypg',
        },
      }
    },
    methods: {
      /**
       * @description: 打开弹窗
       * @param {Object} params - 查询参数
       * @param {String} title - 弹窗标题
       */
      show(params = {}, title = '风险评估结果列表') {
        this.dialogTitle = title
        this.dialogVisible = true
        // 重置查询条件
        this.queryForm = {
          pageNo: 1,
          pageSize: 20,
          orgid: this.orgid || '',
          riskcatidname: '',
          riskname: '',
          riskcatname: '',
          linkDeptName: '',
          cxlevel: '',
          pgStatus: 'ypg',
          ...params,
        }
        this.fetchData()
      },
      handleClose() {
        this.dialogVisible = false
      },
      async fetchData() {
        this.listLoading = true
        try {
          const res = await getTjfxpgjgList(this.queryForm)
          if (res.code === 200) {
            this.riskList = res.data.data.list || []
            this.total = res.data.data.total || 0
          }
        } catch (error) {
          console.error('获取数据失败:', error)
        } finally {
          this.listLoading = false
        }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      formatDate(row, column) {
        let data = row[column.property]
        return formatDay(data)
      },
    },
  }
</script>

<style scoped lang="scss">
  .pagination-wrapper {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }

  /* 风险等级圆点样式 */
  .calCount1 {
    position: relative;
    cursor: pointer;
    padding-left: 20px;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #52ffb7;
    }
  }
  .calCount2 {
    cursor: pointer;
    position: relative;
    padding-left: 20px;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #33d73b;
    }
  }
  .calCount3 {
    cursor: pointer;
    position: relative;
    padding-left: 20px;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #ffb500;
    }
  }
  .calCount4 {
    cursor: pointer;
    position: relative;
    padding-left: 20px;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #ff7f00;
    }
  }
  .calCount5 {
    cursor: pointer;
    position: relative;
    padding-left: 20px;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #e92129;
    }
  }
</style>
