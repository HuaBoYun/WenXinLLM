<template>
  <div>
    <div class="side-bar-menu side-bar-menu-default">
      <div
        class="menu-item menu-item-default"
        @click="onMenuItemDefaultClick"
      >
        <i class="menu-icon menu-icon-default el-icon-plus"></i>
        <div class="menu-title menu-title-default">新对话 <img :src="ctrl" alt="" style="width: 37px;height: 14px;"></div>
      </div>
    </div>

    <!-- 主菜单 -->
    <div class="side-bar-menu">
      <div
        v-for="item in menuList"
        :class="`menu-item ${
          item.hover && !item.actived ? 'menu-item-hover' : ''
        } ${item.actived ? 'menu-item-actived' : ''}`"
        :key="item.label"
        @mouseenter="item.hover = true"
        @mouseleave="item.hover = false"
        @click="(e) => onMenuItemClick(e, item)"
      >
        <i v-if="item.nativeIcon" :class="`menu-icon ${item.icon}`"></i>
        <vab-icon
          v-else
          class="menu-icon"
          :icon="item.icon"
        />
        <div class="menu-title">{{ item.label }}</div>
      </div>
    </div>
    
    <el-divider class="divider-style"></el-divider>

    <!-- 额外菜单 -->
    <div class="extra">
      <div class="extra-header" @click="smartExpand = !smartExpand">
        <i class="menu-icon el-icon-star-off"></i>
        <div class="extra-title">发现智能体</div>
        <i v-if="smartExpand" class="menu-icon el-icon-arrow-down"></i>
        <i v-else class="menu-icon el-icon-arrow-right"></i>
      </div>
      <div class="extra-list" v-if="smartExpand">
        <div
          v-for="item in extraMenuList"
          :class="`menu-item ${
            item.hover && !item.actived ? 'menu-item-hover' : ''
          } ${item.actived ? 'menu-item-actived' : ''}`"
          :key="item.label"
          @mouseenter="item.hover = true"
          @mouseleave="item.hover = false"
          @click="(e) => onExtraMenuItemClick(e, item)"
        >
          <i v-if="item.nativeIcon" :class="`menu-icon ${item.icon}`"></i>
          <img v-else class="extra-icon" :src="item.icon" alt="">
          <div class="menu-title">{{ item.label }}</div>
        </div>
      </div>
    </div>

    <el-divider class="divider-style"></el-divider>

    <!-- 历史对话 -->
    <div class="histroy">
      <div class="histroy-header" @click="expand = !expand">
        <i class="menu-icon el-icon-chat-dot-square"></i>
        <div class="histroy-title">最近对话</div>
        <i v-if="expand" class="menu-icon el-icon-arrow-down"></i>
        <i v-else class="menu-icon el-icon-arrow-right"></i>
      </div>
      <div class="histroy-list" v-if="expand">
        <div class="list-item" v-for="item in dialogueList" @click="$emit('chatNow', {msg: item.name})">{{ item.name }}</div>
        <div class="list-item">查看全部...</div>
      </div>
    </div>
  </div>
</template>

<script>
import ctrl from '@/views/index/components/AI/icon/ctrlk.svg'
import mapData from '@/views/index/components/AI/mock/dataMap.js'

export default {
  props: ['dialogueList'],
  data() {
    return {
      ctrl,
      expand: true,
      smartExpand: true,
      menuList: mapData.menuList,
      extraMenuList: [
        {
          name: 'AISmartModel',
          label: '发现AI智能体',
          icon: 'el-icon-plus',
          nativeIcon: true,
          actived: false,
          hover: false,
        },
      ],
      dl: []
    }
  },
  // watch:{
  //   dialogueList: {
  //     deep: true,
  //     handler(newVal) {
  //       console.log('dialogueListhandler', newVal)
  //       this.dl = newVal
  //       // this.$forceUpdate()
  //     },
  //   }
  // },
  methods: {
    onMenuItemDefaultClick(e) {
      e.stopPropagation()
      this.menuList.map((x) => {
        x.actived = false
      })
      this.extraMenuList.map((x) => {
        x.actived = false
      })
      this.$emit('onMenuItemDefaultClick', 'AINewConversation')
    },
    onMenuItemClick(e, item) {
      e.stopPropagation()
      this.menuList.map((x) => {
        if (item.label === x.label) {
          this.$emit('onMenuItemClick', x)
          x.actived = true
        } else {
          x.actived = false
        }
      })
      this.extraMenuList.map((x) => {
        x.actived = false
      })
    },
    onExtraMenuItemClick(e, item) {
      e.stopPropagation()
      this.extraMenuList.map((x) => {
        if (item.label === x.label) {
          this.$emit('onMenuItemClick', x, x)
          x.actived = true
        } else {
          x.actived = false
        }
      })
      this.menuList.map((x) => {
        x.actived = false
      })
    },
  },
}

</script>

<style scoped>
  .side-bar-menu {
    padding: 0 12px;
  }

  .menu-item {
    display: flex;
    align-items: center;
    height: 38px;
    border-radius: 12px;
    cursor: pointer;
    padding: 8px 6px;
    margin-bottom: 4px;
  }

  .menu-item-actived {
    background: #fff;
    box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
    font-weight: 600;
  }

  .menu-icon {
    width: 24px;
    height: 24px;
    line-height: 24px;
    text-align: center;
  }

  .menu-item-default {
    background: rgba(0, 87, 255, 0.06);
    border: 0.5px solid rgba(0, 102, 255, 0.15);
    margin-top: 16px;
    margin-bottom: 6px;
    font-weight: 600;
  }

  .menu-icon-default {
    color: #0057ff;
  }
  .menu-title {
    margin-left: 8px;
  }
  .menu-title-default {
    color: #0057ff;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .menu-item-hover {
    background: rgba(0, 0, 0, 0.04);
  }

  .divider-style {
    margin: 8px auto !important;
    width: 90% !important;
  }

  .histroy {
    padding: 0 12px;
  }

  .histroy-header {
    display: flex;
    align-items: center;
    padding: 8px 6px;
    border-radius: 12px;
    cursor: pointer;
  }

  .histroy-header:hover {
    background: rgba(0, 0, 0, 0.04);
  }

  .histroy-title {
    flex: 1;
    margin-left: 8px;
  }

  .histroy-list {
    color: rgba(0,0,0,0.5);
    padding-left: 36px;
    font-size: 13px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .histroy-list .list-item {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    height: 32px;
    justify-content: center;
    overflow: hidden;
    cursor: pointer;
  }

  .extra {
    padding: 0 12px;
  }

  .extra-header {
    display: flex;
    align-items: center;
    padding: 8px 6px;
    border-radius: 12px;
    cursor: pointer;
  }

  .extra-header:hover {
    background: rgba(0, 0, 0, 0.04);
  }

  .extra-title {
    flex: 1;
    margin-left: 8px;
  }

  .extra-icon {
    width: 20px;
    height: 20px;
    border-radius: 50%;
  }

  .extra-list {
    color: rgba(0,0,0,0.5);
    /* padding-left: 36px; */
    font-size: 13px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .extra-list .list-item {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    height: 32px;
    justify-content: center;
    overflow: hidden;
    cursor: pointer;
  }

</style>