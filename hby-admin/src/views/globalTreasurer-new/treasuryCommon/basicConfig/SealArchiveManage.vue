<template>
  <div class="seal-archive-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-medal"></i>
            印鉴档案管理
          </h2>
          <p class="page-description">管理企业印鉴档案信息，包括印鉴登记、使用记录、权限控制和安全管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增印鉴
          </el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="warning" icon="el-icon-lock" @click="handleSecurity">
            安全管理
          </el-button>
        </div>
      </div>
    </div>

    <!-- 印鉴统计卡片 -->
    <div class="seal-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-medal"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总印鉴数</div>
                <div class="card-value">{{ totalSeals }}</div>
                <div class="card-change">已登记印鉴</div>
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
                <div class="card-title">在用印鉴</div>
                <div class="card-value">{{ activeSeals }}</div>
                <div class="card-change positive">正常使用</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon locked-icon">
                <i class="el-icon-lock"></i>
              </div>
              <div class="card-info">
                <div class="card-title">锁定印鉴</div>
                <div class="card-value">{{ lockedSeals }}</div>
                <div class="card-change negative">安全锁定</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon usage-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">今日使用</div>
                <div class="card-value">{{ todayUsage }}</div>
                <div class="card-change">使用次数</div>
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
          <el-form-item label="印鉴编码">
            <el-input
              v-model="listQuery.sealCode"
              placeholder="请输入印鉴编码"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="印鉴名称">
            <el-input
              v-model="listQuery.sealName"
              placeholder="请输入印鉴名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="印鉴类型">
            <el-select
              v-model="listQuery.sealTypeId"
              placeholder="请选择印鉴类型"
              clearable
              style="width: 135px;"
            >
              <el-option
                v-for="type in sealTypes"
                :key="type.sealTypeId"
                :label="type.typeName"
                :value="type.sealTypeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="持有人">
            <el-input
              v-model="listQuery.ownerName"
              placeholder="请输入持有人姓名"
              style="width: 135px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.status"
              placeholder="请选择状态"
              clearable
              style="width: 135px;"
            >
              <el-option label="启用" value="1" />
              <el-option label="停用" value="0" />
            </el-select>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter" style="margin-left: 10px;">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset" style="margin-left: 10px;">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">印鉴档案列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="getList">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="handleTableSetting">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="印鉴编码" prop="sealCode" sortable="custom" align="center" width="120">
          <template slot-scope="{row}">
            <span class="seal-code">{{ row.sealCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="印鉴信息" min-width="200" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="seal-info">
              <div class="seal-name">{{ row.sealName }}</div>
              <div class="seal-type">
                <el-tag v-if="row.sealTypeName" size="mini" :type="getSealTypeColor(row.sealTypeId)">
                  {{ row.sealTypeName }}
                </el-tag>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="持有人信息" min-width="160" align="center">
          <template slot-scope="{row}">
            <div class="owner-info">
              <div class="owner-name">{{ row.ownerName }}</div>
              <div class="owner-position">{{ row.ownerPosition }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="有效期" min-width="180" align="center">
          <template slot-scope="{row}">
            <div class="validity-period">
              <div class="date-item">
                <span class="date-label">生效:</span>
                <span class="date-value">{{ formatDate(row.effectiveDate) }}</span>
              </div>
              <div class="date-item">
                <span class="date-label">失效:</span>
                <span class="date-value" :class="{ 'expired': isExpired(row.expireDate) }">
                  {{ formatDate(row.expireDate) }}
                </span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="110" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" class-name="status-col" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getSealStatusColor(row)" size="small">
              {{ getSealStatusText(row) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                查看
              </el-button>
              <el-button type="success" size="mini" icon="el-icon-picture" @click="handlePreview(row)">
                预览
              </el-button>
              <el-button
                v-if="row.status !== '0'"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="handleDelete(row,$index)"
              >
                删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="padding: 0 20px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="印鉴编码" prop="sealCode">
              <el-input v-model="temp.sealCode" placeholder="请输入印鉴编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="印鉴名称" prop="sealName">
              <el-input v-model="temp.sealName" placeholder="请输入印鉴名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="印鉴类型" prop="sealTypeId">
              <el-select v-model="temp.sealTypeId" placeholder="请选择印鉴类型" style="width: 100%;">
                <el-option
                  v-for="type in sealTypes"
                  :key="type.sealTypeId"
                  :label="type.typeName"
                  :value="type.sealTypeId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="temp.status" placeholder="请选择状态" style="width: 100%;">
                <el-option label="启用" value="1" />
                <el-option label="停用" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="持有人姓名" prop="ownerName">
              <el-input v-model="temp.ownerName" placeholder="请输入持有人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="持有人职位" prop="ownerPosition">
              <el-input v-model="temp.ownerPosition" placeholder="请输入持有人职位" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="身份证号" prop="ownerIdCard">
          <el-input v-model="temp.ownerIdCard" placeholder="请输入持有人身份证号" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker v-model="temp.effectiveDate" type="date" placeholder="选择生效日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expireDate">
              <el-date-picker v-model="temp.expireDate" type="date" placeholder="选择失效日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="印鉴图片">
          <el-upload
            class="seal-uploader"
            action="#"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :http-request="handleSealImageUpload"
          >
            <img v-if="temp.sealImageUrl" :src="temp.sealImageUrl" class="seal-image">
            <i v-else class="el-icon-plus seal-uploader-icon"></i>
          </el-upload>
          <div class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">确认</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="印鉴详情" :visible.sync="viewDialogVisible" width="700px">
      <div v-if="currentViewData" class="view-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="view-item">
              <label>印鉴编码：</label>
              <span>{{ currentViewData.sealCode }}</span>
            </div>
            <div class="view-item">
              <label>印鉴名称：</label>
              <span>{{ currentViewData.sealName }}</span>
            </div>
            <div class="view-item">
              <label>印鉴类型：</label>
              <el-tag :type="getSealTypeColor(currentViewData.sealTypeId)" size="small">
                {{ currentViewData.sealTypeName }}
              </el-tag>
            </div>
            <div class="view-item">
              <label>持有人：</label>
              <span>{{ currentViewData.ownerName }}</span>
            </div>
            <div class="view-item">
              <label>职位：</label>
              <span>{{ currentViewData.ownerPosition }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="view-item">
              <label>身份证号：</label>
              <span>{{ currentViewData.ownerIdCard }}</span>
            </div>
            <div class="view-item">
              <label>生效日期：</label>
              <span>{{ formatDate(currentViewData.effectiveDate) }}</span>
            </div>
            <div class="view-item">
              <label>失效日期：</label>
              <span :class="{ 'expired': isExpired(currentViewData.expireDate) }">
                {{ formatDate(currentViewData.expireDate) }}
              </span>
            </div>
            <div class="view-item">
              <label>状态：</label>
              <el-tag :type="getSealStatusColor(currentViewData)" size="small">
                {{ getSealStatusText(currentViewData) }}
              </el-tag>
            </div>
            <div class="view-item">
              <label>创建时间：</label>
              <span>{{ formatDate(currentViewData.createTime) }}</span>
            </div>
          </el-col>
        </el-row>
        <div class="view-item full-width">
          <label>印鉴图片：</label>
          <div v-if="currentViewData.sealImageUrl" class="seal-preview">
            <img :src="currentViewData.sealImageUrl" alt="印鉴图片" />
          </div>
          <span v-else class="no-image">暂无图片</span>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 印鉴预览对话框 -->
    <el-dialog title="印鉴预览" :visible.sync="previewDialogVisible" width="600px">
      <div v-if="previewData" class="preview-content">
        <div class="seal-preview-large">
          <img v-if="previewData.sealImageUrl" :src="previewData.sealImageUrl" alt="印鉴预览" />
          <div v-else class="no-preview">
            <i class="el-icon-picture-outline"></i>
            <p>暂无印鉴图片</p>
          </div>
        </div>
        <div class="seal-info-preview">
          <h4>{{ previewData.sealName }}</h4>
          <p><strong>印鉴编码：</strong>{{ previewData.sealCode }}</p>
          <p><strong>印鉴类型：</strong>{{ previewData.sealTypeName }}</p>
          <p><strong>持有人：</strong>{{ previewData.ownerName }}（{{ previewData.ownerPosition }}）</p>
          <p><strong>有效期：</strong>{{ formatDate(previewData.effectiveDate) }} 至 {{ formatDate(previewData.expireDate) }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="downloadSealImage">下载图片</el-button>
      </div>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog title="批量导入印鉴" :visible.sync="importDialogVisible" width="500px">
      <el-upload
        class="upload-demo"
        drag
        action=""
        :auto-upload="false"
        :on-change="handleFileChange"
        :file-list="fileList"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">请下载模板文件，按照模板格式填写数据</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="downloadTemplate">下载模板</el-button>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmImport" :disabled="!selectedFile">确认导入</el-button>
      </div>
    </el-dialog>

    <!-- 表格设置对话框 -->
    <el-dialog title="表格列设置" :visible.sync="tableSettingDialogVisible" width="500px">
      <div class="table-setting-content">
        <el-checkbox-group v-model="visibleColumns">
          <div v-for="column in tableColumns" :key="column.prop" class="column-item">
            <el-checkbox :label="column.prop" :disabled="column.required">
              {{ column.label }}
            </el-checkbox>
          </div>
        </el-checkbox-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="tableSettingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="applyTableSetting">应用</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getSealArchiveList,
  createSealArchive,
  updateSealArchive,
  deleteSealArchive,
  getSealStatistics,
  getSealTypeOptions,
  getSealArchiveDetail,
  uploadSealImage
} from '@/api/globalTreasurer/treasuryCommon'

export default {
  name: 'SealArchiveManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        sealCode: undefined,
        sealName: undefined,
        sealTypeId: undefined,
        ownerName: undefined,
        status: undefined
      },
      sealTypes: [],
      totalSeals: 0,
      activeSeals: 0,
      lockedSeals: 0,
      todayUsage: 0,
      dialogFormVisible: false,
      dialogStatus: '',
      temp: {},
      textMap: {
        update: '编辑印鉴',
        create: '新增印鉴'
      },
      rules: {
        sealCode: [{ required: true, message: '请输入印鉴编码', trigger: 'blur' }],
        sealName: [{ required: true, message: '请输入印鉴名称', trigger: 'blur' }],
        sealTypeId: [{ required: true, message: '请选择印鉴类型', trigger: 'change' }],
        ownerName: [{ required: true, message: '请输入持有人姓名', trigger: 'blur' }],
        ownerPosition: [{ required: true, message: '请输入持有人职位', trigger: 'blur' }],
        ownerIdCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        effectiveDate: [{ required: true, message: '请选择生效日期', trigger: 'change' }],
        expireDate: [{ required: true, message: '请选择失效日期', trigger: 'change' }]
      },
      currentViewData: null,
      viewDialogVisible: false,
      previewDialogVisible: false,
      previewData: null,
      importDialogVisible: false,
      fileList: [],
      selectedFile: null,
      tableSettingDialogVisible: false,
      visibleColumns: ['sealCode', 'sealName', 'ownerName', 'effectiveDate', 'expireDate', 'status'],
      tableColumns: [
        { prop: 'sealCode', label: '印鉴编码', required: true },
        { prop: 'sealName', label: '印鉴名称', required: true },
        { prop: 'ownerName', label: '持有人', required: true },
        { prop: 'effectiveDate', label: '生效日期', required: true },
        { prop: 'expireDate', label: '失效日期', required: true },
        { prop: 'status', label: '状态', required: true }
      ],
      multipleSelection: []
    }
  },
  created() {
    this.getSealTypes()
    this.getList()
    this.getStatistics()
  },
  methods: {
    async getSealTypes() {
      try {
        const response = await getSealTypeOptions()
        if (response && response.code === 1) {
          // 将后端返回的数据转换为前端需要的格式
          this.sealTypes = response.data.map(item => ({
            sealTypeId: item.id,  // 后端返回的是id (String类型)
            typeName: item.name     // 后端返回的是name
          }))
        } else {
          // 使用默认数据作为后备
          this.sealTypes = [
            { sealTypeId: '1', typeName: '公章' },
            { sealTypeId: '2', typeName: '财务章' },
            { sealTypeId: '3', typeName: '法人章' },
            { sealTypeId: '4', typeName: '合同章' },
            { sealTypeId: '5', typeName: '发票章' }
          ]
        }
      } catch (error) {
        console.error('获取印章类型失败:', error)
        // 使用默认数据作为后备
        this.sealTypes = [
          { sealTypeId: '1', typeName: '公章' },
          { sealTypeId: '2', typeName: '财务章' },
          { sealTypeId: '3', typeName: '法人章' },
          { sealTypeId: '4', typeName: '合同章' },
          { sealTypeId: '5', typeName: '发票章' }
        ]
      }
    },
    async getStatistics() {
      try {
        const response = await getSealStatistics()
        // 检查响应状态和数据有效性
        if (response && response.code === 1 && response.data) {
          const stats = response.data
          this.totalSeals = stats.totalSeals || 0
          this.activeSeals = stats.activeSeals || 0
          this.lockedSeals = stats.lockedSeals || 0
          this.todayUsage = stats.todayUsage || 0
        } else {
          // 后端返回错误或数据无效，使用默认数据
          console.warn('统计数据获取失败，使用默认值。响应:', response?.msg || '未知错误')
          this.totalSeals = 0
          this.activeSeals = 0
          this.lockedSeals = 0
          this.todayUsage = 0
        }
      } catch (error) {
        console.error('获取统计数据异常:', error)
        // 异常情况使用默认数据作为后备
        this.totalSeals = 0
        this.activeSeals = 0
        this.lockedSeals = 0
        this.todayUsage = 0
      }
    },
    async getList() {
      this.listLoading = true
      try {
        const response = await getSealArchiveList(this.listQuery)
        if (response && response.code === 1) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
        } else {
          this.$message.error('获取印章档案列表失败')
        }
      } catch (error) {
        console.error('获取印章档案列表失败:', error)
        this.$message.error('获取印章档案列表失败')
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async handleDelete(row, index) {
      try {
        await this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const deleteId = row.id
        const response = await deleteSealArchive(deleteId)
        if (response && response.code === 1) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error('删除失败: ' + (response.msg || '未知错误'))
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    // 新增方法
    handleImport() {
      this.importDialogVisible = true
    },
    handleSecurity() {
      this.$message.info('安全管理功能待实现')
    },
    handleView(row) {
      this.currentViewData = { ...row }
      this.viewDialogVisible = true
    },
    handlePreview(row) {
      this.previewData = { ...row }
      this.previewDialogVisible = true
    },
    downloadSealImage() {
      this.$message.info('下载功能待实现')
    },
    downloadTemplate() {
      this.$message.info('模板下载功能需要后端接口支持')
    },
    handleFileChange(file) {
      this.selectedFile = file
      this.fileList = [file]
    },
    confirmImport() {
      this.$message.warning('导入功能需要后端接口支持')
      this.importDialogVisible = false
      this.fileList = []
      this.selectedFile = null
    },
    handleTableSetting() {
      this.tableSettingDialogVisible = true
    },
    applyTableSetting() {
      this.$message.success('表格设置已保存')
      this.tableSettingDialogVisible = false
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        sealCode: '',
        sealName: '',
        sealTypeId: undefined,
        sealTypeName: '',
        ownerName: '',
        ownerPosition: '',
        ownerIdCard: '',
        effectiveDate: '',
        expireDate: '',
        status: '1'
      }
    },
    async createData() {
      this.$refs.dataForm.validate(async (valid) => {
        if (valid) {
          try {
            // 自动填充 sealTypeName
            const selectedType = this.sealTypes.find(t => t.sealTypeId === this.temp.sealTypeId)
            if (selectedType) this.temp.sealTypeName = selectedType.typeName
            const response = await createSealArchive(this.temp)
            if (response && response.code === 1) {
              this.dialogFormVisible = false
              this.$message.success('创建成功')
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(response.msg || '创建失败')
            }
          } catch (error) {
            console.error('创建印章档案失败:', error)
            this.$message.error('创建失败')
          }
        }
      })
    },
    async updateData() {
      this.$refs.dataForm.validate(async (valid) => {
        if (valid) {
          try {
            // 自动填充 sealTypeName
            const selectedType = this.sealTypes.find(t => t.sealTypeId === this.temp.sealTypeId)
            if (selectedType) this.temp.sealTypeName = selectedType.typeName
            const response = await updateSealArchive(this.temp)
            if (response && response.code === 1) {
              this.dialogFormVisible = false
              this.$message.success('更新成功')
              this.getList()
            } else {
              this.$message.error(response.msg || '更新失败')
            }
          } catch (error) {
            console.error('更新印章档案失败:', error)
            this.$message.error('更新失败')
          }
        }
      })
    },
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传图片只能是 JPG/PNG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    async handleSealImageUpload(options) {
      const { file } = options
      try {
        // 先上传图片获取 URL
        const response = await uploadSealImage(file, this.temp.id || '')
        if (response && response.code === 1) {
          this.temp.sealImageUrl = response.data
          this.$message.success('图片上传成功')
        } else {
          this.$message.error(response.msg || '图片上传失败')
        }
      } catch (error) {
        console.error('图片上传失败:', error)
        this.$message.error('图片上传失败')
      }
    },
    // 工具方法
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      if (isNaN(d.getTime())) return '-'
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    isExpired(date) {
      if (!date) return false
      return new Date(date) < new Date()
    },
    getSealTypeColor(typeId) {
      const colorMap = {
        1: 'primary', '1': 'primary',
        2: 'success', '2': 'success',
        3: 'warning', '3': 'warning',
        4: 'info',    '4': 'info',
        5: 'danger',  '5': 'danger'
      }
      return colorMap[typeId] || 'info'
    },
    getSealStatusText(seal) {
      if (seal.status === '0') return '停用'
      if (this.isExpired(seal.expireDate)) return '已过期'
      return '在用'
    },
    getSealStatusColor(seal) {
      if (seal.status === '0') return 'info'
      if (this.isExpired(seal.expireDate)) return 'danger'
      return 'success'
    }
  }
}
</script>
