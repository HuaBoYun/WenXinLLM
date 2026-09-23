<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form ref="form" label-width="120px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="底稿编号" prop="number">
            <el-input v-model.trim="form.number" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="底稿名称" prop="number">
            <el-input v-model.trim="form.number" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计对象" prop="number">
            <el-select
              v-model="form.number"
              clearable
              placeholder="请选择审计对象"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in field103Options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计目标" prop="number">
            <el-input v-model.trim="form.number" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计事项描述" prop="number">
            <el-input
              v-model="form.text"
              clearable
              placeholder="请输入审计事项描述"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计过程" prop="number">
            <el-input
              v-model="form.text"
              clearable
              placeholder="请输入审计过程"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计判断" prop="number">
            <el-input
              v-model="form.text"
              clearable
              placeholder="请输入审计判断"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-col :span="24">
      <el-divider>添加附件</el-divider>
    </el-col>
    <el-col :span="24">
      <div style="text-align: right; margin-bottom: 5px">
        <el-button type="success">上传</el-button>
      </div>
      <el-table>
        <el-table-column align="center" label="附件名称" prop="name" />
        <el-table-column align="center" label="文件大小(KB)" prop="name" />
        <el-table-column align="center" label="创建人" prop="name" />
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit2(row)">下载</el-button>
            <el-button type="text" @click="handleEdit2(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'

  export default {
    name: 'DraftView',
    data() {
      return {
        form: {
          field101: '12.27yw-1',
          field102: '自动',
          field103: '预防性',
          field104: '月度',
          field105: '所需资料：',
          field106: '所需资料：',
          field107: '所需资料：',
          field108: '所需资料：',
          field109: '所需资料：',
          field110: '所需资料：',
        },
        rules: {
          number: [{ required: true, trigger: 'blur', message: '请输入' }],
        },
        field103Options: [
          {
            label: '穿行测试',
            value: 1,
          },
          {
            label: '控制测试',
            value: 2,
          },
        ],
        title: '',
        dialogFormVisible: false,
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
          this.form.number = 'XXXXXXXXXX'
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
    },
  }
</script>
