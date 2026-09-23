<template>
  <div class="bill-registration-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-document-add"></i>
            票据登记管理
          </h2>
          <p class="page-description">管理企业票据的登记、录入和基础信息维护</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增票据
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleBatchImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 票据概览卡片 -->
    <div class="bill-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总票据数</div>
                <div class="card-value">{{ totalBills }}</div>
                <div class="card-change">张票据</div>
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
                <div class="card-title">有效票据</div>
                <div class="card-value">{{ activeBills }}</div>
                <div class="card-change positive">正常状态</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon expiring-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">即将到期</div>
                <div class="card-value">{{ expiringBills }}</div>
                <div class="card-change warning">30天内</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">票据金额</div>
                <div class="card-value">{{ totalAmount }}</div>
                <div class="card-change">万元</div>
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
          <el-form-item label="票据号码">
            <el-input
              v-model="listQuery.billNumber"
              placeholder="请输入票据号码"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="票据类型">
            <el-select
              v-model="listQuery.billType"
              placeholder="请选择票据类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
              <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
              <el-option label="支票" value="CHECK" />
              <el-option label="本票" value="PROMISSORY_NOTE" />
              <el-option label="电子票据" value="ELECTRONIC_BILL" />
            </el-select>
          </el-form-item>
          <el-form-item label="票据状态">
            <el-select
              v-model="listQuery.billStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="持有" value="HOLDING" />
              <el-option label="已背书" value="ENDORSED" />
              <el-option label="已贴现" value="DISCOUNTED" />
              <el-option label="已到期" value="MATURED" />
              <el-option label="已作废" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="出票日期">
            <el-date-picker
              v-model="listQuery.issueDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 票据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="billList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="票据ID" prop="billId" width="80" align="center" />
        <el-table-column label="票据号码" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.billNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getBillTypeTagType(row.billType)" size="mini">
              {{ getBillTypeText(row.billType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="票据金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="bill-amount">{{ formatCurrency(row.billAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="出票人" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.drawerName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="收款人" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.payeeName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="出票日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.issueDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getMaturityDateClass(row.maturityDate)">{{ formatDate(row.maturityDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getStatusTagType(row.billStatus)" size="mini">
              {{ getStatusText(row.billStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="登记人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.registrarName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'endorse', row: row}">背书转让</el-dropdown-item>
                <el-dropdown-item :command="{action: 'discount', row: row}">申请贴现</el-dropdown-item>
                <el-dropdown-item :command="{action: 'cancel', row: row}">作废票据</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑票据对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-tabs v-model="activeFormTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据号码" prop="billNumber">
                  <el-input v-model="temp.billNumber" placeholder="请输入票据号码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="票据类型" prop="billType">
                  <el-select v-model="temp.billType" placeholder="请选择票据类型" style="width: 100%;">
                    <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
                    <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
                    <el-option label="支票" value="CHECK" />
                    <el-option label="本票" value="PROMISSORY_NOTE" />
                    <el-option label="电子票据" value="ELECTRONIC_BILL" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据金额" prop="billAmount">
                  <el-input-number
                    v-model="temp.billAmount"
                    :precision="2"
                    :min="0"
                    style="width: 100%;"
                    placeholder="请输入票据金额"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="币种">
                  <el-select v-model="temp.currency" placeholder="请选择币种" style="width: 100%;">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="出票日期" prop="issueDate">
                  <el-date-picker
                    v-model="temp.issueDate"
                    type="date"
                    placeholder="选择出票日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期" prop="maturityDate">
                  <el-date-picker
                    v-model="temp.maturityDate"
                    type="date"
                    placeholder="选择到期日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="当事人信息" name="parties">
            <el-form-item label="出票人" prop="drawerName">
              <el-input v-model="temp.drawerName" placeholder="请输入出票人名称" />
            </el-form-item>
            <el-form-item label="出票人账号">
              <el-input v-model="temp.drawerAccount" placeholder="请输入出票人账号" />
            </el-form-item>
            <el-form-item label="收款人" prop="payeeName">
              <el-input v-model="temp.payeeName" placeholder="请输入收款人名称" />
            </el-form-item>
            <el-form-item label="收款人账号">
              <el-input v-model="temp.payeeAccount" placeholder="请输入收款人账号" />
            </el-form-item>
            <el-form-item label="承兑人">
              <el-input v-model="temp.acceptorName" placeholder="请输入承兑人名称" />
            </el-form-item>
            <el-form-item label="承兑银行">
              <el-input v-model="temp.acceptingBank" placeholder="请输入承兑银行名称" />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="其他信息" name="others">
            <el-form-item label="票据用途">
              <el-select v-model="temp.billPurpose" placeholder="请选择票据用途" style="width: 100%;">
                <el-option label="货款支付" value="PAYMENT" />
                <el-option label="服务费用" value="SERVICE_FEE" />
                <el-option label="投资款项" value="INVESTMENT" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
            <el-form-item label="票据来源">
              <el-select v-model="temp.billSource" placeholder="请选择票据来源" style="width: 100%;">
                <el-option label="收取" value="RECEIVED" />
                <el-option label="开具" value="ISSUED" />
                <el-option label="背书取得" value="ENDORSED" />
              </el-select>
            </el-form-item>
            <el-form-item label="保管地点">
              <el-input v-model="temp.storageLocation" placeholder="请输入保管地点" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="temp.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 票据详情对话框 -->
    <el-dialog title="票据详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentBill" class="bill-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="票据号码">{{ currentBill.billNumber }}</el-descriptions-item>
          <el-descriptions-item label="票据类型">{{ getBillTypeText(currentBill.billType) }}</el-descriptions-item>
          <el-descriptions-item label="票据金额">{{ formatCurrency(currentBill.billAmount) }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ currentBill.currency }}</el-descriptions-item>
          <el-descriptions-item label="出票日期">{{ formatDate(currentBill.issueDate) }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ formatDate(currentBill.maturityDate) }}</el-descriptions-item>
          <el-descriptions-item label="出票人">{{ currentBill.drawerName }}</el-descriptions-item>
          <el-descriptions-item label="收款人">{{ currentBill.payeeName }}</el-descriptions-item>
          <el-descriptions-item label="承兑人">{{ currentBill.acceptorName }}</el-descriptions-item>
          <el-descriptions-item label="承兑银行">{{ currentBill.acceptingBank }}</el-descriptions-item>
          <el-descriptions-item label="票据状态">
            <el-tag :type="getStatusTagType(currentBill.billStatus)">
              {{ getStatusText(currentBill.billStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="登记人">{{ currentBill.registrarName }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>备注信息</h4>
          <p>{{ currentBill.remark || '无' }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEdit(currentBill)">编辑票据</el-button>
      </div>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog title="批量导入票据" :visible.sync="dialogImportVisible" width="600px">
      <el-upload
        class="upload-demo"
        drag
        action="#"
        :auto-upload="false"
        :on-change="handleFileChange"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogImportVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImport">确认导入</el-button>
      </div>
    </el-dialog>

    <!-- 作废票据对话框 -->
    <el-dialog title="作废票据" :visible.sync="dialogCancelVisible" width="500px">
      <el-form ref="cancelForm" :model="cancelForm" :rules="cancelRules" label-width="100px">
        <el-alert
          title="作废后票据将无法恢复，请谨慎操作"
          type="warning"
          :closable="false"
          style="margin-bottom: 20px;"
        />
        <el-form-item label="作废原因" prop="reason">
          <el-input
            v-model="cancelForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入作废原因"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCancelVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmCancelBill">确认作废</el-button>
      </div>
    </el-dialog>

    <!-- 背书转让对话框 -->
    <el-dialog title="背书转让" :visible.sync="dialogEndorseVisible" width="700px">
      <el-alert title="将当前票据背书转让给被背书人" type="info" :closable="false" style="margin-bottom: 20px;" />
      <el-form ref="endorseForm" :model="endorseForm" :rules="endorseRules" label-width="120px" style="padding: 0 20px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="票据号码">
              <el-input v-model="endorseForm.billNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="票据金额">
              <el-input :value="formatCurrency(endorseForm.billAmount)" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="背书类型" prop="endorsementType">
              <el-select v-model="endorseForm.endorsementType" placeholder="请选择背书类型" style="width: 100%;">
                <el-option label="转让背书" value="TRANSFER" />
                <el-option label="质押背书" value="PLEDGE" />
                <el-option label="委托收款背书" value="COLLECTION" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="背书用途" prop="endorsementPurpose">
              <el-select v-model="endorseForm.endorsementPurpose" placeholder="请选择背书用途" style="width: 100%;">
                <el-option label="货款支付" value="PAYMENT" />
                <el-option label="债务清偿" value="DEBT_SETTLEMENT" />
                <el-option label="质押担保" value="GUARANTEE" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="被背书人" prop="endorseeName">
              <el-input v-model="endorseForm.endorseeName" placeholder="请输入被背书人名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="被背书人账号">
              <el-input v-model="endorseForm.endorseeAccount" placeholder="请输入被背书人账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="被背书人开户行">
              <el-input v-model="endorseForm.endorseeBank" placeholder="请输入开户行" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="endorseForm.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预计执行日期">
              <el-date-picker v-model="endorseForm.expectedExecutionDate" type="date" placeholder="选择日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否连续背书">
              <el-switch v-model="endorseForm.isContinuous" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="endorseForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEndorseVisible = false">取消</el-button>
        <el-button type="primary" :loading="endorseSubmitting" @click="submitEndorse">确认背书</el-button>
      </div>
    </el-dialog>

    <!-- 申请贴现对话框 -->
    <el-dialog title="申请贴现" :visible.sync="dialogDiscountVisible" width="700px">
      <el-alert title="将当前票据向银行申请贴现" type="info" :closable="false" style="margin-bottom: 20px;" />
      <el-form ref="discountForm" :model="discountForm" :rules="discountRules" label-width="120px" style="padding: 0 20px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="票据号码">
              <el-input v-model="discountForm.billNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="票据金额">
              <el-input :value="formatCurrency(discountForm.billAmount)" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="贴现银行" prop="discountBank">
              <el-select v-model="discountForm.discountBank" placeholder="请选择贴现银行" style="width: 100%;">
                <el-option label="中国工商银行" value="中国工商银行" />
                <el-option label="中国建设银行" value="中国建设银行" />
                <el-option label="中国农业银行" value="中国农业银行" />
                <el-option label="中国银行" value="中国银行" />
                <el-option label="交通银行" value="交通银行" />
                <el-option label="招商银行" value="招商银行" />
                <el-option label="中信银行" value="中信银行" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="贴现利率(%)" prop="discountRate">
              <el-input-number v-model="discountForm.discountRate" :precision="4" :step="0.01" :min="0" :max="100" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="贴现天数">
              <el-input-number v-model="discountForm.discountPeriod" :min="1" :max="365" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计贴现日期">
              <el-date-picker v-model="discountForm.expectedDiscountDate" type="date" placeholder="选择日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="discountForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDiscountVisible = false">取消</el-button>
        <el-button type="primary" :loading="discountSubmitting" @click="submitDiscount">确认贴现</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBillRegistrationPage,
  getBillRegistrationDetail,
  createBillRegistration,
  updateBillRegistration,
  deleteBillRegistration,
  batchImportBills,
  cancelBill,
  exportBillRegistration,
  getBillRegistrationStatistics
} from '@/api/globalTreasurer-new/billManagement/billRegistration'
import { createBillEndorsement } from '@/api/globalTreasurer-new/billManagement/billEndorsement'
import { createBillDiscount } from '@/api/globalTreasurer-new/billManagement/billDiscount'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillRegistrationManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        billNumber: undefined,
        billType: undefined,
        billStatus: undefined,
        issueDateRange: [],
        issueDateStart: undefined,
        issueDateEnd: undefined
      },
      totalBills: 0,
      activeBills: 0,
      expiringBills: 0,
      totalAmount: '0.00',
      billList: [],
      multipleSelection: [],
      currentBill: null,
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogImportVisible: false,
      dialogCancelVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      activeFormTab: 'basic',
      cancelForm: {
        billId: null,
        reason: ''
      },
      temp: {
        billId: undefined,
        billNumber: '',
        billType: '',
        billAmount: 0,
        currency: 'CNY',
        issueDate: null,
        maturityDate: null,
        drawerName: '',
        drawerAccount: '',
        payeeName: '',
        payeeAccount: '',
        acceptorName: '',
        acceptingBank: '',
        billPurpose: '',
        billSource: '',
        storageLocation: '',
        remark: ''
      },
      rules: {
        billNumber: [{ required: true, message: '票据号码不能为空', trigger: 'blur' }],
        billType: [{ required: true, message: '请选择票据类型', trigger: 'change' }],
        billAmount: [{ required: true, message: '请输入票据金额', trigger: 'blur' }],
        issueDate: [{ required: true, message: '请选择出票日期', trigger: 'change' }],
        maturityDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }],
        drawerName: [{ required: true, message: '出票人不能为空', trigger: 'blur' }],
        payeeName: [{ required: true, message: '收款人不能为空', trigger: 'blur' }]
      },
      cancelRules: {
        reason: [{ required: true, message: '请输入作废原因', trigger: 'blur' }]
      },
      importFile: null,
      // 背书转让
      dialogEndorseVisible: false,
      endorseSubmitting: false,
      endorseForm: {
        billNumber: '',
        billAmount: 0,
        endorsementType: 'TRANSFER',
        endorserName: '',
        endorserAccount: '',
        endorseeName: '',
        endorseeAccount: '',
        endorseeBank: '',
        contactPhone: '',
        expectedExecutionDate: null,
        endorsementPurpose: 'PAYMENT',
        isContinuous: 1,
        riskLevel: 'LOW',
        remark: ''
      },
      endorseRules: {
        endorsementType: [{ required: true, message: '请选择背书类型', trigger: 'change' }],
        endorseeName: [{ required: true, message: '请输入被背书人名称', trigger: 'blur' }],
        endorsementPurpose: [{ required: true, message: '请选择背书用途', trigger: 'change' }]
      },
      // 申请贴现
      dialogDiscountVisible: false,
      discountSubmitting: false,
      discountForm: {
        billNumber: '',
        billAmount: 0,
        discountBank: '',
        discountRate: 0,
        discountPeriod: 0,
        expectedDiscountDate: null,
        remark: ''
      },
      discountRules: {
        discountBank: [{ required: true, message: '请选择贴现银行', trigger: 'change' }],
        discountRate: [{ required: true, message: '请输入贴现利率', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.loadStatistics()
  },
  methods: {
    // 加载统计数据
    loadStatistics() {
      getBillRegistrationStatistics({}).then(response => {
        if (response.code === 1 && response.data) {
          this.totalBills = response.data.totalBills || 0
          this.activeBills = response.data.activeBills || 0
          this.expiringBills = response.data.expiringBills || 0
          this.totalAmount = response.data.totalAmount || '0.00'
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
        // 使用默认值
        this.calculateLocalStatistics()
      })
    },
    // 本地计算统计数据（后备方案）
    calculateLocalStatistics() {
      if (this.billList && this.billList.length > 0) {
        this.totalBills = this.billList.length
        this.activeBills = this.billList.filter(b => b.billStatus === 'HOLDING').length
        // 计算30天内到期的票据
        const today = new Date()
        const thirtyDaysLater = new Date(today.getTime() + 30 * 24 * 60 * 60 * 1000)
        this.expiringBills = this.billList.filter(b => {
          const maturity = new Date(b.maturityDate)
          return maturity >= today && maturity <= thirtyDaysLater && b.billStatus === 'HOLDING'
        }).length
        // 计算总金额（万元）
        const total = this.billList.reduce((sum, b) => sum + (b.billAmount || 0), 0)
        this.totalAmount = (total / 10000).toFixed(2)
      }
    },
    getList() {
      this.listLoading = true
      // 处理日期范围
      const params = { ...this.listQuery }
      if (this.listQuery.issueDateRange && this.listQuery.issueDateRange.length === 2) {
        params.issueDateStart = this.formatDateParam(this.listQuery.issueDateRange[0])
        params.issueDateEnd = this.formatDateParam(this.listQuery.issueDateRange[1])
      }
      delete params.issueDateRange

      // 调用后端API获取真实数据
      getBillRegistrationPage(params).then(response => {
        // 兼容多种返回格式
        if (response.code === 1) {
          if (response.data && Array.isArray(response.data)) {
            this.billList = response.data
            this.total = response.result ? response.result.total : response.data.length
          } else if (response.data && response.data.list) {
            this.billList = response.data.list
            this.total = response.data.total || 0
          } else if (response.result && response.result.list) {
            this.billList = response.result.list
            this.total = response.result.total || 0
          } else {
            this.billList = []
            this.total = 0
          }
          // 更新本地统计
          this.calculateLocalStatistics()
        } else {
          this.$message.error(response.msg || '获取数据失败')
          this.billList = []
          this.total = 0
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取票据登记列表失败:', error)
        this.billList = []
        this.total = 0
        this.listLoading = false
      })
    },
    formatDateParam(date) {
      if (!date) return null
      if (typeof date === 'string') return date
      const d = new Date(date)
      return d.getFullYear() + '-' +
        String(d.getMonth() + 1).padStart(2, '0') + '-' +
        String(d.getDate()).padStart(2, '0')
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 20,
        billNumber: undefined,
        billType: undefined,
        billStatus: undefined,
        issueDateRange: [],
        issueDateStart: undefined,
        issueDateEnd: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '新增票据'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleEdit(row) {
      // 从后端获取最新详情
      getBillRegistrationDetail(row.billId).then(response => {
        if (response.code === 1 && response.data) {
          this.temp = Object.assign({}, response.data)
        } else {
          this.temp = Object.assign({}, row)
        }
        this.dialogStatus = 'update'
        this.dialogTitle = '编辑票据'
        this.dialogFormVisible = true
        this.activeFormTab = 'basic'
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      }).catch(() => {
        this.temp = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogTitle = '编辑票据'
        this.dialogFormVisible = true
        this.activeFormTab = 'basic'
      })
    },
    handleViewDetail(row) {
      // 从后端获取最新详情
      getBillRegistrationDetail(row.billId).then(response => {
        if (response.code === 1 && response.data) {
          this.currentBill = response.data
        } else {
          this.currentBill = row
        }
        this.dialogDetailVisible = true
      }).catch(() => {
        this.currentBill = row
        this.dialogDetailVisible = true
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'endorse':
          this.openEndorseDialog(row)
          break
        case 'discount':
          this.openDiscountDialog(row)
          break
        case 'cancel':
          this.handleCancelBill(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    // 打开背书转让对话框
    openEndorseDialog(row) {
      this.endorseForm = {
        billNumber: row.billNumber,
        billAmount: row.billAmount,
        endorsementType: 'TRANSFER',
        endorserName: row.drawerName || '',
        endorserAccount: '',
        endorseeName: '',
        endorseeAccount: '',
        endorseeBank: '',
        contactPhone: '',
        expectedExecutionDate: null,
        endorsementPurpose: 'PAYMENT',
        isContinuous: 1,
        riskLevel: 'LOW',
        remark: ''
      }
      this.dialogEndorseVisible = true
      this.$nextTick(() => {
        this.$refs['endorseForm'] && this.$refs['endorseForm'].clearValidate()
      })
    },
    // 提交背书转让
    submitEndorse() {
      this.$refs['endorseForm'].validate((valid) => {
        if (valid) {
          this.endorseSubmitting = true
          const submitData = { ...this.endorseForm }
          if (submitData.expectedExecutionDate && typeof submitData.expectedExecutionDate === 'object') {
            submitData.expectedExecutionDate = this.formatDateParam(submitData.expectedExecutionDate)
          }
          createBillEndorsement(submitData).then(response => {
            if (response.code === 1) {
              this.dialogEndorseVisible = false
              this.$message({ type: 'success', message: '背书转让申请提交成功' })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message.error(response.msg || '背书转让申请失败')
            }
            this.endorseSubmitting = false
          }).catch(error => {
            console.error('背书转让失败:', error)
            this.$message.error('背书转让申请失败，请稍后重试')
            this.endorseSubmitting = false
          })
        }
      })
    },
    // 打开申请贴现对话框
    openDiscountDialog(row) {
      // 计算贴现天数（到期日 - 今天）
      let days = 0
      if (row.maturityDate) {
        const maturity = new Date(row.maturityDate)
        const today = new Date()
        days = Math.max(0, Math.ceil((maturity - today) / (1000 * 60 * 60 * 24)))
      }
      this.discountForm = {
        billNumber: row.billNumber,
        billAmount: row.billAmount,
        discountBank: '',
        discountRate: 0,
        discountPeriod: days,
        expectedDiscountDate: null,
        remark: ''
      }
      this.dialogDiscountVisible = true
      this.$nextTick(() => {
        this.$refs['discountForm'] && this.$refs['discountForm'].clearValidate()
      })
    },
    // 提交申请贴现
    submitDiscount() {
      this.$refs['discountForm'].validate((valid) => {
        if (valid) {
          this.discountSubmitting = true
          const submitData = { ...this.discountForm }
          if (submitData.expectedDiscountDate && typeof submitData.expectedDiscountDate === 'object') {
            submitData.expectedDiscountDate = this.formatDateParam(submitData.expectedDiscountDate)
          }
          createBillDiscount(submitData).then(response => {
            if (response.code === 1) {
              this.dialogDiscountVisible = false
              this.$message({ type: 'success', message: '贴现申请提交成功' })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message.error(response.msg || '贴现申请失败')
            }
            this.discountSubmitting = false
          }).catch(error => {
            console.error('贴现申请失败:', error)
            this.$message.error('贴现申请失败，请稍后重试')
            this.discountSubmitting = false
          })
        }
      })
    },
    handleCancelBill(row) {
      this.cancelForm = {
        billId: row.billId,
        reason: ''
      }
      this.dialogCancelVisible = true
    },
    confirmCancelBill() {
      this.$refs['cancelForm'].validate((valid) => {
        if (valid) {
          cancelBill(this.cancelForm.billId, this.cancelForm.reason).then(response => {
            if (response.code === 1) {
              this.dialogCancelVisible = false
              this.$message({ type: 'success', message: '票据已作废!' })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message.error(response.msg || '作废失败')
            }
          }).catch(error => {
            console.error('作废票据失败:', error)
            this.$message.error('作废失败，请稍后重试')
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该票据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBillRegistration([row.billId]).then(response => {
          if (response.code === 1) {
            this.$message({ type: 'success', message: '删除成功!' })
            this.getList()
            this.loadStatistics()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        }).catch(error => {
          console.error('删除票据失败:', error)
          this.$message.error('删除失败，请稍后重试')
        })
      })
    },
    handleBatchImport() {
      this.importFile = null
      this.dialogImportVisible = true
    },
    handleExport() {
      // 调用后端API导出数据
      const params = { ...this.listQuery }
      if (this.listQuery.issueDateRange && this.listQuery.issueDateRange.length === 2) {
        params.issueDateStart = this.formatDateParam(this.listQuery.issueDateRange[0])
        params.issueDateEnd = this.formatDateParam(this.listQuery.issueDateRange[1])
      }
      delete params.issueDateRange

      exportBillRegistration(params).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '票据登记数据_' + new Date().getTime() + '.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
        this.$message({ type: 'success', message: '票据数据导出成功' })
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message({ type: 'error', message: '导出失败，请稍后重试' })
      })
    },
    handleFileChange(file) {
      this.importFile = file.raw
    },
    handleImport() {
      if (!this.importFile) {
        this.$message({ type: 'warning', message: '请选择要导入的文件' })
        return
      }
      // 调用后端API批量导入
      const formData = new FormData()
      formData.append('file', this.importFile)
      batchImportBills(formData).then(response => {
        if (response.code === 1) {
          this.dialogImportVisible = false
          this.importFile = null
          this.getList()
          this.loadStatistics()
          this.$message({ type: 'success', message: response.msg || '票据数据导入成功' })
        } else {
          this.$message.error(response.msg || '导入失败')
        }
      }).catch(error => {
        console.error('导入失败:', error)
        this.$message({ type: 'error', message: '导入失败，请检查文件格式' })
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 格式化日期
          const submitData = { ...this.temp }
          if (submitData.issueDate && typeof submitData.issueDate === 'object') {
            submitData.issueDate = this.formatDateParam(submitData.issueDate)
          }
          if (submitData.maturityDate && typeof submitData.maturityDate === 'object') {
            submitData.maturityDate = this.formatDateParam(submitData.maturityDate)
          }

          createBillRegistration(submitData).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message({ type: 'success', message: '票据登记成功' })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message.error(response.msg || '登记失败')
            }
          }).catch(error => {
            console.error('创建票据失败:', error)
            this.$message.error('登记失败，请稍后重试')
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 格式化日期
          const submitData = { ...this.temp }
          if (submitData.issueDate && typeof submitData.issueDate === 'object') {
            submitData.issueDate = this.formatDateParam(submitData.issueDate)
          }
          if (submitData.maturityDate && typeof submitData.maturityDate === 'object') {
            submitData.maturityDate = this.formatDateParam(submitData.maturityDate)
          }

          updateBillRegistration(submitData).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message({ type: 'success', message: '票据更新成功' })
              this.getList()
            } else {
              this.$message.error(response.msg || '更新失败')
            }
          }).catch(error => {
            console.error('更新票据失败:', error)
            this.$message.error('更新失败，请稍后重试')
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        billId: undefined,
        billNumber: '',
        billType: '',
        billAmount: 0,
        currency: 'CNY',
        issueDate: null,
        maturityDate: null,
        drawerName: '',
        drawerAccount: '',
        payeeName: '',
        payeeAccount: '',
        acceptorName: '',
        acceptingBank: '',
        billPurpose: '',
        billSource: '',
        storageLocation: '',
        remark: ''
      }
    },
    getBillTypeTagType(type) {
      const typeMap = {
        'BANK_ACCEPTANCE': 'success',
        'COMMERCIAL_ACCEPTANCE': 'primary',
        'CHECK': 'warning',
        'PROMISSORY_NOTE': 'info',
        'ELECTRONIC_BILL': 'success'
      }
      return typeMap[type] || 'info'
    },
    getBillTypeText(type) {
      const textMap = {
        'BANK_ACCEPTANCE': '银行承兑汇票',
        'COMMERCIAL_ACCEPTANCE': '商业承兑汇票',
        'CHECK': '支票',
        'PROMISSORY_NOTE': '本票',
        'ELECTRONIC_BILL': '电子票据'
      }
      return textMap[type] || type
    },
    getStatusTagType(status) {
      const typeMap = {
        'HOLDING': 'success',
        'ENDORSED': 'primary',
        'DISCOUNTED': 'warning',
        'MATURED': 'info',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'HOLDING': '持有',
        'ENDORSED': '已背书',
        'DISCOUNTED': '已贴现',
        'MATURED': '已到期',
        'CANCELLED': '已作废'
      }
      return textMap[status] || status
    },
    getMaturityDateClass(maturityDate) {
      const today = new Date()
      const maturity = new Date(maturityDate)
      const diffDays = Math.ceil((maturity - today) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'expired-date'
      if (diffDays <= 30) return 'expiring-date'
      return ''
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    },
    formatDate(value) {
      if (!value) return ''
      // 如果已经是 YYYY-MM-DD 格式的字符串，直接返回
      if (typeof value === 'string' && /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/.test(value)) {
        return value
      }
      const d = new Date(value)
      if (isNaN(d.getTime())) return value
      return d.getFullYear() + '-' +
        String(d.getMonth() + 1).padStart(2, '0') + '-' +
        String(d.getDate()).padStart(2, '0')
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-registration-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .bill-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.active-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.expiring-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
            &.warning {
              color: #E6A23C;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .bill-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .expired-date {
    color: #F56C6C;
    font-weight: 600;
  }

  .expiring-date {
    color: #E6A23C;
    font-weight: 600;
  }

  .bill-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    h4 {
      margin: 16px 0 8px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }
    p {
      margin: 0;
      color: #606266;
      line-height: 1.5;
    }
  }
}
</style>
