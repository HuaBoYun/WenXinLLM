<script>
  import FlowCard from './FlowCard/Preview'
  import { getMockData, NodeUtils } from './FlowCard/util.js'

  export default {
    name: 'Process',
    props: ['tabName', 'conf'],
    data() {
      let data = {}
      if (
        typeof this.conf === 'object' &&
        this.conf !== null &&
        JSON.stringify(this.conf) !== '{}'
      ) {
        data = Object.assign(NodeUtils.createNode('start'), this.conf)
      } else {
        data = getMockData()
      }
      return {
        data, // 流程图数据
        scaleVal: 100, // 流程图缩放比例 100%
        step: 5, // 缩放步长
        updateId: 0, // key值 用于模拟$forceUpdate
        activeData: null, // 被激活的流程卡片数据，用于属性面板编辑
        isProcessCmp: true,
        verifyMode: false,
      }
    },
    methods: {
      // 给父级组件提供的获取流程数据得方法
      getData() {
        this.verifyMode = true
        if (NodeUtils.checkAllNode(this.data)) {
          return Promise.resolve({ formData: this.data })
        } else {
          return Promise.reject({ target: this.tabName })
        }
      },
      /**
       * 接收所有FlowCard事件触发
       * @param { Object } data - 含有event(事件名称)/args(参数)两个属性
       */
      eventReceiver({ event, args }) {
        if (event === 'edit') {
          this.activeData = args[0] // 打开属性面板
          return
        }
        // 本实例只监听了第一层数据（startNode）变动
        // 为了实时更新  采用$forceUpdate刷新 但是由于某些条件下触发失效（未排除清除原因）
        // 使用key + 监听父组件updateId方式强制刷新
        NodeUtils[event](...args)
        this.forceUpdate()
      },

      forceUpdate() {
        this.updateId = this.updateId + 1
      },
      /**
       * 控制流程图缩放
       * @param { Object } val - 缩放增量 是step的倍数 可正可负
       */
      changeScale(val) {
        if (this.scaleVal >= 0 && this.scaleVal <= 200) {
          if (this.scaleVal === 200 && this.scaleVal + val > 200) return
          if (this.scaleVal === 0 && this.scaleVal + val < 0) return
          // 缩放介于0%~200%
          this.scaleVal += val
        }
      },
      /**
       * 属性面板提交事件
       * @param { Object } value - 被编辑的节点的properties属性对象
       */
      onPropEditConfirm(value, content) {
        this.activeData.content = content || '请设置条件'
        let oldProp = this.activeData.properties
        this.activeData.properties = value
        // 修改优先级
        if (NodeUtils.isConditionNode(this.activeData)) {
          value.priority !== oldProp.priority &&
            NodeUtils.resortPrioByCNode(
              this.activeData,
              oldProp.priority,
              this.data
            )
          NodeUtils.setDefaultCondition(this.activeData, this.data)
        }
        if (NodeUtils.isStartNode(this.activeData))
          this.$emit('startNodeChange', this.data)
        this.onClosePanel()
        this.forceUpdate()
      },
      /**
       * 属性面板取消事件
       */
      onClosePanel() {
        this.activeData = null
      },

      // 传formIds 查询指定组件 未传时  判断所有组件
      isFilledPCon(formIds) {
        let res = false
        const loopChild = (parent, callback) =>
          parent.childNode && loop(parent.childNode, callback)
        const loop = (data, callback) => {
          if (res || !data) return // 查找到就退出
          if (Array.isArray(data.conditionNodes)) {
            const uesd = data.conditionNodes.some((c) => {
              const cons = c.properties.conditions || []
              return Array.isArray(formIds)
                ? cons.some((item) => formIds.includes(item.formId)) // 查询特定组件
                : cons.length > 0 // 只要有节点设置了条件 说明就有组件作为条件被使用
            })
            uesd
              ? callback()
              : data.conditionNodes.forEach((t) => loopChild(t, callback))
          }
          loopChild(data, callback)
        }
        loop(this.data, () => (res = true))
        return res
      },
    },
    directives: {
      //拖拽移动
      drag(el) {
        let oDiv = el // 当前元素
        // let self = this // 上下文
        // 禁止选择网页上的文字
        document.onselectstart = function () {
          return false
        }
        oDiv.onmousedown = function (e) {
          // 鼠标按下，计算当前元素距离可视区的距离
          let disX = e.clientX - oDiv.offsetLeft
          let disY = e.clientY - oDiv.offsetTop
          document.onmousemove = function (e) {
            // 通过事件委托，计算移动的距离
            let l = e.clientX - disX
            let t = e.clientY - disY
            // 移动当前元素
            oDiv.style.left = l + 'px'
            oDiv.style.top = t + 'px'
          }
          document.onmouseup = function (e) {
            document.onmousemove = null
            document.onmouseup = null
          }
          // return false不加的话可能导致黏连，就是拖到一个地方时div粘在鼠标上不下来，相当于onmouseup失效
          return false
        }
      },
    },

    render: function (h) {
      return (
        <div
          class="flow-container flow-container-preview"
          style={{ height: '700px' }}
        >
          <div class="scale-slider">
            <i
              class="btn el-icon-minus"
              onClick={this.changeScale.bind(this, -this.step)}
            ></i>
            <span style="font-size:14px;">{this.scaleVal}%</span>
            <i
              class="btn el-icon-plus"
              onClick={this.changeScale.bind(this, this.step)}
            ></i>
          </div>
          <div class="tips">
            <div class="tips-item">
              <span class="icon success">●</span>已完成
            </div>
            <div class="tips-item">
              <span class="icon current">●</span>进行中
            </div>
            <div class="tips-item">
              <span class="icon">●</span>无/未处理
            </div>
          </div>
          <div>
            <FlowCard
              verifyMode={this.verifyMode}
              key={this.updateId}
              data={this.data}
              onEmits={this.eventReceiver}
              style={{ transform: `scale(${this.scaleVal / 100})` }}
            />
          </div>
        </div>
      )
    },
  }
</script>

<style scoped lang="scss">
  $bg-color: #fff;

  .flow-container {
    display: inline-block;
    background: $bg-color;
    width: 100%;
    box-sizing: border-box;
    text-align: center;
    overflow: auto;
    &.flow-container-preview {
      height: calc(100% - 10px);
      >>> .branch-wrap {
        .branch-box {
          background: $bg-color;

          > .col-box {
            &:first-of-type {
              &::before,
              &::after {
                background: $bg-color !important;
              }
            }
            &:last-of-type {
              &::before,
              &::after {
                background: $bg-color;
              }
            }
          }
        }
      }
      >>> .node-wrap-box.approver::before {
        background: #fff;
      }
      >>> .flow-path-card {
        &:hover {
          .title-text {
            border-bottom: none;
          }
        }
        &.condition:hover,
        &.timer:hover {
          box-shadow: 0 0 0 2px #b6b6b6, 0 0 5px 4px rgb(0 0 0 / 20%);
        }
        &.start-node,
        &.approver,
        &.subFlow {
          &:hover {
            box-shadow: 0 0 0 2px #b6b6b6, 0 0 5px 4px rgb(0 0 0 / 20%);
          }
          .header {
            background-color: #b6b6b6;
          }
        }
        &.state-past {
          .header {
            background-color: #67c23a;
          }
          &:hover {
            box-shadow: 0 0 0 2px #67c23a, 0 0 5px 4px rgb(103 194 58 / 20%);
          }
        }
        &.state-curr {
          .header {
            background-color: #1890ff;
          }
          &:hover {
            box-shadow: 0 0 0 2px #1890ff, 0 0 5px 4px rgb(24 144 255 / 20%);
          }
        }
      }
    }
  }

  .scale-slider {
    // margin-left: 200px;
    text-align: right;
    margin-top: 60px;
    z-index: 199;

    .btn {
      display: inline-block;
      padding: 4px;
      border: 1px solid #cacaca;
      border-radius: 3px;
      background: #fff;
      margin-left: 10px;
      margin-right: 10px;
      cursor: pointer;
    }
  }
  .tips {
    position: absolute;
    left: 20px;
    top: 0px;
    z-index: 199;
    text-align: left;
    .tips-item {
      line-height: 20px;
      font-size: 16px;
      display: inline-block;
      margin-right: 15px;
      .icon {
        font-size: 20px;
        margin-right: 5px;
        color: #b6b6b6;
        &.success {
          color: #67c23a;
        }
        &.current {
          color: #1890ff;
        }
      }
    }
  }

  $bg-color: #ebeef5;
  $line-color: #a9b4cd;
  $base-size: 12px;

  // Mixin flex 垂直居中布局
  @mixin flex-center() {
    display: flex;
    flex-wrap: nowrap;
    justify-content: center;
    align-items: center;
  }

  //  三点省略 支持单行多行
  // Mixin { n:Number } n：省略行数限制
  @mixin ellipsis($n) {
    overflow: hidden;
    text-overflow: ellipsis;

    @if $n>1 {
      display: -webkit-box;
      -webkit-line-clamp: $n;
      -webkit-box-orient: vertical;
    } @else {
      white-space: nowrap;
    }
  }

  // 按钮变大效果
  @mixin btn-bigger() {
    transform: scale(1.2);
    box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.1);
  }

  .node-wrap-box {
    position: relative;
    @include flex-center();
    flex-direction: column;

    &.condition,
    &.branchFlow,
    &.interflow {
      padding: 30px 50px 0;
    }

    &.empty {
      overflow: hidden;
    }

    &.approver::before {
      content: '';
      position: absolute;
      top: -10px;
      left: 50%;
      transform: translateX(-50%);
      width: 0;
      height: 4px;
      border-style: solid;
      border-width: 8px 6px 4px;
      border-color: $line-color transparent transparent;
      background: $bg-color;
    }

    &.approver.branchFlow::before,
    &.approver.interflow::before {
      top: 20px;
    }

    &.error {
      &.condition .error-tip,
      &.branchFlow .error-tip,
      &.interflow .error-tip {
        right: 0;
      }

      .error-tip {
        right: -40px;
      }

      .flow-path-card {
        border: 1px solid #f00;

        &:hover {
          border-width: 0;
        }
      }
    }

    .error-tip {
      position: absolute;
      right: 1px;
      top: 15%;
      width: 30px;
      height: 30px;
      color: #f00;
      cursor: pointer;
      border-radius: 50%;
      border: 1px solid;
      line-height: 30px;
      transition: right 0.5s;
    }

    &.condition .error-tip,
    &.branchFlow .error-tip,
    &.interflow .error-tip {
      right: 60px;
    }
  }

  .end-node {
    font-size: $base-size;
    text-align: center;
    @include flex-center();
    flex-direction: column;

    &::before {
      content: '';
      width: 10px;
      height: 10px;
      margin: auto;
      border: none;
      margin-bottom: 12px;
      border-radius: 50%;
      background: #a9b4cd;
    }
  }

  .flow-path-card {
    width: 220px;
    min-height: 82px;
    font-size: $base-size;
    border-radius: 4px;
    text-align: left;
    cursor: pointer;
    overflow: hidden;
    position: relative;
    box-sizing: border-box;
    box-shadow: 0 0 6px 0 rgba(0, 0, 0, 0.3);
    background: #fff;
    border-radius: 2px;
    font-size: 12px;

    &:hover {
      box-shadow: 0 0 0 2px #1890ff, 0 0 5px 4px rgba(0, 0, 0, 0.2);
    }

    &.copy {
      .header {
        background-color: #1890ff;
      }
    }

    &.timer {
      .header {
        color: #f5811c;
        border-bottom: 1px solid #eeeeee;

        .actions {
          color: #606266;
        }
      }
    }

    &.approver,
    &.subFlow {
      &:hover {
        box-shadow: 0 0 0 2px #1890ff, 0 0 5px 4px rgba(0, 0, 0, 0.2);
      }

      .header {
        background-color: #1890ff;
      }
    }

    &.start-node {
      &:hover {
        box-shadow: 0 0 0 2px #576a95, 0 0 5px 4px rgba(0, 0, 0, 0.2);
      }

      .header {
        background-color: #576a95;
      }
    }

    .header {
      padding-left: 10px;
      padding-right: 30px;
      width: 100%;
      height: 30px;
      line-height: 30px;
      color: white;
      position: relative;
      box-sizing: border-box;

      .title-box {
        position: relative;
        display: inline-block;
        @include ellipsis(1);
      }

      .title-input {
        position: absolute;
        left: 0;
        border: none;
        background: inherit;
        color: inherit;
        opacity: 0;
        margin-top: 6px;

        &:focus {
          border-radius: 4px;
          font-size: $base-size;
          padding: 2px;
          padding-left: 4px;
          width: 97%;
          margin-left: 1px;
          height: 18px;
          box-sizing: border-box;
          box-shadow: 0 0 1px 1px #1890ff;
          background-color: $bg-color;
          color: black;
          opacity: 1;
        }
      }

      .title-text {
        vertical-align: middle;
      }

      > .actions {
        position: absolute;
        right: 0;
        top: 0;
        visibility: hidden;
      }

      > .async-state {
        position: absolute;
        right: 25px;
        top: 5px;
      }
    }

    &.timer:hover {
      .actions {
        visibility: visible;
        margin-right: 4px;
      }
    }

    &.subFlow {
      .header {
        .title-box {
          width: 140px !important;
        }
      }
    }

    &:not(.start-node):not(.timer):hover {
      .actions {
        visibility: visible;
        margin-right: 4px;
      }

      .title-text {
        border-bottom: 1px dashed currentColor;
      }
    }

    &.start-node:hover {
      .title-text {
        border-bottom: 1px dashed currentColor;
      }
    }

    .body {
      position: relative;
      padding: 10px;
      box-sizing: border-box;

      .text {
        word-break: break-all;
        margin: 0 ellipsis(4);
      }
    }

    .icon-wrapper {
      position: absolute;
      top: 0;
      height: 100%;
      width: 14px;
      box-sizing: border-box;

      &.left {
        left: 0;
      }

      &.right {
        right: 0;
      }

      > {
        .right-arrow,
        .left-arrow {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
        }
      }
    }
  }

  .flow-path-card.condition {
    .header {
      line-height: 30px;
      color: inherit;
      border-bottom: 1px solid #eeeeee;

      .title-box {
        height: auto !important;
      }

      .title-text {
        color: #15bc83;
      }
    }

    .body {
      padding: 10px;
      color: #606266;
    }

    .icon-wrapper {
      &:hover {
        background-color: #f1f1f1;
      }
    }

    .right-arrow,
    .left-arrow {
      visibility: hidden;
    }

    &:hover {
      .right-arrow,
      .left-arrow {
        visibility: visible;
      }

      .priority {
        display: none;
      }
    }
  }

  .col-box:first-of-type > .node-wrap .left {
    display: none;
  }

  .col-box:last-of-type > .node-wrap .right {
    display: none;
  }

  .add-node-btn-box {
    width: 220px;
    height: 100px;
    position: relative;
    padding-top: 30px;
    margin: auto;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      z-index: -1;
      margin: auto;
      width: 1px;
      height: 100%;
      background-color: $line-color;
    }

    .add-node-btn {
      display: flex;
      justify-content: center;

      .btn {
        width: 32px;
        height: 32px;
        border-radius: 16px;
        cursor: pointer;
        outline: none;
        background-color: #1890ff;
        border-color: transparent;
        transition: transform 0.5s;

        &:hover {
          @include btn-bigger();
        }

        .icon {
          color: white;
        }
      }
    }
  }

  .branch-wrap {
    .branch-box-wrap {
      display: inline-flex;
      flex-direction: column;
      align-items: center;
    }

    .branch-box {
      align-items: stretch;
      border-bottom: 1px solid $line-color;
      border-top: 1px solid $line-color;
      box-sizing: border-box;
      background: $bg-color;

      > .col-box {
        display: flex;
        flex-direction: column;
        align-items: center;
        position: relative;

        &:first-of-type {
          &::before,
          &::after {
            content: '';
            position: absolute;
            left: 0;
            height: 3px;
            width: calc(50% - 1px);
            background: $bg-color;
          }

          &::before {
            top: -2px;
          }

          &::after {
            bottom: -2px;
          }
        }

        &:last-of-type {
          &::before,
          &::after {
            content: '';
            position: absolute;
            right: 0;
            height: 3px;
            width: calc(50% - 1px);
            background: $bg-color;
          }

          &::before {
            top: -2px;
          }

          &::after {
            bottom: -2px;
          }
        }

        .center-line {
          height: 100%;
          width: 1px;
          background: $line-color;
          position: absolute;
        }
      }

      > .btn {
        font-size: 14px;
        z-index: 99;
        cursor: pointer;
        position: absolute;
        top: 0;
        left: 50%;
        outline: none;
        transform: translate(-50%, -50%);
        padding: 9px 16px;
        border: none;
        border-radius: 15px;
        background: white;
        box-shadow: 0 0 10px 0px rgba(0, 0, 0, 0.2);
        transition: transform 0.3s;
        color: #1890ff;

        &:hover {
          transform: scale(1.1) translate(-46%, -50%);
        }
      }
    }
  }

  .condition-box {
    display: flex;
    justify-content: space-around;
    align-items: center;
    text-align: center;
    padding: 10px 0;

    > div:nth-child(1) .iconfont {
      color: #ff943e;
    }

    > div:nth-child(2) .iconfont {
      color: #3296fa;
    }

    .condition-disabled {
      color: #c0c4cc;

      .condition-icon {
        background: #e5e5e5;
        color: #999;
        cursor: default;

        &:hover {
          background: #e5e5e5;
          box-shadow: none;

          > .icon-ym,
          > [class^='el-icon-'],
          > .ym-custom {
            color: #999;
          }
        }
      }
    }

    .condition-icon {
      width: 60px;
      height: 60px;
      line-height: 60px;
      border: 1px solid #e5e5e5;
      border-radius: 30px;
      box-sizing: border-box;
      font-size: 12px;
      cursor: pointer;
      margin-bottom: 4px;

      .icon-ym,
      [class^='el-icon-'],
      .ym-custom {
        font-size: 32px;
      }

      &:hover {
        background: #3296fa;
        box-shadow: 0 0 8px 4px rgba(0, 0, 0, 0.1);

        > .icon-ym,
        > [class^='el-icon-'],
        > .ym-custom {
          color: white;
        }
      }
    }
  }

  .relative {
    position: relative;
  }

  .flex {
    display: flex;
  }

  .justify-center {
    justify-content: center;
  }

  .icon {
    vertical-align: middle;
    width: 14px;
    height: 14px;
    font-size: 14px;
  }

  .priority {
    position: absolute;
    right: 0;
    font-size: 12px;
  }

  input::-ms-clear,
  input::-ms-reveal {
    display: none;
  }
</style>
