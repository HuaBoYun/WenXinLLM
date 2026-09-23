
<template>
  <el-card class="table-responsive">
    <el-tabs v-model="activeName">
      <el-tab-pane label="风险登记" name="first">
        <el-input
          class="search"
          placeholder="风险名称"
          prefix-icon="el-icon-search"
        ></el-input>
        <el-table
          :data="tableData"
          style="width: 100%"
          v-if="activeName == 'first'"
        >
          <el-table-column label="风险名称" prop="name" align="center" />
          <el-table-column label="风险编号" prop="code" align="center" />
          <el-table-column label="风险等级" prop="level" align="center" />
          <el-table-column label="风险来源" prop="soure" align="center" />
          <el-table-column label="承办人" prop="person" align="center" />
          <el-table-column label="登记时间" prop="time" align="center" />
          <el-table-column label="当前状态" prop="status" align="center" />
          <el-table-column label="当前处理人" prop="detal" align="center" />
        </el-table>
        <el-pagination background layout="prev, pager, next" :total="1000" />
      </el-tab-pane>
      <el-tab-pane label="纠纷登记" name="second">
        <el-input
          class="search"
          placeholder="风险名称"
          prefix-icon="el-icon-search"
        ></el-input>
        <el-table
          :data="tableData"
          style="width: 100%"
          v-if="activeName == 'second'"
        >
          <el-table-column label="风险名称" prop="name" align="center" />
          <el-table-column label="风险编号" prop="code" align="center" />
          <el-table-column label="风险等级" prop="level" align="center" />
          <el-table-column label="风险来源" prop="soure" align="center" />
          <el-table-column label="承办人" prop="person" align="center" />
          <el-table-column label="登记时间" prop="time" align="center" />
          <el-table-column label="当前状态" prop="status" align="center" />
          <el-table-column label="当前处理人" prop="detal" align="center" />
        </el-table>
        <el-pagination background layout="prev, pager, next" :total="1000" />
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
  export default {
    data() {
      return {
        activeName: 'first',
        tableData: [
          {
            name: 'IT服务合同',
            code: 'JKJLJ',
            level: '一级',
            soure: '未知',
            person: '张三',
            time: '2022-01-19',
            status: '处理中',
            detal: '李四',
          },
          {
            name: 'IT服务合同',
            code: 'JKJLJ',
            level: '一级',
            soure: '未知',
            person: '张三',
            time: '2022-01-19',
            status: '处理中',
            detal: '李四',
          },
          {
            name: 'IT服务合同',
            code: 'JKJLJ',
            level: '一级',
            soure: '未知',
            person: '张三',
            time: '2022-01-19',
            status: '处理中',
            detal: '李四',
          },
          {
            name: 'IT服务合同',
            code: 'JKJLJ',
            level: '一级',
            soure: '未知',
            person: '张三',
            time: '2022-01-19',
            status: '处理中',
            detal: '李四',
          },
          {
            name: 'IT服务合同',
            code: 'JKJLJ',
            level: '一级',
            soure: '未知',
            person: '张三',
            time: '2022-01-19',
            status: '处理中',
            detal: '李四',
          },
        ],
      }
    },
  }
</script>

<style scoped>
  .table-responsive {
    border: 1px solid #d2d2d2;
  }
  .title {
    margin-left: 10px;
  }
  .search {
    width: 15%;
    margin-bottom: 20px;
    margin-left: 20px;
  }
</style>