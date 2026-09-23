<template>
  <div class="partner-archive-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-user"></i>
            合作伙伴档案管理
          </h2>
          <p class="page-description">
            管理合作伙伴基础档案信息，包括银行、金融机构、供应商等合作方信息
          </p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增合作伙伴
          </el-button>
          <el-button
            type="success"
            icon="el-icon-upload2"
            @click="handleImport"
          >
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出档案
          </el-button>
        </div>
      </div>
    </div>

    <!-- 合作伙伴统计卡片 -->
    <div class="partner-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总合作伙伴</div>
                <div class="card-value">{{ totalPartners }}</div>
                <div class="card-change">已建档案</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon bank-icon">
                <i class="el-icon-office-building"></i>
              </div>
              <div class="card-info">
                <div class="card-title">银行机构</div>
                <div class="card-value">{{ bankPartners }}</div>
                <div class="card-change positive">金融合作</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon supplier-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-info">
                <div class="card-title">供应商</div>
                <div class="card-value">{{ supplierPartners }}</div>
                <div class="card-change">业务合作</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">活跃合作</div>
                <div class="card-value">{{ activePartners }}</div>
                <div class="card-change positive">本月活跃</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="伙伴编码">
            <el-input
              v-model="listQuery.partnerCode"
              placeholder="请输入伙伴编码"
              style="width: 150px"
              clearable
            />
          </el-form-item>
          <el-form-item label="伙伴名称">
            <el-input
              v-model="listQuery.partnerName"
              placeholder="请输入伙伴名称"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="伙伴类型">
            <el-select
              v-model="listQuery.partnerTypeId"
              placeholder="请选择伙伴类型"
              clearable
              style="width: 150px"
            >
              <el-option
                v-for="type in partnerTypes"
                :key="type.partnerTypeId"
                :label="type.typeName"
                :value="type.partnerTypeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="统一信用代码">
            <el-input
              v-model="listQuery.unifiedCreditCode"
              placeholder="请输入统一信用代码"
              style="width: 180px"
              clearable
            />
          </el-form-item>
          <el-form-item label="伙伴状态">
            <el-select
              v-model="listQuery.partnerStatus"
              placeholder="请选择伙伴状态"
              clearable
              style="width: 120px"
            >
              <el-option label="活跃" value="ACTIVE" />
              <el-option label="非活跃" value="INACTIVE" />
              <el-option label="暂停" value="SUSPENDED" />
              <el-option label="黑名单" value="BLACKLISTED" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              @click="handleFilter"
            >
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column
        label="伙伴编码"
        prop="partnerCode"
        sortable="custom"
        align="center"
        width="120"
      >
        <template slot-scope="{ row }">
          <span>{{ row.partnerCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="伙伴名称" width="200px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.partnerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="英文名称" width="180px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.partnerNameEng }}</span>
        </template>
      </el-table-column>
      <el-table-column label="伙伴类型" width="120px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.partnerTypeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="统一信用代码" width="180px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.unifiedCreditCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="法定代表人" width="120px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.legalPerson }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联系人" width="100px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.contactPerson }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联系电话" width="120px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.contactPhone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="信用评级" width="100px" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="getCreditRatingColor(row.creditRating)">
            {{ row.creditRating }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" width="100px" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="getRiskLevelColor(row.riskLevel)">
            {{ getRiskLevelName(row.riskLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="伙伴状态" class-name="status-col" width="100">
        <template slot-scope="{ row }">
          <el-tag :type="getPartnerStatusColor(row.partnerStatus)">
            {{ getPartnerStatusName(row.partnerStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="230"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{ row, $index }">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
            v-if="row.status != 'deleted'"
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.pageNo"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="partnerForm"
        :rules="rules"
        :model="tempPartner"
        :label-width="formLabelWidth"
      >
        <el-tabs v-model="activeTab">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="伙伴编码" prop="partnerCode">
                  <el-input
                    v-model="tempPartner.partnerCode"
                    placeholder="请输入伙伴编码"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="伙伴名称" prop="partnerName">
                  <el-input
                    v-model="tempPartner.partnerName"
                    placeholder="请输入伙伴名称"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="英文名称">
                  <el-input
                    v-model="tempPartner.partnerNameEng"
                    placeholder="请输入英文名称"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="伙伴类型" prop="partnerTypeId">
                  <el-select
                    v-model="tempPartner.partnerTypeId"
                    placeholder="请选择伙伴类型"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="type in partnerTypes"
                      :key="type.partnerTypeId"
                      :label="type.typeName"
                      :value="type.partnerTypeId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="统一信用代码" prop="unifiedCreditCode">
                  <el-input
                    v-model="tempPartner.unifiedCreditCode"
                    placeholder="请输入统一信用代码"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="税号">
                  <el-input
                    v-model="tempPartner.taxNumber"
                    placeholder="请输入税号"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="法定代表人">
                  <el-input
                    v-model="tempPartner.legalPerson"
                    placeholder="请输入法定代表人"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="法人身份证">
                  <el-input
                    v-model="tempPartner.legalPersonIdCard"
                    placeholder="请输入法人身份证"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="注册资本">
                  <el-input-number
                    v-model="tempPartner.registeredCapital"
                    :precision="2"
                    :step="10000"
                    style="width: 100%"
                    placeholder="请输入注册资本"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="成立日期">
                  <el-date-picker
                    v-model="tempPartner.establishDate"
                    type="date"
                    placeholder="选择成立日期"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="经营范围">
              <el-input
                v-model="tempPartner.businessScope"
                type="textarea"
                :rows="2"
                placeholder="请输入经营范围"
              />
            </el-form-item>
          </el-tab-pane>

          <!-- 联系信息 -->
          <el-tab-pane label="联系信息" name="contact">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="联系人">
                  <el-input
                    v-model="tempPartner.contactPerson"
                    placeholder="请输入联系人"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="contactPhone">
                  <el-input
                    v-model="tempPartner.contactPhone"
                    placeholder="请输入联系电话"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="邮箱" prop="contactEmail">
                  <el-input
                    v-model="tempPartner.contactEmail"
                    placeholder="请输入邮箱"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="传真">
                  <el-input
                    v-model="tempPartner.contactFax"
                    placeholder="请输入传真"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="地址">
              <el-input
                v-model="tempPartner.address"
                placeholder="请输入地址"
              />
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="国家">
                  <el-select
                    v-model="tempPartner.countryCode"
                    placeholder="请选择国家"
                    style="width: 100%"
                  >
                    <el-option label="中国" value="CN" />
                    <el-option label="美国" value="US" />
                    <el-option label="英国" value="GB" />
                    <el-option label="日本" value="JP" />
                    <el-option label="德国" value="DE" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="省份">
                  <el-input
                    v-model="tempPartner.province"
                    placeholder="请输入省份"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="城市">
                  <el-input
                    v-model="tempPartner.city"
                    placeholder="请输入城市"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="邮编">
                  <el-input
                    v-model="tempPartner.postalCode"
                    placeholder="请输入邮编"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="网站">
                  <el-input
                    v-model="tempPartner.website"
                    placeholder="请输入网站"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <!-- 风险信息 -->
          <el-tab-pane label="风险信息" name="risk">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="信用评级">
                  <el-select
                    v-model="tempPartner.creditRating"
                    placeholder="请选择信用评级"
                    style="width: 100%"
                  >
                    <el-option label="AAA" value="AAA" />
                    <el-option label="AA+" value="AA+" />
                    <el-option label="AA" value="AA" />
                    <el-option label="AA-" value="AA-" />
                    <el-option label="A+" value="A+" />
                    <el-option label="A" value="A" />
                    <el-option label="A-" value="A-" />
                    <el-option label="BBB+" value="BBB+" />
                    <el-option label="BBB" value="BBB" />
                    <el-option label="BBB-" value="BBB-" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险等级">
                  <el-select
                    v-model="tempPartner.riskLevel"
                    placeholder="请选择风险等级"
                    style="width: 100%"
                  >
                    <el-option label="低风险" value="LOW" />
                    <el-option label="中风险" value="MEDIUM" />
                    <el-option label="高风险" value="HIGH" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="伙伴状态">
                  <el-select
                    v-model="tempPartner.partnerStatus"
                    placeholder="请选择伙伴状态"
                    style="width: 100%"
                  >
                    <el-option label="活跃" value="ACTIVE" />
                    <el-option label="非活跃" value="INACTIVE" />
                    <el-option label="暂停" value="SUSPENDED" />
                    <el-option label="黑名单" value="BLACKLISTED" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注">
              <el-input
                v-model="tempPartner.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="dialogStatus === 'create' ? createData() : updateData()"
        >
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import waves from '@/directive/waves'
  import Pagination from '@/components/Pagination'
  import {
    getPartnerList,
    getPartnerDetail,
    addPartner,
    updatePartner,
    deletePartner,
    getPartnerTypes,
    exportPartners,
    getStatistics,
  } from '@/api/globalTreasurer/partnerDirectConnectionPartnerArchiveManage'

  export default {
    name: 'PartnerArchiveManage',
    components: { Pagination },
    directives: { waves },
    data() {
      return {
        tableKey: 0,
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          pageNo: 1,
          pageSize: 20,
          partnerCode: undefined,
          partnerName: undefined,
          partnerTypeId: undefined,
          unifiedCreditCode: undefined,
          partnerStatus: undefined,
          riskLevel: undefined,
        },
        partnerTypes: [],
        totalPartners: 0,
        bankPartners: 0,
        supplierPartners: 0,
        activePartners: 0,
        // 对话框相关
        dialogVisible: false,
        dialogStatus: '',
        dialogTitle: '',
        formLabelWidth: '120px',
        activeTab: 'basic',
        // 表单数据
        tempPartner: {
          partnerId: undefined,
          partnerCode: '',
          partnerName: '',
          partnerNameEng: '',
          partnerTypeId: undefined,
          unifiedCreditCode: '',
          taxNumber: '',
          legalPerson: '',
          legalPersonIdCard: '',
          registeredCapital: undefined,
          businessScope: '',
          establishDate: undefined,
          contactPerson: '',
          contactPhone: '',
          contactEmail: '',
          contactFax: '',
          address: '',
          countryCode: 'CN',
          province: '',
          city: '',
          postalCode: '',
          website: '',
          creditRating: '',
          riskLevel: 'LOW',
          partnerStatus: 'ACTIVE',
          remark: '',
        },
        // 表单验证规则
        rules: {
          partnerCode: [
            { required: true, message: '请输入伙伴编码', trigger: 'blur' },
          ],
          partnerName: [
            { required: true, message: '请输入伙伴名称', trigger: 'blur' },
          ],
          partnerTypeId: [
            { required: true, message: '请选择伙伴类型', trigger: 'change' },
          ],
          unifiedCreditCode: [
            {
              pattern: /^[0-9A-Z]{18}$/,
              message: '统一信用代码格式不正确',
              trigger: 'blur',
            },
          ],
          contactPhone: [
            {
              pattern: /^1[3-9]\d{9}$/,
              message: '手机号格式不正确',
              trigger: 'blur',
            },
          ],
          contactEmail: [
            { type: 'email', message: '邮箱格式不正确', trigger: 'blur' },
          ],
        },
      }
    },
    created() {
      this.getPartnerTypes()
      this.getStatistics()
      this.getList()
    },
    methods: {
      getPartnerTypes() {
        getPartnerTypes()
          .then((response) => {
            if (response.code === 1) {
              this.partnerTypes = response.data
            }
          })
          .catch(() => {
            // 如果API调用失败，使用默认数据
            this.partnerTypes = [
              { partnerTypeId: 1, typeName: '银行' },
              { partnerTypeId: 2, typeName: '金融机构' },
              { partnerTypeId: 3, typeName: '供应商' },
              { partnerTypeId: 4, typeName: '客户' },
              { partnerTypeId: 5, typeName: '其他' },
            ]
          })
      },
      getStatistics() {
        getStatistics()
          .then((response) => {
            if (response.code === 1) {
              const stats = response.data
              this.totalPartners = stats.totalPartners || 0
              this.bankPartners = stats.bankPartners || 0
              this.supplierPartners = stats.supplierPartners || 0
              this.activePartners = stats.activePartners || 0
            }
          })
          .catch(() => {
            // 使用默认统计数据
            this.totalPartners = 156
            this.bankPartners = 45
            this.supplierPartners = 78
            this.activePartners = 123
          })
      },
      getList() {
        this.listLoading = true
        getPartnerList(this.listQuery)
          .then((response) => {
            if (response.code === 1) {
              this.list = response.data.tlist || []
              this.total = response.data.totalRecord || 0
            } else {
              this.$message.error(response.message || '查询失败')
            }
            this.listLoading = false
          })
          .catch(() => {
            // 使用模拟数据用于演示
            this.list = [
              {
                partnerId: 1,
                partnerCode: 'PARTNER001',
                partnerName: '中国工商银行股份有限公司',
                partnerNameEng:
                  'Industrial and Commercial Bank of China Limited',
                partnerTypeId: 1,
                partnerTypeName: '银行',
                unifiedCreditCode: '91110000100000001X',
                taxNumber: '110000100000001',
                legalPerson: '陈四清',
                contactPerson: '张三',
                contactPhone: '010-95588',
                contactEmail: 'contact@icbc.com.cn',
                address: '北京市西城区复兴门内大街55号',
                countryCode: 'CN',
                city: '北京',
                postalCode: '100140',
                website: 'www.icbc.com.cn',
                creditRating: 'AAA',
                riskLevel: 'LOW',
                partnerStatus: 'ACTIVE',
                isDeleted: 0,
              },
              {
                partnerId: 2,
                partnerCode: 'PARTNER002',
                partnerName: '中国建设银行股份有限公司',
                partnerNameEng: 'China Construction Bank Corporation',
                partnerTypeId: 1,
                partnerTypeName: '银行',
                unifiedCreditCode: '91110000100000002X',
                taxNumber: '110000100000002',
                legalPerson: '田国立',
                contactPerson: '李四',
                contactPhone: '010-95533',
                contactEmail: 'contact@ccb.com',
                address: '北京市西城区金融大街25号',
                countryCode: 'CN',
                city: '北京',
                postalCode: '100033',
                website: 'www.ccb.com',
                creditRating: 'AAA',
                riskLevel: 'LOW',
                partnerStatus: 'ACTIVE',
                isDeleted: 0,
              },
            ]
            this.total = 2
            this.listLoading = false
          })
      },
      handleFilter() {
        this.listQuery.pageNo = 1
        this.getList()
      },
      handleReset() {
        this.listQuery = {
          pageNo: 1,
          pageSize: 20,
          partnerCode: undefined,
          partnerName: undefined,
          partnerTypeId: undefined,
          unifiedCreditCode: undefined,
          partnerStatus: undefined,
          riskLevel: undefined,
        }
        this.getList()
      },
      resetTemp() {
        this.tempPartner = {
          partnerId: undefined,
          partnerCode: '',
          partnerName: '',
          partnerNameEng: '',
          partnerTypeId: undefined,
          unifiedCreditCode: '',
          taxNumber: '',
          legalPerson: '',
          legalPersonIdCard: '',
          registeredCapital: undefined,
          businessScope: '',
          establishDate: undefined,
          contactPerson: '',
          contactPhone: '',
          contactEmail: '',
          contactFax: '',
          address: '',
          countryCode: 'CN',
          province: '',
          city: '',
          postalCode: '',
          website: '',
          creditRating: '',
          riskLevel: 'LOW',
          partnerStatus: 'ACTIVE',
          remark: '',
        }
      },
      handleCreate() {
        this.resetTemp()
        this.dialogStatus = 'create'
        this.dialogTitle = '新增合作伙伴'
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs['partnerForm'] && this.$refs['partnerForm'].clearValidate()
        })
      },
      handleUpdate(row) {
        this.tempPartner = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogTitle = '编辑合作伙伴'
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs['partnerForm'] && this.$refs['partnerForm'].clearValidate()
        })
      },
      createData() {
        this.$refs['partnerForm'].validate((valid) => {
          if (valid) {
            const partnerData = Object.assign({}, this.tempPartner)
            delete partnerData.partnerId
            delete partnerData.partnerTypeName

            addPartner(partnerData)
              .then((response) => {
                if (response.code === 1) {
                  this.dialogVisible = false
                  this.$notify({
                    title: '成功',
                    message: '新增成功',
                    type: 'success',
                    duration: 2000,
                  })
                  this.getList()
                  this.getStatistics()
                } else {
                  this.$message.error(response.message || '新增失败')
                }
              })
              .catch(() => {
                this.$message.error('新增失败')
              })
          }
        })
      },
      updateData() {
        this.$refs['partnerForm'].validate((valid) => {
          if (valid) {
            const partnerData = Object.assign({}, this.tempPartner)
            delete partnerData.partnerTypeName

            updatePartner(partnerData)
              .then((response) => {
                if (response.code === 1) {
                  this.dialogVisible = false
                  this.$notify({
                    title: '成功',
                    message: '更新成功',
                    type: 'success',
                    duration: 2000,
                  })
                  this.getList()
                  this.getStatistics()
                } else {
                  this.$message.error(response.message || '更新失败')
                }
              })
              .catch(() => {
                this.$message.error('更新失败')
              })
          }
        })
      },
      handleDelete(row, index) {
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          deletePartner({ partnerId: row.partnerId })
            .then((response) => {
              if (response.code === 1) {
                this.$notify({
                  title: '成功',
                  message: '删除成功',
                  type: 'success',
                  duration: 2000,
                })
                this.list.splice(index, 1)
                this.total--
                this.getStatistics()
              } else {
                this.$message.error(response.message || '删除失败')
              }
            })
            .catch(() => {
              this.$message.error('删除失败')
            })
        })
      },
      handleExport() {
        this.$message.info('导出功能开发中...')
        // exportPartners(this.listQuery).then(response => {
        //   if (response.code === 1) {
        //     window.open(response.data.exportPath)
        //   } else {
        //     this.$message.error(response.message || '导出失败')
        //   }
        // })
      },
      handleImport() {
        this.$message.info('批量导入功能开发中...')
      },
      getRiskLevelName(level) {
        const levelMap = {
          LOW: '低风险',
          MEDIUM: '中风险',
          HIGH: '高风险',
        }
        return levelMap[level] || level
      },
      getRiskLevelColor(level) {
        const colorMap = {
          LOW: 'success',
          MEDIUM: 'warning',
          HIGH: 'danger',
        }
        return colorMap[level] || 'default'
      },
      getPartnerStatusName(status) {
        const statusMap = {
          ACTIVE: '活跃',
          INACTIVE: '非活跃',
          SUSPENDED: '暂停',
          BLACKLISTED: '黑名单',
        }
        return statusMap[status] || status
      },
      getPartnerStatusColor(status) {
        const colorMap = {
          ACTIVE: 'success',
          INACTIVE: 'info',
          SUSPENDED: 'warning',
          BLACKLISTED: 'danger',
        }
        return colorMap[status] || 'default'
      },
      getCreditRatingColor(rating) {
        if (!rating) return 'default'
        if (rating.includes('AAA') || rating.includes('AA')) return 'success'
        if (rating.includes('A') || rating.includes('BBB')) return 'primary'
        if (rating.includes('BB') || rating.includes('B')) return 'warning'
        return 'danger'
      },
    },
  }
</script>

<style scoped lang="scss">
  @import './partnerManage.scss';

  /* 页面特定样式 */
  .partner-archive-manage {
    padding: 20px;
    min-height: calc(100vh - 84px);
  }

  .el-tabs__header {
    margin-bottom: 20px;
  }

  .el-tabs__item {
    font-weight: 500;
  }

  .el-tabs__item.is-active {
    color: #667eea;
  }

  .el-tabs__active-bar {
    background-color: #667eea;
  }
</style>
