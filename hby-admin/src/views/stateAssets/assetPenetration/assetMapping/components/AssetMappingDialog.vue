<template>
  <el-dialog
    title="资产映射详情"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form :model="form" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产编号">
            <el-input v-model="form.assetCode" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产名称">
            <el-input v-model="form.assetName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产类型">
            <el-select v-model="form.assetType" placeholder="请选择资产类型">
              <el-option label="固定资产" value="fixed"></el-option>
              <el-option label="流动资产" value="current"></el-option>
              <el-option label="无形资产" value="intangible"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产价值">
            <el-input v-model="form.assetValue" type="number">
              <template slot="append">万元</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="映射关系">
        <el-table :data="form.mappingRelations" border>
          <el-table-column prop="targetEntity" label="目标实体"></el-table-column>
          <el-table-column prop="relationType" label="关系类型"></el-table-column>
          <el-table-column prop="mappingRatio" label="映射比例"></el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="editMapping(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="saveMapping">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AssetMappingDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    mappingData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      form: {
        assetCode: 'AST001',
        assetName: '办公楼',
        assetType: 'fixed',
        assetValue: 5000,
        mappingRelations: [
          { targetEntity: '子公司A', relationType: '直接持有', mappingRatio: '60%' },
          { targetEntity: '子公司B', relationType: '间接持有', mappingRatio: '40%' }
        ]
      }
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
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    editMapping(row) {
      this.$message.info('编辑映射关系：' + row.targetEntity)
    },
    saveMapping() {
      this.$message.success('保存资产映射成功')
      this.handleClose()
    }
  }
}
</script>
