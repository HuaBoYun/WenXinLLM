<template>
  <div>
    <span @click="dialogVisible = true">
      <vab-icon icon="artboard-fill" />

      系统帮助
    </span>
    <el-dialog
      :append-to-body="true"
      :close-on-click-modal="false"
      title="系统帮助"
      :visible.sync="dialogVisible"
      width="80%"
    >
      <el-table :data="list">
        <el-table-column align="center" label="模块名称" prop="data" />
        <el-table-column align="center" label="模块描述" prop="data" />
        <el-table-column align="center" label="附件" prop="data" />
        <el-table-column align="center" label="视频" prop="data" />
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <!-- <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template> -->
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">
          取 消
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        dialogVisible: false,
        form: {},
        list: [],
      }
    },
    created() {},
    methods: {
      onBack() {},
    },
  }
</script>
