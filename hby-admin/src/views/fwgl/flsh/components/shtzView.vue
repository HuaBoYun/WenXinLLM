<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="编号" prop="entername">
            <el-input
              v-model="formData.entername"
              clearable
              placeholder="请输入编号"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类型 " prop="entername">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.type"
              placeholder="类型"
              :disabled="!footer"
            >
              <el-option label="审核事项台账" value="审核事项台账" />
              <el-option label="法律意见书台账" value="法律意见书台账" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.type == '审核事项台账'">
          <el-form-item label="审核事项" prop="entername">
            <el-input
              v-model="formData.entername"
              clearable
              placeholder="请输入审核事项台账名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.type == '法律意见书台账'">
          <el-form-item label="法律意见书" prop="entername">
            <el-input
              v-model="formData.entername"
              clearable
              placeholder="请输入法律意见书台账名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起草部门" prop="entername">
            <el-input
              v-model="formData.entername"
              clearable
              placeholder="请选择起草部门"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="起草时间" prop="entername">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.createTime"
              placeholder="起草时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="creatorName">
            <el-input
              v-model="formData.creatorName"
              clearable
              placeholder="请选择创建人"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="创建时间" prop="entername">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.createTime"
              placeholder="创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'

  export default {
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          entercoed: undefined,
          entername: undefined,
          content: undefined,
          type: '审核事项台账',
        },
        footer: true,
        rules: {
          entercoed: [
            {
              required: true,
              message: '请输入进场纪要编号',
              trigger: 'blur',
            },
          ],
          entername: [
            {
              required: true,
              message: '请输入进场纪要名称',
              trigger: 'blur',
            },
          ],
          content: [
            {
              required: true,
              message: '请输入编辑器',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.formData = row.entermeeting
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {
          type: '审核事项台账',
        }
        this.dialogFormVisible = false

        this.footer = true
      },
      add() {},
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */      
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
