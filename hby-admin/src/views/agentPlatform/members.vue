<template>
  <div class="agent-iframe-wrap">
    <iframe :src="iframeSrc" @load="onLoad"></iframe>
    <div v-if="loading" class="agent-loading">
      <i class="el-icon-loading"></i>
      <span>加载中...</span>
    </div>
  </div>
</template>

<script>
import { getToken } from '@/utils/token'

const AGENT_BASE_URL = 'http://localhost:9528'

export default {
  name: 'AgentMembers',
  data() {
    return {
      loading: true,
      iframeSrc: ''
    }
  },
  created() {
    const token = getToken()
    if (token) {
      this.iframeSrc = AGENT_BASE_URL + '/?sso_token=' + encodeURIComponent(token) + '#/members'
    } else {
      this.iframeSrc = AGENT_BASE_URL + '/#/members'
    }
  },
  methods: {
    onLoad() {
      this.loading = false
    }
  }
}
</script>

<style lang="scss" scoped>
.agent-iframe-wrap {
  position: relative;
  width: 100%;
  height: $base-keep-alive-height;

  iframe {
    width: 100%;
    height: 100%;
    border: none;
    display: block;
  }

  .agent-loading {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    color: #909399;
    font-size: 14px;

    i {
      font-size: 32px;
      color: #409eff;
    }
  }
}
</style>
