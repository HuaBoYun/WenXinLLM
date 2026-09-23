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
        label-width="150px"
        :model="form"
        :rules="rules"
      >
        <el-col :span="12">
          <el-form-item label="模型编号" prop="modelcode">
            <el-input
              v-model.trim="form.modelcode"
              placeholder="请输入模型编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型名称" prop="modelname">
            <el-input
              v-model.trim="form.modelname"
              placeholder="请输入模型名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型所属机构/部门" required>
            <el-input
              v-model.trim="form.orgname"
              placeholder="请输入模型所属机构/部门"
              readonly
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型分类" prop="modelcategory">
            <el-input
              v-model.trim="form.modelcategory"
              placeholder="请输入模型分类"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型状态" prop="modelstatus">
            <el-radio-group v-model="form.modelstatus">
              <el-radio label="启用" value="启用" />
              <el-radio label="禁用" value="禁用" />
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" required>
            <el-input v-model.trim="form.realname" readonly disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="提醒方式" prop="modelreminder">
            <el-select
              v-model="form.modelreminder"
              placeholder="请选择提醒方式"
              style="width: 100%"
            >
              <el-option
                v-for="item in modelreminder"
                :key="item.key"
                :label="item.key"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据连接字符串" prop="connectionstrings">
            <el-select
              v-model="form.connectionstrings"
              clearable
              placeholder="请选择数据连接字符串"
              style="width: 100%"
            >
              <el-option
                v-for="item in connectionstrings"
                :key="item.bookid"
                :label="item.bookname"
                :value="item.bookid"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="模型描述" prop="modeldes">
            <el-input
              v-model.trim="form.modeldes"
              maxlength="300"
              show-word-limit
              type="textarea"
              rows="3"
              placeholder="请输入模型描述"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>模型步骤列表</el-divider>
        </el-col>
        <el-col :span="24">
          <el-form-item label="步骤1" prop="modelstep1">
            <el-input v-model.trim="form.modelstep1" type="textarea" rows="5" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="步骤2" prop="modelstep2">
            <el-input v-model.trim="form.modelstep2" type="textarea" rows="5" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="步骤3" prop="modelstep3">
            <el-input v-model.trim="form.modelstep3" type="textarea" rows="5" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="步骤4" prop="modelstep4">
            <el-input v-model.trim="form.modelstep4" type="textarea" rows="5" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="步骤5" prop="modelstep5">
            <el-input v-model.trim="form.modelstep5" type="textarea" rows="5" />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" :loading="loading" @click="save">
        确 定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { createModelManage, editModelManage } from '@/api/monitor/model'

  export default {
    name: 'ModelEdit',
    data() {
      return {
        form: {
          modelcode: '',
          modelname: '',
          modelstatus: '启用',
          modelcategory: '',
          orgid: '',
          staffid: '',
          realname: '',
          modelreminder: '',
          connectionstrings: '',
          modeldes: '',
          modelstep1: '',
          modelstep2: '',
          modelstep3: '',
          modelstep4: '',
          modelstep5: '',
        },
        rules: {
          modelcode: [
            { required: true, trigger: 'blur', message: '请输入编号' },
          ],
          modelname: [
            { required: true, trigger: 'blur', message: '请输入名称' },
          ],
          modelcategory: [
            { required: true, trigger: 'blur', message: '请输入分类' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        connectionstrings: [],
        modelreminder: [
          {
            key: '短信',
            value: '短信',
          },
          {
            key: '邮件',
            value: '邮件',
          },
          {
            key: '短信+邮件',
            value: '短信+邮件',
          },
        ],
        loading: false,
      }
    },
    created() {},
    methods: {
      showEdit(type, data) {
        if (type === 'add') {
          this.title = '新建'
          const {
            books,
            organization: { orgid, orgmeno },
            user: { staffid, realname },
          } = data
          this.connectionstrings = books
          this.form.realname = realname
          this.form.staffid = staffid
          this.form.orgid = orgid
          this.form.orgname = orgmeno
        } else if (type === 'edit') {
          this.title = '编辑'
          const {
            books,
            organization: { orgmeno },
            user: { realname },
            ...row
          } = data
          this.connectionstrings = books
          this.form = {
            ...row,
            realname,
            orgname: orgmeno,
          }
        }
        this.dialogFormVisible = true
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
            const { realname, orgname, ...data } = this.form
            if (this.title === '新建') {
              const { code, msg } = await createModelManage(data)
              if (code === 200) {
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                this.$emit('fetch-data')
              }
              this.close()
            } else if (this.title === '编辑') {
              const { code, msg } = await editModelManage(data)
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
