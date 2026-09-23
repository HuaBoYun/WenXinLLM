<template>
  <el-row v-if="dialogFormVisible" :gutter="15">
    <el-form ref="form" label-width="100px" :model="formData" :rules="rules">
      <el-col :span="12">
        <el-form-item label="合同名称" prop="contractname">
          <el-input
            v-model="formData.contractname"
            clearable
            placeholder=""
            disabled
            readonly
            :style="{ width: '100%' }"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同编号" prop="contractno">
          <el-input
            v-model="formData.contractno"
            clearable
            disabled
            placeholder=""
            readonly
            :style="{ width: '100%' }"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="借阅日期" prop="lenddate">
          <el-date-picker
            v-model="formData.lenddate"
            placeholder=""
            readonly
            :style="{ width: '100%' }"
            value-format="yyyy-MM-dd"
            :disabled="disabled"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="归还日期" prop="returndate">
          <el-date-picker
            v-model="formData.returndate"
            clearable
            placeholder="请输入归还日期"
            :style="{ width: '100%' }"
            value-format="yyyy-MM-dd"
            :disabled="disabled"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="借阅事由" prop="memo">
          <el-input
            :disabled="disabled"
            v-model="formData.memo"
            :autosize="{ minRows: 4, maxRows: 4 }"
            placeholder="请输入借阅事由"
            :style="{ width: '100%' }"
            type="textarea"
          />
        </el-form-item>
      </el-col>
    </el-form>
    <!-- <div style="text-align: right; margin-top: 10px; margin-right: 10px">
      <el-button type="primary" @click="save" v-if="!disabled">确 定</el-button>
    </div> -->
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
        rules: {
          field101: [],
          lenddate: [
            {
              required: true,
              message: '请输入借阅日期',
              trigger: 'blur',
            },
          ],
          returndate: [
            {
              required: true,
              message: '请输入归还日期',
              trigger: 'blur',
            },
          ],
          contractname: [
            {
              required: true,
              message: '请输入合同名称',
              trigger: 'blur',
            },
          ],
          contractno: [
            {
              required: true,
              message: '请输入合同编号',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        disabled: true,
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showDetail(row) {
        this.title = '借阅登记'
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = row[key]
        })

        // this.formData = {
        //   ...row.cy,
        //   ...row.tblContractLead,
        // };
        // readonly下value-format无效
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        if (userInfo.staffid == row.cystaffid && row.cystate === '需调整') {
          this.disabled = false
        }
        let now = new Date()
        this.formData.lenddate = `${now.getFullYear()}-${
          now.getMonth() + 1
        }-${now.getDate()}`

        this.formData.contractId = row.contractid
        this.dialogFormVisible = true
      },
      save() {},
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
