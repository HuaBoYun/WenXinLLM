<template>
  <div class="company-selector">
    <el-select
      v-model="selectedId"
      :placeholder="placeholder"
      :clearable="clearable"
      :filterable="filterable"
      :remote="remote"
      :remote-method="remoteSearch"
      :loading="loading"
      :size="size"
      :style="{ width: width }"
      @change="handleChange"
    >
      <el-option
        v-for="item in companyList"
        :key="item.companyId"
        :label="item.companyName"
        :value="item.companyId"
      >
        <span>{{ item.companyName }}</span>
        <span v-if="item.companyCode" style="float: right; color: #8492a6; font-size: 12px">{{ item.companyCode }}</span>
      </el-option>
    </el-select>
  </div>
</template>
<script>
/**
 * 企业选择器组件
 * 支持远程搜索、本地过滤，统一企业选择交互
 * Props:
 *   value       - 绑定值（企业ID）
 *   placeholder - 占位文本
 *   clearable   - 是否可清空
 *   filterable  - 是否可搜索
 *   remote      - 是否远程搜索
 *   size        - 尺寸
 *   width       - 宽度
 *   loadCompanies - 加载企业列表的函数，返回 Promise<[{ companyId, companyName, companyCode }]>
 * Events:
 *   input  - v-model 更新
 *   change - 选中值变化，参数为选中的企业对象
 */
export default {
  name: 'CompanySelector',
  props: {
    value: { type: String, default: '' },
    placeholder: { type: String, default: '请选择企业' },
    clearable: { type: Boolean, default: true },
    filterable: { type: Boolean, default: true },
    remote: { type: Boolean, default: false },
    size: { type: String, default: 'small' },
    width: { type: String, default: '260px' },
    loadCompanies: { type: Function, default: null },
  },
  data() {
    return {
      selectedId: this.value,
      companyList: [],
      loading: false,
    }
  },
  watch: {
    value(val) { this.selectedId = val },
  },
  created() {
    if (!this.remote) { this.fetchCompanies() }
  },
  methods: {
    async fetchCompanies(query) {
      if (!this.loadCompanies) return
      this.loading = true
      try {
        const list = await this.loadCompanies(query)
        this.companyList = list || []
      } catch (e) {
        this.companyList = []
      } finally {
        this.loading = false
      }
    },
    remoteSearch(query) {
      if (query) { this.fetchCompanies(query) }
      else { this.companyList = [] }
    },
    handleChange(val) {
      this.$emit('input', val)
      const selected = this.companyList.find(c => c.companyId === val)
      this.$emit('change', selected || null)
    },
  },
}
</script>
<style scoped>
.company-selector { display: inline-block; }
</style>

