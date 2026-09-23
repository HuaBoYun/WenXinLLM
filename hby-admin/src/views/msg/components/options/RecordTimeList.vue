<template>
  <el-col :lg="16" :md="18" :sm="24" style="padding-left: 20px; overflow: auto">
    <el-empty v-if="!list || !list.length" description="暂无流转记录" />
    <el-timeline v-else reverse>
      <template v-for="(item, i) in list">
        <el-timeline-item
          :key="i"
          :timestamp="item.handleTime | toDate()"
          placement="top"
        >
          <el-card>
            <el-row>
              <el-col :span="18">
                <p class="timeline-cell">节点：{{ item.nodeName }}</p>
              </el-col>
              <el-col :span="6">
                <p class="timeline-cell" style="text-align: right">
                  <el-link :underline="false" :type="statusType(item.handleStatus)">
                    {{ statusText(item.handleStatus) }}
                  </el-link>
                </p>
              </el-col>
            </el-row>
            <p class="timeline-cell" v-if="item.userName">
              处理人员：{{ item.userName }}
            </p>
            <p
              class="timeline-cell"
              v-if="item.handleOpinion"
              style="white-space: pre-wrap"
            >
              处理意见：{{ item.handleOpinion }}
            </p>
          </el-card>
        </el-timeline-item>
      </template>
    </el-timeline>
  </el-col>
</template>

<script>
  export default {
    name: 'RecordTimeList',
    props: {
      list: { type: Array, default: () => [] },
      taskId: { type: String, default: '' },
    },
    methods: {
      statusType(status) {
        const map = { 0: 'danger', 1: 'success', 2: 'info', 3: 'warning', 4: 'danger' }
        return map[status] || 'info'
      },
      statusText(status) {
        const map = { 0: '审核拒绝', 1: '审核通过', 2: '发起', 3: '撤回', 4: '流程终止' }
        return map[status] || '处理中'
      },
    },
    filters: {
      toDate(v) {
        if (!v) return ''
        const d = typeof v === 'string'
          ? new Date(Date.parse(v.replace(/-/g, '/').replace('T', ' ').split('.')[0]))
          : new Date(v)
        const pad = (n) => String(n).padStart(2, '0')
        return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
      },
    },
  }
</script>

<style lang="scss" scoped>
  .timeline-cell {
    margin: 4px 0;
    font-size: 13px;
  }
</style>
