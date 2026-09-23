<template>
  <el-dialog
    v-if="dialogFormVisible"
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <el-form ref="form" label-width="120px" :model="form" :rules="rules">
      <el-form-item label="组件类型" prop="componentType">
        <el-select
          v-model="form.componentType"
          placeholder="请选择组件类型"
          style="width: 100%"
          filterable
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <div
        v-if="
          form.componentType == 'Cselect' ||
          form.componentType == 'CmultipleSelect' ||
          form.componentType == 'Cradio' ||
          form.componentType == 'Ccheckbox'
        "
      >
        <div v-for="(domain, index) in form.pullDownExtList" :key="domain.key">
          <el-form-item
            :label="'选项' + (index + 1)"
            :prop="'pullDownExtList.' + index + '.value'"
            :rules="[
              { required: true, message: '请输入选项信息', trigger: 'blur' },
            ]"
          >
            <el-input
              v-model="domain.value"
              style="width: 40%"
              placeholder="value"
            ></el-input>
            <el-input
              v-model="domain.label"
              style="width: 40%; margin-left: 20px"
              placeholder="label"
            ></el-input>
            <el-button
              @click.prevent="removeDomain(domain)"
              style="margin-left: 20px"
            >
              删除
            </el-button>
          </el-form-item>
        </div>
      </div>

      <el-form-item
        v-if="
          form.componentType == 'Cselect' ||
          form.componentType == 'CmultipleSelect' ||
          form.componentType == 'Cradio' ||
          form.componentType == 'Ccheckbox'
        "
      >
        <el-button @click="addDomain">新增选项信息</el-button>
      </el-form-item>
      <el-form-item
        v-if="
          form.componentType == 'CselectDepartment' ||
          form.componentType == 'CselectDepartments' ||
          form.componentType == 'CselectPerson' ||
          form.componentType == 'CselectPeople' ||
          form.componentType == 'CselectConmpany' ||
          form.componentType == 'CselectConmpanys' ||
          form.componentType == 'Cskht' ||
          form.componentType == 'Cydskx' ||
          form.componentType == 'Cfkyhzh' ||
          form.componentType == 'Cskyhzh' ||
          form.componentType == 'Cfph' ||
          form.componentType == 'Chtmc' ||
          form.componentType == 'Cpjdx' ||
          form.componentType == 'Cpjmb' ||
          form.componentType == 'Ccsmb'
        "
        label="附属字段"
      >
        <el-input v-model.trim="form.attachedField" />
      </el-form-item>
      <!-- <el-form-item
        v-if="form.componentType"
        label="是否存在联动"
        prop="isLinkage"
      >
        <el-radio-group v-model="form.isLinkage">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item> -->
      <el-form-item label="字段中文名称" prop="orlName">
        <el-input v-model.trim="form.orlName" />
      </el-form-item>
      <el-form-item label="名称底层代码" prop="field">
        <el-input v-model.trim="form.field" />
      </el-form-item>
      <el-form-item label="初始是否必选" prop="isSystemRequired">
        <el-radio-group v-model="form.isSystemRequired">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        label="是否必选"
        prop="isRequired"
        v-if="form.isSystemRequired == 0"
      >
        <el-radio-group v-model="form.isRequired">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="组件宽度" prop="componentWidth">
        <el-radio-group v-model="form.componentWidth">
          <el-radio label="50">50%</el-radio>
          <el-radio label="100">100%</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="组件是否可编辑" prop="isEdit">
        <el-radio-group v-model="form.isEdit">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否编辑展示" prop="isDetails">
        <el-radio-group v-model="form.isDetails">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否列表展示" prop="isList">
        <el-radio-group v-model="form.isList">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="详情排序" prop="sort">
        <el-input v-model.trim="form.sort" />
      </el-form-item>
      <el-form-item label="列表排序" prop="listSort">
        <el-input v-model.trim="form.listSort" />
      </el-form-item>

      <el-form-item label="是否存在分组" prop="isGroup">
        <el-radio-group v-model.trim="form.isGroup" @change="changeGroup">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="组名称" prop="groupName" v-if="form.isGroup == 1">
        <el-input v-model.trim="form.groupName" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { addLRZD, getLRDetail } from '@/api/setting/auths'

  export default {
    name: 'AddFormEdit',
    data() {
      return {
        form: {
          orlName: '',
          field: '',
          isSystemRequired: '',
          isRequired: '',
          componentWidth: '',
          sort: '',
          isGroup: 0,
          groupName: '',
          componentType: '',
          listSort: '',
          isLinkage: 0,
          isEdit: 1,
          attachedField: '',
          isDetails: '',
          isList: '',
          pullDownExtList: [
            {
              value: '',
              label: '',
            },
          ],
        },
        rules: {
          jobname: [{ required: true, trigger: 'blur', message: '请选择表单' }],
        },
        title: '',
        dialogFormVisible: false,
        sceneId: '',
        sceneCode: '',
        options: [
          {
            value: 'Cinput',
            label: '输入框',
          },
          {
            value: 'Ctarea',
            label: '文本域',
          },
          {
            value: 'Cnumber',
            label: '数字输入框',
          },
          {
            value: 'Cselect',
            label: '单选下拉框',
          },
          {
            value: 'CmultipleSelect',
            label: '多选下拉框',
          },
          {
            value: 'Cradio',
            label: '单选框',
          },
          {
            value: 'Ccheckbox',
            label: '多选框',
          },
          {
            value: 'Cyear',
            label: '年份选择框',
          },
          {
            value: 'CdatePicker',
            label: '日期选择框(年月日)',
          },
          {
            value: 'CmultipleDatePicker',
            label: '日期范围选择框(年月日)',
          },
          {
            value: 'CdateRange',
            label: '日期时间选择框(年月日时分秒)',
          },
          {
            value: 'CmultipleDateRange',
            label: '日期时间范围选择框(年月日时分秒)',
          },
          {
            value: 'CselectDepartment',
            label: '单选弹框选择部门',
          },
          {
            value: 'CselectDepartments',
            label: '多选弹框选择部门',
          },
          {
            value: 'CselectPerson',
            label: '单选弹框选择人员',
          },
          {
            value: 'CselectPeople',
            label: '多选弹框选择人员',
          },
          {
            value: 'CselectConmpany',
            label: '单选弹框选择公司',
          },
          {
            value: 'CselectConmpanys',
            label: '多选弹框选择公司',
          },
          {
            value: 'Cskht',
            label: '收款合同（财务信息-收款信息）',
          },
          {
            value: 'Cydskx',
            label: '对应收款项（财务信息-收款信息）',
          },
          {
            value: 'Cfkyhzh',
            label: '付款银行账号（财务信息-收款信息）',
          },
          {
            value: 'Cskyhzh',
            label: '收款银行账号（财务信息-收款信息）',
          },
          {
            value: 'Cfph',
            label: '发票号（财务信息-收款信息）',
          },
          {
            value: 'Chtmc',
            label: '合同名称（财务信息-付款信息）',
          },
          {
            value: 'Cpjdx',
            label: '评价对象（评价管理-评价立项）',
          },
          {
            value: 'Cpjmb',
            label: '评价模板（评价管理-评价立项）',
          },
          {
            value: 'Ccsmb',
            label: '测试模板（内控测试-测试方案）',
          },
        ],
        rules: {
          componentType: [
            {
              required: true,
              message: '请选择组件类型',
              trigger: 'blur',
            },
          ],
          field: [
            {
              required: true,
              message: '请输入底层代码',
              trigger: 'blur',
            },
          ],
          isLinkage: [
            {
              required: true,
              message: '请选择是否联动',
              trigger: 'blur',
            },
          ],
          isSystemRequired: [
            {
              required: true,
              message: '请选择初始是否必选',
              trigger: 'blur',
            },
          ],
          listSort: [
            {
              required: true,
              message: '请输入列表排序',
              trigger: 'blur',
            },
          ],
          sort: [
            {
              required: true,
              message: '请输入详情排序',
              trigger: 'blur',
            },
          ],
          groupName: [
            {
              required: true,
              message: '请输入组名称',
              trigger: 'blur',
            },
          ],
        },
      }
    },
    created() {},
    methods: {
      async showEdit(id, row, sceneCode) {
        this.title = '新增'
        if (row) {
          this.title = '编辑'
          const res = await getLRDetail({ id: row.id })
          this.form.orlName = res.data.orlName
          this.form.field = res.data.field
          this.form.isSystemRequired = res.data.isSystemRequired
          this.form.isRequired = res.data.isRequired
          this.form.componentWidth = res.data.componentWidth
          this.form.sort = res.data.sort
          this.form.id = res.data.id
          this.form.isGroup = res.data.groupName ? 1 : 0
          this.form.groupName = res.data.groupName
          this.form.componentType = res.data.componentType
          this.form.isLinkage = res.data.isLinkage
          this.form.listSort = res.data.listSort
          this.form.isEdit = res.data.isEdit
          this.form.attachedField = res.data.attachedField
          this.form.isList = res.data.isList
          this.form.isDetails = res.data.isDetails
        }

        this.sceneId = id
        this.sceneCode = sceneCode
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      async save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const res = await addLRZD({
              ...this.form,
              sceneId: this.sceneId,
              sceneCode: this.sceneCode,
            })
            if (res.code == 200) {
              this.$baseMessage(
                '新增成功',
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
      removeDomain(item) {
        var index = this.form.pullDownExtList.indexOf(item)
        if (index !== -1) {
          this.form.pullDownExtList.splice(index, 1)
        }
      },
      addDomain() {
        this.form.pullDownExtList.push({
          value: '',
          key: Date.now(),
        })
      },
      changeGroup() {
        this.form.groupName = ''
      },
    },
  }
</script>
