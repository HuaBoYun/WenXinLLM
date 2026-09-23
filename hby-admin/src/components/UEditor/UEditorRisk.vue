<template>
  <el-dialog
    title="模板"
    :visible.sync="dialogVisibleUE"
    width="1200px"
    :append-to-body="true"
    :close-on-click-modal="false"
    :modal-append-to-body="false"
  >
    <el-tabs v-model="activeName">
      <el-tab-pane label="风险数据库" name="first">
        <riskList
          ref="riskList"
          :isUEditor="true"
          @close="dialogVisibleUE = false"
          @submit="submit"
        />
      </el-tab-pane>
      <el-tab-pane label="重大风险汇总" name="second">
        <riskAll
          ref="riskAll"
          :isUEditor="true"
          @submit="submit"
          @close="dialogVisibleUE = false"
        />
      </el-tab-pane>
      <el-tab-pane label="月度评估汇总" name="third">
        <riskReportList
          ref="riskReportList"
          :isUEditor="true"
          @submit="submit"
          @close="dialogVisibleUE = false"
        />
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>
<script>
  import riskList from '@/views/risk/standingBook/index.vue'
  import riskAll from '@/views/risk/riskall/riskall'
  import riskReportList from '@/views/risk/riskReportList/riskReportList'
  export default {
    components: {
      riskList,
      riskAll,
      riskReportList,
    },
    data() {
      return { dialogVisibleUE: false, activeName: 'first' }
    },
    mounted() {},
    methods: {
      showDialog() {
        // 根据当前激活的标签页清空选择
        this.$nextTick(() => {
          switch (this.activeName) {
            case 'first':
              if (this.$refs.riskList?.$refs.multipleTable) {
                this.$refs.riskList.$refs.multipleTable.clearSelection()
              }
              break
            case 'second':
              if (this.$refs.riskAll?.$refs.multipleTable) {
                this.$refs.riskAll.$refs.multipleTable.clearSelection()
              }
              break
            case 'third':
              if (this.$refs.riskReportList?.$refs.multipleTable) {
                this.$refs.riskReportList.$refs.multipleTable.clearSelection()
              }
              break
          }
        })
        this.dialogVisibleUE = true
      },
      submit(selections, type) {
        let content = ''
        switch (this.activeName) {
          case 'first': // 风险数据库
            content = selections
              .map((item) => {
                return [
                  `风险编号：${item.risknumber || ''}`,
                  `风险名称：${item.riskname || ''}`,
                  `风险描述：${item.riskdes || ''}`,
                  `归属单位：${item.unit || ''}`,
                  `风险等级：${item.level || ''}`,
                ].join('<br>')
              })
              .join('<br><br>')
            break

          case 'second': // 重大风险汇总
            content = selections
              .map((item) => {
                return [
                  `风险名称：${item.impRiskName || ''}`,
                  `风险描述：${item.impRiskDetails || ''}`,
                  `牵头领导：${item.impWayStaffName || ''}`,
                  `牵头责任部门：${item.impWayDeptName || ''}`,
                  `本季度风险防控情况：${item.impThisControl || ''}`,
                ].join('<br>')
              })
              .join('<br><br>')
            break

          case 'third': // 月度评估汇总
            content = selections
              .map((item) => {
                return [
                  `风险领域风险：${item.oneRisk || ''}`,
                  `二级风险：${item.twoRisk || ''}`,
                  `三级风险：${item.threeRisk || ''}`,
                  `四级风险：${item.levelFourRisk || ''}`,
                ].join('<br>')
              })
              .join('<br><br>')
            break
        }

        // 添加段落标签包裹内容
        content = `<p>${content}</p>`
        this.$emit('insertContent', content)
        this.dialogVisibleUE = false
      },
    },
  }
</script>
<style lang="scss" scoped></style>
