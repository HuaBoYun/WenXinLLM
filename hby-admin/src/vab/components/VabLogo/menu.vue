<template>
  <div class="recent-menu">
    <div class="menu-header">
      <span class="menu-title">最近访问</span>
      <el-button type="text" class="clear-btn" @click="clearHistory">
        清空
      </el-button>
    </div>

    <div class="menu-list">
      <div
        v-for="(item, index) in menuItems"
        :key="index"
        class="menu-item"
        @click="handleClick(item)"
      >
        <div class="item-left">
          <i :class="item.icon" :style="{ color: item.iconColor }"></i>
          <span class="item-title">{{ item.title }}</span>
        </div>
        <div class="item-right">
          <i v-if="item.hasLink" class="el-icon-link"></i>
          <i v-if="item.isFavorite" class="el-icon-star-on"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'RecentMenu',
    data() {
      return {
        menuItems: [
          {
            title: '期初归溯调整凭证制单',
            icon: 'el-icon-document-checked',
            iconColor: '#409EFF',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: '对账调整凭证制单',
            icon: 'el-icon-document',
            iconColor: '#FF9900',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: '收入确认单',
            icon: 'el-icon-notebook-2',
            iconColor: '#409EFF',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: '调整数据中心',
            icon: 'el-icon-data-analysis',
            iconColor: '#FF9900',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: '调整执行',
            icon: 'el-icon-cloudy',
            iconColor: '#FF9900',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: '特转收款',
            icon: 'el-icon-document',
            iconColor: '#FF9900',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: '局部调整',
            icon: 'el-icon-document',
            iconColor: '#FF9900',
            hasLink: true,
            isFavorite: true,
          },
          {
            title: '账簿类型',
            icon: 'el-icon-collection',
            iconColor: '#8B00FF',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: '非银行金融机构-集团',
            icon: 'el-icon-download',
            iconColor: '#409EFF',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: '非银行金融机构-全局',
            icon: 'el-icon-download',
            iconColor: '#409EFF',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: '分摊规则',
            icon: 'el-icon-set-up',
            iconColor: '#FF9900',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: '预置规则库',
            icon: 'el-icon-search',
            iconColor: '#409EFF',
            hasLink: false,
            isFavorite: false,
          },
          {
            title: 'GB/T 24589.1-2010',
            icon: 'el-icon-set-up',
            iconColor: '#FF9900',
            hasLink: false,
            isFavorite: false,
          },
        ],
      }
    },
    methods: {
      clearHistory() {
        this.menuItems = []
        this.$emit('clear')
      },
      handleClick(item) {
        this.$emit('select', item)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .recent-menu {
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    width: 220px;

    .menu-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0px 15px;
      border-bottom: 1px solid #ebeef5;
      height: 40px;

      .clear-btn {
        color: #409eff;
        padding: 0;
      }

      .menu-title {
        font-size: 14px;
        font-weight: 400;
        color: #303133;
      }
    }

    .menu-list {
      max-height: 400px;
      overflow-y: auto;

      .menu-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0px 15px;
        cursor: pointer;
        height: 40px;
        &:hover {
          background-color: #f5f7fa;
        }

        .item-left {
          display: flex;
          align-items: center;
          gap: 8px;

          i {
            font-size: 16px;
          }

          .item-title {
            color: #303133;
            font-size: 12px;
          }
        }

        .item-right {
          display: flex;
          gap: 5px;

          i {
            font-size: 14px;

            &.el-icon-link {
              color: #909399;
            }

            &.el-icon-star-on {
              color: #f7ba2a;
            }
          }
        }
      }
    }
  }
</style>
