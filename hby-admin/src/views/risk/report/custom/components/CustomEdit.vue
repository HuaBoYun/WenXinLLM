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
          :rules="rules"
          size="medium"
        >
          <el-col :span="12">
            <el-form-item label="报告名称" prop="field101">
              <el-input
                v-model="formData.field101"
                clearable
                placeholder="请输入评估计划编号"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告时间" prop="field102">
              <el-date-picker
                v-model="formData.field102"
                clearable
                format="yyyy-MM-dd"
                placeholder="请选择报告时间"
                :style="{ width: '100%' }"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告类型" prop="field103">
              <el-select
                v-model="formData.field103"
                clearable
                placeholder="请选择报告类型"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in field103Options"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告方式" prop="field104">
              <el-select
                v-model="formData.field104"
                clearable
                placeholder="请选择报告方式"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in field104Options"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告人" prop="field105">
              <el-select
                v-model="formData.field105"
                clearable
                placeholder="请选择报告人"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in field105Options"
                  :key="index"
                  :disabled="item.disabled"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告部门" prop="field106">
              <el-select
                v-model="formData.field106"
                clearable
                placeholder="请选择报告部门"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in field106Options"
                  :key="index"
                  :disabled="item.disabled"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内容" prop="content">
              <tinymce
                v-model="formData.field107"
                :height="300"
                placeholder="请输入编辑器"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>附件</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
              <el-button type="success">上传</el-button>
            </div>
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
                  <el-button type="text" @click="handleEdit2(row)">
                    删除
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
    <vab-upload
      ref="vabUpload"
      :limit="50"
      name="file"
      :size="2"
      url="/upload"
    />
  </div>
</template>
<script>
  import Tinymce from '@/components/Tinymce'
  import VabUpload from '@/extra/VabUpload'

  export default {
    name: 'NormalEdit',
    components: { Tinymce, VabUpload },
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
        rules: {
          field101: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
          field102: [
            {
              required: true,
              message: '请选择报告时间',
              trigger: 'blur',
            },
          ],
          field103: [
            {
              required: true,
              message: '请选择报告类型',
              trigger: 'change',
            },
          ],
          field104: [
            {
              required: true,
              message: '请选择报告方式',
              trigger: 'change',
            },
          ],
          field105: [
            {
              required: true,
              message: '请选择报告人',
              trigger: 'change',
            },
          ],
          field106: [
            {
              required: true,
              message: '请选择报告部门',
              trigger: 'change',
            },
          ],
        },
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
          modules: {
            toolbar: {
              container: [
                ['bold', 'italic', 'underline', 'strike'],
                [{ header: [1, 2, 3, 4, 5, 6, false] }],
                [{ size: ['small', false, 'large', 'huge'] }],
                [{ color: [] }, { background: [] }],
                ['blockquote', 'code-block'],
                [{ list: 'ordered' }, { list: 'bullet' }],
                [{ script: 'sub' }, { script: 'super' }],
                [{ indent: '-1' }, { indent: '+1' }],
                [{ align: [] }],
                [{ direction: 'rtl' }],
                [{ font: [] }],
                ['clean'],
                ['link', 'image', 'vab-upload-image'],
              ],
              handlers: {
                'vab-upload-image': () => {
                  this.$baseConfirm(
                    '演示环境未使用真实文件服务器，故图片上传回显不会生效，开发时请修改为正式文件服务器地址',
                    '开发注意事项！！！',
                    () => {
                      this.$refs['vabUpload'].handleShow()
                    },
                    () => {
                      this.handleAddImg()
                    },
                    '模拟打开文件上传',
                    '模拟添加一张文件服务器图片'
                  )
                },
              },
            },
          },
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
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.formData = Object.assign({}, row)
        }
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style></style>
