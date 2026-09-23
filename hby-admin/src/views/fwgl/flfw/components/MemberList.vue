<template>
  <el-card shadow="never" body-style="padding:0">
    <div slot="header" class="clearfix">
      <span>法律人员信息</span>
      <el-button style="float: right; padding: 3px 0" type="text">
        添加
      </el-button>
    </div>
    <el-table :data="tableData">
      <el-table-column align="center" label="姓名">
        <template #default="{ row }">
          <el-input v-model.trim="row.name" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="性别" prop="name">
        <template #default="{ row }">
          <el-input v-model.trim="row.name" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="岗位" prop="name">
        <template #default="{ row }">
          <el-input v-model.trim="row.name" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="最高学历" prop="name">
        <template #default="{ row }">
          <el-input v-model.trim="row.name" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="最高学历所学专业" prop="name">
        <template #default="{ row }">
          <el-input v-model.trim="row.name" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否在职/在岗" prop="name">
        <template #default="{ row }">
          <el-input v-model.trim="row.name" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="资格证书" prop="name">
        <template #default="{ row }">
          <el-input v-model.trim="row.name" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template>
          <el-button type="text">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script>
  export default {
    name: 'MemberList',
    data() {
      return {
        tableData: [{ name: 'XXXXX' }, { name: 'XXXXX' }, { name: 'XXXXX' }],
      }
    },
  }
</script>
