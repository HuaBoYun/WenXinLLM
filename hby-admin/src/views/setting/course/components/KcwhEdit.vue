<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="100px" :model="formData" :rules="rules">
      <el-form-item label="系列课名称" prop="coursename">
        <el-input
          v-model="formData.coursename"
          clearable
          placeholder="请输入系列课名称"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="排课计划" prop="coursenumber">
        <el-input
          v-model="formData.coursenumber"
          clearable
          placeholder="请输入排课计划"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="系列课分类" prop="coursetype">
        <el-select
          v-model="formData.coursetype"
          clearable
          placeholder="请选择系列课分类"
          :style="{ width: '100%' }"
        >
          <el-option
            v-for="(item, index) in typeOptions"
            :key="index"
            :disabled="item.disabled"
            :label="item.label"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="系列课海报" prop="picurl">
        <file-upload
          v-model="formData.picurl"
          accept=".jpg,.png"
          api="/setting/upload"
          list-type="picture-card"
          :show-file-list="false"
        >
          <img v-if="formData.picurl" class="avatar" :src="formData.picurl" />
          <i v-else class="el-icon-plus"></i>
        </file-upload>
      </el-form-item>
      <el-form-item label="系列课介绍" prop="memo">
        <el-input
          v-model="formData.memo"
          :autosize="{ minRows: 4, maxRows: 4 }"
          placeholder="请输入系列课介绍"
          :style="{ width: '100%' }"
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getCourseTypeList, saveCourse } from '@/api/setting/course'
  import FileUpload from '@/components/FileUpload.vue'

  export default {
    name: 'KcwhEdit',
    components: { FileUpload },
    data: function () {
      return {
        formData: {
          courseid: undefined,
          coursename: undefined,
          coursenumber: undefined,
          coursetype: undefined,
          picurl: null,
          memo: undefined,
        },
        rules: {
          coursename: [
            {
              required: true,
              message: '请输入系列课名称',
              trigger: 'blur',
            },
          ],
          coursenumber: [
            {
              required: true,
              message: '请输入排课计划',
              trigger: 'blur',
            },
          ],
          coursetype: [
            {
              required: true,
              message: '请选择系列课分类',
              trigger: 'change',
            },
          ],
          picurl: [
            {
              required: true,
              message: '请上传系列课海报',
              trigger: 'blur',
            },
          ],
          memo: [
            {
              required: true,
              message: '请输入系列课介绍',
              trigger: 'blur',
            },
          ],
        },
        typeOptions: [],
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {
      this.fetchTypeList()
    },
    methods: {
      resetForm() {
        this.formData = {
          courseid: undefined,
          coursename: undefined,
          coursenumber: undefined,
          coursetype: undefined,
          picurl: undefined,
          memo: undefined,
        }
      },
      showEdit(row) {
        this.resetForm()
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
        this.formData.picurl = undefined
        this.dialogFormVisible = false
      },
      async fetchTypeList() {
        this.listLoading = true
        const {
          data: { tlist },
        } = await getCourseTypeList(this.queryForm)
        this.typeOptions = tlist.map((item) => {
          return {
            label: item.typename,
            value: item.typeId,
          }
        })
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await saveCourse(this.formData)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
<style>
  .el-upload__tip {
    line-height: 1.2;
  }
</style>
