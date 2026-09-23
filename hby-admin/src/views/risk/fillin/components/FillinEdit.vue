<template>
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
        label-width="125px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="风险编号" prop="field101">
            <el-input
              v-model="formData.field101"
              clearable
              placeholder="请输入风险编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位名称" prop="field102">
            <el-input
              v-model="formData.field102"
              clearable
              placeholder="请输入单位名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="一类风险" prop="field103">
            <el-select
              v-model="formData.name"
              clearable
              placeholder="请选择一类风险"
            >
              <el-option
                v-for="item in firstRisks"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="二类风险" prop="field104">
            <el-select
              v-model="formData.name"
              clearable
              placeholder="请选择二类风险"
            >
              <el-option
                v-for="item in secondRisks"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="三类风险" prop="field105">
            <el-select
              v-model="formData.name"
              clearable
              placeholder="请选择三类风险"
            >
              <el-option
                v-for="item in thirdRisks"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉及资金（万元）" prop="field106">
            <el-input
              v-model="formData.field106"
              clearable
              placeholder="请输入涉及资金"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="风险来源" prop="field106">
            <el-input
              v-model="formData.field106"
              :autosize="{ minRows: 4, maxRows: 8 }"
              placeholder="请输入风险来源"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="风险描述" prop="field106">
            <el-input
              v-model="formData.field106"
              :autosize="{ minRows: 4, maxRows: 8 }"
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" prop="field106">
            <el-input
              v-model="formData.field106"
              :autosize="{ minRows: 4, maxRows: 8 }"
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
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

    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'
  import { firstRisks, secondRisks, thirdRisks } from '../riskOptions'

  export default {
    name: 'ProjectEdit',
    data() {
      return {
        firstRisks,
        secondRisks,
        thirdRisks,
        formData: {
          field101: undefined,
          field102: undefined,
          field103: undefined,
          field104: null,
          field105: undefined,
          field106: undefined,
          field107: undefined,
          field108: undefined,
          field109: undefined,
        },
        rules: {
          field101: [
            {
              required: true,
              message: '请输入风险编号',
              trigger: 'blur',
            },
          ],
          field102: [],
          field103: [],
          field104: [],
          field105: [],
          field106: [],
          field107: [],
          field108: [],
          field109: [],
        },
        title: '',
        dialogFormVisible: false,
        tableData: [{ name: 'XXXXX' }, { name: 'XXXXX' }, { name: 'XXXXX' }],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
          this.form.org = '长江集团有限公司'
          this.form.code = 'XXXXXXXXXX'
          this.form.name = 'XXXXXXXXXX'
        }
        this.dialogFormVisible = true
      },
      close() {
        // this.$refs['form'].resetFields()
        // this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      submitForm() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return
          // TODO 提交表单
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
      },
    },
  }
</script>
<style scoped></style>
