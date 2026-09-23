<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="120px"
          :model="formData"
          size="medium"
        >
          <el-col :span="12">
            <el-form-item label="报告名称" prop="field101">
              {{ formData.field101 }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告时间" prop="field102">
              {{ formData.field102 }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告类型" prop="field103">
              {{ formData.field103 }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告方式" prop="field104">
              {{ formData.field104 }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告人" prop="field105">
              {{ formData.field105 }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告部门" prop="field106">
              {{ formData.field106 }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内容" prop="content">
              {{ formData.content }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>附件</el-divider>
          </el-col>
          <el-col :span="24">
            <el-table :data="tableData">
              <el-table-column align="center" label="附件名称" prop="name" />
              <el-table-column
                align="center"
                label="文件大小(KB)"
                prop="name"
              />
              <el-table-column align="center" label="创建人" prop="name" />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleEdit2(row)">
                    下载
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    name: 'NormalEdit',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        formData: {
          field101: undefined,
          field102: undefined,
          field103: null,
          field104: null,
          field105: undefined,
          field106: undefined,
          content: undefined,
        },
        list: [],
        tableData: [],
        field103Options: [
          {
            label: '对内报告',
            value: 1,
          },
          {
            label: '对外报告',
            value: 2,
          },
        ],
        field104Options: [
          {
            label: '定期报告',
            value: 1,
          },
          {
            label: '非定期报告',
            value: 2,
          },
        ],
        field105Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field106Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        options: {
          theme: 'snow',
          bounds: document.body,
          debug: 'warn',
          placeholder: '内容...',
          readOnly: false,
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      save() {},
      showRead(row) {
        this.title = '编辑'
        this.formData = Object.assign({}, row)
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style></style>
