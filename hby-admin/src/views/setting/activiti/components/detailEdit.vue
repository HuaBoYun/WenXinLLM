<template>
  <el-dialog
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
          disabled
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="是否存在联动" prop="isLinkage">
        <el-radio-group v-model="form.isLinkage" disabled>
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item> -->
      <el-form-item label="旧字段名称" prop="orlName">
        <el-input v-model="form.orlName" disabled />
      </el-form-item>
      <el-form-item label="新字段名称" prop="changeName">
        <el-input v-model="form.changeName" />
      </el-form-item>
      <el-form-item label="是否必选" prop="isRequired">
        <el-radio-group
          v-model="form.isRequired"
          :disabled="form.isSystemRequired == 1"
        >
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
      <el-form-item label="选项宽度" prop="componentWidth">
        <el-radio-group v-model="form.componentWidth">
          <el-radio label="50">50%</el-radio>
          <el-radio label="100">100%</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item label="组件是否可编辑" prop="isEdit">
        <el-radio-group v-model="form.isEdit">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item> -->
      <el-form-item label="详情排序" prop="sort">
        <el-input v-model.trim="form.sort" />
      </el-form-item>
      <el-form-item label="列表排序" prop="listSort">
        <el-input v-model.trim="form.listSort" />
      </el-form-item>
      <!-- <el-form-item label="是否存在分组" prop="isGroup">
        <el-radio-group v-model="form.isGroup" @change="changeGroup">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="组名称" prop="groupName" v-if="form.isGroup == 1">
        <el-input v-model="form.groupName" />
      </el-form-item> -->
    </el-form>

    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { updateLRZD, getEditZDDetail } from '@/api/setting/auths'

  export default {
    name: 'AddFormEdit',
    data() {
      return {
        form: {
          isRequired: '',
          componentWidth: '',
          orlName: '',
          changeName: '',
          sort: '',
          listSortsort: '',
          isDetails: '',
          isList: '',
          isGroup: 0,
          groupName: '',
          componentType: '',
          isLinkage: '',
        },
        rules: {
          jobname: [{ required: true, trigger: 'blur', message: '请选择表单' }],
        },
        title: '',
        dialogFormVisible: false,
        sceneId: '',
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
            label: '下拉框',
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
      }
    },
    created() {},
    methods: {
      async showEdit(row, id) {
        this.title = '修改'
        this.sceneId = id
        const res = await getEditZDDetail({ id: row.id })
        this.form.orlName = res.data.orlName
        this.form.isRequired = +res.data.isRequired
        this.form.componentWidth = res.data.componentWidth
        this.form.changeName = res.data.changeName
        this.form.sort = res.data.sort
        this.form.id = res.data.id
        this.form.isDetails = res.data.isDetails
        this.form.isList = res.data.isList
        this.form.isSystemRequired = res.data.isSystemRequired
        this.form.isGroup = res.data.groupName ? 1 : 0
        this.form.groupName = res.data.groupName
        this.form.componentType = res.data.componentType
        this.form.isLinkage = res.data.isLinkage
        this.form.listSort = res.data.listSort
        this.form.isEdit = res.data.isEdit

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
            const res = await updateLRZD({
              ...this.form,
              sceneId: this.sceneId,
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
      changeGroup() {
        this.form.groupName = ''
      },
    },
  }
</script>
