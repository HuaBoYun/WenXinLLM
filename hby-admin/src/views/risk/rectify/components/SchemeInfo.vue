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
        ref="elForm"
        label-width="110px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="方案编号" prop="field101">
            <el-input
              v-model="formData.field102"
              clearable
              placeholder="请输入方案编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案名称" prop="field102">
            <el-input
              v-model="formData.field102"
              clearable
              placeholder="请输入方案名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联审计项目" prop="field107">
            <el-input
              v-model="formData.field107"
              clearable
              placeholder="请输入关联审计项目"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="field108">
            <el-input
              v-model="formData.field108"
              clearable
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="截止时间" prop="field108">
            <el-date-picker
              v-model="formData.curStartYear"
              :default-value="formData.defaultValue"
              format="yyyy"
              :picker-options="formData.startPickerOptions"
              :style="{ width: '100%' }"
              type="year"
              value-format="yyyy"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="field132">
            <el-input
              v-model="formData.field132"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>整改内容</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success">新建</el-button>
          </div>
          <el-table>
            <el-table-column align="center" label="问题编号" prop="name" />
            <el-table-column align="center" label="问题详情" prop="name" />
            <el-table-column align="center" label="问题来源" prop="name" />
            <el-table-column align="center" label="发现人" prop="name" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit2(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success">上传</el-button>
          </div>
          <el-table>
            <el-table-column align="center" label="附件名称" prop="name" />
            <el-table-column align="center" label="文件大小(KB)" prop="name" />
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
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary">确定</el-button>
    </div>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  export default {
    name: 'FlawInfo',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          field101: undefined,
          field102: undefined,
          field107: undefined,
          field108: null,
        },
        rules: {
          field101: [
            {
              required: true,
              message: '请输入资料编号',
              trigger: 'blur',
            },
          ],
          field102: [
            {
              required: true,
              message: '请输入资料名称',
              trigger: 'blur',
            },
          ],
          field107: [
            {
              required: true,
              message: '请输入所属项目',
              trigger: 'blur',
            },
          ],
          field108: [
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
      showEdit() {
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
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
