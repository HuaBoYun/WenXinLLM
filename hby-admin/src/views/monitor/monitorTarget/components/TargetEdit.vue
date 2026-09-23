<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <el-row :gutter="15">
      <el-form
        :rules="rules"
        :model="formData"
        ref="elForm"
        label-width="125px"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="方案编号" prop="solutioncode">
            <el-input
              type="text"
              placeholder="请输入方案编号"
              v-model="formData.solutioncode"
              :disabled="!footer"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案名称" prop="solutionname">
            <el-input
              type="text"
              placeholder="请输入方案名称"
              v-model="formData.solutionname"
              :disabled="!footer"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案状态">
            <el-select v-model="formData.solutionstatus">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="user">
            <el-input
              type="text"
              placeholder="请输入方案名称"
              v-model="formData.user"
              :disabled="true"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input
              type="textarea"
              :rows="4"
              maxlength="300"
              show-word-limit
              v-model="formData.memo"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12"><p>指标</p></el-col>
        <el-col :span="12" :push="9">
          <el-button type="primary" @click="addIndex">添加指标</el-button>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData">
            <el-table-column align="center" label="指标编号" prop="name" />
            <el-table-column align="center" label="指标名称" prop="name" />
            <el-table-column align="center" label="操作" prop="name" />
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
  import { getErrorIndexAdd, SolutionSave } from '@/api/monitor/watch'
  export default {
    name: 'TargetEdit',
    data() {
      return {
        formData: {
          solutioncode: '',
          solutionname: '',
          solutionstatus: '1',
          memo: '',
          orgId: '',
          staffid: '',
          user: '',
        },
        rules: {
          solutioncode: [
            { required: true, trigger: 'blur', message: '请输入方案编号' },
          ],
          solutionname: [
            { required: true, trigger: 'blur', message: '请输入方案名称' },
          ],
          user: [{ required: true, trigger: 'blur', message: '请输入创建人' }],
        },
        title: '',
        dialogFormVisible: false,
        tableData: [],
        options: [
          {
            value: '1',
            label: '启用',
          },
          {
            value: '2',
            label: '停用',
          },
        ],
        footer: true,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
          this.formData.staffid = JSON.parse(
            localStorage.getItem('userInfo')
          ).staffid
          this.formData.user = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
        } else {
          this.title = '编辑'
          this.formData = row
        }
        this.dialogFormVisible = true
      },
      async addIndex() {
        const result = await getErrorIndexAdd({
          orgId: '',
          pageNumber: '',
          pageSize: '',
          solutionid: '',
        })
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const { msg } = await SolutionSave(this.form)

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
  .form-edit .el-cascader {
    width: 100%;
  }
  .formula-view {
    margin-bottom: 10px;
  }
  .formula-view > * {
    margin-right: 10px;
  }
</style>
