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
      <el-form-item label="规则编码" prop="ruleno">
        <el-input v-model.trim="form.ruleno" />
      </el-form-item>
      <el-form-item label="规则名称" prop="rulename">
        <el-input v-model.trim="form.rulename" />
      </el-form-item>
      <el-form-item label="规则结果" prop="retrunresult">
        <el-input v-model.trim="form.retrunresult" />
      </el-form-item>
      <el-form-item label="规则提示" prop="ruletip">
        <el-input v-model.trim="form.ruletip" />
      </el-form-item>
      <el-form-item label="规则状态" prop="rulestatus">
        <el-radio-group v-model="form.rulestatus">
          <el-radio label="启用" value="1" />
          <el-radio label="禁用" value="2" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="规则所属部门" prop="orgName">
        <!-- <el-cascader
          clearable
          :options="options"
          :props="{ multiple: true, checkStrictly: true }"
        /> -->
        <el-input
          v-model="form.orgName"
          clearable
          placeholder="请选择规则所属部门"
          :style="{ width: '256px' }"
          :disabled="true"
        />
        <el-button
          :style="{ marginLeft: '10px' }"
          type="primary"
          @click="$refs.companyTree.show(1, null)"
        >
          选择
        </el-button>
      </el-form-item>
      <el-form-item label="规则对应SQL">
        <el-input v-model.trim="form.rulesql" type="textarea" />
      </el-form-item>
      <el-form-item label="规则描述">
        <el-input
          v-model.trim="form.rulememo"
          maxlength="300"
          show-word-limit
          type="textarea"
        />
      </el-form-item>
      <el-form-item label="字符设置">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button type="success">添加</el-button>
        </div>
      </el-form-item>
      <el-table :data="tableData">
        <el-table-column align="center" label="替换符">
          <template #default="{ row }">
            <el-input v-model.trim="row.name" />
          </template>
        </el-table-column>
        <el-table-column align="center" label="替换值" prop="name">
          <template #default="{ row }">
            <el-input v-model.trim="row.name" />
          </template>
        </el-table-column>
        <el-table-column align="center" label="替换类型" prop="name">
          <template #default="{ row }">
            <el-select v-model="row.name" placeholder="请选择">
              <el-option label="文本" value="0" />
              <el-option label="数字" value="1" />
              <el-option label="时间" value="2" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit2(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <company-tree-model
      ref="companyTree"
      @selected="handleCompanyTreeSelected"
    />
  </el-dialog>
</template>

<script>
  // import { doEdit } from '@/api/table'
  import { saveFromRule } from '@/api/monitor/formMonitor'
  import CompanyTreeModel from '@/components/CompanyTreeModel'

  export default {
    name: 'FormEdit',
    components: {
      CompanyTreeModel,
    },
    data() {
      return {
        form: {
          ruleno: '',
          rulename: '',
          retrunresult: '',
          ruletip: '',
          rulestatus: '',
          orgid: '',
          orgName: '',
          rulesql: '',
          rulememo: '',
        },
        rules: {
          ruleno: [
            { required: true, trigger: 'blur', message: '请输入规则编码' },
          ],
          rulename: [
            { required: true, trigger: 'blur', message: '请输入规则名称' },
          ],
          retrunresult: [
            { required: true, trigger: 'blur', message: '请输入规则结果' },
          ],
          ruletip: [
            { required: true, trigger: 'blur', message: '请输入规则提示' },
          ],
          rulestatus: [
            { required: true, trigger: 'blur', message: '请输入名称' },
          ],
          rulename: [
            { required: true, trigger: 'blur', message: '请输入名称' },
          ],
          orgName: [{ required: true, trigger: 'blur', message: '请选择部门' }],
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
      handleCompanyTreeSelected(node) {
        if (node instanceof Array) {
          const { orgid, orgName, ...other } = this.form
          let form = {
            orgName: node.map((item) => item.name).join(','),
            orgid: node.map((item) => item.id).join(','),
            ...other,
          }
          this.form = form
        } else {
          const { oid2, org2, ...other } = this.form
          let form = { oid2: node.id, org2: node.name, ...other }
          this.form = form
        }
      },
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
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
            const { msg, code } = await saveFromRule(this.form)
            if (code == 200) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
              this.$emit('fetch-data')
            }

            this.close()
          }
        })
      },
    },
  }
</script>
<style>
  .form-edit .el-cascader {
    width: 100%;
  }
</style>
