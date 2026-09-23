<template>
  <el-dialog
    title="编辑组织"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="组织名称" prop="orgName">
            <el-input v-model="form.orgName" placeholder="请输入组织名称"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织编码" prop="orgCode">
            <el-input v-model="form.orgCode" placeholder="请输入组织编码"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="组织类型" prop="orgType">
            <el-select v-model="form.orgType" placeholder="请选择组织类型">
              <el-option label="公司" value="company"></el-option>
              <el-option label="部门" value="department"></el-option>
              <el-option label="团队" value="team"></el-option>
              <el-option label="小组" value="group"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="上级组织" prop="parentOrgId">
            <el-cascader
              v-model="form.parentOrgId"
              :options="orgTreeOptions"
              :props="{ checkStrictly: true, value: 'id', label: 'name' }"
              placeholder="请选择上级组织"
              clearable
            ></el-cascader>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="负责人" prop="managerId">
            <el-select v-model="form.managerId" placeholder="请选择负责人" filterable>
              <el-option label="张三" value="1"></el-option>
              <el-option label="李四" value="2"></el-option>
              <el-option label="王五" value="3"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="成立时间" prop="establishDate">
            <el-date-picker
              v-model="form.establishDate"
              type="date"
              placeholder="选择成立时间"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱地址" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱地址"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="组织状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="暂停">暂停</el-radio>
          <el-radio label="筹建">筹建</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="组织描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="4"
          placeholder="请输入组织描述"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="职能范围">
        <el-checkbox-group v-model="form.functions">
          <el-checkbox label="research">研发</el-checkbox>
          <el-checkbox label="sales">销售</el-checkbox>
          <el-checkbox label="marketing">市场</el-checkbox>
          <el-checkbox label="finance">财务</el-checkbox>
          <el-checkbox label="hr">人力资源</el-checkbox>
          <el-checkbox label="admin">行政</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="权限设置">
        <el-transfer
          v-model="form.permissions"
          :data="permissionOptions"
          :titles="['可选权限', '已选权限']"
          :button-texts="['移除', '添加']"
          filterable
        ></el-transfer>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'OrganizationEditDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    orgData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        orgName: '',
        orgCode: '',
        orgType: '',
        parentOrgId: [],
        managerId: '',
        establishDate: '',
        phone: '',
        email: '',
        status: '正常',
        description: '',
        functions: [],
        permissions: []
      },
      rules: {
        orgName: [
          { required: true, message: '请输入组织名称', trigger: 'blur' }
        ],
        orgCode: [
          { required: true, message: '请输入组织编码', trigger: 'blur' }
        ],
        orgType: [
          { required: true, message: '请选择组织类型', trigger: 'change' }
        ],
        managerId: [
          { required: true, message: '请选择负责人', trigger: 'change' }
        ],
        phone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      },
      orgTreeOptions: [
        {
          id: '1',
          name: '示例云科技有限公司',
          children: [
            { id: '2', name: '技术部' },
            { id: '3', name: '市场部' },
            { id: '4', name: '财务部' }
          ]
        }
      ],
      permissionOptions: [
        { key: 'read', label: '查看权限' },
        { key: 'write', label: '编辑权限' },
        { key: 'delete', label: '删除权限' },
        { key: 'audit', label: '审核权限' },
        { key: 'admin', label: '管理权限' }
      ]
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      if (this.orgData && this.orgData.orgId) {
        // 编辑模式
        this.form = {
          orgName: this.orgData.orgName || '',
          orgCode: this.orgData.orgCode || '',
          orgType: this.orgData.orgType || '',
          parentOrgId: this.orgData.parentOrgId ? [this.orgData.parentOrgId] : [],
          managerId: this.orgData.managerId || '',
          establishDate: this.orgData.establishDate || '',
          phone: this.orgData.phone || '',
          email: this.orgData.email || '',
          status: this.orgData.status || '正常',
          description: this.orgData.description || '',
          functions: this.orgData.functions || [],
          permissions: this.orgData.permissions || []
        }
      } else {
        // 新增模式
        this.form = {
          orgName: '',
          orgCode: '',
          orgType: '',
          parentOrgId: [],
          managerId: '',
          establishDate: '',
          phone: '',
          email: '',
          status: '正常',
          description: '',
          functions: [],
          permissions: []
        }
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    handleConfirm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            await request({
              url: '/monitor/v1/enterprise/data/entry/organization/save',
              method: 'post',
              headers: { 'Content-Type': 'application/json;charset=UTF-8' },
              data: this.form
            })
            this.$message.success('组织信息保存成功')
            this.handleClose()
            this.$emit('refresh')
          } catch (error) {
            this.$message.error(error.message || '组织信息保存失败')
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>
