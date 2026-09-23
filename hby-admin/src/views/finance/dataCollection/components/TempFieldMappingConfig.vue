<template>
  <div class="temp-field-mapping-config">
    <el-card class="mapping-card">
      <div slot="header" class="clearfix">
        <span class="title">临时字段映射配置</span>
        <el-button
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="addMapping"
          style="float: right"
        >
          添加映射
        </el-button>
      </div>

      <el-table
        :data="mappingList"
        stripe
        border
        max-height="400"
        :default-sort="{ prop: 'sortOrder', order: 'ascending' }"
      >
        <el-table-column prop="sourceTableName" label="源表名" width="140">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.sourceTableName"
              size="small"
              placeholder="源表名"
            />
          </template>
        </el-table-column>

        <el-table-column prop="sourceFieldName" label="源字段名" width="140">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.sourceFieldName"
              size="small"
              placeholder="源字段名"
            />
          </template>
        </el-table-column>

        <el-table-column prop="targetTableName" label="目标表名" width="140">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.targetTableName"
              size="small"
              placeholder="目标表名"
            />
          </template>
        </el-table-column>

        <el-table-column prop="targetFieldName" label="目标字段名" width="140">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.targetFieldName"
              size="small"
              placeholder="目标字段名"
            />
          </template>
        </el-table-column>

        <el-table-column prop="calculationLogic" label="计算逻辑" width="180">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.calculationLogic"
              size="small"
              placeholder="如: UPPER(字段名)"
            />
          </template>
        </el-table-column>

        <el-table-column prop="queryCondition" label="查询条件" width="180">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.queryCondition"
              size="small"
              placeholder="如: STATUS = 1"
            />
          </template>
        </el-table-column>

        <el-table-column prop="fieldType" label="字段类型" width="120">
          <template slot-scope="scope">
            <el-select v-model="scope.row.fieldType" size="small" placeholder="字段类型">
              <el-option label="VARCHAR2" value="VARCHAR2" />
              <el-option label="NUMBER" value="NUMBER" />
              <el-option label="DATE" value="DATE" />
              <el-option label="CLOB" value="CLOB" />
              <el-option label="BLOB" value="BLOB" />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column prop="fieldLength" label="字段长度" width="100">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.fieldLength"
              size="small"
              :min="1"
              :max="4000"
              controls-position="right"
            />
          </template>
        </el-table-column>

        <el-table-column prop="isKey" label="主键" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isKey"
              active-value="Y"
              inactive-value="N"
            />
          </template>
        </el-table-column>

        <el-table-column prop="isRequired" label="必填" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isRequired"
              active-value="Y"
              inactive-value="N"
            />
          </template>
        </el-table-column>

        <el-table-column prop="sortOrder" label="排序" width="80">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.sortOrder"
              size="small"
              :min="0"
              controls-position="right"
            />
          </template>
        </el-table-column>

        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-delete"
              @click="deleteMapping(scope.$index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'TempFieldMappingConfig',
  props: {
    value: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      mappingList: []
    }
  },
  watch: {
    value: {
      handler(newVal) {
        this.mappingList = newVal || []
      },
      immediate: true,
      deep: true
    },
    mappingList: {
      handler(newVal) {
        this.$emit('input', newVal)
      },
      deep: true
    }
  },
  methods: {
    addMapping() {
      this.mappingList.push({
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
        sortOrder: this.mappingList.length
      })
    },
    deleteMapping(index) {
      this.$confirm('确定删除该映射配置吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.mappingList.splice(index, 1)
        // 重新排序
        this.mappingList.forEach((item, idx) => {
          item.sortOrder = idx
        })
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    validate() {
      if (!this.mappingList || this.mappingList.length === 0) {
        return true // 允许为空
      }
      
      for (let mapping of this.mappingList) {
        if (!mapping.sourceTableName || !mapping.sourceFieldName ||
            !mapping.targetTableName || !mapping.targetFieldName) {
          this.$message.error('源表名、源字段名、目标表名、目标字段名不能为空')
          return false
        }
      }
      return true
    },
    getData() {
      return this.mappingList
    }
  }
}
</script>

<style scoped lang="scss">
.temp-field-mapping-config {
  .mapping-card {
    margin-top: 10px;
    
    .title {
      font-weight: bold;
      font-size: 14px;
    }
  }
}
</style>

