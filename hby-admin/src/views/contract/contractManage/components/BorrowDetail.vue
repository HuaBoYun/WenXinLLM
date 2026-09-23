<!--
 * @Date: 2022-05-07 11:02:16
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-07 11:27:37
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/BorrowDetail.vue
-->
<template>
  <el-row :gutter="15">
    <el-form ref="form" label-width="100px" :model="formData">
      <el-col :span="12">
        <el-form-item label="合同名称" prop="contractname">
          {{ formData.contractname }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同编号" prop="contractno">
          {{ formData.contractno }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="借阅日期" prop="lenddate">
          {{ formData.lenddate }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="归还日期" prop="returndate">
          {{ formData.returndate }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="借阅事由" prop="memo">
          {{ formData.memo }}
        </el-form-item>
      </el-col>
    </el-form>
  </el-row>
</template>

<script>
  export default {
    name: 'BorrowEdit',
    data() {
      return {
        formData: {
          contractId: undefined,
          contractno: undefined,
          contractname: undefined,
          lenddate: undefined,
          returndate: undefined,
          memo: undefined,
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      //赋值
      showDetail(row) {
        this.title = '借阅信息'
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = row[key]
        })
        this.formData.contractId = row.contractid
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
