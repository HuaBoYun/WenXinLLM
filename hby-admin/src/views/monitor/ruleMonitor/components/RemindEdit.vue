<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <el-form
      ref="form"
      class="form-edit"
      label-width="140px"
      :model="form"
      :rules="rules"
    >
      <el-form-item label="方案编号" prop="solutioncode">
        <el-input v-model.trim="form.solutioncode" />
      </el-form-item>
      <el-form-item label="方案名称" prop="solutionid">
        <el-input v-model.trim="form.solutionid" />
      </el-form-item>
      <el-form-item label="方案状态">
        <el-radio-group v-model="form.solutionstatus">
          <el-radio label="是" :value="form.solutionstatus" />
          <el-radio label="否" :value="form.solutionstatus" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="创建人" prop="solutionname">
        <el-input v-model.trim="form.solutionname" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input
          v-model.trim="form.memo"
          maxlength="300"
          show-word-limit
          type="textarea"
        />
      </el-form-item>
      <el-form-item label="规则">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button type="success" @click="add">添加指标</el-button>
        </div>
      </el-form-item>
      <el-table :data="tableData">
        <el-table-column align="center" label="规则ID">
          <template #default="{ row }">
            <el-input v-model.trim="row.id" />
          </template>
        </el-table-column>
        <el-table-column align="center" label="规则名称" prop="name">
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
          <template #default="{ row, $index }">
            <el-button type="text" @click="handleEdit2(row, $index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'

  export default {
    name: 'RemindEdit',
    data() {
      return {
        form: {
          code: '',
          name: '',
          org: '长江集团有限公司',
          intro: '',
          remark: '',
          check: '0',
        },
        rules: {
          code: [{ required: true, trigger: 'blur', message: '请输入编号' }],
          name: [{ required: true, trigger: 'blur', message: '请输入名称' }],
        },
        title: '',
        dialogFormVisible: false,
        tableData: [],
        options: [
          {
            value: '1',
            label: '党委办公室',
          },
          {
            value: '2',
            label: '审计监察部',
          },
          {
            value: '3',
            label: '风险管理部',
          },
          {
            value: '4',
            label: '总会办公室',
          },
          {
            value: '5',
            label: '总经理办公室',
          },
          {
            value: '6',
            label: '人力资源部',
          },
          {
            value: '7',
            label: '合同管理部',
          },
          {
            value: '8',
            label: '安全管理部',
          },
          {
            value: '9',
            label: '设备运营部',
          },
          {
            value: '10',
            label: '工程管理部',
          },
          {
            value: '11',
            label: '综合管理部',
          },
          {
            value: '12',
            label: '信息运维管理部',
          },
        ],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
          this.form.org = '长江集团有限公司'
          this.form.code = 'XXXXXXXXXX'
          this.form.name = 'XXXXXXXXXX'
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      add() {
        this.tableData.push({
          name: '',
          id: '',
        })
      },
      handleEdit2(row, index) {
        this.tableData.splice(index, 1)
      },
    },
  }
</script>
<style>
  .form-edit .el-cascader {
    width: 100%;
  }
</style>
