<template>
  <div class="node-panel">
    <div
      v-for="item in nodeList"
      :key="item.text"
      class="node-item"
      @mousedown="$_dragNode(item)"
    >
      <div class="node-item-icon" :class="item.class">
        <div
          v-if="item.type === 'user' || item.type === 'time'"
          class="shape"
        ></div>
      </div>
      <span class="node-label">{{ item.text }}</span>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'NodePanel',
    props: {
      lf: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        nodeList: [
          {
            text: '开始',
            type: 'start',
            class: 'node-start',
          },
          {
            text: '矩形',
            type: 'rect',
            class: 'node-rect',
          },
          {
            type: 'user',
            text: '用户',
            class: 'node-user',
          },
          {
            type: 'push',
            text: '推送',
            class: 'node-push',
          },
          {
            type: 'download',
            text: '位置',
            class: 'node-download',
          },
          {
            type: 'end',
            text: '结束',
            class: 'node-end',
          },
        ],
      }
    },
    methods: {
      $_dragNode(item) {
        this.$props.lf.dnd.startDrag({
          type: item.type,
          text: item.label,
        })
      },
    },
  }
</script>

<style lang="scss" scoped>
  .node-panel {
    position: absolute;
    top: 100px;
    left: $base-margin * 2;
    z-index: 101;
    width: 70px;
    padding: 20px 10px;
    text-align: center;
    background-color: white;
    border-radius: 6px;
    box-shadow: 0 0 10px 1px rgb(228, 224, 219);
  }

  .node-item {
    margin-bottom: 20px;
  }

  .node-item-icon {
    display: block;
    width: 30px;
    height: 30px;
    margin: auto;
    background-size: cover;
  }

  .node-label {
    font-size: 12px;
    line-height: 30px;
    user-select: none;
  }

  .node-start {
    background: url('../background/start.png') no-repeat;
    background-size: cover;
  }

  .node-rect {
    border: 1px solid black;
  }

  .node-user {
    background: url('../background/user.png') no-repeat;
    background-size: cover;
  }

  .node-time {
    background: url('../background/time.png') no-repeat;
    background-size: cover;
  }

  .node-push {
    background: url('../background/push.png') no-repeat;
    background-size: cover;
  }

  .node-download {
    background: url('../background/download.png') no-repeat;
    background-size: cover;
  }

  .node-click {
    background: url('../background/click.png') no-repeat;
    background-size: cover;
  }

  .node-end {
    background: url('../background/end.png') no-repeat;
    background-size: cover;
  }
</style>
