<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="80px" :model="form" :rules="rules">
      <el-form-item label="类型名称" prop="name">
        <el-input v-model.trim="form.typeName" />
      </el-form-item>
      <!-- <el-form-item label="上级类型" prop="code">
        <el-select
          v-model="form.code"
          filterable
          placeholder="请选择上级类型"
          style="width: 100%"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item> -->
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { saveContractType, updateContractType } from '@/api/contract/manage'

  export default {
    name: 'CategoryEdit',
    props: {
      parentid: undefined,
    },
    data() {
      return {
        form: {
          typeId: undefined,
          parentid: 0,
          typeName: '',
        },
        rules: {
          code: [{ required: true, trigger: 'blur', message: '请输入编号' }],
          typeName: [
            { required: true, trigger: 'blur', message: '请输入名称' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        options: [
          {
            value: '1',
            label: '党委办公室',
          },
        ],
      }
    },
    watch: {
      parentid(val) {
        this.form.parentid = val
      },
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form.typeId = row.typeid
          this.form.typeName = row.typename
        }
        this.dialogFormVisible = true
      },
      close() {
        // this.$refs['form'].resetFields()
        // this.form = this.$options.data().form
        this.form.typeName = ''
        this.dialogFormVisible = false
      },
      //保存
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let res = null
            if (this.form.parentid === 0) {
              this.form.parentid = undefined
            }
            if (!this.form.typeId) {
              res = await saveContractType(this.form)
            } else {
              res = await updateContractType(this.form)
            }

            if (res.code === '1') {
              this.$baseMessage(
                '保存成功',
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
    },
  }
</script>
