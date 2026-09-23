<template>
  <el-dialog
    title="版本字段映射配置"
    :visible.sync="dialogVisible"
    width="95%"
    @close="handleClose"
  >
    <div class="mapping-container">
      <!-- 版本信息展示 -->
      <el-card class="info-card">
        <div slot="header" class="clearfix">
          <span class="title">版本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :lg="6" :md="12">
            <div class="info-item">
              <span class="label">财务系统:</span>
              <span class="value">{{ versionInfo.financeSystem }}</span>
            </div>
          </el-col>
          <el-col :lg="6" :md="12">
            <div class="info-item">
              <span class="label">版本名称:</span>
              <span class="value">{{ versionInfo.versionName }}</span>
            </div>
          </el-col>
          <el-col :lg="12" :md="24">
            <div class="info-item">
              <span class="label">版本ID:</span>
              <span class="value">{{ versionInfo.versionFid }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 字段映射配置表 -->
      <el-card class="mapping-card">
        <div slot="header" class="clearfix">
          <span class="title">字段映射配置</span>
          <el-button
            type="primary"
            size="small"
            @click="addMapping"
            style="float: right"
          >
            + 添加映射
          </el-button>
        </div>

        <el-table :data="mappingList" stripe border max-height="500" :default-sort="{ prop: 'sortOrder', order: 'ascending' }">
          <el-table-column prop="sourceTableName" label="源表名" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sourceTableName" size="small" placeholder="源表名" />
            </template>
          </el-table-column>
          <el-table-column prop="sourceFieldName" label="源字段名" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sourceFieldName" size="small" placeholder="源字段名" />
            </template>
          </el-table-column>
          <el-table-column prop="targetTableName" label="目标表名" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.targetTableName" size="small" placeholder="目标表名" />
            </template>
          </el-table-column>
          <el-table-column prop="targetFieldName" label="目标字段名" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.targetFieldName" size="small" placeholder="目标字段名" />
            </template>
          </el-table-column>
          <el-table-column prop="fieldType" label="字段类型" width="130" align="center">
            <template slot-scope="scope">
              <el-select v-model="scope.row.fieldType" size="small" style="width: 100%">
                <el-option label="VARCHAR2" value="VARCHAR2" />
                <el-option label="NUMBER" value="NUMBER" />
                <el-option label="DATE" value="DATE" />
                <el-option label="TIMESTAMP" value="TIMESTAMP" />
                <el-option label="CLOB" value="CLOB" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="fieldLength" label="长度" width="100" align="center">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.fieldLength" size="small" type="number" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column prop="isKey" label="主键" width="90" align="center">
            <template slot-scope="scope">
              <el-select v-model="scope.row.isKey" size="small" style="width: 100%">
                <el-option label="是" value="Y" />
                <el-option label="否" value="N" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="isRequired" label="必填" width="90" align="center">
            <template slot-scope="scope">
              <el-select v-model="scope.row.isRequired" size="small" style="width: 100%">
                <el-option label="是" value="Y" />
                <el-option label="否" value="N" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="calculationLogic" label="计算逻辑" min-width="160">
            <template slot-scope="scope">
              <el-input v-model="scope.row.calculationLogic" size="small" placeholder="如: ROUND(AMOUNT, 2)" />
            </template>
          </el-table-column>
          <el-table-column prop="queryCondition" label="查询条件" min-width="160">
            <template slot-scope="scope">
              <el-input v-model="scope.row.queryCondition" size="small" placeholder="如: STATUS = 1" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button
                type="danger"
                size="mini"
                @click="deleteMapping(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="saveMapping">保存配置</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getByVersionFid, saveBatch, deleteByVersionFid, deleteMapping } from '@/api/finance/versionFieldMapping'

export default {
  name: 'VersionFieldMappingModal',
  data() {
    return {
      dialogVisible: false,
      versionInfo: {
        versionFid: '',
        financeSystem: '',
        versionName: ''
      },
      mappingList: [],
      deletedMappingIds: [] // 记录被删除的映射ID
    }
  },
  methods: {
    show(row) {
      this.versionInfo = {
        versionFid: row.fid,
        financeSystem: row.pid ? '财务软件' : '',
        versionName: row.handtext
      }
      this.loadMappings()
      this.dialogVisible = true
    },
    loadMappings() {
      getByVersionFid(this.versionInfo.versionFid).then(res => {
        if (res.code === 1 && res.data) {
          this.mappingList = res.data
        } else {
          this.mappingList = []
        }
      }).catch(err => {
        console.error(err)
        this.mappingList = []
      })
    },
    addMapping() {
      this.mappingList.push({
        mappingId: '',
        versionFid: this.versionInfo.versionFid,
        sourceTableName: '',
        sourceFieldName: '',
        targetTableName: '',
        targetFieldName: '',
        calculationLogic: '',
        queryCondition: '',
        fieldType: 'VARCHAR2',
        fieldLength: 100,
        isKey: 'N',
        isRequired: 'N',
        sortOrder: this.mappingList.length,
        status: 'ACTIVE'
      })
    },
    deleteMapping(index) {
      this.$confirm('确定删除该映射吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const mapping = this.mappingList[index]
        // 如果映射有 mappingId，说明是已保存的记录，需要记录下来以便后续删除
        if (mapping.mappingId && mapping.mappingId.trim() !== '') {
          this.deletedMappingIds.push(mapping.mappingId)
        }
        // 从前端列表中删除
        this.mappingList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    saveMapping() {
      if (!this.mappingList || this.mappingList.length === 0) {
        this.$message.warning('请至少添加一条映射配置')
        return
      }

      // 验证必填字段
      for (let mapping of this.mappingList) {
        if (!mapping.sourceTableName || !mapping.sourceFieldName ||
            !mapping.targetTableName || !mapping.targetFieldName) {
          this.$message.error('源表名、源字段名、目标表名、目标字段名不能为空')
          return
        }
      }

      // 先删除被标记删除的映射
      const deletePromises = this.deletedMappingIds.map(mappingId => {
        return deleteMapping(mappingId)
          .catch(err => {
            console.error(`删除映射 ${mappingId} 失败:`, err)
            // 继续执行，不中断流程
          })
      })

      Promise.all(deletePromises).then(() => {
        // 删除完成后，保存新增和修改的映射
        return saveBatch(this.mappingList)
      }).then(res => {
        if (res.code === 1) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.$emit('refresh')
        } else {
          this.$message.error(res.msg || '保存失败')
        }
      }).catch(err => {
        this.$message.error('保存失败: ' + err.message)
      })
    },
    handleClose() {
      this.versionInfo = {
        versionFid: '',
        financeSystem: '',
        versionName: ''
      }
      this.mappingList = []
      this.deletedMappingIds = []
    }
  }
}
</script>

<style scoped lang="scss">
.mapping-container {
  .info-card {
    margin-bottom: 20px;

    .title {
      font-weight: bold;
      font-size: 14px;
    }

    .info-item {
      padding: 5px 0;

      .label {
        font-weight: bold;
        margin-right: 10px;
        color: #606266;
      }

      .value {
        color: #303133;
      }
    }
  }

  .mapping-card {
    .title {
      font-weight: bold;
      font-size: 14px;
    }

    // 优化表格显示
    ::v-deep .el-table {
      font-size: 12px;

      .el-table__header-wrapper {
        overflow-x: auto;
      }

      .el-table__body-wrapper {
        overflow-x: auto;
      }

      .el-table__cell {
        padding: 8px 4px;
      }

      .el-input__inner,
      .el-select {
        font-size: 12px;
      }
    }
  }
}
</style>

