<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="70%"
    @close="handleClose"
  >
    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="no" label="风险编号" width="180" align="center" />
      <el-table-column
        prop="name"
        label="风险名称"
        width="180"
        align="center"
      />
      <el-table-column
        prop="msg"
        label="催办内容"
        min-width="200"
        align="center"
      />
      <el-table-column
        prop="time"
        label="催办时间"
        width="120"
        align="center"
      />
    </el-table>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import {
    getControlPenetrateList,
    getMonthlyPenetrateList,
  } from '@/api/risk/home.js'

  export default {
    name: 'ReminderDetailDialog',
    data() {
      return {
        visible: false,
        loading: false,
        tableData: [],
        dialogTitle: '',
        currentCategory: '', // 'integrated' 或 'monthly'
        currentType: null, // 1-催办总数量 2-完成数量 3-未完成数量 4-超期数量
      }
    },
    methods: {
      /**
       * 打开弹窗
       * @param {String} category - 类别: 'integrated'(一体化管控措施) 或 'monthly'(月度评估)
       * @param {Number} type - 类型: 1-催办总数量 2-完成数量 3-未完成数量 4-超期数量
       * @param {String} seriesName - 系列名称(完成数量/未完成数量/超期数量/总数量)
       */
      open(category, type, seriesName) {
        this.visible = true
        this.currentCategory = category
        this.currentType = type

        // 设置弹窗标题
        const categoryText =
          category === 'integrated' ? '一体化管控措施' : '月度评估'
        this.dialogTitle = `${categoryText} - ${seriesName}`

        // 加载数据
        this.fetchData()
      },

      // 获取列表数据
      async fetchData() {
        this.loading = true
        try {
          let response

          // 根据类别调用不同的接口
          if (this.currentCategory === 'integrated') {
            response = await getControlPenetrateList({ type: this.currentType })
          } else {
            response = await getMonthlyPenetrateList({ type: this.currentType })
          }

          if (response.code === 200 && response.data) {
            this.tableData = response.data
          } else {
            this.tableData = []
            this.$message.warning(response.msg || '获取数据失败')
          }
        } catch (error) {
          console.error('获取列表数据失败:', error)
          this.$message.error('获取数据失败')
          this.tableData = []
        } finally {
          this.loading = false
        }
      },

      // 关闭弹窗
      handleClose() {
        this.visible = false
        this.tableData = []
        this.currentCategory = ''
        this.currentType = null
      },
    },
  }
</script>

<style scoped lang="scss">
  .dialog-footer {
    text-align: center;
  }
</style>
