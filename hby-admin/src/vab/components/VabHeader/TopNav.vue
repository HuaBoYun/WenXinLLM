<template>
  <div class="top-nav">
    <!-- 导航菜单区域 -->
    <div class="nav-menu">
      <div
        v-for="(item, index) in menuItems"
        :key="index"
        :class="['menu-item', { active: activeIndex === index }]"
        @click="handleMenuClick(index, item)"
      >
        <i :class="item.icon" style="font-size: 18px"></i>
        <span style="font-size: 12px">{{ item.title }}</span>
      </div>
    </div>

    <!-- 更多按钮 -->
    <div class="more-btn">
      <el-button type="text" @click="showMoreMenu = true">
        <i class="el-icon-more" style="color: #fff; margin-left: 10px"></i>
      </el-button>

      <!-- 更多菜单弹窗 -->
      <div v-if="showMoreMenu" class="more-menu-popup">
        <MenuDrawer
          @menus-update="handleMenusUpdate"
          @workstation-menus-change="handleWorkstationMenusChange"
          :workstation-menus="menuItems"
        />
      </div>
    </div>
  </div>
</template>

<script>
  import MenuDrawer from './MenuDrawer.vue'
  export default {
    name: 'TopNav',
    components: {
      MenuDrawer,
    },
    data() {
      return {
        activeIndex: 0,
        menuItems: [
          {
            icon: 'el-icon-document',
            title: '总账会计工作台',
            type: 'accounting',
          },
          {
            icon: 'el-icon-office-building',
            title: '资产会计工作台',
            type: 'asset',
          },
          {
            icon: 'el-icon-s-operation',
            title: '实施工作台',
            type: 'implementation',
          },
        ],
        showMoreMenu: false,
        moreMenuItems: [
          {
            icon: 'el-icon-setting',
            title: '系统设置',
            type: 'settings',
          },
          {
            icon: 'el-icon-user',
            title: '个人中心',
            type: 'profile',
          },
        ],
      }
    },
    methods: {
      handleMenuClick(index, item) {
        this.activeIndex = index
        this.$emit('menu-change', item)
      },
      handleMoreMenuClick(item) {
        this.$emit('more-menu-click', item)
        this.showMoreMenu = false
      },
      handleMenusUpdate({ workstationMenus, adminMenus }) {
        // 更新顶部固定菜单
        this.menuItems = workstationMenus
        // 更新更多菜单
        this.moreMenuItems = adminMenus
      },
      handleWorkstationMenusChange(newMenus) {
        // 实时更新顶部固定菜单
        this.menuItems = newMenus
      },
    },
    mounted() {
      // 添加点击外部关闭弹窗
      document.addEventListener('click', (e) => {
        const moreBtn = this.$el.querySelector('.more-btn')
        if (!moreBtn.contains(e.target)) {
          this.showMoreMenu = false
        }
      })
    },
    beforeDestroy() {
      // 移除事件监听
      document.removeEventListener('click', this.handleClickOutside)
    },
  }
</script>

<style lang="scss" scoped>
  .vab-theme-white {
    .top-nav {
      color: #000;
    }
  }
  .top-nav {
    display: flex;
    align-items: center;
    height: 50px;
    background: transparent;
    // padding: 0 20px;
    color: #fff;
    // margin-left: -20px;

    .logo-area {
      width: 40px;
      margin-right: 20px;

      .logo-img {
        width: 100%;
        height: auto;
      }
    }

    .nav-menu {
      display: flex;
      flex: 1;
      height: 100%;

      .menu-item {
        display: flex;
        align-items: center;
        padding: 0 10px;
        cursor: pointer;
        transition: all 0.3s;
        width: max-content;
        i {
          margin-right: 8px;
          font-size: 18px;
        }

        &:hover {
          background: rgba(255, 255, 255, 0.1);
          cursor: pointer;
        }

        &.active {
          background: rgba(255, 255, 255, 0.2);
          cursor: pointer;
        }
      }
    }

    .more-btn {
      position: relative;

      .more-menu-popup {
        position: absolute;
        top: 100%;
        left: 0;
        background: #fff;
        border-radius: 4px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        z-index: 1000;
        margin-top: 15px;
        min-width: 150px;

        .menu-list {
          border-radius: 10px;
          .more-menu-item {
            display: flex;
            align-items: center;
            padding: 12px 20px;
            cursor: pointer;
            color: #606266;

            i {
              margin-right: 8px;
              font-size: 16px;
            }

            &:hover {
              background-color: #f5f7fa;
            }
          }
        }
      }
    }
  }
</style>
