<template>
  <div class="counterpart-selector">
    <el-select
      v-model="selectedCounterpartId"
      placeholder="请选择相对方"
      style="width: 100%"
      filterable
      remote
      :remote-method="handleSearch"
      :loading="loading"
      clearable
      @change="handleChange"
      @clear="handleClear"
    >
      <el-option
        v-for="item in counterpartOptions"
        :key="item.budgetid"
        :label="`${item.budgetname}${item.counterpartno ? ' (' + item.counterpartno + ')' : ''}`"
        :value="item.budgetid"
      >
        <div class="counterpart-option">
          <div class="counterpart-name">{{ item.budgetname }}</div>
          <div class="counterpart-info">
            <span class="company-code">{{ item.counterpartno || '无编码' }}</span>
            <span class="contact-info" v-if="item.contactperson">
              {{ item.contactperson }}
              <span v-if="item.contactphone">{{ item.contactphone }}</span>
            </span>
          </div>
        </div>
      </el-option>
    </el-select>


  </div>
</template>

<script>
import {
  getCounterpartList,
  searchCounterpartOptions
} from '@/api/contract/riskAssessment'

export default {
  name: 'CounterpartSelector',
  props: {
    value: {
      type: [String, Number],
      default: null
    },

    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      selectedCounterpartId: this.value,
      counterpartOptions: [],
      loading: false
    }
  },
  watch: {
    value(newVal) {
      this.selectedCounterpartId = newVal
    }
  },
  mounted() {
    // 初始加载相对方选项
    this.loadCounterpartOptions()
  },
  methods: {
    // 判断响应是否成功
    isSuccessResponse(response) {
      // 检查多种成功状态码
      const successCodes = [200, 0, '200', '0', '1', 1]
      return successCodes.includes(response.code) || successCodes.includes(response.status)
    },
    // 加载相对方选项
    async loadCounterpartOptions() {
      try {
        this.loading = true
        const response = await getCounterpartList({
          pageNumber: 1,
          pageSize: 50
        })

        console.log('加载相对方选项响应:', response)

        // 解析响应数据
        if (this.isSuccessResponse(response)) {
          // 处理不同的数据结构
          let list = []
          if (response.data && response.data.list) {
            list = response.data.list
          } else if (response.data && Array.isArray(response.data)) {
            list = response.data
          } else if (Array.isArray(response.data)) {
            list = response.data
          }

          // 使用原有相对方模块的字段映射
          this.counterpartOptions = list.map(item => ({
            budgetid: item.budgetid,
            budgetname: item.budgetname,
            counterpartno: item.counterpartno,
            contactperson: item.contactperson,
            contactphone: item.contactphone,
            contactemail: item.contactemail,
            contactaddress: item.contactaddress,
            legalrepresentative: item.legalrepresentative,
            registeredcapital: item.registeredcapital
          }))

          console.log('相对方选项数据:', this.counterpartOptions)
        } else {
          console.error('加载相对方选项失败:', response.message || response.msg)
          this.counterpartOptions = []
          this.$message.error(response.message || response.msg || '加载相对方选项失败')
        }
      } catch (error) {
        console.error('加载相对方选项失败：', error)
        this.counterpartOptions = []
        this.$message.error('加载相对方选项失败：' + (error.message || '网络错误'))
      } finally {
        this.loading = false
      }
    },

    // 搜索相对方
    async handleSearch(query) {
      if (query && query.trim()) {
        try {
          this.loading = true
          const response = await searchCounterpartOptions(query.trim(), 20)
          console.log('搜索相对方响应:', response)

          // 解析响应数据
          if (this.isSuccessResponse(response)) {
            // 处理不同的数据结构
            let list = []
            if (response.data && Array.isArray(response.data)) {
              list = response.data
            } else if (response.data && response.data.list) {
              list = response.data.list
            }

            // 使用原有相对方模块的字段映射
            this.counterpartOptions = list.map(item => ({
              budgetid: item.budgetid,
              budgetname: item.budgetname,
              counterpartno: item.counterpartno,
              contactperson: item.contactperson,
              contactphone: item.contactphone,
              contactemail: item.contactemail,
              contactaddress: item.contactaddress,
              legalrepresentative: item.legalrepresentative,
              registeredcapital: item.registeredcapital
            }))

            console.log('搜索相对方结果:', this.counterpartOptions)
          } else {
            console.error('搜索相对方失败:', response.message || response.msg)
            this.counterpartOptions = []
            this.$message.error(response.message || response.msg || '搜索相对方失败')
          }
        } catch (error) {
          console.error('搜索相对方失败：', error)
          this.counterpartOptions = []
          this.$message.error('搜索相对方失败：' + (error.message || '网络错误'))
        } finally {
          this.loading = false
        }
      } else {
        // 如果搜索关键词为空，加载默认选项
        this.loadCounterpartOptions()
      }
    },

    // 选择变化处理
    handleChange(value) {
      this.selectedCounterpartId = value
      this.$emit('input', value)
      this.$emit('change', value)
      
      // 获取选中的相对方信息
      const selectedCounterpart = this.counterpartOptions.find(item => item.budgetid === value)
      this.$emit('counterpart-selected', selectedCounterpart)
    },

    // 清除选择
    handleClear() {
      this.selectedCounterpartId = null
      this.$emit('input', null)
      this.$emit('change', null)
      this.$emit('counterpart-selected', null)
    },


  }
}
</script>

<style scoped>
.counterpart-selector {
  display: flex;
  align-items: center;
}

.counterpart-option {
  width: 100%;
}

.counterpart-name {
  font-weight: 500;
  color: #303133;
}

.counterpart-info {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.company-code {
  margin-right: 10px;
}

.contact-info {
  font-style: italic;
}

.dialog-footer {
  text-align: right;
}
</style>
