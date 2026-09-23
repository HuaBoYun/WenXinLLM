<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
  >
    <div>
      <el-tree
        :data="data"
        :props="defaultProps"
        @node-click="handleNodeClick"
      />
    </div>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'

  export default {
    name: 'CopyToIndustry',
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        data: [
          {
            label: '一级 1',
            children: [
              {
                label: '二级 1-1',
                children: [
                  {
                    label: '三级 1-1-1',
                  },
                ],
              },
            ],
          },
          {
            label: '一级 2',
            children: [
              {
                label: '二级 2-1',
                children: [
                  {
                    label: '三级 2-1-1',
                  },
                ],
              },
              {
                label: '二级 2-2',
                children: [
                  {
                    label: '三级 2-2-1',
                  },
                ],
              },
            ],
          },
          {
            label: '一级 3',
            children: [
              {
                label: '二级 3-1',
                children: [
                  {
                    label: '三级 3-1-1',
                  },
                ],
              },
              {
                label: '二级 3-2',
                children: [
                  {
                    label: '三级 3-2-1',
                  },
                ],
              },
            ],
          },
        ],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
        }
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      async save() {
        const { msg } = await doEdit(this.form)
        this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        this.$emit('fetch-data')
        this.close()
      },
      handleNodeClick(data) {
        console.log(data)
      },
    },
  }
</script>
