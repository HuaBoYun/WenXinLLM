<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="80px" :model="formData" :rules="rules">
      <el-form-item label="课程类型" prop="typename">
        <el-input
          v-model="formData.typename"
          clearable
          placeholder=""
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="所属模块" prop="type">
        <el-radio-group v-model="formData.type" size="medium">
          <el-radio
            v-for="(item, index) in typeOptions"
            :key="index"
            :disabled="item.disabled"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { saveCourseType } from '@/api/setting/course'

  export default {
    name: 'KclbEdit',
    data: function () {
      return {
        formData: {
          typeId: undefined,
          typename: undefined,
          type: undefined,
          version: undefined,
        },
        rules: {
          typename: [
            {
              required: true,
              message: '课程类型不能为空',
              trigger: 'change',
            },
          ],
          type: [
            {
              required: true,
              message: '所属模块不能为空',
              trigger: 'change',
            },
          ],
        },
        typeOptions: [
          {
            label: '财经论坛',
            value: '1',
          },
          {
            label: '智能财管',
            value: '2',
          },
          {
            label: '智能风控',
            value: '3',
          },
          {
            label: '税务管理',
            value: '4',
          },
          {
            label: '内部审计',
            value: '5',
          },
          {
            label: '法律合规',
            value: '6',
          },
        ],
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {
      // this.getTypeList()
    },
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = row[key]
          })
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await saveCourseType(this.formData)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
