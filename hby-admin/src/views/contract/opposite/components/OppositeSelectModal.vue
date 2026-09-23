<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="选择相对方"
      append-to-body
      :visible.sync="dialogFormVisible"
      width="900px"
      @close="close"
      v-if="dialogFormVisible"
    >
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-input
          v-model="keywords"
          class="search-input"
          placeholder="请输入企业名称关键词"
          size="medium"
          clearable
          @keyup.enter.native="doSearch"
        >
          <el-button slot="append" icon="el-icon-search" @click="doSearch">企业查询</el-button>
        </el-input>
      </div>

      <!-- 查询结果列表 -->
      <div class="result-area" v-loading="loading">
        <ul v-if="dataList.length > 0">
          <li v-for="(item, idx) in dataList" :key="idx" class="result-item">
            <div class="item-header">
              <span class="unit-name">{{ item.entName || item.name || '-' }}</span>
              <el-button
                type="primary"
                size="mini"
                @click="handleSelect(item)"
                style="margin-left: auto;"
              >选择</el-button>
            </div>
            <el-row :gutter="20" class="item-info">
              <el-col :span="10" class="info-text">
                统一社会信用代码：{{ item.creditCode || '-' }}
              </el-col>
              <el-col :span="8" class="info-text">
                法定代表人：{{ item.legalPerson || '-' }}
              </el-col>
            </el-row>
            <el-row :gutter="20" class="item-btns" v-show="false">
              <el-col :span="3">
                <el-button type="text" @click="handleBusinessInfo(item)">工商数据</el-button>
              </el-col>
            </el-row>
          </li>
        </ul>
        <div
          v-if="!loading && dataList.length === 0 && searched"
          class="empty-tip"
        >
          未查询到相关企业，请尝试其他关键词
        </div>
        <div v-if="!searched && !loading" class="empty-tip">
          请输入企业名称进行查询
        </div>
      </div>

      <template #footer>
        <el-button @click="close">关 闭</el-button>
      </template>
    </el-dialog>

    <!-- 工商基础信息弹窗（独立实例，不影响telescope页面） -->
    <opposite-business-info-dialog ref="businessInfo" @select-company="handleSelectFromBusinessInfo" />
    <!-- 经营信息/经营风险/法律风险弹窗：通过 modal-append-to-body=false 避免遮罩层被父弹窗覆盖 -->
    <opposite-business-dialog ref="business" />
    <opposite-risk-dialog ref="risk" />
    <opposite-legal-dialog ref="legal" />
  </div>
</template>

<script>
  import { searchCjbdiEnterprise, queryCjbdiBusinessInfo } from '@/api/risk/cjbdi'
  import OppositeBusinessInfoDialog from './OppositeBusinessInfoDialog.vue'
  import OppositeBusinessDialog from './OppositeBusinessDialog.vue'
  import OppositeRiskDialog from './OppositeRiskDialog.vue'
  import OppositeLegalDialog from './OppositeLegalDialog.vue'

  export default {
    name: 'OppositeSelectModal',
    components: {
      OppositeBusinessInfoDialog,
      OppositeBusinessDialog,
      OppositeRiskDialog,
      OppositeLegalDialog,
    },
    data() {
      return {
        dialogFormVisible: false,
        keywords: '',
        dataList: [],
        loading: false,
        searched: false,
      }
    },
    methods: {
      showEdit() {
        this.keywords = ''
        this.dataList = []
        this.searched = false
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      async doSearch() {
        if (!this.keywords.trim()) {
          this.$message.warning('请输入企业名称关键词')
          return
        }
        this.loading = true
        this.searched = true
        try {
          const res = await searchCjbdiEnterprise({ keyword: this.keywords.trim() })
          if (res.code === 200 && res.data) {
            this.dataList = Array.isArray(res.data) ? res.data : []
          } else {
            this.dataList = []
            if (res.msg) this.$message.warning(res.msg)
          }
        } catch (e) {
          console.error('查询企业失败', e)
          this.$message.error('查询失败，请稍后重试')
          this.dataList = []
        } finally {
          this.loading = false
        }
      },
      getCompanyName(item) {
        return item.entName || item.name || ''
      },
      getCreditCode(item) {
        return item.creditCode || item.uniscId || ''
      },
      // 点击"选择"按钮：先查工商基础信息，用完整数据回填；接口失败时降级用搜索结果数据
      async handleSelect(item) {
        const companyName = this.getCompanyName(item)
        const creditCode = this.getCreditCode(item)
        this.loading = true
        try {
          const res = await queryCjbdiBusinessInfo({ companyName, creditCode })
          if (res.code === 200 && res.data) {
            const d = res.data
            this.$emit('selectCompany', {
              entName: d.entName || companyName,
              creditCode: d.creditCode || creditCode,
              legalPerson: d.legalPerson || '',
              regCapital: d.regCapital || '',
              establishDate: d.establishDate || '',
              entStatus: d.entStatus || '',
              businessFrom: d.businessFrom || '',
              businessTo: d.businessTo || '',
              province: d.province || '',
              city: d.city || '',
              district: d.district || '',
            })
          } else {
            // 接口未返回数据，降级使用搜索结果字段
            this.$emit('selectCompany', {
              entName: companyName,
              creditCode: creditCode,
              legalPerson: item.legalPerson || '',
              establishDate: item.establishDate || item.estiblishTime || '',
              entStatus: item.entStatus || item.regStatus || '',
              regCapital: item.regCapital || '',
              businessFrom: item.businessFrom || '',
              businessTo: item.businessTo || '',
              province: item.province || item.base || '',
              city: item.city || '',
              district: item.district || '',
            })
          }
        } catch (e) {
          console.error('获取工商信息失败，使用搜索结果数据', e)
          this.$emit('selectCompany', {
            entName: companyName,
            creditCode: creditCode,
            legalPerson: item.legalPerson || '',
            establishDate: item.establishDate || item.estiblishTime || '',
            entStatus: item.entStatus || item.regStatus || '',
            regCapital: item.regCapital || '',
            businessFrom: item.businessFrom || '',
            businessTo: item.businessTo || '',
            province: item.province || item.base || '',
            city: item.city || '',
            district: item.district || '',
          })
        } finally {
          this.loading = false
        }
        this.dialogFormVisible = false
      },
      // 工商信息弹窗中点击"选择并填入"后的回调
      handleSelectFromBusinessInfo(info) {
        this.$emit('selectCompany', info)
        this.dialogFormVisible = false
      },
      handleBusinessInfo(item) {
        this.$refs['businessInfo'].showEdit(this.getCompanyName(item), this.getCreditCode(item))
      },
      handleBusiness(item) {
        this.$refs['business'].showEdit(this.getCompanyName(item), this.getCreditCode(item))
      },
      handleRisk(item) {
        this.$refs['risk'].showEdit(this.getCompanyName(item), this.getCreditCode(item))
      },
      handleLegal(item) {
        this.$refs['legal'].showEdit(this.getCompanyName(item), this.getCreditCode(item))
      },
    },
  }
</script>

<style scoped>
  .search-area {
    padding: 16px 0 20px 0;
    text-align: center;
  }
  .search-input {
    max-width: 520px;
  }
  .result-area {
    min-height: 200px;
    max-height: 480px;
    overflow-y: auto;
  }
  ul {
    margin: 0;
    padding: 0;
  }
  .result-item {
    padding: 14px 16px;
    list-style: none;
    border-bottom: 1px solid #eee;
  }
  .result-item:hover {
    background-color: rgba(24, 144, 255, 0.05);
  }
  .item-header {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }
  .unit-name {
    font-size: 17px;
    color: #333;
    font-weight: 500;
  }
  .item-info {
    margin-bottom: 6px;
  }
  .info-text {
    font-size: 13px;
    color: #666;
  }
  .item-btns {
    margin-top: 8px;
  }
  .empty-tip {
    text-align: center;
    color: #999;
    padding: 40px 0;
  }
</style>
