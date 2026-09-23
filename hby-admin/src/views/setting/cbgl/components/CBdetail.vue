<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="14">
        <el-form
          ref="elForm"
          label-width="110px"
          :model="formData"
          :rules="rules"
          size="mini"
        >
          <el-col :span="23">
            <el-form-item label="催办人" prop="creatorName">
              <el-input
                style="width: 100%"
                v-model="formData.creatorName"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="催办时间" prop="createdTime">
              <el-input
                style="width: 100%"
                v-model="formData.createdTime"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预计完成时间" prop="reminderTime">
              <el-input
                style="width: 100%"
                v-model="formData.reminderTime"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="催办内容" prop="reminderContent">
              <el-input
                style="width: 100%"
                v-model="formData.reminderContent"
                clearable
                placeholder="请输入审计目标"
                type="textarea"
                rows="4"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer">
        <el-button @click="close">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getHomeCBListDetail1 } from '@/api/setting/report'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        formData: {},
        rules: {},
      }
    },
    methods: {
      close() {
        this.dialogFormVisible = false
        this.$emit('close')
      },
      showEdit(row) {
        this.dialogFormVisible = true
        getHomeCBListDetail1({ id: row.id })
        this.formData = row
      },
    },
  }
</script>
