<template>
  <el-card>
    <div class="title">采购审批</div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="采购合同" name="first">
        <el-table :data="tableData1" style="width: 100%">
          <el-table-column
            prop="date"
            label="相关项目"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="name"
            label="供应商"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="address"
            label="采购产品"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="all"
            label="小计"
            show-overflow-tooltip
          ></el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="采购订单" name="second">
        <el-table :data="tableData2" style="width: 100%">
          <el-table-column
            prop="date"
            label="采购编号"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="name"
            label="相关项目"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="address"
            label="供应商"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="all"
            label="申请日期"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="people"
            label="申请人"
            show-overflow-tooltip
          ></el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="采购风险" name="third">
        <el-table :data="tableData3" style="width: 100%">
          <el-table-column
            prop="date"
            label="合同名称"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="name"
            label="风险项"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="address"
            label="风险说明"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="all"
            label="风险处理状态"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="people"
            label="发生时间"
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
            date: '上海亘岩集团OA项目（演示数据）',
            name: '腾讯科技（深圳）有限公司',
            address: 'IT设备-服务器',
            all: '100,000.00',
          },
          {
            date: '上海亘岩集团OA项目（演示数据）',
            name: '腾讯科技（深圳）有限公司',
            address: 'IT设备-服务器',
            all: '100,000.00',
          },
          {
            date: '上海亘岩集团OA项目（演示数据）',
            name: '腾讯科技（深圳）有限公司',
            address: 'IT设备-服务器',
            all: '100,000.00',
          },
          {
            date: '上海亘岩集团OA项目（演示数据）',
            name: '腾讯科技（深圳）有限公司',
            address: 'IT设备-服务器',
            all: '100,000.00',
          },
          {
            date: '上海亘岩集团OA项目（演示数据）',
            name: '腾讯科技（深圳）有限公司',
            address: 'IT设备-服务器',
            all: '100,000.00',
          },
          {
            date: '上海亘岩集团OA项目（演示数据）',
            name: '腾讯科技（深圳）有限公司',
            address: 'IT设备-服务器',
            all: '100,000.00',
          },
        ],
        tableData2: [
          {
            date: 'CG-2019-0001',
            name: '上海亘岩集团OA项目（演示数据）',
            address: '腾讯科技（深圳）有限公司',
            all: '2019-01-01',
            people: '张三',
          },
          {
            date: 'CG-2019-0001',
            name: '上海亘岩集团OA项目（演示数据）',
            address: '腾讯科技（深圳）有限公司',
            all: '2019-01-01',
            people: '张三',
          },
          {
            date: 'CG-2019-0001',
            name: '上海亘岩集团OA项目（演示数据）',
            address: '腾讯科技（深圳）有限公司',
            all: '2019-01-01',
            people: '张三',
          },
          {
            date: 'CG-2019-0001',
            name: '上海亘岩集团OA项目（演示数据）',
            address: '腾讯科技（深圳）有限公司',
            all: '2019-01-01',
            people: '张三',
          },
          {
            date: 'CG-2019-0001',
            name: '上海亘岩集团OA项目（演示数据）',
            address: '腾讯科技（深圳）有限公司',
            all: '2019-01-01',
            people: '张三',
          },
          {
            date: 'CG-2019-0001',
            name: '上海亘岩集团OA项目（演示数据）',
            address: '腾讯科技（深圳）有限公司',
            all: '2019-01-01',
            people: '张三',
          },
        ],
        tableData3: [
          {
            date: '维森集团服务器采购合同',
            name: '合同履约率风险',
            address: '合同履约率低于95%',
            all: '未处理',
            people: '2019-01-01',
          },
          {
            date: '维森集团服务器采购合同',
            name: '合同履约率风险',
            address: '合同履约率低于95%',
            all: '未处理',
            people: '2019-01-01',
          },
          {
            date: '维森集团服务器采购合同',
            name: '合同履约率风险',
            address: '合同履约率低于95%',
            all: '未处理',
            people: '2019-01-01',
          },
          {
            date: '维森集团服务器采购合同',
            name: '合同履约率风险',
            address: '合同履约率低于95%',
            all: '未处理',
            people: '2019-01-01',
          },
          {
            date: '维森集团服务器采购合同',
            name: '合同履约率风险',
            address: '合同履约率低于95%',
            all: '未处理',
            people: '2019-01-01',
          },
          {
            date: '维森集团服务器采购合同',
            name: '合同履约率风险',
            address: '合同履约率低于95%',
            all: '未处理',
            people: '2019-01-01',
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
