<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="close"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input
              v-model="form.enterpriseName"
              placeholder="请输入企业名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="统一社会信用代码" prop="creditCode">
            <el-input
              v-model="form.creditCode"
              placeholder="请输入统一社会信用代码"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业类型" prop="enterpriseType">
            <el-select
              v-model="form.enterpriseType"
              placeholder="请选择企业类型"
              style="width: 100%"
            >
              <el-option
                v-for="item in enterpriseTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="注册资本(万元)" prop="registeredCapital">
            <el-input-number
              v-model="form.registeredCapital"
              :min="0"
              :precision="2"
              style="width: 100%"
              placeholder="请输入注册资本"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="法定代表人" prop="legalRepresentative">
            <el-input
              v-model="form.legalRepresentative"
              placeholder="请输入法定代表人"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="成立日期" prop="establishDate">
            <el-date-picker
              v-model="form.establishDate"
              type="date"
              placeholder="请选择成立日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="监管层级" prop="supervisionLevel">
            <el-select
              v-model="form.supervisionLevel"
              placeholder="请选择监管层级"
              style="width: 100%"
            >
              <el-option
                v-for="item in supervisionLevelOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="母公司" prop="parentEnterpriseId">
            <el-select
              v-model="form.parentEnterpriseId"
              placeholder="请选择母公司"
              style="width: 100%"
              filterable
              clearable
            >
              <el-option
                v-for="item in parentEnterpriseOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业状态" prop="enterpriseStatus">
            <el-select
              v-model="form.enterpriseStatus"
              placeholder="请选择企业状态"
              style="width: 100%"
            >
              <el-option
                v-for="item in enterpriseStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="上市状态" prop="listingStatus">
            <el-select
              v-model="form.listingStatus"
              placeholder="请选择上市状态"
              style="width: 100%"
            >
              <el-option
                v-for="item in listingStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="form.listingStatus === 'LISTED'">
        <el-col :span="12">
          <el-form-item label="股票代码" prop="stockCode">
            <el-input
              v-model="form.stockCode"
              placeholder="请输入股票代码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="地区代码" prop="regionCode">
            <el-input
              v-model="form.regionCode"
              placeholder="请输入地区代码"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系人" prop="contactPerson">
            <el-input
              v-model="form.contactPerson"
              placeholder="请输入联系人"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input
              v-model="form.contactPhone"
              placeholder="请输入联系电话"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系邮箱" prop="contactEmail">
            <el-input
              v-model="form.contactEmail"
              placeholder="请输入联系邮箱"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="行业分类代码" prop="industryCode">
            <el-input
              v-model="form.industryCode"
              placeholder="请输入行业分类代码"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="注册地址" prop="registeredAddress">
        <el-input
          v-model="form.registeredAddress"
          type="textarea"
          :rows="2"
          placeholder="请输入注册地址"
        />
      </el-form-item>

      <el-form-item label="经营范围" prop="businessScope">
        <el-input
          v-model="form.businessScope"
          type="textarea"
          :rows="3"
          placeholder="请输入经营范围"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save" :loading="loading">
        {{ loading ? '保存中...' : '保存' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addEnterprise, updateEnterprise, getParentEnterpriseList } from '@/api/stateAssets/enterprise'

export default {
  name: 'EnterpriseForm',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      title: '',
      isEdit: false,
      form: {
        enterpriseId: '',
        enterpriseName: '',
        creditCode: '',
        enterpriseType: '',
        registeredCapital: null,
        establishDate: '',
        legalRepresentative: '',
        registeredAddress: '',
        businessScope: '',
        industryCode: '',
        supervisionLevel: '',
        parentEnterpriseId: '',
        enterpriseStatus: 'NORMAL',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        regionCode: '',
        listingStatus: 'UNLISTED',
        stockCode: '',
      },
      rules: {
        enterpriseName: [
          { required: true, message: '请输入企业名称', trigger: 'blur' },
          { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' },
        ],
        creditCode: [
          { required: true, message: '请输入统一社会信用代码', trigger: 'blur' },
          { pattern: /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/, message: '请输入正确的统一社会信用代码', trigger: 'blur' },
        ],
        enterpriseType: [
          { required: true, message: '请选择企业类型', trigger: 'change' },
        ],
        legalRepresentative: [
          { required: true, message: '请输入法定代表人', trigger: 'blur' },
        ],
        supervisionLevel: [
          { required: true, message: '请选择监管层级', trigger: 'change' },
        ],
        enterpriseStatus: [
          { required: true, message: '请选择企业状态', trigger: 'change' },
        ],
        contactEmail: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
        ],
        contactPhone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' },
        ],
      },
      enterpriseTypeOptions: [
        { label: '国有独资', value: 'STATE_OWNED' },
        { label: '国有控股', value: 'STATE_HOLDING' },
        { label: '国有参股', value: 'STATE_PARTICIPATING' },
      ],
      supervisionLevelOptions: [
        { label: '中央', value: 'CENTRAL' },
        { label: '地方', value: 'LOCAL' },
      ],
      enterpriseStatusOptions: [
        { label: '正常', value: 'NORMAL' },
        { label: '注销', value: 'CANCELLED' },
        { label: '合并', value: 'MERGED' },
        { label: '暂停', value: 'SUSPENDED' },
      ],
      listingStatusOptions: [
        { label: '已上市', value: 'LISTED' },
        { label: '未上市', value: 'UNLISTED' },
      ],
      parentEnterpriseOptions: [],
    }
  },
  methods: {
    show(row) {
      this.dialogVisible = true
      this.isEdit = !!row
      this.title = this.isEdit ? '编辑企业' : '新增企业'
      
      if (this.isEdit) {
        this.form = { ...row }
      } else {
        this.resetForm()
      }
      
      this.fetchParentEnterpriseList()
    },
    close() {
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.form = {
        enterpriseId: '',
        enterpriseName: '',
        creditCode: '',
        enterpriseType: '',
        registeredCapital: null,
        establishDate: '',
        legalRepresentative: '',
        registeredAddress: '',
        businessScope: '',
        industryCode: '',
        supervisionLevel: '',
        parentEnterpriseId: '',
        enterpriseStatus: 'NORMAL',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        regionCode: '',
        listingStatus: 'UNLISTED',
        stockCode: '',
      }
      this.$nextTick(() => {
        this.$refs.form?.clearValidate()
      })
    },
    async fetchParentEnterpriseList() {
      try {
        const { data } = await getParentEnterpriseList()
        this.parentEnterpriseOptions = data.map(item => ({
          label: item.enterpriseName,
          value: item.enterpriseId,
        }))
      } catch (error) {
        console.error('获取母公司列表失败:', error)
      }
    },
    async save() {
      try {
        await this.$refs.form.validate()
        this.loading = true
        
        if (this.isEdit) {
          await updateEnterprise(this.form)
          this.$baseMessage('更新成功', 'success')
        } else {
          await addEnterprise(this.form)
          this.$baseMessage('新增成功', 'success')
        }
        
        this.close()
        this.$emit('refresh')
      } catch (error) {
        if (error !== false) {
          this.$baseMessage(this.isEdit ? '更新失败' : '新增失败', 'error')
        }
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
