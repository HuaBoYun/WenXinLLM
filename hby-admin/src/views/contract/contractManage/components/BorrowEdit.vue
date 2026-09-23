<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="100px" :model="formData" :rules="rules">
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model="formData.contractname"
              clearable
              placeholder="请输入合同名称"
              disabled
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.table.show()"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractno">
            <el-input
              v-model="formData.contractno"
              clearable
              placeholder="请输入合同编号"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="借阅日期" prop="lenddate">
            <el-date-picker
              v-model="formData.lenddate"
              placeholder="请输入借阅日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
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
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="借阅事由" prop="memo">
            <el-input
              v-model="formData.memo"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入借阅事由"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <ProcessList :modal="false" ref="process" />
    <ContractTable ref="table" @selected="handTable" />
  </el-dialog>
</template>

<script>
  import { saveBorrowContract } from '@/api/contract/manage'
  import ProcessList from './ProcessList.vue'
  import ContractTable from '@/views/contract/contractManage/components/ContractTable2.vue'
  export default {
    name: 'BorrowEdit',
    components: {
      ProcessList,
      ContractTable,
    },
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
          memo: [
            {
              required: true,
              message: '请输入借阅事由',
              trigger: 'blur',
            },
          ],
          field110: [],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      //点击列表
      handTable(data) {
        this.formData.contractId = data.contractid
        this.formData.contractno = data.contractno
        this.formData.contractname = data.contractname
      },
      showEdit() {
        this.title = '借阅登记'

        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
      },
      //保存
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg, code, lendId } = await saveBorrowContract(
              this.formData
            )
            if (code == '1') {
              //
              this.$refs['process'].save(13, lendId)
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
