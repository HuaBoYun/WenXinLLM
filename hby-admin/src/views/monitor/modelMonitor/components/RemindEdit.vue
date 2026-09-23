<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    destroy-on-close
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="form"
        class="form-edit"
        label-width="100px"
        :model="form"
        :rules="rules"
      >
        <el-col :span="12">
          <el-form-item label="方案编号" prop="solutioncode">
            <el-input
              v-model.trim="form.solutioncode"
              placeholder="请输入方案编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案名称" prop="solutionname">
            <el-input
              v-model.trim="form.solutionname"
              placeholder="请输入方案名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案状态" prop="solutionstatus">
            <el-radio-group v-model="form.solutionstatus">
              <el-radio label="启用" value="启用" />
              <el-radio label="停用" value="停用" />
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" required>
            <el-input v-model.trim="form.realname" readonly disabled />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="memo">
            <el-input
              v-model.trim="form.memo"
              maxlength="300"
              show-word-limit
              type="textarea"
              rows="3"
              placeholder="请输入备注"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-card shadow="never" :body-style="{ padding: 0 }">
      <div slot="header" class="clearfix">
        <span>模型列表</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="openModel"
        >
          添加模型
        </el-button>
      </div>
      <el-table :data="form.models">
        <el-table-column label="ID" prop="modelid"></el-table-column>
        <el-table-column label="模型编号" prop="modelcode"></el-table-column>
        <el-table-column label="模型名称" prop="modelname"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ $index }">
            <span
              style="color: red; cursor: pointer"
              @click="removeModel($index)"
            >
              删除
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" :loading="loading" @click="save">
        确 定
      </el-button>
    </template>
    <model-list ref="modelList" />
  </el-dialog>
</template>

<script>
  import {
    createModelRemind,
    editModelRemind,
    getModelRemindDetail,
    getModelList,
  } from '@/api/monitor/model/remind'
  import ModelList from './ModelList.vue'

  export default {
    name: 'RemindEdit',
    components: { ModelList },
    data() {
      return {
        form: {
          solutioncode: '',
          solutionname: '',
          solutionstatus: '启用',
          memo: '',
          orgid: '',
          staffid: '',
          realname: '',
          exefrequncy: '',
          memo: '',
          runstatus: 0,
          solutionid: 0,
          type: 0,
        },
        rules: {
          solutioncode: [
            { required: true, trigger: 'blur', message: '请输入编号' },
          ],
          solutionname: [
            { required: true, trigger: 'blur', message: '请输入名称' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        loading: false,
        modelPagination: {
          pageNumber: 1,
          limit: 20,
        },
      }
    },
    created() {},
    methods: {
      async showEdit(type, data) {
        if (type === 'add') {
          this.title = '新建'
          const {
            orgid,
            user: { staffid, realname },
          } = data
          this.form.realname = realname
          this.form.staffid = staffid
          this.form.orgid = orgid
        } else if (type === 'edit') {
          this.title = '编辑'
          const {
            user: { realname },
          } = data
          await this.getModelDetail(data.solutionid)
          this.form.realname = realname
        }
        this.dialogFormVisible = true
      },
      async getModelDetail(infoid) {
        const { code, msg, data } = await getModelRemindDetail({ infoid })
        if (code === 200 && msg === '成功') {
          const { models, solution } = data
          this.form = {
            ...solution,
            models,
          }
        }
      },
      removeModel() {},
      openModel() {
        this.$refs['modelList'].show('', this.form)
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
        this.loading = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            if (this.title === '新建') {
              const { solutionid, ...data } = this.form
              const { code, msg } = await createModelRemind(data)
              if (code === 200) {
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                this.$emit('fetch-data')
              }
              this.close()
            } else if (this.title === '编辑') {
              const { code, msg } = await editModelRemind(this.form)
              if (code === 200) {
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                this.$emit('fetch-data')
              }
              this.close()
            }
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
