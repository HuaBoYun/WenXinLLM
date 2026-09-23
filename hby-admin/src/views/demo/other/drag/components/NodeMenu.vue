<template>
  <div ref="tool" class="flow-menu">
    <div v-for="menu in menuList" :key="menu.id">
      <span class="vab-node-pmenu" @click="menu.open = !menu.open">
        <i
          :class="{
            'el-icon-caret-bottom': menu.open,
            'el-icon-caret-right': !menu.open,
          }"
        />
        &nbsp;{{ menu.name }}
      </span>
      <ul v-show="menu.open" class="vab-node-menu-ul">
        <draggable
          v-model="menu.children"
          v-bind="draggableOptions"
          @end="end"
          @start="move"
        >
          <li
            v-for="subMenu in menu.children"
            :key="subMenu.id"
            class="vab-node-menu-li"
            :type="subMenu.type"
          >
            <i :class="subMenu.ico" />
            {{ subMenu.name }}
          </li>
        </draggable>
      </ul>
    </div>
  </div>
</template>

<script>
  import draggable from 'vuedraggable'

  const mousePosition = {
    left: -1,
    top: -1,
  }

  export default {
    components: {
      draggable,
    },
    data() {
      return {
        activeNames: '1',
        // draggable配置参数参考 https://www.cnblogs.com/weixin186/p/10108679.html
        draggableOptions: {
          preventOnFilter: false,
          sort: false,
          disabled: false,
          ghostClass: 'tt',
          // 不使用H5原生的配置
          forceFallback: true,
          // 拖拽的时候样式
          // fallbackClass: 'flow-node-draggable'
        },
        // 默认打开的左侧菜单的id
        defaultOpeneds: ['1', '2'],
        menuList: [
          {
            id: '1',
            type: 'group',
            name: '开始节点',
            ico: 'el-icon-video-play',
            open: true,
            children: [
              {
                id: '11',
                type: 'timer',
                name: '数据接入',
                ico: 'el-icon-time',
                // 自定义覆盖样式
                style: {},
              },
              {
                id: '12',
                type: 'task',
                name: '接口调用',
                ico: 'el-icon-odometer',
                // 自定义覆盖样式
                style: {},
              },
            ],
          },
          {
            id: '2',
            type: 'group',
            name: '结束节点',
            ico: 'el-icon-video-pause',
            open: true,
            children: [
              {
                id: '21',
                type: 'end',
                name: '流程结束',
                ico: 'el-icon-caret-right',
                // 自定义覆盖样式
                style: {},
              },
              {
                id: '22',
                type: 'over',
                name: '数据清理',
                ico: 'el-icon-shopping-cart-full',
                // 自定义覆盖样式
                style: {},
              },
            ],
          },
        ],
        nodeMenu: {},
      }
    },
    methods: {
      // 根据类型获取左侧菜单对象
      getMenuByType(type) {
        for (let i = 0; i < this.menuList.length; i++) {
          const children = this.menuList[i].children
          for (let j = 0; j < children.length; j++) {
            if (children[j].type === type) {
              return children[j]
            }
          }
        }
        return {}
      },
      // 拖拽开始时触发
      move(evt) {
        const type = evt.item.attributes.type.nodeValue
        this.nodeMenu = this.getMenuByType(type)
      },
      // 拖拽结束时触发
      end(evt) {
        this.$emit('add-node', evt, this.nodeMenu, mousePosition)
      },
      // 是否是火狐浏览器
      isFirefox() {
        const userAgent = navigator.userAgent
        return userAgent.indexOf('Firefox') > -1
      },
    },
  }
</script>
