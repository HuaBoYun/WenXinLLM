<template>
  <span v-if="theme.showSearch">
    <div v-if="popoverVisible" class="overlay" @click="closePopover"></div>
    <el-popover
      v-model="popoverVisible"
      placement="bottom"
      width="300"
      :trigger="'manual'"
      popper-class="custom-popover"
      :append-to-body="false"
    >
      <div class="content">
        <el-input
          placeholder="输入关键字进行过滤"
          v-model="filterText"
          v-if="theme.layout !== 'custom' && theme.layout !== 'tradition'"
        ></el-input>
        <el-tree
          class="filter-tree"
          :data="data"
          :props="defaultProps"
          default-expand-all
          :filter-node-method="filterNode"
          ref="tree"
          @node-click="handleClick"
          :expand-on-click-node="false"
          node-key="id"
        >
          <template #default="{ node, data }">
            <span class="custom-tree-node">
              <vab-icon
                :icon="data.icon"
                class="icon-style"
                :is-custom-svg="true"
              />
              <!-- <i :class="getNodeIcon(data)"></i> -->
              <span>{{ node.label }}</span>
            </span>
          </template>
        </el-tree>
      </div>
      <template #reference>
        <vab-icon
          icon="search-line"
          @click="openPopover"
          v-if="theme.layout !== 'custom' && theme.layout !== 'tradition'"
        />
        <div class="search-container" v-else>
          <el-input
            v-model="filterText"
            placeholder="搜索"
            class="custom-search-input"
            @focus="handleFocus"
          >
            <template #prefix>
              <i class="el-icon-search search-icon"></i>
            </template>
          </el-input>
        </div>
      </template>
    </el-popover>
  </span>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: 'VabSearch',

    data() {
      return {
        popoverVisible: false,
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        queryForm: {
          searchWord: '',
        },
        model: '',
        filterText: '',
        menu: [],
        activeModule: [],
        data: [],
      }
    },
    ...mapGetters({
      theme: 'settings/theme',
    }),
    //监听搜索项变化
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val)
      },
    },
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
      }),
    },
    created() {
      this.$nextTick(() => {
        if (this.theme.showSearch) this.loadAll()
      })
    },
    methods: {
      openPopover() {
        this.filterText = ''
        this.popoverVisible = !this.popoverVisible
      },
      //请求所有的菜单数据
      async loadAll() {
        this.model = localStorage.getItem('model')
        this.menu = JSON.parse(localStorage.getItem('renderMenu')) || []
        // for (let i = 0; i < this.menu.length; i++) {
        //   if (this.menu[i].uniqueIdentification === this.model) {
        //     this.activeModule = this.menu[i]
        //   }
        // }

        console.log(this.activeModule, 'this.activeModule')
        //初始化最右边的数据
        let info = this.menu
        // let info = this.activeModule.menu
        const renderData = this.mapTreeData(info, '')
        this.data = renderData
      },
      //搜索项过滤方法
      filterNode(value, data) {
        if (!value) return true
        return data.label.indexOf(value) !== -1
      },
      //递归菜单接口
      mapTreeData(data, parent) {
        return (
          data &&
          data.map((item, index) => {
            return {
              id: item.id,
              label: item.name,
              path: item.path,
              perms: item.perms,
              ppath: parent || '',
              icon: item.icon,
              children:
                item.children == null || item.children.length <= 0
                  ? []
                  : this.mapTreeData(item.children, item.path),
            }
          })
        )
      },
      //点击树节点
      handleClick(item) {
        //处理点击一级菜单的情况,直接跳转该一级菜单的第一个页面
        if (!item.ppath) {
          let firstMenu = item.children[0]
          let path = '/' + firstMenu.ppath + '/' + firstMenu.path
          this.$router.push(path)
          return
        }
        let path = '/' + item.ppath + '/' + item.path
        this.$router.push(path)
        return
        if (this.model !== this.activeModule.uniqueIdentification) {
          localStorage.setItem('model', this.activeModule.uniqueIdentification)
          localStorage.setItem('modelname', this.activeModule.projectName)
          window.location.href = '/'
          localStorage.setItem('path', path)
        } else {
          console.log('未切换模块')
          this.$router.push(path)
        }
      },
      handleFocus() {
        this.filterText = ''
        this.popoverVisible = true
      },
      handleBlur() {
        setTimeout(() => {
          this.popoverVisible = false
        }, 200)
      },
      onPopoverShow() {
        this.filterText = ''
      },
      onPopoverHide() {
        // 可以在这里添加关闭弹出框时的逻辑
      },
      closePopover() {
        this.popoverVisible = false
      },
      getNodeIcon(data) {
        // 如果节点有子节点，显示文件夹图标
        if (data.children && data.children.length > 0) {
          return 'el-icon-folder'
        }
        // 如果是叶子节点，显示文件图标
        return 'el-icon-document'
      },
    },
  }
</script>
<style lang="scss" scoped>
  .content {
    height: 420px;
    display: flex;
    flex-direction: column;

    .el-input {
      margin-bottom: 10px;
    }
  }
  .filter-tree {
    height: 360px;
    overflow-y: auto;
    flex: 1;
  }
  .search-container {
    // padding: 10px;
    display: flex;
    align-items: center;

    .custom-search-input {
      width: 160px;

      :deep(.el-input__inner) {
        background: #656c78;
        border: none;
        border-radius: 4px;
        height: 30px;
        padding-left: 35px;
        color: #fff;
        transition: all 0.3s;

        &::placeholder {
          color: rgba(255, 255, 255, 0.7);
        }
      }

      :deep(.el-input__prefix) {
        left: 10px;
        display: flex;
        align-items: center;
        .search-icon {
          font-size: 16px;
          color: rgba(255, 255, 255, 0.7);
        }
      }
    }
  }
  ::v-deep {
    .custom-popover {
      margin-top: 17px;
    }

    .el-dialog {
      position: fixed !important;
      margin: 0 !important;
      top: 70px !important;
      right: 8% !important;

      .el-dialog__header {
        display: none;
        border: 0 !important;
      }
      .el-card {
        margin-bottom: 0px;
      }

      .el-dialog__body {
        padding: 0;
        border: 0 !important;
      }
      .el-input {
        margin-bottom: 20px !important;
      }

      .el-form-item__content {
        position: relative;

        i {
          position: absolute;
          top: 14px;
          left: $base-margin/1.5;
        }

        .el-autocomplete {
          width: 100%;

          .el-input__inner {
            width: 100%;
            height: 60px;
            padding-left: $base-padding * 2.5;
            border: 0 !important;
          }
        }
      }
    }
  }

  :deep(.search-popover) {
    padding: 0;

    .el-card {
      border: none;
      box-shadow: none;
    }
  }
  .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: transparent;
    z-index: 2000;
  }
  .custom-tree-node {
    display: flex;
    align-items: center;
    // justify-content: center;
    .vab-icon {
      margin: 0 8px 0 0;
    }
    i {
      margin-right: 8px;
      font-size: 16px;
      color: #909399;
    }
    .icon-style {
      margin-right: 8px;
      font-size: 16px;
      color: #909399;
    }
  }
  .vab-theme-white {
    .search-container {
      // padding: 10px;
      display: flex;
      align-items: center;

      .custom-search-input {
        width: 160px;

        :deep(.el-input__inner) {
          background: transparent;
          border: 1px solid rgba(179, 179, 179, 0.7);
          border-radius: 4px;
          height: 30px;
          padding-left: 35px;
          color: #fff;
          transition: all 0.3s;
          caret-color: rgba(59, 59, 59, 0.7); // 添加这行来设置光标颜色为白色
          &::placeholder {
            color: rgba(179, 179, 179, 0.7);
          }
        }

        :deep(.el-input__prefix) {
          left: 10px;
          display: flex;
          align-items: center;
          .search-icon {
            font-size: 16px;
            color: rgba(179, 179, 179, 0.7) !important;
          }
        }
      }
    }
  }
</style>
