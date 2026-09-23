<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :destroy-on-close="true"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="125px"
          :model="formData"
          :disabled="disabled"
        >
          <el-col :span="12">
            <el-form-item label="方案编号">
              <el-input
                v-model="formData.solutioncode"
                type="text"
                placeholder="请输入方案编号"
                :disabled="!footer"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="方案名称" prop="solutionname">
              <el-input
                v-model="formData.solutionname"
                type="text"
                placeholder="请输入方案名称"
                :disabled="!footer"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="方案状态" prop="solutionstatus">
              <el-select v-model="formData.solutionstatus" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人" prop="realname">
              <el-input
                type="text"
                v-model="formData.realname"
                placeholder="请输入方案名称"
                :disabled="true"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="memo">
              <el-input
                type="textarea"
                :rows="4"
                maxlength="300"
                v-model="formData.memo"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <div v-if="!disabled" style="text-align: right; margin-bottom: 5px">
              <el-button type="success" @click="handleAdd">添加指标</el-button>
            </div>
            <el-table :data="tableData">
              <el-table-column
                align="center"
                label="指标编号"
                prop="solutionid"
              />
              <el-table-column
                align="center"
                label="指标名称"
                prop="indicatorid"
              />
              <el-table-column align="center" label="操作" prop="name" />
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
    <RuleResult ref="ruleresult" />
  </div>
</template>

<script>
  import { getErrorIndexDetail, ToSolutionAdd } from '@/api/monitor/watch'
  import RuleResult from '../../ruleMonitor/components/RuleResult.vue'
  export default {
    name: 'TargetInfo',
    components: { RuleResult },
    data() {
      return {
        dialogFormVisible: false,
        tableData: [],
        footer: true,
        rules: {},
        formData: {},
        title: '',
        disabled: false,
        options: [
          {
            value: '1',
            label: '启用',
          },
          {
            value: '2',
            label: '禁用',
          },
        ],
        value: '',
      }
    },
    created() {},
    methods: {
      showEdit(flag, row) {
        this.dialogFormVisible = true
        this.formData = {}
        if (flag == 'look') {
          getErrorIndexDetail({ selectedid: row.solutionid }).then((res) => {
            this.formData = res.data.solution
            this.tableData = res.data.solutionDetail
          })
          this.title = '详情'
          this.disabled = true
        } else if (flag == 'add') {
          this.title = '新增'
          this.disabled = false
          this.tableData = []
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.realname = userInfo.realname
          this.formData.Orgid = userInfo.staffid
        } else {
          getErrorIndexDetail({ selectedid: row.solutionid }).then((res) => {
            this.formData = res.data.solution
            this.tableData = res.data.solutionDetail
          })
          this.title = '编辑'
          this.disabled = false
        }
      },
      close() {
        this.formData = {}
        this.title = '新增'
        this.dialogFormVisible = false
      },
      handleAdd(row) {
        this.$refs['ruleresult'].showEdit(row)
      },
      async save() {
        const { msg, code } = await ToSolutionAdd(this.formData)
        if (code == 200) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
        this.close()
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
