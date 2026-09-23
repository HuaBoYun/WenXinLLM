<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="80px" :model="form" :rules="rules">
      <el-form-item label="模块编号" prop="modelNo">
        <el-input v-model.trim="form.modelNo" />
      </el-form-item>
      <el-form-item label="模块名称" prop="modelName">
        <el-input v-model.trim="form.modelName" />
      </el-form-item>
      <el-form-item label="所属模块" prop="modelType">
        <el-select
          v-model="form.modelType"
          filterable
          placeholder="请选择所属模块"
          style="width: 100%"
        >
          <el-option
            v-for="item in modelTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <el-divider style="margin-top: 20px">选择流程</el-divider>
    <el-button
      style="margin: 10px"
      type="primary"
      @click="$refs['flow'].show()"
    >
      选择
    </el-button>
    <el-button type="danger" @click="handleDelete()">删除</el-button>
    <el-table
      :data="list"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="序号" prop="date" width="55">
        <template #default="{ $index }">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="流程名称" prop="flowname" show-overflow-tooltip />
      <!-- <el-table-column align="center" label="操作">
        <template #default="{ row }">
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <FlowSelect ref="flow" @select="handleFlowSelect" />
  </el-dialog>
</template>

<script>
  import {
    addModule,
    deleteFlowOfModule,
    updateModule,
  } from '@/api/setting/system'
  import { modelTypeOptions } from '../consts'
  import FlowSelect from './FlowSelect.vue'

  export default {
    name: 'MkcjEdit',
    components: { FlowSelect },
    data() {
      return {
        list: [],
        form: {
          modelId: undefined,
          modelNo: '',
          modelName: '',
          modelType: '',
          flowid: [],
        },
        rules: {
          modelNo: [
            { required: true, trigger: 'blur', message: '请输入模块编号' },
          ],
          modelName: [
            { required: true, trigger: 'blur', message: '请输入模块名称' },
          ],
          modelType: [
            { required: true, trigger: 'blur', message: '请选择所属模块' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        modelTypeOptions,
        multipleSelection: [],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
          this.list = row.flowList
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      handleFlowSelect(data) {
        data.map((item) => (item.isLocal = true))
        this.list = this.list.concat(data)
      },
      handleSelectionChange(data) {
        this.multipleSelection = data
      },
      async handleDelete() {
        if (!this.multipleSelection.length) {
          this.$baseMessage('请选择', 'error', 'vab-hey-message-error')
          return
        }
        const localIds = []
        const flowIds = []
        this.multipleSelection.forEach((item) => {
          if (item.isLocal) {
            localIds.push(item.flowid)
          } else {
            flowIds.push(item.flowid)
          }
        })

        this.list = this.list.filter((item) => {
          return localIds.indexOf(item.flowid) < 0
        })

        if (!flowIds.length) return
        const { msg, code } = await deleteFlowOfModule({
          flowIds,
          modelId: this.form.modelId,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.list = this.list.filter((item) => {
            return flowIds.indexOf(item.flowid) < 0
          })
          this.$emit('fetch-data')
        }
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.form.flowid = this.list.map((item) => item.flowid)
            let res = null
            if (this.form.modelId) {
              res = await updateModule(this.form)
            } else {
              res = await addModule(this.form)
            }
            if (res.code == 1) {
              this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
    },
  }
</script>
