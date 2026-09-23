<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <div style="text-align: right; margin-bottom: 20px">
      <el-button type="success" @click="addDetail">新增</el-button>
    </div>
    <el-table :data="list">
      <el-table-column
        align="center"
        label="旧字段名称"
        prop="orlName"
      ></el-table-column>
      <el-table-column
        align="center"
        label="字段底层英文"
        prop="field"
      ></el-table-column>
      <el-table-column align="center" label="是否必选" prop="isRequired">
        <template #default="{ row }">
          {{ row.isRequired == 1 ? '是' : '否' }}
        </template>
      </el-table-column>
      <!-- <el-table-column
        align="center"
        label="类型"
        prop="type"
      ></el-table-column> -->
      <el-table-column align="center" label="宽度" prop="componentWidth">
        <template #default="{ row }">
          {{ row.componentWidth ? row.componentWidth + '%' : '' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="排序"
        prop="sort"
      ></el-table-column>

      <el-table-column align="center" label="操作">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="Delete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <AddDetail ref="edit" @fetch-data="fetchData"></AddDetail>
    <template #footer>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import AddDetail from './addDetail.vue'
  import { getLRData, delDetailData } from '@/api/setting/auths'
  export default {
    name: 'AddFormEdit',
    components: { AddDetail },
    data() {
      return {
        form: {},
        rules: {
          jobname: [{ required: true, trigger: 'blur', message: '请选择表单' }],
        },
        title: '',
        dialogFormVisible: false,
        sceneId: '',
        sceneCode: '',
        list: [],
      }
    },
    created() {},
    methods: {
      async showEdit(row) {
        this.sceneId = row.id
        this.sceneCode = row.sceneCode
        this.fetchData()
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.dialogFormVisible = false
      },
      async fetchData() {
        const { data } = await getLRData({ sceneId: this.sceneId })
        this.list = data || []
      },
      Delete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await delDetailData({ id: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      addDetail() {
        this.$refs['edit'].showEdit(this.sceneId, null, this.sceneCode)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(this.sceneId, row, this.sceneCode)
      },
    },
  }
</script>
