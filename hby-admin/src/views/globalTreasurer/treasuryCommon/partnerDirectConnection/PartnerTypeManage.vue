<template>
  <div class="partner-type-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-user-solid"></i>
            合作伙伴类型管理
          </h2>
          <p class="page-description">
            管理合作伙伴类型分类，包括类型定义、权限配置、业务范围和风险等级
          </p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增类型
          </el-button>
          <el-button type="success" icon="el-icon-sort" @click="handleSort">
            排序管理
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 类型统计卡片 -->
    <div class="type-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-user-solid"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总类型数</div>
                <div class="card-value">{{ totalPartnerTypes }}</div>
                <div class="card-change">已配置类型</div>
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
                <div class="card-title">金融机构</div>
                <div class="card-value">{{ financialTypes }}</div>
                <div class="card-change positive">银行类型</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon business-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-info">
                <div class="card-title">商业伙伴</div>
                <div class="card-value">{{ businessTypes }}</div>
                <div class="card-change">业务类型</div>
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
                <div class="card-title">启用类型</div>
                <div class="card-value">{{ activeTypes }}</div>
                <div class="card-change positive">正常使用</div>
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
          <el-form-item label="类型编码">
            <el-input
              v-model="listQuery.typeCode"
              placeholder="请输入类型编码"
              style="width: 150px"
              clearable
            />
          </el-form-item>
          <el-form-item label="类型名称">
            <el-input
              v-model="listQuery.typeName"
              placeholder="请输入类型名称"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.isEnabled"
              placeholder="请选择状态"
              clearable
              style="width: 100px"
            >
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="0" />
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
        label="类型编码"
        prop="typeCode"
        sortable="custom"
        align="center"
        width="150"
      >
        <template slot-scope="{ row }">
          <span>{{ row.typeCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型名称" width="200px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.typeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" min-width="300px">
        <template slot-scope="{ row }">
          <span>{{ row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{ row }">
          <span>
            {{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" width="180px" align="center">
        <template slot-scope="{ row }">
          <span>
            {{ row.updateTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{ row }">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
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
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="typeForm"
        :rules="rules"
        :model="tempType"
        :label-width="formLabelWidth"
      >
        <el-form-item label="类型编码" prop="typeCode">
          <el-input
            v-model="tempType.typeCode"
            placeholder="请输入类型编码"
            :disabled="dialogStatus === 'update'"
          />
        </el-form-item>
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="tempType.typeName" placeholder="请输入类型名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="tempType.description"
            type="textarea"
            :rows="3"
            placeholder="请输入类型描述"
          />
        </el-form-item>
        <el-form-item label="上级类型">
          <el-select
            v-model="tempType.parentTypeId"
            placeholder="请选择上级类型"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="type in list.filter(
                (item) => item.partnerTypeId !== tempType.partnerTypeId
              )"
              :key="type.partnerTypeId"
              :label="type.typeName"
              :value="type.partnerTypeId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number
            v-model="tempType.sortOrder"
            :min="0"
            style="width: 100%"
            placeholder="请输入排序"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="tempType.isEnabled"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
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

    <!-- 排序对话框 -->
    <el-dialog title="排序管理" :visible.sync="sortDialogVisible" width="600px">
      <el-table :data="sortList" border>
        <el-table-column label="类型名称" prop="typeName" />
        <el-table-column label="排序" width="120">
          <template slot-scope="{ row }">
            <el-input-number v-model="row.sortOrder" :min="1" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="{ row, $index }">
            <el-button
              size="mini"
              type="primary"
              @click="row.sortOrder = $index + 1"
            >
              重置
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sortDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSort">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import waves from '@/directive/waves'
  import Pagination from '@/components/Pagination'
  import {
    getPartnerTypeList,
    getPartnerTypeDetail,
    addPartnerType,
    updatePartnerType,
    deletePartnerType,
    updateTypeStatus,
    updateSortOrder,
    getStatistics,
    exportTypes,
  } from '@/api/globalTreasurer/partnerDirectConnectionPartnerTypeManage'

  export default {
    name: 'PartnerTypeManage',
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
          typeCode: undefined,
          typeName: undefined,
          isEnabled: undefined,
        },
        // 统计数据
        totalPartnerTypes: 0,
        financialTypes: 0,
        businessTypes: 0,
        activeTypes: 0,
        // 对话框相关
        dialogVisible: false,
        dialogStatus: '',
        dialogTitle: '',
        formLabelWidth: '120px',
        // 表单数据
        tempType: {
          partnerTypeId: undefined,
          typeCode: '',
          typeName: '',
          description: '',
          parentTypeId: undefined,
          sortOrder: 0,
          isEnabled: 1,
        },
        // 表单验证规则
        rules: {
          typeCode: [
            { required: true, message: '请输入类型编码', trigger: 'blur' },
          ],
          typeName: [
            { required: true, message: '请输入类型名称', trigger: 'blur' },
          ],
        },
        // 排序对话框
        sortDialogVisible: false,
        sortList: [],
      }
    },
    created() {
      this.getStatistics()
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        getPartnerTypeList(this.listQuery)
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
                partnerTypeId: 1,
                typeCode: 'BANK',
                typeName: '银行',
                description: '银行类金融机构，包括商业银行、政策性银行等',
                isEnabled: 1,
                createTime: '2024-01-01 10:00:00',
                updateTime: '2024-09-25 15:30:00',
                createUser: 1,
                updateUser: 1,
              },
              {
                partnerTypeId: 2,
                typeCode: 'FINANCIAL',
                typeName: '金融机构',
                description:
                  '非银行金融机构，包括证券公司、保险公司、基金公司等',
                isEnabled: 1,
                createTime: '2024-01-01 10:00:00',
                updateTime: '2024-09-25 15:30:00',
                createUser: 1,
                updateUser: 1,
              },
              {
                partnerTypeId: 3,
                typeCode: 'SUPPLIER',
                typeName: '供应商',
                description: '为企业提供商品或服务的供应商伙伴',
                isEnabled: 1,
                createTime: '2024-01-01 10:00:00',
                updateTime: '2024-09-25 15:30:00',
                createUser: 1,
                updateUser: 1,
              },
              {
                partnerTypeId: 4,
                typeCode: 'CUSTOMER',
                typeName: '客户',
                description: '企业的客户伙伴，包括个人客户和企业客户',
                isEnabled: 1,
                createTime: '2024-01-01 10:00:00',
                updateTime: '2024-09-25 15:30:00',
                createUser: 1,
                updateUser: 1,
              },
              {
                partnerTypeId: 5,
                typeCode: 'OTHER',
                typeName: '其他',
                description: '其他类型的业务伙伴',
                isEnabled: 0,
                createTime: '2024-01-01 10:00:00',
                updateTime: '2024-09-25 15:30:00',
                createUser: 1,
                updateUser: 1,
              },
            ]
            this.total = 5
            this.listLoading = false
          })
      },
      getStatistics() {
        getStatistics()
          .then((response) => {
            if (response.code === 1) {
              const stats = response.data
              this.totalPartnerTypes = stats.totalPartnerTypes || 0
              this.financialTypes = stats.financialTypes || 0
              this.businessTypes = stats.businessTypes || 0
              this.activeTypes = stats.activeTypes || 0
            }
          })
          .catch(() => {
            // 使用默认统计数据
            this.totalPartnerTypes = 5
            this.financialTypes = 2
            this.businessTypes = 2
            this.activeTypes = 4
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
          typeCode: undefined,
          typeName: undefined,
          isEnabled: undefined,
        }
        this.getList()
      },
      resetTemp() {
        this.tempType = {
          partnerTypeId: undefined,
          typeCode: '',
          typeName: '',
          description: '',
          parentTypeId: undefined,
          sortOrder: 0,
          isEnabled: 1,
        }
      },
      handleCreate() {
        this.resetTemp()
        this.dialogStatus = 'create'
        this.dialogTitle = '新增合作伙伴类型'
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs['typeForm'] && this.$refs['typeForm'].clearValidate()
        })
      },
      handleUpdate(row) {
        this.tempType = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogTitle = '编辑合作伙伴类型'
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs['typeForm'] && this.$refs['typeForm'].clearValidate()
        })
      },
      createData() {
        this.$refs['typeForm'].validate((valid) => {
          if (valid) {
            const typeData = Object.assign({}, this.tempType)
            delete typeData.partnerTypeId

            addPartnerType(typeData)
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
        this.$refs['typeForm'].validate((valid) => {
          if (valid) {
            const typeData = Object.assign({}, this.tempType)

            updatePartnerType(typeData)
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
          deletePartnerType({ partnerTypeId: row.partnerTypeId })
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
      handleStatusChange(row) {
        updateTypeStatus({
          partnerTypeId: row.partnerTypeId,
          isEnabled: row.isEnabled,
        })
          .then((response) => {
            if (response.code === 1) {
              this.$notify({
                title: '成功',
                message: '状态更新成功',
                type: 'success',
                duration: 2000,
              })
              this.getStatistics()
            } else {
              // 恢复原状态
              row.isEnabled = row.isEnabled === 1 ? 0 : 1
              this.$message.error(response.message || '状态更新失败')
            }
          })
          .catch(() => {
            // 恢复原状态
            row.isEnabled = row.isEnabled === 1 ? 0 : 1
            this.$message.error('状态更新失败')
          })
      },
      handleSort() {
        this.sortList = this.list.map((item, index) => ({
          partnerTypeId: item.partnerTypeId,
          typeName: item.typeName,
          sortOrder: item.sortOrder || index + 1,
        }))
        this.sortDialogVisible = true
      },
      confirmSort() {
        const sortData = this.sortList.map((item) => ({
          partnerTypeId: item.partnerTypeId,
          sortOrder: item.sortOrder,
        }))
        updateSortOrder({ sortData: sortData })
          .then((response) => {
            if (response.code === 1) {
              this.sortDialogVisible = false
              this.$notify({
                title: '成功',
                message: '排序更新成功',
                type: 'success',
                duration: 2000,
              })
              this.getList()
            } else {
              this.$message.error(response.message || '排序更新失败')
            }
          })
          .catch(() => {
            this.$message.error('排序更新失败')
          })
      },
      handleExport() {
        this.$message.info('导出功能开发中...')
        // exportTypes(this.listQuery).then(response => {
        //   if (response.code === 1) {
        //     window.open(response.data.exportPath)
        //   } else {
        //     this.$message.error(response.message || '导出失败')
        //   }
        // })
      },
    },
  }
</script>

<style scoped lang="scss">
  @import './partnerManage.scss';

  /* 页面特定样式 */
  .partner-type-manage {
    padding: 20px;
    min-height: calc(100vh - 84px);
  }

  .type-overview .overview-card {
    height: 120px;
  }

  /* 状态开关样式 */
  .el-switch.is-checked .el-switch__core {
    background-color: #67c23a;
    border-color: #67c23a;
  }

  .el-switch__label.is-active {
    color: #67c23a;
  }

  /* 表格操作列样式 */
  .el-table .small-padding .cell {
    padding-left: 5px;
    padding-right: 5px;
  }
</style>
