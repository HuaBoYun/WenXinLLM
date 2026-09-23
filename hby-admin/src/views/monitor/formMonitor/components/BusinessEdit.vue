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
      <el-form-item label="规则结果" prop="returnresult">
        <el-input v-model.trim="form.returnresult" />
      </el-form-item>
      <el-form-item label="规则提示" prop="ruletip">
        <el-input v-model.trim="form.ruletip" />
      </el-form-item>
      <el-form-item label="监控字段">
        <el-input
          v-model="form.eleName"
          clearable
          :style="{ width: '500px' }"
        />
        <el-button
          :style="{ marginLeft: '10px' }"
          type="primary"
          @click="$refs.setup.show()"
        >
          选择
        </el-button>
      </el-form-item>
      <el-form-item label="规则状态" prop="rulestatus">
        <el-radio-group v-model="form.rulestatus">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="2">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="规则所属部门" prop="orgname">
        <el-input v-model.trim="form.orgname" disabled />
      </el-form-item>
      <el-form-item label="规则对应SQL" prop="rulesql">
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
      <!-- <el-form-item label="字符设置">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button type="success">添加</el-button>
        </div>
      </el-form-item> -->
      <el-divider>字符设置</el-divider>
      <!-- <Bank :opposite="current" :readonly="true" /> -->
      <!-- <character-set @addCharacter="addCharacter" /> -->
      <div style="text-align: right; margin-bottom: 5px">
        <el-button type="success" @click="addData">添加</el-button>
      </div>
      <el-table :data="tableData">
        <el-table-column align="center" label="替换符" prop="replacekey">
          <template #default="{ row }">
            <el-input v-model.trim="row.replacekey" />
          </template>
        </el-table-column>
        <el-table-column align="center" label="替换值" prop="replacekvalue">
          <template #default="{ row }">
            <el-input v-model.trim="row.replacekvalue" />
          </template>
        </el-table-column>
        <el-table-column align="center" label="替换类型" prop="replacetype">
          <template #default="{ row }">
            <el-select v-model="row.replacetype" placeholder="请选择">
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
          <template slot-scope="scope">
            <el-button type="text" @click="handleDelete(scope.$index)">
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

    <customize-form-list ref="setup" @selected="handleSetupSelected" />
  </el-dialog>
</template>

<script>
  import CustomizeFormList from './CustomizeFormList.vue'
  import CharacterSet from './CharacterSet.vue'
  import { saveFromRule } from '@/api/monitor/formMonitor/index'

  export default {
    name: 'FormEdit',
    components: {
      CustomizeFormList,
      CharacterSet,
    },
    data() {
      return {
        form: {
          ruleno: '',
          rulename: '',
          returnresult: '',
          ruletip: '',
          rulestatus: 1,
          orgid: '',
          orgname: '',
          rulesql: '',
          rulememo: '',
          eleId: '',
          eleName: '',
          replacekey: [],
          replacekvalue: [],
          replacetype: [],
        },
        rules: {
          ruleno: [
            { required: true, trigger: 'blur', message: '请输入规则编号' },
          ],
          rulename: [
            { required: true, trigger: 'blur', message: '请输入规则名称' },
          ],
          returnresult: [
            { required: true, trigger: 'blur', message: '请输入规则结果' },
          ],
          orgname: [
            { required: true, trigger: 'blur', message: '请输入规则所属部门' },
          ],
          rulesql: [
            { required: true, trigger: 'blur', message: '请输入规则对应SQL' },
          ],
          replacekey: [
            { required: true, trigger: 'blur', message: '请输入规则结果' },
          ],
          replacekvalue: [
            { required: true, trigger: 'blur', message: '请输入规则所属部门' },
          ],
          replacetype: [
            { required: true, trigger: 'blur', message: '请输入规则对应SQL' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        tableData: [{ replacekey: '', replacekvalue: '', replacetype: '' }],
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
        count: 0,
      }
    },
    created() {},
    methods: {
      showEdit(row, title) {
        if (title == '添加') {
          this.title = '添加'
          this.form.rulestatus = 1
          this.form.orgid = row.orgid
          this.form.orgname = row.orgname
        } else {
          this.title = '编辑'

          this.form = row
        }
        this.dialogFormVisible = true
      },
      addData() {
        let rowObj = {
          replacekey: '',
          replacekvalue: '',
          replacetype: '',
        }
        this.tableData.push(rowObj)
        // }
      },
      handleDelete(index) {
        this.tableData.splice(index, 1)
      },
      close() {
        this.form = {}
        this.tableData = [
          { replacekey: '', replacekvalue: '', replacetype: '' },
        ]
        this.dialogFormVisible = false
      },
      handleSetupSelected(node) {
        let form = this.form
        form = {
          ...form,
          eleId: node.formid,
          eleName: node.formname,
        }
        this.form = form
      },
      addCharacter(val) {},
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let replacekey = []
            let replacekvalue = []
            let replacetype = []
            for (var i = 0; i < this.tableData.length; i++) {
              if (!this.tableData[i].replacetype) {
                this.$baseMessage(
                  '请选择替换类型',
                  'error',
                  'vab-hey-message-error'
                )
                return
              } else {
                replacekey.push(this.tableData[i].replacekey)
                replacekvalue.push(this.tableData[i].replacekvalue)
                replacetype.push(this.tableData[i].replacetype)
              }
            }

            this.form.replacekey = replacekey
            this.form.replacekvalue = replacekvalue
            this.form.replacetype = replacetype

            const { data, msg } = await saveFromRule(this.form)
            if (msg == '错误') {
              // this.$baseMessage(data.result, 'error', 'vab-hey-message-error')
            } else {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }

            this.$emit('fetch-data')
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
