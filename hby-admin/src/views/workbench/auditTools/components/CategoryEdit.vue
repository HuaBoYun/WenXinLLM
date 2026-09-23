<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      label-width="120px"
      :model="formData"
      :rules="rules"
      :disabled="disabled"
    >
      <el-form-item label="审计类型说明" prop="auditType">
        <el-input v-model="formData.auditType" placeholder="请输入" />
      </el-form-item>
      <el-form-item label="审计类型状态" prop="status">
        <el-select
          v-model="formData.status"
          placeholder="请选择"
          :style="{ width: '100%' }"
        >
          <el-option
            v-for="item in options"
            :label="item.label"
            :value="item.value"
            :key="item.value"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="创建人" prop="createstaff">
        <el-input
          v-model="formData.createstaff"
          disabled
          placeholder="请输入创建人"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createtime">
        <el-date-picker
          v-model="formData.createtime"
          placeholder="请输入创建时间"
          type="date"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          :style="{ width: '100%' }"
          disabled
        />
      </el-form-item> -->
    </el-form>
    <template #footer v-if="!disabled">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
import * as dayjs from 'dayjs'
import {
  saveNbsjTypeOf,
  getNbsjTypeOfInfo,
  modifyNbsjTypeOf,
  getNbsjTypeOfList,
} from '@/api/workbench/auditTools'

export default {
  name: 'LcdyEdit',
  data() {
    return {
      formData: {
        auditType: '',
        parentid: '',
        status: '',
        type: 'YQNS',
        typeId: '',
      },
      rules: {
        auditType: [{ required: true, message: '请输入审计类型说明' }],
        status: [
          { required: true, trigger: 'change', message: '请选择审计类型状态' },
        ],
      },
      title: '',
      type: '',
      dialogFormVisible: false,
      disabled: false,
      options: [
        {
          value: 2,
          label: '正常',
        },
        {
          value: 1,
          label: '禁用',
        },
      ],
    }
  },
  created() {},
  methods: {
    showEdit(row, type) {
      this.type = type
      if (type == 'add') {
        this.title = '新增'
        this.formData.parentid = row.typeId == 1 ? '' : row.typeId
        this.formData.typeId = ''
      } else if (type == 'edit') {
        this.title = '编辑'
        this.getData(row.typeId)
      } else {
        this.title = '详情'
        this.disabled = true
        this.getData(row.typeId)
      }
      this.dialogFormVisible = true
    },
    async getData(typeid) {
      const { data } = await getNbsjTypeOfInfo({ typeid })
      this.formData = data.date
    },
    close() {
      this.formData = {
        auditType: '',
        parentid: '',
        status: '',
        type: 'YQNS',
        typeId: '',
      }
      this.$refs['form'].resetFields()
      this.dialogFormVisible = false
    },
    save() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          let getUrl = this.type == 'add' ? saveNbsjTypeOf : modifyNbsjTypeOf
          const { code } = await getUrl(this.formData)
          if (code == 0) return
          const { data } = await getNbsjTypeOfList({ parentId: 0 })
          let treeData = data.date
          this.$emit('fetch-data', this.formData.parentid, treeData)
          this.close()
        }
      })
    },
  },
}
</script>
