<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-28 10:39:23
 * @FilePath: /hb-admin/src/views/setting/system/components/FlowCategoryAdd.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="80px" :model="form" :rules="rules">
      <el-form-item label="流程编号" prop="title">
        <el-input v-model.trim="form.flownumber" />
      </el-form-item>
      <el-form-item label="流程名称" prop="flowName">
        <el-input v-model.trim="form.flowName" />
      </el-form-item>
      <el-form-item label="父流程" prop="fatherflowname">
        <el-input v-model.trim="form.fatherflowname" readonly />
      </el-form-item>
    </el-form>
    <template #footer>
      <!-- <el-button
        v-if="title === '编辑'"
        style="float: left"
        type="danger"
        @click="deleteNode"
      >
        删 除
      </el-button> -->
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    addBusinessTree,
    deleteBusinessTree,
    getBusinessTreeDetail,
    updateBusinessTree,
  } from '@/api/setting/system'

  export default {
    name: 'FlowCategoryAdd',
    data() {
      return {
        form: {
          flownumber: undefined,
          flowName: undefined,
          fatherflowid: undefined,
          fatherflowname: undefined,
        },
        rules: {
          flownumber: [
            { required: true, trigger: 'blur', message: '请输入流程编号' },
          ],
          flowName: [
            { required: true, trigger: 'blur', message: '请输入流程名称' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        action: undefined,
      }
    },
    created() {},
    methods: {
      async showEdit(node, action) {
        this.action = action
        const {
          // parent,
          data: { id, label },
        } = node
        if (action == 'add') {
          this.title = '添加'
          this.form.fatherflowid = id
          this.form.fatherflowname = label
        } else if (action == 'update') {
          this.title = '编辑'
          if (id == 0) {
            const msg = '请选择流程控制'
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            return
          }
          // 需要flownumber所以需要请求
          const res = await getBusinessTreeDetail({ flowid: id })
          // 修改时不需要传fatherflowid
          // const pid = parent.data.id
          this.form.flowid = res.flowid
          this.form.flownumber = res.flownumber
          this.form.flowName = res.flowname
          this.form.fatherflowname = res.father
          // this.form.fatherflowid = pid
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      async deleteNode(node) {
        if (!node || node.data.id == 0) {
          const msg = '请选择流程控制'
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          return
        } else if (node.childNodes && node.childNodes.length) {
          const msg = '请先删除子级'
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          return
        }
        const { msg, code } = await deleteBusinessTree({ flowid: node.data.id })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-tree')
        }
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const fn =
              this.action == 'add' ? addBusinessTree : updateBusinessTree
            const { msg } = await fn(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.close()
            this.$emit('fetch-tree')
          }
        })
      },
    },
  }
</script>
