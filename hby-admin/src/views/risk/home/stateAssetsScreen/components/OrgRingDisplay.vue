<template>
  <div class="org-ring-display">
    <!-- 扫描圆圈动画 -->
    <div class="scan-rings">
      <div class="scan-ring" v-for="i in 3" :key="i" :style="getScanRingStyle(i)"></div>
    </div>

    <!-- 中心：根节点组织名称 -->
    <div class="center-org">
      <div class="center-glow"></div>
      <div class="center-emblem">
        <div class="emblem-icon">&#9775;</div>
      </div>
      <div class="center-name" :title="rootName">{{ rootName }}</div>
      <div class="center-sub">国有资产监督管理</div>
    </div>

    <!-- 内环：一级子节点 -->
    <div class="ring-layer ring-layer--inner" v-if="level1Nodes.length > 0">
      <div
        v-for="(node, idx) in level1Nodes"
        :key="'l1-' + idx"
        class="org-node org-node--level1"
        :style="getNodeStyle(idx, level1Nodes.length, 165)"
      >
        <div class="node-dot node-dot--level1"></div>
        <div class="node-name">{{ node.name || node.orgName }}</div>
        <div class="node-sub" v-if="node.children">{{ node.children.length }}个子级</div>
      </div>
    </div>

    <!-- 外环：二级子节点（最多16个） -->
    <div class="ring-layer ring-layer--outer" v-if="level2Nodes.length > 0">
      <div
        v-for="(node, idx) in level2Nodes.slice(0, 16)"
        :key="'l2-' + idx"
        class="org-node org-node--level2"
        :style="getNodeStyle(idx, Math.min(level2Nodes.length, 16), 260)"
      >
        <div class="node-dot node-dot--level2"></div>
        <div class="node-name node-name--small">{{ node.name || node.orgName }}</div>
      </div>
    </div>

    <!-- 连线 SVG -->
    <svg class="connector-svg" viewBox="-300 -300 600 600">
      <!-- 内环连线 -->
      <g v-if="level1Nodes.length > 0">
        <line
          v-for="(node, idx) in level1Nodes"
          :key="'line1-' + idx"
          x1="0" y1="0"
          :x2="getLineEnd(idx, level1Nodes.length, 140).x"
          :y2="getLineEnd(idx, level1Nodes.length, 140).y"
          stroke="rgba(0,200,255,0.25)"
          stroke-width="1"
          stroke-dasharray="3,4"
        />
      </g>
      <!-- 圆弧 -->
      <circle cx="0" cy="0" r="80" fill="none" stroke="rgba(0,200,255,0.15)" stroke-width="1" />
      <circle cx="0" cy="0" r="165" fill="none" stroke="rgba(0,200,255,0.12)" stroke-width="1" stroke-dasharray="4,6" />
      <circle cx="0" cy="0" r="260" fill="none" stroke="rgba(0,200,255,0.08)" stroke-width="1" stroke-dasharray="2,8" />
    </svg>

    <!-- 加载中提示 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-text">
        <span class="loading-dot" v-for="i in 3" :key="i"></span>
        加载组织架构中
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrgRingDisplay',
  props: {
    orgData: {
      type: Array,
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    rootNode() {
      return this.orgData && this.orgData.length > 0 ? this.orgData[0] : null
    },
    rootName() {
      if (!this.rootNode) return '国有资产监督管理委员会'
      return this.rootNode.name || this.rootNode.orgName || '国有资产监督管理委员会'
    },
    level1Nodes() {
      if (!this.rootNode || !this.rootNode.children) return []
      return this.rootNode.children.slice(0, 12)
    },
    level2Nodes() {
      const nodes = []
      this.level1Nodes.forEach((n) => {
        if (n.children) nodes.push(...n.children)
      })
      return nodes
    },
  },
  methods: {
    getNodeStyle(index, total, radius) {
      const angle = (index / total) * 2 * Math.PI - Math.PI / 2
      const x = Math.cos(angle) * radius
      const y = Math.sin(angle) * radius
      return {
        position: 'absolute',
        left: `calc(50% + ${x}px)`,
        top: `calc(50% + ${y}px)`,
        transform: 'translate(-50%, -50%)',
      }
    },
    getLineEnd(index, total, radius) {
      const angle = (index / total) * 2 * Math.PI - Math.PI / 2
      return {
        x: Math.cos(angle) * radius,
        y: Math.sin(angle) * radius,
      }
    },
    getScanRingStyle(i) {
      return {
        width: `${180 + i * 120}px`,
        height: `${180 + i * 120}px`,
        animationDelay: `${(i - 1) * 1}s`,
        animationDuration: `${3 + i * 0.5}s`,
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.org-ring-display {
  position: relative;
  width: 600px;
  height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;

  @media (max-width: 1400px) {
    width: 520px;
    height: 520px;
  }

  .scan-rings {
    position: absolute;
    inset: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    pointer-events: none;

    .scan-ring {
      position: absolute;
      border-radius: 50%;
      border: 1px solid rgba(0, 200, 255, 0.2);
      animation: scanPulse 3s ease-out infinite;
    }
  }

  @keyframes scanPulse {
    0% { opacity: 0.6; transform: scale(0.95); }
    50% { opacity: 0.2; transform: scale(1.02); }
    100% { opacity: 0.6; transform: scale(0.95); }
  }

  .center-org {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    z-index: 10;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;

    .center-glow {
      position: absolute;
      width: 160px;
      height: 160px;
      border-radius: 50%;
      background: radial-gradient(circle, rgba(0, 200, 255, 0.15) 0%, transparent 70%);
      pointer-events: none;
    }

    .center-emblem {
      width: 64px;
      height: 64px;
      border-radius: 50%;
      background: linear-gradient(135deg, #0d47a1, #1565c0);
      border: 2px solid rgba(0, 200, 255, 0.6);
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 0 20px rgba(0, 200, 255, 0.4), inset 0 0 15px rgba(0, 100, 200, 0.3);

      .emblem-icon {
        font-size: 28px;
        color: rgba(255, 213, 79, 0.9);
      }
    }

    .center-name {
      font-size: 13px;
      font-weight: 700;
      color: rgba(255, 255, 255, 0.95);
      text-align: center;
      max-width: 140px;
      line-height: 1.3;
      text-shadow: 0 0 10px rgba(0, 200, 255, 0.5);
    }

    .center-sub {
      font-size: 10px;
      color: rgba(0, 200, 255, 0.6);
      letter-spacing: 2px;
    }
  }

  .ring-layer {
    position: absolute;
    inset: 0;
    pointer-events: none;
  }

  .org-node {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 3px;
    pointer-events: auto;
    cursor: default;

    &--level1:hover .node-name {
      color: rgba(0, 200, 255, 1);
      text-shadow: 0 0 8px rgba(0, 200, 255, 0.6);
    }

    .node-dot {
      border-radius: 50%;
      flex-shrink: 0;

      &--level1 {
        width: 8px;
        height: 8px;
        background: rgba(0, 200, 255, 0.8);
        box-shadow: 0 0 8px rgba(0, 200, 255, 0.6);
      }

      &--level2 {
        width: 5px;
        height: 5px;
        background: rgba(255, 213, 79, 0.7);
        box-shadow: 0 0 4px rgba(255, 213, 79, 0.4);
      }
    }

    .node-name {
      font-size: 11px;
      color: rgba(255, 255, 255, 0.85);
      text-align: center;
      max-width: 70px;
      line-height: 1.2;
      word-break: break-all;
      transition: all 0.2s;
      text-shadow: 0 1px 4px rgba(0, 0, 0, 0.8);

      &--small {
        font-size: 10px;
        color: rgba(255, 255, 255, 0.6);
        max-width: 55px;
      }
    }

    .node-sub {
      font-size: 9px;
      color: rgba(0, 200, 255, 0.5);
    }
  }

  .connector-svg {
    position: absolute;
    inset: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;
  }

  .loading-overlay {
    position: absolute;
    inset: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(3, 15, 45, 0.6);
    border-radius: 50%;

    .loading-text {
      color: rgba(0, 200, 255, 0.8);
      font-size: 13px;
      display: flex;
      align-items: center;
      gap: 6px;

      .loading-dot {
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background: rgba(0, 200, 255, 0.8);
        animation: dotBlink 1.2s ease-in-out infinite;

        &:nth-child(2) { animation-delay: 0.2s; }
        &:nth-child(3) { animation-delay: 0.4s; }
      }
    }
  }

  @keyframes dotBlink {
    0%, 100% { opacity: 0.2; }
    50% { opacity: 1; }
  }
}
</style>
