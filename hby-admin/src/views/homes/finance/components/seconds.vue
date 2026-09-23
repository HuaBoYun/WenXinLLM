<template>
  <el-card>
    <div class="title">流程中心</div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="支付类" name="first">
        <el-table :data="tableData1" style="width: 100%">
          <el-table-column
            prop="date"
            label="流程名称"
            width="680"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="发起人"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="address"
            label="发起时间"
            show-overflow-tooltip
          ></el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="报销类" name="second">
        <el-table :data="tableData2" style="width: 100%">
          <el-table-column
            prop="date"
            label="流程名称"
            width="680"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="发起人"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="address"
            label="发起时间"
            show-overflow-tooltip
          ></el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="预算类" name="third">
        <el-table :data="tableData3" style="width: 100%">
          <el-table-column
            prop="date"
            label="流程名称"
            width="680"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="发起人"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="address"
            label="发起时间"
            show-overflow-tooltip
          ></el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
  export default {
    name: 'ThirdLeft',
    data() {
      return {
        activeName: 'first',
        tableData1: [
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
        ],
        tableData2: [
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
        ],
        tableData3: [
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
          {
            date: '合同付款申请流程-杨文元-2019-12-10（未付款金额(转台账):4999778, 已付款金额(转台账):222.00）',
            name: '王小虎',
            address: '2022-12-10',
          },
        ],
      }
    },
  }
</script>

<style scoped>
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #333;
    padding: 10px 0;
  }
</style>
