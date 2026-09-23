<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="基础信息 — 工商基础信息"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <div v-loading="listLoading">
        <el-descriptions title="企业基本信息" :column="2" border v-if="bizInfo.entName">
          <el-descriptions-item label="企业名称">{{ bizInfo.entName }}</el-descriptions-item>
          <el-descriptions-item label="统一社会信用代码">{{ bizInfo.creditCode }}</el-descriptions-item>
          <el-descriptions-item label="法定代表人">{{ bizInfo.legalPerson }}</el-descriptions-item>
          <el-descriptions-item label="注册资本">{{ bizInfo.regCapital }}</el-descriptions-item>
          <el-descriptions-item label="成立日期">{{ bizInfo.establishDate }}</el-descriptions-item>
          <el-descriptions-item label="企业状态">{{ bizInfo.entStatus }}</el-descriptions-item>
          <el-descriptions-item label="企业类型">{{ bizInfo.entType }}</el-descriptions-item>
          <el-descriptions-item label="所属行业">{{ bizInfo.industry }}</el-descriptions-item>
          <el-descriptions-item label="登记机关">{{ bizInfo.regAuthority }}</el-descriptions-item>
          <el-descriptions-item label="核准日期">{{ bizInfo.approvalDate }}</el-descriptions-item>
          <el-descriptions-item label="营业期限" :span="2">{{ bizInfo.businessFrom }} 至 {{ bizInfo.businessTo }}</el-descriptions-item>
          <el-descriptions-item label="企业地址" :span="2">{{ bizInfo.address }}</el-descriptions-item>
          <el-descriptions-item label="经营范围" :span="2">{{ bizInfo.businessScope }}</el-descriptions-item>
        </el-descriptions>
        <div v-else style="text-align: center; padding: 40px; color: #999;">
          暂无工商基础信息
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { queryCjbdiData, getCjbdiDetail } from '@/api/risk/cjbdi'

  export default {
    name: 'CjbdiBaseInfoDialog',
    data() {
      return {
        dialogFormVisible: false,
        listLoading: false,
        companyName: '',
        creditCode: '',
        bizInfo: {},
      }
    },
    methods: {
      async showEdit(name, creditCode) {
        this.companyName = name
        this.creditCode = creditCode
        this.dialogFormVisible = true
        this.listLoading = true
        this.bizInfo = {}
        try {
          // 调用CJBDI查询接口，categoryId=1(工商基础信息)
          const res = await queryCjbdiData({
            companyName: name,
            creditCode: creditCode,
            categoryIds: [1],
          })
          if (res.code === 200 && res.data && res.data.length > 0) {
            const result = res.data[0]
            if (result.status === 1 && result.dataCount > 0) {
              // 优先使用查询结果中已附带的detailItems（telescope场景companyId为null）
              if (result.detailItems) {
                const items = Array.isArray(result.detailItems) ? result.detailItems : [result.detailItems]
                if (items.length > 0) {
                  this.bizInfo = items[0]
                }
              } else if (result.companyId) {
                // 有companyId时走detail接口
                const detailRes = await getCjbdiDetail({ companyId: result.companyId, categoryId: 1 })
                if (detailRes.code === 200 && detailRes.data && detailRes.data.items && detailRes.data.items.length > 0) {
                  this.bizInfo = detailRes.data.items[0]
                }
              }
            }
          }
        } catch (e) {
          console.error('查询工商基础信息失败', e)
          this.$message.error('查询失败')
        } finally {
          this.listLoading = false
        }
      },
      close() {
        this.dialogFormVisible = false
        this.bizInfo = {}
      },
    },
  }
</script>

