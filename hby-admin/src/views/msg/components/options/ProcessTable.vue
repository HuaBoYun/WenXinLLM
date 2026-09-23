<template>
  <el-table :data="data">
    <el-table-column align="center" label="序号" prop="workUnitName">
      <template #default="{ $index }">
        {{ $index + 1 }}
      </template>
    </el-table-column>
    <el-table-column
      align="center"
      label="审核节点"
      prop="nodeName"
      width="200"
    >
      <template #default="{ row, $index }">
        {{
          $index == 0
            ? '发起人'
            : $index == data.length - 1
            ? '审批完成'
            : row.nodeName
        }}
      </template>
    </el-table-column>
    <el-table-column
      align="center"
      label="执行人"
      prop="userName"
      width="120"
    ></el-table-column>
    <el-table-column align="center" label="状态" prop="completion">
      <template #default="{ row, $index }">
        {{
          $index != data.length - 1
            ? row.completion == 1
              ? '已办'
              : '未办理'
            : ''
        }}
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
  export default {
    props: ['data'],
    data() {
      return {
        list: [],
      }
    },
  }
</script>

<style></style>
