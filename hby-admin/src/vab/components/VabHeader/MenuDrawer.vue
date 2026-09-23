<template>
  <div class="menu-drawer">
    <!-- 菜单列表 -->
    <div class="menu-list">
      <!-- 工作台菜单组 -->
      <div
        class="menu-group"
        @drop="handleDrop($event, 'workstation')"
        @dragover.prevent
      >
        <div
          v-for="(item, index) in workstationMenus"
          :key="index"
          :class="['menu-item', { active: activeMenu === item.id }]"
          draggable="true"
          @dragstart="handleDragStart($event, item, 'workstation', index)"
          @click="handleMenuClick(item)"
        >
          <img
            :src="point"
            alt=""
            style="width: 30px; height: 30px; padding: 0 0 5px 0"
          />

          <div class="menu-icon">
            <i :class="item.icon" :style="{ color: item.iconColor }"></i>
          </div>
          <span class="menu-title">{{ item.title }}</span>
          <div class="menu-actions" v-if="item.actions">
            <i class="el-icon-link" v-if="item.actions.link"></i>
            <i class="el-icon-star-off" v-if="item.actions.star"></i>
          </div>
        </div>
      </div>

      <!-- 分割线 -->
      <div class="divider">固定到导航栏</div>

      <!-- 管理员工作台菜单组 -->
      <div
        class="menu-group"
        @drop="handleDrop($event, 'admin')"
        @dragover.prevent
      >
        <div
          v-for="(item, index) in adminMenus"
          :key="index"
          :class="['menu-item', { active: activeMenu === item.id }]"
          draggable="true"
          @dragstart="handleDragStart($event, item, 'admin', index)"
          @click="handleMenuClick(item)"
        >
          <img
            :src="point"
            alt=""
            style="width: 30px; height: 30px; padding: 0 0 5px 0"
          />
          <div class="menu-icon">
            <i :class="item.icon" :style="{ color: item.iconColor }"></i>
          </div>
          <span class="menu-title">{{ item.title }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import point from '@/assets/error_images/point.png'
  export default {
    name: 'MenuDrawer',
    data() {
      return {
        activeMenu: '',
        point,
        adminMenus: [
          {
            id: 'production-manager',
            title: '生产管理员工作台-离散',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'production-process',
            title: '生产管理员工作台-流程',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'asset-operation',
            title: '资产运维工作台',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'cost',
            title: '成本估算工作台',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'sales',
            title: '销售员工作台',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'purchase',
            title: '采购员工作台',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'product-cost',
            title: '产品成本工作台',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'production-manager',
            title: '生产管理员工作台-离散',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'production-process',
            title: '生产管理员工作台-流程',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'asset-operation',
            title: '资产运维工作台',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'cost',
            title: '成本估算工作台',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'sales',
            title: '销售员工作台',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'purchase',
            title: '采购员工作台',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
          {
            id: 'product-cost',
            title: '产品成本工作台',
            icon: 'el-icon-monitor',
            iconColor: '#409EFF',
          },
        ],
      }
    },
    props: {
      workstationMenus: {
        type: Array,
        required: true,
        default: () => [],
      },
    },
    methods: {
      handleMenuClick(item) {
        this.activeMenu = item.id
        this.$emit('menu-select', item)
      },
      handleDragStart(event, item, sourceGroup, sourceIndex) {
        event.dataTransfer.setData(
          'application/json',
          JSON.stringify({
            item,
            sourceGroup,
            sourceIndex,
          })
        )
      },
      handleDrop(event, targetGroup) {
        const data = JSON.parse(event.dataTransfer.getData('application/json'))
        const { item, sourceGroup, sourceIndex } = data

        // 如果是同一组内拖拽，忽略
        if (sourceGroup === targetGroup) return

        // 如果目标是工作台菜单组且已有3个项目，则不允许添加
        if (
          targetGroup === 'workstation' &&
          this.workstationMenus.length >= 3
        ) {
          this.$message.warning('导航栏菜单最多只能固定三个')
          return
        }

        // 从源数组中移除
        const sourceArray =
          sourceGroup === 'workstation'
            ? this.workstationMenus
            : this.adminMenus
        sourceArray.splice(sourceIndex, 1)

        // 添加到目标数组
        const targetArray =
          targetGroup === 'workstation'
            ? this.workstationMenus
            : this.adminMenus
        targetArray.push(item)

        // 触发菜单更新事件
        this.$emit('menus-update', {
          workstationMenus: this.workstationMenus,
          adminMenus: this.adminMenus,
        })
      },
    },
    watch: {
      workstationMenus: {
        handler(newVal) {
          // 当工作台菜单发生变化时，通知父组件
          this.$emit('workstation-menus-change', newVal)
        },
        deep: true,
      },
    },
  }
</script>

<style lang="scss" scoped>
  .menu-drawer {
    background: #fff;
    height: 100%;
    width: 250px;
    border-radius: 5px;
    .menu-list {
      padding: 10px 0;

      .menu-group {
        .menu-item {
          display: flex;
          align-items: center;
          padding: 12px 0px;
          cursor: pointer;
          color: #333;
          &:hover {
            background: #f5f7fa;
          }

          &.active {
            background: #ecf5ff;
            color: #409eff;
          }

          .menu-icon {
            width: 24px;
            margin-right: 8px;
            text-align: center;

            i {
              font-size: 18px;
            }
          }

          .menu-title {
            flex: 1;
            font-size: 12px;
          }

          .menu-actions {
            display: flex;
            gap: 8px;

            i {
              font-size: 16px;
              color: #909399;

              &:hover {
                color: #409eff;
              }
            }
          }
        }
      }

      .divider {
        // margin: 10px 0;
        // padding: 0 20px;
        height: 32px;
        line-height: 32px;
        color: #909399;
        font-size: 12px;
        text-align: center;
        position: relative;

        &::before,
        &::after {
          content: '';
          position: absolute;
          top: 50%;
          width: 80px;
          height: 1px;
          border-top: 1px dashed #dcdfe6;
        }

        &::before {
          left: 0px;
        }

        &::after {
          right: 0px;
        }
      }

      .menu-group:last-child {
        max-height: calc(250px);
        overflow-y: auto;

        &::-webkit-scrollbar {
          width: 6px;
        }

        &::-webkit-scrollbar-thumb {
          background: #c0c4cc;
          border-radius: 3px;
        }

        &::-webkit-scrollbar-track {
          background: #f5f7fa;
        }
      }
    }
  }
</style>
